package veripark.billcalculator.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nadirhussain on 02/10/2015.
 */
public class CalPreferences {

    private static final String PREF_NAME = "BillAppPref";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";


    private static CalPreferences calPreferences = null;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor prefEditor = null;

    public static CalPreferences getInstance(Context context) {
        if (calPreferences == null) {
            calPreferences = new CalPreferences(context);
        }
        return calPreferences;
    }

    private CalPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
    }

    public void updateFirstTimeFlag() {
        prefEditor.putBoolean(IS_FIRST_TIME, false);
        prefEditor.commit();
    }
    public boolean getISFirstTime(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME,true);
    }
}
