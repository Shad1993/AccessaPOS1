package com.accessa.ibora.sales.ticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.ModifyItemActivity;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.bumptech.glide.Glide;

public class TicketFragment extends Fragment  {
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;

    private   FrameLayout emptyFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_fragment, container, false);





        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();

        mAdapter = new TicketAdapter(getActivity(), cursor);
        mRecyclerView.setAdapter(mAdapter);


        // Retrieve the SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if there is a transaction ID in SharedPreferences
        String transactionIdInProgress = sharedPreferences.getString("transaction_id", null);

        // Load the data based on the transaction ID
        if (transactionIdInProgress != null) {
            Cursor cursor1 = mDatabaseHelper.getTransactionsByStatusAndId(DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS, transactionIdInProgress);
            mAdapter.swapCursor(cursor1);
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


        // Retrieve the delete button and set its click listener
        Button checkoutButton = view.findViewById(R.id.buttonCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = calculateTotalAmount();
                updateTransactionStatus(totalAmount);
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
                        String ItemId = itemIdEditText.getText().toString();


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
    private void updateTransactionStatus(double totalAmount) {
        // Update the transaction status for all in-progress transactions to "Completed"
        mDatabaseHelper.updateAllTransactionsStatus(DatabaseHelper.TRANSACTION_STATUS_COMPLETED);

        // Refresh the data in the RecyclerView
        refreshData(totalAmount);

        // Retrieve the SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get the SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all the stored data in SharedPreferences
        editor.clear();

        // Apply the changes
        editor.apply();
    }


    private double calculateTotalAmount() {
        // Implement your logic to calculate the total amount
        double totalAmount = 0.0;

        // Iterate through the cursor or any other data source to calculate the total amount

        return totalAmount;
    }


    public void refreshData(double totalAmount) {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
        // Show or hide the RecyclerView and empty view based on the cursor count
        if (cursor != null && cursor.getCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        }
        TextView totalAmountTextView = getView().findViewById(R.id.textViewSubtotal);
        totalAmountTextView.setText(String.valueOf(totalAmount));
    }
}
