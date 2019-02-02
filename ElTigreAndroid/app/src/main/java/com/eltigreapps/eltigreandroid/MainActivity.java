package com.eltigreapps.eltigreandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eltigreapps.eltigreandroid.activities.CategoriesActivity;
import com.eltigreapps.eltigreandroid.adapters.BooksLoadAdapter;
import com.eltigreapps.eltigreandroid.events.CategoryChoosenEvent;
import com.eltigreapps.eltigreandroid.model.Book;
import com.eltigreapps.eltigreandroid.utils.GlobalUtil;
import com.eltigreapps.eltigreandroid.utils.ScalingUtility;
import com.eltigreapps.eltigreandroid.utils.VerticalSpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.leftActionAreaLayout)
    RelativeLayout leftActionLayout;
    @BindView(R.id.centralTitle)
    TextView centralTitleView;
    @BindView(R.id.booksRecyclerView)
    RecyclerView booksRecyclerView;
    @BindView(R.id.downloadBar)
    ProgressBar downloadProgressBar;
    @BindView(R.id.errorView)
    TextView errorMessageView;
    @BindView(R.id.booksSearchView)
    SearchView booksSearchView;

    private DatabaseReference mDatabase;
    private ArrayList<Book> booksList = new ArrayList<>();
    private BooksLoadAdapter booksLoadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachViewToActivity();
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        booksSearchView.setOnQueryTextListener(booksQueryListener);
        loadBooksDataFromFirebase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void attachViewToActivity() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void loadBooksDataFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("newbooks");

        showProgressBar();
        mDatabase.addValueEventListener(booksReadListener);

    }

    private void showProgressBar() {
        downloadProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        downloadProgressBar.setVisibility(View.GONE);
    }

    private void onLoadSuccess() {
        hideProgressBar();
        errorMessageView.setVisibility(View.GONE);


        booksLoadAdapter = new BooksLoadAdapter(this, booksList);
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration((int) ScalingUtility.resizeDimension(20));
        booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        booksRecyclerView.setAdapter(booksLoadAdapter);
        booksRecyclerView.addItemDecoration(itemDecoration);

        booksRecyclerView.setVisibility(View.VISIBLE);
    }

    private void onLoadFailure() {
        hideProgressBar();

        errorMessageView.setVisibility(View.VISIBLE);
        booksRecyclerView.setVisibility(View.GONE);
    }

    private ValueEventListener booksReadListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GlobalUtil.printLog("MainActivity", "onDataChange");
            booksList.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                Book book = postSnapshot.getValue(Book.class);
                booksList.add(book);
            }

            onLoadSuccess();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            GlobalUtil.printLog("MainActivity", "onCancelled");
            onLoadFailure();
        }
    };

    @OnClick(R.id.booksSearchView)
    public void onSearchBarClicked(View v) {
        booksSearchView.setIconified(false);
    }

    @OnClick(R.id.leftActionAreaLayout)
    public void loadAllBooksInViews() {
        hideKeyBoard();
        booksLoadAdapter.refreshAdapter(booksList);
        updateTtitleBarOnAllBooksScreen();
        booksSearchView.setQuery("",true);
    }

    @OnClick(R.id.categoriesTitleView)
    public void launchCategoriesScreen() {
        hideKeyBoard();
        Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(intent);
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(booksSearchView.getWindowToken(),0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CategoryChoosenEvent event) {
        updateTtitleBarOnCategorySelection(event.categoryName);
        List<Book> categoryBooks = getBooksOfCategory(event.categoryName);
        booksLoadAdapter.refreshAdapter(categoryBooks);

        booksSearchView.setQuery("",true);
    }

    private void updateTtitleBarOnCategorySelection(String catName) {
        String title = getString(R.string.label_category) + ": " + catName;

        leftActionLayout.setVisibility(View.VISIBLE);
        centralTitleView.setText(title);
    }
    private void updateTtitleBarOnAllBooksScreen(){
        leftActionLayout.setVisibility(View.INVISIBLE);
        centralTitleView.setText(getString(R.string.label_biblioteca));
    }

    private List<Book> getBooksOfCategory(String catName) {
        List<Book> categoryBooks = new ArrayList<>();
        for (Book book : booksList) {
            if (book.getTitle().toLowerCase().startsWith(catName.toLowerCase())) {
                categoryBooks.add(book);
            }
        }
        return categoryBooks;
    }

    private OnQueryTextListener booksQueryListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            booksLoadAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            booksLoadAdapter.getFilter().filter(newText);
            return false;
        }
    };


}
