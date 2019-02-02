package com.olympus.viewsms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.olympus.viewsms.data.DBAdapter;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.fragment.MoreThemeFragment;
import com.olympus.viewsms.fragment.MyThemeFragment;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Utils;

import java.util.ArrayList;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
		
		if(DBAdapter.getExistingConnection()!=null){
			DBAdapter.getExistingConnection().destroyDatabaseInstance();
		}


	}

    @Override
    protected void onResume() {
        super.onResume();

        MoreThemeFragment.themes=new ArrayList<Theme>();
        MyThemeFragment.themes=new ArrayList<Theme>();
        MoreThemeFragment.themes = new ThemeDAO(this).getMoreTheme();
        MyThemeFragment.themes=new ThemeDAO(this).getAvailableTheme();

        populateWearThemesIdPackages();

        new SplashCounter(1000,2000).start();
    }
    public static void populateWearThemesIdPackages(){
        Utils.wearThemesIdsPackagesMap.clear();
        Utils.wearThemesIdsPackagesMap.put(22,"com.olympusthemes.adventure");
        Utils.wearThemesIdsPackagesMap.put(23,"com.olympusthemes.southpark");
        Utils.wearThemesIdsPackagesMap.put(24,"com.olympusthemes.dumbways");
        Utils.wearThemesIdsPackagesMap.put(25,"com.olympusthemes.eleblue");
        Utils.wearThemesIdsPackagesMap.put(26,"com.olympusthemes.elegreen");
        Utils.wearThemesIdsPackagesMap.put(27,"com.olympusthemes.elepurple");
        Utils.wearThemesIdsPackagesMap.put(28,"com.olympusthemes.titanfall");
        Utils.wearThemesIdsPackagesMap.put(8,"com.olympusthemes.amazingroyal");
    }

    private void startMainActivity(){
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        SplashActivity.this.finish();
    }
    public class SplashCounter extends CountDownTimer{

       public SplashCounter(long tick,long total){
           super(tick,total);
       }
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            startMainActivity();
        }
    }

}
