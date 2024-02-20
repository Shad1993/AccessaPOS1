package com.accessa.ibora.login;




import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_Shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.accessa.ibora.Constants;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.MainActivityMobile;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.salestype;
import com.bumptech.glide.Glide;

public class login extends AppCompatActivity {
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_Users = "Users";

    // Column names
    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String PAYMENT_TYPE_KEY = "PaymentType";
    static final String COLUMN_CASHOR_id = "cashorid";
    private static final String COLUMN_PIN = "pin";
    private static final String COLUMN_CASHOR_LEVEL = "cashorlevel";
    static final String COLUMN_CASHOR_NAME = "cashorname";
    private static final String COLUMN_CASHOR_DEPARTMENT = "cashorDepartment";
    private static final String COLUMN_COMPANY_NAME = "CompanyName";
    private AlertDialog alertDialog;
    private EditText editTextPIN;
    private StringBuilder enteredPIN;

    private SQLiteDatabase database;
    String dbName = Constants.DB_NAME;
    private DBManager dbManager;
    private DatabaseHelper mDatabaseHelper;

    //sharedpreferences

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove onscreen Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.login);


        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        // Create or open the SharedPreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("pricelevel", Context.MODE_PRIVATE);

        // Check if the key is already present
        if (!sharedPreferences.contains("selectedPriceLevel")) {
            // If not present, set the default value
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selectedPriceLevel", "Price Level 1");
            editor.apply();
        }

        // Initialize SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Set "roomnum" to 1
        editor.putInt("roomnum", 1);
        editor.putString("table_id", "1");

        // Commit the changes
        editor.apply();
        // Initialize views
        editTextPIN = findViewById(R.id.editTextPIN);


        // Initialize the StringBuilder for entered PIN
        enteredPIN = new StringBuilder();
        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);
        database = mDatabaseHelper.getReadableDatabase();





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

        // Query the database for the entered PIN
        Cursor cursor = mDatabaseHelper.getUserByPIN(enteredPIN);



        if (cursor.moveToFirst()) {


            // PIN matched, login successfull
            // Get the cashor's name from the cursor
            String cashorName = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_NAME));
            String cashorId = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id));
            String cashorlevel = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_LEVEL));
            String ShopName = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_Shop));



            // Remove the buyer info from shared preferences
            clearBuyerInfoFromPrefs();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cashorName", cashorName); // Store cashor's name
            editor.putString("cashorId", cashorId); // Store cashor's ID
            editor.putString("cashorlevel", cashorlevel); // Store cashor's level
            editor.putString("ShopName", ShopName); // Store company name
            editor.apply();
            getSelectedPaymentType();



            Intent intent = new Intent(this, salestype.class);
            intent.putExtra("cashorName", cashorName);
            intent.putExtra("cashorId", cashorId);
            intent.putExtra("ShopName", ShopName);
            startActivity(intent);
                // Use a Handler to delay the redirection





        } else {

            // Create an editor to modify the preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

// Clear all the stored values
            editor.clear();

// Apply the changes
            editor.apply();
            // PIN not found, login failed
            Toast.makeText(this, getString(R.string.pinfailed), Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.wrongPin));
            builder.setMessage(getString(R.string.wrongpinget));

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
                    onClearButtonClick(view);
                }
            });

            builder.setCancelable(false);
        }

        cursor.close();
    }
    private void clearBuyerInfoFromPrefs() {
        SharedPreferences sharedPrefs = this.getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }
    public void onNumberButtonClick(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        // Append the pressed number to the entered PIN
        enteredPIN.append(number);

        // Update the PIN EditText with the entered numbers
        editTextPIN.setText(enteredPIN.toString());
    }
    private String getSelectedPaymentType() {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PAYMENT_TYPE_KEY, "full"); // Default to "full" if not found
    }

    private void saveSelectedPaymentType(String paymentType) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PAYMENT_TYPE_KEY, paymentType);
        editor.apply();
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
