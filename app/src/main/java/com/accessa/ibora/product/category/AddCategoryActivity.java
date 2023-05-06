package com.accessa.ibora.product.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.accessa.ibora.R;
import com.accessa.ibora.product.menu.Product;

public class AddCategoryActivity extends Activity  {

    private Button addTodoBtn;
    private EditText NameEditText;
    private EditText descEditText;

    Button AddButton;
    private CatDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.activity_add_record);

        NameEditText = (EditText) findViewById(R.id.itemName_edittext);
        descEditText = (EditText) findViewById(R.id.description_edittext);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new CatDBManager(this);
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
        final String name = NameEditText.getText().toString();
        final String desc = descEditText.getText().toString();

        dbManager.insert(name, desc);

        Intent main = new Intent(AddCategoryActivity.this, Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);
    }



}
