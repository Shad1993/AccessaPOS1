package com.accessa.ibora.product.category;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.menu.Product;

public class ModifyCategoryActivity extends Activity  {

    private EditText NameText;
    private Button buttonupdate, buttondelete;
    private EditText descText;
    private EditText LongdescText;

    private long _id;

    private CatDBManager dbManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.activity_modify_record);

        dbManager = new CatDBManager(this);
        dbManager.open();

        NameText = (EditText) findViewById(R.id.name_edittext);
        descText = (EditText) findViewById(R.id.description_edittext);



        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        _id = Long.parseLong(id);

        NameText.setText(name);
        descText.setText(desc);

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
        Toast.makeText(getApplicationContext(), "Item MODIFIED", Toast.LENGTH_LONG).show();
        dbManager.update(_id, title, desc);
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
