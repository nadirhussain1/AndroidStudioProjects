package com.prayertimes.qibla.appsourcehub.adpater;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.prayertimes.qibla.appsourcehub.model.QuranScript;
import com.prayertimes.qibla.appsourcehub.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import muslim.prayers.time.R;
public class IndividualChapterAdapter extends BaseAdapter
{

    int ar_font;
    ArrayList arabic;
    Context context;
    List dbfr;
    List dbgrm;
    List dbindo;
    List dblist;
    List dblist1;
    List dbmal;
    List dbspan;
    List dbtru;
    List dbur;
    int en_font;
    protected int style[] = {
        R.style.FontStyle_Small, R.style.FontStyle_Medium, R.style.FontStyle_Large
    };
    protected int styleheader[] = {
        R.style.FontStyle_title_small, R.style.FontStyle_title_medium, R.style.FontStyle_title_large
    };
    Typeface tf;
    String translation;

    public IndividualChapterAdapter(Context context1, List list, ArrayList arraylist, List list1, List list2, List list3, List list4, 
            List list5, List list6, List list7, List list8, int i, int j, Typeface typeface)
    {
        translation = "";
        context = context1;
        dblist = list;
        arabic = arraylist;
        dblist1 = list1;
        dbfr = list2;
        dbgrm = list3;
        dbindo = list4;
        dbmal = list5;
        dbspan = list6;
        dbtru = list7;
        dbur = list8;
        ar_font = i;
        en_font = j;
        tf = typeface;
        LogUtils.i((new StringBuilder("french ")).append(list2.size()).toString());
    }

    public int getCount()
    {
        return arabic.size();
    }

    public Object getItem(int i)
    {
        return null;
    }

    public long getItemId(int i)
    {
        return 0L;
    }

    public void getText(String s)
    {
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            ((ClipboardManager)context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", s));
        } else
        {
            ((android.text.ClipboardManager)context.getSystemService("clipboard")).setText(s);
        }
        Toast.makeText(context, getString(R.string.toast_copiedtoclipboard), 0).show();
    }

    private CharSequence getString(int toastCopiedtoclipboard) {
		// TODO Auto-generated method stub
		return null;
	}

	public View getView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.quransinglechapterlistitem, null);
        final TextView tv_english = (TextView)view1.findViewById(R.id.tv_ayah_english);
        TextView textview = (TextView)view1.findViewById(R.id.tv_ayah_arabic);
        TextView textview1 = (TextView)view1.findViewById(R.id.tv_ayah_transliteration);
        final TextView tv_ayah_french = (TextView)view1.findViewById(R.id.tv_ayah_french);
        final TextView tv_ayah_german = (TextView)view1.findViewById(R.id.tv_ayah_german);
        final TextView tv_ayah_indonesian = (TextView)view1.findViewById(R.id.tv_ayah_indonesian);
        final TextView tv_ayah_malay = (TextView)view1.findViewById(R.id.tv_ayah_malay);
        final TextView tv_ayah_spanish = (TextView)view1.findViewById(R.id.tv_ayah_spanish);
        final TextView tv_ayah_turkish = (TextView)view1.findViewById(R.id.tv_ayah_turkish);
        final TextView tv_ayah_urdu = (TextView)view1.findViewById(R.id.tv_ayah_urdu);
        textview.setTypeface(tf);
        tv_english.setText((new StringBuilder("\n English \n")).append(((QuranScript)dblist1.get(i)).text).toString());
        textview.setText((CharSequence)arabic.get(i));
        textview1.setText((new StringBuilder(String.valueOf(i + 1))).append(".").append(((QuranScript)dblist.get(i)).text).toString());
        tv_ayah_french.setVisibility(0);
        tv_english.setTextAppearance(context, style[en_font]);
        textview.setTextAppearance(context, style[ar_font]);
        textview1.setTextAppearance(context, style[en_font]);
        tv_ayah_french.setTextAppearance(context, style[en_font]);
        tv_ayah_german.setTextAppearance(context, style[en_font]);
        tv_ayah_indonesian.setTextAppearance(context, style[en_font]);
        tv_ayah_malay.setTextAppearance(context, style[en_font]);
        tv_ayah_spanish.setTextAppearance(context, style[en_font]);
        tv_ayah_turkish.setTextAppearance(context, style[en_font]);
        tv_ayah_urdu.setTextAppearance(context, style[en_font]);
        if(dbfr.size() > 0)
        {
            LogUtils.i((new StringBuilder(" french ")).append(((QuranScript)dbfr.get(i)).text).toString());
            tv_ayah_french.setVisibility(0);
            tv_ayah_french.setText((new StringBuilder("\n French \n")).append(((QuranScript)dbfr.get(i)).text).toString());
        } else
        {
            tv_ayah_french.setVisibility(8);
        }
        if(dbgrm.size() > 0)
        {
            tv_ayah_german.setVisibility(0);
            tv_ayah_german.setText((new StringBuilder("\n German \n")).append(((QuranScript)dbgrm.get(i)).text).toString());
        } else
        {
            tv_ayah_german.setVisibility(8);
        }
        if(dbindo.size() > 0)
        {
            tv_ayah_indonesian.setVisibility(0);
            tv_ayah_indonesian.setText((new StringBuilder("\n Indonesian \n")).append(((QuranScript)dbindo.get(i)).text).toString());
        } else
        {
            tv_ayah_indonesian.setVisibility(8);
        }
        if(dbmal.size() > 0)
        {
            tv_ayah_malay.setVisibility(0);
            tv_ayah_malay.setText((new StringBuilder("\n Malay \n")).append(((QuranScript)dbmal.get(i)).text).toString());
        } else
        {
            tv_ayah_malay.setVisibility(8);
        }
        if(dbspan.size() > 0)
        {
            tv_ayah_spanish.setVisibility(0);
            tv_ayah_spanish.setText((new StringBuilder("\n Spanish \n")).append(((QuranScript)dbspan.get(i)).text).toString());
        } else
        {
            tv_ayah_spanish.setVisibility(8);
        }
        if(dbtru.size() > 0)
        {
            tv_ayah_turkish.setVisibility(0);
            tv_ayah_turkish.setText((new StringBuilder("\n Turkish \n")).append(((QuranScript)dbtru.get(i)).text).toString());
        } else
        {
            tv_ayah_turkish.setVisibility(8);
        }
        if(dbur.size() > 0)
        {
            tv_ayah_urdu.setVisibility(0);
            tv_ayah_urdu.setText((new StringBuilder("\n Urdu \n")).append(((QuranScript)dbur.get(i)).text).toString());
        } else
        {
            tv_ayah_urdu.setVisibility(8);
        }
        tv_english.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_english.getText().toString());
            }
        });
        tv_ayah_french.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_french.getText().toString());
            }
        });
        tv_ayah_german.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_german.getText().toString());
            }
        });
        tv_ayah_indonesian.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_indonesian.getText().toString());
            }
        });
        tv_ayah_malay.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_malay.getText().toString());
            }
        });
        tv_ayah_spanish.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_spanish.getText().toString());
            }
        });
        tv_ayah_turkish.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_turkish.getText().toString());
            }
        });
        tv_ayah_urdu.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                getText(tv_ayah_urdu.getText().toString());
            }
        });
        return view1;
    }
}
