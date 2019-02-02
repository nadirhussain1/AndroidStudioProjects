package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.model.DeliveryModel;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import java.util.List;

/**
 * Created by nadirhussain on 25/11/2018.
 */

public class OrderStatusAdapter extends Adapter<OrderStatusAdapter.OrderStatusViewHolder> {
    private Activity activity;
    private List<DeliveryModel> ordersStatusList;


    public OrderStatusAdapter(Activity activity, List<DeliveryModel> ordersStatusList) {
        this.activity = activity;
        this.ordersStatusList = ordersStatusList;
    }

    @Override
    public OrderStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_order_status, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new OrderStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderStatusViewHolder holder, int position) {
        DeliveryModel model = ordersStatusList.get(position);
        holder.orderDateTextView.setText(model.getDate().split(" ")[0]);
        holder.orderStatusTextView.setText(model.getStatus());
        if (model.getStatus().contentEquals(GlobalConstants.STATUS_PENDING)) {
            holder.orderStatusTextView.setTextColor(activity.getResources().getColor(R.color.ratingBarActiveColor));
        } else if (model.getStatus().contentEquals(GlobalConstants.STATUS_DELIVERED)) {
            holder.orderStatusTextView.setTextColor(activity.getResources().getColor(R.color.green_color_in_app));
        } else {
            holder.orderStatusTextView.setTextColor(activity.getResources().getColor(R.color.red_color_in_app));
        }
    }

    @Override
    public int getItemCount() {
        return ordersStatusList.size();
    }

    public class OrderStatusViewHolder extends ViewHolder {
        public TextView orderDateTextView, orderStatusTextView;

        public OrderStatusViewHolder(View itemView) {
            super(itemView);

            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
        }
    }
}
