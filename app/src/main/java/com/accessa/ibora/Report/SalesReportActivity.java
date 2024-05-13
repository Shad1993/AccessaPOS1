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

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Settings.SettingsDashboard;
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
    private SharedPreferences sharedPreferences;
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
   private FloatingActionButton mAddFab;
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
            Log.d("payment", paymentItems.toString());

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
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                if (id == R.id.Sales) {
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    // Set "roomnum" to 1
                    editor.putInt("roomnum", 1);
                    editor.putInt("room_id", 1);
                    editor.putString("table_id", "0");
                    editor.putString("table_num", "0");
                    Intent intent = new Intent(SalesReportActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.Receipts) {
                    Intent intent = new Intent(SalesReportActivity.this, ReceiptActivity.class);
                    startActivity(intent);
                } else if (id == R.id.Shift) {
                    Intent intent = new Intent(SalesReportActivity.this, SalesReportActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.Items) {
                    Intent intent = new Intent(SalesReportActivity.this, Product.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.Settings) {
                    Intent intent = new Intent(SalesReportActivity.this, SettingsDashboard.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    Toast.makeText(getApplicationContext(), "Help is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_Admin) {
                    Intent intent = new Intent(SalesReportActivity.this, AdminActivity.class);
                    startActivity(intent);
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
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        cashierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected cashier from the spinner
                String selectedCashier = parent.getItemAtPosition(position).toString();

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

                // Update the data based on the selected cashier and date range
                updateDataForDateRangeAndCashier(startDate, endDate, selectedCashier);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected, if needed
            }
        });


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

    private void updateDataForDateRangeAndCashier(Date startDate, Date endDate, String selectedCashier) {
        // Format the start and end dates as strings
        String startDateString = formatDateTimes(startDate);
        String endDateString = formatDateTimes(endDate);

        // Query the database to get the filtered payment items and total sale amount for the selected cashier
        List<PaymentItem> filteredPaymentItems = mDatabaseHelper.getFilteredPaymentItemsByCashier(startDateString, endDateString, selectedCashier);
        double totalSaleAmount = mDatabaseHelper.getTotalSaleAmountByCashier(startDateString, endDateString, selectedCashier);

        // Update the RecyclerView adapter with the filtered payment items
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
        } else {
            filteredPaymentItems = mDatabaseHelper.getFilteredPaymentItemsByCashier(startDateString, endDateString, selectedCashier);
            totalSaleAmount = mDatabaseHelper.getTotalSaleAmountByCashier(startDateString, endDateString, selectedCashier);
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

// Query the database to get the daily amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?", new String[]{formattedStartDate, formattedEndDate});

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

// Query the database to get the weekly amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?", new String[]{formattedStartDate, formattedEndDate});

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

// Query the database to get the monthly amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?", new String[]{formattedStartDate, formattedEndDate});


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



    private void logout() {
        // Perform any necessary cleanup or logout actions here
        // For example, you can clear session data, close database connections, etc.
        // Create an editor to modify the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all the stored values
        editor.clear();

        // Apply the changes
        editor.apply();

        // Redirect to the login activity
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish(); // Optional: Finish the current activity to prevent navigating back to it using the back button
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
