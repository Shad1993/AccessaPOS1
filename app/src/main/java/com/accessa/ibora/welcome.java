package com.accessa.ibora;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Admin.RegistorAdmin;
import com.accessa.ibora.company.InsertCompanyDataActivity;

public class welcome extends AppCompatActivity {

    private Button welcomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.welcome);




        welcomeButton = findViewById(R.id.buttonGetStarted);

        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setView(R.layout.dialog_layout_offline_online);

                AlertDialog dialog = builder.create();
                dialog.show();

                Button buttonWorkOffline = dialog.findViewById(R.id.buttonWorkOffline);
                Button buttonSynchronize = dialog.findViewById(R.id.buttonSynchronize);

                buttonWorkOffline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(welcome.this, RegistorAdmin.class);
                        startActivity(intent);
                        dialog.dismiss(); // Close the dialog
                    }
                });

                buttonSynchronize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Close the dialog
                    }
                });
            }
        });

    }
}
