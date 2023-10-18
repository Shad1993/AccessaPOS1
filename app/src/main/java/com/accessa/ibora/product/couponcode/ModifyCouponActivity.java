package com.accessa.ibora.product.couponcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ModifyCouponActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;
    private EditText couponcode;
    private EditText inputAmount;
    private EditText startDateTextView;
    private DatePicker startDatePicker;
    private EditText endDateTextView;
    private DatePicker endDatePicker;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;


    private DBManager dbManager;

    private long _id;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
    private SharedPreferences sharedPreferences;

    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Coupon");
        setContentView(R.layout.modify_coupon_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
         cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID

        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        // Initialize your UI elements
        couponcode = findViewById(R.id.couponcode);
        inputAmount = findViewById(R.id.inputAmount);
        startDatePicker = findViewById(R.id.startDatePicker);
        endDatePicker = findViewById(R.id.endDatePicker);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);



        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        Coupon coupon = dbManager.getCouponById(id);
        if (coupon != null) {
            couponcode.setText(coupon.getCode());
            inputAmount.setText(String.valueOf(coupon.getDiscount()));

            // Parse the start date from the database and set it in the DatePicker
            SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String startDateString = coupon.getStartDate();
                if (startDateString != null) {
                    Date startDate = dbDateFormat.parse(startDateString);
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(startDate);
                    int startYear = startCalendar.get(Calendar.YEAR);
                    int startMonth = startCalendar.get(Calendar.MONTH);
                    int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
                    startDatePicker.init(startYear, startMonth, startDay, null);
                }

                // Similarly, parse and set the end date
                String endDateString = coupon.getEndDate();
                if (endDateString != null) {
                    Date endDate = dbDateFormat.parse(endDateString);
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(endDate);
                    int endYear = endCalendar.get(Calendar.YEAR);
                    int endMonth = endCalendar.get(Calendar.MONTH);
                    int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
                    endDatePicker.init(endYear, endMonth, endDay, null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }
        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDept();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }


    private void updateDept() {
        String couponCode = couponcode.getText().toString().trim();
        String amount = inputAmount.getText().toString().trim();

        // Retrieve the selected start date from the DatePicker
        int startYear = startDatePicker.getYear();
        int startMonth = startDatePicker.getMonth();
        int startDay = startDatePicker.getDayOfMonth();
        String startDate = String.format(Locale.US, "%04d-%02d-%02d", startYear, startMonth + 1, startDay);

        // Retrieve the selected end date from the DatePicker
        int endYear = endDatePicker.getYear();
        int endMonth = endDatePicker.getMonth();
        int endDay = endDatePicker.getDayOfMonth();
        String endDate = String.format(Locale.US, "%04d-%02d-%02d", endYear, endMonth + 1, endDay);
        // Construct the start and end date strings in the desired format
        String startDateString = String.format("%04d-%02d-%02d", startYear, startMonth, startDay);
        String endDateString = String.format("%04d-%02d-%02d", endYear, endMonth, endDay);


        String status = determineStatus(startDateString, endDateString);
        String userId = Userid_Edittext.getText().toString().trim();

        if (couponCode.isEmpty() || amount.isEmpty()  || userId.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you have a method in your DBManager to update the coupon info
        boolean isUpdated = dbManager.updateCoupon(_id, couponCode, amount, status,startDate, endDate, userId);

        if (isUpdated) {
            Toast.makeText(this, getString(R.string.updatecoupon), Toast.LENGTH_SHORT).show();
            System.out.println(startDate + " " + endDate+ " " + status);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedupdatecoupon), Toast.LENGTH_SHORT).show();
        }
    }


    private String determineStatus(String startDate, String endDate) {
        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        if (endDate.compareTo(currentDate) < 0) {
            // End date is in the past
            return "Expired";
        } else if (startDate.compareTo(currentDate) > 0) {
            // Start date is in the future
            return "Soon Coming";
        } else {
            // Current date is within the date range
            return "Available";
        }
    }



    private void deleteItem() {
        boolean isDeleted = dbManager.deleteCoupon(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, getString(R.string.deleteDept), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedDeleteDept), Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Dept_fragment");
        startActivity(home_intent1);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
