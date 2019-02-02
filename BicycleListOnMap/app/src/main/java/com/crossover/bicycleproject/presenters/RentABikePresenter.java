package com.crossover.bicycleproject.presenters;

import com.crossover.bicycleproject.contracts.RentABikeContract;
import com.crossover.bicycleproject.model.Message;
import com.crossover.bicycleproject.model.Payment;
import com.crossover.bicycleproject.retrofitconnection.BiyCycleApiRest;
import com.crossover.bicycleproject.retrofitconnection.RentAppEndpointInterface;
import com.crossover.bicycleproject.utils.GlobalData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class RentABikePresenter implements RentABikeContract.RentABikeUserContract {
    private RentABikeContract.RentABikeViewContract viewContract;

    public RentABikePresenter(RentABikeContract.RentABikeViewContract viewContract) {
        this.viewContract = viewContract;
    }

    // Validate Credit Card Number. Currently only verification is that it should be 16 digits.
    // If it is then make a call to rent a bike by providing all needed information.
    // Currently expiry date and security code has been supposed fixed. As they are not mentioned
    // in requirements.

    @Override
    public void invokeRentABikeService(Payment payment) {
        if (payment.getNumber() == null || payment.getNumber().length() != 16) {
            viewContract.showCreditCardErrorMessage();
            return;
        }

        RentAppEndpointInterface apiService = BiyCycleApiRest.getClient().create(RentAppEndpointInterface.class);
        Call<Message> rentABikeCall = apiService.rentBiCylce(GlobalData.accessToken, payment);
        rentABikeCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message message = response.body();
                viewContract.showRentServiceMessage(message.getMessage());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                viewContract.showRentServiceMessage(t.getMessage());
            }
        });
    }
}
