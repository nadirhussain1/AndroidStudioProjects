package com.edwardvanraak.materialbarcodescannerexample.views;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescannerexample.R;
import com.edwardvanraak.materialbarcodescannerexample.interfaces.OnMultiResultItemClickListener;
import com.edwardvanraak.materialbarcodescannerexample.model.results.ListItemData;
import com.edwardvanraak.materialbarcodescannerexample.utils.ScalingUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 28/03/2017.
 */

public class MultiResultAdapter extends RecyclerView.Adapter<MultiResultAdapter.ResultHolder> {
    private Activity activity;
    private ArrayList<ListItemData> itemsList;
    private OnMultiResultItemClickListener onMultiResultItemClickListener;

    public MultiResultAdapter(Activity activity, ArrayList<ListItemData> listItemData, OnMultiResultItemClickListener itemClickListener) {
        this.activity = activity;
        this.itemsList = listItemData;
        this.onMultiResultItemClickListener = itemClickListener;
    }


    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_result, parent, false);
        new ScalingUtility(activity).scaleRootView(itemView);
        return new MultiResultAdapter.ResultHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        ListItemData listItemData = itemsList.get(position);
        if (!TextUtils.isEmpty(listItemData.smallImageUrl)) {
            Picasso.with(activity).load(listItemData.smallImageUrl).resize(400,400).into(holder.iconView);
        }

        if(!TextUtils.isEmpty(listItemData.productGroup)) {
            String category = listItemData.productGroup;
            holder.categoryView.setText(category);
            holder.categoryView.setVisibility(View.VISIBLE);
        }else{
            holder.categoryView.setVisibility(View.GONE);
        }

        holder.itemNameTextView.setText(listItemData.title);

        if (!TextUtils.isEmpty(listItemData.rank)) {
            holder.rankingValueTextView.setText(listItemData.rank);
            holder.rankLayout.setVisibility(View.VISIBLE);
        } else {
            holder.rankLayout.setVisibility(View.GONE);
        }

        if (listItemData.listPrice != null) {
            String price = listItemData.listPrice.getDisplayValue();
            holder.priceValueView.setText(price);
            holder.priceLayout.setVisibility(View.VISIBLE);
        } else {
            holder.priceLayout.setVisibility(View.GONE);
        }
    }

    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconView;
        TextView categoryView, itemNameTextView, rankingValueTextView, priceValueView;
        LinearLayout rankLayout, priceLayout;

        public ResultHolder(View itemView) {
            super(itemView);

            iconView = (ImageView) itemView.findViewById(R.id.iconView);
            categoryView = (TextView) itemView.findViewById(R.id.categoryTextView);
            itemNameTextView = (TextView) itemView.findViewById(R.id.itemNameTextView);
            rankingValueTextView = (TextView) itemView.findViewById(R.id.rankingValueTextView);
            priceValueView = (TextView) itemView.findViewById(R.id.priceValueTextView);
            priceLayout = (LinearLayout) itemView.findViewById(R.id.priceLayout);
            rankLayout = (LinearLayout) itemView.findViewById(R.id.rankingLayout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position < itemsList.size()) {
                String identifier = itemsList.get(position).identifier;
                onMultiResultItemClickListener.itemClicked(identifier);
            }
        }
    }
}
