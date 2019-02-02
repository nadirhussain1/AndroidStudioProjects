package com.crossover.bicycleproject.contracts;

import com.crossover.bicycleproject.model.Places;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public interface DataLoaderContract {
    interface DataLoaderViewContract{
        void displayPlaces(Places places);
        void displayErrorToast(String message);
    }
    interface DataLoaderUserContract{
        void fetchPlacesData();
    }

}
