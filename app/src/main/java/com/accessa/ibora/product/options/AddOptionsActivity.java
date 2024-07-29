package com.accessa.ibora.product.options;


import static com.accessa.ibora.product.items.DatabaseHelper.OPTIONS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.OPTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANTS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_BARCODE;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.Variant_OPTION_ID;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddOptionsActivity extends Activity {
    private DatabaseHelper mDatabaseHelper;

    private EditText OptName_Edittext;

    private  String selectedDepartmentCode;

    private String departmentid;
    private String cashorId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Options");


        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);


        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID




        setContentView(R.layout.add_options_activity);

        OptName_Edittext = findViewById(R.id.DeptName_edittext);

        // Add Record
        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecord();
                    }
                });
// Inside your onCreate or setup method
        Button addVariantButton = findViewById(R.id.add_variant);
        addVariantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddVariantDialog();
            }
        });



    }

    private void showAddVariantDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_variant);

        // Initialize dialog views
        EditText barcodeEditText = dialog.findViewById(R.id.editTextBarcode);
        EditText descEditText = dialog.findViewById(R.id.editTextDescription);
        EditText priceEditText = dialog.findViewById(R.id.editTextPrice);
        EditText ItemIdEditText = dialog.findViewById(R.id.editTextitemid);
        Button addButton = dialog.findViewById(R.id.addButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String barcode = barcodeEditText.getText().toString().trim();
                String description = descEditText.getText().toString().trim();
                String priceText = priceEditText.getText().toString().trim();
                String itemid = ItemIdEditText.getText().toString().trim();
                long newoptionid= 1;

                if (!barcode.isEmpty() && !description.isEmpty() && !priceText.isEmpty() && !itemid.isEmpty()) {
                    // Convert price to double
                    double price = Double.parseDouble(priceText);

                    // Get the OPTION_ID associated with the current option
                    long optionId = getCurrentOptionId(); // Implement this method to get the OPTION_ID
                    Log.e("optionId", String.valueOf(optionId));
                    if(optionId <= 0)
                    {
                         newoptionid= 1;
                    }
                    else
                    {
                         newoptionid= optionId + 1;
                    }

                    Log.e("newoptionid", String.valueOf(newoptionid));
                    // Display a toast or perform any other action as needed


                    // Insert the variant into the database
                    insertVariantIntoDatabase(String.valueOf(newoptionid), barcode, description, price,itemid);


                    // Close the dialog
                    dialog.dismiss();
                } else {
                    Toast.makeText(AddOptionsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void insertVariantIntoDatabase(String optionId, String barcode, String description, double price,String itemid) {
        try {
            // Assuming you have a DatabaseHelper instance
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Open the database for writing
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Check if the barcode already exists
            if (isBarcodeUnique(db, barcode) || isItemidUnique(db,itemid)) {
                // Prepare the ContentValues to insert into the VARIANTS_TABLE_NAME
                ContentValues values = new ContentValues();
                values.put(VARIANT_ID, optionId); // Assuming VARIANT_ID is the correct column name for OPTION_ID in the VARIANTS_TABLE_NAME
                values.put(VARIANT_BARCODE, barcode);
                values.put(VARIANT_DESC, description);
                values.put(VARIANT_PRICE, price);
                values.put(VARIANT_ITEM_ID, itemid);
                // Insert the data into the VARIANTS_TABLE_NAME
                long variantId = db.insert(VARIANTS_TABLE_NAME, null, values);

                // Close the database
                db.close();

                if (variantId != -1) {
                    // Insertion successful
                    // Optionally, you can use the variantId if needed for further actions
                    // After successful insertion, dynamically create a button for the variant
                    createVariantButton(optionId, barcode, description, price);
                } else {
                    // Insertion failed
                    Log.e("DatabaseInsert", "Failed to insert data into VARIANTS_TABLE_NAME");
                }
            } else {
                // Barcode is not unique, show a popup or toast
                Toast.makeText(AddOptionsActivity.this, "Barcode already exists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the insertion process
            e.printStackTrace();
            // Display a toast or perform any other action as needed
            Toast.makeText(AddOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            Log.e("DatabaseInsert", "Exception: " + e.getMessage());
        }
    }

    private boolean isBarcodeUnique(SQLiteDatabase db, String barcode) {
        String[] projection = {VARIANT_BARCODE};
        String selection = VARIANT_BARCODE + " = ?";
        String[] selectionArgs = {barcode};

        Cursor cursor = db.query(VARIANTS_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        boolean isUnique = cursor == null || cursor.getCount() == 0;

        if (cursor != null) {
            cursor.close();
        }

        return isUnique;
    }
    private boolean isItemidUnique(SQLiteDatabase db, String itemid) {
        String[] projection = {VARIANT_ITEM_ID};
        String selection = VARIANT_ITEM_ID + " = ?";
        String[] selectionArgs = {itemid};

        Cursor cursor = db.query(VARIANTS_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        boolean isUnique = cursor == null || cursor.getCount() == 0;

        if (cursor != null) {
            cursor.close();
        }

        return isUnique;
    }
    private void createVariantButton(String optionId, String barcode, String description, double price) {
        LinearLayout variantButtonsLayout = findViewById(R.id.variantButtonsLayout);

        Button variantButton = new Button(this);
        variantButton.setText(description); // Set the button text to the variant description
        variantButton.setTag(barcode); // Set a tag to identify the variant (you can use barcode or variantId)
        variantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, e.g., open details or perform an action
                Toast.makeText(AddOptionsActivity.this, "Variant Clicked: " + description, Toast.LENGTH_SHORT).show();
            }
        });

        // Add the button to the layout
        variantButtonsLayout.addView(variantButton);

        // Optionally, you can customize the button appearance (e.g., set background, text color, etc.)
    }

    // Implement this method to get the OPTION_ID associated with the current option
    private long getCurrentOptionId() {
        // Assuming you have a DatabaseHelper instance
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Open the database for reading
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {OPTION_ID}; // Replace "OPTION_ID" with the actual column name

        // Order the results by descending order of "OPTION_ID" to get the latest ID
        String sortOrder = OPTION_ID + " DESC";

        // Perform the query to get the last inserted row's ID
        Cursor cursor = db.query(OPTIONS_TABLE_NAME, projection, null, null, null, null, sortOrder);

        long lastOptionId = -1;

        if (cursor.moveToFirst()) {
            // Retrieve the last inserted row's ID
            lastOptionId = cursor.getLong(cursor.getColumnIndex(OPTION_ID));
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return lastOptionId;
    }


    private void addRecord() {

        String OptName = OptName_Edittext.getText().toString().trim();

        // Check if all required fields are filled
        if (OptName.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();

        dbManager.insertOpt(OptName );
        dbManager.close();

        // Clear the input
        OptName_Edittext.setText("");


        returnHome();


    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Option_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}