package com.accessa.ibora.sales.ticket.Checkout;

import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_INVOICE_REF;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.Buyer.Buyer;
import com.accessa.ibora.MRA.MRADBN;
import com.accessa.ibora.MRA.MRAdaptor;
import com.accessa.ibora.MRA.Mra;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Report.ReportActivity;
import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.SplashFlashActivity;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.ticket.ModifyItemDialogFragment;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.accessa.ibora.scanner.InbuiltScannerFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class SplitTicketFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private Buyer selectedBuyer;
    private String cashierId,cashierLevel,shopname, cashiername;
    private DatabaseHelper mDatabaseHelper;
private double totalAmount,TaxtotalAmount;
    private   FrameLayout emptyFrameLayout;


    private  String ItemId,PosNum;
    private String VatVall;
    private static final String POSNumber="posNumber";
private String transactionIdInProgress;
    private int transactionCounter = 1;
private TextView textViewVATs,textViewTotals;
    private SoundPool soundPool;
    private int soundId;
    private IWoyouService woyouService;

    private DBManager dbManager;
    private String actualdate,tableid;
    private int roomid;
    private boolean isBuyerSelected = false;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
     private  List<String> data ;
    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);

                   // showSecondaryScreen(data);

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);

        // Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");


        mDatabaseHelper = new DatabaseHelper(getContext());
    transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);

        if ("sunmiT2".equalsIgnoreCase(deviceType)) {
           // showSecondaryScreen(data);
        }  else {
            Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.navigation_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.split_ticket_fragment, container, false);




        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);
        SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
       String  buyername = sharedPreferences2.getString("BuyerName", null);
        String  buyerpricelevel = sharedPreferences2.getString("PriceLevel", null);




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


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);

        Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
        actualdate = mDatabaseHelper.getCurrentDate();

        mAdapter = new TicketAdapter(getActivity(), cursor1);
        mRecyclerView.setAdapter(mAdapter);

        dbManager = new DBManager(getContext());
        dbManager.open();

        textViewVATs=view.findViewById(R.id.textViewVATs);
        textViewTotals=view.findViewById(R.id.textViewTotals);
        textViewVATs.setText(getString(R.string.tax) + " : Rs 0.00");
        textViewTotals.setText(getString(R.string.Total) + " : Rs 0.00");




        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierLevel = sharedPreference.getString("cashorlevel", null);
        shopname = sharedPreference.getString("ShopName", null);
        cashiername = sharedPreference.getString("cashorName", null);

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);



        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper




        // Retrieve the total amount and total tax amount from the transactionheader table
        Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
            int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

            totalAmount = cursor.getDouble(columnIndexTotalAmount);
            TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
            // Update the tax and total amount TextViews
            TextView taxTextView = view.findViewById(R.id.textViewVAT);
            String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
            taxTextView.setText(getString(R.string.tax) + ": Rs " + formattedTaxAmount);

            TextView totalAmountTextView = view.findViewById(R.id.textViewTotal);
            String formattedTotalAmount = String.format("%.2f", totalAmount);
            totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
        }

        // Load the data based on the transaction ID
        if (transactionIdInProgress != null) {
            Cursor cursor2 = mDatabaseHelper.getTransactionsByStatusAndId(DatabaseHelper.TRANSACTION_STATUS_IN_PROGRESS, transactionIdInProgress);
            mAdapter.swapCursor(cursor2);
        }

        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(getContext())
                .asGif()
                .load(R.drawable.transact)
                .into(imageView);

         emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }








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
                        dialogFragment.setTargetFragment(SplitTicketFragment.this, 0);
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
    private void showSaveOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Save Transaction as");
        String[] options = {"Proforma", "Debit Note", "Credit Note"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's selection here
                if (which == 0) {
                    String Type="PRF";
                    double totalAmount = calculateTotalAmount();
                    double TaxtotalAmount = calculateTotalTax();
                    SaveTransaction(Type,totalAmount,TaxtotalAmount);
                    clearBuyerInfoFromPrefs();

                } else if (which == 1) {
                    String Type="DRN";
                    double totalAmount = calculateTotalAmount();
                    double TaxtotalAmount = calculateTotalTax();
                    SaveTransaction(Type,totalAmount,TaxtotalAmount);
                    clearBuyerInfoFromPrefs();

                }
                else if (which == 2) {
                    String Type="CRN";
                    double totalAmount = calculateTotalAmount();
                    double TaxtotalAmount = calculateTotalTax();
                    SaveTransaction(Type,totalAmount,TaxtotalAmount);
                    clearBuyerInfoFromPrefs();
                }
            }
        });
        builder.show();
    }


    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void startNewActivity(String type, String newTransactionId,String invoiceRefIdentifier,String roomid, String tableid) {
        // Create an Intent to start the desired activity based on the selected type
        Intent intent = new Intent(getContext(), MRADBN.class);

        // Pass any data you need to the new activity using intent extras
        intent.putExtra("TransactionType", type);
        intent.putExtra("newtransactionid", newTransactionId);
        intent.putExtra("invoiceRefIdentifier", invoiceRefIdentifier);
        intent.putExtra("roomid", roomid);
        intent.putExtra("tableid", tableid);
        if (selectedBuyer != null) {
            // Access selectedBuyer properties here


            // Pass the selected buyer's information as an extra
            intent.putExtra("selectedBuyerName", selectedBuyer.getNames());
            intent.putExtra("selectedBuyerTAN", selectedBuyer.getTan());
            intent.putExtra("selectedBuyerCompanyName", selectedBuyer.getCompanyName());
            intent.putExtra("selectedBuyerType", selectedBuyer.getBuyerType());
            intent.putExtra("selectedBuyerBRN", selectedBuyer.getBrn());
            intent.putExtra("selectedBuyerNIC", selectedBuyer.getNic());
            intent.putExtra("selectedBuyerAddresse", selectedBuyer.getBusinessAddr());
            intent.putExtra("selectedBuyerprofile", selectedBuyer.getProfile());
            mDatabaseHelper.updateTransactionBuyerInfo(
                    newTransactionId,
                    invoiceRefIdentifier,
                    selectedBuyer.getNames(),
                    selectedBuyer.getTan(),
                    selectedBuyer.getCompanyName(),
                    selectedBuyer.getBusinessAddr(),
                    selectedBuyer.getBrn(),
                    cashierId
            );

            startActivity(intent);
        } else {


            // Pass the selected buyer's information as an extra
            intent.putExtra("selectedBuyerName", "");
            intent.putExtra("selectedBuyerTAN", "");
            intent.putExtra("selectedBuyerCompanyName", "");
            intent.putExtra("selectedBuyerType", "");
            intent.putExtra("selectedBuyerBRN", "");
            intent.putExtra("selectedBuyerNIC", "");
            intent.putExtra("selectedBuyerAddresse", "");
            intent.putExtra("selectedBuyerprofile", "");

            startActivity(intent);
        }
    }
    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.scanner_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
        fragmentTransaction.commit();
    }
    private void SaveTransaction(String Type,double totalAmount, double TaxtotalAmount) {


        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount);

