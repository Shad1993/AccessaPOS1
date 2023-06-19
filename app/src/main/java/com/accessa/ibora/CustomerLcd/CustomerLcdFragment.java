package com.accessa.ibora.CustomerLcd;

import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;
import static com.accessa.ibora.product.items.DatabaseHelper._ID;

import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;

import java.util.Random;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class CustomerLcdFragment extends Fragment {

    private static final String TRANSACTION_TICKET_NO = "";
    private DatabaseHelper mDatabaseHelper;
    private IWoyouService woyouService;
    private TicketClearedListener ticketclearedListener;
    private double totalAmount, taxTotalAmount;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private TicketReloadListener ticketReloadListener;
    public interface TicketReloadListener {
        void onTicketReload();
    }
    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);

            // Call the method to display on the LCD here
            displayOnLCD();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_lcd_settings, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

        mDatabaseHelper = new DatabaseHelper(requireContext()); // Initialize DatabaseHelper


        // Find and set click listeners for all buttons
        Button button1 = rootView.findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1(v);
            }
        });

        Button button2 = rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2(v);
            }
        });

        Button button3 = rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3(v);
            }
        });

        Button button4 = rootView.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4(v);
            }
        });

        Button button5 = rootView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button5(v);
            }
        });

        Button button6 = rootView.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button6(v);
            }
        });

        Button buttonclear = rootView.findViewById(R.id.buttonClear);
        buttonclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button7(v);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private void displayOnLCD() {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(transactionIdInProgress);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                totalAmount = cursor.getDouble(columnIndexTotalAmount);
                taxTotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                String formattedTaxAmount = String.format("%.2f", taxTotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);

                woyouService.sendLCDDoubleString("Total: Rs" + formattedTotalAmount, "Tax: " + formattedTaxAmount, null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().unbindService(connService);
    }
    public void button1(View view) {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button2(View view) {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        //woyouService.sendLCDCommand(2);
        displayOnLCD();
    }

    public void button3(View view) {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(3);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button4(View view) {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(4);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button5(View view) {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDString("Sunmi!", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button6(View view) {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void button7(View view) {
        // Create an instance of the DatabaseHelper class

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext()); // Replace 'context' with the appropriate context for your application

// Get a writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.putNull(TRANSACTION_TICKET_NO); // Set the field to null
        String whereClause = _ID + " = ?";
        String[] whereArgs = {String.valueOf(transactionIdInProgress)}; // Replace transactionHeaderId with the actual header ID
        db.delete(TRANSACTION_HEADER_TABLE_NAME, whereClause, whereArgs);

        // Delete corresponding data in the transaction table
        whereClause = TRANSACTION_ID + " = ?";
        String[] whereArgs1 = {String.valueOf(transactionIdInProgress)};
        db.delete(TRANSACTION_TABLE_NAME, whereClause, whereArgs1);

        // Optionally, you can notify the user or perform any other actions after clearing the transaction

        Toast.makeText(getContext(), getText(R.string.transactioncleared), Toast.LENGTH_SHORT).show();
        refreshTicketFragment();
        generateNewTransactionId();

        // Notify the listener that an item is added
        if (ticketclearedListener != null) {
            ticketclearedListener.onTransactionCleared();
        }
        // Notify the listener to reload the TicketFragment
        if (ticketReloadListener != null) {
            ticketReloadListener.onTicketReload();
        }
    }
    private void refreshTicketFragment() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            ticketFragment.refreshData(calculateTotalAmount(),calculateTotalTax());
        }
    }
    public double calculateTotalAmount() {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public double calculateTotalTax() {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
        double TaxtotalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalTaxColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalTaxColumnIndex);
                TaxtotalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return TaxtotalAmount;
    }


    private String generateNewTransactionId() {
        // Implement your logic to generate a unique transaction ID
        // For example, you can use a combination of timestamp and a random number
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(10000);
        return "TXN-" + timestamp + "-" + random;
    }

    public interface TicketClearedListener {


        void onTransactionCleared();
    }
}