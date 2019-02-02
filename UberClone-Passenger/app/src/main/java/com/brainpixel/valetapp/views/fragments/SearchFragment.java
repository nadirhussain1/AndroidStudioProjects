package com.brainpixel.valetapp.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainpixel.valetapp.R;
import com.brainpixel.valetapp.core.LocationHandler;
import com.brainpixel.valetapp.interfaces.EventBusClasses.NetworkStateChanged;
import com.brainpixel.valetapp.interfaces.EventBusClasses.PinLocationClickedEvent;
import com.brainpixel.valetapp.interfaces.EventBusClasses.SearchEditorInputChanged;
import com.brainpixel.valetapp.interfaces.EventBusClasses.SearchLocationPressedEvent;
import com.brainpixel.valetapp.model.CustomLocation;
import com.brainpixel.valetapp.model.search.PlacesSearchResponse;
import com.brainpixel.valetapp.model.search.SearchDetailResponse;
import com.brainpixel.valetapp.model.search.SearchedPlaceItem;
import com.brainpixel.valetapp.network.RetroApiClient;
import com.brainpixel.valetapp.network.RetroInterface;
import com.brainpixel.valetapp.utils.ApiHelper;
import com.brainpixel.valetapp.utils.GlobalUtil;
import com.brainpixel.valetapp.utils.ScalingUtility;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nadirhussain on 06/03/2017.
 */

public class SearchFragment extends Fragment {
    private static final String GOOGLE_PLACES_API_ROOT = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private static final String GOOGLE_PLACES_DETAIL_API = "https://maps.googleapis.com/maps/api/place/details/json?";
    public static final String ROUTE_API_URL = "http://maps.googleapis.com/maps/api/directions/json?";

