package com.accessa.ibora.Functions;

import static com.accessa.ibora.product.items.DatabaseHelper.CASH_REPORT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_DATETIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_POSNUM;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTAL;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTALIZER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TRANSACTION_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_TABLE_NAME;
import static com.accessa.ibora.sales.Sales.SalesFragment.mRecyclerView;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import com.accessa.ibora.Constants;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.Tables.TableAdapter;
import com.accessa.ibora.sales.keyboard.CustomEditText;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class FunctionFragment extends Fragment {
    private AlertDialog alertDialog;
    private SearchView searchView;
    private StringBuilder enteredBarcode;

    private List<EditText> chequeEditTexts = new ArrayList<>();
    private CustomEditText editTextBarcode;
    private SQLiteDatabase database;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private TextWatcher textWatcher;
    private TableAdapter mAdapter;
    int roomid;
    String tableid;
    private IWoyouService woyouService;
    private String cashierId,cashierLevel,shopname,posNum;
    String dbName = Constants.DB_NAME;

    TextView totalTextView = null;
    TextView totalizerTextView =null ;
    TextView differenceTextView ;
    double total;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;


        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);

        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierLevel = sharedPreference.getString("cashorlevel", null);
        shopname = sharedPreference.getString("ShopName", null);

        SharedPreferences sharedPreferencepos = requireContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        posNum = sharedPreferencepos.getString("posNumber", null);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        String currentDate = getCurrentDate();
        double totalizerSum = dbHelper.getTotalizerSumForCurrentDate(currentDate);

        // Check the value and set the content view accordingly
        if ("mobile".equalsIgnoreCase(deviceType)) {
            view = inflater.inflate(R.layout.mobile_keyboard_fragment, container, false);
        } else if ("sunmiT2".equalsIgnoreCase(deviceType)) {
             view = inflater.inflate(R.layout.functions_fragment, container, false);
        }
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = Integer.parseInt(String.valueOf(preferences.getInt("roomnum", 0)));
        tableid = String.valueOf(preferences.getInt("table_id", 0));
        Cursor roomCursor = dbHelper.getAllTables1(String.valueOf(roomid));
        mAdapter = new TableAdapter(getActivity(), roomCursor);

        // Find the number buttons and set OnClickListener
        CardView buttonloan = view.findViewById(R.id.buttonLoan);
        CardView buttoncashin= view.findViewById(R.id.buttonCashin);
        CardView buttonpickup = view.findViewById(R.id.buttonPickUp);
        CardView buttoncount = view.findViewById(R.id.buttonCount);
        CardView buttondrawer = view.findViewById(R.id.buttonopendrawer);
        CardView buttonpricelevel = view.findViewById(R.id.buttonpricelevel);
        CardView buttonsearchTable = view.findViewById(R.id.buttonSearchTable);


        buttonsearchTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTableSearchPopup(v);
            }
        });
        buttonpricelevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to show the coin count popup
                showPriceLevelDialog();
            }
        });
        buttoncount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to show the coin count popup
                showCoinCountPopup(v);
            }
        });

        buttonpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt(cashierLevel);

                if (level >= 5) {

                    try {
                        woyouService.openDrawer(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    // Inflate the custom dialog layout
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.pickup_dialog, null);

                    // Create the dialog
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogBuilder.setView(dialogView);

                    // Find views in the custom dialog layout
                    EditText pickupAmountEditText = dialogView.findViewById(R.id.loanAmountEditText);
                    Button confirmButton = dialogView.findViewById(R.id.confirmButton);
                    Button cancelButton = dialogView.findViewById(R.id.cancelButton);




                    // Create and show the dialog
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    // Handle confirm button click
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the pickup amount and posnum from the EditTexts
                            String pickup = pickupAmountEditText.getText().toString();

                            // Check if the pickup amount and posnum are not empty
                            if (!pickup.isEmpty() && !posNum.isEmpty()) {
                                // Here, you can handle the pickup amount and posnum

                                // Make the pickup amount negative
                                double negativePickupAmount = -Double.parseDouble(pickup);

                                // Update the FINANCIAL_TABLE with the pickup details
                                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues values = new ContentValues();

                                // Use a specific Locale for date formatting
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String currentDate = dateFormat.format(new Date());

                                values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Insert only the date
                                values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId); // Use the current cashier ID
                                values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Pick Up");
                                values.put(FINANCIAL_COLUMN_POSNUM, posNum); // Insert the posnum

                                // Check if a row with the same transaction code ("Pick Up"), date, cashier ID, and posnum already exists
                                String[] whereArgs = new String[] {"Pick Up", currentDate, cashierId, posNum};
                                Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                                        FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                        whereArgs, null, null, null);

                                if (cursor.moveToFirst()) {
                                    // If a row with the same transaction code, date, cashier ID, and posnum exists, update the values
                                    int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                                    double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                                    double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                                    values.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                                    values.put(FINANCIAL_COLUMN_TOTAL, currentTotal + negativePickupAmount); // Update the total
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, currentTotalizer + negativePickupAmount); // Update the totalizer

                                    db.update(FINANCIAL_TABLE_NAME, values,
                                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                            whereArgs);
                                } else {
                                    // If no row with the same transaction code, date, cashier ID, and posnum exists, insert a new row
                                    values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                                    values.put(FINANCIAL_COLUMN_TOTAL, negativePickupAmount); // Initialize total as the negative pickup amount
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, negativePickupAmount); // Initialize totalizer as the negative pickup amount

                                    db.insert(FINANCIAL_TABLE_NAME, null, values);
                                }

                                // Insert the same data into the CashReports table with a negative value
                                ContentValues cashReportValues = new ContentValues();
                                cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                                cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                                cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                                cashReportValues.put(FINANCIAL_COLUMN_TOTAL, negativePickupAmount); // Negative pickup amount
                                cashReportValues.put(FINANCIAL_COLUMN_POSNUM, posNum);

                                db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);

                                // Open the drawer (add your drawer opening code here)

                                // Close the dialog
                                dialog.dismiss();
                            } else {
                                // Inform the user that the pickup amount and posnum are required
                                Toast.makeText(getContext(), "Please enter a Pick up amount.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    // Handle cancel button click
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close the dialog
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Not allowed to perform this action.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttoncashin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt(cashierLevel);

                if (level >= 5) {

                    try {
                        woyouService.openDrawer(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    // Inflate the custom dialog layout
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.cashin_dialog, null);

                    // Create the dialog
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogBuilder.setView(dialogView);

                    // Find views in the custom dialog layout
                    EditText CashinAmountEditText = dialogView.findViewById(R.id.CashinAmountEditText);
                    Button confirmButton = dialogView.findViewById(R.id.confirmButton);
                    Button cancelButton = dialogView.findViewById(R.id.cancelButton);

                    // Create and show the dialog
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    // Handle confirm button click
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the cash in amount from the EditText
                            String cashinAmount = CashinAmountEditText.getText().toString();

                            // Check if the cash in amount is not empty
                            if (!cashinAmount.isEmpty() && !posNum.isEmpty()) {
                                // Here, you can handle the cash in amount and posNum

                                // Update the FINANCIAL_TABLE with the cash in details
                                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues values = new ContentValues();

                                // Use a specific Locale for date formatting
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String currentDate = dateFormat.format(new Date());

                                values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Insert only the date
                                values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId); // Use the current cashier ID
                                values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Cash In");
                                values.put(FINANCIAL_COLUMN_POSNUM, posNum); // Insert the posNum

                                // Check if a row with the same transaction code ("Cash In"), date, cashier ID, and posNum already exists
                                String[] whereArgs = new String[] {"Cash In", currentDate, cashierId, posNum};
                                Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                                        FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                        whereArgs, null, null, null);

                                if (cursor.moveToFirst()) {
                                    // If a row with the same transaction code, date, cashier ID, and posNum exists, update the values
                                    int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                                    double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                                    double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                                    values.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                                    values.put(FINANCIAL_COLUMN_TOTAL, currentTotal + Double.parseDouble(cashinAmount)); // Update the total
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, currentTotalizer + Double.parseDouble(cashinAmount)); // Update the totalizer

                                    db.update(FINANCIAL_TABLE_NAME, values,
                                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                            whereArgs);
                                } else {
                                    // If no row with the same transaction code, date, cashier ID, and posNum exists, insert a new row
                                    values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                                    values.put(FINANCIAL_COLUMN_TOTAL, Double.parseDouble(cashinAmount)); // Initialize total
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, Double.parseDouble(cashinAmount)); // Initialize totalizer

                                    db.insert(FINANCIAL_TABLE_NAME, null, values);
                                }

                                // Insert the same data into the CashReports table with a positive value
                                ContentValues cashReportValues = new ContentValues();
                                cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                                cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                                cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                                cashReportValues.put(FINANCIAL_COLUMN_TOTAL, Double.parseDouble(cashinAmount)); // Positive cash in amount
                                cashReportValues.put(FINANCIAL_COLUMN_POSNUM, posNum);

                                db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);

                                // Open the drawer (add your drawer opening code here)

                                // Close the dialog
                                dialog.dismiss();
                            } else {
                                // Inform the user that the cash in amount and posNum are required
                                Toast.makeText(getContext(), "Please enter a Cash In amount.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Handle cancel button click
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close the dialog
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Not allowed to perform this action.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt(cashierLevel);

                if (level >= 5) {

                    try {
                        woyouService.openDrawer(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    // Inflate the custom dialog layout
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.loan_dialog, null);

                    // Create the dialog
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogBuilder.setView(dialogView);

                    // Find views in the custom dialog layout
                    EditText loanAmountEditText = dialogView.findViewById(R.id.loanAmountEditText);
                    Button confirmButton = dialogView.findViewById(R.id.confirmButton);
                    Button cancelButton = dialogView.findViewById(R.id.cancelButton);

                    // Create and show the dialog
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    // Handle confirm button click
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the loan amount and posnum from the EditTexts
                            String loanAmount = loanAmountEditText.getText().toString();

                            // Check if the loan amount and posnum are not empty
                            if (!loanAmount.isEmpty() && !posNum.isEmpty()) {
                                // Update the FINANCIAL_TABLE with the loan details
                                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues values = new ContentValues();

                                // Use a specific Locale for date formatting
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String currentDate = dateFormat.format(new Date());

                                values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Insert only the date
                                values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId); // Use the current cashier ID
                                values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Loan");
                                values.put(FINANCIAL_COLUMN_POSNUM, posNum); // Insert the posnum

                                // Check if a row with the same transaction code ("Loan"), date, cashier ID, and posnum already exists
                                String[] whereArgs = new String[] {"Loan", currentDate, cashierId, posNum};
                                Cursor cursor = db.query(FINANCIAL_TABLE_NAME, null,
                                        FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                        whereArgs, null, null, null);

                                if (cursor.moveToFirst()) {
                                    // If a row with the same transaction code, date, cashier ID, and posnum exists, update the values
                                    int currentQuantity = cursor.getInt(cursor.getColumnIndex(FINANCIAL_COLUMN_QUANTITY));
                                    double currentTotal = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTAL));
                                    double currentTotalizer = cursor.getDouble(cursor.getColumnIndex(FINANCIAL_COLUMN_TOTALIZER));

                                    values.put(FINANCIAL_COLUMN_QUANTITY, currentQuantity + 1); // Increment the quantity
                                    values.put(FINANCIAL_COLUMN_TOTAL, currentTotal + Double.parseDouble(loanAmount)); // Update the total
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, currentTotalizer + Double.parseDouble(loanAmount)); // Update the totalizer

                                    db.update(FINANCIAL_TABLE_NAME, values,
                                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                            whereArgs);
                                } else {
                                    // If no row with the same transaction code, date, cashier ID, and posnum exists, insert a new row
                                    values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                                    values.put(FINANCIAL_COLUMN_TOTAL, Double.parseDouble(loanAmount)); // Initialize total
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, Double.parseDouble(loanAmount)); // Initialize totalizer

                                    db.insert(FINANCIAL_TABLE_NAME, null, values);
                                }
                                // Insert the same data into the CashReports table with a positive value
                                ContentValues cashReportValues = new ContentValues();
                                cashReportValues.put(FINANCIAL_COLUMN_DATETIME, currentDate);
                                cashReportValues.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId);
                                cashReportValues.put(FINANCIAL_COLUMN_QUANTITY, 1); // Quantity is always 1 for cash reports
                                cashReportValues.put(FINANCIAL_COLUMN_TOTAL, Double.parseDouble(loanAmount)); // Positive cash in amount
                                cashReportValues.put(FINANCIAL_COLUMN_POSNUM, posNum);

                                db.insert(CASH_REPORT_TABLE_NAME, null, cashReportValues);

                                // Open the drawer (add your drawer opening code here)

                                // Close the dialog
                                dialog.dismiss();
                            } else {
                                // Inform the user that the cash in amount and posNum are required
                                Toast.makeText(getContext(), "Please enter a Cash In amount.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Handle cancel button click
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close the dialog
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Not allowed to perform this action.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttondrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level= Integer.parseInt(cashierLevel);
                if(level >= 5){
                    try {
                        woyouService.openDrawer(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
                }
            }

        });


        return view;
    }


    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    private void showTableSearchPopup(View anchorView) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.searchtable, null);

        EditText searchEditText = popupView.findViewById(R.id.popup_searchEditText);
        Button okButton = popupView.findViewById(R.id.popup_okButton);
        Button cancelButton = popupView.findViewById(R.id.popup_cancelButton);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTableName = searchEditText.getText().toString().trim();
                popupWindow.dismiss();

                // Call a method in TablesFragment to handle the insert and filter
                ((MainActivity) requireActivity()).filterAndInsertTable(newTableName);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(anchorView);
    }



    private void handleTableSearch(String searchQuery) {
        // Implement the logic to search tables based on the provided query
        // You can use the searchQuery to filter the tables in your RecyclerView or perform any other action.
        // Update the RecyclerView adapter accordingly.
        // For example, you can call filterRecyclerView(searchQuery) method or update the query directly in your RecyclerView adapter.
        filterRecyclerView(searchQuery);
    }
    private void filterRecyclerView(String searchQuery) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        // Use the modified searchTables() method to fetch filtered tables data
        Cursor filteredCursor = dbHelper.searchTables(String.valueOf(roomid), searchQuery);
        mAdapter.swapCursor(filteredCursor);

        // Check if the dataset is empty and show/hide the empty state accordingly
        showEmptyState(filteredCursor.getCount() <= 0);
    }
    private void showEmptyState(boolean showEmpty) {
        AppCompatImageView imageView = getView().findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);
        FrameLayout emptyFrameLayout = getView().findViewById(R.id.empty_frame_layout);
        if (showEmpty) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
    }

    public void showCoinCountPopup(View view) {
        // Inflate the pop-up layout
        View popupView = getLayoutInflater().inflate(R.layout.count_dialog, null);

        // Create the pop-up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(popupView);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        String currentDate = getCurrentDate();
        double totalizerSum = dbHelper.getTotalizerSumForCurrentDate(currentDate);

        // Show the pop-up dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Find the total EditText in your count dialog layout
         totalTextView = popupView.findViewById(R.id.totalTextView);
         totalizerTextView = popupView.findViewById(R.id.totalizerTotalTextView);
         differenceTextView = popupView.findViewById(R.id.differenceTextView);
        Button saveButton = popupView.findViewById(R.id.saveButton);

        // Find all the EditText fields for coin denominations
        EditText editText1Cent = popupView.findViewById(R.id.editText1Cent);
        EditText editText5Cents = popupView.findViewById(R.id.editText5Cents);
        EditText editText10Cents = popupView.findViewById(R.id.editText10Cents);
        EditText editText20Cents = popupView.findViewById(R.id.editText20Cents);
        EditText editText50Cents = popupView.findViewById(R.id.editText50Cents);
        EditText editText1RupeeCoin = popupView.findViewById(R.id.editText1RupeeCoin);
        EditText editText5RupeesCoin = popupView.findViewById(R.id.editText5RupeesCoin);
        EditText editText10RupeesCoin = popupView.findViewById(R.id.editText10RupeesCoin);
        EditText editText20RupeesCoin = popupView.findViewById(R.id.editText20RupeesCoin);
        EditText editText25RupeesNote = popupView.findViewById(R.id.editText25RupeesNote);
        EditText editText50RupeesNote = popupView.findViewById(R.id.editText50RupeesNote);
        EditText editText100RupeesNote = popupView.findViewById(R.id.editText100RupeesNote);
        EditText editText200RupeesNote = popupView.findViewById(R.id.editText200RupeesNote);
        EditText editText500RupeesNote = popupView.findViewById(R.id.editText500RupeesNote);
        EditText editText1000RupeesNote = popupView.findViewById(R.id.editText1000RupeesNote);
        EditText editText2000RupeesNote = popupView.findViewById(R.id.editText2000RupeesNote);
        EditText newChequeEditText = new EditText(getContext());


// In your showCoinCountPopup method:
        Button addChequeButton = popupView.findViewById(R.id.addChequeButton);
        addChequeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new EditText for the cheque

                newChequeEditText.setHint("Cheque");
                newChequeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                // Add the new cheque EditText to the layout
                LinearLayout chequesLayout = popupView.findViewById(R.id.chequesLayout);
                chequesLayout.addView(newChequeEditText);

                // Add the new cheque EditText to the list for tracking
                chequeEditTexts.add(newChequeEditText);

            }
        });

        // Set a TextWatcher to calculate the total dynamically
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Calculate the total value
                 total = 0.0;
                  total += getEditTextValueAsDouble(editText1Cent) * 0.01; // 1 cent = 0.01
                total += getEditTextValueAsDouble(editText5Cents) * 0.05; // 5 cents = 0.05
                total += getEditTextValueAsDouble(editText10Cents) * 0.10; // 10 cents = 0.10
                total += getEditTextValueAsDouble(editText20Cents) * 0.20; // 20 cents = 0.20
                total += getEditTextValueAsDouble(editText50Cents) * 0.50; // 50 cents = 0.50
                total += getEditTextValueAsDouble(editText1RupeeCoin) * 1.00; // 1 Rupee = 1.00
                total += getEditTextValueAsDouble(editText5RupeesCoin) * 5.00; // 5 Rupees = 5.00
                total += getEditTextValueAsDouble(editText10RupeesCoin) * 10.00; // 10 Rupees = 10.00
                total += getEditTextValueAsDouble(editText20RupeesCoin) * 20.00; // 20 Rupees = 20.00
                total += getEditTextValueAsDouble(editText25RupeesNote) * 25.00; // 25 Rupees Note = 25.00
                total += getEditTextValueAsDouble(editText50RupeesNote) * 50.00; // 50 Rupees Note = 50.00
                total += getEditTextValueAsDouble(editText100RupeesNote) * 100.00; // 100 Rupees Note = 100.00
                total += getEditTextValueAsDouble(editText200RupeesNote) * 200.00; // 200 Rupees Note = 200.00
                total += getEditTextValueAsDouble(editText500RupeesNote) * 500.00; // 500 Rupees Note = 500.00
                total += getEditTextValueAsDouble(editText1000RupeesNote) * 1000.00; // 1000 Rupees Note = 1000.00
                total += getEditTextValueAsDouble(editText2000RupeesNote) * 2000.00; // 2000 Rupees Note = 2000.00


