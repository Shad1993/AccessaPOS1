package com.accessa.ibora.printer;


import static com.accessa.ibora.Constants.DB_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.INVOICE_SETTLEMENT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_DATE_TRANSACTION;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_INVOICE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_PAYMENT_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_TOTAL_AMOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE_CREATED;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_TTC;

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

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.Report.PaymentAdapter;
import com.accessa.ibora.Report.PaymentItem;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.company.Company;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.bumptech.glide.Glide;
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


public class PrintReport extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private TextView CompanyName;
    private String Shopname;
    private List<PaymentItem> paymentItems; // Define the paymentItems list

    private PaymentAdapter paymentAdapter;
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
    private String cashorlevel,ShopName,LogoPath;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;
    private DBManager dbManager;
    private String    OpeningHours;
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
            int lineWidths= 38;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {



                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper







                    // Set up the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PrintReport.this));


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

                            printLogoAndReceipt(service, LogoPath,600,300);


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

                            // Get the current date and time
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String currentDateTime = dateFormat.format(new Date());
                            String datetime = "Date/Time: " + currentDateTime;

                            service.printText(cashiename + "\n", null);
                            service.printText(cashierid + "\n", null);
                            service.printText(Posnum + "\n", null);
                            service.printText(datetime + "\n\n", null);
                        }



                            // Print a line separator
                            String lineSeparator = "=".repeat(lineWidth);
                            String singlelineSeparator = "-".repeat(lineWidth);
                            service.printText(lineSeparator + "\n", null);

                        Date currentDate = new Date();
                        double dailyAmount = getDailyAmount(currentDate);

                        String formattedTotalDaily = String.format("%.2f", dailyAmount);
                        String formattedDaily= "Rs "  + formattedTotalDaily;
                        String TotalDaily= "Total Amount Today "  ;


                        int TenderTypesDaily = lineWidths- formattedDaily.length() - TotalDaily.length();
                        String DailyLine = TotalDaily + " ".repeat(Math.max(0, TenderTypesDaily)) + formattedDaily;
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
                        paymentItems = getPaymentItemsForToday();
                        // Initialize the paymentAdapter
                        paymentAdapter = new PaymentAdapter(paymentItems, getApplicationContext());
                        int lineWidths= 41;
                        for (PaymentItem item : paymentItems) {
                            String paymentName = item.getPaymentName();
                            double totalAmount = item.getAmountPaid();

                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedPaymentInfo = "Payment Method: " + paymentName ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidth - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                        }
                       double cashreturn= mDatabaseHelper.getSumOfCashReturnForCurrentDate();
                        String formattedTcashreturn = String.format("%.2f", cashreturn);
                        String cashret=" -Rs " + formattedTcashreturn;
                        String formattedcashreturnInfo = "Cash Return: " ;
                        int cashreturnPadding = lineWidth - cashret.length() - formattedcashreturnInfo.length();
                        String CashreturnLine = formattedcashreturnInfo + " ".repeat(Math.max(0, cashreturnPadding)) + cashret;
                        service.printText(CashreturnLine + "\n", null);
                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table

                         currentDate = new Date();
                        double weeklyAmount = getWeeklyAmount(currentDate);
                        String formattedTotalWeekly = String.format("%.2f", weeklyAmount);
                        String TotalWeekly = "Total Amount This Week "  ;
                        String formattedWeekly="Rs " + formattedTotalWeekly;
                        lineWidths= 38;
                        int TenderTypesWeekly = lineWidths - formattedWeekly.length() - TotalWeekly.length();
                         String weeklyLine = TotalWeekly + " ".repeat(Math.max(0, TenderTypesWeekly)) + formattedWeekly;
                        // Enable bold text
                        boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        service.printText(weeklyLine + "\n", null);

                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        paymentItems = getPaymentItemsForThisWeek();
                        // Initialize the paymentAdapter
                        paymentAdapter = new PaymentAdapter(paymentItems, getApplicationContext());

                        for (PaymentItem item : paymentItems) {
                            String paymentName = item.getPaymentName();
                            double totalAmount = item.getAmountPaid();

                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedPaymentInfo = "Payment Method: " + paymentName ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidth - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                        }
                         cashreturn= mDatabaseHelper.getSumOfCashReturnForCurrentWeek();
                         formattedTcashreturn = String.format("%.2f", cashreturn);
                         cashret=" -Rs " + formattedTcashreturn;
                         formattedcashreturnInfo = "Cash Return: " ;
                         cashreturnPadding = lineWidth - cashret.length() - formattedcashreturnInfo.length();
                         CashreturnLine = formattedcashreturnInfo + " ".repeat(Math.max(0, cashreturnPadding)) + cashret;
                        service.printText(CashreturnLine + "\n", null);
                        service.printText(lineSeparator + "\n", null);

                        currentDate = new Date();

                        lineWidths= 38;
                        double monthlyAmount = getMonthlyAmount(currentDate);
                        String formattedmonthlyAmountT = String.format("%.2f", monthlyAmount);
                        String formattedMonthly="Rs " + formattedmonthlyAmountT;
                        String TotalMontly = "Total Amount This Month "  ;
                        int ReturnTypesPadding = lineWidths - formattedMonthly.length() - TotalMontly.length();
                        String MonthlyLine = TotalMontly + " ".repeat(Math.max(0, ReturnTypesPadding)) + formattedMonthly;
                        // Enable bold text
                        boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);
                        service.printText(MonthlyLine + "\n", null);

                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        service.setAlignment(1, null); // Align center
                        paymentItems = getPaymentItemsForThisMonth();
                        // Initialize the paymentAdapter
                        paymentAdapter = new PaymentAdapter(paymentItems, getApplicationContext());

                        for (PaymentItem item : paymentItems) {
                            String paymentName = item.getPaymentName();
                            double totalAmount = item.getAmountPaid();

                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedPaymentInfo = "Payment Method: " + paymentName ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidth - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                        }
                        cashreturn= mDatabaseHelper.getSumOfCashReturnForCurrentMonth();
                        formattedTcashreturn = String.format("%.2f", cashreturn);
                        cashret=" -Rs " + formattedTcashreturn;
                        formattedcashreturnInfo = "Cash Return: " ;
                        cashreturnPadding = lineWidth - cashret.length() - formattedcashreturnInfo.length();
                        CashreturnLine = formattedcashreturnInfo + " ".repeat(Math.max(0, cashreturnPadding)) + cashret;
                        service.printText(CashreturnLine + "\n", null);
                        service.printText(lineSeparator + "\n", null);

                        currentDate = new Date();


                        double yearlyAmount = getYearlyAmount(currentDate);
                        String formattedyearlyAmountT = String.format("%.2f", yearlyAmount);
                        String formattedYearly="Rs " + formattedyearlyAmountT;
                        String TotalYearly = "Total Amount This Year "  ;
                         ReturnTypesPadding = lineWidths - formattedYearly.length() - TotalYearly.length();
                        String YearlyLine = TotalYearly + " ".repeat(Math.max(0, ReturnTypesPadding)) + formattedYearly;
                        // Enable bold text
                        boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);
                        service.printText(YearlyLine + "\n", null);

                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        service.setAlignment(1, null); // Align center
                        paymentItems = getPaymentItemsForThisYear();
                        // Initialize the paymentAdapter
                        paymentAdapter = new PaymentAdapter(paymentItems, getApplicationContext());

                        for (PaymentItem item : paymentItems) {
                            String paymentName = item.getPaymentName();
                            double totalAmount = item.getAmountPaid();

                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedPaymentInfo = "Payment Method: " + paymentName ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidth - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                        }
                        cashreturn= mDatabaseHelper.getSumOfCashReturnForCurrentYear();
                        formattedTcashreturn = String.format("%.2f", cashreturn);
                        cashret=" -Rs " + formattedTcashreturn;
                        formattedcashreturnInfo = "Cash Return: " ;
                        cashreturnPadding = lineWidth - cashret.length() - formattedcashreturnInfo.length();
                        CashreturnLine = formattedcashreturnInfo + " ".repeat(Math.max(0, cashreturnPadding)) + cashret;
                        service.printText(CashreturnLine + "\n", null);
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
                                            Log.d("drawer16", "drawer16");

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
                            Log.d("drawer17", "drawer17");

                            service.openDrawer(null);

                        }


                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(PrintReport.this, SalesReportActivity.class);

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
    private List<PaymentItem> getPaymentItemsForThisMonth() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Get today's date
        Date currentDate = new Date();

        // Calculate the start and end date of the current month
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

        // Format the start and end dates as 'yyyy-MM-dd' (assuming your date column is in this format)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the desired timezone
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Retrieve the data from the database for the current month
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                SETTLEMENT_PAYMENT_NAME,
                "SUM(" + SETTLEMENT_AMOUNT + ") AS " + SETTLEMENT_AMOUNT, // Calculate the sum of SETTLEMENT_AMOUNT
                SETTLEMENT_DATE_TRANSACTION
        };

        // Define the query parameters to filter by the current month's date range
        String selection = SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?";
        String[] selectionArgs = { startDateString, endDateString };
        String groupBy = SETTLEMENT_PAYMENT_NAME;

        // Execute the query
        Cursor cursor = db.query(
                INVOICE_SETTLEMENT_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                groupBy,
                null,
                null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndex(SETTLEMENT_AMOUNT));
                    String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                    Date transactionDate = parseDate(transactionDateString); // Parse the date string to a Date object

                    PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                    paymentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here (e.g., logging, error handling)
        } finally {
            // Close the Cursor and the database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return paymentItems;
    }
    private List<PaymentItem> getPaymentItemsForThisYear() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Get today's date
        Date currentDate = new Date();

        // Calculate the start and end date of the current year
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();

        // Format the start and end dates as 'yyyy-MM-dd' (assuming your date column is in this format)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the desired timezone
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Retrieve the data from the database for the current year
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                SETTLEMENT_PAYMENT_NAME,
                "SUM(" + SETTLEMENT_AMOUNT + ") AS " + SETTLEMENT_AMOUNT, // Calculate the sum of SETTLEMENT_AMOUNT
                SETTLEMENT_DATE_TRANSACTION
        };

        // Define the query parameters to filter by the current year's date range
        String selection = SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?";
        String[] selectionArgs = { startDateString, endDateString };
        String groupBy = SETTLEMENT_PAYMENT_NAME;

        // Execute the query
        Cursor cursor = db.query(
                INVOICE_SETTLEMENT_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                groupBy,
                null,
                null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndex(SETTLEMENT_AMOUNT));
                    String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                    Date transactionDate = parseDate(transactionDateString); // Parse the date string to a Date object

                    PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                    paymentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here (e.g., logging, error handling)
        } finally {
            // Close the Cursor and the database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return paymentItems;
    }

    private List<PaymentItem> getPaymentItemsForThisWeek() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Get today's date
        Date currentDate = new Date();

        // Calculate the start and end date of the current week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        // Format the start and end dates as 'yyyy-MM-dd' (assuming your date column is in this format)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the desired timezone
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Retrieve the data from the database for the current week
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                SETTLEMENT_PAYMENT_NAME,
                "SUM(" + SETTLEMENT_AMOUNT + ") AS " + SETTLEMENT_AMOUNT, // Calculate the sum of SETTLEMENT_AMOUNT
                SETTLEMENT_DATE_TRANSACTION
        };

        // Define the query parameters to filter by the current week's date range
        String selection = SETTLEMENT_DATE_TRANSACTION + " >= ? AND " + SETTLEMENT_DATE_TRANSACTION + " < ?";
        String[] selectionArgs = { startDateString, endDateString };
        String groupBy = SETTLEMENT_PAYMENT_NAME;

        // Execute the query
        Cursor cursor = db.query(
                INVOICE_SETTLEMENT_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                groupBy,
                null,
                null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndex(SETTLEMENT_AMOUNT));
                    String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                    Date transactionDate = parseDate(transactionDateString); // Parse the date string to a Date object

                    PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                    paymentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here (e.g., logging, error handling)
        } finally {
            // Close the Cursor and the database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return paymentItems;
    }

    private List<PaymentItem> getPaymentItemsForToday() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        // Get today's date
        Date currentDate = new Date();

        // Format the current date as 'yyyy-MM-dd' (assuming your date column is in this format)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the desired timezone
        String currentDateString = dateFormat.format(currentDate);


        // Retrieve the data from the database for today's date
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                SETTLEMENT_PAYMENT_NAME,
                "SUM(" + SETTLEMENT_AMOUNT + ") AS " + SETTLEMENT_AMOUNT ,// Calculate the sum of SETTLEMENT_AMOUNT

                SETTLEMENT_DATE_TRANSACTION
        };

        // Define the query parameters to filter by today's date
        String selection = SETTLEMENT_DATE_TRANSACTION + "=?";
        String[] selectionArgs = { currentDateString };
        String groupBy = SETTLEMENT_PAYMENT_NAME;

        // Execute the query
        Cursor cursor = db.query(
                INVOICE_SETTLEMENT_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                groupBy,
                null,
                null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String paymentName = cursor.getString(cursor.getColumnIndex(SETTLEMENT_PAYMENT_NAME));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndex(SETTLEMENT_AMOUNT));
                    String transactionDateString = cursor.getString(cursor.getColumnIndex(SETTLEMENT_DATE_TRANSACTION));
                    Date transactionDate = parseDate(transactionDateString); // Parse the date string to a Date object

                    PaymentItem item = new PaymentItem(paymentName, totalAmount, transactionDate);
                    paymentItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here (e.g., logging, error handling)
        } finally {
            // Close the Cursor and the database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return paymentItems;
    }


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
        Cursor cursor = database.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')",
                new String[]{formattedStartDate, formattedEndDate}
        );


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
        Cursor cursor = database.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')",
                new String[]{formattedStartDate, formattedEndDate}
        );

        double amount = 0.0;

        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(0);
        }

        cursor.close();

        return amount;


    }
    private double getYearlyAmount(Date date) {
        // Calculate the start and end date of the given year
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();

        // Format startDate and endDate to 'yyyy-MM-dd' format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        // Query the database to get the yearly amount
        Cursor cursor = database.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')",
                new String[]{formattedStartDate, formattedEndDate}
        );


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
        // Query the database to get the yearly amount
        Cursor cursor = database.rawQuery(
                "SELECT SUM(CASE " +
                        "WHEN " + TRANSACTION_STATUS + " = 'Completed' THEN " + TRANSACTION_TOTAL_TTC + " " +
                        "WHEN " + TRANSACTION_STATUS + " = 'CRN' THEN -" + TRANSACTION_TOTAL_TTC + " " +
                        "ELSE 0 END) AS TotalSumTTC " +
                        "FROM " + TRANSACTION_HEADER_TABLE_NAME + " " +
                        "WHERE " + TRANSACTION_DATE_CREATED + " >= ? AND " + TRANSACTION_DATE_CREATED + " < ? " +
                        "AND (" + TRANSACTION_STATUS + " = 'Completed' OR " + TRANSACTION_STATUS + " = 'CRN')",
                new String[]{formattedStartDate, formattedEndDate}
        );

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

