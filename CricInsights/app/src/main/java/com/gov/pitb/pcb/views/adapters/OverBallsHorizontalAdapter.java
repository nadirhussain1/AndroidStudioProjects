package com.gov.pitb.pcb.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gov.pitb.pcb.R;
import com.gov.pitb.pcb.data.MatchStateManager;
import com.gov.pitb.pcb.data.db.InsightsDbManager;
import com.gov.pitb.pcb.data.db.dynamic.Delivery;
import com.gov.pitb.pcb.data.preferences.InsightsPreferences;
import com.gov.pitb.pcb.data.server.permissions.MatchPermData;
import com.gov.pitb.pcb.navigators.EventBusEvents.OnPreviousDeliverySelected;
import com.gov.pitb.pcb.utils.ViewScaleHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nadirhussain on 20/06/2017.
 */

public class OverBallsHorizontalAdapter extends RecyclerView.Adapter<OverBallsHorizontalAdapter.BallHolder> {
    private List<Delivery> deliveries;
    private Activity activity = null;
    private int userType;
    private int highlightedPosition;
    MatchPermData matchPermData;

    public OverBallsHorizontalAdapter(Activity activity, List<Delivery> deliveries) {
        this.activity = activity;
        this.deliveries = deliveries;
        this.highlightedPosition = -1;
        matchPermData = InsightsDbManager.getMatchPermData();

        this.userType = InsightsPreferences.getUserType(activity);
    }

    public void refreshDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
        notifyDataSetChanged();
    }


    @Override
    public BallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.over_ball_info_layout, parent, false);
        new ViewScaleHandler(activity).scaleRootView(itemView);
        return new BallHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BallHolder holder, int position) {
        Delivery delivery = deliveries.get(position);
        String overBallNumber = "" + delivery.getOverId() + "." + delivery.getBallNumberOfOver();
        holder.ballNumberTextView.setText(overBallNumber);

        if (delivery.getDeliveryId() == MatchStateManager.getInstance().getCurrentDeliveryId()) {
            holder.ballRunsInfoTextView.setBackgroundResource(R.drawable.current_ball_circle_bg);
            holder.ballRunsInfoTextView.setText("");
        } else if (delivery.isRemaining()) {
            holder.ballRunsInfoTextView.setBackgroundResource(R.drawable.remaining_ball_cicle_bg);
            holder.ballRunsInfoTextView.setText("");
        } else {
            if (position == highlightedPosition) {
                holder.ballRunsInfoTextView.setBackgroundResource(R.drawable.done_highlighted_ball_bg);
            } else {
                holder.ballRunsInfoTextView.setBackgroundResource(R.drawable.done_ball_circle_bg);
            }
            if (matchPermData.isScore()) {
                setBallInfoInScorerMode(holder.ballRunsInfoTextView, delivery);
            } else {
                setBallInfoInAnalystMode(holder.ballRunsInfoTextView, delivery);
            }

        }
    }

    private void setBallInfoInAnalystMode(TextView textView, Delivery delivery) {
        if (!delivery.isValidBall()) {
            textView.setText("EX");
        } else {
            textView.setText("");
        }
    }

    private void setBallInfoInScorerMode(TextView textView, Delivery delivery) {
        if (delivery.isOutOnThisDelivery()) {
            textView.setText("W");
        } else if (!TextUtils.isEmpty(delivery.getNoType())) {
            textView.setText("NB");
        } else if (!TextUtils.isEmpty(delivery.getWideType())) {
            textView.setText("WB");
        } else {
            textView.setText("" + delivery.getRuns());
        }
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public class BallHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public View itemView;
        public TextView ballRunsInfoTextView, ballNumberTextView;

        public BallHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ballRunsInfoTextView = (TextView) itemView.findViewById(R.id.ballRunsInfoTextView);
            ballNumberTextView = (TextView) itemView.findViewById(R.id.ballNumberTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int selectedPosition = getAdapterPosition();
            Delivery selectedDelivery = deliveries.get(selectedPosition);
            boolean switchToDelivery = false;
            if (isCurrentDelivery(selectedDelivery)) {
                highlightedPosition = -1;
                switchToDelivery = true;
            } else if (!selectedDelivery.isRemaining()) {
                highlightedPosition = selectedPosition;
                switchToDelivery = true;
            }

            if (switchToDelivery) {
                notifyDataSetChanged();

                OnPreviousDeliverySelected onPreviousDeliverySelected = new OnPreviousDeliverySelected();
                onPreviousDeliverySelected.selectedDelivery = selectedDelivery;
                EventBus.getDefault().post(onPreviousDeliverySelected);
            }

        }

        private boolean isCurrentDelivery(Delivery delivery) {
            return delivery.getDeliveryId() == MatchStateManager.getInstance().getCurrentDeliveryId();
        }


    }
}
