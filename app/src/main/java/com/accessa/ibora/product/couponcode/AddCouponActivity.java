package com.accessa.ibora.product.couponcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCouponActivity extends Activity {

    private AlertDialog alertDialog;
      private EditText DeptName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText Deptcode_Edittext;

    private String cashorId;
    private String cashorName;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle(getString(R.string.addDept));


        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);


        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level




        setContentView(R.layout.add_department_activity);

        DeptName_Edittext = findViewById(R.id.DeptName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        Deptcode_Edittext = findViewById(R.id.deptcode_edittext);

        // Add Record
        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecord();
                    }
                });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }private void addRecord() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String DeptName = DeptName_Edittext.getText().toString().trim();
        String LastModified = dateFormat.format(new Date(currentTimeMillis));
        String DateCreated = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String DeptCode = Deptcode_Edittext.getText().toString().trim();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Check if all required fields are filled
        if (DeptName.isEmpty() ||  DeptCode.isEmpty()
                ) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        // Check if the department code already exists
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.isDepartmentCodeExists(DeptCode)) {
            // Show pop-up dialog with GIF animation and message

            View view = LayoutInflater.from(this).inflate(R.layout.dialog_gif, null);

            // Get a reference to the AppCompatImageView
            AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);
            builder.setMessage(getString(R.string.deptcodeexists));
            // Load the GIF using Glide
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.close)
                    .into(gifImageView);


            // Set the custom view to the AlertDialog
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
            // Check if the activity is still running before showing the dialog
            if (!isFinishing()) {
                alertDialog.show();
            }

            // Find the "Retry" button
            Button retryButton = view.findViewById(R.id.button_retry);

            // Set a click listener for the "Retry" button
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retry button clicked
                    // Perform any necessary actions here
                    // For example, you can close the dialog or retry the login process
                    alertDialog.dismiss(); // Dismiss the dialog
                    // Add your desired actions here
                    Deptcode_Edittext.setText("");

                }
            });

            builder.setCancelable(false);




            return;
        }

// Check if the department name already exists in the database
        Cursor departmentCursor = databaseHelper.getDepartmentByName(DeptName);
        if (departmentCursor.moveToFirst()) {
            // Inflate the custom view for the AlertDialog
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_gif, null);

            // Get a reference to the AppCompatImageView
            AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);
            builder.setMessage(getString(R.string.deptnameexist));
            // Load the GIF using Glide
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.close)
                    .into(gifImageView);


            // Set the custom view to the AlertDialog
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
            // Check if the activity is still running before showing the dialog
            if (!isFinishing()) {
                alertDialog.show();
            }

            // Find the "Retry" button
            Button retryButton = view.findViewById(R.id.button_retry);

            // Set a click listener for the "Retry" button
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retry button clicked
                    // Perform any necessary actions here
                    // For example, you can close the dialog or retry the login process
                    alertDialog.dismiss(); // Dismiss the dialog
                    DeptName_Edittext.setText("");
                    // Add your desired actions here

                }
            });

            builder.setCancelable(false);




            return;
        }


        dbManager.insertDept(DeptName,DateCreated, LastModified, UserId, DeptCode);
        dbManager.close();

        // Clear the input
        DeptName_Edittext.setText("");
        LastModified_Edittext.setText("");
        Userid_Edittext.setText("");
        Deptcode_Edittext.setText("");

        returnHome();


    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Dept_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}