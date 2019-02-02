package com.prayertimes.qibla.appsourcehub.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

class Utils
{

    private static final String TAG = "SQLiteAssetHelper";

    Utils()
    {
    }

    public static String convertStreamToString(InputStream inputstream)
    {
        return (new Scanner(inputstream)).useDelimiter("\\A").next();
    }

    public static ZipInputStream getFileFromZip(InputStream inputstream)
        throws IOException
    {
        ZipInputStream zipinputstream = new ZipInputStream(inputstream);
        ZipEntry zipentry = zipinputstream.getNextEntry();
        if(zipentry != null)
        {
            Log.w(TAG, (new StringBuilder("extracting file: '")).append(zipentry.getName()).append("'...").toString());
            return zipinputstream;
        } else
        {
            return null;
        }
    }

    public static List splitSqlScript(String s, char c)
    {
        ArrayList arraylist = new ArrayList();
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag = false;
        char ac[] = s.toCharArray();
        int i = 0;
        do
        {
            if(i >= s.length())
            {
                if(stringbuilder.length() > 0)
                {
                    arraylist.add(stringbuilder.toString().trim());
                }
                return arraylist;
            }
            if(ac[i] == '"')
            {
                if(flag)
                {
                    flag = false;
                } else
                {
                    flag = true;
                }
            }
            if(ac[i] == c && !flag)
            {
                if(stringbuilder.length() > 0)
                {
                    arraylist.add(stringbuilder.toString().trim());
                    stringbuilder = new StringBuilder();
                }
            } else
            {
                stringbuilder.append(ac[i]);
            }
            i++;
        } while(true);
    }

    public static void writeExtractedFileToDisk(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        byte abyte0[] = new byte[1024];
        do
        {
            int i = inputstream.read(abyte0);
            if(i <= 0)
            {
                outputstream.flush();
                outputstream.close();
                inputstream.close();
                return;
            }
            outputstream.write(abyte0, 0, i);
        } while(true);
    }

}
