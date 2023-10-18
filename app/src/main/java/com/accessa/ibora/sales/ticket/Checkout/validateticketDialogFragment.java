package com.accessa.ibora.sales.ticket.Checkout;

import static com.accessa.ibora.product.items.DatabaseHelper.CASH_REPORT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_DATETIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_POSNUM;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTAL;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTALIZER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TRANSACTION_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.getCurrentDateTime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MRA.Mra;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.POP.PopMobileAmountDialogFragment;
import com.accessa.ibora.POP.PopMobileDialogFragment;
import com.accessa.ibora.POP.PopQRDialogFragment;
import com.accessa.ibora.QR.QRFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class validateticketDialogFragment extends DialogFragment  {
    public interface DataPassListener {
        void onDataPass(String name, String id, String QR);
    }
    private QRFragment.DataPassListener dataPassListener;
    private DBManager Xdatabasemanager;
    private RecyclerView mRecyclerView;

    private IWoyouService woyouService;
    private String qrname,code, qrid;
    private CheckoutGridAdapter mAdapter;
    private static final int QR_CODE_REQUEST_CODE = 1;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private static final String ARG_Transaction_id = "transactionId";
    private static final String POSNumber="posNumber";
    private String qrMra,Transaction_Id,PosNum,amountpaid,pop,mrairn,Buyname,BuyBRN,BuyTAN,BuyAdd,BuyComp,BuyNIC,BuyProfile,BuyType;
    private String cashierId,cashierName,cashorlevel,CompanyName,posNum;


    private double cashReturn;
    private Set<String> selectedItems; // Store selected items
    private TextView IDselectedQRTextView;
    private double totalAmount, TaxtotalAmount;
    private DatabaseHelper mDatabaseHelper;
    private LinearLayout containerLayout; // Added
    private  Button validateButton;
    private static final String amounts = "amount";
    private static final String popFragment = "popfragment";
    private static final String Buyertype="Buyertype";

    private static final String BuyerName="buyername";
    private static final String Buyertan="BuyerTan";
    private static final String BuyerNIC="BuyerNIC";
    private static final String BuyerProfile="BuyerProfile";
    private static final String BuyerCompName="BuyerCompName";
    private static final String BuyerBusinessAddress="BuyerBusinessAdress";

    private static final String BuyerbRN="BuyerBRN";

    private View view; // Declare the view variable

    private String receivedQRCode; // Declare a member variable



    // You can create a public method to get the stored QR code string


    public static validateticketDialogFragment newInstance(String transactionId, String amount, String popFraction,String BuyerType, String buyername, String BuyerTan,String BuyerCompname,String BusinessAddess,String BuyerBRN,String buyerNIC,String buyerProfile) {
        validateticketDialogFragment fragment = new validateticketDialogFragment();
        Bundle args = new Bundle();

        args.putString(ARG_Transaction_id, transactionId);
        args.putString(amounts, amount);
        args.putString(popFragment, popFraction);
        args.putString(Buyertype, BuyerType);
        args.putString(BuyerName, buyername);
        args.putString(Buyertan, BuyerTan);
        args.putString(BuyerCompName, BuyerCompname);
        args.putString(BuyerBusinessAddress, BusinessAddess);
        args.putString(BuyerbRN, BuyerBRN);
        args.putString(BuyerNIC, buyerNIC);
        args.putString(BuyerProfile, buyerProfile);

        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        selectedItems = new HashSet<>(); // Initialize the set

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);


        SharedPreferences sharedPreference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierName = sharedPreference.getString("cashorName", null);
        cashorlevel = sharedPreference.getString("cashorlevel", null);
        CompanyName=sharedPreference.getString("CompanyName",null);
        SharedPreferences sharedPreferencepos = requireContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        posNum = sharedPreferencepos.getString("posNumber", null);
        // Check if intent has data
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String receivedAmount = intent.getStringExtra("amount");
            String receivedPopFraction = intent.getStringExtra("popFraction");

            if (receivedAmount != null && receivedPopFraction != null) {
                // Perform actions based on received data
                // For example, find and click the button if needed
                if (receivedPopFraction.equals("SOME_VALUE")) {
                    // Find the button and programmatically click it
                    Button button = view.findViewById(R.id.buttonCash);
                    button.performClick();
                }
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_validate_transaction, null);

        containerLayout = view.findViewById(R.id.container_layout); // Initialize the container layout

        int numberOfColumns = 2;
        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor3 = mDatabaseHelper.getAllPaymentMethod();

        mAdapter = new CheckoutGridAdapter(getContext(), cursor3);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idTextView = view.findViewById(R.id.id_text_view);
                TextView nameTextView = view.findViewById(R.id.name_text_view);
                TextView qrTextView = view.findViewById(R.id.icon);

                String id = idTextView.getText().toString();
                String qrCode = qrTextView.getText().toString();
                String name = nameTextView.getText().toString();
                if(id !=null && (id.equals("1") && name.equals("POP")))
                {
                    showPopAmountOptionsDialog(); // Call the showPopOptionsDialog() method here for "POP" button click

                } else if (id != null && (id.equals("6") && name.equals("Coupon Code"))) {
                    // Create an AlertDialog.Builder
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Enter Barcode"); // Set dialog title

                    // Create an EditText for the barcode input
                    final EditText editTextBarcode = new EditText(getContext());
                    editTextBarcode.setHint("Barcode Number"); // Set hint for EditText
                    alertDialogBuilder.setView(editTextBarcode);

                    // Create a method for handling barcode validation
                    DialogInterface.OnClickListener validateBarcode = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // This logic is the same as before
                            String barcode = editTextBarcode.getText().toString();
                            boolean isBarcodeValid = mDatabaseHelper.isValidBarcode(barcode);

                            if (isBarcodeValid) {
                                Toast.makeText(getContext(), "Valid Barcode", Toast.LENGTH_SHORT).show();
                                // Barcode is valid, start another activity and pass the amount
                                // Intent intent = new Intent(getContext(), AnotherActivity.class);
                                // intent.putExtra("amount", calculateAmount()); // Pass the calculated amount
                                // startActivity(intent);
                            } else {
                                // Barcode is invalid, show an error message
                                Toast.makeText(getContext(), "Invalid Barcode", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    // Set positive button (Enter)
                    alertDialogBuilder.setPositiveButton("Enter", validateBarcode);

                    // Set an OnEditorActionListener for the EditText
                    editTextBarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                // "Enter" key on the keyboard was pressed
                                validateBarcode.onClick(null, DialogInterface.BUTTON_POSITIVE); // Trigger the validation logic
                                return true;
                            }
                            return false;
                        }
                    });

                    // Create and show the AlertDialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }



                else if (id != null && !selectedItems.contains(id)) {
                    selectedItems.add(id);
                    // Create a new TextView
                    TextView textView = new TextView(getContext());
                    textView.setText(name); // Set the text for the new TextView
                    textView.setGravity(Gravity.CENTER);

                    // Add the TextView to the container layout
                    containerLayout.addView(textView);
                    String amount = " Rs 0.00";
                    // Create a new EditText
                    EditText editText = new EditText(getContext());
                    editText.setHint(amount);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    // Create layout parameters for centering the EditText
                    LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                            500,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    editParams.gravity = Gravity.CENTER;

                    // Apply the layout parameters to the EditText
                    editText.setLayoutParams(editParams);

                    // Set other properties of the EditText
                    Drawable drawable = getResources().getDrawable(R.drawable.edittext1_rounded_bg);
                    editText.setBackground(drawable);
                    editText.setTextColor(getResources().getColor(R.color.BleuAccessaText));
                    editText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                    // Add a TextWatcher to the EditText
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            calculateTotalAmount();
                        }
                    });

                    // Add the EditText to the container layout
                    containerLayout.addView(editText);
                    // Check if id is "1" and name is "POP"

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click
                // ...
            }
        }));

        Xdatabasemanager = new DBManager(getContext());
        Xdatabasemanager.open();

        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(getContext());

        // Get the arguments passed to the dialog fragment
        Bundle args = getArguments();
        if (args != null) {
            Transaction_Id = args.getString(ARG_Transaction_id);
            amountpaid = args.getString(amounts);
            pop= args.getString(popFragment);
            BuyType=args.getString(Buyertype);
            Buyname=args.getString(BuyerName);
            BuyTAN=args.getString(Buyertan);
            BuyComp=args.getString(BuyerCompName);
            BuyAdd=args.getString(BuyerBusinessAddress);
            BuyBRN=args.getString(BuyerbRN);
            BuyNIC=args.getString(BuyerNIC);
            BuyProfile=args.getString(BuyerProfile);



            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

            SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
            cashierId = sharedPreference.getString("cashorId", null);

            mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper

            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                totalAmount = cursor.getDouble(columnIndexTotalAmount);
                TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                TextView totalAmountTextView = view.findViewById(R.id.textViewAmount);
                String formattedTotalAmount = String.format("%.2f", totalAmount);
                totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
            }
        }

        // Add this code inside the onCreateDialog() method of validateticketDialogFragment
        EditText amountReceivedEditText = view.findViewById(R.id.editAbbrev);
         validateButton = view.findViewById(R.id.buttonCash);

        amountReceivedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountReceivedString = s.toString();
                double amountReceived = 0.0;

                if (!amountReceivedString.isEmpty()) {
                    try {
                        amountReceived = Double.parseDouble(amountReceivedString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                double totalAmountEntered = 0.0;

                // Iterate over the container layout to calculate the total amount entered
                for (int i = 0; i < containerLayout.getChildCount(); i++) {
                    View childView = containerLayout.getChildAt(i);

                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        String amountText = editText.getText().toString();
                        double enteredAmount = 0.0;
                        if (!amountText.isEmpty()) {
                            enteredAmount = Double.parseDouble(amountText);
                        }
                        totalAmountEntered += enteredAmount;
                    }
                }

                double remainingAmount = totalAmount - totalAmountEntered;

                TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
                TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);
                TextView textViewCashReturn = view.findViewById(R.id.textViewCashReturn);

                if (remainingAmount < 0) {
                    remainingAmount = 0;
                    remainingAmountTextView.setVisibility(View.GONE);
                    remainingTotalAmountTextView.setVisibility(View.GONE);
                    textViewCashReturn.setVisibility(View.VISIBLE);
                } else {
                    remainingAmountTextView.setVisibility(View.VISIBLE);
                    remainingTotalAmountTextView.setVisibility(View.VISIBLE);
                    textViewCashReturn.setVisibility(View.GONE);
                }

                remainingAmountTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", remainingAmount));

                TextView textViewCashReturnAmount = view.findViewById(R.id.textViewCashReturnAmount);
                TextView cashReturnTextView = view.findViewById(R.id.textViewCashReturnAmount);
                if (totalAmountEntered >= totalAmount) {
                    double cashReturn = totalAmountEntered - totalAmount;
                    cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));


                    textViewCashReturnAmount.setVisibility(View.VISIBLE);
                    cashReturnTextView.setVisibility(View.VISIBLE);
                    validateButton.setVisibility(View.VISIBLE);
                } else {
                    textViewCashReturnAmount.setVisibility(View.GONE);
                    cashReturnTextView.setVisibility(View.GONE);
                    validateButton.setVisibility(View.GONE);
                }
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printData();
            }

            private void printData() {



                // Get the amount received
                double amountReceived = 0.0;
                if (!amountReceivedEditText.getText().toString().isEmpty()) {
                    amountReceived = Double.parseDouble(amountReceivedEditText.getText().toString());
                }

                // Iterate over the container layout to get the settlement details
                ArrayList<SettlementItem> settlementItems = new ArrayList<>();
                for (int i = 0; i < containerLayout.getChildCount(); i += 2) {
                    View childView = containerLayout.getChildAt(i);
                    if (childView instanceof TextView) {
                        TextView nameTextView = (TextView) childView;
                        String paymentName = nameTextView.getText().toString();

                        View amountView = containerLayout.getChildAt(i + 1);
                        if (amountView instanceof EditText) {
                            EditText amountEditText = (EditText) amountView;
                            String amountText = amountEditText.getText().toString();
                            double settlementAmount = 0.0;
                            if (!amountText.isEmpty()) {
                                settlementAmount = Double.parseDouble(amountText);
                            }
                            settlementItems.add(new SettlementItem(paymentName, settlementAmount));
                        }
                    }
                }

// Update the table with settlement details

                // Update the table with settlement details
                for (SettlementItem settlementItem : settlementItems) {

                    // Use a specific Locale for date formatting
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String currentDate = dateFormat.format(new Date());

                    boolean updated = mDatabaseHelper.insertSettlementAmount(settlementItem.getPaymentName(), settlementItem.getSettlementAmount(), Transaction_Id, PosNum, currentDate);

                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Use the current date
                    values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                    values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, settlementItem.getPaymentName());
                    values.put(FINANCIAL_COLUMN_POSNUM, PosNum); // Insert the posnum

// Check if a row with the same payment name, current date, cashier ID, and posnum already exists
                    String[] whereArgs = new String[] {settlementItem.getPaymentName(), currentDate, cashierId, PosNum};
                    Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                            whereArgs, null, null, null);

                    if (cursor.moveToFirst()) {
                        // If a row with the same payment name, current date, cashier ID, and posnum exists, update the values
                        int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                        double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                        double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                        values.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                        values.put(FINANCIAL_COLUMN_TOTAL, currentTotal + settlementItem.getSettlementAmount()); // Update the total

                        // Check if the payment name is "Cash" or "Cheques" to update the totalizer
                        if ("Cash".equals(settlementItem.getPaymentName()) || "Cheque".equals(settlementItem.getPaymentName())) {
                            values.put(FINANCIAL_COLUMN_TOTALIZER, currentTotalizer + settlementItem.getSettlementAmount()); // Update the totalizer

                        } else {
                            // If the payment name is not "Cash" or "Cheques," do not update the totalizer
                            values.put(FINANCIAL_COLUMN_TOTALIZER, 0.00);
                        }

                        db.update(FINANCIAL_TABLE_NAME, values, FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " +
                                FINANCIAL_COLUMN_DATETIME + " = ? AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " +
                                FINANCIAL_COLUMN_POSNUM + " = ?", whereArgs);
                    } else {
                        // If no row with the same payment name and current date exists, insert a new row
                        values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                        values.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Initialize total
                        values.put(FINANCIAL_COLUMN_TOTALIZER, settlementItem.getSettlementAmount()); // Initialize totalizer

                        db.insert(FINANCIAL_TABLE_NAME, null, values);
                    }
                    if ("Cash".equals(settlementItem.getPaymentName()) ) {
                        ContentValues cashReportValues = new ContentValues();
                        cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                        cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                        cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                        cashReportValues.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Positive cash in amount
                        cashReportValues.put(FINANCIAL_COLUMN_POSNUM, PosNum);

                        db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);
                    }
                    if (updated) {
                        Toast.makeText(getActivity(), "Settlement amount insert for " + settlementItem.getPaymentName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to insert settlement amount for " + settlementItem.getPaymentName(), Toast.LENGTH_SHORT).show();
                    }

                }

                // Declare a variable to hold the total amount
                double totalAmountinserted = 0.0;

                    // Iterate over the settlement items to calculate the total amount
                for (SettlementItem item : settlementItems) {
                    totalAmountinserted += item.getSettlementAmount();
                }

                // Print or use the total amount as needed

                double cashReturn= totalAmountinserted- totalAmount;
                // Pass the amount received and settlement items as extras to the print activity
                Intent intent = new Intent(getActivity(), Mra.class);
                intent.putExtra("amount_received", String.valueOf(totalAmountinserted));
                intent.putExtra("cash_return", String.valueOf(cashReturn));
                intent.putExtra("settlement_items", settlementItems);
                intent.putExtra("id", Transaction_Id);
                intent.putExtra("selectedBuyerName", Buyname);
                intent.putExtra("selectedBuyerTAN", BuyTAN);
                intent.putExtra("selectedBuyerCompanyName", BuyComp);
                intent.putExtra("selectedBuyerType",BuyType);
                intent.putExtra("selectedBuyerBRN", BuyBRN);
                intent.putExtra("selectedBuyerNIC", BuyNIC);
                intent.putExtra("selectedBuyerAddresse", BuyAdd);
                intent.putExtra("selectedBuyerprofile", BuyProfile);


                if (cashReturn != 0.0) {
                    cashReturn = -cashReturn; // Make cashReturn negative

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String currentDate = dateFormat.format(new Date());

                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    // Create an entry for "Cash Return" in the financial table
                    ContentValues cashReturnValues = new ContentValues();
                    cashReturnValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                    cashReturnValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                    cashReturnValues.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Cash Return");
                    cashReturnValues.put(FINANCIAL_COLUMN_POSNUM, PosNum);
                    cashReturnValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is set to 1 for Cash Return
                    cashReturnValues.put(FINANCIAL_COLUMN_TOTAL, cashReturn); // Negate cashReturn
                    cashReturnValues.put(FINANCIAL_COLUMN_TOTALIZER, cashReturn); // Negate cashReturn

                    // Check if a row with the same payment name, current date, cashier ID, and posnum already exists
                    String[] whereArgs = new String[]{"Cash Return", currentDate, cashierId, PosNum};
                    Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                            whereArgs, null, null, null);

                    if (cursor.moveToFirst()) {
                        // If a row with the same payment name, current date, cashier ID, and posnum exists, update the values
                        int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                        double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                        double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                        cashReturnValues.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                        cashReturnValues.put(FINANCIAL_COLUMN_TOTAL, currentTotal + cashReturn); // Update the total
                        cashReturnValues.put(FINANCIAL_COLUMN_TOTALIZER, currentTotalizer + cashReturn); // Update the totalizer

                        db.update(FINANCIAL_TABLE_NAME, cashReturnValues, FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " +
                                FINANCIAL_COLUMN_DATETIME + " = ? AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " +
                                FINANCIAL_COLUMN_POSNUM + " = ?", whereArgs);
                    } else {
                        // If no row with the same payment name and current date exists, insert a new row
                        db.insert(FINANCIAL_TABLE_NAME, null, cashReturnValues);

                    }

                    ContentValues cashReportValues = new ContentValues();
                    cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                    cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                    cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                    cashReportValues.put(FINANCIAL_COLUMN_TOTAL, cashReturn); // Positive cash in amount
                    cashReportValues.put(FINANCIAL_COLUMN_POSNUM, PosNum);

                    db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);
                }


                String MRAMETHOD="Single";
                insertCashReturn(String.valueOf(cashReturn), String.valueOf(totalAmountinserted),qrMra,mrairn,MRAMETHOD);
                startActivity(intent);
            }

        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void showPopAmountOptionsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Select Amount");
        String[] options = {"Pay part of amount  by POP", "Pay Full amount by POP"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's selection here
                if (which == 0) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    builder.setView(R.layout.validate_amount);

                    // Set up the dialog
                    androidx.appcompat.app.AlertDialog dialogmob = builder.create();
                    dialogmob.show();

                    // Get references to the views in the popup layout
                    EditText editamount = dialogmob.findViewById(R.id.editAmount);
                    Button btnInsert = dialogmob.findViewById(R.id.btnInsert);

                    // Set up button click listener
                    btnInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Retrieve the entered amount from the EditText
                            String amountInserted = editamount.getText().toString();
                            showPopOptionsWithAmountDialog(amountInserted);

                        }
                    });
                } else if (which == 1) {
                    showPopOptionsDialog();
                }
            }
        });
        builder.show();
    }

    private void showPopOptionsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Select Payment Method");
        String[] options = {"Pay by mobile number", "Pay by QR code"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's selection here
                if (which == 0) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    builder.setView(R.layout.validate_num);

                    // Set up the dialog
                    androidx.appcompat.app.AlertDialog dialogmob = builder.create();
                    dialogmob.show();

                    // Get references to the views in the popup layout
                    EditText editTerminalNo = dialogmob.findViewById(R.id.editTerminalNo);
                    EditText editamount = dialogmob.findViewById(R.id.editCompanyName);
                    Button btnInsert = dialogmob.findViewById(R.id.btnInsert);

                    // Set up button click listener
                    btnInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Retrieve the entered mobile number from the EditText
                            String mobileNumber = editTerminalNo.getText().toString();

                            // Create the PopMobileDialogFragment instance and pass the mobile number as an argument
                            PopMobileDialogFragment popMobileDialogFragment = PopMobileDialogFragment.newInstance(mobileNumber);
                            // Hide the keyboard before showing the dialog
                            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            // Show the PopMobileDialogFragment
                            popMobileDialogFragment.show(getChildFragmentManager(), "pop_mobile_dialog");

                            // Dismiss the dialog
                            dialog.dismiss();
                            // Dismiss the dialogmob
                            dialogmob.dismiss();

                        }
                    });
                } else if (which == 1) {
                    // User selected "Pay by QR code"
                    // Perform the action for this option
                    // For example, open a QR code scanner activity
                    Cursor cursor = mDatabaseHelper.getTransactionHeader();
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);

                        double totalAmount = cursor.getDouble(columnIndexTotalAmount);

                        // Format the double to a string with two decimal places
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String formattedTotalAmount = decimalFormat.format(totalAmount);
                        Log.e("total amount", formattedTotalAmount);
                        String fileNameMerchName = "merch_name.txt";
                        String fileNamePhone = "mob_num.txt";
                        String fileNametill = "till_num.txt";

                        String MerchantName = readTextFromFile(fileNameMerchName);
                        String TillNo = readTextFromFile(fileNametill);
                        String PhoneNum = readTextFromFile(fileNamePhone);
                        String amountsVariation = countAndConcatenate(formattedTotalAmount);
                        String QR=    "00020101021126630009mu.maucas0112BKONMUM0XXXX021103011065958031500000000000005252047278530348054" +amountsVariation+"5802MU5912"+MerchantName+"6015Agalega North I62220211"+PhoneNum+"0703"+TillNo+"6304BE4F";
                        Log.e("qrstring", QR);
                        showSecondaryScreen("POP","1",QR);


                        // Create the PopMobileDialogFragment instance and pass the mobile number as an argument
                        PopQRDialogFragment popMobileDialogFragment = PopQRDialogFragment.newInstance(QR);

                        // Show the PopMobileDialogFragment
                        popMobileDialogFragment.show(getChildFragmentManager(), "pop_qr_dialog");

                        // Dismiss the dialog
                        dialog.dismiss();
                    }


                }
            }
        });
        builder.show();
    }
    public static String countAndConcatenate(String input) {
        int charCount = input.length();
        String formattedCharCount = String.format("%02d", charCount);
        return formattedCharCount + input;
    }
    private void showPopOptionsWithAmountDialog(String Amount) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Select Payment Method");
        String[] options = {"Pay by mobile number", "Pay by QR code"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's selection here
                if (which == 0) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    builder.setView(R.layout.validate_num_amount);

                    // Set up the dialog
                    androidx.appcompat.app.AlertDialog dialogmob = builder.create();
                    dialogmob.show();

                    // Get references to the views in the popup layout
                    EditText editTerminalNo = dialogmob.findViewById(R.id.editTerminalNo);
                    EditText editamount = dialogmob.findViewById(R.id.editAmount);
                    editamount.setText(Amount);
                    Button btnInsert = dialogmob.findViewById(R.id.btnInsert);

                    // Set up button click listener
                    btnInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Retrieve the entered mobile number from the EditText
                            String mobileNumber = editTerminalNo.getText().toString();// Retrieve the entered amount from the EditText
                            String amountInserted = editamount.getText().toString();


                            // Create the PopMobileDialogFragment instance and pass the mobile number as an argument
                            PopMobileAmountDialogFragment popMobileAmountDialogFragment = PopMobileAmountDialogFragment.newInstance(mobileNumber,amountInserted);
                            // Hide the keyboard before showing the dialog
                            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            // Show the PopMobileDialogFragment
                            popMobileAmountDialogFragment.show(getChildFragmentManager(), "pop_mobile_dialog");

                            // Dismiss the dialog
                            dialog.dismiss();
                            // Dismiss the dialogmob
                            dialogmob.dismiss();

                        }
                    });
                } else if (which == 1) {
                    // User selected "Pay by QR code"
                    // Perform the action for this option
                    // For example, open a QR code scanner activity
                    Cursor cursor = mDatabaseHelper.getTransactionHeader();
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);

                        double totalAmount = cursor.getDouble(columnIndexTotalAmount);

                        // Format the double to a string with two decimal places
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String formattedTotalAmount = decimalFormat.format(totalAmount);
                        Log.e("total amount", formattedTotalAmount);
                        String fileNameMerchName = "merch_name.txt";
                        String fileNamePhone = "mob_num.txt";
                        String fileNametill = "till_num.txt";

                        String MerchantName = readTextFromFile(fileNameMerchName);
                        String TillNo = readTextFromFile(fileNametill);
                        String PhoneNum = readTextFromFile(fileNamePhone);
                        String amountsVariation = countAndConcatenate(formattedTotalAmount);
                        String QR=    "00020101021126630009mu.maucas0112BKONMUM0XXXX021103011065958031500000000000005252047278530348054" +amountsVariation+"5802MU5912"+MerchantName+"6015Agalega North I62220211"+PhoneNum+"0703"+TillNo+"6304BE4F";
                        Log.e("qrstring", QR);
                        showSecondaryScreen("POP","1",QR);


                        // Create the PopMobileDialogFragment instance and pass the mobile number as an argument
                        PopQRDialogFragment popMobileDialogFragment = PopQRDialogFragment.newInstance(QR);
                        // Hide the keyboard before showing the dialog
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                        // Show the PopMobileDialogFragment
                        popMobileDialogFragment.show(getChildFragmentManager(), "pop_qr_dialog");

                        // Dismiss the dialog
                        dialog.dismiss();
                    }


                }
            }
        });
        builder.show();
    }
    private  String readTextFromFile(String fileName) {
        try {
            FileInputStream fileInputStream = getContext().openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showSecondaryScreen(String name,String id, String QR) {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();
        String formattedTaxAmount = null,formattedTotalAmount = null;
        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            SeconScreenDisplay secondaryDisplay = new SeconScreenDisplay(getActivity(), presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                double taxTotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                formattedTaxAmount = String.format("%.2f", taxTotalAmount);
                formattedTotalAmount = String.format("%.2f", totalAmount);
            }
            // Get the selected item from the RecyclerView
            String selectedName = name;
            String selectedQR = QR;
            // Convert the QR code string to a Bitmap
            Bitmap qrBitmap = generateQRCodeBitmap(selectedQR);
            // Update the text and QR code on the secondary screen
            secondaryDisplay.updateTextAndQRCode(selectedName, qrBitmap, formattedTaxAmount, formattedTotalAmount);
        } else {
            // Secondary screen not found or not supported
            //  Toast.makeText(getActivity(), "Secondary screen not found or not supported", Toast.LENGTH_SHORT).show();
            dataPassListener.onDataPass(name, id, QR);

        }
    }

    private Bitmap generateQRCodeBitmap(String qrCode) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 400;
        int height = 400;
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(qrCode, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        Bitmap qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return qrBitmap;
    }

    private Display getPresentationDisplay() {
        DisplayManager displayManager = (DisplayManager) requireContext().getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                return display;
            }
        }
        return null;
    }


