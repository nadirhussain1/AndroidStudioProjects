package com.prayertimes.qibla.appsourcehub.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAssetHelper extends SQLiteOpenHelper
{
    public static class SQLiteAssetException extends SQLiteException
    {

        public SQLiteAssetException()
        {
        }

        public SQLiteAssetException(String s)
        {
            super(s);
        }
    }


    private static final String ASSET_DB_PATH = "databases";
    private static final String TAG = "SQLiteAssetHelper";
    private String mAssetPath;
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private String mDatabasePath;
    private final android.database.sqlite.SQLiteDatabase.CursorFactory mFactory;
    private int mForcedUpgradeVersion;
    private boolean mIsInitializing;
    private final String mName;
    private final int mNewVersion;
    private String mUpgradePathFormat;

    public SQLiteAssetHelper(Context context, String s, android.database.sqlite.SQLiteDatabase.CursorFactory cursorfactory, int i)
    {
        this(context, s, null, cursorfactory, i);
    }

    public SQLiteAssetHelper(Context context, String s, String s1, android.database.sqlite.SQLiteDatabase.CursorFactory cursorfactory, int i)
    {
        super(context, s, cursorfactory, i);
        mDatabase = null;
        mIsInitializing = false;
        mForcedUpgradeVersion = 0;
        if(i < 1)
        {
            throw new IllegalArgumentException((new StringBuilder("Version must be >= 1, was ")).append(i).toString());
        }
        if(s == null)
        {
            throw new IllegalArgumentException("Database name cannot be null");
        }
        mContext = context;
        mName = s;
        mFactory = cursorfactory;
        mNewVersion = i;
        mAssetPath = (new StringBuilder("databases/")).append(s).toString();
        if(s1 != null)
        {
            mDatabasePath = s1;
        } else
        {
            mDatabasePath = (new StringBuilder(String.valueOf(context.getApplicationInfo().dataDir))).append("/databases").toString();
        }
        mUpgradePathFormat = (new StringBuilder("databases/")).append(s).append("_upgrade_%s-%s.sql").toString();
    }

    private void copyDatabaseFromAssets() throws SQLiteAssetException
    {
        String s;
        String s1;
        boolean flag;
        java.util.zip.ZipInputStream zipinputstream;
        InputStream inputstream1;
        Log.w(TAG, "copying database from assets...");
        s = mAssetPath;
        s1 = (new StringBuilder(String.valueOf(mDatabasePath))).append("/").append(mName).toString();
        try{
        	flag = false;
	        InputStream inputstream3 = mContext.getAssets().open(s);
	        inputstream1 = inputstream3;
	        
        }catch(Exception e){
        	InputStream inputstream;
            try
            {
                inputstream = mContext.getAssets().open((new StringBuilder(String.valueOf(s))).append(".gz").toString());
            }
            catch(IOException ioexception2)
            {
                SQLiteAssetException sqliteassetexception = new SQLiteAssetException((new StringBuilder("Missing ")).append(mAssetPath).append(" file (or .zip, .gz archive) in assets, or target folder not writable").toString());
                sqliteassetexception.setStackTrace(ioexception2.getStackTrace());
                throw sqliteassetexception;
            }
            inputstream1 = inputstream;
            flag = false;
        }
        try
        {
            File file = new File((new StringBuilder(String.valueOf(mDatabasePath))).append("/").toString());
            if(!file.exists())
            {
                file.mkdir();
            }
            if(!flag)
            {
            	Utils.writeExtractedFileToDisk(inputstream1, new FileOutputStream(s1));
            	Log.w(TAG, "database copy complete");
                return;
            }
            zipinputstream = Utils.getFileFromZip(inputstream1);
            if(zipinputstream != null)
            {
            	Utils.writeExtractedFileToDisk(zipinputstream, new FileOutputStream(s1));
            	Log.w(TAG, "database copy complete");
                return;
            }
            throw new SQLiteAssetException("Archive is missing a SQLite database file");
        }
        catch(IOException ioexception3)
        {
            SQLiteAssetException sqliteassetexception1 = new SQLiteAssetException((new StringBuilder("Unable to write ")).append(s1).append(" to data directory").toString());
            sqliteassetexception1.setStackTrace(ioexception3.getStackTrace());
            throw sqliteassetexception1;
        }
    }

    private SQLiteDatabase createOrOpenDatabase(boolean flag)
        throws SQLiteAssetException
    {
        boolean flag1 = (new File((new StringBuilder(String.valueOf(mDatabasePath))).append("/").append(mName).toString())).exists();
        SQLiteDatabase sqlitedatabase = null;
        if(flag1)
        {
            sqlitedatabase = returnDatabase();
        }
        if(sqlitedatabase != null)
        {
            if(flag)
            {
                Log.w(TAG, "forcing database upgrade!");
                copyDatabaseFromAssets();
                sqlitedatabase = returnDatabase();
            }
            return sqlitedatabase;
        } else
        {
            copyDatabaseFromAssets();
            return returnDatabase();
        }
    }

    private void getUpgradeFilePaths(int i, int j, int k, ArrayList arraylist)
    {
        int l;
        int i1;
        if(getUpgradeSQLStream(j, k) != null)
        {
            String s = mUpgradePathFormat;
            Object aobj[] = new Object[2];
            aobj[0] = Integer.valueOf(j);
            aobj[1] = Integer.valueOf(k);
            arraylist.add(String.format(s, aobj));
            l = j - 1;
            i1 = j;
        } else
        {
            l = j - 1;
            i1 = k;
        }
        if(l < i)
        {
            return;
        } else
        {
            getUpgradeFilePaths(i, l, i1, arraylist);
            return;
        }
    }

    private InputStream getUpgradeSQLStream(int i, int j)
    {
        String s = mUpgradePathFormat;
        Object aobj[] = new Object[2];
        aobj[0] = Integer.valueOf(i);
        aobj[1] = Integer.valueOf(j);
        String s1 = String.format(s, aobj);
        InputStream inputstream;
        try
        {
            inputstream = mContext.getAssets().open(s1);
        }
        catch(IOException ioexception)
        {
            Log.w(TAG, (new StringBuilder("missing database upgrade script: ")).append(s1).toString());
            return null;
        }
        return inputstream;
    }

    private SQLiteDatabase returnDatabase()
    {
        SQLiteDatabase sqlitedatabase;
        try
        {
            sqlitedatabase = SQLiteDatabase.openDatabase((new StringBuilder(String.valueOf(mDatabasePath))).append("/").append(mName).toString(), mFactory, 0);
        }
        catch(SQLiteException sqliteexception)
        {
            Log.w(TAG, (new StringBuilder("could not open database ")).append(mName).append(" - ").append(sqliteexception.getMessage()).toString());
            return null;
        }
        return sqlitedatabase;
    }

    public void close()
    {
        synchronized(this){
        	if(mIsInitializing)
            {
                throw new IllegalStateException("Closed during initialization");
            }
            if(mDatabase != null && mDatabase.isOpen())
            {
                mDatabase.close();
                mDatabase = null;
            }
        }
    }

    public SQLiteDatabase getReadableDatabase()
    {
    	SQLiteDatabase sqlitedatabase1 = null;
    	try{
	        synchronized(this){
	        	try{
	    	        if(mDatabase == null || !mDatabase.isOpen()){
	    	        	if(mIsInitializing)
	    	            {
	    	                throw new IllegalStateException("getReadableDatabase called recursively");
	    	            }
	    	            SQLiteDatabase sqlitedatabase2 = getWritableDatabase();
	    	            sqlitedatabase1 = sqlitedatabase2;
	    	        }else
	    	        	sqlitedatabase1 = mDatabase;
	            }catch(SQLiteException sqliteexception){
	            	if(mName == null)
	                {
	                    throw sqliteexception;
	                }
	                Log.e(TAG, (new StringBuilder("Couldn't open ")).append(mName).append(" for writing (will try read-only):").toString(), sqliteexception);
	                SQLiteDatabase sqlitedatabase = null;
	                mIsInitializing = true;
	                String s = mContext.getDatabasePath(mName).getPath();
	                sqlitedatabase = SQLiteDatabase.openDatabase(s, mFactory, 1);
	                if(sqlitedatabase.getVersion() != mNewVersion)
	                {
	                    throw new SQLiteException((new StringBuilder("Can't upgrade read-only database from version ")).append(sqlitedatabase.getVersion()).append(" to ").append(mNewVersion).append(": ").append(s).toString());
	                }
	                onOpen(sqlitedatabase);
	                Log.w(TAG, (new StringBuilder("Opened ")).append(mName).append(" in read-only mode").toString());
	                mDatabase = sqlitedatabase;
	                sqlitedatabase1 = mDatabase;
	                mIsInitializing = false;
	                if(sqlitedatabase != null) {
	                	if(sqlitedatabase != mDatabase)
	                		sqlitedatabase.close();
	                }
	            }
	            return sqlitedatabase1;
	        }
    	}catch(Exception exception){
    		try {
				throw exception;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	 return sqlitedatabase1;
    }

    public SQLiteDatabase getWritableDatabase()
    {
    	SQLiteDatabase sqlitedatabase;
    	SQLiteDatabase sqlitedatabase1;
    	try{
	        synchronized(this){
	        	if(mDatabase == null || !mDatabase.isOpen() || mDatabase.isReadOnly()){
	            	if(mIsInitializing)
	                {
	                    throw new IllegalStateException("getWritableDatabase called recursively");
	                }
	                sqlitedatabase = null;
	                int i;
	                mIsInitializing = true;
	                sqlitedatabase = createOrOpenDatabase(false);
	                i = sqlitedatabase.getVersion();
	                if(i != 0)
	                {
	                	if(i < mForcedUpgradeVersion)
	                    {
	                        sqlitedatabase = createOrOpenDatabase(true);
	                        sqlitedatabase.setVersion(mNewVersion);
	                        i = sqlitedatabase.getVersion();
	                    }
	                }
	                if(i != mNewVersion){
	        	        sqlitedatabase.beginTransaction();
	        	        if(i == 0)
	        	        	onCreate(sqlitedatabase);
	        	        else{
	        	        	if(i > mNewVersion)
	        	                Log.w(TAG, (new StringBuilder("Can't downgrade read-only database from version ")).append(i).append(" to ").append(mNewVersion).append(": ").append(sqlitedatabase.getPath()).toString());
	        	            onUpgrade(sqlitedatabase, i, mNewVersion);
	        	        }
	        	        sqlitedatabase.setVersion(mNewVersion);
	        	        sqlitedatabase.setTransactionSuccessful();
	        	        sqlitedatabase.endTransaction();
	                }
	                onOpen(sqlitedatabase);
	                mIsInitializing = false;
	                SQLiteDatabase sqlitedatabase2 = mDatabase;
	                if(sqlitedatabase2 != null)
	                {
	                	try
	                    {
	                        mDatabase.close();
	                    }
	                    catch(Exception exception3) { }
	                }
	                mDatabase = sqlitedatabase;
	            }else
	            	sqlitedatabase = mDatabase;
	            return sqlitedatabase;
	        }
    	}catch(Exception e){
    		return null;
    	}
    }

    public final void onConfigure(SQLiteDatabase sqlitedatabase)
    {
    }

    public final void onCreate(SQLiteDatabase sqlitedatabase)
    {
    }

    public final void onDowngrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        Iterator iterator;
        String s;
        String s1;
        Log.w(TAG, (new StringBuilder("Upgrading database ")).append(mName).append(" from version ").append(i).append(" to ").append(j).append("...").toString());
        ArrayList arraylist = new ArrayList();
        getUpgradeFilePaths(i, j - 1, j, arraylist);
        if(arraylist.isEmpty())
        {
            Log.e(TAG, (new StringBuilder("no upgrade script path from ")).append(i).append(" to ").append(j).toString());
            throw new SQLiteAssetException((new StringBuilder("no upgrade script path from ")).append(i).append(" to ").append(j).toString());
        }
        Collections.sort(arraylist, new VersionComparator());
        iterator = arraylist.iterator();
        while(iterator.hasNext())
        {
        	s = (String)iterator.next();
            Log.w(TAG, (new StringBuilder("processing upgrade: ")).append(s).toString());
            try{
	            s1 = Utils.convertStreamToString(mContext.getAssets().open(s));
	            if(s1 != null)
	            {
	            	String s2;
	                Iterator iterator1 = Utils.splitSqlScript(s1, ';').iterator();
	                while(iterator1.hasNext()){
	                	s2 = (String)iterator1.next();
	                	if(s2.trim().length() > 0)
	                		sqlitedatabase.execSQL(s2);
	                }	
	            }
            }catch(IOException ioexception){
            	ioexception.printStackTrace();
            }
        }
        Log.w(TAG, (new StringBuilder("Successfully upgraded database ")).append(mName).append(" from version ").append(i).append(" to ").append(j).toString());
    }

    public void setForcedUpgrade()
    {
        setForcedUpgrade(mNewVersion);
    }

    public void setForcedUpgrade(int i)
    {
        mForcedUpgradeVersion = i;
    }

    public void setForcedUpgradeVersion(int i)
    {
        setForcedUpgrade(i);
    }

}
