package com.olympus.viewsms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.olympus.viewsms.adapter.ChatListAdapter;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;


public class SMS_Floating_Window extends Activity {
    public static boolean is_shown = false;
    public static String senderNumber = null;

    private View mainParentView = null;
    private int mCurrentX = 0;
    private int mCurrentY = 0;
    public Animation bounceAnimation = null;
    private PopupWindow window = null;
    private EditText replyEditor = null;
    private SmsData sms = null;
    private static ArrayList<SmsData> conversationList = new ArrayList<SmsData>();
    private static ChatListAdapter chatAdapter = null;
    private static ListView messageListView = null;

    View replyOlympusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        is_shown = true;
        conversationList.clear();
        chatAdapter = null;

        mainParentView = new View(this);
        setContentView(mainParentView);
        findInitialCenterOfScreen();
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        replyOlympusView = inflater.inflate(R.layout.new_sms_reply_layout, null, true);

        RelativeLayout containerLayout = (RelativeLayout) replyOlympusView.findViewById(R.id.container);
        containerLayout.setOnClickListener(containerBounceClickListener);

        ImageView widgetHead = (ImageView) replyOlympusView.findViewById(R.id.widgetIconHead);
        widgetHead.setOnTouchListener(windowFloatTouchListener);

        setCustomTheme(replyOlympusView);
        loadPreviousConversations();

        messageListView = (ListView) replyOlympusView.findViewById(R.id.messagesListView);
        chatAdapter = new ChatListAdapter(this, conversationList);
        messageListView.setAdapter(chatAdapter);
        messageListView.setSelection(conversationList.size() - 1);

        window = new PopupWindow(replyOlympusView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOnDismissListener(windowDismissListener);
        window.setAnimationStyle(R.style.ExitAnimStyle);


        mainParentView.post(new Runnable() {
            @Override
            public void run() {
                window.showAtLocation(mainParentView, Gravity.TOP | Gravity.LEFT, mCurrentX, mCurrentY);
                replyOlympusView.startAnimation(bounceAnimation);
            }
        });

        initClicks(replyOlympusView);
    }

    private void findInitialCenterOfScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y - getStatusBarHeight();
        float density = getResources().getDisplayMetrics().density;

        int popWidth = (int) (320 * density);
        int popHeight = (int) (380 * density);

