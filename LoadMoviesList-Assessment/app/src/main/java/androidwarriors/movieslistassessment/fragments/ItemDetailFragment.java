package androidwarriors.movieslistassessment.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidwarriors.movieslistassessment.R;
import androidwarriors.movieslistassessment.activities.ItemDetailActivity;
import androidwarriors.movieslistassessment.activities.ItemListActivity;
import androidwarriors.movieslistassessment.model.Cast;
import androidwarriors.movieslistassessment.model.Director;
import androidwarriors.movieslistassessment.model.MovieItem;
import androidwarriors.movieslistassessment.utils.SharedDataManager;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private MovieItem movieItem;
    private ImageView posterImageView, trailerPlayImage;
    private VideoView trailerVideoView;
    private TextView descriptionTextView, releaseYearTextView, ratingTextView, castTextView, directorTextView, genresTextView, reviewTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            int position = getArguments().getInt(ARG_ITEM_ID);
            movieItem = SharedDataManager.moviesList.get(position);

            if(getActivity() instanceof  ItemDetailActivity) {
                ((ItemDetailActivity) getActivity()).setTitle(movieItem.getHeadline());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getActivity())
                .load(movieItem.getCardImages().get(0).getUrl())
                .into(posterImageView);

        descriptionTextView.setText(movieItem.getSynopsis());

        String releaseYearText = getString(R.string.releaseYear) + movieItem.getYear();
        String ratingText = getString(R.string.rating_text);
        if (movieItem.getRating() != null) {
            ratingText = ratingText + movieItem.getRating();
        }else{
            ratingText=ratingText+"NA";
        }
        String genresText = getString(R.string.genresText);
        String directorText = getString(R.string.directorText);
        String castsText = getString(R.string.castText);
        String reviewText = getString(R.string.reviewUrl) + movieItem.getUrl();

        List<String> genres = movieItem.getGenres();
        List<Director> directors = movieItem.getDirectors();
        List<Cast> casts = movieItem.getCast();

        for (int count = 0; count < genres.size(); count++) {
            genresText = genresText + genres.get(count);
            genresText += ",";
        }
        for (int count = 0; count < directors.size(); count++) {
            directorText = directorText + directors.get(count).getName();
            directorText += ",";
        }
        for (int count = 0; count < casts.size(); count++) {
            castsText = castsText + casts.get(count).getName();
            castsText += ",";
        }

        genresText = genresText.substring(0, genresText.length() - 1);
        castsText = castsText.substring(0, castsText.length() - 1);
        directorText = directorText.substring(0, directorText.length() - 1);


        releaseYearTextView.setText(releaseYearText);
        ratingTextView.setText(ratingText);
        castTextView.setText(castsText);
        directorTextView.setText(directorText);
        genresTextView.setText(genresText);
        reviewTextView.setText(reviewText);

        reviewTextView.setMovementMethod(LinkMovementMethod.getInstance());
        trailerPlayImage.setOnClickListener(trailerClickListener);


    }

    private void initViews(View rootView) {
        posterImageView = (ImageView) rootView.findViewById(R.id.posterImageView);
        descriptionTextView = (TextView) rootView.findViewById(R.id.description);
        releaseYearTextView = (TextView) rootView.findViewById(R.id.releaseYearTextView);
        ratingTextView = (TextView) rootView.findViewById(R.id.ratingTextView);
        castTextView = (TextView) rootView.findViewById(R.id.castTextView);
        directorTextView = (TextView) rootView.findViewById(R.id.directorTextView);
        genresTextView = (TextView) rootView.findViewById(R.id.genresTextView);
        reviewTextView = (TextView) rootView.findViewById(R.id.reviewTextView);
        trailerPlayImage = (ImageView) rootView.findViewById(R.id.playButton);
        trailerVideoView = (VideoView) rootView.findViewById(R.id.trailerVideoView);

    }

    private View.OnClickListener trailerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trailerPlayImage.setVisibility(View.GONE);
            playVideo();
        }
    };

    private void playVideo() {
        trailerVideoView.setMediaController(new MediaController(getActivity()));
        trailerVideoView.setVideoPath(movieItem.getVideos().get(0).getUrl());
        trailerVideoView.setOnCompletionListener(videoCompletionListener);
        trailerVideoView.start();
    }

    private MediaPlayer.OnCompletionListener videoCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            trailerPlayImage.setVisibility(View.VISIBLE);
        }
    };
}
