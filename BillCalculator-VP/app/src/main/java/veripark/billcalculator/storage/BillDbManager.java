package veripark.billcalculator.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import veripark.billcalculator.models.Category;
import veripark.billcalculator.models.CustomerBill;

public class BillDbManager {

    private static BillDbManager dbManager = null;
    private SQLiteDatabase dbObject = null;


    public static BillDbManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new BillDbManager(context);
        }
        return dbManager;
    }

    private BillDbManager(Context context) {
        BillDbHelper openHelper = new BillDbHelper(context);
        dbObject = openHelper.getWritableDatabase();
    }

    public void closeExistingDatabase() {
        closeConnection();
        dbManager = null;
    }

    private void closeConnection() {
        try {
            if (dbObject != null && dbObject.isOpen()) {
                dbObject.close();
                System.gc();
            }
            SQLiteDatabase.releaseMemory();
        } catch (Exception e) {

        } finally {
            if (dbObject != null && dbObject.isOpen()) {
                SQLiteDatabase.releaseMemory();
                dbObject.close();
            }
        }
    }

    //Add Reading value for customer in database
    public void addReading(CustomerBill customerBill) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCustomer.COL_CUSTOMER_NUMBER, customerBill.getServiceNumber());
        contentValues.put(TableCustomer.COL_READING, customerBill.getReading());

        String where = TableCustomer.COL_CUSTOMER_NUMBER + "=? ";
        String[] args = new String[]{customerBill.getServiceNumber()};
        int updatedRow = dbObject.update(TableCustomer.TABLE_NAME, contentValues, where, args);
        if (updatedRow <= 0) {
            dbObject.insert(TableCustomer.TABLE_NAME, null, contentValues);
        }
    }

    public int getPrevReading(String serviceNumber) {
        String where = TableCustomer.COL_CUSTOMER_NUMBER + "=" + serviceNumber;
        Cursor cursor = dbObject.query(TableCustomer.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(TableCustomer.COL_READING));
        }
        return 0;
    }

    public void addCategory(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCategory.COL_CAT_LIMIT, category.getLimit());
        contentValues.put(TableCategory.COL_CAT_RATE, category.getRate());

        dbObject.insert(TableCategory.TABLE_NAME, null, contentValues);
    }

    public void deleteCategory(Category category) {
        String where = TableCategory.COL_ID + "=" + category.getId();
        int rows = dbObject.delete(TableCategory.TABLE_NAME, where, null);
    }

    //Get all categories by ascending order, so that categories with low limit are at top/first
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<Category>();
        String orderBy=TableCategory.COL_CAT_LIMIT+" ASC";
        Cursor cursor = dbObject.query(TableCategory.TABLE_NAME, null, null, null, null, null, orderBy);
        while(cursor.moveToNext()){
            Category category=new Category();
            category.setId(cursor.getInt(0));
            category.setLimit(cursor.getInt(1));
            category.setRate(cursor.getInt(2));

            list.add(category);
        }

        return list;
    }


}