    @BindView(R.id.horizontalBar)
    ProgressBar horizontalBar;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.searchRecyclerView)
    RecyclerView searchRecyclerView;

    private Snackbar messageSnackBar = null;
    private View rootView = null;
    private String searchedLocationDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_searchloc, null);
        new ScalingUtility(getActivity()).scaleRootView(rootView);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsAndClicks();
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
        if (messageSnackBar != null) {
            messageSnackBar.dismiss();
        }
    }

    private void initViewsAndClicks() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        searchRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void sendResultsToHome(LatLng searchedLocLatLng) {
        SearchLocationPressedEvent searchLocationPressedEvent = new SearchLocationPressedEvent();
        searchLocationPressedEvent.searchedLocLatLng = searchedLocLatLng;
        searchLocationPressedEvent.locationName = searchedLocationDescription;

        EventBus.getDefault().post(searchLocationPressedEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SearchEditorInputChanged event) {
        initFetchPredictions(event.inputText);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkStateChanged event) {
        if (!GlobalUtil.isInternetConnected(getActivity()) && messageSnackBar == null) {
            messageSnackBar = GlobalUtil.showRedSnackBar(rootView, "Not connected with internet");
        } else if (messageSnackBar != null) {
            messageSnackBar.dismiss();
            messageSnackBar = null;
        }
    }

    private void initFetchPredictions(final String text) {
        CustomLocation bestLocation = LocationHandler.getCurrentBestLocation();
        final String location = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fetchGooglePlacesPrediction(text, location, getString(R.string.google_maps_key));
            }
        });

    }


    private void controlVisibilityOfBar(boolean show) {
        if (isAdded()) {
            if (show) {
                horizontalBar.setVisibility(View.VISIBLE);
            } else {
                horizontalBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void displayErrorMessageView(String message) {
        messageTextView.setText(message);
        searchRecyclerView.setVisibility(View.GONE);
    }

    private void displayResults(List<SearchedPlaceItem> searchList) {
        SearchResultsAdapter adapter = new SearchResultsAdapter(searchList);
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.pinLocationTextView)
    public void pinLocationClicked() {
        EventBus.getDefault().post(new PinLocationClickedEvent());
    }


    private void fetchGooglePlacesPrediction(String text, String location, String apiKey) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            displayErrorMessageView(getString(R.string.connectivity_error));
            return;
        }

        controlVisibilityOfBar(true);

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<PlacesSearchResponse> call = apiService.getSearchPlacePredictionsList(GOOGLE_PLACES_API_ROOT, text, location, "20000", apiKey);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<PlacesSearchResponse>() {
            @Override
            public void onResponse(Call<PlacesSearchResponse> call, Response<PlacesSearchResponse> response) {
                if (isAdded()) {
                    controlVisibilityOfBar(false);
                    PlacesSearchResponse placesSearchResponse = response.body();
                    if (placesSearchResponse != null
                            && placesSearchResponse.getSearchedPlaceItemList() != null && placesSearchResponse.getSearchedPlaceItemList().size() > 0) {

                        displayResults(placesSearchResponse.getSearchedPlaceItemList());
                    } else {
                        displayErrorMessageView(getString(R.string.no_search_result));
                    }
                }

            }


            @Override
            public void onFailure(Call<PlacesSearchResponse> call, Throwable t) {
                if (isAdded()) {
                    controlVisibilityOfBar(false);
                }
            }
        });
    }

    private void fetchPlaceDetails(String reference, String apiKey) {
        if (!GlobalUtil.isInternetConnected(getActivity())) {
            displayErrorMessageView(getString(R.string.connectivity_error));
            return;
        }

        controlVisibilityOfBar(true);

        RetroInterface apiService = RetroApiClient.getClient().create(RetroInterface.class);
        Call<SearchDetailResponse> call = apiService.getPlaceDetails(GOOGLE_PLACES_DETAIL_API, false, reference, apiKey);
        ApiHelper.enqueueWithRetry(getActivity(), call, new retrofit2.Callback<SearchDetailResponse>() {
            @Override
            public void onResponse(Call<SearchDetailResponse> call, Response<SearchDetailResponse> response) {
                if (isAdded()) {
                    controlVisibilityOfBar(false);
                    SearchDetailResponse searchDetailResponse = response.body();
                    if (searchDetailResponse != null
                            && searchDetailResponse.getSearchDetailResult() != null
                            && searchDetailResponse.getSearchDetailResult().getSearchItemGeomtry() != null) {


                        double latitude = searchDetailResponse.getSearchDetailResult().getSearchItemGeomtry().getSearchLocationItem().getLatitude();
                        double longitude = searchDetailResponse.getSearchDetailResult().getSearchItemGeomtry().getSearchLocationItem().getLongitude();
                        sendResultsToHome(new LatLng(latitude,longitude));

                    } else {
                        displayErrorMessageView(getString(R.string.no_search_result));
                    }
                }

            }

            @Override
            public void onFailure(Call<SearchDetailResponse> call, Throwable t) {
                if (isAdded()) {
                    controlVisibilityOfBar(false);
                }
            }
        });
    }

    private class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsHolder> {
        List<SearchedPlaceItem> searchedPlaceItemList;

        public SearchResultsAdapter(List<SearchedPlaceItem> list) {
            searchedPlaceItemList = list;
        }

        @Override
        public SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mineView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_searched_location, parent, false);
            new ScalingUtility(getActivity()).scaleRootView(mineView);
            return new SearchResultsHolder(mineView);
        }

        @Override
        public void onBindViewHolder(SearchResultsHolder holder, int position) {
            SearchedPlaceItem searchedPlaceItem = searchedPlaceItemList.get(position);
            holder.locationDescriptionView.setText(searchedPlaceItem.getDescription().split(",")[0]);
        }

        @Override
        public int getItemCount() {
            return searchedPlaceItemList.size();
        }

        public class SearchResultsHolder extends RecyclerView.ViewHolder implements OnClickListener {
            TextView locationDescriptionView;

            public SearchResultsHolder(View itemView) {
                super(itemView);
                locationDescriptionView = (TextView) itemView.findViewById(R.id.locationDescriptionView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (searchedPlaceItemList != null && position < searchedPlaceItemList.size()) {
                    SearchedPlaceItem searchedPlaceItem = searchedPlaceItemList.get(position);
                    searchedLocationDescription = searchedPlaceItem.getDescription().split(",")[0];
                    fetchPlaceDetails(searchedPlaceItem.getReference(), getString(R.string.google_maps_key));
                }
            }
        }
    }


}
