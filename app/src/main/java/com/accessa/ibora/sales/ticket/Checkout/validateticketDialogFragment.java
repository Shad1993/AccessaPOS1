package com.accessa.ibora.sales.ticket.Checkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.POP.PopMobileDialogFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class validateticketDialogFragment extends DialogFragment  {

    private DBManager Xdatabasemanager;
    private RecyclerView mRecyclerView;
    private CheckoutGridAdapter mAdapter;
    private static final int QR_CODE_REQUEST_CODE = 1;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private static final String ARG_Transaction_id = "transactionId";
    private static final String POSNumber="posNumber";
    private String qrMra,Transaction_Id,PosNum,amountpaid,pop;
    private String cashierId;
    private double cashReturn;
    private Set<String> selectedItems; // Store selected items
    private TextView IDselectedQRTextView;
    private double totalAmount, TaxtotalAmount;
    private DatabaseHelper mDatabaseHelper;
    private LinearLayout containerLayout; // Added
    private  Button validateButton;
    private static final String amounts = "amount";
    private static final String popFragment = "popfragment";
    private static final String MRAQR="mraqr";

    private View view; // Declare the view variable

    private String receivedQRCode; // Declare a member variable



    // You can create a public method to get the stored QR code string


    public static validateticketDialogFragment newInstance(String transactionId, String amount, String popFraction, String result) {
        validateticketDialogFragment fragment = new validateticketDialogFragment();
        Bundle args = new Bundle();

        args.putString(ARG_Transaction_id, transactionId);
        args.putString(amounts, amount);
        args.putString(popFragment, popFraction);
        args.putString(MRAQR, result);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedItems = new HashSet<>(); // Initialize the set

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);
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
                    showPopOptionsDialog(); // Call the showPopOptionsDialog() method here for "POP" button click

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
            qrMra=args.getString(MRAQR);
            Log.d("MRAqr",qrMra);

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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String transactionDate = dateFormat.format(new Date()); // Replace 'new Date()' with your actual transaction date

                    boolean updated = mDatabaseHelper.insertSettlementAmount(settlementItem.getPaymentName(), settlementItem.getSettlementAmount(), Transaction_Id, PosNum,transactionDate);

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
                Intent intent = new Intent(getActivity(), printerSetup.class);
                intent.putExtra("amount_received", totalAmountinserted);
                intent.putExtra("cash_return", cashReturn);
                intent.putExtra("settlement_items", settlementItems);
                intent.putExtra("mraQR", qrMra);
                insertCashReturn(cashReturn,totalAmountinserted,qrMra);
                startActivity(intent);
            }

        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
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
                    EditText editCompanyName = dialogmob.findViewById(R.id.editCompanyName);
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

                        // User selected "Pay by QR code"
                        // Perform the action for this option
                        // For example, open a QR code scanner activity
                        showSecondaryScreen("POP","00020101021126630009mu.maucas0112BKONMUM0XXXX021103011065958031500000000000005252047278530348054071927.035802MU5912IntermartOne6015Agalega North I622202112305786280707031436304BE4F");

                }
            }
        });
        builder.show();
    }
    private void showSecondaryScreen(String name, String QR) {
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
            Toast.makeText(getActivity(), "Secondary screen not found or not supported", Toast.LENGTH_SHORT).show();
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


public void  insertCashReturn(double cashReturn, double totalAmountinserted, String qrMra){

    // Insert the cash return value to the header table
    boolean cashReturnUpdated = mDatabaseHelper.insertcashReturn(cashReturn,totalAmountinserted, Transaction_Id,qrMra);
    if (cashReturnUpdated) {
        Toast.makeText(getActivity(), "Cash return inserted: " + cashReturn, Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(getActivity(), "Failed to insert cash return amount: " + cashReturn, Toast.LENGTH_SHORT).show();
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
