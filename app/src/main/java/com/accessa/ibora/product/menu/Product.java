package com.accessa.ibora.product.menu;

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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.Department.DepartmentFragment;
import com.accessa.ibora.product.SubDepartment.SubDepartmentFragment;
import com.accessa.ibora.product.Vendor.VendorFragment;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.FirstFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Product extends FragmentActivity implements MenuFragment.OnMenufragListener {

    private boolean doubleBackToExitPressedOnce = false;
    private String cashorId;
    private String cashorName;
    private TextView name;
    private TextView CashorId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.ac_main);

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);


        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level



        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Get the header view from the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Find the TextView within the header view
        name = headerView.findViewById(R.id.name);
        CashorId = headerView.findViewById(R.id.CashorId);


        // Set the user ID and name in the TextViews
        CashorId.setText(cashorId);
        name.setText(cashorName);


        // Get the intent extra
        String fragmentKey = getIntent().getStringExtra("fragment");

        // Check if the intent contains the desired fragment key
        if (fragmentKey != null && fragmentKey.equals("Category_fragment")) {
            Fragment newFragment = new CategoryFragment();
            // create a FragmentManager
            FragmentManager fm = getSupportFragmentManager();

            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }else if (fragmentKey != null && fragmentKey.equals("Item_fragment")) {
            Fragment newFragment = new FirstFragment();
            // create a FragmentManager
            FragmentManager fm = getSupportFragmentManager();

            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }else if (fragmentKey != null && fragmentKey.equals("Dept_fragment")) {
            Fragment newFragment = new DepartmentFragment();
            // create a FragmentManager
            FragmentManager fm = getSupportFragmentManager();

            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }else if (fragmentKey != null && fragmentKey.equals("SUBDept_fragment")) {
            Fragment newFragment = new SubDepartmentFragment();
            // create a FragmentManager
            FragmentManager fm = getSupportFragmentManager();

            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }else if (fragmentKey != null && fragmentKey.equals("Vend_fragment")) {
            Fragment newFragment = new VendorFragment();
            // create a FragmentManager
            FragmentManager fm = getSupportFragmentManager();

            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
            fragmentTransaction.addToBackStack(newFragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
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

                if(id==R.id.Sales){
                    Toast.makeText(getApplicationContext(), "Sales is Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Product.this, MainActivity.class);
                    startActivity(intent);
                } else if (id==R.id.Receipts) {
                    Toast.makeText(getApplicationContext(), "Receipts is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.Shift) {
                    Toast.makeText(getApplicationContext(), "Shift is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id == R.id.Items) {
                    Toast.makeText(getApplicationContext(), "Items is Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Product.this, Product.class);
                    startActivity(intent);
                    return true;
                }else if (id==R.id.Settings) {
                    Toast.makeText(getApplicationContext(), "settings is Clicked",Toast.LENGTH_SHORT).show();
                }
                else if (id==R.id.nav_logout) {
                    Toast.makeText(getApplicationContext(), "login is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.Help) {
                    Toast.makeText(getApplicationContext(), "Help is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.nav_Admin) {
                    Toast.makeText(getApplicationContext(), "Admin is Clicked",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
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
}
