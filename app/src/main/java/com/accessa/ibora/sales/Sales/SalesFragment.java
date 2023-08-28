package com.accessa.ibora.sales.Sales;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.items.ItemAdapter;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.FragmentResultListener;
public class SalesFragment extends Fragment implements FragmentResultListener {


    private RecyclerView mRecyclerView;
    private ItemGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierId,shopname;
    private ItemAddedListener itemAddedListener;
    private double totalAmount,TaxtotalAmount;
    private DBManager dbManager;
    private  String existingTransactionId;
    private double UnitPrice,priceAfterDiscount,TotalDiscount;
    private int transactionCounter = 1;
    private String VatVall;
    private String Nature;
    private String TaxCode;
    private String ItemCode;
    private String Currency;
    private String price;
    private  String barcode;
    private String VatType;
    public TextView nameTextView;
    public TextView descriptionTextView;
    public TextView idTextView;
    public TextView priceTextView;
    public ImageView productImage;
    private String actualdate;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private static final String POSNumber="posNumber";
    private String transactionIdInProgress,PosNum; // Transaction ID for "InProgress" status
    private BarcodeListener barcodeListener;


    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

    }

    public void searchItemByBarcode(String searchText) {

        showItemDialog(searchText);
    }

    public interface BarcodeListener {
        void onBarcodeReceived(String barcode);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_fragment, container, false);


        int numberOfColumns = 6;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);




        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);

        // Set the fragment result listener
        getParentFragmentManager().setFragmentResultListener("barcodesearched", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("barcodesearched")) {
                    // Retrieve the barcode value from the result bundle
                    barcode = result.getString("barcode");

                    // Handle the barcode value as needed
                    if (barcode != null) {
                        // search and display in pop up
                        searchItemByBarcode(barcode);

                    }
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener("barcodeKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("barcodeKey")) {
                    // Retrieve the barcode value from the result bundle
                    barcode = result.getString("barcode");
                    // Handle the barcode value as needed
                    if (barcode != null) {
                        //add to transaction
                        insertItemIntoTransaction( barcode);
                    }
                }
            }
        });

        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        shopname=sharedPreference.getString("ShopName",null);
        dbManager = new DBManager(getContext());
        dbManager.open();


        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mDatabaseHelper = new DatabaseHelper(getContext());
        actualdate = mDatabaseHelper.getCurrentDate();
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
                TextView priceTextView = view.findViewById(R.id.priceNoRs_text_view);

                String id = idTextView.getText().toString();
                String title = subjectEditText.getText().toString();
                String longDescription = longDescriptionEditText.getText().toString();
                price = priceTextView.getText().toString();
                String transactionId;

                Item item = dbManager.getItemById(id);
                if (item != null) {
                    VatVall = item.getVAT();
                    VatType=item.getVAT();
                    priceAfterDiscount=item.getPriceAfterDiscount();
                    TotalDiscount=item.getTotalDiscount();
                    Nature=item.getNature();
                    TaxCode=item.getTaxCode();
                    Currency=item.getCurrency();
                    ItemCode=item.getItemCode();

                }


                String transactionStatus = "InProgress";
                String transactionSaved = "PFT";
                String transactionCDN = "CRN";
                String transactionDBN = "DRN";

                if (transactionStatus.equals("InProgress") || transactionSaved.equals("PFT") || transactionCDN.equals("CRN") || transactionDBN.equals("DRN")  ) {
                    if (transactionIdInProgress == null) {
                        transactionIdInProgress = generateNewTransactionId(); // Generate a new transaction ID for "InProgress" status
                        // Store the transaction ID in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TRANSACTION_ID_KEY, transactionIdInProgress);
                        editor.apply();
                        refreshTicketFragment();
                        refreshTotal();

                    }
                    transactionId = transactionIdInProgress;


                } else {
                    transactionId = generateNewTransactionId(); // Generate a new transaction ID
                    refreshTicketFragment();
                    refreshTotal();
                }

                // Get the current date and time for the transaction
                String transactionDate = getCurrentDateTime();

                // Insert/update the transaction into the Transaction table
                int itemId = Integer.parseInt(id);
                double totalPrice = Double.parseDouble(price);
                String vat= String.valueOf(calculateTax());
                String priceWithoutVat= String.valueOf(calculatePricewithoutTax());
                refreshTicketFragment();
                refreshTotal();
