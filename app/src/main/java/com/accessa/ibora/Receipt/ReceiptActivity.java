package com.accessa.ibora.Receipt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.Help.Help;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.MainActivityMobile;
import com.accessa.ibora.R;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.Settings.SettingsMenuFragment.OnMenufragListener;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.BodyActivity;
import com.accessa.ibora.product.menu.BodyFragment;
import com.accessa.ibora.product.menu.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class ReceiptActivity extends AppCompatActivity implements OnMenufragListener {
    private MaterialToolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private String cashorId;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private SharedPreferences sharedPreferences;
    private TextView CompanyName;
    private String Shopname;
    private ReceiptAdapter receiptAdapter;
    private SearchView searchView;
    private Spinner dateFilterSpinner;
    private SharedPreferences usersharedPreferences;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.receipt_dashboard);
        mDatabaseHelper = new DatabaseHelper(this);
        usersharedPreferences = this.getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

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

                int levelNumber = Integer.parseInt(cashorlevel); // Retrieve user's level

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
                        // Commit the changes
                        editor.apply();
                        Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Sales", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Receipts) {
                    // Check permission for Receipts
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "receipts_", levelNumber)) {
                        Intent intent = new Intent(ReceiptActivity.this, ReceiptActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipts", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Shift) {
                    // Check permission for Shift
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "shift_", levelNumber)) {
                        Intent intent = new Intent(ReceiptActivity.this, SalesReportActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Shift", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Items) {
                    // Check permission for Items
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "Items_", levelNumber)) {
                        Intent intent = new Intent(ReceiptActivity.this, Product.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Settings) {
                    // Check permission for Settings
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "settings_", levelNumber)) {
                        Intent intent = new Intent(ReceiptActivity.this, SettingsDashboard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Settings", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    // Check permission for Help
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "help_", levelNumber)) {
                        Intent intent = new Intent(ReceiptActivity.this, Help.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Help", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_Admin) {
                    // Check permission for Admin
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "admin_", levelNumber)) {
                        Intent intent = new Intent(ReceiptActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Admin", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });





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
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000); // Wait for 2 seconds before resetting the double back press flag

        // Replace the code below with the intent to navigate to the login screen
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }



    @Override
    public void onMenufrag(String s) {
        BodyFragment fragment1 = (BodyFragment) getSupportFragmentManager().findFragmentById(R.id.bodyFragment);
        if (fragment1 != null && fragment1.isInLayout()) {
            fragment1.setText(s);
        } else {
            Intent intent = new Intent(this, BodyActivity.class);
            intent.putExtra("value", s);
            startActivity(intent);
        }

    }
}
