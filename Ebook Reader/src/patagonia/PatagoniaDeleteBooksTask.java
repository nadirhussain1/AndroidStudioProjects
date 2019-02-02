package patagonia;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;

import java.util.List;

/**
 * Created by rod on 3/14/16.
 */
public class PatagoniaDeleteBooksTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = PatagoniaDeleteBooksTask.class.getSimpleName();
    protected LibraryService libraryService;
    public PatagoniaAsyncResponse mDelegate = null;

    public PatagoniaDeleteBooksTask(LibraryService libraryService, PatagoniaAsyncResponse delegate) {
        this.libraryService = libraryService;
        this.mDelegate = delegate;
    }

    public interface PatagoniaAsyncResponse {
        void deleteFinish();
    }

    protected Void doInBackground(Void... params) {

        ParseQuery<ParseBook> query = ParseBook.getQuery();
        query.findInBackground(new FindCallback<ParseBook>() {
            @Override
            public void done(List<ParseBook> objects, ParseException e) {
                if (e == null) {
                    ParseObject.unpinAllInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(LOG_TAG, "Success deleting local parse books");
                            } else {
                                Log.e(LOG_TAG, "Error deleting local parse books", e);
                            }
                        }
                    });
                } else {
                    Log.e(LOG_TAG, "Error getting local parse books for deletion", e);
                }
            }
        });

        QueryResult<LibraryBook> allByTitle = this.libraryService.findAllByTitle("");
        if ( allByTitle.getSize() > 0) {
            int index = 0;
            while (index < allByTitle.getSize()) {
                LibraryBook book = allByTitle.getItemAt(index);
                libraryService.deleteBook(book.getFileName());
                Log.d(LOG_TAG, "Book Deleted: " + book.getTitle());
                index++;
            }
        }
        allByTitle.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mDelegate.deleteFinish();
        super.onPostExecute(aVoid);
    }
}
