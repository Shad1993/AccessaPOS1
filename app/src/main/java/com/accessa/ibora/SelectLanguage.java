package com.accessa.ibora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.login.login;
import com.accessa.ibora.product.menu.Product;

public class SelectLanguage extends AppCompatActivity {
    Button buttonEng, buttonFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectlanguage);

        // Eng language
        buttonEng = (Button) findViewById(R.id.buttonEng);
        buttonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        // Fr language
        buttonFr = (Button) findViewById(R.id.buttonFr);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivityFr();
            }
        });
    }



    public void openNewActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openNewActivityFr(){
        Intent intent = new Intent(this, Product.class);
        startActivity(intent);
    }
}