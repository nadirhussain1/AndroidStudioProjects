package com.prayertimes.qibla.appsourcehub.adpater;

import android.content.Context;
import android.view.*;
import android.widget.*;
import muslim.prayers.time.R;
public class AdapterCompass extends BaseAdapter
{

    String arr_title[] = {
        "Default Compass", "Compass One", "Compass Two", "Compass Three", "Compass Four", "Compass Five", "Compass Six", "Compass Seven", "Compass Eight", "Compass Nine", 
        "Compass Ten", "Compass Eleven", "Compass Twelve", "Compass Thirteen", "Compass Fourteen"
    };
    Context context;
    Integer image_menu[];
    int pos;

    public AdapterCompass(Context context1)
    {
        pos = 0;
        Integer ainteger[] = new Integer[15];
        ainteger[0] = Integer.valueOf(R.drawable.default_compass);
        ainteger[1] = Integer.valueOf(R.drawable.needle1_thumb);
        ainteger[2] = Integer.valueOf(R.drawable.needle2_thumb);
        ainteger[3] = Integer.valueOf(R.drawable.needle3_thumb);
        ainteger[4] = Integer.valueOf(R.drawable.needle4_thumb);
        ainteger[5] = Integer.valueOf(R.drawable.needle5_thumb);
        ainteger[6] = Integer.valueOf(R.drawable.needle6_thumb);
        ainteger[7] = Integer.valueOf(R.drawable.needle7_thumb);
        ainteger[8] = Integer.valueOf(R.drawable.needle8_thumb);
        ainteger[9] = Integer.valueOf(R.drawable.needle9_thumb);
        ainteger[10] = Integer.valueOf(R.drawable.needle10_thumb);
        ainteger[11] = Integer.valueOf(R.drawable.needle11_thumb);
        ainteger[12] = Integer.valueOf(R.drawable.needle12_thumb);
        ainteger[13] = Integer.valueOf(R.drawable.needle13_thumb);
        ainteger[14] = Integer.valueOf(R.drawable.needle14_thumb);
        image_menu = ainteger;
        context = context1;
    }

    public int getCount()
    {
        return arr_title.length;
    }

    public Object getItem(int i)
    {
        return null;
    }

    public long getItemId(int i)
    {
        return 0L;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.listview_item_compass, viewgroup, false);
        TextView textview = (TextView)view1.findViewById(R.id.title);
        ((ImageView)view1.findViewById(R.id.img_icon)).setBackgroundResource(image_menu[i].intValue());
        textview.setText(arr_title[i]);
        return view1;
    }

    public void setSelection(int i)
    {
        pos = i;
        notifyDataSetChanged();
    }
}
