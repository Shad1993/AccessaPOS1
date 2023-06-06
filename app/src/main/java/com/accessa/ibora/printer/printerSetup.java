package com.accessa.ibora.printer;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.sales.ticket.Ticket;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.util.List;

public class printerSetup extends AppCompatActivity {
    private SunmiPrinterService sunmiPrinterService;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private String transactionIdInProgress;

    private DatabaseHelper mDatabaseHelper;
    private String   itemLine, itemLine1;
    private String cashierName,cashierId;
     private double totalAmount,TaxtotalAmount;
    private String cashorlevel;
    private TicketAdapter adapter;

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(printerSetup.this, "Printer connected", Toast.LENGTH_SHORT).show();


                    mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

                    SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    cashierId = sharedPreference.getString("cashorId", null);
                    cashierName = sharedPreference.getString("cashorName", null);
                    cashorlevel = sharedPreference.getString("cashorlevel", null);

                    // Inflate the custom print layout
                    View customPrintLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.print_layout, null);

                    // Populate the text views with the desired content
                    TextView titleTextView = customPrintLayout.findViewById(R.id.textViewTitle);
                    titleTextView.setText("Cashier Name: " + cashierName + "\n");

                    TextView contentTextView = customPrintLayout.findViewById(R.id.textViewContent);
                    contentTextView.setText("This is a sample print layout.");

                    TextView footerTextView = customPrintLayout.findViewById(R.id.textViewFooter);
                    footerTextView.setText("Footer information");

                    // Set up the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(printerSetup.this));
                    Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactions();
                    adapter = new TicketAdapter(printerSetup.this, cursor1);
                    recyclerView.setAdapter(adapter);

                    // Get the data from the adapter
                    List<Transaction> item = adapter.getData();


                    try {
                        // Print the custom layout
                        service.printText(titleTextView.getText().toString(), null);
                        service.printText(contentTextView.getText().toString(), null);
                        service.printText(footerTextView.getText().toString(), null);

                        // Print a line separator
                        int lineWidth = 48; // Adjust this value according to the width of your paper
                        String lineSeparator = "=".repeat(lineWidth);
                        service.printText(lineSeparator + "\n", null);

                        // Print the data from the RecyclerView
                        for (Transaction items : item) {
                            String itemName = items.getItemName();
                            String itemQuantity = String.valueOf(items.getItemQuantity());

                            // Calculate the padding for the item name
                            int itemNamePadding = 20 - itemName.length(); // Assuming a fixed width of 20 characters for the item name


                            // Inside the for loop where you print the data from the RecyclerView
                            String itemPrice = "Rs " + String.format("%.2f", items.getItemPrice());
                            String unitPrice ="Rs " + String.format("%.2f", items.getUnitPrice());
                            // Create the formatted item line
                            String itemLine = itemName + " ".repeat(Math.max(15, itemNamePadding))  + " ".repeat(Math.max(15, itemNamePadding))  + itemPrice;

                            // Create the formatted item line
                            String itemLine1 = itemQuantity + " X " + unitPrice ;

                            service.printText(itemLine , null);
                            service.printText(itemLine1 + "\n", null);
                        }
                        // Print a line separator

                        service.printText(lineSeparator + "\n", null);

                        // Retrieve the total amount and total tax amount from the transactionheader table
                        Cursor cursor = mDatabaseHelper.getTransactionHeader(transactionIdInProgress);
                        Toast.makeText(printerSetup.this, "id" +transactionIdInProgress, Toast.LENGTH_SHORT).show();
                        if (cursor != null && cursor.moveToFirst()) {
                            int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                            int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                            totalAmount = cursor.getDouble(columnIndexTotalAmount);
                            TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);



                            String formattedTotalAmount = String.format("%.2f", totalAmount);

                            String Total= getString(R.string.Total);
                            String TVA= getString(R.string.Vat);
                            String TotalValue= formattedTotalAmount;
                            String TotalVAT= String.valueOf(TaxtotalAmount);
                            int itemNamePadding = 20 - Total.length(); // Assuming a fixed width of 20 characters for the item name
                             itemLine = Total + " ".repeat(Math.max(15, itemNamePadding))  + " ".repeat(Math.max(15, itemNamePadding))  + TotalValue;
                             itemLine1 = TVA + " ".repeat(Math.max(15, itemNamePadding))  + " ".repeat(Math.max(15, itemNamePadding))  + TotalVAT;




                        }
                        service.printText(itemLine +"\n\n" , null);
                        service.printText(itemLine1 + "\n\n", null);
                        String Paymentmethod= "Cash";


                        service.printText(Paymentmethod + "\n\n", null);
                        service.printText(lineSeparator + "\n", null);
                        // Cut the paper
                        service.cutPaper(null);



                        // Update the transaction status for all in-progress transactions to "Completed"
                        mDatabaseHelper.updateAllTransactionsHeaderStatus(DatabaseHelper.TRANSACTION_STATUS_COMPLETED);

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
            Toast.makeText(this, "Printer service initialization successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Printer service initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the printer service
        try {
            InnerPrinterManager.getInstance().unBindService(this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            throw new RuntimeException(e);
        }
    }
}
