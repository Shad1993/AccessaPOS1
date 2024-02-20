package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.category.CategoryDatabaseHelper.CatName;
import static com.accessa.ibora.product.items.DatabaseHelper.DEPARTMENT_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.DISCOUNT_VALUE;
import static com.accessa.ibora.product.items.DatabaseHelper.SUBDEPARTMENT_NAME;
import static com.accessa.ibora.sales.Sales.ItemGridAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModifyItemActivity extends Activity {
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private String formattedDate;

    private  long optionIds;
    private String imagePath;
    private EditText NameText;
    private EditText descText;
    private EditText PriceEditText,PriceEditText2,PriceEditText3;
    private EditText WeightEditText;
    private EditText BarcodeEditText;
    private EditText SKUEditText;
    private Spinner DepartmentSpinner;
    private Spinner SubDepartmentSpinner;
    private Spinner CategorySpinner;
    private EditText LongDescEditText;
    private EditText QuantityEditText;
    private EditText VariantEditText;
    private Spinner Discount;
    private EditText ItemCode;
    private Spinner Nature,Currency;

    private EditText CostEditText;
    private DatePicker ExpiryDates;
    private RadioGroup soldByRadioGroup;

    private RadioGroup vatRadioGroup;
    private Button buttonUpdate;
    private Button buttonDelete;
    private SwitchCompat Available4Sale;
    private SwitchCompat optionsSwitch;
    private SwitchCompat hascomment,Options;
    private SwitchCompat ExpirydateSwitch;
    private LinearLayout optionsContainer;
    private boolean isAvailableForSale,isOptionrequired;
    private boolean isCommentRequired;
    private TextView ExpiryDateText;
    private DBManager dbManager;
    private String ImageLink,cashorId;
    private long _id;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper mDatabaseHelper;
    private CategoryDatabaseHelper catDatabaseHelper;
private ImageView image_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Items");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_modify_record);

        catDatabaseHelper = new CategoryDatabaseHelper(this);

        mDatabaseHelper = new DatabaseHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID

        NameText = findViewById(R.id.itemName_edittext);
        descText = findViewById(R.id.description_edittext);
        PriceEditText = findViewById(R.id.price_edittext);
        PriceEditText2 = findViewById(R.id.price2_edittext);
        PriceEditText3 = findViewById(R.id.price3_edittext);
        BarcodeEditText = findViewById(R.id.IdBarcode_edittext);
        DepartmentSpinner = findViewById(R.id.Dept_spinner);
        SubDepartmentSpinner = findViewById(R.id.SubDept_spinner);
        CategorySpinner = findViewById(R.id.category_spinner);
        SKUEditText = findViewById(R.id.sku_edittext);
        LongDescEditText = findViewById(R.id.long_description_edittext);
        QuantityEditText = findViewById(R.id.quantity_edittext);
        ExpiryDates = findViewById(R.id.expirydate_picker);
        VariantEditText = findViewById(R.id.variant_edittext);
        WeightEditText = findViewById(R.id.weight_edittext);
        CostEditText = findViewById(R.id.cost_edittext);
        soldByRadioGroup = findViewById(R.id.soldBy_radioGroup);
        vatRadioGroup = findViewById(R.id.VAT_radioGroup);
        hascomment=findViewById(R.id.comment_switch);
        Available4Sale = findViewById(R.id.Avail4Sale_switch);
        optionsSwitch = findViewById(R.id.options_switch);
        optionsContainer = findViewById(R.id.options_container);
        ExpirydateSwitch = findViewById(R.id.perishable_switch);
        ExpiryDateText= findViewById(R.id.ExpiryText);
        ItemCode = findViewById(R.id.IdItemCode_edittext);
        Discount = findViewById(R.id.discount_spinner);
        Currency = findViewById(R.id.Currency_spinner);

        // Create a list of currency options
        List<String> currencyOptions = new ArrayList<>();
        currencyOptions.add("MUR");
        currencyOptions.add("USD");
        currencyOptions.add("EUR");
        currencyOptions.add("GBP");

        // Assuming you have already retrieved the item's ID as _id
        String cur = mDatabaseHelper.getCurrencyById(Long.parseLong(id));

        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> CurrencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyOptions);
        CurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Currency.setAdapter(CurrencyAdapter);

        if (cur != null) {
            // Handle the retrieved "Nature" value
            for (int i = 0; i < currencyOptions.size(); i++) {
                if (currencyOptions.get(i).equals(cur)) {
                    Currency.setSelection(i);
                    break;
                }
            }
        }

        // Set an item selection listener for the "Nature" spinner
        Currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemCode = parent.getItemAtPosition(position).toString();
                // Handle the selected nature here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });



        // Inside your onCreate method, after initializing other views
        Nature = findViewById(R.id.Nature_spinner);

        // Create a list of options for the Nature spinner
        List<String> natureOptions = new ArrayList<>();
        natureOptions.add("GOODS");
        natureOptions.add("SERVICES");

        // Assuming you have already retrieved the item's ID as _id
        String nature = mDatabaseHelper.getNatureById(Long.parseLong(id));

        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> natureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, natureOptions);
        natureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Nature.setAdapter(natureAdapter);

        if (nature != null) {
            // Handle the retrieved "Nature" value
            for (int i = 0; i < natureOptions.size(); i++) {
                if (natureOptions.get(i).equals(nature)) {
                    Nature.setSelection(i);
                    break;
                }
            }
        }

