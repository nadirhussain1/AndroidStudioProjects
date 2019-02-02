package com.eltigreapps.eltigreandroid.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.eltigreapps.eltigreandroid.R;
import com.eltigreapps.eltigreandroid.model.Book;
import com.eltigreapps.eltigreandroid.utils.ScalingUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadirhussain on 23/05/2018.
 */

public class BooksLoadAdapter extends Adapter<BooksLoadAdapter.BooksLoadHolder> implements Filterable {
    private Activity activity;
    private List<Book> books;
    private List<Book> filteredBooks;
    private int targetWidth, targetHeight;

    public BooksLoadAdapter(Activity activity, List<Book> books) {
        this.activity = activity;
        this.books = books;
        this.filteredBooks = books;

        targetWidth = (int) ScalingUtility.resizeDimension(300);
        targetHeight = (int) ScalingUtility.resizeDimension(500);

    }

    public void refreshAdapter(List<Book> books) {
        this.books = books;
        this.filteredBooks = books;
        notifyDataSetChanged();
    }

    @Override
    public BooksLoadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_book, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new BooksLoadHolder(view);
    }

    @Override
    public void onBindViewHolder(BooksLoadHolder holder, int position) {
        Book book = filteredBooks.get(position);
        holder.bookTitleView.setText(book.getTitle());

        Picasso.get().load(book.getCoverUrl()).resize(targetWidth, targetHeight).placeholder(R.drawable.cover_placeholder).into(holder.coverImageView);
    }

    @Override
    public int getItemCount() {
        return filteredBooks.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    filteredBooks = books;
                } else {
                    List<Book> filteredList = new ArrayList<>();
                    for (Book book : books) {
                        if (book.getTitle().toLowerCase().startsWith(filterString.toLowerCase())) {
                            filteredList.add(book);
                        }
                    }

                    filteredBooks = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBooks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBooks = (List<Book>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class BooksLoadHolder extends ViewHolder {

        ImageView coverImageView;
        TextView bookTitleView;

        public BooksLoadHolder(View itemView) {
            super(itemView);

            coverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
            bookTitleView = (TextView) itemView.findViewById(R.id.bookTitleView);
        }
    }
}
