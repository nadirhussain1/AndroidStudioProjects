package com.crossover.bicycleproject.contracts;

import com.crossover.bicycleproject.model.Payment;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public interface RentABikeContract {
    interface RentABikeViewContract{
        void showRentServiceMessage(String message);
        void showCreditCardErrorMessage();
    }
    interface RentABikeUserContract{
        void invokeRentABikeService(Payment payment);
    }
}
