package com.accessa.ibora.Sync;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.google.android.material.snackbar.Snackbar;

public class signup extends AppCompatActivity {

    EditText edtEmailAddress, edtPassword, edtConfirmPassword;
    Button btnSignUp;
    ProgressBar progressBar;
    LinearLayout lvparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.pbbar);
        lvparent = findViewById(R.id.lvparent);
        this.setTitle("User SignUp");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(edtEmailAddress.getText().toString()) ||
                        isEmpty(edtPassword.getText().toString()) ||
                        isEmpty(edtConfirmPassword.getText().toString()))
                    ShowSnackBar("Please enter all fields");
                else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))
                    ShowSnackBar("Password does not match");
                else {
                    AddUsers addUsers = new AddUsers();
                    addUsers.execute("");
                }

            }
        });
    }

    public void ShowSnackBar(String message) {
        Snackbar.make(lvparent, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public Boolean isEmpty(String strValue) {
        if (strValue == null || strValue.trim().equals(("")))
            return true;
        else
            return false;
    }

    private class AddUsers extends AsyncTask<String, Void, String> {
        String emailId, password;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            emailId = edtEmailAddress.getText().toString();
            password = edtPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionHelper con = new ConnectionHelper();
                Connection connect = ConnectionHelper.CONN();

                String queryStmt = "Insert into tblUsers " +
                        " (UserId,Password,UserRole) values "
                        + "('"
                        + emailId
                        + "','"
                        + password
                        + "','User')";

                PreparedStatement preparedStatement = connect
                        .prepareStatement(queryStmt);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                return "Added successfully";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage().toString();
            } catch (Exception e) {
                return "Exception. Please check your code and database.";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
            ShowSnackBar(result);
            progressBar.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.VISIBLE);
            if (result.equals("Added successfully")) {
                // Clear();
            }

        }
    }

}
