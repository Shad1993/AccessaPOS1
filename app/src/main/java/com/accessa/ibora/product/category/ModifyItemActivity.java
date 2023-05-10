package com.accessa.ibora.product.category;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.menu.Product;

public class ModifyItemActivity extends Activity  {

    private EditText NameText;
    private Button buttonupdate, buttondelete;
    private EditText descText;

    private long _id;

    private Cursor CursorUpdate;
    private DBManager dbManager;
    private EditText PriceEditText;
    private EditText WeightEditText;
    private EditText BarcodeEditText;
    private EditText DepartmentEditText;
    private EditText SubDepartmentEditText;
    private EditText ExpirydateEditText;
    private EditText VATEditText;
    private EditText DeptEditText;
    private EditText LongDescEditText;
    private EditText QuantityEditText;

    private DatabaseHelper mDatabaseHelper;
    private EditText CategoryEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Items");
// Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_modify_record);

        dbManager = new DBManager(this);
        dbManager.open();

        NameText = (EditText) findViewById(R.id.name_edittext);
        descText = (EditText) findViewById(R.id.description_edittext);
        PriceEditText = (EditText) findViewById(R.id.price_edittext);
        CategoryEditText = (EditText) findViewById(R.id.category_edittext);
        BarcodeEditText = (EditText) findViewById(R.id.Barcode_edittext) ;
        DeptEditText = (EditText) findViewById(R.id.department_edittext) ;
        SubDepartmentEditText =(EditText) findViewById(R.id.SubDept_edittext) ;
        LongDescEditText=(EditText) findViewById(R.id.Longdescription_edittext);
        WeightEditText= (EditText) findViewById(R.id.Weight_edittext);
        QuantityEditText = (EditText) findViewById(R.id.quantity_edittext);
        ExpirydateEditText= (EditText) findViewById(R.id.Expiry_edittext);
        VATEditText= (EditText)findViewById(R.id.Vat_edittext);

//gets you the contents of edit text
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String longDesc = intent.getStringExtra("Longdesc");
        String Barcode = intent.getStringExtra("Barcode");
        String ExpiryDate = intent.getStringExtra("ExpiryDate");
        String price = intent.getStringExtra("price");
        String Category = intent.getStringExtra("Category");

        mDatabaseHelper = new DatabaseHelper(this);



//displays it in a textview..
        _id = Long.parseLong(id);
        NameText.setText(name);
        descText.setText(desc);
        PriceEditText.setText(price);
        CategoryEditText.setText(Category);
        LongDescEditText.setText(longDesc);
        BarcodeEditText.setText(Barcode);
        ExpirydateEditText.setText(ExpiryDate);
        // update
        buttonupdate = (Button) findViewById(R.id.btn_update);
        buttonupdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivity();
            }
        });

        // delete
        buttondelete = (Button) findViewById(R.id.btn_delete);
        buttondelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivityDel();
            }
        });
    }
    public void openNewActivity(){
        String title = NameText.getText().toString();
        String desc = descText.getText().toString();

         String price = PriceEditText.getText().toString();
         String category = CategoryEditText.getText().toString();
         String Barcode = BarcodeEditText.getText().toString();
         String Department = DeptEditText.getText().toString();
         String SubDepartment = SubDepartmentEditText.getText().toString();
         String LongDescription = LongDescEditText.getText().toString();
         String Quantity = QuantityEditText.getText().toString();
         String ExpiryDate = ExpirydateEditText.getText().toString();
         String VAT = VATEditText.getText().toString();


        Toast.makeText(getApplicationContext(), "Item MODIFIED", Toast.LENGTH_LONG).show();
        dbManager.update(_id, title, desc,price,Barcode, category,Department,SubDepartment,LongDescription,Quantity,ExpiryDate,VAT);
        this.returnHome();
    }


    public void openNewActivityDel(){
        dbManager.delete(_id);
        this.returnHome();
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }


    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent1);


    }


}
