package patagonia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.library.ImportCallback;
import net.nightwhistler.pageturner.library.ImportTask;
import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.library.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import jedi.option.None;
import jedi.option.Option;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

/**
 * Created by rod on 7/10/15.
 */
public class PatagoniaImportTask extends ImportTask {

    private static final Logger LOG = LoggerFactory.getLogger("PatagoniaImportTask");

    public PatagoniaImportTask(Context context, LibraryService libraryService, ImportCallback callBack, Configuration config, boolean copyToLibrary, boolean silent) {
        super(context, libraryService, callBack, config, copyToLibrary, silent);
    }


    @Override
    public Option<Void> doInBackground(File... params) {

        doInBackground(params[0]);

        return new None();
    }

    private void doInBackground(File parent) {
        LOG.debug("Starting import from Patagonia ... ");
        List<File> books = new ArrayList<>();
        //Hay red?
        if (isNetworkAvailable() == true) {
            downloadEpubsInFolder(parent, books);
        } else {
            //No hay red
            //Hay libros?
            QueryResult<LibraryBook> allByTitle = libraryService.findAllByTitle("");
            if (allByTitle.getSize() == 0) {
                //Mensaje
                importFailed = String.format( context.getString(R.string.no_network), parent.getPath());
                return;
            }
        }
    }

    private void refreshEpubsInFolder(File folder, List<File> items) {

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    return true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    return true;
        }

        return false;
    }

    private void downloadEpubsInFolder( File folder, List<File> items) {

        //Consulto a Parse.com - Si no hay red entonces avanzo al Local Storage:
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book");
        query.setLimit(200);

        try {

            List<ParseObject> objects = query.find();

            for (int i = 0; i < objects.size(); i++) {


                //Download the files:
                ParseObject epub = objects.get(i);

                final ParseFile coverFile = epub.getParseFile("cover");

                publishProgress(UPDATE_FOLDER, i + 1);

                //libraryService
                QueryResult<LibraryBook> books = libraryService.findAllByTitle(epub.getString("title"));
                if (books.getSize() > 0) continue; //O sea el Objeto ya existe.

                try {

                    byte[] data = coverFile.getData();

                    try {
                        try {
                            if (importBook( data , epub)) {
                                booksImported++;
                                LOG.info("Added EPUB: " + epub.get("title"));
                            }
                        } catch (OutOfMemoryError oom) {
                            return;
                        }


                    } catch (Exception f) {
                        LOG.debug("EXCEPTION " + f.getMessage());
                    }


                } catch (Exception e) {
                    LOG.debug("EXCEPTION " + e.getMessage());
                }

            }


        } catch (ParseException e) {
            LOG.debug("EXCEPTION " + e.getMessage());
        }

    }

    @Override
    public void doOnPostExecute(Option<Void> none) {

        if ( importFailed != null ) {
            callBack.importFailed(importFailed, silent);
        } else {
            this.callBack.importComplete(booksImported, errors, emptyLibrary, silent);
        }
    }

    protected boolean importBook(byte[] cover, ParseObject epub) {


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

                LOG.debug("Adding sampleLimit ... " + epub.getNumber("sampleLimit").toString());

                importedBook.getMetadata().getOtherProperties().put(new QName("sampleLimit"), epub.getNumber("sampleLimit").toString());

                //Falta el purchased desde el servicio Google Play:
                importedBook.getMetadata().getOtherProperties().put(new QName("purchased"), "0");

                ParseFile epubFileUrl = epub.getParseFile("container");

                libraryService.storeBookByCoverFile(epubFileUrl.getUrl(), cover, importedBook, false, this.copyToLibrary);

                return true;

            } catch (Exception io ) {
                if ( ! isCancelled() ) {
                    LOG.error("Error while reading cover book " , io);
                } else {
                    LOG.info("Ignoring error since we were cancelled", io );
                }
            }

        return false;
    }

}
