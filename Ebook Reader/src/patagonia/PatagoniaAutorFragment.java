package patagonia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import net.nightwhistler.pageturner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PatagoniaAutorFragment extends android.support.v4.app.Fragment {

    private final String LOG_TAG = PatagoniaAutorFragment.class.getSimpleName();
    private String mAuthorName;
    private RecyclerView mAuthorBooksRecyclerView;
    private BookAdapter mAdapter;
    public static final String BOOK_SELECTED = "patagonia.patagoniaautorfragment.bookselected";

    public PatagoniaAutorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuthorName = (String) getActivity().getIntent().getSerializableExtra(PatagoniaAutorActivity.EXTRA_PATAGONIA_AUTHOR_NAME);
        getActivity().setTitle(mAuthorName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patagonia_autor, container, false);
        mAuthorBooksRecyclerView = (RecyclerView) view.findViewById(R.id.author_books_recycler_view);
        mAuthorBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.autores_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_biografia:
                showBiografia();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String loadAutorsJson() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("autors.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private String getBio() {
        try {
            JSONArray jsonArray = new JSONArray(loadAutorsJson());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject autorInfo = jsonArray.getJSONObject(i);
                String autorPath = removeAccents(TextUtils.join("_", mAuthorName.split(" ")));
                if (autorInfo.getString("AUTOR").equals(autorPath)) {
                    return autorInfo.getString("BIOGRAFÍA");
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error loading autors json", e);
        }
        return null;
    }

    private String removeAccents(String text) {
        return text == null ? null :
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private void showBiografia() {
        String bio = getBio();
        AlertDialog bioDialog = new
                AlertDialog.Builder(getActivity()).setTitle(mAuthorName).setMessage(bio).setPositiveButton(android.R.string.ok, null).create();
        bioDialog.show();
    }

    public interface OnBookSelectedListener {
        void bookSelected(String objectId);
    }

    private void updateUI() {
        ParseQuery<ParseBook> query = ParseBook.getAuthorQuery(mAuthorName);
        query.findInBackground(new FindCallback<ParseBook>() {
            @Override
            public void done(List<ParseBook> list, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, "Error Getting author books: " + e.getMessage());
                } else {
                    mAdapter = new BookAdapter(list, new OnBookSelectedListener() {
                        @Override
                        public void bookSelected(String objectId) {
                            setSelectedBook(objectId);
                        }
                    });
                    mAuthorBooksRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    public void setSelectedBook(String objectId) {
        Intent intent = new Intent();
        intent.putExtra(BOOK_SELECTED, objectId);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private class BookHolder extends RecyclerView.ViewHolder {
        public TextView mBookNameTextView;

        public BookHolder(View itemView) {
            super(itemView);
            mBookNameTextView = (TextView) itemView;
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<ParseBook> mBooks;
        private OnBookSelectedListener mListener;

        public BookAdapter(List<ParseBook> books, OnBookSelectedListener listener) {
            mBooks = books;
            mListener = listener;
        }

        @Override
        public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new BookHolder(view);
        }

        @Override
        public void onBindViewHolder(BookHolder holder, int position) {
            ParseBook book = mBooks.get(position);
            holder.mBookNameTextView.setText(book.getTitle());
            holder.mBookNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.bookSelected(book.getObjectId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBooks.size();
        }
    }
}
