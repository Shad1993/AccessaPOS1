package com.accessa.ibora.product.SubCategory;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.accessa.ibora.R;
import com.accessa.ibora.product.category.CategoryDBManager;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.defaults.colorpicker.ColorPickerPopup;

public class ModifySubCategoryActivity extends Activity {

    private EditText CatNameText;
    private Button buttonupdate, buttondelete;
    private EditText ColorText;

    private long _id;

    private Cursor CursorUpdate;
    private CategoryDBManager CatdbManager;

    private DatabaseHelper mDatabaseHelper;

    private ImageView imageView;

    SwitchCompat printerOptionSwitch;
    private boolean IsNeededToPrint;
    private Button  mPickColorButton;

    private View mColorPreview;

    private int mDefaultColor;
    private Map<String, Integer> categoryMap; // Declare categoryMap as a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Sub Category");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_sub_category);

        CatdbManager = new CategoryDBManager(this);
        CatdbManager.open();

        CatNameText = (EditText) findViewById(R.id.CatName_edittext);
        mDatabaseHelper = new DatabaseHelper(this);
        printerOptionSwitch = findViewById(R.id.printer_option_switch);
        categoryMap = mDatabaseHelper.getCategories();
        List<String> categoryNames = new ArrayList<>(categoryMap.keySet());
        Spinner catSpinner = findViewById(R.id.cat_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String CatName = intent.getStringExtra("CatName");
        String relatedcatid = intent.getStringExtra("relatedcatid");

        boolean printingstatus = mDatabaseHelper.getPrintingStatus(Long.parseLong(id));
        Log.d("printingstatus", "  " + printingstatus);

        _id = Long.parseLong(id);
        CatNameText.setText(CatName);

        // Initialize IsNeededToPrint based on the current switch status
        IsNeededToPrint = printingstatus;
        printerOptionSwitch.setChecked(printingstatus);

        printerOptionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsNeededToPrint = isChecked;
                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifySubCategoryActivity.this, R.string.categoryprinted, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifySubCategoryActivity.this, R.string.categorynotprinted, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Find the corresponding category name based on the relatedcatid
        int relatedCategoryId = Integer.parseInt(relatedcatid);
        String relatedCategoryName = null;
        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            if (entry.getValue() == relatedCategoryId) {
                relatedCategoryName = entry.getKey();
                break;
            }
        }

        if (relatedCategoryName != null) {
            // Find the index of the related category in the spinner's list
            int spinnerPosition = adapter.getPosition(relatedCategoryName);
            // Set the spinner to the correct position
            catSpinner.setSelection(spinnerPosition);
        }
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryNames.get(position); // Get selected category name
                int selectedCategoryId = categoryMap.get(selectedCategory); // Get the corresponding category ID
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no selection is made
            }
        });

        buttonupdate = (Button) findViewById(R.id.btn_update);
        buttonupdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivity();
            }
        });

        buttondelete = (Button) findViewById(R.id.btn_delete);
        buttondelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivityDel();
            }
        });
    }


    public void openNewActivity() {
        String CatName = CatNameText.getText().toString();
        Spinner catSpinner = findViewById(R.id.cat_spinner);
        String printingstatus = String.valueOf(IsNeededToPrint);
        String selectedCategory = catSpinner.getSelectedItem().toString();
        int selectedCategoryId = categoryMap.get(selectedCategory);
        Log.d("selectedCategory", String.valueOf(printingstatus));


        // Use selectedCategoryId as needed
        Toast.makeText(ModifySubCategoryActivity.this, "printingstatus ID: " + printingstatus, Toast.LENGTH_SHORT).show();

        if (CatName.isEmpty() ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
       mDatabaseHelper.updateSubCategory(_id, CatName, selectedCategoryId,printingstatus);
        returnHome();
    }

    public void openNewActivityDel() {
        mDatabaseHelper.deletesubcat(_id);
        returnHome();
        Toast.makeText(getApplicationContext(), "Category Deleted", Toast.LENGTH_LONG).show();
    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "SuB_Category_fragment");
        startActivity(home_intent1);
    }
}
