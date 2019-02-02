package com.prayertimes.qibla.appsourcehub.adpater;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import muslim.prayers.time.R;
public class AdapterMenu extends BaseAdapter
{

    ArrayList arr_title;
    Context context;
    Integer image_menu[];
    LayoutInflater inflater;
    int pos;

    public AdapterMenu(Context context1, ArrayList arraylist)
    {
        pos = 0;
        Integer ainteger[] = new Integer[12];
        ainteger[0] = Integer.valueOf(R.drawable.ic_prayer);
        ainteger[1] = Integer.valueOf(R.drawable.ic_ramadan_prayer);
        ainteger[2] = Integer.valueOf(R.drawable.ic_more);
        ainteger[3] = Integer.valueOf(R.drawable.ic_rate);
        ainteger[4] = Integer.valueOf(R.drawable.ic_share);
        ainteger[5] = Integer.valueOf(R.drawable.ic_qibla);
        ainteger[6] = Integer.valueOf(R.drawable.ic_facebook);
        ainteger[7] = Integer.valueOf(R.drawable.ic_twitter);
        ainteger[8] = Integer.valueOf(R.drawable.ic_question);
        ainteger[9] = Integer.valueOf(R.drawable.ic_feedback);
        ainteger[10] = Integer.valueOf(R.drawable.ic_about);
        ainteger[11] = Integer.valueOf(R.drawable.ic_close);
        image_menu = ainteger;
        context = context1;
        inflater = (LayoutInflater)context1.getSystemService("layout_inflater");
        arr_title = arraylist;
    }

    public int getCount()
    {
        return arr_title.size();
    }

    public Object getItem(int i)
    {
        return Integer.valueOf(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = inflater.inflate(R.layout.category_customlist, viewgroup, false);
        TextView textview = (TextView)view1.findViewById(R.id.title);
        ImageView imageview = (ImageView)view1.findViewById(R.id.img_active);
        ((ImageView)view1.findViewById(R.id.img_icon)).setBackgroundResource(image_menu[i].intValue());
        textview.setText((CharSequence)arr_title.get(i));
        if(pos == i)
        {
            imageview.setVisibility(0);
            view1.setBackgroundResource(R.drawable.apptheme_list_selector_pressed);
            return view1;
        } else
        {
            imageview.setVisibility(4);
            view1.setBackgroundResource(0);
            return view1;
        }
    }

    public void setSelection(int i)
    {
        pos = i;
        notifyDataSetChanged();
    }
}
