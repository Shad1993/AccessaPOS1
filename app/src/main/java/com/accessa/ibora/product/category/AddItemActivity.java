package com.accessa.ibora.product.category;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.accessa.ibora.R;
import com.accessa.ibora.product.menu.Product;

public class AddItemActivity extends Activity  {

    private Button addTodoBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    Button AddButton;
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


    private EditText CategoryEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Record");

        setContentView(R.layout.activity_add_record);

        subjectEditText = (EditText) findViewById(R.id.itemName_edittext);
        descEditText = (EditText) findViewById(R.id.description_edittext);
        PriceEditText = (EditText) findViewById(R.id.price_edittext);
        CategoryEditText = (EditText) findViewById(R.id.category_edittext);
        BarcodeEditText = (EditText) findViewById(R.id.IdBarcode_edittext) ;
        DeptEditText = (EditText) findViewById(R.id.Dept_edittext) ;
        SubDepartmentEditText =(EditText) findViewById(R.id.SubDept_edittext) ;
        LongDescEditText=(EditText) findViewById(R.id.long_description_edittext);
        WeightEditText= (EditText) findViewById(R.id.weight_edittext);
        QuantityEditText = (EditText) findViewById(R.id.quantity_edittext);
        ExpirydateEditText= (EditText) findViewById(R.id.expirydate_edittext);
        VATEditText= (EditText)findViewById(R.id.Vat_edittext);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();


        // Add Record
        AddButton = (Button) findViewById(R.id.add_record);
        AddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
    }



    public void openNewActivity(){
        final String name = subjectEditText.getText().toString();
        final String desc = descEditText.getText().toString();
        final String price = PriceEditText.getText().toString();
        final String category = CategoryEditText.getText().toString();
        final String Barcode = BarcodeEditText.getText().toString();
       final String Department = DeptEditText.getText().toString();
        final String SubDepartment = SubDepartmentEditText.getText().toString();
        final String LongDescription = LongDescEditText.getText().toString();
        final String Quantity = QuantityEditText.getText().toString();
        final String ExpiryDate = ExpirydateEditText.getText().toString();
        final String VAT = VATEditText.getText().toString();

        dbManager.insert(name, desc, price, category,Barcode,Department,SubDepartment,LongDescription,Quantity,ExpiryDate,VAT);

        Intent main = new Intent(AddItemActivity.this, Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);
    }



}
