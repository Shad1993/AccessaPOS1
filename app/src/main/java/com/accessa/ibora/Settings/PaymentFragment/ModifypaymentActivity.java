package com.accessa.ibora.Settings.PaymentFragment;

import static com.accessa.ibora.product.items.DatabaseHelper.DisplayPhoneNumber;
import static com.accessa.ibora.product.items.DatabaseHelper.Displayqr;
import static com.accessa.ibora.product.items.DatabaseHelper.OpenDrawer;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_PhoneNumber;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_QR;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_COLUMN_VIsibility;
import static com.accessa.ibora.product.items.DatabaseHelper.PAYMENT_METHOD_TABLE_NAME;
import static com.accessa.ibora.sales.Sales.ItemGridAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.accessa.ibora.QR.QR;
import com.accessa.ibora.R;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifypaymentActivity extends Activity {
    private Button buttonUpdate;
    private Button buttonDelete;
    private SwitchCompat Draweropen,MakeVisible,displayQrSwitch,displayPhoneSwitch;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private String iconPath; // Declare iconPath here
    private boolean isDrawerOpen,IsVisible,ISQR,ISPhone;;
    private static final int GALLERY_REQUEST_CODE = 1;
        String imagelink;
    private DBManager dbManager;
    private EditText QRName_Edittext;
    private EditText qrEditText ;
    private   EditText phoneEditText ;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private ImageView iconImageView;
    private String imagePath;
    private long _id;
    private String  id;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
    private SharedPreferences sharedPreferences;

    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Payment Method");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_payment_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        Toast.makeText(this, "User ID: " + cashorId, Toast.LENGTH_SHORT).show();
        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();
        Button selectIconButton = findViewById(R.id.select_icon_button);
        QRName_Edittext = findViewById(R.id.qrName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        iconImageView = findViewById(R.id.icon_imageview);

// Declare the SwitchCompat and EditText components
         displayQrSwitch = findViewById(R.id.display_qr_switch);
         displayPhoneSwitch = findViewById(R.id.display_phone_switch);
        qrEditText = findViewById(R.id.qr_edittext);
        phoneEditText = findViewById(R.id.phone_number_edittext);
        Draweropen = findViewById(R.id.draweropener);
        MakeVisible=findViewById(R.id.makevisible);
        displayPhoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ISPhone = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifypaymentActivity.this, R.string.makevisibletext, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifypaymentActivity.this, R.string.makeinvisible, Toast.LENGTH_SHORT).show();
                }
            }
        });
        displayQrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ISQR = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifypaymentActivity.this, R.string.makevisibletext, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifypaymentActivity.this, R.string.makeinvisible, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ModifypaymentActivity.this, R.string.makevisibletext, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifypaymentActivity.this, R.string.makeinvisible, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Draweropen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDrawerOpen = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifypaymentActivity.this, R.string.draweropenrmsg, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifypaymentActivity.this, R.string.drawernotopen, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Image selection button
        selectIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOptionsDialog();
            }
        });


        Intent intent = getIntent();
         id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        _id = Long.parseLong(id);
        payment payments = dbManager.getpaymentById(id);
        if (payments != null) {
            // set switch as true if available for sale
            Boolean isConditionMet = payments.getOpenDrawer();
            int isactuallyvisible = payments.getVisibility();
            int qrvisible = payments.getDisplayqr();
            int phonevisible = payments.getDispplayphone();
            String visi= String.valueOf(isactuallyvisible);
            String qrvisi= String.valueOf(qrvisible);
            String phonevisi= String.valueOf(phonevisible);

            if (Boolean.TRUE.equals(isConditionMet)) {
                Draweropen.setChecked(true);

            }
            if (payments.getVisibility()==1 || visi.equals("true")) {
                MakeVisible.setChecked(true);

            }

            if (payments.getDisplayqr()==1 || qrvisi.equals("true")) {
                displayQrSwitch.setChecked(true);

                    qrEditText.setVisibility(View.VISIBLE);

            }else {
                qrEditText.setVisibility(View.GONE);
            }
            if (payments.getDispplayphone()==1 || phonevisi.equals("true")) {
                displayPhoneSwitch.setChecked(true);
                phoneEditText.setVisibility(View.VISIBLE);
            }else {
                phoneEditText.setVisibility(View.GONE);
            }
            qrEditText.setText(payments.getPaymentMethodqr());
            phoneEditText.setText(payments.getPaymentMethodPhone());
            QRName_Edittext.setText(payments.getPaymentName());
            imagelink=payments.getPaymentMethodIcon();
            // Use Glide to load the image from the file path
            Glide.with(this)
                    .load(imagelink)  // Path to the image file (could be a URI or file path)
                    .into(iconImageView);  // Load into your ImageView

        }

        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePayment();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePayment();
            }
        });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }
    private void openImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Web Link", "Device Memory"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Web Link
                        openWebImageDialog();
                        break;
                    case 1: // Device Memory
                        openDeviceImagePicker();
                        break;
                }
            }
        });
        builder.show();
    }
    private void openWebImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image URL");

        final EditText urlEditText = new EditText(this);
        urlEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(48, 0, 48, 0);
        urlEditText.setLayoutParams(layoutParams);

        builder.setView(urlEditText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imageUrl = urlEditText.getText().toString().trim();
                loadImageFromUrl(imageUrl);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void loadImageFromUrl(String imageUrl) {
        // Implement code to load image from URL
        imagePath = imageUrl;
        // You can also display the selected image here if needed
        ImageView imageView = findViewById(R.id.icon_imageview);
        Glide.with(this).load(imageUrl).into(imageView);
    }
    private void openDeviceImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }
    private void updatePayment() {


        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String paymentName = QRName_Edittext.getText().toString().trim();
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String icon = iconPath; // Use the path obtained from onActivityResult
        String drawer =String.valueOf(isDrawerOpen);
        String visible =String.valueOf(IsVisible);
        String phonevisible =String.valueOf(ISPhone);
        String qrvisible =String.valueOf(ISQR);
        String phonenum = phoneEditText.getText().toString().trim();
        String qrnum = qrEditText.getText().toString().trim();

        if (paymentName.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty()  ) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        // Retrieve the existing qr details
        String image ;
        if( imagePath== null) {
            image=  imagelink;

        } else{
            image=  imagePath;

        }


        boolean isUpdated = dbManager.updatePayment( _id,paymentName, lastmodified, UserId, image,drawer,visible,phonevisible,qrvisible,phonenum,qrnum);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, getString(R.string.updatQR), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedupdate), Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePayment() {
        boolean isDeleted = dbManager.deletepaymentMethod(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, getString(R.string.deleteqr), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedDelete), Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Pay_fragment");
        startActivity(home_intent1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                Uri selectedImageUri = data.getData();
                imagePath = getPathFromUri(selectedImageUri);
                displaySelectedImage(selectedImageUri);
            }
        }
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }
    private void displaySelectedImage(Uri imageUri) {
        ImageView imageView = findViewById(R.id.icon_imageview);


        if (isWebLink(String.valueOf(imageUri))) {
            // Load image from web link
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.emptybasket) // Placeholder image while loading
                    .error(R.drawable.iboralogos1)
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Load image from local storage
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadLocalImage(imageView, String.valueOf(imageUri));
            }
        }
    }

    private boolean isWebLink(String url) {
        return URLUtil.isValidUrl(url);
    }
    private void loadLocalImage(ImageView imageView, String imageLocation) {
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.emptybasket);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
