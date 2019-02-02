package com.brainpixel.valetapp.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.model.help.HelpContensResponse;
import com.brainpixel.valetapp.model.help.HelpDataItem;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.brainpixel.valetapp.views.custom.DividerItemDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 13/05/2017.
 */

public class HelpContentsActivity extends AppCompatActivity {
    @BindView(R.id.helpContentsRecyclerView)
    RecyclerView helpContentsRecyclerView;
    @BindView(R.id.helpDetailWebView)
    WebView helpItemDetailsWebview;
    @BindView(R.id.errorTextView)
    TextView errorTextView;
    @BindView(R.id.loadingBar)
    ProgressBar horizontalLoadingBar;
    private HelpContentsListAdapter helpContentsListAdapter = null;
    private LinkedList<HelpDataItem> helpDataItems=new LinkedList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.fragment_help_contents, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);
        setupViews();
        fetchHelpContents("null");
    }

    private void setupViews() {
        helpItemDetailsWebview.getSettings().setJavaScriptEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        helpContentsRecyclerView.setLayoutManager(mLayoutManager);
        helpContentsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @OnClick(R.id.backClickArea)
    public void backArrowClicked() {
        handleBack();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        if (helpDataItems.size() > 0) {
            HelpDataItem latestItem = helpDataItems.get(helpDataItems.size()-1);
            helpDataItems.remove(latestItem);
            fetchHelpContents("" + latestItem.getHParentId());
        } else {
            finish();
        }
    }

    private void showHorizontalBar() {
        horizontalLoadingBar.setVisibility(View.VISIBLE);
    }

    private void hideHorizontalBar() {
        horizontalLoadingBar.setVisibility(View.GONE);
    }

    private void showErrorView() {
        errorTextView.setVisibility(View.VISIBLE);
        helpContentsRecyclerView.setVisibility(View.GONE);
        helpItemDetailsWebview.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        helpContentsRecyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        helpItemDetailsWebview.setVisibility(View.GONE);
    }

    private void showDetailWebView() {
        helpItemDetailsWebview.setVisibility(View.VISIBLE);
        helpContentsRecyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
    }

    private void onApiCallFailed() {
        hideHorizontalBar();
        showErrorView();
    }

    private void showHelpItemsOnList(List<HelpDataItem> helpDataItems) {
        if (helpContentsListAdapter == null) {
            helpContentsListAdapter = new HelpContentsListAdapter(helpDataItems);
            helpContentsRecyclerView.setAdapter(helpContentsListAdapter);
        } else {
            helpContentsListAdapter.refreshHelpResults(helpDataItems);
        }
        showRecyclerView();
    }

    private void showHelpItemInWebView(String webContent) {
        helpItemDetailsWebview.loadData(webContent, "text/html", "UTF-8");
        showDetailWebView();
    }

    private void handleResults(HelpContensResponse helpContensResponse) {
        if (helpContensResponse.getData().size() == 1) {
            showHelpItemInWebView(helpContensResponse.getData().get(0).getHDescription());
        } else {
            showHelpItemsOnList(helpContensResponse.getData());
        }
    }

    private void fetchHelpContents(String parentId) {
        showHorizontalBar();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<HelpContensResponse> call = apiService.getHelpContents(parentId);
        ApiHelper.enqueueWithRetry(this, call, new retrofit2.Callback<HelpContensResponse>() {
            @Override
            public void onResponse(Call<HelpContensResponse> call, Response<HelpContensResponse> response) {
                hideHorizontalBar();
                if (response == null || response.body() == null) {
                    showErrorView();
                    return;
                }
                HelpContensResponse helpContensResponse = response.body();
                if (!helpContensResponse.getSuccess()) {
                    showErrorView();
                    return;
                }
                handleResults(helpContensResponse);
            }

            @Override
            public void onFailure(Call<HelpContensResponse> call, Throwable t) {
                showErrorView();
            }
        });
    }


    private class HelpContentsListAdapter extends RecyclerView.Adapter<HelpContentsListAdapter.HelpListRowHolder> {
        List<HelpDataItem> helpDataItemList;

        public HelpContentsListAdapter(List<HelpDataItem> list) {
            helpDataItemList = list;
        }

        public void refreshHelpResults(List<HelpDataItem> list) {
            helpDataItemList = list;
            notifyDataSetChanged();
        }

        @Override
        public HelpListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mineView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_help_item, parent, false);
            new ScalingUtility(HelpContentsActivity.this).scaleRootView(mineView);
            return new HelpListRowHolder(mineView);
        }

        @Override
        public void onBindViewHolder(HelpListRowHolder holder, int position) {
            HelpDataItem helpDataItem = helpDataItemList.get(position);
            holder.helpItemTextView.setText(helpDataItem.getHTitle());
        }

        @Override
        public int getItemCount() {
            return helpDataItemList.size();
        }

        public class HelpListRowHolder extends RecyclerView.ViewHolder implements OnClickListener {
            TextView helpItemTextView;

            public HelpListRowHolder(View itemView) {
                super(itemView);
                helpItemTextView = (TextView) itemView.findViewById(R.id.itemTextView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position < helpDataItemList.size()) {
                    HelpDataItem helpDataItem = helpDataItemList.get(position);
                    helpDataItems.add(helpDataItem);
                    fetchHelpContents("" + helpDataItem.getHId());
                }
            }
        }
    }


}
