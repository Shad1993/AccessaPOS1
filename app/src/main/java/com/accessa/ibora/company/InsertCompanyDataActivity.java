package com.accessa.ibora.company;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.accessa.ibora.Admin.RegistorCashor;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.SelectProfile;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;

public class InsertCompanyDataActivity extends Activity {

    private EditText SHOPNAME;

    private ImageView imageLogo;
    private Button btnUploadImage;
    private EditText editVATNo;
    private EditText editBRNNo;
    private EditText editADR1;
    private EditText editADR2;
    private EditText editADR3;
    private EditText editTelNo;
    private EditText editFaxNo;

    private EditText editCompanyName;
    private Spinner spinnerCashierLevel;
    private Button btnInsertData;

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        SHOPNAME = findViewById(R.id.editAbbrev);
        imageLogo = findViewById(R.id.imageLogo);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        editVATNo = findViewById(R.id.editVATNo);
        editBRNNo = findViewById(R.id.editBRNNo);
        editADR1 = findViewById(R.id.editADR1);
        editADR2 = findViewById(R.id.editADR2);
        editADR3 = findViewById(R.id.editADR3);
        editTelNo = findViewById(R.id.editTelNo);
        editFaxNo = findViewById(R.id.editFaxNo);
        editCompanyName = findViewById(R.id.editCompanyName);
        btnInsertData = findViewById(R.id.btnInsertData);



        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageLogo.setImageURI(imageUri);
            imageLogo.setVisibility(View.VISIBLE);
        }
    }

    private void insertData() {
        String abbrev = SHOPNAME.getText().toString();
        String vatNo = editVATNo.getText().toString();
        String brnNo = editBRNNo.getText().toString();
        String adr1 = editADR1.getText().toString();
        String adr2 = editADR2.getText().toString();
        String adr3 = editADR3.getText().toString();
        String telNo = editTelNo.getText().toString();
        String faxNo = editFaxNo.getText().toString();
        String companyName = editCompanyName.getText().toString();

        String logoPath = getLogoPath();

        ContentValues values = new ContentValues();
        values.put("Abbrev", abbrev);
        values.put("Logo", logoPath);
        values.put("VAT_No", vatNo);
        values.put("BRN_No", brnNo);
        values.put("ADR_1", adr1);
        values.put("ADR_2", adr2);
        values.put("ADR_3", adr3);
        values.put("Tel_No", telNo);
        values.put("Fax_No", faxNo);
        values.put("Company_Name", companyName);


        ContentValues value = new ContentValues();
        value.put("CompanyName", companyName);
        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        // Retrieve the current count of discounts
        int currentCompanyCount = dbManager.getCompanyCount();

        // Check if the current count exceeds the limit
        if (currentCompanyCount >= 1) {
            // Show a pop-up message indicating the limit has been reached
            Toast.makeText(this, R.string.MaxCom, Toast.LENGTH_SHORT).show();
            return;
        }
        long result = database.insert("std_access", null, values);
        int rowsAffected = database.update("Users", value, null, null);
        dbManager.close();



        if (rowsAffected > 0) {
            Toast.makeText(InsertCompanyDataActivity.this, "Data updated successfully in users", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(InsertCompanyDataActivity.this, "Failed to update data in users", Toast.LENGTH_SHORT).show();
        }
        if (result != -1) {
            Toast.makeText(InsertCompanyDataActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields
            SHOPNAME.setText("");
            imageLogo.setImageResource(R.drawable.accessalogo);
            imageUri = null;
            imageLogo.setVisibility(View.GONE);
            editVATNo.setText("");
            editBRNNo.setText("");
            editADR1.setText("");
            editADR2.setText("");
            editADR3.setText("");
            editTelNo.setText("");
            editFaxNo.setText("");
            editCompanyName.setText("");
            Intent intent = new Intent(InsertCompanyDataActivity.this, RegistorCashor.class);
            startActivity(intent);
        } else {
            Toast.makeText(InsertCompanyDataActivity.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLogoPath() {
        if (imageUri != null) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();
                return imagePath;
            }
        }
        return "";
    }
}
