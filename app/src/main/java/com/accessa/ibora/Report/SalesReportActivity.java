package com.accessa.ibora.Report;

import static com.accessa.ibora.Constants.DB_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_PAYMENT_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
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
import com.accessa.ibora.Receipt.ReceiptAdapter;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
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
    private SharedPreferences sharedPreferences;
    private MaterialToolbar toolbar;
    private TextView CompanyName;
    private String Shopname;
    private ReceiptAdapter receiptAdapter;
    private SearchView searchView;
    private Spinner dateFilterSpinner;
    private TextView dailyAmountTextView;
    private TextView weeklyAmountTextView;
    private TextView monthlyAmountTextView;
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


        mDatabaseHelper = new DatabaseHelper(this);

        List<PaymentItem> paymentItems = getPaymentItems();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        PaymentAdapter adapter = new PaymentAdapter(paymentItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name

        //toolbar
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(R.string.Receipts);
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
        dailyAmountTextView.setText("Rs " +formatAmount(dailyAmount));

        // Calculate weekly amount
        double weeklyAmount = getWeeklyAmount(currentDate);
        weeklyAmountTextView.setText("Rs " +formatAmount(weeklyAmount));

        // Calculate monthly amount
        double monthlyAmount = getMonthlyAmount(currentDate);
        monthlyAmountTextView.setText("Rs " +formatAmount(monthlyAmount));
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

        // Query the database to get the daily amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_DATE + " >= ? AND " + TRANSACTION_DATE + " < ?" ,new String[]{formatDateTime(startDate), formatDateTime(endDate)});
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

        // Query the database to get the weekly amount

        Cursor cursor = database.rawQuery("SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_DATE + " >= ? AND " + TRANSACTION_DATE + " < ?", new String[]{formatDateTime(startDate), formatDateTime(endDate)});


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

        // Query the database to get the monthly amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + TOTAL_PRICE + ") FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_DATE + " >= ? AND " + TRANSACTION_DATE + " < ?",new String[]{formatDateTime(startDate), formatDateTime(endDate)});
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

    private List<PaymentItem> getPaymentItems() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Retrieve the data from the database or any other source
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT " + SETTLEMENT_PAYMENT_NAME + ", SUM(" + SETTLEMENT_AMOUNT + ") AS totalAmount " +
                "FROM " + INVOICE_SETTLEMENT_TABLE_NAME +
                " GROUP BY " + SETTLEMENT_PAYMENT_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));

                PaymentItem item = new PaymentItem(paymentName, totalAmount);
                paymentItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return paymentItems;
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        if (database != null) {
            database.close();
        }
    }
}
