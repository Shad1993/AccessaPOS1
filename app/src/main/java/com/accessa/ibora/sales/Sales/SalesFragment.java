package com.accessa.ibora.sales.Sales;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.accessa.ibora.product.items.Variant;
import com.accessa.ibora.sales.Tables.TablesFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.FragmentResultListener;
public class SalesFragment extends Fragment implements FragmentResultListener {


    private static final String PREF_ROOM_ID = "room_id";
    public static RecyclerView mRecyclerView;
    private ItemGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierId,shopname;
    private ItemAddedListener itemAddedListener;
    private double totalAmount,TaxtotalAmount;
    private DBManager dbManager;
    private static final String PREF_TABLE_ID = "table_id";
    private  String existingTransactionId;
    private double UnitPrice,priceAfterDiscount,TotalDiscount;
    private int transactionCounter = 1;
    private String VatVall;
    private String Nature,Hascomment, RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem;
    private boolean HasOptions;
    private String TaxCode;
    private String tableid;
    private static int roomid;
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
    EditText option1edittext ;
    EditText option2edittext ;
    EditText option3edittext ;
    EditText option4edittext ;
    EditText option5edittext ;
    EditText supplementsedittext ;

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
        // Retrieve data from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            roomid = Integer.parseInt(arguments.getString("room", "Default Value"));
            tableid = String.valueOf(arguments.getString("table", "Default Value"));

            // Now, 'receivedData' contains the data passed from the SendingFragment
            // Use 'receivedData' as needed in your fragment logic
        }



        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = Integer.parseInt(String.valueOf(preferences.getInt("roomnum", 0)));
        tableid = preferences.getString("table_id", "0");
// Get data from SharedPreferences
// Now the table ID in SharedPreferences is updated and can be accessed elsewhere in your app

