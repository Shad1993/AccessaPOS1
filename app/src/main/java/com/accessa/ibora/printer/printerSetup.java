package com.accessa.ibora.printer;

import static android.app.PendingIntent.getActivity;

import static com.accessa.ibora.product.items.DatabaseHelper.PREFERENCE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.STATUS_KEY;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.ForceRestart;
import com.accessa.ibora.LoadingActivity;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.SplashActivity;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.Tables.TableAdapter;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.SplittedTicketAdapter;
import com.accessa.ibora.sales.ticket.Ticket;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class printerSetup extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private String transactionIdInProgress;
    private String tableid;
    boolean areAllItemsNotSelectedNotPaid;
    boolean areNoItemsSelectedNorPaid;
    boolean isAtLeastOneItemSelected = true;
    boolean areNOItemsSelected = false;
    boolean areAllItemsPaid = false;
    private String printerpartname ;
    private static final String PREFS_NAME = "AppSessionPrefs";
    private static final String KEY_SAME_SESSION = "IsSameSession";
    private boolean printable ;
    private SharedPreferences sharedPreferences;
    private String roomid;
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
    private SplittedTicketAdapter mSplittedAdapter;
    String firstTransactionTicketNo = null;
    String secondTransactionTicketNo = null;
    private String    OpeningHours;
    private   String      TelNum,compTelNum,compFaxNum;
    private  String  FaxNum;
    private String paymentName,PosNum ;
    private static final String POSNumber="posNumber";
    private  ArrayList<SettlementItem> settlementItems = new ArrayList<>();


    private double settlementAmount ;
    private String mraqr,paymentmethod ;
    private double amountReceived,cashReturn;
    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {

            int lineWidth = 48; // Adjust this value according to the width of your paper
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(KEY_SAME_SESSION, true);
                    editor.apply(); // Save changes asynchronously

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
                    RecyclerView splittedrecyclerView = findViewById(R.id.splittedrecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(printerSetup.this));
                    splittedrecyclerView.setLayoutManager(new LinearLayoutManager(printerSetup.this));


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
                            String CompanyAdress1 = cursorCompany.getString(columnCompanyAddressIndex);
                            String CompanyAdress2 = cursorCompany.getString(columnCompanyAddress2Index);
                            String CompanyAdress3 = cursorCompany.getString(columnCompanyAddress3Index);
                            String shopName = cursorCompany.getString(columnShopNameIndex);
                            String ShopAdress = cursorCompany.getString(columnShopAddressIndex);
                            String ShopAdress2 = cursorCompany.getString(columnShopAddress2Index);
                            String ShopAdress3 = cursorCompany.getString(columnShopAddress3Index);
                            String CompanyVatNo = cursorCompany.getString(columnCompanyVATIndex);
                            String CompanyBRNNo = cursorCompany.getString(columnCompanyBRNIndex);
                            String inv = "Invoice" + "\n";
                            OpeningHours = cursorCompany.getString(columnOpeningHoursIndex);
                            TelNum = cursorCompany.getString(columnTelNumIndex);
                            FaxNum = cursorCompany.getString(columnFaxIndex);
                            compTelNum = cursorCompany.getString(columncompTelNumIndex);
                            compFaxNum = cursorCompany.getString(columncompFaxIndex);
                            service.setFontSize(24, null);
                            service.setAlignment(1, null);
                            // Print the formatted company name line
                            service.printText(inv, null);

                            LogoPath = cursorCompany.getString(columnLogoPathIndex);

                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if( printerpartname.equals("Logo") && printable) {

                                    printLogoAndReceipt(service, LogoPath, 600, 300);

                                }
                            }


                            // Create the formatted company name line
                            String companyNameLine = companyName + "\n";
                            // Create the formatted companyAddress1Line  line

                            String CompAdd1andAdd2 = CompanyAdress1 + ", " + CompanyAdress2 + "\n";
                            String CompAdd3 = CompanyAdress3 + "\n";
                            String Add1andAdd2 = ShopAdress + ", " + ShopAdress2 + "\n";
                            String shopAdress3 = ShopAdress3 + "\n";
                            String CompVatNo = getString(R.string.Vat) + "  : " + CompanyVatNo + "\n";
                            String CompBRNNo = getString(R.string.BRN) + "  : " + CompanyBRNNo + "\n";
                            String shopname = shopName + "\n";
                            String Tel = "Tel : " + TelNum;
                            String Fax = "Fax : " + FaxNum;
                            String compTel = "Tel : " + compTelNum;
                            String compFax = "Fax : " + compFaxNum;

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
                                   // service.printText(shopAdress3, null);
                                    service.printText(Tel + "\n", null);
                                  //  service.printText(Fax + "\n", null);
                                    service.printText(CompVatNo, null);
                                    service.printText(CompBRNNo, null);
                                }
                            }
                            service.setAlignment(0, null);


                        }

                        // Print the custom layout
                        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(roomid,tableid);
                        Log.d("Debugid", "Transaction ID: " + transactionIdInProgress);
                        int actualcopyprinted =0;
                    if(transactionIdInProgress != null){
                         actualcopyprinted = mDatabaseHelper.getCopyPrinted(transactionIdInProgress);
                        mDatabaseHelper.updateCopyPrinted(transactionIdInProgress, actualcopyprinted);
                    }else{
                         actualcopyprinted =1;
                    }


                        int newactualcopyprinted = mDatabaseHelper.getCopyPrinted(transactionIdInProgress);
                        int covers = mDatabaseHelper.getNumberOfCovers(transactionIdInProgress);
                        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                        String status = preferences.getString(STATUS_KEY, "default_value");
                        String relatedtransid = mDatabaseHelper.getRelatedTransactionId(transactionIdInProgress);
                        String cashiorname = "Cashier Name: " + cashierName + "\n";
                        String Tillids  = "Pos Num: " + PosNum + "\n";
                        String RelTrID = "Related Trans Id: " + relatedtransid + "\n";
                        String cashierid = "Cashier Id: " + cashierId + "\n";
                        String room = "Room Number: " + roomid + "\n";
                        String table = "Table Number: " + tableid + "\n";
                        String Printed = "Copy Printed: " + newactualcopyprinted + "\n";
                        String SalesType = "Sales Type -" + status + "\n";
                        String coversnum = "Number Of Servings: " + covers + "\n";
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Cashior name") && printable) {
                                service.printText(cashiorname, null);
                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Related Transaction Id") && printable) {
                                service.printText(RelTrID, null);
                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("SalesType") && printable) {
                                service.printText(SalesType, null);
                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Number Of servings") && printable) {
                                service.printText(coversnum, null);
                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            if (printerpartname.equals("Cashier Id") && printable) {
                                service.printText(cashierid + "\n", null);

                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("POS NUM") && printable) {
                                service.printText(Tillids, null);
                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Room Number") && printable) {

                                service.printText(room, null);
                            }
                        }

                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Table Number") && printable) {

                                service.printText(table, null);
                            }
                        }
                        for (PrinterSetupPrefs setup : printerSetups) {
                            printerpartname = setup.getName();
                            printable = setup.isDrawerOpens();

                            if (printerpartname.equals("Copy Printed") && printable) {

                                service.printText(Printed, null);
                            }
                        }
                        // Print a line separator

                        String lineSeparator = "=".repeat(lineWidth);
                        String singlelineSeparator = "-".repeat(lineWidth);
                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursorsplit = mDatabaseHelper.getTransactionHeaderTotal(roomid, tableid);
                        if (cursorsplit != null && cursorsplit.moveToFirst()) {
                            int columnIndexSplitType = cursorsplit.getColumnIndex(DatabaseHelper.TRANSACTION_SPLIT_TYPE);

                            String transactionid = mDatabaseHelper.getTransactionTicketNo(roomid, tableid);
                            Cursor cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);
                            if (cursortrans != null) {
                                int transactionCount = cursortrans.getCount(); // Get the number of rows
                                if (transactionCount > 1) {
                                    Log.d("TransactionInfo", "More than one transaction in progress: " + transactionCount);

                                     firstTransactionTicketNo = null;
                                     secondTransactionTicketNo = null;

                                    if (cursortrans.moveToFirst()) {
                                        int index = 0;
                                        do {
                                            String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                            String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                            Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                            // Assign transaction ticket numbers to variables
                                            if (index == 0) {
                                                firstTransactionTicketNo = ticketNo;
                                            } else if (index == 1) {
                                                secondTransactionTicketNo = ticketNo;
                                            }
                                            index++; // Increment index for tracking rows

                                            isAtLeastOneItemSelected = true;
                                            areNOItemsSelected = false;
                                            areAllItemsPaid = false;

                                        } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions
                                    }

                                    // Log the variables for debugging
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                    Log.d("TransactionInfo", "Second Transaction Ticket No: " + secondTransactionTicketNo);

                                } else if (transactionCount == 1) {
                                    Log.d("TransactionInfo", "Only one transaction in progress.");
                                    cursortrans.moveToFirst(); // Move to the first row
                                    String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                    String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                    Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                    isAtLeastOneItemSelected = false;
                                    areNOItemsSelected = true;
                                    areAllItemsPaid = false;

                                    // Save the single transaction ticket number as the first variable
                                    String firstTransactionTicketNo = ticketNo;
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                } else {
                                    Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                }
                                cursortrans.close();
                            } else {
                                Log.d("TransactionInfo", "No transactions found for the given room and table.");
                            }

                            Log.e("newTransactionId", String.valueOf(transactionid));
                          //  boolean areNOItemsSelected = mDatabaseHelper.areAllItemsNotSelected(transactionid);
                            Log.e("areNOItemsSelected", String.valueOf(areNOItemsSelected));
                            splittype = cursorsplit.getString(columnIndexSplitType);
                            Log.e("splittype", splittype);
                            if (areNOItemsSelected) {
                                //  String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                                //  String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                                Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactionsbytable(transactionid, String.valueOf(roomid), tableid);
                                Cursor cursorsplitted = mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(transactionid, roomid, tableid);


                                adapter = new TicketAdapter(printerSetup.this, cursor1);
                                mSplittedAdapter = new SplittedTicketAdapter(printerSetup.this, cursorsplitted);
                                recyclerView.setAdapter(adapter);
                                splittedrecyclerView.setAdapter(mSplittedAdapter);


                                List<Transaction> item = adapter.getData();


                                Log.d("room", roomid);
                                Log.d("table", tableid);
                                Log.d("trans11", item.toString());


                                for (Transaction items : item) {
                                    String itemName = items.getItemName();
                                    int itemQuantity = items.getItemQuantity();
                                    String family = items.getFamille();

                                    int itemid = items.getitem_id();
                                    String itemprice = "Rs " + String.format("%.2f", items.getItemPrice());
                                    double totalprice = items.getItemPrice();
                                    double unitprice = totalprice / itemQuantity;
                                    // Calculate the padding for the item name
                                    int itemNamePadding = lineWidth - itemprice.length() - itemName.length();

                                    Log.e("splittype", splittype);
                                    // Inside the for loop where you print the data from the RecyclerView
                                    // Inside the for loop where you print the data from the RecyclerView
                                    String newid = String.valueOf(items.getunique_id());
                                    Log.e("newid1", String.valueOf(newid));
                                    Log.e("itemName", itemName);
                                    double currentprice = mDatabaseHelper.getItemPrice(String.valueOf(itemid));
                                    Log.e("currentprice1", String.valueOf(currentprice));
                                    double numericUnitPrice = Double.parseDouble(items.getUnitPrice());
                                    String formattedUnitPrice = "Rs " + String.format("%.2f", unitprice);


                                    // Create the formatted item line


                                    String QuantityLine = itemQuantity + " X " + formattedUnitPrice;
                                    String ItemsInSplitBill = itemQuantity + " X " + formattedUnitPrice;
                                    // Create the formatted item price line
                                    String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                                    service.printText(itemPriceLine + "\n", null);

                                    String famille = mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(newid));
                                    String CatName = mDatabaseHelper.getCatNameById(famille);
                                    String comment = mDatabaseHelper.getTransactionCommentById(Integer.parseInt(newid));
                                    Log.d("famille1", String.valueOf(famille));
                                    Log.d("CatName", String.valueOf(CatName));
                                    Log.d("items.getunique_id()", String.valueOf(newid));

                                    if ("OPEN FOOD".equals(CatName) && comment != null) {

                                        String openfoodText = "--->" + comment;

                                        service.printText(openfoodText + "\n", null);

                                    }
                                    service.printText(QuantityLine + "\n", null);
                                   // double discount = mDatabaseHelper.getTransactionTotalDiscount(Integer.parseInt(newid), transactionid);
                                    double discount = Double.parseDouble(items.getTotalDiscount());
                                    double discountvalue = mDatabaseHelper.getTransactionDiscount(Integer.parseInt(newid), transactionid);

                                    Log.d("discount1", String.valueOf(discount));
                                    Log.d("discountvalue", String.valueOf(discountvalue));
                                    Log.d("items.getunique_id()", String.valueOf(newid));
                                    if (discount != 0 && discountvalue != 0) {
                                        String UNITPRICEText = "--->Unit Price";
                                        String DiscountText = "--->Discount";
                                        String DiscountRateText = "--->Discount Rate";

                                        String formattedDiscountAmount = String.format("Rs %.2f", discount); // Add "Rs" before the discount amount
                                        String formattedunitPrice = String.format("Rs %.2f", numericUnitPrice);
                                        // Calculate the discount rate

                                        String formattedDiscountRate = String.format("%.2f%%", discountvalue); // Format as percentage
                                        // Calculate padding for Discount Rate line
                                        int unitRatePadding = lineWidth - UNITPRICEText.length() - formattedunitPrice.length();
                                        String unitpriceRateLine = UNITPRICEText + " ".repeat(Math.max(0, unitRatePadding)) + formattedunitPrice;

                                        // Calculate padding for Discount line
                                        int discountTypesPadding = lineWidth - DiscountText.length() - formattedDiscountAmount.length();
                                        String discountTypesLine = DiscountText + " ".repeat(Math.max(0, discountTypesPadding)) + formattedDiscountAmount;

                                        // Calculate padding for Discount Rate line
                                        int discountRatePadding = lineWidth - DiscountRateText.length() - formattedDiscountRate.length();
                                        String discountRateLine = DiscountRateText + " ".repeat(Math.max(0, discountRatePadding)) + formattedDiscountRate;

                                        // Print Discount amount and Discount Rate
                                        service.printText(unitpriceRateLine + "\n", null);
                                        service.printText(discountTypesLine + "\n", null);
                                        service.printText(discountRateLine + "\n", null);
                                    }
                                } // Print the data from the RecyclerView
                                if (cursorsplitted != null && cursorsplitted.moveToFirst()) {
                                    List<Transaction> splitteditem = mSplittedAdapter.getData();
                                    Log.d("trans12", splitteditem.toString());
                                    service.printText(lineSeparator + "\n", null);
                                    String itemsinsplbill = getString(R.string.itemsinsplitbil);
                                    String styleitem = "*** " + itemsinsplbill + "  ***";
                                    service.setAlignment(1, null);
                                    service.printText(styleitem + "\n", null);
                                    service.setAlignment(0, null);
                                    for (Transaction items : splitteditem) {
                                        String itemName = items.getItemName();
                                        int itemQuantity = items.getItemQuantity();
                                        int itemid = items.getitem_id();
                                        String itemprice = "Rs " + String.format("%.2f", items.getItemPrice());
                                        double totalprice = items.getItemPrice();
                                        double unitprice = totalprice / itemQuantity;
                                        // Calculate the padding for the item name
                                        int itemNamePadding = lineWidth - itemprice.length() - itemName.length();

                                        Log.e("splittype", splittype);
                                        // Inside the for loop where you print the data from the RecyclerView
                                        // Inside the for loop where you print the data from the RecyclerView
                                        String newid = String.valueOf(items.getunique_id());
                                        Log.e("newid1", String.valueOf(newid));
                                        Log.e("itemName", itemName);
                                        double currentprice = mDatabaseHelper.getItemPrice(String.valueOf(itemid));
                                        Log.e("currentprice1", String.valueOf(currentprice));
                                        double numericUnitPrice = Double.parseDouble(items.getUnitPrice());
                                        String formattedUnitPrice = "Rs " + String.format("%.2f", unitprice);


                                        // Create the formatted item line

                                        String QuantityLine = itemQuantity + " X " + formattedUnitPrice;

                                        // Create the formatted item price line
                                        String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                                        service.printText(itemPriceLine + "\n", null);


                                        String famille = mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(newid));
                                        String CatName = mDatabaseHelper.getCatNameById(famille);
                                        String comment = mDatabaseHelper.getTransactionCommentById(Integer.parseInt(newid));
                                        Log.d("famille2", String.valueOf(famille));
                                        Log.d("CatName", String.valueOf(CatName));
                                        Log.d("items.getunique_id()", String.valueOf(newid));

                                        if ("OPEN FOOD".equals(CatName) && comment != null) {

                                            String openfoodText = "--->" + comment;

                                            service.printText(openfoodText + "\n", null);

                                        }
                                        service.printText(QuantityLine + "\n", null);
                                      //  double discount = mDatabaseHelper.getTransactionTotalDiscount(Integer.parseInt(newid), transactionid);
                                        double discount = Double.parseDouble(items.getTotalDiscount());
                                        double discountvalue = mDatabaseHelper.getTransactionDiscount(Integer.parseInt(newid), transactionid);
                                        Log.d("discount2", String.valueOf(discount));
                                        Log.d("discountvalue", String.valueOf(discountvalue));
                                        Log.d("items.getunique_id()", String.valueOf(newid));
                                        if (discount != 0 && discountvalue != 0) {
                                            String UNITPRICEText = "--->Unit Price";
                                            String DiscountText = "--->Discount";
                                            String DiscountRateText = "--->Discount Rate";

                                            String formattedDiscountAmount = String.format("Rs %.2f", discount); // Add "Rs" before the discount amount
                                            String formattedunitPrice = String.format("Rs %.2f", numericUnitPrice);
                                            // Calculate the discount rate

                                            String formattedDiscountRate = String.format("%.2f%%", discountvalue); // Format as percentage
                                            // Calculate padding for Discount Rate line
                                            int unitRatePadding = lineWidth - UNITPRICEText.length() - formattedunitPrice.length();
                                            String unitpriceRateLine = UNITPRICEText + " ".repeat(Math.max(0, unitRatePadding)) + formattedunitPrice;

                                            // Calculate padding for Discount line
                                            int discountTypesPadding = lineWidth - DiscountText.length() - formattedDiscountAmount.length();
                                            String discountTypesLine = DiscountText + " ".repeat(Math.max(0, discountTypesPadding)) + formattedDiscountAmount;

                                            // Calculate padding for Discount Rate line
                                            int discountRatePadding = lineWidth - DiscountRateText.length() - formattedDiscountRate.length();
                                            String discountRateLine = DiscountRateText + " ".repeat(Math.max(0, discountRatePadding)) + formattedDiscountRate;

                                            // Print Discount amount and Discount Rate
                                            service.printText(unitpriceRateLine + "\n", null);
                                            service.printText(discountTypesLine + "\n", null);
                                            service.printText(discountRateLine + "\n", null);
                                        }
                                    }
                                    //service.printText(lineSeparator + "\n", null);
                                }
                            } else {


                                Cursor cursor2 = mDatabaseHelper.getAllSplittedInProgressTransactions(String.valueOf(roomid), tableid);
                                adapter = new TicketAdapter(printerSetup.this, cursor2);
                                recyclerView.setAdapter(adapter);


                                List<Transaction> itemsplitted = adapter.getData();

                                for (Transaction items : itemsplitted) {
                                    String itemName = items.getItemName();
                                    int itemQuantity = items.getItemQuantity();


                                    String itemprice = "Rs " + String.format("%.2f", items.getItemPrice());
                                    int itemid = items.getitem_id();
                                    double totalprice = items.getItemPrice();
                                    double unitprice = totalprice / itemQuantity;
                                    // Calculate the padding for the item name
                                    int itemNamePadding = lineWidth - itemprice.length() - itemName.length();


                                    String newid = String.valueOf(items.getunique_id());
                                    Log.e("getitem_id", String.valueOf(itemid));
                                    Log.e("newid", newid);
                                    double currentprice = mDatabaseHelper.getItemPrice(newid);
                                    Log.e("currentprice", String.valueOf(currentprice));
                                    double numericUnitPrice = Double.parseDouble(items.getUnitPrice());
                                    String formattedUnitPrice = "Rs " + String.format("%.2f", unitprice);


                                    // Create the formatted item line
                                    String QuantityLine = itemQuantity + " X " + formattedUnitPrice;

                                    // Create the formatted item price line
                                    String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;


                                    service.printText(itemPriceLine + "\n", null);


                                    String famille = mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(newid));
                                    Log.d("famille3", String.valueOf(famille));
                                    Log.d("transactionIdInProgress", String.valueOf(transactionIdInProgress));
                                    String CatName ="none";
                                    if(famille != null){
                                         CatName = mDatabaseHelper.getCatNameById(famille);
                                        Log.d("CatName", String.valueOf(CatName));
                                    }


                                    String comment = mDatabaseHelper.getTransactionCommentById(Integer.parseInt(newid));
                                    Log.d("items.getunique_id()", String.valueOf(newid));


                                    if ("OPEN FOOD".equals(CatName) && comment != null) {

                                        String openfoodText = "--->" + comment;

                                        service.printText(openfoodText + "\n", null);

                                    }
                                    service.printText(QuantityLine + "\n", null);
                                    double discount = mDatabaseHelper.getTransactionTotalDiscountbyItemId(itemid, transactionIdInProgress);

                                    double discountvalue = mDatabaseHelper.getTransactionDiscountByItemId(itemid, transactionIdInProgress);
                                    Log.d("discount3", String.valueOf(discount));
                                    Log.d("discountvalue", String.valueOf(discountvalue));
                                    Log.d("items.getunique_id()", String.valueOf(itemid));
                                    if (discount != 0 && discountvalue != 0) {
                                        String UNITPRICEText = "--->Unit Price";
                                        String DiscountText = "--->Discount";
                                        String DiscountRateText = "--->Discount Rate";

                                        String formattedDiscountAmount = String.format("Rs %.2f", discount); // Add "Rs" before the discount amount
                                        String formattedunitPrice = String.format("Rs %.2f", numericUnitPrice);
                                        // Calculate the discount rate

                                        String formattedDiscountRate = String.format("%.2f%%", discountvalue); // Format as percentage
                                        // Calculate padding for Discount Rate line
                                        int unitRatePadding = lineWidth - UNITPRICEText.length() - formattedunitPrice.length();
                                        String unitpriceRateLine = UNITPRICEText + " ".repeat(Math.max(0, unitRatePadding)) + formattedunitPrice;

                                        // Calculate padding for Discount line
                                        int discountTypesPadding = lineWidth - DiscountText.length() - formattedDiscountAmount.length();
                                        String discountTypesLine = DiscountText + " ".repeat(Math.max(0, discountTypesPadding)) + formattedDiscountAmount;

                                        // Calculate padding for Discount Rate line
                                        int discountRatePadding = lineWidth - DiscountRateText.length() - formattedDiscountRate.length();
                                        String discountRateLine = DiscountRateText + " ".repeat(Math.max(0, discountRatePadding)) + formattedDiscountRate;

                                        // Print Discount amount and Discount Rate
                                        service.printText(unitpriceRateLine + "\n", null);
                                        service.printText(discountTypesLine + "\n", null);
                                        service.printText(discountRateLine + "\n", null);
                                    }
                                }


                            }
                        }

                        // Print a line separator

                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table
                        String transactionid = mDatabaseHelper.getTransactionTicketNo(roomid, tableid);
                        Log.e("transactionid1", String.valueOf(transactionid));

                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursor = mDatabaseHelper.getTransactionHeader(roomid, tableid);
                        int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                        int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
                        int columnIndexTimeCreated = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TIME_CREATED);
                        int columnIndexDateCreated = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED);
                        if (cursor != null && cursor.moveToFirst()) {

                            // String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                            //  String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                            Cursor cursor1 = mDatabaseHelper.getSplittedInProgressNotSelectedNotPaidTransactions(transactionid, String.valueOf(roomid), tableid);
                            Cursor cursor2 = mDatabaseHelper.getAllSplittedInProgressTransactions(String.valueOf(roomid), tableid);
                            Cursor cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);
                            if (cursortrans != null) {
                                int transactionCount = cursortrans.getCount(); // Get the number of rows
                                if (transactionCount > 1) {
                                    Log.d("TransactionInfo", "More than one transaction in progress: " + transactionCount);



                                    if (cursortrans.moveToFirst()) {
                                        int index = 0;
                                        do {
                                            String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                            String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                            Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                            // Assign transaction ticket numbers to variables
                                            if (index == 0) {
                                                firstTransactionTicketNo = ticketNo;
                                            } else if (index == 1) {
                                                secondTransactionTicketNo = ticketNo;
                                            }
                                            index++; // Increment index for tracking rows

                                            isAtLeastOneItemSelected = true;
                                            areNOItemsSelected = false;
                                            areAllItemsPaid = false;

                                        } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions
                                    }

                                    // Log the variables for debugging
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                    Log.d("TransactionInfo", "Second Transaction Ticket No: " + secondTransactionTicketNo);

                                } else if (transactionCount == 1) {
                                    Log.d("TransactionInfo", "Only one transaction in progress.");
                                    cursortrans.moveToFirst(); // Move to the first row
                                    String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                    String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                    Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                    isAtLeastOneItemSelected = false;
                                    areNOItemsSelected = true;
                                    areAllItemsPaid = false;
                                    if(tableid.startsWith("T") ){
                                        unmergeTable(tableid);
                                    }
                                    // Save the single transaction ticket number as the first variable
                                    String firstTransactionTicketNo = ticketNo;
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                } else {
                                    Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                }
                                cursortrans.close();
                            } else {
                                Log.d("TransactionInfo", "No transactions found for the given room and table.");
                            }



                            //boolean areNOItemsSelected = mDatabaseHelper.areAllItemsNotSelected(transactionIdInProgress);
                           // boolean areAllItemsNotSelectedNotPaid = mDatabaseHelper.areAllItemsNotSelectedNotPaid(transactionIdInProgress);
                          //  boolean areNoItemsSelectedNorPaid = mDatabaseHelper.areNoItemsSelectedNorPaid(transactionIdInProgress);
                            if (isAtLeastOneItemSelected) {
                                if (cursor1 != null && cursor1.moveToFirst()) {


                                    Log.d("TransactionInfo", "Second Transaction Ticket No: " + secondTransactionTicketNo);
                                    totalAmount = mDatabaseHelper.calculateTotalAmountBasedOnId(secondTransactionTicketNo, roomid, tableid);
                                    TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmountsNotSelectedNotPaid(secondTransactionTicketNo, roomid, tableid);
                                    Log.e("totalAmountpt11", String.valueOf(totalAmount));
                                    cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);

                                    if (cursortrans != null) {
                                        int transactionCount = cursortrans.getCount(); // Get the number of rows
                                        if (transactionCount > 1) {
                                            Log.d("TransactionInfosss", "More than one transaction in progress: " + transactionCount);

                                            firstTransactionTicketNo = null;
                                            secondTransactionTicketNo = null;

                                            if (cursortrans.moveToFirst()) {
                                                int index = 0;
                                                do {
                                                    String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                                    String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                                    Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                                    // Assign transaction ticket numbers to variables
                                                    if (index == 0) {
                                                        firstTransactionTicketNo = ticketNo;
                                                    } else if (index == 1) {
                                                        secondTransactionTicketNo = ticketNo;
                                                    }
                                                    index++; // Increment index for tracking rows


                                                } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions

                                            }


                                        } else if (transactionCount == 1) {
                                          cursortrans.moveToFirst(); // Move to the first row
                                            String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                            String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));

                                            if(tableid.startsWith("T") ){
                                                unmergeTable(tableid);
                                            }

                                            // Save the single transaction ticket number as the first variable
                                            String firstTransactionTicketNo = ticketNo;

                                        } else {
                                            Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                        }
                                        cursortrans.close();
                                    } else {
                                        Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                    }


                                }
                                DateCreated = cursor.getString(columnIndexDateCreated);
                                timeCreated = cursor.getString(columnIndexTimeCreated);


                            } else if (areNOItemsSelected) {
                                totalAmount = cursor.getDouble(columnIndexTotalAmount);
                                TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                                DateCreated = cursor.getString(columnIndexDateCreated);
                                timeCreated = cursor.getString(columnIndexTimeCreated);
                                Log.e("totalAmountpt0", String.valueOf(totalAmount));
                            } else if
                            (areNOItemsSelected) {
                                if (cursor1 != null && cursor1.moveToFirst()) {

                                    totalAmount = mDatabaseHelper.calculateTotalAmountsNotSelectedNotPaid(transactionIdInProgress, roomid, tableid);
                                    TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmountsNotSelectedNotPaid(transactionIdInProgress, roomid, tableid);
                                    Log.e("totalAmountpt1", String.valueOf(totalAmount));
                                }

                                DateCreated = cursor.getString(columnIndexDateCreated);
                                timeCreated = cursor.getString(columnIndexTimeCreated);

                            } else {

                                if (cursor2 != null && cursor2.moveToFirst()) {

                                    totalAmount = mDatabaseHelper.calculateTotalAmounts(roomid, tableid);
                                    TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmounts(roomid, tableid);

                                    Log.e("totalAmountpt2", String.valueOf(totalAmount));

                                } else {
                                    totalAmount = cursor.getDouble(columnIndexTotalAmount);
                                    TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                                }
                                DateCreated = cursor.getString(columnIndexDateCreated);
                                timeCreated = cursor.getString(columnIndexTimeCreated);
                            }
                            if (cashorlevel.equals("1")) {

                                if (areAllItemsNotSelectedNotPaid) {
                                    Log.d("areAllItemsNotSelectedNotPaid", String.valueOf(areAllItemsNotSelectedNotPaid));
                                    if (cursor1 != null && cursor1.moveToFirst()) {

                                        totalAmount = mDatabaseHelper.calculateTotalAmountsNotSelectedNotPaid(transactionid, roomid, tableid);
                                        TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmountsNotSelectedNotPaid(transactionid, roomid, tableid);
                                        Log.d("totalAmountpt11", String.valueOf(totalAmount));
                                    }
                                    Log.d("transactionid", String.valueOf(transactionid));
                                    Log.d("transactionIdInProgress", String.valueOf(transactionIdInProgress));
                                    totalAmount = mDatabaseHelper.getTransactionTotalTTC(transactionid);
                                    TaxtotalAmount = mDatabaseHelper.getTransactionTotalTX1(transactionid);
                                    Log.d("totalAmountpt11", String.valueOf(totalAmount));
                                    DateCreated = cursor.getString(columnIndexDateCreated);
                                    timeCreated = cursor.getString(columnIndexTimeCreated);
                                    cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);

                                    if (cursortrans != null) {
                                        int transactionCount = cursortrans.getCount(); // Get the number of rows
                                        if (transactionCount > 1) {

                                            Log.d("TransactionInfosss2", "No transactions found for the given room and table.");
                                            firstTransactionTicketNo = null;
                                            secondTransactionTicketNo = null;

                                            if (cursortrans.moveToFirst()) {
                                                int index = 0;
                                                do {
                                                    String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                                    String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));


                                                    // Assign transaction ticket numbers to variables
                                                    if (index == 0) {
                                                        firstTransactionTicketNo = ticketNo;
                                                    } else if (index == 1) {
                                                        secondTransactionTicketNo = ticketNo;
                                                    }
                                                    index++; // Increment index for tracking rows


                                                } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions

                                            }


                                        } else if (transactionCount == 1) {
                                            cursortrans.moveToFirst(); // Move to the first row
                                            String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                            String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));

                                            if(tableid.startsWith("T") ){
                                                unmergeTable(tableid);
                                            }

                                            // Save the single transaction ticket number as the first variable
                                            String firstTransactionTicketNo = ticketNo;

                                        } else {
                                            Log.d("TransactionInfosss2", "No transactions found for the given room and table.");
                                        }
                                        cursortrans.close();
                                    } else {
                                        Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                    }




                                } else if (areNoItemsSelectedNorPaid) {
                                    Log.d("areNoItemsSelectedNorPaid", String.valueOf(areNoItemsSelectedNorPaid));
                                    totalAmount = cursor.getDouble(columnIndexTotalAmount);
                                    TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                                    DateCreated = cursor.getString(columnIndexDateCreated);
                                    timeCreated = cursor.getString(columnIndexTimeCreated);
                                    Log.e("totalAmountpt0", String.valueOf(totalAmount));
                                } else if
                                (areNOItemsSelected) {
                                    Log.d("areNOItemsSelected", String.valueOf(areNOItemsSelected));
                                    if (cursor1 != null && cursor1.moveToFirst()) {

                                        totalAmount = mDatabaseHelper.calculateTotalAmountsNotSelectedNotPaid(transactionid, roomid, tableid);
                                        TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmountsNotSelectedNotPaid(transactionid, roomid, tableid);
                                        Log.e("totalAmountpt1", String.valueOf(totalAmount));
                                    }

                                    DateCreated = cursor.getString(columnIndexDateCreated);
                                    timeCreated = cursor.getString(columnIndexTimeCreated);

                                } else {

                                    if (cursor2 != null && cursor2.moveToFirst()) {

                                        totalAmount = mDatabaseHelper.calculateTotalAmounts(roomid, tableid);
                                        TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmounts(roomid, tableid);
                                        Log.e("totalAmountpt2", String.valueOf(totalAmount));

                                    } else {
                                        totalAmount = cursor.getDouble(columnIndexTotalAmount);
                                        TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                                    }
                                    DateCreated = cursor.getString(columnIndexDateCreated);
                                    timeCreated = cursor.getString(columnIndexTimeCreated);
                                }
                            }
                            Log.e("totalAmountval", String.valueOf(totalAmount));
                            String Total = getString(R.string.Total);
                            String TVA = getString(R.string.Vat);
                            String AmountWitoutVat = "Amount Excluding Vat  ";
                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedTotalTAXAmount = String.format("%.2f", TaxtotalAmount);
                            double amountwvat = totalAmount - TaxtotalAmount;
                            String formattedamountwvat = String.format("%.2f", amountwvat);
                            String TotalValue = "Rs " + formattedTotalAmount;
                            String TotalVAT = "Rs " + formattedTotalTAXAmount;
                            String TotalAmounTWOVAT = "Rs " + formattedamountwvat;
                            int lineWidths = 38;
                            // Calculate the padding for the item name
                            int TotalValuePadding = lineWidths - TotalValue.length() - Total.length();
                            int TaxValuePadding = lineWidth - TotalVAT.length() - TVA.length();
                            int AmountWVatValuePadding = lineWidth - TotalAmounTWOVAT.length() - AmountWitoutVat.length();


                            // Enable bold text and set font size to 30
                            byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                            service.sendRAWData(boldOnBytes, null);
                            service.setFontSize(30, null);

                            // Print the "Total" and its value with the calculated padding
                            String totalLine = Total + " ".repeat(Math.max(0, TotalValuePadding)) + TotalValue;
                            service.printText(totalLine + "\n", null);

                            // Disable bold text and reset font size
                            byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                            service.sendRAWData(boldOffBytes, null);
                            service.setFontSize(24, null);


                            // Query the transaction table to get distinct VAT types
                            Cursor vatCursor = mDatabaseHelper.getDistinctVATTypes(transactionIdInProgress, String.valueOf(roomid), tableid);
                            if (vatCursor != null && vatCursor.moveToFirst()) {
                                StringBuilder vatTypesBuilder = new StringBuilder();
                                do {
                                    int columnIndexVATType = vatCursor.getColumnIndex(DatabaseHelper.VAT_Type);
                                    String vatType = vatCursor.getString(columnIndexVATType);
                                    vatTypesBuilder.append(vatType).append(", ");
                                } while (vatCursor.moveToNext());
                                vatCursor.close();

                                // Remove the trailing comma and space
                                String vatTypes = vatTypesBuilder.toString().trim();
                                if (vatTypes.endsWith(",")) {
                                    vatTypes = vatTypes.substring(0, vatTypes.length() - 1);
                                }


                                int vatTypesPadding = lineWidth - TotalVAT.length() - vatTypes.length();
                                String vatTypesLine = vatTypes + " ".repeat(Math.max(0, vatTypesPadding)) + TotalVAT;
                                service.printText(vatTypesLine + "\n", null);
                            }
                            String AmountWOVATLine = AmountWitoutVat + " ".repeat(Math.max(0, AmountWVatValuePadding)) + TotalAmounTWOVAT;
                            service.printText(AmountWOVATLine + "\n", null);
                        }
                        String formattedTotalTender = String.format("%.2f", amountReceived);
                        String formattedTotalReturn = String.format("%.2f", cashReturn);


                        String TenderTotalAmount = "Total Amount Paid ";
                        String CashReturn = "Cash Return  ";
                        String formattedtender = "Rs " + formattedTotalTender;
                        String formattedReturn = "Rs " + formattedTotalReturn;


                        String formattedTotalTenderpop = String.format("%.2f", totalAmount);
                        String formattedtenderpop = "Rs " + formattedTotalTenderpop;


                        int lineWidths = 41;
                        int TenderTypesPadding = lineWidths - formattedtender.length() - TenderTotalAmount.length();
                        int TenderTypesPaddingpop = lineWidths - formattedtenderpop.length() - TenderTotalAmount.length();
                        service.printText(singlelineSeparator + "\n", null);

                        // Enable bold text and set font size to 30
                        byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(28, null);


                        // Disable bold text and reset font size
                        byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        Cursor cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);
                        if (cursortrans != null) {
                            int transactionCount = cursortrans.getCount(); // Get the number of rows
                            if (transactionCount > 1) {
                                Log.d("TransactionInfo", "More than one transaction in progress: " + transactionCount);

                                 firstTransactionTicketNo = null;
                                 secondTransactionTicketNo = null;

                                if (cursortrans.moveToFirst()) {
                                    int index = 0;
                                    do {
                                        String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                        String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                        Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                        // Assign transaction ticket numbers to variables
                                        if (index == 0) {
                                            firstTransactionTicketNo = ticketNo;
                                        } else if (index == 1) {
                                            secondTransactionTicketNo = ticketNo;
                                        }
                                        index++; // Increment index for tracking rows

                                        isAtLeastOneItemSelected = true;
                                        areNOItemsSelected = false;
                                        areAllItemsPaid = false;

                                    } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions
                                }

                                // Log the variables for debugging
                                Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                Log.d("TransactionInfo", "Second Transaction Ticket No: " + secondTransactionTicketNo);

                            } else if (transactionCount == 1) {
                                Log.d("TransactionInfo", "Only one transaction in progress.");
                                cursortrans.moveToFirst(); // Move to the first row
                                String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                isAtLeastOneItemSelected = false;
                                areNOItemsSelected = true;
                                areAllItemsPaid = false;

                                // Save the single transaction ticket number as the first variable
                                String firstTransactionTicketNo = ticketNo;
                                Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                            } else {
                                Log.d("TransactionInfo", "No transactions found for the given room and table.");
                            }
                            cursortrans.close();
                        } else {
                            Log.d("TransactionInfo", "No transactions found for the given room and table.");
                        }
                 if(isAtLeastOneItemSelected){
                    transactionIdInProgress = secondTransactionTicketNo;
                           }
                        List<SettlementItem> settlementItems = mDatabaseHelper.getSettlementItemsByTransactionId(transactionIdInProgress);
                        if (settlementItems != null) {
                            // Use the settlementItems as needed
                            // You can iterate over the list and access the payment name and settlement amount for each item
                            for (SettlementItem items : settlementItems) {
                                String TenderAmount;
                                paymentName = items.getPaymentName();
                                settlementAmount = items.getSettlementAmount();

                                if (paymentName.equals("Subtotal")) {
                                    TenderAmount = paymentName;


                                    settlementAmount = 0;

                                } else {
                                    TenderAmount = "Amount Paid in  " + paymentName;
                                }
                                if (settlementAmount == 0) {
                                    settlementAmount = totalAmount;
                                }

                                String formattedSettlementAmount = String.format("%.2f", settlementAmount);
                                String formatteditemtender = "Rs " + formattedSettlementAmount;
                                int settlementTypesPadding = lineWidth - formatteditemtender.length() - TenderAmount.length();
                                String TenderItemsTypesLine = TenderAmount + " ".repeat(Math.max(0, settlementTypesPadding)) + formatteditemtender;

                                service.printText(TenderItemsTypesLine + "\n", null);


                                        paymentName = items.getPaymentName();
                                        // Query the transaction table to get distinct VAT types
                                        Cursor DrawerCursor = mDatabaseHelper.getDistinctDrawerconfig(paymentmethod);
                                        if (DrawerCursor != null && DrawerCursor.moveToFirst()) {
                                            StringBuilder vatTypesBuilder = new StringBuilder();
                                            do {
                                                int columnIndexDrawer = DrawerCursor.getColumnIndex(DatabaseHelper.OpenDrawer);
                                                String draweropen = DrawerCursor.getString(columnIndexDrawer);
                                                Log.d("cashReturndadae", String.valueOf(cashReturn));
                                                if (cashReturn > 0 || "true".equals(draweropen)) {
                                                    Log.d("drawer22", paymentmethod);

                                                    service.openDrawer(null);

                                                }
                                                vatTypesBuilder.append(draweropen).append(", ");
                                            } while (DrawerCursor.moveToNext());
                                            DrawerCursor.close();

                                            // Remove the trailing comma and space
                                            String Drawer = vatTypesBuilder.toString().trim();
                                            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                                            sharedPreferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putString("table_id", "0").apply();
                                            sharedPreferences.edit().putString("table_num", "0").apply();



                                        }




                            }
                        } else if (paymentName.equals("POP")) {
                            paymentName = "POP";
                            settlementAmount = totalAmount;
                            String formattedSettlementAmount = String.format("%.2f", settlementAmount);
                            String TenderAmount = "Amount Paid with  " + paymentName;
                            String formatteditemtender = "Rs " + formattedSettlementAmount;
                            int settlementTypesPadding = lineWidth - formatteditemtender.length() - TenderAmount.length();
                            String TenderItemsTypesLine = TenderAmount + " ".repeat(Math.max(0, settlementTypesPadding)) + formatteditemtender;
                            service.printText(TenderItemsTypesLine + "\n", null);
                            // Update the table with settlement details

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String transactionDate = dateFormat.format(new Date()); // Replace 'new Date()' with your actual transaction date
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                            String currentTime = timeFormat.format(new Date());
                            mDatabaseHelper.insertSettlementAmount(paymentName, settlementAmount, transactionIdInProgress, PosNum, transactionDate, currentTime, String.valueOf(roomid), tableid);

                        }


                        if (cashReturn > 0) {
                            service.printText(singlelineSeparator + "\n", null);
                            int ReturnTypesPadding = lineWidth - formattedReturn.length() - CashReturn.length();
                            String ReturnTypesLine = CashReturn + " ".repeat(Math.max(0, ReturnTypesPadding)) + formattedReturn;
                            service.printText(ReturnTypesLine + "\n", null);

                        }
                        service.printText(lineSeparator + "\n", null);
                        service.setAlignment(1, null); // Align center
                        // Footer Text
                        String FooterText = getString(R.string.Footer_See_You);
                        String Footer1Text = getString(R.string.Footer_Have);
                        String Footer2Text = getString(R.string.Footer_OpenHours);
                        String Openinghours = Footer2Text + OpeningHours;

                        if (mraqr != null && (!mraqr.startsWith("Request Failed") && !mraqr.startsWith("Error response code 1: 400"))) {
                            // Log the received QR code string
                            Log.d("QR_DEBUG", "Received QR Code: " + mraqr);
                            String MraFiscalised = "MRA Fiscalised";
                            String MRATransid = transactionIdInProgress;
                            service.printText(MraFiscalised + "\n", null);
                            service.setFontSize(22, null);
                            service.printText("MRA Transaction Id: " + MRATransid + "\n", null);
                            service.setFontSize(24, null);


                            // Decode Base64 string to byte array
                            byte[] imageBytes;
                            try {
                                Log.d("Base64String", mraqr);  // Log the Base64 string
                                imageBytes = android.util.Base64.decode(mraqr, android.util.Base64.DEFAULT);
                            } catch (IllegalArgumentException e) {
                                // Handle decoding errors
                                e.printStackTrace();
                                return;
                            }

                            // Log the decoded byte array for debugging
                            Log.d("QR_DEBUG", "Decoded byte array length: " + imageBytes.length);

                            try {
                                // Convert the decoded byte array to a Bitmap
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                // Print the Bitmap using your existing print method
                                service.printBitmap(bitmap, null);
                                service.printText(" " + "\n", null);
                            } catch (Exception e) {
                                // Handle decoding or printing errors
                                e.printStackTrace();
                            }
                        }

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
                        // Print the centered footer2 text

                        service.setAlignment(0, null); // Align left
                        // Concatenate the formatted date and time
                        String dateTime = DateCreated + " " + timeCreated;
                        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                        String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

                        // Calculate the padding for the item name
                        int DateIDPadding = lineWidth - dateTime.length() - transactionIdInProgress.length();

                        // Create the formatted item price line
                        String DateTimeIdLine = dateTime + " ".repeat(Math.max(0, DateIDPadding)) + transactionIdInProgress;

                        // Print  date and time
                        service.printText(DateTimeIdLine + "\n\n", null);




                        // Cut the paper
                        service.cutPaper(null);


                        //  Clear cache
                        //    mDatabaseHelper.clearCache(getApplicationContext()); // 'this' refers to the Activity context
                        //  mDatabaseHelper.clearSQLiteCache();
                      /*   Glide.get(getApplicationContext()).clearMemory();
                        new Thread(() -> {
                            Glide.get(getApplicationContext()).clearDiskCache();
                        }).start();
                        Picasso picasso = new Picasso.Builder(getApplicationContext()).memoryCache(new LruCache(0)).build();


                       */
                        String transactionType;
                        mDatabaseHelper.updateCoverCount(0,tableid,Integer.parseInt(roomid));


                        if (cashorlevel.equals("1")) {
                            cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);

                            if (cursortrans != null) {
                                int transactionCount = cursortrans.getCount(); // Get the number of rows
                                if (transactionCount > 1) {
                                    Log.d("TransactionInfo", "More than one transaction in progress: " + transactionCount);

                                    firstTransactionTicketNo = null;
                                    secondTransactionTicketNo = null;

                                    if (cursortrans.moveToFirst()) {
                                        int index = 0;
                                        do {
                                            String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                            String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                            Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                            // Assign transaction ticket numbers to variables
                                            if (index == 0) {
                                                firstTransactionTicketNo = ticketNo;
                                            } else if (index == 1) {
                                                secondTransactionTicketNo = ticketNo;
                                            }
                                            index++; // Increment index for tracking rows

                                            isAtLeastOneItemSelected = true;
                                            areNOItemsSelected = false;
                                            areAllItemsPaid = false;

                                        } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions
                                        mDatabaseHelper.updatePaidStatusForSelectedRowsById(secondTransactionTicketNo);
                                    }

                                    // Log the variables for debugging
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                    Log.d("TransactionInfo", "Second Transaction Ticket No: " + secondTransactionTicketNo);

                                } else if (transactionCount == 1) {
                                    Log.d("TransactionInfo", "Only one transaction in progress.");
                                    cursortrans.moveToFirst(); // Move to the first row
                                    String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                    String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                    Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                    isAtLeastOneItemSelected = false;
                                    areNOItemsSelected = true;
                                    areAllItemsPaid = false;

                                    // Save the single transaction ticket number as the first variable
                                    String firstTransactionTicketNo = ticketNo;
                                    mDatabaseHelper.updatePaidStatusForSelectedRowsById(transactionIdInProgress);
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                } else {
                                    Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                }
                                cursortrans.close();
                            } else {
                                Log.d("TransactionInfo", "No transactions found for the given room and table.");
                            }





                            SharedPreferences sharedPreferences2 = getSharedPreferences("PaymentPrefs", Context.MODE_PRIVATE);
                            transactionIdInProgress= sharedPreferences2.getString("new_transaction_id", null);
                            boolean areAllItemsPaid = mDatabaseHelper.areAllItemsPaid(transactionIdInProgress);
                            Log.e("areAllItemsPaid", String.valueOf(areAllItemsPaid));
                            boolean areNOItemsSelected = mDatabaseHelper.areAllItemsNotSelected(transactionIdInProgress);
                            Log.e("areNOItemsSelected", String.valueOf(areNOItemsSelected));
                            boolean isAtLeastOneItemSelected = mDatabaseHelper.isAtLeastOneItemSelected(transactionIdInProgress);
                            Log.e("isAtLeastOneItemSelected", String.valueOf(isAtLeastOneItemSelected));
                            if (areNOItemsSelected) {
                                mDatabaseHelper.updatePaidStatusForNotSelectedRows(transactionIdInProgress);
                                mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);
                            }
                            if (areAllItemsPaid) {
                                // All items with the specified roomId, tableId, and status are selected
                                mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);
                                mDatabaseHelper.updatePaidStatusForSelectedRows(transactionIdInProgress, roomid, tableid);

                            } else if (isAtLeastOneItemSelected) {
                                mDatabaseHelper.updatePaidStatusForSelectedRows(transactionIdInProgress, roomid, tableid);
                                mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);
                            } else {
                                // Some items are not selected
                                mDatabaseHelper.updatePaidStatusForSelectedRows(transactionIdInProgress, roomid, tableid);
                                boolean paid = mDatabaseHelper.areAllTransactionsPaid(roomid, tableid);
                                Log.e("paid", String.valueOf(paid));
                                if (paid) {
                                    if(cashorlevel.equals("0")){
                                        mDatabaseHelper.updateAllTransactionsHeaderStatus(DatabaseHelper.TRANSACTION_STATUS_TRN, String.valueOf(roomid), tableid);

                                    }else{
                                        mDatabaseHelper.updateAllTransactionsHeaderStatus(DatabaseHelper.TRANSACTION_STATUS_COMPLETED, String.valueOf(roomid), tableid);
                                     String oldrealid=   mDatabaseHelper.getRelatedTransactionId(relatedtransid);
                                        Log.d("transactionIdInProgress" , transactionIdInProgress );
                                        Log.d("oldrealid" , oldrealid );
                                        Log.d("rlatesttransIde" , latesttransId );
                                        Log.d("newTransactionIde" , relatedtransid );
                                      /*  if (latesttransId.contains("-PRF-")) {
                                            //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
                                            mDatabaseHelper.duplicateHeaderTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid);
                                            mDatabaseHelper.duplicateTransactionLines(latesttransId,newTransactionId);
                                            mDatabaseHelper.updateTransactionStatusoldprf(latesttransId,"OLDPRF");
                                        }else{
                                            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
                                            // Update the transaction ID in the header table for transactions with status "InProgress"
                                            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

                                        }

                                       */
                                    }

                                }
                            }


                        } else {



                             cursortrans = mDatabaseHelper.getInProgressTransactions(Integer.parseInt(roomid), tableid);

                            if (cursortrans != null) {
                                int transactionCount = cursortrans.getCount(); // Get the number of rows
                                if (transactionCount > 1) {
                                    Log.d("TransactionInfo", "More than one transaction in progress: " + transactionCount);

                                     firstTransactionTicketNo = null;
                                     secondTransactionTicketNo = null;

                                    if (cursortrans.moveToFirst()) {
                                        int index = 0;
                                        do {
                                            String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                            String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                            Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                            // Assign transaction ticket numbers to variables
                                            if (index == 0) {
                                                firstTransactionTicketNo = ticketNo;
                                            } else if (index == 1) {
                                                secondTransactionTicketNo = ticketNo;
                                            }
                                            index++; // Increment index for tracking rows

                                            isAtLeastOneItemSelected = true;
                                            areNOItemsSelected = false;
                                            areAllItemsPaid = false;

                                        } while (cursortrans.moveToNext() && index < 2); // Exit after two transactions
                                    }

                                    // Log the variables for debugging
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                    Log.d("TransactionInfo", "Second Transaction Ticket No: " + secondTransactionTicketNo);

                                } else if (transactionCount == 1) {
                                    Log.d("TransactionInfo", "Only one transaction in progress.");
                                    cursortrans.moveToFirst(); // Move to the first row
                                    String ticketNo = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_TICKET_NO));
                                    String statustrans = cursortrans.getString(cursortrans.getColumnIndexOrThrow(TRANSACTION_STATUS));
                                    Log.d("TransactionInfo", "Ticket No: " + ticketNo + ", Status: " + statustrans);

                                    isAtLeastOneItemSelected = false;
                                    areNOItemsSelected = true;
                                    areAllItemsPaid = false;

                                    // Save the single transaction ticket number as the first variable
                                    String firstTransactionTicketNo = ticketNo;
                                    Log.d("TransactionInfo", "First Transaction Ticket No: " + firstTransactionTicketNo);
                                } else {
                                    Log.d("TransactionInfo", "No transactions found for the given room and table.");
                                }
                                cursortrans.close();
                            } else {
                                Log.d("TransactionInfo", "No transactions found for the given room and table.");
                            }




                            // boolean areAllItemsPaid = mDatabaseHelper.areAllItemsPaid(transactionIdInProgress);
                            Log.e("areAllItemsPaid", String.valueOf(areAllItemsPaid));
                          //  boolean areNOItemsSelected = mDatabaseHelper.areAllItemsNotSelected(transactionIdInProgress);
                            Log.e("areNOItemsSelected", String.valueOf(areNOItemsSelected));
                           // boolean isAtLeastOneItemSelected = mDatabaseHelper.isAtLeastOneItemSelected(transactionIdInProgress);
                            Log.e("isAtLeastOneItemSelected", String.valueOf(isAtLeastOneItemSelected));

                            if (areNOItemsSelected) {
                                if(cashorlevel.equals("0")) {
                                    mDatabaseHelper.updatePaidStatusForNotSelectedRows(transactionIdInProgress);
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);

                                }else{
                                    mDatabaseHelper.updatePaidStatusForNotSelectedRows(transactionIdInProgress);
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_COMPLETED, transactionIdInProgress, roomid, tableid);
                                    String oldrealid=   mDatabaseHelper.getRelatedTransactionId(transactionIdInProgress);

                                      /*  if (latesttransId.contains("-PRF-")) {
                                            //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
                                            mDatabaseHelper.duplicateHeaderTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid);
                                            mDatabaseHelper.duplicateTransactionLines(latesttransId,newTransactionId);
                                            mDatabaseHelper.updateTransactionStatusoldprf(latesttransId,"OLDPRF");
                                        }else{
                                            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
                                            // Update the transaction ID in the header table for transactions with status "InProgress"
                                            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

                                        }

                                       */
                                }

                            }
                            if (areAllItemsPaid) {
                                if(cashorlevel.equals("0")) {
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);
                                    mDatabaseHelper.updatePaidStatusForSelectedRows(transactionIdInProgress, roomid, tableid);

                                }else{
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_COMPLETED, transactionIdInProgress, roomid, tableid);
                                    mDatabaseHelper.updatePaidStatusForSelectedRows(transactionIdInProgress, roomid, tableid);
                                    Log.d("rlatesttransIde1" , latesttransId );
                                    Log.d("newTransactionIde1" , relatedtransid );
                                      /*  if (latesttransId.contains("-PRF-")) {
                                            //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
                                            mDatabaseHelper.duplicateHeaderTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid);
                                            mDatabaseHelper.duplicateTransactionLines(latesttransId,newTransactionId);
                                            mDatabaseHelper.updateTransactionStatusoldprf(latesttransId,"OLDPRF");
                                        }else{
                                            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
                                            // Update the transaction ID in the header table for transactions with status "InProgress"
                                            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

                                        }

                                       */
                                }


                            } else if (isAtLeastOneItemSelected) {
                                if(cashorlevel.equals("0")) {
                                    mDatabaseHelper.updatePaidStatusForSelectedRows(secondTransactionTicketNo, roomid, tableid);
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, secondTransactionTicketNo, roomid, tableid);

                                }else{
                                    mDatabaseHelper.updatePaidStatusForSelectedRows(secondTransactionTicketNo, roomid, tableid);
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_COMPLETED, secondTransactionTicketNo, roomid, tableid);
                                    Log.d("rlatesttransIde2" , latesttransId );
                                    Log.d("newTransactionIde2" , relatedtransid );
                                      /*  if (latesttransId.contains("-PRF-")) {
                                            //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
                                            mDatabaseHelper.duplicateHeaderTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid);
                                            mDatabaseHelper.duplicateTransactionLines(latesttransId,newTransactionId);
                                            mDatabaseHelper.updateTransactionStatusoldprf(latesttransId,"OLDPRF");
                                        }else{
                                            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
                                            // Update the transaction ID in the header table for transactions with status "InProgress"
                                            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

                                        }

                                       */
                                }


                            } else {
                                // Some items are not selected
                                mDatabaseHelper.updatePaidStatusForSelectedRows(transactionIdInProgress, roomid, tableid);
                                boolean paid = mDatabaseHelper.areAllTransactionsPaid(roomid, tableid);
                                Log.e("paid", String.valueOf(paid));
                                if (paid) {
                                    if(cashorlevel.equals("0")) {
                                        mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);

                                    }else{
                                        mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_COMPLETED, transactionIdInProgress, roomid, tableid);
                                    //    Log.d("rlatesttransIde21" , latesttransId );
                                    //    Log.d("newTransactionIde21" , relatedtransid );
                                    /* if(relatedtransid.contains("-PRF-")){
                                            //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
                                            mDatabaseHelper.duplicateHeaderTransactionData(relatedtransid,latesttransId, String.valueOf(roomid),tableid);
                                            mDatabaseHelper.duplicateTransactionLines(latesttransId,relatedtransid);
                                            mDatabaseHelper.updateTransactionStatusoldprf(relatedtransid,"OLDPRF");
                                        }else{
                                            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,relatedtransid, String.valueOf(roomid),tableid,3);
                                            // Update the transaction ID in the header table for transactions with status "InProgress"
                                            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,relatedtransid, String.valueOf(roomid),tableid);

                                        }           
*/

                                    }
                                }
                            }
                            boolean isprf = startsWithPRF(transactionIdInProgress);

                            if (isprf) {

                                Log.d("isprfs", String.valueOf(isprf));
                                if(cashorlevel.equals("0")) {
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_TRN, transactionIdInProgress, roomid, tableid);

                                }else{
                                    mDatabaseHelper.updateTransactionHeaderStatusfornew(DatabaseHelper.TRANSACTION_STATUS_COMPLETED, transactionIdInProgress, roomid, tableid);
                                    Log.d("rlatesttransIde4" , latesttransId );
                                    Log.d("newTransactionIde4" , relatedtransid );
                                      /*  if (latesttransId.contains("-PRF-")) {
                                            //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
                                            mDatabaseHelper.duplicateHeaderTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid);
                                            mDatabaseHelper.duplicateTransactionLines(latesttransId,newTransactionId);
                                            mDatabaseHelper.updateTransactionStatusoldprf(latesttransId,"OLDPRF");
                                        }else{
                                            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
                                            // Update the transaction ID in the header table for transactions with status "InProgress"
                                            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

                                        }

                                       */
                                }



                            }

                            List<String> tableIds = extractTableIds(tableid);
                            for (String tableId : tableIds) {
                                Log.d("extractTableIds", "Table ID: " + tableId);


                                boolean paid = mDatabaseHelper.areAllTransactionsPaid(roomid, tableid);
                                Log.e("paid", String.valueOf(paid));
                                if (paid) {

                                    mDatabaseHelper.resetMergedStatusToDefault( roomid, tableId);

                                }
                            }

                        }


                        // Update the transaction status for all in-progress transactions to "Completed"

                        clearBuyerInfoFromPrefs();
                        updateTransactionStatus();
                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        // Display a pop-up with a "Thanks" message and a button
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                           /*     Intent intent = new Intent(printerSetup.this, MainActivity.class);
                               intent.putExtra("cash_return_key", cashReturn); // Replace cashReturn with your actual cash return value
                                startActivity(intent);
                                */

                                restartApp(getApplicationContext(),cashReturn);
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
    private List<String> extractTableIds(String newTableId) {
        List<String> tableIds = new ArrayList<>();

        // Split the newTableId string by the "+" symbol
        String[] tableNumbers = newTableId.split("\\+");

        for (String tableNum : tableNumbers) {
            // Trim and remove "T " prefix if it exists
            String cleanedTableNum = tableNum.trim().replace("T ", "");
            tableIds.add(cleanedTableNum);
        }

        return tableIds;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_printer);
        String anReturbed = getIntent().getStringExtra("amount_received");
        String Cashreturn = getIntent().getStringExtra("cash_return");
        tableid = getIntent().getStringExtra("tableid");
        roomid = getIntent().getStringExtra("roomid");

        SharedPreferences preferences = this.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
        roomid = String.valueOf(preferences.getInt("room_id", 0));
        tableid = String.valueOf(preferences.getString("table_id", "0"));

        if(Cashreturn != null){
            cashReturn = Double.parseDouble(Cashreturn);

        }
         settlementItems = getIntent().getParcelableArrayListExtra("settlement_items");
        mraqr = getIntent().getStringExtra("mraQR");

        SharedPreferences sharedPreferences = getSharedPreferences("PaymentPrefs", Context.MODE_PRIVATE);
        paymentmethod = sharedPreferences.getString("paymentname", null);
        Log.d("paymentmethod", paymentmethod);
        Log.d("Cashreturndada", Cashreturn);
        mDatabaseHelper = new DatabaseHelper(this);
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(roomid,tableid);
if(anReturbed !=null){
    amountReceived = Double.parseDouble(anReturbed);

}
        boolean isprf=  startsWithPRF(latesttransId);
        if(isprf) {
         newtransactionIdInProgress = generateNewTransactionId();
            Log.d("isprf1", String.valueOf(isprf));

            Log.d("newtransactionIdInProgress", String.valueOf(newtransactionIdInProgress));
            Log.d("latesttransId", String.valueOf(newtransactionIdInProgress));
            mDatabaseHelper.updateSettlementInvoiceId(latesttransId,newtransactionIdInProgress, String.valueOf(roomid),tableid);
            mDatabaseHelper.updateCashReportTransactionId(latesttransId,newtransactionIdInProgress, String.valueOf(roomid),tableid);
            // Update the transaction ID in the transaction table for transactions with status "InProgress"
            mDatabaseHelper.updateTransactionTransactionIdInProgress(latesttransId,newtransactionIdInProgress, String.valueOf(roomid),tableid);
            // Update the transaction ID in the header table for transactions with status "InProgress"
            Log.d("updateHeaderTransactionIdInProgress", String.valueOf(newtransactionIdInProgress));
            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newtransactionIdInProgress, String.valueOf(roomid),tableid);




            transactionIdInProgress=newtransactionIdInProgress;

        }else{
            transactionIdInProgress=latesttransId;
        }
        final KonfettiView konfettiView = findViewById(R.id.konfettiView);
        konfettiView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                konfettiView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0, 90)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12, 5))
                        .setPosition(-konfettiView.getWidth(), 0f, -konfettiView.getWidth(), (float)konfettiView.getHeight())
                        .streamFor(300, 4500L);
            }
        });

        final KonfettiView konfettiView1 = findViewById(R.id.konfettiView1);
        konfettiView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                konfettiView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                konfettiView1.build()
                        .addColors(Color.RED, Color.GREEN, Color.BLUE)
                        .setDirection(180, 270)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12, 5))
                        .setPosition(konfettiView1.getWidth(), 0f, konfettiView1.getWidth(),(float) konfettiView1.getHeight())
                        .streamFor(300, 4500L);
            }
        });



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

    public String generateNewTransactionId() {
        SharedPreferences sharedPreference = this.getSharedPreferences("Login", Context.MODE_PRIVATE);

        String ShopName = sharedPreference.getString("ShopName", null);
        // Retrieve the last used counter value from shared preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("TransactionCounter", Context.MODE_PRIVATE);
        int lastCounter = sharedPreferences.getInt("counter", 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateTime = dateFormat.format(new Date());

        // Increment the counter for the next transaction
        int currentCounter = lastCounter + 1;

        // Save the updated counter value in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("counter", currentCounter);
        editor.apply();

        // Extract the first three letters from companyName
        String companyLetters = ShopName.substring(0, Math.min(ShopName.length(), 3)).toUpperCase();

        String posNumberLetters = null;
        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
        if (cursorCompany != null && cursorCompany.moveToFirst()) {
            int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_POS_Num);
            PosNum= cursorCompany.getString(columnCompanyNameIndex);
            posNumberLetters = PosNum.substring(0, Math.min(PosNum.length(), 3)).toUpperCase();

        }
        // Generate the transaction ID by combining the three letters and the counter
        return currentDateTime +"-" + companyLetters + "-" + posNumberLetters +  "-" + currentCounter;
    }
    public static boolean startsWithPRF(String str) {
        if (str != null && str.contains("-PRF-")) {
            return true;
        }
        return false;
    }

    private String extractNumericPart(String tableString) {
        // Split the string based on space and return the last part
        String[] parts = tableString.split(" ");
        return parts[parts.length - 1];
    }
    private void unmergeTable(String selectedTableNum) {


        // Extract the numeric part from selectedTableToMerge
        String numericPart = extractNumericPart(selectedTableNum);

        // Update your database to unmerge the table
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues valuesTable2 = new ContentValues();
        // Update MERGED and MERGED_SET_ID columns
        values.put(DatabaseHelper.MERGED, 0);
        values.put(DatabaseHelper.MERGED_SET_ID,"0");

        int rowsUpdated = db.update(DatabaseHelper.TABLES, values, DatabaseHelper.MERGED_SET_ID + " = ?",
                new String[]{selectedTableNum});
        valuesTable2.put(DatabaseHelper.MERGED, 0);

        db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.TABLE_NUMBER + " = ?",
                new String[]{numericPart});
        db.close();

        if (rowsUpdated > 0) {
            // Table unmerged successfully
            Log.d("UnmergeTable", "Table " + selectedTableNum + " has been unmerged.");
        } else if (rowsUpdated == 0) {
            // No rows were updated
            Log.d("UnmergeTable", "Failed to unmerge table " + selectedTableNum + ". No rows were updated.");
        } else {
            // An error occurred
            Log.d("UnmergeTable", "Failed to unmerge table " + selectedTableNum + ". Error occurred during update.");
        }

        // Refresh the UI to reflect the changes
        // You may need to update your RecyclerView adapter or rerun the database query to fetch updated data


        SharedPreferences    sharedPreferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", "0").apply();
        sharedPreferences.edit().putInt("roomnum", Integer.parseInt(roomid)).apply();


    }
    public static void restartApp(Context context, double cashReturn) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("cash_return_key", cashReturn);
        ProcessPhoenix.triggerRebirth(context, intent);
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

    private void clearBuyerInfoFromPrefs() {
        SharedPreferences sharedPrefs = this.getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
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


    public void updateTransactionStatus() {

        // Retrieve the SharedPreferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get the SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all the stored data in SharedPreferences
        editor.clear();

        // Apply the changes
        editor.apply();



    }


    public static boolean isSameSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_SAME_SESSION, false);
    }
}
