package androidwarriors.bgimagecaptureapp.upload;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by nadirhussain on 28/05/2016.
 */
public class ImageUploader {
    private static final String UPLOAD_URL = "http://www.kyk.cl/upload.php";
    private static String PARAM_IMAGE = "image";
    private static String PARAM_SIZE = "size";


    public static void uploadImage(Context context, final String capturedImage,final String sizeDimension) {
        Log.e("Upload", "uploadImage start function  dimension ="+sizeDimension);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("Upload", "uploaded=" + s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Upload", "upload error" + volleyError);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put(PARAM_IMAGE, capturedImage);
                params.put(PARAM_SIZE, sizeDimension);
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    // This is for debugging logs..
    public static void writeToSdCard(String content) {
        FileOutputStream fileOutputStream = null;
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath());
            dir.mkdirs();
            File file = new File(dir, "cameraApplicationlog.txt");

            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
        } catch (Exception exception) {

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {

                }
            }
        }

    }

}
