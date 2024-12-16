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

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import androidx.gridlayout.widget.GridLayout;
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

    private StringBuilder enteredcomment;
    private AlertDialog alertDialog; // Declare as a member variable

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
    String newitemid;
    private String Nature,Hascomment,HasSupplements, RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem;
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
        // Set up the result listener for search queries


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
        getParentFragmentManager().setFragmentResultListener("searchQuery", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {


                if (requestKey.equals("searchQuery")) {
                    // Retrieve search criteria from the bundle
                    String searchDescription = result.getString("description", "");


                    // Perform the search with the provided criteria
                    Cursor cursor;
                     cursor = mDatabaseHelper.searchItems(searchDescription);

                    if (searchDescription == null || searchDescription.isEmpty()) {
                        cursor = mDatabaseHelper.getAllItems();  // Assuming you have a method to get all items
                    } else {
                        cursor = mDatabaseHelper.searchItems(searchDescription);  // Use searchItems() from DatabaseHelper
                    }
                    if (cursor != null) {
                        mAdapter = new ItemGridAdapter(getContext(), cursor);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {

                    }

                }



            }
        });
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
                        HasSupplements=item.gethassupplements();
                        Hascomment=item.gethascomment();
                        RelatedItem=item.getRelateditem();
                        RelatedItem2=item.getRelateditem2();
                        RelatedItem3=item.getRelateditem3();
                        RelatedItem4=item.getRelateditem4();
                        RelatedItem5=item.getRelateditem5();
                        SupplementsItem=item.getrelatedSupplement();
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
                    HasSupplements=item.gethassupplements();
                    RelatedItem=item.getRelateditem();
                    RelatedItem2=item.getRelateditem2();
                    RelatedItem3=item.getRelateditem3();
                    RelatedItem4=item.getRelateditem4();
                    RelatedItem5=item.getRelateditem5();
                    SupplementsItem=item.getrelatedSupplement();
                    float quantity= item.getQuantity();
                    Barcode=item.getBarcode();
                    Weight=item.getWeight();
                    catname=item.getCategory();
                    Log.d("HasOptions1", String.valueOf(HasOptions));
                    if (Boolean.TRUE.equals(HasOptions) || "1".equals(HasOptions)) {


                        Log.d("HasOptions", String.valueOf(HasOptions));

                        showOptionPopup(Hascomment,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                    }else if (cursor != null && cursor.moveToFirst()) {
                        // Retrieve the existing transaction ID for the item
                        existingTransactionId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_ID));

                        sentToKitchen = String.valueOf(mDatabaseHelper.getLatestSentToKitchen(Barcode,existingTransactionId));


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

                                            // Insert a new transaction with IS_PAID as 0
                                            mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                        }
                                        refreshTicketFragment();
                                        refreshTotal();

                                }else {

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
                                    boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);
                                        if (!isdiscounted) {
                                            if(existingitems){
                                                mDatabaseHelper.updateTransactionWithZeroDiscount(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);


                                            }else {

                                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                                cashierId = sharedPreference.getString("cashorId", null);
                                                newpriceWithoutVat = pricewithoutVat / newQuantity;
                                                String ShopName = sharedPreference.getString("ShopName", null);

                                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                                    String MRAMETHOD = "Single";
                                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                                    String Catnum = String.valueOf(catId);

                                                    // Insert a new transaction with IS_PAID as 0
                                                    mDatabaseHelper.insertTransaction(null, itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                                }
                                            }
                                       }else{


                                            if ("OPEN FOOD".equals(catname)) {
                                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                                cashierId = sharedPreference.getString("cashorId", null);
                                                newpriceWithoutVat = pricewithoutVat / newQuantity;
                                                String ShopName = sharedPreference.getString("ShopName", null);

                                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                                    String MRAMETHOD = "Single";
                                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                                    String Catnum = String.valueOf(catId);

                                                    // Insert a new transaction with IS_PAID as 0
                                                    mDatabaseHelper.insertTransaction(null, itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                                }
                                            }else {
                                                if ("OPEN FOOD".equals(catname)) {
                                                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                                    cashierId = sharedPreference.getString("cashorId", null);
                                                    newpriceWithoutVat = pricewithoutVat / newQuantity;
                                                    String ShopName = sharedPreference.getString("ShopName", null);

                                                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                                        String MRAMETHOD = "Single";
                                                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                                                        String Catnum = String.valueOf(catId);

                                                        // Insert a new transaction with IS_PAID as 0
                                                        mDatabaseHelper.insertTransaction(null, itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                                    }
                                                }else {
                                                    Log.d("updateTransaction1", "= " + newQuantity);
                                                    mDatabaseHelper.updateTransaction(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                                                }

                                            }
                                       }
                                    refreshTicketFragment();
                                    refreshTotal();
                                }
                            }

                            }

                        // Check if the existing transaction ID matches the current transaction ID
                        else if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {

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
                            boolean isnotdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);
                            boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));
                            boolean existingdiscounteditems=mDatabaseHelper.checkDiscountedItemExists(latesttransId,tableid, String.valueOf(roomid), String.valueOf(itemId));
                            boolean existingnondiscounteddiscounteditems=mDatabaseHelper.checkNonDidcountedItemExists(latesttransId,tableid, String.valueOf(roomid), String.valueOf(itemId));

                            if (!isnotdiscounted && existingitems) {
                                boolean updateSuccess = mDatabaseHelper.updateTransactionWithZeroDiscount(itemId, newpriceWithoutVat, newQuantity, totaltaxbeforediscount, newtotaltaxafterdiscount, latesttransId, newTotalPrice, newTotalDiscount, newVat, VatType, String.valueOf(roomid), tableid);

                                if (!updateSuccess) {
                                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    cashierId = sharedPreference.getString("cashorId", null);
                                    String ShopName = sharedPreference.getString("ShopName", null);

                                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                        String MRAMETHOD = "Single";
                                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                                        String Catnum = String.valueOf(catId);

                                        // Insert a new transaction with IS_PAID as 0
                                        mDatabaseHelper.insertTransaction(null, itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                    }
                                }


                            } else if(!isnotdiscounted){

                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                cashierId = sharedPreference.getString("cashorId", null);
                                String ShopName = sharedPreference.getString("ShopName", null);

                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                    String MRAMETHOD = "Single";
                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                    String Catnum = String.valueOf(catId);

                                    // Insert a new transaction with IS_PAID as 0
                                    mDatabaseHelper.insertTransaction(null, itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                }
                            }else{
                                if ("OPEN FOOD".equals(catname)) {
                                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    cashierId = sharedPreference.getString("cashorId", null);
                                    newpriceWithoutVat = pricewithoutVat / newQuantity;
                                    String ShopName = sharedPreference.getString("ShopName", null);

                                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                        String MRAMETHOD = "Single";
                                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                                        String Catnum = String.valueOf(catId);

                                        // Insert a new transaction with IS_PAID as 0
                                        mDatabaseHelper.insertTransaction(null, itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                    }
                                }else {
                                     taxbeforediscount=calculateinitialTax(String.valueOf(unitprice));

                                     newQuantity = currentQuantity + 1;
                                     pricewithoutVat= Double.parseDouble(priceWithoutVat);
                                     newpriceWithoutVat= pricewithoutVat * newQuantity;
                                     totaltaxbeforediscount= taxbeforediscount * newQuantity;
                                     newTotalDiscount= newQuantity * TotalDiscount;
                                     taxafterdiscount= calculateTax();
                                     newtotaltaxafterdiscount= taxafterdiscount * newQuantity;
                                     newTotalPrice = UnitPrice * newQuantity;
                                     newVat = newQuantity * calculateTax();

                                    Log.d("updateTransaction2", "= " + newQuantity);

                                    mDatabaseHelper.updateTransaction(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                                }

                            }
                            refreshTicketFragment();
                            refreshTotal();
                        }else {

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

                                            // Insert a new transaction with IS_PAID as 0
                                            mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


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

                    if(Boolean.FALSE.equals(HasOptions) && (HasSupplements.trim().equals("1") || HasSupplements.trim().equals("true"))){

                        List<Variant> supplementList = dbManager.getSupplementListById(SupplementsItem);

                        if (supplementList != null && !supplementList.isEmpty()) {
                            for (Variant variant : supplementList) {


                                Button variantButton = new Button(getContext());
                                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                                variantButton.setText(buttonText); // Set the button text to include both description and price
                                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                                NewBarcode = variant.getBarcode();
                                String newDesc = variant.getDescription();
                                newitemid = variant.getVariantitemid();

                            }
                            showOptionPopupForSupplements(newitemid, RelatedItem, RelatedItem2, RelatedItem3, RelatedItem4, RelatedItem5, SupplementsItem, itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

                        }
                    }

                                // Item not selected, insert a new transaction with quantity 1 and total price
                                mDatabaseHelper.insertTransaction(newitemid,itemId, Barcode, Weight,taxbeforediscount,currentpriceWithoutVat, CompanyShopNumber,Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

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

    // Public method to be called by the MainActivity for searching items

    // Method to show popup dialog for number of covers


    private boolean isRoomTableInProgress(String roomid, String tableid) {
        Cursor cursor = mDatabaseHelper.getInProgressRecord(roomid, tableid);
        return cursor != null && cursor.moveToFirst();
    }


    private void showOptionPopupForSupplements(String relatedoptionid, String id, String id2, String id3, String id4, String id5, String relatensupplement, int itemId, String transactionId, String transactionDate, int quantity, double newTotalPrice, double vat, String longDescription, Double unitPrice, Double priceWithoutVat, String VatType, String PosNum, String Nature, String ItemCode, String currency, String TaxCode, double priceAfterDiscount, Double TotalDiscount, String roomid, String tableid, int ispaid) {

        // Create an AlertDialog.Builder and set the title
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(R.string.choixsup); // Set the title of the dialog
// Inflate the custom layout for the dialog title
        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        dialogBuilder.setCustomTitle(titleView);
        // Inflate the custom layout for the dialog

        View dialogView = inflater.inflate(R.layout.custom_supplements_dialog, null);
        dialogBuilder.setView(dialogView);

        // Declare and initialize the AlertDialog
        AlertDialog alertDialog = dialogBuilder.create();
// Set the click listener for the close button
        ImageButton closeDialogButton = titleView.findViewById(R.id.closeDialogButton);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Dismiss the dialog when the close button is clicked
            }
        });
        // Get the list of supplements
        List<Variant> supplementList = dbManager.getSupplementListById(relatensupplement);

        if (supplementList != null && !supplementList.isEmpty()) {
            GridLayout supplementButtonsLayout = dialogView.findViewById(R.id.SupplementsButtonsLayout);
            supplementButtonsLayout.setColumnCount(5); // Set maximum 5 columns per row

            for (Variant variant : supplementList) {
                Button variantButton = new Button(getContext());
                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                variantButton.setText(buttonText); // Set the button text to include both description and price
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                NewBarcode = variant.getBarcode();
                String newDesc = variant.getDescription();
                String newitemid = variant.getVariantitemid();
                double newprice = variant.getPrice();

                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        insertsupplementdata(relatedoptionid, SupplementsItem, id, itemId, transactionId, transactionDate, vat, longDescription, priceWithoutVat, NewBarcode, newDesc, newprice, newitemid);
                        mDatabaseHelper.updateRelatedOptionId(transactionId, relatedoptionid, String.valueOf(itemId));
                        v.setBackgroundColor(getResources().getColor(R.color.red)); // Change button color

                    }
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
              //  variantButtonsLayout.addView(variantButton);
                // Configure GridLayout parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Distribute width equally
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Weight for equal distribution
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
                params.setMargins(8, 8, 8, 8); // Add margin around buttons
                variantButton.setLayoutParams(params);

                // Add button to GridLayout
                supplementButtonsLayout.addView(variantButton);
            }
        } else {

            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.SupplementsButtonsLayout).setVisibility(View.GONE);
        }
        // Add negative button (Cancel button)
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the Cancel button click
                dialog.dismiss();
            }
        });
        // Show the AlertDialog
        alertDialog.show();
    }

    private void showOptionPopup(String Hascomment,String id,String id2,String id3,String id4,String id5,String relatensupplement,int itemId,String transactionId,String transactionDate,int quantity, double newTotalPrice, double vat, String longDescription,Double unitPrice, Double priceWithoutVat,String VatType,String PosNum,String Nature,String ItemCode, String currency,String TaxCode,double priceAfterDiscount,Double TotalDiscount,String roomid,String tableid,int ispaid ) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext()); // Define dialogBuilder
        dialogBuilder.setTitle(R.string.choix); // Set the title of the dialog
