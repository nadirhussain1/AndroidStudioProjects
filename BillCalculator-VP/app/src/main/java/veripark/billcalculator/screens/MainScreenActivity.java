package veripark.billcalculator.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import veripark.billcalculator.interfaces.BillConstants;
import veripark.billcalculator.models.Category;
import veripark.billcalculator.models.CustomerBill;
import veripark.billcalculator.storage.BillDbManager;
import veripark.billcalculator.storage.CalPreferences;

public class MainScreenActivity extends AppCompatActivity {
    private EditText readingEditor = null;
    private EditText serviceNumberEditor = null;
    private TextView prevReadingView = null;
    private TextView costView = null;

    private int currentReadingValue = 0;
    private int prevReading;
    private String serviceNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_screen);
        initViews();
        enterDefaultCategory();
    }

    //This method inflates xml views and binds them with java corresponding objects
    private void initViews() {
        readingEditor = (EditText) findViewById(R.id.readingEditor);
        serviceNumberEditor = (EditText) findViewById(R.id.customerNumEditor);
        Button calculateButton = (Button) findViewById(R.id.calcualteButton);
        Button configureButton = (Button) findViewById(R.id.configureButton);
        prevReadingView = (TextView) findViewById(R.id.PrevValueView);
        costView = (TextView) findViewById(R.id.costValueView);

        //bind click listeners with buttons
        calculateButton.setOnClickListener(calculateClickListener);
        configureButton.setOnClickListener(configueClickListener);
    }

    //Purpose of this method is to add two default categories at the start of app in database.
    private void enterDefaultCategory(){
        if(CalPreferences.getInstance(this).getISFirstTime()){
            Category catA=new Category();
            catA.setLimit(BillConstants.DEFAULT_CAT_A_LIMIT);
            catA.setRate(BillConstants.DEFAULT_CAT_A_RATE);

            Category catB=new Category();
            catB.setLimit(BillConstants.DEFAULT_CAT_B_LIMIT);
            catB.setRate(BillConstants.DEFAULT_CAT_B_RATE);

            BillDbManager.getInstance(this).addCategory(catA);
            BillDbManager.getInstance(this).addCategory(catB);
            CalPreferences.getInstance(this).updateFirstTimeFlag();
        }
    }



    private int calculate() {
        if (validateInput()) {
            prevReading = BillDbManager.getInstance(this).getPrevReading(serviceNumber);  //Get previous recodring for same customer if it exists.
            if (currentReadingValue <= prevReading) {
                Toast.makeText(this, getString(R.string.less_reading_error), Toast.LENGTH_LONG).show();
                return -1;
            }
            int totalUnitsToCharge = currentReadingValue - prevReading;   //total units which need to be charged to customer
            ArrayList<Category> catList=BillDbManager.getInstance(this).getAllCategories();  //get categories saved into Db;
            int billAmount=0;
            int catPosition=0;       //start from first category with lowest limit
            int calculatedUnits=0;  //this represents how many units have been calculated.
            while(totalUnitsToCharge>calculatedUnits){
                //Check if category is last then its upper limit doesn't matter. If units are less than limit of category then we need to check how many units
                // still remaining to be calculated.

                if(catPosition==catList.size()-1 || totalUnitsToCharge<catList.get(catPosition).getLimit()){
                    int diff=totalUnitsToCharge-calculatedUnits;
                    billAmount=billAmount+(diff*catList.get(catPosition).getRate());
                    break;
                }else{
                    int diff=catList.get(catPosition).getLimit()-calculatedUnits;
                    billAmount=billAmount+(diff*catList.get(catPosition).getRate());
                    calculatedUnits=calculatedUnits+diff;
                    catPosition++;
                }
            }
            return  billAmount;
        }
        return -1;
    }

    //Validate user input of reading and service number
    private boolean validateInput() {
        String readingValue = readingEditor.getText().toString();
        serviceNumber = serviceNumberEditor.getText().toString();
        if (TextUtils.isEmpty(readingValue)) {
            Toast.makeText(this, getString(R.string.empty_reading_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (serviceNumberEditor.length() < 10) {
            Toast.makeText(this, getString(R.string.digits_number_error), Toast.LENGTH_LONG).show();
            return false;
        }
        currentReadingValue = Integer.valueOf(readingValue);
        return true;
    }
   //Display calculated cost and save it into database for record of prev value.
    private void displayAndSave(int cost) {
        prevReadingView.setText("" + prevReading);
        costView.setText("" + cost);

        CustomerBill customerBill = new CustomerBill(serviceNumber, currentReadingValue);
        BillDbManager.getInstance(this).addReading(customerBill);
    }
    private void goToConfigActivity(){
        Intent intent=new Intent(this,ConfigActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener calculateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cost = calculate();
            if (cost > 0) {
                displayAndSave(cost);
            }
        }
    };
    private View.OnClickListener configueClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          goToConfigActivity();
        }
    };


}
