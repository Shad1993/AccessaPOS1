package com.accessa.ibora.Settings.PaymentFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.items.AddItemActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPaymentActivity extends Activity {

    private AlertDialog alertDialog;
      private EditText methodName_Edittext;
    private ImageView iconImageView;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
   private EditText qrEditText ;
   private   EditText phoneEditText ;
    private SwitchCompat Draweropen,MakeVisible;
    private boolean isDrawerOpen,IsVisible,ISQR,ISPhone;
    private String cashorId;
    private String cashorName;
    private SharedPreferences sharedPreferences;
    private static final int GALLERY_REQUEST_CODE = 1;
    private String iconPath; // Declare iconPath here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle(getString(R.string.addpayment));


        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);


        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level




        setContentView(R.layout.add_payment_activity);

        methodName_Edittext = findViewById(R.id.qrName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        Button selectIconButton = findViewById(R.id.select_icon_button);
         iconImageView = findViewById(R.id.icon_imageview);
        Draweropen = findViewById(R.id.draweropener);
        MakeVisible=findViewById(R.id.makevisible);
// Declare the SwitchCompat and EditText components
        SwitchCompat displayQrSwitch = findViewById(R.id.display_qr_switch);
        SwitchCompat displayPhoneSwitch = findViewById(R.id.display_phone_switch);
     qrEditText = findViewById(R.id.qr_edittext);
         phoneEditText = findViewById(R.id.phone_number_edittext);
// Set listeners for SwitchCompat to toggle visibility of EditText fields
        displayPhoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ISPhone = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddPaymentActivity.this, R.string.makevisibletext, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddPaymentActivity.this, R.string.makeinvisible, Toast.LENGTH_SHORT).show();
                }
            }
        });
        displayQrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ISQR = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddPaymentActivity.this, R.string.makevisibletext, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddPaymentActivity.this, R.string.makeinvisible, Toast.LENGTH_SHORT).show();
                }
            }
        });
        displayQrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ISQR = isChecked;
                // If the switch is checked, show the QR EditText, otherwise hide it
                if (isChecked) {
                    qrEditText.setVisibility(View.VISIBLE);
                } else {
                    qrEditText.setVisibility(View.GONE);
                }
            }
        });

        displayPhoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ISPhone = isChecked;
                // If the switch is checked, show the Phone number EditText, otherwise hide it
                if (isChecked) {
                    phoneEditText.setVisibility(View.VISIBLE);
                } else {
                    phoneEditText.setVisibility(View.GONE);
                }
            }
        });
        MakeVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IsVisible = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddPaymentActivity.this, R.string.makevisibletext, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddPaymentActivity.this, R.string.makeinvisible, Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectIconButton.setOnClickListener(v -> {
            // Show options to pick image from gallery or input URL
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Icon")
                    .setItems(new String[]{"Choose from Gallery", "Enter Image URL"}, (dialog, which) -> {
                        if (which == 0) {
                            // Open gallery to pick an image
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_REQUEST_CODE);
                        } else if (which == 1) {
                            // Show input dialog to enter image URL
                            showUrlInputDialog();
                        }
                    })
                    .show();
        });

        Draweropen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDrawerOpen = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddPaymentActivity.this, R.string.draweropenrmsg, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddPaymentActivity.this, R.string.drawernotopen, Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    }
    private void addRecord() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String MethodName = methodName_Edittext.getText().toString().trim();
        String LastModified = dateFormat.format(new Date(currentTimeMillis));
        String DateCreated = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String Icon = iconPath; // Use the path obtained from onActivityResult
        String visible =String.valueOf(IsVisible);
        String phonevisible =String.valueOf(ISPhone);
        String qrvisible =String.valueOf(ISQR);
        String phonenum = phoneEditText.getText().toString().trim();
        String qrnum = qrEditText.getText().toString().trim();
        String DrawerValue = String.valueOf(isDrawerOpen);
        // Get the values from the SwitchCompat controls


        // Get the QR code and phone number only if the switches are enabled
        String qrCode = "";
        String phoneNumber = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Check if all required fields are filled
        if (MethodName == null || MethodName.isEmpty() ) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }


        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        // Check if the department code already exists
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if( Icon != null ) {
            if (databaseHelper.isQrCodeExists(Icon)) {
                // Show pop-up dialog with GIF animation and message

                View view = LayoutInflater.from(this).inflate(R.layout.dialog_gif, null);

                // Get a reference to the AppCompatImageView
                AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);
                builder.setMessage(getString(R.string.qrcodeexists));
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


                    }
                });

                builder.setCancelable(false);


                return;
            }
        }
// Check if the department name already exists in the database
        Cursor departmentCursor = databaseHelper.getQRByName(MethodName);
        if (departmentCursor.moveToFirst()) {
            // Inflate the custom view for the AlertDialog
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_gif, null);

            // Get a reference to the AppCompatImageView
            AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);
            builder.setMessage(getString(R.string.qrcodeexists));
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
                    methodName_Edittext.setText("");
                    // Add your desired actions here

                }
            });

            builder.setCancelable(false);




            return;
        }
        Log.d("visible", "= " + visible);




        dbManager.insertPaymentMethod(MethodName,DateCreated, LastModified, UserId, Icon,visible,DrawerValue,phonevisible,qrvisible,phonenum,qrnum);
        dbManager.close();

        // Clear the input
        methodName_Edittext.setText("");
        LastModified_Edittext.setText("");
        Userid_Edittext.setText("");


        returnHome();


    }
    private void showUrlInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image URL");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String url = input.getText().toString();
            // Load image from URL into ImageView
            Glide.with(this).load(url).into(iconImageView);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Display the selected image in the ImageView
            iconImageView.setImageURI(selectedImageUri);

            // Get the actual file path
            String imagePath = getRealPathFromURI(selectedImageUri);

            // Store this path in a variable or field to use in the addRecord method
            iconPath = imagePath;  // This is the actual path to use in the database
        }
    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }


    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Pay_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}