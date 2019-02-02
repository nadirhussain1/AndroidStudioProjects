package com.olympus.viewsms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.olympus.viewsms.model.Theme;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
/**
 * 
 * @author nvn-itpro
 *
 */
public class DBAdapter extends SQLiteOpenHelper {

	private static int DATABASE_VERSION =3;
	private static int OLD_VERSION=0;

	public static String DB_PATH = "";
	public static final String DB_NAME = "com.olympus.viewsms.sqlite";
	private SQLiteDatabase dataBase=null;
	private final Context context;
	private static DBAdapter connection;
	private boolean shouldUpgrade=false;
	private boolean shouldCopy=false;

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		shouldCopy=true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		OLD_VERSION=oldVersion;
		shouldUpgrade=true;
	}
	private void doUpgrade(){
		openDataBase();

		shouldUpgrade=false;
		if(OLD_VERSION==1){
			String sql = "ALTER TABLE themes ADD COLUMN theme_type INTEGER";
			dataBase.execSQL(sql);
		}else if(DATABASE_VERSION==3){
			String sql = "ALTER TABLE themes ADD COLUMN button_bottom INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN button_width INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN is_button_divider INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN content_width INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN content_height INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN content_left INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN content_top INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN theme_height INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN reply_box_width INTEGER";
			dataBase.execSQL(sql);
			sql = "ALTER TABLE themes ADD COLUMN reply_color TEXT";
			dataBase.execSQL(sql);
			
			addNewThemes();
		}

		close();
	}

	@Override
	public synchronized void close() {
		if (dataBase != null)
		{
			dataBase.close();
			dataBase=null;
			SQLiteDatabase.releaseMemory();
		}
		super.close();
	}
	private void addNewThemes(){
		String ctitle="#000000";
		String ctext="#000000";
		String replyColor="#707070";
		
		Theme adventureTimeTheme=new Theme(context,22,"Adventure Time Byyaaww!",3.97f, 0,"adv_time_by",0,ctitle, ctext,replyColor,15,15,110,0,60,73,4,2,20,-1,1,260,60,5,10,390,170);
		addTheme(adventureTimeTheme);

		ctitle="#FFFFFF";
		ctext="#FDE931";
		replyColor="#484848";
		
		Theme southParkTheme=new Theme(context,23,"South Park",3.97f, 0,"south_park",0,ctitle, ctext,replyColor,5,5,75,0,68,50,5,2,4,-1,1,260,60,5,50,390,200);
		addTheme(southParkTheme);
		
		ctitle="#343637";
		ctext="#000000";
		replyColor="#484848";
		
		Theme dumbWaysTheme=new Theme(context,24,"Dumb Ways to Die",3.97f, 0,"dumb_ways_to_die",0,ctitle, ctext,replyColor,10,10,50,20,68,50,5,2,4,-1,1,260,100,10,75,390,200);
		addTheme(dumbWaysTheme);
		
		ctitle="#FFFFFF";
		ctext="#146379";
		replyColor="#FFFFFF";
		Theme elegantBlue=new Theme(context,25,"Elegant Blue",1.97f, 0,"elegant_blue",0,ctitle, ctext,replyColor,10,10,50,20,60,50,4,2,15,-1,1,260,100,10,30,390,220);
		addTheme(elegantBlue);
		
		
		Theme elegantgreen=new Theme(context,26,"Elegant Green",1.97f, 0,"elegant_green",0,ctitle, ctext,replyColor,10,10,50,20,60,50,4,2,15,-1,1,260,100,10,30,390,220);
		addTheme(elegantgreen);
		
		ctext="#FFFFFF";
		Theme elegantPurple=new Theme(context,27,"Elegant Purple",1.97f, 0,"elegant_purple",0,ctitle, ctext,replyColor,10,10,50,20,60,50,4,2,15,-1,1,260,100,10,30,390,220);
		addTheme(elegantPurple);
		
		ctitle="#FFFFFF";
		ctext="#FFFFFF";
		replyColor="#484848";
		Theme titanFallTheme=new Theme(context,28,"Titanfall",3.97f, 0,"titanfall",0,ctitle, ctext,replyColor,10,10,90,20,100,50,6,2,15,-1,1,260,150,10,30,390,200);
		addTheme(titanFallTheme);
	}
	public static synchronized DBAdapter getDBAdapterInstance(Context context) {
		if (connection == null) {
			Log.d("Database","Connection Null");
			connection = new DBAdapter(context);
		}
		return connection;
	}
	public static DBAdapter getExistingConnection(){
		return connection;
	}
	public  void destroyDatabaseInstance(){
		close();
		connection=null;
	}

	public void createDataBase() throws IOException {
		boolean dbExist=checkDataBase();
		boolean isFreshInstall=false;
		dataBase=getReadableDatabase();
		close();
		
		if(shouldCopy && !dbExist){
			try {
				isFreshInstall=true;
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
		if(shouldUpgrade || isFreshInstall){
			doUpgrade();
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = context.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();

		shouldCopy=false;
	}

	public void openDataBase() throws SQLException {
		if(dataBase==null){
			String myPath = DB_PATH + DB_NAME;
			dataBase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
		}
	}

	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return dataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(
			String tableName, String[] tableColumns, String whereClase,
			String whereArgs[], String groupBy, String having, String orderBy) {

		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = dataBase.query(tableName, tableColumns, whereClase,
				whereArgs, groupBy, having, orderBy);
		if (cursor.moveToFirst()) {
			do {
				list = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					list.add(cursor.getString(i));
				}
				retList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return retList;

	}

	public long insertRecordsInDB(String tableName, String nullColumnHack,ContentValues initialValues) {
		return dataBase.insert(tableName, nullColumnHack, initialValues);
	}

	public boolean updateRecordInDB(String tableName,
			ContentValues initialValues, String whereClause, String whereArgs[]) {
		return dataBase
				.update(tableName, initialValues, whereClause, whereArgs) > 0;
	}

	public int updateRecordsInDB(String tableName, ContentValues initialValues,
			String whereClause, String whereArgs[]) {
		return dataBase
				.update(tableName, initialValues, whereClause, whereArgs);
	}

	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return dataBase.delete(tableName, whereClause, whereArgs);
	}

	public Cursor selectCursorFromDB(String query, String[] selectionArgs) {
		return dataBase.rawQuery(query, selectionArgs);
	}

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(String query,
			String[] selectionArgs) {
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = dataBase.rawQuery(query, selectionArgs);
		if (cursor.moveToFirst()) {
			do {
				list = new ArrayList<String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					list.add(cursor.getString(i));
				}
				retList.add(list);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return retList;
	}
	public long addTheme(Theme n) {
		long id;
		ContentValues values = new ContentValues();
		values.put("id", n.getId());
		values.put("name", n.getName());
		values.put("price", n.getPrice());
		values.put("is_paid", n.getIs_paid());
		values.put("product_id", n.getProduct_id());
		values.put("coming_soon", n.getIs_coming_soon());
		values.put("ctitle", n.getCtitle());
		values.put("ctext", n.getCtext());
		values.put("reply_color", n.getReply_color());
		values.put("l", n.L);
		values.put("r", n.R);
		values.put("t", n.T);
		values.put("b", n.B);
		values.put("h1", n.h1);
		values.put("h2", n.h2);
		values.put("title_layout", n.getTitle_layout());
		values.put("l2", n.L2);
		values.put("r2", n.R2);
		values.put("theme_type", n.getTheme_type());
		values.put("button_bottom", n.button_bottom);
		values.put("button_width", n.button_width);
		values.put("is_button_divider", n.getIs_button_divider());
		values.put("content_width", n.content_width);
		values.put("content_height", n.content_height);
		values.put("content_left", n.content_left);
		values.put("content_top", n.content_top);
		values.put("theme_height", n.theme_height);
		values.put("reply_box_width", n.getReply_box_width());
		id =insertRecordsInDB("themes", null, values);
		return id;
	}

}
