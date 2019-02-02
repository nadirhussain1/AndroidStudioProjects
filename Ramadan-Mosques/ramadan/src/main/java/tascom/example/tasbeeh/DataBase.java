package tascom.example.tasbeeh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DataBase
{
    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            sqlitedatabase.execSQL("CREATE TABLE if not exists table_tasbeehlist (key_id INTEGER PRIMARY KEY AUTOINCREMENT ,title TEXT, description TEXT,countvalue INTEGER);");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
        }

        DatabaseHelper(Context context)
        {
            super(context, "Tasbeeh", null, 1);
        }
    }


    public static final String DATABASE_NAME = "Tasbeeh";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_COUNT = "countvalue";
    public static final String KEY_DESCRIBTION = "description";
    public static final String KEY_ID = "key_id";
    public static final String KEY_TITLE = "title";
    public static final String TABLE_INSERTTASBEEH = "table_inserttasbeeh";
    public static final String TABLE_TASBEEHLIST = "table_tasbeehlist";
    private static final String TABLE_TASBEEHLIST_CREATE = "CREATE TABLE if not exists table_tasbeehlist (key_id INTEGER PRIMARY KEY AUTOINC" +
"REMENT ,title TEXT, description TEXT,countvalue INTEGER);"
;
    private static SQLiteDatabase mDb;
    private static DatabaseHelper mDbHelper;
    private Context mCtx;

    public DataBase(Context context)
    {
        mCtx = context;
    }

    public long checkTasbeehList(int i, String s, String s1, String s2)
    {
        String s3 = (new StringBuilder("SELECT * FROM table_tasbeehlist WHERE key_id=")).append(i).toString();
        if(mDb.rawQuery(s3, null).getCount() <= 0)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("key_id", Integer.valueOf(i));
            contentvalues.put("title", s);
            contentvalues.put("description", s1);
            contentvalues.put("countvalue", s2);
            mDb.insert("table_tasbeehlist", null, contentvalues);
            return 1L;
        } else
        {
            return 2L;
        }
    }

    public void close()
    {
        if(mDbHelper != null)
        {
            mDbHelper.close();
        }
    }

    public long deleteTasbeehList(int i)
    {
        try
        {
            mDb.delete("table_tasbeehlist", (new StringBuilder("key_id=")).append(i).toString(), null);
        }
        catch(SQLException sqlexception)
        {
            Log.d("Error Message", sqlexception.getMessage());
            return 0L;
        }
        return 1L;
    }

    public List fetchAllFeedList(int i)
    {
        ArrayList arraylist = new ArrayList();
        Cursor cursor = mDb.query("table_tasbeehlist", new String[] {
            "key_id", "title", "description", "countvalue"
        }, null, null, null, null, "key_id ASC ");
        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                DataBaseModel databasemodel = new DataBaseModel();
                databasemodel.setKey_id(cursor.getInt(0));
                databasemodel.setTitle(cursor.getString(1));
                databasemodel.setDescription(cursor.getString(2));
                databasemodel.setCount(cursor.getInt(3));
                arraylist.add(databasemodel);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return arraylist;
    }

    public List gettasbeeh(int i)
    {
        ArrayList arraylist = new ArrayList();
        String s = (new StringBuilder("SELECT * FROM table_tasbeehlist WHERE key_id=")).append(i).toString();
        Cursor cursor = mDb.rawQuery(s, null);
        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                DataBaseModel databasemodel = new DataBaseModel();
                databasemodel.setKey_id(cursor.getInt(0));
                databasemodel.setTitle(cursor.getString(1));
                databasemodel.setDescription(cursor.getString(2));
                databasemodel.setCount(cursor.getInt(3));
                arraylist.add(databasemodel);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return arraylist;
    }

    public long inserTasbeehList(String s, String s1, int i)
    {
        try
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("title", s);
            contentvalues.put("description", s1);
            contentvalues.put("countvalue", Integer.valueOf(i));
            mDb.insert("table_tasbeehlist", null, contentvalues);
        }
        catch(SQLException sqlexception)
        {
            Log.d("Error Message", sqlexception.getMessage());
            return (long)i;
        }
        return 1L;
    }

    public DataBase openDataBase()
        throws SQLException
    {
        if(mDbHelper == null)
        {
            mDbHelper = new DatabaseHelper(mCtx);
        }
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
}
