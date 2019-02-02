package patagonia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import net.nightwhistler.pageturner.R;

public class PatagoniaAutorActivity extends FragmentActivity {

    public static final String EXTRA_PATAGONIA_AUTHOR_NAME = "patagonia_author_name";

    public static Intent newIntent(Context packageContext, String authorName) {
        Intent intent = new Intent(packageContext, PatagoniaAutorActivity.class);
        intent.putExtra(EXTRA_PATAGONIA_AUTHOR_NAME, authorName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patagonia_autor);
    }

}
