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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import woyou.aidlservice.jiuiv5.ILcdCallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class CustomerLcdFragment extends Fragment {

    private static final String TRANSACTION_TICKET_NO = "";
    private static final String POSNumber="posNumber";
    private String tableid;
    private int roomid;
    private DatabaseHelper mDatabaseHelper;
    private IWoyouService woyouService;
    private int transactionCounter = 1;
    private String cashierId,shopname,PosNum;
    private DBManager dbManager;
    private TicketClearedListener ticketclearedListener;
    private double totalAmount, taxTotalAmount;
    private String transactionIdInProgress;

    private String actualdate;
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
        // Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");

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

        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        shopname=sharedPreference.getString("ShopName",null);

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);

        dbManager = new DBManager(getContext());
        actualdate = mDatabaseHelper.getCurrentDate();
    }

    public void displayOnLCD() {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
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

    public Bitmap generateQRCode(String data, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        int matrixWidth = bitMatrix.getWidth();
        int matrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[matrixWidth * matrixHeight];
        for (int y = 0; y < matrixHeight; y++) {
            int offset = y * matrixWidth;
            for (int x = 0; x < matrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap qrCodeBitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
        qrCodeBitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);

        return qrCodeBitmap;
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
            String content = "MCB Juice!"; // Set your desired content
            int fontSize = 10; // Set your desired font size
            int textColor = Color.WHITE; // Set your desired text color
            Typeface typeface = Typeface.DEFAULT; // Set your desired font typeface

            // Generate QR code bitmap
            int qrCodeSize = 50; // Set your desired QR code size
            BitMatrix qrCodeMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
            int qrCodeWidth = qrCodeMatrix.getWidth();
            int qrCodeHeight = qrCodeMatrix.getHeight();

            Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < qrCodeWidth; x++) {
                for (int y = 0; y < qrCodeHeight; y++) {
                    qrCodeBitmap.setPixel(x, y, qrCodeMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Generate text bitmap
            Paint textPaint = new Paint();
            textPaint.setTextSize(fontSize);
            textPaint.setColor(textColor);
            textPaint.setTypeface(typeface);

            Rect textBounds = new Rect();
            textPaint.getTextBounds(content, 0, content.length(), textBounds);
            int textWidth = textBounds.width();
            int textHeight = textBounds.height();

            Bitmap textBitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
            Canvas textCanvas = new Canvas(textBitmap);
            textCanvas.drawText(content, 0, textHeight, textPaint);

            // Create composite bitmap
            int compositeWidth = qrCodeWidth + textWidth;
            int compositeHeight = Math.max(qrCodeHeight, textHeight);

            Bitmap compositeBitmap = Bitmap.createBitmap(compositeWidth, compositeHeight, Bitmap.Config.ARGB_8888);
            Canvas compositeCanvas = new Canvas(compositeBitmap);
            compositeCanvas.drawColor(Color.BLACK);

            compositeCanvas.drawBitmap(qrCodeBitmap, 0, (compositeHeight - qrCodeHeight) / 2, null);
            compositeCanvas.drawBitmap(textBitmap, qrCodeWidth, (compositeHeight - textHeight) / 2, null);

            woyouService.sendLCDBitmap(compositeBitmap, null);
        } catch (RemoteException | WriterException e) {
            e.printStackTrace();
        }
    }





    public void button7(View view) {


        clearTransact();

    }
public void clearTransact(){
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
// Notify the listener that an item is added
    if (ticketclearedListener != null) {
        ticketclearedListener.onTransactionCleared();
    }
    Toast.makeText(getContext(), getText(R.string.transactioncleared), Toast.LENGTH_SHORT).show();


}
    public static Bitmap textToBitmap(String text, float textSize) {
        // Create a Paint object
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);

        // Calculate the size of the text
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);

        // Create a Bitmap of the appropriate size
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        // Draw the text onto the Bitmap
        canvas.drawText(text, 0, baseline, paint);

        // Return the Bitmap
        return image;
    }

    public double calculateTotalAmount() {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
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
    public double calculateTotalTax(int roomid, String tableid) {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
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



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TicketClearedListener) {
            ticketclearedListener = (TicketClearedListener) context;
        }

    }
    public interface TicketClearedListener {


        void onTransactionCleared();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        ticketclearedListener = null;
    }

}