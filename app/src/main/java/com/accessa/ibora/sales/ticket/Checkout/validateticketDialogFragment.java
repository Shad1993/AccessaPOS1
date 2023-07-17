package com.accessa.ibora.sales.ticket.Checkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.QR.QRGridAdapter;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class validateticketDialogFragment extends DialogFragment {
    private DBManager Xdatabasemanager;
    private RecyclerView mRecyclerView;
    private CheckoutGridAdapter mAdapter;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private static final String ARG_Transaction_id = "transactionId";
    private static final String POSNumber="posNumber";
    private String Transaction_Id,PosNum;
    private String cashierId;
    private double cashReturn;
    private Set<String> selectedItems; // Store selected items
    private TextView IDselectedQRTextView;
    private double totalAmount, TaxtotalAmount;
    private DatabaseHelper mDatabaseHelper;
    private LinearLayout containerLayout; // Added
    private  Button validateButton;

    private View view; // Declare the view variable

    public static validateticketDialogFragment newInstance(String transactionId) {
        validateticketDialogFragment fragment = new validateticketDialogFragment();
        Bundle args = new Bundle();

        args.putString(ARG_Transaction_id, transactionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedItems = new HashSet<>(); // Initialize the set

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_validate_transaction, null);

        containerLayout = view.findViewById(R.id.container_layout); // Initialize the container layout

        int numberOfColumns = 2;
        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor3 = mDatabaseHelper.getAllPaymentMethod();

        mAdapter = new CheckoutGridAdapter(getContext(), cursor3);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idTextView = view.findViewById(R.id.id_text_view);
                TextView nameTextView = view.findViewById(R.id.name_text_view);
                TextView qrTextView = view.findViewById(R.id.icon);

                String id = idTextView.getText().toString();
                String qrCode = qrTextView.getText().toString();
                String name = nameTextView.getText().toString();
                if (id != null && !selectedItems.contains(id)) {
                    selectedItems.add(id);
                    // Create a new TextView
                    TextView textView = new TextView(getContext());
                    textView.setText(name); // Set the text for the new TextView
                    textView.setGravity(Gravity.CENTER);

                    // Add the TextView to the container layout
                    containerLayout.addView(textView);
                    String amount = " Rs 0.00";
                    // Create a new EditText
                    EditText editText = new EditText(getContext());
                    editText.setHint(amount);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    // Create layout parameters for centering the EditText
                    LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                            500,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    editParams.gravity = Gravity.CENTER;

                    // Apply the layout parameters to the EditText
                    editText.setLayoutParams(editParams);

                    // Set other properties of the EditText
                    Drawable drawable = getResources().getDrawable(R.drawable.edittext1_rounded_bg);
                    editText.setBackground(drawable);
                    editText.setTextColor(getResources().getColor(R.color.BleuAccessaText));
                    editText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                    // Add a TextWatcher to the EditText
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            calculateTotalAmount();
                        }
                    });

                    // Add the EditText to the container layout
                    containerLayout.addView(editText);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click
                // ...
            }
        }));

        Xdatabasemanager = new DBManager(getContext());
        Xdatabasemanager.open();

        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(getContext());

        // Get the arguments passed to the dialog fragment
        Bundle args = getArguments();
        if (args != null) {
            Transaction_Id = args.getString(ARG_Transaction_id);

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

            SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
            cashierId = sharedPreference.getString("cashorId", null);

            mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper

            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                totalAmount = cursor.getDouble(columnIndexTotalAmount);
                TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                TextView totalAmountTextView = view.findViewById(R.id.textViewAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);
                totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
            }
        }

        // Add this code inside the onCreateDialog() method of validateticketDialogFragment
        EditText amountReceivedEditText = view.findViewById(R.id.editAbbrev);
         validateButton = view.findViewById(R.id.buttonCash);

        amountReceivedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountReceivedString = s.toString();
                double amountReceived = 0.0;

                if (!amountReceivedString.isEmpty()) {
                    try {
                        amountReceived = Double.parseDouble(amountReceivedString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                double totalAmountEntered = 0.0;

                // Iterate over the container layout to calculate the total amount entered
                for (int i = 0; i < containerLayout.getChildCount(); i++) {
                    View childView = containerLayout.getChildAt(i);

                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        String amountText = editText.getText().toString();
                        double enteredAmount = 0.0;
                        if (!amountText.isEmpty()) {
                            enteredAmount = Double.parseDouble(amountText);
                        }
                        totalAmountEntered += enteredAmount;
                    }
                }

                double remainingAmount = totalAmount - totalAmountEntered;

                TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
                TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);
                TextView textViewCashReturn = view.findViewById(R.id.textViewCashReturn);

                if (remainingAmount < 0) {
                    remainingAmount = 0;
                    remainingAmountTextView.setVisibility(View.GONE);
                    remainingTotalAmountTextView.setVisibility(View.GONE);
                    textViewCashReturn.setVisibility(View.VISIBLE);
                } else {
                    remainingAmountTextView.setVisibility(View.VISIBLE);
                    remainingTotalAmountTextView.setVisibility(View.VISIBLE);
                    textViewCashReturn.setVisibility(View.GONE);
                }

                remainingAmountTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", remainingAmount));

                TextView textViewCashReturnAmount = view.findViewById(R.id.textViewCashReturnAmount);
                TextView cashReturnTextView = view.findViewById(R.id.textViewCashReturnAmount);
                if (totalAmountEntered >= totalAmount) {
                    double cashReturn = totalAmountEntered - totalAmount;
                    cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));


                    textViewCashReturnAmount.setVisibility(View.VISIBLE);
                    cashReturnTextView.setVisibility(View.VISIBLE);
                    validateButton.setVisibility(View.VISIBLE);
                } else {
                    textViewCashReturnAmount.setVisibility(View.GONE);
                    cashReturnTextView.setVisibility(View.GONE);
                    validateButton.setVisibility(View.GONE);
                }
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printData();
            }

            private void printData() {
                // Get the amount received
                double amountReceived = 0.0;
                if (!amountReceivedEditText.getText().toString().isEmpty()) {
                    amountReceived = Double.parseDouble(amountReceivedEditText.getText().toString());
                }

                // Iterate over the container layout to get the settlement details
                ArrayList<SettlementItem> settlementItems = new ArrayList<>();
                for (int i = 0; i < containerLayout.getChildCount(); i += 2) {
                    View childView = containerLayout.getChildAt(i);
                    if (childView instanceof TextView) {
                        TextView nameTextView = (TextView) childView;
                        String paymentName = nameTextView.getText().toString();

                        View amountView = containerLayout.getChildAt(i + 1);
                        if (amountView instanceof EditText) {
                            EditText amountEditText = (EditText) amountView;
                            String amountText = amountEditText.getText().toString();
                            double settlementAmount = 0.0;
                            if (!amountText.isEmpty()) {
                                settlementAmount = Double.parseDouble(amountText);
                            }
                            settlementItems.add(new SettlementItem(paymentName, settlementAmount));
                        }
                    }
                }

// Update the table with settlement details

                // Update the table with settlement details
                for (SettlementItem settlementItem : settlementItems) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String transactionDate = dateFormat.format(new Date()); // Replace 'new Date()' with your actual transaction date

                    boolean updated = mDatabaseHelper.insertSettlementAmount(settlementItem.getPaymentName(), settlementItem.getSettlementAmount(), Transaction_Id, PosNum,transactionDate);

                    if (updated) {

                        Toast.makeText(getActivity(), "Settlement amount insert for " + settlementItem.getPaymentName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to insert settlement amount for " + settlementItem.getPaymentName(), Toast.LENGTH_SHORT).show();
                    }
                }

                // Declare a variable to hold the total amount
                double totalAmountinserted = 0.0;

                    // Iterate over the settlement items to calculate the total amount
                for (SettlementItem item : settlementItems) {
                    totalAmountinserted += item.getSettlementAmount();
                }

                // Print or use the total amount as needed


                double cashReturn= totalAmountinserted- totalAmount;
                // Pass the amount received and settlement items as extras to the print activity
                Intent intent = new Intent(getActivity(), printerSetup.class);
                intent.putExtra("amount_received", totalAmountinserted);
                intent.putExtra("cash_return", cashReturn);
                intent.putExtra("settlement_items", settlementItems);
                insertCashReturn(cashReturn,totalAmountinserted);
                startActivity(intent);
            }

        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }




