package com.nippt.arabicamharicdictionary.free.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import com.nippt.arabicamharicdictionary.free.widget.AutoCompleteDbAdapter;

public class ItemAutoTextAdapter extends SimpleCursorAdapter {


    private AutoCompleteDbAdapter mDbHelper;
    private boolean isClose = false;


    final static int[] to = new int[]{android.R.id.text1};
    final static String[] from = new String[]{"word1"};

    /**
     * Constructor. Note that no cursor is needed when we create the adapter.
     * Instead, cursors are created on demand when completions are needed for
     * the field. (see
     * {@link ItemAutoTextAdapter#runQueryOnBackgroundThread(CharSequence)}.)
     *
     * @param dbHelper The AutoCompleteDbAdapter in use by the outer class object.
     */
    public ItemAutoTextAdapter(Context ctx, AutoCompleteDbAdapter dbHelper) {
        // Call the SimpleCursorAdapter constructor with a null Cursor.
        super(ctx, android.R.layout.simple_dropdown_item_1line, null, from, to);
        mDbHelper = dbHelper;
    }

    /**
     * Invoked by the AutoCompleteTextView field to get completions for the
     * current input.
     * <p>
     * NOTE: If this method either throws an exception or returns null, the
     * Filter class that invokes it will log an error with the traceback, but
     * otherwise ignore the problem. No choice list will be displayed. Watch
     * those error logs!
     *
     * @param constraint The input entered thus far. The resulting query will search
     *                   for states whose name begins with this string.
     * @return A Cursor that is positioned to the first row (if one exists) and
     * managed by the activity.
     */
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (isClose)
            return null;
        mDbHelper.setClose(isClose);
        if (getFilterQueryProvider() != null) {
            return getFilterQueryProvider().runQuery(constraint);
        }
        if (isClose)
            return null;
        mDbHelper.setClose(isClose);
        Cursor cursor = mDbHelper.getMatchingStates((constraint != null ? constraint.toString() : null));

        return cursor;
    }

    /**
     * Called by the AutoCompleteTextView field to get the text that will be
     * entered in the field after a choice has been made.
     * <p>
     * The cursor, positioned to a particular row in the list.
     *
     * @return A String representing the row's text value. (Note that this
     * specializes the base class return value for this method, which is
     * {@link CharSequence}.)
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String convertToString(Cursor cursor) {
        final int columnIndex = cursor.getColumnIndexOrThrow(DbUtil.COL_WORD1);
        final String str = cursor.getString(columnIndex);
        return str.toLowerCase();
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    /**
     * Called by the AutoCompleteTextView field when a choice has been made by
     * the user.
     *
     * @param listView
     *            The ListView containing the choices that were displayed to the
     *            user.
     * @param view
     *            The field representing the selected choice
     * @param position
     *            The position of the choice within the list (0-based)
     * @param id
     *            The id of the row that was chosen (as provided by the _id
     *            column in the cursor.)
     */

}