// Update the transaction ID for all items in progress
                if (transactionStatus.equals("InProgress")) {
                    mDatabaseHelper.updateTransactionIds(transactionIdInProgress);
                    refreshTicketFragment();
                    refreshTotal();
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
                        refreshTicketFragment();
                        refreshTotal();
                    } else {
                        item = dbManager.getItemById(String.valueOf(itemId));
                        if (item != null) {
                            VatVall = item.getVAT();
                            priceAfterDiscount=item.getPriceAfterDiscount();
                            double unitprice = item.getPrice();
                            TotalDiscount=item.getTotalDiscount();
                            VatType = item.getVAT();
                            Nature = item.getNature();
                            Currency = item.getCurrency();
                            TaxCode=item.getTaxCode();
                            ItemCode = item.getItemCode();
                            double UnitPrice = Double.parseDouble(price);
                            double newTotalPrice = UnitPrice * 1;

                            // Item has a different transaction ID, insert a new transaction
                            mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency,TaxCode,priceAfterDiscount,TotalDiscount);

                            refreshTicketFragment();
                            refreshTotal();
                        }
                    }
                } else {
                    item = dbManager.getItemById(String.valueOf(itemId));
                    if (item != null) {
                        VatVall = item.getVAT();
                        double unitprice = item.getPrice();
                        priceAfterDiscount=item.getPriceAfterDiscount();
                        VatType = item.getVAT();
                        TotalDiscount=item.getTotalDiscount();
                        TaxCode=item.getTaxCode();
                        Nature = item.getNature();
                        Currency = item.getCurrency();
                        ItemCode = item.getItemCode();
                        double UnitPrice = Double.parseDouble(price);
                        double newTotalPrice = UnitPrice * 1;

                        // Item not selected, insert a new transaction with quantity 1 and total price
                        mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount);
                        refreshTicketFragment();
                        refreshTotal();
                    }
                }

                if (cursor != null) {
                    cursor.close();
                }
                item = dbManager.getItemById(id);
                if (item != null) {
                    VatVall = item.getVAT();
                    priceAfterDiscount=item.getPriceAfterDiscount();
                    double unitprice=item.getPrice();
                    TotalDiscount=item.getTotalDiscount();
                    VatType=item.getVAT();
                    TaxCode=item.getTaxCode();
                    Nature=item.getNature();
                    Currency=item.getCurrency();
                    ItemCode=item.getItemCode();

                    SendToHeader(unitprice, calculateTax());
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
    public void insertItemIntoTransaction(String barcode) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
        if (barcode != null) {


            // Retrieve the item details based on the barcode from your database or API
            Item item = dbManager.getItemByBarcode(barcode);
            String transactionId;
            if (item != null) {
                // Get the required information from the item object
                int id = item.getId();
                String longDescription = item.getLongDescription();
                price = String.valueOf(item.getPrice());
                VatVall = item.getVAT();
                priceAfterDiscount=item.getPriceAfterDiscount();
                VatType=item.getVAT();
                TotalDiscount=item.getTotalDiscount();
                Nature=item.getNature();
                TaxCode=item.getTaxCode();
                Currency=item.getCurrency();
                ItemCode=item.getItemCode();
                String Available4sale = String.valueOf(item.getAvailableForSale());

                String transactionStatus = "InProgress";
                String transactionSaved = "PFT";
                String transactionCDN = "CRN";
                String transactionDBN = "DRN";



                if (Available4sale != null && Available4sale.equalsIgnoreCase("true")) {
                    if (transactionStatus.equals("InProgress") || transactionSaved.equals("PFT")|| transactionCDN.equals("CRN")|| transactionDBN.equals("DRN")) {
                        if (transactionIdInProgress == null) {
                            transactionIdInProgress = generateNewTransactionId(); // Generate a new transaction ID for "InProgress" status
                            // Store the transaction ID in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TRANSACTION_ID_KEY, transactionIdInProgress);
                            editor.apply();
                            refreshTicketFragment();
                            refreshTotal();

                        }
                        transactionId = transactionIdInProgress;

                    } else {
                        transactionId = generateNewTransactionId(); // Generate a new transaction ID
                        refreshTicketFragment();
                        refreshTotal();
                    }

                    // Get the current date and time for the transaction
                    String transactionDate = getCurrentDateTime();

                    // Insert/update the transaction into the Transaction table
                    int itemId = Integer.parseInt(String.valueOf(id));
                    double totalPrice = Double.parseDouble(price);
                    String vat = String.valueOf(calculateTax());
                    String priceWithoutVat = String.valueOf(calculatePricewithoutTax());
                    refreshTicketFragment();
                    refreshTotal();
// Update the transaction ID for all items in progress
                    if (transactionStatus.equals("InProgress")) {
                        mDatabaseHelper.updateTransactionIds(transactionIdInProgress);
                        refreshTicketFragment();
                        refreshTotal();
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


                            int newQuantity = currentQuantity + 1;
                            double newTotalPrice = UnitPrice * newQuantity;
                            double newVat = newQuantity * calculateTax();
                            mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice, newVat, VatType);
                            refreshTicketFragment();
                            refreshTotal();
                        } else {
                            item = dbManager.getItemById(String.valueOf(itemId));
                            if (item != null) {
                                VatVall = item.getVAT();
                                double unitprice = item.getPrice();
                                priceAfterDiscount=item.getPriceAfterDiscount();
                                TotalDiscount=item.getTotalDiscount();
                                VatType = item.getVAT();
                                TaxCode=item.getTaxCode();
                                Nature = item.getNature();
                                Currency = item.getCurrency();
                                ItemCode = item.getItemCode();

                                double UnitPrice = Double.parseDouble(price);
                                double newTotalPrice = UnitPrice * 1;

                                // Item has a different transaction ID, insert a new transaction
                                mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount);

                                refreshTicketFragment();
                                refreshTotal();
                            }
                        }
                    } else {
                        item = dbManager.getItemById(String.valueOf(itemId));
                        if (item != null) {
                            VatVall = item.getVAT();
                            double unitprice = item.getPrice();
                            priceAfterDiscount=item.getPriceAfterDiscount();
                            VatType = item.getVAT();
                            TotalDiscount=item.getTotalDiscount();
                            TaxCode=item.getTaxCode();
                            Nature = item.getNature();
                            Currency = item.getCurrency();
                            ItemCode = item.getItemCode();
                            double UnitPrice = Double.parseDouble(price);
                            double newTotalPrice = UnitPrice * 1;

                            // Item not selected, insert a new transaction with quantity 1 and total price
                            mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount);
                            refreshTicketFragment();
                            refreshTotal();
                        }
                    }

                    if (cursor != null) {
                        cursor.close();
                    }
                    item = dbManager.getItemById(String.valueOf(id));
                    if (item != null) {
                        VatVall = item.getVAT();
                        priceAfterDiscount=item.getPriceAfterDiscount();
                        double unitprice=item.getPrice();
                        VatType=item.getVAT();
                        TotalDiscount=item.getTotalDiscount();
                        TaxCode=item.getTaxCode();
                        Nature=item.getNature();
                        Currency=item.getCurrency();
                        ItemCode=item.getItemCode();
                        SendToHeader(unitprice, calculateTax());
                    }
                    // Notify the listener that an item is added
                    if (itemAddedListener != null) {
                        itemAddedListener.onItemAdded();
                    }


                } else {
                    Toast.makeText(getContext(), barcode + getText(R.string.itemnotavailables), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Barcode not found in current system, please contact admin to add this barcode", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void refreshTicketFragment() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);

        if (ticketFragment != null) {
            ticketFragment.refreshData(calculateTotalAmount(),calculateTotalTax());
        }
    }
    private void showItemDialog(String searchText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the custom dialog layout
        View dialogView = inflater.inflate(R.layout.search_by_barcode, null);
        builder.setView(dialogView);

        // Find the RecyclerView in the dialog layout
        RecyclerView dialogRecyclerView = dialogView.findViewById(R.id.dialog_recycler_view);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get the item data for the RecyclerView
        Cursor itemCursor = mDatabaseHelper.getAllItemsByBarcode(searchText);
        ItemAdapter dialogAdapter = new ItemAdapter(getActivity(), itemCursor);
        dialogRecyclerView.setAdapter(dialogAdapter);

        // Set the OnItemTouchListener on the RecyclerView{
        dialogRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), dialogRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);


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
                            priceAfterDiscount=item.getPriceAfterDiscount();
                            VatType=item.getVAT();
                            TotalDiscount=item.getTotalDiscount();
                            TaxCode=item.getTaxCode();
                            VatType=item.getVAT();
                            Nature=item.getNature();
                            Currency=item.getCurrency();
                            ItemCode=item.getItemCode();
                        }


                        String transactionStatus = "InProgress";


                        String status = mDatabaseHelper.getTransactionStatus(transactionIdInProgress);


                        if (status.equals("InProgress") || status.equals("PFT") || status.equals("DRN") || status.equals("CRN")  ) {
                            if (transactionIdInProgress == null) {
                                transactionIdInProgress = generateNewTransactionId(); // Generate a new transaction ID for "InProgress" status
                                // Store the transaction ID in SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(TRANSACTION_ID_KEY, transactionIdInProgress);
                                editor.apply();
                                refreshTicketFragment();
                                refreshTotal();

                            }
                            transactionId = transactionIdInProgress;

                        } else {
                            transactionId = generateNewTransactionId(); // Generate a new transaction ID
                            refreshTicketFragment();
                            refreshTotal();
                        }

                        // Get the current date and time for the transaction
                        String transactionDate = getCurrentDateTime();

                        // Insert/update the transaction into the Transaction table
                        int itemId = Integer.parseInt(id);
                        double totalPrice = Double.parseDouble(price);
                        String vat= String.valueOf(calculateTax());
                        String priceWithoutVat= String.valueOf(calculatePricewithoutTax());
                        refreshTicketFragment();
                        refreshTotal();
