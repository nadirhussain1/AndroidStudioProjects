package patagonia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.nightwhistler.pageturner.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.text.Normalizer.Form;

/**
 * A placeholder fragment containing a simple view.
 */
public class PatagoniaAutoresFragment extends android.support.v4.app.Fragment {

    private RecyclerView mAuthorsRecyclerView;
    private AuthorAdapter mAdapter;
    private PatagoniaStore mStore;
    private final String LOG_TAG = PatagoniaAutoresFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patagonia_autores, container, false);
        mAuthorsRecyclerView = (RecyclerView) view.findViewById(R.id.authors_recycler_view);
        mAuthorsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        updateUI();
        return view;
    }



    private void updateUI() {
        mStore = new PatagoniaStore();
        mStore.setAppObjectListener(new PatagoniaStore.appObjectListener() {
            @Override
            public void appObjectCallback(ParseObject app) {
                ParseQuery<ParseAppBook> query = ParseAppBook.getQuery(app);
                query.findInBackground(new FindCallback<ParseAppBook>() {
                    @Override
                    public void done(List<ParseAppBook> list, ParseException e) {
                        if (e != null) {
                            Log.e(LOG_TAG, "Error Getting local app books: " + e.getMessage());
                        } else {
                            List<String> authors = new ArrayList<String>();
                            for (ParseAppBook appBook : list) {
                                ParseBook book = appBook.getBook();
                                authors.add(book.getAuthor());
                            }
                            Set<String> authorsSet = new HashSet<String>(authors);
                            List<String> authorsList = new ArrayList<String>(authorsSet);
                            mAdapter = new AuthorAdapter(authorsList);
                            mAuthorsRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
            }
        });
        mStore.getAppObject(getActivity().getPackageName());
    }

    private class AuthorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mNameTextView;
        public ImageView mThumbView;

        public AuthorHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView) itemView.findViewById(R.id.autor_name_textview);
            mThumbView = (ImageView) itemView.findViewById(R.id.autor_thumb);
        }

        @Override
        public void onClick(View v) {
            String authorName = (String) mNameTextView.getText();
            Intent intent = PatagoniaAutorActivity.newIntent(getActivity(), authorName);
            getActivity().startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private class AuthorAdapter extends RecyclerView.Adapter<AuthorHolder> {
        private List<String> mAuthors;

        public AuthorAdapter(List<String> authors) {
            mAuthors = authors;
        }

        @Override
        public AuthorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.autor_grid, parent, false);
            return new AuthorHolder(view);
        }

        private String autorPath(String authorName) {
            String autorPath = removeAccents(TextUtils.join("_", authorName.split(" "))) + ".jpg";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("s3.amazonaws.com")
                    .appendPath("eltigre-contentdelivery-mobilehub-1997480338")
                    .appendPath("Autores").appendPath(autorPath);
            return builder.build().toString();
        }

        private String removeAccents(String text) {
            return text == null ? null :
                    Normalizer.normalize(text, Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        }

        @Override
        public void onBindViewHolder(AuthorHolder holder, int position) {
            String authorName = mAuthors.get(position);
            holder.mNameTextView.setText(authorName);
            Glide.with(getActivity())
                    .load(autorPath(authorName))
                    .thumbnail(0.3f)
                    .centerCrop()
                    .crossFade()
                    .into(holder.mThumbView);
        }

        @Override
        public int getItemCount() {
            return mAuthors.size();
        }
    }
}
