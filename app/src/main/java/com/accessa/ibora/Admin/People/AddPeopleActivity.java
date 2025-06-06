package com.accessa.ibora.Admin.People;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPeopleActivity extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 123;



    private DatabaseHelper mDatabaseHelper;
    private EditText CashierNameTextView,Userid_Edittext;
    private  String cashierLevel;
    private EditText PinTextView;
    private EditText DepartmentTextView;
    private Spinner LevelSpinner;
    private String ShopName;
    private String cashorId;
    private String cashorName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Retrieve the locale value from intent extras
        String locale = getIntent().getStringExtra("locale");
        setTitle(getString(R.string.addpeople));





        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        ShopName = sharedPreferences.getString("ShopName", null); // Retrieve cashor's level

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID



        setContentView(R.layout.activity_add_people);

        PinTextView = findViewById(R.id.PinEditText);
        CashierNameTextView = findViewById(R.id.IdCashierName_edittext);
        DepartmentTextView = findViewById(R.id.department_edittext);
        LevelSpinner = findViewById(R.id.Cashier_spinner);

// Get the array from resources and convert it to a list
        String[] cashierLevelsArray = getResources().getStringArray(R.array.cashier_levels);
        List<String> cashierLevelsList = new ArrayList<>(Arrays.asList(cashierLevelsArray));

// Replace "0" with "Trainee"
        cashierLevelsList.set(0, "Trainee");

// Create an ArrayAdapter with the modified list
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cashierLevelsList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LevelSpinner.setAdapter(spinnerAdapter);


        mDatabaseHelper = new DatabaseHelper(this);




        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

    }




    private void addRecord() {
        String pin = PinTextView.getText().toString();
        String enteredPIN = PinTextView.getText().toString();

        if (enteredPIN.isEmpty()) {
            Toast.makeText(this, "Please enter a PIN", Toast.LENGTH_SHORT).show();
            return;
        }
        // Define regex patterns
        String pinPattern = "^\\d{4,6}$";            // Numeric PIN, 4-6 digits
        String namePattern = "^[A-Za-z\\s]{2,26}$";  // Letters and spaces only, 2-50 characters

        // Check if PIN is valid
        if (!enteredPIN.matches(pinPattern)) {
            PinTextView.setError("PIN should be 4-6 digits long");
            return;
        }
        Cursor cursor = mDatabaseHelper.getUserByPIN(enteredPIN);

        if (cursor.moveToFirst()) {
            Toast.makeText(this, "PIN already registered", Toast.LENGTH_SHORT).show();
        } else {
            Cursor stdAccessCursor = mDatabaseHelper.getStdAccessData();

            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String DateCreated = dateFormat.format(new Date(currentTimeMillis));
            String LastModified = dateFormat.format(new Date(currentTimeMillis));
             cashierLevel = LevelSpinner.getSelectedItem().toString();
            if (cashierLevel.equals("Trainee")) {
                cashierLevel = "0";
            }
            String cashorname = CashierNameTextView.getText().toString();
            String cashordepartment = DepartmentTextView.getText().toString();

            // Validate required fields
            if (cashorname.isEmpty() || cashordepartment.isEmpty() || cashierLevel.isEmpty()) {
                Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate name and department with regex
            if (!cashorname.matches(namePattern)) {
                CashierNameTextView.setError("Name should only contain letters and be 2-50 characters long");
                return;
            }
            if (!cashordepartment.matches(namePattern)) {
                DepartmentTextView.setError("Department should only contain letters and be 2-50 characters long");
                return;
            }

            // Check if the department name already exists in the database
            Cursor departmentCursor = mDatabaseHelper.getDepartmentByName(cashordepartment);
            if (departmentCursor.moveToFirst()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Department Already Exists");
                builder.setMessage("The department already exists. Do you want to insert the user into this department?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Insert the record into the database
                        DBManager dbManager = new DBManager(AddPeopleActivity.this);
                        dbManager.open();



                        dbManager.insertUser(pin, cashorname, cashierLevel, cashordepartment,ShopName, DateCreated, LastModified, mDatabaseHelper);
                        dbManager.close();

                        // Clear the input fields
                        PinTextView.setText("");
                        DepartmentTextView.setText("");
                        CashierNameTextView.setText("");
                        // Reset spinners to default selection
                        LevelSpinner.setSelection(0);

                        // Redirect to the Product activity
                        Intent intent = new Intent(AddPeopleActivity.this, AdminActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear the input fields
                        PinTextView.setText("");
                        DepartmentTextView.setText("");
                        CashierNameTextView.setText("");
                        // Reset spinners to default selection
                        LevelSpinner.setSelection(0);

                        // Redirect to the Product activity
                        Intent intent = new Intent(AddPeopleActivity.this, AdminActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                builder.show();
                return;
            }



            // Insert the record into the database
            DBManager dbManager = new DBManager(this);
            dbManager.open();



            dbManager.insertUser(pin,cashorname, cashierLevel, cashordepartment, ShopName,DateCreated, LastModified,mDatabaseHelper);
            dbManager.close();

            // Clear the input fields
            PinTextView.setText("");
            DepartmentTextView.setText("");
            CashierNameTextView.setText("");
            // Reset spinners to default selection
            LevelSpinner.setSelection(0);


            // Redirect to the Product activity
            Intent intent = new Intent(AddPeopleActivity.this, AdminActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseHelper.close();
    }
}

