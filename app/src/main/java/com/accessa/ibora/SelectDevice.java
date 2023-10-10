package com.accessa.ibora;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Admin.RegistorCashor;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class SelectDevice extends AppCompatActivity {
    Button buttonMobile, buttonT2;
private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase database;
    private static IWoyouService woyouService;
    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);

            // Call the method to display on the LCD here
            displayOnLCd();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdevice);
        Intent intent1 = new Intent();
        intent1.setPackage("woyou.aidlservice.jiuiv5");
        intent1.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent1, connService, Context.BIND_AUTO_CREATE);
        showSecondaryScreen();
        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);
        database = mDatabaseHelper.getReadableDatabase();
        buttonMobile = findViewById(R.id.sunmiMob);
        buttonMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store "mobile" as the device type in shared preferences
                storeDeviceType("mobile");
            }
        });

// mob
        buttonT2 = findViewById(R.id.SunmiT2);
        buttonT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store "sunmit2" as the device type in shared preferences
                storeDeviceType("sunmit2");
            }
        });



    }

    // Method to store the device type in shared preferences
    private void storeDeviceType(String deviceType) {
        // Define a custom name for your SharedPreferences
        String customSharedPreferencesName = "device";

// Create the SharedPreferences instance with the custom name
        SharedPreferences sharedPreferences = getSharedPreferences(customSharedPreferencesName, Context.MODE_PRIVATE);

// Now, you can edit and use this sharedPreferences as needed
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("device_type", deviceType);
        editor.apply();

// Start the SelectLanguage activity
        Intent intent = new Intent(SelectDevice.this, SelectLanguage.class);
        startActivity(intent);

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

            displayOnLCd();
        }
    }
    public  void displayOnLCd() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {


            woyouService.sendLCDDoubleString("Welcome" , "Back " , null);

        } catch (RemoteException e) {
            e.printStackTrace();
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

}
