package com.accessa.ibora;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.Admin.RegistorAdmin;
import com.accessa.ibora.Sync.SyncActivitySync;
import com.accessa.ibora.Sync.SyncAddToMssql;
import com.accessa.ibora.Sync.SyncService;
import com.accessa.ibora.product.items.AddItemActivity;

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout_brn_password, null);
                        builder.setView(dialogView);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        EditText brnEditText = dialogView.findViewById(R.id.brnEditText);
                        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);
                        EditText ServerNameEditText = dialogView.findViewById(R.id.serverEditText);
                        EditText DBNAMEEditText = dialogView.findViewById(R.id.dbnameEditText);
                        EditText usernameditText = dialogView.findViewById(R.id.userEditText);
                        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

                        buttonSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Retrieve BRN and password from EditText fields
                                String brn = brnEditText.getText().toString();
                                String password = passwordEditText.getText().toString();
                                String serverName = ServerNameEditText.getText().toString();
                                String DBName = DBNAMEEditText.getText().toString();
                                String userName = usernameditText.getText().toString();

                                // Regex patterns
                                String brnPattern = "^[a-zA-Z0-9]+$";  // Alphanumeric only

                                // Validate input fields
                                if (!brn.matches(brnPattern)) {
                                    brnEditText.setError("BRN must be alphanumeric.");
                                    return;
                                }



                                // If all validations pass, start sync
                                SyncActivitySync.startSync(welcome.this, brn, password, serverName, DBName,userName);
                                dialog.dismiss(); // Close the dialog
                            }
                        });
                    }
                });


            }
        });

    }
}
