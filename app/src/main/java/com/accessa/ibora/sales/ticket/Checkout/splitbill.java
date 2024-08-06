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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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
    private StringBuilder enterednumber;
    EditText numberOfPeopleEditText;
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
        numberOfPeopleEditText = view.findViewById(R.id.editTextNumberOfPeople);
        numberOfPeopleEditText.setInputType(InputType.TYPE_NULL);
        numberOfPeopleEditText.setTextIsSelectable(true);
        enterednumber = new StringBuilder();
        // Find the number buttons and set OnClickListener
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        Button button3 = view.findViewById(R.id.button3);
        Button button4 = view.findViewById(R.id.button4);
        Button button5 = view.findViewById(R.id.button5);
        Button button6 = view.findViewById(R.id.button6);
        Button button7 = view.findViewById(R.id.button7);
        Button button8 = view.findViewById(R.id.button8);
        Button button9 = view.findViewById(R.id.button9);
        Button button0 = view.findViewById(R.id.button0);
        Button buttonbackspace = view.findViewById(R.id.buttonbackspace);
        Button buttonComma = view.findViewById(R.id.buttonComma);
        Button buttonClear = view.findViewById(R.id.buttonClear);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("0");
            }
        });

        buttonbackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackspaceButtonClick();
            }
        });

        buttonComma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick(",");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }        });




        EditText splitAmountEditText = view.findViewById(R.id.editTextSplitAmount);
        EditText splitTaxtotalAmount = view.findViewById(R.id.editTextSplitVat);

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
                    ticketFragment.refreshData(totalAmount,totalTax,"movetobottom");
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
    public void onClearButtonClick(View view) {

        onclearButtonClick();


    }
    private void onclearButtonClick() {
        // Clear the entered PIN and update the PIN EditText
        enterednumber.setLength(0);
        numberOfPeopleEditText.setText("");
        numberOfPeopleEditText.setText("");
        numberOfPeopleEditText.requestFocus();

    }
    private void onBackspaceButtonClick() {
        // Check if there are characters to remove
        if (enterednumber.length() > 0) {
            // Remove the last character from the entered barcode
            enterednumber.deleteCharAt(enterednumber.length() - 1);
            numberOfPeopleEditText.setText(enterednumber.toString());
        }
    }
    public void onNumberButtonClick(String number) {
        // Append the pressed number to the entered PIN
        enterednumber.append(number);

        // Update the PIN EditText with the entered numbers
        numberOfPeopleEditText.setText(enterednumber.toString());
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
