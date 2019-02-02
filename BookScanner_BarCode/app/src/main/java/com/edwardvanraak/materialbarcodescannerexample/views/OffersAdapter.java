package com.edwardvanraak.materialbarcodescannerexample.views;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.interfaces.OnOffersItemClickListener;
import com.edwardvanraak.materialbarcodescannerexample.model.results.Offer;
import com.edwardvanraak.materialbarcodescannerexample.utils.GlobalUtil;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by nadirhussain on 03/04/2017.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersHolder> {
    private Activity activity;
    private ArrayList<Offer> offersList;
    private OnOffersItemClickListener itemClickListener;

    public OffersAdapter(Activity activity, ArrayList<Offer> listItemData, OnOffersItemClickListener itemClickListener) {
        this.activity = activity;
        this.offersList = listItemData;
        this.itemClickListener = itemClickListener;
        sortOffersData();
    }

    private void sortOffersData() {
        if (offersList != null) {
            Collections.sort(offersList, new Comparator<Offer>() {
                @Override
                public int compare(Offer offerOne, Offer offerTwo) {
                    if (offerOne.amount > offerTwo.amount) {
                        return 1;
                    } else if (offerOne.amount < offerTwo.amount) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
    }

    @Override
    public OffersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_offers, parent, false);
        new ScalingUtility(activity).scaleRootView(itemView);
        return new OffersAdapter.OffersHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OffersHolder holder, int position) {
        Offer offer = offersList.get(position);
        holder.offersAmountTextView.setText(GlobalUtil.formatAmount(offer.amount));
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class OffersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView offersAmountTextView;

        public OffersHolder(View itemView) {
            super(itemView);
            offersAmountTextView = (TextView) itemView.findViewById(R.id.offersValueTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            itemClickListener.onOfferItemClicked(offersList.get(position).amount);
        }
    }
}
