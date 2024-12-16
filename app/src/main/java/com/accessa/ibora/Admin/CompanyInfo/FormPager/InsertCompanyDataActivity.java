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
    private String shopName,SHOPNUMBER;
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

    private String OpenningHours;
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
        SHOPNUMBER="";
        // Set up initial button visibility
        updateButtonVisibility(viewPager.getCurrentItem());

        // Set a listener for the ViewPager to update button visibility on slide change
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // No action needed here
            }

            @Override
            public void onPageSelected(int position) {
                updateButtonVisibility(position); // Update button visibility based on the current slide
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // No action needed here
            }
        });

        // Button click listener for navigating to the previous slide
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
                    SHOPNUMBER = pagerAdapter.getsixteenSlideData();

                    // Define regex patterns
                    String namePattern = "^[A-Za-z ]+$";  // Only letters and spaces
                    String numberPattern = "^[0-9]+$";    // Only digits

                    // Validate all inputs
                    boolean isValid = true;

                    // Validate Company Name
                    if (companyName.isEmpty() || !companyName.matches(namePattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid company name. Only letters and spaces allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Shop Name
                    if (shopName.isEmpty() || !shopName.matches(namePattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid shop name. Only letters and spaces allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Shop Number
                    if (SHOPNUMBER.isEmpty() || !SHOPNUMBER.matches(numberPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid shop number. Only digits allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // If all inputs are valid, move to the next slide
                    if (isValid) {
                        viewPager.setCurrentItem(1); // Move to the second slide
                    }

                    // Retrieve the logo path after the user has selected an image
                    LogoPath = pagerAdapter.getLogoPath();
                }
                else if (currentSlide == 1) {
                    // Save data from the second slide
                    vatNo = pagerAdapter.getThirdSlideData();
                    brnNo = pagerAdapter.getFourthSlideData();

                    // Define regex pattern for alphanumeric validation
                    String alphanumericPattern = "^[A-Za-z0-9]+$";  // Only letters and numbers

                    // Validate all inputs
                    boolean isValid = true;

                    // Validate VAT Number
                    if (vatNo.isEmpty() || !vatNo.matches(alphanumericPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid VAT number. Only alphanumeric characters allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate BRN Number
                    if (brnNo.isEmpty() || !brnNo.matches(alphanumericPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid BRN number. Only alphanumeric characters allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // If all inputs are valid, move to the next slide
                    if (isValid) {
                        viewPager.setCurrentItem(2); // Move to the third slide
                    }
                }
                else if (currentSlide == 2) {
                    // Save data from the third slide
                    adr1 = pagerAdapter.getFifthSlideData();
                    adr2 = pagerAdapter.getSixthSlideData();
                    adr3 = pagerAdapter.getSeventhSlideData();
                    telNo = pagerAdapter.getEighthSlideData();
                    faxNo = pagerAdapter.getNinthSlideData();

                    // Define regex patterns for validation
                    String addressPattern = "^[A-Za-z0-9\\s]+$";  // Letters, numbers, and spaces only
                    String numberPattern = "^[0-9]+$";            // Only digits

                    boolean isValid = true;

                    // Validate Address 1
                    if (adr1.isEmpty() || !adr1.matches(addressPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid address 1. Only letters, numbers, and spaces are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Address 2
                    if (adr2.isEmpty() || !adr2.matches(addressPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid address 2. Only letters, numbers, and spaces are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Address 3
                    if (adr3.isEmpty() || !adr3.matches(addressPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid address 3. Only letters, numbers, and spaces are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Telephone Number
                    if (telNo.isEmpty() || !telNo.matches(numberPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid telephone number. Only digits are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Fax Number
                    if (faxNo.isEmpty() || !faxNo.matches(numberPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid fax number. Only digits are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // If all inputs are valid, move to the next slide
                    if (isValid) {
                        viewPager.setCurrentItem(3); // Move to the fourth slide
                    }
                }
                else if (currentSlide == 3) {
                    // Save data from the third slide
                    Compadr1 = pagerAdapter.getTenthSlideData();
                    Compadr2 = pagerAdapter.getEleventhSlideData();
                    Compadr3 = pagerAdapter.gettwelveSlideData();
                    ComptelNo = pagerAdapter.getThirteenSlideData();
                    CompfaxNo = pagerAdapter.getForteenSlideData();
                    OpenningHours = pagerAdapter.getFifteenSlideData();

                    // Define regex patterns for validation
                    String addressPattern = "^[A-Za-z0-9\\s]+$";  // Letters, numbers, and spaces only
                    String numberPattern = "^[0-9]+$";            // Only digits
                    boolean isValid = true;

                    // Validate Company Address 1
                    if (Compadr1.isEmpty() || !Compadr1.matches(addressPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid company address 1. Only letters, numbers, and spaces are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Company Address 2
                    if (Compadr2.isEmpty() || !Compadr2.matches(addressPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid company address 2. Only letters, numbers, and spaces are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Company Address 3
                    if (Compadr3.isEmpty() || !Compadr3.matches(addressPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid company address 3. Only letters, numbers, and spaces are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Company Telephone Number
                    if (ComptelNo.isEmpty() || !ComptelNo.matches(numberPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid company telephone number. Only digits are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Company Fax Number
                    if (CompfaxNo.isEmpty() || !CompfaxNo.matches(numberPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid company fax number. Only digits are allowed.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // Validate Opening Hours
                    if (OpenningHours.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Opening hours cannot be empty.", Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }

                    // If all inputs are valid, proceed to insert the data
                    if (isValid) {
                        // Call the method to insert the data
                        insertData();
                    }

                }
            }
        });
    }
    // Method to update button visibility based on the current slide
    private void updateButtonVisibility(int currentSlide) {
        if (currentSlide == 0) {
            btnPrevious.setVisibility(View.GONE); // Hide Previous on the first slide
        } else {
            btnPrevious.setVisibility(View.VISIBLE); // Show Previous on other slides
        }


    }
    private void insertData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase database = null;
        long result = -1;
        int rowsAffected = 0;

        try {
            database = dbHelper.getWritableDatabase();
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("Logo", LogoPath);
            values.put("company_name", companyName);
            values.put("ShopName", shopName);
            values.put("ShopNumber", SHOPNUMBER);
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
            values.put("OpenningHours", OpenningHours);

            result = database.insert("std_access", null, values);

            ContentValues updateValues = new ContentValues();
            updateValues.put("ShopName", shopName);
            rowsAffected = database.update("Users", updateValues, null, null);

            if (result != -1 && rowsAffected > 0) {
                database.setTransactionSuccessful();
                Intent intent = new Intent(this, RegistorCashor.class);
                startActivity(intent);
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to insert or update data", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }

}
