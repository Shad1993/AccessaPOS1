package com.accessa.ibora.product.Department;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.accessa.ibora.R;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyDepartmentActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;


    private DBManager dbManager;
    private EditText DeptName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText Deptcode_Edittext;
    private long _id;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
    private SharedPreferences sharedPreferences;

    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Department");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_department_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
         cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        Toast.makeText(this, "User ID: " + cashorId, Toast.LENGTH_SHORT).show();
        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        DeptName_Edittext = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        Deptcode_Edittext = findViewById(R.id.deptcode_edittext);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        Department department = dbManager.getDepartmentById(id);
        if (department != null) {
            DeptName_Edittext.setText(department.getName());
            LastModified_Edittext.setText(department.getLastModified());
            Deptcode_Edittext.setText(department.getDepartmentCode());

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
        String DeptCode = Deptcode_Edittext.getText().toString().trim();

        if (DeptName.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty() || DeptCode.isEmpty() ) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        // Retrieve the existing department details
        Department existingDepartment = dbManager.getDepartmentById(String.valueOf(_id));

        // Check if the department code already exists and is not the same as the existing code
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.isDepartmentCodeExists(DeptCode) && !DeptCode.equals(existingDepartment.getDepartmentCode())) {
            Toast.makeText(this, getString(R.string.deptcodeexists), Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbManager.updateDept( _id,name, lastmodified, UserId, DeptCode);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, getString(R.string.updateDept), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedupdateDept), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteDept(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, getString(R.string.deleteDept), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedDeleteDept), Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Dept_fragment");
        startActivity(home_intent1);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
