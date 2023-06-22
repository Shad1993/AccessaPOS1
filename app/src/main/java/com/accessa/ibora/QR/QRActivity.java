package com.accessa.ibora.QR;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.product.items.DatabaseHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import woyou.aidlservice.jiuiv5.IWoyouService;


public class QRActivity extends AppCompatActivity implements QRFragment.DataPassListener {
    private DatabaseHelper mDatabaseHelper;
    private IWoyouService woyouService;
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
            displayQROnLCD("1234","test");
        }
    };

    private void displayQROnLCD(String code, String name) {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            int fontSize = 10; // Set your desired font size
            int textColor = Color.WHITE; // Set your desired text color
            Typeface typeface = Typeface.DEFAULT; // Set your desired font typeface

            // Generate QR code bitmap
            int qrCodeSize = 50; // Set your desired QR code size
            BitMatrix qrCodeMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
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
            textPaint.getTextBounds(name, 0, name.length(), textBounds);
            int textWidth = textBounds.width();
            int textHeight = textBounds.height();

            Bitmap textBitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
            Canvas textCanvas = new Canvas(textBitmap);
            textCanvas.drawText(code, 0, textHeight, textPaint);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);


        mDatabaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper


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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connService);
    }

    @Override
    public void onDataPass(String name, String id, String QR) {
        // Handle the passed data here
        // You can start the QRActivity or perform any other action
        Intent modifyIntent = new Intent(getApplicationContext(), QRActivity.class);
        modifyIntent.putExtra("name", name);
        modifyIntent.putExtra("id", id);
        modifyIntent.putExtra("qr", QR);
        startActivity(modifyIntent);
        displayQROnLCD(QR, name);

    }
}
