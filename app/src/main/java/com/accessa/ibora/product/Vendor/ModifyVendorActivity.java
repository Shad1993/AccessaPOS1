package com.accessa.ibora.product.Vendor;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.SubDepartment.SubDepartment;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModifyVendorActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;

private EditText VendorCode;
    private DBManager dbManager;
    private EditText VendName;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText PhoneNumber_edittext;
    private EditText Street_edittext;
    private EditText Town_edittext;
    private EditText PostalCode_edittext;
    private EditText Email_edittext;
    private EditText InternalCode_edittext;
    private EditText Salesmen_edittext;
    private long _id;
    private DatabaseHelper mDatabaseHelper;

    private SharedPreferences sharedPreferences;

    private String cashorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Department");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_vendor_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
         cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID



        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        VendName = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        VendorCode = findViewById(R.id.VendCode_edittext);
        PhoneNumber_edittext = findViewById(R.id.PhoneNumber_edittext);
        Street_edittext = findViewById(R.id.Street_edittext);
        Town_edittext = findViewById(R.id.Town_edittext);
        PostalCode_edittext = findViewById(R.id.PostalCode_edittext);
        Email_edittext = findViewById(R.id.Email_edittext);
        InternalCode_edittext = findViewById(R.id.InternalCode_edittext);
        Salesmen_edittext = findViewById(R.id.Salesmen_edittext);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        Vendor vendor = dbManager.getVendorById(id);
        if (vendor != null) {
            VendName.setText(vendor.getNomFournisseur());
            LastModified_Edittext.setText(vendor.getLastModified());
            VendorCode.setText(vendor.getCodeFournisseur());
            PhoneNumber_edittext.setText(vendor.getPhoneNumber());
            Street_edittext.setText(vendor.getStreet());
            Town_edittext.setText(vendor.getTown());
            PostalCode_edittext.setText(vendor.getPostalCode());
            Email_edittext.setText(vendor.getEmail());
            InternalCode_edittext.setText(vendor.getInternalCode());
            Salesmen_edittext.setText(vendor.getSalesmen());
        }




        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVendor();
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


    private void updateVendor() {
        String name = VendName.getText().toString().trim();

        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String VendName = name;
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String VendCode = VendorCode.getText().toString().trim();
        String updatedPhoneNumber = PhoneNumber_edittext.getText().toString();
        String updatedStreet = Street_edittext.getText().toString();
        String updatedTown = Town_edittext.getText().toString();
        String updatedPostalCode = PostalCode_edittext.getText().toString();
        String updatedEmail = Email_edittext.getText().toString();
        String updatedInternalCode = InternalCode_edittext.getText().toString();
        String updatedSalesmen = Salesmen_edittext.getText().toString();


            if (VendName.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty() || VendCode.isEmpty() || updatedPhoneNumber.isEmpty() ) {
                Toast.makeText(this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }


        boolean isUpdated = dbManager.updateVendor( _id,name, lastmodified, UserId, VendCode,updatedPhoneNumber,updatedStreet,updatedTown,updatedPostalCode,updatedEmail,updatedInternalCode,updatedSalesmen);
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
