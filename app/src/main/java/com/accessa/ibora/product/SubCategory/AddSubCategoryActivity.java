package com.accessa.ibora.product.SubCategory;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.accessa.ibora.R;
import com.accessa.ibora.product.category.CategoryDBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.defaults.colorpicker.ColorPickerPopup;


public class AddSubCategoryActivity extends Activity {

    private Button addTodoBtn;
    private EditText CatNameEditText;

    Button AddButton;
    private CategoryDBManager CatdbManager;

    private boolean IsNeededToPrint;
    private DatabaseHelper mDatabaseHelper;

    SwitchCompat printerOptionSwitch;
    private Map<String, Integer> categoryMap; // Declare categoryMap as a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Sub Category");

        setContentView(R.layout.addsubcategory);
        mDatabaseHelper = new DatabaseHelper(this);
        CatNameEditText = (EditText) findViewById(R.id.CatName_edittext);

         printerOptionSwitch = findViewById(R.id.printer_option_switch);
        Spinner catSpinner = findViewById(R.id.cat_spinner);
        categoryMap = mDatabaseHelper.getCategories();
        List<String> categoryNames = new ArrayList<>(categoryMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter);

        printerOptionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsNeededToPrint = isChecked;
                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddSubCategoryActivity.this, R.string.categoryprinted, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddSubCategoryActivity.this, R.string.categorynotprinted, Toast.LENGTH_SHORT).show();
                }
            }
        });


        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryNames.get(position); // Get selected category name
                int selectedCategoryId = categoryMap.get(selectedCategory); // Get the corresponding category ID

                // Use selectedCategoryId as needed
                Toast.makeText(AddSubCategoryActivity.this, "Selected Category ID: " + selectedCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no selection is made
            }
        });


        //Add Record
        addTodoBtn = (Button) findViewById(R.id.add_record);

        CatdbManager = new CategoryDBManager(this);
        CatdbManager.open();


        // Add Record
        AddButton = (Button) findViewById(R.id.add_record);
        AddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
    }



    public void openNewActivity() {
        final String CatName = CatNameEditText.getText().toString();
        String printingstatus = String.valueOf(IsNeededToPrint);

        // Get selected category ID from Spinner
        Spinner catSpinner = findViewById(R.id.cat_spinner);
        String selectedCategory = catSpinner.getSelectedItem().toString();
        int selectedCategoryId = categoryMap.get(selectedCategory);

        if (CatName.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert data into database
        mDatabaseHelper.insertSubCategory(CatName, selectedCategoryId, printingstatus);

        // Create an Intent to navigate to the desired Fragment
        Intent intent = new Intent(AddSubCategoryActivity.this, Product.class);
        intent.putExtra("fragment", "SuB_Category_fragment");

        startActivity(intent);
    }



}
