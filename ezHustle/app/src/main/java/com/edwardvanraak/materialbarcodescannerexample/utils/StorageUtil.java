package com.edwardvanraak.materialbarcodescannerexample.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by nadirhussain on 15/04/2017.
 */

public class StorageUtil {

    private StorageUtil() {

    }

    public static String getDatabaseExternalPath() {
        File appDirectory = Environment.getExternalStoragePublicDirectory("ezHustle");
        if (!appDirectory.exists()) {
            appDirectory.mkdir();
        }
        String mFileName = appDirectory + File.separator + "ezhustle.db";
        return mFileName;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean doesLocalDbExists() {
        File appDirectory = Environment.getExternalStoragePublicDirectory("ezHustle");
        File file = new File(appDirectory, "ezhustle.db");
        return file.exists();
    }


}

