package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.adapters.MyOrdersAdapter.MyOdersViewHolder;
import com.brainpixel.deliveryapp.handlers.OrderItemClicked;
import com.brainpixel.deliveryapp.model.Order;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nadirhussain on 24/11/2018.
 */

public class MyOrdersAdapter extends Adapter<MyOdersViewHolder> {
    private Activity activity;
    private List<Order> orders;


    public MyOrdersAdapter(Activity activity, List<Order> orders) {
        this.activity = activity;
        this.orders = orders;
    }

    @Override
    public MyOdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_my_orders, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new MyOdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOdersViewHolder holder, int position) {
        Order order = orders.get(position);
        String creationDate = order.getCreationDate();

        holder.orderNumberTextView.setText(order.getOrderNumber());
        holder.orderDateTextView.setText(creationDate.split(" ")[0]);
        holder.orderBillTextView.setText(order.getTotalBill());

        if (order.getOrderStatus().contentEquals(GlobalConstants.STATUS_CANCELLED)) {
            holder.itemRootView.setBackgroundColor(activity.getResources().getColor(R.color.red_color_in_app));
            holder.orderNumberTextView.setTextColor(Color.WHITE);
            holder.orderDateTextView.setTextColor(Color.WHITE);
            holder.orderBillTextView.setTextColor(Color.WHITE);
        } else {
            holder.itemRootView.setBackgroundColor(Color.WHITE);
            holder.orderNumberTextView.setTextColor(Color.BLACK);
            holder.orderDateTextView.setTextColor(Color.parseColor("#AA000000"));
            holder.orderBillTextView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyOdersViewHolder extends ViewHolder implements OnClickListener {
        public View itemRootView;
        public TextView orderNumberTextView, orderDateTextView, orderBillTextView;

        public MyOdersViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            orderNumberTextView = itemView.findViewById(R.id.orderNumberTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderBillTextView = itemView.findViewById(R.id.orderBillTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OrderItemClicked event = new OrderItemClicked();
            event.position = getAdapterPosition();

            EventBus.getDefault().post(event);
        }
    }
}
