package androidwarriors.movieslistassessment.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidwarriors.movieslistassessment.R;
import androidwarriors.movieslistassessment.activities.ItemDetailActivity;
import androidwarriors.movieslistassessment.activities.ItemListActivity;
import androidwarriors.movieslistassessment.fragments.ItemDetailFragment;
import androidwarriors.movieslistassessment.model.Cast;
import androidwarriors.movieslistassessment.model.Director;
import androidwarriors.movieslistassessment.model.MovieItem;

/**
 * Created by nadirhussain on 16/07/2016.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {
    private  List<MovieItem> movies = new ArrayList<MovieItem>();
    private Context mContext;
    private boolean mTwoPane;

    public MoviesListAdapter(List<MovieItem> items,Context context,boolean isTwoPane) {
        this.movies = items;
        this.mContext = context;
        this.mTwoPane = isTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(movies.get(position).getCardImages().get(0).getUrl())
                .fit()
                .into(holder.posterView);

        String ratingText = mContext.getString(R.string.rating_text);
        if (movies.get(position).getRating() != null) {
            ratingText = ratingText + movies.get(position).getRating();
        }else{
            ratingText=ratingText+"NA";
        }
        String genresText=mContext.getString(R.string.genresText);
        String directorText = mContext.getString(R.string.directorText);
        String castsText = mContext.getString(R.string.castText);

        List<String> genres = movies.get(position).getGenres();
        List<Director> directors = movies.get(position).getDirectors();
        List<Cast> casts = movies.get(position).getCast();

        for(int count=0;count<genres.size();count++){
            genresText=genresText+genres.get(count);
            genresText+=",";
        }
        for(int count=0;count<directors.size();count++){
            directorText=directorText+directors.get(count).getName();
            directorText+=",";
        }
        for(int count=0;count<casts.size();count++){
            castsText=castsText+casts.get(count).getName();
            castsText+=",";
        }

        genresText=genresText.substring(0,genresText.length()-1);
        castsText=castsText.substring(0,castsText.length()-1);
        directorText=directorText.substring(0,directorText.length()-1);



        holder.titleView.setText(movies.get(position).getHeadline());
        holder.ratingView.setText(ratingText);
        holder.genresView.setText(genresText);
        holder.directorView.setText(directorText);
        holder.castView.setText(castsText);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, position);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    ((ItemListActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID,position);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public  ImageView posterView;
        public  TextView titleView,ratingView,genresView,directorView,castView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            posterView = (ImageView) view.findViewById(R.id.MovieCoverImageView);
            titleView = (TextView) view.findViewById(R.id.movieTitleView);
            ratingView = (TextView) view.findViewById(R.id.ratingTextView);
            genresView = (TextView) view.findViewById(R.id.genresTextView);
            directorView = (TextView) view.findViewById(R.id.directorTextView);
            castView = (TextView) view.findViewById(R.id.castTextView);
        }


    }
}