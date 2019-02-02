package patagonia;

import android.content.SharedPreferences;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;

import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by rod on 5/8/16.
 */
@ParseClassName("DownloadEvent")
public class DownloadEvent extends ParseObject {

    private static final String LOG_TAG = DownloadEvent.class.getSimpleName();
    private static final String PATAGONIA_CHECK_ALREADY_DONE_PREFS_KEY = "patagoniaCheckAlreadyDonePrefsKey";

    public DownloadEvent() {}

    public static void registerEvent(UserBook userBook, Date downloadDate) {
        DownloadEvent event = new DownloadEvent();
        event.put("user", userBook.getParseObject("user"));
        event.put("book", userBook.getParseObject("book"));
        event.put("downloadDate", downloadDate);
        event.put("os", "android");
        String myDeviceModel = android.os.Build.MODEL;
        event.put("deviceInfo", myDeviceModel);
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, "Success registering download event");
                } else {
                    Log.e(LOG_TAG, "Error registrgin download event", e);
                }
            }
        });
    }

    public static void executeCheckDownloadEventsTask(LibraryService libraryService, SharedPreferences prefs) {
        Boolean checkDone = prefs.getBoolean(PATAGONIA_CHECK_ALREADY_DONE_PREFS_KEY, false);
        if (!checkDone) {
            Log.d(LOG_TAG, "Checking download events");
            CheckDownloadEventsTask checkEventsTask = new CheckDownloadEventsTask(libraryService);
            checkEventsTask.execute();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PATAGONIA_CHECK_ALREADY_DONE_PREFS_KEY, true).apply();
        }
    }

    private static void registerEvent(ParseUser user, ParseBook book, Date downloadDate) {
        DownloadEvent event = new DownloadEvent();
        event.put("user", user);
        event.put("book", book);
        event.put("os", "android");
        event.put("downloadDate", downloadDate);
        String myDeviceModel = android.os.Build.MODEL;
        event.put("deviceInfo", myDeviceModel);
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, "Success registering download event");
                } else {
                    Log.e(LOG_TAG, "Error registrgin download event", e);
                }
            }
        });
    }

    public static void checkEvents(LibraryService libraryService) {
        QueryResult<LibraryBook> allByTitle = libraryService.findAllByLastRead("");
        for(int i = 0; i < allByTitle.getSize(); i = i+1) {
            LibraryBook book = allByTitle.getItemAt(i);
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (book.isDownloaded() && (currentUser != null)) {
                ParseQuery<ParseBook> query = ParseQuery.getQuery(ParseBook.class);
                query.fromLocalDatastore();
                query.ignoreACLs();
                query.whereEqualTo("title", book.getTitle());
                query.getFirstInBackground(new GetCallback<ParseBook>() {
                    @Override
                    public void done(ParseBook parseBook, ParseException e) {
                        if (e == null) {
                            try {
                                registerEvent(currentUser, parseBook, book.getDownloadDate());
                            } catch (URISyntaxException registerEventException) {
                                Log.e(LOG_TAG, "Error registering on checking events", registerEventException);
                            }
                        }
                    }
                });
            }
        }
    }
}