// Inflate the custom layout for the dialog title
        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_option_dialog_title, null);
        dialogBuilder.setCustomTitle(titleView);

        View dialogView = inflater.inflate(R.layout.custom_comment_dialog, null);
        dialogBuilder.setView(dialogView);
        // Create a custom layout for the dialog
// Set the click listener for the close button
        ImageButton closeDialogButton = titleView.findViewById(R.id.closeDialogButton);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Dismiss the dialog when the close button is clicked
            }
        });
        List<Variant> variantList = dbManager.getVariantsById(id);
        List<Variant> variantList2 = dbManager.getVariantsById(id2);
        List<Variant> variantList3 = dbManager.getVariantsById(id3);
        List<Variant> variantList4 = dbManager.getVariantsById(id4);
        List<Variant> variantList5 = dbManager.getVariantsById(id5);

        // Declare a reference to AlertDialog
         alertDialog = null;

        if (variantList != null && !variantList.isEmpty()) {
            for (Variant variant : variantList) {
                // Log each VARIANT_DESC

                GridLayout gridLayout = dialogView.findViewById(R.id.variantButtonsLayout);  // Ensure this is a GridLayout in your XML layout

                gridLayout.setColumnCount(4); // 4 columns per row

                Button variantButton = new Button(getContext());
                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                variantButton.setText(buttonText); // Set the button text to include both description and price
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String NewBarcode = variant.getBarcode();



                String newDesc = variant.getDescription();
                String newitemid = variant.getVariantitemid();
                double newprice = variant.getPrice();
                String optionName = mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

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
                Button myButton = dialogView.findViewById(R.id.myButton);
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ItemExistence", "id " + id + ", itemId: " + itemId + ", longDescription: " + longDescription + ", NewBarcode: " + NewBarcode);

                        insertdata(null, SupplementsItem, id, itemId, transactionId, transactionDate, vat, longDescription, priceWithoutVat, NewBarcode, newDesc, newprice, newitemid);
// Make the button visible when the variant button is clicked
                        if( (HasSupplements.trim().equals("1") || HasSupplements.trim().equals("true"))) {
                            // Add the "Next" button to the layout
                            myButton.setVisibility(View.VISIBLE);
                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Handle the Next button click
                                    showOptionPopupForSupplements(newitemid,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                    alertDialog.dismiss(); // Dismiss the dialog when a variant button is clicked
                                }
                            });



                        }
                    }
                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant
                        return true; // Consume the long click event
                    }
                });

                // Set LayoutParams for the button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Allow weight-based distribution
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal width
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Let the grid manage rows
                variantButton.setLayoutParams(params);

