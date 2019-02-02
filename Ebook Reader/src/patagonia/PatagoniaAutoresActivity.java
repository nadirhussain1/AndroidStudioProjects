package patagonia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import net.nightwhistler.pageturner.R;

public class PatagoniaAutoresActivity extends FragmentActivity {

    private final String LOG_TAG = PatagoniaAutoresActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patagonia_autores);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
