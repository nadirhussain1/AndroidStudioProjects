package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnItemClickListener;
import com.brainpixel.deliveryapp.model.CartItem;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.brainpixel.deliveryapp.views.QuantityDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 30/09/2018.
 */


public class CartAdapter extends Adapter<CartAdapter.CartItemViewHolder> {
    private ArrayList<CartItem> itemsList = new ArrayList<>();
    private ArrayList<CartItem> filteredList = new ArrayList<>();

    private OnItemClickListener itemClickListener;
    private Activity activity;
    private boolean isGrid = true;

    public CartAdapter(Activity activity, ArrayList<CartItem> items, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.itemsList = items;
        filteredList.addAll(itemsList);
        this.itemClickListener = onItemClickListener;
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_cart_item, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new CartItemViewHolder(view);
    }

    public void displayGridView() {
        isGrid = true;
    }

    public void displayListView() {
        isGrid = false;
    }

    public void filterResults(String filter) {
        filteredList.clear();
        for (CartItem mainItem : itemsList) {
            if (mainItem.getName().toLowerCase().contains(filter)) {
                filteredList.add(mainItem);
            }
        }
        notifyDataSetChanged();
    }

    public void refreshItems(ArrayList<CartItem> items) {
        this.itemsList = items;
        filteredList.clear();
        filteredList.addAll(itemsList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        CartItem item = filteredList.get(position);

        if (isGrid) {
            holder.listItemLayout.setVisibility(View.GONE);
            holder.gridItemLayout.setVisibility(View.VISIBLE);

            holder.gridItemNameView.setText(item.getName());
            holder.gridItemPriceView.setText("Rs. " + item.getPrice());
            holder.gridQuantityEditor.setText(item.getCartQuantity());

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
            holder.listQuantityEditor.setText(item.getCartQuantity());

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

    public class CartItemViewHolder extends ViewHolder implements OnClickListener {
        public RelativeLayout gridItemLayout, listItemLayout;

        public ImageView gridItemIconView, listItemIconView;
        public TextView gridItemNameView, listItemNameView, gridItemPriceView, listItemPriceView, gridQuantityEditor, listQuantityEditor;
        public LinearLayout gridQuantityLayout, listQuantityLayout;


        public CartItemViewHolder(View itemView) {
            super(itemView);

            gridItemLayout = itemView.findViewById(R.id.gridItemLayout);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);

            gridItemIconView = itemView.findViewById(R.id.gridItemIconView);
            listItemIconView = itemView.findViewById(R.id.listItemIconView);
            gridItemNameView = itemView.findViewById(R.id.gridItemNameView);
            listItemNameView = itemView.findViewById(R.id.listItemNameView);
            gridItemPriceView = itemView.findViewById(R.id.gridItemPriceView);
            listItemPriceView = itemView.findViewById(R.id.listItemPriceView);

            gridQuantityEditor = itemView.findViewById(R.id.gridQuantityEditor);
            listQuantityEditor = itemView.findViewById(R.id.listQuantityEditor);
            gridQuantityLayout = itemView.findViewById(R.id.gridQuantityLayout);
            listQuantityLayout = itemView.findViewById(R.id.listQuantityLayout);

            View gridTrashClickArea = itemView.findViewById(R.id.gridTrashClickArea);
            View listTrashClickArea = itemView.findViewById(R.id.listTrashClickArea);

            gridTrashClickArea.setOnClickListener(this);
            listTrashClickArea.setOnClickListener(this);

            gridQuantityEditor.setOnClickListener(this);
            listQuantityEditor.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            CartItem cartItem = filteredList.get(getAdapterPosition());
            if (v.getId() == R.id.gridTrashClickArea || v.getId() == R.id.listTrashClickArea) {
                itemClickListener.itemClicked(cartItem);
            } else {
                displayChangeQuantityDialog(cartItem);
            }
        }

    }

    private void displayChangeQuantityDialog(CartItem cartItem) {
        QuantityDialog quantityDialog=new QuantityDialog(activity,cartItem);
        quantityDialog.showDialog();
    }

}

