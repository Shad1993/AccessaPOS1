package com.accessa.ibora.sales.Sales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.product.menu.Product;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SalesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ItemGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierId;
    private ItemAddedListener itemAddedListener;
    private double totalAmount,TaxtotalAmount;
    private DBManager dbManager;
    private  String existingTransactionId;
    private double UnitPrice;
    private String VatVall;
    private String price;
    private String VatType;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private String transactionIdInProgress; // Transaction ID for "InProgress" status
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_fragment, container, false);

        int numberOfColumns = 6;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);



        SharedPreferences sharedPreference = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        dbManager = new DBManager(getContext());
        dbManager.open();


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
                 price = priceTextView.getText().toString();
                String transactionId;

                Item item = dbManager.getItemById(id);
                if (item != null) {
                     VatVall = item.getVAT();
                    VatType=item.getVAT();

                }


                String transactionStatus = "InProgress";
                String transactionSaved = "Saved";

                if (transactionStatus.equals("InProgress") || transactionSaved.equals("Saved")  ) {
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
                String vat= String.valueOf(calculateTax());
                String priceWithoutVat= String.valueOf(calculatePricewithoutTax());

// Update the transaction ID for all items in progress
                if (transactionStatus.equals("InProgress")) {
                    mDatabaseHelper.updateTransactionIds(transactionIdInProgress);

                }

// Check if the item with the same ID is already selected
                Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId);
                if (cursor != null && cursor.moveToFirst()) {
                    // Retrieve the existing transaction ID for the item
                    existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));

                    // Check if the existing transaction ID matches the current transaction ID
                    if (existingTransactionId != null && existingTransactionId.equals(transactionIdInProgress)) {
                        // Item already selected, update the quantity and total price
                        int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));

                         UnitPrice = Double.parseDouble(price);


                          int  newQuantity = currentQuantity + 1;
                          double  newTotalPrice = UnitPrice * newQuantity;
                        double newVat= newQuantity * calculateTax();
                            mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice,newVat,VatType);

                    } else {

                      double  UnitPrice = Double.parseDouble(price);
                        double  newTotalPrice = UnitPrice * 1;

                        // Item has a different transaction ID, insert a new transaction
                        mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription,UnitPrice, Double.parseDouble(priceWithoutVat), VatType);

                        refreshTicketFragment();
                        refreshTotal();
                    }
                } else {
                    double  UnitPrice = Double.parseDouble(price);
                    double  newTotalPrice = UnitPrice * 1;

                    // Item not selected, insert a new transaction with quantity 1 and total price
                    mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription,UnitPrice, Double.parseDouble(priceWithoutVat),VatType);
                    refreshTicketFragment();
                    refreshTotal();
                }

                if (cursor != null) {
                    cursor.close();
                }
                // Notify the listener that an item is added
                if (itemAddedListener != null) {
                    itemAddedListener.onItemAdded();
                }
                SendToHeader(totalAmount,TaxtotalAmount);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click
                // ...
            }
        }));

        return view;

    }

    private void refreshTicketFragment() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            ticketFragment.refreshData(calculateTotalAmount(),calculateTotalTax());
        }
    }
    private void refreshTotal() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            ticketFragment.updateheader(calculateTotalAmount(),calculateTotalTax());
        }
    }
    public double calculateTax() {
        double TaxAmount = 0.0;

        double  UnitPrice = Double.parseDouble(price);
        if(VatVall.equals("VAT 15%"))
        {
            TaxAmount= UnitPrice * 0.15;


        }else{
            TaxAmount= 0.0;

        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedTaxAmount = decimalFormat.format(TaxAmount);
        TaxAmount = Double.parseDouble(formattedTaxAmount);

        return TaxAmount;
    }
    public double calculatePricewithoutTax() {

        double PriceWithoutVat=0.0;
        double  UnitPrice = Double.parseDouble(price);
        if(VatVall.equals("VAT 15%"))
        {

            PriceWithoutVat= UnitPrice * 0.85;

        } else{
            PriceWithoutVat= UnitPrice ;

        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedTaxAmount = decimalFormat.format(PriceWithoutVat);
        PriceWithoutVat = Double.parseDouble(formattedTaxAmount);

        return PriceWithoutVat;
    }

    public double calculateTotalAmount() {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
        double totalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
            do {
                double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                totalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return totalAmount;
    }
    public double calculateTotalTax() {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
        double TaxtotalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalTaxColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalTaxColumnIndex);
                TaxtotalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return TaxtotalAmount;
    }

    private void SendToHeader(double totalAmount, double taxtotalAmount) {


        // Save the transaction details in the TRANSACTION_HEADER table
        if (transactionIdInProgress != null) {


            // Get the current date and time
            String currentDate = mDatabaseHelper.getCurrentDate();
            String currentTime = mDatabaseHelper.getCurrentTime();

            // Calculate the total HT_A (priceWithoutVat) and total TTC (totalAmount)
            double totalHT_A = calculateTotalAmount();
            double totalTTC = totalAmount;
            String transactionStatus = "InProgress";
            // Get the total quantity of items in the transaction
            int quantityItem = mDatabaseHelper.calculateTotalItemQuantity();

            // Retrieve the cashier ID from SharedPreferences

            // Save the transaction details in the TRANSACTION_HEADER table
            boolean success = mDatabaseHelper.saveTransactionHeader(
                    transactionIdInProgress,
                    totalAmount,
                    currentDate,
                    currentTime,
                    totalHT_A,
                    totalTTC,
                    quantityItem,
                    cashierId,
                    transactionStatus
            );


        }
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