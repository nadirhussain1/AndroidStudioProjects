package androidwarriors.movieslistassessment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nadirhussain on 16/07/2016.
 */
public class CommonUtil {
    public static boolean isInternetConnected(Context ctx) {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }
}
