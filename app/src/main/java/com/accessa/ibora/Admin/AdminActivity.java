package com.accessa.ibora.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accessa.ibora.Admin.CompanyInfo.CompanyInfoFragment;
import com.accessa.ibora.Admin.People.PeopleFragment;
import com.accessa.ibora.Help.Help;
import com.accessa.ibora.MRA.Mra;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Settings.SettingsDashboard;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.Discount.DiscountFragment;
import com.accessa.ibora.product.SubDepartment.SubDepartmentFragment;
import com.accessa.ibora.product.Vendor.VendorFragment;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.product.options.OptionsFragment;
import com.accessa.ibora.product.supplements.SupplementsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity implements AdminMenuFragment.OnMenufragListener {
    private MaterialToolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private String cashorId,Shopname;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private TextView CompanyName;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences usersharedPreferences,AccessLevelsharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.admin_layout);
        mDatabaseHelper = new DatabaseHelper(this);
        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name

        //toolbar
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(R.string.Admin);
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

        // Get the intent extra
        String fragmentKey = getIntent().getStringExtra("fragment");

        // Check if the intent contains the desired fragment key
        if (fragmentKey != null && fragmentKey.equals("Category_fragment")) {
            Fragment newFragment = new CategoryFragment();
            setToolbarTitle(getString(R.string.category)); // Set the toolbar title

            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("people_fragment")) {
            Fragment newFragment = new PeopleFragment();
            setToolbarTitle(getString(R.string.People)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Company_info_fragment")) {
            Fragment newFragment = new CompanyInfoFragment();
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
            setToolbarTitle(getString(R.string.options)); // Set the toolbar title
            replaceFragment(newFragment);
        } else if (fragmentKey != null && fragmentKey.equals("Supplement_fragment")) {
            Fragment newFragment = new SupplementsFragment();
            setToolbarTitle(getString(R.string.Supplements)); // Set the toolbar title
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

                // Initialize SharedPreferences


                 usersharedPreferences = getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                AccessLevelsharedPreferences = getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

                int levelNumber = Integer.parseInt(cashorlevel); // Adjust this based on the user's level

                if (id == R.id.Sales) {
                    String Activity="sales_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                        // Set room details
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("roomnum", 1);
                        editor.putInt("room_id", 1);
                        editor.putString("table_id", "0");
                        editor.putString("table_num", "0");
                        editor.apply();

                        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                        startActivity(intent);
                        });
                    }else if (!canHigherAccessReceipt && canAccessReceipt) {
                        // Set room details
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("roomnum", 1);
                        editor.putInt("room_id", 1);
                        editor.putString("table_id", "0");
                        editor.putString("table_num", "0");
                        editor.apply();

                        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Sales", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Receipts) {
                    String Activity="Receipts_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(AdminActivity.this, ReceiptActivity.class);
                            startActivity(intent);
                        });
                    }else  if (canAccessReceipt && !canHigherAccessReceipt) { // Corrected key
                        Intent intent = new Intent(AdminActivity.this, ReceiptActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipts", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Shift) {
                    String Activity="shift_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);

                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(AdminActivity.this, ReceiptActivity.class);
                            startActivity(intent);
                        });
                    }else  if (canAccessReceipt && !canHigherAccessReceipt) { // Corrected key
                        Intent intent = new Intent(AdminActivity.this, ReceiptActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipts", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == R.id.Items) {
                    String Activity="Items_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);

                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(AdminActivity.this, Product.class);
                            startActivity(intent);
                        });
                    }else  if (canAccessReceipt && !canHigherAccessReceipt) { // Corrected key
                        Intent intent = new Intent(AdminActivity.this, Product.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Settings) {
                    String Activity="settings_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);

                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(AdminActivity.this, SettingsDashboard.class);
                            startActivity(intent);
                        });
                    }else  if (canAccessReceipt && !canHigherAccessReceipt) { // Corrected key
                        Intent intent = new Intent(AdminActivity.this, SettingsDashboard.class);
                        startActivity(intent);
                        return true;
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
                            Intent intent = new Intent(AdminActivity.this, Help.class);
                            startActivity(intent);
                        });
                    }else  if (canAccessReceipt && !canHigherAccessReceipt) { // Corrected key
                        Intent intent = new Intent(AdminActivity.this, Help.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Help", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.nav_Admin) {
                    String Activity="admin_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);

                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                            startActivity(intent);
                        });
                    } else if ((!canHigherAccessReceipt && canAccessReceipt) || levelNumber==7 )  {
                        Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Admin", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
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
        AdminBodyFragment fragment1 = (AdminBodyFragment) getSupportFragmentManager().findFragmentById(R.id.bodyFragment);
        if (fragment1 != null && fragment1.isInLayout()) {
            fragment1.setText(s);
        } else {
            Intent intent = new Intent(this, AdminBodyActivity.class);
            intent.putExtra("value", s);
            startActivity(intent);
        }

    }
}
