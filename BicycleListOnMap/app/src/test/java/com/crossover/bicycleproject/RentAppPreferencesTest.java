package com.crossover.bicycleproject;

import android.content.SharedPreferences;

import com.crossover.bicycleproject.storage.RentAppPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by nadirhussain on 26/07/2016.
 */
public class RentAppPreferencesTest {
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor mMockEditor;
    private RentAppPreferences rentAppPreferences;

    @Before
    public void setRentAppPreferences(){
        MockitoAnnotations.initMocks(this);
        when(mMockEditor.commit()).thenReturn(true);
        when(sharedPreferences.edit()).thenReturn(mMockEditor);

        rentAppPreferences = new RentAppPreferences(sharedPreferences);
    }
    @Test
    public void saveAccessTokenTest(){
       assertThat(rentAppPreferences.saveAccessToken("ABSCCDCD"),is(true));
    }

}
