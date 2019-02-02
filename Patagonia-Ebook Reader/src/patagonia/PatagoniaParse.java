package patagonia;

import android.view.View;

/**
 * Created by rod on 3/12/16.
 */
public class PatagoniaParse {

    public interface OnGridItemClickListener{
        void onGridItemClick(View view, ParseBook book, int position);
    }
    public interface OnBookItemClickListener{
        void onGridItemClick(View view, SimpleBook book, int position);
    }
}
