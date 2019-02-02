package androidwarriors.bgimagecaptureapp.configurations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidwarriors.bgimagecaptureapp.R;

/**
 * Created by nhussain on 5/29/2016.
 */
public class ConfigActivity extends AppCompatActivity {
    private Switch flashSwitch = null;
    private Spinner intervalSpinner = null;
    private Spinner sizeSpinner = null;
    public static float[] intervalFactorList = new float[]{1.0f, 3.0f, 5.0f, 10.0f, 30.0f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configurations_screen);
        initViewsClicks();
    }

    private void initViewsClicks() {
        intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        flashSwitch = (Switch) findViewById(R.id.flashSwitchView);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonClickListener);

        int intPrevIndex = ConfigPreferences.getInstance(this).getIntervalIndex();
        int sizePrevIndex = ConfigPreferences.getInstance(this).getSizeIndex();
        intervalSpinner.setSelection(intPrevIndex);
        sizeSpinner.setSelection(sizePrevIndex);

        flashSwitch.setChecked(ConfigPreferences.getInstance(this).getFlashSwitchState());
    }

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            save();
        }
    };

    private void save() {
        int currentIntIndex = intervalSpinner.getSelectedItemPosition();
        int currentSizeIndex = sizeSpinner.getSelectedItemPosition();

        ConfigPreferences.getInstance(this).saveIntervalIndex(currentIntIndex);
        ConfigPreferences.getInstance(this).saveSizeIndex(currentSizeIndex);
        ConfigPreferences.getInstance(this).saveFlashSwitchState(flashSwitch.isChecked());
        finish();
    }
}
