package com.accessa.ibora.Admin;



import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Constants;
import com.accessa.ibora.R;
import com.accessa.ibora.company.InsertCompanyDataActivity;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistorAdmin extends AppCompatActivity {

    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_Users = "Users";

    private Spinner spinnerCashierLevel;
    private EditText editTextPIN,editTextLevel,editTextCashor,editTextName,editTextCompanyName;
    private StringBuilder enteredPIN;
    private DBManager dbManager;
    String dbName = Constants.DB_NAME;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.register_admin);



// Initialize EditText fields
        editTextPIN = findViewById(R.id.editTextPIN);
        editTextCashor = findViewById(R.id.editTextCashor);
        editTextName = findViewById(R.id.editTextName);



        // Set click listener for Register button
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        // Initialize views
        editTextPIN = findViewById(R.id.editTextPIN);





        // Initialize the StringBuilder for entered PIN
        enteredPIN = new StringBuilder();
        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);
        database = mDatabaseHelper.getReadableDatabase();
    }



    private void register() {
        String pin = editTextPIN.getText().toString();
        String enteredPIN = editTextPIN.getText().toString();

        if (enteredPIN.isEmpty()) {
            Toast.makeText(this, "Please enter a PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = mDatabaseHelper.getUserByPIN(enteredPIN);

        if (cursor.moveToFirst()) {
            Toast.makeText(this, "PIN already registered", Toast.LENGTH_SHORT).show();
        } else {
            Cursor stdAccessCursor = mDatabaseHelper.getStdAccessData();

            if (stdAccessCursor.moveToFirst()) {
                String companyName = stdAccessCursor.getString(stdAccessCursor.getColumnIndexOrThrow("company_name"));

                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                String DateModified = dateFormat.format(new Date(currentTimeMillis));
                String LastModified = dateFormat.format(new Date(currentTimeMillis));
                String cashorname = editTextCashor.getText().toString();
                String cashordepartment = "Admin";
                String cashierLevel = "5" ;
                // Auto-increment the department code
                String departmentCode = DBManager.getAutoIncrementedDepartmentCode();
                ContentValues values = new ContentValues();
                ContentValues values1 = new ContentValues();
                values.put("CompanyName", companyName);
                values.put("pin", enteredPIN);
                values.put("cashorname", cashorname);
                values.put("cashorDepartment", cashordepartment);
                values.put("cashorlevel", cashierLevel);
                values.put("DateCreated", DateModified);
                values.put("LastModified", LastModified);
                values1.put("DepartmentCode", departmentCode);
                values1.put("cashorDepartment", cashordepartment);

                long result = mDatabaseHelper.insertUserData(values, values1);

                if (result != -1) {
                    Toast.makeText(this, "User data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to insert user data", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                // Additional fields for registration
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                String cashierLevel = "5";
                String cashorname = editTextCashor.getText().toString();
                String cashordepartment = editTextName.getText().toString();
                String DateModified = dateFormat.format(new Date(currentTimeMillis));
                String LastModified = dateFormat.format(new Date(currentTimeMillis));
                dbManager = new DBManager(getApplicationContext());
                dbManager.open();
                Cursor cursor1 = dbManager.Registor(pin, cashierLevel, cashorname, cashordepartment,DateModified,LastModified);


                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistorAdmin.this, InsertCompanyDataActivity.class);
                startActivity(intent);
                return;
            }

            // User data has been inserted, proceed to login or any other desired activity
            Intent intent = new Intent(RegistorAdmin.this, login.class);
            startActivity(intent);
        }
        cursor.close();
    }


    public void onClearButtonClick(View view) {
        // Clear the entered PIN and update the PIN EditText
        enteredPIN.setLength(0);
        editTextPIN.setText("");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        if (database != null) {
            database.close();
        }
    }
}






