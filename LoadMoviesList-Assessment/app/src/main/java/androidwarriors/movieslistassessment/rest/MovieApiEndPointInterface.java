package androidwarriors.movieslistassessment.rest;

import java.util.List;

import androidwarriors.movieslistassessment.model.MovieItem;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by nadirhussain on 16/07/2016.
 */


public interface MovieApiEndPointInterface {
    @GET("vodassets/showcase.json")
    Call<List<MovieItem>> getMoviesList();
}
