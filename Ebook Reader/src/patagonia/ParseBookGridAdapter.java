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
import com.parse.ParseQueryAdapter;

import net.nightwhistler.pageturner.PageTurner;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.library.LibraryService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rod on 3/12/16.
 */
public class ParseBookGridAdapter extends ParseRecyclerQueryAdapter<ParseBook, ParseBookGridAdapter.ViewHolder> {
    public final static String TAG = ParseBookGridAdapter.class.getSimpleName();
    private static final int ANIMATED_ITEMS_COUNT = 20;
    private int lastAnimatedPosition = -1;

    private Context mContext;
    private DeviceUtils mDeviceUtils;
    private PatagoniaParse.OnGridItemClickListener mItemClickListener;
    private LibraryService libraryService;

    public ParseBookGridAdapter(ParseQueryAdapter.QueryFactory<ParseBook> factory, boolean hasStableIds, Context context, LibraryService libraryService) {
        super(factory, hasStableIds);
        this.mContext = context;
        this.mDeviceUtils = PageTurner.getInstance().getUtils().getDeviceUtils();
        this.libraryService = libraryService;
        try {
            this.mItemClickListener = (PatagoniaParse.OnGridItemClickListener) context;
        }catch (ClassCastException e){
            //
        }

    }

    @Override
    public ParseBookGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.book_grid,parent,false);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(ParseBookGridAdapter.ViewHolder holder, final int position) {
        runEnterAnimation(holder.itemView, position);
        final ParseBook item = (ParseBook) getItem(position);
        PatagoniaImportBookTask importTask = new PatagoniaImportBookTask(libraryService);
        importTask.execute(item);
        holder.mGridThumbnail.setMinimumHeight(mDeviceUtils.getGridItemHeight());
        Log.d(TAG, item.getObjectId());
        Glide.with(mContext)
                .load(item.getCoverUrl())
                .placeholder(R.drawable.unknown_cover)
                .thumbnail(0.3f)
                .crossFade()
                .into(holder.mGridThumbnail);
        holder.mGridThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onGridItemClick(v, item, position);
            }
        });

        holder.mExtraInfo.setText(item.getTitle());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.mGridThumbnail);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.gridThumbnail)
        ImageView mGridThumbnail;
        @Bind(R.id.extraInfo)
        TextView mExtraInfo;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
                    .setDuration(position*100)
                    .start();
        }
    }

    public void setOnGridItemClickListener(PatagoniaParse.OnGridItemClickListener listener){
        this.mItemClickListener = listener;
    }
}
