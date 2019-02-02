package com.nippt.arabicamharicdictionary.free;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

import com.nippt.arabicamharicdictionary.R;
import com.nippt.arabicamharicdictionary.free.database.DatabaseHelper;

import java.io.File;

public class SplashActivity extends Activity {
    File flagFile;

    private static final int DELAY_TIME = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        initDatabase();
    }

    protected void initDatabase() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    copyDbFromAssetsToInternalStorage();
                    goToMainActivity();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, DELAY_TIME);
    }

    private void copyDbFromAssetsToInternalStorage() {
        try {
            DatabaseHelper mDbHelper = new DatabaseHelper(this);
            mDbHelper.createDataBase();
        } catch (Exception exception) {
            Log.d("DatabaseDebug", "Exception = " + exception);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_amharic_dictionary, menu);
        return true;
    }

    private void goToMainActivity() {
        Intent localIntent = new Intent(this, SearchActivity.class);
        startActivity(localIntent);
        finish();
    }
}
