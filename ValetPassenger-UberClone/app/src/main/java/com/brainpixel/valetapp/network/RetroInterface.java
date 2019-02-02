package com.brainpixel.valetapp.network;


import com.brainpixel.valetapp.model.GeneralServerResponse;
import com.brainpixel.valetapp.model.help.HelpContensResponse;
import com.brainpixel.valetapp.model.login.LoginResponse;
import com.brainpixel.valetapp.model.ride.GetDriverLocationResponse;
import com.brainpixel.valetapp.model.ride.GetDriversResponse;
import com.brainpixel.valetapp.model.ride.GetRideStatusResponse;
import com.brainpixel.valetapp.model.ride.GetStartedRideResponse;
import com.brainpixel.valetapp.model.ride.RequestRideResponse;
import com.brainpixel.valetapp.model.route.RouteResultResponse;
import com.brainpixel.valetapp.model.search.PlacesSearchResponse;
import com.brainpixel.valetapp.model.search.SearchDetailResponse;
import com.brainpixel.valetapp.model.settings.GeneralSettingsResponse;
import com.brainpixel.valetapp.model.trips.TripsListResponse;

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

    @FormUrlEncoded
    @POST("middleware_user/register_web_user")
    Call<GeneralServerResponse> registerWebUser(@Field("u_first_name") String firstName, @Field("u_last_name") String lastName, @Field("u_email") String email, @Field("u_password") String password,
                                                @Field("u_mobile_no") String mobileNumber, @Field("role_id") int role);

    @FormUrlEncoded
    @POST("middleware_user/user_login")
    Call<LoginResponse> login(@Field("u_email") String email, @Field("u_password") String password, @Field("role_id") int role);

    @FormUrlEncoded
    @POST("middleware_user/forgot_password")
    Call<GeneralServerResponse> forgotPassword(@Field("u_email") String email);

    @FormUrlEncoded
    @POST("middleware_user/get_driver_in_radious")
    Call<GetDriversResponse> getDriversListInRadius(@Field("u_latitude") double latitude, @Field("u_longitude") double longitude, @Field("u_id") String userId);


    @POST("middleware_user/general_settings")
    Call<GeneralSettingsResponse> getGeneralSettings();

    @FormUrlEncoded
    @POST("middleware_user/request_for_ride")
    Call<RequestRideResponse> requestRide(@Field("r_rider_id") String riderId, @Field("r_from_lat_lng") String fromLatLng, @Field("r_to_lat_lng") String toLatLng, @Field("r_from_location_name") String fromLocName, @Field("r_to_location_name") String toLocName);


    @FormUrlEncoded
    @POST("middleware_user/schedule_ride")
    Call<GeneralServerResponse> scheduleRide(@Field("r_rider_id") String riderId, @Field("r_from_lat_lng") String fromLatLng, @Field("r_to_lat_lng") String toLatLng,
                                             @Field("r_from_location_name") String fromLocName, @Field("r_to_location_name") String toLocName, @Field("ride_schedule_time") String rideScheduleTime);

    @FormUrlEncoded
    @POST("middleware_user/cancel_ride")
    Call<GeneralServerResponse> cancelRide(@Field("r_id") String rideId, @Field("cancel_by_id") String userId);

    @FormUrlEncoded
    @POST("middleware_driver/get_driver_location")
    Call<GetDriverLocationResponse> getDriverLatestLocation(@Field("driver_id") String driverId);

    @FormUrlEncoded
    @POST("middleware_ride/get_ride_status")
    Call<GetRideStatusResponse> getRideStatus(@Field("r_id") String rideId);

    @FormUrlEncoded
    @POST("middleware_passenger/get_started_ride")
    Call<GetStartedRideResponse> getStartedRide(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("middleware_user/get_help_contents")
    Call<HelpContensResponse> getHelpContents(@Field("help_content_parent_id") String parentId);

    @Multipart
    @POST("middleware_user/edit_profile")
    Call<LoginResponse> editUserProfile(@Part MultipartBody.Part userImageBody, @Part("u_id") RequestBody userId, @Part("u_first_name") RequestBody firstName, @Part("u_last_name") RequestBody lastName, @Part("u_password") RequestBody password,
                                        @Part("u_mobile_no") RequestBody mobileNumber);


    @FormUrlEncoded
    @POST("middleware_passenger/get_passenger_history")
    Call<TripsListResponse> getPastTripsList(@Field("user_id") String userId, @Field("no_of_records") int noOfRecords, @Field("pageno") int pageNumber);


    @FormUrlEncoded
    @POST("middleware_user/get_upcoming_trips")
    Call<TripsListResponse> getUpcomingTrips(@Field("passenger_id") String userId);

    @FormUrlEncoded
    @POST("middleware_user/update_scheduled_ride")
    Call<GeneralServerResponse> updateRideScheduledTime(@Field("r_ride_id") String rideId, @Field("ride_new_schedule_time") String newDateTimeString);
}
