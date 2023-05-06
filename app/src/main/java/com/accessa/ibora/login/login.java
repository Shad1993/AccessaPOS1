package com.accessa.ibora.login;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;

public class login extends AppCompatActivity {

    private EditText editTextPIN;
    private StringBuilder enteredPIN;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        editTextPIN = findViewById(R.id.editTextPIN);

        // Create or open the database
        database = openOrCreateDatabase("LoginDB", MODE_PRIVATE, null);

        // Create the table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS User (pin TEXT)");

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

    public void onClearButtonClick(View view) {
        // Clear the entered PIN and update the PIN EditText
        enteredPIN.setLength(0);
        editTextPIN.setText("");
    }

    public void onLoginButtonClick(View view) {
        String enteredPIN = editTextPIN.getText().toString();

        // Query the database for a matching PIN
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE pin = ?", new String[]{enteredPIN});

        if (cursor.moveToFirst()) {
            // PIN matched, login successful
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            // PIN not found, login failed
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
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
