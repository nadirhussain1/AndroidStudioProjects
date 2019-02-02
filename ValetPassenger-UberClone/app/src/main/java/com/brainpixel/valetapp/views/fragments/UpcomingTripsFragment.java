package com.brainpixel.valetapp.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.interfaces.EventBusClasses.OnUpcomingTripCancelled;
import com.brainpixel.valetapp.interfaces.EventBusClasses.UpcomingTripScheduleUpdated;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.trips.TripData;
import com.brainpixel.valetapp.model.trips.TripsListResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.brainpixel.valetapp.views.custom.GridSpacingItemDecoration;
import com.brainpixel.valetapp.views.dialogs.CancelScheduleRideWindow;
import com.brainpixel.valetapp.views.dialogs.EditScheduleTimeWindow;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 02/06/2017.
 */

public class UpcomingTripsFragment extends Fragment {
    @BindView(R.id.tripsRecyclerView)
    RecyclerView tripsRecyclerView;
    @BindView(R.id.errorTextView)
    TextView errorTextView;
    @BindView(R.id.loadingBar)
    ProgressBar horizontalLoadingBar;

    private int selectedTripPosition;
    private UpcomingTripsAdapter upcomingTripsAdapter;
    private List<TripData> tripsListData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trips_list_layout, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        tripsRecyclerView.setLayoutManager(mLayoutManager);
        tripsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tripsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, GlobalUtil.dpToPx(getActivity(), 20), true));
        fetchUpcomingTrips();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void showHorizontalBar() {
        horizontalLoadingBar.setVisibility(View.VISIBLE);
    }

    private void hideHorizontalBar() {
        horizontalLoadingBar.setVisibility(View.GONE);
    }

    private void showErrorView() {
        errorTextView.setVisibility(View.VISIBLE);
        tripsRecyclerView.setVisibility(View.GONE);

    }

    private void showRecyclerView() {
        tripsRecyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    private void handleResults(TripsListResponse tripsListResponse) {
        if (tripsListResponse != null && tripsListResponse.getData() != null && tripsListResponse.getData().size() > 0) {
            tripsListData = tripsListResponse.getData();
            upcomingTripsAdapter = new UpcomingTripsAdapter();
            tripsRecyclerView.setAdapter(upcomingTripsAdapter);
            showRecyclerView();
        } else {
            showErrorView();
        }
    }


    private void fetchUpcomingTrips() {
        showHorizontalBar();
        String userId = GlobalDataManager.getInstance().loggedInUserData.getUserId();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<TripsListResponse> call = apiService.getUpcomingTrips(userId);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<TripsListResponse>() {
            @Override
            public void onResponse(Call<TripsListResponse> call, Response<TripsListResponse> response) {
                hideHorizontalBar();
                if (response == null || response.body() == null) {
                    handleResults(null);
                    return;
                }
                TripsListResponse pastTripsListResponse = response.body();
                handleResults(pastTripsListResponse);
            }

            @Override
            public void onFailure(Call<TripsListResponse> call, Throwable t) {
                handleResults(null);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnUpcomingTripCancelled event) {
        tripsListData.remove(selectedTripPosition);
        upcomingTripsAdapter.notifyDataSetChanged();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpcomingTripScheduleUpdated event) {
        tripsListData.get(selectedTripPosition).setRScheduleTime(event.updatedScheduleTime);
        upcomingTripsAdapter.notifyDataSetChanged();
    }


    private class UpcomingTripsAdapter extends RecyclerView.Adapter<UpcomingTripsAdapter.TripRowHolder> {
        @Override
        public TripRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mineView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upcoming_trip, parent, false);
            new ScalingUtility(getActivity()).scaleRootView(mineView);
            return new TripRowHolder(mineView);
        }

        @Override
        public void onBindViewHolder(TripRowHolder holder, int position) {
            TripData tripData = tripsListData.get(position);
            holder.dateTextView.setText(tripData.getRScheduleTime());
            holder.sourceLocNameView.setText(tripData.getRFromLocationName());
            holder.dstLocNameView.setText(tripData.getRToLocationName());

            LatLng sourceLatLng = new LatLng(tripData.getRFromLat(), tripData.getRFromLng());
            LatLng destiLatLng = new LatLng(tripData.getRToLat(), tripData.getRToLng());

            String estimatedFare = "Estimated Fare: " + GlobalUtil.calculateEstimatedFare(sourceLatLng, destiLatLng);
            holder.fareTextView.setText(estimatedFare);
        }

        @Override
        public int getItemCount() {
            return tripsListData.size();
        }

        public class TripRowHolder extends RecyclerView.ViewHolder implements OnClickListener {
            public TextView dateTextView, fareTextView, sourceLocNameView, dstLocNameView;

            public TripRowHolder(View itemView) {
                super(itemView);
                dateTextView = (TextView) itemView.findViewById(R.id.scheduleTimeTextView);
                fareTextView = (TextView) itemView.findViewById(R.id.estFareTextView);
                sourceLocNameView = (TextView) itemView.findViewById(R.id.sourceLocationNameTextView);
                dstLocNameView = (TextView) itemView.findViewById(R.id.destLocationNameTextView);
                itemView.findViewById(R.id.cancelRideTextView).setOnClickListener(this);
                itemView.findViewById(R.id.editTimeTextView).setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                selectedTripPosition = getAdapterPosition();
                String rideId = "" + tripsListData.get(selectedTripPosition).getRId();
                switch (view.getId()) {
                    case R.id.cancelRideTextView:
                        showCancelRideWindow(rideId);
                        break;
                    case R.id.editTimeTextView:
                        String timeString = tripsListData.get(selectedTripPosition).getRScheduleTime();
                        showEditTimeWindow(rideId, timeString);
                        break;
                }
            }
        }

        private void showCancelRideWindow(String rideId) {
            CancelScheduleRideWindow cancelScheduleRideWindow = new CancelScheduleRideWindow(getActivity(), rideId);
            cancelScheduleRideWindow.showDialog();
        }

        private void showEditTimeWindow(String rideId, String timeDateString) {
            EditScheduleTimeWindow editScheduleTimeWindow = new EditScheduleTimeWindow(getActivity(), rideId, timeDateString);
            editScheduleTimeWindow.showDialog();
        }

    }


}
