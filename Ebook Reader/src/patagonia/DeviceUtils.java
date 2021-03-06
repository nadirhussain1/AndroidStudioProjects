package patagonia;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import net.nightwhistler.pageturner.R;

/**
 * Created by rod on 3/12/16.
 */
public class DeviceUtils {

    Context mContext;
    public DeviceUtils(Context context){
        this.mContext = context;
    }
    public Point getScreenPoint(){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public Point getScaledPoint(int width, int height){
        Point screenPoint = getScreenPoint();
        int screenWidth = screenPoint.x;
        int newWidth, newHeight;

        if(width != 0){
            newWidth = screenWidth/ Config.GRID_COLUMNS;
            newHeight = height*newWidth/width;
        } else {
            newWidth = width;
            newHeight = height;
        }


        return new Point(newWidth,newHeight);
    }

    public int getGridItemHeight(){
        return (getScreenPoint().x/Config.GRID_COLUMNS)-getSpanWidth();
    }

    public int getSpanWidth(){
        return mContext.getResources().getDimensionPixelSize(R.dimen.recycler_grid_spacing);
    }

    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;

    /**
     * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection using <code>isConnected()</code>
     * Checks for generic Exceptions and writes them to logcat as <code>CheckConnectivity Exception</code>.
     * Make sure AndroidManifest.xml has appropriate permissions.
     * @param con Application context
     * @return Boolean
     */
    public Boolean isConnected(Context con){

        try{
            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(wifiInfo.isConnected() || mobileInfo.isConnected())
            {
                return true;
            }
        }
        catch(Exception e){
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }

        return false;
    }

    public int convertDpToPx(int dp){
        Resources r = mContext.getResources();
        return(int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
}
