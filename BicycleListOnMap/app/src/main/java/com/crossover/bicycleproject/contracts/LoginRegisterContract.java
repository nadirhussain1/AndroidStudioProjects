package com.crossover.bicycleproject.contracts;

import android.content.Context;

import com.crossover.bicycleproject.model.AccessToken;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public interface LoginRegisterContract {
    interface LoginRegisterViewContract {
        void showNoConnectivityAlert(String message);

        void showEmailErrorToast();

        void showPasswordErrorToast();

        void goToDataLoadingActivity(AccessToken accessToken);

        void showLoginAuthError(String message);
    }

    interface LoginRegisterUserContract {
        boolean checkInternetConnectivity(Context context);

        void verifyCredentials(String email, String pass, boolean isRegister);

        void doRegisterUser(String email, String password);

        void doAuthenticateUser(String email, String password);

    }
}
