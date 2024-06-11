package com.accessa.ibora;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.accessa.ibora.POP.CancelPaymentPOPDialogFragment;
import com.accessa.ibora.QR.QRFragment;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.login.login;
import com.accessa.ibora.printer.externalprinterlibrary2.Kitchen.SendNoteToKitchenActivity;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.ItemAdapter;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.sales.RoomsAndTable.RoomsFragment;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.Tables.TablesFragment;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.accessa.ibora.sales.ticket.ModifyItemDialogFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import android.hardware.display.DisplayManager;
import android.view.Display;
import android.widget.VideoView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity  implements SalesFragment.ItemAddedListener,CustomerLcdFragment.TicketClearedListener, ModifyItemDialogFragment.ItemClearedListener, QRFragment.DataPassListener, validateticketDialogFragment.DataPassListener , RoomsFragment.OnRoomUpdateListener,TablesFragment.OnTableClickListener, TablesFragment.OnReloadFragmentListener {
    private boolean doubleBackToExitPressedOnce = false;
    private TextDisplay customPresentation;
    private String tableid;
    private int roomid;
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
    private List<String> data ;
    private String cashorId;
    private String cashorName;
    private TextView CompanyName;
    private String Shopname,PosNum;
    private static final String POSNumber="posNumber";
    private static MainActivity instance;
    private SharedPreferences sharedPreferences;
    private String transactionIdInProgress; // Transaction ID for "InProgress" status
    private TicketFragment ticketFragment;
    private CustomerLcdFragment customerLcdFragment;
    private int transactionCounter = 1;
    private String actualdate;

    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private BroadcastReceiver cancelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String popreqid = intent.getStringExtra("popreqid");
            String key = intent.getStringExtra("key");
            String IV = intent.getStringExtra("IV");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Show the CancelPaymentPOPDialogFragment after a short delay
                    CancelPaymentPOPDialogFragment dialogFragment = CancelPaymentPOPDialogFragment.newInstance(popreqid, key, IV);
                    dialogFragment.show(getSupportFragmentManager(), "CancelPaymentDialog");
                }
            }, 100); // Adjust the delay time as needed
        }
    };

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TicketFragment ticketFragment1=new TicketFragment();
        ticketFragment1.setHasOptionsMenu(true);
        double cashReturn = getIntent().getDoubleExtra("cash_return_key", 0.0); // Replace 0.0 with a default value if needed
