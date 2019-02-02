package com.brainpixel.valetapp.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.model.GlobalDataManager;
import com.brainpixel.valetapp.model.trips.TripData;
import com.brainpixel.valetapp.model.trips.TripsListResponse;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.brainpixel.valetapp.views.custom.EndlessRecyclerOnScrollListener;
import com.brainpixel.valetapp.views.custom.GridSpacingItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 02/06/2017.
 */

public class PastTripsFragment extends Fragment {
    @BindView(R.id.tripsRecyclerView)
    RecyclerView tripsRecyclerView;
    @BindView(R.id.errorTextView)
    TextView errorTextView;
    @BindView(R.id.loadingBar)
    ProgressBar horizontalLoadingBar;

    private int pageNumber = 0;
    private int records = 5;
    private boolean areAllRecordsLoaded = false;
    private ArrayList<TripData> pastTripList = new ArrayList<>();
    private PastTripsAdapter pastTripsAdapter = null;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

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
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!areAllRecordsLoaded) {
                    pageNumber = current_page;
                    fetchPastTrips();
                }
            }
        };
        tripsRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        fetchPastTrips();

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

    private void loadResultsIntoRecyclerView(List<TripData> tripsList) {
        pastTripList.addAll(tripsList);
        if (pastTripsAdapter == null) {
            pastTripsAdapter = new PastTripsAdapter();
            tripsRecyclerView.setAdapter(pastTripsAdapter);
        } else {
            pastTripsAdapter.notifyDataSetChanged();
        }
    }

    private void handleResults(TripsListResponse pastTripsListResponse) {
        if (pastTripsListResponse != null && pastTripsListResponse.getData() != null && pastTripsListResponse.getData().size() > 0) {
            loadResultsIntoRecyclerView(pastTripsListResponse.getData());
            showRecyclerView();
        } else if (pastTripList.size() == 0) {
            showErrorView();
            areAllRecordsLoaded = true;
        }
    }

    private void fetchPastTrips() {
        showHorizontalBar();
        String userId = GlobalDataManager.getInstance().loggedInUserData.getUserId();
        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<TripsListResponse> call = apiService.getPastTripsList(userId, records, pageNumber);
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


    private class PastTripsAdapter extends RecyclerView.Adapter<PastTripsAdapter.PastTripRowHolder> {

        @Override
        public PastTripRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mineView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_past_trip, parent, false);
            new ScalingUtility(getActivity()).scaleRootView(mineView);
            return new PastTripRowHolder(mineView);
        }

        @Override
        public void onBindViewHolder(PastTripRowHolder holder, int position) {
            TripData pastTripData = pastTripList.get(position);
            if (!TextUtils.isEmpty(pastTripData.getRMapScreenshotImg())) {
                Picasso.with(getActivity()).load(pastTripData.getRMapScreenshotImg()).placeholder(R.drawable.map_placeholder).into(holder.tripScreenshotView);
            } else {
                Picasso.with(getActivity()).load(R.drawable.map_placeholder).into(holder.tripScreenshotView);
            }

            if (pastTripData.getRTotalCharged() == null) {
                holder.fareTextView.setText("Cancelled");
            } else {
                holder.fareTextView.setText("" + pastTripData.getRTotalCharged());
            }

            if (pastTripData.getRStartDatetime() != null) {
                holder.dateTextView.setText(pastTripData.getRStartDatetime());
            } else {
                holder.dateTextView.setText(pastTripData.getRScheduleTime());
            }

        }

        @Override
        public int getItemCount() {
            return pastTripList.size();
        }

        public class PastTripRowHolder extends RecyclerView.ViewHolder implements OnClickListener {
            public ImageView tripScreenshotView;
            public TextView dateTextView, fareTextView;

            public PastTripRowHolder(View itemView) {
                super(itemView);
                tripScreenshotView = (ImageView) itemView.findViewById(R.id.tripImageView);
                dateTextView = (TextView) itemView.findViewById(R.id.dateTimeTextView);
                fareTextView = (TextView) itemView.findViewById(R.id.fareTextView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();

            }
        }
    }
}
