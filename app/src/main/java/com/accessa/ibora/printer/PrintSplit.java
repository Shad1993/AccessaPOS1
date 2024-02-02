package com.accessa.ibora.printer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
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


public class PrintSplit extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private String tableid;
   private  double splitAmount;
    private int roomid,numberOfPeople;
    private String transactionIdInProgress;
    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount;
    private  String DateCreated,timeCreated,TransactionType,MRAQR;
    private double TenderAmount,CashReturn;
    private String cashorlevel,ShopName,LogoPath;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;
 private String    OpeningHours;
    private   String      TelNum,compTelNum,compFaxNum,TransactionTypename;
    private  String  FaxNum;
    private String paymentName,PosNum ;
    private static final String POSNumber="posNumber";
    private  ArrayList<SettlementItem> settlementItems = new ArrayList<>();
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
                    recyclerView.setLayoutManager(new LinearLayoutManager(PrintSplit.this));
                    Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactionsByType(TransactionType,transactionIdInProgress);
                    adapter = new TicketAdapter(PrintSplit.this, cursor1);
                    recyclerView.setAdapter(adapter);

                    // Get the data from the adapter
                    List<Transaction> item = adapter.getData();


                    try {
                        for (int i = 0; i < numberOfPeople; i++) {



                            // Retrieve the total amount and total tax amount from the transactionheader table

                            Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);


                            if (cursor != null && cursor.moveToFirst()) {
                                int columnIndexshopnum = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_SHOP_NO);
                                int columnIndexcashierCode = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_CASHIER_CODE);
                                int columnIndexTerminalnum = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TERMINAL_NO);


                                String ShopNumber = cursor.getString(columnIndexshopnum);
                                String cashierCode = cursor.getString(columnIndexcashierCode);
                                String TerminalNum = cursor.getString(columnIndexTerminalnum);
                                // Retrieve the total amount and total tax amount from the transactionheader table
                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfobyShopId(ShopNumber);

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
                                    OpeningHours = cursorCompany.getString(columnOpeningHoursIndex);
                                    TelNum = cursorCompany.getString(columnTelNumIndex);
                                    FaxNum = cursorCompany.getString(columnFaxIndex);
                                    compTelNum = cursorCompany.getString(columncompTelNumIndex);
                                    compFaxNum = cursorCompany.getString(columncompFaxIndex);

                                    LogoPath = cursorCompany.getString(columnLogoPathIndex);

                                    printLogoAndReceipt(service, LogoPath, 100, 100);
                                    if (TransactionType.equals("PRF")) {
                                        TransactionTypename = "Proforma";
                                    } else if (TransactionType.equals("CRN")) {
                                        TransactionTypename = "Credit Note";
                                    } else if (TransactionType.equals("DRN")) {
                                        TransactionTypename.equals("Debit Note");
                                    } else if (TransactionType.equals("Completed")) {
                                        TransactionTypename = "Duplicata";
                                    } else if (TransactionType.equals("InProgress")) {
                                        TransactionTypename = "Duplicata";
                                    }
                                    String duplicata = TransactionTypename + "\n\n\n";
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
                                    // Print the formatted company name line
                                    service.printText(duplicata, null);

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
                                Cursor itemCursor = mDatabaseHelper.getUserById(Integer.parseInt(cashierCode));
                                if (itemCursor != null && itemCursor.moveToFirst()) {
                                    int cashiernameindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME);
                                    int cashieridindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id);
                                    String Cashiername = itemCursor.getString(cashiernameindex);
                                    String Cashierid = itemCursor.getString(cashieridindex);
                                    String cashiename = "Cashier Name: " + Cashiername;
                                    String cashierid = "Cashier Id: " + Cashierid;
                                    String Posnum = "POS Number: " + TerminalNum;
                                    service.printText(cashiename + "\n", null);
                                    service.printText(cashierid + "\n", null);
                                    service.printText(Posnum + "\n", null);
                                }

                            }
                            // Print a line separator
                            String lineSeparator = "=".repeat(lineWidth);
                            String singlelineSeparator = "-".repeat(lineWidth);
                            service.printText(lineSeparator + "\n", null);

                            // Print the data from the RecyclerView

                            String itemName = "Menu Repas";
                            int itemQuantity = 1;
                            String itemprice ="Rs " + String.format("%.2f", splitAmount);

                                // Calculate the padding for the item name
                                int itemNamePadding = lineWidth - itemprice.length() - itemName.length();

                               //double numericUnitPrice = Double.parseDouble(items.getUnitPrice());
                                String formattedUnitPrice = "Rs " + String.format("%.2f", splitAmount);

                                // Create the formatted item line
                                String QuantityLine = itemQuantity + " X " + formattedUnitPrice;

                                // Create the formatted item price line
                                String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                                service.printText(itemPriceLine + "\n", null);
                                service.printText(QuantityLine + "\n", null);

                            // Print a line separator

                            service.printText(lineSeparator + "\n", null);

                            // Retrieve the total amount and total tax amount from the transactionheader table
                            Cursor cursor11 = mDatabaseHelper.getTransactionHeaderType(TransactionType, transactionIdInProgress);

                            if (cursor11 != null && cursor11.moveToFirst()) {
                                int columnIndexTotalAmount = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                                int columnIndexTotalTaxAmount = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
                                int columnIndexTimeCreated = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TIME_CREATED);
                                int columnIndexDateCreated = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED);
                                int TotalTenderindex = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_PAID);
                                int CashReturnindex = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_CASH_RETURN);
                                int QrCodeindex = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_MRA_QR);

                                totalAmount = cursor11.getDouble(columnIndexTotalAmount);
                                TaxtotalAmount = cursor11.getDouble(columnIndexTotalTaxAmount);
                                DateCreated = cursor11.getString(columnIndexDateCreated);
                                timeCreated = cursor11.getString(columnIndexTimeCreated);
                                TenderAmount = cursor11.getDouble(TotalTenderindex);
                                CashReturn = cursor11.getDouble(CashReturnindex);
                                MRAQR = cursor11.getString(QrCodeindex);


                                String formattedTotalAmount = String.format("%.2f", splitAmount);
                                String formattedTotalTAXAmount = String.format("%.2f", TaxtotalAmount);

                                String Total = getString(R.string.Total);
                                String TVA = getString(R.string.Vat);

                                String TotalValue = "Rs " + formattedTotalAmount;
                                String TotalVAT = "Rs " + formattedTotalTAXAmount;
                                int lineWidths = 38;
                                // Calculate the padding for the item name
                                int TotalValuePadding = lineWidths - TotalValue.length() - Total.length();
                                int TaxValuePadding = lineWidth - TotalVAT.length() - TVA.length();


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
                                Cursor vatCursor = mDatabaseHelper.getDistinctVATTypes1(transactionIdInProgress);
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


                            }
                            String formattedTotalTender = String.format("%.2f", TenderAmount);
                            String formattedTotalReturn = String.format("%.2f", CashReturn);


                            String TenderTotalAmount = "Total Amount Paid ";
                            String CashReturns = "Cash Return  ";
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
                            // Retrieve the total amount and total tax amount from the transactionheader table
                            Cursor cursorsetlement = mDatabaseHelper.getTransactionSettlementById(transactionIdInProgress);

                            if (cursor != null && cursor.moveToFirst()) {
                                int columnIndexPaymentName = cursorsetlement.getColumnIndex(DatabaseHelper.SETTLEMENT_PAYMENT_NAME);
                                int columnIndexPaymentAmount = cursorsetlement.getColumnIndex(DatabaseHelper.SETTLEMENT_AMOUNT);

                                if (paymentName != null) {
                                    paymentName = cursorsetlement.getString(columnIndexPaymentName);
                                    settlementAmount = cursorsetlement.getDouble(columnIndexPaymentAmount);
                                    if (paymentName.equals("POP")) {

                                        String TenderTypesLine = TenderTotalAmount + " ".repeat(Math.max(0, TenderTypesPaddingpop)) + formattedtenderpop;
                                        service.printText(TenderTypesLine + "\n", null);

                                    } else {

                                        String TenderTypesLine = TenderTotalAmount + " ".repeat(Math.max(0, TenderTypesPadding)) + formattedtender;
                                        service.printText(TenderTypesLine + "\n", null);
                                    }

                                    // Disable bold text and reset font size
                                    byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                                    service.sendRAWData(boldOffBytes, null);
                                    service.setFontSize(24, null);


                                    if (paymentName.equals("POP")) {
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

                                        boolean updated = mDatabaseHelper.insertSettlementAmount(paymentName, settlementAmount, transactionIdInProgress, PosNum, transactionDate, String.valueOf(roomid), tableid);


                                        if (updated) {

                                            //   Toast.makeText(getApplicationContext(), "Settlement amount insert for " + paymentName, Toast.LENGTH_SHORT).show();
                                        } else {
                                            //  Toast.makeText(getApplicationContext(), "Failed to insert settlement amount for " + paymentName, Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                } else {
                                    String TenderTypesLine = TenderTotalAmount + " ".repeat(Math.max(0, TenderTypesPadding)) + formattedtender;
                                    service.printText(TenderTypesLine + "\n", null);
                                    // Disable bold text and reset font size
                                    byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                                    service.sendRAWData(boldOffBytes, null);
                                    service.setFontSize(24, null);
                                }

                            } else {
                                String TenderTypesLine = TenderTotalAmount + " ".repeat(Math.max(0, TenderTypesPadding)) + formattedtender;
                                service.printText(TenderTypesLine + "\n", null);

                            }

                            if (CashReturn > 0) {
                                service.printText(singlelineSeparator + "\n", null);
                                int ReturnTypesPadding = lineWidth - formattedReturn.length() - CashReturns.length();
                                String ReturnTypesLine = CashReturns + " ".repeat(Math.max(0, ReturnTypesPadding)) + formattedReturn;
                                service.printText(ReturnTypesLine + "\n", null);

                            }
                            service.printText(lineSeparator + "\n", null);
                            service.setAlignment(1, null); // Align center
                            // Footer Text
                            String FooterText = getString(R.string.Footer_See_You);
                            String Footer1Text = getString(R.string.Footer_Have);
                            String Footer2Text = getString(R.string.Footer_OpenHours);
                            String Openinghours = Footer2Text + OpeningHours;

                            if (MRAQR != null && !MRAQR.equals("Request Failed")) {
                                service.printText("MRA Response" + "\n", null);
                                service.printQRCode(MRAQR + "\n", 2, 1, null);
                            }

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
                            int DateIDPadding = lineWidth - dateTime.length() - transactionIdInProgress.length();

                            // Create the formatted item price line
                            String DateTimeIdLine = dateTime + " ".repeat(Math.max(0, DateIDPadding)) + transactionIdInProgress;

                            // Print  date and time
                            service.printText(DateTimeIdLine + "\n\n", null);

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

                                        // Remove the trailing comma and space
                                        String Drawer = vatTypesBuilder.toString().trim();

                                    }

                                }
                            }

                            // Cut the paper
                            service.cutPaper(null);

                            // Open the cash drawer
                            byte[] openDrawerBytes = new byte[]{0x1B, 0x70, 0x00, 0x32, 0x32};
                            service.sendRAWData(openDrawerBytes, null);
                            // Open the cash drawer

                            if (CashReturn > 0) {
                                service.openDrawer(null);

                            }

                        }
                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        // Display a pop-up with a "Thanks" message and a button
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(PrintSplit.this, MainActivity.class);
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

        // Initialize SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");
        // Retrieve the extras from the intent
        Intent intent = getIntent();
        if (intent != null) {
            transactionIdInProgress = intent.getStringExtra("transactionId");
            TransactionType = intent.getStringExtra("transactionType");
            numberOfPeople = intent.getIntExtra("numberOfPeople",1);
            splitAmount = intent.getDoubleExtra("amountsplitted",0.0);

        }
        // Initialize the DatabaseHelper
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

