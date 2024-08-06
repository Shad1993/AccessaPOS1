package com.accessa.ibora.sales.ticket.Checkout;

import static com.accessa.ibora.product.items.DatabaseHelper.CASH_REPORT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.COUPON_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_DATETIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_PAYMENT;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_POSNUM;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_SHOP_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTAL;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTALIZER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TRANSACTION_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TransId;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_CashReturn;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.SETTLEMENT_SHOP_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_SHIFT_NUMBER;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.Functions.FunctionFragment;
import com.accessa.ibora.MRA.Mra;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.POP.PopMobileAmountDialogFragment;
import com.accessa.ibora.POP.PopMobileDialogFragment;
import com.accessa.ibora.POP.PopQRDialogFragment;
import com.accessa.ibora.QR.QRFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.Settings.Rooms.Rooms;
import com.accessa.ibora.printer.externalprinterlibrary2.Kitchen.SendNoteToKitchenActivity;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.Tables.TablesFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class validateticketDialogFragment extends DialogFragment  {
    public interface DataPassListener {
        void onDataPass(String name, String id, String QR);
    }

    String id;
    private QRFragment.DataPassListener dataPassListener;
    private String tableid;
    private EditText clickedEditText;
    private SalesFragment.ItemAddedListener itemAddedListener;
    private String roomid;
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
    private String cashierId,cashierName,cashorlevel,CompanyName,posNum,ShopNumber;

    TextView totalAmountTextView;
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
    GridLayout gridLayout;
    private static final String BuyerName="buyername";
    private static final String Buyertan="BuyerTan";
    private static final String BuyerNIC="BuyerNIC";
    private static final String BuyerProfile="BuyerProfile";
    private static final String BuyerCompName="BuyerCompName";
    private static final String BuyerBusinessAddress="BuyerBusinessAdress";
    EditText amountReceivedEditText;
    private static final String BuyerbRN="BuyerBRN";
    private StringBuilder enteredPIN;
    private View view; // Declare the view variable

    private String receivedQRCode; // Declare a member variable

    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String PAYMENT_TYPE_KEY = "PaymentType";

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
        SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = String.valueOf(preferences.getInt("roomnum", 0));
        tableid = preferences.getString("table_id", "0");

        SharedPreferences sharedPreference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierName = sharedPreference.getString("cashorName", null);
        cashorlevel = sharedPreference.getString("cashorlevel", null);
        CompanyName=sharedPreference.getString("CompanyName",null);
        ShopNumber=sharedPreference.getString("ShopId",null);
        SharedPreferences sharedPreferencepos = requireContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        posNum = sharedPreferencepos.getString("posNumber", null);

// Initialize the StringBuilder for entered PIN
        enteredPIN = new StringBuilder();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_validate_transaction, container, false);


        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_validate_transaction, null);

        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);
        // Add this code inside the onCreateDialog() method of validateticketDialogFragment
         amountReceivedEditText = view.findViewById(R.id.editAbbrev);
        amountReceivedEditText.setFocusable(false);
        amountReceivedEditText.setFocusableInTouchMode(false);
        amountReceivedEditText.setCursorVisible(false);
        amountReceivedEditText.setClickable(true);

        amountReceivedEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event here if needed
            }
        });
        containerLayout = view.findViewById(R.id.container_layout); // Initialize the container layout
        validateButton = view.findViewById(R.id.buttonCash);
        int numberOfColumns = 1;
        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
         gridLayout = view.findViewById(R.id.grid);
        GifImageView gifImageView = view.findViewById(R.id.gif);

        // Set up button click listeners in the dialog
        setButtonClickListeners();
        // Find the number buttons and set OnClickListener
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        Button button3 = view.findViewById(R.id.button3);
        Button button4 = view.findViewById(R.id.button4);
        Button button5 = view.findViewById(R.id.button5);
        Button button6 = view.findViewById(R.id.button6);
        Button button7 = view.findViewById(R.id.button7);
        Button button8 = view.findViewById(R.id.button8);
        Button button9 = view.findViewById(R.id.button9);
        Button button0 = view.findViewById(R.id.button0);
        Button buttonbackspace = view.findViewById(R.id.buttonbackspace);
        Button buttonComma = view.findViewById(R.id.buttonComma);
        Button buttonClear = view.findViewById(R.id.buttonClear);


        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor3 = mDatabaseHelper.getAllPaymentMethod();

        mAdapter = new CheckoutGridAdapter(getContext(), cursor3);
        mRecyclerView.setAdapter(mAdapter);


        if (!isPaymentSplitted()) {
            // Full payment: Take the total amount as the value
            gridLayout.setVisibility(View.GONE);
            gifImageView.setVisibility(View.VISIBLE);
            amountReceivedEditText.setVisibility(View.GONE);
            containerLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            validateButton.setVisibility(View.GONE);
        }




        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idTextView = view.findViewById(R.id.id_text_view);
                TextView nameTextView = view.findViewById(R.id.name_text_view);
                TextView qrTextView = view.findViewById(R.id.icon);


                // Set the visibility of the GridLayout to VISIBLE
                gridLayout.setVisibility(View.VISIBLE);
                containerLayout.setVisibility(View.VISIBLE);

                 id = idTextView.getText().toString();
                String qrCode = qrTextView.getText().toString();
                String name = nameTextView.getText().toString();
                if (name.equals("Cash") || name.equals("Cheque")){



                    try {
                        woyouService.openDrawer(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                                if (!isPaymentSplitted()) {
                    // Full payment: Take the total amount as the value
                    gridLayout.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.VISIBLE);
                    containerLayout.setVisibility(View.GONE);
                    amountReceivedEditText.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    validateButton.setVisibility(View.GONE);
                    handleFullPayment(id);
                }
                else if (id !=null && (id.equals("1") && name.equals("POP")))
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



                else if (id != null ) {
                    selectedItems.add(id);

                    handleSplittedPayment(id);
                    amountReceivedEditText.setText("");
                    gridLayout.setVisibility(View.VISIBLE);
                    containerLayout.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    amountReceivedEditText.setVisibility(View.VISIBLE);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (amountReceivedEditText != null) {
                                onNumberButtonClick(v,"1");
                            } else {
                                // Handle the case where clickedEditText is null
                                Toast.makeText(getContext(), "No EditText selected", Toast.LENGTH_SHORT).show();
                                // Alternatively, perform another action to handle this case
                            }
                        }
                    });


                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"2");
                        }
                    });

                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"3");
                        }
                    });

                    button4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"4");
                        }
                    });

                    button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"5");
                        }
                    });

                    button6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"6");
                        }
                    });

                    button7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"7");
                        }
                    });

                    button8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"8");
                        }
                    });

                    button9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"9");
                        }
                    });

                    button0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick(v,"0");
                        }
                    });



                    buttonbackspace.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackspaceButtonClick(amountReceivedEditText);
                        }
                    });

                    buttonComma.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            oncommentButtonClick( ".");
                        }
                    });

                    buttonClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClearButtonClick(amountReceivedEditText);
                        }        });




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
            Cursor cursor = mDatabaseHelper.getTransactionHeaderTotal(roomid,tableid);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);
                int columnIndexSplitType = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_SPLIT_TYPE);

                boolean allItemsSelected =  mDatabaseHelper.areAllItemsSelected(roomid,tableid);

                String transactionid= mDatabaseHelper.getTransactionTicketNo(roomid,tableid);
                Cursor cursor1 = mDatabaseHelper.getSplittedInProgressNotSelectedNotPaidTransactions(transactionid,String.valueOf(roomid),tableid);
                Cursor cursor2 = mDatabaseHelper.getAllSplittedInProgressTransactions(String.valueOf(roomid),tableid);

                boolean areNOItemsSelected=  mDatabaseHelper.areAllItemsNotSelected(transactionid);
                boolean areAllItemsNotSelectedNotPaid=  mDatabaseHelper.areAllItemsNotSelectedNotPaid(transactionid);
                boolean areNoItemsSelectedNorPaid=  mDatabaseHelper.areNoItemsSelectedNorPaid(transactionid);
                Log.e("areNOItemsSelected" , String.valueOf(areNOItemsSelected));
                Log.e("areAllItemsNotSelectedNotPaid" , String.valueOf(areAllItemsNotSelectedNotPaid));
                Log.e("areNoItemsSelectedNorPaid" , String.valueOf(areNoItemsSelectedNorPaid));
                if(areAllItemsNotSelectedNotPaid) {
                    if (cursor1 != null && cursor1.moveToFirst()) {

                        totalAmount =mDatabaseHelper.calculateTotalAmount(roomid,tableid);
                        TaxtotalAmount=mDatabaseHelper.calculateTotalTaxAmount(roomid,tableid);
                        totalAmountTextView = view.findViewById(R.id.textViewAmount);
                        String formattedTotalAmount = String.format("%.2f", totalAmount);
                        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                        Log.e("formattedTotalAmount0" , formattedTotalAmount + " " +formattedTotalAmount);
                        // Call the getSumOfAmount method
                        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                        double sumOfAmountAmountpaid = mDatabaseHelper.getSumOfAmount(latesttransId, Integer.parseInt(roomid), tableid);

                        double remainingAmount = totalAmount - sumOfAmountAmountpaid;

                        TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
                        TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);


                        if(sumOfAmountAmountpaid>0){
                            remainingTotalAmountTextView.setVisibility(view.VISIBLE);
                            remainingAmountTextView.setVisibility(View.VISIBLE);
                            remainingAmountTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", remainingAmount));

                        }else{
                            remainingTotalAmountTextView.setVisibility(view.GONE);
                            remainingAmountTextView.setVisibility(View.GONE);

                        }

                    }

                } else if (areNoItemsSelectedNorPaid) {
                    totalAmount = cursor.getDouble(columnIndexTotalAmount);
                    TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);
                    totalAmountTextView = view.findViewById(R.id.textViewAmount);
                    String formattedTotalAmount = String.format("%.2f", totalAmount);
                    totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                    Log.e("formattedTotalAmount1" , formattedTotalAmount + " " +formattedTotalAmount);
                } else if
                (areNOItemsSelected) {
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                          String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                        totalAmount =mDatabaseHelper.calculateTotalAmountsNotSelectedNotPaid(latesttransId,roomid,tableid);
                        TaxtotalAmount=mDatabaseHelper.calculateTotalTaxAmountsNotSelectedNotPaid(latesttransId,roomid,tableid);
                        totalAmountTextView = view.findViewById(R.id.textViewAmount);
                        String formattedTotalAmount = String.format("%.2f", totalAmount);
                        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                        Log.e("formattedTotalAmount23" , formattedTotalAmount + " " +formattedTotalAmount);
                    }


                }else {


                        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        roomid = String.valueOf(preferences.getInt("roomnum", 0));
                        tableid = preferences.getString("table_id", "");
                        Log.e("rooms table" , roomid + " " +tableid);
                        double totalsplit= mDatabaseHelper.calculateTotalAmounts(roomid,tableid);
                        totalAmountTextView = view.findViewById(R.id.textViewAmount);
                        String formattedTotalAmount = String.format("%.2f", totalsplit);
                        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                    Log.e("totalsplit" , totalsplit + " " +totalsplit);

                }

            /*    if (allItemsSelected) {

                    totalAmount = cursor.getDouble(columnIndexTotalAmount);
                    TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                    totalAmountTextView = view.findViewById(R.id.textViewAmount);
                    String formattedTotalAmount = String.format("%.2f", totalAmount);
                    totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                } else if (areNOItemsSelected) {
                    totalAmount = cursor.getDouble(columnIndexTotalAmount);
                    TaxtotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);


                    totalAmountTextView = view.findViewById(R.id.textViewAmount);
                    String formattedTotalAmount = String.format("%.2f", totalAmount);
                    totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                } else {
                    SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                    roomid = String.valueOf(preferences.getInt("roomnum", 0));
                    tableid = preferences.getString("table_id", "");
                    Log.e("rooms table" , roomid + " " +tableid);
                    double totalsplit= mDatabaseHelper.calculateTotalAmounts(roomid,tableid);
                    totalAmountTextView = view.findViewById(R.id.textViewAmount);
                    String formattedTotalAmount = String.format("%.2f", totalsplit);
                    totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
                }
*/
            }
        }




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

                if (!amountReceivedString.isEmpty() && !amountReceivedString.equals(".")) {
                    try {
                        amountReceived = Double.parseDouble(amountReceivedString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                double totalAmountEntered = 0.0;
                String amountReceivedText = amountReceivedEditText.getText().toString();
                if (!amountReceivedText.isEmpty() && !amountReceivedText.equals(".")) {
                    totalAmountEntered = Double.parseDouble(amountReceivedText);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                // Call the getSumOfAmount method
                String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);
                boolean isAtLeastOneItemSelected=mDatabaseHelper.isAtLeastOneItemSelected(latesttransId);
                double totalsplit= mDatabaseHelper.calculateTotalAmounts(roomid,tableid);
                double sumOfAmountAmountpaid = mDatabaseHelper.getSumOfAmount(latesttransId, Integer.parseInt(roomid), tableid);
                double sum = 0.0;
                List<DatabaseHelper.SettlementSummary> summaries = mDatabaseHelper.getSettlementSummaries(latesttransId, Integer.parseInt(roomid), tableid);

                // Calculate the subtotal and display payment methods
                if (summaries != null && !summaries.isEmpty()) {

                    StringBuilder paymentMethods = new StringBuilder();

                    for (DatabaseHelper.SettlementSummary summary : summaries) {
                        sum += summary.sum;
                    }
                    sumOfAmountAmountpaid= sum;
                }
                double remainingAmount = totalAmount - sumOfAmountAmountpaid - totalAmountEntered;

                TextView remainingAmountTextView = view.findViewById(R.id.textViewAmountdue);
                TextView remainingTotalAmountTextView = view.findViewById(R.id.textViewTotalAmountdue);


                TextView textViewCashReturn = view.findViewById(R.id.textViewCashReturn);
if(isAtLeastOneItemSelected){
    remainingAmount= totalsplit  - totalAmountEntered-sum;
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
    Log.e("rooms table" , roomid + " " +tableid);
    Log.e("totalsplit1" , totalsplit + " " +totalsplit);
    if (isAtLeastOneItemSelected &&  totalAmountEntered >= totalsplit && isPaymentSplitted()) {
        double cashReturn = totalAmountEntered - totalsplit;
        cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
        Log.d("(sumOfAmountAmountpaid + totalAmountEntered)1", String.valueOf((sumOfAmountAmountpaid + totalAmountEntered)));
        Log.d("(sumOfAmountAmountpaid ", String.valueOf((sumOfAmountAmountpaid)));
        Log.d("(totalAmountEntered ", String.valueOf((totalAmountEntered)));
        Log.d("(totalAmount ", String.valueOf((totalsplit)));
        textViewCashReturnAmount.setVisibility(View.VISIBLE);
        cashReturnTextView.setVisibility(View.VISIBLE);
        validateButton.setVisibility(View.GONE);
    }else  if (!isAtLeastOneItemSelected && (sumOfAmountAmountpaid + totalAmountEntered) >= totalAmount && isPaymentSplitted()) {
        double cashReturn = (sumOfAmountAmountpaid + totalAmountEntered) - totalAmount;
        cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
        Log.d("(sumOfAmountAmountpaid + totalAmountEntered)2", String.valueOf((sumOfAmountAmountpaid + totalAmountEntered)));
        Log.d("(sumOfAmountAmountpaid ", String.valueOf((sumOfAmountAmountpaid)));
        Log.d("(totalAmountEntered ", String.valueOf((totalAmountEntered)));
        Log.d("(totalAmount2 ", String.valueOf((totalAmount)));
        textViewCashReturnAmount.setVisibility(View.VISIBLE);
        cashReturnTextView.setVisibility(View.VISIBLE);
        validateButton.setVisibility(View.GONE);
    } else {
        textViewCashReturnAmount.setVisibility(View.GONE);
        cashReturnTextView.setVisibility(View.GONE);
        validateButton.setVisibility(View.GONE);
        Log.d("(totalAmount3 ", String.valueOf((totalAmount)));
    }
}else{
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
    Log.e("rooms table" , roomid + " " +tableid);
    Log.e("totalsplit1" , totalsplit + " " +totalsplit);
    if (isAtLeastOneItemSelected &&  totalAmountEntered >= totalsplit && isPaymentSplitted()) {
        double cashReturn = totalAmountEntered - totalsplit;
        cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
        Log.d("(sumOfAmountAmountpaid + totalAmountEntered)1", String.valueOf((sumOfAmountAmountpaid + totalAmountEntered)));
        Log.d("(sumOfAmountAmountpaid ", String.valueOf((sumOfAmountAmountpaid)));
        Log.d("(totalAmountEntered ", String.valueOf((totalAmountEntered)));
        Log.d("(totalAmount ", String.valueOf((totalsplit)));
        textViewCashReturnAmount.setVisibility(View.VISIBLE);
        cashReturnTextView.setVisibility(View.VISIBLE);
        validateButton.setVisibility(View.GONE);
    }else  if (!isAtLeastOneItemSelected && (sumOfAmountAmountpaid + totalAmountEntered) >= totalAmount && isPaymentSplitted()) {
        double cashReturn = (sumOfAmountAmountpaid + totalAmountEntered) - totalAmount;
        cashReturnTextView.setText(getString(R.string.currency_symbol) + " " + String.format(Locale.getDefault(), "%.2f", cashReturn));
        Log.d("(sumOfAmountAmountpaid + totalAmountEntered)2", String.valueOf((sumOfAmountAmountpaid + totalAmountEntered)));
        Log.d("(sumOfAmountAmountpaid ", String.valueOf((sumOfAmountAmountpaid)));
        Log.d("(totalAmountEntered ", String.valueOf((totalAmountEntered)));
        Log.d("(totalAmount2 ", String.valueOf((totalAmount)));
        textViewCashReturnAmount.setVisibility(View.VISIBLE);
        cashReturnTextView.setVisibility(View.VISIBLE);
        validateButton.setVisibility(View.GONE);
    } else {
        textViewCashReturnAmount.setVisibility(View.GONE);
        cashReturnTextView.setVisibility(View.GONE);
        validateButton.setVisibility(View.GONE);
        Log.d("(totalAmount3 ", String.valueOf((totalAmount)));
    }
}

            }
        });
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                printData();
            }

            private void printData() {
                String statusType= mDatabaseHelper.getLatestTransactionStatus(roomid,tableid);


                String latesttransId= mDatabaseHelper.getLatestTransactionId(roomid,tableid,statusType);

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
                    Log.d("insertsettlementtest1", settlementItem.getPaymentName() );

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    String currentTime = timeFormat.format(new Date());
                    boolean updated = mDatabaseHelper.insertSettlementAmount(settlementItem.getPaymentName(), settlementItem.getSettlementAmount(), latesttransId, PosNum, currentDate,currentTime, String.valueOf(roomid),tableid);

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
                        values.put(FINANCIAL_COLUMN_TOTALIZER,settlementItem.getPaymentName()); // Update the totalizer



                        db.update(FINANCIAL_TABLE_NAME, values, FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " +
                                FINANCIAL_COLUMN_DATETIME + " = ? AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " +
                                FINANCIAL_COLUMN_POSNUM + " = ?", whereArgs);
                    } else {
                        // If no row with the same payment name and current date exists, insert a new row
                        values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                        values.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Initialize total
                        values.put(FINANCIAL_COLUMN_TOTALIZER, settlementItem.getPaymentName()); // Initialize totalizer

                        db.insert(FINANCIAL_TABLE_NAME, null, values);
                    }
                    if ("Cash".equals(settlementItem.getPaymentName()) ) {
                        ContentValues cashReportValues = new ContentValues();
                        cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                        cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                        cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                        cashReportValues.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Positive cash in amount
                        cashReportValues.put(FINANCIAL_COLUMN_POSNUM, PosNum);
                        cashReportValues.put(FINANCIAL_CashReturn, cashReturn);
                        cashReportValues.put(FINANCIAL_COLUMN_TransId, latesttransId);
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

                cashReturn= totalAmountinserted- totalAmount;



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
                intent.putExtra("roomid", roomid);
                intent.putExtra("tableid", tableid);


                if (cashReturn > 0.0) {



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
                    cashReturnValues.put(FINANCIAL_COLUMN_TOTALIZER, "Cash Return"); // Negate cashReturn

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


                        cashReturnValues.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                        cashReturnValues.put(FINANCIAL_COLUMN_TOTAL, currentTotal + cashReturn); // Update the total
                        cashReturnValues.put(FINANCIAL_COLUMN_TOTALIZER, "Cash Return"); // Update the totalizer
                        cashReturnValues.put(FINANCIAL_COLUMN_SHOP_NUMBER, ShopNumber); // Update the totalizer

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
                    cashReportValues.put(FINANCIAL_CashReturn, cashReturn);
                    cashReportValues.put(FINANCIAL_COLUMN_TransId, latesttransId);
                    db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);
                    startActivity(intent);

                }


                String MRAMETHOD="Single";
                insertCashReturn(String.valueOf(cashReturn), String.valueOf(totalAmountinserted),qrMra,mrairn,MRAMETHOD);
                startActivity(intent);
            }

        });



        Button splitPaymentButton = view.findViewById(R.id.Splitpayment);
        Button fullPaymentButton = view.findViewById(R.id.full_payment);

        // Retrieve the selected payment type from SharedPreferences
        String selectedPaymentType = getSelectedPaymentType();

        // Set button colors based on the selected payment type
        updateButtonColors(selectedPaymentType, splitPaymentButton, fullPaymentButton);

        splitPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the selected payment type to SharedPreferences
                saveSelectedPaymentType("splitted");
                gifImageView.setVisibility(view.GONE);
                gridLayout.setVisibility(view.VISIBLE);
                containerLayout.setVisibility(View.GONE);
                validateButton.setVisibility(View.GONE);
                amountReceivedEditText.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                // Update button colors
                updateButtonColors("splitted", splitPaymentButton, fullPaymentButton);

            }
        });

        fullPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the selected payment type to SharedPreferences
                saveSelectedPaymentType("full");
                mRecyclerView.setVisibility(view.VISIBLE);
                // Full payment: Take the total amount as the value
                gridLayout.setVisibility(view.GONE);
                gifImageView.setVisibility(view.VISIBLE);
                amountReceivedEditText.setVisibility(View.GONE);
                containerLayout.setVisibility(View.GONE);
                validateButton.setVisibility(View.GONE);
                // Update button colors
                updateButtonColors("full", splitPaymentButton, fullPaymentButton);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Checkout")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the positive button click
                        // You can leave it empty if you don't need to do anything on positive click
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the negative button click
                        // You can leave it empty if you don't need to do anything on negative click
                    }
                })
                .create();


    }





    private void handleSplittedPayment(String id) {

        // Get the amount received
        double amountReceived = 0.0;
        if (!amountReceivedEditText.getText().toString().isEmpty()) {
            amountReceived = Double.parseDouble(amountReceivedEditText.getText().toString());
        }
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        // Iterate over the container layout to get the settlement details
        ArrayList<SettlementItem> settlementItems = new ArrayList<>();
        for (int i = 0; i < containerLayout.getChildCount(); i += 2) {
            View childView = containerLayout.getChildAt(i);
            if (childView instanceof TextView) {
                // Use the loop variable i to identify the correct payment details
                PaymentDetails paymentDetails = mDatabaseHelper.getPaymentDetailsById(id);
                String paymentName = paymentDetails.getPaymentName();
                double settlementAmount = amountReceived;
                // Use a specific Locale for date formatting
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String currentDate = dateFormat.format(new Date());
                Log.d("insertsettlementtest2", paymentName );
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String currentTime = timeFormat.format(new Date());
                mDatabaseHelper.insertSettlementAmount(paymentName,settlementAmount, latesttransId, PosNum,currentDate,currentTime, String.valueOf(roomid),tableid);

                settlementItems.add(new SettlementItem(paymentName, settlementAmount));
            }
        }
// Assuming you have a method in your DatabaseHelper to fetch payment details by id
        PaymentDetails paymentDetails = mDatabaseHelper.getPaymentDetailsById(id);

        if (paymentDetails != null) {
            // Assuming PaymentDetails has methods to get paymentName and amount
            String paymentName = paymentDetails.getPaymentName();
            double settlementAmount = amountReceived;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDate = dateFormat.format(new Date());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String currentTime = timeFormat.format(new Date());
            settlementItems.add(new SettlementItem(paymentName, settlementAmount));
            Log.d("insertsettlementtest3", paymentName );

            mDatabaseHelper.insertSettlementAmount(paymentName,settlementAmount, latesttransId, PosNum,currentDate,currentTime, String.valueOf(roomid),tableid);
            Log.d("PaymentDetails2", "Name: " + paymentName + ", Amount: " + settlementAmount+ " " + transactionIdInProgress);

            Log.d("settlementItems", String.valueOf(settlementItems));
        }

        Log.d("id", String.valueOf(id));
        // Take the total amount as the value
        // This logic is the same as before, replace it with your own logic

        double cashReturn= 0.0;

        System.out.println("selectedBuyerTANs: " + BuyTAN);
        // Pass the amount received and settlement items as extras to the print activity

        for (SettlementItem settlementItem : settlementItems) {

            // Use a specific Locale for date formatting
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDate = dateFormat.format(new Date());

            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Use the current date
            values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
            values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, settlementItem.getPaymentName());
            values.put(FINANCIAL_COLUMN_POSNUM, PosNum); // Insert the posnum

            // Check if a row with the same payment name, current date, cashier ID, and posnum already exists and has no shift number
            String[] whereArgs = new String[] {settlementItem.getPaymentName(), currentDate, cashierId, PosNum};
            Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                    FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                            FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ? AND " +
                            TRANSACTION_SHIFT_NUMBER + " IS NULL",
                    whereArgs, null, null, null);

            if (cursor.moveToFirst()) {
                // If a row with the same payment name, current date, cashier ID, and posnum exists and has no shift number, update the values
                int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                values.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                values.put(FINANCIAL_COLUMN_TOTAL, currentTotal + settlementItem.getSettlementAmount()); // Update the total
                values.put(FINANCIAL_COLUMN_TOTALIZER, settlementItem.getPaymentName()); // Update the totalizer

                db.update(FINANCIAL_TABLE_NAME, values, FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " +
                        FINANCIAL_COLUMN_DATETIME + " = ? AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " +
                        FINANCIAL_COLUMN_POSNUM + " = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL", whereArgs);
            } else {
                // If no row with the same payment name and current date exists or has a shift number, insert a new row
                values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                values.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Initialize total
                values.put(FINANCIAL_COLUMN_TOTALIZER, settlementItem.getPaymentName()); // Initialize totalizer
                values.put(FINANCIAL_COLUMN_SHOP_NUMBER, ShopNumber);
                values.put(FINANCIAL_COLUMN_PAYMENT, settlementItem.getPaymentName());
                db.insert(FINANCIAL_TABLE_NAME, null, values);
            }
            cursor.close();

            if ("Cash".equals(settlementItem.getPaymentName())) {
                // Check if a row with the current date, cashier ID, posnum, and no shift number already exists in the cash report table
                String[] cashWhereArgs = new String[] {currentDate, cashierId, PosNum};
                Cursor cashCursor = db.query(CASH_REPORT_TABLE_NAME, null,
                        FINANCIAL_COLUMN_DATETIME + " = ? AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " +
                                FINANCIAL_COLUMN_POSNUM + " = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL",
                        cashWhereArgs, null, null, null);

                if (cashCursor.moveToFirst()) {
                    // If a row exists, update it
                    double currentTotal = cashCursor.getDouble(cashCursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                    ContentValues cashReportValues = new ContentValues();
                    cashReportValues.put(FINANCIAL_COLUMN_TOTAL, currentTotal + settlementItem.getSettlementAmount());
                    cashReportValues.put(FINANCIAL_CashReturn, cashReturn); // Ensure this field is updated as well
                    db.update(CASH_REPORT_TABLE_NAME, cashReportValues, FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                            FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ? AND " +
                            TRANSACTION_SHIFT_NUMBER + " IS NULL", cashWhereArgs);
                } else {
                    // If no row exists, insert a new row
                    ContentValues cashReportValues = new ContentValues();
                    cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                    cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                    cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                    cashReportValues.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Positive cash in amount
                    cashReportValues.put(FINANCIAL_COLUMN_POSNUM, PosNum);
                    cashReportValues.put(FINANCIAL_CashReturn, cashReturn);
                    cashReportValues.put(FINANCIAL_COLUMN_TransId, latesttransId);
                    cashReportValues.put(TRANSACTION_SHIFT_NUMBER, (String) null); // Set shift number as null initially
                    db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);
                }
                cashCursor.close();
            }

            mDatabaseHelper.updateTenderAmount(latesttransId, settlementItem.getSettlementAmount());
        }


        // Notify the listener that an item is added
        if (itemAddedListener != null) {
            itemAddedListener.onItemAdded(String.valueOf(roomid),tableid);
        }
        double totalAmountinserted = 0.0;
        // Iterate over the settlement items to calculate the total amount
        for (SettlementItem item : settlementItems) {
            totalAmountinserted += item.getSettlementAmount();
        }
        double totalsplit= mDatabaseHelper.calculateTotalAmounts(roomid,tableid);
        String MRAMETHOD="Single";
        insertCashReturn(String.valueOf(cashReturn), String.valueOf(totalAmountinserted),qrMra,mrairn,MRAMETHOD);
        boolean isAtLeastOneItemSelected= mDatabaseHelper.isAtLeastOneItemSelected(latesttransId);
        Log.d("cashReturn", "= " + cashReturn);
        Log.d("total", "= " + totalsplit);
        Log.d("isAtLeastOneItemSelected", "= " + isAtLeastOneItemSelected);


        double sum = 0.0;
        List<DatabaseHelper.SettlementSummary> summaries = mDatabaseHelper.getSettlementSummaries(latesttransId, Integer.parseInt(roomid), tableid);

        // Calculate the subtotal and display payment methods
        if (summaries != null && !summaries.isEmpty()) {

            StringBuilder paymentMethods = new StringBuilder();

            for (DatabaseHelper.SettlementSummary summary : summaries) {
                sum += summary.sum;
            }

        }


        if(isAtLeastOneItemSelected) {
            cashReturn= sum - totalsplit;

            Log.d("total", "= " + totalsplit);
            Log.d("sum", "= " + sum);
            Log.d("totalAmountinserted", "= " + totalAmountinserted);
            Log.d("cashReturn", "= " + cashReturn);

            if( cashReturn>=0){
                String newtransid=  generateNewTransactionId();
                Log.d("latesttransIditemsel", String.valueOf(latesttransId));
                Log.d("newtransiditemsel", String.valueOf(newtransid));
                mDatabaseHelper.updateTransactionIdForSelected(latesttransId,newtransid, String.valueOf(roomid),tableid,0);
                // Update the transaction ID in the header table for transactions with status "InProgress"
                SendToHeader(totalsplit, mDatabaseHelper.calculateTax(totalAmount,"VAT 15%"),newtransid);

                mDatabaseHelper.updateSettlementTransactionId(latesttransId,newtransid,roomid,tableid);
                Intent intent = new Intent(getActivity(), Mra.class);
                intent.putExtra("amount_received", String.valueOf(totalsplit));
                intent.putExtra("cash_return", String.valueOf(cashReturn));
                intent.putExtra("settlement_items", settlementItems);
                intent.putExtra("id", newtransid);
                intent.putExtra("selectedBuyerName", Buyname);
                intent.putExtra("selectedBuyerTAN", BuyTAN);
                intent.putExtra("selectedBuyerCompanyName", BuyComp);
                intent.putExtra("selectedBuyerType", BuyType);
                intent.putExtra("selectedBuyerBRN", BuyBRN);
                intent.putExtra("selectedBuyerNIC", BuyNIC);
                intent.putExtra("selectedBuyerAddresse", BuyAdd);
                intent.putExtra("selectedBuyerprofile", BuyProfile);
                intent.putExtra("roomid", roomid);
                intent.putExtra("tableid", tableid);
                System.out.println("selectedBuyerTANs: " + BuyTAN);
                startActivity(intent);
                mDatabaseHelper.updatePaidStatusForSelectedRowsById(latesttransId);
            }


        }else {
            cashReturn= sum - totalAmount;

            Log.d("total", "= " + totalsplit);
            Log.d("sum", "= " + sum);
            Log.d("totalAmountinserted", "= " + totalAmountinserted);
            Log.d("cashReturn", "= " + cashReturn);
            if( cashReturn>=0){

                Log.d("latesttransIditemsel", String.valueOf(latesttransId));

                // Update the transaction ID in the header table for transactions with status "InProgress"
                SendToHeader(totalAmount, mDatabaseHelper.calculateTax(totalAmount,"VAT 15%"),latesttransId);

                mDatabaseHelper.updateSettlementTransactionId(latesttransId,latesttransId,roomid,tableid);
                Intent intent = new Intent(getActivity(), Mra.class);
                intent.putExtra("amount_received", String.valueOf(totalsplit));
                intent.putExtra("cash_return", String.valueOf(cashReturn));
                intent.putExtra("settlement_items", settlementItems);
                intent.putExtra("id", latesttransId);
                intent.putExtra("selectedBuyerName", Buyname);
                intent.putExtra("selectedBuyerTAN", BuyTAN);
                intent.putExtra("selectedBuyerCompanyName", BuyComp);
                intent.putExtra("selectedBuyerType", BuyType);
                intent.putExtra("selectedBuyerBRN", BuyBRN);
                intent.putExtra("selectedBuyerNIC", BuyNIC);
                intent.putExtra("selectedBuyerAddresse", BuyAdd);
                intent.putExtra("selectedBuyerprofile", BuyProfile);
                intent.putExtra("roomid", roomid);
                intent.putExtra("tableid", tableid);
                System.out.println("selectedBuyerTANs: " + BuyTAN);
                startActivity(intent);
                mDatabaseHelper.updatePaidStatusForSelectedRowsById(latesttransId);
            }
        }

    }

    private void SendToHeader(double totalAmount, double taxtotalAmount,String newtransid) {

            transactionIdInProgress=newtransid;
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
    private void showNumberOfCoversDialog(final OnCoversInputListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        listener.onCoversInput(numberOfCovers);
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

    // Listener interface to handle the input
    interface OnCoversInputListener {
        void onCoversInput(int numberOfCovers);
    }

    public String generateNewTransactionId() {
        // Retrieve the last used counter value from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("TransactionCounter", Context.MODE_PRIVATE);
        int lastCounter = sharedPreferences.getInt("counter", 1);
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);

        String ShopName = sharedPreference.getString("ShopName", null);
        // Increment the counter for the next transaction
        int currentCounter = lastCounter + 1;

        // Save the updated counter value in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("counter", currentCounter);
        editor.apply();

        // Extract the first three letters from companyName
        String companyLetters = ShopName.substring(0, Math.min(ShopName.length(), 3)).toUpperCase();

        String posNumberLetters = null;
        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
        if (cursorCompany != null && cursorCompany.moveToFirst()) {
            int columnCompanyNameIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_POS_Num);
            PosNum= cursorCompany.getString(columnCompanyNameIndex);
            posNumberLetters = PosNum.substring(0, Math.min(PosNum.length(), 3)).toUpperCase();

        }
        // Generate the transaction ID by combining the three letters and the counter
        return companyLetters + "-" + posNumberLetters + "-" + currentCounter;
    }
    private void handleFullPayment(String id) {
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
if (totalAmount==0){
     totalAmount= mDatabaseHelper.calculateTotalAmounts(roomid,tableid);
}

        double sumOfAmountAmountpaid = mDatabaseHelper.getSumOfAmount(latesttransId, Integer.parseInt(roomid), tableid);

        double remainingAmount = totalAmount - sumOfAmountAmountpaid;
        if(sumOfAmountAmountpaid==0){
            remainingAmount=totalAmount;
        }else{
            remainingAmount=totalAmount - sumOfAmountAmountpaid;
        }

        Log.d("sumOfAmountAmountpaid", String.valueOf(sumOfAmountAmountpaid));
        Log.d("totalAmount", String.valueOf(totalAmount));
        Log.d("remainingAmount", String.valueOf(remainingAmount));
        // Iterate over the container layout to get the settlement details
        ArrayList<SettlementItem> settlementItems = new ArrayList<>();
        for (int i = 0; i < containerLayout.getChildCount(); i += 2) {
            View childView = containerLayout.getChildAt(i);
            if (childView instanceof TextView) {
                // Use the loop variable i to identify the correct payment details
                PaymentDetails paymentDetails = mDatabaseHelper.getPaymentDetailsById(id);
                String paymentName = paymentDetails.getPaymentName();
                double settlementAmount = remainingAmount;
                // Use a specific Locale for date formatting
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String currentDate = dateFormat.format(new Date());
                Log.d("insertsettlementtest2", paymentName );
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String currentTime = timeFormat.format(new Date());
                mDatabaseHelper.insertSettlementAmount(paymentName,settlementAmount, latesttransId, PosNum,currentDate,currentTime, String.valueOf(roomid),tableid);

                settlementItems.add(new SettlementItem(paymentName, settlementAmount));
            }
        }
// Assuming you have a method in your DatabaseHelper to fetch payment details by id
        PaymentDetails paymentDetails = mDatabaseHelper.getPaymentDetailsById(id);

        if (paymentDetails != null) {
            // Assuming PaymentDetails has methods to get paymentName and amount
            String paymentName = paymentDetails.getPaymentName();
            double settlementAmount = remainingAmount;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDate = dateFormat.format(new Date());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String currentTime = timeFormat.format(new Date());
            settlementItems.add(new SettlementItem(paymentName, settlementAmount));
            Log.d("insertsettlementtest3", paymentName );

            mDatabaseHelper.insertSettlementAmount(paymentName,settlementAmount, latesttransId, PosNum,currentDate,currentTime, String.valueOf(roomid),tableid);
            Log.d("PaymentDetails2", "Name: " + paymentName + ", Amount: " + settlementAmount+ " " + transactionIdInProgress);

            Log.d("settlementItems", String.valueOf(settlementItems));
        }

        Log.d("id", String.valueOf(id));
        // Take the total amount as the value
        // This logic is the same as before, replace it with your own logic

        double cashReturn= 0.0;

  // Update the table with settlement details
        for (SettlementItem settlementItem : settlementItems) {

            // Use a specific Locale for date formatting
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDate = dateFormat.format(new Date());

            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Use the current date
            values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
            values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, settlementItem.getPaymentName());
            values.put(FINANCIAL_COLUMN_POSNUM, PosNum); // Insert the posnum

            // Check if a row with the same payment name, current date, cashier ID, and posnum already exists and has no shift number
            String[] whereArgs = new String[] {settlementItem.getPaymentName(), currentDate, cashierId, PosNum};
            Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                    FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                            FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ? AND " +
                            TRANSACTION_SHIFT_NUMBER + " IS NULL",
                    whereArgs, null, null, null);

            if (cursor.moveToFirst()) {
                // If a row with the same payment name, current date, cashier ID, and posnum exists and has no shift number, update the values
                int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                values.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                values.put(FINANCIAL_COLUMN_TOTAL, currentTotal + settlementItem.getSettlementAmount()); // Update the total
                values.put(FINANCIAL_COLUMN_TOTALIZER, settlementItem.getPaymentName()); // Update the totalizer

                db.update(FINANCIAL_TABLE_NAME, values, FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " +
                        FINANCIAL_COLUMN_DATETIME + " = ? AND " + FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " +
                        FINANCIAL_COLUMN_POSNUM + " = ? AND " + TRANSACTION_SHIFT_NUMBER + " IS NULL", whereArgs);
            } else {
                // If no row with the same payment name and current date exists or has a shift number, insert a new row
                values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                values.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Initialize total
                values.put(FINANCIAL_COLUMN_TOTALIZER, settlementItem.getPaymentName()); // Initialize totalizer
                values.put(FINANCIAL_COLUMN_SHOP_NUMBER, ShopNumber);
                values.put(FINANCIAL_COLUMN_PAYMENT, settlementItem.getPaymentName());
                db.insert(FINANCIAL_TABLE_NAME, null, values);
            }
            cursor.close();

            if ("Cash".equals(settlementItem.getPaymentName())) {
                // Insert a new row into the cash report table
                ContentValues cashReportValues = new ContentValues();
                cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                cashReportValues.put(FINANCIAL_COLUMN_TOTAL, settlementItem.getSettlementAmount()); // Positive cash in amount
                cashReportValues.put(FINANCIAL_COLUMN_POSNUM, PosNum);
                cashReportValues.put(FINANCIAL_CashReturn, cashReturn);
                cashReportValues.put(FINANCIAL_COLUMN_TransId, latesttransId);
                cashReportValues.put(TRANSACTION_SHIFT_NUMBER, (String) null); // Set shift number as null initially

                db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);
            }
        }



        String MRAMETHOD="Single";
        insertCashReturn(String.valueOf(cashReturn), String.valueOf(remainingAmount),qrMra,mrairn,MRAMETHOD);
        Log.d("latesttransId", String.valueOf(latesttransId));
        boolean isAtLeastOneItemSelected= mDatabaseHelper.isAtLeastOneItemSelected(latesttransId);
        if(isAtLeastOneItemSelected && cashReturn >= 0) {
            String newtransid=  generateNewTransactionId();
            Log.d("latesttransId", String.valueOf(latesttransId));
            Log.d("newtransid", String.valueOf(newtransid));
            mDatabaseHelper.updateTransactionIdForSelected(latesttransId,newtransid, String.valueOf(roomid),tableid,0);
            // Update the transaction ID in the header table for transactions with status "InProgress"
            SendToHeader(remainingAmount, mDatabaseHelper.calculateTax(remainingAmount,"VAT 15%"),newtransid);

            mDatabaseHelper.updateSettlementTransactionId(latesttransId,newtransid,roomid,tableid);
            Intent intent = new Intent(getActivity(), Mra.class);
            intent.putExtra("amount_received", String.valueOf(remainingAmount));
            intent.putExtra("cash_return", String.valueOf(cashReturn));
            intent.putExtra("settlement_items", settlementItems);
            intent.putExtra("id", newtransid);
            intent.putExtra("selectedBuyerName", Buyname);
            intent.putExtra("selectedBuyerTAN", BuyTAN);
            intent.putExtra("selectedBuyerCompanyName", BuyComp);
            intent.putExtra("selectedBuyerType", BuyType);
            intent.putExtra("selectedBuyerBRN", BuyBRN);
            intent.putExtra("selectedBuyerNIC", BuyNIC);
            intent.putExtra("selectedBuyerAddresse", BuyAdd);
            intent.putExtra("selectedBuyerprofile", BuyProfile);
            intent.putExtra("roomid", roomid);
            intent.putExtra("tableid", tableid);
            System.out.println("selectedBuyerTANs: " + BuyTAN);
            startActivity(intent);

        }else{
            Intent intent = new Intent(getActivity(), Mra.class);
            intent.putExtra("amount_received", String.valueOf(remainingAmount));
            intent.putExtra("cash_return", String.valueOf(cashReturn));
            intent.putExtra("settlement_items", settlementItems);
            intent.putExtra("id", latesttransId);
            intent.putExtra("selectedBuyerName", Buyname);
            intent.putExtra("selectedBuyerTAN", BuyTAN);
            intent.putExtra("selectedBuyerCompanyName", BuyComp);
            intent.putExtra("selectedBuyerType", BuyType);
            intent.putExtra("selectedBuyerBRN", BuyBRN);
            intent.putExtra("selectedBuyerNIC", BuyNIC);
            intent.putExtra("selectedBuyerAddresse", BuyAdd);
            intent.putExtra("selectedBuyerprofile", BuyProfile);
            intent.putExtra("roomid", roomid);
            intent.putExtra("tableid", tableid);
            System.out.println("selectedBuyerTANs: " + BuyTAN);
            startActivity(intent);
            mDatabaseHelper.updatePaidStatusForSelectedRowsById(latesttransId);
        }
    }


    private boolean isPaymentSplitted() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String paymentType = prefs.getString(PAYMENT_TYPE_KEY, "");
        return "splitted".equals(paymentType);
    }
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    private String getSelectedPaymentType() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PAYMENT_TYPE_KEY, "full"); // Default to "full" if not found
    }

    private void saveSelectedPaymentType(String paymentType) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PAYMENT_TYPE_KEY, paymentType);
        editor.apply();
    }

    private void updateButtonColors(String selectedPaymentType, Button splitButton, Button fullButton) {
        if ("splitted".equals(selectedPaymentType)) {
            // Set colors for split payment button
            splitButton.setBackgroundColor(getResources().getColor(R.color.BleuAccessaText));
            fullButton.setBackgroundColor(Color.GRAY);
        } else {
            // Set colors for full payment button
            splitButton.setBackgroundColor(Color.GRAY);
            fullButton.setBackgroundColor(getResources().getColor(R.color.BleuAccessaText));
        }
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
                    Cursor cursor = mDatabaseHelper.getTransactionHeader(roomid,tableid);
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

    private void setButtonClickListeners() {
        int[] buttonIds = new int[] {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonComma, R.id.buttonbackspace,
                R.id.buttonClear
        };

        for (int id : buttonIds) {
            Button button = view.findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNumberButtonClick((Button) v);
                }
            });
        }
    }

    public void onNumberButtonClick(Button button) {
        if (amountReceivedEditText != null) {
            String buttonText = button.getText().toString();

            switch (buttonText) {
                case "BS": // Handle backspace
                    CharSequence currentText = amountReceivedEditText.getText();
                    if (currentText.length() > 0) {
                        amountReceivedEditText.setText(currentText.subSequence(0, currentText.length() - 1));
                    }

                    break;
                case "CL/": // Handle clear
                    amountReceivedEditText.setText("");
                    break;
                default: // Handle numbers and comma
                    amountReceivedEditText.append(buttonText);
                    break;
            }
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
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
                    Cursor cursor = mDatabaseHelper.getTransactionHeader(roomid,tableid);
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
            Cursor cursor = mDatabaseHelper.getTransactionHeader(roomid,tableid);
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
    transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(roomid,tableid);
    Log.d("tr1", String.valueOf(transactionIdInProgress));
     mDatabaseHelper.insertcashReturn(cashReturn,totalAmountinserted, transactionIdInProgress,qrMra,mrairn,MRAMETHOD);

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


    private void bindWoyouService() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private void unbindWoyouService() {
        if (woyouService != null) {
            requireActivity().unbindService(connService);
            woyouService = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bindWoyouService();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindWoyouService();
    }
    private void displayQROnLCD(String code, String name) {
        if (woyouService == null) {
            //   Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name == null) {
            // Handle the case where name is null
            Log.e("displayQROnLCD", "Name is null");
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



    public void onClearButtonClick(EditText ReceivedEditText) {

        onclearButtonClick(amountReceivedEditText);
       // onclearButtonClick(ReceivedEditText);


    }

    private void onclearButtonClick(EditText ReceivedEditText) {

        if (amountReceivedEditText != null) {
            // Insert the letter into the EditText
            amountReceivedEditText.setText("");
           // ReceivedEditText.setText("");
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    public void oncommentButtonClick(String letter) {
        if (amountReceivedEditText != null) {
            // Get the current text from the EditText
            String currentText = amountReceivedEditText.getText().toString();

            // Check if the current text is empty and the input letter is a decimal point
            if (currentText.isEmpty() && letter.equals(".")) {
                amountReceivedEditText.setText("0" + letter);
            } else {
                // Append the letter as usual
                amountReceivedEditText.append(letter);
            }

            // Move the cursor to the end of the text
            amountReceivedEditText.setSelection(amountReceivedEditText.getText().length());
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    public void onNumberButtonClick(View view, String number) {

        if (amountReceivedEditText != null) {
            // Insert the letter into the EditText
            amountReceivedEditText.append(number);
            //ReceivedEditText.append(letter);
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }

    }
    private void removeEditText() {
        if (amountReceivedEditText != null) {
            containerLayout.removeView(amountReceivedEditText);
            amountReceivedEditText = null; // Clear the reference to the EditText
        }

        // Hide the layouts if needed
        gridLayout.setVisibility(View.GONE);
        containerLayout.setVisibility(View.GONE);

    }


    private void onBackspaceButtonClick(EditText text ) {
        // Get the current text from editTextOption1
        Editable editable = text.getText();

        // Get the length of the text
        int length = editable.length();

        // If there are characters in the text, remove the last character
        if (length > 0) {
            editable.delete(length - 1, length);
        }
    }
    private void showNumberOfPeopleDialog() {
        // Example: Show a dialog to get the number of people
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View numberPickerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_number_picker, null);
        builder.setView(numberPickerView);

        final NumberPicker numberPicker = numberPickerView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30); // Adjust the maximum value as needed

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int numberOfPeople = numberPicker.getValue();
                // Handle the selected number of people
                Toast.makeText(getActivity(), "Number of People: " + numberOfPeople, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
            }
        });

        builder.create().show();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SalesFragment.ItemAddedListener) {
            itemAddedListener = (SalesFragment.ItemAddedListener) context;
        }

    }
}
