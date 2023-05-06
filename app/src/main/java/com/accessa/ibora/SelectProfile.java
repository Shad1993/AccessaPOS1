package com.accessa.ibora;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.login.RegistorCashor;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.menu.Product;

public class SelectProfile extends AppCompatActivity {
    Button buttonEng, buttonFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.selectprofile);

        // Eng language
        buttonEng = (Button) findViewById(R.id.buttonCashor);
        buttonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        // Fr language
        buttonFr = (Button) findViewById(R.id.buttonAdmin);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivityAdmin();
            }
        });
    }



    public void openNewActivity(){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void openNewActivityAdmin(){
        Intent intent = new Intent(this, RegistorCashor.class);
        startActivity(intent);
    }
}