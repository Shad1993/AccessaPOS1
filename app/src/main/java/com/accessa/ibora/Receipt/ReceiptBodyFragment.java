package com.accessa.ibora.Receipt;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.bumptech.glide.Glide;

public class ReceiptBodyFragment extends Fragment {

    private boolean doubleBackToExitPressedOnce = false;
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;

private String transactionId;
    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction_details, container, false);

        // Retrieve the ID from arguments
        transactionId = getArguments().getString("id");

            Toast.makeText(getContext(), "transId" + " " + transactionId, Toast.LENGTH_SHORT).show();

            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            // Initialize the database helper
            mDatabaseHelper = new DatabaseHelper(getContext());


            // Fetch the transaction details from the database based on the transaction ID
            Cursor cursor = mDatabaseHelper.getTransactionDetails(String.valueOf(transactionId));

            // Check if the cursor contains valid data
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve the transaction details from the cursor
                String transactionDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE));
                int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));
                // ... Retrieve other transaction details


                // Set the transaction details to TextViews or any other views in your layout
                TextView transactionIdTextView = view.findViewById(R.id.transaction_id_text_view);
                TextView transactionDateTextView = view.findViewById(R.id.transaction_date_text_view);
                TextView quantityTextView = view.findViewById(R.id.quantity_text_view);
                TextView totalPriceTextView = view.findViewById(R.id.total_price_text_view);
                // ... Initialize other TextViews

                transactionIdTextView.setText(String.valueOf(transactionId));
                transactionDateTextView.setText(transactionDate);
                quantityTextView.setText(String.valueOf(quantity));
                totalPriceTextView.setText(String.valueOf(totalPrice));
                // ... Set values to other TextViews
                // Close the cursor
                cursor.close();
            } else {
                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
            }

            mDatabaseHelper = new DatabaseHelper(getContext());
            Cursor cursor1 = mDatabaseHelper.getAllTransactions(transactionId);
            mAdapter = new TicketAdapter(getContext(), cursor1);
            mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // set text
    public void setText(String item) {
        TextView view = getView().findViewById(R.id.detailsText);
        view.setText(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
    }

    // Handle back button press
    public static ReceiptBodyFragment newInstance(String id) {
        ReceiptBodyFragment fragment = new ReceiptBodyFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }
}
