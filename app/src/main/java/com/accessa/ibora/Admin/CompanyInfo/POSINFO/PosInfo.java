package com.accessa.ibora.Admin.CompanyInfo.POSINFO;

import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_COMPANY_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_POS_Num;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_TerminalNo;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_POS_ACCESS;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_STD_ACCESS;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.Admin.RegistorCashor;
import com.accessa.ibora.R;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PosInfo extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String cashorId;
    private String cashorName;
    private TextView CompanyName;
    private String shopName;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseHelper = new DatabaseHelper(this);

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        shopName = sharedPreferences.getString("ShopName", null); // Retrieve company name

        showInsertPopup();
    }

    private void showInsertPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.popup_insert_pos_access);

        // Set up the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Get references to the views in the popup layout
        EditText editTerminalNo = dialog.findViewById(R.id.editTerminalNo);
        EditText editCompanyName = dialog.findViewById(R.id.editCompanyName);
        Button btnInsert = dialog.findViewById(R.id.btnInsert);

        // Set up button click listener
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered data from the views
                String terminalNo = editTerminalNo.getText().toString();
                String companyName = shopName;
                String cashOrId = cashorId;

                // Perform validation and data insertion into the database
                if (validateData(terminalNo)) {
                    insertDataIntoPosAccessTable(terminalNo, companyName, cashOrId);
                    dialog.dismiss(); // Close the popup
                }
            }
        });
    }

    private boolean validateData(String terminalNo) {
        // Check if terminalNo is empty
        if (TextUtils.isEmpty(terminalNo)) {
            Toast.makeText(this, "Terminal No is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if terminalNo contains only digits (integer only)
        String regex = "\\d+"; // Modify this if you need a different pattern
        if (!terminalNo.matches(regex)) {
            Toast.makeText(this, "Invalid Terminal No. Only numbers are allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All data is valid
        return true;
    }

    private void insertDataIntoPosAccessTable(String terminalNo, String companyName, String cashOrId) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // Get the current date and time
        String dateTime = getCurrentDateTime();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TerminalNo, terminalNo);
        values.put(COLUMN_SHOPNAME, shopName);
        values.put(COLUMN_CASHOR_id, cashOrId);
        values.put(DatabaseHelper.LastModified, dateTime);
        values.put(DatabaseHelper.DateCreated, dateTime);

        long result = db.insert(TABLE_NAME_POS_ACCESS, null, values);
        if (result != -1) {


                // Data inserted successfully
                savePosNumber(terminalNo); // Save POS number to SharedPreferences
                Toast.makeText(this, "POS Number saved", Toast.LENGTH_SHORT).show();
                Log.d("update1","POS Number saved");
                // Now, update all rows in the std table with the same pos_num
                ContentValues updateValues = new ContentValues();
                updateValues.put(COLUMN_POS_Num, terminalNo); // Update the pos_num column

                // Perform the update operation without a WHERE clause (update all rows)
                int rowsUpdated = db.update(TABLE_NAME_STD_ACCESS, updateValues, null, null);

                if (rowsUpdated > 0) {
                    // Rows in std table updated successfully
                    Log.d("update","update");
                    Toast.makeText(this, "std table updated", Toast.LENGTH_SHORT).show();
                } else {
                    // No rows were updated
                    Toast.makeText(this, "std table not updated", Toast.LENGTH_SHORT).show();
                    Log.d("not update","not update");
                }
            // Data inserted successfully

            savePosNumber(terminalNo); // Save POS number to SharedPreferences
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        } else {
            // Failed to insert data
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void savePosNumber(String posNumber) {
        // To save data in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("posNumber", posNumber);
        editor.apply();
    }
}
