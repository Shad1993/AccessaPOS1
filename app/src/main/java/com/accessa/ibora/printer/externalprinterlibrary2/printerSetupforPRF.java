package com.accessa.ibora.printer.externalprinterlibrary2;

import static com.accessa.ibora.product.items.DatabaseHelper.PREFERENCE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.STATUS_KEY;

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
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.PrintSplit;
import com.accessa.ibora.printer.PrinterSetupPrefs;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.jakewharton.processphoenix.ProcessPhoenix;
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


public class printerSetupforPRF extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private static final String TRANSACTION_ID_KEY = "transaction_id";

    private String tableid;
    private String roomid;
    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1,TransactionTypename;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount;
    private  String DateCreated,timeCreated;
    private String cashorlevel,ShopName,LogoPath;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;
    private boolean printable ;
    private String printerpartname ;

    private String    OpeningHours;
    private   String      TelNum,compTelNum,compFaxNum;
    private  String  FaxNum;
    private String paymentName,PosNum ;
    private static final String POSNumber="posNumber";
    private  ArrayList<SettlementItem> settlementItems = new ArrayList<>();


    private double settlementAmount ;
    private String mraqr ,irn,invoicetype,newtransactionid;
    private double amountReceived,cashReturn;
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
                    recyclerView.setLayoutManager(new LinearLayoutManager(printerSetupforPRF.this));
                    Cursor cursor1;
                    if (invoicetype.equals("DRN") || invoicetype.equals("CRN")) {
                        cursor1 = mDatabaseHelper.getTransactionById1(newtransactionid);
                        Log.d("room1", roomid);
                        Log.d("table1", tableid);
                        Log.d("invoicetype1", invoicetype);
                        Log.d("newtransactionid", newtransactionid);
                    }else

                    {

                        cursor1 = mDatabaseHelper.getAllInProgressTransactionsbytable(newtransactionid,String.valueOf(roomid),tableid);
                        Log.d("room11", roomid);
                        Log.d("table11", tableid);
                    }
                    adapter = new TicketAdapter(printerSetupforPRF.this, cursor1);
                    recyclerView.setAdapter(adapter);

                    // Get the data from the adapter
                    List<Transaction> item = adapter.getData();
                    Log.d("room", roomid);
                    Log.d("table", tableid);
                Log.d("trans11", item.toString());
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
                             OpeningHours= cursorCompany.getString(columnOpeningHoursIndex);
                             TelNum= cursorCompany.getString(columnTelNumIndex);
                             FaxNum= cursorCompany.getString(columnFaxIndex);
                            compTelNum= cursorCompany.getString(columncompTelNumIndex);
                            compFaxNum= cursorCompany.getString(columncompFaxIndex);

                            LogoPath = cursorCompany.getString(columnLogoPathIndex);
                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if( printerpartname.equals("Logo") && printable) {

                                    printLogoAndReceipt(service, LogoPath, 600, 300);

                                }
                            }

                            if (invoicetype.equals("PRF") || invoicetype.equals("OLDPRF")) {
                                TransactionTypename = "Proforma";
                            } else if (invoicetype.equals("CRN")) {
                                TransactionTypename = "Credit Note";
                            } else if (invoicetype.equals("DRN")) {
                                TransactionTypename="Debit Note";
                            } else if (invoicetype.equals("Completed")) {
                                TransactionTypename = "Duplicata";
                            } else if (invoicetype.equals("InProgress")) {
                                TransactionTypename = "Duplicata";
                            }
                            String duplicata = TransactionTypename + "\n\n\n";
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
                            service.setFontSize(24, null);
                            service.setAlignment(1, null);
                            // Print the formatted company name line
                            service.printText(duplicata, null);


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

                        // Print the custom layout
                        int actualcopyprinted= mDatabaseHelper.getCopyPrinted(newtransactionid);
                        mDatabaseHelper.updateCopyPrinted(newtransactionid,actualcopyprinted);
                        int newactualcopyprinted= mDatabaseHelper.getCopyPrinted(newtransactionid);
                        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                        String status = preferences.getString(STATUS_KEY, "default_value");
                        int covers=mDatabaseHelper.getNumberOfCovers(newtransactionid);
                        String relatedtransid= mDatabaseHelper.getRelatedTransactionId(newtransactionid);
                        String RelTrID= "Related Trans Id: " + relatedtransid  + "\n";
                        String cashiorname = "Cashier Name: " + cashierName + "\n";
                        String cashierid= "Cashier Id: " + cashierId ;
                        String Posnum = "POS Number: " + PosNum;
                        String room= "Room Number: " + roomid + "\n";
                        String table= "Table Number: " + tableid +"\n";
                        String Printed= "Copy Printed: " + newactualcopyprinted +"\n";
                        String SalesType= "Sales Type -" + status +"\n";
                        String coversnum= "Number Of Servings: " + covers + "\n";

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
                        // Print a line separator

                        String lineSeparator = "=".repeat(lineWidth);
                        String singlelineSeparator = "-".repeat(lineWidth);
                        service.printText(lineSeparator + "\n", null);
                        boolean areNOItemsSelected=  mDatabaseHelper.areAllItemsNotSelected(newtransactionid);
                        // Print the data from the RecyclerView
                        for (Transaction items : item) {
                            String itemName = items.getItemName();
                            int itemQuantity = items.getItemQuantity();
                            String itemprice ="Rs " + String.format("%.2f", items.getItemPrice());
                            int itemid = items.getitem_id();
                            double totalprice=items.getItemPrice();
                            double unitprice= totalprice/itemQuantity;
                            // Calculate the padding for the item name
                            int itemNamePadding = lineWidth - itemprice.length() - itemName.length();


                            // Inside the for loop where you print the data from the RecyclerView


                            double currentprice= mDatabaseHelper.getItemPrice(String.valueOf(itemid));
                            Log.e("currentprice1" , String.valueOf(currentprice));
                            double numericUnitPrice = Double.parseDouble(items.getUnitPrice());
                            String formattedUnitPrice = "Rs " + String.format("%.2f", unitprice);

                            // Create the formatted item line

                            String QuantityLine = itemQuantity + " X " + formattedUnitPrice ;

                            // Create the formatted item price line
                            String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                            service.printText(itemPriceLine + "\n" , null);
                            service.printText(QuantityLine + "\n", null);
                        }
                        // Print a line separator

                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table


                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursor = mDatabaseHelper.getTransactionHeaderdetails(newtransactionid);

                        if (cursor != null && cursor.moveToFirst()) {
                            int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                            int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
                            int columnIndexTimeCreated = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TIME_CREATED);
                            int columnIndexDateCreated = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED);



                            double totalPriceSum = mDatabaseHelper.getTotalAmount(newtransactionid, roomid, tableid);
                            double totalVATSum = mDatabaseHelper.getVAT(newtransactionid,String.valueOf(roomid), tableid);
                            Log.d("totalAmount" , String.valueOf(totalAmount));
                            Log.d("totalPriceSum" , String.valueOf(totalPriceSum));
                            totalAmount = cursor.getDouble(columnIndexTotalAmount);
                            TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                            DateCreated = cursor.getString(columnIndexDateCreated);
                            timeCreated = cursor.getString(columnIndexTimeCreated);
                            Log.d("totalAmount" , String.valueOf(totalAmount));
                            Log.d("totalPriceSum" , String.valueOf(totalPriceSum));



                            String Total = getString(R.string.Total);
                                String TVA = getString(R.string.Vat);
                            String AmountWitoutVat= "Amount Excluding Vat  " ;
                                String formattedTotalAmount = String.format("%.2f", totalPriceSum);
                                String formattedTotalTAXAmount = String.format("%.2f", totalVATSum);
                            double amountwvat= totalAmount - TaxtotalAmount;
                            String formattedamountwvat = String.format("%.2f", amountwvat);
                                String TotalValue = "Rs " + formattedTotalAmount;
                                String TotalVAT = "Rs " + formattedTotalTAXAmount;
                            String TotalAmounTWOVAT="Rs " +formattedamountwvat;
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
                                Cursor vatCursor = mDatabaseHelper.getDistinctVATTypes(newtransactionid, String.valueOf(roomid), tableid);
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



                        String TenderTotalAmount= "Total Amount Paid "  ;
                        String CashReturn = "Cash Return  "  ;
                        String formattedtender= "Rs "  + formattedTotalTender;
                        String formattedReturn="Rs " + formattedTotalReturn;



                        String formattedTotalTenderpop = String.format("%.2f", totalAmount);
                        String formattedtenderpop= "Rs "  + formattedTotalTenderpop;


                        int lineWidths= 41;
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

                        if (!"OLDPRF".equals(invoicetype) &&!"PRF".equals(invoicetype) && !"CRN".equals(invoicetype) && !"DRN".equals(invoicetype)) {
    if (settlementItems != null) {
        // Use the settlementItems as needed
        // You can iterate over the list and access the payment name and settlement amount for each item
        for (SettlementItem items : settlementItems) {

            paymentName = items.getPaymentName();
            settlementAmount = items.getSettlementAmount();
            String formattedSettlementAmount = String.format("%.2f", settlementAmount);
            String TenderAmount = "Amount Paid in  " + paymentName;
            String formatteditemtender = "Rs " + formattedSettlementAmount;
            int settlementTypesPadding = lineWidth - formatteditemtender.length() - TenderAmount.length();
            String TenderItemsTypesLine = TenderAmount + " ".repeat(Math.max(0, settlementTypesPadding)) + formatteditemtender;
            service.printText(TenderItemsTypesLine + "\n", null);

            if(cashReturn >0) {
                service.printText(singlelineSeparator + "\n", null);
                int ReturnTypesPadding = lineWidth - formattedReturn.length() - CashReturn.length();
                String ReturnTypesLine = CashReturn + " ".repeat(Math.max(0, ReturnTypesPadding)) + formattedReturn;
                service.printText(ReturnTypesLine + "\n", null);

            }
        }
    }
}else{  service.printText("" + "\n", null);

                        }



                        service.printText(lineSeparator + "\n", null);
                        service.setAlignment(1, null); // Align center
                        // Footer Text
                        String FooterText = getString(R.string.Footer_See_You);
                        String Footer1Text = getString(R.string.Footer_Have);
                        String Footer2Text = getString(R.string.Footer_OpenHours);
                        String Openinghours= Footer2Text + OpeningHours ;

                        if (mraqr != null && !mraqr.startsWith("Request Failed")) {
                            // Log the received QR code string
                            Log.d("QR_DEBUG", "Received QR Code: " + mraqr);
                            String MraFiscalised="MRA Fiscalised";
                            String MRATransid = newtransactionid;
                            service.printText(MraFiscalised + "\n", null);
                            service.setFontSize(22, null);
                            service.printText("MRA Transaction Id: "+ MRATransid + "\n", null);
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

                        service.setAlignment(0, null); // Align left
                        // Concatenate the formatted date and time
                        String dateTime = DateCreated + " " + timeCreated;
                        // Calculate the padding for the item name
                        int DateIDPadding = lineWidth - dateTime.length() - newtransactionid.length();

                        // Create the formatted item price line
                        String DateTimeIdLine = dateTime + " ".repeat(Math.max(0, DateIDPadding)) + newtransactionid;

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
                                        if (cashReturn > 0 || "true".equals(vatType)) {

                                        }
                                        vatTypesBuilder.append(vatType).append(", ");
                                    } while (DrawerCursor.moveToNext());
                                    DrawerCursor.close();

                                    // Remove the trailing comma and space
                                    String Drawer = vatTypesBuilder.toString().trim();


                                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                                    sharedPreferences.edit().putString("table_id", "0").apply();
                                    sharedPreferences.edit().putString("table_num", "0").apply();

                                    List<String> tableIds = extractTableIds(tableid);
                                    for (String tableId : tableIds) {
                                        Log.d("extractTableIds", "Table ID: " + tableId);
                                         areNOItemsSelected=  mDatabaseHelper.newareAllItemsNotSelected(roomid,tableId);
                                        Log.e("areNOItemsSelected", String.valueOf(areNOItemsSelected));
                                        if (areNOItemsSelected) {

                                            mDatabaseHelper.resetMergedStatusToDefault( roomid, tableId);

                                        }
                                    }



                                }

                            }
                        }



                        // Cut the paper
                        service.cutPaper(null);




                       String transactionType;
                        if(cashorlevel.equals("Trainee")) {

                            mDatabaseHelper.updateTransactionHeaderStatusWithoutCondition(DatabaseHelper.TRANSACTION_STATUS_TRN,newtransactionid,roomid,tableid);
                        }else{
                            mDatabaseHelper.updateTransactionHeaderStatusWithoutCondition(invoicetype,newtransactionid,roomid,tableid);
                        }


                        // Update the transaction status for all in-progress transactions to "Completed"


                        if(!"PRF".equals(invoicetype) || !"OLDPRF".equals(invoicetype)) {
                           // mDatabaseHelper.updateCoverCount(0,tableid, Integer.parseInt(roomid));

                            updateTransactionStatus();
                        }


                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        // Display a pop-up with a "Thanks" message and a button
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            // Intent intent = new Intent(printerSetupforPRF.this, MainActivity.class);

                               // startActivity(intent);

                                restartApp(getApplicationContext(),cashReturn);
                            }
                        });
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }


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
    public static void restartApp(Context context, double cashReturn) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("cash_return_key", cashReturn);
        ProcessPhoenix.triggerRebirth(context, intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_printer);
        String anReturbed = getIntent().getStringExtra("amount_received");
        String Cashreturn = getIntent().getStringExtra("cash_return");
        tableid = getIntent().getStringExtra("tableid");
        roomid = getIntent().getStringExtra("roomid");
         amountReceived = Double.parseDouble(anReturbed);
         cashReturn = Double.parseDouble(Cashreturn);

        System.out.println("transacid!" + roomid );
         settlementItems = getIntent().getParcelableArrayListExtra("settlement_items");
        mraqr = getIntent().getStringExtra("mraQR");
        irn = getIntent().getStringExtra("MRAIRN");
        invoicetype = getIntent().getStringExtra("invoicetype");
        newtransactionid= getIntent().getStringExtra("newtransactionid");

        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);

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

}
