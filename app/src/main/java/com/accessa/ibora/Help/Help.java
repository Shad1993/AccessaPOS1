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
        AccessLevelsharedPreferences = getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

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
                            Intent intent = new Intent(Help.this, MainActivity.class);
                            startActivity(intent);
                        });
                        // Check permission for Sales
                    } else if (!canHigherAccessReceipt && canAccessReceipt) {
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

                    String Activity="receipts_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Help.this, ReceiptActivity.class);
                            startActivity(intent);
                        });
                        // Check permission for Sales
                    } else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Help.this, ReceiptActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Receipts", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Shift) {

                    String Activity="shift_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent shiftIntent = new Intent(Help.this, SalesReportActivity.class);
                            startActivity(shiftIntent);
                        });
                        // Check permission for Sales
                    } else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent shiftIntent = new Intent(Help.this, SalesReportActivity.class);
                        startActivity(shiftIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Shift", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Items) {
                    String Activity="Items_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Help.this, Product.class);
                            startActivity(intent);
                        });
                        // Check permission for Sales
                    } else if (!canHigherAccessReceipt && canAccessReceipt) {
                        Intent intent = new Intent(Help.this, Product.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Access Denied: Items", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.Settings) {
                    // Check permission for Settings

                    String Activity="settings_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Help.this, SettingsDashboard.class);
                            startActivity(intent);
                        });
                        // Check permission for Sales
                    } else if (!canHigherAccessReceipt && canAccessReceipt) {
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
                    String Activity="admin_";

                    boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                    boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                    if (canHigherAccessReceipt ) {
                        showPinDialog(Activity, () -> {
                            Intent intent = new Intent(Help.this, AdminActivity.class);
                            startActivity(intent);;
                        });
                        // Check permission for Sales
                    }  else if ((!canHigherAccessReceipt && canAccessReceipt) || levelNumber==7 )  {
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
