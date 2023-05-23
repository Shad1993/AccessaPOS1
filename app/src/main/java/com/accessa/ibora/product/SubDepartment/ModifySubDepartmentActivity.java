package com.accessa.ibora.product.SubDepartment;

import static com.accessa.ibora.product.category.CategoryDatabaseHelper.CatName;
import static com.accessa.ibora.product.items.DatabaseHelper.DEPARTMENT_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
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
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModifySubDepartmentActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;

    private Spinner DepartmentCodeSpinner;
    private DBManager dbManager;
    private EditText DeptName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private  String selectedDepartmentCode;
    private String departmentid;
    private long _id;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private  String departmentNames;
    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Department");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_sub_department_activity);

        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
         cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        DeptName_Edittext = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        DepartmentCodeSpinner = findViewById(R.id.Dept_Code_spinner);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        SubDepartment Subdepartment = dbManager.getSubDepartmentById(id);
        if (Subdepartment != null) {
            DeptName_Edittext.setText(Subdepartment.getSubName());
            LastModified_Edittext.setText(Subdepartment.getLastModified());
            DepartmentCodeSpinner.setSelection(getSpinnerIndex(DepartmentCodeSpinner, Subdepartment.getSubDepartmentCode()));
        }
        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDept();
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


        Cursor SubdepartmentCodeCursor = mDatabaseHelper.getAllSubDepartment();
        List<String> subDepartments = new ArrayList<>();
        subDepartments.add(getString(R.string.AllDepartments));
        if (SubdepartmentCodeCursor.moveToFirst()) {
            do {
                String departmentCode = SubdepartmentCodeCursor.getString(SubdepartmentCodeCursor.getColumnIndex(DEPARTMENT_CODE));
                String deptName = SubdepartmentCodeCursor.getString(SubdepartmentCodeCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
                String combinedString = departmentCode + " - " + deptName;
                subDepartments.add(combinedString );

                departmentid = SubdepartmentCodeCursor.getString(SubdepartmentCodeCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID));
            } while (SubdepartmentCodeCursor.moveToNext());
        }

        SubdepartmentCodeCursor.close();
        ArrayAdapter<String> spinnerAdapterSubDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subDepartments);
        spinnerAdapterSubDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentCodeSpinner.setAdapter(spinnerAdapterSubDept);


        DepartmentCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        String existingSubDepartment = Subdepartment.getSubDepartmentCode();
// Get the index of the existing category value in the spinner's data source

        int SubDeptIndex = spinnerAdapterSubDept.getPosition(existingSubDepartment);



        // Set the selected item of the CategorySpinner to the existing category value
        DepartmentCodeSpinner.setSelection(SubDeptIndex);

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }


    private void updateDept() {
        String name = DeptName_Edittext.getText().toString().trim();

        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String DeptName = DeptName_Edittext.getText().toString().trim();
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String DeptCode = selectedDepartmentCode;
        if (DeptName.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty() || DeptCode.isEmpty() ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbManager.updateDept( _id,name, lastmodified, UserId, DeptCode);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, "Department updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update Department", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteDept(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Dept_fragment");
        startActivity(home_intent1);
    }


    private int getSpinnerIndex(Spinner spinner, String value) {
        // Retrieve department codes and names from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(spinner.getContext());
        Cursor subDepartmentCursor = databaseHelper.getAllSubDepartment();

        List<String> subDepartments = new ArrayList<>();
        List<String> departmentNames = new ArrayList<>(); // List to store department names

        // Iterate through the cursor and add department codes and names to the lists
     
        if (subDepartmentCursor.moveToFirst()) {
            do {
                String departmentCode = subDepartmentCursor.getString(subDepartmentCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE));
                String deptName = subDepartmentCursor.getString(subDepartmentCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
                String combinedString = departmentCode + " - " + deptName;
                subDepartments.add(combinedString);
                departmentNames.add(deptName); // Add the deptName to the departmentNames list
            } while (subDepartmentCursor.moveToNext());
        }


        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, subDepartments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Find the index of the selected value in the spinner
        int index = subDepartments.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);

            // Retrieve the corresponding department name from the departmentNames list
            String deptName = departmentNames.get(index);

            // Use the retrieved deptName as needed
            // For example, you can assign it to a variable or use it in your code logic

            return index;
        }

        // Toast the index, department code, and department name
        Toast.makeText(this, "Index: " + index, Toast.LENGTH_SHORT).show();

        // If the selected value is not found in subDepartments, default to the first item
        spinner.setSelection(0);

        return 0; // Default index
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
