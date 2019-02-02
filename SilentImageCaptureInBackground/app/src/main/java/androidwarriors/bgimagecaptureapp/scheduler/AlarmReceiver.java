package androidwarriors.bgimagecaptureapp.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidwarriors.bgimagecaptureapp.capture.ImageCaptureService;

/**
 * Created by nadirhussain on 27/05/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE=1000;

    // This is the method invoked by Alarm Manager at specific intervals as per user configurations.
    // This method launches ImageCapture Screen.
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DebugReboot","Inside AlarmReceiver");
        Intent imageCapSerIntent = new Intent(context, ImageCaptureService.class);
        context.startService(imageCapSerIntent);

    }
}
