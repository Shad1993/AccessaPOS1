package com.accessa.ibora.Admin.People;

import static com.accessa.ibora.product.category.CategoryDatabaseHelper.CatName;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_LEVEL;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.DEPARTMENT_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.Department;
import static com.accessa.ibora.product.items.DatabaseHelper.SUBDEPARTMENT_NAME;
import static com.accessa.ibora.sales.Sales.ItemGridAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.Admin.cashier;
import com.accessa.ibora.R;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.menu.Product;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModifyPeopleActivity extends Activity {
    private EditText NameText,DepartmentText;
    private EditText PIN;
    private Spinner spinnerCashierLevel,spinnerDepartment;
    private Button buttonUpdate;
    private Button buttonDelete;

    private DBManager dbManager;

    private long _id;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify People Details");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_modify_people_record);


        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        NameText = findViewById(R.id.IdCashierName_edittext);
        PIN = findViewById(R.id.PinEditText);

        spinnerCashierLevel = findViewById(R.id.Cashier_spinner);
        spinnerDepartment = findViewById(R.id.dept_spinner);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");


        _id = Long.parseLong(id);



        cashier user = dbManager.getUserById(id);
        if (user != null) {
            NameText.setText(user.getcashorname());
            PIN.setText(user.getpin());
            spinnerCashierLevel.setSelection(getSpinnerIndex1(spinnerCashierLevel, user.getcashorlevel()));
            spinnerDepartment.setSelection(getSpinnerIndex(spinnerDepartment, user.getcashorDepartment()));


        }
        Cursor departmentCursor = mDatabaseHelper.getAllDepartment();
        Cursor levelCursor  = mDatabaseHelper.getAlllevels();
        List<String> levels = new ArrayList<>();
        levels.add("All Levels");
        if (levelCursor.moveToFirst()) {
            do {
                String level1 = levelCursor.getString(levelCursor.getColumnIndex(COLUMN_CASHOR_LEVEL));
                levels.add(level1);
            } while (levelCursor.moveToNext());
        }
        levelCursor.close();

        List<String> departments = new ArrayList<>();
        departments.add("All Departments");
        if (departmentCursor.moveToFirst()) {
            do {
                String department = departmentCursor.getString(departmentCursor.getColumnIndex(DEPARTMENT_NAME));
                departments.add(department);
            } while (departmentCursor.moveToNext());
        }
        departmentCursor.close();
        // Get the array from resources and convert it to a list
        String[] cashierLevelsArray = getResources().getStringArray(R.array.cashier_levels);
        List<String> cashierLevelsList = new ArrayList<>(Arrays.asList(cashierLevelsArray));

// Replace "0" with "Trainee"
        cashierLevelsList.set(0, "Trainee");

// Create an ArrayAdapter with the modified list
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cashierLevelsList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Populate spinner with options 1 to 5

        spinnerCashierLevel.setAdapter(spinnerAdapter);


        ArrayAdapter<String> spinnerAdapterDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapterDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(spinnerAdapterDept);



        spinnerCashierLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

// Retrieve the existing category value from the database (replace item.getCategory() with the correct method)
        String existinglevel = user.getcashorlevel();

// Get the index of the existing category value in the spinner's data source
        int levelIndex = spinnerAdapter.getPosition(existinglevel);

        // Retrieve the existing category value from the database (replace item.getCategory() with the correct method)
        String existingDepartment = user.getcashorDepartment();
        // Get the index of the existing category value in the spinner's data source
        int DeptIndex = spinnerAdapterDept.getPosition(existingDepartment);


        // Set the selected item of the department to the existing category value
        spinnerDepartment.setSelection(DeptIndex);

        // Set the selected item of the level to the existing category value
        spinnerCashierLevel.setSelection(levelIndex);

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }







    private void updateItem() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String name = NameText.getText().toString().trim();
        String dept = spinnerDepartment.getSelectedItem().toString();
        String level = spinnerCashierLevel.getSelectedItem().toString();
        if (level.equals("Trainee")) {
            level = "0";
        }
        String enteredPIN = PIN.getText().toString();


        String namePattern = "^[A-Za-z\\s]{2,26}$"; // Letters and spaces only, 2-26 characters
        String pinPattern = "^\\d{4,6}$";           // Numeric PIN with 4-6 digits

        // Validate fields
        if (name.isEmpty() || enteredPIN.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!name.matches(namePattern)) {
            NameText.setError("Name should only contain letters and be 2-50 characters long");
            return;
        }
        if (!enteredPIN.matches(pinPattern)) {
            PIN.setError("PIN should be 4-6 digits long");
            return;
        }

        Cursor cursor = mDatabaseHelper.getUserByPIN(enteredPIN);

        if (cursor.moveToFirst() && cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id)) != _id) {
            // PIN already exists for another user
            Toast.makeText(this, "PIN already registered for another user", Toast.LENGTH_SHORT).show();
            return;
        }


            boolean isUpdated = dbManager.updateUser(_id, name, enteredPIN, dept ,level,lastmodified);
            returnHome();
            if (isUpdated) {
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
            }
        }


    private void deleteItem() {
        boolean isDeleted = dbManager.deleteUser(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), AdminActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "people_fragment");
        startActivity(home_intent1);
    }
    private int getSpinnerIndex(Spinner spinner, String value) {
        // Retrieve departments from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(spinner.getContext());
        Cursor departmentCursor = databaseHelper.getAllDepartment();

        List<String> departments = new ArrayList<>();

        // Iterate through the cursor and add department names to the list
        if (departmentCursor.moveToFirst()) {
            do {
                String department = departmentCursor.getString(departmentCursor.getColumnIndex(DEPARTMENT_NAME));
                departments.add(department);
            } while (departmentCursor.moveToNext());
        }

        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapterDept = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, departments);
        spinnerAdapterDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterDept);

        // Find the index of the selected value in the spinner
        int index = departments.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);
            Toast.makeText(spinner.getContext(), "Index: " + index, Toast.LENGTH_SHORT).show();
            return index;
        }

        // If the selected value is not found, default to the first item
        spinner.setSelection(0);
        Toast.makeText(spinner.getContext(), "Index: 0", Toast.LENGTH_SHORT).show();
        return 0; // Default index
    }

    private int getSpinnerIndex1(Spinner spinner, String value) {
        // Retrieve departments from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(spinner.getContext());
        Cursor levelCursor = databaseHelper.getAlllevels();

        List<String> user = new ArrayList<>();

        // Iterate through the cursor and add department names to the list
        if (levelCursor.moveToFirst()) {
            do {
                String userlevel = levelCursor.getString(levelCursor.getColumnIndex(COLUMN_CASHOR_LEVEL));
                user.add(userlevel);
            } while (levelCursor.moveToNext());
        }

        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapterLevel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, user);
        spinnerAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCashierLevel.setAdapter(spinnerAdapterLevel);


        // Find the index of the selected value in the spinner
        int index = user.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);
            Toast.makeText(spinner.getContext(), "Index: " + index, Toast.LENGTH_SHORT).show();
            return index;
        }

        // If the selected value is not found, default to the first item
        spinner.setSelection(0);
        Toast.makeText(spinner.getContext(), "Index: 0", Toast.LENGTH_SHORT).show();
        return 0; // Default index
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseHelper.close();
    }
}
