package com.accessa.ibora.sales.Sales;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SalesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ItemGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private  String transactionStatus;
    private ItemAddedListener itemAddedListener;

    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private String transactionIdInProgress; // Transaction ID for "InProgress" status
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_fragment, container, false);

        int numberOfColumns = 6;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);




        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllItems();

        mAdapter = new ItemGridAdapter(getContext(), cursor);
        mRecyclerView.setAdapter(mAdapter);

        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(this)
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                TextView idTextView = view.findViewById(R.id.id_text_view);
                TextView subjectEditText = view.findViewById(R.id.name_text_view);
                TextView longDescriptionEditText = view.findViewById(R.id.Longdescription_text_view);
                TextView priceTextView = view.findViewById(R.id.price_text_view);

                String id = idTextView.getText().toString();
                String title = subjectEditText.getText().toString();
                String longDescription = longDescriptionEditText.getText().toString();
                String price = priceTextView.getText().toString();
                String transactionId;



                String transactionStatus = "InProgress";

                if (transactionStatus.equals("InProgress")) {
                    if (transactionIdInProgress == null) {
                        transactionIdInProgress = generateNewTransactionId(); // Generate a new transaction ID for "InProgress" status
                        // Store the transaction ID in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TRANSACTION_ID_KEY, transactionIdInProgress);
                        editor.apply();
                    }
                    transactionId = transactionIdInProgress;
                } else {
                    transactionId = generateNewTransactionId(); // Generate a new transaction ID
                }

                // Get the current date and time for the transaction
                String transactionDate = getCurrentDateTime();

                // Insert/update the transaction into the Transaction table
                int itemId = Integer.parseInt(id);
                double totalPrice = Double.parseDouble(price);
                String vat = String.valueOf(2);

                // Check if the item with the same ID is already selected
                Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId);
                if (cursor != null && cursor.moveToFirst()) {
                    // Retrieve the existing transaction ID for the item
                    String existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));

                    // Compare the existing transaction ID with the one in SharedPreferences
                    if (existingTransactionId.equals(transactionIdInProgress)) {
                        // Item already selected, update the quantity and total price
                        int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));
                        double currentTotalPrice = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));

                        int newQuantity = currentQuantity + 1;
                        double newTotalPrice = currentTotalPrice + totalPrice;

                        mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice);
                    } else {
                        // Item has a different transaction ID, insert a new transaction
                        mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, totalPrice, Double.parseDouble(vat), longDescription, transactionStatus);
                    }
                } else {
                    // Item not selected, insert a new transaction with quantity 1 and total price
                    mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, totalPrice, Double.parseDouble(vat), longDescription, transactionStatus);
                }


                if (cursor != null) {
                    cursor.close();
                }

                // Notify the listener that an item is added
                if (itemAddedListener != null) {
                    itemAddedListener.onItemAdded();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click
                // ...
            }
        }));

        return view;

    }
    private String generateNewTransactionId() {
        // Implement your logic to generate a unique transaction ID
        // For example, you can use a combination of timestamp and a random number
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(10000);
        return "TXN-" + timestamp + "-" + random;
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ItemAddedListener) {
            itemAddedListener = (ItemAddedListener) context;
        }
    }
    public interface ItemAddedListener {
        void onItemAdded();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        itemAddedListener = null;
    }
}
