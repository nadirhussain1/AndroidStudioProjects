package patagonia;

import com.parse.ui.ParseLoginDispatchActivity;

import net.nightwhistler.pageturner.activity.LibraryActivity;

/**
 * Created by rod on 9/30/15.
 */
public class LoginDispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return LibraryActivity.class;
    }
}