if (cashReturn != 0.0) {
    // Display cash return amount in a pop-up dialog
    // Create a layout inflater to inflate a custom layout for the dialog
    LayoutInflater inflater = getLayoutInflater();
    View dialogLayout = inflater.inflate(R.layout.custom_dialog_cashreturn, null);

// Find the ImageView in the custom layout to set the GIF animation
    ImageView gifImageView = dialogLayout.findViewById(R.id.gifImageView);
// Set the GIF animation to the ImageView
    Glide.with(this).load(R.drawable.cashreturn).into(gifImageView); // Replace loading_animation with your actual GIF resource

// Create the AlertDialog.Builder with the custom layout
    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
    builder.setView(dialogLayout);

// Set title and message as before
    builder.setTitle("Cash Return")
            .setMessage("Cash Return Amount:Rs " + cashReturn)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Positive button click
                    dialog.dismiss(); // Close the dialog
                }
            })
            .show();

}
        // Initialize SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);

        roomid = Integer.parseInt(String.valueOf(preferences.getInt("roomnum", 0)));
        tableid = String.valueOf(preferences.getString("table_id", "0"));
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
        // remove onscreen Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Retrieve the value from shared preferences

        sharedPreferences = this.getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);


        // Check the value and set the content view accordingly
        if ("mobile".equalsIgnoreCase(deviceType)) {
            setContentView(R.layout.main_activity_mini_pos);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment salesFragment = fragmentManager.findFragmentById(R.id.sales_fragment);

            if (salesFragment != null) {

                fragmentManager.beginTransaction().hide(salesFragment).commit();

            }
        } else if ("sunmiT2".equalsIgnoreCase(deviceType)) {
            setContentView(R.layout.activity_main);
        }



        showSecondaryScreen();

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
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name


        SharedPreferences shardPreference = getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);


        actualdate = mDatabaseHelper.getCurrentDate();


        // Register the BroadcastReceiver to receive the broadcast
        registerReceiver(cancelReceiver, new IntentFilter("com.accessa.ibora.CANCEL_ACTION"));


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
        CompanyName.setText(Shopname);

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
                    Intent intent = new Intent(MainActivity.this, ReceiptActivity.class);
                    startActivity(intent);
                } else if (id == R.id.Shift) {
                    Intent intent = new Intent(MainActivity.this, SalesReportActivity.class);
                    startActivity(intent);
                    return true;
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
                    Intent intent = new Intent(MainActivity.this, SendNoteToKitchenActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_Admin) {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                    return true;
                }
                return true;
            }
        });

    }

    private void showSecondaryScreen() {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();

        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            SeconScreenDisplay secondaryDisplay = new SeconScreenDisplay(this, presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();

            // Example: Displaying a video
            VideoView videoView = secondaryDisplay.findViewById(R.id.presentation_video);
            videoView.setVisibility(View.VISIBLE);
            String videoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/welcome.mp4";
            videoView.setVideoPath(videoPath);
            videoView.start();

            // Example: Displaying text
            TextView textView = secondaryDisplay.findViewById(R.id.presentation_text);
            textView.setVisibility(View.VISIBLE);
            String text = "Welcome !";
            textView.setText(text);
        } else {
            // Secondary screen not found or not supported

            displayOnLCd();
        }
    }
    public  void displayOnLCd() {
        if (woyouService == null) {

            return;
        }

        try {


            woyouService.sendLCDDoubleString("Welcome" , "Back " , null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private Display getPresentationDisplay() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                return display;
            }
        }
        return null;
    }

    public static MainActivity getInstance() {
        return instance;
    }
    public void logout() {

        SharedPreferences sharedPrefs = this.getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPrefs.edit();
        editor1.clear();
        editor1.apply();
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
                    if (!isFinishing()) {
                        // Permission not granted, handle the situation or show an error message
                        Toast.makeText(instance, "No Second screen", Toast.LENGTH_SHORT).show();
                    }
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
        if (!isFinishing()) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
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



    @Override
    public void onItemAdded(String roomid, String tableid) {

        // Refresh the TicketFragment when an item is added in the SalesFragment
        SalesFragment salesFragment = (SalesFragment) getSupportFragmentManager().findFragmentById(R.id.sales_fragment);
        if (salesFragment != null) {
            double totalAmount = salesFragment.calculateTotalAmount();
            double TaxtotalAmount = salesFragment.calculateTotalTax();
            TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
            if (ticketFragment != null) {
                ticketFragment.refreshDatatable(totalAmount, TaxtotalAmount, String.valueOf(roomid),tableid);
               ticketFragment.updateheadertable(totalAmount,TaxtotalAmount, String.valueOf(roomid), tableid);
                CustomerLcd instance = new CustomerLcd();
                displayOnLCD();
            }
        }
    }

    public  void displayOnLCD() {
        if (woyouService == null) {

            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
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
            double taxTotalAmount = customerLcdFragment.calculateTotalTax(roomid,tableid);
            TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
            if (ticketFragment != null) {
                ticketFragment.refreshData(totalAmount, taxTotalAmount);
                ticketFragment.updateheader(totalAmount, taxTotalAmount);

                customerLcdFragment.displayOnLCD();
            }
        }
    }

    public void onItemDeleted() {
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = ModifyItemDialogFragment.calculateTotalAmount(String.valueOf(roomid),tableid);
            double taxTotalAmount = ModifyItemDialogFragment.calculateTotalTax(roomid,tableid);
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);


                showSecondaryScreen1(data);

        }
    }

    @Override
    public void onAmountModified() {
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            double totalAmount = ModifyItemDialogFragment.calculateTotalAmount(String.valueOf(roomid),tableid);
            double taxTotalAmount = ModifyItemDialogFragment.calculateTotalTax(roomid,tableid);
            ticketFragment.refreshData(totalAmount, taxTotalAmount);
            ticketFragment.updateheader(totalAmount, taxTotalAmount);


                showSecondaryScreen1(data);

        }
    }

    public void showSecondaryScreen1(List<String> data) {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplays();

        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            TransactionDisplay secondaryDisplay = new TransactionDisplay(this, presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();

            // Update the RecyclerView data on the secondary screen
            secondaryDisplay.updateRecyclerViewData(data);
        } else {

            // Secondary screen not found or not supported
            displayOnLCD();
        }
    }

    private Display getPresentationDisplays() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                return display;
            }
        }
        return null;
    }


    private void displayQROnLCD(String code, String name) {
        if (woyouService == null) {

            return;
        }

        try {
            if (name.equals("POP")) {
                int qrCodeSize = 30; // Set your desired QR code size
                BitMatrix qrCodeMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
                int qrCodeWidth = qrCodeMatrix.getWidth();
                int qrCodeHeight = qrCodeMatrix.getHeight();

// Create a new bitmap with height and width swapped to rotate the QR code by 90 degrees
                Bitmap qrCodeRotatedBitmap = Bitmap.createBitmap(qrCodeHeight, qrCodeWidth, Bitmap.Config.ARGB_8888);
                for (int x = 0; x < qrCodeWidth; x++) {
                    for (int y = 0; y < qrCodeHeight; y++) {
                        qrCodeRotatedBitmap.setPixel(y, qrCodeWidth - 1 - x, qrCodeMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

// Calculate the offset to move the QR code upwards
                int offset = -2; // Set your desired offset (use a negative value to move upwards)

// Resize the QR code bitmap to the desired size
                int newQrCodeWidth = 38; // Set your desired width
                int newQrCodeHeight = 38; // Set your desired height
                Bitmap resizedQrCodeBitmap = Bitmap.createScaledBitmap(qrCodeRotatedBitmap, newQrCodeWidth, newQrCodeHeight, false);

// Create a new bitmap with the required height to accommodate the offset and draw the resized QR code
                Bitmap compositeBitmap = Bitmap.createBitmap(newQrCodeWidth, newQrCodeHeight + Math.abs(offset), Bitmap.Config.ARGB_8888);
                Canvas compositeCanvas = new Canvas(compositeBitmap);

// Draw a white background
                compositeCanvas.drawColor(Color.WHITE);

// Draw the resized and rotated QR code on the canvas with the offset
                compositeCanvas.drawBitmap(resizedQrCodeBitmap, 0, offset, null);
                Bitmap qrCodeBitmap = generateQRCode(code, 50, 50); // Adjust the size as needed

                woyouService.sendLCDBitmap(qrCodeBitmap, null);


                // Create an AlertDialog to display the QR code
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("QR Code Popup"); // Set a title for the dialog
                ImageView imageView = new ImageView(this);
                 qrCodeBitmap = generateQRCode(code, 200, 200); // Adjust the size as needed

                imageView.setImageBitmap(qrCodeBitmap);
                builder.setView(imageView);

                // Add a button to close the dialog
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
                    @Override
                    protected void onConnected(SunmiPrinterService service) {

                        int lineWidth = 48; // Adjust this value according to the width of your paper
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {




                                try {

                                        service.printText(compositeBitmap + "\n" , null);
                                        service.printBitmap(compositeBitmap , null);


                                    // Cut the paper
                                    service.cutPaper(null);



                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDisconnected() {
                        // Printer service is disconnected, you cannot print
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(printerSetup.this, "Printer disconnected", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };

            }
            else {
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
}
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
           displayOnLCD();
        }


    }
    private Bitmap generateQRCode(String content, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();
            int[] pixels = new int[matrixWidth * matrixHeight];

            for (int y = 0; y < matrixHeight; y++) {
                for (int x = 0; x < matrixWidth; x++) {
                    pixels[y * matrixWidth + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (customPresentation != null) {
            customPresentation.dismiss();
            customPresentation = null;
        }
        unregisterReceiver(cancelReceiver);
    }

    @Override
    public void onRoomUpdate(int currentId) {

        // Implement the logic to refresh the TablesFragment here
        // Get the TablesFragment instance and update its UI
        TablesFragment tablesFragment = (TablesFragment) getSupportFragmentManager().findFragmentById(R.id.TableFragment);
        if (tablesFragment != null) {
            // Update the UI in TablesFragment
            tablesFragment.updateUIForRoomUpdate(currentId);
        }
    }

    @Override
    public void onTableClicked(String tableId,String roomnum) {
        // Handle the table click event
        // Example: Refresh the TicketFragment
        refreshTicketFragment(tableId,roomnum);
    }

    private void refreshTicketFragment(String tableId, String roomnum) {
        // Refresh the TicketFragment when an item is added in the SalesFragment

            TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentById(R.id.right_container);
            if (ticketFragment != null) {
                ticketFragment.refreshDatatable(calculateTotalAmount(roomnum,tableId), calculateTotalTaxAmount(roomnum,tableId),roomnum,tableId);

                //ticketFragment.updateheader(calculateTotalAmount(roomnum,tableId),1);
                CustomerLcd instance = new CustomerLcd();
                displayOnLCD();
            }

    }
    public double calculateTotalAmount(String roomid,String tableid) {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public double calculateTotalTaxAmount(String roomid,String tableid) {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public void filterAndInsertTable(String newTableName) {
        TablesFragment tablesFragment = (TablesFragment) getSupportFragmentManager().findFragmentById(R.id.TableFragment);

        if (tablesFragment != null) {
            tablesFragment.handleFilterAndInsert(newTableName);
        }
    }
    @Override
    public void onReloadFragment(String tableId, String roomnum) {
        // Reload the other fragment
        SalesFragment salesFragment = (SalesFragment) getSupportFragmentManager().findFragmentById(R.id.sales_fragment);

        if (salesFragment != null) {
            salesFragment.reload(tableId,roomnum); // Define a method like this in YourOtherFragment to handle the reload
        }
    }
}
