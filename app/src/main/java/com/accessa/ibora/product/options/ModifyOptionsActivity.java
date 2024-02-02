package com.accessa.ibora.product.options;



import static com.accessa.ibora.product.items.DatabaseHelper.OPTIONS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.OPTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANTS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_BARCODE;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.VARIANT_PRICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Vendor.Vendor;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Variant;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModifyOptionsActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;


    private DBManager dbManager;
    private EditText OptionName;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;

    private long _id;
    private DatabaseHelper mDatabaseHelper;

    private SharedPreferences sharedPreferences;

    private String cashorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Options");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_options_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID



        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        OptionName = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);



        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);



        Options1 Options = dbManager.getOptionsById(id);
        if (Options != null) {
            OptionName.setText(Options.getOptionsName());


        }
        List<Variant> variantList = dbManager.getVariantsById(id);
        if (variantList != null && !variantList.isEmpty()) {
            for (Variant variant : variantList) {
                createVariantButton(Long.parseLong(id), variant.getBarcode(), variant.getDescription(), variant.getPrice());
            }
        }

// Inside your onCreate or setup method
        Button addVariantButton = findViewById(R.id.add_variant);
        addVariantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddVariantDialogs(id);
            }
        });


        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOptions();
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

    private void updateVariantInDatabase(long variantOptionId, String newBarcode, String newDescription, double newPrice) {
        try {
            // Assuming you have a DatabaseHelper instance
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Open the database for writing
            SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Prepare the ContentValues to update the row in the VARIANTS_TABLE_NAME
                ContentValues values = new ContentValues();
                values.put(VARIANT_BARCODE, newBarcode);
                values.put(VARIANT_DESC, newDescription);
                values.put(VARIANT_PRICE, newPrice);

                // Update the data in the VARIANTS_TABLE_NAME based on VARIANT_ID
                int rowsAffected = db.update(VARIANTS_TABLE_NAME, values, VARIANT_BARCODE + " = ?", new String[]{String.valueOf(newBarcode)});

                // Close the database
                db.close();

                if (rowsAffected > 0) {
                    // Update successful
                    // Optionally, you can perform any other action needed after successful update
                    createVariantButton(variantOptionId, newBarcode, newDescription, newPrice);
                } else {
                    // No rows were affected, update failed
                    Log.e("DatabaseUpdate", "Failed to update data in VARIANTS_TABLE_NAME");
                }

        } catch (Exception e) {
            // Handle any exceptions that occur during the update process
            e.printStackTrace();
            // Display a toast or perform any other action as needed
            Toast.makeText(ModifyOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            Log.e("DatabaseUpdate", "Exception: " + e.getMessage());
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
    private void createVariantButton(long optionId, String barcode, String description, double price) {
        LinearLayout variantButtonsLayout = findViewById(R.id.variantButtonsLayout);

        Button variantButton = new Button(this);
        variantButton.setText(description); // Set the button text to the variant description
        variantButton.setTag(barcode); // Set a tag to identify the variant (you can use barcode or variantId)

        // Click listener for normal click
        variantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, e.g., open details or perform an action
                showAddVariantDialog(optionId, barcode, description, price);
            }
        });

        // Long click listener for deletion
        variantButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Handle long click, e.g., show confirmation dialog and delete the variant
                showDeleteVariantDialog(optionId, barcode);
                return true; // Consume the long click event
            }
        });

        // Add the button to the layout
        variantButtonsLayout.addView(variantButton);

        // Optionally, you can customize the button appearance (e.g., set background, text color, etc.)
    }

    private void showDeleteVariantDialog(long optionId, String barcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Variant");
        builder.setMessage("Are you sure you want to delete this variant?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call a method to delete the variant from the database
                deleteVariantFromDatabase(optionId, barcode);

                // Remove the button from the layout
                removeVariantButton(barcode);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteVariantFromDatabase(long optionId, String barcode) {
        boolean isDeleted = dbManager.deleteVariant(barcode);
        returnHome();
        if (isDeleted) {

            Toast.makeText(this, R.string.Variants_deleted_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.failed_to_delete_Variants, Toast.LENGTH_SHORT).show();
        }
    }

    private void removeVariantButton(String barcode) {
        LinearLayout variantButtonsLayout = findViewById(R.id.variantButtonsLayout);

        // Find and remove the button with the specified barcode tag
        for (int i = 0; i < variantButtonsLayout.getChildCount(); i++) {
            View child = variantButtonsLayout.getChildAt(i);
            if (child instanceof Button && child.getTag() != null && child.getTag().equals(barcode)) {
                variantButtonsLayout.removeViewAt(i);
                break;
            }
        }
    }

    private void showAddVariantDialogs(String optionid) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_variant);

        // Initialize dialog views
        EditText barcodeEditText = dialog.findViewById(R.id.editTextBarcode);
        EditText descEditText = dialog.findViewById(R.id.editTextDescription);
        EditText priceEditText = dialog.findViewById(R.id.editTextPrice);
        Button addButton = dialog.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String barcode = barcodeEditText.getText().toString().trim();
                String description = descEditText.getText().toString().trim();
                String priceText = priceEditText.getText().toString().trim();
                long newoptionid= 1;

                if (!barcode.isEmpty() && !description.isEmpty() && !priceText.isEmpty()) {
                    // Convert price to double
                    double price = Double.parseDouble(priceText);

                    // Get the OPTION_ID associated with the current option
                    long optionId = Long.parseLong(optionid); // Implement this method to get the OPTION_ID
                    Log.e("optionId", String.valueOf(optionId));
                    if(optionId <= 0)
                    {
                        newoptionid= 1;
                    }
                    else
                    {
                        newoptionid= optionId ;
                    }

                    Log.e("newoptionid", String.valueOf(newoptionid));
                    // Display a toast or perform any other action as needed


                    // Insert the variant into the database
                    insertVariantIntoDatabase(newoptionid, barcode, description, price);


                    // Close the dialog
                    dialog.dismiss();
                } else {
                    Toast.makeText(ModifyOptionsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }


    private void insertVariantIntoDatabase(long optionId, String barcode, String description, double price) {
        try {
            // Assuming you have a DatabaseHelper instance
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Open the database for writing
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Check if the barcode already exists
            if (isBarcodeUnique(db, barcode)) {
                // Prepare the ContentValues to insert into the VARIANTS_TABLE_NAME
                ContentValues values = new ContentValues();
                values.put(VARIANT_ID, optionId); // Assuming VARIANT_ID is the correct column name for OPTION_ID in the VARIANTS_TABLE_NAME
                values.put(VARIANT_BARCODE, barcode);
                values.put(VARIANT_DESC, description);
                values.put(VARIANT_PRICE, price);

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
                Toast.makeText(ModifyOptionsActivity.this, "Barcode already exists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the insertion process
            e.printStackTrace();
            // Display a toast or perform any other action as needed
            Toast.makeText(ModifyOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            Log.e("DatabaseInsert", "Exception: " + e.getMessage());
        }
    }
    private void showAddVariantDialog(long optionId,String barcode,String description,double Price) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_modify_variant);

        // Initialize dialog views
        EditText barcodeEditText = dialog.findViewById(R.id.editTextBarcode);
        EditText descEditText = dialog.findViewById(R.id.editTextDescription);
        EditText priceEditText = dialog.findViewById(R.id.editTextPrice);
        Button addButton = dialog.findViewById(R.id.addButton);
        String price= String.valueOf(Price);
        descEditText.setText(description);
        barcodeEditText.setText(barcode);
        priceEditText.setText(price);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String barcode = barcodeEditText.getText().toString().trim();
                String description = descEditText.getText().toString().trim();
                String priceText = priceEditText.getText().toString().trim();
                long newoptionid= 1;

                if (!barcode.isEmpty() && !description.isEmpty() && !priceText.isEmpty()) {
                    // Convert price to double
                    double price = Double.parseDouble(priceText);

                    // Get the OPTION_ID associated with the current option
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
                    updateVariantInDatabase(newoptionid, barcode, description, price);


                    // Close the dialog
                    dialog.dismiss();
                } else {
                    Toast.makeText(ModifyOptionsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void updateOptions() {

        String name = OptionName.getText().toString().trim();




        if ( name .isEmpty()  ) {
            Toast.makeText(this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }


        boolean isUpdated = dbManager.updateOptions( _id, name);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, R.string.Options_updated_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } else {

            Toast.makeText(this, R.string.failed_to_update_options, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteOption(_id);
        returnHome();
        if (isDeleted) {

            Toast.makeText(this, R.string.OPTION_deleted_successfully, Toast.LENGTH_SHORT).show();
            deleteVariants();
            finish();
        } else {
            Toast.makeText(this, R.string.failed_to_delete_Options, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteVariants() {
        boolean isDeleted = dbManager.deleteVariants(_id);
        returnHome();
        if (isDeleted) {

            Toast.makeText(this, R.string.Variants_deleted_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.failed_to_delete_Variants, Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Options_fragment");
        startActivity(home_intent1);
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseHelper.close();
    }
}
