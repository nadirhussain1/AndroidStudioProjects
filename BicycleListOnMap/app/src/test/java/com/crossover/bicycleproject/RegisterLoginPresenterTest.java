package com.crossover.bicycleproject;

import com.crossover.bicycleproject.contracts.LoginRegisterContract;
import com.crossover.bicycleproject.model.AccessToken;
import com.crossover.bicycleproject.presenters.RegisterLoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

/**
 * Created by nadirhussain on 26/07/2016.
 */
public class RegisterLoginPresenterTest {

    @Mock
    LoginRegisterContract.LoginRegisterViewContract viewContract;
    @Mock
    AccessToken accessToken;
    private RegisterLoginPresenter registerLoginPresenter;

    @Before
    public void setRegisterLoginPresenter(){
        MockitoAnnotations.initMocks(this);
        registerLoginPresenter = new RegisterLoginPresenter(viewContract);
    }
    @Test
    public void verifyInCorrectPassCredentialsTest(){
        registerLoginPresenter.verifyCredentials("nadir@gmail.com","",true);
        verify(viewContract).showPasswordErrorToast();
    }
    @Test
    public void verifyInCorrectEmailCredentialsTest(){
        registerLoginPresenter.verifyCredentials("nadir@gmail","sab",true);
        verify(viewContract).showEmailErrorToast();
    }

}
