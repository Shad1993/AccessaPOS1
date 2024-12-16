package com.accessa.ibora.printer;


import static com.accessa.ibora.Constants.DB_NAME;

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

import com.accessa.ibora.ItemsReport.CatDataModel;
import com.accessa.ibora.ItemsReport.DataModel;
import com.accessa.ibora.ItemsReport.ItemSummary;
import com.accessa.ibora.ItemsReport.PaymentMethodAdapter;
import com.accessa.ibora.ItemsReport.PaymentMethodDataModel;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PrintShiftReport extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private TextView CompanyName;
    private String Shopname;

    private List<DataModel> newDataList,EventList;

    private List<CatDataModel> CatDataList;
    private PaymentMethodAdapter paymentAdapter;
    private SQLiteDatabase database;
    private String cashorId;
    private String cashorName;
    private SharedPreferences sharedPreferences;
    private boolean printable ;
    private String printerpartname ;
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
    private String shiftnumber;
    private String totalTax ;
    private String totalAmountWOVat;
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
                    recyclerView.setLayoutManager(new LinearLayoutManager(PrintShiftReport.this));


                    try {
                        List<PrinterSetupPrefs> printerSetups = mDatabaseHelper.getPrinterSetups(getApplicationContext());

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
                        Cursor itemCursor = mDatabaseHelper.getUserById(Integer.parseInt(cashorId));
                        if (itemCursor != null && itemCursor.moveToFirst()) {
                            int cashiernameindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME);
                            int cashieridindex = itemCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id);

                            String Cashiername = itemCursor.getString(cashiernameindex);
                            String Cashierid = itemCursor.getString(cashieridindex);

                            SharedPreferences shardPreference = getApplicationContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                            PosNum = shardPreference.getString(POSNumber, null);
                            String Till_id = readTextFromFile("till_num.txt");

                            int covernumber= mDatabaseHelper.getSumNumberOfCoversbyreporttype(reportType);
                            String numberofcovers = "Number Of covers: " + covernumber;
                            String lastshift = "******: "+"Shift Number: " + shiftnumber+"******";

                            String cashiename = "Cashier Name: " + Cashiername;
                            String cashierid = "Cashier Id: " + Cashierid;
                            String Posnum = "POS Number: " + PosNum;
                            service.printText(lastshift + "\n\n", null);

                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if (printerpartname.equals("Cashior name") && printable) {
                                    service.printText(cashiename + "\n", null);
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
                                    service.printText(Posnum + "\n", null);
                                }
                            }

                            for (PrinterSetupPrefs setup : printerSetups) {
                                printerpartname = setup.getName();
                                printable = setup.isDrawerOpens();

                                if (printerpartname.equals("Number Of servings") && printable) {
                                    service.printText(numberofcovers + "\n", null);
                                }
                            }
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
                        newDataList = fetchDataBasedOnReportTypeAndShift(reportType, Integer.parseInt(shiftnumber));
                        // Initialize the paymentAdapter

                        int lineWidths= 48;
                        for (DataModel item : newDataList) {
                            String paymentName = item.getLongDescription();
                            String comment = item.getComment();

                            double totalAmount = item.getTotalPrice();
                                    int quantity= item.getQuantity();

                            String transactionid = item.getTransactionid();
                          ;
                            if (transactionid != null && transactionid.startsWith("CRN")) {
                                // Transaction ID starts with "CRN"
                                totalAmount=-totalAmount;
                                // Implement your logic here
                            } else {
                                // Transaction ID does not start with "CRN"
                                totalAmount=totalAmount;
                            }
                            String formattedTotalAmount = String.format("%.2f", totalAmount);


                            String formattedPaymentInfo =  paymentName + " X " + quantity ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidths - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                            if ("EVENTS".equals(paymentName) && comment != null) {

                                EventList=mDatabaseHelper.getDataForEvents(reportType, Integer.parseInt(shiftnumber));
                                for (DataModel items : EventList) {

                                    String commentsname = items.getComment();
                                    double totalAmounts = items.getTotalPrice();
                                    int quantitys= items.getQuantity();
                                    formattedTotalAmount = String.format("%.2f", totalAmounts);


                                    formattedPaymentInfo =  commentsname + " X " + quantitys ;
                                    formattedam="("+" Rs " + formattedTotalAmount+ ")";
                                    formattedPaymentInfo = "--->" + formattedPaymentInfo;
                                    remainingSpace = lineWidths - formattedPaymentInfo.length()- formattedam.length();

                                    paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                                    // Print the payment information for the current item
                                    service.printText(paddedPaymentInfo + "\n", null);
                                }
                            }
                        }
                        service.printText(lineSeparator + "\n", null);


                        String TotalCatDaily= "All Categories sold " + frequencytoday ;

                         boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        service.printText(TotalCatDaily + "\n", null);

                        // Disable bold text
                         boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        // Initialize the paymentItems list (e.g., retrieve data from the database)
                        newDataList = fetchDataBasedOnReportTypeAndShift(reportType, Integer.parseInt(shiftnumber));
                        // Initialize the paymentAdapter
                        // Initialize the paymentItems list (e.g., retrieve data from the database)
                        CatDataList = fetchCatDataBasedOnReportTypeByShift(reportType, Integer.parseInt(shiftnumber));
                        // Initialize the paymentAdapter

                         lineWidths= 48;
                        for (CatDataModel item : CatDataList) {
                            String categorycode = item.getCategorycode();
                            double totalAmount = item.getTotalPrice();
                            int quantity= item.getTotalQuantity();
                            String transactionid = item.getTransactionid();

                            if (transactionid != null && transactionid.startsWith("CRN")) {
                                // Transaction ID starts with "CRN"
                                totalAmount=-totalAmount;
                                // Implement your logic here
                            } else {
                                // Transaction ID does not start with "CRN"
                                totalAmount=totalAmount;
                            }
                            Log.d("categorycode" , String.valueOf(categorycode));
                            Log.d("quantity" , String.valueOf(quantity));
                            String CategoryName = mDatabaseHelper.getCatNameById(categorycode);

                            if (categorycode.equals("0")){

                                CategoryName="Menu Repas";
                            }
                            String formattedTotalAmount = String.format("%.2f", totalAmount);


                            String formattedPaymentInfo =  CategoryName + " X " + quantity ;
                            String formattedam=" Rs " + formattedTotalAmount;

                            int remainingSpace = lineWidths - formattedPaymentInfo.length()- formattedam.length();

                            String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedam;

                            // Print the payment information for the current item
                            service.printText(paddedPaymentInfo + "\n", null);
                        }
                        service.printText(lineSeparator + "\n", null);
                        String TotalSettlement = "Sellers(Encaissement) ";
                        boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        service.printText(TotalSettlement + "\n", null);

                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        // Get a list of all cashier IDs from the database
                     List<String> cashierIds = mDatabaseHelper.getAllCashierIds();

                        for (String cashierId : cashierIds) {

                            // Fetch cashier name by ID
                            String cashierName = mDatabaseHelper.getCashierNameById(Integer.parseInt(cashierId)).toString();
                            String SellerName = "Seller Name: " + cashierName;
                            service.printText(SellerName + "\n", null);

                            // Fetch payment method data for the current cashier
                            List<PaymentMethodDataModel> SettlementDataLists = fetchPaymentMethodDataBasedOnReportTypeAndCashierAndShiftNumber(reportType, Integer.parseInt(cashierId), Integer.parseInt(shiftnumber));
                            for (PaymentMethodDataModel item : SettlementDataLists) {
                                String paymentName = item.getPaymentName();
                                double totalAmount = item.getTotalAmount();
                                int quantity = item.getPaymentCount();

                                Log.d("paymentname", String.valueOf(paymentName));
                                Log.d("quantity", String.valueOf(quantity));

                               String formattedTotalAmount = String.format("%.2f", totalAmount);

                                String formattedPaymentInfo = paymentName + " X " + quantity;
                                String formattedAmount = " Rs " + formattedTotalAmount;

                                int remainingSpace = lineWidth - formattedPaymentInfo.length() - formattedAmount.length();

                                String paddedPaymentInfo = formattedPaymentInfo + " ".repeat(Math.max(0, remainingSpace)) + formattedAmount;

                                // Print the payment information for the current item
                                service.printText(paddedPaymentInfo + "\n", null);
                            }


                        }


                        service.printText(lineSeparator + "\n", null);
                        String TotalsalesSettlement= "Sellers(Vente) "  ;
                        boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        service.printText(TotalsalesSettlement + "\n", null);

                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        // Get a list of all cashier IDs from the database
                         cashierIds = mDatabaseHelper.getAllCashierIds();

                        for (String cashierId : cashierIds) {


                            double amountWOVat = mDatabaseHelper.getSumOfTransactionTotalHTAperShift(cashierId,reportType, Integer.parseInt(shiftnumber));
                            double grandTotal = mDatabaseHelper.getSumOfTransactionTotalTTCPerShift(cashierId,reportType, Integer.parseInt(shiftnumber));
                            double tax = grandTotal- amountWOVat;
                            String formattedTotalAmount = String.format("%.2f", grandTotal);
                            String formattedTamountWOVat = String.format("%.2f", amountWOVat);
                            String formattedtax = String.format("%.2f", tax);
                            String Total= "Total";
                            String TVA= getString(R.string.Vat);
                            String amountWoVat= "Amount W/0 VAT";
                            String TotalValueWoVat= "Rs " + formattedTamountWOVat;
                            String TotalVAT= "Rs " + formattedtax;
                            String TotalValue= "Rs " + formattedTotalAmount;
                            String cashiorname= mDatabaseHelper.getCashierNameById(Integer.parseInt(cashierId)).toString();
                            String SellerName = "Seller Name: " + cashiorname;
                            int TotalValuePadding = lineWidth - TotalValue.length() - Total.length();
                            int TaxValuePadding = lineWidth - TotalVAT.length() - TVA.length();
                            int AmountWoVatValuePadding = lineWidth - TotalValueWoVat.length() - amountWoVat.length();
                           String totalLine = Total + " ".repeat(Math.max(0, TotalValuePadding)) + TotalValue;
                            service.printText(SellerName + "\n", null);
                            service.printText(totalLine + "\n", null);



                        }
                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table
                        String MeansOfpayment= "Means Of payment "  ;

                        boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);

                        service.printText(MeansOfpayment + "\n", null);

                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);




                            double cashret = mDatabaseHelper.getSumglobalCashReturnPerShift(reportType, Integer.parseInt(shiftnumber));
                            double tenderval = mDatabaseHelper.getSumOfTotalForCashTransactionsByReportTypePerShift(reportType, Integer.parseInt(shiftnumber));
                            double cashbackval = 0.0;
                            double loans = mDatabaseHelper.getSumOfTotalForLoanTransactionsByReportTypePerShift(reportType, Integer.parseInt(shiftnumber));
                            double pickupval = mDatabaseHelper.getSumOfTotalForPickupTransactionsByReportTypeperShift(reportType, Integer.parseInt(shiftnumber));
                            double cashinval = mDatabaseHelper.getSumOfTotalForCashInTransactionsByReportTypepershift(reportType, Integer.parseInt(shiftnumber));
                        double creditnotetotal= mDatabaseHelper.getSumOfTransactionTotalTTCForCRN(reportType,Integer.parseInt(shiftnumber));

                        double amountindrawerval = (tenderval + cashinval + loans + pickupval) - (cashret + cashbackval)-creditnotetotal;
                            double cashnetval = tenderval - cashret;

                            // Format values to 2 decimal places
                            String formattedCashret = String.format("%.2f", cashret);
                            String formattedTenderval = String.format("%.2f", tenderval);
                            String formattedCashbackval = String.format("%.2f", cashbackval);
                            String formattedLoans = String.format("%.2f", loans);
                            String formattedPickupval = String.format("%.2f", pickupval);
                            String formattedCashinval = String.format("%.2f", cashinval);
                            String formattedAmountindrawerval = String.format("%.2f", amountindrawerval);
                            String formattedCashnetval = String.format("%.2f", cashnetval);
                        String formattedCreditnote = String.format("%.2f", creditnotetotal);

                            // Prepare the strings for printing
                            String cash = "CASH: Rs " + formattedTenderval;
                        String creditnote = "Refund: -Rs " + formattedCreditnote;

                        String cashreturn = "Change Returned: Rs " + formattedCashret;
                            String cashback = "CASHBACK: Rs " + formattedCashbackval;
                            String cashin = "Cash In: Rs " + formattedCashinval;
                            String loan = "Loan: Rs " + formattedLoans;
                            String pickup = "Pickup: Rs " + formattedPickupval;
                            String cashnet = "Cash Net: Rs " + formattedCashnetval;

                        String amountindrawer = "Amount In Drawer: Rs " + formattedAmountindrawerval;

                            // Print the values
                            service.printText(cash + "\n", null);
                            service.printText(cashreturn + "\n", null);
                            service.printText(cashback + "\n", null);
                            service.printText(cashin + "\n", null);
                            service.printText(loan + "\n", null);
                            service.printText(pickup + "\n", null);
                        service.printText(creditnote + "\n", null);

                        service.printText(amountindrawer + "\n\n\n", null);
                            // Enable bold text and set font size to 30
                            boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                            service.sendRAWData(boldOnBytes, null);
                            service.setFontSize(30, null);


                            service.printText(cashnet + "\n", null);


                            // Disable bold text
                            boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                            service.sendRAWData(boldOffBytes, null);
                            service.setFontSize(24, null);

                        List<PaymentMethodDataModel> SettlementDataLists = fetchPaymentMethodDataBasedOnReportTypeAndShiftNumber(reportType, Integer.parseInt(shiftnumber));
                        for (PaymentMethodDataModel item : SettlementDataLists) {
                            String paymentName = item.getPaymentName();
                            double totalAmount = item.getTotalAmount();
                            int quantity = item.getPaymentCount();

                            Log.d("paymentname", String.valueOf(paymentName));
                            Log.d("quantity", String.valueOf(quantity));
                            if(paymentName.equals("Cash")){

                                totalAmount=totalAmount- cashret;
                            }
                            String formattedTotalAmount = String.format("%.2f", totalAmount);

                            String formattedPaymentInfo = paymentName ;
                            String formattedAmount = " Rs " + formattedTotalAmount;


                            String payment= formattedPaymentInfo + formattedAmount;
                            service.printText(payment + "\n", null);

                        }


                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table
                        double grandTotal = mDatabaseHelper.getSumOfTransactionTotalTTCPerShiftwocashior(reportType, Integer.parseInt(shiftnumber));

                        double doubleval= grandTotal;
                        int covernumber= mDatabaseHelper.getSumNumberOfCoversByReportTypePerShift(reportType, Integer.parseInt(shiftnumber));
                        double average= doubleval/covernumber;
                        String formatedtotalval = String.format("%.2f", doubleval);
                        String formatedaverage = String.format("%.2f", average);
                        String averagetext=  covernumber + " Couverts :Rs " + formatedtotalval + " Average :Rs " + formatedaverage;
                        service.printText(averagetext + "\n", null);
                        service.printText(lineSeparator + "\n", null);
                        String Total= "Total";
                        String TVA= getString(R.string.Vat);
                        String amountWoVat= "Amount W/0 VAT";


                         lineWidths= 38;
                        int lineWidth = 48;
                        // Calculate the padding for the item name
                        double tax = Double.parseDouble(totalTax);
                        double amountWOVat = grandTotal- Double.parseDouble(totalTax);

                        String formattedTotalAmount = String.format("%.2f", grandTotal);
                        String formattedtax = String.format("%.2f", tax);
                        String formattedtamountWOVat = String.format("%.2f", amountWOVat);
                        String TotalValue= "Rs " + formattedTotalAmount;
                        String TotalValueWoVat= "Rs " + formattedtamountWOVat;
                        String TotalVAT= "Rs " + formattedtax;

                        int TotalValuePadding = lineWidth - TotalValueWoVat.length() - Total.length();
                        int TaxValuePadding = lineWidth - TotalVAT.length() - TVA.length();
                        int AmountWoVatValuePadding = lineWidth - TotalValueWoVat.length() - amountWoVat.length();
                        // Enable bold text and set font size to 30
                       boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(30, null);
                        int TotalValuePaddings = lineWidths - TotalValue.length() - Total.length();
                        // Print the "Total" and its value with the calculated padding
                        String totalLine = Total + " ".repeat(Math.max(0, TotalValuePaddings)) + TotalValue;

                        service.printText(totalLine + "\n", null);


                        // Disable bold text
                        boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);
                        service.setAlignment(1, null); // Align center
                        String totalamountWoVatLine = amountWoVat + " ".repeat(Math.max(0, AmountWoVatValuePadding)) + TotalValueWoVat;
                        String totaltaxLine = TVA + " ".repeat(Math.max(0, TaxValuePadding)) + TotalVAT;
                        service.printText(totalamountWoVatLine + "\n", null);
                        service.printText(totaltaxLine + "\n\n", null);
                        service.printText(lineSeparator + "\n\n", null);


                        // Cut the paper
                        service.cutPaper(null);



                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(PrintShiftReport.this, MainActivity.class);

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
        totalAmountWOVat = intent.getStringExtra("totalAmount");
        shiftnumber= intent.getStringExtra("shiftNumber");
        Log.d("shiftnumber", String.valueOf(shiftnumber));
        if (localeString != null) {
            Locale locale = Locale.forLanguageTag(localeString);
            // Use the locale as needed
            Log.d("PrintDailyReport", "Received Locale: " + locale.toString());
        }
        if (shiftnumber  != null) {

            // Use the locale as needed
            Log.d("PrintDailyReport", "Received Locale: " + shiftnumber);
        }
        if (reportType != null) {
            // Use the reportType as needed
            Log.d("PrintDailyReport", "Received Report Type: " + reportType);
        }

        if (totalTax != null) {
            // Use the totalTax as needed
            Log.d("PrintDailyReport", "Received Total Tax: " + totalTax);
        }

        if (totalAmountWOVat != null) {
            // Use the totalAmount as needed
            Log.d("PrintDailyReport", "Received Total AmountWoVat: " + totalAmount);
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

    private List<PaymentMethodDataModel> fetchPaymentMethodDataBasedOnReportTypeAndShiftNumber(String reportType,int shiftnumber) {
        // Implement the logic to fetch payment method data based on the report type and cashier ID


        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch payment method data based on report type
        // Replace the method and parameters with your actual database queries
        paymentMethodDataList = mDatabaseHelper.getPaymentMethodDataForReportTypeAndShift(reportType,shiftnumber);
        Log.d("ReportPopupDialog", "Payment Method Data: " + paymentMethodDataList.toString());

        return paymentMethodDataList;
    }
    private List<PaymentMethodDataModel> fetchPaymentMethodDataBasedOnReportTypeAndCashierAndShiftNumber(String reportType, int cashierId,int shiftnumber) {
        // Implement the logic to fetch payment method data based on the report type and cashier ID


        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch payment method data based on report type
        // Replace the method and parameters with your actual database queries
        paymentMethodDataList = mDatabaseHelper.getPaymentMethodDataForReportTypeAndShiftAndCashier(reportType,shiftnumber, cashierId);
        Log.d("ReportPopupDialog", "Payment Method Data: " + paymentMethodDataList.toString());

        return paymentMethodDataList;
    }


    private  List<DataModel> fetchDataBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        // Fetch data based on the selected report type and shift number
        return mDatabaseHelper.getDataBasedOnReportTypeAndShift(reportType, shiftNumber);
    }
    private List<CatDataModel> fetchCatDataBasedOnReportTypeByShift(String reportType,int shiftnum) {
        // Implement your logic to fetch data based on the selected report type
        // For now, return a dummy list
        List<CatDataModel> dummyDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch data based on report type
        // Replace the method and parameters with your actual database queries
        dummyDataList = mDatabaseHelper.getDataBasedOnTransactionFamilleAndShiftNumber(reportType,shiftnum);

        return dummyDataList;
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

