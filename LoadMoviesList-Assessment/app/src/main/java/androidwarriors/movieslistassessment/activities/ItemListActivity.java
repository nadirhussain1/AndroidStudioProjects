package androidwarriors.movieslistassessment.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import androidwarriors.movieslistassessment.R;
import androidwarriors.movieslistassessment.adapters.MoviesListAdapter;
import androidwarriors.movieslistassessment.model.MovieItem;
import androidwarriors.movieslistassessment.rest.MovieApiEndPointInterface;
import androidwarriors.movieslistassessment.rest.MoviesApiRestClient;
import androidwarriors.movieslistassessment.utils.SharedDataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    private boolean mTwoPane; // Whether or not the activity is in two-pane mode, i.e. running on a tabletdevice.
    private ProgressDialog progressDialog =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        setUpToolBar();
        loadMoviesApiData();

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;   // The detail container view will be present only in the large-screen layouts (res/values-w900dp).
        }
    }
    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }
    private void initRecyclerView(){
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }
    private void loadMoviesApiData(){
        //if(!CommonUtil.isInternetConnected(this)){
            showAlertDialog(getString(R.string.no_connection_dialog_msg));
            return;
        //}
//        progressDialog = ProgressDialog.show(this,getString(R.string.loading_dialog_title),getString(R.string.loading_dialog_message), true);
//        fetchMoviesList();
    }
    private void fetchMoviesList(){
        MovieApiEndPointInterface apiService = MoviesApiRestClient.getClient().create(MovieApiEndPointInterface.class);

        Call<List<MovieItem>> call = apiService.getMoviesList();
        call.enqueue(new Callback<List<MovieItem>>() {
            @Override
            public void onResponse(Call<List<MovieItem>>call, Response<List<MovieItem>> response) {
                SharedDataManager.moviesList = response.body();
                progressDialog.cancel();
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<List<MovieItem>>call, Throwable t) {
                progressDialog.cancel();
                showAlertDialog(getString(R.string.error_dialog_message));
            }
        });
    }
    private void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)

                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Neutral", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getString(R.string.no_connection_dialog_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ItemListActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new MoviesListAdapter(SharedDataManager.moviesList,this,mTwoPane));
    }


}
