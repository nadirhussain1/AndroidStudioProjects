package veripark.billcalculator.screens;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import veripark.billcalculator.models.Category;
import veripark.billcalculator.storage.BillDbManager;

/**
 * Created by nadirhussain on 29/10/2015.
 */
public class ConfigActivity extends Activity {
    private ListView catListView;
    private Dialog dialog;
    private EditText catLimitEditor;
    private EditText catRateEditor;
    private CategoryAdapter categoryAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_screen);
        initViews();
    }

    //Initializes views and list of categories.
    private void initViews() {
        categoryAdapter=new CategoryAdapter(BillDbManager.getInstance(this).getAllCategories(),this);
        catListView=(ListView)findViewById(R.id.catListView);
        catListView.setAdapter(categoryAdapter);

        initDialog();
        Button addNewButton = (Button) findViewById(R.id.addNewCatButton);
        Button goBackButton=(Button)findViewById(R.id.goBackButton);
        addNewButton.setOnClickListener(addNewClickListener);
        goBackButton.setOnClickListener(goBackClickListener);
    }
    //When new category added then we need to refresh list to show to user;
    private void refreshList(){
        categoryAdapter.setList(BillDbManager.getInstance(this).getAllCategories());
        categoryAdapter.notifyDataSetChanged();
    }



    //Check values entered by the user for new category
   private void validateAndAddCategory(){
       String catLimit = catLimitEditor.getText().toString();
       String catRate = catRateEditor.getText().toString();

       if (TextUtils.isEmpty(catLimit) || TextUtils.isEmpty(catRate)) {
           Toast.makeText(this, getString(R.string.empty_config), Toast.LENGTH_LONG).show();
           return;
       }
       int intLmit = Integer.valueOf(catLimit);
       int intRate = Integer.valueOf(catRate);
       Category category=new Category();
       category.setLimit(intLmit);
       category.setRate(intRate);

       BillDbManager.getInstance(this).addCategory(category);
       refreshList();
       hideDialog();
   }

    //UI for adding new category values
    private void initDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.add_cat_dialog_screen);

        Button doneButton=(Button) dialog.findViewById(R.id.doneButton);
        Button cancelButton=(Button) dialog.findViewById(R.id.cancelButton);
        catLimitEditor=(EditText)dialog.findViewById(R.id.catLimitEditor);
        catRateEditor=(EditText)dialog.findViewById(R.id.catRateEditor);
        doneButton.setOnClickListener(addDoneClickListener);
        cancelButton.setOnClickListener(cancelClickListener);
    }

    private void showDialog() {
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null) {
            dialog.hide();
        }
    }
    private void cancelDialog(){
        if (dialog != null) {
            dialog.cancel();
        }
    }

    private View.OnClickListener addNewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();
        }
    };
    private View.OnClickListener addDoneClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validateAndAddCategory();
        }
    };
    private View.OnClickListener goBackClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           ConfigActivity.this.finish();
        }
    };
    private View.OnClickListener cancelClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           cancelDialog();
        }
    };
}
