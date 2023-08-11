package com.accessa.ibora.Admin;

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

import com.accessa.ibora.Admin.CompanyInfo.CompanyInfoFragment;
import com.accessa.ibora.Admin.People.PeopleFragment;
import com.accessa.ibora.MRA.Mra;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Settings.SettingsDashboard;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.Discount.DiscountFragment;
import com.accessa.ibora.product.SubDepartment.SubDepartmentFragment;
import com.accessa.ibora.product.Vendor.VendorFragment;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.menu.Product;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.admin_layout);

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

                if (id == R.id.Sales) {
                    Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.Receipts) {
                    Intent intent = new Intent(AdminActivity.this, ReceiptActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.Shift) {
                    Toast.makeText(getApplicationContext(), "Shift is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.Items) {
                    Intent intent = new Intent(AdminActivity.this, Product.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.Settings) {
                    Intent intent = new Intent(AdminActivity.this, SettingsDashboard.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    logout();
                    return true;
                } else if (id == R.id.Help) {

                    Intent intent = new Intent(AdminActivity.this, Mra.class);
                    startActivity(intent);
                } else if (id == R.id.nav_Admin) {
                    Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
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
