package com.accessa.ibora.Report;

import static com.accessa.ibora.Constants.DB_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_DATE_TRANSACTION;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_PAYMENT_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_TOTAL_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_CASHIER_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE_CREATED;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME_ARCHIVE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_TTC;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.Help.Help;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.login.login;
import com.accessa.ibora.printer.PrintDuplicata;
import com.accessa.ibora.printer.PrintReport;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.AddItemActivity;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesReportActivity extends AppCompatActivity {
    private String cashorId;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private List<PaymentItem> paymentItems; // Define the paymentItems list
    private SharedPreferences sharedPreferences,usersharedPreferences,AccessLevelsharedPreferences;
    private MaterialToolbar toolbar;
    private TextView CompanyName;
    private String Shopname;
    private PaymentAdapter paymentAdapter;
    private SearchView searchView;
    private Spinner dateFilterSpinner;
    private TextView dailyAmountTextView;
    private TextView weeklyAmountTextView;
    private TextView monthlyAmountTextView;
    private TextView dateTextView,enddateTextView,TotalSaleAmountTextView;
    private ImageView datePickerIcon;
    private RecyclerView recyclerView;
   private FloatingActionButton mAddFab,mExitFab;
    private DatabaseHelper mDatabaseHelper;
    public static final String TRANSACTION_TABLE_NAME = "Transactions";
    public static final String TRANSACTION_TOTAL_PRICE = "TotalPrice";
    public static final String TRANSACTION_DATE = "TransactionDate";
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        usersharedPreferences = getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
        AccessLevelsharedPreferences = this.getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);


        setContentView(R.layout.activity_sales_report);

        dailyAmountTextView = findViewById(R.id.dailyAmountTextView);
        weeklyAmountTextView = findViewById(R.id.weeklyAmountTextView);
        monthlyAmountTextView = findViewById(R.id.monthlyAmountTextView);
        // Get references to the date TextView and ImageView
         dateTextView = findViewById(R.id.dateTextView);
         datePickerIcon = findViewById(R.id.datePickerIcon);
         enddateTextView=findViewById(R.id.enddateTextView);
        TotalSaleAmountTextView = findViewById(R.id.totalSaleAmountTextView);

        // Get the current date
        Date currentDates = new Date();

        // Format the current date as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String currentDateString = dateFormat.format(currentDates);

        // Set the current date as the default date in the TextView
        dateTextView.setText(currentDateString);
        enddateTextView.setText(currentDateString);
        mDatabaseHelper = new DatabaseHelper(this);

        // Initialize the paymentItems list (e.g., retrieve data from the database)
        paymentItems = getPaymentItems();
        // Initialize the paymentAdapter
        paymentAdapter = new PaymentAdapter(paymentItems, this);


        // Set up the RecyclerView
         recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(paymentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name

        //toolbar
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(R.string.Report);
        setSupportActionBar(toolbar);




        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Get the header view from the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Find the TextView within the header view
        name = headerView.findViewById(R.id.name);
        CashorId = headerView.findViewById(R.id.CashorId);
        CompanyName = headerView.findViewById(R.id.Company_name);

        // Set the user ID and name in the TextViews
        CashorId.setText(cashorId);
        name.setText(cashorName);
        CompanyName.setText(Shopname);

        // Retrieve the list of users from the database
        List<Cashier> userList = getUsersFromDatabase();

        // Create an ArrayAdapter using the userList
        ArrayAdapter<Cashier> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userList);

        // Set the dropdown layout resource for the ArrayAdapter
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Get the Spinner view
        Spinner cashierSpinner = findViewById(R.id.cashierSpinner);

        // Set the ArrayAdapter on the Spinner
        cashierSpinner.setAdapter(userAdapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("drawer27", "drawer27");

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                int levelNumber = Integer.parseInt(cashorlevel); // Adjust this based on the user's level

                if (id == R.id.Sales) {
                    String Activity="sales_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            // Set "roomnum" to 1
                            editor.putInt("roomnum", 1);
                            editor.putInt("room_id", 1);
                            editor.putString("table_id", "0");
                            editor.putString("table_num", "0");
                            editor.apply(); // Commit the changes

                            Intent intent = new Intent(SalesReportActivity.this, MainActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        // Set "roomnum" to 1
                        editor.putInt("roomnum", 1);
                        editor.putInt("room_id", 1);
                        editor.putString("table_id", "0");
                        editor.putString("table_num", "0");
                        editor.apply(); // Commit the changes

                        Intent intent = new Intent(SalesReportActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Sales", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Receipts) {
                    String Activity="receipts_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(SalesReportActivity.this, ReceiptActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(SalesReportActivity.this, ReceiptActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Sales", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Shift) {
                    String Activity="shift_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(SalesReportActivity.this, SalesReportActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(SalesReportActivity.this, SalesReportActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Shift", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Items) {
                    String Activity="Items_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(SalesReportActivity.this, Product.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(SalesReportActivity.this, Product.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Settings) {
                    String Activity="settings_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(SalesReportActivity.this, SettingsDashboard.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(SalesReportActivity.this, SettingsDashboard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    Intent intent = new Intent(SalesReportActivity.this, Help.class);
                    startActivity(intent);
                } else if (id == R.id.nav_Admin) {
                    String Activity="admin_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(SalesReportActivity.this, AdminActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if ((!canHigherAccessReceipt && canAccessReceipt) || levelNumber==7 )  {
                        Intent intent = new Intent(SalesReportActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Admin", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });


        // Open your SQLite database
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // Get current date
        Date currentDate = new Date();

        // Calculate daily amount
        double dailyAmount = getDailyAmount(currentDate);
        dailyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " +formatAmount(dailyAmount));

        // Calculate weekly amount
        double weeklyAmount = getWeeklyAmount(currentDate);
        weeklyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " +formatAmount(weeklyAmount));

        // Calculate monthly amount
        double monthlyAmount = getMonthlyAmount(currentDate);
        monthlyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " "+formatAmount(monthlyAmount));


        double totalAmount = getTotalSaleAmountperday(currentDate);

// Set the total amount to the TextView
        TotalSaleAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " + formatAmount(totalAmount));




        // Set click listeners for the date TextView and ImageView
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mAddFab = findViewById(R.id.print_fab);
        mExitFab =findViewById(R.id.exit_fab);

        mExitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesReportActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelNumber = Integer.parseInt(cashorlevel);

                String Activity="printReport_";

                boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                if (canHigherAccessReceipt ) {
                    showPinDialog(Activity, () -> {
                        openNewActivity();
                    });
                }
                // Check permission for Receipts
                else if (!canHigherAccessReceipt && canAccessReceipt) {
                    openNewActivity();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                }


            }
        });

        cashierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected cashier from the spinner
                Cashier selectedCashier = (Cashier) parent.getItemAtPosition(position);
                String selectedCashiername = parent.getItemAtPosition(position).toString();

                // Retrieve the cashier's ID instead of the name
                String selectedCashierId = selectedCashier.getId();

                // Get the current selected start and end dates
                String startDateString = dateTextView.getText().toString();
                String endDateString = enddateTextView.getText().toString();

                // Convert the start and end dates to Date objects
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                Date startDate;
                Date endDate;
                try {
                    startDate = dateFormat.parse(startDateString);
                    endDate = dateFormat.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle the parsing error if needed
                    return;
                }

                // Update the data based on the selected cashier and date range
                updateDataForDateRangeAndCashier(startDate, endDate, selectedCashierId,selectedCashiername);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected, if needed
            }
        });


    }

    private void showPinDialog(String activity, Runnable onSuccessAction) {
        // Inflate the PIN dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View pinDialogView = inflater.inflate(R.layout.pin_dialog, null);
        EditText pinEditText = pinDialogView.findViewById(R.id.editTextPIN);

        // Find buttons
        Button buttonClear = pinDialogView.findViewById(R.id.buttonClear);
        Button buttonLogin = pinDialogView.findViewById(R.id.buttonLogin);

        // Set up button click listeners
        setPinButtonClickListeners(pinDialogView, pinEditText);

        // Create the PIN dialog
        AlertDialog.Builder pinBuilder = new AlertDialog.Builder(this);
        pinBuilder.setTitle("Enter PIN")
                .setView(pinDialogView);
        AlertDialog pinDialog = pinBuilder.create();
        pinDialog.show();

        // Clear button functionality
        buttonClear.setOnClickListener(v -> onpinClearButtonClick(pinEditText));

        // Login button functionality
        buttonLogin.setOnClickListener(v -> {
            String enteredPIN = pinEditText.getText().toString();
            int cashorLevel = validatePIN(enteredPIN);

            if (cashorLevel != -1) { // PIN is valid
                SharedPreferences sharedPreferences = this.getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

                // Check if the user has permission
                boolean accessAllowed = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, activity, cashorLevel);
                if (accessAllowed) {
                    String cashorName =mDatabaseHelper.getCashorNameByPin(enteredPIN);
                    int cashorId =mDatabaseHelper.getCashorIdByPin(enteredPIN);
                    mDatabaseHelper.logUserActivity(cashorId, cashorName, cashorLevel, activity);
                    onSuccessAction.run(); // Execute the provided action on success
                    pinDialog.dismiss(); // Dismiss the PIN dialog after successful login
                } else {
                    showPermissionDeniedDialog(); // Show a permission denied dialog
                }
            } else {
                Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("You do not have permission to access this feature.")
                .setPositiveButton("OK", null)
                .show();
    }
    private int validatePIN(String enteredPIN) {
        // Fetch the cashor level based on the entered PIN
        int cashorLevel = mDatabaseHelper.getCashorLevelByPIN(enteredPIN);

        // Return the cashor level if valid, or -1 if invalid
        return cashorLevel;
    }
    public void onpinClearButtonClick(EditText ReceivedEditText) {

        onclearButtonClick(ReceivedEditText);
        onPinclearButtonClick(ReceivedEditText);


    }
    private void onclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");
            // ReceivedEditText.setText("");
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(this, "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    private void onPinclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");

        } else {
            // Show a toast message if EditText is null
            Toast.makeText(this, "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    private void setPinButtonClickListeners(View pinDialogView, final EditText pinEditText) {
        int[] buttonIds = new int[] {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonClear
        };

        for (int id : buttonIds) {
            Button button = pinDialogView.findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPinNumberButtonClick((Button) v, pinEditText);
                }
            });
        }
    }
    public void onPinNumberButtonClick(Button button, EditText pinEditText) {
        if (pinEditText != null) {
            String buttonText = button.getText().toString();

            switch (buttonText) {
                case "Clear": // Handle clear
                    pinEditText.setText("");
                    break;
                case "BS": // Handle backspace
                    CharSequence currentText = pinEditText.getText();
                    if (currentText.length() > 0) {
                        pinEditText.setText(currentText.subSequence(0, currentText.length() - 1));
                    }
                    break;
                default: // Handle numbers
                    pinEditText.append(buttonText);
                    break;
            }
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(this, "EditText is not initialized", Toast.LENGTH_SHORT).show();
        }
    }
    public void openNewActivity() {
        Configuration configuration = getResources().getConfiguration();
        Locale currentLocale = configuration.locale;


        // Start the AddItemActivity
        Intent intent = new Intent(this, PrintReport.class);
        intent.putExtra("locale", currentLocale.toString());
        startActivity(intent);
    }
    private void showDatePickerDialog() {
        // Get the current selected start and end dates
        String startDateString = dateTextView.getText().toString();
        String endDateString = enddateTextView.getText().toString();

        // Convert the start and end dates to Date objects
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date startDate;
        Date endDate;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing error if needed
            return;
        }

        // Create a date picker dialog for the start date
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(SalesReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the selected start date with the chosen date
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date newStartDate = calendar.getTime();

                        // Update the start date TextView with the new selected date
                        String newStartDateString = formatDates(newStartDate);
                        dateTextView.setText(newStartDateString);

                        // Get the current selected end date
                        String endDateString = enddateTextView.getText().toString();
                        Date endDate;
                        try {
                            endDate = dateFormat.parse(endDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Handle the parsing error if needed
                            return;
                        }

                        // Update the data or perform any necessary actions based on the new date range
                        updateDataForDateRange(newStartDate, endDate);
                    }
                },
                startDate.getYear() + 1900,
                startDate.getMonth(),
                startDate.getDate());

        // Create a date picker dialog for the end date
        DatePickerDialog endDatePickerDialog = new DatePickerDialog(SalesReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the selected end date with the chosen date
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date newEndDate = calendar.getTime();

                        // Update the end date TextView with the new selected date
                        String newEndDateString = formatDates(newEndDate);
                        enddateTextView.setText(newEndDateString);

                        // Get the current selected start date
                        String startDateString = dateTextView.getText().toString();
                        Date startDate;
                        try {
                            startDate = dateFormat.parse(startDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Handle the parsing error if needed
                            return;
                        }

                        // Update the data or perform any necessary actions based on the new date range
                        updateDataForDateRange(startDate, newEndDate);
                    }
                },
                endDate.getYear() + 1900,
                endDate.getMonth(),
                endDate.getDate());

        // Show the date picker dialogs
        endDatePickerDialog.show(); // Show the end date picker dialog first
        startDatePickerDialog.show(); // Show the start date picker dialog last
    }

    private void updateDataForDateRangeAndCashier(Date startDate, Date endDate, String selectedCashier,String cashiorname) {
        // Format the start and end dates as strings
        String startDateString = formatDateTimes(startDate);
        String endDateString = formatDateTimes(endDate);
        List<PaymentItem> filteredPaymentItems ;
        double totalSaleAmount;
        if (cashiorname.equals("All Cashiers")) {
            filteredPaymentItems = mDatabaseHelper.getFilteredPaymentItems(startDateString, endDateString);
            totalSaleAmount = mDatabaseHelper.getTotalSaleAmount(startDateString, endDateString);
            // Get current date
            Date currentDate = new Date();

            // Calculate daily amount
            double dailyAmount = getDailyAmount(currentDate);
            dailyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " +formatAmount(dailyAmount));

            // Calculate weekly amount
            double weeklyAmount = getWeeklyAmount(currentDate);
            weeklyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " +formatAmount(weeklyAmount));

            // Calculate monthly amount
            double monthlyAmount = getMonthlyAmount(currentDate);
            monthlyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " "+formatAmount(monthlyAmount));


        } else {


            filteredPaymentItems = mDatabaseHelper.getFilteredPaymentItemsByCashierCode(startDateString, endDateString, selectedCashier);
            totalSaleAmount = mDatabaseHelper.getTotalSaleAmountpercashior(startDateString, endDateString, selectedCashier);
            // Get current date
            Date currentDate = new Date();

            // Calculate daily amount
            double dailyAmount = getDailyAmountPerCashior(currentDate,selectedCashier);
            dailyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " +formatAmount(dailyAmount));

            // Calculate weekly amount
            double weeklyAmount = getWeeklyAmountpercashior(currentDate,selectedCashier);
            weeklyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " +formatAmount(weeklyAmount));

            // Calculate monthly amount
            double monthlyAmount = getMonthlyAmountpercashior(currentDate,selectedCashier);
            monthlyAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " "+formatAmount(monthlyAmount));



        }

        paymentAdapter.updateItems(filteredPaymentItems);

        // Set the total sale amount to the TextView
        TotalSaleAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " + formatAmount(totalSaleAmount));
    }

    private void updateDataForDateRange(Date startDate, Date endDate) {
        // Format the start and end dates as strings
        String startDateString = formatDateTimes(startDate);
        String endDateString = formatDateTimes(endDate);

        // Get the selected cashier ID
        Spinner cashierSpinner = findViewById(R.id.cashierSpinner);
        String selectedCashier = cashierSpinner.getSelectedItem().toString();

        // Query the database to get the filtered payment items and total sale amount
        List<PaymentItem> filteredPaymentItems;
        double totalSaleAmount;

        if (selectedCashier.equals("All Cashiers")) {
            filteredPaymentItems = mDatabaseHelper.getFilteredPaymentItems(startDateString, endDateString);
            totalSaleAmount = mDatabaseHelper.getTotalSaleAmount(startDateString, endDateString);

            Log.d("startDateString0", String.valueOf(startDateString));
            Log.d("endDateString0", String.valueOf(endDateString));
            Log.d("selectedCashier0", String.valueOf(selectedCashier));
            Log.d("filteredPaymentItems0", String.valueOf(filteredPaymentItems));
            Log.d("totalSaleAmount0", String.valueOf(totalSaleAmount));
        } else {


            filteredPaymentItems = mDatabaseHelper.getFilteredPaymentItemsByCashier(startDateString, endDateString, selectedCashier);
            totalSaleAmount = mDatabaseHelper.getTotalSaleAmountByCashier(startDateString, endDateString, selectedCashier);

            Log.d("startDateString1", String.valueOf(startDateString));
            Log.d("endDateString1", String.valueOf(endDateString));
            Log.d("selectedCashier1", String.valueOf(selectedCashier));
            Log.d("filteredPaymentItems1", String.valueOf(filteredPaymentItems));
            Log.d("totalSaleAmount1", String.valueOf(totalSaleAmount));

        }

        // Update the RecyclerView adapter with the filtered payment items
        paymentAdapter.updateItems(filteredPaymentItems);

        // Set the total sale amount to the TextView
        TotalSaleAmountTextView.setText(getResources().getString(R.string.currency_symbol) + " " + formatAmount(totalSaleAmount));
    }


    // Helper method to format the selected date as a string

    private String formatDates(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
    private double getDailyAmount(Date date) {
        // Calculate the start and end date of the current day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

// Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);
        Cursor cursor = database.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? " +
                        "AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')",
                new String[]{formattedStartDate, formattedEndDate}
        );

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;

    }

    private double getWeeklyAmount(Date date) {
        // Calculate the start and end date of the current week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        // Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        // Query the database including archive table
        Cursor cursor = database.rawQuery(
                "SELECT SUM(TotalSumTTC) FROM (" +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN') " +
                        "UNION ALL " +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME_ARCHIVE + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')" +
                        ")",
                new String[]{formattedStartDate, formattedEndDate, formattedStartDate, formattedEndDate}
        );

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();
        return amount;
    }

    private double getMonthlyAmount(Date date) {
        // Calculate the start and end date of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

        // Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        // Query the database including archive table
        Cursor cursor = database.rawQuery(
                "SELECT SUM(TotalSumTTC) FROM (" +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN') " +
                        "UNION ALL " +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME_ARCHIVE + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')" +
                        ")",
                new String[]{formattedStartDate, formattedEndDate, formattedStartDate, formattedEndDate}
        );

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();
        return amount;
    }

    private double getDailyAmountPerCashior(Date date, String cashorId) {
        // Calculate the start and end date of the current day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        // Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        // Query the database to get the daily amount filtered by cashorId
        Cursor cursor = database.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND " + TRANSACTION_CASHIER_CODE + " = ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')",
                new String[]{formattedStartDate, formattedEndDate, cashorId}
        );


        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;
    }
    private double getWeeklyAmountpercashior(Date date, String cashorId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        Cursor cursor = database.rawQuery(
                "SELECT SUM(TotalSumTTC) FROM (" +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND " + TRANSACTION_CASHIER_CODE + " = ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN') " +
                        "UNION ALL " +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME_ARCHIVE + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND " + TRANSACTION_CASHIER_CODE + " = ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')" +
                        ")",
                new String[]{formattedStartDate, formattedEndDate, cashorId, formattedStartDate, formattedEndDate, cashorId}
        );

        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();
        return amount;
    }

    private double getMonthlyAmountpercashior(Date date, String cashorId) {
        // Calculate the start and end date of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

        // Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        // Query the database including archive table
        Cursor cursor = database.rawQuery(
                "SELECT SUM(TotalSumTTC) FROM (" +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND " + TRANSACTION_CASHIER_CODE + " = ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN') " +
                        "UNION ALL " +
                        "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME_ARCHIVE + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND " + TRANSACTION_CASHIER_CODE + " = ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')" +
                        ")",
                new String[]{formattedStartDate, formattedEndDate, cashorId, formattedStartDate, formattedEndDate, cashorId}
        );

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();
        return amount;
    }


    private String formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(amount);
    }
    private String formatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
    private String formatDateTimes(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }
    private List<Cashier> getUsersFromDatabase() {
        List<Cashier> userList = new ArrayList<>();
        // Add the special "All Cashiers" entry
        userList.add(new Cashier("0", "All Cashiers"));

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mDatabaseHelper.getAllUsers();

        if (cursor.moveToFirst()) {
            do {
                String cashorId = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_id));
                String cashorName = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_NAME));

                Cashier cashier = new Cashier(cashorId, cashorName);
                userList.add(cashier);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }


    private List<PaymentItem> getPaymentItems() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Retrieve the data from the database or any other source
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                SETTLEMENT_PAYMENT_NAME,
                SETTLEMENT_AMOUNT,
                SETTLEMENT_DATE_TRANSACTION
        };

        // Define the query parameters (if any)
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = SETTLEMENT_PAYMENT_NAME;

        // Execute the query
        Cursor cursor = db.query(
                INVOICE_SETTLEMENT_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                groupBy,
                null,
                null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndex(SETTLEMENT_AMOUNT));
                    String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                    Date transactionDate = parseDate(transactionDateString); // Parse the date string to a Date object

                    PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                    paymentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here (e.g., logging, error handling)
        } finally {
            // Close the Cursor and the database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return paymentItems;
    }


    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private double getTotalSaleAmountperday(Date curDay) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Set the start and end date for the selected day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1); // Add one day to include all transactions on the selected day
        Date endDate = calendar.getTime();


        // Query the database to calculate the total sale amount for the specified day
        Cursor cursor = db.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ?", new String[]{formatDateTimes(curDay)});

        double totalAmount = 0.0;

        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return totalAmount;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                ContextCompat.getSystemService(this, ConnectivityManager.class);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public void logout() {
        MssqlDataSync mssqlDataSync = new MssqlDataSync();

        try {
            if (isNetworkAvailable()) {
                SharedPreferences preferences = getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);

                // Retrieve values from SharedPreferences (or use defaults if not set)
                String _user = preferences.getString("_user", null);
                String _pass = preferences.getString("_pass", null);
                String _DB = preferences.getString("_DB", null);
                String _server = preferences.getString("_server", null);

                // Run server check asynchronously
                mDatabaseHelper.isServerReachableAsync(_server, isReachable -> {
                    try {
                        if (!isReachable) {
                            Toast.makeText(this, "Server is offline. Sync skipped.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("LogoutSyncok", "Database  logout");
                            // Perform sync operations
                            mssqlDataSync.syncTransactionsFromSQLiteToMSSQL(this);
                            mssqlDataSync.syncTransactionHeaderFromMSSQLToSQLite(this);
                            mssqlDataSync.syncInvoiceSettlementFromMSSQLToSQLite(this);
                            mssqlDataSync.syncCountingReportDataFromSQLiteToMSSQL(this);
                            mssqlDataSync.syncCashReportDataFromSQLiteToMSSQL(this);
                            mssqlDataSync.syncFinancialReportDataFromSQLiteToMSSQL(this);
                        }
                    } catch (SQLException e) {
                        Log.e("LogoutSyncError", "Database error during logout", e);
                        Toast.makeText(this, "Database error. Proceeding with logout.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("LogoutSyncError", "Unexpected error during logout", e);
                        Toast.makeText(this, "Unexpected error occurred. Proceeding with logout.", Toast.LENGTH_SHORT).show();
                    } finally {
                        // Ensure logout happens after sync or error handling
                        clearSharedPreferences();
                        navigateToLogin();
                    }
                });

            } else {
                Toast.makeText(this, "No network connection. Sync skipped.", Toast.LENGTH_SHORT).show();
                clearSharedPreferences();
                navigateToLogin();
            }
        } catch (Exception e) {
            Log.e("LogoutError", "Unexpected error during logout", e);
            Toast.makeText(this, "Unexpected error occurred. Proceeding with logout.", Toast.LENGTH_SHORT).show();
            clearSharedPreferences();
            navigateToLogin();
        }
    }
    private void clearSharedPreferences() {
        SharedPreferences sharedPrefs = getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();

        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        if (database != null) {
            database.close();
        }
    }
}
