package com.prayertimes.qibla.appsourcehub.preference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.*;
import android.util.Log;
import java.lang.reflect.*;

public class PreferenceManagerCompat
{
    static interface OnPreferenceTreeClickListener
    {

        public abstract boolean onPreferenceTreeClick(PreferenceScreen preferencescreen, Preference preference);
    }


    private static final String TAG = "PreferenceManagerCompat";

    public PreferenceManagerCompat()
    {
    }

    static void dispatchActivityDestroy(PreferenceManager preferencemanager)
    {
        try
        {
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("dispatchActivityDestroy", new Class[0]);
            method.setAccessible(true);
            method.invoke(preferencemanager, new Object[0]);
            return;
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.dispatchActivityDestroy by reflection", exception);
        }
    }

    static void dispatchActivityResult(PreferenceManager preferencemanager, int i, int j, Intent intent)
    {
        try
        {
            Class aclass[] = new Class[3];
            aclass[0] = Integer.TYPE;
            aclass[1] = Integer.TYPE;
            aclass[2] = android.content.Intent.class;
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("dispatchActivityResult", aclass);
            method.setAccessible(true);
            Object aobj[] = new Object[3];
            aobj[0] = Integer.valueOf(i);
            aobj[1] = Integer.valueOf(j);
            aobj[2] = intent;
            method.invoke(preferencemanager, aobj);
            return;
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.dispatchActivityResult by reflection", exception);
        }
    }

    static void dispatchActivityStop(PreferenceManager preferencemanager)
    {
        try
        {
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("dispatchActivityStop", new Class[0]);
            method.setAccessible(true);
            method.invoke(preferencemanager, new Object[0]);
            return;
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.dispatchActivityStop by reflection", exception);
        }
    }

    static PreferenceScreen getPreferenceScreen(PreferenceManager preferencemanager)
    {
        PreferenceScreen preferencescreen;
        try
        {
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("getPreferenceScreen", new Class[0]);
            method.setAccessible(true);
            preferencescreen = (PreferenceScreen)method.invoke(preferencemanager, new Object[0]);
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.getPreferenceScreen by reflection", exception);
            return null;
        }
        return preferencescreen;
    }

    static PreferenceScreen inflateFromIntent(PreferenceManager preferencemanager, Intent intent, PreferenceScreen preferencescreen)
    {
        PreferenceScreen preferencescreen1;
        try
        {
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("inflateFromIntent", new Class[] {
                android.content.Intent.class, android.preference.PreferenceScreen.class
            });
            method.setAccessible(true);
            preferencescreen1 = (PreferenceScreen)method.invoke(preferencemanager, new Object[] {
                intent, preferencescreen
            });
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.inflateFromIntent by reflection", exception);
            return null;
        }
        return preferencescreen1;
    }

    static PreferenceScreen inflateFromResource(PreferenceManager preferencemanager, Activity activity, int i, PreferenceScreen preferencescreen)
    {
        PreferenceScreen preferencescreen1;
        try
        {
            Class aclass[] = new Class[3];
            aclass[0] = android.content.Context.class;
            aclass[1] = Integer.TYPE;
            aclass[2] = android.preference.PreferenceScreen.class;
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("inflateFromResource", aclass);
            method.setAccessible(true);
            Object aobj[] = new Object[3];
            aobj[0] = activity;
            aobj[1] = Integer.valueOf(i);
            aobj[2] = preferencescreen;
            preferencescreen1 = (PreferenceScreen)method.invoke(preferencemanager, aobj);
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.inflateFromResource by reflection", exception);
            return null;
        }
        return preferencescreen1;
    }

    static PreferenceManager newInstance(Activity activity, int i)
    {
        PreferenceManager preferencemanager;
        try
        {
            Class aclass[] = new Class[2];
            aclass[0] = android.app.Activity.class;
            aclass[1] = Integer.TYPE;
            Constructor constructor = android.preference.PreferenceManager.class.getDeclaredConstructor(aclass);
            constructor.setAccessible(true);
            Object aobj[] = new Object[2];
            aobj[0] = activity;
            aobj[1] = Integer.valueOf(i);
            preferencemanager = (PreferenceManager)constructor.newInstance(aobj);
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call constructor PreferenceManager by reflection", exception);
            return null;
        }
        return preferencemanager;
    }

    static void setFragment(PreferenceManager preferencemanager, PreferenceFragment preferencefragment)
    {
    }

    static void setOnPreferenceTreeClickListener(PreferenceManager preferencemanager, final OnPreferenceTreeClickListener listener)
    {
        Field field;
        try
        {
	        field = android.preference.PreferenceManager.class.getDeclaredField("mOnPreferenceTreeClickListener");
	        field.setAccessible(true);
	        if(listener != null)
	        {
                ClassLoader classloader = field.getType().getClassLoader();
                Class aclass[] = new Class[1];
                aclass[0] = field.getType();
                field.set(preferencemanager, Proxy.newProxyInstance(classloader, aclass, new InvocationHandler() {
                    public Object invoke(Object obj, Method method, Object aobj[])
                    {
                        if(method.getName().equals("onPreferenceTreeClick"))
                        {
                            return Boolean.valueOf(listener.onPreferenceTreeClick((PreferenceScreen)aobj[0], (Preference)aobj[1]));
                        } else
                        {
                            return null;
                        }
                    }
                }));
                return;
            }
	        field.set(preferencemanager, null);
        }catch(Exception exception){
            Log.w(TAG, "Couldn't set PreferenceManager.mOnPreferenceTreeClickListener by reflection", exception);
        }
    }

    static boolean setPreferences(PreferenceManager preferencemanager, PreferenceScreen preferencescreen)
    {
        boolean flag;
        try
        {
            Method method = android.preference.PreferenceManager.class.getDeclaredMethod("setPreferences", new Class[] {
                android.preference.PreferenceScreen.class
            });
            method.setAccessible(true);
            flag = ((Boolean)method.invoke(preferencemanager, new Object[] {
                preferencescreen
            })).booleanValue();
        }
        catch(Exception exception)
        {
            Log.w(TAG, "Couldn't call PreferenceManager.setPreferences by reflection", exception);
            return false;
        }
        return flag;
    }

}
