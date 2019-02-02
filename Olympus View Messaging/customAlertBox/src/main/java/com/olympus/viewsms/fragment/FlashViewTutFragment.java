package com.olympus.viewsms.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olympus.viewsms.R;

public class FlashViewTutFragment extends Fragment {
    private View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_flash_tut, container, false);
        ImageView videoThumbnail = (ImageView) view.findViewById(R.id.videoThumbnail);
        videoThumbnail.setOnClickListener(playVideoClickListener);

        return view;
    }

    private OnClickListener playVideoClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + "kB_EtbNk8TA"));
            startActivity(intent);
        }
    };

}
