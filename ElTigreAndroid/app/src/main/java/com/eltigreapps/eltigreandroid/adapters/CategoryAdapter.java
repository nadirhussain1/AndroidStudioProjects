package com.eltigreapps.eltigreandroid.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eltigreapps.eltigreandroid.R;
import com.eltigreapps.eltigreandroid.events.OnCategoryClickListener;
import com.eltigreapps.eltigreandroid.utils.ScalingUtility;

import java.util.List;

/**
 * Created by nadirhussain on 27/05/2018.
 */

public class CategoryAdapter extends Adapter<CategoryAdapter.CategoryHolder> {
    private Activity activity;
    private List<String> categories;
    private OnCategoryClickListener categoryClickListener;

    public CategoryAdapter(Activity activity, List<String> categories, OnCategoryClickListener categoryClickListener) {
        this.activity = activity;
        this.categories = categories;
        this.categoryClickListener = categoryClickListener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_category, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.categoryNameView.setText(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryHolder extends ViewHolder implements OnClickListener {
        TextView categoryNameView;

        public CategoryHolder(View itemView) {
            super(itemView);

            categoryNameView = (TextView) itemView.findViewById(R.id.categoryNameTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            categoryClickListener.onCategoryClicked(categories.get(getAdapterPosition()));
        }
    }
}
