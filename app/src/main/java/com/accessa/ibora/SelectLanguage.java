package com.accessa.ibora;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Admin.RegistorCashor;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.List;
import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {
    Button buttonEng, buttonFr;
private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.selectlanguage);

        showSecondaryScreen();
        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);
        database = mDatabaseHelper.getReadableDatabase();
        // Eng language
        buttonEng = findViewById(R.id.buttonEng);
        buttonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Locale.ENGLISH);
            }

        });

        // Fr language
        buttonFr = findViewById(R.id.buttonFr);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Locale.FRENCH);
            }
        });
    }

    public void openNewActivity(Locale locale) {
        // Set the app's locale to the selected language
        Locale.setDefault(locale);

        // Update the configuration to reflect the new locale
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());


        CheckTableUser();
        CheckTableCompany();
    }

   public void CheckTableUser(){
        // Check if "Users" table is not empty
        boolean isUserTableEmpty = mDatabaseHelper.isUserTableEmpty();

        if (!isUserTableEmpty) {
            // "Users" table is not empty, redirect to the login activity
            Intent intent = new Intent(SelectLanguage.this, login.class);
            startActivity(intent);
            finish(); // Optional: Finish the current activity to prevent going back to it
        } else {
            // "Users" table is empty, proceed with the current flow
            // Start the SelectProfile activity
            Intent intent = new Intent(this, RegistorCashor.class);
            startActivity(intent);
        }
    }
    private void showSecondaryScreen() {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();

        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            SeconScreenDisplay secondaryDisplay = new SeconScreenDisplay(this, presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();

            // Update the RecyclerView data on the secondary screen
            secondaryDisplay.displaydefault();
        } else {
            // Secondary screen not found or not supported
            Toast.makeText(this, "Secondary screen not found or not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private Display getPresentationDisplay() {
        DisplayManager displayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                return display;
            }
        }
        return null;
    }
    public void CheckTableCompany(){
        // Check if "Company" table is not empty
        boolean isUserTableEmpty = mDatabaseHelper.isCompanyTableEmpty();

        if (!isUserTableEmpty) {
            // "Company" table is not empty, redirect to the login activity
            Intent intent = new Intent(SelectLanguage.this, login.class);
            startActivity(intent);
            finish(); // Optional: Finish the current activity to prevent going back to it
        } else {
            // "Company" table is empty, proceed with the current flow
            // Start the SelectProfile activity
            Intent intent = new Intent(this, welcome.class);
            startActivity(intent);
        }
    }
}
