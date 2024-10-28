package com.accessa.ibora.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.Buyer.buyerFragment;
import com.accessa.ibora.Help.Help;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.Settings.Language.SelectLanguageFragment;
import com.accessa.ibora.Settings.PaymentFragment.PaymentFragment;
import com.accessa.ibora.Settings.PrinterSetup.PrinterSetUpFragment;
import com.accessa.ibora.Settings.QRMethods.QRSettingsFragment;
import com.accessa.ibora.Settings.Rooms.RoomsFragment;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.Department.DepartmentFragment;
import com.accessa.ibora.product.Discount.DiscountFragment;
import com.accessa.ibora.product.SubDepartment.SubDepartmentFragment;
import com.accessa.ibora.product.Vendor.VendorFragment;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.FirstFragment;
import com.accessa.ibora.product.menu.BodyActivity;
import com.accessa.ibora.product.menu.BodyFragment;
import com.accessa.ibora.Settings.SettingsMenuFragment.OnMenufragListener;
import com.accessa.ibora.product.menu.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class SettingsDashboard extends AppCompatActivity implements OnMenufragListener {
    private MaterialToolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private String cashorId;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences sharedPreferences,usersharedPreferences;
    private TextView CompanyName;
    private String Company_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.settings_dashboard);
        mDatabaseHelper = new DatabaseHelper(this);

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        usersharedPreferences = getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Company_name = sharedPreferences.getString("ShopName", null); // Retrieve company name

        //toolbar
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(R.string.Settings);
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
        CompanyName.setText(Company_name);

        // Get the intent extra
        String fragmentKey = getIntent().getStringExtra("fragment");

        // Check if the intent contains the desired fragment key
        if (fragmentKey != null && fragmentKey.equals("Qr_fragment")) {
            Fragment newFragment = new QRSettingsFragment();
            setToolbarTitle(getString(R.string.QR)); // Set the toolbar title

            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Pay_fragment")) {
            Fragment newFragment = new PaymentFragment();
            setToolbarTitle(getString(R.string.PaymentMethods)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("buyer_fragment")) {
            Fragment newFragment = new buyerFragment();
            setToolbarTitle(getString(R.string.PaymentMethods)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Rooms_fragment")) {
            Fragment newFragment = new RoomsFragment();
            setToolbarTitle(getString(R.string.Roomstable)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Printer_fragment")) {
            Fragment newFragment = new PrinterSetUpFragment();
            setToolbarTitle(getString(R.string.PrinterSettings)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("language_fragment")) {
            Fragment newFragment = new SelectLanguageFragment();
            setToolbarTitle(getString(R.string.Language)); // Set the toolbar title
            replaceFragment(newFragment);
        }
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

                // Assuming you have a way to determine the user's access level
                int levelNumber = Integer.parseInt(cashorlevel); // Adjust this based on the user's actual level

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
                        Intent intent = new Intent(SettingsDashboard.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Sales", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Receipts) {
                    // Check permission for Receipts
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "Receipts_", levelNumber)) {
                        Intent intent = new Intent(SettingsDashboard.this, ReceiptActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipts", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Shift) {
                    // Check permission for Shift
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "shift_", levelNumber)) {
                        Intent intent = new Intent(SettingsDashboard.this, SalesReportActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Shift", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Items) {
                    // Check permission for Items
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "Items_", levelNumber)) {
                        Intent intent = new Intent(SettingsDashboard.this, Product.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Settings) {
                    // Check permission for Settings
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "settings_", levelNumber)) {
                        Intent intent = new Intent(SettingsDashboard.this, SettingsDashboard.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Settings", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    // Check permission for Help
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "help_", levelNumber)) {
                        Intent intent = new Intent(SettingsDashboard.this, Help.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Help", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.nav_Admin) {
                    // Check permission for Admin
                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "admin_", levelNumber)) {
                        Intent intent = new Intent(SettingsDashboard.this, AdminActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Admin", Toast.LENGTH_SHORT).show();
                    }
                }

                return true; // Indicate that the event was handled
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

    private void replaceFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.bodyFragment, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
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
