package com.accessa.ibora.product.Vendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddVendorActivity extends Activity {
    private DatabaseHelper mDatabaseHelper;
    private TextView VendorCode;
    private EditText DeptName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText PhoneNumber_Edittext;
    private EditText Street_Edittext;
    private EditText Town_Edittext;
    private EditText PostalCode_Edittext;
    private EditText Email_Edittext;
    private EditText InternalCode_Edittext;
    private EditText Salesmen_Edittext;

    private String selectedDepartmentCode;

    private String departmentid;
    private String cashorId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Department");

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID

        setContentView(R.layout.add_vendor_activity);

        DeptName_Edittext = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        VendorCode = findViewById(R.id.VendCode_edittext);
        PhoneNumber_Edittext = findViewById(R.id.PhoneNumber_edittext);
        Street_Edittext = findViewById(R.id.Street_edittext);
        Town_Edittext = findViewById(R.id.Town_edittext);
        PostalCode_Edittext = findViewById(R.id.PostalCode_edittext);
        Email_Edittext = findViewById(R.id.Email_edittext);
        InternalCode_Edittext = findViewById(R.id.InternalCode_edittext);
        Salesmen_Edittext = findViewById(R.id.Salesmen_edittext);

        // Add Record
        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

        // Set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));
    }

    private void addRecord() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String VendorName = DeptName_Edittext.getText().toString().trim();
        String LastModified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String VendCode = VendorCode.getText().toString().trim();
        String PhoneNumber = PhoneNumber_Edittext.getText().toString().trim();
        String Street = Street_Edittext.getText().toString().trim();
        String Town = Town_Edittext.getText().toString().trim();
        String PostalCode = PostalCode_Edittext.getText().toString().trim();
        String Email = Email_Edittext.getText().toString().trim();
        String InternalCode = InternalCode_Edittext.getText().toString().trim();
        String Salesmen = Salesmen_Edittext.getText().toString().trim();
        // Regex patterns
        String phonePattern = "^[0-9]{7,15}$"; // Only digits, length between 7-15 (adjust as needed)
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // Basic email pattern
        String postalCodePattern = "^[0-9]{4,10}$"; // Only digits, length between 4-10 (adjust if needed)

        // Check if all required fields are filled
        if (VendorName.isEmpty() || VendCode.isEmpty() || PhoneNumber.isEmpty() ||
                Street.isEmpty() || Town.isEmpty() || PostalCode.isEmpty() ||
                Email.isEmpty() || InternalCode.isEmpty() || Salesmen.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate phone number
        if (!PhoneNumber.matches(phonePattern)) {
            PhoneNumber_Edittext.setError("Invalid phone number format");
            return;
        }

        // Validate email
        if (!Email.matches(emailPattern)) {
            Email_Edittext.setError("Invalid email format");
            return;
        }

        // Validate postal code
        if (!PostalCode.matches(postalCodePattern)) {
            PostalCode_Edittext.setError("Invalid postal code format");
            return;
        }

        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        dbManager.insertVendor(VendorName, LastModified, UserId, VendCode, PhoneNumber, Street,
                Town, PostalCode, Email, InternalCode, Salesmen);
        dbManager.close();

        // Clear the input
        DeptName_Edittext.setText("");
        LastModified_Edittext.setText("");
        Userid_Edittext.setText("");
        VendorCode.setText("");
        PhoneNumber_Edittext.setText("");
        Street_Edittext.setText("");
        Town_Edittext.setText("");
        PostalCode_Edittext.setText("");
        Email_Edittext.setText("");
        InternalCode_Edittext.setText("");
        Salesmen_Edittext.setText("");

        returnHome();
    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Vend_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
