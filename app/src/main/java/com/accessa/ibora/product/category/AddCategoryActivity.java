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

public class AddCategoryActivity extends Activity  {

    private Button addTodoBtn;
    private EditText CatNameEditText;
    private EditText ColorEditText;
    Button AddButton;
    private CategoryDBManager CatdbManager;




    private EditText CategoryEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Category");

        setContentView(R.layout.addcategory);

        CatNameEditText = (EditText) findViewById(R.id.CatName_edittext);
        ColorEditText = (EditText) findViewById(R.id.Color_edittext);


        addTodoBtn = (Button) findViewById(R.id.add_record);

        CatdbManager = new CategoryDBManager(this);
        CatdbManager.open();


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
        final String CatName = CatNameEditText.getText().toString();
        final String Color = ColorEditText.getText().toString();


        CatdbManager.insert(CatName, Color);

        Intent main = new Intent(AddCategoryActivity.this, Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);
    }



}
