package com.prayertimes.qibla.appsourcehub.map;

import android.util.Log;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import org.apache.http.client.HttpResponseException;

public class GooglePlaces
{

    private static final String API_KEY = "AIzaSyBeY9bgjH9ECea9SNQiOVZbcMlC83mzSmY";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private double _latitude;
    private double _longitude;
    private double _radius;

    public GooglePlaces()
    {
    }

    public static HttpRequestFactory createRequestFactory(HttpTransport httptransport)
    {
        return httptransport.createRequestFactory(new HttpRequestInitializer() {

            public void initialize(HttpRequest httprequest)
            {
                GoogleHeaders googleheaders = new GoogleHeaders();
                googleheaders.setApplicationName("AndroidHive-Places-Test");
                httprequest.setHeaders(googleheaders);
                httprequest.addParser(new JsonHttpParser(new JacksonFactory()));
            }

        });
    }

    public PlaceDetails getPlaceDetails(String s)
        throws Exception
    {
        PlaceDetails placedetails;
        try
        {
            HttpRequest httprequest = createRequestFactory(HTTP_TRANSPORT).buildGetRequest(new GenericUrl("https://maps.googleapis.com/maps/api/place/details/json?"));
            
            httprequest.getUrl().put("key", "AIzaSyBeY9bgjH9ECea9SNQiOVZbcMlC83mzSmY");
            httprequest.getUrl().put("reference", s);
            httprequest.getUrl().put("sensor", "false");
            placedetails = (PlaceDetails)httprequest.execute().parseAs(com.prayertimes.qibla.appsourcehub.map.PlaceDetails.class);
        }
        catch(HttpResponseException httpresponseexception)
        {
            Log.e("Error in Perform Details", httpresponseexception.getMessage());
            throw httpresponseexception;
        }
        return placedetails;
    }

    public PlacesList search(double d, double d1, double d2, String s)
        throws Exception
    {
        _latitude = d;
        _longitude = d1;
        _radius = d2;
        HttpRequest httprequest;
        PlacesList placeslist;
        try
        {
            httprequest = createRequestFactory(HTTP_TRANSPORT).buildGetRequest(new GenericUrl("https://maps.googleapis.com/maps/api/place/search/json?"));
            httprequest.getUrl().put("key", "AIzaSyBeY9bgjH9ECea9SNQiOVZbcMlC83mzSmY");
            httprequest.getUrl().put("location", (new StringBuilder(String.valueOf(_latitude))).append(",").append(_longitude).toString());
            httprequest.getUrl().put("radius", Double.valueOf(_radius));
            LogUtils.d((new StringBuilder(String.valueOf(_latitude))).append(" lat ").append(_longitude).toString());
            httprequest.getUrl().put("sensor", "false");
            if(s != null)
            	httprequest.getUrl().put("types", s);
            placeslist = (PlacesList)httprequest.execute().parseAs(com.prayertimes.qibla.appsourcehub.map.PlacesList.class);
            Log.d("Places Status", (new StringBuilder()).append(placeslist.status).toString());
            return placeslist;
        }
        catch(HttpResponseException httpresponseexception)
        {
            Log.e("Error:", httpresponseexception.getMessage());
            return null;
        }
    }

}
