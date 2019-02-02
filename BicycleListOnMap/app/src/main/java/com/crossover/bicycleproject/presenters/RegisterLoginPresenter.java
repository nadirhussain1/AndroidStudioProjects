package com.crossover.bicycleproject.presenters;

import android.content.Context;

import com.crossover.bicycleproject.contracts.LoginRegisterContract;
import com.crossover.bicycleproject.model.AccessToken;
import com.crossover.bicycleproject.model.Credentials;
import com.crossover.bicycleproject.retrofitconnection.BiyCycleApiRest;
import com.crossover.bicycleproject.retrofitconnection.RentAppEndpointInterface;
import com.crossover.bicycleproject.utils.CommonUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nadirhussain on 25/07/2016.
 */
public class RegisterLoginPresenter implements LoginRegisterContract.LoginRegisterUserContract {
    private static final String DEFAULT_EMAIL_ID = "crossover@crossover.com";
    private static final String DEFAULT_PASSWORD = "crossover";
    private RentAppEndpointInterface apiService;
    private Credentials defaultCredentials;

    LoginRegisterContract.LoginRegisterViewContract viewContract;

    public RegisterLoginPresenter(LoginRegisterContract.LoginRegisterViewContract viewContract) {
        this.viewContract = viewContract;
        apiService = BiyCycleApiRest.getClient().create(RentAppEndpointInterface.class);
        defaultCredentials = new Credentials(DEFAULT_EMAIL_ID, DEFAULT_PASSWORD);
    }

    @Override
    public boolean checkInternetConnectivity(Context context) {
        return CommonUtil.isInternetConnected(context);
    }

    @Override
    public void verifyCredentials(String email, String pass, boolean isRegister) {
        // Validate email first. If it is invalid then show message to user.
        if (!CommonUtil.isValidEmail(email)) {
            viewContract.showEmailErrorToast();
            return;
        }
        // Check that password is not empty at all.
        if (pass.isEmpty()) {
            viewContract.showPasswordErrorToast();
            return;
        }
        if (isRegister) {
            doRegisterUser(email, pass);
        } else {
            doAuthenticateUser(email, pass);
        }

    }

    @Override
    public void doRegisterUser(String email, String password) {
        // Putting default credentials as in requirements it is mentioned
        // that server side code works just with default credentials
        Call<AccessToken> accessTokenCall = apiService.registerUser(defaultCredentials);
        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken accessToken = response.body();
                if (accessToken != null) {
                    viewContract.goToDataLoadingActivity(accessToken);
                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                viewContract.showLoginAuthError(t.getMessage());
            }
        });

    }

    @Override
    public void doAuthenticateUser(String email, String password) {
        // Putting default credentials as in requirements it is mentioned
        // that server side code works just with default credentials
        Call<AccessToken> accessTokenCall = apiService.authUser(defaultCredentials);
        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken accessToken = response.body();
                if (accessToken != null) {
                    viewContract.goToDataLoadingActivity(accessToken);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                viewContract.showLoginAuthError(t.getMessage());
            }
        });
    }


}
