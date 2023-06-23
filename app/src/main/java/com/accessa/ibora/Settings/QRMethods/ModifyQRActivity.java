package com.accessa.ibora.Settings.QRMethods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accessa.ibora.QR.QR;
import com.accessa.ibora.R;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyQRActivity extends Activity {
    private Button buttonUpdate;
    private Button buttonDelete;


    private DBManager dbManager;
    private EditText QRName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText QRcode_Edittext;
    private long _id;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
    private SharedPreferences sharedPreferences;

    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify QR");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_qr_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        Toast.makeText(this, "User ID: " + cashorId, Toast.LENGTH_SHORT).show();
        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        QRName_Edittext = findViewById(R.id.qrName_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        QRcode_Edittext = findViewById(R.id.qrcode_edittext);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        QR qr = dbManager.getQRById(id);
        if (qr != null) {
            QRName_Edittext.setText(qr.getName());
            QRcode_Edittext.setText(qr.getQRcode());

        }
        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQR();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQR();
            }
        });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }


    private void updateQR() {
        String name = QRName_Edittext.getText().toString().trim();

        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String QRName = QRName_Edittext.getText().toString().trim();
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String QRCode = QRcode_Edittext.getText().toString().trim();

        if (QRName.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty() || QRCode.isEmpty() ) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        // Retrieve the existing qr details
        QR existingQR = dbManager.getQRById(String.valueOf(_id));

        // Check if the qr code already exists and is not the same as the existing code
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.isQrCodeExists(QRCode) && !QRCode.equals(existingQR.getQRcode())) {
            Toast.makeText(this, getString(R.string.qrcodeexists), Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbManager.updateQR( _id,name, lastmodified, UserId, QRCode);
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, getString(R.string.updatQR), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.failedupdate), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteQR() {
        boolean isDeleted = dbManager.deleteQR(_id);
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
        home_intent1.putExtra("fragment", "Qr_fragment");
        startActivity(home_intent1);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
