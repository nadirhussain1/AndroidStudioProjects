package patagonia;

import android.net.Uri;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by rod on 3/12/16.
 */
@ParseClassName("Book")
public class ParseBook extends ParseObject {

    public ParseBook() {}

    public static ParseQuery<ParseBook> getQuery() {
        ParseQuery<ParseBook> query = ParseQuery.getQuery(ParseBook.class);
        query.orderByAscending("title");
        query.setLimit(200);
        query.fromLocalDatastore();
        query.ignoreACLs();
        return query;
    }

    public static ParseQuery<ParseBook> getAuthorQuery(String author) {
        ParseQuery<ParseBook> query = ParseQuery.getQuery(ParseBook.class);
        query.whereEqualTo("author", author);
        query.orderByAscending("title");
        query.fromLocalDatastore();
        return query;
    }

    public String getTitle() {
        return getString("title");
    }
    public String getAuthor() { return getString("author"); }
    public String getEISBN() { return getString("eISBN"); }

    public String getCoverUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("s3.amazonaws.com")
                .appendPath(Config.BUCKET_NAME)
                .appendPath("Books")
                .appendPath(getEISBN() + "_cover.jpg");

        return builder.build().toString();
    }

    public static ParseQueryAdapter.QueryFactory<ParseBook> getQueryFactory(){
        return new ParseQueryAdapter.QueryFactory<ParseBook>() {
            @Override
            public ParseQuery<ParseBook> create() {
                return getQuery();
            }
        };
    }

    public void checkDownloadCredits(GetCallback<UserBook> callback) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseQuery<UserBook> query = ParseQuery.getQuery(UserBook.class);
            query.whereEqualTo("user", currentUser);
            ParseQuery<ParseBook> bookQuery = ParseQuery.getQuery(ParseBook.class);
            bookQuery.whereEqualTo("objectId", getObjectId());
            query.whereMatchesQuery("book", bookQuery);
            query.getFirstInBackground(callback);
        }
    }
}
