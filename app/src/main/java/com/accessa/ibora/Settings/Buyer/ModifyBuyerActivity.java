package com.accessa.ibora.Settings.Buyer;


import android.app.Activity;
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
import com.accessa.ibora.product.SubDepartment.SubDepartment;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class ModifyBuyerActivity extends Activity {
    private Button buttonUpdate;
    private Button buttonDelete;


    private DBManager dbManager;
    private EditText Name_Edittext;
    private EditText compName_Edittext;
    private EditText Tan_Edittext;
    private EditText BRN_Edittext;
    private EditText Adresse_Edittext;
    private EditText Type_Edittext;
    private EditText NIC_Edittext,editTextOtherNames;

    private long _id;
    private DatabaseHelper mDatabaseHelper;
    private Spinner spinnerBuyerType , spinnerBuyerProfile;

    private SharedPreferences sharedPreferences;

    private String cashorId;

    private  Spinner spinnerPriceLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Buyers");
        setContentView(R.layout.modify_buyer_info);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID



        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        Name_Edittext = findViewById(R.id.etName);
        editTextOtherNames = findViewById(R.id.etOtherName);
        compName_Edittext = findViewById(R.id.etcompName);
        Tan_Edittext = findViewById(R.id.etTAN);
        BRN_Edittext = findViewById(R.id.etBRN);
        Adresse_Edittext = findViewById(R.id.etBusinessAddr);
        spinnerBuyerType = findViewById(R.id.spinnerBuyerType);
        spinnerBuyerProfile = findViewById(R.id.spinnerBuyerprofile);
        NIC_Edittext = findViewById(R.id.etNIC);
        spinnerPriceLevel= findViewById(R.id.spinnerPriceLevel);


        // Initialize and set up the ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.buyer_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuyerType.setAdapter(adapter);

        // Initialize and set up the ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this,
                R.array.buyer_profiles, android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuyerProfile.setAdapter(adapters);

        // Initialize and set up the ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> adapters1 = ArrayAdapter.createFromResource(this,
                R.array.price_levels, android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceLevel.setAdapter(adapters1);

        mDatabaseHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);
        SubDepartment Subdepartment = dbManager.getSubDepartmentById(id);


        Buyer buyer = dbManager.getBuyerById(id);
        if (buyer != null) {
            Name_Edittext.setText(buyer.getNames());
            compName_Edittext.setText(buyer.getCompanyName());
            editTextOtherNames.setText(buyer.getBuyerOtherName());
            Tan_Edittext.setText(buyer.getTan());
            BRN_Edittext.setText(buyer.getBrn());
            Adresse_Edittext.setText(buyer.getBusinessAddr());


            // Get the array of buyer types from resources
            String[] buyerTypes = getResources().getStringArray(R.array.buyer_types);
            // Find the index of the buyer's type in the array
            int buyerTypeIndex = Arrays.asList(buyerTypes).indexOf(buyer.getBuyerType());

            // Get the array of buyer types from resources
            String[] buyerProfile = getResources().getStringArray(R.array.buyer_profiles);
            // Find the index of the buyer's type in the array
            int buyerProfileIndex = Arrays.asList(buyerProfile).indexOf(buyer.getProfile());


            // Get the array of buyer types from resources
            String[] buyerPriceLevel = getResources().getStringArray(R.array.price_levels);
            // Find the index of the buyer's type in the array
            int buyerPriceLevelIndex = Arrays.asList(buyerTypes).indexOf(buyer.getPriceLevel());



            // Set the selected item in the spinner
            spinnerBuyerType.setSelection(buyerTypeIndex);
            spinnerBuyerProfile.setSelection(buyerProfileIndex);
            NIC_Edittext.setText(buyer.getNic());
            spinnerPriceLevel.setSelection(buyerPriceLevelIndex);

        }


        buttonUpdate = findViewById(R.id.btnupdateBuyer);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDept();
            }
        });
        buttonDelete = findViewById(R.id.btndeleteBuyer);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });


    }


    private void updateDept() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String name = Name_Edittext.getText().toString().trim();
        String Othername = editTextOtherNames.getText().toString().trim();
        String compname = compName_Edittext.getText().toString().trim();
        String tan = Tan_Edittext.getText().toString().trim();
        String brn = BRN_Edittext.getText().toString().trim();
        String add = Adresse_Edittext.getText().toString().trim();
        String buyerType = spinnerBuyerType.getSelectedItem().toString();
        String Buyerprofile = spinnerBuyerProfile.getSelectedItem().toString();
        String BuyerPriceLevel = spinnerPriceLevel.getSelectedItem().toString();
        String nic = NIC_Edittext.getText().toString().trim();


        if (name.isEmpty()|| Othername.isEmpty() || BuyerPriceLevel.isEmpty() || compname.isEmpty() || tan.isEmpty() || brn.isEmpty() || add.isEmpty() || buyerType.isEmpty() || Buyerprofile.isEmpty() || nic.isEmpty() ) {
            Toast.makeText(this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbManager.updateBuyer( _id,name,Othername, compname, tan, brn,add,buyerType,Buyerprofile,nic,BuyerPriceLevel,lastmodified,cashorId);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, R.string.UpdatedBuyer, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.failUpdateBuyer, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteBuyer(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, R.string.DeleteBuyer, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.NotDeletedBuyer, Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "buyer_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDatabaseHelper.close();
    }
}

