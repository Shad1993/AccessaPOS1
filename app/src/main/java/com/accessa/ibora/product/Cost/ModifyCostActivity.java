package com.accessa.ibora.product.Cost;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Vendor.Vendor;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyCostActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;

private Spinner VendorCode;
    private DBManager dbManager;
    private EditText Barcode;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;

    private long _id;
    private DatabaseHelper mDatabaseHelper;

    private SharedPreferences sharedPreferences;

    private String cashorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Department");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_cost_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
         cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID



        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        Barcode = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        VendorCode = findViewById(R.id.Vendor_Code_spinner);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        Vendor vendor = dbManager.getVendorById(id);
        if (vendor != null) {
            Barcode.setText(vendor.getNomFournisseur());
            LastModified_Edittext.setText(vendor.getLastModified());
            //VendorCode.setSelection(getSpinnerIndex(CostCodeSpinner, Cost.getSubDepartmentCode()));

        }




        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCost();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });


// department code spinner
        mDatabaseHelper = new DatabaseHelper(this);

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }


    private void updateCost() {


        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String VendCode  = cashorId;



            if (  lastmodified.isEmpty() || UserId.isEmpty() || VendCode.isEmpty() ) {
                Toast.makeText(this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }


        boolean isUpdated = dbManager.updateCost( _id, lastmodified, UserId, VendCode);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, R.string.vendor_updated_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } else {

            Toast.makeText(this, R.string.failed_to_update_vendor, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteVendor(_id);
        returnHome();
        if (isDeleted) {

            Toast.makeText(this, R.string.vendor_deleted_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.failed_to_delete_vendor, Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Vend_fragment");
        startActivity(home_intent1);
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseHelper.close();
    }
}
