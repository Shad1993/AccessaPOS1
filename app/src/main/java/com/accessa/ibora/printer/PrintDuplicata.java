package com.accessa.ibora.printer;


import static android.app.PendingIntent.getActivity;
import static com.accessa.ibora.product.items.DatabaseHelper.PREFERENCE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.STATUS_KEY;

import java.nio.charset.Charset;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptActivity;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.SplittedTicketAdapter;
import com.accessa.ibora.sales.ticket.Ticket;
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


public class PrintDuplicata extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private String tableid;
    private int roomid;
    private String transactionIdInProgress;
    private boolean printable ;
    private String printerpartname ;
    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount,WOTaxtotalAmount;
    private  String DateCreated,timeCreated,TransactionType,MRAQR;
    private double TenderAmount,CashReturn;
    private String cashorlevel,ShopName,LogoPath;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;
    private SplittedTicketAdapter SplittedAdapter;

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
                    recyclerView.setLayoutManager(new LinearLayoutManager(PrintDuplicata.this));
                    RecyclerView splittedrecyclerView = findViewById(R.id.splittedrecyclerView);
                    splittedrecyclerView.setLayoutManager(new LinearLayoutManager(PrintDuplicata.this));
                    Cursor cursor1 = mDatabaseHelper.getValidTransactionsByType(TransactionType,transactionIdInProgress);
                    Cursor cursorcleared = mDatabaseHelper.getClearedTransactionsByType(TransactionType,transactionIdInProgress);

                    Cursor cursorsplitted= mDatabaseHelper.getSplittedTransactionsByType(TransactionType,transactionIdInProgress);
                    if (TransactionType.equals("CancelledOrder")) {
                        adapter = new TicketAdapter(PrintDuplicata.this, cursorcleared);
                    }
                    else{
                        adapter = new TicketAdapter(PrintDuplicata.this, cursor1);
                    }



                    SplittedAdapter= new SplittedTicketAdapter(PrintDuplicata.this, cursorsplitted);
                    recyclerView.setAdapter(adapter);
                    splittedrecyclerView.setAdapter(SplittedAdapter);

                    // Get the data from the adapter
                    List<Transaction> item = adapter.getData();
                    List<Transaction> cleareditem = adapter.getData();
                    List<Transaction> splitteditem = SplittedAdapter.getData();

                    try {
                        List<PrinterSetupPrefs> printerSetups = mDatabaseHelper.getPrinterSetups(getApplicationContext());

                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursor = mDatabaseHelper.getTransactionHeaderType(TransactionType,transactionIdInProgress);

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


                                for (PrinterSetupPrefs setup : printerSetups) {
                                    printerpartname = setup.getName();
                                    printable = setup.isDrawerOpens();

                                    if( printerpartname.equals("Logo") && printable) {

                                        printLogoAndReceipt(service, LogoPath, 600, 300);

                                    }
                                }
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
                                else if (TransactionType.equals("OLDPRF")) {
                                    TransactionTypename = "Proforma";
                                }
                                else if (TransactionType.equals("CancelledOrder")) {
                                    TransactionTypename = "Cancelled Order";
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
                            Cursor itemCursor = mDatabaseHelper.getUserById(Integer.parseInt(cashierCode));

                            if (itemCursor != null && itemCursor.moveToFirst()) {
                                int cashiernameindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME);
                                int cashieridindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id);
                                int roomnumber=mDatabaseHelper.getRoomIdByTransactionTicketNo(transactionIdInProgress);
                                int tablenumber= mDatabaseHelper.getTableIdByTransactionTicketNo(transactionIdInProgress);
                                String Cashiername = itemCursor.getString(cashiernameindex);
                                String Cashierid = itemCursor.getString(cashieridindex);
                                Log.d("transactionIdInProgress", transactionIdInProgress);
                                int actualcopyprinted= mDatabaseHelper.getCopyPrinted(transactionIdInProgress);
                                mDatabaseHelper.updateCopyPrinted(transactionIdInProgress,actualcopyprinted);
                                int newactualcopyprinted= mDatabaseHelper.getCopyPrinted(transactionIdInProgress);
                               String status=mDatabaseHelper.getOrderTypeByTransactionTicketNo(transactionIdInProgress);
                                int covers=mDatabaseHelper.getNumberOfCovers(transactionIdInProgress);
                                String relatedtransid= mDatabaseHelper.getRelatedTransactionId(transactionIdInProgress);
                                String RelTrID= "Related Trans Id: " + relatedtransid + "\n";
                                String cashiename = "Cashier Name: " + Cashiername+ "\n";
                                String cashierid = "Cashier Id: " + Cashierid+ "\n";
                                String Posnum = "POS Number: " + TerminalNum+ "\n";
                                String room= "Room Number: " + roomnumber + "\n";
                                String table= "Table Number: " + tablenumber +"\n";
                                String Printed= "Copy Printed: " + newactualcopyprinted +"\n";
                                String SalesType= "Sales Type -" + status +"\n";
                                String coversnum= "Number Of Servings: " + covers + "\n";
                                for (PrinterSetupPrefs setup : printerSetups) {
                                    printerpartname = setup.getName();
                                    printable = setup.isDrawerOpens();

                                    if (printerpartname.equals("Cashior name") && printable) {
                                        service.printText(cashiename, null);
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
                                        service.printText(Posnum, null);
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
                            }


                        }
                            // Print a line separator
                            String lineSeparator = "=".repeat(lineWidth);
                            String singlelineSeparator = "-".repeat(lineWidth);
                            service.printText(lineSeparator + "\n", null);

                        // Print the data from the RecyclerView
                        for (Transaction items : item) {
                            String itemName = items.getItemName();
                            int itemQuantity = items.getItemQuantity();
                            String itemprice ="Rs " + String.format("%.2f", items.getItemPrice());

                            // Calculate the padding for the item name
                            int itemNamePadding = lineWidth - itemprice.length() - itemName.length();


                            // Inside the for loop where you print the data from the RecyclerView


                            double numericUnitPrice = Double.parseDouble(items.getUnitPrice());
                            String formattedUnitPrice = "Rs " + String.format("%.2f", numericUnitPrice);


                            // Create the formatted item line

                            String QuantityLine = itemQuantity + " X " + formattedUnitPrice ;

                            // Create the formatted item price line
                            String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                            service.printText(itemPriceLine + "\n" , null);
                            service.printText(QuantityLine + "\n", null);
                        } if (cursorsplitted != null && cursorsplitted.moveToFirst()) {

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


                                // Inside the for loop where you print the data from the RecyclerView
                                // Inside the for loop where you print the data from the RecyclerView
                                String newid = String.valueOf(items.getId());
                                Log.e("newid1", String.valueOf(itemid));
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
                                service.printText(QuantityLine + "\n", null);
                            }
                        }
                        // Print a line separator

                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table


                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursor11 = mDatabaseHelper.getTransactionHeaderType(TransactionType,transactionIdInProgress);

                        if (cursor11 != null && cursor11.moveToFirst()) {
                            int columnIndexTotalAmount = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);

                            int columnIndexTotalAmountWOTaxAmount = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_HT_A);
                            int columnIndexTimeCreated = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TIME_CREATED);
                            int columnIndexDateCreated = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED);
                            int TotalTenderindex = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_PAID);
                            int CashReturnindex = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_CASH_RETURN);
                            int QrCodeindex = cursor11.getColumnIndex(DatabaseHelper.TRANSACTION_MRA_QR);

                            totalAmount = cursor11.getDouble(columnIndexTotalAmount);
                            WOTaxtotalAmount=cursor11.getDouble(columnIndexTotalAmountWOTaxAmount);
                            TaxtotalAmount = totalAmount -WOTaxtotalAmount;

                            DateCreated = cursor11.getString(columnIndexDateCreated);
                            timeCreated = cursor11.getString(columnIndexTimeCreated);
                            TenderAmount = cursor11.getDouble(TotalTenderindex);
                            CashReturn=cursor11.getDouble(CashReturnindex);
                            MRAQR=cursor11.getString(QrCodeindex);



                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedTotalTAXAmount = String.format("%.2f", TaxtotalAmount);
                            String AmountWitoutVat= "Amount Excluding Vat  " ;
                            String Total= getString(R.string.Total);
                            String TVA= getString(R.string.Vat);
                            double amountwvat= WOTaxtotalAmount;
                            String formattedamountwvat = String.format("%.2f", amountwvat);
                            String TotalValue= "Rs " + formattedTotalAmount;
                            String TotalVAT= "Rs " + formattedTotalTAXAmount;
                            String TotalAmounTWOVAT="Rs " +formattedamountwvat;
                            int lineWidths= 38;
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

                            String AmountWOVATLine = AmountWitoutVat + " ".repeat(Math.max(0, AmountWVatValuePadding)) + TotalAmounTWOVAT;
                            service.printText(AmountWOVATLine + "\n", null);
                        }
                        String formattedTotalTender = String.format("%.2f", TenderAmount);
                        String formattedTotalReturn = String.format("%.2f", CashReturn);



                        String TenderTotalAmount= "Total Amount Paid "  ;
                        String CashReturns = "Cash Return  "  ;
                        String formattedtender= "Rs "  + formattedTotalTender;
                        String formattedReturn="Rs " + formattedTotalReturn;



                        String formattedTotalTenderpop = String.format("%.2f", totalAmount);
                        String formattedtenderpop= "Rs "  + formattedTotalTenderpop;


                        int lineWidths= 41;
                        int TenderTypesPadding = lineWidths - formattedtender.length() - TenderTotalAmount.length();
                        int TenderTypesPaddingpop = lineWidths - formattedtenderpop.length() - TenderTotalAmount.length();
                        service.printText(singlelineSeparator + "\n", null);

// Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursorsetlement = mDatabaseHelper.getTransactionSettlementById(transactionIdInProgress);
                        Log.d("transactionIdInProgress10", transactionIdInProgress);

                        if (cursorsetlement != null && cursorsetlement.moveToFirst()) {
                            do {
                                int columnIndexPaymentName = cursorsetlement.getColumnIndex(DatabaseHelper.SETTLEMENT_PAYMENT_NAME);
                                int columnIndexPaymentAmount = cursorsetlement.getColumnIndex(DatabaseHelper.SETTLEMENT_AMOUNT);

                                if (columnIndexPaymentName != -1 && columnIndexPaymentAmount != -1) {
                                    String paymentName = cursorsetlement.getString(columnIndexPaymentName);
                                    double settlementAmount = cursorsetlement.getDouble(columnIndexPaymentAmount);
                                    Log.d("paymentName", paymentName);

                                    String formattedSettlementAmount = String.format("%.2f", settlementAmount);
                                    String TenderAmount = "Amount Paid in " + paymentName;
                                    String formatteditemtender = "Rs " + formattedSettlementAmount;
                                    int settlementTypesPadding = lineWidth - formatteditemtender.length() - TenderAmount.length();
                                    String TenderItemsTypesLine = TenderAmount + " ".repeat(Math.max(0, settlementTypesPadding)) + formatteditemtender;

                                    service.printText(TenderItemsTypesLine + "\n", null);

                                    // Additional logic for specific payment names (e.g., "POP")
                                    if (paymentName.equals("POP")) {
                                        // Additional printing or processing for "POP" payment
                                        // This block is just an example of customization
                                        service.printText("Processed POP Payment\n", null);
                                    }
                                }
                            } while (cursorsetlement.moveToNext()); // Move to the next row in the Cursor
                        } else {
                            String TenderTypesLine = TenderTotalAmount + " ".repeat(Math.max(0, TenderTypesPadding)) + formattedtender;
                            service.printText(TenderTypesLine + "\n", null);
                        }

// Close the Cursor to release resources
                        if (cursorsetlement != null) {
                            cursorsetlement.close();
                        }




                        if(CashReturn >0) {
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
                        String Openinghours= Footer2Text + OpeningHours ;


                        if (MRAQR != null && !MRAQR.startsWith("Request Failed")) {
                            service.printText("MRA Transaction Id: "+ transactionIdInProgress + "\n", null);

                            // Decode Base64 string to byte array
                            byte[] imageBytes;
                            try {
                                imageBytes = android.util.Base64.decode(MRAQR, android.util.Base64.DEFAULT);
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



                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        // Display a pop-up with a "Thanks" message and a button
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(PrintDuplicata.this, ReceiptActivity.class);

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
        tableid = String.valueOf(preferences.getString("table_id", "0"));
        // Retrieve the extras from the intent
        Intent intent = getIntent();
        if (intent != null) {
            transactionIdInProgress = intent.getStringExtra("transactionId");
            TransactionType = intent.getStringExtra("transactionType");

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