// Add button to GridLayout
                gridLayout.addView(variantButton);
            }
        } else {

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
                GridLayout gridLayout = dialogView.findViewById(R.id.variantButtonsLayout2);  // Ensure this is a GridLayout in your XML layout
                gridLayout.setColumnCount(4); // 4 columns per row


                Button variantButton = new Button(getContext());
                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                variantButton.setText(buttonText); // Set the button text to include both description and price
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String NewBarcode = variant.getBarcode();
                String newDesc = variant.getDescription();
                String newitemid = variant.getVariantitemid();
                double newprice = variant.getPrice();
                String optionName = mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

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
                Button myButton = dialogView.findViewById(R.id.myButton);
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        insertdata(null, SupplementsItem, id, itemId, transactionId, transactionDate, vat, longDescription, priceWithoutVat, NewBarcode, newDesc, newprice, newitemid);
                        // Make the button visible when the variant button is clicked

                        if( (HasSupplements.trim().equals("1") || HasSupplements.trim().equals("true"))) {
                            // Add the "Next" button to the layout
                            myButton.setVisibility(View.VISIBLE);
                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Handle the Next button click
                                    showOptionPopupForSupplements(newitemid,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                    alertDialog.dismiss(); // Dismiss the dialog when a variant button is clicked
                                }
                            });



                        }
                    }
                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant
                        return true; // Consume the long click event
                    }
                });
                // Set LayoutParams for the button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Allow weight-based distribution
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal width
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Let the grid manage rows
                variantButton.setLayoutParams(params);

