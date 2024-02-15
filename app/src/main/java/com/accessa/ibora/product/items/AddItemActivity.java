package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.items.DatabaseHelper.OPTIONS_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.OPTION_NAME;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.accessa.ibora.R;
import com.accessa.ibora.Sync.SyncAddToMssql;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends Activity {
    static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private String imagePath;
    private Spinner Discount;
    private static final String _user = "db_a9c818_test_admin";
    private static final String _pass = "Test1234";
    private static final String _DB = "db_a9c818_test";
    private static final String _server = "SQL8005.site4now.net";
    private CategoryDatabaseHelper catDatabaseHelper;
    private DatabaseHelper mDatabaseHelper;
    private EditText subjectEditText;
    private EditText ItemCode;
    private Spinner Nature,Currency;
    private EditText descEditText;
    private EditText priceEditText,price2EditText,price3EditText;
    private Spinner categorySpinner, departmentSpinner, subDepartmentSpinner;
    private EditText barcodeEditText;
    private EditText longDescEditText;
    private EditText weightEditText;
    private EditText costEditText;
    private EditText skuEditText;
    private EditText variantEditText;
    private EditText quantityEditText;
    private DatePicker expiryDatePicker;
    private EditText vatEditText;
    private String formattedDate;
    private RadioGroup soldByRadioGroup,comments_radioGroup;
    private RadioGroup vatRadioGroup;
    private SQLiteDatabase database;
    private SwitchCompat Available4Sale,hascomment,Options;
    private SwitchCompat ExpirydateSwitch;
    private boolean isAvailableForSale, isCommentRequired,isOptionrequired;
    private TextView ExpiryDateText;
    private String cashorId;
    private String cashorName;
    private SharedPreferences sharedPreferences;
    private EditText Userid_Edittext;
    private EditText LastModified_Edittext;
    private SwitchCompat optionsSwitch;
    private LinearLayout optionsContainer;
    private List<Integer> selectedOptionIds = new ArrayList<>();
    private static final int MAX_SELECTION_LIMIT = 5;
    private int[] selectedOptions = new int[MAX_SELECTION_LIMIT];

    private long optionId1;
    private long optionId2;
    private long optionId3;
    private long optionId4;
    private long optionId5;

    private int optionCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Retrieve the locale value from intent extras
        String locale = getIntent().getStringExtra("locale");
        setTitle(getString(R.string.send));

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID

        setContentView(R.layout.activity_add_record);






        subjectEditText = findViewById(R.id.itemName_edittext);
        descEditText = findViewById(R.id.description_edittext);
        priceEditText = findViewById(R.id.price_edittext);
        price2EditText = findViewById(R.id.price2_edittext);
        price3EditText = findViewById(R.id.price3_edittext);
        categorySpinner = findViewById(R.id.category_spinner);
        barcodeEditText = findViewById(R.id.IdBarcode_edittext);
        departmentSpinner = findViewById(R.id.Dept_spinner);
        subDepartmentSpinner = findViewById(R.id.SubDept_spinner);
        longDescEditText = findViewById(R.id.long_description_edittext);
        weightEditText = findViewById(R.id.weight_edittext);
        variantEditText = findViewById(R.id.variant_edittext);
        skuEditText = findViewById(R.id.sku_edittext);
        costEditText = findViewById(R.id.cost_edittext);
        quantityEditText = findViewById(R.id.quantity_edittext);
        expiryDatePicker = findViewById(R.id.expirydate_picker);
        vatRadioGroup = findViewById(R.id.VAT_radioGroup);
        soldByRadioGroup = findViewById(R.id.soldBy_radioGroup);
        hascomment=findViewById(R.id.comment_switch);
        Available4Sale = findViewById(R.id.Avail4Sale_switch);
        optionsSwitch = findViewById(R.id.options_switch);
        optionsContainer = findViewById(R.id.options_container);
        ExpirydateSwitch = findViewById(R.id.perishable_switch);
        ExpiryDateText= findViewById(R.id.ExpiryText);
        Discount = findViewById(R.id.discount_spinner);

        ItemCode = findViewById(R.id.IdItemCode_edittext);
        // Inside your onCreate method, after initializing other views
        Currency = findViewById(R.id.Currency_spinner);

        // Create a list of currency options
        List<String> currencyOptions = new ArrayList<>();
        currencyOptions.add("MUR");
        currencyOptions.add("USD");
        currencyOptions.add("EUR");
        currencyOptions.add("GBP");


        // Create an ArrayAdapter for the Currency spinner
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyOptions);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Currency.setAdapter(currencyAdapter);


        // Inside your onCreate method, after initializing other views
        Nature = findViewById(R.id.Nature_spinner);

