package com.accessa.ibora.product.Discount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDiscontActivity extends Activity {


      private EditText DiscName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText Discvalue_Edittext;

    private String cashorId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Discount");


        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        setContentView(R.layout.add_discount_activity);

        DiscName_Edittext = findViewById(R.id.Discname_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        Discvalue_Edittext = findViewById(R.id.DiscAmount_edittext);

        // Add Record
        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecord();
                    }
                });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }
    private void addRecord() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String DiscName = DiscName_Edittext.getText().toString().trim();
        String LastModified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String Discvalue = Discvalue_Edittext.getText().toString().trim();

        // Regex for DiscName: only letters and spaces
        String discNamePattern = "^[a-zA-Z ]+$";
        // Validate DiscName using the regex pattern
        if (!DiscName.matches(discNamePattern)) {
            DiscName_Edittext.setError("Discount name can only contain letters and spaces.");
            return;
        }
        Discvalue = Discvalue.replace("%", ""); // Remove the "%" symbol if present
        // Check if all required fields are filled
        if (Discvalue.isEmpty() ||  DiscName.isEmpty()
                ) {
            Toast.makeText(this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate Discvalue as a number between 1 and 100
        double discountValue;
        try {
            discountValue = Double.parseDouble(Discvalue);
            if (discountValue < 1 || discountValue > 100) {
                Toast.makeText(this, "Discount value must be between 1 and 100.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid discount value. Please enter a numeric value.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        // Retrieve the current count of discounts
        int currentDiscountCount = dbManager.getDiscountCount();

        // Check if the current count exceeds the limit (e.g., 10)
        if (currentDiscountCount >= 10) {
            // Show a pop-up message indicating the limit has been reached
            Toast.makeText(this, R.string.MaxDis, Toast.LENGTH_SHORT).show();
            return;
        }
        dbManager.insertDisc(DiscName, LastModified, UserId, String.valueOf(discountValue));
        dbManager.close();

        // Clear the input
        DiscName_Edittext.setText("");
        LastModified_Edittext.setText("");
        Userid_Edittext.setText("");
        Discvalue_Edittext.setText("");

        returnHome();

    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Discount_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}