// Retrieve table_id and roomnum
        tableid = String.valueOf(preferences.getString("table_id", "0")); // "defaultTableId" is a default value in case the key is not found
        roomid = preferences.getInt("roomnum", 0); // -1 is a default value in case the key is not found


        int numberOfColumns = 12;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

        mDatabaseHelper = new DatabaseHelper(getContext());

        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);

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

         sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

        String ShopName = sharedPreference.getString("ShopName", null);



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


                if (tableid == "defaultTableId") {
                    // Table ID is 1, show a pop-up
                    showTableIdOnePopup();
                } else {

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
                    VatType = item.getVAT();
                    priceAfterDiscount = item.getPriceAfterDiscount();
                    TotalDiscount = item.getTotalDiscount();
                    Nature = item.getNature();
                    TaxCode = item.getTaxCode();
                    Currency = item.getCurrency();
                    ItemCode = item.getItemCode();
                    HasOptions=item.gethasoptions();
                    Hascomment=item.gethascomment();
                    RelatedItem=item.getRelateditem();
                    RelatedItem2=item.getRelateditem2();
                    RelatedItem3=item.getRelateditem3();
                    RelatedItem4=item.getRelateditem4();
                    RelatedItem5=item.getRelateditem5();
                    SupplementsItem=item.getRelateditem();


                }


                String transactionStatus = "InProgress";
                String transactionSaved = "PRF";
                String transactionCDN = "CRN";
                String transactionDBN = "DRN";


                if (transactionStatus.equals("InProgress") || transactionStatus.equals("PRF") || transactionSaved.equals("PRF") || transactionCDN.equals("CRN") || transactionDBN.equals("DRN")) {
                    if (!isRoomTableInProgress(String.valueOf(roomid), tableid)) {
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
                String vat = String.valueOf(calculateTax());
                String priceWithoutVat = String.valueOf(calculatePricewithoutTax());
                refreshTicketFragment();
                refreshTotal();
// Update the transaction ID for all items in progress
                if (transactionStatus.equals("InProgress") ||(transactionStatus.equals("PRF"))) {
                    mDatabaseHelper.updateTransactionIds(transactionIdInProgress, String.valueOf(roomid),tableid);
                    refreshTicketFragment();
                    refreshTotal();
                }

// Check if the item with the same ID is already selected
                Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId, String.valueOf(roomid),tableid);
                    HasOptions=item.gethasoptions();
                    Hascomment=item.gethascomment();
                    RelatedItem=item.getRelateditem();
                    RelatedItem2=item.getRelateditem2();
                    RelatedItem3=item.getRelateditem3();
                    RelatedItem4=item.getRelateditem4();
                    RelatedItem5=item.getRelateditem5();
                    SupplementsItem=item.getRelateditem();
                    float quantity= item.getQuantity();

                    if (Boolean.TRUE.equals(HasOptions) || Hascomment.trim().equals("true")) {


                        Log.d("relateditems1", RelatedItem+ " " +RelatedItem2 + " " + RelatedItem3 + " " + RelatedItem4 + " " + RelatedItem5 );

                        showOptionPopup(Hascomment,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                    } else if
                    (cursor != null && cursor.moveToFirst()) {
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
                        mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice, newVat, VatType, String.valueOf(roomid),tableid);
                        refreshTicketFragment();
                        refreshTotal();
                    } else {
                        if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_PAID)) == 1) {
                            // Item is paid, insert a new transaction with IS_PAID as 0
                            item = dbManager.getItemById(String.valueOf(itemId));
                            if (item != null) {
                                // Other item details extraction code...

                                double UnitPrice = Double.parseDouble(price);
                                double newTotalPrice = UnitPrice * 1;

                                // Insert a new transaction with IS_PAID as 0
                                mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

                                refreshTicketFragment();
                                refreshTotal();
                            }
                        }
                    }
                } else {
                    item = dbManager.getItemById(String.valueOf(itemId));
                    if (item != null) {
                        VatVall = item.getVAT();
                        double unitprice = item.getPrice();
                        priceAfterDiscount = item.getPriceAfterDiscount();
                        VatType = item.getVAT();
                        TotalDiscount = item.getTotalDiscount();
                        TaxCode = item.getTaxCode();
                        Nature = item.getNature();
                        Currency = item.getCurrency();
                        ItemCode = item.getItemCode();
                        double UnitPrice = Double.parseDouble(price);
                        double newTotalPrice = UnitPrice * 1;

                        // Item not selected, insert a new transaction with quantity 1 and total price
                        mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid),tableid,0);
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
                    priceAfterDiscount = item.getPriceAfterDiscount();
                    double unitprice = item.getPrice();
                    TotalDiscount = item.getTotalDiscount();
                    VatType = item.getVAT();
                    TaxCode = item.getTaxCode();
                    Nature = item.getNature();
                    Currency = item.getCurrency();
                    ItemCode = item.getItemCode();

                    SendToHeader(unitprice, calculateTax());
                }
                // Notify the listener that an item is added
                if (itemAddedListener != null) {
                    itemAddedListener.onItemAdded(String.valueOf(roomid),tableid);
                }


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

    private boolean isRoomTableInProgress(String roomid, String tableid) {
        Cursor cursor = mDatabaseHelper.getInProgressRecord(roomid, tableid);
        return cursor != null && cursor.moveToFirst();
    }


    private void showOptionPopup(String Hascomment,String id,String id2,String id3,String id4,String id5,String relatensupplement,int itemId,String transactionId,String transactionDate,int quantity, double newTotalPrice, double vat, String longDescription,Double unitPrice, Double priceWithoutVat,String VatType,String PosNum,String Nature,String ItemCode, String currency,String TaxCode,double priceAfterDiscount,Double TotalDiscount,String roomid,String tableid,int ispaid ) {


        // Create a custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.custom_comment_dialog, null);
        List<Variant> variantList = dbManager.getVariantsById(id);
        List<Variant> variantList2 = dbManager.getVariantsById(id2);
        List<Variant> variantList3 = dbManager.getVariantsById(id3);
        List<Variant> variantList4 = dbManager.getVariantsById(id4);
        List<Variant> variantList5 = dbManager.getVariantsById(id5);
        List<Variant> supplementList = dbManager.getSupplementListById(relatensupplement);



        if (variantList != null && !variantList.isEmpty()) {
            for (Variant variant : variantList) {
                // Log each VARIANT_DESC



                LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.variantButtonsLayout);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String newbarcode=variant.getBarcode();
                String newDesc=variant.getDescription();
                String newitemid=variant.getVariantitemid();
                double newprice=variant.getPrice();
                String optionName= mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2= mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3= mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4= mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5= mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

                TextView textView = dialogView.findViewById(R.id.OptionNametextView); // Replace R.id.textView with the ID of your TextView
                TextView textView2 = dialogView.findViewById(R.id.OptionNametextView2);
                TextView textView3 = dialogView.findViewById(R.id.OptionNametextView3);
                TextView textView4 = dialogView.findViewById(R.id.OptionNametextView4);
                TextView textView5 = dialogView.findViewById(R.id.OptionNametextView5);
                textView.setText(optionName);
                textView2.setText(optionName2);
                textView3.setText(optionName3);
                textView4.setText(optionName4);
                textView5.setText(optionName5);// Set the text for the option
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertdata(id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,newbarcode,newDesc,newprice,newitemid);

                        if (getContext() != null && variantButtonsLayout != null) { // Create and add EditText dynamically
                        option1edittext = new EditText(getContext());
                        option1edittext.setHint(variant.getDescription() +" - Enter your comment");

                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 20, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option1edittext, params);

                        } else {
                            Log.e("InsertData", "Context or variantButtonsLayout is null");
                        }
                    };

                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant

                        return true; // Consume the long click event
                    }
                });

                // Add the button to the layout
                variantButtonsLayout.addView(variantButton);
            }
        } else {
            Log.d("relateditems", "No variants found for ID " + id);
            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.variantButtonsLayout).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView5).setVisibility(View.GONE);
        }


        if (variantList2 != null && !variantList2.isEmpty()) {
            for (Variant variant : variantList2) {
               LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.variantButtonsLayout2);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String newbarcode=variant.getBarcode();
                String newDesc=variant.getDescription();
                String newitemid=variant.getVariantitemid();
                double newprice=variant.getPrice();
                String optionName= mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2= mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3= mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4= mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5= mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

                TextView textView = dialogView.findViewById(R.id.OptionNametextView); // Replace R.id.textView with the ID of your TextView
                TextView textView2 = dialogView.findViewById(R.id.OptionNametextView2);
                TextView textView3 = dialogView.findViewById(R.id.OptionNametextView3);
                TextView textView4 = dialogView.findViewById(R.id.OptionNametextView4);
                TextView textView5 = dialogView.findViewById(R.id.OptionNametextView5);
                textView.setText(optionName);
                textView2.setText(optionName2);
                textView3.setText(optionName3);
                textView4.setText(optionName4);
                textView5.setText(optionName5);// Set the text for the option
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertdata(id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,newbarcode,newDesc,newprice,newitemid);
                        // Create and add EditText dynamically
                        if (getContext() != null && variantButtonsLayout != null) {
                            option2edittext = new EditText(getContext());
                            option2edittext.setHint(variant.getDescription() + " - Enter your comment");
                            // Create layout parameters to define how the EditText should be positioned
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 20, 0, 0); // Adjust margins as needed

                            // Add the EditText below the button
                            variantButtonsLayout.addView(option2edittext, params);
                        }else {
                                Log.e("InsertData", "Context or variantButtonsLayout is null");
                            }
                    };

                });



                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant

                        return true; // Consume the long click event
                    }
                });

                // Add the button to the layout
                variantButtonsLayout.addView(variantButton);
            }
        } else {
            Log.d("relateditems", "No variants found for ID " + id);
            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.variantButtonsLayout2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView5).setVisibility(View.GONE);
        }

        if (variantList3 != null && !variantList3.isEmpty()) {
            for (Variant variant : variantList3) {
                // Log each VARIANT_DESC



                LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.variantButtonsLayout3);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String newbarcode=variant.getBarcode();
                String newDesc=variant.getDescription();
                String newitemid=variant.getVariantitemid();
                double newprice=variant.getPrice();
                String optionName= mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2= mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3= mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4= mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5= mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

                TextView textView = dialogView.findViewById(R.id.OptionNametextView); // Replace R.id.textView with the ID of your TextView
                TextView textView2 = dialogView.findViewById(R.id.OptionNametextView2);
                TextView textView3 = dialogView.findViewById(R.id.OptionNametextView3);
                TextView textView4 = dialogView.findViewById(R.id.OptionNametextView4);
                TextView textView5 = dialogView.findViewById(R.id.OptionNametextView5);
                textView.setText(optionName);
                textView2.setText(optionName2);
                textView3.setText(optionName3);
                textView4.setText(optionName4);
                textView5.setText(optionName5);// Set the text for the option
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertdata(id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,newbarcode,newDesc,newprice,newitemid);

                        option3edittext = new EditText(getContext());
                        option3edittext.setHint(variant.getDescription() +" - Enter your comment");

                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 16, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option3edittext, params);
                    };

                });



                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant

                        return true; // Consume the long click event
                    }
                });

                // Add the button to the layout
                variantButtonsLayout.addView(variantButton);
            }
        } else {
            Log.d("relateditems", "No variants found for ID " + id);
            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.variantButtonsLayout3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView5).setVisibility(View.GONE);
        }

        if (variantList4 != null && !variantList4.isEmpty()) {
            for (Variant variant : variantList4) {
                // Log each VARIANT_DESC



                LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.variantButtonsLayout4);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String newbarcode=variant.getBarcode();
                String newDesc=variant.getDescription();
                String newitemid=variant.getVariantitemid();
                double newprice=variant.getPrice();
                String optionName= mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2= mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3= mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4= mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5= mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

                TextView textView = dialogView.findViewById(R.id.OptionNametextView); // Replace R.id.textView with the ID of your TextView
                TextView textView2 = dialogView.findViewById(R.id.OptionNametextView2);
                TextView textView3 = dialogView.findViewById(R.id.OptionNametextView3);
                TextView textView4 = dialogView.findViewById(R.id.OptionNametextView4);
                TextView textView5 = dialogView.findViewById(R.id.OptionNametextView5);
                textView.setText(optionName);
                textView2.setText(optionName2);
                textView3.setText(optionName3);
                textView4.setText(optionName4);
                textView5.setText(optionName5);// Set the text for the option
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertdata(id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,newbarcode,newDesc,newprice,newitemid);

                        option4edittext = new EditText(getContext());
                        option4edittext.setHint(variant.getDescription() +" - Enter your comment");
                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 16, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option4edittext, params);
                    };

                });



                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant

                        return true; // Consume the long click event
                    }
                });

                // Add the button to the layout
                variantButtonsLayout.addView(variantButton);
            }
        } else {
            Log.d("relateditems", "No variants found for ID " + id);
            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.variantButtonsLayout4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView5).setVisibility(View.GONE);
        }

        if (variantList5 != null && !variantList5.isEmpty()) {
            for (Variant variant : variantList5) {
                // Log each VARIANT_DESC



                LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.variantButtonsLayout5);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String newbarcode=variant.getBarcode();
                String newDesc=variant.getDescription();
                String newitemid=variant.getVariantitemid();
                double newprice=variant.getPrice();
                String optionName= mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2= mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3= mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4= mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5= mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

                TextView textView = dialogView.findViewById(R.id.OptionNametextView); // Replace R.id.textView with the ID of your TextView
                TextView textView2 = dialogView.findViewById(R.id.OptionNametextView2);
                TextView textView3 = dialogView.findViewById(R.id.OptionNametextView3);
                TextView textView4 = dialogView.findViewById(R.id.OptionNametextView4);
                TextView textView5 = dialogView.findViewById(R.id.OptionNametextView5);
                textView.setText(optionName);
                textView2.setText(optionName2);
                textView3.setText(optionName3);
                textView4.setText(optionName4);
                textView5.setText(optionName5);// Set the text for the option
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertdata(id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,newbarcode,newDesc,newprice,newitemid);

                        // Create and add EditText dynamically
                        option5edittext = new EditText(getContext());
                        option5edittext.setHint(variant.getDescription() +" - Enter your comment");
                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 16, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option5edittext, params);
                    };

                });



                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant

                        return true; // Consume the long click event
                    }
                });

                // Add the button to the layout
                variantButtonsLayout.addView(variantButton);
            }
        } else {
            Log.d("relateditems", "No variants found for ID " + id);
            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.variantButtonsLayout5).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView5).setVisibility(View.GONE);
        }
        if (supplementList != null && !supplementList.isEmpty()) {
            for (Variant variant : supplementList) {
                // Log each VARIANT_DESC



                LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.SupplementsButtonsLayout);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String newbarcode=variant.getBarcode();
                String newDesc=variant.getDescription();
                String newitemid=variant.getVariantitemid();
                double newprice=variant.getPrice();
                String optionName= mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2= mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3= mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4= mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5= mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

                TextView textView = dialogView.findViewById(R.id.OptionNametextView); // Replace R.id.textView with the ID of your TextView
                TextView textView2 = dialogView.findViewById(R.id.OptionNametextView2);
                TextView textView3 = dialogView.findViewById(R.id.OptionNametextView3);
                TextView textView4 = dialogView.findViewById(R.id.OptionNametextView4);
                TextView textView5 = dialogView.findViewById(R.id.OptionNametextView5);
                textView.setText(optionName);
                textView2.setText(optionName2);
                textView3.setText(optionName3);
                textView4.setText(optionName4);
                textView5.setText(optionName5);// Set the text for the option
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertdata(id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,newbarcode,newDesc,newprice,newitemid);

                        // Create and add EditText dynamically
                       supplementsedittext= new EditText(getContext());
                        supplementsedittext.setHint(variant.getDescription() +" - Enter your comment");
                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 20, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(supplementsedittext, params);
                    };

                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant

                        return true; // Consume the long click event
                    }
                });

                // Add the button to the layout
                variantButtonsLayout.addView(variantButton);
            }
        } else {
            Log.d("supplements", "No supplements found for ID " + id);
            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.SupplementsButtonsLayout).setVisibility(View.GONE);

        }


        // Build the dialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Options/Comments");

        // Add positive button (OK button)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the comment from the EditText fields

                String commentoption3 = null;
                String commentoption4 = null;
                String commentoption5 = null;
                String commentSupplements1 = null;
                String commentoption1 = null;
                if (option1edittext != null) {
                    commentoption1 = option1edittext.getText().toString();
                }
                else {
                    commentoption1 = "test";
                }
                String commentoption2 = null;
                if (option2edittext != null) {
                    commentoption2 = option2edittext.getText().toString();
                }
                else {
                    commentoption2 = "test";
                }
                if (option3edittext != null) {
                    // Access option2edittext here
                    commentoption3 = option3edittext.getText().toString();
                    // Other operations...
                } else {
                    // EditText has not been created yet
                    commentoption3 = "test";
                }
                if (option4edittext != null) {
                    // Access option2edittext here
                    commentoption4 = option4edittext.getText().toString();
                    // Other operations...
                } else {
                    // EditText has not been created yet
                    commentoption4 = "test";
                }

                if (option5edittext != null) {
                    // Access option2edittext here
                    commentoption5 = option5edittext.getText().toString();
                    // Other operations...
                } else {
                    // EditText has not been created yet
                    commentoption5 = "test";
                }

                if (supplementsedittext != null) {
                    // Access option2edittext here
                    commentSupplements1 = supplementsedittext.getText().toString();
                    // Other operations...
                } else {
                    // EditText has not been created yet
                    commentSupplements1 = "test";
                }
                // Update the comments in the database
                updateCommentForTransaction(transactionIdInProgress,  commentoption1,id);
                updateCommentForTransaction(transactionIdInProgress, commentoption2,id2);
                updateCommentForTransaction(transactionIdInProgress, commentoption3,id3);
                updateCommentForTransaction(transactionIdInProgress, commentoption4,id4);
                updateCommentForTransaction(transactionIdInProgress,  commentoption5,id5);
                updateCommentForTransaction(transactionIdInProgress,  commentSupplements1,relatensupplement);

                // Reset the EditText fields for the next use
                if (option1edittext != null) {
                    option1edittext.setText("");
                }
                if (option2edittext != null) {
                    option2edittext.setText("");
                }

                if (option3edittext != null) {
                    option3edittext.setText("");
                }

                if (option4edittext != null) {
                    option4edittext.setText("");
                }
                if (option5edittext != null) {
                    option5edittext.setText("");
                }
                if (supplementsedittext != null) {
                    supplementsedittext.setText("");
                }


            }
        });


        // Add negative button (Cancel button)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the Cancel button click or leave it empty
            }
        });

        // Show the dialog
        builder.show();
    }

    public void insertdata(String id,int itemId,String transactionId, String transactionDate,double vat, String longDescription,double priceWithoutVat,String newbarcode, String newdesc,double newprice,String newitemid) {
        Item item = dbManager.getItemById(id);
        Log.d("newbarcode", newbarcode);
        Cursor cursor = mDatabaseHelper.getTransactionByItemId(Integer.parseInt(newitemid), String.valueOf(roomid), tableid);
            longDescription= longDescription+"-"+newdesc;

        if
        (cursor != null && cursor.moveToFirst()) {
            // Retrieve the existing transaction ID for the item
            existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));

            // Check if the existing transaction ID matches the current transaction ID
            if (existingTransactionId != null && existingTransactionId.equals(transactionIdInProgress)) {
                // Item already selected, update the quantity and total price
                int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));

                UnitPrice = newprice;


                int newQuantity = currentQuantity + 1;
                double newTotalPrice = UnitPrice * newQuantity;
                double newVat = newQuantity * calculateTax();
                mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid), newQuantity, newTotalPrice, newVat, VatType, String.valueOf(roomid), tableid);
                refreshTicketFragment();
                refreshTotal();
            } else {
                if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_PAID)) == 1) {
                    // Item is paid, insert a new transaction with IS_PAID as 0
                    item = dbManager.getItemById(String.valueOf(itemId));
                    if (item != null) {
                        // Other item details extraction code...

                        double UnitPrice = newprice;
                        double newTotalPrice = UnitPrice * 1;

                        // Insert a new transaction with IS_PAID as 0
                        mDatabaseHelper.insertTransaction(Integer.parseInt(newitemid), transactionId, transactionDate, 1, newTotalPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

                        refreshTicketFragment();
                        refreshTotal();
                    }
                }
            }
        } else {
            item = dbManager.getItemById(String.valueOf(itemId));

            double UnitPrice = newprice;
            double newTotalPrice = UnitPrice * 1;

            // Item not selected, insert a new transaction with quantity 1 and total price
            mDatabaseHelper.insertTransaction(Integer.parseInt(newitemid), transactionId, transactionDate, 1, newTotalPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
            refreshTicketFragment();
            refreshTotal();

        }

        if (cursor != null) {
            cursor.close();
        }
        item = dbManager.getItemById(id);
        if (item != null) {
            VatVall = item.getVAT();

            double unitprice = item.getPrice();

            SendToHeader(unitprice, calculateTax());
        }
        // Notify the listener that an item is added
        if (itemAddedListener != null) {
            itemAddedListener.onItemAdded(String.valueOf(roomid), tableid);
        }

    }
    public void updateCommentForTransaction(String transactionId, String comment,String id) {
        // Call your database helper method to update the comment for the given transaction ID
        mDatabaseHelper.updateTransactionComment(transactionId, comment,id);
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
                    if (transactionStatus.equals("InProgress") || transactionSaved.equals("PRF")|| transactionCDN.equals("CRN")|| transactionDBN.equals("DRN")) {
                        if (!isRoomTableInProgress(String.valueOf(roomid), tableid)) {
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
                    if (transactionStatus.equals("InProgress") || transactionStatus.equals("PRF")) {
                        mDatabaseHelper.updateTransactionIds(transactionIdInProgress, String.valueOf(roomid),tableid);
                        refreshTicketFragment();
                        refreshTotal();
                    }

// Check if the item with the same ID is already selected
                    Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId, String.valueOf(roomid),tableid);
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
                            mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice, newVat, VatType, String.valueOf(roomid),tableid);
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
                                    mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid),tableid,0);

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
                            mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid),tableid,0);
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
                        itemAddedListener.onItemAdded(String.valueOf(roomid),tableid);
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


                        if (status.equals("InProgress") || status.equals("PRF") || status.equals("DRN") || status.equals("CRN")  ) {
                            if (!isRoomTableInProgress(String.valueOf(roomid), tableid)) {
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
                        if (transactionStatus.equals("InProgress") || transactionStatus.equals("PRF")) {
                            mDatabaseHelper.updateTransactionIds(transactionIdInProgress, String.valueOf(roomid),tableid);

                            refreshTicketFragment();
                            refreshTotal();
                        }

                        // Check if the item with the same ID is already selected
                        Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId, String.valueOf(roomid),tableid);
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
                                mDatabaseHelper.updateTransaction(itemId, newQuantity, newTotalPrice,newVat,VatType, String.valueOf(roomid),tableid);
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
                                    mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid),tableid,0);

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
                                mDatabaseHelper.insertTransaction(itemId, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid),tableid,0);
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
                            itemAddedListener.onItemAdded(String.valueOf(roomid),tableid);
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
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
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
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
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
    private void showTableIdOnePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Table Not Selected");
        builder.setMessage("You cannot perform further actions without selecting a table.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Close the dialog if needed
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

                String MRAMETHOD="Single";
                String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);

                // Save the transaction details in the TRANSACTION_HEADER table
                 mDatabaseHelper.saveTransactionHeader(
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
                        PosNum,
                         MRAMETHOD,
                         String.valueOf(roomid),
                         tableid

                );

            }
        }
    }



    public String generateNewTransactionId() {
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

        String posNumberLetters = null;
        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(shopname);
        if (cursorCompany != null && cursorCompany.moveToFirst()) {
            int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_POS_Num);
            PosNum= cursorCompany.getString(columnCompanyNameIndex);
             posNumberLetters = PosNum.substring(0, Math.min(PosNum.length(), 3)).toUpperCase();

        }
        // Generate the transaction ID by combining the three letters and the counter
        return companyLetters + "-" + posNumberLetters + "-" + currentCounter;
    }

    public void reload(String roomid, String tableid) {
        // Implement the logic to reload/refresh your fragment here
        // For example, if you need to refresh the UI or update data

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
        void onItemAdded(String roomid,String tableid);


    }
    @Override
    public void onDetach() {
        super.onDetach();
        itemAddedListener = null;
    }

}