// Add button to GridLayout
                gridLayout.addView(variantButton);

            }
        } else {

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

                GridLayout gridLayout = dialogView.findViewById(R.id.variantButtonsLayout3);  // Ensure this is a GridLayout in your XML layout
                gridLayout.setColumnCount(4); // 4 columns per row

                Button variantButton = new Button(getContext());
                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                variantButton.setText(buttonText); // Set the button text to include both description and price
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String NewBarcode = variant.getBarcode();
                String newDesc = variant.getDescription();
                String newitemid = variant.getVariantitemid();
                double newprice = variant.getPrice();
                String optionName = mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

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
                Button myButton = dialogView.findViewById(R.id.myButton);
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        insertdata(null, SupplementsItem, id, itemId, transactionId, transactionDate, vat, longDescription, priceWithoutVat, NewBarcode, newDesc, newprice, newitemid);
                        if( (HasSupplements.trim().equals("1") || HasSupplements.trim().equals("true"))) {
                            // Add the "Next" button to the layout
                            myButton.setVisibility(View.VISIBLE);
                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Handle the Next button click
                                    showOptionPopupForSupplements(newitemid,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                    alertDialog.dismiss(); // Dismiss the dialog when a variant button is clicked
                                }
                            });



                        }
                    }
                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant
                        return true; // Consume the long click event
                    }
                });

                // Set LayoutParams for the button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Allow weight-based distribution
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal width
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Let the grid manage rows
                variantButton.setLayoutParams(params);

