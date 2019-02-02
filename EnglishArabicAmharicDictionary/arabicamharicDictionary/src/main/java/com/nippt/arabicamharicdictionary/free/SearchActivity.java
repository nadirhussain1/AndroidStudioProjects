package com.nippt.arabicamharicdictionary.free;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.nippt.arabicamharicdictionary.R;
import com.nippt.arabicamharicdictionary.free.database.DatabaseHelper;
import com.nippt.arabicamharicdictionary.free.database.DbUtil;
import com.nippt.arabicamharicdictionary.free.database.ItemAutoTextAdapter;
import com.nippt.arabicamharicdictionary.free.widget.AutoCompleteDbAdapter;
import com.nippt.arabicamharicdictionary.model.Word;
import com.tjeannin.apprate.AppRate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class SearchActivity extends BaseActivity implements android.view.View.OnClickListener, OnInitListener {

    public static final String KEY_DETAIL_WORD = "KEY_DETAIL_WORD";
    private CustomKeyboard amharicKeyboard, arabicKeyboard;

    private AutoCompleteTextView englishSearchEditor, arabicSearchEditor, amharicSearchEditor;
    private ItemAutoTextAdapter engAdapter, arabicAdapter, amharicAdapter;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private TextView englishStaticTextView, mEnglishWordView, arabicLangWordView, amharicLangWordView;
    private RelativeLayout mNoResult, mProgress, mContent, englishSpeakLayout;
    private ImageButton mFavourite, btnShare, btnRead, btnSlidingMenu;
    private LinearLayout contentSeach;

    private boolean isSelectAll = false;
    private Typeface textfont, textfont1;
    private int selectedWordId;


    private TextToSpeech mTts;
    private String mTtsQueueSentence;
    private static final String SHARE_MESSAGE = "Download the Amharic Free Dictionary for Android from the PlayStore today!";
    public static boolean showWordOfToday;


    public void finish() {
        hideKeyboards();
        mDb.close();
        super.finish();
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Word selectedWord = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            selectedWord = (Word) getIntent().getSerializableExtra(KEY_DETAIL_WORD);
        }

        // Init AppRater.
        new AppRate(this).setMinDaysUntilPrompt(0).setMinLaunchesUntilPrompt(5).setShowIfAppHasCrashed(false).init();
        setContentView(R.layout.search_layout);

        initSlidingMenu();
        initViews();
        initClicks();
        initListAdapters();
        showAdmobAd();
        initCustomKeyboards();


        if (selectedWord != null) {
            displaySelectedWord(selectedWord);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       // getWindow().setSoftInputMode(3);
    }

    @Override
    public void onStop() {
        super.onStop();
      //  getWindow().setSoftInputMode(3);
    }

    @Override
    public void onBackPressed() {
        if (isCustomKeyboardVisible()) {
            hideKeyboards();
        } else {
            finish();
        }
    }


    private void initViews() {
        englishSearchEditor = (AutoCompleteTextView) findViewById(R.id.engSearchEditText);
        arabicSearchEditor = (AutoCompleteTextView) findViewById(R.id.arabicSearchEditText);
        amharicSearchEditor = (AutoCompleteTextView) findViewById(R.id.amharicSearchEditText);

        englishStaticTextView = (TextView) findViewById(R.id.englishStaticWordTextView);
        englishSpeakLayout = (RelativeLayout) findViewById(R.id.englishSpeakLayout);
        mEnglishWordView = (TextView) findViewById(R.id.englishWordTextView);
        arabicLangWordView = (TextView) findViewById(R.id.secondLanWordTextView);
        amharicLangWordView = (TextView) findViewById(R.id.thirdLangWordTextView);
        btnRead = (ImageButton) this.findViewById(R.id.btnRead);
        btnSlidingMenu = (ImageButton) this.findViewById(R.id.btnSlidingMenu);
        contentSeach = (LinearLayout) this.findViewById(R.id.contentSearch);
        btnShare = (ImageButton) this.findViewById(R.id.btnShare);
        mNoResult = (RelativeLayout) findViewById(R.id.no_result);
        mProgress = (RelativeLayout) findViewById(R.id.progressbar);
        mContent = (RelativeLayout) findViewById(R.id.content);
        mFavourite = (ImageButton) findViewById(R.id.add_favourite);


        textfont = Typeface.createFromAsset(this.getAssets(), "fonts/gfzemen.ttf");
        textfont1 = Typeface.createFromAsset(this.getAssets(), "fonts/arial.ttf");
        arabicLangWordView.setTypeface(textfont);

        mContent.setVisibility(View.GONE);
        mNoResult.setVisibility(View.VISIBLE);
    }

    private void initClicks() {
        btnRead.setOnClickListener(this);
        btnSlidingMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                slidingMenu.toggle(true);
            }
        });

        contentSeach.setDrawingCacheEnabled(true);
        btnShare.setOnClickListener(this);
        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFavoriteDialog();
            }
        });
    }

    private void initListAdapters() {
        mDbHelper = new DatabaseHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        AutoCompleteDbAdapter englishDbHelper = new AutoCompleteDbAdapter(this, 0);
        AutoCompleteDbAdapter arabicDbHelper = new AutoCompleteDbAdapter(this, 1);
        AutoCompleteDbAdapter amharicDbHelper = new AutoCompleteDbAdapter(this, 2);

        engAdapter = new ItemAutoTextAdapter(this, englishDbHelper);
        arabicAdapter = new ItemAutoTextAdapter(this, arabicDbHelper);
        amharicAdapter = new ItemAutoTextAdapter(this, amharicDbHelper);

        englishSearchEditor.setAdapter(engAdapter);
        arabicSearchEditor.setAdapter(arabicAdapter);
        amharicSearchEditor.setAdapter(amharicAdapter);

        englishSearchEditor.setOnItemClickListener(engItemClickListener);
        arabicSearchEditor.setOnItemClickListener(arabicItemClickListener);
        amharicSearchEditor.setOnItemClickListener(amharicItemClickListener);


    }

    private void showAdmobAd() {
        AdView adview = (AdView) findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }

    private void initCustomKeyboards() {
        arabicKeyboard = new CustomKeyboard(this, R.id.arabicKeyboardView, R.xml.hexkbd_arab);
        amharicKeyboard = new CustomKeyboard(this, R.id.amharicKeyboardView, R.xml.hexkbd);

        arabicKeyboard.registerEditText(R.id.arabicSearchEditText);
        amharicKeyboard.registerEditText(R.id.amharicSearchEditText);

    }


    private void hideKeyboards() {
        if (arabicKeyboard != null && arabicKeyboard.isCustomKeyboardVisible()) {
            arabicKeyboard.hideCustomKeyboard();
        }
        if (amharicKeyboard != null && amharicKeyboard.isCustomKeyboardVisible()) {
            amharicKeyboard.hideCustomKeyboard();
        }
    }

    private boolean isCustomKeyboardVisible() {
        return arabicKeyboard.isCustomKeyboardVisible() || amharicKeyboard.isCustomKeyboardVisible();
    }

    private void closeAdapters(boolean close) {
        engAdapter.setClose(close);
        amharicAdapter.setClose(close);
        arabicAdapter.setClose(close);
    }

    protected void onDestroy() {
        hideKeyboards();
        closeAdapters(true);
        super.onDestroy();
    }

    protected void onPause() {
        hideKeyboards();
        closeAdapters(true);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        closeAdapters(false);

        if (showWordOfToday)
            showDialogWordOfToday();
    }


    public void showDialogWordOfToday() {
        showWordOfToday = false;

        Random random = new Random();
        int pos = random.nextInt() % 1000;
        Word word = DbUtil.fetchWord(mDb, Math.abs(pos), 0);
        displaySelectedWord(word);


        addHistory(word, true);
        isSelectAll = true;
        hideKeyboards();
    }


    private void displayItemClicked(Word word) {
        mNoResult.setVisibility(View.GONE);
        mContent.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        displaySelectedWord(word);
    }

    private OnItemClickListener engItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Cursor cur = (Cursor) adapterView.getItemAtPosition(position);
            Word word = DbUtil.getWordFromCursor(cur, 0);
            cur.close();

            displayItemClicked(word);
        }
    };
    private OnItemClickListener arabicItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Cursor cur = (Cursor) adapterView.getItemAtPosition(position);
            Word word = DbUtil.getWordFromCursor(cur, 1);
            cur.close();

            displayItemClicked(word);
        }
    };
    private OnItemClickListener amharicItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Cursor cur = (Cursor) adapterView.getItemAtPosition(position);
            Word word = DbUtil.getWordFromCursor(cur, 2);
            cur.close();

            displayItemClicked(word);
        }
    };

    private void displaySelectedWord(Word word) {
        selectedWordId = word.getWordId();
        String engWord = word.getEnglishWord();
        if (TextUtils.isEmpty(engWord)) {
            englishStaticTextView.setVisibility(View.GONE);
            englishSpeakLayout.setVisibility(View.GONE);
        } else {
            String normalText, blueText;
            if (engWord.contains("(")) {
                int index = engWord.indexOf("(");
                normalText = engWord.substring(0, index - 1);
                blueText = engWord.substring(index, engWord.length());
            } else {
                normalText = engWord;
                blueText = "";
            }

            String styledText = normalText + " <font color='blue'>" + blueText + "</font>";
            mEnglishWordView.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

            englishStaticTextView.setVisibility(View.VISIBLE);
            englishSpeakLayout.setVisibility(View.VISIBLE);
        }


        arabicLangWordView.setText(word.getArabicWord());
        amharicLangWordView.setText(word.getAmharicWord());


        mProgress.setVisibility(View.GONE);
        mNoResult.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
        addHistory(word, true);
        isSelectAll = true;
        hideKeyboards();
    }

    private int getIndentifier(String name) {
        int i = this.getResources().getIdentifier(name, "drawable",
                this.getPackageName());
        return i;
    }


    private void showFavoriteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add favourite word");
        builder.setMessage("Do you want to add this word to your favourite?");
        builder.setNegativeButton("Cancel", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                addFavourite(selectedWordId);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void addHistory(Word word, boolean isUpdateDate) {
        if (mDb != null) {
            ContentValues values = new ContentValues();

            values.put(DbUtil.COL_WORDID, word.getWordId());
            values.put(DbUtil.COL_ENG_WORD, word.getEnglishWord());
            values.put(DbUtil.COL_ARABIC_WORD, word.getArabicWord());
            values.put(DbUtil.COL_AMHARIC_WORD, word.getAmharicWord());
            values.put(DbUtil.COL_HISTORY, 1);
            SimpleDateFormat m = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String s = m.format(new Date());
            values.put(DbUtil.COL_ADDED, s);

            long kq = mDb.insert(DbUtil.HISTORY_TABLE, null, values);
            if (kq == -1) {
                System.out.println("Added failure");
                if (!isUpdateDate) {
                    values.remove(DbUtil.COL_ADDED);
                }
                values.remove(DbUtil.COL_WORDID);
                mDb.update(DbUtil.HISTORY_TABLE, values, "WordId = " + word.getWordId() + " AND history = 0", null);
            } else {
                System.out.println("Add ok " + word.getEnglishWord());
            }
        }
    }

    private void addFavourite(int wordId) {

        if (mDb != null) {
            ContentValues values = new ContentValues();
            Date d = new Date();
            values.put("favourite", true);
            SimpleDateFormat m = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String s = m.format(new Date());
            values.put("favourite_day", s);
            int kq = mDb.update(DbUtil.HISTORY_TABLE, values, "wordId = " + wordId, null);
            if (kq == -1) {
                System.out.println("Added favourtie failure");
            } else {
                System.out.println("Add favourite ok " + wordId);
                // HistoryActivity.mActivity.updateFav(wordId, 1);
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == btnShare) {
            share();
        } else {
            if (mTts == null) {
                String word = mEnglishWordView.getText().toString();

                if (word.contains("(")) {
                    int index = word.indexOf("(");
                    mTtsQueueSentence = word.substring(0, index - 1);
                    Log.d("Index of ( :  ", mTtsQueueSentence + "");
                } else {
                    mTtsQueueSentence = word;
                }

                Intent checkIntent = new Intent();
                checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(checkIntent, CHECK_TTS_DATA_CODE);
            } else {
                String word = mEnglishWordView.getText().toString();
                if (word.contains("(")) {
                    int index = word.indexOf("(");
                    mTtsQueueSentence = word.substring(0, index - 1);
                    Log.d("Index of ( :  ", mTtsQueueSentence + "");
                } else {
                    mTtsQueueSentence = word;
                }

                mTts.speak(mTtsQueueSentence, TextToSpeech.QUEUE_FLUSH, null);
                Toast.makeText(this, mTtsQueueSentence, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private static final int CHECK_TTS_DATA_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_TTS_DATA_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap captureUsingDrawingCache() {
        contentSeach.buildDrawingCache();
        Bitmap b1 = contentSeach.getDrawingCache();
        Bitmap b = b1.copy(Bitmap.Config.ARGB_8888, false);
        return b;
    }

    private void share() {
        Bitmap bitmap = captureUsingDrawingCache();
        File cache = getApplicationContext().getExternalCacheDir();
        File sharefile = new File(cache, "toshare.png");
        try {
            FileOutputStream out = new FileOutputStream(sharefile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {

        }

        // Now send it out to share
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_TEXT, SHARE_MESSAGE);
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
        try {
            startActivity(Intent.createChooser(share, "Share photo"));
        } catch (Exception e) {

        }

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.US);

            if (mTtsQueueSentence != null && mTtsQueueSentence.length() > 0) {
                mTts.speak(mTtsQueueSentence, TextToSpeech.QUEUE_FLUSH, null);
                mTtsQueueSentence = "";
            }
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this,
                    "Can't start the TST System",
                    Toast.LENGTH_SHORT).show();
        }
    }


}


