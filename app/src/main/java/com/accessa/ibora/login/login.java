package com.accessa.ibora.login;

import static com.accessa.ibora.login.RegistorCashor.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.login.RegistorCashor.COLUMN_CASHOR_id;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.accessa.ibora.Constants;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.bumptech.glide.Glide;

public class login extends AppCompatActivity {

    private AlertDialog alertDialog;
    private EditText editTextPIN;
    private StringBuilder enteredPIN;

    private SQLiteDatabase database;
    String dbName = Constants.DB_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // remove onscreen Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        String enteredPIN = editTextPIN.getText().toString();

        // Query the database for a matching PIN
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE pin = ?", new String[]{enteredPIN});

        if (cursor.moveToFirst()) {
            // PIN matched, login successfull
            // Get the cashor's name from the cursor
            String cashorName = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_NAME));
            String cashorId = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id));

            // Create and show the AlertDialog with the welcome message and cashor's name
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Welcome to Ibora");
            builder.setMessage("Welcome, " + cashorName + "!");






            // Inflate the custom view for the AlertDialog
            View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null);

            // Get a reference to the AppCompatImageView
            AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);

            // Load the GIF using Glide
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.folderwalk)
                    .into(gifImageView);
                  // Find the "Retry" button
                Button retryButton = view.findViewById(R.id.button_retry);

                // Set the visibility of the "Retry" button to GONE
                retryButton.setVisibility(View.GONE);

                 // Remove the click listener for the "Retry" button
                retryButton.setOnClickListener(null);

            // Set the custom view to the AlertDialog
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();


            final Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("cashorName", cashorName); // Pass cashorName to MainActivity
            intent.putExtra("cashorId", cashorId); // Pass cashorId to MainActivity
            // Use a Handler to delay the redirection
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) { // Check if the activity is still running
                        startActivity(intent);
                        finish(); // Optional: Finish the current activity if needed
                    }
                }
            }, 1000);  // Delay in milliseconds (1 second
            builder.setCancelable(false);

        } else {
            // PIN not found, login failed
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong PIN");
            builder.setMessage("Wrong PIN, Please Retry!");

            // Inflate the custom view for the AlertDialog
            View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null);

            // Get a reference to the AppCompatImageView
            AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);

            // Load the GIF using Glide
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.close)
                    .into(gifImageView);

            // Set the custom view to the AlertDialog
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();

            // Check if the activity is still running before showing the dialog
            if (!isFinishing()) {
                alertDialog.show();
            }

            // Find the "Retry" button
            Button retryButton = view.findViewById(R.id.button_retry);

            // Set a click listener for the "Retry" button
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retry button clicked
                    // Perform any necessary actions here
                    // For example, you can close the dialog or retry the login process
                    alertDialog.dismiss(); // Dismiss the dialog
                    // Add your desired actions here
                }
            });

            builder.setCancelable(false);
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
        // Dismiss the dialog if it is showing to prevent window leaks
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        // Close the database connection when the activity is destroyed
        if (database != null) {
            database.close();
        }
    }
}