public void  insertCashReturn(double cashReturn,double totalAmountinserted){

    // Insert the cash return value to the header table
    boolean cashReturnUpdated = mDatabaseHelper.insertcashReturn(cashReturn,totalAmountinserted, Transaction_Id);
    if (cashReturnUpdated) {
        Toast.makeText(getActivity(), "Cash return inserted: " + cashReturn, Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(getActivity(), "Failed to insert cash return amount: " + cashReturn, Toast.LENGTH_SHORT).show();
    }
}
    public void returnHome() {
        Intent home_intent1 = new Intent(getActivity(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home_intent1);
    }

    private void calculateTotalAmount() {
        double totalAmountEntered = 0.0;

        // Iterate over the container layout to calculate the total amount entered
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View childView = containerLayout.getChildAt(i);

            if (childView instanceof EditText) {
                EditText editText = (EditText) childView;
                String amountText = editText.getText().toString();
                double enteredAmount = 0.0;
                if (!amountText.isEmpty()) {
                    enteredAmount = Double.parseDouble(amountText);
                }
                totalAmountEntered += enteredAmount;
            }
        }

        double remainingAmount = totalAmount - totalAmountEntered;

        TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
        TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);
        TextView textViewCashReturn = view.findViewById(R.id.textViewCashReturn);

        if (remainingAmount < 0) {
            remainingAmount = 0;
            remainingAmountTextView.setVisibility(View.GONE);
            remainingTotalAmountTextView.setVisibility(View.GONE);
            textViewCashReturn.setVisibility(View.VISIBLE);
        } else {
            remainingAmountTextView.setVisibility(View.VISIBLE);
            remainingTotalAmountTextView.setVisibility(View.VISIBLE);
            textViewCashReturn.setVisibility(View.GONE);
        }

        remainingAmountTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", remainingAmount));

        TextView textViewCashReturnAmount = view.findViewById(R.id.textViewCashReturnAmount);
        TextView cashReturnTextView = view.findViewById(R.id.textViewCashReturnAmount);
        if (totalAmountEntered >= totalAmount) {
            double cashReturn = totalAmountEntered - totalAmount;
            cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
            textViewCashReturnAmount.setVisibility(View.VISIBLE);
            cashReturnTextView.setVisibility(View.VISIBLE);
            validateButton.setVisibility(View.VISIBLE);
        } else {
            textViewCashReturnAmount.setVisibility(View.GONE);
            cashReturnTextView.setVisibility(View.GONE);
            validateButton.setVisibility(View.GONE);
        }
    }
}
