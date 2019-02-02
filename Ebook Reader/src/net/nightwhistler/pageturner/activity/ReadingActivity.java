/*
 * Copyright (C) 2012 Alex Kuiper
 * 
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */
package net.nightwhistler.pageturner.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.fragment.ReadingFragment;
import net.nightwhistler.pageturner.view.NavigationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import jedi.option.Option;

public class ReadingActivity extends AppCompatActivity {
    private ReadingFragment readingFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;

    private ExpandableListView mExpandableListView;
    private NavigationAdapter adapter;

    private static final Logger LOG = LoggerFactory.getLogger("ReadingActivity");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_reading);
        loadFragment();
        initDrawer();
    }

    private void loadFragment() {
        readingFragment = new ReadingFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, readingFragment).commit();
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setupDrawer();
    }


    public void startPreferences() {
        if (readingFragment != null) {
            this.readingFragment.saveConfigState();
        }

        readingFragment.saveReadingPosition();
        readingFragment.getBookView().releaseResources();

        Intent intent = new Intent(this, PageTurnerPrefsActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onSearchRequested() {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        readingFragment.onWindowFocusChanged(hasFocus);
    }

    public void onMediaButtonEvent(View view) {
        this.readingFragment.onMediaButtonEvent(view.getId());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return readingFragment.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (readingFragment.dispatchKeyEvent(event)) {
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    protected List<NavigationCallback> getMenuItems(Configuration config) {

        List<NavigationCallback> menuItems = new ArrayList<>();

        String nowReading = getString(R.string.now_reading, config.getLastReadTitle());
        NavigationCallback readingCallback = new NavigationCallback(nowReading);
        menuItems.add(readingCallback);
        Log.d("TableOfContentDebug", "Inside getMenuItems");

        if (this.readingFragment != null) {
            Log.d("TableOfContentDebug", "ReadingFragment Not Null");
            if (this.readingFragment.hasTableOfContents()) {

                NavigationCallback tocCallback = new NavigationCallback(getString(R.string.toc_label));
                List<NavigationCallback> tabletofContent = readingFragment.getTableOfContents();
                Log.d("TableOfContentDebug", "Size of TabletOfContents=" + tabletofContent.size());
                tocCallback.addChildren(tabletofContent);
                readingCallback.addChild(tocCallback);
            }

            if (this.readingFragment.hasHighlights()) {
                NavigationCallback highlightsCallback = new NavigationCallback(getString(R.string.highlights));
                readingCallback.addChild(highlightsCallback);
                highlightsCallback.addChildren(readingFragment.getHighlights());
            }


            if (this.readingFragment.hasBookmarks()) {
                NavigationCallback bookmarksCallback = new NavigationCallback(getString(R.string.bookmarks));
                readingCallback.addChild(bookmarksCallback);

                bookmarksCallback.addChildren(readingFragment.getBookmarks());
            }
        }

        return menuItems;
    }

    public void initDrawerItems() {
        if (mExpandableListView != null) {
            this.adapter = new NavigationAdapter(this, getMenuItems(Configuration.getInstance(this)), this::createExpandableListView, 0);
            setClickListeners(mExpandableListView, this.adapter);
        }
    }

    private ExpandableListView createExpandableListView(List<NavigationCallback> items, int level) {
        ExpandableListView e = new ExpandableListView(this) {
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                /*
                * Adjust height
                */
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(10000, MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        setClickListeners(e, new NavigationAdapter(this, items, this::createExpandableListView, level));
        return e;
    }

    private void setClickListeners(ExpandableListView expandableListView, NavigationAdapter adapter) {

        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupClickListener(
                (e, v, groupId, l) -> this.onGroupClick(adapter, groupId));

        expandableListView.setOnChildClickListener(
                (e, v, groupId, childId, l) -> this.onChildClick(adapter, groupId, childId));

        expandableListView.setOnItemLongClickListener(
                (av, v, position, id) -> this.onItemLongClick(adapter, position, id));

        expandableListView.setGroupIndicator(null);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected boolean onGroupClick(NavigationAdapter adapter, int groupId) {

        LOG.debug("Got onGroupClick for group " + groupId + " on level " + adapter.getLevel());

        Option<Boolean> group = adapter.findGroup(groupId).map(g -> {
            if (g.hasChildren()) {
                return false; //Let the superclass handle it and expand the group
            } else {
                g.onClick();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        return group.getOrElse(false);
    }

    protected boolean onChildClick(NavigationAdapter adapter, int groupId, int childId) {

        LOG.debug("Got onChildClick event for group " + groupId + " and child " + childId
                + " on level " + adapter.getLevel());

        Option<NavigationCallback> childItem = adapter.findChild(groupId, childId);

        childItem.forEach(item -> {
            if (!item.hasChildren()) {
                item.onClick();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        return false;
    }

    protected boolean onItemLongClick(NavigationAdapter adapter, int position, long id) {

        LOG.debug("Got long click on position" + position + " on level " + adapter.getLevel());

        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(id);
            int childPosition = adapter.getIndexForChildId(groupPosition, ExpandableListView.getPackedPositionChild(id));
            Option<NavigationCallback> childItem = adapter.findChild(groupPosition, childPosition);
            LOG.debug("Long-click on " + groupPosition + ", " + childPosition);
            LOG.debug("Child-item: " + childItem);

            childItem.match(
                    NavigationCallback::onLongClick,
                    () -> LOG.error("Could not get child-item for " + position + " and id " + id)
            );

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
