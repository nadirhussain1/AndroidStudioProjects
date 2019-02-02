package veripark.billcalculator.storage;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by nadirhussain on 12/11/2015.
 */
public class TableCategory {

    public static final String TABLE_NAME="table_category";

    public  static final  String COL_ID="_id";
    public  static final  String COL_CAT_LIMIT="col_cat_limit";
    public  static final  String COL_CAT_RATE="col_cat_rate";



    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COL_CAT_LIMIT + " INTEGER , "
                + COL_CAT_RATE + " INTEGER )");
    }
}
