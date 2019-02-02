package com.edwardvanraak.materialbarcodescannerexample;

import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crashlytics.android.Crashlytics;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner.OnResultListener;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.edwardvanraak.materialbarcodescannerexample.interfaces.AlertDialogSingleButtonClickListener;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflinePriceItem;
import com.edwardvanraak.materialbarcodescannerexample.model.offline.OfflineProductItem;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ItemDetails;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ListItemData;
import com.edwardvanraak.materialbarcodescannerexample.network.RetroApiClient;
import com.edwardvanraak.materialbarcodescannerexample.network.RetroInterface;
import com.edwardvanraak.materialbarcodescannerexample.storage.CustomPreferences;
import com.edwardvanraak.materialbarcodescannerexample.storage.DbManager;
import com.edwardvanraak.materialbarcodescannerexample.utils.ApiHelper;
import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;
import com.edwardvanraak.materialbarcodescannerexample.utils.SoundManager;
import com.edwardvanraak.materialbarcodescannerexample.utils.StorageUtil;
import com.edwardvanraak.materialbarcodescannerexample.views.DetailFragment;
import com.edwardvanraak.materialbarcodescannerexample.views.DownloadFragment;
import com.edwardvanraak.materialbarcodescannerexample.views.MultiResultFragment;
import com.edwardvanraak.materialbarcodescannerexample.views.MyAccountFragment;
import com.edwardvanraak.materialbarcodescannerexample.views.MyCostFragment;
import com.edwardvanraak.materialbarcodescannerexample.views.SettingsFragment;
import com.edwardvanraak.materialbarcodescannerexample.views.WebviewActivity;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.edwardvanraak.materialbarcodescannerexample.model.results.ListItemData.parseListItemData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnGestureListener, AlertDialogSingleButtonClickListener {
    public static final String BARCODE_KEY = "BARCODE";
    public static final String CLICK_RECEIVER_ACTION = "com.barcode.result.click.action";
    public static final String KEY_ITEM_IDENTIFIER = "KEY_ITEM_IDENTIFIER";
    private static final String INVALID_TOKEN_MSG = "The token is not valid";
    private static final String BOOK_BAR_CODE_PREFIX = "978";
    private static final int PERMISSION_REQ_CODE = 100;
    private static final int SCAN_REQ_CODE = 150;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.searchInputEditor)
    EditText searchInputEditor;
    @BindView(R.id.progressBar)
    ProgressBar searchProgressBar;
    @BindView(R.id.contentLayout)
    FrameLayout contentLayout;


    private Barcode barcodeResult;
    private boolean isSearchInProgress = false;
    private Fragment fragment = null;
    private MultiItemClickReceiver multiItemClickReceiver;
    private GestureDetector gestureDetector;
    private  ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        loadInitialScreen();

        if (!hasPermissions()) {
            GlobalUtil.showAlertMessageWithSingleButton(this, getString(R.string.alert_permission_title), getString(R.string.alert_permission_msg), this);
        } else {
            initializeDB();
        }
        multiItemClickReceiver = new MultiItemClickReceiver();
        SoundManager.initSounds(this);
        gestureDetector = new GestureDetector(this, this);
       // contentLayout.setOnTouchListener(contentTouchListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CLICK_RECEIVER_ACTION);
        registerReceiver(multiItemClickReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(multiItemClickReceiver);
    }


    private void loadInitialScreen() {
        if (CustomPreferences.isUserLoggedIn(this)) {
            GlobalUtil.auth_token_value = CustomPreferences.getAuthToken(this);
        } else {
            switchToAccountScreen();
        }
    }

    private boolean hasPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private void initViews() {
        configureToolBar();
        configureDrawer();
        addDrawerHeaderView();
    }

    private void configureToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addDrawerHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.navigation_drawer_header, null);
        navigationView.addHeaderView(headerView);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.scannerActionIcon)
    public void scannerIconClicked() {
        startScan();
    }

    @OnEditorAction(R.id.searchInputEditor)
    public boolean onSearchEditAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            initFreeSearch();
            return true;
        }
        return false;
    }

    @OnClick(R.id.searchIconView)
    public void searchIconClicked() {
        initFreeSearch();
    }

    private void initFreeSearch() {
        if (CustomPreferences.isUserLoggedIn(this)) {
            String searchText = searchInputEditor.getText().toString();
            if (!TextUtils.isEmpty(searchText)) {
                performFreeTextSearch(searchText);
            }
        } else {
            Toast.makeText(this, "Please first login in my account screen", Toast.LENGTH_SHORT).show();
        }
    }


    private void startScan() {
        if (!CustomPreferences.isUserLoggedIn(this)) {
            Toast.makeText(this, "Please first login in my account screen", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isSearchInProgress) {
            MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                    .withActivity(MainActivity.this)
                    .withEnableAutoFocus(true)
                    .withBleepEnabled(true)
                    .withBackfacingCamera()
                    .withCenterTracker()
                    .withText("Scanning...")
                    .withResultListener(barCodeResultListener)
                    .build();

            materialBarcodeScanner.startScan();
        }
    }

    private OnResultListener barCodeResultListener = new OnResultListener() {
        @Override
        public void onResult(Barcode barcode) {
            barcodeResult = barcode;
            searchInputEditor.setText(barcodeResult.displayValue);
            performSearchForScannedBarCode(barcodeResult.displayValue);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan();
                return;
            }
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error")
                    .setMessage(R.string.no_camera_permission)
                    .setPositiveButton(android.R.string.ok, listener)
                    .show();
        } else if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                initializeDB();
                return;
            }
        }


    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(OfflineProductItem.class);
        configurationBuilder.addModelClasses(OfflinePriceItem.class);
        configurationBuilder.setDatabaseName(StorageUtil.getDatabaseExternalPath());
        ActiveAndroid.initialize(configurationBuilder.create());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (CustomPreferences.isUserLoggedIn(this)) {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_account:
                    switchToAccountScreen();
                    break;
                case R.id.nav_cost:
                    switchToMyCostScreen();
                    break;
                case R.id.nav_download:
                    switchToDownloadScreen();
                    break;
                case R.id.nav_help:
                    openHelpUrlInWebView(getString(R.string.help_url));
                    break;
                case R.id.nav_settings:
                    switchToSettingsScreen();
                    break;
            }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerToggle.onOptionsItemSelected(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void switchToScanScreen() {
        startScan();
    }

    private void switchToAccountScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new MyAccountFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }

    private void switchToSettingsScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new SettingsFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }

    private void switchToDownloadScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new DownloadFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }

    private void removeFragment() {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            fragment = null;
        }
    }


    private void switchToMyCostScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new MyCostFragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSingleOKPressListener() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
    }

    private void showProgressBar() {
        searchProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        searchProgressBar.setVisibility(View.INVISIBLE);
    }

    private void displayMessage(String message) {
        messageTextView.setText(message);
        messageTextView.setVisibility(View.VISIBLE);
        removeFragment();
    }

    private void hideMessage() {
        messageTextView.setVisibility(View.INVISIBLE);
    }

    private void startSearch() {
        isSearchInProgress = true;
        hideMessage();
        if (fragment != null ) {
            removeFragment();
        }
    }

    private void stopSearch() {
        isSearchInProgress = false;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInputEditor.getWindowToken(), 0);

    }

    private void endSearch() {
        hideProgressBar();
        hideKeyboard();
        stopSearch();
    }

    private void displayMultipleResultsFragment(ArrayList<ListItemData> listItemData) {
        endSearch();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new MultiResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MultiResultFragment.KEY_ITEMS_LIST, listItemData);
        fragment.setArguments(bundle);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();

    }

    private void displayDetailFragment(final ItemDetails itemDetails) {
        endSearch();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailFragment.KEY_ITEM, itemDetails);
        fragment.setArguments(bundle);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, fragment).addToBackStack(null);
        fragmentTransaction.commit();

    }


    private ArrayList<ListItemData> parseFreeSearchResults(String jsonResponse) {
        ArrayList<ListItemData> dataItemsList = new ArrayList<>();
        try {
            Object json = new JSONTokener(jsonResponse).nextValue();
            if (json instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) json;
                for (int count = 0; count < jsonArray.length(); count++) {
                    ListItemData listItemData = parseListItemData(jsonArray.getJSONObject(count));
                    dataItemsList.add(listItemData);
                }
            } else {
                JSONObject jsonObject = (JSONObject) json;
                ListItemData listItemData = parseListItemData(jsonObject);
                dataItemsList.add(listItemData);
            }


        } catch (Exception exception) {
            GlobalUtil.printLog("ParseException", "" + exception);
        }
        return dataItemsList;
    }

    private boolean isJsonArray(String jsonResponse) {
        try {
            Object json = new JSONTokener(jsonResponse).nextValue();
            return json instanceof JSONArray;
        } catch (Exception e) {

        }
        return false;
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
        hideProgressBar();
        stopSearch();
        if (isTokenExpired(error)) {
            GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), "Your token has expired. Please login again");
            switchToAccountScreen();
        } else {
            displayMessage(error);
        }

    }


    private void performFreeTextSearch(String text) {
        boolean isOfflineSettingsChecked = CustomPreferences.getOfflineDbSettingsFlag(this);
        if (!isSearchInProgress) {
            if (!isOfflineSettingsChecked) {
                if (!GlobalUtil.isInternetConnected(this)) {
                    GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.no_connection_message));
                    return;
                } else {
                    startSearch();
                    showProgressBar();
                    makeFreeTextSearchApiCall(text);
                }
            } else {
                if (!StorageUtil.doesLocalDbExists()) {
                    GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), "Local db file has been removed. So download it before you can perform offline search");
                    return;
                }
                startSearch();
                showProgressBar();
                makeFreeTextOfflineSearch(text);
            }


        }

    }

    private void performSearchForScannedBarCode(String barCodeValue) {
        boolean isOfflineSettingsChecked = CustomPreferences.getOfflineDbSettingsFlag(this);
        if (!isSearchInProgress) {
            if (!isOfflineSettingsChecked) {
                if (!GlobalUtil.isInternetConnected(this)) {
                    GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), getString(R.string.no_connection_message));
                    return;
                } else {
                    startSearch();
                    showProgressBar();
                    String url = getSearchUrl(barCodeValue);
                    makeScannedBarcodeSearchApiCall(url);
                }
            } else {
                if (!StorageUtil.doesLocalDbExists()) {
                    GlobalUtil.showMessageAlertWithOkButton(this, getString(R.string.alert_message_title), "Local db file has been removed. So download it before you can perform offline search");
                    return;
                }
                startSearch();
                showProgressBar();
                makeScannedBarcodeOfflineSearch(barCodeValue);
            }


        }
    }

    private String getSearchUrl(String displayValue) {
        String url = "";
        if (displayValue.startsWith(BOOK_BAR_CODE_PREFIX)) {
            url = RetroApiClient.ISBN_SEARCH_URL + displayValue;
        } else if (displayValue.length() == 10) {
            url = RetroApiClient.ASIN_SEARCH_URL + displayValue;
        } else {
            url = RetroApiClient.UPC_SEARCH_URL + displayValue;
        }
        return url;
    }

    private void makeScannedBarcodeOfflineSearch(String barCodeValue) {
        OfflineProductItem offlineProductItem = null;
        if (barCodeValue.startsWith(BOOK_BAR_CODE_PREFIX)) {
            offlineProductItem = DbManager.getProductsByISBN(barCodeValue);
        } else if (barCodeValue.length() == 10) {
            offlineProductItem = DbManager.getProductsByAsin(barCodeValue);
        }

        if (offlineProductItem == null) {
            displayMessage("No item found");
            endSearch();
            return;
        }

        List<OfflinePriceItem> offlinePriceItems = DbManager.getPriceItemsByProductId(offlineProductItem.getProductId());
        ItemDetails itemDetails = ItemDetails.getItemDetailsFromOfflineData(offlineProductItem, offlinePriceItems);
        if (itemDetails != null) {
            displayDetailFragment(itemDetails);
            return;
        }

        endSearch();
    }


    private void makeFreeTextOfflineSearch(String text) {
        List<OfflineProductItem> offlineProductItems = DbManager.getProductsByText(text);
        if (offlineProductItems == null || offlineProductItems.size() == 0) {
            displayMessage("No item found");
            endSearch();
            return;
        }
        if (offlineProductItems.size() > 1) {
            ArrayList<ListItemData> listItems = getListItemFromOfflineProducts(offlineProductItems);
            displayMultipleResultsFragment(listItems);
        } else {
            OfflineProductItem productItem = offlineProductItems.get(0);
            List<OfflinePriceItem> offlinePriceItems = DbManager.getPriceItemsByProductId(productItem.getProductId());
            ItemDetails itemDetails = ItemDetails.getItemDetailsFromOfflineData(productItem, offlinePriceItems);
            if (itemDetails != null) {
                displayDetailFragment(itemDetails);
                return;
            }

        }
        endSearch();

    }


    private ArrayList<ListItemData> getListItemFromOfflineProducts(List<OfflineProductItem> offlineProductItems) {
        ArrayList<ListItemData> listItems = new ArrayList<>();
        for (OfflineProductItem offlineProductItem : offlineProductItems) {
            listItems.add(offlineProductItem.getListItemReplica());
        }
        return listItems;
    }


    private void makeFreeTextSearchApiCall(String text) {
        String token = CustomPreferences.getAuthToken(this);
        String url = RetroApiClient.FREE_SEARCH_URL + text;

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<ResponseBody> call = apiService.performFreeTextSearch(token, url);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String errorMessage = getString(R.string.unable_to_fetch_error);
                if (response != null && response.body() != null) {
                    try {
                        String jsonRawResponse = response.body().string();
                        String error = getError(jsonRawResponse);
                        if (error != null) {
                            handleError(error);
                            return;
                        }
                        if (isJsonArray(jsonRawResponse)) {
                            ArrayList<ListItemData> results = parseFreeSearchResults(jsonRawResponse);
                            if (results != null && results.size() > 0) {
                                displayMultipleResultsFragment(results);
                            }
                            return;
                        } else {
                            ItemDetails itemDetails = ItemDetails.parseItemDetails(jsonRawResponse);
                            if (itemDetails != null) {
                                displayDetailFragment(itemDetails);
                                return;
                            }
                        }


                    } catch (Exception ex) {
                        GlobalUtil.printLog("ParseException", "" + ex);
                        errorMessage = "Malformed data received";
                    }
                }

                displayMessage(errorMessage);
                endSearch();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                displayMessage(getString(R.string.unable_to_fetch_error));
                endSearch();
            }
        });
    }


    private void makeScannedBarcodeSearchApiCall(String url) {
        String token = CustomPreferences.getAuthToken(this);
        String cost = "" + CustomPreferences.getMyCostValue(this);
        String inboundShipping = "" + CustomPreferences.getInboundShipping(this);

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<ResponseBody> call = apiService.performScanSearch(token, url, cost, inboundShipping);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String errorMessage = getString(R.string.unable_to_fetch_error);
                if (response != null && response.body() != null) {
                    try {
                        String jsonRawResponse = response.body().string();
                        String error = getError(jsonRawResponse);
                        if (error != null) {
                            handleError(error);
                            return;
                        }
                        if (isJsonArray(jsonRawResponse)) {
                            ArrayList<ListItemData> results = parseFreeSearchResults(jsonRawResponse);
                            if (results != null && results.size() > 0) {
                                displayMultipleResultsFragment(results);
                            }
                            return;
                        } else {
                            ItemDetails itemDetails = ItemDetails.parseItemDetails(jsonRawResponse);
                            if (itemDetails != null) {
                                displayDetailFragment(itemDetails);
                                return;
                            }
                        }

                    } catch (Exception ex) {
                        GlobalUtil.printLog("ParseException", "" + ex);
                        errorMessage = "Malformed data received";
                    }
                }

                displayMessage(errorMessage);
                endSearch();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                displayMessage(getString(R.string.unable_to_fetch_error));
                endSearch();
            }
        });
    }


    @Override
    public boolean onDown(MotionEvent e) {
        GlobalUtil.printLog("GestureDebug", "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        GlobalUtil.printLog("GestureDebug", "onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        GlobalUtil.printLog("GestureDebug", "OnFling");
        float horizontalDiff = Math.abs(e1.getX() - e2.getX());
        float verticalDiff =  Math.abs(e1.getY() - e2.getY());
        if (horizontalDiff > 100 && horizontalDiff>verticalDiff) {
            switchToScanScreen();
        }
        return true;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        View v = getCurrentFocus();
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }

        return gestureDetector.onTouchEvent(event);
    }

    private class MultiItemClickReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String identifier = intent.getStringExtra(KEY_ITEM_IDENTIFIER);
            performSearchForScannedBarCode(identifier);
        }
    }

    private void openHelpUrlInWebView(String url) {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(WebviewActivity.KEY_WEBVIEW_URL, url);
        startActivity(intent);
    }

}
