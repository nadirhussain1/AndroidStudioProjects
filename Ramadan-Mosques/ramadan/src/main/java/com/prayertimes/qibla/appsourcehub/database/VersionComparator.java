package com.prayertimes.qibla.appsourcehub.database;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

class VersionComparator
    implements Comparator
{

    private static final String TAG = "SQLiteAssetHelper";
    private Pattern pattern;

    VersionComparator()
    {
        pattern = Pattern.compile(".*_upgrade_([0-9]+)-([0-9]+).*");
    }

    public int compare(Object obj, Object obj1)
    {
        return compare((String)obj, (String)obj1);
    }

    public int compare(String s, String s1)
    {
        byte byte0;
        int i;
        int j;
        int k;
        int l;
        byte0 = -1;
        Matcher matcher = pattern.matcher(s);
        Matcher matcher1 = pattern.matcher(s1);
        if(!matcher.matches())
        {
            Log.w(TAG, (new StringBuilder("could not parse upgrade script file: ")).append(s).toString());
            throw new SQLiteAssetHelper.SQLiteAssetException("Invalid upgrade script file");
        }
        if(!matcher1.matches())
        {
            Log.w(TAG, (new StringBuilder("could not parse upgrade script file: ")).append(s1).toString());
            throw new SQLiteAssetHelper.SQLiteAssetException("Invalid upgrade script file");
        }
        i = Integer.valueOf(matcher.group(1)).intValue();
        j = Integer.valueOf(matcher1.group(1)).intValue();
        k = Integer.valueOf(matcher.group(2)).intValue();
        l = Integer.valueOf(matcher1.group(2)).intValue();
        if(i != j){
        	if(i >= j)
                return 1;
        	return byte0;
        }
        if(k != l){
        	if(k >= l)
                return 1;
            return byte0;
        }
        byte0 = 0;
        return byte0;
    }
}
