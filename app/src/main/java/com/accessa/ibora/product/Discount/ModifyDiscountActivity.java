package com.accessa.ibora.product.Discount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyDiscountActivity extends Activity {

    private Button buttonUpdate;
    private Button buttonDelete;


    private DBManager dbManager;
    private EditText DiscName_Edittext;
    private EditText LastModified_Edittext;
    private EditText Userid_Edittext;
    private EditText DiscAmount_Edittext;
    private long _id;
    private DatabaseHelper mDatabaseHelper;

    private SharedPreferences sharedPreferences;

    private String cashorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Department");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_discount_activity);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        String cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
         cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID


        Toast.makeText(this, "User ID: " + cashorId, Toast.LENGTH_SHORT).show();


        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        DiscName_Edittext = findViewById(R.id.Discname_edittext);
        LastModified_Edittext = findViewById(R.id.LastModified_edittext);
        Userid_Edittext = findViewById(R.id.userid_edittext);
        DiscAmount_Edittext = findViewById(R.id.DiscAmount_edittext);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        _id = Long.parseLong(id);


        Discount discount = dbManager.getDiscountById(id);
        if (discount != null) {
            DiscName_Edittext.setText(discount.getDiscountName());
            LastModified_Edittext.setText(discount.getDiscountTimestamp());
            DiscAmount_Edittext.setText(String.valueOf(discount.getDiscountValue()) + "%");


        }
        Userid_Edittext.setText(String.valueOf(cashorId));

        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDisc();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

    }


    private void updateDisc() {
        String name = DiscName_Edittext.getText().toString().trim();

        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String DiscName = DiscName_Edittext.getText().toString().trim();
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String UserId = cashorId;
        String Discvalue = DiscAmount_Edittext.getText().toString().trim();
        Discvalue = Discvalue.replace("%", ""); // Remove the "%" symbol if present
// Regex for DiscName: only letters and spaces
        String discNamePattern = "^[a-zA-Z ]+$";
        // Validate DiscName using the regex pattern
        if (!DiscName.matches(discNamePattern)) {
            DiscName_Edittext.setError("Discount name can only contain letters and spaces.");
            return;
        }
        double discountValue;
        try {
            discountValue = Double.parseDouble(Discvalue);
            if (discountValue < 1 || discountValue > 100) {
                Toast.makeText(this, "Discount value must be between 1 and 100.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid discount value. Please enter a numeric value.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (DiscName.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty() || Discvalue.isEmpty() ) {
            Toast.makeText(this, R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbManager.updateDisc( _id,name, lastmodified, UserId, String.valueOf(discountValue));
        returnHome();
        if (isUpdated) {
            Toast.makeText(this, R.string.Disc, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.failDisc, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteDisc(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, R.string.DeleteDiscount, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.NotDeleteddiscount, Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Discount_fragment");
        startActivity(home_intent1);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDatabaseHelper.close();
    }
}