// Add button to GridLayout
                gridLayout.addView(variantButton);
            }
        } else {

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

                GridLayout gridLayout = dialogView.findViewById(R.id.variantButtonsLayout4);  // Ensure this is a GridLayout in your XML layout
                gridLayout.setColumnCount(4); // 4 columns per row

                Button variantButton = new Button(getContext());
                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                variantButton.setText(buttonText); // Set the button text to include both description and price
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String NewBarcode = variant.getBarcode();
                String newDesc = variant.getDescription();
                String newitemid = variant.getVariantitemid();
                double newprice = variant.getPrice();
                String optionName = mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

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
                Button myButton = dialogView.findViewById(R.id.myButton);
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        insertdata(null, SupplementsItem, id, itemId, transactionId, transactionDate, vat, longDescription, priceWithoutVat, NewBarcode, newDesc, newprice, newitemid);
                        // Make the button visible when the variant button is clicked
                        if( (HasSupplements.trim().equals("1") || HasSupplements.trim().equals("true"))) {
                            // Add the "Next" button to the layout
                            myButton.setVisibility(View.VISIBLE);
                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Handle the Next button click
                                    showOptionPopupForSupplements(newitemid,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                    alertDialog.dismiss(); // Dismiss the dialog when a variant button is clicked
                                }
                            });



                        }
                    }
                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant
                        return true; // Consume the long click event
                    }
                });

                // Set LayoutParams for the button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Allow weight-based distribution
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal width
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Let the grid manage rows
                variantButton.setLayoutParams(params);

// Add button to GridLayout
                gridLayout.addView(variantButton);
            }
        } else {

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

                GridLayout gridLayout = dialogView.findViewById(R.id.variantButtonsLayout5);  // Ensure this is a GridLayout in your XML layout
                gridLayout.setColumnCount(4); // 4 columns per row

                Button variantButton = new Button(getContext());
                String buttonText = variant.getDescription() + " - Rs " + variant.getPrice(); // Display name and price

                variantButton.setText(buttonText); // Set the button text to include both description and price
                variantButton.setTag(variant.getBarcode()); // Set a tag to identify the variant (you can use barcode or variantId)
                String NewBarcode = variant.getBarcode();
                String newDesc = variant.getDescription();
                String newitemid = variant.getVariantitemid();
                double newprice = variant.getPrice();
                String optionName = mDatabaseHelper.getOptionNameById(Integer.parseInt(id));
                String optionName2 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id2));
                String optionName3 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id3));
                String optionName4 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id4));
                String optionName5 = mDatabaseHelper.getOptionNameById(Integer.parseInt(id5));

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
                Button myButton = dialogView.findViewById(R.id.myButton);
                // Click listener for normal click
                variantButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        insertdata(null, SupplementsItem, id, itemId, transactionId, transactionDate, vat, longDescription, priceWithoutVat, NewBarcode, newDesc, newprice, newitemid);
                        if( (HasSupplements.trim().equals("1") || HasSupplements.trim().equals("true"))) {
                            // Add the "Next" button to the layout
                            myButton.setVisibility(View.VISIBLE);
                            myButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Handle the Next button click
                                    showOptionPopupForSupplements(newitemid,RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, vat, longDescription, UnitPrice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                    alertDialog.dismiss(); // Dismiss the dialog when a variant button is clicked
                                }
                            });



                        }
                    }
                });

                // Long click listener for deletion
                variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Handle long click, e.g., show confirmation dialog and delete the variant
                        return true; // Consume the long click event
                    }
                });

                // Set LayoutParams for the button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Allow weight-based distribution
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal width
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Let the grid manage rows
                variantButton.setLayoutParams(params);

