package com.nippt.arabicamharicdictionary.free.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nippt.arabicamharicdictionary.free.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "amharic_arabic_db.sqlite";
    public static final int DATABASE_VERSION = 1;
    private Context mycontext;
    private String DB_PATH = "";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mycontext = context;
        DB_PATH = Utils.getDBPath(mycontext);
    }


    private boolean checkDataBase() {
        return (new File((new StringBuilder(String.valueOf(DB_PATH))).append(DATABASE_NAME).toString())).exists();
    }

    private void copyDataBase() throws IOException {
        Log.d("DatabaseDebug", "copyDataBase ");
        InputStream inputstream = mycontext.getAssets().open(DATABASE_NAME);
        FileOutputStream fileoutputstream = new FileOutputStream((new StringBuilder(String.valueOf(DB_PATH))).append(DATABASE_NAME).toString());
        byte abyte0[] = new byte[1024];
        do {
            int i = inputstream.read(abyte0);
            if (i <= 0) {
                fileoutputstream.flush();
                fileoutputstream.close();
                inputstream.close();
                return;
            }
            fileoutputstream.write(abyte0, 0, i);
        } while (true);
    }

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
                throw new Error("Error copying database");
            }
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}