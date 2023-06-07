package com.accessa.ibora.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.SelectProfile;
import com.accessa.ibora.product.items.DatabaseHelper;

public class InsertCompanyDataActivity extends Activity {

    private EditText editAbbrev;
    private EditText editNoStock;
    private EditText editNoPrices;
    private EditText editDefSupplierCode;
    private EditText editLogo;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        editAbbrev = findViewById(R.id.editAbbrev);
        editNoStock = findViewById(R.id.editNoStock);
        editNoPrices = findViewById(R.id.editNoPrices);
        editDefSupplierCode = findViewById(R.id.editDefSupplierCode);
        editLogo = findViewById(R.id.editLogo);
        editVATNo = findViewById(R.id.editVATNo);
        editBRNNo = findViewById(R.id.editBRNNo);
        editADR1 = findViewById(R.id.editADR1);
        editADR2 = findViewById(R.id.editADR2);
        editADR3 = findViewById(R.id.editADR3);
        editTelNo = findViewById(R.id.editTelNo);
        editFaxNo = findViewById(R.id.editFaxNo);
        editCompanyName = findViewById(R.id.editCompanyName);
        spinnerCashierLevel = findViewById(R.id.spinnerCashierLevel);
        btnInsertData = findViewById(R.id.btnInsertData);

        // Populate spinner with options 1 to 5
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cashier_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCashierLevel.setAdapter(adapter);

        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        String abbrev = editAbbrev.getText().toString();
        int noStock = Integer.parseInt(editNoStock.getText().toString());
        int noPrices = Integer.parseInt(editNoPrices.getText().toString());
        String defSupplierCode = editDefSupplierCode.getText().toString();
        String logo = editLogo.getText().toString();
        String vatNo = editVATNo.getText().toString();
        String brnNo = editBRNNo.getText().toString();
        String adr1 = editADR1.getText().toString();
        String adr2 = editADR2.getText().toString();
        String adr3 = editADR3.getText().toString();
        String telNo = editTelNo.getText().toString();
        String faxNo = editFaxNo.getText().toString();
        String companyName = editCompanyName.getText().toString();
        String cashierLevel = spinnerCashierLevel.getSelectedItem().toString();

        ContentValues values = new ContentValues();
        values.put("Abbrev", abbrev);
        values.put("No_Stock", noStock);
        values.put("No_Prices", noPrices);
        values.put("Def_Supplier_Code", defSupplierCode);
        values.put("Logo", logo);
        values.put("VAT_No", vatNo);
        values.put("BRN_No", brnNo);
        values.put("ADR_1", adr1);
        values.put("ADR_2", adr2);
        values.put("ADR_3", adr3);
        values.put("Tel_No", telNo);
        values.put("Fax_No", faxNo);
        values.put("Company_Name", companyName);
        values.put("Cashier_Level", cashierLevel);

        long result = database.insert("Table_Name_Std_Access", null, values);
        if (result != -1) {
            Toast.makeText(InsertCompanyDataActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields
            editAbbrev.setText("");
            editNoStock.setText("");
            editNoPrices.setText("");
            editDefSupplierCode.setText("");
            editLogo.setText("");
            editVATNo.setText("");
            editBRNNo.setText("");
            editADR1.setText("");
            editADR2.setText("");
            editADR3.setText("");
            editTelNo.setText("");
            editFaxNo.setText("");
            editCompanyName.setText("");
            Intent intent = new Intent(InsertCompanyDataActivity.this, SelectProfile.class);
            startActivity(intent);
        } else {
            Toast.makeText(InsertCompanyDataActivity.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }
    }
}
