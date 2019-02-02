package com.connectivityapps.flashlight;

import android.app.Activity;
import android.os.Bundle;

import com.connectivityapps.utils.GlobalUtil;

/**
 * Created by nadirhussain on 05/03/2015.
 */
public class KillActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalUtil.releaseCamera();
        killApplication();
    }
    private void killApplication(){
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }
}
