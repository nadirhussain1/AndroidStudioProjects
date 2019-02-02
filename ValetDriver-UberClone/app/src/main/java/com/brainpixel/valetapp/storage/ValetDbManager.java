package com.brainpixel.valetapp.storage;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.brainpixel.valetapp.model.login.LoggedInUserData;

import java.util.List;

/**
 * Created by nadirhussain on 12/03/2017.
 */

public class ValetDbManager {
    private ValetDbManager() {

    }

    public static void saveLoggedInUser(LoggedInUserData loggedInUserData) {
        loggedInUserData.save();
    }

    public static LoggedInUserData retrieveSavedLoggedInUser(Context context) {
        List<LoggedInUserData> list = new Select()
                .from(LoggedInUserData.class)
                .execute();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public static void deleteLoggedInUser(String uId) {
        new Delete().from(LoggedInUserData.class).where("u_id = ?", uId).execute();
    }
}
