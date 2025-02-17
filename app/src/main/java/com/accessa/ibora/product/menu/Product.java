package com.accessa.ibora.product.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.Help.Help;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.product.SubCategory.SubCategoryFragment;
import com.accessa.ibora.product.couponcode.CouponFragment;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.MenuFragment.OnMenufragListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.Department.DepartmentFragment;
import com.accessa.ibora.product.Discount.DiscountFragment;
import com.accessa.ibora.product.SubDepartment.SubDepartmentFragment;
import com.accessa.ibora.product.Vendor.VendorFragment;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.FirstFragment;
import com.accessa.ibora.product.options.OptionsFragment;
import com.accessa.ibora.product.supplements.SupplementsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Product extends AppCompatActivity implements MenuFragment.OnMenufragListener {
    private MaterialToolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private String cashorId;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences usersharedPreferences,AccessLevelsharedPreferences;
    private TextView shopName;
    private String ShopName;
    private DatabaseHelper mDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.ac_main);
        mDatabaseHelper = new DatabaseHelper(this);
        usersharedPreferences = this.getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
        AccessLevelsharedPreferences = this.getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        ShopName = sharedPreferences.getString("ShopName", null); // Retrieve company name

        //toolbar
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(R.string.Items);
        setSupportActionBar(toolbar);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Get the header view from the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Find the TextView within the header view
        name = headerView.findViewById(R.id.name);
        CashorId = headerView.findViewById(R.id.CashorId);
        shopName = headerView.findViewById(R.id.Company_name);


        // Set the user ID and name in the TextViews
        CashorId.setText(cashorId);
        name.setText(cashorName);
        shopName.setText(ShopName);

        // Get the intent extra
        String fragmentKey = getIntent().getStringExtra("fragment");

        // Check if the intent contains the desired fragment key
        if (fragmentKey != null && fragmentKey.equals("Category_fragment")) {
            Fragment newFragment = new CategoryFragment();
            setToolbarTitle(getString(R.string.category)); // Set the toolbar title

            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Item_fragment")) {
            Fragment newFragment = new FirstFragment();
            setToolbarTitle(getString(R.string.Items)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Dept_fragment")) {
            Fragment newFragment = new DepartmentFragment();
            setToolbarTitle(getString(R.string.Department)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("SUBDept_fragment")) {
            Fragment newFragment = new SubDepartmentFragment();
            setToolbarTitle(getString(R.string.SubDept)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Vend_fragment")) {
            Fragment newFragment = new VendorFragment();
            setToolbarTitle(getString(R.string.vendor)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Discount_fragment")) {
            Fragment newFragment = new DiscountFragment();
            setToolbarTitle(getString(R.string.discount)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Option_fragment")) {
            Fragment newFragment = new OptionsFragment();
            setToolbarTitle(getString(R.string.Options)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("coupon_fragment")) {
            Fragment newFragment = new CouponFragment();
            setToolbarTitle(getString(R.string.couponcode)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Supplement_fragment")) {
            Fragment newFragment = new SupplementsFragment();
            setToolbarTitle(getString(R.string.Supplements)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("SuB_Category_fragment")) {
            Fragment newFragment = new SubCategoryFragment();
            setToolbarTitle(getString(R.string.SubCategory)); // Set the toolbar title
            replaceFragment(newFragment);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("drawer24", "drawer24");

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                int levelNumber = Integer.parseInt(cashorlevel); // Get the user's level

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
                            editor.apply();

                            Intent intent = new Intent(Product.this, MainActivity.class);
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
                        editor.apply();

                        Intent intent = new Intent(Product.this, MainActivity.class);
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
                            Intent intent = new Intent(Product.this, ReceiptActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Product.this, ReceiptActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipt", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Shift) {
                    String Activity="shift_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Product.this, SalesReportActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Product.this, SalesReportActivity.class);
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
                            Intent intent = new Intent(Product.this, Product.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Product.this, Product.class);
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
                            Intent intent = new Intent(Product.this, SettingsDashboard.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Product.this, SettingsDashboard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Settings", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    String Activity="help_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Product.this, Help.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Product.this, Help.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Help", Toast.LENGTH_SHORT).show();
                    }


                } else if (id == R.id.nav_Admin) {
                    String Activity="admin_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Product.this, AdminActivity.class);
                            startActivity(intent);
                        });
                    }
                    // Check permission for Receipts
                    else if ((!canHigherAccessReceipt && canAccessReceipt) || levelNumber==7 )  {
                        Intent intent = new Intent(Product.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Admin", Toast.LENGTH_SHORT).show();
                    }

                }

                return true; // Indicate that the event was handled
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
