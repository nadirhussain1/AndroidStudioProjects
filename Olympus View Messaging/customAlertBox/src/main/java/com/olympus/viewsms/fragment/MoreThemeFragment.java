package com.olympus.viewsms.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.olympus.viewsms.MainActivity;
import com.olympus.viewsms.R;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.billings.IabHelper;
import com.olympus.viewsms.util.billings.IabResult;
import com.olympus.viewsms.util.billings.Purchase;
import com.olympus.viewsms.view.CustomPagerTransformer;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.List;

public class MoreThemeFragment extends Fragment {

    private static final String[] CONTENT = new String[]{"POPULAR THEMES", "COUNTRY THEMES", "UNIQUE THEMES"};
    public static final int RC_REQUEST = 10001;

    // Debug tag, for logging
    private static final String TAG = "MoreThemeFragment";

    private ThemeDAO themeDAO;
    private MainActivity main_activity;
    private View view;
    public  static List<Theme> themes;
    private Theme buying_theme = null;
    private ThemeFragment currentThemeFragment = null;
    //in-app
    public boolean in_app_enable;
    public IabHelper mHelper;

    private FragmentStatePagerAdapter adapter_pager;
    private int cur_page_index = 0;

    private ProgressDialog pd;


    public static MoreThemeFragment newInstance(int cur_page_index) {
        MoreThemeFragment fragment = new MoreThemeFragment();
        fragment.cur_page_index = cur_page_index;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_activity = ((MainActivity) getActivity());

      themeDAO = new ThemeDAO(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_moretheme, container, false);


        view.findViewById(R.id.loadingBar).setVisibility(View.GONE);

        //tab pager
        adapter_pager = new ThemeTabAdapter(main_activity.getSupportFragmentManager());
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setPageTransformer(true,new CustomPagerTransformer());
        pager.setAdapter(adapter_pager);
        TitlePageIndicator indicator = (TitlePageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setCurrentItem(cur_page_index);

        //in-app
//		mHelper = new IabHelper(getActivity(), getString(R.string.inapp_license_key));
//		// Start setup. This is asynchronous and the specified listener will be called once setup completes.
//        Log.d("MoreThemeFragment", "Starting setup.");
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result) {
//                if (!result.isSuccess()) {	// Oh noes, there was a problem.
//                	Utils.alert(getActivity(), getString(R.string.inapp_not_supported));
//                	in_app_enable=false;
//                	return;
//                }
//                in_app_enable=true;
//                pd=ProgressDialog.show(getActivity(), null, getString(R.string.checking_product), true, false);
//                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
//                List additionalSkuList = new ArrayList();
//                for(Theme theme:themes) {
//                	if(theme.getProduct_id()!=null && !theme.getProduct_id().equals(""))
//                		additionalSkuList.add(theme.getProduct_id());
//                }
//                mHelper.queryInventoryAsync(true, additionalSkuList, mQueryFinishedListener);
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                //mHelper.queryInventoryAsync(mGotInventoryListener);
//            }
//        });
//
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    class ThemeTabAdapter extends FragmentStatePagerAdapter {
        public ThemeTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            cur_page_index = position;
            currentThemeFragment = ThemeFragment.newInstance(MoreThemeFragment.this, position);
            return currentThemeFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    public void launchPurchaseFlow(Theme buying_theme) {
        String payload = "";
        this.buying_theme = buying_theme;
        mHelper.launchPurchaseFlow(getActivity(), buying_theme.getProduct_id(), RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
        if (set)
            pd = ProgressDialog.show(getActivity(), null, getString(R.string.plz_wait), true, true);
        else pd.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } finally {
            }
        }
        mHelper = null;
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    /*IabHelper.QueryInventoryFinishedListener mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        	pd.dismiss();

        	Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                //alert("Failed to query inventory: " + result);
                Utils.alert(getActivity(), "Failed to query inventory!");
                return;
            }

            //Query inventory was successful.
            *//*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             *//*
            int i=0;
            while(i<themes.size())
            {
            	Theme theme=themes.get(i);
            	Purchase infiniteGasPurchase = inventory.getPurchase(theme.getProduct_id());
            	boolean is_paid = (infiniteGasPurchase != null && verifyDeveloperPayload(infiniteGasPurchase));
            	if(is_paid)
            	{
            		//theme.setIs_paid(1);
            		//themeDAO.updateTheme(theme);
            		themeDAO.updateThemeIs_paid(theme.getId(), 1);
            		themes.remove(i);
            	}
            	else i++;
            }

            ThemeFragment theme_frag=(ThemeFragment)adapter_pager.getItem(cur_page_index);
            if(theme_frag!=null && theme_frag.adapter!=null)
            	theme_frag.adapter.notifyDataSetChanged();
            //setWaitScreen(false);
        }
    };*/

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         * 
         * WARNING: Locally generating a random string when starting a purchase and 
         * verifying it here might seem like a good approach, but this will fail in the 
         * case where the user purchases an item on one device and then uses your app on 
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         * 
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         * 
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on 
         *    one device work on other devices owned by the user).
         * 
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */
        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            /*if (result.isFailure()) {
                alert("Error purchasing: " + result);
            	//alert("Error purchasing!");
                //setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                alert("Error purchasing. Authenticity verification failed.");
            	//alert("Error purchasing!");
            	//setWaitScreen(false);
                return;
            }
            
            //Purchase successful.
            for(Theme theme:themes)
            {
            	if (purchase.getSku().equals(theme.getProduct_id())) {
	                alert("Thank you for purchase! You can apply this theme now");
	                getTheme(theme);
	                break;
	            }
            }*/
            //
            if (purchase != null && purchase.getPurchaseState() == 0 && buying_theme != null) {
                getTheme(buying_theme);
            }
        }
    };

    //get theme after buy
    public void getTheme(Theme theme) {
        themeDAO.updateThemeIs_paid(theme.getId(), 1);
        Toast.makeText(getActivity(), theme.getName() + " " + getString(R.string.theme_added), Toast.LENGTH_SHORT).show();

        //main_activity.OnClickMoreTheme();
    }


}
