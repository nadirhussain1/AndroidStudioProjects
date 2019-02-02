package com.olympus.viewsms.data;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.olympus.viewsms.model.Theme;

import java.io.IOException;
import java.util.ArrayList;

public class ThemeDAO {
	private DBAdapter dbAdapter;
	private Context context;
	
	public ThemeDAO(Context context) {
		super();
		this.context = context;
		this.dbAdapter = DBAdapter.getDBAdapterInstance(context);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.i(context.getClass().getSimpleName(), e.getMessage());
		}
	}
	
	private Theme createThemeObject(ArrayList<String> list)
	{
		Theme s = new Theme(context);
		try {
			s.setId(Integer.parseInt(list.get(0)));
			s.setName(list.get(1));
			s.setPrice(Float.parseFloat(list.get(2)));
			s.setIs_paid(Integer.parseInt(list.get(3)));
			s.setProduct_id(list.get(4));
			s.setIs_coming_soon(Integer.parseInt(list.get(5)));
			s.setCtitle(list.get(6));
			s.setCtext(list.get(7));
			s.setL(Integer.parseInt(list.get(8)));
			s.setR(Integer.parseInt(list.get(9)));
			s.setT(Integer.parseInt(list.get(10)));
			s.setB(Integer.parseInt(list.get(11)));
			s.setH1(Integer.parseInt(list.get(12)));
			s.setH2(Integer.parseInt(list.get(13)));
			s.setTitle_layout(Integer.parseInt(list.get(14)));
			s.setL2(Integer.parseInt(list.get(15)));
			s.setR2(Integer.parseInt(list.get(16)));
			s.setTheme_type(Integer.parseInt(list.get(17)));
			s.setButton_bottom(Integer.parseInt(list.get(18)));
			s.setButton_width(Integer.parseInt(list.get(19)));
			s.setIs_button_divider(Integer.parseInt(list.get(20)));
			s.setContent_width(Integer.parseInt(list.get(21)));
			s.setContent_height(Integer.parseInt(list.get(22)));
			s.setContent_left(Integer.parseInt(list.get(23)));
			s.setContent_top(Integer.parseInt(list.get(24)));
			s.setTheme_height(Integer.parseInt(list.get(25)));	
			s.setReply_box_width(Integer.parseInt(list.get(26)));
			s.setReply_color(list.get(27));
		} catch (Exception e) {
			Log.i(context.getClass().getSimpleName(), e.getMessage());
		}
		return s;
	}

	public ArrayList<Theme> getAll() {
		ArrayList<Theme> out = new ArrayList<Theme>();
		dbAdapter.openDataBase();
		String query = "SELECT * FROM themes ORDER BY id ASC";
		ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);
		dbAdapter.close();
		for (int i = 0; i < stringList.size(); i++)
			out.add(createThemeObject(stringList.get(i)));
		return out;
	}
	
	public ArrayList<Theme> getAvailableTheme() {
		ArrayList<Theme> out = new ArrayList<Theme>();
		dbAdapter.openDataBase();
		String query = "SELECT * FROM themes where is_paid=1 ORDER BY id ASC";
		ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);
		dbAdapter.close();
		for (int i = 0; i < stringList.size(); i++) 
			out.add(createThemeObject(stringList.get(i)));
		return out;
	}
	
	public ArrayList<Theme> getMoreTheme() {
		ArrayList<Theme> out = new ArrayList<Theme>();
		dbAdapter.openDataBase();
		String query = "SELECT * FROM themes where is_paid=0 ORDER BY id ASC";
		ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);
		dbAdapter.close();
		for (int i = 0; i < stringList.size(); i++)
			out.add(createThemeObject(stringList.get(i)));
		return out;
	}
	
	public Theme getByID(int id) {
		dbAdapter.openDataBase();
		String query = "SELECT * FROM themes where id="+id+" ORDER BY id ASC";
		ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);

		if(stringList.size()>0)	return createThemeObject(stringList.get(0));
		return null;
	}
	
	public long addTheme(Theme n) {
		long id;
		ContentValues values = new ContentValues();
		values.put("name", n.getName());
		values.put("price", n.getPrice());
		values.put("is_paid", n.getIs_paid());
		values.put("product_id", n.getProduct_id());
		values.put("coming_soon", n.getIs_coming_soon());
		values.put("ctitle", n.getCtitle());
		values.put("ctext", n.getCtext());
		values.put("l", n.getL());
		values.put("r", n.getR());
		values.put("t", n.getT());
		values.put("b", n.getB());
		values.put("h1", n.getH1());
		values.put("h2", n.getH2());
		values.put("title_layout", n.getTitle_layout());
		values.put("l2", n.getL2());
		values.put("r2", n.getR2());
		values.put("theme_id", n.getTheme_type());
		dbAdapter.openDataBase();
		id = dbAdapter.insertRecordsInDB("themes", null, values);
		dbAdapter.close();
		return id;
	}
	
	/*public void updateTheme(Theme n) {
		ContentValues values = new ContentValues();
		values.put("name", n.getName());
		values.put("price", n.getPrice());
		values.put("is_paid", n.getIs_paid());
		values.put("product_id", n.getProduct_id());
		values.put("coming_soon", n.getIs_coming_soon());
		values.put("ctitle", n.getCtitle());
		values.put("ctext", n.getCtext());
		values.put("l", n.getL());
		values.put("r", n.getR());
		values.put("t", n.getT());
		values.put("b", n.getB());
		values.put("h1", n.getH1());
		values.put("h2", n.getH2());
		Log.i("$$$$$$$$$$$$$$$$$$", n.getTitle_layout()+"");
		values.put("title_layout", n.getTitle_layout());
		values.put("l2", n.getL2());
		values.put("r2", n.getR2());
		dbAdapter.openDataBase();
		dbAdapter.updateRecordInDB("themes", values, "id = ?", new String[] { n.getId()+""});
		dbAdapter.close();
	}*/
	public void updateThemeIs_paid(int id, int is_paid) {
		ContentValues values = new ContentValues();
		values.put("is_paid", is_paid);
		dbAdapter.openDataBase();
		dbAdapter.updateRecordInDB("themes", values, "id = ?", new String[] { id+""});
		dbAdapter.close();
	}

	public void deleteTheme(int id) {
		dbAdapter.openDataBase();
		dbAdapter.deleteRecordInDB("themes", "id = ?",new String[] { id+""});
		dbAdapter.close();
	}
	
	public void deleteAllTheme() {
		dbAdapter.openDataBase();
		dbAdapter.deleteRecordInDB("themes", null,null);
		dbAdapter.close();
	}
}
