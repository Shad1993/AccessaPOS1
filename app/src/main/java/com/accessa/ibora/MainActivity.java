package com.accessa.ibora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.CustomerLcd.CustomerLcd;
import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.ItemAdapter;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.ModifyItemDialogFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SalesFragment.ItemAddedListener,CustomerLcdFragment.TicketClearedListener, ModifyItemDialogFragment.ItemClearedListener {
    private boolean doubleBackToExitPressedOnce = false;

    private TextView name;
    private TextView CashorId;
    private ItemAdapter mAdapter;
    private TextView emptyView;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;

    private DatabaseHelper mDatabaseHelper;
    private TextView cashorNameTextView;
    private TextView cashorIdTextView;
    private String cashorId;
    private String cashorName;
    private TextView CompanyName;
    private String Company_name;
    private static MainActivity instance;
    private SharedPreferences sharedPreferences;
    private String transactionIdInProgress; // Transaction ID for "InProgress" status
    private TicketFragment ticketFragment;
    private CustomerLcdFragment customerLcdFragment;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
        // remove onscreen Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        instance = this;

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Company_name = sharedPreferences.getString("CompanyName", null); // Retrieve company name





        String transactionId;
        String transactionStatus = "Started";
        String transactionSaved = "Saved";

        if (transactionStatus.equals("Started")|| transactionStatus.equals("InProgress") || transactionSaved.equals("Saved")  ) {
            if (transactionIdInProgress == null) {
                transactionIdInProgress = generateNewTransactionId(); // Generate a new transaction ID for "InProgress" status
                // Store the transaction ID in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TRANSACTION_ID_KEY, transactionIdInProgress);
                editor.apply();



            }
            transactionId = transactionIdInProgress;

        } else {
            transactionId = generateNewTransactionId(); // Generate a new transaction ID

        }

        //toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
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

        // Initialize the TicketFragment
        ticketFragment = new TicketFragment();

        // Add the TicketFragment to the right_container
        getSupportFragmentManager().beginTransaction()
                .add(R.id.right_container, ticketFragment)
                .commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
// Get the intent extra
        String fragmentKey = getIntent().getStringExtra("fragment");
        // Check if the intent contains the desired fragment key
        if (fragmentKey != null && fragmentKey.equals("Ticket_fragment")) {
            Fragment newFragment = new CategoryFragment();

            replaceFragment(newFragment);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                if (id == R.id.Sales) {
                    // Already in the sales screen, do nothing
                } else if (id == R.id.Receipts) {
                    Toast.makeText(getApplicationContext(), "Receipts is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.Shift) {
                    Toast.makeText(getApplicationContext(), "Shift is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.Items) {
                    Intent intent = new Intent(MainActivity.this, Product.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.Settings) {
                    Toast.makeText(getApplicationContext(), "Settings is Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, CustomerLcd.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {
                    Toast.makeText(getApplicationContext(), "Help is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_Admin) {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    public static MainActivity getInstance() {
        return instance;
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
    private void replaceFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.TicketFragment, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private String generateNewTransactionId() {
        // Implement your logic to generate a unique transaction ID
        // For example, you can use a combination of timestamp and a random number
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(10000);
        return "TXN-" + timestamp + "-" + random;
    }
    @Override
    public void onItemAdded() {
        // Refresh the TicketFragment when an item is added in the SalesFragment
        SalesFragment salesFragment = (SalesFragment) getSupportFragmentManager().findFragmentById(R.id.sales_fragment);
        if (salesFragment != null) {
            double totalAmount = salesFragment.calculateTotalAmount();
            double TaxtotalAmount = salesFragment.calculateTotalTax();
            TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
            if (ticketFragment != null) {
                ticketFragment.refreshData(totalAmount, TaxtotalAmount);
               ticketFragment.updateheader(totalAmount,TaxtotalAmount);

            }
        }
    }


    @Override
    public void onTransactionCleared() {

        // Refresh the TicketFragment when transaction cleared
        CustomerLcdFragment customerLcdFragment = (CustomerLcdFragment) getSupportFragmentManager().findFragmentById(R.id.customerDisplay_fragment);
        if (customerLcdFragment != null) {
            double totalAmount = customerLcdFragment.calculateTotalAmount();
            double taxTotalAmount = customerLcdFragment.calculateTotalTax();
            TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
            if (ticketFragment != null) {
                ticketFragment.refreshData(totalAmount, taxTotalAmount);
                ticketFragment.updateheader(totalAmount, taxTotalAmount);


            }
        }
    }


    @Override
    public  void onItemDeleted() {
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = ModifyItemDialogFragment.calculateTotalAmount();
            double taxTotalAmount = ModifyItemDialogFragment.calculateTotalTax();
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);
            Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onAmountModified() {
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = ModifyItemDialogFragment.calculateTotalAmount();
            double taxTotalAmount = ModifyItemDialogFragment.calculateTotalTax();
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);

        }

    }
    public  void onTransationCompleted() {
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = 0.0;
            double taxTotalAmount = 0.0;
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);
            Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
        }


    }
}
