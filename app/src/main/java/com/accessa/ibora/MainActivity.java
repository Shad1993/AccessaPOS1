package com.accessa.ibora;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.Menu;
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
import com.accessa.ibora.CustomerLcd.TextDisplay;
import com.accessa.ibora.QR.QRActivity;
import com.accessa.ibora.QR.QRFragment;
import com.accessa.ibora.Settings.SettingsDashboard;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.hardware.display.DisplayManager;
import android.view.Display;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity implements SalesFragment.ItemAddedListener,CustomerLcdFragment.TicketClearedListener, ModifyItemDialogFragment.ItemClearedListener, QRFragment.DataPassListener {
    private boolean doubleBackToExitPressedOnce = false;
    private TextDisplay customPresentation;
    private TextView name;
    private TextView CashorId;
    private ItemAdapter mAdapter;
    private TextView emptyView;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE = 1001;
    private DatabaseHelper mDatabaseHelper;
    private static IWoyouService woyouService;
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
    public void onDataPass(String name, String id, String QR) {
        // Handle the passed data here
        // You can start the QRActivity or perform any other action

      displayQROnLCD(QR, name);
    }
    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);

            // Call the method to display on the LCD here
            displayOnLCD();
        }
    };
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Check if the selected menu item belongs to the TicketFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (currentFragment instanceof TicketFragment) {
            TicketFragment ticketFragment = (TicketFragment) currentFragment;
            return ticketFragment.onOptionsItemSelected(item);
        }

        // Handle other menu items as needed
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TicketFragment ticketFragment1=new TicketFragment();
        ticketFragment1.setHasOptionsMenu(true);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
        // remove onscreen Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);





        Intent intent1 = new Intent();
        intent1.setPackage("woyou.aidlservice.jiuiv5");
        intent1.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent1, connService, Context.BIND_AUTO_CREATE);

        instance = this;
        mDatabaseHelper = new DatabaseHelper(this);
        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Company_name = sharedPreferences.getString("CompanyName", null); // Retrieve company name


        // Check if the permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Permission has not been granted, request it
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE);
        } else {
            DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = displayManager.getDisplays();

            // Display the custom presentation on the second screen
            if (displays.length > 1) {
                Display secondScreen = displays[1]; // Assuming the second screen is at index 1

                customPresentation = new TextDisplay(this, secondScreen);

                // Update the window type to allow overlay on the second screen
                WindowManager.LayoutParams params = customPresentation.getWindow().getAttributes();
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                customPresentation.getWindow().setAttributes(params);

                customPresentation.show();
        }


        }



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
                    Intent intent = new Intent(MainActivity.this, SettingsDashboard.class);
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
    public void logout() {
        // Perform any necessary cleanup or logout actions here
        // For example, you can clear session data, close database connections, etc.
        // Create an editor to modify the preferences
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE) {
            // Check if the permission has been granted or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                // Permission granted, continue with your code
                DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
                Display[] displays = displayManager.getDisplays();

                // Display the custom presentation on the second screen
                if (displays.length > 1) {
                    Display secondScreen = displays[1]; // Assuming the second screen is at index 1

                    customPresentation = new TextDisplay(this, secondScreen);

                    // Update the window type to allow overlay on the second screen
                    WindowManager.LayoutParams params = customPresentation.getWindow().getAttributes();
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    customPresentation.getWindow().setAttributes(params);

                    customPresentation.show();
                } else {
                    // Permission not granted, handle the situation or show an error message
                    Toast.makeText(instance, "No Second screen", Toast.LENGTH_SHORT).show();
                }
            }
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
                CustomerLcd instance = new CustomerLcd();
                displayOnLCD();
            }
        }
    }

    public  void displayOnLCD() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(transactionIdInProgress);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

               double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                double  TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);

                woyouService.sendLCDDoubleString("Total: Rs" + formattedTotalAmount, "Tax: " + formattedTaxAmount, null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
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

                customerLcdFragment.displayOnLCD();
            }
        }
    }

    @Override
    public  void onItemDeleted() {
        CustomerLcdFragment customerLcdFragment = (CustomerLcdFragment) getSupportFragmentManager().findFragmentById(R.id.customerDisplay_fragment);
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
       // CustomerLcdFragment customerLcdFragment = (CustomerLcdFragment) getSupportFragmentManager().findFragmentById(R.id.customerDisplay_fragment);
        if (ticketFragment != null) {
            double totalAmount = ModifyItemDialogFragment.calculateTotalAmount();
            double taxTotalAmount = ModifyItemDialogFragment.calculateTotalTax();
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);
            customerLcdFragment.displayOnLCD();

        }


    }
    @Override
    public void onAmountModified() {
        CustomerLcdFragment customerLcdFragment = (CustomerLcdFragment) getSupportFragmentManager().findFragmentById(R.id.customerDisplay_fragment);
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = ModifyItemDialogFragment.calculateTotalAmount();
            double taxTotalAmount = ModifyItemDialogFragment.calculateTotalTax();
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);
            customerLcdFragment.displayOnLCD();

        }

    } private void displayQROnLCD(String code, String name) {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            int fontSize = 10; // Set your desired font size
            int textColor = Color.WHITE; // Set your desired text color
            Typeface typeface = Typeface.DEFAULT; // Set your desired font typeface

            // Generate QR code bitmap
            int qrCodeSize = 50; // Set your desired QR code size
            BitMatrix qrCodeMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
            int qrCodeWidth = qrCodeMatrix.getWidth();
            int qrCodeHeight = qrCodeMatrix.getHeight();

            Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < qrCodeWidth; x++) {
                for (int y = 0; y < qrCodeHeight; y++) {
                    qrCodeBitmap.setPixel(x, y, qrCodeMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Generate text bitmap
            Paint textPaint = new Paint();
            textPaint.setTextSize(fontSize);
            textPaint.setColor(textColor);
            textPaint.setTypeface(typeface);

            Rect textBounds = new Rect();
            textPaint.getTextBounds(name, 0, name.length(), textBounds);
            int textWidth = textBounds.width();
            int textHeight = textBounds.height();

            Bitmap textBitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
            Canvas textCanvas = new Canvas(textBitmap);
            textCanvas.drawText(name, 0, textHeight, textPaint);

            // Create composite bitmap
            int compositeWidth = qrCodeWidth + textWidth;
            int compositeHeight = Math.max(qrCodeHeight, textHeight);

            Bitmap compositeBitmap = Bitmap.createBitmap(compositeWidth, compositeHeight, Bitmap.Config.ARGB_8888);
            Canvas compositeCanvas = new Canvas(compositeBitmap);
            compositeCanvas.drawColor(Color.BLACK);

            compositeCanvas.drawBitmap(qrCodeBitmap, 0, (compositeHeight - qrCodeHeight) / 2, null);
            compositeCanvas.drawBitmap(textBitmap, qrCodeWidth, (compositeHeight - textHeight) / 2, null);

            woyouService.sendLCDBitmap(compositeBitmap, null);
        } catch (RemoteException | WriterException e) {
            e.printStackTrace();
        }

    }

    public  void onTransationCompleted() {

        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = 0.0;
            double taxTotalAmount = 0.0;
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);
           displayOnLCD();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (customPresentation != null) {
            customPresentation.dismiss();
            customPresentation = null;
        }
    }
}
