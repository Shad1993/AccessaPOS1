package com.accessa.ibora.product.couponcode;

import static android.app.DownloadManager.COLUMN_STATUS;

import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_CASHIER_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_DATE_CREATED;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_DISCOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_END_DATE;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_START_DATE;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_TIME_CREATED;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import com.accessa.ibora.printer.PrintCoupon;
import com.accessa.ibora.printer.PrintDuplicata;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.fragment.app.DialogFragment;

import com.accessa.ibora.R;

public class CouponDialogFragment extends DialogFragment {

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private EditText inputAmount;
    private String cashorId;
    private SharedPreferences sharedPreferences;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Coupon Settings");

        // Inflate the custom layout
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_coupon_dialog, null);
        builder.setView(dialogView);




        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        // Now, you can retrieve data from SharedPreferences
        cashorId = sharedPreferences.getString("cashorId", null);



        // Find UI elements
        startDatePicker = dialogView.findViewById(R.id.startDatePicker);
        endDatePicker = dialogView.findViewById(R.id.endDatePicker);
        inputAmount = dialogView.findViewById(R.id.inputAmount);


        // Set positive button and its click listener
        builder.setPositiveButton("Generate Barcode", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                // Get the entered amount
                String amount = inputAmount.getText().toString();

                // Get the start and end dates (similar to the previous code)
                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth() + 1;
                int startDay = startDatePicker.getDayOfMonth();
                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth() + 1;
                int endDay = endDatePicker.getDayOfMonth();

                // Construct the start and end date strings in the desired format
                String startDateString = String.format("%04d-%02d-%02d", startYear, startMonth, startDay);
                String endDateString = String.format("%04d-%02d-%02d", endYear, endMonth, endDay);
                // Get the selected status from the radio buttons
                String status = determineStatus(startDateString, endDateString);

                // Generate the barcode based on the selected options, amount, and dates
                String barcode = generateBarcode();

                // You can now use the 'barcode' value or save it to the database
                insertCouponData(status, amount, startDateString, endDateString,barcode,cashorId);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Set negative button and its click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    private void insertCouponData(String status, String amount, String startDate, String endDate,String code,String cashorId) {
        // Create or open the database
        SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        // Add the date_created value to the ContentValues object
        // Create a ContentValues object to hold the data
        ContentValues values = new ContentValues();
        values.put(COUPON_STATUS, status);
        values.put(COUPON_DISCOUNT, amount);
        values.put(COUPON_START_DATE, startDate);
        values.put(COUPON_END_DATE, endDate);
        values.put(COUPON_CODE, code);
        values.put(COUPON_CASHIER_ID, cashorId);

        // Add date_created and time_created values
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentDate = sdfDate.format(new Date());
        String currentTime = sdfTime.format(new Date());
        values.put(COUPON_DATE_CREATED, currentDate);
        values.put(COUPON_TIME_CREATED, currentTime);


        // Insert the data into the table
        long newRowId = db.insert(COUPON_TABLE_NAME, null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            // Data was inserted successfully
            Toast.makeText(getActivity(), "Coupon data inserted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), PrintCoupon.class);
            intent.putExtra("amount", amount);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            intent.putExtra("code", code);


            startActivity(intent);
        } else {
            // Insertion failed
            Toast.makeText(getActivity(), "Failed to insert coupon data", Toast.LENGTH_SHORT).show();
        }

        // Close the database
        db.close();
    }

    private String determineStatus(String startDate, String endDate) {
        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        if (endDate.compareTo(currentDate) < 0) {
            // End date is in the past
            return "Expired";
        } else if (startDate.compareTo(currentDate) > 0) {
            // Start date is in the future
            return "Soon Coming";
        } else {
            // Current date is within the date range
            return "Available";
        }
    }



    // Implement your barcode generation logic here
    private String generateBarcode() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }

}
