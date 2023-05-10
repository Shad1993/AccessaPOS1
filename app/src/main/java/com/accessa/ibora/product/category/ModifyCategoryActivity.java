package com.accessa.ibora.product.category;

import static com.accessa.ibora.product.category.CategoryDatabaseHelper.Color;

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

public class ModifyCategoryActivity extends Activity  {

    private EditText CatNameText;
    private Button buttonupdate, buttondelete;
    private EditText ColorText;

    private long _id;

    private Cursor CursorUpdate;
    private CategoryDBManager CatdbManager;


    private CategoryDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Category");
// Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_modify_record);

        CatdbManager = new CategoryDBManager(this);
        CatdbManager.open();

        CatNameText = (EditText) findViewById(R.id.name_edittext);
        ColorText = (EditText) findViewById(R.id.description_edittext);



//gets you the contents of edit text
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String CatName = intent.getStringExtra("title");


        mDatabaseHelper = new CategoryDatabaseHelper(this);



//displays it in a textview..
        _id = Long.parseLong(id);
        CatNameText.setText(CatName);
        ColorText.setText(Color);

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
        String CatName = CatNameText.getText().toString();
        String Color = ColorText.getText().toString();




        Toast.makeText(getApplicationContext(), "Item MODIFIED", Toast.LENGTH_LONG).show();
        CatdbManager.update(_id, CatName, Color);
        this.returnHome();
    }


    public void openNewActivityDel(){CatdbManager.delete(_id);
        this.returnHome();
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }


    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent1);


    }


}
