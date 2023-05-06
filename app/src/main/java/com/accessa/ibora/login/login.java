package com.accessa.ibora.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;

public class login extends AppCompatActivity {
    EditText Id;
    Button loginButton;
    MyDBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Id = (EditText) findViewById(R.id.Id);

        loginButton = (Button) findViewById(R.id.loginButton);
        DB = new MyDBHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Identifyer = Id.getText().toString();


                if(Identifyer.equals(""))
                    Toast.makeText(login.this, "Please enter a valid ID", Toast.LENGTH_SHORT).show();
                else{


                    Boolean checkUserId = DB.checkId(Identifyer);
                    if(checkUserId==true){
                        Toast.makeText(login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}