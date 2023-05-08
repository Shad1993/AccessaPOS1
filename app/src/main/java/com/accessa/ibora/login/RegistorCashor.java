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

public class RegistorCashor extends AppCompatActivity {

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "User";

    // Column names

    static final String COLUMN_CASHOR_id = "cashorid";
    private static final String COLUMN_PIN = "pin";
    private static final String COLUMN_CASHOR_LEVEL = "cashorlevel";
    static final String COLUMN_CASHOR_NAME = "cashorname";
    private static final String COLUMN_CASHOR_DEPARTMENT = "cashorDepartment";

    private EditText editTextPIN,editTextLevel,editTextCashor,editTextName;
    private StringBuilder enteredPIN;

    String dbName = Constants.DB_NAME;

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

        // Create or open the database
        database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        // Define the SQL statement for creating the table
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_CASHOR_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PIN + " TEXT, "
                + COLUMN_CASHOR_LEVEL + " INTEGER, "
                + COLUMN_CASHOR_NAME + " TEXT, "
                + COLUMN_CASHOR_DEPARTMENT + " TEXT)";

// Execute the create table query
        database.execSQL(createTableQuery);

        // Initialize the StringBuilder for entered PIN
        enteredPIN = new StringBuilder();
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
            Cursor cursor = database.rawQuery("SELECT * FROM User WHERE pin = ?", new String[]{enteredPIN});

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
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE pin = ?", new String[]{enteredPIN});

        if (cursor.moveToFirst()) {
            // PIN already exists, registration failed
            Toast.makeText(this, "PIN already registered", Toast.LENGTH_SHORT).show();
        } else {
            // Additional fields for registration

            String cashorlevel = editTextLevel.getText().toString();
            String cashorname = editTextCashor.getText().toString();
            String cashordepartment = editTextName.getText().toString();

            // Insert the new user into the database
            database.execSQL("INSERT INTO User (pin, cashorlevel, cashorname, cashordepartment) VALUES (?, ?, ?, ?)",
                    new String[]{enteredPIN, String.valueOf(cashorlevel), cashorname, cashordepartment});

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






