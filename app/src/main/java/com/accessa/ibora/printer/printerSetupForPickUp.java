package com.accessa.ibora.printer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class printerSetupForPickUp extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private boolean printable ;
    private String printerpartname ;
    private String tableid;
    private SharedPreferences sharedPreferences;
    String shopnum ,type;
    double amount;
    String date ;
    String time;

    private String      splittype;
    private   String newtransactionIdInProgress;
    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount;
    private  String DateCreated,timeCreated;
    private String cashorlevel,ShopName,LogoPath;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;

    private String    OpeningHours;
    private   String      TelNum,compTelNum,compFaxNum;
    private  String  FaxNum;
    private String paymentName,PosNum ;
    private static final String POSNumber="posNumber";



    private double settlementAmount ;


    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {

            int lineWidth = 48; // Adjust this value according to the width of your paper
            runOnUiThread(new Runnable() {
                @Override
                public void run() {



                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper




                    SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    cashierId = sharedPreference.getString("cashorId", null);
                    cashierName = sharedPreference.getString("cashorName", null);
                    cashorlevel = sharedPreference.getString("cashorlevel", null);
                    ShopName = sharedPreference.getString("ShopName", null);


                    SharedPreferences shardPreference = getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                    PosNum = shardPreference.getString(POSNumber, null);

                    // Inflate the custom print layout
                    View customPrintLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.print_layout, null);

                    // Populate the text views with the desired content
                    TextView titleTextView = customPrintLayout.findViewById(R.id.textViewTitle);
                    titleTextView.setText("Cashier Name: " + cashierName + "\n");

                    TextView contentTextView = customPrintLayout.findViewById(R.id.textViewContent);
                    contentTextView.setText("POS Number: "+ PosNum + "\n");

                    // Set up the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(printerSetupForPickUp.this));


                    try {


                        List<PrinterSetupPrefs> printerSetups = mDatabaseHelper.getPrinterSetups(getApplicationContext());

                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);

                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                            int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_COMPANY_NAME);
                            int columnCompanyAddressIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_1);
                            int columnCompanyAddress2Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_2);
                            int columnCompanyAddress3Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_ADR_3);
                            int columnShopNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNAME);
                            int columnShopAddressIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_1);
                            int columnShopAddress2Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_2);
                            int columnShopAddress3Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_3);
                            int columnCompanyVATIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_VAT_NO);
                            int columnCompanyBRNIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_BRN_NO);
                            int columnLogoPathIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Logo);
                            int columnOpeningHoursIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Opening_Hours);
                            int columncompTelNumIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_TEL_NO);
                            int columncompFaxIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Comp_FAX_NO);
                            int columnTelNumIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_TEL_NO);
                            int columnFaxIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_FAX_NO);

                            String companyName = cursorCompany.getString(columnCompanyNameIndex);
                            String CompanyAdress1= cursorCompany.getString(columnCompanyAddressIndex);
                            String CompanyAdress2= cursorCompany.getString(columnCompanyAddress2Index);
                            String CompanyAdress3= cursorCompany.getString(columnCompanyAddress3Index);
                            String shopName = cursorCompany.getString(columnShopNameIndex);
                            String ShopAdress= cursorCompany.getString(columnShopAddressIndex);
                            String ShopAdress2= cursorCompany.getString(columnShopAddress2Index);
                            String ShopAdress3= cursorCompany.getString(columnShopAddress3Index);
                            String CompanyVatNo= cursorCompany.getString(columnCompanyVATIndex);
                            String CompanyBRNNo= cursorCompany.getString(columnCompanyBRNIndex);
                            String inv= "Invoice" + "\n";
                             OpeningHours= cursorCompany.getString(columnOpeningHoursIndex);
                             TelNum= cursorCompany.getString(columnTelNumIndex);
                             FaxNum= cursorCompany.getString(columnFaxIndex);
                            compTelNum= cursorCompany.getString(columncompTelNumIndex);
                            compFaxNum= cursorCompany.getString(columncompFaxIndex);
                            service.setFontSize(24, null);
                            service.setAlignment(1, null);
                            // Print the formatted company name line
                            service.printText(inv, null);

                            LogoPath = cursorCompany.getString(columnLogoPathIndex);
                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if( printerpartname.equals("Logo") && printable) {

                                    printLogoAndReceipt(service, LogoPath, 100, 100);

                                }
                            }
                            // Create the formatted company name line
                            String companyNameLine = companyName + "\n";
                            // Create the formatted companyAddress1Line  line

                            String CompAdd1andAdd2 = CompanyAdress1 + ", " + CompanyAdress2 + "\n";
                            String CompAdd3 = CompanyAdress3 + "\n";
                            String Add1andAdd2 = ShopAdress + ", " + ShopAdress2 + "\n";
                            String shopAdress3 = ShopAdress3+ "\n";
                            String CompVatNo= getString(R.string.Vat) + "  : " + CompanyVatNo + "\n";
                            String CompBRNNo= getString(R.string.BRN) + "  : " + CompanyBRNNo + "\n\n";
                            String shopname=  shopName + "\n";
                            String Tel= "Tel : " + TelNum ;
                            String Fax= "Fax : " + FaxNum ;
                            String compTel= "Tel : " + compTelNum ;
                            String compFax= "Fax : " + compFaxNum ;

                            // Enable bold text
                            byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                            service.sendRAWData(boldOnBytes, null);
                            service.setFontSize(30, null);
                            service.setAlignment(1, null);

                            // Print the formatted company name line
                            service.printText(companyNameLine, null);

                            // Disable bold text
                            byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                            service.sendRAWData(boldOffBytes, null);
                            service.setFontSize(24, null);
                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if (printerpartname.equals("Company Info") && printable) {

                                    // Print the formatted company name line
                                    service.printText(CompAdd1andAdd2, null);
                                    service.printText(CompAdd3, null);
                                    service.printText(compTel + "\n", null);
                                    service.printText(compFax + "\n\n", null);
                                }
                            }
                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if (printerpartname.equals("Shop Info") && printable) {

                                    // Print the formatted company name line
                                    service.printText(shopname, null);
                                    service.printText(Add1andAdd2, null);
                                    service.printText(shopAdress3, null);
                                    service.printText(Tel + "\n", null);
                                    service.printText(Fax + "\n", null);
                                    service.printText(CompVatNo, null);
                                    service.printText(CompBRNNo, null);
                                }
                            }

                            service.setAlignment(0, null);



                        }

                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Cashior name") && printable) {
                                service.printText(titleTextView.getText().toString(), null);
                            }
                        }
                        // Print the custom layout
                        String cashierid= "Cashier Id: " + cashierId ;
                        for (PrinterSetupPrefs setup : printerSetups) {
                            if (printerpartname.equals("Cashier Id") && printable) {
                                service.printText(cashierid + "\n", null);

                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("POS NUM") && printable) {
                                service.printText(contentTextView.getText().toString(), null);
                            }
                        }

                        // Print a line separator

                        String lineSeparator = "=".repeat(lineWidth);

                        service.printText(lineSeparator + "\n", null);



                        // Retrieve the total amount and total tax amount from the transactionheader table


                        String itemprice ="Rs " + String.format("%.2f", amount);

                        // Calculate the padding for the item name
                        int itemNamePadding = lineWidth - itemprice.length() - type.length();


                        // Inside the for loop where you print the data from the RecyclerView



                        String formattedUnitPrice = "Rs " + amount;

                        // Create the formatted item line
                        String QuantityLine = "1" + " X " + formattedUnitPrice ;

                        // Create the formatted item price line
                        String itemPriceLine = type + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                        service.printText(itemPriceLine + "\n" , null);
                        service.printText(QuantityLine + "\n", null);



                        int lineWidths= 41;


                        // Enable bold text and set font size to 30
                        byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(28, null);


                        // Disable bold text and reset font size
                        byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);






                        service.printText(lineSeparator + "\n", null);
                        service.setAlignment(1, null); // Align center
                        // Footer Text
                        String FooterText = getString(R.string.Footer_See_You);
                        String Footer1Text = getString(R.string.Footer_Have);
                        String Footer2Text = getString(R.string.Footer_OpenHours);
                        String Openinghours= Footer2Text + OpeningHours ;





                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("See You Again") && printable) {

                                service.printText(FooterText + "\n", null);
                            }
                        }
                        // Print the centered footer text
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Have a Nice Day") && printable) {

                                service.printText(Footer1Text + "\n", null);
                            }
                        }

                        // Print the centered footer1 text

                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Opening Hours") && printable) {

                                service.printText(Openinghours + "\n", null);
                            }
                        }

                        service.setAlignment(0, null); // Align left
                        // Concatenate the formatted date and time
                        String dateTime = date + " " + time;
                        // Calculate the padding for the item name
                        int DateIDPadding = lineWidth - dateTime.length()          ;

                        // Create the formatted item price line
                        String DateTimeIdLine = dateTime + " ".repeat(Math.max(0, DateIDPadding)) ;

                        // Print  date and time
                        service.printText(DateTimeIdLine + "\n\n", null);





                        // Cut the paper
                        service.cutPaper(null);

                        // Open the cash drawer
                        byte[] openDrawerBytes = new byte[]{0x1B, 0x70, 0x00, 0x32, 0x32};
                        service.sendRAWData(openDrawerBytes, null);
                        // Open the cash drawer


                       String transactionType;



                        // Update the transaction status for all in-progress transactions to "Completed"


                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        // Display a pop-up with a "Thanks" message and a button
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(printerSetupForPickUp.this, MainActivity.class);

                                startActivity(intent);


                            }
                        });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_printer);
        amount = getIntent().getDoubleExtra("amount", 0.0); // Default value is 0.0
        type = getIntent().getStringExtra("type");
         date = getIntent().getStringExtra("date");
        shopnum = getIntent().getStringExtra("shopnum");
         time = getIntent().getStringExtra("time");


        Log.d("amount1", String.valueOf(amount));


        mDatabaseHelper = new DatabaseHelper(this);








        // Initialize the printer service
        boolean result = false;
        try {
            result = InnerPrinterManager.getInstance().bindService(this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            throw new RuntimeException(e);
        }

        if (result) {
           // Toast.makeText(this, "Printer service initialization successful", Toast.LENGTH_SHORT).show();
        } else {
          //  Toast.makeText(this, "Printer service initialization failed", Toast.LENGTH_SHORT).show();
        }
    }



    private void printLogoAndReceipt(SunmiPrinterService service, String LogoPath, int desiredLogoWidth, int desiredLogoHeight) {
        try {
            // Check if the service is connected
            if (service == null) {
                //Toast.makeText(this, "Printer service is not connected", Toast.LENGTH_SHORT).show();
                return;
            }

            // Provide the path to the logo image
            String logoPath = LogoPath;

            // Load the logo image as a Bitmap with options that preserve transparency
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap logoBitmap = BitmapFactory.decodeFile(logoPath, options);

            if (logoBitmap == null) {
                // Assuming 'context' is the reference to your activity or application context
                int resourceId = R.drawable.accessalogo;

                // Get the resource name (without the extension) from the resource ID
                String resourceName = this.getResources().getResourceEntryName(resourceId);

                // Get the resource type (e.g., "drawable") from the resource ID
                String resourceType = this.getResources().getResourceTypeName(resourceId);

                // Now you can construct the path as "android.resource://your_package_name/resource_type/resource_name"
                String imagePath = "android.resource://" + this.getPackageName() + "/" + resourceType + "/" + resourceName;

                // Provide the path to the logo image
                logoPath = imagePath;

                // Load the logo image as a Bitmap with options that preserve transparency
                BitmapFactory.Options options1 = new BitmapFactory.Options();
                options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
                logoBitmap = BitmapFactory.decodeFile(logoPath, options1);
                return;
            }

            // Resize the logo image to fit the desired dimensions
            Bitmap resizedLogoBitmap = Bitmap.createScaledBitmap(logoBitmap, desiredLogoWidth, desiredLogoHeight, true);

            // Print the logo image
            InnerResultCallback innerResultCallback = null;
            service.setAlignment(1, innerResultCallback); // Center alignment
            service.printBitmap(resizedLogoBitmap, innerResultCallback);
            service.lineWrap(1, innerResultCallback); // Print a new line after the logo
            service.setAlignment(0, innerResultCallback); // Left alignment

            // Destroy the logo bitmaps to free up memory
            logoBitmap.recycle();
            resizedLogoBitmap.recycle();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unbind the printer service
        try {
            InnerPrinterManager.getInstance().unBindService(this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            throw new RuntimeException(e);
        }
    }




}
