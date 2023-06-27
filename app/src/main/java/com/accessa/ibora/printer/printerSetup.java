package com.accessa.ibora.printer;

import static android.app.PendingIntent.getActivity;
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
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.Ticket;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.util.ArrayList;
import java.util.List;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class printerSetup extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private String transactionIdInProgress;

    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1;
    private String cashierName,cashierId;
    private double totalAmount,TaxtotalAmount;
    private  String DateCreated,timeCreated;
    private String cashorlevel,CompanyName,LogoPath;
    private TextView textViewVAT,textViewTotal;
    private TicketAdapter adapter;
    private String paymentName ;

    private  ArrayList<SettlementItem> settlementItems = new ArrayList<>();


    private double settlementAmount ;
    private double amountReceived,cashReturn;
    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {

            int lineWidth = 48; // Adjust this value according to the width of your paper
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(printerSetup.this, "Printer connected", Toast.LENGTH_SHORT).show();


                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

                    SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    cashierId = sharedPreference.getString("cashorId", null);
                    cashierName = sharedPreference.getString("cashorName", null);
                    cashorlevel = sharedPreference.getString("cashorlevel", null);
                    CompanyName = sharedPreference.getString("CompanyName", null);

                    // Inflate the custom print layout
                    View customPrintLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.print_layout, null);

                    // Populate the text views with the desired content
                    TextView titleTextView = customPrintLayout.findViewById(R.id.textViewTitle);
                    titleTextView.setText("Cashier Name: " + cashierName + "\n");

                    TextView contentTextView = customPrintLayout.findViewById(R.id.textViewContent);
                    contentTextView.setText("POS: POS1"+ "\n");

                    // Set up the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(printerSetup.this));
                    Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactions();
                    adapter = new TicketAdapter(printerSetup.this, cursor1);
                    recyclerView.setAdapter(adapter);

                    // Get the data from the adapter
                    List<Transaction> item = adapter.getData();


                    try {



                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(CompanyName);

                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                            int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_COMPANY_NAME);
                            int columnCompanyAddressIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_1);
                            int columnCompanyAddress2Index = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_ADR_2);
                            int columnCompanyVATIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_VAT_NO);
                            int columnCompanyBRNIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_BRN_NO);
                            int columnLogoPathIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Logo);
                            String companyName = cursorCompany.getString(columnCompanyNameIndex);
                            String CompanyAdress1= cursorCompany.getString(columnCompanyAddressIndex);
                            String CompanyAdress2= cursorCompany.getString(columnCompanyAddress2Index);
                            String CompanyVatNo= cursorCompany.getString(columnCompanyVATIndex);
                            String CompanyBRNNo= cursorCompany.getString(columnCompanyBRNIndex);
                            LogoPath = cursorCompany.getString(columnLogoPathIndex);

                            printLogoAndReceipt(service,LogoPath);

                            // Create the formatted company name line
                            String companyNameLine = companyName + "\n";
                            // Create the formatted companyAddress1Line  line
                            String companyAddress1Line = CompanyAdress1 + "\n\n";
                            String Add1andAdd2 = CompanyAdress1 + ", " + CompanyAdress2 + "\n";
                            String CompVatNo= getString(R.string.Vat) + "  : " + CompanyVatNo + "\n";
                            String CompBRNNo= getString(R.string.BRN) + "  : " + CompanyBRNNo + "\n";
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
                            // Print the formatted company name line
                            service.printText(companyAddress1Line, null);
                            // Print the formatted company name line
                            service.printText(companyNameLine, null);
                            service.printText(Add1andAdd2, null);
                            service.printText(CompVatNo, null);
                            service.printText(CompBRNNo, null);

                            service.setAlignment(0, null);



                        }


                        // Print the custom layout
                        service.printText(titleTextView.getText().toString(), null);
                        service.printText(contentTextView.getText().toString(), null);

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

                            String unitPrice ="Rs " + String.format("%.2f", items.getUnitPrice());


                            // Create the formatted item line

                            String QuantityLine = itemQuantity + " X " + unitPrice ;

                            // Create the formatted item price line
                            String itemPriceLine = itemName + " ".repeat(Math.max(0, itemNamePadding)) + itemprice;

                            service.printText(itemPriceLine + "\n" , null);
                            service.printText(QuantityLine + "\n", null);
                        }
                        // Print a line separator

                        service.printText(lineSeparator + "\n", null);
                        // Retrieve the total amount and total tax amount from the transactionheader table


                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursor = mDatabaseHelper.getTransactionHeader(transactionIdInProgress);

                        if (cursor != null && cursor.moveToFirst()) {
                            int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                            int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
                            int columnIndexTimeCreated = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TIME_CREATED);
                            int columnIndexDateCreated = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED);

                            totalAmount = cursor.getDouble(columnIndexTotalAmount);
                            TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                            DateCreated = cursor.getString(columnIndexDateCreated);
                            timeCreated = cursor.getString(columnIndexTimeCreated);



                            String formattedTotalAmount = String.format("%.2f", totalAmount);
                            String formattedTotalTAXAmount = String.format("%.2f", TaxtotalAmount);

                            String Total= getString(R.string.Total);
                            String TVA= getString(R.string.Vat);

                            String TotalValue= "Rs " + formattedTotalAmount;
                            String TotalVAT= "Rs " + formattedTotalTAXAmount;
                                int lineWidths= 38;
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
                            Cursor vatCursor = mDatabaseHelper.getDistinctVATTypes(transactionIdInProgress);
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
                        String formattedTotalTender = String.format("%.2f", amountReceived);
                        String formattedTotalReturn = String.format("%.2f", cashReturn);



                        String TenderTotalAmount= "Total Amount Paid "  ;
                        String CashReturn = "Cash Return  "  ;
                        String formattedtender= "Rs "  + formattedTotalTender;
                        String formattedReturn="Rs " + formattedTotalReturn;






                        int lineWidths= 41;
                        int TenderTypesPadding = lineWidths - formattedtender.length() - TenderTotalAmount.length();
                        service.printText(singlelineSeparator + "\n", null);

                        // Enable bold text and set font size to 30
                        byte[] boldOnBytes = new byte[]{0x1B, 0x45, 0x01};
                        service.sendRAWData(boldOnBytes, null);
                        service.setFontSize(28, null);


                        String TenderTypesLine = TenderTotalAmount + " ".repeat(Math.max(0, TenderTypesPadding)) + formattedtender;
                        service.printText(TenderTypesLine + "\n", null);


                        // Disable bold text and reset font size
                        byte[] boldOffBytes = new byte[]{0x1B, 0x45, 0x00};
                        service.sendRAWData(boldOffBytes, null);
                        service.setFontSize(24, null);


                        if (settlementItems != null) {
                            // Use the settlementItems as needed
                            // You can iterate over the list and access the payment name and settlement amount for each item
                            for (SettlementItem items : settlementItems) {

                                paymentName = items.getPaymentName();
                                settlementAmount = items.getSettlementAmount();
                                String formattedSettlementAmount = String.format("%.2f", settlementAmount);
                                String TenderAmount= "Amount Paid in  " + paymentName ;
                                String formatteditemtender= "Rs "  + formattedSettlementAmount;
                                int settlementTypesPadding = lineWidth - formatteditemtender.length() - TenderAmount.length();
                                String TenderItemsTypesLine = TenderAmount + " ".repeat(Math.max(0, settlementTypesPadding)) + formatteditemtender;
                                service.printText(TenderItemsTypesLine + "\n", null);
                            }
                        }

                        if(cashReturn >0) {
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


                        // Print the centered footer text
                        service.printText(FooterText + "\n", null);


                        // Print the centered footer1 text
                        service.printText(Footer1Text + "\n", null);



                        // Print the centered footer2 text
                        service.printText(Footer2Text + "\n", null);
                        service.setAlignment(0, null); // Align left
                        // Concatenate the formatted date and time
                        String dateTime = DateCreated + " " + timeCreated;
                        // Calculate the padding for the item name
                        int DateIDPadding = lineWidth - dateTime.length() - transactionIdInProgress.length();

                        // Create the formatted item price line
                        String DateTimeIdLine = dateTime + " ".repeat(Math.max(0, DateIDPadding)) + transactionIdInProgress;

                        // Print  date and time
                        service.printText(DateTimeIdLine + "\n\n", null);

                        // Cut the paper
                        service.cutPaper(null);

                        // Open the cash drawer
                        byte[] openDrawerBytes = new byte[]{0x1B, 0x70, 0x00, 0x32, 0x32};
                        service.sendRAWData(openDrawerBytes, null);
                        // Open the cash drawer
                        if(paymentName == "Cash") {
                            service.openDrawer(null);

                        }

                        // Update the transaction status for all in-progress transactions to "Completed"
                        mDatabaseHelper.updateAllTransactionsHeaderStatus(DatabaseHelper.TRANSACTION_STATUS_COMPLETED);

                        updateTransactionStatus();
                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity != null) {
                            mainActivity.onTransationCompleted();
                        }

                        // Display a pop-up with a "Thanks" message and a button
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(printerSetup.this);
                                builder.setTitle("Transaction Completed");
                                builder.setMessage("Thanks for Using AccessaPOS. Next customer?");
                                builder.setPositiveButton("Next Customer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(printerSetup.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
                                        startActivity(intent);
                                    }
                                });
                                builder.setCancelable(false); // Prevent dialog from being dismissed by clicking outside
                                builder.show();
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
                    Toast.makeText(printerSetup.this, "Printer disconnected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_printer);
         amountReceived = getIntent().getDoubleExtra("amount_received", 0.0);
         cashReturn = getIntent().getDoubleExtra("cash_return", 0.0);


         settlementItems = getIntent().getParcelableArrayListExtra("settlement_items");



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
            Toast.makeText(this, "Printer service initialization successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Printer service initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void printLogoAndReceipt(SunmiPrinterService service, String LogoPath) {
        try {
            // Check if the service is connected
            if (service == null) {
                Toast.makeText(this, "Printer service is not connected", Toast.LENGTH_SHORT).show();
                return;
            }

            // Provide the path to the logo image
            String logoPath = LogoPath;

            // Load the logo image as a Bitmap with options that preserve transparency
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap logoBitmap = BitmapFactory.decodeFile(logoPath, options);

            if (logoBitmap == null) {
                Toast.makeText(this, "Failed to load company logo", Toast.LENGTH_SHORT).show();
                return;
            }

            // Resize the logo image to fit the receipt width
            int receiptWidth = 384; // Adjust this value according to your receipt paper width
            Bitmap resizedLogoBitmap = Bitmap.createScaledBitmap(logoBitmap, receiptWidth, logoBitmap.getHeight(), true);

            // Print the logo image
            InnerResultCallback innerResultCallback = null;
            service.setAlignment(1, innerResultCallback); // Center alignment
            service.printBitmap(resizedLogoBitmap, null);
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


    private void updateTransactionStatus() {

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
