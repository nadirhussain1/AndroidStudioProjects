package com.olympus.viewsms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olympus.viewsms.R;
import com.olympus.viewsms.model.ContactApplied;
import com.olympus.viewsms.util.Scale;
import com.squareup.picasso.Picasso;

import java.util.List;

//An adapter to display the data in list view.
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

	private Context context;
	private List<ContactApplied> items;
	private boolean isUnlockFree;
	private int checkedItemsCount=0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_number;
        ImageView contact_thumb;
        LinearLayout ln_bg;
        CheckBox chk_select;

        public ViewHolder(View convertView) {
            super(convertView);
            tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            ln_bg = (LinearLayout) convertView.findViewById(R.id.ln_bg);
            contact_thumb=(ImageView)convertView.findViewById(R.id.contactThumbnail);
            chk_select=(CheckBox) convertView.findViewById(R.id.chk_select);
        }
    }

	public ContactListAdapter(Context context, List<ContactApplied> items,boolean isUnlockFree) {
		this.context = context;
		this.items=items;
		this.isUnlockFree=isUnlockFree;
	}

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_contact_menu, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactListAdapter.ViewHolder holder, final int position) {
        ContactApplied rowItem = items.get(position);
        holder.tv_number.setText(rowItem.getNumber());
        holder.tv_name.setText(rowItem.getName());
        holder.chk_select.setChecked(rowItem.getCheck());
        holder.chk_select.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyContact(position);
            }
        });
        holder.ln_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyContact(position);
            }
        });
        int imageDimen= Scale.cvDPtoPX(context,50);
        Picasso.with(context).load(rowItem.getThumbnailUri()).placeholder(R.drawable.default_thumbnail).resize(imageDimen,imageDimen).into(holder.contact_thumb);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

	private void applyContact(int pos){
		items.get(pos).setCheck(!items.get(pos).getCheck());

	}

}