        mCurrentX = (width - popWidth) / 2;
        mCurrentY = (height - popHeight) / 2;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
    }

    @Override
    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }


    private void initClicks(View view) {
        Button cancelButton = (Button) view.findViewById(R.id.closeButton);
        cancelButton.setOnClickListener(cancelButtonClickListener);

        Button sendReplyButton = (Button) view.findViewById(R.id.sendButton);
        sendReplyButton.setOnClickListener(sendReplyClickListener);

        replyEditor = (EditText) view.findViewById(R.id.replyContentEditor);
    }

    private void loadPreviousConversations() {
        senderNumber = sms.getSenderNumber();
        String where = "address='" + senderNumber + "'";
        Uri mSmsInboxQueryUri = Uri.parse("content://sms");
        Cursor cursor = getContentResolver().query(mSmsInboxQueryUri, new String[]{"person", "date", "body", "type"}, where, null, null);
        while (cursor.moveToNext()) {

            String name = cursor.getString(0);
            long date = cursor.getLong(1);
            String body = cursor.getString(2);
            int type = cursor.getInt(3);

            SmsData smsData = new SmsData(senderNumber, name, body, date, type);
            conversationList.add(0, smsData);

        }

    }

    private void setCustomTheme(View view) {
        try {
            sms = (SmsData) getIntent().getSerializableExtra("SmsData");
            String thumbnailUri=getIntent().getStringExtra("avatar");
            sms.setThumbnail(thumbnailUri);


            Theme cur_theme = Utils.getAppliedTheme(this, sms);

            ImageView img_bg = (ImageView) view.findViewById(R.id.img_bg);
            img_bg.setImageDrawable(Utils.getDrawable(this, "ic" + cur_theme.getId() + "_bg"));
            if (cur_theme.getTheme_height() > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Scale.cvDPtoPX(this, 290), Scale.cvDPtoPX(this, cur_theme.getTheme_height()));
                params.addRule(RelativeLayout.RIGHT_OF, R.id.floatingHeadLayout);
                img_bg.setLayoutParams(params);

                RelativeLayout container = (RelativeLayout) view.findViewById(R.id.container);
                container.setLayoutParams(params);
                container.setPadding(cur_theme.getL(), cur_theme.getT(), cur_theme.getR(), 0);
            }

            ImageView img_s2 = (ImageView) view.findViewById(R.id.img_s2);
            img_s2.setImageDrawable(Utils.getDrawable(this, "ic" + cur_theme.getId() + "_s2"));

            if (cur_theme.getId() >= 22) {
                EditText replyEditBox = (EditText) view.findViewById(R.id.replyContentEditor);
                Button enterButton = (Button) view.findViewById(R.id.sendButton);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Scale.cvDPtoPX(this, cur_theme.getReply_box_width()), LayoutParams.WRAP_CONTENT);
                params.setMargins(Scale.cvDPtoPX(this, 5), 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                replyEditBox.setLayoutParams(params);

                replyEditBox.setBackgroundResource(Utils.getDrawableResourceId(this, "ic" + cur_theme.getId() + "_replybox"));
                replyEditBox.setTextColor(Color.parseColor(cur_theme.getReply_color()));
                replyEditBox.setHighlightColor(Color.parseColor(cur_theme.getReply_color()));
                enterButton.setBackgroundResource(Utils.getDrawableResourceId(this, "ic" + cur_theme.getId() + "_enter"));

                RelativeLayout bottomBar = (RelativeLayout) view.findViewById(R.id.bottomBar);
                params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Scale.cvDPtoPX(this, 40));
                params.setMargins(0, 0, 0, Scale.cvDPtoPX(this, 20));
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bottomBar.setLayoutParams(params);
            }


        } catch (Exception e) {
            Log.d("FloatingWindow", e.toString());
        }
    }

    private void closeScreen() {
        is_shown = false;
        if (window != null) {
            window.dismiss();
        }
    }

    public static void refreshReplyWindow(SmsData sentSms) {
        conversationList.add(sentSms);
        chatAdapter.notifyDataSetChanged();
        messageListView.setSelection(conversationList.size() - 1);
    }

    OnClickListener cancelButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            closeScreen();
        }
    };
    OnTouchListener windowFloatTouchListener = new OnTouchListener() {
        private float mDx;
        private float mDy;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                mDx = mCurrentX - event.getRawX();
                mDy = mCurrentY - event.getRawY();
            } else if (action == MotionEvent.ACTION_MOVE) {
                mCurrentX = (int) (event.getRawX() + mDx);
                mCurrentY = (int) (event.getRawY() + mDy);
                window.update(mCurrentX, mCurrentY, -1, -1);
            }
            return true;
        }
    };
    OnClickListener sendReplyClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String message = replyEditor.getText().toString();
            try {
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(sms.getSenderNumber(), null, parts, null, null);

                ContentValues values = new ContentValues();
                values.put("address", sms.getSenderNumber());
                values.put("date", "" + System.currentTimeMillis());
                values.put("read", 1);
                values.put("type", 2);
                values.put("seen", 1);
                values.put("body", message);

                Uri uri = Uri.parse("content://sms/");
                getContentResolver().insert(uri, values);

                SmsData sentSms = new SmsData(sms.getSenderNumber(), "", message, System.currentTimeMillis(), 0);
                refreshReplyWindow(sentSms);

                replyEditor.setText("");

            } catch (Exception e) {
                Toast.makeText(SMS_Floating_Window.this, getString(R.string.sms_sent_failed), Toast.LENGTH_LONG).show();
                Log.d("Exception", e.toString());
                e.printStackTrace();
            }

        }
    };
    OnClickListener containerBounceClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            replyOlympusView.startAnimation(bounceAnimation);

        }
    };
    OnDismissListener windowDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss() {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    is_shown = false;
                    setResult(RESULT_OK);
                    SMS_Floating_Window.this.finish();
                }
            }, 1000);

        }
    };

}
