package com.accessa.ibora.Help;

import static com.accessa.ibora.Constants.DB_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_DATE_TRANSACTION;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_PAYMENT_NAME;

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
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.login.login;
import com.accessa.ibora.printer.PrintReport;
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

public class Help extends AppCompatActivity {
    private String cashorId;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private List<PaymentItem> paymentItems; // Define the paymentItems list
    private SharedPreferences sharedPreferences,usersharedPreferences;
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


        setContentView(R.layout.activity_help);


        TotalSaleAmountTextView = findViewById(R.id.totalSaleAmountTextView);




        usersharedPreferences = getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name

        //toolbar
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(R.string.Help);
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

                int levelNumber = Integer.parseInt(cashorlevel); // Adjust this based on the user's level

                if (id == R.id.Sales) {
                    // Check permission for Sales
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "sales_", levelNumber)) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        // Set "roomnum" to 1
                        editor.putInt("roomnum", 1);
                        editor.putInt("room_id", 1);
                        editor.putString("table_id", "0");
                        editor.putString("table_num", "0");
                        editor.apply(); // Commit the changes
                        Intent intent = new Intent(Help.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Sales", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Receipts) {
                    // Check permission for Receipts
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "receipts_", levelNumber)) {
                        Intent intent = new Intent(Help.this, ReceiptActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipts", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Shift) {
                    // Check permission for Shift
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "shift_", levelNumber)) {
                        Intent shiftIntent = new Intent(Help.this, SalesReportActivity.class);
                        startActivity(shiftIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Shift", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Items) {
                    // Check permission for Items
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "Items_", levelNumber)) {
                        Intent intent = new Intent(Help.this, Product.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Settings) {
                    // Check permission for Settings
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "settings_", levelNumber)) {
                        Intent intent = new Intent(Help.this, SettingsDashboard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Settings", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    Intent intent = new Intent(Help.this, Help.class);
                    startActivity(intent);
                } else if (id == R.id.nav_Admin) {
                    // Check permission for Admin
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "admin_", levelNumber)) {
                        Intent intent = new Intent(Help.this, AdminActivity.class);
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






    }




    private void logout() {
        MssqlDataSync mssqlDataSync = new MssqlDataSync();
        mssqlDataSync.syncTransactionsFromSQLiteToMSSQL(this);
        mssqlDataSync.syncTransactionHeaderFromMSSQLToSQLite(this);
        mssqlDataSync.syncInvoiceSettlementFromMSSQLToSQLite(this);
        mssqlDataSync.syncCountingReportDataFromSQLiteToMSSQL(this);
        mssqlDataSync.syncCashReportDataFromSQLiteToMSSQL(this);
        mssqlDataSync.syncFinancialReportDataFromSQLiteToMSSQL(this);
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
