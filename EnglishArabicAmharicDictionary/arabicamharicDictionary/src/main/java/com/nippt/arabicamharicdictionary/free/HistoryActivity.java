package com.nippt.arabicamharicdictionary.free;

//import com.nippt.amharicdictionary.edittext.AutoCompleteInfo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nippt.arabicamharicdictionary.R;
import com.nippt.arabicamharicdictionary.adapter.HistoryAdapter;
import com.nippt.arabicamharicdictionary.free.database.DatabaseHelper;
import com.nippt.arabicamharicdictionary.free.database.DbUtil;
import com.nippt.arabicamharicdictionary.free.database.History;
import com.nippt.arabicamharicdictionary.free.listener.HistoryListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends BaseActivity implements
        OnItemClickListener, HistoryListener {

    private ListView mListView;
    private static DatabaseHelper mHelper;
    private static SQLiteDatabase mDb;
    private ArrayList<History> mDataList;
    private HistoryAdapter mAdapter;
    private String date_update;
    public static HistoryActivity mActivity;
    private ImageButton btnSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mActivity = this;
        date_update = "2013-01-01 00:00:00";
        mHelper = new DatabaseHelper(this);
        mDb = mHelper.getWritableDatabase();
        setContentView(R.layout.history_layout);

        this.mListView = ((ListView) findViewById(R.id.HistoryList));
        btnSlidingMenu = (ImageButton) this.findViewById(R.id.btnSlidingMenu);
        btnSlidingMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                slidingMenu.toggle(true);
            }
        });

        initSlidingMenu();

        mDataList = new ArrayList<History>();

        View localView = findViewById(R.id.EmptyView);
        this.mListView.setEmptyView(localView);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setOnCreateContextMenuListener(this);

        mAdapter = new HistoryAdapter(this);
        mAdapter.setListener(this);
        mAdapter.setData(mDataList);
        mListView.setAdapter(mAdapter);
        showAdmobAd();

    }
    private void showAdmobAd(){
        AdView adview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent localIntent = new Intent(HistoryActivity.this, SearchActivity.class);
        localIntent.putExtra(SearchActivity.KEY_DETAIL_WORD, mDataList.get(arg2));
        HistoryActivity.this.startActivity(localIntent);
    }

    private synchronized ArrayList<History> getHistory(String update) {
        ArrayList<History> mDataList = new ArrayList<History>();
        System.out.println("Get history updated " + update);
        try {
            Cursor cursor = DbUtil.fetchHistory(mDb, update);
            SimpleDateFormat m = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            date_update = m.format(new Date());
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(DbUtil.COL_WORDID));
                    String engWord = cursor.getString(cursor.getColumnIndex(DbUtil.COL_ENG_WORD));
                    String secondLangWord = cursor.getString(cursor.getColumnIndex(DbUtil.COL_ARABIC_WORD));
                    String thirdLangWord = cursor.getString(cursor.getColumnIndex(DbUtil.COL_AMHARIC_WORD));
                    String added = cursor.getString(cursor.getColumnIndex(DbUtil.COL_ADDED));
                    int fav = cursor.getInt(cursor.getColumnIndex(DbUtil.COL_ADDED));

                    History history = new History(Integer.parseInt(id), engWord, secondLangWord, thirdLangWord);
                    history.setmFavourite(fav);
                    mDataList.add(history);
                }
                cursor.close();
                return mDataList;
            }

        } catch (SQLException e) {
            Log.e("AutoCompleteDbAdapter", e.toString());
            throw e;
        }

        return mDataList;
    }

    @Override
    protected void onResume() {
        ArrayList<History> mTmp = getHistory(date_update);
        for (int i = 0; i < mTmp.size(); i++) {
            if (mAdapter != null) {
                if (mAdapter.getData() != null) {
                    mAdapter.getData().add(0, mTmp.get(i));
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        super.onResume();
    }

    public void updateFav(int WordId, int kq) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (WordId == mDataList.get(i).getWordId()) {
                mDataList.get(i).setmFavourite(kq);
            }
        }
        mAdapter.notifyDataSetInvalidated();
    }

    private int removeHistoryFromDatabase(int wordId) {
        if (HistoryActivity.mDb != null) {
            ContentValues values = new ContentValues();
            Date d = new Date();
            values.put("history", false);
            Cursor cursor = mDb.rawQuery("Select favourite from HISTORY Where WordId = " + wordId, null);
            if (cursor.moveToNext()) {
                int favourite = cursor.getInt(0);
                int kq = 0;
                if (favourite == 1) {
                    kq = HistoryActivity.mDb.update(DbUtil.HISTORY_TABLE, values, DbUtil.COL_WORDID + " = " + wordId, null);
                } else {
                    kq = HistoryActivity.mDb.delete(DbUtil.HISTORY_TABLE, DbUtil.COL_WORDID + " = " + wordId,
                            null);
                }
                return kq;
            }

        }
        return 0;
    }

    @Override
    public void removeHistory(int id) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getWordId() == id) {
                int kq = removeHistoryFromDatabase(mDataList.get(i).getWordId());
                if (kq > 0)
                    mDataList.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
