package com.accessa.ibora.Receipt;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;

public class TransactionDetailsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private long transactionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        String transactionId = intent.getStringExtra("id");

        // Initialize the database helper
        mDatabaseHelper = new DatabaseHelper(this);


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
            TextView transactionIdTextView = findViewById(R.id.transaction_id_text_view);
            TextView transactionDateTextView = findViewById(R.id.transaction_date_text_view);
            TextView quantityTextView = findViewById(R.id.quantity_text_view);
            TextView totalPriceTextView = findViewById(R.id.total_price_text_view);
            // ... Initialize other TextViews

            transactionIdTextView.setText(String.valueOf(transactionId));
            transactionDateTextView.setText(transactionDate);
            quantityTextView.setText(String.valueOf(quantity));
            totalPriceTextView.setText(String.valueOf(totalPrice));
            // ... Set values to other TextViews
            // Close the cursor
            cursor.close();
        }else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor cursor1 = mDatabaseHelper.getAllTransactions(transactionId);
        mAdapter = new TicketAdapter(this, cursor1);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        mDatabaseHelper.close();
    }
}
