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

public class login extends AppCompatActivity {

    private EditText editTextPIN;
    private StringBuilder enteredPIN;

    private SQLiteDatabase database;
    String dbName = Constants.DB_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.login);


        // Initialize views
        editTextPIN = findViewById(R.id.editTextPIN);


        // Create or open the database
        database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        // Create the table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS User (pin TEXT)");

        // Initialize the StringBuilder for entered PIN
        enteredPIN = new StringBuilder();
        // Set click listener for Login button
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
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
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        } else {
            // PIN not found, login failed
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    public void onNumberButtonClick(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        // Append the pressed number to the entered PIN
        enteredPIN.append(number);

        // Update the PIN EditText with the entered numbers
        editTextPIN.setText(enteredPIN.toString());
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