// Create a list of options for the Nature spinner
        List<String> natureOptions = new ArrayList<>();
        natureOptions.add("GOODS");
        natureOptions.add("SERVICES");

// Create an ArrayAdapter for the spinner
        ArrayAdapter<String> natureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, natureOptions);
        natureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Nature.setAdapter(natureAdapter);
        Discount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDiscount = parent.getItemAtPosition(position).toString();
                // Do something with the selectedDiscount (e.g., store it in a variable)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });


        Userid_Edittext = findViewById(R.id.userid_edittext);
        // Image selection button
        Button imageButton = findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOptionsDialog();
            }
        });

// Add this after setting up the adapter for the Nature spinner
        Nature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedNature = parent.getItemAtPosition(position).toString();
                // Do something with the selectedNature (e.g., store it in a variable)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

// Add this after setting up the adapter for the Currency spinner
        Currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCurrency = parent.getItemAtPosition(position).toString();
                // Do something with the selectedCurrency (e.g., store it in a variable)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        hascomment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCommentRequired = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddItemActivity.this, R.string.hascomment, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddItemActivity.this, R.string.hanocomment, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Available4Sale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAvailableForSale = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(AddItemActivity.this, R.string.itemavailable, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(AddItemActivity.this, R.string.itemnotavailable, Toast.LENGTH_SHORT).show();
                }
            }
        });



        ExpirydateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ExpiryDateText.setVisibility(View.VISIBLE);
                    expiryDatePicker.setVisibility(View.VISIBLE);
                } else {
                    // Toggle button is unchecked
                    ExpiryDateText.setVisibility(View.GONE);
                    expiryDatePicker.setVisibility(View.GONE);
                }
            }
        });

        soldByRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    String selectedOption = selectedRadioButton.getText().toString();

                    if (selectedOption.equals(getString(R.string.soldBy_each))) {
                        weightEditText.setVisibility(View.GONE);
                        quantityEditText.setVisibility(View.VISIBLE);
                    } else if (selectedOption.equals(getString(R.string.soldBy_volume))) {
                        weightEditText.setVisibility(View.VISIBLE);
                        quantityEditText.setVisibility(View.GONE);
                    }
                }
            }
        });

        int year = expiryDatePicker.getYear();
        int month = expiryDatePicker.getMonth() + 1;
        int dayOfMonth = expiryDatePicker.getDayOfMonth();

        expiryDatePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the formattedDate variable with the new selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(calendar.getTime());
            }
        });
        catDatabaseHelper = new CategoryDatabaseHelper(this);
        mDatabaseHelper = new DatabaseHelper(this);
        populateDiscountSpinner();


        Cursor categoryCursor = catDatabaseHelper.getAllCategory();
        Cursor departmentCursor = mDatabaseHelper.getAllDepartment();
        Cursor subDepartmentCursor = mDatabaseHelper.getAllSubDepartmentby();


        List<String> categories = new ArrayList<>();
        categories.add(getString(R.string.AllCategory));
        if (categoryCursor.moveToFirst()) {
            do {
                String category = categoryCursor.getString(categoryCursor.getColumnIndex(CategoryDatabaseHelper.CatName));
                categories.add(category);
            } while (categoryCursor.moveToNext());
        }
        categoryCursor.close();

        List<String> departments = new ArrayList<>();
        departments.add(getString(R.string.AllDepartments));
        if (departmentCursor.moveToFirst()) {
            do {
                String department = departmentCursor.getString(departmentCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
                departments.add(department);
            } while (departmentCursor.moveToNext());
        }
        departmentCursor.close();

        List<String> subDepartments = new ArrayList<>();
        subDepartments.add(getString(R.string.AllSubDepartments));
        if (subDepartmentCursor.moveToFirst()) {
            do {
                String subDepartment = subDepartmentCursor.getString(subDepartmentCursor.getColumnIndex(DatabaseHelper.SUBDEPARTMENT_NAME));
                subDepartments.add(subDepartment);
            } while (subDepartmentCursor.moveToNext());
        }
        subDepartmentCursor.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> spinnerAdapterDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        ArrayAdapter<String> spinnerAdapterSubDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subDepartments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        departmentSpinner.setAdapter(spinnerAdapterDept);
        subDepartmentSpinner.setAdapter(spinnerAdapterSubDept);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        subDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        Button addButton = findViewById(R.id.add_record);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

        //set userid and last Modified
        Userid_Edittext.setText(String.valueOf(cashorId));

        optionsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOptionrequired = isChecked;
                if (isChecked) {
                    // Display options
                    displayOptions();

                } else {
                    // Clear options
                    optionsContainer.removeAllViews();

                }
            }
        });
    }
    private void displayOptions() {
        // Retrieve options from the database
        List<Options> optionsList = mDatabaseHelper.getAllOptions1();

        // Clear previous options
        optionsContainer.removeAllViews();

        // Dynamically create checkboxes for each option
        for (int i = 0; i < optionsList.size(); i++) {
            Options options = optionsList.get(i);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(options.getOptionname());
            checkBox.setChecked(false); // Set the initial state as unchecked

            final long optionId = options.getOptionId(); // Get the option ID from the Options object

            // Set a listener for checkbox changes
            final int finalIndex = i; // Store the index in a final variable for use inside the listener
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Check if the maximum limit is reached
                        if (selectedOptionIds.size() < MAX_SELECTION_LIMIT) {
                            // Checkbox is checked, add the option ID to the selected list
                            selectedOptionIds.add((int) optionId);
                            // Store the option ID in the corresponding variable
                            switch (finalIndex) {
                                case 0:
                                    optionId1 = optionId;
                                    break;
                                case 1:
                                    optionId2 = optionId;
                                    break;
                                case 2:
                                    optionId3 = optionId;
                                    break;
                                case 3:
                                    optionId4 = optionId;
                                    break;
                                case 4:
                                    optionId5 = optionId;
                                    break;
                            }
                            // Toast the option ID
                            Toast.makeText(AddItemActivity.this, "Option ID: " + optionId + " checked", Toast.LENGTH_SHORT).show();
                        } else {
                            // Maximum limit reached, uncheck the checkbox and display a message
                            checkBox.setChecked(false);
                            Toast.makeText(AddItemActivity.this, "Maximum limit of 5 options reached", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Checkbox is unchecked, remove the option ID from the selected list
                        selectedOptionIds.remove(Integer.valueOf((int) optionId));
                        // Clear the corresponding variable
                        switch (finalIndex) {
                            case 0:
                                optionId1 = 0;
                                break;
                            case 1:
                                optionId2 = 0;
                                break;
                            case 2:
                                optionId3 = 0;
                                break;
                            case 3:
                                optionId4 = 0;
                                break;
                            case 4:
                                optionId5 = 0;
                                break;
                        }
                    }
                    updateSelectedOptions(); // Display selected option IDs
                }
            });

            // Add the checkbox to the container
            optionsContainer.addView(checkBox);
        }
    }

    private void updateSelectedOptions() {
        // Display the selected option IDs
        StringBuilder selectedOptionsText = new StringBuilder("Selected Option IDs: ");
        if (optionId1 != 0) selectedOptionsText.append(optionId1).append(", ");
        if (optionId2 != 0) selectedOptionsText.append(optionId2).append(", ");
        if (optionId3 != 0) selectedOptionsText.append(optionId3).append(", ");
        if (optionId4 != 0) selectedOptionsText.append(optionId4).append(", ");
        if (optionId5 != 0) selectedOptionsText.append(optionId5).append(", ");

        // Remove the trailing comma and space
        if (selectedOptionsText.length() > 2) {
            selectedOptionsText.setLength(selectedOptionsText.length() - 2);
        }

        // Show the selected option IDs in a Toast
        Toast.makeText(AddItemActivity.this, selectedOptionsText.toString(), Toast.LENGTH_SHORT).show();
    }


    private void populateDiscountSpinner() {
        Cursor discountCursor = mDatabaseHelper.getAllDiscounts();
        // Fetch discount data from your database
        List<String> discountOptions = new ArrayList<>();
        // Fetch the data and populate the discountOptions list

        discountOptions.add(getString(R.string.NODISCOUNT));
        if (discountCursor.moveToFirst()) {
            do {
                String discount_name = discountCursor.getString(discountCursor.getColumnIndex(DatabaseHelper.DISCOUNT_NAME));
                String discount_Value= discountCursor.getString(discountCursor.getColumnIndex(DatabaseHelper.DISCOUNT_VALUE));
                discountOptions.add(discount_Value);
            } while (discountCursor.moveToNext());
        }
        discountCursor.close();

        ArrayAdapter<String> discountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, discountOptions);
        discountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Discount.setAdapter(discountAdapter);
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

    private void openDeviceImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
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
    private void displaySelectedImage(Uri imageUri) {
        ImageView imageView = findViewById(R.id.image_view);


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
    private void loadImageFromUrl(String imageUrl) {
        // Implement code to load image from URL
        imagePath = imageUrl;
        // You can also display the selected image here if needed
        ImageView imageView = findViewById(R.id.image_view);
        Glide.with(this).load(imageUrl).into(imageView);
    }
    private void addRecord() {

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String LastModified = dateFormat.format(new Date(currentTimeMillis));

        soldByRadioGroup = findViewById(R.id.soldBy_radioGroup);
        int selectedId = soldByRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            // No radio button is selected
            Toast.makeText(this, getString(R.string.emptySoldBy), Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedSoldBy = selectedRadioButton.getText().toString();

        vatRadioGroup = findViewById(R.id.VAT_radioGroup);
        int selectedId1 = vatRadioGroup.getCheckedRadioButtonId();
        if (selectedId1 == -1) {
            // No radio button is selected
            Toast.makeText(this, getString(R.string.emptyvat), Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedRadioButton1 = findViewById(selectedId1);
        String selectedVat = selectedRadioButton1.getText().toString();

        String barcode = barcodeEditText.getText().toString().trim();
        String name = subjectEditText.getText().toString().trim();
        String desc = descEditText.getText().toString().trim();
        String itemCode = ItemCode.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();

        if (quantity.isEmpty()) {
            quantity = "0"; // Assign a default value of 0 if the quantity is empty
        }
        String DateCreated = dateFormat.format(new Date(currentTimeMillis));
        String department = departmentSpinner.getSelectedItem().toString().trim();
        String longDescription = longDescEditText.getText().toString().trim();
        String subDepartment = subDepartmentSpinner.getSelectedItem().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String price2 = price2EditText.getText().toString().trim();
        String price3 = price3EditText.getText().toString().trim();

        double prices;
        double prices2;
        double prices3;

        if (!price.isEmpty()) {
            prices = Double.parseDouble(price);
        } else {
            // Handle the case where price is empty, e.g., assign a default value
            prices = 0.0; // You can change this default value as needed
        }
        if (!price2.isEmpty()) {
            prices2 = Double.parseDouble(price2);
        } else {
            // Handle the case where price is empty, e.g., assign a default value
            prices2 = 0.0; // You can change this default value as needed
        }
        if (!price3.isEmpty()) {
            prices3 = Double.parseDouble(price3);
        } else {
            // Handle the case where price is empty, e.g., assign a default value
            prices3 = 0.0; // You can change this default value as needed
        }


        String selectedNature = Nature.getSelectedItem().toString();
        String selectedCurrency = Currency.getSelectedItem().toString();
        String selectedDiscount = Discount.getSelectedItem().toString();
        double currentPrice;
        double currentPrice2;
        double currentPrice3;

        double discountedAmount;
        double discountedAmount2;
        double discountedAmount3;

        int selectedDiscounts = 0;
        if (selectedDiscount.equals("No Discount")) {
            currentPrice = prices;
            currentPrice2 = prices2;
            currentPrice3 = prices3;
            discountedAmount = 0.00;
            discountedAmount2 = 0.00;
            discountedAmount3 = 0.00;

        } else {
            selectedDiscounts = Integer.parseInt(Discount.getSelectedItem().toString());
            // Corrected discountedAmount calculation
            discountedAmount = (selectedDiscounts / 100.0) * prices;
            discountedAmount2 = (selectedDiscounts / 100.0) * prices2;
            discountedAmount3 = (selectedDiscounts / 100.0) * prices3;

            // Calculate the current price
            currentPrice = prices - discountedAmount;
            currentPrice2 = prices2 - discountedAmount2;
            currentPrice3 = prices3 - discountedAmount3;


        }


        String vat = selectedVat;
        String VatCode = "";
        if (vat.equals("VAT 15%")) {
            VatCode = "TC01";
        } else if (vat.equals("VAT 0%")) {
            VatCode = "TC02";
        } else if (vat.equals("VAT Exempted")) {
            VatCode = "TC03";
        }
        String variant = variantEditText.getText().toString().trim();
        String sku = skuEditText.getText().toString().trim();
        String cost = costEditText.getText().toString().trim();
        String weight = weightEditText.getText().toString().trim();
        if (weight.isEmpty()) {
            weight = "0"; // Assign a default value of 0 if the weight is empty
        }
        String expiryDate = formattedDate;
        String availableForSale = String.valueOf(isAvailableForSale);
        String commentrequired = String.valueOf(isCommentRequired);
        String optionStatus= String.valueOf(isOptionrequired);
        String soldBy = selectedSoldBy;

        String UserId = cashorId;
        String image;
        if (imagePath == null) {
            image = null;

        } else {
            image = imagePath;

        }

        // Check if all required fields are filled
        if (name.isEmpty() || desc.isEmpty() || price.isEmpty() || price2.isEmpty() || price3.isEmpty() || barcode.isEmpty()
                || department.isEmpty() || subDepartment.isEmpty() || category.isEmpty() || commentrequired.isEmpty()
                || availableForSale.isEmpty() || longDescription.isEmpty()
                || variant.isEmpty() || sku.isEmpty() || cost.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }


        // Check if the department code already exists
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.checkBarcodeExists(barcode)) {
            Toast.makeText(this, getString(R.string.barcodecodeexists), Toast.LENGTH_SHORT).show();
            return;
        }


        showUpdateConfirmationDialog(name, desc, selectedDiscounts, price, price2, price3, category, barcode, Float.parseFloat(weight), department,
                subDepartment, longDescription, quantity, expiryDate, vat,
                availableForSale,optionStatus, String.valueOf(optionId1), String.valueOf(optionId2),String.valueOf(optionId3),String.valueOf(optionId4),String.valueOf(optionId5),  commentrequired,soldBy, image, variant, sku, cost, UserId, DateCreated, LastModified,
                selectedNature, selectedCurrency, itemCode, VatCode, String.valueOf(discountedAmount), String.valueOf(discountedAmount2), String.valueOf(discountedAmount3), currentPrice, currentPrice2, currentPrice3);


        // Clear the input fields
        subjectEditText.setText("");
        descEditText.setText("");
        priceEditText.setText("");
        barcodeEditText.setText("");
        longDescEditText.setText("");
        weightEditText.setText("");
        quantityEditText.setText("");
        variantEditText.setText("");
        skuEditText.setText("");
        costEditText.setText("");

        // Reset spinners to default selection
        categorySpinner.setSelection(0);
        departmentSpinner.setSelection(0);
        subDepartmentSpinner.setSelection(0);


    }



    //  retrieve the value of isAvailableForSale
    private boolean getIsAvailableForSale() {
        return isAvailableForSale;
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


    private void showUpdateConfirmationDialog(String name, String desc, int selectedDiscounts,String price,String price2,String price3, String category, String barcode, float parseFloat, String department, String subDepartment, String longDescription, String quantity, String expiryDate, String vat, String availableForSale, String options,String commentrequired,String optionIds,String optionId2, String optionId3,String optionId4,String optionId5, String soldBy, String image, String variant, String sku, String cost, String userId, String dateCreated, String lastModified, String selectedNature, String selectedCurrency, String itemCode, String vatCode, String valueOf,String discountedamount2,String discountedamount3, double currentPrice,double currentPrice2,double currentPrice3) {
        if (isFinishing()) {
            // Activity is finishing, do not show the dialog
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.update_confirmation_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();


        // Insert the record into the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to upload data to the online database here
                String Name = name;
                String Descript = desc;
                String Price = price;
                String Price2 = price2;
                String Price3 = price3;
                String Category = category;
                String Barcode = barcode;
                float Weight = parseFloat;
                String Department = department;
                String SubDepartment = subDepartment;
                String LongDescription = longDescription;
                String Quantity = quantity;
                String ExpiryDate = expiryDate;
                String VAT = vat;
                String AvailableForSale = availableForSale;
                String Options = options;
                String hascomment = commentrequired;
                String Optionselectedid=optionIds;
                String Optionselectedid2=optionId2;
                String Optionselectedid3=optionId3;
                String Optionselectedid4=optionId4;
                String Optionselectedid5=optionId5;
                String SoldBy = soldBy;
                String Image = image;
                String Variant = variant;
                String SKU = sku;
                String Cost = cost;
                String UserId = userId;
                String DateCreated = dateCreated;
                String LastModified = lastModified;
                String SelectedNature = selectedNature;
                String SelectedCurrency = selectedCurrency;
                String ItemCode = itemCode;
                String VATCode = vatCode;
                int discountrate = selectedDiscounts;
                String ValueOf = valueOf;
                String Discountedamount2 = discountedamount2;
                String Discountedamount3 = discountedamount3;
                double CurrentPrice = currentPrice;
                double CurrentPrice2 = currentPrice2;
                double CurrentPrice3 = currentPrice3;

                dbManager.insert(name, desc,selectedDiscounts, price,price2,price3, category, barcode, parseFloat, department,
                        subDepartment, longDescription, quantity, expiryDate, vat,
                        availableForSale,options,commentrequired,optionIds,optionId2,optionId3,optionId4,optionId5,soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,
                        selectedNature, selectedCurrency, itemCode,vatCode, valueOf,discountedamount2,discountedamount3, currentPrice,currentPrice2,currentPrice3,"Online");


// Trigger the service to insert data
                SyncAddToMssql.startSync(AddItemActivity.this, Name, Descript,discountrate, Price,Price2,Price3, Category, Barcode, Weight, Department,
                        SubDepartment, LongDescription, Quantity, ExpiryDate, VAT,
                        AvailableForSale,Options,hascomment,Optionselectedid,Optionselectedid2,Optionselectedid3,Optionselectedid4,Optionselectedid5, SoldBy, Image, Variant, SKU, Cost, UserId, DateCreated, LastModified,
                        SelectedNature, SelectedCurrency, ItemCode,VATCode, ValueOf,Discountedamount2,Discountedamount3, CurrentPrice,CurrentPrice2,CurrentPrice3);

                // Redirect to the Product activity
                Intent intent = new Intent(AddItemActivity.this, Product.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbManager.insert(name, desc,selectedDiscounts, price,price2,price3, category, barcode, parseFloat, department,
                        subDepartment, longDescription, quantity, expiryDate, vat,
                        availableForSale,options,commentrequired,optionIds,optionId2,optionId3,optionId4,optionId5, soldBy, image, variant, sku, cost, userId, dateCreated, lastModified,
                        selectedNature, selectedCurrency, itemCode,vatCode, valueOf,discountedamount2,discountedamount3, currentPrice,currentPrice2,currentPrice3,"Offline");

                dbManager.close();

                // Redirect to the Product activity
                Intent intent = new Intent(AddItemActivity.this, Product.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    // Handle the permission request response
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reload the image

            } else {
                // Permission denied, handle accordingly (e.g., show a message or use a placeholder image)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}

