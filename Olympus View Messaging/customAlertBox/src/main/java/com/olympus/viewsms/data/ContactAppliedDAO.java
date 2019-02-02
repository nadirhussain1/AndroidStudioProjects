package com.olympus.viewsms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.olympus.viewsms.model.ContactApplied;
import com.olympus.viewsms.util.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class ContactAppliedDAO {
	private DBAdapter dbAdapter;
	private Context context;
	
	public ContactAppliedDAO(Context context) {
		super();
		this.context = context;
		this.dbAdapter = DBAdapter.getDBAdapterInstance(context);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.i(context.getClass().getSimpleName(), e.getMessage());
		}
	}
	
	private ContactApplied createContactAppliedObject(ArrayList<String> list)
	{
		ContactApplied s = new ContactApplied();
		try {
			s.setId(Integer.parseInt(list.get(0)));
			s.setName(list.get(1));
			s.setNumber(list.get(2));
			s.setTheme_id(Integer.parseInt(list.get(3)));
		} catch (Exception e) {
			Log.i(context.getClass().getSimpleName(), e.getMessage());
		}
		return s;
	}

	public int getThemeIdByNumber(String num) {
        ArrayList<ContactApplied> out = new ArrayList<ContactApplied>();
		dbAdapter.openDataBase();
        String query = "SELECT * FROM contacts_applied where number='"+num+"'";
		Cursor cursor = dbAdapter.selectCursorFromDB(query, null);
        if(cursor.moveToFirst()){
           return cursor.getInt(3);
        }
        return 0;

	}
	
	public ArrayList<ContactApplied> getAll(int theme_id) {
		ArrayList<ContactApplied> out = new ArrayList<ContactApplied>();
		dbAdapter.openDataBase();
		String query = "SELECT * FROM contacts_applied where theme_id="+theme_id;
		ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);
		dbAdapter.close();
		for (int i = 0; i < stringList.size(); i++) 
			out.add(createContactAppliedObject(stringList.get(i)));
		return out;
	}
	public long addContactApplied(ContactApplied n) {
		long id;
		ContentValues values = new ContentValues();
		values.put("name", n.getName());
		values.put("number", Utils.format(n.getNumber()));
		values.put("theme_id", n.getTheme_id());
		dbAdapter.openDataBase();
		id = dbAdapter.insertRecordsInDB("contacts_applied", null, values);
		dbAdapter.close();
		return id;
	}
	
	public void updateContactApplied(ContactApplied n) {
		ContentValues values = new ContentValues();
		values.put("name", n.getName());
		values.put("number", n.getNumber());
		values.put("theme_id", n.getTheme_id());
		dbAdapter.openDataBase();
		dbAdapter.updateRecordInDB("contacts_applied", values, "id = ?", new String[] { n.getId()+""});
		dbAdapter.close();
	}

	public void deleteContactApplied(int id) {
		dbAdapter.openDataBase();
		dbAdapter.deleteRecordInDB("contacts_applied", "id = ?",new String[] { id+""});
		dbAdapter.close();
	}
	
	public void deleteContactApplied(String name,String number) {
		dbAdapter.openDataBase();
		dbAdapter.deleteRecordInDB("contacts_applied", "name = ? and number=?",new String[] { name+"",number+""});
		dbAdapter.close();
	}
	
	public void deleteAllContactApplied() {
		dbAdapter.openDataBase();
		dbAdapter.deleteRecordInDB("contacts_applied", null,null);
		dbAdapter.close();
	}
	public void deleteAllContactApplied(int theme_id) {
		dbAdapter.openDataBase();
		dbAdapter.deleteRecordInDB("contacts_applied", "theme_id = ?",new String[] { theme_id+""});
		dbAdapter.close();
	}
}
