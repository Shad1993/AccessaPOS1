package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.category.CategoryDatabaseHelper._ID;
import static com.accessa.ibora.product.items.DatabaseHelper.AvailableForSale;
import static com.accessa.ibora.product.items.DatabaseHelper.Barcode;
import static com.accessa.ibora.product.items.DatabaseHelper.Category;
import static com.accessa.ibora.product.items.DatabaseHelper.Cost;
import static com.accessa.ibora.product.items.DatabaseHelper.DESC;
import static com.accessa.ibora.product.items.DatabaseHelper.DateCreated;
import static com.accessa.ibora.product.items.DatabaseHelper.Department;
import static com.accessa.ibora.product.items.DatabaseHelper.ExpiryDate;
import static com.accessa.ibora.product.items.DatabaseHelper.Image;
import static com.accessa.ibora.product.items.DatabaseHelper.LastModified;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.Name;
import static com.accessa.ibora.product.items.DatabaseHelper.Price;
import static com.accessa.ibora.product.items.DatabaseHelper.Quantity;
import static com.accessa.ibora.product.items.DatabaseHelper.SKU;
import static com.accessa.ibora.product.items.DatabaseHelper.SoldBy;
import static com.accessa.ibora.product.items.DatabaseHelper.SubDepartment;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.UserId;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.Variant;
import static com.accessa.ibora.product.items.DatabaseHelper.Weight;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.accessa.ibora.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Loadfromusb extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "com.example.yourapp.USB_PERMISSION";
    private UsbManager usbManager;
    private PendingIntent permissionIntent;
    private List<String[]> csvData = new ArrayList<>();
    private boolean permissionGranted = false;
    private static final int PICK_FILE_REQUEST = 1;
    private int successfulInsertions = 0;
    private ProgressBar progressBar;
    private void insertCsvDataToDatabase() {
        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Execute the database operations in a background thread
        new InsertCsvDataTask().execute(csvData.toArray(new String[0][]));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_from_usb);

        progressBar = findViewById(R.id.progressBar);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);

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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Set the MIME type to any file type
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }



    private void readCsvFileFromUsb(Uri fileUri) {
        if (fileUri != null) {
            try {
                // Get the InputStream from the content URI.
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                if (inputStream != null) {
                    // Read the CSV file using a BufferedReader.
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    boolean isFirstRow = true;
                    int currentLine = 0;

                    // Clear the previous csvData list
                    csvData.clear();

                    while ((line = reader.readLine()) != null) {
                        if (isFirstRow) {
                            isFirstRow = false;
                            continue; // Skip the first row (header row).
                        }
                        // Split the CSV line using the appropriate separator (e.g., comma).
                        String[] data = parseCsvLine(line); // Use a custom parser
                        csvData.add(data); // Add the parsed data to the csvData list.
                        Log.d("CSV", line);
                        currentLine++;

                        // Update the progress bar
                        int progress = (int) (((float) currentLine / csvData.size()) * 100);
                        progressBar.setProgress(progress);
                    }
                    reader.close();
                    // Now csvData contains the parsed CSV data in a list of string arrays.

                    // Update the total items TextView
                    TextView totalItemsTextView = findViewById(R.id.totalItemsTextView);
                    totalItemsTextView.setText("Total items in CSV: " + csvData.size());
                } else {
                    // Failed to open the input stream.
                    Toast.makeText(this, "Failed to open CSV file.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Error occurred while reading the file.
                Toast.makeText(this, "Error reading CSV file.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // File selection was canceled or something went wrong.
            Toast.makeText(this, "File selection canceled or error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentToken = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentToken.toString().trim());
                currentToken = new StringBuilder();
            } else {
                currentToken.append(c);
            }
        }

        // Add the last token
        values.add(currentToken.toString().trim());

        return values.toArray(new String[0]);
    }



    private class InsertCsvDataTask extends AsyncTask<String[], Integer, Integer> {
        private int totalLines;
        private int currentLine;
        private int remainingDataCount;
        private int duplicateBarcodeCount;
        @Override
        protected Integer doInBackground(String[]... data) {
        // Create a DatabaseHelper instance.
        DatabaseHelper dbHelper = new DatabaseHelper(Loadfromusb.this);

        // Get a writable database instance.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Get the current date and time as a default value for DateCreated and LastModified.
        String currentDate = getCurrentDateTime();

            // Calculate the total number of lines to be inserted
            totalLines = data.length;
            currentLine = 0;
            remainingDataCount = totalLines;
            duplicateBarcodeCount = 0;
            int successfulInsertions = 0;

            try {
                for (String[] rowData : data) {
                    String barcode = rowData[0];

                    // Check if the barcode already exists in the database.
                    Cursor cursor = db.query(TABLE_NAME, null, Barcode + "=?", new String[]{barcode}, null, null, null);
                    boolean exists = cursor.moveToFirst();
                    cursor.close();
                    // Update the count of duplicate barcodes
                    if (exists) {
                        duplicateBarcodeCount++;
                    }
                    ContentValues values = new ContentValues();
                    values.put(Barcode, barcode);
                    values.put(Name, rowData[1]);
                    values.put(DESC, rowData[2]);
                values.put(Category, rowData[3]);
                values.put(Quantity, rowData[4]);
                values.put(Department, rowData[5]);
                values.put(LongDescription, rowData[6]);
                values.put(SubDepartment, rowData[7]);
                values.put(Price, rowData[8]);
                values.put(VAT, rowData[9]);
                values.put(ExpiryDate, rowData[10]);
                values.put(AvailableForSale, rowData[11]);
                values.put(SoldBy, rowData[12]);
                values.put(Image, rowData[13]);
                values.put(SKU, rowData[14]);
                values.put(Variant, rowData[15]);
                values.put(Cost, rowData[16]);
                values.put(Weight, rowData[17]);
                values.put(UserId, rowData[18]);
                values.put(DateCreated, rowData[19] != null ? rowData[19] : currentDate); // Use currentDate if DateCreated is null
                values.put(LastModified, rowData[20] != null ? rowData[20] : currentDate); // Use currentDate if LastModified is null

                if (exists) {
                    // Update the existing record with the same barcode.
                    db.update(TABLE_NAME, values, Barcode + "=?", new String[]{barcode});
                } else {
                    // Insert a new record.
                    db.insert(TABLE_NAME, null, values);
                }

                    currentLine++;
                    successfulInsertions++;

                    // Update the progress
                    publishProgress((int) (((float) currentLine / totalLines) * 100));
                }
            } finally {
                // Close the database to free up resources.
                db.close();
            }

            return successfulInsertions;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length > 1 && values[0] == -1) {
                // Show an error message for incorrectly formatted data
                int lineNumber = values[1];

                String errorMessage = "Error in CSV format at line " + lineNumber;
                showErrorDialog(errorMessage);
            } else {
                // Update the progress bar
                progressBar.setProgress(values[0]);

                // Calculate the remaining data count
                remainingDataCount = totalLines - currentLine;

                // Update the UI with the counts
                updateCountUI();
            }

            // Update the TextView with the successful insertions count
            TextView successfulDataTextView = findViewById(R.id.successfulDataTextView);
            successfulDataTextView.setText("Successfully inserted data count: " + successfulInsertions);
        }



        @Override
        protected void onPostExecute(Integer successfulInsertions) {
            super.onPostExecute(successfulInsertions);
            // Database insertion/update is complete.
            Toast.makeText(Loadfromusb.this, "CSV data inserted/updated in the database.", Toast.LENGTH_SHORT).show();
            // Hide the progress bar when the task is complete
            progressBar.setVisibility(View.INVISIBLE);

            // Update the TextView with the successful insertions count
            TextView successfulDataTextView = findViewById(R.id.successfulDataTextView);
            successfulDataTextView.setText("Successfully inserted data count: " + successfulInsertions);
        }

        private void updateCountUI() {
            // Update the TextViews with the counts of remaining data and duplicate barcodes
            TextView remainingDataTextView = findViewById(R.id.remainingDataTextView);
            TextView duplicateBarcodeTextView = findViewById(R.id.duplicateBarcodeTextView);

            String remainingDataText = "Remaining data to insert: " + remainingDataCount;
            String duplicateBarcodeText = "Duplicate barcode count: " + duplicateBarcodeCount;

            remainingDataTextView.setText(remainingDataText);
            duplicateBarcodeTextView.setText(duplicateBarcodeText);
        }
    }
    private String getCurrentDateTime() {
        // Get the current date and time as a formatted string (you can adjust the format as needed).
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            // Do something with the selected file URI (e.g., read, copy, process, etc.)
            // Here, we'll just show a toast message with the file name.

            readCsvFileFromUsb(fileUri); // Read the CSV file and parse its content.
            insertCsvDataToDatabase();
            String fileName = getFileNameFromUri(fileUri);
            Toast.makeText(this, "Selected file: " + fileName, Toast.LENGTH_SHORT).show();


        }
    }
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return fileName;
    }
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }
}
