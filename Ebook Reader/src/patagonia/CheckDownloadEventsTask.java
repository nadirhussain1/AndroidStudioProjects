package patagonia;

import android.os.AsyncTask;

import net.nightwhistler.pageturner.library.LibraryService;

/**
 * Created by rod on 5/8/16.
 */
public class CheckDownloadEventsTask extends AsyncTask<Void, Void, Void> {

    private LibraryService mLibraryService;

    public CheckDownloadEventsTask(LibraryService libraryService) {
        this.mLibraryService = libraryService;
    }

    protected Void doInBackground(Void... params) {
        DownloadEvent.checkEvents(mLibraryService);
        return null;
    }
}
