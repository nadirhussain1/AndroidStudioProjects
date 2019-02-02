package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.handlers.OnSearchedLocationClick;
import com.brainpixel.deliveryapp.model.searchloc.SearchedPlaceItem;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nadirhussain on 19/11/2018.
 */


public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsHolder> {
    private Activity activity;
    private List<SearchedPlaceItem> searchedPlaceItemList;

    public SearchResultsAdapter(Activity activity, List<SearchedPlaceItem> list) {
        this.activity = activity;
        this.searchedPlaceItemList = list;
    }

    @Override
    public SearchResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mineView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_searched_location, parent, false);
        new ScalingUtility(activity).scaleRootView(mineView);
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

                OnSearchedLocationClick event=new OnSearchedLocationClick();
                event.clickedPlaceItem=searchedPlaceItem;
                EventBus.getDefault().post(event);
//                searchedLocationDescription = searchedPlaceItem.getDescription().split(",")[0];
//                fetchPlaceDetails(searchedPlaceItem.getReference(), getString(R.string.google_maps_key));
            }
        }
    }
}
