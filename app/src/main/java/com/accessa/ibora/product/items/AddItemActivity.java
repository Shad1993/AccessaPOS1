package com.accessa.ibora.product.items;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.category.Category;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends Activity {
    private CategoryDatabaseHelper CatDatabaseHelper;
    private DatabaseHelper mDatabaseHelper;
    private EditText subjectEditText;
    private EditText descEditText;
    private EditText PriceEditText;
    private Spinner CategorySpinner,DepartmentSpinner,SubDepartmentSpinner;
    private EditText BarcodeEditText;

    private EditText LongDescEditText;
    private EditText WeightEditText;
    private EditText QuantityEditText;
    private DatePicker expirydate_picker;
    private EditText VATEditText;
    private String formattedDate;
    private RadioGroup soldBy_radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Record");

        setContentView(R.layout.activity_add_record);

        subjectEditText = findViewById(R.id.itemName_edittext);
        descEditText = findViewById(R.id.description_edittext);
        PriceEditText = findViewById(R.id.price_edittext);
        CategorySpinner = findViewById(R.id.category_spinner);
        BarcodeEditText = findViewById(R.id.IdBarcode_edittext);
        DepartmentSpinner = findViewById(R.id.Dept_spinner);
        SubDepartmentSpinner = findViewById(R.id.SubDept_spinner);
        LongDescEditText = findViewById(R.id.long_description_edittext);
        WeightEditText = findViewById(R.id.weight_edittext);
        QuantityEditText = findViewById(R.id.quantity_edittext);
        expirydate_picker = findViewById(R.id.expirydate_picker);
        VATEditText = findViewById(R.id.Vat_edittext);



        int year = expirydate_picker.getYear();
        int month = expirydate_picker.getMonth() + 1; // Month is zero-based, so add 1
        int dayOfMonth = expirydate_picker.getDayOfMonth();

         formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year);

        // Retrieve the items from the database
        CatDatabaseHelper = new CategoryDatabaseHelper(this);
        mDatabaseHelper=new DatabaseHelper(this);
        Cursor cursor = CatDatabaseHelper.getAllCategory();
        Cursor cursor1 = mDatabaseHelper.getAllDepartment();
        Cursor cursor2 = mDatabaseHelper.getAllDepartment();

        List<String> categories = new ArrayList<>();
        categories.add("All Category");
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndex(CategoryDatabaseHelper.CatName));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        List<String> departments = new ArrayList<>();
        categories.add("All Departments");
        if (cursor1.moveToFirst()) {
            do {
                String department = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
                categories.add(department);
            } while (cursor1.moveToNext());
        }
        cursor.close();
        List<String> subdepartments = new ArrayList<>();
        categories.add("All sub Departments");
        if (cursor2.moveToFirst()) {
            do {
                String subdepartment = cursor2.getString(cursor.getColumnIndex(DatabaseHelper.SUBDEPARTMENT_NAME));
                categories.add(subdepartment);
            } while (cursor2.moveToNext());
        }
        cursor.close();
        // Create an ArrayAdapter for the spinner with the custom layout
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(spinnerAdapter);
        DepartmentSpinner.setAdapter(spinnerAdapter);
        SubDepartmentSpinner.setAdapter(spinnerAdapter);

        // Set a listener for item selection
        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });
        DepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        SubDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        // Add Record
        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecord();
                    }
                });



    }private void addRecord() {
        soldBy_radioGroup = (RadioGroup) findViewById(R.id.soldBy_radioGroup);

        int selectedId = soldBy_radioGroup.getCheckedRadioButtonId();

        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedSoldBy = selectedRadioButton.getText().toString();

        String barcode = BarcodeEditText.getText().toString().trim();
        String name = subjectEditText.getText().toString().trim();
        String desc = descEditText.getText().toString().trim();
        String category = CategorySpinner.getSelectedItem().toString().trim();
        String quantity = QuantityEditText.getText().toString().trim();
        String department = DepartmentSpinner.getText().toString().trim();
        String longDescription = LongDescEditText.getText().toString().trim();
        String subDepartment = SubDepartmentSpinner.getText().toString().trim();
        String price = PriceEditText.getText().toString().trim();
        String vat = VATEditText.getText().toString().trim();
        String expiryDate = formattedDate;
        String availableForSale = formattedDate;
        String soldBy = selectedSoldBy;
        String image = formattedDate;
        // Check if all required fields are filled
        if (name.isEmpty() || desc.isEmpty() || price.isEmpty() || barcode.isEmpty()
                || department.isEmpty() || subDepartment.isEmpty() || category.isEmpty() || availableForSale.isEmpty()
                || soldBy.isEmpty() || image.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        dbManager.insert(name, desc, price, category, barcode, department,
                subDepartment, longDescription, quantity, expiryDate, vat, availableForSale, soldBy, image);
        dbManager.close();

        // Clear the input
        subjectEditText.setText("");
        descEditText.setText("");
        PriceEditText.setText("");
        BarcodeEditText.setText("");
        LongDescEditText.setText("");
        WeightEditText.setText("");
        QuantityEditText.setText("");

        VATEditText.setText("");

        // Reset spinners to default selection
        CategorySpinner.setSelection(0);
        DepartmentSpinner.setText("");
        SubDepartmentSpinner.setText("");

        // Redirect to the Product activity
        Intent intent = new Intent(AddItemActivity.this, Product.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        CatDatabaseHelper.close();
    }
}