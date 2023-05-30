package com.accessa.ibora.sales.Sales;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.items.ModifyItemActivity;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SalesFragment extends Fragment  {
    private RecyclerView mRecyclerView;
    private ItemGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private DBManager xDb;
    private List<Item> mSelectedItems;
    private HashMap<String, Integer> selectedItemQuantities = new HashMap<>();


    private int itemid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_fragment, container, false);

        int numberOfColumns = 6;

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

        mSelectedItems = new ArrayList<>();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView subjectEditText = view.findViewById(R.id.name_text_view);
                        TextView longDescriptionEditText = view.findViewById(R.id.Longdescription_text_view);
                        TextView priceTextView = view.findViewById(R.id.price_text_view);

                        String id = idTextView.getText().toString();
                        String title = subjectEditText.getText().toString();
                        String LongDescription = longDescriptionEditText.getText().toString();
                        String price = priceTextView.getText().toString();

                        // Get the current date and time for the transaction
                        String transactionDate = getCurrentDateTime();

                        // Insert the transaction into the Transaction table
                        int itemId = Integer.parseInt(id);
                        String longDescription = LongDescription;

                        double totalPrice = Double.parseDouble(price);
                        String vat = String.valueOf(2);

                        // Check if the item with the same ID is already selected
                        Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId);
                        if (cursor != null && cursor.moveToFirst()) {
                            // Item already selected, update the quantity and total price
                            int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));
                            double currentTotalPrice = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));

                            int newQuantity = currentQuantity + 1;
                            double newTotalPrice = currentTotalPrice + totalPrice;

                            mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice);
                        } else {
                            // Item not selected, insert a new transaction with quantity 1 and total price
                            mDatabaseHelper.insertTransaction(itemId, transactionDate, 1, totalPrice, Double.parseDouble(vat), longDescription);
                        }

                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );



        return view;
    }




    private String getCurrentDateTime() {
        // Get the current date and time in the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logout();
            }
        });
    }

    private void logout() {
        Intent intent = new Intent(requireContext(), login.class);
        startActivity(intent);
        requireActivity().finish();
    }


}
