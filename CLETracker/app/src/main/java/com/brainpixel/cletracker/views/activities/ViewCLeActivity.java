package com.brainpixel.cletracker.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.brainpixel.cletracker.R;
import com.brainpixel.cletracker.utils.ScalingUtility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 20/04/2017.
 */

public class ViewCLeActivity extends AppCompatActivity {
    public static final String KEY_CERTIFICATE_PATH = "KEY_CERTIFICATE_PATH";

    @BindView(R.id.certificateIconView)
    ImageView certificateImageView;
    private String certificatePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.view_certificate_layout, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
        ButterKnife.bind(this, view);

        if (getIntent() != null && getIntent().getExtras() != null) {
            certificatePath = getIntent().getStringExtra(KEY_CERTIFICATE_PATH);
            Picasso.with(this).load(certificatePath).into(certificateImageView);
        }
    }

    @OnClick(R.id.backIconView)
    public void backArrowClicked(){
        finish();
    }

}
