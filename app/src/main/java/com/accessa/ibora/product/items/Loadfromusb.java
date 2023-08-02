package com.accessa.ibora.product.items;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.accessa.ibora.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Loadfromusb extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "com.example.yourapp.USB_PERMISSION";
    private UsbManager usbManager;
    private PendingIntent permissionIntent;
    private boolean permissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        // Register a BroadcastReceiver to listen for USB permission requests.
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);

        // Check if USB permission is already granted or request it if not.
        checkUsbPermission();

        // Open the file picker when the activity starts.
        openUsbFilePicker();
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // USB permission granted, now read the CSV file.
                            // Not required for file picker implementation.
                        }
                    } else {
                        // USB permission denied.
                        // Not required for file picker implementation.
                    }
                }
            }
        }
    };

    private void checkUsbPermission() {
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        for (UsbDevice usbDevice : usbDeviceList.values()) {
            if (usbManager.hasPermission(usbDevice)) {
                // USB permission already granted, read the CSV file.
                // Not required for file picker implementation.
            } else {
                // Request USB permission.
                usbManager.requestPermission(usbDevice, permissionIntent);
            }
        }
    }

    private void openUsbFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 123);
    }







    private void readCsvFileFromUsb(Uri fileUri) {
        if (fileUri != null) {
            try {
                // Check if the URI points to a local file.
                if (fileUri.getScheme().equals("file")) {
                    File csvFile = new File(fileUri.getPath());
                    if (csvFile.exists()) {
                        // Read the CSV file using a BufferedReader.
                        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // Process each line of the CSV file here.
                            // For example, you can split the line using a comma if your CSV is comma-separated.
                            String[] data = line.split(",");
                            // Now you can do something with the data array.
                        }
                        reader.close();
                    } else {
                        // The CSV file does not exist on the USB pendrive.
                        Toast.makeText(this, "CSV file not found on the USB pendrive.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // The selected file is not a local file (not on the USB pendrive).
                    Toast.makeText(this, "Please select a file from the USB pendrive.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // File selection was canceled or something went wrong.
            Toast.makeText(this, "File selection canceled or error occurred.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            // User has selected the USB pendrive.
            Uri treeUri = data.getData();
            getContentResolver().takePersistableUriPermission(treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            // Now, we can use the DocumentFile API to browse the files and folders inside the USB pendrive.
            // For simplicity, we'll just list the files at the root level.

            // Get the root DocumentFile for the USB pendrive.
            DocumentFile root = DocumentFile.fromTreeUri(this, treeUri);
            if (root != null && root.isDirectory()) {
                DocumentFile[] files = root.listFiles();
                if (files != null) {
                    for (DocumentFile file : files) {
                        // Process each file here.
                        if ("text/csv".equals(file.getType())) {
                            // If the file is a CSV file, read its content.
                            readCsvFileFromUsb(file.getUri());

                            // Show a toast message with the file name.
                            Toast.makeText(this, "Selected file: " + file.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            // Now, after processing the selected files, open the file picker again to let the user select another file.
            openUsbFilePicker();
        } else {
            // File selection was canceled or something went wrong.
            Toast.makeText(this, "File selection canceled or error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }
}
