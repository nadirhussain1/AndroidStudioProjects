package veripark.billcalculator.screens;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import veripark.billcalculator.models.Category;
import veripark.billcalculator.storage.BillDbManager;

/**
 * Created by nadirhussain on 12/11/2015.
 */
public class CategoryAdapter extends BaseAdapter {
    private ArrayList<Category> listItems;
    private Context mContext = null;

    public CategoryAdapter(ArrayList<Category> list, Context context) {
        this.listItems = list;
        this.mContext = context;
    }
    public void setList(ArrayList<Category> list){
        this.listItems=list;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.cat_row, null);
        }

        TextView limtiTextView = (TextView) convertView.findViewById(R.id.catLimitText);
        TextView rateTextView = (TextView) convertView.findViewById(R.id.catRateText);
        TextView deleteTextView = (TextView) convertView.findViewById(R.id.DeleteText);

        String limitText = mContext.getResources().getString(R.string.cat_limit) + "" + listItems.get(position).getLimit();
        String rateText = mContext.getResources().getString(R.string.cat_rate) + "" + listItems.get(position).getRate();
        limtiTextView.setText(limitText);
        rateTextView.setText(rateText);

        deleteTextView.setTag(position);
        deleteTextView.setOnClickListener(deleteClickListener);

        return convertView;
    }

    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            BillDbManager.getInstance(mContext).deleteCategory(listItems.get(position));  //Delete category from Db
            listItems.remove(position);   //Remove category from java list
            notifyDataSetChanged();    //refresh UI
        }
    };


}
