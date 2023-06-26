package com.accessa.ibora.sales.ticket.Checkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.Locale;

public class validateticketDialogFragment extends DialogFragment {
    private DBManager Xdatabasemanager;
    private RecyclerView mRecyclerView;
    private CheckoutGridAdapter mAdapter;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private static final String ARG_Transaction_id = "transactionId";
    private   String Transaction_Id;
    private String cashierId;
    private double totalAmount,TaxtotalAmount;
    private DatabaseHelper mDatabaseHelper;

    public static validateticketDialogFragment newInstance( String transactionId) {
        validateticketDialogFragment fragment = new validateticketDialogFragment();
        Bundle args = new Bundle();

        args.putString(ARG_Transaction_id, transactionId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_validate_transaction, null);

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
                TextView NameEditText = view.findViewById(R.id.name_text_view);
                TextView QrEditText = view.findViewById(R.id.code);



                String id1 = idTextView.getText().toString();
                String id = idTextView.getText().toString();
                String QR = QrEditText.getText().toString();
                String name = NameEditText.getText().toString();


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
            Cursor cursor = mDatabaseHelper.getTransactionHeader(Transaction_Id);
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
        Button validateButton = view.findViewById(R.id.buttonCash);

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
                TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
                TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);
                TextView textViewCashReturn= view.findViewById(R.id.textViewCashReturn);

                double remainingAmount = totalAmount - amountReceived;
                if (remainingAmount < 0) {
                    remainingAmount = 0;
                    remainingAmountTextView.setVisibility(View.GONE);
                    remainingTotalAmountTextView.setVisibility(View.GONE);
                    validateButton.setVisibility(View.VISIBLE);
                }else{
                    remainingAmountTextView.setVisibility(View.VISIBLE);
                    remainingTotalAmountTextView.setVisibility(View.VISIBLE);
                    validateButton.setVisibility(View.GONE);
                }

                remainingAmountTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", remainingAmount));

                TextView textViewCashReturnAmount =view.findViewById(R.id.textViewCashReturnAmount);
                TextView cashReturnTextView = view.findViewById(R.id.textViewCashReturnAmount);
                if (amountReceived > totalAmount) {
                    double cashReturn = amountReceived - totalAmount;
                    cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
                    textViewCashReturnAmount.setVisibility(View.VISIBLE);
                    cashReturnTextView.setVisibility(View.VISIBLE);
                    textViewCashReturn.setVisibility(View.VISIBLE);
                } else {
                    textViewCashReturn.setVisibility(View.GONE);
                    cashReturnTextView.setVisibility(View.GONE);
                    textViewCashReturnAmount.setVisibility(View.GONE);
                }
            }
        });





        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                printData();
            }

            private void printData() {
                Intent intent = new Intent(getActivity(), printerSetup.class);

                // Pass the amount received and cash return values as extras
                double amountReceived = 0.0;
                if (!amountReceivedEditText.getText().toString().isEmpty()) {
                    amountReceived = Double.parseDouble(amountReceivedEditText.getText().toString());
                }
                double cashReturn = 0.0;
                if (amountReceived > totalAmount) {
                    cashReturn = amountReceived - totalAmount;
                }
                intent.putExtra("amount_received", amountReceived);
                intent.putExtra("cash_return", cashReturn);

                startActivity(intent);
            }

        });


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void deleteItem(String itemId) {
        // Perform the delete operation here
        if (Xdatabasemanager != null) {
            boolean deleted = Xdatabasemanager.deleteTransacItem(Long.parseLong(itemId));
            if (deleted) {
                Toast.makeText(getActivity(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                returnHome();
            } else {
                Toast.makeText(getActivity(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Ticket_fragment");
        startActivity(home_intent1);
    }
    public interface ModifyItemDialogListener {
        void onItemModified(String quantity, String price, String longDesc);
    }
}
