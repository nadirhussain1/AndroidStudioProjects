package com.prayertimes.qibla.appsourcehub.adpater;

import android.content.Context;
import android.text.Html;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.prayertimes.qibla.appsourcehub.model.Country;
import java.util.*;
import muslim.prayers.time.R;
public class AdapterCountry extends BaseAdapter
{
    public class ViewHolder
    {

        TextView country;
        TextView population;
        TextView rank;

        public ViewHolder()
        {
            super();
        }
    }


    private ArrayList arraylist;
    private ArrayList countrylist;
    LayoutInflater inflater;
    Context mContext;

    public AdapterCountry(Context context, ArrayList arraylist1)
    {
        countrylist = new ArrayList();
        mContext = context;
        countrylist = arraylist1;
        inflater = LayoutInflater.from(mContext);
        arraylist = new ArrayList();
        arraylist.addAll(arraylist1);
    }

    public void filter(String s)
    {
        String s1 = s.toLowerCase(Locale.getDefault());
        countrylist.clear();
        if(s1.length() == 0)
        {
            countrylist.addAll(arraylist);
        } else
        {
            Iterator iterator = arraylist.iterator();
            while(iterator.hasNext()) 
            {
                Country country = (Country)iterator.next();
                if(country.getName().toLowerCase(Locale.getDefault()).contains(s1))
                {
                    countrylist.add(country);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getCount()
    {
        return countrylist.size();
    }

    public Country getItem(int i)
    {
        return (Country)countrylist.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item_country, null);
            viewholder.country = (TextView)view.findViewById(R.id.country);
            view.setTag(viewholder);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        viewholder.country.setText(Html.fromHtml(((Country)countrylist.get(i)).getName()));
        return view;
    }
}
