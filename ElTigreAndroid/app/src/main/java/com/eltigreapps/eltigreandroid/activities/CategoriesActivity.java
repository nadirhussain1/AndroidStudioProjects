package com.eltigreapps.eltigreandroid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.eltigreapps.eltigreandroid.R;
import com.eltigreapps.eltigreandroid.adapters.CategoryAdapter;
import com.eltigreapps.eltigreandroid.events.CategoryChoosenEvent;
import com.eltigreapps.eltigreandroid.events.OnCategoryClickListener;
import com.eltigreapps.eltigreandroid.utils.ScalingUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 27/05/2018.
 */

public class CategoriesActivity extends AppCompatActivity implements OnCategoryClickListener {
    private final static String[] categories = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    @BindView(R.id.categoriesRecyclerView)
    RecyclerView categoriesRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachViewToActivity();
        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    private void attachViewToActivity() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_categories, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void setUpRecyclerView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, Arrays.asList(categories), this);

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoriesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onCategoryClicked(String categoryName) {
        CategoryChoosenEvent categoryChoosenEvent = new CategoryChoosenEvent();
        categoryChoosenEvent.categoryName = categoryName;

        EventBus.getDefault().post(categoryChoosenEvent);
        finish();
    }

    @OnClick(R.id.leftActionAreaLayout)
    public void closeIconClicked() {
        finish();
    }
}