if(Type.equals("DRN")) {
    // Retrieve the data for receipts with QR codes
    Cursor receiptCursor = mDatabaseHelper.getAllReceipt();




    // Create a custom dialog to display the receipt data
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
    View dialogView = getLayoutInflater().inflate(R.layout.popup_receipt_list, null);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setTitle("Select a Receipt");

    // Initialize your RecyclerView
    RecyclerView popupRecyclerView = dialogView.findViewById(R.id.popupRecyclerView);

// Set a layout manager (for example, LinearLayoutManager)
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    popupRecyclerView.setLayoutManager(layoutManager);

// Initialize and set up your adapter
    MRAdaptor popupAdapter = new MRAdaptor(receiptCursor);
    popupRecyclerView.setAdapter(popupAdapter);


// Set a click listener for the items in the dialog
    popupRecyclerView.addOnItemTouchListener(
            new RecyclerDepartmentClickListener(getContext(), popupRecyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView idTextView = view.findViewById(R.id.id_text_view);
                    TextView deptNameEditText = view.findViewById(R.id.name_text_view);

                    String id1 = idTextView.getText().toString();
                    String id = idTextView.getText().toString();
                    String name = deptNameEditText.getText().toString();

                    // Get the latest transaction counter value from SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
                    int latestTransactionDBNCounter = sharedPreferences.getInt("transaction_counter_DBN", 0);
                    SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    // Initialize SharedPreferences
                    SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                    roomid = preferences.getInt("roomnum", 0);
                    tableid = preferences.getString("table_id", "");
                    transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);
                    System.out.println("transactionIdInProgress: " + transactionIdInProgress);
                    // Increment the transaction counter
                    latestTransactionDBNCounter++;
                    SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                    PosNum = shardPreference.getString(POSNumber, null);
                    // Generate the transaction ID with the format "MEMO-integer"
                    String newTransactionId = Type + "-" +PosNum + "-" + latestTransactionDBNCounter;
                    // Update the transaction ID in the transaction table for transactions with status "InProgress"
                    mDatabaseHelper.updateTransactionTransactionIdInProgress(transactionIdInProgress,newTransactionId, String.valueOf(roomid),tableid);
                    // Update the transaction ID in the header table for transactions with status "InProgress"
                    mDatabaseHelper.updateHeaderTransactionIdInProgress(newTransactionId, String.valueOf(roomid),tableid);

                    // Update the transaction counter in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("transaction_counter_DBN", latestTransactionDBNCounter);
                    editor.apply();
                    String MRAMETHOD="Single";
                    // Update the transaction status for all in-progress transactions to "saved"
                    mDatabaseHelper.updateAllTransactionsStatus(Type,MRAMETHOD,name);
                    updateTransactionStatus();



                    // Start the activity with the selected receipt data
                    startNewActivity(Type, newTransactionId, name, String.valueOf(roomid),tableid);
                    getActivity().finish();


                }

                @Override
                public void onLongItemClick(View view, int position) {
                    // Do whatever you want on long item click
                }
            })
    );



    // Create and show the dialog
    AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();
} else if (Type.equals("PRF")) {

    // Get the latest transaction counter value from SharedPreferences
    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
    int latestTransactionProformaCounter = sharedPreferences.getInt("transaction_counter_Proforma", 0);


    // Initialize SharedPreferences
    SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
    roomid = preferences.getInt("roomnum", 0);
    tableid = preferences.getString("table_id", "");
    System.out.println("roomid: " + roomid);
    System.out.println("tableid: " + tableid);
    transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);
    System.out.println("transactionIdInProgressprf: " + transactionIdInProgress);

    SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
    PosNum = shardPreference.getString(POSNumber, null);

    // Increment the transaction counter
    latestTransactionProformaCounter++;

    // Generate the transaction ID with the format "MEMO-integer"
    String newTransactionId = Type + "-" +PosNum + "-" + latestTransactionProformaCounter;
    // Update the transaction ID in the transaction table for transactions with status "InProgress"
    mDatabaseHelper.updateTransactionTransactionIdInProgress(transactionIdInProgress,newTransactionId, String.valueOf(roomid),tableid);
    // Update the transaction ID in the header table for transactions with status "InProgress"
    mDatabaseHelper.updateHeaderTransactionIdInProgress(newTransactionId, String.valueOf(roomid),tableid);

    // Update the transaction counter in SharedPreferences
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt("transaction_counter_Proforma", latestTransactionProformaCounter);
    editor.apply();
                    String MRAMETHOD="Single";
    // Update the transaction status for all in-progress transactions to "saved"
    mDatabaseHelper.updateAllTransactionsStatus(Type,MRAMETHOD,null);
  //  updateTransactionStatus();

                    // Start the activity with the selected receipt data

    startNewActivity(Type, newTransactionId, null, String.valueOf(roomid),tableid);
                    getActivity().finish();




}else if (Type.equals("CRN")) {
    // Retrieve the data for receipts with QR codes
    Cursor newCursor1 = mDatabaseHelper.getAllReceiptWithQR();

    // Create a custom dialog to display the receipt data
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
    View dialogView = getLayoutInflater().inflate(R.layout.popup_receipt_list, null);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setTitle("Select a Receipt");
    // Create and show the dialog
    AlertDialog alertDialog = dialogBuilder.create();
// Find the "Cancel" button in your dialog's layout
    Button cancelButton = dialogView.findViewById(R.id.saveButton);

// Set an OnClickListener for the "Cancel" button
    cancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the latest transaction counter value from SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
            int latestTransactionCDNCounter = sharedPreferences.getInt("transaction_counter_CDN", 0);
            // Initialize SharedPreferences
            SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
            roomid = preferences.getInt("roomnum", 0);
            tableid = preferences.getString("table_id", "");
            transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);


            SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
            PosNum = shardPreference.getString(POSNumber, null);


            // Increment the transaction counter
            latestTransactionCDNCounter++;

            // Generate the transaction ID with the format "MEMO-integer"
            String newTransactionId = Type + "-" +PosNum + "-" +latestTransactionCDNCounter;
            // Update the transaction ID in the transaction table for transactions with status "InProgress"
            mDatabaseHelper.updateTransactionTransactionIdInProgress(transactionIdInProgress,newTransactionId, String.valueOf(roomid),tableid);
            // Update the transaction ID in the header table for transactions with status "InProgress"
            mDatabaseHelper.updateHeaderTransactionIdInProgress(newTransactionId, String.valueOf(roomid),tableid);

            // Update the transaction counter in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("transaction_counter_CDN", latestTransactionCDNCounter);
            editor.apply();
            String MRAMETHOD="Single";
            // Update the transaction status for all in-progress transactions to "saved"
            mDatabaseHelper.updateAllTransactionsStatus(Type,MRAMETHOD,null);
            updateTransactionStatus();

            // Start the activity with the selected receipt data

            startNewActivity(Type, newTransactionId, "null", String.valueOf(roomid),tableid);

            getActivity().finish();


            // Dismiss the dialog when the "Cancel" button is clicked
            alertDialog.dismiss();
        }
    });
    // Initialize your RecyclerView
    RecyclerView popupRecyclerView = dialogView.findViewById(R.id.popupRecyclerView);

