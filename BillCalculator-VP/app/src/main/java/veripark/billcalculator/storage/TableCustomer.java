package veripark.billcalculator.storage;

import android.database.sqlite.SQLiteDatabase;

public class TableCustomer {
    public static final String TABLE_NAME="table_customer";

    public  static final  String COL_ID="_id";
    public  static final  String COL_CUSTOMER_NUMBER="col_customer_number";
    public  static final  String COL_READING="col_reading_value";



    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COL_CUSTOMER_NUMBER + " TEXT not null unique , "
                + COL_READING + " INTEGER )");
    }
}
