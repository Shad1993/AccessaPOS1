package com.accessa.ibora.Admin.CompanyInfo.FormPager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.accessa.ibora.Admin.RegistorCashor;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class InsertCompanyDataActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button btnNext,btnPrevious;

    private CompanyPagerAdapter pagerAdapter;

    // Data fields for each slide
    private String companyName;
    private String shopName;
    private String vatNo;
    private String brnNo;
    private String adr1;
    private String adr2;
    private String adr3;
    private String telNo;
    private String faxNo;
    private String Compadr1;
    private String Compadr2;
    private String Compadr3;
    private String ComptelNo;
    private String CompfaxNo;
    private String LogoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_info);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        pagerAdapter = new CompanyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Set initial data for the first slide
        companyName = "";
        shopName = "";
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSlide = viewPager.getCurrentItem();
                if (currentSlide > 0) {
                    viewPager.setCurrentItem(currentSlide - 1); // Move to the previous slide
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSlide = viewPager.getCurrentItem();
                if (currentSlide == 0) {
                    // Save data from the first slide
                    companyName = pagerAdapter.getFirstSlideData();
                    shopName = pagerAdapter.getSecondSlideData();
                    viewPager.setCurrentItem(1); // Move to the second slide

                    // Retrieve the logo path after the user has selected an image
                    LogoPath = pagerAdapter.getLogoPath();
                } else if (currentSlide == 1) {
                    // Save data from the second slide
                    vatNo = pagerAdapter.getThirdSlideData();
                    brnNo = pagerAdapter.getFourthSlideData();
                    viewPager.setCurrentItem(2); // Move to the third slide
                } else if (currentSlide == 2) {
                    // Save data from the third slide
                    adr1 = pagerAdapter.getFifthSlideData();
                    adr2 = pagerAdapter.getSixthSlideData();
                    adr3 = pagerAdapter.getSeventhSlideData();
                    telNo = pagerAdapter.getEighthSlideData();
                    faxNo = pagerAdapter.getNinthSlideData();

                    viewPager.setCurrentItem(3); // Move to the third slide
                }else if (currentSlide == 3) {
                    // Save data from the third slide
                    Compadr1 = pagerAdapter.getTenthSlideData();
                    Compadr2 = pagerAdapter.getEleventhSlideData();
                    Compadr3 = pagerAdapter.gettwelveSlideData();
                    ComptelNo = pagerAdapter.getThirteenSlideData();
                    CompfaxNo = pagerAdapter.getForteenSlideData();

                    // Call the method to insert the data
                    insertData();

                    // Optionally, navigate to the next activity or perform other actions
                    // startActivity(new Intent(InsertCompanyDataActivity.this, NextActivity.class));
                }
            }
        });
    }

    private void insertData() {
        // Perform the data insertion here
        // Use the data fields (companyName, shopName, vatNo, brnNo, adr1, adr2, adr3, telNo, faxNo) for insertion logic
        // You can use a database or any other storage mechanism to store the data

        // Example: Insert data into a SQLite database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Logo", LogoPath);
        values.put("company_name", companyName);
        values.put("ShopName", shopName);
        values.put("vat_no", vatNo);
        values.put("BRN_No", brnNo);
        values.put("adr_1", adr1);
        values.put("adr_2", adr2);
        values.put("adr_3", adr3);
        values.put("tel_no", telNo);
        values.put("fax_no", faxNo);
        values.put("ComPanyAdress1", Compadr1);
        values.put("ComPanyAdress2", Compadr2);
        values.put("ComPanyAdress3", Compadr3);
        values.put("ComPanyphoneNumber", ComptelNo);
        values.put("ComPanyFaxNumber", CompfaxNo);

        ContentValues value = new ContentValues();
        value.put("ShopName", shopName);


        long result = database.insert("std_access", null, values);
        int rowsAffected = database.update("Users", value, null, null);
        database.close();

        if (result != -1) {
            Intent intent = new Intent(this, RegistorCashor.class);
            startActivity(intent);
        } else {
            Toast.makeText(InsertCompanyDataActivity.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }
    }
}
