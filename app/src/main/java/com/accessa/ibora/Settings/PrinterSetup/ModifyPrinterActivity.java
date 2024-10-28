package com.accessa.ibora.Settings.PrinterSetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.PaymentFragment.payment;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyPrinterActivity extends Activity {
    private Button buttonUpdate;

    private SwitchCompat AllowPrinting;
    private boolean IsPrintingAllowed;

    private DBManager dbManager;
    private EditText Name_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText QRcode_Edittext;
    private long _id;
    private String  id;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
    private SharedPreferences sharedPreferences;

    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Printer Set Up");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_printert_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        Toast.makeText(this, "User ID: " + cashorId, Toast.LENGTH_SHORT).show();
        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();
        // Retrieve data from the intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
            String printstatus =intent.getStringExtra("printstatus");
        Name_Edittext = findViewById(R.id.qrName_edittext);

        Log.d("rooms_ERROR", "Fprintstatus: " + printstatus);
        AllowPrinting = findViewById(R.id.draweropener);
        // Populate EditText fields with received data
        Name_Edittext.setText(name);

        // Set the switch based on printstatus
        if (printstatus == null || printstatus.equals(" Pas Besoin D'imprimer.") || printstatus.equals("Printing not allowed.") || printstatus.equals("0")) {
            AllowPrinting.setChecked(false);
            Log.d("printstatus", " " + printstatus);
        } else {
            AllowPrinting.setChecked(true);
        }

        AllowPrinting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsPrintingAllowed = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifyPrinterActivity.this, R.string.draweropenrmsg, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifyPrinterActivity.this, R.string.drawernotopen, Toast.LENGTH_SHORT).show();
                }
            }
        });


         id = intent.getStringExtra("id");
        _id = Long.parseLong(id);




        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePayment();

            }
        });




    }


    private void updatePayment() {

        updateItem(getApplicationContext(), String.valueOf(_id),Name_Edittext,AllowPrinting);


    }

    public void updateItem(Context context, String idToUpdate, EditText editText, SwitchCompat switchCompat) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PrinterSetupPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the existing JSON string
        String jsonString = sharedPreferences.getString("PrinterSetupMethods", "[]");

        try {
            // Parse the JSON string into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);

            // Create a new JSONArray to hold the modified data
            JSONArray newArray = new JSONArray();

            // Retrieve new values from EditText and SwitchCompat
            String newName = editText.getText().toString();
            boolean newDrawerOpens = switchCompat.isChecked();

            // Iterate through the original array and update the item with the matching id
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.optString("id");

                if (id.equals(idToUpdate)) {
                    // Update fields for the item with the matching id
                    jsonObject.put("drawerOpens", newDrawerOpens);
                    jsonObject.put("name", newName);
                }

                // Add the (possibly modified) item to the new array
                newArray.put(jsonObject);
            }

            // Convert the new JSONArray back to a JSON string
            String updatedJsonString = newArray.toString();

            // Save the updated JSON string to SharedPreferences
            editor.putString("PrinterSetupMethods", updatedJsonString);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
        returnHome();
    }
    public void removeItemById(Context context, String idToRemove) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PrinterSetupPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the existing JSON string
        String jsonString = sharedPreferences.getString("PrinterSetupMethods", "[]");

        try {
            // Parse the JSON string into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);

            // Create a new JSONArray to hold the filtered data
            JSONArray newArray = new JSONArray();

            // Iterate through the original array and add items to the new array except the one with the matching id
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.optString("id");

                if (!id.equals(idToRemove)) {
                    newArray.put(jsonObject);
                }
            }

            // Convert the new JSONArray back to a JSON string
            String updatedJsonString = newArray.toString();

            // Save the updated JSON string to SharedPreferences
            editor.putString("PrinterSetupMethods", updatedJsonString);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
        returnHome();
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Printer_fragment");
        startActivity(home_intent1);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
