package com.nippt.arabicamharicdictionary.free.widget;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nippt.arabicamharicdictionary.free.database.DatabaseHelper;
import com.nippt.arabicamharicdictionary.free.database.DbUtil;

/**
 * Simple database access helper class.
 *
 * @author Dan Breslau
 */
public class AutoCompleteDbAdapter {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Activity mActivity;
    private boolean isClose = false;
    private int langSelected = 0;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param activity the Activity that is using the database
     */
    public AutoCompleteDbAdapter(Activity activity,int langSelected) {
        this.mActivity = activity;
        this.langSelected = langSelected;
        mDbHelper = new DatabaseHelper(activity);
        mDb = mDbHelper.getWritableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Return a Cursor that returns all states (and their state capitals) where
     * the state name begins with the given constraint string.
     *
     * @param constraint Specifies the first letters of the states to be listed. If
     *                   null, all rows are returned.
     * @return Cursor managed and positioned to the first state, if found
     * @throws SQLException if query fails
     */
    public Cursor getMatchingStates(String constraint) throws SQLException {
        String queryString = DbUtil.getFetchAllResultsQuery(langSelected);
        if (constraint != null) {
            constraint = constraint.trim() + "%";
            queryString += " WHERE word1 LIKE ?";
        }
        String params[] = {constraint};

        if (constraint == null) {
            // If no parameters are used in the query,
            // the params arg must be null.
            params = null;
        }
        if (constraint.trim().equals("")) {
            return null;
        }
        try {
            if (isClose)
                return null;
            Cursor cursor = mDb.rawQuery(queryString, params);
            if (cursor != null) {
                this.mActivity.startManagingCursor(cursor);
                cursor.moveToFirst();
                return cursor;
            }
        } catch (SQLException e) {
            Log.e("AutoCompleteDbAdapter", e.toString());
            throw e;
        }

        return null;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

}
