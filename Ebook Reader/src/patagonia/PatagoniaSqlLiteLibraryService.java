package patagonia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import net.nightwhistler.pageturner.library.LibraryBook;
import net.nightwhistler.pageturner.library.LibraryDatabaseHelper;
import net.nightwhistler.pageturner.library.QueryResult;
import net.nightwhistler.pageturner.library.SqlLiteLibraryService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jedi.option.Option;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;

import static jedi.option.Options.none;
import static jedi.option.Options.some;

/**
 * Created by leonardoabarrientosc on 15-07-15.
 */
public class PatagoniaSqlLiteLibraryService extends SqlLiteLibraryService {

    private final String LOG_TAG = PatagoniaSqlLiteLibraryService.class.getSimpleName();

    public PatagoniaSqlLiteLibraryService(Context context){
        super(context);
    }

    public void storeBookByCoverFile(String fileNameUrl, byte[] cover, Book book, boolean updateLastRead, boolean copyFile ) throws IOException {
        Log.d("LibraryDatabase", "Inside storeBookByCoverFile PatagoniaLibraryService");

        Metadata metaData = book.getMetadata();

        String authorFirstName = "Unknown author";
        String authorLastName = "";

        if ( metaData.getAuthors().size() > 0 ) {
            authorFirstName = metaData.getAuthors().get(0).getFirstname();
            authorLastName = metaData.getAuthors().get(0).getLastname();
        }

        String description = "";

        if ( ! metaData.getDescriptions().isEmpty() ) {
            description = metaData.getDescriptions().get(0);
        }

        String title = book.getTitle();

        int sampleLimit = -1;

        /*if ( ! metaData.getOtherProperties().get(new QName("sampleLimit")).isEmpty() ) {
            sampleLimit = Integer.parseInt(book.getMetadata().getOtherProperties().get(new QName("sampleLimit")).toString());
        }*/

        int purchased = 0;

        /*if ( ! metaData.getOtherProperties().get(new QName("purchased")).isEmpty() ) {
            purchased = Integer.parseInt(book.getMetadata().getOtherProperties().get(new QName("purchased")).toString());
        }*/

        Log.d(LOG_TAG, "Storing " + title);

        this.helper.storeNewBook(fileNameUrl,
                authorFirstName, authorLastName, title,
                description, cover,
                updateLastRead, sampleLimit, purchased);

    }

    public void updatePurchased(String fileName, int purchased){
        helper.updatePurchased (fileName, purchased);
    }

    public void updatePurchasedBySku(String sku, int purchased){
        helper.updatePurchasedBySku(sku, purchased);
    }

    public void updateFilename( String fileNameOld, String fileNameNew ) {
        helper.updateFilename( fileNameOld, fileNameNew );
    }

    @Override
    public QueryResult<LibraryBook> findUnread(String filter) {
        return helper.findAllKeyedBy(
                LibraryDatabaseHelper.Field.a_last_name,
                LibraryDatabaseHelper.Order.ASC, filter);

    }

    @Override
    public QueryResult<LibraryBook> findAllByLastRead(String filter) {
        return helper.findAllKeyedBy(
                LibraryDatabaseHelper.Field.a_last_name,
                LibraryDatabaseHelper.Order.ASC, filter);
    }

    @Override
    public QueryResult<LibraryBook> findAllByAuthor(String filter) {
        return helper.findAllKeyedBy(
                LibraryDatabaseHelper.Field.a_last_name,
                LibraryDatabaseHelper.Order.ASC, filter );

    }

    @Override
    public QueryResult<LibraryBook> findAllByLastAdded(String filter) {
        return helper.findAllKeyedBy(
                LibraryDatabaseHelper.Field.a_last_name,
                LibraryDatabaseHelper.Order.ASC, filter);
    }

    protected Option<byte[]> resizeImage( byte[] input ) {

        if ( input == null ) {
            return none();
        }

        Bitmap bitmapOrg = BitmapFactory.decodeByteArray(input, 0, input.length);

        if ( bitmapOrg == null ) {
            return none();
        }

        int height = bitmapOrg.getHeight();
        int width = bitmapOrg.getWidth();
        int newHeight = 600;

        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleHeight, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                width, height, matrix, true);

        bitmapOrg.recycle();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);

        resizedBitmap.recycle();

        return some(bos.toByteArray());

    }

}
