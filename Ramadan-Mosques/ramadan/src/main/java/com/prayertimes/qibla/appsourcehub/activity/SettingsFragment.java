package com.prayertimes.qibla.appsourcehub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference;
import com.prayertimes.qibla.appsourcehub.preference.PreferenceFragment;
import com.prayertimes.qibla.appsourcehub.support.AppLocationService;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;
import com.prayertimes.qibla.appsourcehub.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import muslim.prayers.time.R;

/**
 * Created by nadirhussain on 25/08/2016.
 */
public class SettingsFragment extends PreferenceFragment implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int LOCATION_INTENT_CALLED = 1;
    private static final String TAG_ARABIC_FONT = "arabicfont";
    private static final String TAG_ASR_DAYLIGHT = "Asrdaylight";
    private static final String TAG_CITY_AUTO = "change_city_auto";
    private static final String TAG_CITY_MANUAL = "change_city_manual";
    private static final String TAG_DHUHR_DAYLIGHT = "Dhuhrdaylight";
    private static final String TAG_ENGLISH_FONT = "englishfont";
    private static final String TAG_FAJAR_DAYLIGHT = "Fajardaylight";
    private static final String TAG_HIJRI = "hijriday";
    private static final String TAG_IMSAK_DAYLIGHT = "Imsakdaylight";
    private static final String TAG_ISHA_DAYLIGHT = "Ishadaylight";
    private static final String TAG_JURISTIC = "juristic";
    private static final String TAG_LATITUDE = "higherLatitudes";
    private static final String TAG_LATLNG = "latlng";
    private static final String TAG_MAGHRIB_DAYLIGHT = "Maghribdaylight";
    private static final String TAG_METHOD = "method";
    private static final String TAG_NOTIFICATION = "notification";
    private static final String TAG_RINGTONE = "ringtone";
    private static final String TAG_RINGTONE_TITLE = "ringtone_title";
    private static final String TAG_TIME = "timeformat";
    private static final String TAG_TIMEZONE = "timezone";
    AppLocationService appLocationService;
    MaterialEditTextPreference asd;
    Preference city_automatic;
    Preference city_manual;
    MaterialEditTextPreference dhd;
    android.content.SharedPreferences.Editor editor;
    MaterialEditTextPreference fjd;
    MaterialEditTextPreference imd;
    MaterialEditTextPreference isd;
    ListPreference lar;
    ListPreference len;
    ListPreference ln;
    ListPreference lph;
    ListPreference lpj;
    ListPreference lpl;
    ListPreference lpm;
    ListPreference lpt;
    ListPreference lt;
    Preference ltln;
    MaterialEditTextPreference mhd;
    RingtonePreference rp;
    SharedPreferences sp;
    Utils parentActivity;

    public void getLocation() {
        Location location;
        appLocationService = new AppLocationService(getActivity());
        location = appLocationService.getLocation();
        LogUtils.i((new StringBuilder("location ")).append(location).toString());
        if (location == null) {
            parentActivity.SavePref(parentActivity.USER_CITY, "");
            parentActivity.SavePref(parentActivity.USER_STATE, "");
            parentActivity.SavePref(parentActivity.USER_STREET, "");
            parentActivity.SavePref(parentActivity.USER_COUNTRY, "");
            return;
        }
        try {
            Iterator iterator;
            double d = location.getLatitude();
            double d1 = location.getLongitude();
            parentActivity.saveString(parentActivity.USER_LAT, String.valueOf(d));
            parentActivity.saveString(parentActivity.USER_LNG, String.valueOf(d1));
            ltln.setSummary((new StringBuilder(String.valueOf(d))).append(",").append(d1).toString());
            iterator = (new Geocoder(getActivity(), Locale.ENGLISH)).getFromLocation(Double.parseDouble(parentActivity.loadString(parentActivity.USER_LAT)), Double.parseDouble(parentActivity.loadString(parentActivity.USER_LNG)), 1).iterator();
            while (iterator.hasNext()) {
                Address address;
                address = (Address) iterator.next();
                LogUtils.i((new StringBuilder(String.valueOf(address.getLocality()))).append(" city ").append(address.getAdminArea()).append(" state ").append(address.getCountryName()).toString());
                if (address != null) {
                    String s = address.getLocality();
                    String s1 = address.getCountryName();
                    String s2 = address.getAdminArea();
                    String s3 = address.getSubLocality();
                    parentActivity.SavePref(parentActivity.USER_CITY, s);
                    parentActivity.SavePref(parentActivity.USER_STATE, s2);
                    parentActivity.SavePref(parentActivity.USER_COUNTRY, s1);
                    parentActivity.SavePref(parentActivity.USER_STREET, s3);
                    city_automatic.setSummary(getString(R.string.lblcitycountry, new Object[]{
                            s, s2, s1
                    }));
                    city_manual.setSummary(getString(R.string.lblcitycountry, new Object[]{
                            s, s2, s1
                    }));
                }
            }
        } catch (Exception e) {
            parentActivity.SavePref(parentActivity.USER_CITY, "");
            parentActivity.SavePref(parentActivity.USER_STATE, "");
            parentActivity.SavePref(parentActivity.USER_STREET, "");
            parentActivity.SavePref(parentActivity.USER_COUNTRY, "");
        }
    }

    public void onActivityResult(int i, int j, Intent intent) {
        LogUtils.i((new StringBuilder(" on activity result")).append(j).append(" ").append(intent).toString());
        if (j == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
                rp.setSummary(ringtone.getTitle(getActivity()));
                editor = sp.edit();
                editor.putString("ringtone", uri.toString());
                editor.putString("ringtone_title", ringtone.getTitle(getActivity()));
                editor.commit();
            }
            return;
        }
        if (j == 1) {
            try {
                getLocation();
                return;
            } catch (Exception exception1) {
                return;
            }
        }
        try {
            getLocation();
            return;
        } catch (Exception exception) {
            return;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        parentActivity = (Utils) getActivity();
        addPreferencesFromResource(R.xml.adjust);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rp = (RingtonePreference) findPreference("ringtone");
        lpm = (ListPreference) findPreference("method");
        lpj = (ListPreference) findPreference("juristic");
        lpl = (ListPreference) findPreference("higherLatitudes");
        lpl = (ListPreference) findPreference("higherLatitudes");
        lpt = (ListPreference) findPreference("timezone");
        lar = (ListPreference) findPreference("arabicfont");
        len = (ListPreference) findPreference("englishfont");
        lph = (ListPreference) findPreference("hijriday");
        lt = (ListPreference) findPreference("timeformat");
        ln = (ListPreference) findPreference("notification");
        imd = (MaterialEditTextPreference) findPreference("Imsakdaylight");
        fjd = (MaterialEditTextPreference) findPreference("Fajardaylight");
        dhd = (MaterialEditTextPreference) findPreference("Dhuhrdaylight");
        asd = (MaterialEditTextPreference) findPreference("Asrdaylight");
        mhd = (MaterialEditTextPreference) findPreference("Maghribdaylight");
        isd = (MaterialEditTextPreference) findPreference("Ishadaylight");
        lpm.setSummary(lpm.getEntry());
        lpj.setSummary(lpj.getEntry());
        lpl.setSummary(lpl.getEntry());
        lph.setSummary(lph.getEntry());
        lar.setSummary(lar.getEntry());
        len.setSummary(len.getEntry());
        lt.setSummary(lt.getEntry());
        ln.setSummary(ln.getEntry());
        imd.setSummary((new StringBuilder(String.valueOf(sp.getString("Imsakdaylight", "0")))).append(" Minutes").toString());
        fjd.setSummary((new StringBuilder(String.valueOf(sp.getString("Fajardaylight", "0")))).append(" Minutes").toString());
        dhd.setSummary((new StringBuilder(String.valueOf(sp.getString("Dhuhrdaylight", "0")))).append(" Minutes").toString());
        asd.setSummary((new StringBuilder(String.valueOf(sp.getString("Asrdaylight", "0")))).append(" Minutes").toString());
        mhd.setSummary((new StringBuilder(String.valueOf(sp.getString("Maghribdaylight", "0")))).append(" Minutes").toString());
        isd.setSummary((new StringBuilder(String.valueOf(sp.getString("Ishadaylight", "0")))).append(" Minutes").toString());
        String as[];
        if (sp.getString("Imsakdaylight", "0").equals("")) {
            save("Imsakdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Imsakdaylight", "0"));
            } catch (NumberFormatException numberformatexception) {
                save("Imsakdaylight");
            }
        }
        if (sp.getString("Fajardaylight", "0").equals("")) {
            save("Fajardaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Fajardaylight", "0"));
            } catch (NumberFormatException numberformatexception1) {
                save("Fajardaylight");
            }
        }
        if (sp.getString("Dhuhrdaylight", "0").equals("")) {
            save("Dhuhrdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Dhuhrdaylight", "0"));
            } catch (NumberFormatException numberformatexception2) {
                save("Dhuhrdaylight");
            }
        }
        if (sp.getString("Asrdaylight", "0").equals("")) {
            save("Asrdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Asrdaylight", "0"));
            } catch (NumberFormatException numberformatexception3) {
                save("Asrdaylight");
            }
        }
        if (sp.getString("Maghribdaylight", "0").equals("")) {
            save("Maghribdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Maghribdaylight", "0"));
            } catch (NumberFormatException numberformatexception4) {
                save("Maghribdaylight");
            }
        }
        if (sp.getString("Ishadaylight", "0").equals("")) {
            save("Ishadaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Ishadaylight", "0"));
            } catch (NumberFormatException numberformatexception5) {
                save("Ishadaylight");
            }
        }
        rp.setSummary(sp.getString("ringtone_title", ""));
        as = TimeZone.getAvailableIDs();
        lpt.setEntries(as);
        lpt.setEntryValues(as);
        sp.getString("ringtone", "default ringtone");
        if (sp.getString("timezone", "").equals("")) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String s = calendar.getTimeZone().getID().toString();
            lpt.setValue(sp.getString("timezone", s));
            LogUtils.i((new StringBuilder("timeee")).append(s).toString());
        } else {
            lpt.setValue(lpt.getValue());
        }
        lpt.setSummary(lpt.getEntry());
        city_manual = findPreference("change_city_manual");
        city_manual.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                parentActivity.disableAllAlarm(getActivity());
                parentActivity.cancel_all_alarm(getActivity());
                Intent intent = (new Intent(getActivity(), com.prayertimes.qibla.appsourcehub.activity.ActivitySearch.class)).putExtra("cat", "settings");
                intent.putExtra("type", "country");
                startActivity(intent);
                getActivity().finish();
                return true;
            }
        });
        ltln = findPreference("latlng");
        ltln.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                parentActivity.disableAllAlarm(getActivity());
                parentActivity.cancel_all_alarm(getActivity());
                Intent intent = new Intent(getActivity(), com.prayertimes.qibla.appsourcehub.activity.ActivityLatLng.class);
                startActivity(intent);
                parentActivity.finish();
                return true;
            }
        });
        city_automatic = findPreference("change_city_auto");
        city_automatic.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (parentActivity.isOnline()) {
                    parentActivity.disableAllAlarm(getActivity());
                    parentActivity.cancel_all_alarm(getActivity());
                    getLocation();
                } else {
                    parentActivity.showalert();
                }
                return true;
            }
        });
    }

    public void onDestroy() {
        LogUtils.i("", "destroy fragment settings");
        if (appLocationService != null) {
            appLocationService.stopUsingGPS();
            getActivity().stopService(new Intent(getActivity(), com.prayertimes.qibla.appsourcehub.activity.ActivityMain.class));
        }
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        LogUtils.i("pause");
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        String s = parentActivity.LoadPref(parentActivity.USER_CITY);
        String s1 = parentActivity.LoadPref(parentActivity.USER_COUNTRY);
        String s2 = parentActivity.LoadPref(parentActivity.USER_STATE);
        city_automatic.setSummary(getString(R.string.lblcitycountry, new Object[]{
                s, s2, s1
        }));
        city_manual.setSummary(getString(R.string.lblcitycountry, new Object[]{
                s, s2, s1
        }));
    }

    public void onResume() {
        super.onResume();
        LogUtils.i("resume");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        String s = parentActivity.LoadPref(parentActivity.USER_CITY);
        String s1 = parentActivity.LoadPref(parentActivity.USER_COUNTRY);
        String s2 = parentActivity.LoadPref(parentActivity.USER_STATE);
        city_automatic.setSummary((new StringBuilder(String.valueOf(s))).append(" ").append(s2).append(" ").append(s1).toString());
        city_manual.setSummary((new StringBuilder(String.valueOf(s))).append(" ").append(s2).append(" ").append(s1).toString());
        ltln.setSummary((new StringBuilder(String.valueOf(parentActivity.loadString("user_lat")))).append(",").append(parentActivity.loadString("user_lng")).toString());
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String s) {
        parentActivity.disableAllAlarm(getActivity());
        parentActivity.cancel_all_alarm(getActivity());
        lpm.setSummary(lpm.getEntry());
        lpj.setSummary(lpj.getEntry());
        lpl.setSummary(lpl.getEntry());
        lpt.setSummary(lpt.getEntry());
        lph.setSummary(lph.getEntry());
        lar.setSummary(lar.getEntry());
        len.setSummary(len.getEntry());
        lt.setSummary(lt.getEntry());
        ln.setSummary(ln.getEntry());
        imd.setSummary((new StringBuilder(String.valueOf(sp.getString("Imsakdaylight", "0")))).append(" Minutes").toString());
        fjd.setSummary((new StringBuilder(String.valueOf(sp.getString("Fajardaylight", "0")))).append(" Minutes").toString());
        dhd.setSummary((new StringBuilder(String.valueOf(sp.getString("Dhuhrdaylight", "0")))).append(" Minutes").toString());
        asd.setSummary((new StringBuilder(String.valueOf(sp.getString("Asrdaylight", "0")))).append(" Minutes").toString());
        mhd.setSummary((new StringBuilder(String.valueOf(sp.getString("Maghribdaylight", "0")))).append(" Minutes").toString());
        isd.setSummary((new StringBuilder(String.valueOf(sp.getString("Ishadaylight", "0")))).append(" Minutes").toString());
        String s1;
        if (sp.getString("Imsakdaylight", "0").equals("")) {
            save("Imsakdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Imsakdaylight", "0"));
            } catch (NumberFormatException numberformatexception) {
                save("Imsakdaylight");
            }
        }
        if (sp.getString("Fajardaylight", "0").equals("")) {
            save("Fajardaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Fajardaylight", "0"));
            } catch (NumberFormatException numberformatexception1) {
                save("Fajardaylight");
            }
        }
        if (sp.getString("Dhuhrdaylight", "0").equals("")) {
            save("Dhuhrdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Dhuhrdaylight", "0"));
            } catch (NumberFormatException numberformatexception2) {
                save("Dhuhrdaylight");
            }
        }
        if (sp.getString("Asrdaylight", "0").equals("")) {
            save("Asrdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Asrdaylight", "0"));
            } catch (NumberFormatException numberformatexception3) {
                save("Asrdaylight");
            }
        }
        if (sp.getString("Maghribdaylight", "0").equals("")) {
            save("Maghribdaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Maghribdaylight", "0"));
            } catch (NumberFormatException numberformatexception4) {
                save("Maghribdaylight");
            }
        }
        if (sp.getString("Ishadaylight", "0").equals("")) {
            save("Ishadaylight");
        } else {
            try {
                Integer.parseInt(sp.getString("Ishadaylight", "0"));
            } catch (NumberFormatException numberformatexception5) {
                save("Ishadaylight");
            }
        }
        s1 = sp.getString("ringtone", "default ringtone");
        rp.setSummary(s1);
    }

    public void save(String s) {
        editor = sp.edit();
        editor.putString(s, "0");
        editor.commit();
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("SETTINGS");
        builder.setMessage("Enable Location Provider! Go to settings menu?");
        builder.setPositiveButton("Settings", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                getActivity().startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 1);
            }
        });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                dialoginterface.cancel();
            }
        });
        builder.show();
    }


}