// Add button to GridLayout
                gridLayout.addView(variantButton);
            }
        } else {

            // Hide the layout containing buttons and option names if no data is available
            dialogView.findViewById(R.id.variantButtonsLayout5).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView2).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView3).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView4).setVisibility(View.GONE);
            dialogView.findViewById(R.id.OptionNametextView5).setVisibility(View.GONE);
        }

        // Create and show the AlertDialog
        alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    public void insertdata(String relatedoptionid,String SupplementsItem,String id,int itemId,String transactionId, String transactionDate,double vat, String longDescription,double priceWithoutVat,String NewBarcode, String newdesc,double newprice,String newitemid) {
        Item item = dbManager.getItemById(id);

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


            // Check if the existing transaction ID matches the current transaction ID
            if (sentToKitchen.equals("1")) {


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

                            // Insert a new transaction with IS_PAID as 0
                            mDatabaseHelper.insertTransaction(null,Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

                        }
                        refreshTicketFragment();
                        refreshTotal();

                    }else {

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


                        }
                        double newVat =  vatAmount;
                        boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);


                        if(!isdiscounted && existingitems ){
                            mDatabaseHelper.updateTransactionWithZeroDiscount(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);


                        } else if(!isdiscounted){

                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                            cashierId = sharedPreference.getString("cashorId", null);
                            newpriceWithoutVat= pricewithoutVat / newQuantity;
                            String ShopName = sharedPreference.getString("ShopName", null);

                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                String MRAMETHOD = "Single";
                                String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                int catId = mDatabaseHelper.getCatIdFromName(catname);
                                String Catnum = String.valueOf(catId);

                                // Insert a new transaction with IS_PAID as 0
                                mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                            }
                        }else{

                            if ("OPEN FOOD".equals(catname)) {
                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                cashierId = sharedPreference.getString("cashorId", null);
                                newpriceWithoutVat = pricewithoutVat / newQuantity;
                                String ShopName = sharedPreference.getString("ShopName", null);

                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                    String MRAMETHOD = "Single";
                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                    String Catnum = String.valueOf(catId);

                                    // Insert a new transaction with IS_PAID as 0
                                    mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                }
                            }else {
                                Log.d("updateTransaction3", "= " + newQuantity);
                                mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid),newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                            }

                        }
                        refreshTicketFragment();
                        refreshTotal();
                    }
                }

            }

            // Check if the existing transaction ID matches the current transaction ID
            else if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {


                // Item already selected, update the quantity and total price
                int currentQuantity = mDatabaseHelper.getQuantity(latesttransId,NewBarcode);

                UnitPrice = newprice;
                float unitprice=0;
                if(NewBarcode != null){
                     unitprice= mDatabaseHelper.getUnitPriceFromBarcode(NewBarcode);
                }else{
                     unitprice = item.getPrice();
                }


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


                }
                double newVat =  vatAmount;


                double newtotaltaxafterdiscount= newVat ;
                boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);
                boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));

                if(!isdiscounted && existingitems ){
                    mDatabaseHelper.updateTransactionWithZeroDiscount(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                } else if(!isdiscounted){
                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    cashierId = sharedPreference.getString("cashorId", null);
                    newpriceWithoutVat= pricewithoutVat / newQuantity;
                    String ShopName = sharedPreference.getString("ShopName", null);

                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                        String MRAMETHOD = "Single";
                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                        String Catnum = String.valueOf(catId);

                        // Insert a new transaction with IS_PAID as 0
                        mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                    }
                }else{

                    if ("OPEN FOOD".equals(catname)) {
                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                        cashierId = sharedPreference.getString("cashorId", null);
                        newpriceWithoutVat = pricewithoutVat / newQuantity;
                        String ShopName = sharedPreference.getString("ShopName", null);

                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                            int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                            String MRAMETHOD = "Single";
                            String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                            int catId = mDatabaseHelper.getCatIdFromName(catname);
                            String Catnum = String.valueOf(catId);

                            // Insert a new transaction with IS_PAID as 0
                            mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                        }
                    }else {
                        Log.d("updateTransaction4", "= " + newQuantity);
                        mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid),newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                    }

                }

                refreshTicketFragment();
                refreshTotal();
            } else {

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

                                double vatAmount=0;
                                if (VatType.equals("VAT 15%")) {
                                    double vatRate = 0.15; // 15% VAT rate

                                    // Calculate the price without VAT
                                    double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                                    // Calculate the VAT amount
                                    vatAmount = newTotalPrice - priceExcludingVAT;

                                }

                                // Insert a new transaction with IS_PAID as 0
                                mDatabaseHelper.insertTransaction(null,Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

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


                // Item not selected, insert a new transaction with quantity 1 and total price
                mDatabaseHelper.insertTransaction(null,Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


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


            SendToHeader(unitprice, calculateTax());
        }
        // Notify the listener that an item is added
        if (itemAddedListener != null) {
            itemAddedListener.onItemAdded(String.valueOf(roomid), tableid);
        }

    }
    public void insertsupplementdata(String relatedoptionid,String SupplementsItem,String id,int itemId,String transactionId, String transactionDate,double vat, String longDescription,double priceWithoutVat,String NewBarcode, String newdesc,double newprice,String newitemid) {
        Item item = dbManager.getItemById(id);

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


            // Check if the existing transaction ID matches the current transaction ID
            if (sentToKitchen.equals("1")) {


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

                            // Insert a new transaction with IS_PAID as 0
                            mDatabaseHelper.insertTransaction(relatedoptionid,Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                        }
                        refreshTicketFragment();
                        refreshTotal();

                    }else {

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


                        }
                        double newVat =  vatAmount;
                        boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);


                        if(!isdiscounted && existingitems ){
                            mDatabaseHelper.updateTransactionWithZeroDiscount(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);


                        } else if (!isdiscounted) {


                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                            cashierId = sharedPreference.getString("cashorId", null);
                            newpriceWithoutVat= pricewithoutVat / newQuantity;
                            String ShopName = sharedPreference.getString("ShopName", null);

                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                String MRAMETHOD = "Single";
                                String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                int catId = mDatabaseHelper.getCatIdFromName(catname);
                                String Catnum = String.valueOf(catId);
                                // Insert a new transaction with IS_PAID as 0
                                mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                            }
                        }else{

                            if ("OPEN FOOD".equals(catname)) {
                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                cashierId = sharedPreference.getString("cashorId", null);
                                newpriceWithoutVat = pricewithoutVat / newQuantity;
                                String ShopName = sharedPreference.getString("ShopName", null);

                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                    String MRAMETHOD = "Single";
                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                    String Catnum = String.valueOf(catId);

                                    // Insert a new transaction with IS_PAID as 0
                                    mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                }
                            }else {
                                Log.d("updateTransaction5", "= " + newQuantity);
                                mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid),newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                            }

                        }
                        refreshTicketFragment();
                        refreshTotal();
                    }
                }

            }

            // Check if the existing transaction ID matches the current transaction ID
            else if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {

                // Item already selected, update the quantity and total price
                int currentQuantity = mDatabaseHelper.getQuantity(latesttransId,NewBarcode);

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

                }
                double newVat =  vatAmount;


                double newtotaltaxafterdiscount= newVat ;
                boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);

            boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));

            if(!isdiscounted && existingitems ){
                mDatabaseHelper.updateTransactionWithZeroDiscount(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

            } else if (!isdiscounted){
                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    cashierId = sharedPreference.getString("cashorId", null);

                    String ShopName = sharedPreference.getString("ShopName", null);

                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                        String MRAMETHOD = "Single";
                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                        String Catnum = String.valueOf(catId);

                        // Insert a new transaction with IS_PAID as 0
                        mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                    }
                }else{

                if ("OPEN FOOD".equals(catname)) {
                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    cashierId = sharedPreference.getString("cashorId", null);
                    newpriceWithoutVat = pricewithoutVat / newQuantity;
                    String ShopName = sharedPreference.getString("ShopName", null);

                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                        String MRAMETHOD = "Single";
                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                        String Catnum = String.valueOf(catId);

                        // Insert a new transaction with IS_PAID as 0
                        mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                    }
                }else {
                    Log.d("updateTransaction6", "= " + newQuantity);
                    mDatabaseHelper.updateTransaction(Integer.parseInt(newitemid),newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid), tableid);

                }

                }

                refreshTicketFragment();
                refreshTotal();
            } else {


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

                                double vatAmount=0;
                                if (VatType.equals("VAT 15%")) {
                                    double vatRate = 0.15; // 15% VAT rate

                                    // Calculate the price without VAT
                                    double priceExcludingVAT = newTotalPrice / (1 + vatRate);

                                    // Calculate the VAT amount
                                    vatAmount = newTotalPrice - priceExcludingVAT;

                                }

                                // Insert a new transaction with IS_PAID as 0
                                mDatabaseHelper.insertTransaction(relatedoptionid,Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

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



                // Item not selected, insert a new transaction with quantity 1 and total price
                mDatabaseHelper.insertTransaction(relatedoptionid,Integer.parseInt(newitemid), NewBarcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, vatAmount, longDescription, unitprice, priceWithoutVat, VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

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

                        // Check if the existing transaction ID matches the current transaction ID
                        if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {

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
                            boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);

                            boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));

                            if(!isdiscounted && existingitems ){
                                Log.d("updateTransaction7", "= " + newQuantity);
                                mDatabaseHelper.updateTransaction(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                            } else if (!isdiscounted){
                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                cashierId = sharedPreference.getString("cashorId", null);

                                String ShopName = sharedPreference.getString("ShopName", null);

                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                    String MRAMETHOD = "Single";
                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                    int catId = mDatabaseHelper.getCatIdFromName(catname);
                                    String Catnum = String.valueOf(catId);

                                    // Insert a new transaction with IS_PAID as 0
                                    mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                }
                            }else{
                                Log.d("updateTransaction8", "= " + newQuantity);
                                mDatabaseHelper.updateTransaction(itemId, newpriceWithoutVat,newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                            }
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

                                            mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
                                           // showOptionPopupForSupplements(String.valueOf(itemId),RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);

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


                                // Item not selected, insert a new transaction with quantity 1 and total price
                                mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight,taxbeforediscount,currentpriceWithoutVat, CompanyShopNumber,Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                               // showOptionPopupForSupplements(String.valueOf(itemId),RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, UnitPrice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
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
    public void refreshTicketFragment() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);

        if (ticketFragment != null) {
            ticketFragment.refreshData(calculateTotalAmount(),calculateTotalTax(),"movetobottom");
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

                            // Check if the existing transaction ID matches the current transaction ID
                            if (existingTransactionId != null && existingTransactionId.equals(latesttransId) && sentToKitchen.equals("0")) {

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
                                boolean isdiscounted=mDatabaseHelper.isItemDiscountZero(latesttransId,itemId);

                                boolean existingitems=mDatabaseHelper.checkItemExists(latesttransId,tableid, String.valueOf(roomid));

                                if(!isdiscounted && existingitems ){
                                    mDatabaseHelper.updateTransactionWithZeroDiscount(itemId,newpriceWithoutVat, newQuantity,totaltaxbeforediscount,newVat,latesttransId, newTotalPrice,newTotalDiscount, newVat, VatType, String.valueOf(roomid),tableid);

                                } else if (!isdiscounted){
                                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    cashierId = sharedPreference.getString("cashorId", null);

                                    String ShopName = sharedPreference.getString("ShopName", null);

                                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                        String MRAMETHOD = "Single";
                                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                        int catId = mDatabaseHelper.getCatIdFromName(catname);
                                        String Catnum = String.valueOf(catId);
                                        // Insert a new transaction with IS_PAID as 0
                                        mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                    }
                                }else{
                                    if ("OPEN FOOD".equals(catname)) {
                                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                        cashierId = sharedPreference.getString("cashorId", null);
                                        newpriceWithoutVat = pricewithoutVat / newQuantity;
                                        String ShopName = sharedPreference.getString("ShopName", null);

                                        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                        if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                            int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                            String MRAMETHOD = "Single";
                                            String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                            int catId = mDatabaseHelper.getCatIdFromName(catname);
                                            String Catnum = String.valueOf(catId);
                                            // Insert a new transaction with IS_PAID as 0
                                            mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                        }
                                    }else {
                                        if ("OPEN FOOD".equals(catname)) {
                                            SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                            cashierId = sharedPreference.getString("cashorId", null);
                                            newpriceWithoutVat = pricewithoutVat / newQuantity;
                                            String ShopName = sharedPreference.getString("ShopName", null);

                                            Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                            if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                                int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                                String MRAMETHOD = "Single";
                                                String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                                int catId = mDatabaseHelper.getCatIdFromName(catname);
                                                String Catnum = String.valueOf(catId);
                                                // Insert a new transaction with IS_PAID as 0
                                                mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, pricewithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, UnitPrice, calculateTax(), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                            }
                                        }else {
                                            Log.d("updateTransaction9", "= " + newQuantity);
                                            mDatabaseHelper.updateTransaction(itemId, newpriceWithoutVat,newQuantity,totaltaxbeforediscount,newtotaltaxafterdiscount,latesttransId, newTotalPrice,newTotalDiscount,newVat,VatType, String.valueOf(roomid),tableid);
                                        }

                                    }

                                }
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

                                                // Item has a different transaction ID, insert a new transaction
                                                mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight, taxbeforediscount, currentpriceWithoutVat, CompanyShopNumber, Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                                showOptionPopupForSupplements(String.valueOf(itemId),RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
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

                                    // Item not selected, insert a new transaction with quantity 1 and total price
                                    mDatabaseHelper.insertTransaction(null,itemId, Barcode, Weight,taxbeforediscount,currentpriceWithoutVat, CompanyShopNumber,Catnum, transactionId, transactionDate, 1, newTotalPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);


                                    showOptionPopupForSupplements(String.valueOf(itemId),RelatedItem,RelatedItem2,RelatedItem3,RelatedItem4,RelatedItem5,SupplementsItem,itemId, transactionId, transactionDate, 1, UnitPrice, Double.parseDouble(vat), longDescription, unitprice, Double.parseDouble(priceWithoutVat), VatType, PosNum, Nature, ItemCode, Currency, TaxCode, priceAfterDiscount, TotalDiscount, String.valueOf(roomid), tableid, 0);
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
            int covernumber=mDatabaseHelper.getCoverCount(tableid,roomid);
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
            Log.d("updateNumberOfCovers", covernumber +" " + latesttransId);
            Log.d("updateNumberOfCoversx", covernumber +" " + transactionIdInProgress);
            mDatabaseHelper.updateNumberOfCovers(transactionIdInProgress,covernumber);

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