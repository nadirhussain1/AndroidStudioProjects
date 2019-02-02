package com.olympus.viewsms.util;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.olympus.viewsms.R;
import com.olympus.viewsms.SplashActivity;
import com.olympus.viewsms.data.ContactAppliedDAO;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.model.SharedData;
import com.olympus.viewsms.model.SharedDialog;
import com.olympus.viewsms.model.SmsData;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.widget.Widget;
import com.olympus.viewsms.widget.WidgetStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Utils {
    static int prevdialog_id = 0;  //prevdialog_id for step by step open dialog
    public static final int DATA_CHANGED_TEXT = 0;

    //public static final  String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static final String WEAR_PATH_ID = "/path/to/olympus";
    private static final String SENDER = "SENDER";
    private static final String SENDER_NUMBER = "SENDER_NUMBER";
    private static final String CHAT_HISTORY = "HISTORY";
    private static final String BODY = "BODY";
    private static final String THEME = "THEME";
    private static final String TITLE_COLOR = "TITLE_COLOR";
    private static final String CONTENT_COLOR = "CONTENT_COLOR";

    public static HashMap<Integer,String> wearThemesIdsPackagesMap=new HashMap<Integer, String>();

    private static final String[] PHOTO_ID_PROJECTION = new String[]{
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
    };

    private static final String[] PHOTO_BITMAP_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Photo.PHOTO
    };

    public static void alert(Context ctx, String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(ctx);
        bld.setMessage(message);
        bld.setNeutralButton(ctx.getString(R.string.ok), null);
        bld.create().show();
    }

    private static void hasRead(Context context, int index) {
        SharedData.getInstance().receivedMessages.remove(index);
        Utils.updateMyWidgets(context);
    }

    public static void showSMSDialog(Context cxt) {

        if (SharedDialog.getInstance().dlg_reply.is_shown) {
            int size = SharedData.getInstance().receivedMessages.size();
            if (SharedData.getInstance().receivedMessages.get(size - 1).size()>0) {
                if (SharedDialog.getInstance().dlg_reply.senderNumber.equalsIgnoreCase(SharedData.getInstance().receivedMessages.get(size - 1).get(0).getSenderNumber())) {
                    int subSize = SharedData.getInstance().receivedMessages.get(size - 1).size();
                    SmsData smsData = SharedData.getInstance().receivedMessages.get(size - 1).get(subSize - 1);
                    smsData.setType(1);
                    SharedDialog.getInstance().dlg_reply.refreshReplyWindow(smsData);
                    hasRead(cxt, size - 1);
                }
            }

        } else {
            Intent local = new Intent();
            if (SharedDialog.getInstance().dlg_single.is_shown)
                local.setAction(cxt.getString(R.string.finish_action_SMS_Single) + prevdialog_id);
            if (SharedDialog.getInstance().dlg_stack.is_shown)
                local.setAction(cxt.getString(R.string.finish_action_SMS_StackView) + prevdialog_id);
            if (SharedDialog.getInstance().dlg_miss.is_shown)
                local.setAction(cxt.getString(R.string.finish_action_SMS_MissedContact) + prevdialog_id);

            cxt.sendBroadcast(local);
            prevdialog_id = (new Random()).nextInt(); //generate new prevdialog_id

            //show new dialog
            if (SharedData.getInstance().getSizeAll() > 0) {
                Intent in = null;
                if (SharedData.getInstance().getSizeAll() == 1) {             //only one sms
                    in = new Intent(cxt, SharedDialog.getInstance().dlg_single.getClass());
                } else {
                    if (SharedData.getInstance().receivedMessages.size() == 1) {   //all sms are 1 person
                        in = new Intent(cxt, SharedDialog.getInstance().dlg_stack.getClass());
                    } else {
                        in = new Intent(cxt, SharedDialog.getInstance().dlg_miss.getClass());  //more person
                    }
                }
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("prevdialog_id", prevdialog_id);
                cxt.startActivity(in);
                Log.i("===========", "ready to show");
            } else {
                populateRecentChats(cxt);
                Intent in = new Intent(cxt, SharedDialog.getInstance().dlg_miss.getClass());  //more person
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("prevdialog_id", prevdialog_id);
                in.putExtra("recentChats", true);
                cxt.startActivity(in);
            }
        }
    }



    public static Bitmap getScaledSocialBitmap(Context cxt, int sourceId, int size) {
        Bitmap bitmap = null;
        switch (sourceId) {
            case 0:
                bitmap = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.facebook_thumbnail);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.whatsapp_thumbnail);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.viber_thumbnail);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.twitter_thumbnail);
                break;

            default:
                break;
        }


        return Bitmap.createScaledBitmap(bitmap, size, size, false);
    }

    public static void updateMyWidgets(Context context) {
        Log.d("Utilssssss", "Update My Widgets");
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(new ComponentName(context, Widget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(Widget.WIDGET_IDS_KEY, ids);
        updateIntent.putExtra(Widget.WIDGET_ID, "widgetUpdate");
        context.sendBroadcast(updateIntent);
    }

    public static void updateStackWidgets(Context context) {
        Log.d("Utilssssss", "Update Stack Widgets");
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(new ComponentName(context, WidgetStack.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(WidgetStack.WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }

    public static int getLayoutID(Context cxt, String res) {
        return cxt.getResources().getIdentifier(res, "layout", cxt.getPackageName());
    }

    public static Drawable getDrawable(Context cxt, String res) {
        int id;
        try {
            id = cxt.getResources().getIdentifier(res, "drawable", cxt.getPackageName());
            return cxt.getResources().getDrawable(id);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getBitmap(Context cxt, String res) {

        int id = cxt.getResources().getIdentifier(res, "drawable", cxt.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(cxt.getResources(), id);

        return bitmap;
    }

    public static int getDrawableResourceId(Context cxt, String res) {
        int id;
        try {
            id = cxt.getResources().getIdentifier(res, "drawable", cxt.getPackageName());
        } catch (Exception e) {
            return 0;
        }
        return id;
    }


    public static Theme getAppliedTheme(Context cxt, SmsData sms) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
        int theme_id_applyall = prefs.getInt(Constants.SET_FOR_ALL_OR_ALL_OTHERS, 0);
        int theme_id = Constants.DEFAULT_FIRST_THEME_ID;  //default //no any theme

        if (theme_id_applyall != 0) {
            theme_id = theme_id_applyall;
        }

        if (sms != null) {
            ContactAppliedDAO contactDAO = new ContactAppliedDAO(cxt);

            int id = contactDAO.getThemeIdByNumber(sms.getSenderNumber());
            if(id==0){
                id=contactDAO.getThemeIdByNumber(format(sms.getSenderNumber()));
            }
            if (id > 0) {
                theme_id = id;
            }
        }

        Theme theme = (new ThemeDAO(cxt)).getByID(theme_id);
        return theme;
    }

    public static String format(String x) {
        x = x.replace(" ", "");
        x = x.replace("(", "");
        x = x.replace(")", "");
        x = x.replace(".", "");
        x = x.replace("-", "");
        x = x.replace("+", "");
        if(x.length()>5){
            return x.substring(x.length()-5);
        }
        return x;
    }

    public static SmsData getSmsObject(Cursor cursor, Context mContext) {
        String senderNumber = cursor.getString(0);
        String name = retrieveContactName(senderNumber, mContext);
        long date = cursor.getLong(1);
        String body = cursor.getString(2);
        String thumbnailUri = fetchThumbnailUri(senderNumber, mContext);

        if (name == null) {
            name = "";
        }
        return new SmsData(senderNumber, name, body, date, thumbnailUri);
    }

    private static void populateRecentChats(Context context) {
        SharedData.getInstance().fiveRecentConversations.clear();
        Uri mSmsInboxQueryUri = Uri.parse("content://sms");
        Cursor cursor = context.getContentResolver().query(mSmsInboxQueryUri, new String[]{"address", "date", "body"}, null, null, null);

        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            int size = SharedData.getInstance().fiveRecentConversations.size();
            boolean shouldAdd = true;
            if (size >= 5) {
                break;
            } else {
                for (int count = 0; count < size; count++) {
                    String extractedNumber = SharedData.getInstance().fiveRecentConversations.get(count).get(0).getSenderNumber();
                    if (PhoneNumberUtils.compare(address, extractedNumber)) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) {
                    SmsData smsData = getSmsObject(cursor, context);
                    ArrayList<SmsData> smsDataList = new ArrayList<SmsData>();
                    smsDataList.add(smsData);
                    SharedData.getInstance().fiveRecentConversations.add(size, smsDataList);
                }
            }
        }


    }

    public static String retrieveContactName(String number, Context mContext) {
        Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String contactName = "";
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
            cursor.moveToFirst();
            contactName = cursor.getString(0);
        } catch (Exception e) {
            Log.d("Exception", "" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contactName;
    }


    public static String fetchThumbnailUri(String number, Context mContext) {
        final Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(number));
        final Cursor cursor = mContext.getContentResolver().query(uri, PHOTO_ID_PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        String thumbnailUri = null;
        try {

            if (cursor.moveToFirst()) {
                thumbnailUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            }

        } finally {
            cursor.close();
        }
        return thumbnailUri;
    }


    public static void showWatchNotification(Context context, GoogleApiClient mGoogleApiClient, SmsData smsData, Theme cur_theme) {
        int themeId;

        if (cur_theme!=null && isWearThemeSupported(context,cur_theme.getId())) {
            Log.d("WearDebug", "supported for wear");
            themeId = cur_theme.getId();
        } else {
            Log.d("WearDebug", "not supported for wear");
            themeId = Constants.DEFAULT_FIRST_THEME_ID;
        }
        String senderTitle = smsData.getSenderName();
        if (senderTitle==null || senderTitle.length()==0) {
            senderTitle = smsData.getSenderNumber();
        }

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEAR_PATH_ID);
        putDataMapRequest.getDataMap().putString(SENDER, senderTitle);
        putDataMapRequest.getDataMap().putString(SENDER_NUMBER, smsData.getSenderNumber());
        putDataMapRequest.getDataMap().putString(CHAT_HISTORY, loadChatHistory(context, smsData.getSenderNumber()));
        putDataMapRequest.getDataMap().putString(BODY, smsData.getBody());
        putDataMapRequest.getDataMap().putInt(THEME, themeId);
        putDataMapRequest.getDataMap().putString(TITLE_COLOR, cur_theme.getCtitle());
        putDataMapRequest.getDataMap().putString(CONTENT_COLOR, cur_theme.getCtext());

        Log.d("WearDebug", "Watch Notification sent from phone themeId="+themeId);
        PutDataRequest request = putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        Log.d("WearDebug", "putDataItem status: " + dataItemResult.getStatus().toString());
                    }
                });
    }

    private static String loadChatHistory(Context context, String senderNumber) {
        int size = 0;
        String conversationHistory = "";
        String name=null;

        Uri mSmsQueryUri = Uri.parse("content://sms");
        Cursor cursor = context.getContentResolver().query(mSmsQueryUri, new String[]{"address","body", "type"}, null, null, null);
            while (cursor.moveToNext() && size < 10) {
                String address = cursor.getString(0);
                if (address != null && address.equalsIgnoreCase(senderNumber)) {
                    String body = cursor.getString(1);
                    int type = cursor.getInt(2);

                    if (type == 1) {

                        if(name ==null){
                            name=retrieveContactName(address,context);
                        }
                        if(name !=null && name.length()>0){
                            conversationHistory = conversationHistory + name + "\n";
                        }else{
                            conversationHistory = conversationHistory + address + "\n";
                        }

                    } else {
                        conversationHistory += "Me:";
                    }
                    conversationHistory += body;
                    size++;
                }
                conversationHistory = conversationHistory + "\n\n";
            }


        return conversationHistory;
    }

    public static void saveLogsToSdCard(String filename, String value) {
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        FileOutputStream fos;
        byte[] dataToWrite = value.getBytes();
        try {
            fos = new FileOutputStream(file);
            fos.write(dataToWrite);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }
    }

    public static void exportDbToSdCard(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + context.getPackageName() + "/databases/" + "com.olympus.viewsms.sqlite";
                String backupDBPath = "olympus.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public static boolean isAppInstalled(Context activity,String appPackage){
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("WearDebug", "Not Installed=");
           return false;
        }
    }
    public static boolean isWearThemeSupported(Context activity,int themeId){
        if(wearThemesIdsPackagesMap.size()==0){
            SplashActivity.populateWearThemesIdPackages();
        }
        String themePackage=wearThemesIdsPackagesMap.get(themeId);
        Log.d("WearDebug", "ThemePackage="+themePackage);
        if(themePackage !=null && !themePackage.isEmpty() && isAppInstalled(activity,themePackage)){
           return true;
        }
        return false;
    }

}
