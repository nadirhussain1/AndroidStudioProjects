package com.prayertimes.qibla.appsourcehub.preference;

import android.content.Intent;
import android.os.*;
import android.preference.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import muslim.prayers.time.R;
public abstract class PreferenceFragment extends Fragment
    implements PreferenceManagerCompat.OnPreferenceTreeClickListener
{
    public static interface OnPreferenceStartFragmentCallback
    {

        public abstract boolean onPreferenceStartFragment(PreferenceFragment preferencefragment, Preference preference);
    }


    private static final int FIRST_REQUEST_CODE = 100;
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private Handler mHandler;
    private boolean mHavePrefs;
    private boolean mInitDone;
    private ListView mList;
    private android.view.View.OnKeyListener mListOnKeyListener;
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus = new Runnable() {
        public void run()
        {
            mList.focusableViewAvailable(mList);
        }
    };

    public PreferenceFragment()
    {
        mHandler = new Handler() {
            public void handleMessage(Message message)
            {
                switch(message.what)
                {
                default:
                    return;

                case 1: // '\001'
                    bindPreferences();
                    break;
                }
            }
        };
        mListOnKeyListener = new android.view.View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyevent)
            {
                if(mList.getSelectedItem() instanceof Preference)
                {
                    mList.getSelectedView();
                }
                return false;
            }
        };
    }

    private void bindPreferences()
    {
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        if(preferenceScreen != null)
        {
            preferenceScreen.bind(getListView());
        }
        if(android.os.Build.VERSION.SDK_INT <= 10)
        {
            getListView().setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView adapterview, View view, int i, long l)
                {
                    if(adapterview instanceof ListView)
                    {
                        i -= ((ListView)adapterview).getHeaderViewsCount();
                    }
                    Object obj = preferenceScreen.getRootAdapter().getItem(i);
                    if(!(obj instanceof Preference))
                    {
                        return;
                    }
                    Preference preference = (Preference)obj;
                    try
                    {
                        Method method = android.preference.Preference.class.getDeclaredMethod("performClick", new Class[] {
                            android.preference.PreferenceScreen.class
                        });
                        method.setAccessible(true);
                        Object aobj[] = new Object[1];
                        aobj[0] = preferenceScreen;
                        method.invoke(preference, aobj);
                        return;
                    }
                    catch(InvocationTargetException invocationtargetexception)
                    {
                        return;
                    }
                    catch(IllegalAccessException illegalaccessexception)
                    {
                        return;
                    }
                    catch(NoSuchMethodException nosuchmethodexception)
                    {
                        return;
                    }
                }
            });
        }
    }

    private void ensureList()
    {
        if(mList != null)
        {
            return;
        }
        View view = getView();
        if(view == null)
        {
            throw new IllegalStateException("Content view not yet created");
        }
        View view1 = view.findViewById(0x102000a);
        if(!(view1 instanceof ListView))
        {
            throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView cl" +
"ass"
);
        }
        mList = (ListView)view1;
        if(mList == null)
        {
            throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
        } else
        {
            mList.setOnKeyListener(mListOnKeyListener);
            mHandler.post(mRequestFocus);
            return;
        }
    }

    private void postBindPreferences()
    {
        if(mHandler.hasMessages(1))
        {
            return;
        } else
        {
            mHandler.obtainMessage(1).sendToTarget();
            return;
        }
    }

    private void requirePreferenceManager()
    {
        if(mPreferenceManager == null)
        {
            throw new RuntimeException("This should be called after super.onCreate.");
        } else
        {
            return;
        }
    }

    public void addPreferencesFromIntent(Intent intent)
    {
        requirePreferenceManager();
        setPreferenceScreen(PreferenceManagerCompat.inflateFromIntent(mPreferenceManager, intent, getPreferenceScreen()));
    }

    public void addPreferencesFromResource(int i)
    {
        requirePreferenceManager();
        setPreferenceScreen(PreferenceManagerCompat.inflateFromResource(mPreferenceManager, getActivity(), i, getPreferenceScreen()));
    }

    public Preference findPreference(CharSequence charsequence)
    {
        if(mPreferenceManager == null)
        {
            return null;
        } else
        {
            return mPreferenceManager.findPreference(charsequence);
        }
    }

    public ListView getListView()
    {
        ensureList();
        return mList;
    }

    public PreferenceManager getPreferenceManager()
    {
        return mPreferenceManager;
    }

    public PreferenceScreen getPreferenceScreen()
    {
        return PreferenceManagerCompat.getPreferenceScreen(mPreferenceManager);
    }

    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        if(mHavePrefs)
        {
            bindPreferences();
        }
        mInitDone = true;
        if(bundle != null)
        {
            Bundle bundle1 = bundle.getBundle("android:preferences");
            if(bundle1 != null)
            {
                PreferenceScreen preferencescreen = getPreferenceScreen();
                if(preferencescreen != null)
                {
                    preferencescreen.restoreHierarchyState(bundle1);
                }
            }
        }
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        PreferenceManagerCompat.dispatchActivityResult(mPreferenceManager, i, j, intent);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mPreferenceManager = PreferenceManagerCompat.newInstance(getActivity(), 100);
        PreferenceManagerCompat.setFragment(mPreferenceManager, this);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.preference_list_fragment, viewgroup, false);
    }

    public void onDestroy()
    {
        super.onDestroy();
        PreferenceManagerCompat.dispatchActivityDestroy(mPreferenceManager);
    }

    public void onDestroyView()
    {
        mList = null;
        mHandler.removeCallbacks(mRequestFocus);
        mHandler.removeMessages(1);
        super.onDestroyView();
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen, Preference preference)
    {
        if(getActivity() instanceof OnPreferenceStartFragmentCallback)
        {
            return ((OnPreferenceStartFragmentCallback)getActivity()).onPreferenceStartFragment(this, preference);
        } else
        {
            return false;
        }
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        PreferenceScreen preferencescreen = getPreferenceScreen();
        if(preferencescreen != null)
        {
            Bundle bundle1 = new Bundle();
            preferencescreen.saveHierarchyState(bundle1);
            bundle.putBundle("android:preferences", bundle1);
        }
    }

    public void onStart()
    {
        super.onStart();
        PreferenceManagerCompat.setOnPreferenceTreeClickListener(mPreferenceManager, this);
    }

    public void onStop()
    {
        super.onStop();
        PreferenceManagerCompat.dispatchActivityStop(mPreferenceManager);
        PreferenceManagerCompat.setOnPreferenceTreeClickListener(mPreferenceManager, null);
    }

    public void setPreferenceScreen(PreferenceScreen preferencescreen)
    {
        if(PreferenceManagerCompat.setPreferences(mPreferenceManager, preferencescreen) && preferencescreen != null)
        {
            mHavePrefs = true;
            if(mInitDone)
            {
                postBindPreferences();
            }
        }
    }


}
