package com.edwardvanraak.materialbarcodescannerexample.views;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflinePriceItem;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflineProductItem;
import com.edwardvanraak.materialbarcodescannerexample.network.RetroApiClient;
import com.edwardvanraak.materialbarcodescannerexample.network.RetroInterface;
import com.edwardvanraak.materialbarcodescannerexample.storage.CustomPreferences;
import com.edwardvanraak.materialbarcodescannerexample.storage.DbManager;
import com.edwardvanraak.materialbarcodescannerexample.utils.ApiHelper;
import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;
import com.edwardvanraak.materialbarcodescannerexample.utils.StorageUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Modifier;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 16/04/2017.
 */

public class DownloadFragment extends Fragment {
    private static final String INVALID_TOKEN_MSG = "The token is not valid";
    private static final int pageSize = 100;
    @BindView(R.id.contentLayout)
    RelativeLayout contentLayout;
    @BindView(R.id.errorView)
    TextView errorMessageView;
    @BindView(R.id.actionButton)
    Button actionButton;
    @BindView(R.id.cancelButton)
    Button cancelButton;
    @BindView(R.id.downloadProgressBar)
    ProgressBar downloadProgressbar;
    private long mLastClickTime = 0;
    private boolean isDownloadInProgress = false;
    private int currentPage = -1;
    private long totalCount = 0;
    private float currentProgress = 0;
    private float progressIncrement = 0;
    private Gson gson = null;
    private String currentProductTimeStamp;
    private String currentPricesTimeStamp;
    private String latestProductTimeStamp;
    private String latestPricesTimeStamp;

