package com.connectivityapps.shared;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.connectivityapps.flashlight.R;
import com.connectivityapps.storage.FlashPreferences;
import com.connectivityapps.utils.FontsUtil;
import com.connectivityapps.utils.ScalingUtility;

/**
 * Created by nadirhussain on 03/03/2015.
 */
public class WarningController {
    private Context mContext=null;
    private View warningView=null;
    private Dialog warningDialog=null;
    private CheckBox warningCheckBox=null;
    private int[] textViewId=new int[]{R.id.warningTextView,R.id.warningDescriptionTextView,R.id.dontShowTextView,R.id.acknowledgeTextView};

    public WarningController(Context context){
        this.mContext=context;
        warningView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.warning_layout, null);
        ScalingUtility.getInstance((Activity)mContext).scaleView(warningView);
        initAndApplyFonts();
        warningDialog=new Dialog(mContext,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }
    public void showDialog() {
        warningDialog.setContentView(warningView);
        warningDialog.show();

    }
    public void hideDialog(){
        warningDialog.cancel();
        SettingsController.getInstance().showWarningDialog=!warningCheckBox.isChecked();
        FlashPreferences.getInstance(mContext).showWarningDialogSettings();

    }
    private void initAndApplyFonts(){
       for(int count=0;count<textViewId.length;count++){
           TextView textView=(TextView)warningView.findViewById(textViewId[count]);
           FontsUtil.applyFontToText(mContext,textView,FontsUtil.ROBOTO_REGULAR);

           if(count==(textViewId.length-1)){
               textView.setOnClickListener(acknowledgeClickListener);
           }
       }
        warningCheckBox=(CheckBox)warningView.findViewById(R.id.DontCheckBox);
    }
    private View.OnClickListener acknowledgeClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideDialog();
        }
    };
}
