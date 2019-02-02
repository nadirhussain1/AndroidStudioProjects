package patagonia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.nightwhistler.pageturner.PageTurner;
import net.nightwhistler.pageturner.R;

import java.util.ArrayList;

/**
 * Created by nadirhussain on 25/08/2016.
 */
public class PatagoniaFirebaseAdapter extends RecyclerView.Adapter<PatagoniaFirebaseAdapter.BookViewHolder> {
    private static final int ANIMATED_ITEMS_COUNT = 20;
    private int lastAnimatedPosition = -1;

    private Context mContext;
    private DeviceUtils mDeviceUtils;
    private PatagoniaParse.OnBookItemClickListener mItemClickListener;
    private ArrayList<String> booksEisbnsList = new ArrayList<>();


    public PatagoniaFirebaseAdapter(ArrayList<String> booksEisbnsList, Context context) {
        this.booksEisbnsList = booksEisbnsList;
        this.mContext = context;
        this.mDeviceUtils = PageTurner.getInstance().getUtils().getDeviceUtils();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_grid, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        String bookEisbn = booksEisbnsList.get(position);
        holder.bookCoverPhoto.setMinimumHeight(mDeviceUtils.getGridItemHeight());
        DatabaseReference booksReference = FirebaseDatabase.getInstance().getReference().child("books").child(bookEisbn);
        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                String description = (String) dataSnapshot.child("description").getValue();
                String author = (String) dataSnapshot.child("author").getValue();
                holder.bookTitle.setText(title);
                Glide.with(mContext)
                        .load(SimpleBook.getCoverUrl(bookEisbn))
                        .placeholder(R.drawable.unknown_cover)
                        .thumbnail(0.3f)
                        .crossFade()
                        .into(holder.bookCoverPhoto);
                holder.bookCoverPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleBook simpleBook = new SimpleBook(title, bookEisbn);
                        simpleBook.setDescription(description);
                        simpleBook.setAuthor(author);
                        mItemClickListener.onGridItemClick(v, simpleBook, position);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseDebug", "Db Error=" + databaseError.getDetails());
            }
        });


    }

    @Override
    public int getItemCount() {
        return booksEisbnsList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCoverPhoto;
        TextView bookTitle;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookCoverPhoto = (ImageView) itemView.findViewById(R.id.gridThumbnail);
            bookTitle = (TextView) itemView.findViewById(R.id.extraInfo);
        }
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(mDeviceUtils.getScreenPoint().y);
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(position * 100)
                    .start();
        }
    }

    public void setOnBookItemClickListener(PatagoniaParse.OnBookItemClickListener listener) {
        this.mItemClickListener = listener;
    }

}