// Set a layout manager (for example, LinearLayoutManager)
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    popupRecyclerView.setLayoutManager(layoutManager);

// Initialize and set up your adapter
    MRAdaptor popupAdapter = new MRAdaptor(newCursor1);
    popupRecyclerView.setAdapter(popupAdapter);

// Set a click listener for the items in the dialog
    popupRecyclerView.addOnItemTouchListener(
            new RecyclerDepartmentClickListener(getContext(), popupRecyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView idTextView = view.findViewById(R.id.id_text_view);
                    TextView deptNameEditText = view.findViewById(R.id.name_text_view);

                    String id1 = idTextView.getText().toString();
                    String id = idTextView.getText().toString();
                    String name = deptNameEditText.getText().toString();
    // Get the latest transaction counter value from SharedPreferences
    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
    int latestTransactionCDNCounter = sharedPreferences.getInt("transaction_counter_CDN", 0);

    SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    transactionIdInProgress = sharedPreference.getString(TRANSACTION_ID_KEY, null);

    SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
    PosNum = shardPreference.getString(POSNumber, null);


    // Increment the transaction counter
    latestTransactionCDNCounter++;

    // Generate the transaction ID with the format "MEMO-integer"
    String newTransactionId = Type + "-" +PosNum + "-" +latestTransactionCDNCounter;
    // Update the transaction ID in the transaction table for transactions with status "InProgress"
    mDatabaseHelper.updateTransactionTransactionIdInProgress(transactionIdInProgress,newTransactionId, String.valueOf(roomid),tableid);
    // Update the transaction ID in the header table for transactions with status "InProgress"
    mDatabaseHelper.updateHeaderTransactionIdInProgress(newTransactionId, String.valueOf(roomid),tableid);

    // Update the transaction counter in SharedPreferences
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt("transaction_counter_CDN", latestTransactionCDNCounter);
    editor.apply();
                    String MRAMETHOD="Single";
    // Update the transaction status for all in-progress transactions to "saved"
    mDatabaseHelper.updateAllTransactionsStatus(Type,MRAMETHOD,name);
    updateTransactionStatus();

                    // Start the activity with the selected receipt data
                    startNewActivity(Type, newTransactionId, name, String.valueOf(roomid),tableid);

                    getActivity().finish();


                }

                @Override
                public void onLongItemClick(View view, int position) {
                    // Do whatever you want on long item click
                }
            })
    );



    // Create and show the dialog
    AlertDialog alertDialogs = dialogBuilder.create();
    alertDialogs.show();

}

    }

    public void updateTransactionStatus() {

        // Retrieve the SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get the SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all the stored data in SharedPreferences
        editor.clear();

        // Apply the changes
        editor.apply();

    }


    public void clearTransact(){
        // Create an instance of the DatabaseHelper class
// Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");
        mDatabaseHelper.deleteDataByInProgressStatus(String.valueOf(roomid),tableid);


        // Optionally, you can notify the user or perform any other actions after clearing the transaction
// Notify the listener that an item is added

        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);


        if ("sunmiT2".equalsIgnoreCase(deviceType)) {
            //showSecondaryScreen(data);
        }  else {
            Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
        }
     //   recreate(getActivity());
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), getText(R.string.transactioncleared), Toast.LENGTH_SHORT).show();


    }


    public void showSecondaryScreen(List<String> data) {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();

        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            TransactionDisplay secondaryDisplay = new TransactionDisplay(getActivity(), presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();

            // Update the RecyclerView data on the secondary screen
            secondaryDisplay.updateRecyclerViewData(data);
        } else {
            // Secondary screen not found or not supportedF
            displayOnLCD();
        }
    }

    private Display getPresentationDisplay() {
        if (isAdded()) {  // Check if the Fragment is attached to an Activity
            DisplayManager displayManager = (DisplayManager) requireContext().getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = displayManager.getDisplays();
            for (Display display : displays) {
                if ((display.getFlags() & Display.FLAG_SECURE) != 0
                        && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                        && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                    return display;
                }
            }
        }
        return null;
    }

    public  double calculateTotalAmount1() {
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
    public  double calculateTotalTax1() {
        Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);

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
    public void displayOnLCD() {
        if (woyouService == null) {

            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

               double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                double taxTotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                String formattedTaxAmount = String.format("%.2f", taxTotalAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);

                woyouService.sendLCDDoubleString("Total: Rs" + formattedTotalAmount, "Tax: " + formattedTaxAmount, null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public double calculateTotalAmount() {
        // Implement your logic to calculate the total amount
        double totalAmount = 0.0;

        // Iterate through the cursor or any other data source to calculate the total amount

        return totalAmount;
    }
    public double calculateTotalTax() {
        // Implement your logic to calculate the total amount
        double TaxtotalAmount = 0.0;

        // Iterate through the cursor or any other data source to calculate the total amount

        return TaxtotalAmount;
    }
    public void updateheadertable(double totalAmount, double TaxtotalAmount, String roomid, String tableid) {

        // Get the current date and time
        String currentDate = mDatabaseHelper.getCurrentDate();
        String currentTime = mDatabaseHelper.getCurrentTime();

        // Calculate the total HT_A (priceWithoutVat) and total TTC (totalAmount)

        double totalTTC = totalAmount;
        double totaltax = TaxtotalAmount;
        double totalHT_A = totalTTC - totaltax;
        Toast.makeText(getContext(), "Room:" + roomid + " - Table" + tableid, Toast.LENGTH_SHORT).show();


        // Get the total quantity of items in the transaction
        int quantityItem = mDatabaseHelper.calculateTotalItemQuantity(transactionIdInProgress);

        // Save the transaction details in the TRANSACTION_HEADER table
        boolean success = mDatabaseHelper.updateTransactionHeader(

                totalAmount,
                currentDate,
                currentTime,
                totalHT_A,
                totalTTC,
                quantityItem,
                totaltax,
                cashierId,
                String.valueOf(roomid),
                tableid
        );

        if (success) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
            String deviceType = sharedPreferences.getString("device_type", null);


            if ("sunmiT2".equalsIgnoreCase(deviceType)) {
                showSecondaryScreen(data);
            } else {
                Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
            }

        } else {

            Intent intent = new Intent(getActivity(), SplashFlashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable window animation
            startActivity(intent);
            getActivity().finish();


        }

    }

        public void updateheader(double totalAmount, double TaxtotalAmount) {

        // Get the current date and time
        String currentDate = mDatabaseHelper.getCurrentDate();
        String currentTime = mDatabaseHelper.getCurrentTime();

        // Calculate the total HT_A (priceWithoutVat) and total TTC (totalAmount)

        double totalTTC = totalAmount;
        double totaltax = TaxtotalAmount;
        double totalHT_A = totalTTC - totaltax;


        // Get the total quantity of items in the transaction
        int quantityItem = mDatabaseHelper.calculateTotalItemQuantity(transactionIdInProgress);

        // Save the transaction details in the TRANSACTION_HEADER table
        boolean success = mDatabaseHelper.updateTransactionHeader(

                totalAmount,
                currentDate,
                currentTime,
                totalHT_A,
                totalTTC,
                quantityItem,
                totaltax,
                cashierId,
                String.valueOf(roomid),
                tableid
        );

        if (success) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
            String deviceType = sharedPreferences.getString("device_type", null);


            if ("sunmiT2".equalsIgnoreCase(deviceType)) {
                showSecondaryScreen(data);
            }  else {
                Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
            }

        } else {

            Intent intent = new Intent(getActivity(), SplashFlashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable window animation
            startActivity(intent);
            getActivity().finish();


        }


    }






    public  void refreshData(double totalAmount, double TaxtotalAmount) {


        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
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



        // Play the sound effect
        playSoundEffect();
    }
    public  void refreshDatatable(double totalAmount, double TaxtotalAmount,String roonum,String tableid) {

        // Set the data to the fragment views
        TextView roomTextView = getView().findViewById(R.id.textViewRoom);
        roomTextView.setText("Room: " + roonum);

        TextView tableTextView = getView().findViewById(R.id.textViewTable);
        tableTextView.setText(" - Table: " + tableid);

        TextView cashierTextView = getView().findViewById(R.id.textViewCashier);
        cashierTextView.setText(" - Cashier: " + cashierId);
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(roonum,tableid);
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



        // Play the sound effect
        playSoundEffect();
    }
    // Helper method to save buyer info to shared preferences
    private void saveBuyerInfoToPrefs(String buyerName, String priceLevel) {
        SharedPreferences sharedPrefs = requireContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("BuyerName", buyerName);
        editor.putString("PriceLevel", priceLevel);

        editor.apply();
    }

    // Helper method to clear buyer info from shared preferences
    private void clearBuyerInfoFromPrefs() {
        SharedPreferences sharedPrefs = requireContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    private void playSoundEffect() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
