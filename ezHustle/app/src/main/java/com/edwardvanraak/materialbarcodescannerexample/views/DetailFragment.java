package com.edwardvanraak.materialbarcodescannerexample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.interfaces.OnOffersItemClickListener;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ItemDetails;
import com.edwardvanraak.materialbarcodescannerexample.model.results.Offer;
import com.edwardvanraak.materialbarcodescannerexample.storage.CustomPreferences;
import com.edwardvanraak.materialbarcodescannerexample.utils.DividerItemDecoration;
import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;
import com.edwardvanraak.materialbarcodescannerexample.utils.SoundManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 01/04/2017.
 */

public class DetailFragment extends Fragment {
    public static final String KEY_ITEM = "KEY_ITEM";
    private ItemDetails itemDetails;

    @BindView(R.id.itemIconView)
    ImageView itemIconView;
    @BindView(R.id.categoryTextView)
    TextView categoryTextView;
    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;
    @BindView(R.id.rankValueTextView)
    TextView rankValueTextView;
    @BindView(R.id.weightValueTextView)
    TextView weightValueTextView;
    @BindView(R.id.orangeEstimateValueTextView)
    TextView orangeEstimateValueTextView;
    @BindView(R.id.amazonValueTextView)
    TextView amazonValueTextView;
    @BindView(R.id.profitLayout)
    RelativeLayout profitLayout;
    @BindView(R.id.profileValueTextView)
    TextView profitValueTextView;
    @BindView(R.id.advisedTextView)
    TextView advisedTextView;
    @BindView(R.id.fbaCountTextView)
    TextView fbaCountTextView;
    @BindView(R.id.usedCountTextView)
    TextView usedCountTextView;
    @BindView(R.id.newOffersCountTextView)
    TextView newOffersCountTextView;
    @BindView(R.id.fbaOffersRecyclerView)
    RecyclerView fbaOffersRecyclerView;
    @BindView(R.id.usedOffersRecyclerView)
    RecyclerView usedOffersRecyclerView;
    @BindView(R.id.newOffersRecyclerView)
    RecyclerView newOffersRecyclerView;

    @BindView(R.id.estimatePriceLayout)
    LinearLayout estimatePriceLayout;
    @BindView(R.id.amazonPriceLayout)
    LinearLayout amazonPriceLayout;
    @BindView(R.id.weightLayout)
    LinearLayout weightLayout;

