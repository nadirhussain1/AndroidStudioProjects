package com.brainpixel.valetapp.network;


import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.help.HelpContensResponse;
import com.brainpixel.valetapp.model.login.LoginResponse;
import com.brainpixel.valetapp.model.ride.CompleteRideResponse;
import com.brainpixel.valetapp.model.ride.GetPassengersApiResponse;
import com.brainpixel.valetapp.model.ride.GetRideStatusResponse;
import com.brainpixel.valetapp.model.ride.GetStartedRideResponse;
import com.brainpixel.valetapp.model.route.RouteResultResponse;
import com.brainpixel.valetapp.model.search.PlacesSearchResponse;
import com.brainpixel.valetapp.model.search.SearchDetailResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by nadirhussain on 26/09/2016.
 */

public interface RetroInterface {
    @GET
    Call<PlacesSearchResponse> getSearchPlacePredictionsList(@Url String url, @Query("input") String inputText, @Query("location") String location, @Query("radius") String radius, @Query("key") String apiKey);

    @GET
    Call<SearchDetailResponse> getPlaceDetails(@Url String url, @Query("sensor") boolean sensor, @Query("reference") String reference, @Query("key") String apiKey);

    @GET
    Call<RouteResultResponse> fetchRoutePoints(@Url String url, @Query("origin") String originLoc, @Query("destination") String destLoc, @Query("sensor") boolean sensor, @Query("units") String units, @Query("mode") String mode);

    @Multipart
    @POST("middleware_user/register_web_user")
    Call<GeneralServerResponse> registerWebUser(@Part MultipartBody.Part idCardBody, @Part MultipartBody.Part licenseBody, @Part("u_first_name") RequestBody firstName, @Part("u_last_name") RequestBody lastName, @Part("u_email") RequestBody email, @Part("u_password") RequestBody password,
                                                @Part("u_mobile_no") RequestBody mobileNumber, @Part("role_id") RequestBody role);

    @FormUrlEncoded
    @POST("middleware_user/user_login")
    Call<LoginResponse> login(@Field("u_email") String email, @Field("u_password") String password, @Field("role_id") int role);

    @FormUrlEncoded
    @POST("middleware_user/forgot_password")
    Call<GeneralServerResponse> forgotPassword(@Field("u_email") String email);

    @FormUrlEncoded
    @POST("middleware_user/update_user_location")
    Call<GeneralServerResponse> updateLocation(@Field("u_id") String userId, @Field("r_id") int rideId, @Field("u_latitude") double latitude, @Field("u_longitude") double longitude);

    @FormUrlEncoded
    @POST("middleware_driver/get_user_rides")
    Call<GetPassengersApiResponse> getPassengersList(@Field("r_driver_id") String userId, @Field("r_lat_lng") String latlng);

    @FormUrlEncoded
    @POST("middleware_driver/accept_ride")
    Call<GeneralServerResponse> acceptRide(@Field("r_id") String rId, @Field("r_driver_id") String driverId, @Field("r_rider_id") String riderId);

    @FormUrlEncoded
    @POST("middleware_user/cancel_ride")
    Call<GeneralServerResponse> cancelRide(@Field("r_id") int rId, @Field("cancel_by_id") int riderId);

    @FormUrlEncoded
    @POST("middleware_driver/start_ride")
    Call<GeneralServerResponse> startRide(@Field("r_id") String rId, @Field("r_actual_from_lat_lng") String actualLatLng, @Field("r_actual_from_location_name") String fromLocName);

    @Multipart
    @POST("middleware_driver/upload_ride_image")
    Call<GeneralServerResponse> uploadRideImage(@Part("r_id") RequestBody rId, @Part MultipartBody.Part mapScreenshotBody);

    @FormUrlEncoded
    @POST("middleware_ride/get_ride_status")
    Call<GetRideStatusResponse> getRideStatus(@Field("r_id") String rideId);

    @FormUrlEncoded
    @POST("middleware_driver/get_started_ride")
    Call<GetStartedRideResponse> getStartedRide(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("middleware_driver/complete_ride")
    Call<CompleteRideResponse> completeRide(@Field("r_id") String rideId, @Field("r_actual_to_lat_lng") String actualLatLng, @Field("r_actual_to_location_name") String actualLocName);

    @FormUrlEncoded
    @POST("middleware_user/update_driver_online_status")
    Call<GeneralServerResponse> updateOnlineStatus(@Field("driver_id") String driverId, @Field("online_status") String onlineStatus);

    @FormUrlEncoded
    @POST("middleware_user/get_help_contents")
    Call<HelpContensResponse> getHelpContents(@Field("help_content_parent_id") String parentId);

    @Multipart
    @POST("middleware_user/edit_profile")
    Call<LoginResponse> editUserProfile(@Part MultipartBody.Part idCardBody, @Part MultipartBody.Part licenseBody, @Part MultipartBody.Part userImageBody, @Part("u_id") RequestBody userId, @Part("u_first_name") RequestBody firstName, @Part("u_last_name") RequestBody lastName, @Part("u_password") RequestBody password,
                                        @Part("u_mobile_no") RequestBody mobileNumber);

    @FormUrlEncoded
    @POST("middleware_user/get_profile_details")
    Call<LoginResponse> getProfileDetails(@Field("u_id") String uId);


}
