package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.model.ShippingItem;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nadirhussain on 21/11/2018.
 */

public class ShippingItemsAdapter extends Adapter<ShippingItemsAdapter.ShippingItemViewHolder> {

    private Activity activity;
    private List<ShippingItem> shippingItems;


    public ShippingItemsAdapter(Activity activity, List<ShippingItem> shippingItems) {
        this.activity = activity;
        this.shippingItems = shippingItems;
    }

    @Override
    public ShippingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_shipping_item, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new ShippingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShippingItemViewHolder holder, int position) {
        ShippingItem shippingItem = shippingItems.get(position);
        MainItem mainItem = GlobalDataManager.getInstance().getMainItem(shippingItem.getMainItemId());
        if (mainItem != null) {
            holder.itemNameView.setText(mainItem.getName());
            if (!TextUtils.isEmpty(mainItem.getImage())) {
                Picasso.get().load(mainItem.getImage()).placeholder(R.drawable.item_placeholder).into(holder.itemIconView);
            } else {
                Picasso.get().load(R.drawable.item_placeholder).into(holder.itemIconView);
            }
        }
        holder.priceTextView.setText(shippingItem.getItemPrice());
        holder.quantityTextView.setText(shippingItem.getQuantity());

        int subTotal = GlobalUtil.getIntValue(shippingItem.getItemPrice()) * GlobalUtil.getIntValue(shippingItem.getQuantity());
        holder.subTotalTextView.setText("" + subTotal);
    }

    @Override
    public int getItemCount() {
        return shippingItems.size();
    }

    public class ShippingItemViewHolder extends ViewHolder {
        public ImageView itemIconView;
        public TextView itemNameView, priceTextView, quantityTextView, subTotalTextView;

        public ShippingItemViewHolder(View itemView) {
            super(itemView);

            itemIconView = itemView.findViewById(R.id.itemIconView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            subTotalTextView = itemView.findViewById(R.id.subTotalTextView);

        }


    }
}
