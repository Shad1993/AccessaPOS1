package com.accessa.ibora.product.Department;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

public class AddDepartmentActivity extends Activity {


      private EditText DeptName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText Deptcode_Edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Department");

        setContentView(R.layout.add_department_activity);

        DeptName_Edittext = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        Deptcode_Edittext = findViewById(R.id.deptcode_edittext);

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


        String DeptName = DeptName_Edittext.getText().toString().trim();
        String LastModified = LastModified_Edittext.getText().toString().trim();
        String UserId = Userid_Edittext.getText().toString().trim();
        String DeptCode = Deptcode_Edittext.getText().toString().trim();

        // Check if all required fields are filled
        if (DeptName.isEmpty() || LastModified.isEmpty() || UserId.isEmpty() || DeptCode.isEmpty()
                ) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        dbManager.insertDept(DeptName, LastModified, UserId, DeptCode);
        dbManager.close();

        // Clear the input
        DeptName_Edittext.setText("");
        LastModified_Edittext.setText("");
        Userid_Edittext.setText("");
        Deptcode_Edittext.setText("");


        // Redirect to the Product activity
        Intent intent = new Intent(AddDepartmentActivity.this, Product.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}