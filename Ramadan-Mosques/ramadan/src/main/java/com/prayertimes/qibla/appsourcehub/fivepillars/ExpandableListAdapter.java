package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.content.Context;
import android.view.*;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import muslim.prayers.time.R;
public class ExpandableListAdapter extends BaseExpandableListAdapter
{

    private Context _context;
    private HashMap _listDataChild;
    private List _listDataHeader;

    public ExpandableListAdapter(android.view.View.OnClickListener onclicklistener, List list, HashMap hashmap)
    {
        _context = (Context)onclicklistener;
        _listDataHeader = list;
        _listDataChild = hashmap;
    }

    public Object getChild(int i, int j)
    {
        return ((List)_listDataChild.get(_listDataHeader.get(i))).get(j);
    }

    public long getChildId(int i, int j)
    {
        return (long)j;
    }

    public View getChildView(int i, int j, boolean flag, View view, ViewGroup viewgroup)
    {
        String s = (String)getChild(i, j);
        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService("layout_inflater")).inflate(R.layout.list_items, null);
        }
        ((TextView)view.findViewById(R.id.lblListItem)).setText(s);
        return view;
    }

    public int getChildrenCount(int i)
    {
        return ((List)_listDataChild.get(_listDataHeader.get(i))).size();
    }

    public Object getGroup(int i)
    {
        return _listDataHeader.get(i);
    }

    public int getGroupCount()
    {
        return _listDataHeader.size();
    }

    public long getGroupId(int i)
    {
        return (long)i;
    }

    public View getGroupView(int i, boolean flag, View view, ViewGroup viewgroup)
    {
        String s = (String)getGroup(i);
        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService("layout_inflater")).inflate(R.layout.list_items, null);
        }
        ((TextView)view.findViewById(R.id.lblListItem)).setText(s);
        return view;
    }

    public boolean hasStableIds()
    {
        return false;
    }

    public boolean isChildSelectable(int i, int j)
    {
        return true;
    }
}
