package com.accessa.ibora.login;



import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Constants;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;

public class RegistorCashor extends AppCompatActivity {

    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_Users = "Users";


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
        setContentView(R.layout.registor);



// Initialize EditText fields
        editTextPIN = findViewById(R.id.editTextPIN);
        editTextLevel = findViewById(R.id.editTextLevel);
        editTextCashor = findViewById(R.id.editTextCashor);
        editTextName = findViewById(R.id.editTextName);
        editTextCompanyName= findViewById(R.id.editTextCompanyName);

        // Set click listener for Login button
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

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

    public void onNumberButtonClick(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        // Append the pressed number to the entered PIN
        enteredPIN.append(number);

        // Update the PIN EditText with the entered numbers
        editTextPIN.setText(enteredPIN.toString());
    }
    private void login() {
        // Retrieve user input from EditText fields
        String pin = editTextPIN.getText().toString();

            String enteredPIN = editTextPIN.getText().toString();

            // Query the database for a matching PIN
        Cursor cursor = mDatabaseHelper.getUserByPIN(enteredPIN);

            if (cursor.moveToFirst()) {
                // PIN matched, login successful
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                // Navigate to another activity
                Intent intent = new Intent(RegistorCashor.this, login.class);
                startActivity(intent);
            } else {
                // PIN not found, login failed
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        }
        // Perform login operation using SQLite database
        // Add your code here to check the login credentials and navigate to the appropriate activity
        // For example, you can use a SQLiteOpenHelper to handle database operations




    private void register() {
        // Retrieve user input from EditText fields
        String pin = editTextPIN.getText().toString();

        // Perform registration operation using SQLite database
        // Add your code here to insert the registration details into the database
        String enteredPIN = editTextPIN.getText().toString();

        // Check if the entered PIN is empty
        if (enteredPIN.isEmpty()) {
            Toast.makeText(this, "Please enter a PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query the database for the entered PIN
        Cursor cursor = mDatabaseHelper.getUserByPIN(enteredPIN);

        if (cursor.moveToFirst()) {
            // PIN already exists, registration failed
            Toast.makeText(this, "PIN already registered", Toast.LENGTH_SHORT).show();
        } else {
            // Additional fields for registration

            String cashorlevel = editTextLevel.getText().toString();
            String cashorname = editTextCashor.getText().toString();
            String cashordepartment = editTextName.getText().toString();
            String CompanyName= editTextCompanyName.getText().toString();
            dbManager = new DBManager(getApplicationContext());
            dbManager.open();
            Cursor cursor1 = dbManager.Registor(pin, cashorlevel, cashorname, cashordepartment,CompanyName );


            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistorCashor.this, login.class);
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






