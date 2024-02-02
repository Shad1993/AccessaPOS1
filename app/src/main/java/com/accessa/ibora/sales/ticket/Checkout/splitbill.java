package com.accessa.ibora.sales.ticket.Checkout;



import static com.accessa.ibora.product.items.DatabaseHelper.ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_UNIT_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT_Type;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.accessa.ibora.R;
import com.accessa.ibora.printer.PrintSplit;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class splitbill extends Dialog {

    private SharedPreferences sharedPreferences;
    private double splitAmount = 0.0,SpliVat=0.0;
    private static final String POSNumber="posNumber";
    private DatabaseHelper mDatabaseHelper;
    private String transactionid;
    private int numberOfPeople = 0;
    private double amountTextView,TaxtotalAmount;  // Add this TextView
    private TextView numberOfPeopleTextView;  // Add this TextView

    public splitbill(Context context, double amounttotal,double TaxtotalAmount,String transid) {
        super(context);
        this.amountTextView = amounttotal;
        this.TaxtotalAmount = TaxtotalAmount;
        this.transactionid = transid;
        this.numberOfPeopleTextView = numberOfPeopleTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.splitbill, null);
        setContentView(view);
        mDatabaseHelper = new DatabaseHelper(getContext());

        EditText splitAmountEditText = view.findViewById(R.id.editTextSplitAmount);
        EditText splitTaxtotalAmount = view.findViewById(R.id.editTextSplitVat);
        EditText numberOfPeopleEditText = view.findViewById(R.id.editTextNumberOfPeople);
        TextView resultTextView = view.findViewById(R.id.resultTextView);

        splitAmountEditText.setText(String.valueOf(amountTextView));
        splitTaxtotalAmount.setText(String.valueOf(TaxtotalAmount));

        Button splitButton = view.findViewById(R.id.splitButton);
        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Format the current date and time as per your requirement
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDate = dateFormat.format(new Date());
                double totalAmount = Double.parseDouble(splitAmountEditText.getText().toString());
                double totalTax = Double.parseDouble(splitTaxtotalAmount.getText().toString());
                numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                splitAmount = totalAmount / numberOfPeople;
                SpliVat = totalTax / numberOfPeople;
                SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                int roomid = preferences.getInt("roomnum", 0);
                String tableid = preferences.getString("table_id", "");

                String transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);
                StringBuilder resultTextBuilder = new StringBuilder();
                mDatabaseHelper.getWritableDatabase().delete(
                        TRANSACTION_TABLE_NAME,
                        ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_ID + " = ?",
                        new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                );
                for (int i = 0; i < numberOfPeople; i++) {

                    // Insert new records
                    ContentValues values = new ContentValues();
                    values.put(ROOM_ID, roomid);
                    values.put(TABLE_ID, tableid);
                    values.put(TRANSACTION_ID, transactionIdInProgress);
                    long uniqueItemId = System.currentTimeMillis();
                    values.put(ITEM_ID, uniqueItemId);
                    values.put(TRANSACTION_DATE, currentDate);
                    values.put(QUANTITY, 1);
                    values.put(TOTAL_PRICE, splitAmount);
                    values.put(TRANSACTION_UNIT_PRICE, splitAmount);

                    // Parse VAT and VAT_Type from existing records
                    Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery(
                            "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                                    " WHERE " + ROOM_ID + " = ? AND " +
                                    TABLE_ID + " = ? AND " +
                                    TRANSACTION_ID + " = ?",
                            new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                    );


                        values.put(VAT, SpliVat); // Replace with default value or retrieve from somewhere else
                    if(SpliVat>0) {
                        values.put(VAT_Type, "VAT 0%"); // Replace with default value or retrieve from somewhere else
                    } else{
                        values.put(VAT_Type, "VAT 15%");
                    }

                    values.put(LongDescription, "Menu Repas");

                    mDatabaseHelper.getWritableDatabase().insert(TRANSACTION_TABLE_NAME, null, values);

                    mDatabaseHelper.updateTransactionSplitType(transactionid,"Splitted");
                    // Build result text
                    resultTextBuilder.append("Menu Repas - Rs ").append(formatDecimal(splitAmount));
                    if (i < numberOfPeople - 1) {
                        resultTextBuilder.append("\n");
                    }
                }

                // Display result text
                resultTextView.setText(resultTextBuilder.toString());
                TicketFragment ticketFragment = new TicketFragment();

                if (ticketFragment != null) {
                    ticketFragment.refreshData(totalAmount,totalTax);
                }
            }
        });

        // Set an OnClickListener for the Print button
        Button printButton = view.findViewById(R.id.PrintButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add code to handle the print action
                // For example, you can call a method to print the result
                // Create an Intent to start the desired activity based on the selected type
                // Print the receipt for each person

                for (int i = 0; i < numberOfPeople; i++) {

                    // Initialize SharedPreferences
                    SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                    int roomid = preferences.getInt("roomnum", 0);
                    String tableid = preferences.getString("table_id", "");
                    System.out.println("roomid: " + roomid);
                    System.out.println("tableid: " + tableid);
                    String transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);



                   Intent intent = new Intent(getContext(), PrintSplit.class);
                    intent.putExtra("transactionId", transactionIdInProgress);
                    intent.putExtra("transactionType", "PRF");
                    intent.putExtra("numberOfPeople", numberOfPeople);
                    intent.putExtra("amountsplitted", splitAmount);
                    getContext().startActivity(intent);
                }
            }
        });
        // Initialize UI components in the dialog
        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the close button is clicked
                dismiss();
            }
        });

        // Open another dialog to get the number of persons to split



    }

    // Add this method to get the split amount
    public double getSplitAmount() {
        return splitAmount;
    }

    // Helper method to format a double to two decimal places
    private String formatDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(value);
    }
}
