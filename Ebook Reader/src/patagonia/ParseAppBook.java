package patagonia;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by rod on 3/12/16.
 */
@ParseClassName("AppBook")
public class ParseAppBook extends ParseObject  {
    public ParseAppBook() {}

    public static ParseQueryAdapter.QueryFactory<ParseAppBook> getQueryFactory(ParseObject app){
        return new ParseQueryAdapter.QueryFactory<ParseAppBook>() {
            @Override
            public ParseQuery<ParseAppBook> create() {
                return getQuery(app);
            }
        };
    }

    public ParseBook getBook() {
        return (ParseBook)getParseObject("book");
    }

    public static ParseQuery<ParseAppBook> getQuery(ParseObject app) {
        ParseQuery<ParseAppBook> query = ParseQuery.getQuery("AppBook");
        query.whereEqualTo("app", app);
        query.include("book");
        query.setLimit(200);
        query.orderByAscending("order");
        query.fromLocalDatastore();
        return query;
    }
}