// Update the transaction ID for all items in progress
                        if (transactionStatus.equals("InProgress")) {
                            mDatabaseHelper.updateTransactionIds(transactionIdInProgress);
                            refreshTicketFragment();
                            refreshTotal();
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
                                refreshTicketFragment();
                                refreshTotal();
                            } else {
                                item = dbManager.getItemById(String.valueOf(itemId));
                                if (item != null) {
                                    VatVall = item.getVAT();
                                    priceAfterDiscount=item.getPriceAfterDiscount();
                                    double unitprice = item.getPrice();
                                    VatType = item.getVAT();
                                    TotalDiscount=item.getTotalDiscount();
                                    TaxCode=item.getTaxCode();
                                    Nature = item.getNature();
                                    Currency = item.getCurrency();
                                    ItemCode = item.getItemCode();
                                    double UnitPrice = Double.parseDouble(price);
                                    double newTotalPrice = UnitPrice * 1;

                                    // Item has a different transaction ID, insert a new transaction
                                    mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount);

                                    refreshTicketFragment();
                                    refreshTotal();
                                }
                            }
                        } else {
                            item = dbManager.getItemById(String.valueOf(itemId));
                            if (item != null) {
                                VatVall = item.getVAT();
                                priceAfterDiscount=item.getPriceAfterDiscount();
                                double unitprice = item.getPrice();
                                VatType = item.getVAT();
                                TotalDiscount=item.getTotalDiscount();
                                TaxCode=item.getTaxCode();
                                Nature = item.getNature();
                                Currency = item.getCurrency();
                                ItemCode = item.getItemCode();
                                double UnitPrice = Double.parseDouble(price);
                                double newTotalPrice = UnitPrice * 1;

                                // Item not selected, insert a new transaction with quantity 1 and total price
                                mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount);
                                refreshTicketFragment();
                                refreshTotal();
                            }
                        }

                        if (cursor != null) {
                            cursor.close();
                        }
                        item = dbManager.getItemById(id);
                        if (item != null) {
                            VatVall = item.getVAT();
                            priceAfterDiscount=item.getPriceAfterDiscount();
                            double unitprice=item.getPrice();
                            SendToHeader(unitprice, calculateTax());
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

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
            double totalHT_A = totalAmount- taxtotalAmount;
            double totalTTC = totalAmount;
            double TaxAmount=taxtotalAmount;
            String transactionStatus = "InProgress";
            // Get the total quantity of items in the transaction
           int quantityItem = mDatabaseHelper.calculateTotalItemQuantity(transactionIdInProgress);
            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
            cashierId = sharedPreference.getString("cashorId", null);

            String ShopName = sharedPreference.getString("ShopName", null);

            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);


                String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);

                // Save the transaction details in the TRANSACTION_HEADER table
                boolean success = mDatabaseHelper.saveTransactionHeader(
                        CompanyShopNumber,
                        transactionIdInProgress,
                        totalAmount,
                        currentDate,
                        currentTime,
                        totalHT_A,
                        totalTTC,
                        TaxAmount,
                        quantityItem,
                        cashierId,
                        transactionStatus,
                        PosNum
                );

            }
        }
    }



    private String generateNewTransactionId() {
        // Retrieve the last used counter value from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("TransactionCounter", Context.MODE_PRIVATE);
        int lastCounter = sharedPreferences.getInt("counter", 1);

        // Increment the counter for the next transaction
        int currentCounter = lastCounter + 1;

        // Save the updated counter value in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("counter", currentCounter);
        editor.apply();

        // Extract the first three letters from companyName
        String companyLetters = shopname.substring(0, Math.min(shopname.length(), 3)).toUpperCase();
        String posNumberLetters = PosNum.substring(0, Math.min(PosNum.length(), 3)).toUpperCase();

        // Generate the transaction ID by combining the three letters and the counter
        return companyLetters + "-" + posNumberLetters + "-" + currentCounter;
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
        if (context instanceof BarcodeListener) {
            barcodeListener = (BarcodeListener) context;
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