package com.accessa.ibora.sales.ticket;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.bumptech.glide.Glide;

public class TicketFragment extends Fragment  {
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
private double totalAmount,TaxtotalAmount;
    private   FrameLayout emptyFrameLayout;
    private  String ItemId;
private String transactionIdInProgress;
    private SoundPool soundPool;
    private int soundId;

    private static final String TRANSACTION_ID_KEY = "transaction_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_fragment, container, false);

        refreshData( totalAmount,  TaxtotalAmount);
        updateAmounts();

// Initialize the SoundPool and load the sound file
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        soundId = soundPool.load(getActivity(), R.raw.beep, 1);

// Play the sound effect when an item is added
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            }
        });

     //   SendToHeader(totalAmount,TaxtotalAmount);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactions();

        mAdapter = new TicketAdapter(getActivity(), cursor1);
        mRecyclerView.setAdapter(mAdapter);





        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);




        // Load the data based on the transaction ID
        if (transactionIdInProgress != null) {
            Cursor cursor2 = mDatabaseHelper.getTransactionsByStatusAndId(DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS, transactionIdInProgress);
            mAdapter.swapCursor(cursor2);
        }

        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(getContext())
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

         emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }


        // Retrieve the checkout button and set its click listener
        Button checkoutButton = view.findViewById(R.id.buttonCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = calculateTotalAmount();
                double TaxtotalAmount = calculateTotalTax();
                updateTransactionStatus(totalAmount,TaxtotalAmount);
                // Get the activity associated with the fragment
                AppCompatActivity activity = (AppCompatActivity) requireActivity();

                // Create and show the dialog fragment with the data
                validateticketDialogFragment dialogFragment = validateticketDialogFragment.newInstance(transactionIdInProgress);
                dialogFragment.setTargetFragment(TicketFragment.this, 0);
                dialogFragment.show(activity.getSupportFragmentManager(), "validate_transaction_dialog");
            }
        });


        // Retrieve the checkout button and set its click listener
        Button saveButton = view.findViewById(R.id.saveTicket);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = calculateTotalAmount();
                double TaxtotalAmount = calculateTotalTax();
                SaveTransaction(totalAmount,TaxtotalAmount);
            }
        });


        // Set item click listener for RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Get the activity associated with the fragment
                        AppCompatActivity activity = (AppCompatActivity) requireActivity();

                        TextView LongDescTextView = view.findViewById(R.id.Longdescription_text_view);
                        TextView QuantityEditText = view.findViewById(R.id.quantity_text_view);
                        TextView PriceEditText = view.findViewById(R.id.price_text_view);
                        TextView itemIdEditText = view.findViewById(R.id.id_text_view);



                        String LongDesc = LongDescTextView.getText().toString();
                        String Quatity = QuantityEditText.getText().toString();
                        String Price = PriceEditText.getText().toString();
                         ItemId = itemIdEditText.getText().toString();


                        // Create and show the dialog fragment with the data
                        ModifyItemDialogFragment dialogFragment = ModifyItemDialogFragment.newInstance(Quatity, Price, LongDesc,ItemId);
                        dialogFragment.setTargetFragment(TicketFragment.this, 0);
                        dialogFragment.show(activity.getSupportFragmentManager(), "modify_item_dialog");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );

        return view;
    }
    private void updateTransactionStatus(double totalAmount, double TaxtotalAmount) {

        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount);



        // Update the transaction status for all in-progress transactions to "Completed"
      //  mDatabaseHelper.updateAllTransactionsStatus(DatabaseHelper.TRANSACTION_STATUS_COMPLETED);

  /*
        // Retrieve the SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get the SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all the stored data in SharedPreferences
        editor.clear();

        // Apply the changes
        editor.apply();*/
    }



    private void SaveTransaction(double totalAmount,double TaxtotalAmount) {
        // Update the transaction status for all in-progress transactions to "saved"
        mDatabaseHelper.updateAllTransactionsStatus(DatabaseHelper.TRANSACTION_STATUS_Saved);

        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount);


    }

    private double calculateTotalAmount() {
        // Implement your logic to calculate the total amount
        double totalAmount = 0.0;

        // Iterate through the cursor or any other data source to calculate the total amount

        return totalAmount;
    }
    private double calculateTotalTax() {
        // Implement your logic to calculate the total amount
        double TaxtotalAmount = 0.0;

        // Iterate through the cursor or any other data source to calculate the total amount

        return TaxtotalAmount;
    }
public void updateheader(double totalAmount, double TaxtotalAmount){
    // Get the current date and time
    String currentDate = mDatabaseHelper.getCurrentDate();
    String currentTime = mDatabaseHelper.getCurrentTime();

    // Calculate the total HT_A (priceWithoutVat) and total TTC (totalAmount)
    double totalHT_A = calculateTotalAmount();
    double totalTTC = totalAmount;

    // Get the total quantity of items in the transaction
    int quantityItem = mDatabaseHelper.calculateTotalItemQuantity();

    // Retrieve the cashier ID from SharedPreferences

    // Save the transaction details in the TRANSACTION_HEADER table
    boolean success = mDatabaseHelper.updateTransactionHeader(
            transactionIdInProgress,
            totalAmount,
            currentDate,
            currentTime,
            totalHT_A,
            totalTTC,
            quantityItem
    );

    if (success) {
        // Transaction header saved successfully
        Toast.makeText(getContext(), "Transaction updated", Toast.LENGTH_SHORT).show();
    } else {
        // Failed to save transaction header, handle the error
        Toast.makeText(getContext(), "Failed to update transaction header", Toast.LENGTH_SHORT).show();
    }

}
    public void refreshData(double totalAmount, double TaxtotalAmount) {
        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
        mAdapter = new TicketAdapter(getActivity(), cursor);
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();


        // Scroll to the last item in the RecyclerView
        int itemCount = mAdapter.getItemCount();
        if (itemCount > 0) {
            mRecyclerView.smoothScrollToPosition(itemCount - 1);
        }

        // Show or hide the RecyclerView and empty view based on the cursor count
        if (cursor != null && cursor.getCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        }

        // Update the tax and total amount TextViews
        TextView taxTextView = getView().findViewById(R.id.textViewVAT);
        String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
        taxTextView.setText(getString(R.string.tax) + ": Rs " + formattedTaxAmount);

        TextView totalAmountTextView = getView().findViewById(R.id.textViewTotal);
        String formattedTotalAmount = String.format("%.2f", totalAmount);
        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);


        updateheader(totalAmount,TaxtotalAmount);

        // Play the sound effect
        playSoundEffect();
    }
    private void updateAmounts() {
        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper

        // Retrieve the total amount and total tax amount from the transactionheader table
        Cursor cursor = mDatabaseHelper.getTransactionHeader(transactionIdInProgress);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
            int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

            totalAmount = cursor.getDouble(columnIndexTotalAmount);
            TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

            Toast.makeText(getContext(), "total" + totalAmount, Toast.LENGTH_SHORT).show();
            // Update the tax and total amount TextViews
            TextView taxTextView = getView().findViewById(R.id.textViewVAT);
            String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
            taxTextView.setText(getString(R.string.tax) + ": Rs " + formattedTaxAmount);

            TextView totalAmountTextView = getView().findViewById(R.id.textViewTotal);
            String formattedTotalAmount = String.format("%.2f", totalAmount);
            totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
        }
        if (cursor != null) {
            cursor.close(); // Close the cursor if it's not null
        }
    }
    private void playSoundEffect() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
