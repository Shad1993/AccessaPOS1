package com.accessa.ibora.product.Department;

import android.app.Activity;
import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Department");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_department_activity);

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


        Item item = dbManager.getDepartmentById(id);
        if (item != null) {
            DeptName_Edittext.setText(item.getName());
            LastModified_Edittext.setText(item.getDescription());
            Userid_Edittext.setText(String.valueOf(item.getPrice()));
            Deptcode_Edittext.setText(item.getBarcode());

        }


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
    }


    private void updateDept() {
        String name = DeptName_Edittext.getText().toString().trim();
        String lastmodified = LastModified_Edittext.getText().toString().trim();
        String UserId = Userid_Edittext.getText().toString().trim();
        String DeptCode = Deptcode_Edittext.getText().toString().trim();

        if (name.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty() || DeptCode.isEmpty() ) {
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





    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
