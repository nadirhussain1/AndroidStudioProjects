package com.nippt.arabicamharicdictionary.free.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nippt.arabicamharicdictionary.model.Word;

/**
 * Created by nadirhussain on 31/01/2017.
 */

public final class DbUtil {
    public static final String ENGLISH_DICT_TABLE = "English_Am_Ar_DB";
    public static final String ARABIC_DICT_TABLE = "ArabicDB";
    public static final String AMHARIC_DICT_TABLE = "AmharicDB";

    public static final String HISTORY_TABLE = "HISTORY";
    public static final String FAVOURITE_TABLE = "FAVOURITE";

    public static final String COL_ID = "_id";
    public static final String COL_WORD1 = "word1";
    public static final String COL_WORD2 = "word2";
    public static final String COL_WORD3 = "word3";

    public static final String COL_WORDID = "WordId";
    public static final String COL_ENG_WORD = "EnglishWord";
    public static final String COL_ARABIC_WORD = "ArabicWord";
    public static final String COL_AMHARIC_WORD = "AmharicWord";

    public static final String COL_FAVOURITE = "favourite";
    public static final String COL_ADDED = "Added";
    public static final String COL_HISTORY = "history";

    private DbUtil() {

    }

    public static Cursor fetchFavourites(SQLiteDatabase mDb, String update) {
        String queryString = "SELECT" + " " + COL_WORDID + "," + COL_ENG_WORD + "," + COL_ARABIC_WORD + "," + COL_AMHARIC_WORD + " FROM " +
                HISTORY_TABLE + " WHERE favourite = 1 AND DATETIME('" + update + "')<=favourite_day order by favourite_day desc";
        String[] params = {};

        Cursor cursor = mDb.rawQuery(queryString, params);

        return cursor;
    }

    public static Cursor fetchHistory(SQLiteDatabase mDb, String update) {
        String queryString = "SELECT" + " " + COL_WORDID + "," + COL_ENG_WORD + "," + COL_ARABIC_WORD + "," + COL_AMHARIC_WORD + "," + COL_ADDED + "," + COL_FAVOURITE + " FROM " +
                HISTORY_TABLE + " WHERE DATETIME('" + update + "')<=Added  AND  history = 1 order by Added desc";
        String[] params = {};

        Cursor cursor = mDb.rawQuery(queryString, params);
        return cursor;
    }

    public static Word fetchWord(SQLiteDatabase mDb, int id, int langSelected) {
        String queryString = getFetchAllResultsQuery(langSelected);
        queryString = queryString + " WHERE " + COL_ID + " = " + id;

        Cursor cursor = mDb.rawQuery(queryString, new String[]{});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return getWordFromCursor(cursor,langSelected);
        }
        return null;
    }

    public static String getArabicFetchAllQuery() {
        return "SELECT" + " " + DbUtil.COL_ID + "," + DbUtil.COL_WORD1 + "," + DbUtil.COL_WORD2 + " FROM " + DbUtil.ARABIC_DICT_TABLE;
    }

    public static String getAmharicFetchAllQuery() {
        return "SELECT" + " " + DbUtil.COL_ID + "," + DbUtil.COL_WORD1 + "," + DbUtil.COL_WORD2 + " FROM " + DbUtil.AMHARIC_DICT_TABLE;
    }

    public static String getEnglishFetchAllQuery() {
        return "SELECT" + " " + DbUtil.COL_ID + "," + DbUtil.COL_WORD1 + "," + DbUtil.COL_WORD2 + "," + DbUtil.COL_WORD3 + " FROM " + DbUtil.ENGLISH_DICT_TABLE;
    }

    public static String getFetchAllResultsQuery(int langSelcted) {
        switch (langSelcted) {
            case 0:
                return getEnglishFetchAllQuery();
            case 1:
                return getArabicFetchAllQuery();
            case 2:
                return getAmharicFetchAllQuery();
        }
        return null;
    }


    public static Word getWordFromCursor(Cursor cursor, int langSelected) {
        Word word = new Word();
        word.setWordId(cursor.getInt(0));
        switch (langSelected) {
            case 2:
                word.setAmharicWord(cursor.getString(1));
                word.setArabicWord(cursor.getString(2));
                break;
            case 1:
                word.setAmharicWord(cursor.getString(2));
                word.setArabicWord(cursor.getString(1));
                break;
            case 0:
                word.setEnglishWord(cursor.getString(1));
                word.setAmharicWord(cursor.getString(3));
                word.setArabicWord(cursor.getString(2));
                break;
        }
        return word;
    }
}
