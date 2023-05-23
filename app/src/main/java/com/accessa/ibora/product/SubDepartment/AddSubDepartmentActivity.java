package com.accessa.ibora.product.SubDepartment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AddSubDepartmentActivity extends Activity {
    private DatabaseHelper mDatabaseHelper;
    private Spinner  departmentCodeSpinner;
      private EditText DeptName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
   private  String selectedDepartmentCode;

   private String departmentid;
    private String cashorId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Department");


        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);


        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID




        setContentView(R.layout.add_subdepartment_activity);

        DeptName_Edittext = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        departmentCodeSpinner = findViewById(R.id.Dept_Code_spinner);
        // Add Record
        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecord();
                    }
                });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

        // department code spinner
        mDatabaseHelper = new DatabaseHelper(this);
        Cursor departmentCodeCursor = mDatabaseHelper.getAllDepartment();
        List<String> departments = new ArrayList<>();
        departments.add(getString(R.string.AllDepartments));
        if (departmentCodeCursor.moveToFirst()) {
            do {
                String departmentCode = departmentCodeCursor.getString(departmentCodeCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE));
                String departmentName = departmentCodeCursor.getString(departmentCodeCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
                departments.add(departmentCode + " - " + departmentName);

                 departmentid = departmentCodeCursor.getString(departmentCodeCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID));
            } while (departmentCodeCursor.moveToNext());
        }
        departmentCodeCursor.close();

        ArrayAdapter<String> spinnerAdapterDeptCode = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        spinnerAdapterDeptCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentCodeSpinner.setAdapter(spinnerAdapterDeptCode);
        departmentCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Extract the department code from the selected item
                String[] parts = selectedItem.split(" - ");
                 selectedDepartmentCode = parts[0];
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

    }


    private void addRecord() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String SubDeptName = DeptName_Edittext.getText().toString().trim();
        String LastModified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String DeptCode = selectedDepartmentCode;

        // Check if all required fields are filled
        if (SubDeptName.isEmpty()
                ) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        int selectedDepartmentId = Integer.parseInt(departmentid);// get the selected department ID from the database or other means
        dbManager.insertSubDept(SubDeptName, LastModified, UserId, DeptCode, selectedDepartmentId);
        dbManager.close();

        // Clear the input
        DeptName_Edittext.setText("");
        LastModified_Edittext.setText("");
        Userid_Edittext.setText("");


        returnHome();


    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "SUBDept_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}