// Add the value of each cheque to the total
                for (EditText chequeEditText : chequeEditTexts) {
                    total += getEditTextValueAsDouble(chequeEditText);
                }

                // Update the total EditText with the calculated total
// Update the total TextView with the calculated total

                totalTextView.setText("Total: Rs " + String.format("%.2f", total));




                // Update the new total TextView with the calculated total from the totalizer
                totalizerTextView.setText("Totalizer Total: Rs " + String.format("%.2f", totalizerSum));
                // Calculate the difference
                double difference =   total - totalizerSum;
                double totalval=total;

                differenceTextView.setText("Difference: Rs " + difference);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Inside the onClick method of your "Save" button's OnClickListener:

                        // Initialize your database helper.
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());  // 'this' should be your activity or context


                        int cashierIds = Integer.parseInt(cashierId); // Replace with the actual cashier ID or retrieve it from your app's logic.

// Insert data into the database
                        dbHelper.insertCountingReport(totalizerSum, totalval, difference, cashierIds);
// Display a toast message
                        Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear all the EditText fields
                        editText1Cent.getText().clear();
                        editText5Cents.getText().clear();
                        editText10Cents.getText().clear();
                        editText20Cents.getText().clear();
                        editText50Cents.getText().clear();
                        editText1RupeeCoin.getText().clear();
                        editText5RupeesCoin.getText().clear();
                        editText10RupeesCoin.getText().clear();
                        editText20RupeesCoin.getText().clear();
                        editText25RupeesNote.getText().clear();
                        editText50RupeesNote.getText().clear();
                        editText100RupeesNote.getText().clear();
                        editText200RupeesNote.getText().clear();
                        editText500RupeesNote.getText().clear();
                        editText1000RupeesNote.getText().clear();
                        editText2000RupeesNote.getText().clear();

                        // Clear the new cheque EditText fields
                        for (EditText chequeEditText : chequeEditTexts) {
                            chequeEditText.getText().clear();
                        }

                        // Clear the total and difference TextViews
                        totalTextView.setText("Total: Rs 0.00");
                        differenceTextView.setText("Difference: Rs 0.00");
                    }
                });
            }
        };



        // Attach the TextWatcher to all coin denomination EditText fields
        editText1Cent.addTextChangedListener(textWatcher);
        editText5Cents.addTextChangedListener(textWatcher);
        editText10Cents.addTextChangedListener(textWatcher);
        editText20Cents.addTextChangedListener(textWatcher);
        editText50Cents.addTextChangedListener(textWatcher);
        editText1RupeeCoin.addTextChangedListener(textWatcher);
        editText5RupeesCoin.addTextChangedListener(textWatcher);
        editText10RupeesCoin.addTextChangedListener(textWatcher);
        editText20RupeesCoin.addTextChangedListener(textWatcher);
        editText25RupeesNote.addTextChangedListener(textWatcher);
        editText50RupeesNote.addTextChangedListener(textWatcher);
        editText100RupeesNote.addTextChangedListener(textWatcher);
        editText200RupeesNote.addTextChangedListener(textWatcher);
        editText500RupeesNote.addTextChangedListener(textWatcher);
        editText1000RupeesNote.addTextChangedListener(textWatcher);
        editText2000RupeesNote.addTextChangedListener(textWatcher);
        newChequeEditText.addTextChangedListener(textWatcher);
    }

    // Helper method to convert EditText value to a double
    private double getEditTextValueAsDouble(EditText editText) {
        try {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                return Double.parseDouble(text);
            }
        } catch (NumberFormatException e) {
            // Handle the case where the text is not a valid number
        }
        return 0.0; // Return 0 if the text is empty or not a valid number
    }

    public void showPriceLevelDialog() {
        // Define the options
        final String[] priceLevels = {"Price Level 1", "Price Level 2", "Price Level 3"};

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

        SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        String buyerName = sharedPreferences2.getString("BuyerName", null);

        if (transactionIdInProgress == null || transactionIdInProgress.isEmpty()) {
            if (buyerName == null || buyerName.isEmpty()) {
                // Both conditions are met (no transaction in progress and no selected buyer), so allow changing price level.

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select a Price Level")
                        .setItems(priceLevels, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle the selected price level
                                String selectedPriceLevel = priceLevels[which];

                                if (selectedPriceLevel != null && !selectedPriceLevel.isEmpty()) {
                                    SharedPreferences sharedPreferencesPriceLevel = requireContext().getSharedPreferences("pricelevel", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferencesPriceLevel.edit();
                                    editor.putString("selectedPriceLevel", selectedPriceLevel);
                                    editor.apply();

                                    // Show a new dialog with a message
                                    AlertDialog.Builder messageBuilder = new AlertDialog.Builder(requireContext());
                                    messageBuilder.setTitle("Message")
                                            .setMessage("Price level selected: " + selectedPriceLevel)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Navigate to MainActivity when OK is clicked
                                                    Intent intent = new Intent(requireContext(), MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                    messageBuilder.create().show();
                                }
                            }
                        });
                builder.create().show();
            } else {
                // If a buyer is selected, show a message to handle this case.
                // Transaction is in progress, show a message to complete the transaction or update individual items.
                AlertDialog.Builder transactionBuilder = new AlertDialog.Builder(requireContext());
                transactionBuilder.setTitle("Buyer  Selected")
                        .setMessage("You must remove active Buyer  before changing the price level.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle what to do when the user clicks "OK" in the incomplete transaction dialog.
                                // This might involve taking the user to a transaction screen or item price update screen.
                            }
                        });
                transactionBuilder.create().show();
            }
        } else {
            // Transaction is in progress, show a message to complete the transaction or update individual items.
            AlertDialog.Builder transactionBuilder = new AlertDialog.Builder(requireContext());
            transactionBuilder.setTitle("Incomplete Transaction")
                    .setMessage("You must complete the transaction or update individual item prices before changing the price level.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle what to do when the user clicks "OK" in the incomplete transaction dialog.
                            // This might involve taking the user to a transaction screen or item price update screen.
                        }
                    });
            transactionBuilder.create().show();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // Dismiss the dialog if it is showing to prevent window leaks
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        // Close the database connection when the fragment is destroyed
        if (database != null) {
            database.close();
        }
    }
}