    private boolean isCancelled = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.download_screen_layout, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        gson = createGsonObject();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (CustomPreferences.isOfflineDbEnabled(getActivity()).equalsIgnoreCase("0")) {
            contentLayout.setVisibility(View.GONE);
            errorMessageView.setText("You don't have access to download offline database");
            errorMessageView.setVisibility(View.VISIBLE);
        } else {
            contentLayout.setVisibility(View.VISIBLE);
            errorMessageView.setVisibility(View.GONE);

            if (!checkPermissionsState()) {
                actionButton.setClickable(false);
                GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_permission_title), "You can't download database as you have denied required permissions.");
            } else if (!StorageUtil.isExternalStorageWritable()) {
                actionButton.setClickable(false);
                GlobalUtil.showMessageAlertWithOkButton(getActivity(), "Storage Error", "You don't have external storage available needed to save database");
            } else {
                actionButton.setClickable(true);
            }
        }

    }

    private boolean checkPermissionsState() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(getActivity(), permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    (ActivityCompat.checkSelfPermission(getActivity(), permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @OnClick(R.id.actionButton)
    public void downloadButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_error_title), getString(R.string.no_connection_message));
            return;
        }
        if (!isDownloadInProgress) {
            isCancelled = false;
            isDownloadInProgress = true;
            currentProgress = 0;
            currentProductTimeStamp = CustomPreferences.getProductTimestamp(getActivity());
            currentPricesTimeStamp = CustomPreferences.getPricesTimestamp(getActivity());
            initDownloadProcess();
        }
    }

    @OnClick(R.id.cancelButton)
    public void onCancelClick() {
        cancelDownloadProcess();
    }


    private void showDownloadProcessStopped() {
        actionButton.setText("Check for Update");
        isDownloadInProgress = false;
        GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_error_title), "Download process stopped. Check your internet");
    }

    private void initDownloadProcess() {
        actionButton.setText("Download In Progress");
        cancelButton.setVisibility(View.VISIBLE);
        downloadProductCount(GlobalUtil.auth_token_value, currentProductTimeStamp);
    }

    private void startDownloadProducts() {
        if (totalCount > 0) {
            progressIncrement = (100.0f * pageSize) / totalCount;
            currentPage = -1;
            latestProductTimeStamp = null;
            latestPricesTimeStamp = null;
            downloadProductNextpage();
        } else {
            completeDownloadProcess();
        }
    }

    private void startDownloadProductPrices() {
        currentPage = -1;
        downloadPricesNextPage();
    }

    private void downloadProductNextpage() {
        if (!isCancelled) {
            currentPage++;
            updateProgress();
            fetchProductsPageApiCall(GlobalUtil.auth_token_value, currentProductTimeStamp);
        }
    }

    private void downloadPricesNextPage() {
        if (!isCancelled) {
            currentPage++;
            updateProgress();
            fetchProductsPricesPageApiCall(GlobalUtil.auth_token_value, currentPricesTimeStamp);
        }
    }

    private void updateProgress() {
        currentProgress += progressIncrement;
        GlobalUtil.printLog("DownloadDebug", "Progress=" + currentProgress);
        downloadProgressbar.setProgress((int) currentProgress);
    }

    private void completeDownloadProcess() {
        downloadProgressbar.setProgress(100);
        actionButton.setText("Download Completed");
        cancelButton.setVisibility(View.GONE);
        isDownloadInProgress = false;
        CustomPreferences.saveofflineDbDownloadStatus(getActivity(), true);
    }

    private void cancelDownloadProcess() {
        isCancelled = true;
        downloadProgressbar.setProgress(0);
        isDownloadInProgress = false;
        cancelButton.setVisibility(View.GONE);
    }


    private void downloadProductCount(final String authorization, final String timeStamp) {
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<ResponseBody> call = apiService.getTotalCountOfAmazonProduct(authorization, timeStamp);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (isAdded()) {
                    if (response == null || response.body() == null) {
                        showDownloadProcessStopped();
                        return;
                    }
                    try {
                        String jsonRawResponse = response.body().string();
                        String error = getError(jsonRawResponse);
                        if (error != null) {
                            handleError(error);
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(jsonRawResponse);
                        totalCount = jsonObject.getInt("total");
                        GlobalUtil.printLog("DownloadDebug", "ProductCount=" + totalCount);
                        downloadPricesCount(authorization, currentPricesTimeStamp);
                    } catch (Exception e) {
                        GlobalUtil.printLog("DownloadDebug", "ProductCount=" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isAdded()) {
                    showDownloadProcessStopped();
                }
            }
        });
    }

    private void downloadPricesCount(String authorization, String timeStamp) {
        GlobalUtil.printLog("DownloadDebug", "downloadPricesCount TimeStamp=" + timeStamp);
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<ResponseBody> call = apiService.getTotalCountOfAmazonProductPrices(authorization, timeStamp);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (isAdded()) {
                    if (response == null || response.body() == null) {
                        showDownloadProcessStopped();
                        return;
                    }
                    try {
                        String jsonRawResponse = response.body().string();
                        String error = getError(jsonRawResponse);
                        if (error != null) {
                            handleError(error);
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(jsonRawResponse);
                        int count = jsonObject.getInt("total");
                        GlobalUtil.printLog("DownloadDebug", "PricesCount=" + count);
                        totalCount += count;
                        startDownloadProducts();
                    } catch (Exception e) {
                        GlobalUtil.printLog("DownloadDebug", "PricesCount=" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isAdded()) {
                    showDownloadProcessStopped();
                }
            }
        });
    }

    private Gson createGsonObject() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    private void fetchProductsPageApiCall(String authorization, String timeStamp) {
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<ResponseBody> call = apiService.getAmazonProductPage(authorization, timeStamp, currentPage, pageSize);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (isAdded() && !isCancelled) {
                    if (response == null || response.body() == null) {
                        GlobalUtil.printLog("DownloadDebug", "Response is Null");
                        showDownloadProcessStopped();
                        return;
                    }
                    try {
                        String jsonString = response.body().string();
                        String error = getError(jsonString);
                        if (error != null) {
                            handleError(error);
                            return;
                        }

                        JSONArray jsonArray = new JSONArray(jsonString);
                        for (int count = 0; count < jsonArray.length(); count++) {
                            String jsonObject = jsonArray.getString(count);
                            OfflineProductItem offlineProductItem = gson.fromJson(jsonObject, OfflineProductItem.class);
                            DbManager.saveProductItem(offlineProductItem);
                            latestProductTimeStamp = "" + offlineProductItem.getDate();
                        }
                        if (jsonArray.length() >= pageSize) {
                            downloadProductNextpage();
                        } else {
                            if (!TextUtils.isEmpty(latestProductTimeStamp)) {
                                CustomPreferences.saveProductTimeStamp(getActivity(), latestProductTimeStamp);
                            }
                            startDownloadProductPrices();
                        }


                    } catch (Exception e) {
                        GlobalUtil.printLog("DownloadDebug", "" + e.toString());
                        showDownloadProcessStopped();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isAdded()) {
                    GlobalUtil.printLog("DownloadDebug", "" + t.getMessage());
                    showDownloadProcessStopped();
                }
            }
        });
    }

    private void fetchProductsPricesPageApiCall(String authorization, String timeStamp) {
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<ResponseBody> call = apiService.getAmazonProductPricesPage(authorization, timeStamp, currentPage, pageSize);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (isAdded() && !isCancelled) {
                    if (response == null || response.body() == null) {
                        GlobalUtil.printLog("DownloadDebug", "Response Null");
                        showDownloadProcessStopped();
                        return;
                    }
                    try {
                        String jsonString = response.body().string();
                        String error = getError(jsonString);
                        if (error != null) {
                            handleError(error);
                            return;
                        }

                        JSONArray jsonArray = new JSONArray(jsonString);
                        for (int count = 0; count < jsonArray.length(); count++) {
                            String jsonObject = jsonArray.getString(count);
                            OfflinePriceItem offlinePriceItem = gson.fromJson(jsonObject, OfflinePriceItem.class);
                            DbManager.savePriceItem(offlinePriceItem);
                            latestPricesTimeStamp = "" + offlinePriceItem.getDate();
                        }
                        if (jsonArray.length() >= pageSize) {
                            downloadPricesNextPage();
                        } else {
                            if (!TextUtils.isEmpty(latestPricesTimeStamp)) {
                                CustomPreferences.savePricesTimeStamp(getActivity(), latestPricesTimeStamp);
                            }
                            completeDownloadProcess();
                        }

                    } catch (Exception e) {
                        GlobalUtil.printLog("DownloadDebug", "Exception=" + e.toString());
                        showDownloadProcessStopped();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isAdded()) {
                    GlobalUtil.printLog("DownloadDebug", "Exception=" + t.getMessage());
                    showDownloadProcessStopped();
                }
            }
        });
    }

    private String getError(String jsonResponse) {
        String error = null;
        try {
            Object json = new JSONTokener(jsonResponse).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) json;
                if (jsonObject.has("error")) {
                    error = jsonObject.getString("error");
                }

            }
        } catch (Exception e) {
            error = "Malformed data received ";
        }
        return error;
    }

    private boolean isTokenExpired(String error) {
        return error.contains(INVALID_TOKEN_MSG);
    }

    private void handleError(String error) {
        cancelDownloadProcess();
        if (isTokenExpired(error)) {
            GlobalUtil.showMessageAlertWithOkButton(getActivity(), getString(R.string.alert_message_title), "Your token has expired. Please login again");
        }

    }
}
