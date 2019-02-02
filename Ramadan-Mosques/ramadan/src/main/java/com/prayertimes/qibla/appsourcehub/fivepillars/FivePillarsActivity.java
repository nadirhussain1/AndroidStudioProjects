package com.prayertimes.qibla.appsourcehub.fivepillars;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import com.prayertimes.qibla.appsourcehub.utils.Utils;
import muslim.prayers.time.R;
public class FivePillarsActivity extends Utils
{

    ImageView fasting;
    ImageView hajj;
    ImageView salah;
    ImageView shahadah;
    ImageView zakat;

    public FivePillarsActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fivepillars);
        Actionbar(getString(R.string.title_lbl_pillar));
        typeface();
        Analytics(getString(R.string.title_lbl_pillar));
        banner_ad();
        shahadah = (ImageView)findViewById(R.id.Shahadah);
        salah = (ImageView)findViewById(R.id.Salah);
        zakat = (ImageView)findViewById(R.id.Zakat);
        fasting = (ImageView)findViewById(R.id.Fasting);
        hajj = (ImageView)findViewById(R.id.Hajj);
        shahadah.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity((new Intent(FivePillarsActivity.this, com.prayertimes.qibla.appsourcehub.fivepillars.PillarsListActivity.class)).putExtra("name", "shahadah"));
            }
        });
        salah.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity((new Intent(FivePillarsActivity.this, com.prayertimes.qibla.appsourcehub.fivepillars.PillarsListActivity.class)).putExtra("name", "salah"));
            }
        });
        zakat.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity((new Intent(FivePillarsActivity.this, com.prayertimes.qibla.appsourcehub.fivepillars.PillarsListActivity.class)).putExtra("name", "zakat"));
            }
        });
        fasting.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity((new Intent(FivePillarsActivity.this, com.prayertimes.qibla.appsourcehub.fivepillars.PillarsListActivity.class)).putExtra("name", "fasting"));
            }
        });
        hajj.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity((new Intent(FivePillarsActivity.this, com.prayertimes.qibla.appsourcehub.fivepillars.PillarsListActivity.class)).putExtra("name", "haji"));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        menu.findItem(R.id.menu_ok).setVisible(false);
        return true;
    }
}
