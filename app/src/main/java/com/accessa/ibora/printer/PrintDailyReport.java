package com.accessa.ibora.printer;


import static com.accessa.ibora.Constants.DB_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_DATE_TRANSACTION;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_PAYMENT_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.ItemsReport.DataModel;
import com.accessa.ibora.ItemsReport.PaymentMethodAdapter;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Report.PaymentAdapter;
import com.accessa.ibora.Report.PaymentItem;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.company.Company;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class PrintDailyReport extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private TextView CompanyName;
    private String Shopname;

    private List<DataModel> newDataList;
    private PaymentMethodAdapter paymentAdapter;
    private SQLiteDatabase database;
    private String cashorId;
    private String cashorName;
    private SharedPreferences sharedPreferences;

    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount;
    private  String DateCreated,timeCreated;
    private double TenderAmount,CashReturn;
    private String cashorlevel,ShopName,LogoPath,OpeningHours;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;
    private DBManager dbManager;

   private String localeString ;
    private String reportType ;
    private String totalTax ;
    private String totalAmounts;
    private   String      TelNum,compTelNum,compFaxNum,TransactionTypename;
    private  String  FaxNum;
    private String paymentName,PosNum ;
    private static final String POSNumber="posNumber";
    private  ArrayList<SettlementItem> settlementItems = new ArrayList<>();

    private TextView CashorId;
    private double settlementAmount ;

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {

            int lineWidth = 48; // Adjust this value according to the width of your paper
            runOnUiThread(new Runnable() {
                @Override
                public void run() {



                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper







                    // Set up the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PrintDailyReport.this));


                    try {

                        dbManager = new DBManager(getApplicationContext());
                        dbManager.open();

                        Company company = dbManager.getCompanyInfo();
                        if (company != null) {


                            String companyName = company.getCompanyName();
                            String CompanyAdress1 = company.getCompADR();
                            String CompanyAdress2 = company.getCompADR2();
                            String CompanyAdress3 = company.getCompADR3();
                            String shopName = company.getShopName();
                            String ShopAdress = company.getADR1();
                            String ShopAdress2 = company.getADR2();
                            String ShopAdress3 = company.getADR3();
                            String CompanyVatNo = company.getVATNo();
                            String CompanyBRNNo = company.getBRNNo();

                            OpeningHours = company.getOpeninghours();
                            TelNum = company.getTelNo();
                            FaxNum = company.getFaxNo();
                            compTelNum = company.getComptel();
                            compFaxNum = company.getCompFax();

                            LogoPath = company.getImage();

                            printLogoAndReceipt(service, LogoPath,100,100);


                            // Create the formatted company name line
                            String companyNameLine = companyName + "\n";
                            // Create the formatted companyAddress1Line  line

                            String CompAdd1andAdd2 = CompanyAdress1 + ", " + CompanyAdress2 + "\n";
                            String CompAdd3 = CompanyAdress3 + "\n";
                            String Add1andAdd2 = ShopAdress + ", " + ShopAdress2 + "\n";
                            String shopAdress3 = ShopAdress3 + "\n";
                            String CompVatNo = getString(R.string.Vat) + "  : " + CompanyVatNo + "\n";
                            String CompBRNNo = getString(R.string.BRN) + "  : " + CompanyBRNNo + "\n\n";
                            String shopname = shopName + "\n";
                            String Tel = "Tel : " + TelNum;
                            String Fax = "Fax : " + FaxNum;
                            String compTel = "Tel : " + compTelNum;
                            String compFax = "Fax : " + compFaxNum;

                            service.setFontSize(24, null);
                            service.setAlignment(1, null);


                            // Enable bold text
                            byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                            service.sendRAWData(boldOnBytes, null);
                            service.setFontSize(30, null);


                            // Print the formatted company name line
                            service.printText(companyNameLine, null);

                            // Disable bold text
                            byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                            service.sendRAWData(boldOffBytes, null);
                            service.setFontSize(24, null);
                            // Print the formatted company name line
                            service.printText(CompAdd1andAdd2, null);
                            service.printText(CompAdd3, null);
                            service.printText(compTel + "\n", null);
                            service.printText(compFax + "\n\n", null);
                            // Print the formatted company name line
                            service.printText(shopname, null);
                            service.printText(Add1andAdd2, null);
                            service.printText(shopAdress3, null);
                            service.printText(Tel + "\n", null);
                            service.printText(Fax + "\n", null);
                            service.printText(CompVatNo, null);
                            service.printText(CompBRNNo, null);

                            service.setAlignment(0, null);


                        }
                        Cursor itemCursor = mDatabaseHelper.getUserById(Integer.parseInt(cashorId));
                        if (itemCursor != null && itemCursor.moveToFirst()) {
                            int cashiernameindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME);
                            int cashieridindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id);

                            String Cashiername = itemCursor.getString(cashiernameindex);
                            String Cashierid = itemCursor.getString(cashieridindex);
                            String Till_id = readTextFromFile("till_num.txt");
                            String cashiename = "Cashier Name: " + Cashiername;
                            String cashierid = "Cashier Id: " + Cashierid;
                            String Posnum = "POS Number: " + Till_id;
                            service.printText(cashiename + "\n", null);
                            service.printText(cashierid + "\n", null);
                            service.printText(Posnum + "\n", null);
                        }



                            // Print a line separator
                            String lineSeparator = "=".repeat(lineWidth);

                            service.printText(lineSeparator + "\n", null);





                        String frequencytoday="";
                        if (reportType.equals("Daily")){

                            frequencytoday="Today";
                        } else if (reportType.equals("Weekly")) {
                            frequencytoday="This Week";
                        }
                        else if (reportType.equals("Monthly")) {
                            frequencytoday="This Month";
                        }else if (reportType.equals("Yearly")) {
                            frequencytoday="This Year";
                        }
                        String TotalDaily= "All Items sold " + frequencytoday ;



                        String DailyLine = TotalDaily ;
                        // Enable bold text
                        byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        service.printText(DailyLine + "\n", null);

                        // Disable bold text
                        byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        // Initialize the paymentItems list (e.g., retrieve data from the database)
                        newDataList = fetchDataBasedOnReportType(reportType);
                        // Initialize the paymentAdapter

                        int lineWidths= 41;
                        for (DataModel item : newDataList) {
                            String paymentName = item.getLongDescription();
                            double totalAmount = item.getTotalPrice();
                                    int quantity= item.getQuantity();
                            String formattedTotalAmount = String.format("%.2f", totalAmount);


                            String formattedPaymentInfo =  paymentName + " X " + quantity ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidths - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                        }
                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table

                        String Total= getString(R.string.Total);
                        String TVA= getString(R.string.Vat);

                        String TotalValue= "Rs " + totalAmounts;
                        String TotalVAT= "Rs " + totalTax;
                         lineWidths= 38;
                        // Calculate the padding for the item name
                        int TotalValuePadding = lineWidths - TotalValue.length() - Total.length();
                        int TaxValuePadding = lineWidths - TotalVAT.length() - TVA.length();


                        // Enable bold text and set font size to 30
                       boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        // Print the "Total" and its value with the calculated padding
                        String totalLine = Total + " ".repeat(Math.max(0, TotalValuePadding)) + TotalValue;
                        String totaltaxLine = TVA + " ".repeat(Math.max(0, TaxValuePadding)) + TotalVAT;
                        service.printText(totalLine + "\n", null);
                        service.printText(totaltaxLine + "\n", null);





                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        service.setAlignment(1, null); // Align center


                        service.printText(lineSeparator + "\n", null);
                        // Footer Text
                        String FooterText = getString(R.string.Footer_See_You);
                        String Footer1Text = getString(R.string.Footer_Have);
                        String Footer2Text = getString(R.string.Footer_OpenHours);
                        String Openinghours= Footer2Text + OpeningHours ;


                        // Print the centered footer text
                        service.printText(FooterText + "\n", null);


                        // Print the centered footer1 text
                        service.printText(Footer1Text + "\n", null);




                        // Print the centered footer2 text
                        service.printText(Openinghours + "\n", null);

                        service.setAlignment(0, null); // Align left
                        // Concatenate the formatted date and time
                        String dateTime = DateCreated + " " + timeCreated;
                        // Calculate the padding for the item name
                        Date currentDates = new Date();

                        // Format the current date as a string
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentDateString = dateFormat.format(currentDates);

                        // Print  date and time
                        service.printText(currentDateString + "\n\n", null);

                        if (settlementItems != null) {
                            // Use the settlementItems as needed
                            // You can iterate over the list and access the payment name and settlement amount for each item
                            for (SettlementItem items : settlementItems) {

                                paymentName = items.getPaymentName();
                                // Query the transaction table to get distinct VAT types
                                Cursor DrawerCursor = mDatabaseHelper.getDistinctDrawerconfig(paymentName);
                                if (DrawerCursor != null && DrawerCursor.moveToFirst()) {
                                    StringBuilder vatTypesBuilder = new StringBuilder();
                                    do {
                                        int columnIndexDrawer = DrawerCursor.getColumnIndex(DatabaseHelper.OpenDrawer);
                                        String vatType = DrawerCursor.getString(columnIndexDrawer);
                                        if (CashReturn > 0 || "true".equals(vatType)) {
                                            service.openDrawer(null);
                                        }
                                        vatTypesBuilder.append(vatType).append(", ");
                                    } while (DrawerCursor.moveToNext());
                                    DrawerCursor.close();



                                }

                            }
                        }



                        // Cut the paper
                        service.cutPaper(null);

                        // Open the cash drawer
                        byte[] openDrawerBytes = new byte[]{0x1B, 0x70, 0x00, 0x32, 0x32};
                        service.sendRAWData(openDrawerBytes, null);
                        // Open the cash drawer

                        if(CashReturn > 0) {
                            service.openDrawer(null);

                        }


                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(PrintDailyReport.this, MainActivity.class);

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






    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_printer);
        // Retrieve the intent and its extras
        Intent intent = getIntent();
         localeString = intent.getStringExtra("locale");
         reportType = intent.getStringExtra("reportType");
         totalTax = intent.getStringExtra("totalTax");
         totalAmounts = intent.getStringExtra("totalAmount");

        if (localeString != null) {
            Locale locale = Locale.forLanguageTag(localeString);
            // Use the locale as needed
            Log.d("PrintDailyReport", "Received Locale: " + locale.toString());
        }

        if (reportType != null) {
            // Use the reportType as needed
            Log.d("PrintDailyReport", "Received Report Type: " + reportType);
        }

        if (totalTax != null) {
            // Use the totalTax as needed
            Log.d("PrintDailyReport", "Received Total Tax: " + totalTax);
        }

        if (totalAmounts != null) {
            // Use the totalAmount as needed
            Log.d("PrintDailyReport", "Received Total Amount: " + totalAmount);
        }

        mDatabaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper
        database = mDatabaseHelper.getWritableDatabase(); // Initialize the SQLiteDatabase object
        // Open your SQLite database
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // Retrieve the shared preferences
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name

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
    private  String readTextFromFile(String fileName) {
        try {
            FileInputStream fileInputStream = this.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<DataModel> fetchDataBasedOnReportType(String reportType) {
        // Implement your logic to fetch data based on the selected report type
        // For now, return a dummy list
        List<DataModel> dummyDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch data based on report type
        // Replace the method and parameters with your actual database queries
        dummyDataList = mDatabaseHelper.getDataBasedOnReportType(reportType);

        return dummyDataList;
    }
    private double getDailyAmount(Date date) {
        // Calculate the start and end date of the current day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

// Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

// Query the database to get the daily amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?", new String[]{formattedStartDate, formattedEndDate});

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;

    }

    private double getWeeklyAmount(Date date) {
        // Calculate the start and end date of the current week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date endDate = calendar.getTime();

// Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

// Query the database to get the weekly amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?", new String[]{formattedStartDate, formattedEndDate});

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;


    }

    private double getMonthlyAmount(Date date) {
        // Calculate the start and end date of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

// Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

// Query the database to get the monthly amount
        Cursor cursor = database.rawQuery("SELECT SUM(" + SETTLEMENT_AMOUNT + ") FROM " + INVOICE_SETTLEMENT_TABLE_NAME + " WHERE " + SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?", new String[]{formattedStartDate, formattedEndDate});


        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;
    }

    private String formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(amount);
    }
    private String formatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
    private String formatDateTimes(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
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

