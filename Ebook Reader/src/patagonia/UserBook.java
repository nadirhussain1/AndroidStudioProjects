package patagonia;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by rod on 5/8/16.
 */
@ParseClassName("UserBook")
public class UserBook extends ParseObject {
    public UserBook() {}

    public Boolean haveCredits() { return getCredits() > 0; }

    public int getCredits() { return getInt("credits"); }
    public void setCredits(int credits) { put("credits", credits); }

    public void consumeCredits(Date downloadDate) {
        setCredits(getCredits() - 1);
        DownloadEvent.registerEvent(this, downloadDate);
        this.saveInBackground();
    }

    public interface UserBookInitCreditsCallback {
        public void done(UserBook newUserBook, ParseException e);
    }

    public static void initCredits(ParseUser user, ParseBook book, UserBookInitCreditsCallback callback) {
        UserBook userBook = new UserBook();
        userBook.put("user", user);
        userBook.put("book", book);
        userBook.setCredits(100);
        userBook.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(userBook, e);
            }
        });
    }
}
