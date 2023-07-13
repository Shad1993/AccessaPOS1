package com.accessa.ibora.CustomerLcd;

import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;
import static com.accessa.ibora.product.items.DatabaseHelper._ID;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

import woyou.aidlservice.jiuiv5.IWoyouService;
import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomerLcd extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private static IWoyouService woyouService;
    private double totalAmount,TaxtotalAmount;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_lcd_settings);
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);


        mDatabaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper


    }
    public  void displayOnLCD() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                totalAmount = cursor.getDouble(columnIndexTotalAmount);
                TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);

                woyouService.sendLCDDoubleString("Total: Rs1" + formattedTotalAmount, "Tax: " + formattedTaxAmount, null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connService);
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
    public void button1(View view) {
        if(woyouService == null){
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button2(View view) {
        if(woyouService == null){
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //woyouService.sendLCDCommand(2);
            woyouService.sendLCDDoubleString("Total:€999.99","Change:¥999.99" ,null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button3(View view) {
        if(woyouService == null){
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(3);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button4(View view) {
        if(woyouService == null){
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(4);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button5(View view) {
        if(woyouService == null){
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDString("Sunmi!", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void button6(View view) {
        if(woyouService == null){
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //woyouService.sendLCDBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), null);
            String qrCodeData = "Your QR code data";
            int qrCodeWidth = 500; // Set your desired width
            int qrCodeHeight = 500; // Set your desired height
            Bitmap qrCodeBitmap = generateQRCode(qrCodeData, qrCodeWidth, qrCodeHeight);

            // Create a temporary file to store the QR code bitmap
            File tempFile = File.createTempFile("temp_qr_code", ".png", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            // Get the resource ID of the temporary file
            int resourceId = getResources().getIdentifier(tempFile.getName(), "drawable", getPackageName());

            // Send the QR code bitmap to the LCD screen
            woyouService.sendLCDBitmap(qrCodeBitmap, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
