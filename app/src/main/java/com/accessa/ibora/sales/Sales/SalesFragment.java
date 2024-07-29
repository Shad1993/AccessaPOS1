package com.accessa.ibora.sales.Sales;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;

import com.accessa.ibora.R;
import com.accessa.ibora.printer.externalprinterlibrary2.Kitchen.SendNoteToKitchenActivity;
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
    private EditText editTextOption1;
    private StringBuilder enteredcomment;

    private  String  latesttransId;
    private  String NewBarcode;
    private static final String PREF_ROOM_ID = "room_id";
    public static RecyclerView mRecyclerView;
    private ItemGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierId,shopname,catname;
    private ItemAddedListener itemAddedListener;
    private double totalAmount,TaxtotalAmount;
    private DBManager dbManager;
    private static final String PREF_TABLE_ID = "table_id";
    private  String existingTransactionId,sentToKitchen;
    private double UnitPrice,priceAfterDiscount,TotalDiscount;
    private int transactionCounter = 1;
    private String VatVall,Barcode;
    private String Nature,Hascomment, RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem;
    private boolean HasOptions;
    private String TaxCode;
    private String tableid;
    private static int roomid;
    private String ItemCode;
    private String Currency;
    private String price,initialprice;
    private  String barcode;
    private float Weight;
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


                if (tableid.equals("defaultTableId")  ||  tableid .equals("0")    ){
                    // Table ID is 1, show a pop-up
                    showTableIdOnePopup();
                } else {

                    TextView idTextView = view.findViewById(R.id.id_text_view);
                    TextView subjectEditText = view.findViewById(R.id.name_text_view);
                    TextView longDescriptionEditText = view.findViewById(R.id.Longdescription_text_view);
                    TextView priceTextView = view.findViewById(R.id.priceNoRs_text_view);
                    TextView initialpriceTextView = view.findViewById(R.id.initialprice_text_view);
                    String id = idTextView.getText().toString();
                    String title = subjectEditText.getText().toString();
                    String longDescription = longDescriptionEditText.getText().toString();
                    price = priceTextView.getText().toString();
                    initialprice=initialpriceTextView.getText().toString();
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
                        Barcode= item.getBarcode();
                        catname=item.getCategory();


                    }


                    String transactionStatus = "InProgress";
                    String transactionSaved = "PRF";
                    String transactionCDN = "CRN";
                    String transactionDBN = "DRN";

                    String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                     latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                    if (transactionStatus.equals("InProgress") || transactionStatus.equals("PRF") || transactionSaved.equals("PRF") || transactionCDN.equals("CRN") || transactionDBN.equals("DRN")) {
                        if (!isRoomTableInProgress(String.valueOf(roomid), tableid)) {
                            Log.d("INSERT_Transactions1", "test1" );
                            transactionIdInProgress = generateNewTransactionId(); // Generate a new transaction ID for "InProgress" status
                            // Store the transaction ID in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TRANSACTION_ID_KEY, transactionIdInProgress);
                            editor.apply();
                            refreshTicketFragment();
                            refreshTotal();
                            transactionId = transactionIdInProgress;
                        }
                        transactionId = transactionIdInProgress;

                        Log.d("transactionId", transactionId );
                    } else {
                        Log.d("INSERT_Transactions2", "test1" );
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
                    double unitprice = item.getPrice();
                    double currentpriceWithoutVat  = calculatecurrentPricewithoutTax(unitprice);
                    refreshTicketFragment();
                    refreshTotal();
// Update the transaction ID for all items in progress
                    if (transactionStatus.equals("InProgress") ||(transactionStatus.equals("PRF"))) {

                         statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                        mDatabaseHelper.updateTransactionIds(latesttransId, String.valueOf(roomid),tableid);
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
                    Barcode=item.getBarcode();
                    Weight=item.getWeight();
                    catname=item.getCategory();

                    if (Boolean.TRUE.equals(HasOptions)) {



                        showOptionPopup(Hascomment,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                    } else if
                    (cursor != null && cursor.moveToFirst()) {
                        // Retrieve the existing transaction ID for the item
                        existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));

                        sentToKitchen = String.valueOf(mDatabaseHelper.getLatestSentToKitchen(Barcode,existingTransactionId));
                        Log.d("sentToKitchen1", sentToKitchen);

                        if (sentToKitchen.equals("1")) {

                            boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));
                            int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));
                            if(existingitems )
                            {


                                    // Item is paid, insert a new transaction with IS_PAID as 0
                                    item = dbManager.getItemById(String.valueOf(itemId));
                                    if (item != null) {
                                        // Other item details extraction code...
                                        String CompanyShopNumber;
                                        unitprice = item.getPrice();
                                        double UnitPrice = Double.parseDouble(price);
                                        double newTotalPrice = UnitPrice * 1;
                                        double taxbeforediscount = calculateinitialTax(String.valueOf(unitprice));

                                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                        cashierId = sharedPreference.getString("cashorId", null);

                                        String ShopName = sharedPreference.getString("ShopName", null);

                                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                            int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                            String MRAMETHOD = "Single";
                                            CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                            int catId = mDatabaseHelper.getCatIdFromName(catname);
                                            String Catnum = String.valueOf(catId);
                                            Log.d("roomtable1", roomid + " " + tableid);
                                            // Insert a new transaction with IS_PAID as 0
                                            mDatabaseHelper.insertTransaction(itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                        }
                                        refreshTicketFragment();
                                        refreshTotal();

                                }else {
                                        Log.d("sentToKitchentest1", sentToKitchen);
                                    // Item already selected, update the quantity and total price
                                     currentQuantity = mDatabaseHelper.getQuantity(transactionId,Barcode);

                                    UnitPrice = Double.parseDouble(price);

                                    double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                                    int newQuantity = currentQuantity + 1;
                                    double pricewithoutVat= Double.parseDouble(priceWithoutVat);
                                    double newpriceWithoutVat= pricewithoutVat * newQuantity;
                                    double totaltaxbeforediscount= taxbeforediscount * newQuantity;
                                    double newTotalDiscount= newQuantity * TotalDiscount;
                                    double taxafterdiscount= calculateTax();
                                    double newtotaltaxafterdiscount= taxafterdiscount * newQuantity;
                                    double newTotalPrice = UnitPrice * newQuantity;
                                    double newVat = newQuantity * calculateTax();
                                    mDatabaseHelper.updateTransaction(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);
                                    refreshTicketFragment();
                                    refreshTotal();
                                }
                            }

                            }

                        // Check if the existing transaction ID matches the current transaction ID
                        else if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {
                            Log.d("sentToKitchentestupdate2", sentToKitchen);
                            Log.d("existingTransactionId1", existingTransactionId);
                            Log.d("transactionIdInProgress", transactionIdInProgress);
                            Log.d("latesttransId", latesttransId);
                            Log.d("sentToKitchentest1", sentToKitchen);
                            // Item already selected, update the quantity and total price
                            int currentQuantity = mDatabaseHelper.getQuantity(latesttransId,Barcode);

                            UnitPrice = Double.parseDouble(price);

                            double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                            int newQuantity = currentQuantity + 1;
                            double pricewithoutVat= Double.parseDouble(priceWithoutVat);
                            double newpriceWithoutVat= pricewithoutVat * newQuantity;
                            double totaltaxbeforediscount= taxbeforediscount * newQuantity;
                            double newTotalDiscount= newQuantity * TotalDiscount;
                            double taxafterdiscount= calculateTax();
                            double newtotaltaxafterdiscount= taxafterdiscount * newQuantity;
                            double newTotalPrice = UnitPrice * newQuantity;
                            double newVat = newQuantity * calculateTax();
                            mDatabaseHelper.updateTransaction(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);
                            refreshTicketFragment();
                            refreshTotal();
                        }else {
                            Log.d("existingTransactionId", existingTransactionId);
                            Log.d("transactionIdInProgress", transactionIdInProgress);
                            Log.d("latesttransId", latesttransId);
                            Log.d("sentToKitchentest1", sentToKitchen);
                                    // Item is paid, insert a new transaction with IS_PAID as 0
                                    item = dbManager.getItemById(String.valueOf(itemId));
                                    if (item != null) {
                                        // Other item details extraction code...
                                        String CompanyShopNumber;
                                        unitprice = item.getPrice();
                                        double UnitPrice = Double.parseDouble(price);
                                        double newTotalPrice = UnitPrice * 1;
                                        double taxbeforediscount = calculateinitialTax(String.valueOf(unitprice));

                                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                        cashierId = sharedPreference.getString("cashorId", null);

                                        String ShopName = sharedPreference.getString("ShopName", null);

                                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                            int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                            String MRAMETHOD = "Single";
                                            CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                            int catId = mDatabaseHelper.getCatIdFromName(catname);
                                            String Catnum = String.valueOf(catId);
                                            Log.d("roomtable2", roomid + " " + tableid);
                                            // Insert a new transaction with IS_PAID as 0
                                            mDatabaseHelper.insertTransaction(itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                        }
                                        refreshTicketFragment();
                                        refreshTotal();
                                    }
                                }


                    } else {
                        item = dbManager.getItemById(String.valueOf(itemId));
                        if (item != null) {
                            VatVall = item.getVAT();
                            unitprice = item.getPrice();
                            priceAfterDiscount = item.getPriceAfterDiscount();
                            VatType = item.getVAT();
                            TotalDiscount = item.getTotalDiscount();
                            TaxCode = item.getTaxCode();
                            Nature = item.getNature();
                            Currency = item.getCurrency();
                            ItemCode = item.getItemCode();
                            double UnitPrice = Double.parseDouble(price);
                            double newTotalPrice = UnitPrice * 1;
                            double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                            Barcode=item.getBarcode();
                            Weight=item.getWeight();
                            catname=item.getCategory();
                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                            cashierId = sharedPreference.getString("cashorId", null);
                            String CompanyShopNumber;
                            String ShopName = sharedPreference.getString("ShopName", null);

                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                String MRAMETHOD = "Single";
                                CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                int catId = mDatabaseHelper.getCatIdFromName(catname);

                                String Catnum= String.valueOf(catId);
                                Log.d("roomtable3", roomid + " " + tableid);
                                Log.d("transactionId2", transactionId );
                    boolean hasitem =mDatabaseHelper.hasItemsForTransactionId(transactionId);
                    if(!hasitem){
                        showNumberOfCoversDialog(transactionId);
                    }
                                // Item not selected, insert a new transaction with quantity 1 and total price
                                mDatabaseHelper.insertTransaction(itemId, Barcode, Weight,taxbeforediscount,currentpriceWithoutVat, CompanyShopNumber,Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

                            }
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
                        unitprice = item.getPrice();
                        TotalDiscount = item.getTotalDiscount();
                        VatType = item.getVAT();
                        TaxCode = item.getTaxCode();
                        Nature = item.getNature();
                        Currency = item.getCurrency();
                        ItemCode = item.getItemCode();
                        Barcode=item.getBarcode();
                        Weight=item.getWeight();
                        catname=item.getCategory();
                        Log.d("SendToHeader1", "SendToHeader" );

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
    // Method to show popup dialog for number of covers
    private void showNumberOfCoversDialog(String transactionid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Number of Covers");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numberOfCoversStr = input.getText().toString().trim();
                if (!numberOfCoversStr.isEmpty()) {
                    try {
                        int numberOfCovers = Integer.parseInt(numberOfCoversStr);
                        // Use numberOfCovers variable as needed
                        processNumberOfCovers(transactionid,numberOfCovers);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Number of covers cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Method to process the number of covers entered
    private void processNumberOfCovers(String trsansactionid,int numberOfCovers) {
        // Use the numberOfCovers variable as needed in your code
        // For example, store it as a class member or pass it to another method
        Log.d("NumberOfCovers", "Number of covers entered: " + numberOfCovers);
mDatabaseHelper.updateNumberOfCovers(trsansactionid,numberOfCovers);
        // Continue with your application logic after setting numberOfCovers
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

        // Find the EditText field in the custom layout
        editTextOption1 = dialogView.findViewById(R.id.editTextOption1);
        editTextOption1.setInputType(InputType.TYPE_NULL);
        editTextOption1.setTextIsSelectable(true);
        // Find the number buttons and set OnClickListener
        Button button1 = dialogView.findViewById(R.id.button1);
        Button button2 = dialogView.findViewById(R.id.button2);
        Button button3 = dialogView.findViewById(R.id.button3);
        Button button4 = dialogView.findViewById(R.id.button4);
        Button button5 = dialogView.findViewById(R.id.button5);
        Button button6 = dialogView.findViewById(R.id.button6);
        Button button7 = dialogView.findViewById(R.id.button7);
        Button button8 = dialogView.findViewById(R.id.button8);
        Button button9 = dialogView.findViewById(R.id.button9);
        Button button0 = dialogView.findViewById(R.id.button0);
        Button buttonbackspace = dialogView.findViewById(R.id.buttonbackspace);
        Button buttonComma = dialogView.findViewById(R.id.buttonComma);
        Button buttonClear = dialogView.findViewById(R.id.buttonClear);
        Button buttonA = dialogView.findViewById(R.id.buttonInsertA);
        Button buttonB = dialogView.findViewById(R.id.buttonInsertB);
        Button buttonC = dialogView.findViewById(R.id.buttonInsertC);
        Button buttonD = dialogView.findViewById(R.id.buttonInsertD);
        Button buttonE = dialogView.findViewById(R.id.buttonInsertE);
        Button buttonF = dialogView.findViewById(R.id.buttonInsertF);
        Button buttonG = dialogView.findViewById(R.id.buttonInsertG);
        Button buttonH = dialogView.findViewById(R.id.buttonInsertH);
        Button buttonI = dialogView.findViewById(R.id.buttonInsertI);
        Button buttonJ = dialogView.findViewById(R.id.buttonInsertJ);
        Button buttonK = dialogView.findViewById(R.id.buttonInsertK);
        Button buttonL = dialogView.findViewById(R.id.buttonInsertL);
        Button buttonM = dialogView.findViewById(R.id.buttonInsertM);
        Button buttonN = dialogView.findViewById(R.id.buttonInsertN);
        Button buttonO = dialogView.findViewById(R.id.buttonInsertO);
        Button buttonP = dialogView.findViewById(R.id.buttonInsertP);
        Button buttonQ = dialogView.findViewById(R.id.buttonInsertQ);
        Button buttonR = dialogView.findViewById(R.id.buttonInsertR);
        Button buttonS = dialogView.findViewById(R.id.buttonInsertS);
        Button buttonT = dialogView.findViewById(R.id.buttonInsertT);
        Button buttonU = dialogView.findViewById(R.id.buttonInsertU);
        Button buttonV = dialogView.findViewById(R.id.buttonInsertV);
        Button buttonW = dialogView.findViewById(R.id.buttonInsertW);
        Button buttonX = dialogView.findViewById(R.id.buttonInsertX);
        Button buttonY = dialogView.findViewById(R.id.buttonInsertY);
        Button buttonZ = dialogView.findViewById(R.id.buttonInsertZ);
        Button buttonSpace = dialogView.findViewById(R.id.buttonInsertspace);
        // Set OnClickListener for number buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "0");
            }
        });



        buttonbackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackspaceButtonClick();
            }
        });

        buttonComma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, ",");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }        });


        buttonSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, " ");
            }
        });
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "A");
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "B");
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "C");
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "D");
            }
        });
        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "E");
            }
        });
        buttonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "F");
            }
        });
        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "G");
            }
        });
        buttonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "H");
            }
        });
        buttonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "I");
            }
        });
        buttonJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "J");
            }
        });
        buttonK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "K");
            }
        });
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "L");
            }
        });
        buttonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "M");
            }
        });
        buttonN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "N");
            }
        });
        buttonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "O");
            }
        });
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "P");
            }
        });
        buttonQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Q");
            }
        });
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "R");
            }
        });
        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "S");
            }
        });
        buttonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "T");
            }
        });
        buttonU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "U");
            }
        });
        buttonV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "V");
            }
        });
        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "W");
            }
        });
        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "X");
            }
        });
        buttonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Y");
            }
        });
        buttonZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Z");
            }
        });



        if (variantList != null && !variantList.isEmpty()) {
            for (Variant variant : variantList) {
                // Log each VARIANT_DESC

                LinearLayout variantButtonsLayout = dialogView.findViewById(R.id.variantButtonsLayout);

                Button variantButton = new Button(getContext());
                variantButton.setText(variant.getDescription()); // Set the button text to the variant description
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String NewBarcode=variant.getBarcode();

                Log.d("NewBarcode", String.valueOf(NewBarcode));

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
                        Log.d("relateditemsfound1", "No variants found for ID " + id);
                        insertdata(HasOptions,SupplementsItem ,id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,NewBarcode,newDesc,newprice,newitemid);

                    /*   if (getContext() != null && variantButtonsLayout != null) { // Create and add EditText dynamically
                        option1edittext = new EditText(getContext());
                        option1edittext.setHint(variant.getDescription() +" - Enter your comment");

                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 1, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option1edittext, params);

                        } else {
                            Log.e("InsertData", "Context or variantButtonsLayout is null");
                        }

                     */
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
                String NewBarcode=variant.getBarcode();
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
                        Log.d("relateditemsfound2", "No variants found for ID " + id);

                        insertdata(HasOptions,SupplementsItem,id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,NewBarcode,newDesc,newprice,newitemid);
                        // Create and add EditText dynamically
                      /*  if (getContext() != null && variantButtonsLayout != null) {
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

                       */
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
                String NewBarcode=variant.getBarcode();
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
                        Log.d("relateditemsfound3", "No variants found for ID " + id);

                        insertdata(HasOptions,SupplementsItem,id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,NewBarcode,newDesc,newprice,newitemid);

                     /*   option3edittext = new EditText(getContext());
                        option3edittext.setHint(variant.getDescription() +" - Enter your comment");

                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 16, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option3edittext, params);

                      */
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

                String NewBarcode=variant.getBarcode();
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
                        Log.d("relateditemsfound4", "No variants found for ID " + id);

                        insertdata(HasOptions,SupplementsItem,id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,NewBarcode,newDesc,newprice,newitemid);

                /*        option4edittext = new EditText(getContext());
                        option4edittext.setHint(variant.getDescription() +" - Enter your comment");
                        // Create layout parameters to define how the EditText should be positioned
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 16, 0, 0); // Adjust margins as needed

                        // Add the EditText below the button
                        variantButtonsLayout.addView(option4edittext, params);

                 */
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
                String NewBarcode=variant.getBarcode();
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
                        Log.d("relateditemsfound5", "No variants found for ID " + id);

                        insertdata(HasOptions,SupplementsItem,id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,NewBarcode,newDesc,newprice,newitemid);

                     /*   // Create and add EditText dynamically
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

                      */
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
                NewBarcode=variant.getBarcode();
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
                        Log.d("relateditemsfound6", "No variants found for ID " + id);

                        insertdata(HasOptions,SupplementsItem,id,itemId,transactionId,transactionDate,vat,longDescription,priceWithoutVat,NewBarcode,newDesc,newprice,newitemid);

                /*        // Create and add EditText dynamically
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

                 */
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
        builder.setTitle("Menu");



        // Add positive button (OK button)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the comment from the EditText fields
                String option1Text = editTextOption1.getText().toString();
                // Check if the EditText is not null and not empty
                if (!TextUtils.isEmpty(option1Text)) {
                    // Create an Intent to launch SendNoteToKitchenActivity
                    Intent intent = new Intent(getContext(), SendNoteToKitchenActivity.class);
                    // Put the text as an extra in the Intent
                    intent.putExtra("note_text", option1Text);
                    // Start the activity
                    startActivity(intent);
                } else {
                    // Show a toast or alert indicating that the EditText is empty
                    Toast.makeText(getContext(), "Please enter a note", Toast.LENGTH_SHORT).show();
                }

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

    public void insertdata(Boolean HasOption,String SupplementsItem,String id,int itemId,String transactionId, String transactionDate,double vat, String longDescription,double priceWithoutVat,String NewBarcode, String newdesc,double newprice,String newitemid) {
        Item item = dbManager.getItemById(id);
        Log.d("newbarcode", NewBarcode);
        Log.d("newitemid", newitemid);
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        Cursor cursor = mDatabaseHelper.getTransactionByItemId(Integer.parseInt(newitemid), String.valueOf(roomid), tableid);
        if(mDatabaseHelper.isItemIdExists(newitemid)) {
            longDescription = longDescription + "-" + newdesc;
        }else if (mDatabaseHelper.isVariantBarcodeExists(newitemid)){
            longDescription = "Sup" + "-" + newdesc;
        }
        if
        (cursor != null && cursor.moveToFirst()) {
            // Retrieve the existing transaction ID for the item
            existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
            sentToKitchen = String.valueOf(mDatabaseHelper.getLatestSentToKitchen(NewBarcode,existingTransactionId));

            Log.d("sentToKitchen4", sentToKitchen);
            // Check if the existing transaction ID matches the current transaction ID
            if (sentToKitchen.equals("1")) {
                Log.d("sentToKitchentest2", sentToKitchen);

                boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));
                if(existingitems )
                {


                    // Item is paid, insert a new transaction with IS_PAID as 0
                    item = dbManager.getItemById(String.valueOf(itemId));
                    if (item != null) {
                        // Other item details extraction code...
                        String CompanyShopNumber;
                       double unitprice = item.getPrice();
                        String VatType= item.getVAT();
                        double UnitPrice = Double.parseDouble(price);
                        double newTotalPrice = UnitPrice * 1;
                        double taxbeforediscount = calculateinitialTax(String.valueOf(unitprice));
                        double vatAmount=0;
                        if (VatType.equals("VAT 15%")) {
                            double vatRate = 0.15; // 15% VAT rate

                            // Calculate the price without VAT
                            double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                            // Calculate the VAT amount
                             vatAmount = newTotalPrice - priceExcludingVAT;

                            // Logging the calculated values
                            Log.d("PriceExcludingVAT5", String.valueOf(priceExcludingVAT));
                            Log.d("VATAmount5", String.valueOf(vatAmount));
                        }
                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                        cashierId = sharedPreference.getString("cashorId", null);

                        String ShopName = sharedPreference.getString("ShopName", null);

                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                            int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                            double currentpriceWithoutVat = calculatecurrentPricewithoutTax(unitprice);
                            String MRAMETHOD = "Single";
                            CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                            int catId = mDatabaseHelper.getCatIdFromName(catname);
                            String Catnum = String.valueOf(catId);
                            Log.d("roomtable4", roomid + " " + tableid);
                            // Insert a new transaction with IS_PAID as 0
                            mDatabaseHelper.insertTransaction(Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                        }
                        refreshTicketFragment();
                        refreshTotal();

                    }else {
                        Log.d("sentToKitchentest", sentToKitchen);
                        // Item already selected, update the quantity and total price
                        int currentQuantity = mDatabaseHelper.getQuantity(latesttransId,NewBarcode);
                        double unitprice = item.getPrice();
                        UnitPrice = Double.parseDouble(price);

                        double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                        int newQuantity = currentQuantity + 1;
                        double pricewithoutVat= Double.parseDouble(String.valueOf(priceWithoutVat));
                        double newpriceWithoutVat= pricewithoutVat * newQuantity;
                        double totaltaxbeforediscount= taxbeforediscount * newQuantity;
                        double newTotalDiscount= newQuantity * TotalDiscount;


                        double newTotalPrice = UnitPrice * newQuantity;
                        double vatAmount=0;
                        if (VatType.equals("VAT 15%")) {
                            double vatRate = 0.15; // 15% VAT rate

                            // Calculate the price without VAT
                            double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                            // Calculate the VAT amount
                            vatAmount = newTotalPrice - priceExcludingVAT;

                            // Logging the calculated values
                            Log.d("PriceExcludingVAT6", String.valueOf(priceExcludingVAT));
                            Log.d("VATAmount6", String.valueOf(vatAmount));
                        }
                        double newVat =  vatAmount;
                        mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid),newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);
                        refreshTicketFragment();
                        refreshTotal();
                    }
                }

            }

            // Check if the existing transaction ID matches the current transaction ID
            else if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {

                Log.d("existingTransactionId2", existingTransactionId);
                Log.d("transactionIdInProgress", transactionIdInProgress);
                Log.d("latesttransId", latesttransId);
                Log.d("sentToKitchentest1", sentToKitchen);
                Log.d("NewBarcode", NewBarcode);
                // Item already selected, update the quantity and total price
                int currentQuantity = mDatabaseHelper.getQuantity(latesttransId,NewBarcode);
                Log.d("currentQuantity", String.valueOf(currentQuantity));
                UnitPrice = newprice;
                double unitprice = item.getPrice();

                double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                int newQuantity = currentQuantity + 1;
                double pricewithoutVat= priceWithoutVat;
                double newpriceWithoutVat= pricewithoutVat * newQuantity;
                double totaltaxbeforediscount= taxbeforediscount * newQuantity;
                double newTotalDiscount= newQuantity * TotalDiscount;
                double newTotalPrice = UnitPrice * newQuantity;
                double vatAmount=0;
                if (VatType.equals("VAT 15%")) {
                    double vatRate = 0.15; // 15% VAT rate

                    // Calculate the price without VAT
                    double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                    // Calculate the VAT amount
                    vatAmount = newTotalPrice - priceExcludingVAT;

                    // Logging the calculated values
                    Log.d("PriceExcludingVAT2", String.valueOf(priceExcludingVAT));
                    Log.d("VATAmount2", String.valueOf(vatAmount));
                }
                double newVat =  vatAmount;


                double newtotaltaxafterdiscount= newVat ;
                mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid),newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid), tableid);
                Log.d("newQuantity", String.valueOf(newQuantity));
                refreshTicketFragment();
                refreshTotal();
            } else {

                Log.d("existingTransactionId3", existingTransactionId);
                Log.d("transactionIdInProgress", transactionIdInProgress);
                Log.d("latesttransId", latesttransId);
                Log.d("sentToKitchentest3", sentToKitchen);
                Log.d("NewBarcode3", NewBarcode);
                if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_PAID)) == 1) {
                    if (sentToKitchen.equals("1")) {

                        // Item is paid, insert a new transaction with IS_PAID as 0
                        item = dbManager.getItemById(String.valueOf(itemId));
                        if (item != null) {
                            // Other item details extraction code...
                            double unitprice = item.getPrice();

                            double UnitPrice = newprice;
                            double newTotalPrice = UnitPrice * 1;
                            double taxbeforediscount = calculateinitialTax(String.valueOf(unitprice));

                            String CompanyShopNumber;
                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                            String ShopName = sharedPreference.getString("ShopName", null);

                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                                double currentpriceWithoutVat = calculatecurrentPricewithoutTax(unitprice);

                                String MRAMETHOD = "Single";
                                CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                int catId = mDatabaseHelper.getCatIdFromName(catname);
                                String Catnum = String.valueOf(catId);
                                Log.d("roomtable5", roomid + " " + tableid);
                                double vatAmount=0;
                                if (VatType.equals("VAT 15%")) {
                                    double vatRate = 0.15; // 15% VAT rate

                                    // Calculate the price without VAT
                                    double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                                    // Calculate the VAT amount
                                    vatAmount = newTotalPrice - priceExcludingVAT;

                                    // Logging the calculated values
                                    Log.d("PriceExcludingVAT3", String.valueOf(priceExcludingVAT));
                                    Log.d("VATAmount3", String.valueOf(vatAmount));
                                }

                                // Insert a new transaction with IS_PAID as 0
                                mDatabaseHelper.insertTransaction(Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                            }
                            refreshTicketFragment();
                            refreshTotal();
                        }
                    }
                }
            }
        } else {
            item = dbManager.getItemById(String.valueOf(itemId));
            double unitprice = item.getPrice();

            double UnitPrice = newprice;
            double newTotalPrice = UnitPrice * 1;
            double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

            String VatType= item.getVAT();

            double vatAmount=0;
            if (VatType.equals("VAT 15%")) {
                double vatRate = 0.15; // 15% VAT rate

                // Calculate the price without VAT
                double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                // Calculate the VAT amount
                vatAmount = newTotalPrice - priceExcludingVAT;

                // Logging the calculated values
                Log.d("PriceExcludingVAT4", String.valueOf(priceExcludingVAT));
                Log.d("VATAmount4", String.valueOf(vatAmount));
            }

            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

            String ShopName = sharedPreference.getString("ShopName", null);
            String CompanyShopNumber;
            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                double currentpriceWithoutVat  = calculatecurrentPricewithoutTax(unitprice);

                String MRAMETHOD = "Single";
                CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                int catId = mDatabaseHelper.getCatIdFromName(catname);
                String Catnum= String.valueOf(catId);
                Log.d("INSERT_Transaction", "4");
                Log.d("roomtable6", roomid + " " + tableid);
                boolean hasitem =mDatabaseHelper.hasItemsForTransactionId(transactionId);
                if(!hasitem){
                    showNumberOfCoversDialog(transactionId);
                }
                // Item not selected, insert a new transaction with quantity 1 and total price
                mDatabaseHelper.insertTransaction(Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
            } refreshTicketFragment();
            refreshTotal();

        }

        if (cursor != null) {
            cursor.close();
        }
        item = dbManager.getItemById(id);
        if (item != null) {
            VatVall = item.getVAT();

            double unitprice = item.getPrice();
            Log.d("SendToHeader2", unitprice +" " + calculateTax());

            SendToHeader(unitprice, calculateTax());
        }
        // Notify the listener that an item is added
        if (itemAddedListener != null) {
            itemAddedListener.onItemAdded(String.valueOf(roomid), tableid);
        }

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
                Barcode=item.getBarcode();
                catname=item.getCategory();
                String transactionStatus = "InProgress";
                String transactionSaved = "PFT";
                String transactionCDN = "CRN";
                String transactionDBN = "DRN";



                if (Available4sale != null && Available4sale.equalsIgnoreCase("true")) {
                    if (transactionStatus.equals("InProgress") || transactionSaved.equals("PRF")|| transactionCDN.equals("CRN")|| transactionDBN.equals("DRN")) {
                        if (!isRoomTableInProgress(String.valueOf(roomid), tableid)) {
                            Log.d("INSERT_Transactions3", "test1" );
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
                        Log.d("INSERT_Transaction4", "test1" );
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

                        String  statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                        mDatabaseHelper.updateTransactionIds(latesttransId, String.valueOf(roomid),tableid);
                        refreshTicketFragment();
                        refreshTotal();
                    }

// Check if the item with the same ID is already selected
                    Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId, String.valueOf(roomid),tableid);
                    if (cursor != null && cursor.moveToFirst()) {
                        // Retrieve the existing transaction ID for the item
                        existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                        sentToKitchen = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_SentToKitchen));
                        Log.d("sentToKitchen2", sentToKitchen);
                        // Check if the existing transaction ID matches the current transaction ID
                        if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {
                            Log.d("existingTransactionId3", existingTransactionId);
                            Log.d("transactionIdInProgress", transactionIdInProgress);
                            Log.d("latesttransId", latesttransId);
                            Log.d("sentToKitchentest1", sentToKitchen);
                            // Item already selected, update the quantity and total price
                            int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));

                            UnitPrice = Double.parseDouble(price);
                            double unitprice=item.getPrice();
                            double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                            int newQuantity = currentQuantity + 1;
                            double pricewithoutVat= Double.parseDouble(priceWithoutVat);
                            double newpriceWithoutVat= pricewithoutVat * newQuantity;
                            double totaltaxbeforediscount= taxbeforediscount * newQuantity;
                            double newTotalDiscount= newQuantity * TotalDiscount;
                            double newTotalPrice = UnitPrice * newQuantity;
                            double newVat = newQuantity * calculateTax();
                            double taxafterdiscount= calculateTax();
                            double newtotaltaxafterdiscount= taxafterdiscount * newQuantity;
                            mDatabaseHelper.updateTransaction(itemId, newpriceWithoutVat,newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);
                            refreshTicketFragment();
                            refreshTotal();
                        } else {
                            if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_PAID)) == 1) {
                                if (sentToKitchen.equals("1")) {
                                    item = dbManager.getItemById(String.valueOf(itemId));
                                    if (item != null) {
                                        VatVall = item.getVAT();
                                        double unitprice = item.getPrice();
                                        priceAfterDiscount = item.getPriceAfterDiscount();
                                        TotalDiscount = item.getTotalDiscount();
                                        VatType = item.getVAT();
                                        TaxCode = item.getTaxCode();
                                        Nature = item.getNature();
                                        Currency = item.getCurrency();
                                        ItemCode = item.getItemCode();
                                        Barcode = item.getBarcode();
                                        catname = item.getCategory();
                                        double UnitPrice = Double.parseDouble(price);
                                        double newTotalPrice = UnitPrice * 1;
                                        Weight = item.getWeight();
                                        double taxbeforediscount = calculateinitialTax(String.valueOf(unitprice));

                                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                                        String ShopName = sharedPreference.getString("ShopName", null);
                                        String CompanyShopNumber;
                                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                            int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                                            double currentpriceWithoutVat = calculatecurrentPricewithoutTax(unitprice);

                                            String MRAMETHOD = "Single";
                                            CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                            int catId = mDatabaseHelper.getCatIdFromName(catname);
                                            String Catnum = String.valueOf(catId);
                                            Log.d("INSERT_Transaction", "5");
                                            // Item has a different transaction ID, insert a new transaction
                                            Log.d("roomtable7", roomid + " " + tableid);
                                            mDatabaseHelper.insertTransaction(itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                        }
                                        refreshTicketFragment();
                                        refreshTotal();

                                    }
                                }}
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
                            Barcode=item.getBarcode();
                            Weight=item.getWeight();
                            double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                            String ShopName = sharedPreference.getString("ShopName", null);
                            String CompanyShopNumber;
                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                                double currentpriceWithoutVat  = calculatecurrentPricewithoutTax(unitprice);

                                String MRAMETHOD = "Single";
                                CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                int catId = mDatabaseHelper.getCatIdFromName(catname);
                                String Catnum= String.valueOf(catId);
                                Log.d("INSERT_Transaction", "6");
                                Log.d("roomtable8", roomid + " " + tableid);
                                // Item not selected, insert a new transaction with quantity 1 and total price
                                mDatabaseHelper.insertTransaction(itemId, Barcode, Weight,taxbeforediscount,currentpriceWithoutVat, CompanyShopNumber,Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                            }
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
                        Barcode=item.getBarcode();
                        Log.d("SendToHeader3", "SendToHeader" );

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
                        TextView realpriceTextView = view.findViewById(R.id.price_text_view);

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
                            Barcode=item.getBarcode();
                        }


                        String transactionStatus = "InProgress";


                        String status = mDatabaseHelper.getTransactionStatus(transactionIdInProgress);


                        if (status.equals("InProgress") || status.equals("PRF") || status.equals("DRN") || status.equals("CRN")  ) {
                            if (!isRoomTableInProgress(String.valueOf(roomid), tableid)) {
                                Log.d("INSERT_Transactions5", "test1" );
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
                            Log.d("INSERT_Transactions6", "test1" );
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

                            String  statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                            String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                            mDatabaseHelper.updateTransactionIds(latesttransId, String.valueOf(roomid),tableid);

                            refreshTicketFragment();
                            refreshTotal();
                        }

                        // Check if the item with the same ID is already selected
                        Cursor cursor = mDatabaseHelper.getTransactionByItemId(itemId, String.valueOf(roomid),tableid);
                        if (cursor != null && cursor.moveToFirst()) {
                            existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));
                            sentToKitchen = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_SentToKitchen));
                            Log.d("sentToKitchen3", sentToKitchen);
                            // Check if the existing transaction ID matches the current transaction ID
                            if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {
                                Log.d("existingTransactionId4", existingTransactionId);
                                Log.d("transactionIdInProgress", transactionIdInProgress);
                                Log.d("latesttransId", latesttransId);
                                Log.d("sentToKitchentest1", sentToKitchen);
                                // Item already selected, update the quantity and total price
                                int currentQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUANTITY));

                                UnitPrice = Double.parseDouble(price);

                                TotalDiscount=item.getTotalDiscount();
                                double unitprice=item.getPrice();
                                double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));


                                int newQuantity = currentQuantity + 1;
                                double pricewithoutVat= Double.parseDouble(priceWithoutVat);
                                double newpriceWithoutVat= pricewithoutVat * newQuantity;
                                double totaltaxbeforediscount= taxbeforediscount * newQuantity;
                                double newTotalDiscount= newQuantity * TotalDiscount;
                                double  newTotalPrice = UnitPrice * newQuantity;
                                double newVat= newQuantity * calculateTax();
                                double taxafterdiscount= calculateTax();
                                double newtotaltaxafterdiscount= taxafterdiscount * newQuantity;
                                mDatabaseHelper.updateTransaction(itemId, newpriceWithoutVat,newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount,newVat,VatType, String.valueOf(roomid),tableid);
                                refreshTicketFragment();
                                refreshTotal();
                            } else {
                                if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_PAID)) == 1) {
                                    if (sentToKitchen.equals("1")) {
                                        item = dbManager.getItemById(String.valueOf(itemId));
                                        if (item != null) {
                                            VatVall = item.getVAT();
                                            priceAfterDiscount = item.getPriceAfterDiscount();
                                            double unitprice = item.getPrice();
                                            VatType = item.getVAT();
                                            TotalDiscount = item.getTotalDiscount();
                                            TaxCode = item.getTaxCode();
                                            Nature = item.getNature();
                                            Currency = item.getCurrency();
                                            ItemCode = item.getItemCode();
                                            double UnitPrice = Double.parseDouble(price);
                                            double newTotalPrice = UnitPrice * 1;
                                            Barcode = item.getBarcode();
                                            Weight = item.getWeight();
                                            double taxbeforediscount = calculateinitialTax(String.valueOf(unitprice));

                                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                                            String ShopName = sharedPreference.getString("ShopName", null);
                                            String CompanyShopNumber;
                                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                                                double currentpriceWithoutVat = calculatecurrentPricewithoutTax(unitprice);

                                                String MRAMETHOD = "Single";
                                                CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                                int catId = mDatabaseHelper.getCatIdFromName(catname);
                                                String Catnum = String.valueOf(catId);
                                                Log.d("INSERT_Transaction", "7 ");
                                                Log.d("roomtable9", roomid + " " + tableid);
                                                // Item has a different transaction ID, insert a new transaction
                                                mDatabaseHelper.insertTransaction(itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                            }
                                            refreshTicketFragment();
                                            refreshTotal();
                                        }
                                    }}
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
                                Barcode=item.getBarcode();
                                Weight=item.getWeight();
                                double taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);

                                String ShopName = sharedPreference.getString("ShopName", null);
                                String CompanyShopNumber;
                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);
                                    double currentpriceWithoutVat  = calculatecurrentPricewithoutTax(unitprice);

                                    String MRAMETHOD = "Single";
                                    CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);

                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                    String Catnum= String.valueOf(catId);
                                    Log.d("INSERT_Transaction", "8 ");
                                    Log.d("roomtable10", roomid + " " + tableid);
                                    // Item not selected, insert a new transaction with quantity 1 and total price
                                    mDatabaseHelper.insertTransaction(itemId, Barcode, Weight,taxbeforediscount,currentpriceWithoutVat, CompanyShopNumber,Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                }refreshTicketFragment();
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
                            Log.d("SendToHeader4", "SendToHeader" );

                            SendToHeader(unitprice, calculateTax());
                            Barcode=item.getBarcode();
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

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void refreshTotal() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            ticketFragment.updateheader(calculateTotalAmount(),calculateTotalTax());
        }
    }
    public double calculateTax() {
        double taxAmount = 0.0;

        double unitPriceInclusive = Double.parseDouble(price);
        double unitPriceExclusive = unitPriceInclusive / 1.15; // Removing 15% VAT to get exclusive price

        if (VatVall.equals("VAT 15%")) {
            taxAmount = unitPriceExclusive * 0.15; // Calculate VAT based on exclusive price
        }

        // Ensure that the tax amount is rounded to two decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return taxAmount;
    }
    public double calculateinitialTax(String initialprice) {
        double taxAmount = 0.0;

        double unitPriceInclusive = Double.parseDouble(initialprice);
        double unitPriceExclusive = unitPriceInclusive / 1.15; // Removing 15% VAT to get exclusive price

        if (VatVall.equals("VAT 15%")) {
            taxAmount = unitPriceExclusive * 0.15; // Calculate VAT based on exclusive price
        }

        // Ensure that the tax amount is rounded to two decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return taxAmount;
    }
    public double calculatePricewithoutTax() {
        double priceWithoutVat = 0.0;
        double unitPriceInclusive = Double.parseDouble(price);

        if (VatVall.equals("VAT 15%")) {
            priceWithoutVat = unitPriceInclusive / 1.15; // Calculate price without VAT
        } else {
            priceWithoutVat = unitPriceInclusive; // No VAT, price remains the same
        }

        // Ensure that the price without VAT is rounded to two decimal places
        priceWithoutVat = Math.round(priceWithoutVat * 100.0) / 100.0;

        return priceWithoutVat;
    }

    public double calculatecurrentPricewithoutTax(double currentprice) {
        double priceWithoutVat = 0.0;
        double unitPriceInclusive = currentprice;

        if (VatVall.equals("VAT 15%")) {
            priceWithoutVat = unitPriceInclusive / 1.15; // Calculate price without VAT
        } else {
            priceWithoutVat = unitPriceInclusive; // No VAT, price remains the same
        }

        // Ensure that the price without VAT is rounded to two decimal places
        priceWithoutVat = Math.round(priceWithoutVat * 100.0) / 100.0;

        return priceWithoutVat;
    }
    public double calculateTotalAmount() {

        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
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
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
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

            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

            double sumBeforeDisc = mDatabaseHelper.getSumOfTransactionVATBeforeDiscByTransactionId(db,transactionIdInProgress);
            double sumAfterDisc = mDatabaseHelper.getSumOfTransactionVATAfterDiscByTransactionId(db,transactionIdInProgress);

            db.close();
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
                        tableid,
                        sumBeforeDisc,
                        sumAfterDisc

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


    private void onBackspaceButtonClick() {
        // Get the current text from editTextOption1
        Editable editable = editTextOption1.getText();

        // Get the length of the text
        int length = editable.length();

        // If there are characters in the text, remove the last character
        if (length > 0) {
            editable.delete(length - 1, length);
        }
    }
    public void onClearButtonClick(View view) {

        onclearButtonClick();


    }

    private void onclearButtonClick() {
        editTextOption1.setText(""); // Set the text of editTextOption1 to an empty string
    }

    public void oncommentButtonClick(View view, String letter) {
        if (editTextOption1 != null) {
            // Insert the letter into the EditText
            editTextOption1.append(letter);
        }
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