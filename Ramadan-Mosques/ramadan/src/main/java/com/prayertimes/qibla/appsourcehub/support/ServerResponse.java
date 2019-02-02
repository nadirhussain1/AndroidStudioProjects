package com.prayertimes.qibla.appsourcehub.support;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import java.io.*;
import java.net.*;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServerResponse
{
    public enum RequestType
    {
    	GET("GET", 0),
        POST("POST", 1);
        static 
        {
            RequestType arequesttype[] = new RequestType[2];
            arequesttype[0] = GET;
            arequesttype[1] = POST;
        }

        private RequestType(String s, int i)
        {
        }
    }


    public static final String tag = "ServerResponse";
    private String url;

    public ServerResponse()
    {
    }

    public ServerResponse(String s)
    {
        url = s;
        LogUtils.d("ServerResponse", (new StringBuilder("Url -> ")).append(s).toString());
    }

    public Bitmap getImageBitmap()
    {
        Bitmap bitmap = null;
        try
        {
            URLConnection urlconnection = (new URL(url)).openConnection();
            urlconnection.connect();
            InputStream inputstream = urlconnection.getInputStream();
            BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);
            bitmap = BitmapFactory.decodeStream(bufferedinputstream);
            bufferedinputstream.close();
            inputstream.close();
        }
        catch(IOException ioexception)
        {
            LogUtils.e(url, "Error getting bitmap");
            return bitmap;
        }
        return bitmap;
    }

    public Reader getReaderfromURL(RequestType requesttype, List list)
    {
    	HttpResponse httpresponse;
        InputStreamReader inputstreamreader = null;
        DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
        try{
	        if(requesttype != RequestType.GET){
	        	RequestType requesttype1 = RequestType.POST;
	            httpresponse = null;
	            if(requesttype == requesttype1){
	    	        HttpResponse httpresponse1;
	    	        HttpPost httppost = new HttpPost(url);
	    	        httppost.setEntity(new UrlEncodedFormEntity(list));
	    	        httpresponse1 = defaulthttpclient.execute(httppost);
	    	        httpresponse = httpresponse1;
	            }
	        }else
	        	httpresponse = defaulthttpclient.execute(new HttpGet(url));
	        InputStream inputstream = httpresponse.getEntity().getContent();
	        inputstreamreader = null;
	        if(httpresponse != null)
	        {
	        	InputStreamReader inputstreamreader1 = new InputStreamReader(inputstream);
	            LogUtils.d("ServerResponse", (new StringBuilder("JsonObject -> ")).append(inputstreamreader1).toString());
	            inputstreamreader = inputstreamreader1;
	        }
        }catch(Exception e){}
        return inputstreamreader;
    }

    public String getStringfromURL(RequestType requesttype, String s)
    {
    	HttpResponse httpresponse;
    	InputStream inputstream;
        String s1 = "";
        DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
        try{
	        if(requesttype != RequestType.GET){
	        	RequestType requesttype1 = RequestType.POST;
	            httpresponse = null;
	            if(requesttype == requesttype1){
	    	        HttpResponse httpresponse1;
	    	        LogUtils.d("ServerResponse", (new StringBuilder("Body -> ")).append(s).toString());
	    	        HttpPost httppost = new HttpPost(url);
	    	        httppost.setEntity(new StringEntity(s));
	    	        httpresponse1 = defaulthttpclient.execute(httppost);
	    	        httpresponse = httpresponse1;
	            }
	        }else
	        	httpresponse = defaulthttpclient.execute(new HttpGet(url));
	        try{
		        InputStream inputstream1 = httpresponse.getEntity().getContent();
		        inputstream = inputstream1;
	        }catch(Exception e){
	        	inputstream = null;
	        }
	        BufferedReader bufferedreader;
	        StringBuilder stringbuilder;
	        bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
	        stringbuilder = new StringBuilder();
	        while(true){
		        String s2 = bufferedreader.readLine();
		        if(s2 == null)
		        	break;
		        stringbuilder.append((new StringBuilder(String.valueOf(s2))).append("\n").toString());
	        }
	        inputstream.close();
	        s1 = stringbuilder.toString();
	        LogUtils.d("ServerResponse", (new StringBuilder("String -> ")).append(s1).toString());
        }catch(Exception e){}
        return s1;
    }

    public Reader loadJSONReaderFromAsset(Activity activity, String s)
    {
        InputStreamReader inputstreamreader;
        try
        {
            inputstreamreader = new InputStreamReader(activity.getAssets().open(s));
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            return null;
        }
        return inputstreamreader;
    }
}
