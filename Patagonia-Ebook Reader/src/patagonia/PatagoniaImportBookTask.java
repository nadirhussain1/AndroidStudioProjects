package patagonia;

import android.os.AsyncTask;
import android.util.Log;

import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;

import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by rod on 3/13/16.
 */
public class PatagoniaImportBookTask extends AsyncTask<ParseBook, Void, Void> {

    private final String LOG_TAG = PatagoniaImportBookTask.class.getSimpleName();
    protected LibraryService libraryService;

    public PatagoniaImportBookTask(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    protected Void doInBackground(ParseBook... params) {
        if (params.length == 0) {
            return null;
        }

        ParseBook book = params[0];
        QueryResult<LibraryBook> books = libraryService.findAllByTitle(book.getTitle());
        if (books.getSize() > 0) {
            Log.d(LOG_TAG, "Book already addedd: " + book.getTitle());
            return null;
        }

        Log.d(LOG_TAG,"Should add: " + book.getTitle());
        if (importBook(book)) {
            Log.d(LOG_TAG, "Added EPUB: " + book.getTitle());
        }
        return null;
    }

    protected boolean importBook(ParseBook epub) {
        try {

            // read epub file
            EpubReader epubReader = new PatagoniaEpubReader();

            Book importedBook = new Book();

            importedBook.getMetadata().addTitle(epub.getString("title"));

            //Author
            importedBook.getMetadata().getAuthors().add(new Author(epub.getString("author")));

            importedBook.getMetadata().getDescriptions().clear();

            List<String> descriptions = new ArrayList<>();
            descriptions.add(epub.getString("description"));

            importedBook.getMetadata().setDescriptions(descriptions);
            Log.d("LibraryDatabase", "Inside Import book");
            libraryService.storeBookByCoverFile("http://" + epub.getString("title"), null, importedBook, false, false);

            return true;

        } catch (Exception io ) {
            if ( ! isCancelled() ) {
                Log.e(LOG_TAG, "Error while reading cover book ", io);
            } else {
                Log.i(LOG_TAG, "Ignoring error since we were cancelled", io);
            }
        }

        return false;
    }
}