// Set an item selection listener for the "Nature" spinner
        Nature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedNature = parent.getItemAtPosition(position).toString();
                // Handle the selected nature here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });





        ImageView imageView = findViewById(R.id.image_view);



        // Image selection button
        Button imageButton = findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOptionsDialog();
            }
        });




        Available4Sale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAvailableForSale = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifyItemActivity.this, "Item Available For Sale", Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifyItemActivity.this, "Item Not Available For Sale", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hascomment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCommentRequired = isChecked;

                if (isChecked) {
                    // Switch is checked
                    Toast.makeText(ModifyItemActivity.this, R.string.hascomment, Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is unchecked
                    Toast.makeText(ModifyItemActivity.this, R.string.hanocomment, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        ExpirydateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ExpiryDateText.setVisibility(View.VISIBLE);
                    ExpiryDates.setVisibility(View.VISIBLE);
                } else {
                    // Toggle button is unchecked
                    ExpiryDateText.setVisibility(View.GONE);
                    ExpiryDates.setVisibility(View.GONE);
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
                        WeightEditText.setVisibility(View.GONE);
                        QuantityEditText.setVisibility(View.VISIBLE);
                    } else if (selectedOption.equals(getString(R.string.soldBy_volume))) {
                        WeightEditText.setVisibility(View.VISIBLE);
                        QuantityEditText.setVisibility(View.GONE);
                    }
                }
            }
        });

        int year = ExpiryDates.getYear();
        int month = ExpiryDates.getMonth() + 1;
        int dayOfMonth = ExpiryDates.getDayOfMonth();

        ExpiryDates.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the formattedDate variable with the new selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(calendar.getTime());
            }
        });



        _id = Long.parseLong(id);




        Item item = dbManager.getItemById(id);
        if (item != null) {
            NameText.setText(item.getName());
            descText.setText(item.getDescription());
            PriceEditText.setText(String.valueOf(item.getPrice()));
            PriceEditText2.setText(String.valueOf(item.getPrice2()));
            PriceEditText3.setText(String.valueOf(item.getPrice3()));
            BarcodeEditText.setText(item.getBarcode());
            DepartmentSpinner.setSelection(getSpinnerIndex1(DepartmentSpinner, item.getDepartment()));
            SubDepartmentSpinner.setSelection(getSpinnerIndex2(SubDepartmentSpinner, item.getSubDepartment()));
            CategorySpinner.setSelection(getSpinnerIndex(CategorySpinner, item.getCategory()));
            Discount.setSelection(getSpinnerIndex4(Discount, item.getDiscount()));
            CostEditText.setText(String.valueOf(item.getPrice()));
            VariantEditText.setText(item.getVariant());
            ItemCode.setText(item.getItemCode());

             ImageLink= item.getImage();

            Glide.with(this).load(ImageLink).into(imageView);

            LongDescEditText.setText(item.getLongDescription());
            WeightEditText.setText(String.valueOf(item.getWeight()));
            QuantityEditText.setText(String.valueOf(item.getQuantity()));
            SKUEditText.setText(item.getSKU());
            if(item.getExpiryDate() != null) {
                setDateOnDatePicker(item.getExpiryDate());
                // Check the specific condition
                boolean isConditionMet = true;

            // Set the switch as true if the condition is met
                if (isConditionMet) {
                    ExpirydateSwitch.setChecked(true);
                }
            }
            // set switch as true if available for sale
            Boolean isConditionMet =item.getAvailableForSale();

            if (Boolean.TRUE.equals(isConditionMet)) {
                Available4Sale.setChecked(true);
            }


            Boolean hasoptions =item.gethasoptions();

            Log.d("hasoption", String.valueOf(hasoptions));
            if (Boolean.TRUE.equals(hasoptions)) {
                optionsSwitch.setChecked(true);
            }

            String hascomments =item.gethascomment();

            if (hascomments.trim().equals("true")){
                Log.d("hascomments", hascomments);
                hascomment.setChecked(true);
            }
        }

        Cursor categoryCursor = catDatabaseHelper.getAllCategory();
        Cursor departmentCursor = mDatabaseHelper.getAllDepartment();
        Cursor subDepartmentCursor = mDatabaseHelper.getAllSubDepartment();
        Cursor discountCursor = mDatabaseHelper.getAllDiscounts();


        List<String> categories = new ArrayList<>();
        categories.add("All Categories");
        if (categoryCursor.moveToFirst()) {
            do {
                String category = categoryCursor.getString(categoryCursor.getColumnIndex(CatName));
                categories.add(category);
            } while (categoryCursor.moveToNext());
        }
        categoryCursor.close();

        List<String> departments = new ArrayList<>();
        departments.add("All Departments");
        if (departmentCursor.moveToFirst()) {
            do {
                String department = departmentCursor.getString(departmentCursor.getColumnIndex(DEPARTMENT_NAME));
                departments.add(department);
            } while (departmentCursor.moveToNext());
        }
        departmentCursor.close();

        List<String> subDepartments = new ArrayList<>();
        subDepartments.add("All Sub Departments");
        if (subDepartmentCursor.moveToFirst()) {
            do {
                String subDepartment = subDepartmentCursor.getString(subDepartmentCursor.getColumnIndex(SUBDEPARTMENT_NAME));
                subDepartments.add(subDepartment);
            } while (subDepartmentCursor.moveToNext());
        }
        subDepartmentCursor.close();


        // Fetch discount data from your database
        List<String> discountOptions = new ArrayList<>();
        // Fetch the data and populate the discountOptions list

        discountOptions.add(getString(R.string.NODISCOUNT));
        if (discountCursor.moveToFirst()) {
            do {
                String discount_name = discountCursor.getString(discountCursor.getColumnIndex(DatabaseHelper.DISCOUNT_NAME));
                String discount_Value= discountCursor.getString(discountCursor.getColumnIndex(DISCOUNT_VALUE));
                discountOptions.add(discount_Value);
            } while (discountCursor.moveToNext());
        }
        discountCursor.close();




        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> discountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, discountOptions);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> spinnerAdapterDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        ArrayAdapter<String> spinnerAdapterSubDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subDepartments);
        discountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapterDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapterSubDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        natureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(spinnerAdapter);
        DepartmentSpinner.setAdapter(spinnerAdapterDept);
        SubDepartmentSpinner.setAdapter(spinnerAdapterSubDept);
        Discount.setAdapter(discountAdapter);


        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        // Retrieve the existing category value from the database (replace item.getCategory() with the correct method)
        String existingCategory = item.getCategory();
        String existingDepartment = item.getDepartment();
        String existingSubDepartment = item.getSubDepartment();
        String existingDiscount = item.getRateDiscount();
        // Get the index of the existing category value in the spinner's data source
        int categoryIndex = spinnerAdapter.getPosition(existingCategory);
        int DeptIndex = spinnerAdapterDept.getPosition(existingDepartment);
        int SubDeptIndex = spinnerAdapterSubDept.getPosition(existingSubDepartment);
        int DiscountIndex = discountAdapter.getPosition(existingDiscount);


        // Set the selected item of the CategorySpinner to the existing category value
        CategorySpinner.setSelection(categoryIndex);

        // Set the selected item of the CategorySpinner to the existing category value
        DepartmentSpinner.setSelection(DeptIndex);

        // Set the selected item of the CategorySpinner to the existing category value
        SubDepartmentSpinner.setSelection(SubDeptIndex);

        Discount.setSelection(DiscountIndex);

        // Retrieve the existing soldBy value from the database (replace item.getSoldBy() with the correct method)
        String existingSoldBy = item.getSoldBy();

        // Find the radio button corresponding to the existing soldBy value and set it as checked
        if (existingSoldBy != null && existingSoldBy.equals("Each")) {
            soldByRadioGroup.check(R.id.soldBy_each_radio);
        } else if (existingSoldBy != null && existingSoldBy.equals("Volume")) {
            soldByRadioGroup.check(R.id.soldBy_volume_radio);
        }

        // Retrieve the existing VAT value from the database (replace item.getVAT() with the correct method)
        String existingVAT = item.getVAT();

        // Check the radio button based on the value
        if (existingVAT != null && existingVAT.equals("VAT 0%")) {
            vatRadioGroup.check(R.id.VAT_0_radio);
        } else if (existingVAT != null && existingVAT.equals("VAT Exempted")) {
            vatRadioGroup.check(R.id.VAT_Exempted_radio);
        } else if (existingVAT != null && existingVAT.equals("VAT 15%")) {
            vatRadioGroup.check(R.id.VAT_15_radio);
        }


        // Expiry date switch

        DepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        SubDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        Discount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Do something with the selectedDiscount (e.g., store it in a variable)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });


        buttonUpdate = findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });
        buttonDelete = findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
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
    private void displayOptions() {

        // Retrieve options from the database
        List<Options> variantsList = mDatabaseHelper.getAllVariants();

        // Clear previous options
        optionsContainer.removeAllViews();

        // Dynamically create checkboxes for each variant
        for (Options options : variantsList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(options.getOptionname());
            checkBox.setChecked(false); // Set the initial state as unchecked

            optionIds = options.getOptionId(); // Get the option ID from the Options object

            // Set a listener for checkbox changes
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Checkbox is checked, show a toast with the option ID
                        Toast.makeText(ModifyItemActivity.this, "Option ID: " + optionIds + " checked", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Add the checkbox to the container
            optionsContainer.addView(checkBox);
        }
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
    private void loadImageFromUrl(String imageUrl) {
        // Implement code to load image from URL
        imagePath = imageUrl;
        // You can also display the selected image here if needed
        ImageView imageView = findViewById(R.id.image_view);
        Glide.with(this).load(imageUrl).into(imageView);
    }
    private void updateItem() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String lastmodified = dateFormat.format(new Date(currentTimeMillis));
        String name = NameText.getText().toString().trim();
        String desc = descText.getText().toString().trim();
        String price = PriceEditText.getText().toString().trim();
        String price2 = PriceEditText2.getText().toString().trim();
        String price3 = PriceEditText3.getText().toString().trim();
        String barcode = BarcodeEditText.getText().toString().trim();
        String department = DepartmentSpinner.getSelectedItem().toString();
        String subDepartment = SubDepartmentSpinner.getSelectedItem().toString();
        String category = CategorySpinner.getSelectedItem().toString();
        String sku = SKUEditText.getText().toString().trim();
        String variant = VariantEditText.getText().toString().trim();
        String cost = CostEditText.getText().toString().trim();
        String weight = WeightEditText.getText().toString().trim();
        String longDescription = LongDescEditText.getText().toString().trim();
        String quantity = QuantityEditText.getText().toString().trim();
        String expiryDate = getDateFromDatePicker(ExpiryDates);
        String soldBy = getSelectedRadioButtonText(soldByRadioGroup);
        String vat = getSelectedRadioButtonText(vatRadioGroup);
        String availableForSale =String.valueOf(isAvailableForSale);
        String itemCode =ItemCode.getText().toString().trim();
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
            currentPrice = Double.parseDouble(price);
            currentPrice2 = Double.parseDouble(price2);
            currentPrice3 = Double.parseDouble(price3);
            discountedAmount = 0.00;
            discountedAmount2 = 0.00;
            discountedAmount3 = 0.00;

        } else {
            selectedDiscounts = Integer.parseInt(Discount.getSelectedItem().toString());

            double prices= Double.parseDouble(price);
            double prices2= Double.parseDouble(price2);
            double prices3= Double.parseDouble(price3);


            // Corrected discountedAmount calculation
            discountedAmount = (selectedDiscounts / 100.0) * prices;
            discountedAmount2 = (selectedDiscounts / 100.0) * prices2;
            discountedAmount3 = (selectedDiscounts / 100.0) * prices3;

            // Calculate the current price
            currentPrice = prices - discountedAmount;
            currentPrice2 = prices2 - discountedAmount2;
            currentPrice3 = prices3 - discountedAmount3;


        }

        String vats = vat;
        String VatCode = "";
        if (vat.equals("VAT 15%")) {
            VatCode = "TC01";
        } else if (vat.equals("VAT 0%")) {
            VatCode = "TC02";
        } else if (vat.equals("VAT Exempted")) {
            VatCode = "TC03";
        }

        String image ;
        if( imagePath== null) {
            image=  ImageLink;

        } else{
            image=  imagePath;

        }


        if (name.isEmpty() || desc.isEmpty() || price.isEmpty() ||price2.isEmpty() || price3.isEmpty() || barcode.isEmpty() || department.isEmpty()
                || subDepartment.isEmpty() || longDescription.isEmpty() || quantity.isEmpty() || expiryDate.isEmpty()
                || soldBy.isEmpty() || vat.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }




        boolean isUpdated = dbManager.updateItem( _id,name, desc,selectedDiscounts, price,price2,price3, category, barcode, Float.parseFloat(weight), department,
                subDepartment, longDescription, quantity, expiryDate, vat,
                availableForSale, soldBy, image, variant, sku, cost, cashorId,  lastmodified,
                selectedNature, selectedCurrency, itemCode,VatCode, discountedAmount,discountedAmount2,discountedAmount3, currentPrice,currentPrice2,currentPrice3,"Offline");



        returnHome();
        if (isUpdated) {
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        boolean isDeleted = dbManager.deleteItem(_id);
        returnHome();
        if (isDeleted) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Item_fragment");
        startActivity(home_intent1);
    }

    private int getSpinnerIndex1(Spinner spinner, String value) {
        // Retrieve departments from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(spinner.getContext());
        Cursor departmentCursor = databaseHelper.getAllDepartment();

        List<String> departments = new ArrayList<>();

        // Iterate through the cursor and add department names to the list
        if (departmentCursor.moveToFirst()) {
            do {
                String department = departmentCursor.getString(departmentCursor.getColumnIndex(DEPARTMENT_NAME));
                departments.add(department);
            } while (departmentCursor.moveToNext());
        }

        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapterDept = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, departments);
        spinnerAdapterDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterDept);

        // Find the index of the selected value in the spinner
        int index = departments.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);
            Toast.makeText(spinner.getContext(), "Index: " + index, Toast.LENGTH_SHORT).show();
            return index;
        }

        // If the selected value is not found, default to the first item
        spinner.setSelection(0);
        Toast.makeText(spinner.getContext(), "Index: 0", Toast.LENGTH_SHORT).show();
        return 0; // Default index
    }



    private int getSpinnerIndex2(Spinner spinner, String value) {
        // Retrieve categories from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(spinner.getContext());
        Cursor subDepartmentCursor = databaseHelper.getAllSubDepartment();

        List<String> subdepartment = new ArrayList<>();

        // Iterate through the cursor and add categories to the list
        if (subDepartmentCursor.moveToFirst()) {
            do {
                String category = subDepartmentCursor.getString(subDepartmentCursor.getColumnIndex(SUBDEPARTMENT_NAME));
                subdepartment.add(category);
            } while (subDepartmentCursor.moveToNext());
        }

        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapterSubDept = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, subdepartment);
        spinnerAdapterSubDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterSubDept);

        // Find the index of the selected value in the spinner
        int index = subdepartment.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);
            return index;
        }

        // If the selected value is not found in categories, default to the first item
        spinner.setSelection(0);
        return 0; // Default index
    }
    private int getSpinnerIndex(Spinner spinner, String value) {
        // Retrieve categories from the database
        CategoryDatabaseHelper databaseHelper = new CategoryDatabaseHelper(spinner.getContext());
        Cursor categoryCursor = databaseHelper.getAllCategory();

        List<String> categories = new ArrayList<>();

        // Iterate through the cursor and add categories to the list
        if (categoryCursor.moveToFirst()) {
            do {
                String category = categoryCursor.getString(categoryCursor.getColumnIndex(CatName));
                categories.add(category);
            } while (categoryCursor.moveToNext());
        }

        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Find the index of the selected value in the spinner
        int index = categories.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);
            return index;
        }

        // If the selected value is not found in categories, default to the first item
        spinner.setSelection(0);
        return 0; // Default index
    }

    private int getSpinnerIndex4(Spinner spinner, String value) {
        // Retrieve categories from the database

        DatabaseHelper databaseHelper = new DatabaseHelper(spinner.getContext());
        Cursor DiscountCursor = databaseHelper.getAllDiscounts();
        List<String> discounts = new ArrayList<>();

        // Iterate through the cursor and add categories to the list
        if (DiscountCursor.moveToFirst()) {
            do {
                String category = DiscountCursor.getString(DiscountCursor.getColumnIndex(DISCOUNT_VALUE));
                discounts.add(category);
            } while (DiscountCursor.moveToNext());
        }

        // Create an ArrayAdapter and populate it with the retrieved data
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, discounts);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Find the index of the selected value in the spinner
        int index = discounts.indexOf(value);
        if (index != -1) {
            // Set the spinner selection to the found index
            spinner.setSelection(index);
            return index;
        }

        // If the selected value is not found in categories, default to the first item
        spinner.setSelection(0);
        return 0; // Default index
    }

    private String getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void setDateOnDatePicker(String date) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                Date parsedDate = dateFormat.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                ExpiryDates.init(year, month, day, null);
            } catch (ParseException e) {
                e.printStackTrace();
            }

    }
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
    private String getSelectedRadioButtonText(RadioGroup radioGroup) {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId != -1) {
            RadioButton radioButton = findViewById(checkedRadioButtonId);
            return radioButton.getText().toString();
        }
        return "";
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        catDatabaseHelper.close();
        mDatabaseHelper.close();
    }
}
