package com.nippt.arabicamharicdictionary.free;

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
import com.nippt.arabicamharicdictionary.adapter.FavouriteAdapter;
import com.nippt.arabicamharicdictionary.free.database.DatabaseHelper;
import com.nippt.arabicamharicdictionary.free.database.DbUtil;
import com.nippt.arabicamharicdictionary.free.database.History;
import com.nippt.arabicamharicdictionary.free.listener.FavListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FavoriteActivity extends BaseActivity implements FavListener, OnItemClickListener {

    private String date_update = "2013-01-01 00:00:00";
    private ListView mListView;
    private FavouriteAdapter mAdapter;
    private ArrayList<History> mDataList;
    private ImageButton btnSlidingMenu;
    private static DatabaseHelper mHelper;
    private static SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_layout);

        initSlidingMenu();

        mListView = ((ListView) findViewById(R.id.HistoryList));
        View localView = findViewById(R.id.EmptyView);
        mListView.setEmptyView(localView);
        this.mListView.setOnCreateContextMenuListener(this);

        btnSlidingMenu = (ImageButton) this.findViewById(R.id.btnSlidingMenu);
        btnSlidingMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                slidingMenu.toggle(true);
            }
        });

        mHelper = new DatabaseHelper(this);
        mDb = mHelper.getWritableDatabase();

        mDataList = new ArrayList<History>();

        mAdapter = new FavouriteAdapter(this);
        mAdapter.setFavouriteListener(this);
        mAdapter.setData(mDataList);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        showAdmobAd();

    }

    private void showAdmobAd() {
        AdView adview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }


    private synchronized ArrayList<History> getFavourite(String update) {
        ArrayList<History> mDataList = new ArrayList<History>();
        System.out.println("Get fav updated " + update);
        try {
            Cursor cursor = DbUtil.fetchFavourites(mDb, update);
            SimpleDateFormat m = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            date_update = m.format(new Date());

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(DbUtil.COL_WORDID));
                    String englishWord = cursor.getString(cursor.getColumnIndex(DbUtil.COL_ENG_WORD));
                    String secondLanguageWord = cursor.getString(cursor.getColumnIndex(DbUtil.COL_ARABIC_WORD));
                    String thirdLangWord = cursor.getString(cursor.getColumnIndex(DbUtil.COL_AMHARIC_WORD));

                    History history = new History(Integer.parseInt(id), englishWord, secondLanguageWord, thirdLangWord);
                    mDataList.add(history);
                }
                cursor.close();
                return mDataList;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("AutoCompleteDbAdapter", e.toString());
            throw e;
        }

        return mDataList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<History> mTmp = getFavourite(date_update);
        for (int i = 0; i < mTmp.size(); i++) {
            if (mAdapter != null) {
                if (mAdapter.getData() != null) {
                    mAdapter.getData().add(0, mTmp.get(i));
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void removeFavourite(int id) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getWordId() == id) {
                int kq = removeFavouriteFromDatabase(mDataList.get(i).getWordId());
                if (kq > 0)
                    mDataList.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private int removeFavouriteFromDatabase(int wordId) {
        if (mDb != null) {
            ContentValues values = new ContentValues();
            values.put(DbUtil.COL_FAVOURITE, false);
            int kq = mDb.update(DbUtil.HISTORY_TABLE, values, DbUtil.COL_WORDID + " = " + wordId, null);
            if (kq == -1) {
                System.out.println("Added favourtie failure");
            } else {
                HistoryActivity.mActivity.updateFav(wordId, 0);
                System.out.println("Add favourite ok " + wordId);
            }
            return kq;
        }
        return 0;
    }


//    @Override
//    public void goDetail(int id) {
//        Intent localIntent = new Intent(FavoriteActivity.this, SearchActivity.class);
//        localIntent.putExtra(DbUtil.COL_WORDID, id);
//        startActivity(localIntent);
//    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent localIntent = new Intent(FavoriteActivity.this, SearchActivity.class);
        localIntent.putExtra(SearchActivity.KEY_DETAIL_WORD, mDataList.get(arg2));
        startActivity(localIntent);
    }


}
