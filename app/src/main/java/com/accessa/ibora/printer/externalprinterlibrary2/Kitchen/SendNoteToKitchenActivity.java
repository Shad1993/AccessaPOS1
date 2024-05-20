package com.accessa.ibora.printer.externalprinterlibrary2.Kitchen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.printer.externalprinterlibrary2.PrinterListAdapter;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.sales.ticket.Transaction;
import com.sunmi.externalprinterlibrary2.ConnectCallback;
import com.sunmi.externalprinterlibrary2.ResultCallback;
import com.sunmi.externalprinterlibrary2.SearchCallback;
import com.sunmi.externalprinterlibrary2.SearchMethod;
import com.sunmi.externalprinterlibrary2.SunmiPrinterManager;
import com.sunmi.externalprinterlibrary2.exceptions.SearchException;
import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;
import com.sunmi.externalprinterlibrary2.style.AlignStyle;
import com.sunmi.externalprinterlibrary2.style.BarcodeType;
import com.sunmi.externalprinterlibrary2.style.CloudPrinterStatus;
import com.sunmi.externalprinterlibrary2.style.ErrorLevel;
import com.sunmi.externalprinterlibrary2.style.HriStyle;
import com.sunmi.externalprinterlibrary2.style.ImageAlgorithm;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SendNoteToKitchenActivity extends AppCompatActivity implements PrinterListAdapter.OnItemClickListener, SearchCallback {
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private PrinterListAdapter adapter;
    private TicketAdapter adapter1;
    private int method;
    int roomid;
    private DatabaseHelper mDatabaseHelper;
    String tableid;
        private  String noteText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Retrieve the text from the Intent
         noteText = getIntent().getStringExtra("note_text");
        SharedPreferences preferences = this.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = String.valueOf(preferences.getString("table_id", "0"));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.search_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDatabaseHelper = new DatabaseHelper(getApplicationContext()); // Initialize DatabaseHelper

        RecyclerView recyclerView = findViewById(R.id.search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrinterListAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        // Initiate USB search
        method = SearchMethod.USB;
        search();
    }

    private void search() {
        try {
            SunmiPrinterManager.getInstance().searchCloudPrinter(this, method, this);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SunmiPrinterManager.getInstance().stopSearch(this, method);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(CloudPrinter cloudPrinter) {
        // Handle selected device and perform connection and printing tasks
        connectAndPrint(cloudPrinter);
    }

    private void connectAndPrint(CloudPrinter cloudPrinter) {
        cloudPrinter.connect(this, new ConnectCallback() {
            @Override
            public void onConnect() {
                // Connection successful, perform printing tasks
               printTasks(cloudPrinter,noteText);

            }

            @Override
            public void onFailed(String s) {
                // Connection failed, show error message
                Toast.makeText(SendNoteToKitchenActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisConnect() {
                // Handle disconnect if needed
            }
        });
    }

    private void printTasks(CloudPrinter cloudPrinter,String noteText) {
        // Perform printing tasks here

        printSample( cloudPrinter,noteText);
        // Add more printing tasks if needed


    }





    public void printSample(CloudPrinter cloudPrinter,String noteText) {

        String currentDateTime = getCurrentDateTime();
        cloudPrinter.setAlignment(AlignStyle.CENTER);
        cloudPrinter.appendText("****");
        cloudPrinter.setBoldMode(true);
        cloudPrinter.setCharacterSize(2, 2);
        cloudPrinter.appendText("#T"+ tableid);
        cloudPrinter.appendText("#R"+ roomid);
        cloudPrinter.setBoldMode(false);
        cloudPrinter.setCharacterSize(1, 1);
        cloudPrinter.printText("****");
        cloudPrinter.setAlignment(AlignStyle.CENTER);

            cloudPrinter.dotsFeed(20);
            cloudPrinter.setCharacterSize(1, 2);

            cloudPrinter.initStyle();
            cloudPrinter.dotsFeed(20);
            cloudPrinter.printText(currentDateTime);
        cloudPrinter.printText("--------------------------------");

        Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactionsbytable(String.valueOf(roomid), tableid);
        if (cursor1 != null && cursor1.moveToFirst()) {
            do {
                String id = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.ITEM_ID));
                String quantity = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.QUANTITY));
                String description = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.LongDescription));
                String price = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TOTAL_PRICE));
                String comment = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_COMMENT));
                String transactionIdInProgress = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                Log.d("transactionIdInProgress", transactionIdInProgress);
                mDatabaseHelper.setTransactionSentToKitchen(transactionIdInProgress);

                String sentToKitchen = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TRANSACTION_SentToKitchen));

                Log.d("sentToKitchen", sentToKitchen);
if(sentToKitchen.equals("0")) {
    cloudPrinter.printColumnsText(new String[]{description, "X", quantity}, new int[]{26, 2, 4},
            new AlignStyle[]{AlignStyle.LEFT, AlignStyle.LEFT, AlignStyle.RIGHT});
    if(comment != null) {
        cloudPrinter.setCharacterSize(1, 1);
        cloudPrinter.printText(comment);
    }
}
                // Use the variables as needed
                // For example, you can pass them to another method or use them to populate views
            } while (cursor1.moveToNext());
        }


        cloudPrinter.lineFeed(3);
            cloudPrinter.postCutPaper(true, 0);
        cloudPrinter.commitTransBuffer(new ResultCallback() {
            @Override
            public void onComplete() {
                // Printing completed successfully
                Toast.makeText(SendNoteToKitchenActivity.this, R.string.toast_print_ok, Toast.LENGTH_LONG).show();
                // Disconnect after printing
                cloudPrinter.release(SendNoteToKitchenActivity.this);
                // Close the activity and return to the previous one
                finish();
            }

            @Override
            public void onFailed(CloudPrinterStatus cloudPrinterStatus) {
                // Printing failed, show error message on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SendNoteToKitchenActivity.this, String.format(getString(R.string.toast_print_failed), cloudPrinterStatus.name()), Toast.LENGTH_LONG).show();
                    }
                });

                // Disconnect after printing (in case of failure)
                cloudPrinter.release(SendNoteToKitchenActivity.this);
                // Close the activity and return to the previous one
                finish();
            }
        });

    }
    @Override
    public void onFound(CloudPrinter cloudPrinter) {
        runOnUiThread(() -> adapter.addData(cloudPrinter));
    }
}
