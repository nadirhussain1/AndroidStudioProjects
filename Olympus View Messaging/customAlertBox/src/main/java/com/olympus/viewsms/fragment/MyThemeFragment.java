package com.olympus.viewsms.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.olympus.viewsms.PreviewActivity;
import com.olympus.viewsms.R;
import com.olympus.viewsms.adapter.ContactListAdapter;
import com.olympus.viewsms.adapter.ThemeAdapter;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickBuy;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickPreview;
import com.olympus.viewsms.data.ContactAppliedDAO;
import com.olympus.viewsms.model.ContactApplied;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyThemeFragment extends Fragment {

    private View view;
    public SlidingMenu slidingmenu;

    private ContactAppliedDAO contactDAO;
    public static List<Theme> themes;
    private List<ContactApplied> contact_applieds_curtheme;
    private ThemeAdapter adapter;
    private List<ContactApplied> contact_list;
    private int cur_theme_id;    //not be 0
    private int theme_id_applyall;   //can be 0, 0 is not for any themes

    private RecyclerView recyclerView;
    private RecyclerView lv_contact;
    private CheckBox chk_all;

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Log.d("Debug", "OnCreate() Of MyThemeFragment");
    }

    public void recycleAdapterBitmaps() {
        if (adapter != null) {
            adapter.recycleBitmaps();
            System.gc();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Debug", "OnDestroy() Of MyThemeFragment");
        recycleAdapterBitmaps();
        System.gc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mytheme, container, false);

        loadAdmobBannerAd(view);

        cur_theme_id = prefs.getInt(Constants.APPLIED_THEME_ID, Constants.DEFAULT_FIRST_THEME_ID);
        chk_all = (CheckBox) view.findViewById(R.id.check_default);
        chk_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_all.isChecked() == true)  //so, before check is false
                {
                    Builder ad = new AlertDialog.Builder(getActivity());
                    ad.setTitle("Set as default theme");
                    ad.setMessage("This theme will be set for all those contacts which have no themes set");
                    ad.setCancelable(false);
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            theme_id_applyall = cur_theme_id;
                            saveSetforallorallother(theme_id_applyall);
                        }
                    });
                    ad.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chk_all.setChecked(false);
                        }
                    });
                    ad.show();
                } else {
                    theme_id_applyall = 0;  //uncheck this
                    saveSetforallorallother(theme_id_applyall);
                }
            }
        });


        contactDAO = new ContactAppliedDAO(getActivity());
        adapter = new ThemeAdapter(getActivity(), ThemeAdapter.MODE_AVAILABLE, themes);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        lv_contact = (RecyclerView) view.findViewById(R.id.contactslist);
        LinearLayoutManager contactLayoutManager = new LinearLayoutManager(getActivity());
        lv_contact.setLayoutManager(contactLayoutManager);

        adapter.setOnClickBuyListener(new OnClickBuy() {
            @Override
            public void onClickBuy(int pos) {
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putInt(Constants.APPLIED_THEME_ID, themes.get(pos).getId());
                prefsEditor.commit();
                //adapter.notifyDataSetChanged();

                cur_theme_id = themes.get(pos).getId();

                //open menu
                slidingmenu.toggle();
            }
        });
        adapter.setOnClickPreviewListener(new OnClickPreview() {
            @Override
            public void onClickPreview(int pos) {
                Intent i = new Intent(getActivity(), PreviewActivity.class);
                i.putExtra("theme_id", themes.get(pos).getId());
                i.putExtra("avartar_id", pos % Constants.TOTAL_DEMO_AVARTARS);
                startActivity(i);
            }
        });

        initSlidingMenu();

        return view;
    }

    private void saveSetforallorallother(int theme_id_applyall) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt(Constants.SET_FOR_ALL_OR_ALL_OTHERS, theme_id_applyall);
        prefsEditor.commit();
    }

    private void loadAdmobBannerAd(View view) {
        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7724E66CE6A65C50FCDDC8F9091B585C").build();
        adView.loadAd(adRequest);
    }

    public void toggleSlidingmenu() {
        if (isAdded()) {
            slidingmenu.toggle();
        }
    }

    private void initSlidingMenu() {

        slidingmenu = (SlidingMenu) view.findViewById(R.id.slidingmenulayout);
        slidingmenu.setMode(SlidingMenu.RIGHT);
        slidingmenu.setOnOpenedListener(new OnOpenedListener() {
            @Override
            public void onOpened() {
                contact_applieds_curtheme = new ArrayList<ContactApplied>();
                contact_applieds_curtheme = contactDAO.getAll(cur_theme_id);
                Toast.makeText(getActivity(), contact_applieds_curtheme.size() + " " + getString(R.string.contact_applied), Toast.LENGTH_SHORT).show();

                theme_id_applyall = prefs.getInt(Constants.SET_FOR_ALL_OR_ALL_OTHERS, 0);  //0 is not for any theme
                chk_all.setChecked(theme_id_applyall == cur_theme_id);

                getContactsDetails();  //reload contact after open menu
            }
        });
        slidingmenu.setOnClosedListener(new OnClosedListener() {
            @Override
            public void onClosed() {
                if (contact_list != null && contact_list.size() > 0) {
                    saveContactApplied();
                    Log.i("====================", "changes saved");
                }
            }
        });
    }

    //Method to retrieve Contact details from the phone database and display in the app in a list view
    private void getContactsDetails() {

        //A Progress dialog with a spinning wheel, to instruct the user about the app's current state
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), getString(R.string.plz_wait), getString(R.string.retrieve_contact), true);

        //A new worker thread is created to retrieve and display the contacts.
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();

                contact_list = new ArrayList<ContactApplied>();
                Cursor phoneContactsCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, Phone.DISPLAY_NAME + " COLLATE NOCASE ASC");
                if (phoneContactsCursor.getCount() == 0) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.not_found_contact), Toast.LENGTH_SHORT).show();
                    return;
                }
                phoneContactsCursor.moveToFirst();
                do {
                    String name = phoneContactsCursor.getString(phoneContactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = phoneContactsCursor.getString(phoneContactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String photoUri=phoneContactsCursor.getString(phoneContactsCursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI));
                    if(!TextUtils.isDigitsOnly(name)) {
                        ContactApplied item = new ContactApplied(-1, name, Utils.format(number), cur_theme_id, photoUri);  //-1 is not nesceesssary
                        if (isApplied(item)) {
                            item.setCheck(true);
                          //  Log.i("======contact applied======", item.getNumber());
                        }
                        contact_list.add(item);
                    }

                } while (phoneContactsCursor.moveToNext());

                lv_contact.post(new Runnable() {
                    @Override
                    public void run() {
                        ContactListAdapter adapter = new ContactListAdapter(getActivity(), contact_list, false);
                        lv_contact.setAdapter(adapter);
                    }
                });
                dialog.dismiss();
            }
        }).start();
    }

    private void saveContactApplied() {
        contact_applieds_curtheme = new ArrayList<ContactApplied>();
        contactDAO.deleteAllContactApplied(cur_theme_id);
        int n = 0;
        for (ContactApplied c : contact_list) {
            if (c.getCheck()) {
                contactDAO.deleteContactApplied(c.getName(), c.getNumber());  //delete all in other themes
                contactDAO.addContactApplied(c);                              //add to current theme
                n++;
            }
        }
        Toast.makeText(getActivity(), n + " " + getString(R.string.contact_applied), Toast.LENGTH_SHORT).show();
    }

    boolean isApplied(ContactApplied c) {

        for (ContactApplied cc : contact_applieds_curtheme) {
             String contactAppliedNumber=cc.getNumber();
             if(contactAppliedNumber.length()>5){
                contactAppliedNumber=contactAppliedNumber.substring(contactAppliedNumber.length()-5);
             }
            if (cc.getName().equals(c.getName()) && contactAppliedNumber.equals(c.getNumber()))
                return true;
        }
        return false;
    }
}
