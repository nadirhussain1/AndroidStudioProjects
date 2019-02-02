package com.connectivityapps.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;
import android.util.Log;

import com.connectivityapps.flashlight.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nadirhussain on 24/02/2015.
 */
public class GlobalUtil {
    public static Camera camera;
    public static Camera.Parameters params;
    public static boolean isCameraReady=false;
    public static int batteryLevelPercentage;
    public static boolean isSosFullScreenShowing=false;
    public static boolean isCameraFeatEnabled=false;

    public static int getDrawableId(Context context,String resName){
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }
    public static void getCamera(Activity activity) {
        if (GlobalUtil.camera == null && hasCamera(activity)) {

            int cameraIndex = -1;
            int cameraCount = Camera.getNumberOfCameras();

            //Find Back facing camera
            for (int i = 0; i < cameraCount && cameraIndex == -1; i++) {
                CameraInfo info = new CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                    cameraIndex = i;
                }
            }
            //Find front facing camera
            if(cameraIndex ==-1){
                for (int i = 0; i < cameraCount && cameraIndex == -1; i++) {
                    CameraInfo info = new CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                        cameraIndex = i;
                    }
                }
            }

            //Check If any camera found
            try {
                if (cameraIndex != -1) {
                    camera = Camera.open(cameraIndex);
                    if(camera !=null) {
                        params = camera.getParameters();
                    }
                }
            }catch(Exception exception){
                Log.d("Exception",""+exception);
            }
        }
    }
    private static boolean hasCamera(Activity activity){
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    public static void releaseCamera(){
       if(camera !=null){
           camera.stopPreview();
           camera.release();
           camera=null;
       }
    }
    public static void updateBatteryStatus(Context mContext){
        int mode = batteryLevelPercentage % 5;
        String battery_icon = "battery_" + (batteryLevelPercentage - mode);
        int batteryLevelIconResId = GlobalUtil.getDrawableId(mContext, battery_icon);


       MainActivity. batteryLevelIconView.setBackgroundResource(batteryLevelIconResId);
       MainActivity. batteryLevelTextView.setText(batteryLevelPercentage + "%");
    }
    public static void saveLogsToSdCard(String filename, String value) {
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        FileOutputStream fos;
        byte[] dataToWrite = value.getBytes();
        try {
            fos = new FileOutputStream(file);
            fos.write(dataToWrite);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }
    }

}
