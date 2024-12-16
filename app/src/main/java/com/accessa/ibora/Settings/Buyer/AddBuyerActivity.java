package com.accessa.ibora.Settings.Buyer;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBuyerActivity extends AppCompatActivity {

    private EditText editTextName,editTextcompname;
    private EditText editTextTan;
    private EditText editTextBrn,editTextOtherNames;
    private EditText editTextBusinessAddr;
    private EditText editTextBuyerType;
    private Spinner spinnerBuyerType,spinnerBuyerProfile;
    private EditText editTextNic;
    private Button buttonSave;
    private  Spinner spinnerPriceLevel;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buyer);

        databaseHelper = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        editTextName = findViewById(R.id.etName);
        editTextOtherNames = findViewById(R.id.etOtherName);
        editTextcompname = findViewById(R.id.etcompName);
        editTextTan = findViewById(R.id.etTAN);
        editTextBrn = findViewById(R.id.etBRN);
        editTextBusinessAddr = findViewById(R.id.etBusinessAddr);
        editTextNic = findViewById(R.id.etNIC);
        buttonSave = findViewById(R.id.btnAddBuyer);

        // Find the Spinner for price levels
         spinnerPriceLevel = findViewById(R.id.spinnerPriceLevel);

// Get the array of price levels from resources
        String[] priceLevels = getResources().getStringArray(R.array.price_levels);

// Create an ArrayAdapter using the price levels array and a default spinner layout
        ArrayAdapter<String> priceLevelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priceLevels);

// Specify the layout to use when the list of choices appears
        priceLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinnerPriceLevel.setAdapter(priceLevelAdapter);

        spinnerBuyerType = findViewById(R.id.spinnerBuyerType);

        // Get the array of buyer types from resources
        String[] buyerTypes = getResources().getStringArray(R.array.buyer_types);

        // Create an ArrayAdapter using the buyer types array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buyerTypes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerBuyerType.setAdapter(adapter);

         spinnerBuyerProfile = findViewById(R.id.spinnerBuyerprofile);

        // Get the array of buyer types from resources
        String[] buyerProfile = getResources().getStringArray(R.array.buyer_profiles);

        // Create an ArrayAdapter using the buyer types array and a default spinner layout
        ArrayAdapter<String> adapters = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buyerProfile);

        // Specify the layout to use when the list of choices appears
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerBuyerProfile.setAdapter(adapters);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBuyer();
            }
        });
    }

    private void saveBuyer() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String name = editTextName.getText().toString();
        String Othername = editTextOtherNames.getText().toString();
        String tan = editTextTan.getText().toString();
        String companyName =editTextcompname.getText().toString();
        String brn = editTextBrn.getText().toString();
        String businessAddr = editTextBusinessAddr.getText().toString();
        String priceLevel = spinnerPriceLevel.getSelectedItem().toString(); // Get the selected price level

        String buyerType = spinnerBuyerType.getSelectedItem().toString();

        String Buyerprofile = spinnerBuyerProfile.getSelectedItem().toString();
        String nic = editTextNic.getText().toString();

        Buyer newBuyer = new Buyer(name,Othername, tan, brn, businessAddr, buyerType,Buyerprofile, nic,companyName,priceLevel,cashorId,lastmodified,lastmodified);

        boolean success = databaseHelper.addBuyer(newBuyer);

        if (success) {
            // Redirect to the Product activity
            returnHome();
            finish();
        } else {
            Toast.makeText(this, "Failed To insert buyer", Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "buyer_fragment");
        startActivity(home_intent1);
    }
}
