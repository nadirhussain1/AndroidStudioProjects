package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnItemClickListener;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 02/09/2018.
 */

public class HomeAdapter extends Adapter<HomeAdapter.RecyclerViewMainHolder> {
    private ArrayList<MainItem> itemsList = new ArrayList<>();
    private ArrayList<MainItem> filteredList = new ArrayList<>();

    private OnItemClickListener itemClickListener;
    private Activity activity;
    private boolean isGrid = true;

    public HomeAdapter(Activity activity, ArrayList<MainItem> items, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.itemsList = items;
        filteredList.addAll(itemsList);
        this.itemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerViewMainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_home, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new RecyclerViewMainHolder(view);
    }

    public void displayGridView() {
        isGrid = true;
    }

    public void displayListView() {
        isGrid = false;
    }

    public void filterResults(String filter) {
        filteredList.clear();
        for (MainItem mainItem : itemsList) {
            if (mainItem.getName().toLowerCase().contains(filter)) {
                filteredList.add(mainItem);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerViewMainHolder holder, int position) {
        MainItem item = filteredList.get(position);

        if (isGrid) {
            holder.listItemLayout.setVisibility(View.GONE);
            holder.gridItemLayout.setVisibility(View.VISIBLE);

            holder.gridItemNameView.setText(item.getName());
            holder.gridItemPriceView.setText("Rs. " + item.getPrice());
            holder.gridItemRatingBar.setRating(Float.valueOf(item.getAvgRating()));

            if (!TextUtils.isEmpty(item.getImage())) {
                Picasso.get().load(item.getImage()).placeholder(R.drawable.item_placeholder).into(holder.gridItemIconView);
            } else {
                Picasso.get().load(R.drawable.item_placeholder).into(holder.gridItemIconView);
            }
        } else {

            holder.listItemLayout.setVisibility(View.VISIBLE);
            holder.gridItemLayout.setVisibility(View.GONE);

            holder.listItemNameView.setText(item.getName());
            holder.listItemPriceView.setText("Rs. " + item.getPrice());
            holder.listItemRatingBar.setRating(Float.valueOf(item.getAvgRating()));

            if (!TextUtils.isEmpty(item.getImage())) {
                Picasso.get().load(item.getImage()).placeholder(R.drawable.item_placeholder).into(holder.listItemIconView);
            } else {
                Picasso.get().load(R.drawable.item_placeholder).into(holder.listItemIconView);
            }
        }


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class RecyclerViewMainHolder extends ViewHolder implements OnClickListener {
        public RelativeLayout gridItemLayout, listItemLayout;

        public ImageView gridItemIconView, listItemIconView;
        public TextView gridItemNameView, listItemNameView, gridItemPriceView, listItemPriceView;
        public RatingBar gridItemRatingBar, listItemRatingBar;

        public RecyclerViewMainHolder(View itemView) {
            super(itemView);

            gridItemLayout = itemView.findViewById(R.id.gridItemLayout);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);

            gridItemIconView = itemView.findViewById(R.id.gridItemIconView);
            listItemIconView = itemView.findViewById(R.id.listItemIconView);
            gridItemNameView = itemView.findViewById(R.id.gridItemNameView);
            listItemNameView = itemView.findViewById(R.id.listItemNameView);
            gridItemPriceView = itemView.findViewById(R.id.gridItemPriceView);
            listItemPriceView = itemView.findViewById(R.id.listItemPriceView);

            gridItemRatingBar = itemView.findViewById(R.id.gridItemRatingBar);
            listItemRatingBar = itemView.findViewById(R.id.listItemRatingBar);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            MainItem selectedItem = filteredList.get(getAdapterPosition());
            itemClickListener.itemClicked(selectedItem);
        }
    }
}