    @BindView(R.id.adviseInfoDetailLayout)
    RelativeLayout adviseInfoDetailLayout;
    @BindView(R.id.initialSalePriceTextView)
    TextView initialSalePriceTextView;
    @BindView(R.id.inboundShippingTextView)
    TextView inboundShippingTextView;
    @BindView(R.id.costTextView)
    TextView costTextView;
    @BindView(R.id.amazonFeeTextView)
    TextView amazonFeeTextView;
    @BindView(R.id.profitTextView)
    TextView profitTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            itemDetails = (ItemDetails) getArguments().getSerializable(KEY_ITEM);
        }

        View rootView = inflater.inflate(R.layout.detail_screen, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setValuesToViews();
    }

    private void setValuesToViews() {
        Picasso.with(getActivity()).load(itemDetails.smallImageUrl).into(itemIconView);

        if (itemDetails.isOffline) {
            categoryTextView.setVisibility(View.GONE);
            profitValueTextView.setText("Offline");
            advisedTextView.setText("Offline");

            profitLayout.setBackgroundResource(R.drawable.grey_profit_round_rect_bg);
            advisedTextView.setBackgroundResource(R.drawable.grey_advisor_rect_bg);
        } else {
            categoryTextView.setText(itemDetails.productGroup);
            updateProfiltValueLabel(itemDetails.profit);

            advisedTextView.setText(itemDetails.advisor);
            String soundKey = SoundManager.BLANK_SOUND;

            if (itemDetails.advisor != null && itemDetails.advisor.toLowerCase().contains("reject")) {
                soundKey = SoundManager.REJECT_SOUND;
                advisedTextView.setBackgroundResource(R.drawable.red_rejected_rounded_bg);
            } else if (itemDetails.advisor != null && itemDetails.advisor.toLowerCase().contains("buy")) {
                soundKey = SoundManager.BUY_SOUND;
                advisedTextView.setBackgroundResource(R.drawable.green_advisor_rect_bg);
            } else {
                advisedTextView.setBackgroundResource(R.drawable.grey_advisor_rect_bg);
            }
            if (CustomPreferences.isSoundEnabled(getActivity())) {
                SoundManager.playSound(getActivity(), soundKey);
            }
        }

        descriptionTextView.setText(itemDetails.title);
        rankValueTextView.setText(itemDetails.rank);
        if (!TextUtils.isEmpty(itemDetails.weight)) {
            weightValueTextView.setText(itemDetails.weight);
        } else {
            weightLayout.setVisibility(View.GONE);
        }

        if (itemDetails.listPrice != null) {
            orangeEstimateValueTextView.setText(itemDetails.listPrice.getDisplayValue());
        } else {
            estimatePriceLayout.setVisibility(View.GONE);
        }
        if (itemDetails.amazonPrice != null) {
            amazonValueTextView.setText(itemDetails.amazonPrice.getDisplayValue());
        } else {
            amazonPriceLayout.setVisibility(View.GONE);
        }


        fbaCountTextView.setText("" + itemDetails.fbaOffersList.size());
        usedCountTextView.setText("" + itemDetails.usedOffersList.size());
        newOffersCountTextView.setText("" + itemDetails.newOffersList.size());

        configureRecyclerView(fbaOffersRecyclerView, itemDetails.fbaOffersList, offersItemClickListener);
        configureRecyclerView(newOffersRecyclerView, itemDetails.newOffersList, offersItemClickListener);
        configureRecyclerView(usedOffersRecyclerView, itemDetails.usedOffersList, offersItemClickListener);
    }

    private void updateProfiltValueLabel(double profit) {
        if (profit < 0) {
            profitLayout.setBackgroundResource(R.drawable.red_profit_rounded_rect_bg);
        } else if (profit > 0) {
            profitLayout.setBackgroundResource(R.drawable.green_profit_round_bg);
        } else {
            profitLayout.setBackgroundResource(R.drawable.grey_profit_round_rect_bg);
        }

        profitValueTextView.setText(GlobalUtil.formatAmount(profit));
    }

    private void configureRecyclerView(RecyclerView recyclerView, ArrayList<Offer> offerArrayList, OnOffersItemClickListener onRecyclerViewItemClickListener) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        OffersAdapter offersAdapter = new OffersAdapter(getActivity(), offerArrayList, onRecyclerViewItemClickListener);
        recyclerView.setAdapter(offersAdapter);
    }


    private OnOffersItemClickListener offersItemClickListener = new OnOffersItemClickListener() {
        @Override
        public void onOfferItemClicked(double initialPrice) {
            double profit = itemDetails.getUpdatedProfit(initialPrice);
            updateProfiltValueLabel(profit);
        }
    };


    @OnClick(R.id.camelBottomIconView)
    public void camelBottomIconClicked() {
        String url = getString(R.string.camel_url);
        if (!TextUtils.isEmpty(url)) {
            url = url + itemDetails.id;
            openUrlInWebView(url);
        }
    }

    @OnClick(R.id.booksBottomIconView)
    public void booksBottomIconClicked() {
        String url = getString(R.string.books_url);
        if (!TextUtils.isEmpty(url) && itemDetails.id.startsWith("978")) {
            url = url + itemDetails.id;
            openUrlInWebView(url);
        }
    }

    @OnClick(R.id.amazonBottomIconView)
    public void amazonBottomIconClicked() {
        String url = getString(R.string.amazon_url);
        if (!TextUtils.isEmpty(url)) {
            url = url + itemDetails.id;
            openUrlInWebView(url);
        }
    }

    @OnClick(R.id.eBayBottomIconView)
    public void ebayBottomIconClicked() {
        String url = getString(R.string.ebay_url);
        if (!TextUtils.isEmpty(url)) {
            url = url + itemDetails.id;
            openUrlInWebView(url);
        }
    }

    private void openUrlInWebView(String url) {
        Intent intent = new Intent(getActivity(), WebviewActivity.class);
        intent.putExtra(WebviewActivity.KEY_WEBVIEW_URL, url);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.advisedTextView)
    public void advisedTextViewClicked() {
        if (!TextUtils.isEmpty(itemDetails.advisor) && (itemDetails.advisor.equalsIgnoreCase("Buy") || itemDetails.advisor.equalsIgnoreCase("Reject"))) {
            initialSalePriceTextView.setText(GlobalUtil.formatAmount(itemDetails.initialSalePrice));
            inboundShippingTextView.setText("( " + GlobalUtil.formatAmount(itemDetails.calculatedInboundShipping) + " )");
            costTextView.setText("( " + GlobalUtil.formatAmount(itemDetails.cost) + " )");
            amazonFeeTextView.setText("( " + GlobalUtil.formatAmount(itemDetails.partialFee) + " )");
            profitTextView.setText(GlobalUtil.formatAmount(itemDetails.profit));

            adviseInfoDetailLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.closeTextView)
    public void closeInfoClicked() {
        adviseInfoDetailLayout.setVisibility(View.GONE);
    }


}
