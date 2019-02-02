package com.olympus.viewsms.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olympus.viewsms.PreviewActivity;
import com.olympus.viewsms.R;
import com.olympus.viewsms.adapter.ThemeAdapter;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickBuy;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickPreview;
import com.olympus.viewsms.util.Constants;

public class OlympusThemeFragment extends Fragment {
    private View view;

  //  private List<Theme> themes;
    private ThemeAdapter adapter;

    private RecyclerView recyclerView;

    public static OlympusThemeFragment newInstance() {
        return new OlympusThemeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_olympustheme, container, false);

//        themes = new ArrayList<Theme>();
//        ThemeDAO themeDAO = new ThemeDAO(getActivity());
//        themes = themeDAO.getAvailableTheme();
        adapter = new ThemeAdapter(getActivity(), ThemeAdapter.MODE_OLYMPUS, MyThemeFragment.themes);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickBuyListener(new OnClickBuy() {
            @Override
            public void onClickBuy(int pos) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putInt(Constants.APPLIED_OLYMPUS_THEME_ID, MyThemeFragment.themes.get(pos).getId());
                prefsEditor.commit();
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnClickPreviewListener(new OnClickPreview() {
            @Override
            public void onClickPreview(int pos) {
                Intent i = new Intent(getActivity(), PreviewActivity.class);
                i.putExtra("theme_id", MyThemeFragment.themes.get(pos).getId());
                i.putExtra("avartar_id", pos % Constants.TOTAL_DEMO_AVARTARS);
                startActivity(i);
            }
        });

        return view;
    }

}
