package patagonia;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Created by rod on 3/12/16.
 */
public class Utils {
    private static Utils INSTANCE = null;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static AmazonS3Client sS3Client;
    private static TransferUtility sTransferUtility;

    static Context mContext;

    private Utils(){}

    public static void initialize(Context context){
        mContext = context;
    }

    public static synchronized Utils getInstance() {
        if (mContext == null) {
            throw new IllegalArgumentException("Impossible to get the instance. You mus call initialize method in your Application class");
        }

        if (INSTANCE == null) {
            INSTANCE = new Utils();
        }

        return INSTANCE;
    }

    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    Config.COGNITO_POOL_ID,
                    Regions.US_EAST_1);
        }
        return sCredProvider;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
        }
        return sS3Client;
    }

    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }

        return sTransferUtility;
    }

    public DeviceUtils getDeviceUtils(){
        return new DeviceUtils(mContext);
    }

    public static void throwNullException(Object object, String name){
        if (object == null) {
            throw new NullPointerException(name+" must not be null");
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void launchEmailApp(Context context, String emailTo){
        if (emailTo.length() == 0) {
            return;
        }
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        String aEmailList[] = {emailTo};

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.setType("plain/text");
        context.startActivity(emailIntent);
    }

    public static void launchBrowser(Context context, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}

