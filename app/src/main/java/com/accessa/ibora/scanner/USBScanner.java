package com.accessa.ibora.scanner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Iterator;

public class USBScanner extends AppCompatActivity {

    private static final String TAG = "USBScannerActivity";
    private static final String ACTION_USB_PERMISSION = "com.accessa.ibora.USB_PERMISSION";

    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbDeviceConnection usbConnection;
    private PendingIntent permissionIntent;

    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // Permission granted, open the USB device and start scanning
                            openUsbDevice(device);
                            scan();
                        }
                    } else {
                        // Permission denied for USB device
                        Log.d(TAG, "Permission denied for USB device: " + device);

                        // Show a toast message for permission denied
                        Toast.makeText(context, "Permission denied for USB device", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the USB manager
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);

        // Request permission to access USB devices
        requestUsbPermission();

        // Update the context used for the toast messages
        Context context = this.getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }

    private void requestUsbPermission() {
        // Get a list of connected USB devices
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        // Check if any USB device is connected
        if (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            usbDevice = device;

            // Check if permission is already granted
            if (usbManager.hasPermission(device)) {
                // Permission already granted, open the USB device and start scanning
                openUsbDevice(device);
                scan();
            } else {
                // Permission not granted, request permission to access the USB device
                usbManager.requestPermission(device, permissionIntent);
            }
        } else {
            // No USB device found
            Log.d(TAG, "No USB device found");
            Toast.makeText(this, "No USB device found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openUsbDevice(UsbDevice device) {
        UsbInterface usbInterface = device.getInterface(0);
        UsbEndpoint endpoint = usbInterface.getEndpoint(0);

        // Open the USB device and claim the interface
        usbConnection = usbManager.openDevice(device);
        if (usbConnection != null && usbConnection.claimInterface(usbInterface, true)) {
            // USB device opened successfully
            Log.d(TAG, "USB device opened: " + device.getDeviceName());
            Toast.makeText(this, "USB device opened: " + device.getDeviceName(), Toast.LENGTH_SHORT).show();

            // Start listening for incoming data from the USB device
            byte[] buffer = new byte[endpoint.getMaxPacketSize()];
            int bytesRead = usbConnection.bulkTransfer(endpoint, buffer, buffer.length, 1000);
            if (bytesRead > 0) {
                // Data received from the USB device
                String barcodeData = new String(buffer, 0, bytesRead);
                Log.d(TAG, "Barcode scanned: " + barcodeData);

                // Toast the barcode value
                Toast.makeText(this, "Scanned: " + barcodeData, Toast.LENGTH_SHORT).show();
            } else {
                // No data received from the USB device
                Log.d(TAG, "No data received from USB device");
                Toast.makeText(this, "No data received from USB device", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Failed to open USB device
            Log.d(TAG, "Failed to open USB device: " + device.getDeviceName());
            Toast.makeText(this, "Failed to open USB device: " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
        }
    }


    private void scan() {
        // Initialize the barcode scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the scanning result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Scanning was canceled
                Log.d(TAG, "Scanning canceled");

                // Show a toast message for scanning canceled
                Toast.makeText(this, "Scanning canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Scanning successful, retrieve the barcode value
                String barcode = result.getContents();
                Log.d(TAG, "Barcode scanned: " + barcode);

                // Show a toast message for the scanned barcode
                Toast.makeText(this, "Scanned: " + barcode, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