public void  insertCashReturn(String cashReturn, String totalAmountinserted, String qrMra, String mrairn, String MRAMETHOD){

     mDatabaseHelper.insertcashReturn(cashReturn,totalAmountinserted, Transaction_Id,qrMra,mrairn,MRAMETHOD);

}
    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);

            // Call the method to display on the LCD here

            displayQROnLCD(code,qrname);

        }
    };
    private void displayQROnLCD(String code, String name) {
        if (woyouService == null) {
            //   Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (name.equals("POP")){
                int fontSize = 10; // Set your desired font size
                int textColor = Color.WHITE; // Set your desired text color
                Typeface typeface = Typeface.DEFAULT; // Set your desired font typeface

                // Generate QR code bitmap
                int qrCodeSize = 2; // Set your desired QR code size
                BitMatrix qrCodeMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
                int qrCodeWidth = qrCodeMatrix.getWidth();
                int qrCodeHeight = qrCodeMatrix.getHeight();

                Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888);
                for (int x = 0; x < qrCodeWidth; x++) {
                    for (int y = 0; y < qrCodeHeight; y++) {
                        qrCodeBitmap.setPixel(x, y, qrCodeMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                // Generate text bitmap
                Paint textPaint = new Paint();
                textPaint.setTextSize(fontSize);
                textPaint.setColor(textColor);
                textPaint.setTypeface(typeface);

                Rect textBounds = new Rect();
                textPaint.getTextBounds(name, 0, name.length(), textBounds);
                int textWidth = textBounds.width();
                int textHeight = textBounds.height();

                Bitmap textBitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(name, 0, textHeight, textPaint);

                // Create composite bitmap
                int compositeWidth = qrCodeWidth + textWidth;
                int compositeHeight = Math.max(qrCodeHeight, textHeight);

                Bitmap compositeBitmap = Bitmap.createBitmap(compositeWidth, compositeHeight, Bitmap.Config.ARGB_8888);
                Canvas compositeCanvas = new Canvas(compositeBitmap);
                compositeCanvas.drawColor(Color.BLACK);

                compositeCanvas.drawBitmap(qrCodeBitmap, 0, (compositeHeight - qrCodeHeight) / 2, null);
                compositeCanvas.drawBitmap(textBitmap, qrCodeWidth, (compositeHeight - textHeight) / 2, null);

                woyouService.sendLCDBitmap(compositeBitmap, null);

            }else {
                int fontSize = 10; // Set your desired font size
                int textColor = Color.WHITE; // Set your desired text color
                Typeface typeface = Typeface.DEFAULT; // Set your desired font typeface

                // Generate QR code bitmap
                int qrCodeSize = 50; // Set your desired QR code size
                BitMatrix qrCodeMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
                int qrCodeWidth = qrCodeMatrix.getWidth();
                int qrCodeHeight = qrCodeMatrix.getHeight();

                Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888);
                for (int x = 0; x < qrCodeWidth; x++) {
                    for (int y = 0; y < qrCodeHeight; y++) {
                        qrCodeBitmap.setPixel(x, y, qrCodeMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                // Generate text bitmap
                Paint textPaint = new Paint();
                textPaint.setTextSize(fontSize);
                textPaint.setColor(textColor);
                textPaint.setTypeface(typeface);

                Rect textBounds = new Rect();
                textPaint.getTextBounds(name, 0, name.length(), textBounds);
                int textWidth = textBounds.width();
                int textHeight = textBounds.height();

                Bitmap textBitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(name, 0, textHeight, textPaint);

                // Create composite bitmap
                int compositeWidth = qrCodeWidth + textWidth;
                int compositeHeight = Math.max(qrCodeHeight, textHeight);

                Bitmap compositeBitmap = Bitmap.createBitmap(compositeWidth, compositeHeight, Bitmap.Config.ARGB_8888);
                Canvas compositeCanvas = new Canvas(compositeBitmap);
                compositeCanvas.drawColor(Color.BLACK);

                compositeCanvas.drawBitmap(qrCodeBitmap, 0, (compositeHeight - qrCodeHeight) / 2, null);
                compositeCanvas.drawBitmap(textBitmap, qrCodeWidth, (compositeHeight - textHeight) / 2, null);

                woyouService.sendLCDBitmap(compositeBitmap, null);
            }
        } catch (RemoteException | WriterException e) {
            e.printStackTrace();
        }

    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getActivity(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home_intent1);
    }

    private void calculateTotalAmount() {
        double totalAmountEntered = 0.0;

        // Iterate over the container layout to calculate the total amount entered
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View childView = containerLayout.getChildAt(i);

            if (childView instanceof EditText) {
                EditText editText = (EditText) childView;
                String amountText = editText.getText().toString();
                double enteredAmount = 0.0;
                if (!amountText.isEmpty()) {
                    enteredAmount = Double.parseDouble(amountText);
                }
                totalAmountEntered += enteredAmount;
            }
        }

        double remainingAmount = totalAmount - totalAmountEntered;

        TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
        TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);
        TextView textViewCashReturn = view.findViewById(R.id.textViewCashReturn);

        if (remainingAmount < 0) {
            remainingAmount = 0;
            remainingAmountTextView.setVisibility(View.GONE);
            remainingTotalAmountTextView.setVisibility(View.GONE);
            textViewCashReturn.setVisibility(View.VISIBLE);
        } else {
            remainingAmountTextView.setVisibility(View.VISIBLE);
            remainingTotalAmountTextView.setVisibility(View.VISIBLE);
            textViewCashReturn.setVisibility(View.GONE);
        }

        remainingAmountTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", remainingAmount));

        TextView textViewCashReturnAmount = view.findViewById(R.id.textViewCashReturnAmount);
        TextView cashReturnTextView = view.findViewById(R.id.textViewCashReturnAmount);
        if (totalAmountEntered >= totalAmount) {
            double cashReturn = totalAmountEntered - totalAmount;
            cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
            textViewCashReturnAmount.setVisibility(View.VISIBLE);
            cashReturnTextView.setVisibility(View.VISIBLE);
            validateButton.setVisibility(View.VISIBLE);
        } else {
            textViewCashReturnAmount.setVisibility(View.GONE);
            cashReturnTextView.setVisibility(View.GONE);
            validateButton.setVisibility(View.GONE);
        }
    }




}
