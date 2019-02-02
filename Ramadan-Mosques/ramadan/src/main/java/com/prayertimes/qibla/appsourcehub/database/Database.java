package com.prayertimes.qibla.appsourcehub.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prayertimes.qibla.appsourcehub.model.QuranScript;

public class Database extends SQLiteAssetHelper
{

    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_VERSES = "verses";

    public Database(Context context, String s)
    {
        super(context, s, null, 1);
    }

    public List getTransliteration(int i)
    {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList arraylist = new ArrayList();
        Cursor cursor = sqlitedatabase.rawQuery((new StringBuilder("select * from verses where sura = ")).append(i).toString(), null);
        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                QuranScript quranscript = new QuranScript();
                quranscript.setSura(cursor.getInt(0));
                quranscript.setAyah(cursor.getInt(1));
                quranscript.setText(cursor.getString(2));
                arraylist.add(quranscript);
                System.out.println("==== IN DB:"+quranscript.text);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return arraylist;
    }
}
