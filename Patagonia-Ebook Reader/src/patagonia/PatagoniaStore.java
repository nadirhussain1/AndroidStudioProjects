package patagonia;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rod on 3/16/16.
 */
public class PatagoniaStore {

    private appObjectListener mAppObjectListener;
    private appBookObjectsListener mAppBooksObjectsListener;
    private BookObjectsListener mBooksObjectsListener;
    private ParseObject mApp;
    private final String LOG_TAG = PatagoniaStore.class.getSimpleName();

    public interface appObjectListener {
        void appObjectCallback(ParseObject app);
    }

    public interface appBookObjectsListener {
        void appBookObjectsCallBack(Boolean shouldReload);
    }

    private interface SyncBooksListener {
        void syncBooksCallBack(Boolean shouldReload);
    }

    public interface BookObjectsListener {
        void bookObjectsCallBack(Boolean shouldReload);
    }

    public void setAppObjectListener(appObjectListener appObjectListener) {
        this.mAppObjectListener = appObjectListener;
    }

    public void setAppBooksObjectsListener(appBookObjectsListener appBooksObjectsListener) {
        this.mAppBooksObjectsListener = appBooksObjectsListener;
    }

    public void setBooksObjectsListener(BookObjectsListener booksObjectsListener) {
        this.mBooksObjectsListener = booksObjectsListener;
    }

    public void getAppObject(String packageName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("App");
        query.whereEqualTo("appleId", packageName);
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    getAppObjectFromParse(packageName);
                } else if (mAppObjectListener != null) {
                    Log.d(LOG_TAG, "App object retrieved from local datastore");
                    mApp = object;
                    mAppObjectListener.appObjectCallback(object);
                }
            }
        });
    }
    
    private void getAppObjectFromParse(String packageName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("App");
        query.whereEqualTo("appleId", packageName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.e(LOG_TAG, "Error retrieving app object: " + e.getMessage());
                } else if (mAppObjectListener != null) {
                    Log.d(LOG_TAG, "App object retrieved from Parse");
                    object.pinInBackground();
                    mApp = object;
                    mAppObjectListener.appObjectCallback(object);
                }
            }
        });
    }

    private void syncBooks(List<ParseBook> books, SyncBooksListener listener) {
        ParseQuery<ParseBook> query = ParseBook.getQuery();
        query.findInBackground(new FindCallback<ParseBook>() {
            @Override
            public void done(List<ParseBook> localList, ParseException e) {
                if (e == null) {

                    if (!books.equals(localList)) { // Should reload
                        for (ParseBook book : localList) {
                            if (!books.contains(book)) {
                                book.unpinInBackground();
                            }
                        }
                        listener.syncBooksCallBack(true);
                    } else { // No Need to reload
                        listener.syncBooksCallBack(false);
                    }
                } else {
                    Log.d(LOG_TAG, "Error Getting local app books: " + e.getMessage());
                    listener.syncBooksCallBack(false);
                }
            }
        });
    }

    public void getAppBooksFromParse() {
        ParseQuery<ParseAppBook> query = ParseQuery.getQuery("AppBook");
        query.whereEqualTo("app", mApp);
        query.include("book");
        query.setLimit(200);
        query.orderByAscending("order");
        query.findInBackground(new FindCallback<ParseAppBook>() {
            public void done(List<ParseAppBook> appBooks, ParseException e) {
                if (e == null) {
                    /*syncBooks(appBooks, new SyncBooksListener() {
                        @Override
                        public void syncBooksCallBack(Boolean shouldReload) {
                            for (ParseAppBook appBook : filteredAppBooks) {
                                Log.d(LOG_TAG, "Retrieved book: " + appBook.getBook().getTitle());
                                appBook.pinInBackground();
                            }

                            if (mAppBooksObjectsListener != null) {
                                mAppBooksObjectsListener.appBookObjectsCallBack(shouldReload);
                            }
                        }
                    });*/
                } else {
                    Log.d(LOG_TAG, "Error Getting app books: " + e.getMessage());
                    if (mAppBooksObjectsListener != null) {
                        mAppBooksObjectsListener.appBookObjectsCallBack(false);
                    }
                }
            }
        });
    }

    public void getBooksFromParse() {
        ParseQuery<ParseBook> query = ParseQuery.getQuery(ParseBook.class);
        query.orderByAscending("title");
        query.setLimit(200);
        query.findInBackground(new FindCallback<ParseBook>() {
            @Override
            public void done(List<ParseBook> books, ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, "Retrieved " + books.size() + " books");
                    syncBooks(books, new SyncBooksListener() {
                        @Override
                        public void syncBooksCallBack(Boolean shouldReload) {
                            ParseObject.pinAllInBackground(books);
                            if (mBooksObjectsListener != null) {
                                mBooksObjectsListener.bookObjectsCallBack(shouldReload);
                            }
                        }
                    });
                } else {
                    Log.d(LOG_TAG, "Error getting books: " + e.getMessage());
                    if (mBooksObjectsListener != null) {
                        mBooksObjectsListener.bookObjectsCallBack(false);
                    }
                }
            }
        });
    }
}
