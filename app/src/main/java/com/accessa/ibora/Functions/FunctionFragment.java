package com.accessa.ibora.Functions;

import static com.accessa.ibora.product.items.DatabaseHelper.CASH_REPORT_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CASHOR_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_CURRENT_TIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_DATETIME;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_PAYMENT;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_POSNUM;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_SHOP_NUMBER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTAL;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TOTALIZER;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_COLUMN_TRANSACTION_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.FINANCIAL_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_TTC;
import static com.accessa.ibora.sales.Sales.SalesFragment.mRecyclerView;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import com.accessa.ibora.Constants;
import com.accessa.ibora.ItemsReport.DataModel;
import com.accessa.ibora.ItemsReport.ItemSummary;
import com.accessa.ibora.ItemsReport.PaymentMethodAdapter;
import com.accessa.ibora.ItemsReport.PaymentMethodDataModel;
import com.accessa.ibora.ItemsReport.SalesReportAdapter;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Report.ReportActivity;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.printer.CloseShiftReport;

import com.accessa.ibora.printer.PrintShiftReport;
import com.accessa.ibora.printer.externalprinterlibrary2.printerSetupforPRF;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.printer.printerSetupForPickUp;
import com.accessa.ibora.product.Rooms.Room;
import com.accessa.ibora.product.items.AddItemActivity;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.Tables.TableAdapter;
import com.accessa.ibora.sales.Tables.TableRoomAdapter;
import com.accessa.ibora.sales.Tables.TablesFragment;
import com.accessa.ibora.sales.keyboard.CustomEditText;
import com.accessa.ibora.sales.ticket.Checkout.Cashier;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class FunctionFragment extends Fragment {
    private AlertDialog alertDialog;
    private SearchView searchView;
    EditText editTextOption1;
    private String selectedTableToMerge;
    private SalesFragment.ItemAddedListener itemAddedListener;
    private OnTableClickListener tableClickListener;
    private  List<String> data ;
    private TablesFragment.OnReloadFragmentListener reloadListener;
    private StringBuilder enteredBarcode;
    private DatabaseHelper mDatabaseHelper;
    private List<EditText> chequeEditTexts = new ArrayList<>();
    private CustomEditText editTextBarcode;
    private SQLiteDatabase database;
    private String transactionIdInProgress;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private TextWatcher textWatcher;
    private TableAdapter mAdapter;
    private TableRoomAdapter mRoomAdapter;
    int roomid;

    private Spinner spinnerReportType,cashiorspinnerReportType,spinnershiftnum;
    private RecyclerView recyclerViewReports;
    private RecyclerView secondRecyclerView;
    private SalesReportAdapter reportAdapter;
    private PaymentMethodAdapter paymentMethodAdapter;
    private String selectedTableToTransfer;
    private SharedPreferences usersharedPreferences;
    String tableid;
    private IWoyouService woyouService;
    private String cashierId,cashierLevel,shopname,posNum,shopId;
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
        usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);

        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierLevel = sharedPreference.getString("cashorlevel", null);
        shopname = sharedPreference.getString("ShopName", null);
        shopId = sharedPreference.getString("ShopId", null);

        SharedPreferences sharedPreferencepos = requireContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        posNum = sharedPreferencepos.getString("posNumber", null);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        String currentDate = getCurrentDate();
        double totalizerSum = dbHelper.getTotalizerSumForCurrentDate(currentDate);

        // Check the value and set the content view accordingly
        if ("mobile".equalsIgnoreCase(deviceType)) {
            view = inflater.inflate(R.layout.mobile_keyboard_fragment, container, false);
        } else if ("sunmit2".equalsIgnoreCase(deviceType)) {
             view = inflater.inflate(R.layout.functions_fragment, container, false);
        }
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = Integer.parseInt(String.valueOf(preferences.getInt("roomnum", 0)));
        tableid = String.valueOf(preferences.getString("table_id", "0"));
        Cursor roomCursor = dbHelper.getAllTables1(String.valueOf(roomid));
        mAdapter = new TableAdapter(getActivity(), roomCursor);
        mRoomAdapter= new TableRoomAdapter(getActivity(), roomCursor);
        // Find the number buttons and set OnClickListener
        CardView buttonloan = view.findViewById(R.id.buttonLoan);
        CardView buttoncashin= view.findViewById(R.id.buttonCashin);
        CardView buttonpickup = view.findViewById(R.id.buttonPickUp);
        CardView buttoncount = view.findViewById(R.id.buttonCount);
        CardView buttondrawer = view.findViewById(R.id.buttonopendrawer);
        CardView buttonpricelevel = view.findViewById(R.id.buttonpricelevel);
        CardView buttonsearchTable = view.findViewById(R.id.buttonSearchTable);
        CardView buttonShiftNumber = view.findViewById(R.id.btnSetShiftNumber);
        CardView selecttabletNumber = view.findViewById(R.id.btnselectroomtable);

        boolean hasInProgressOrPRF=mDatabaseHelper.hasInProgressOrPRF();
        selecttabletNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelNumber = Integer.parseInt(cashierLevel);
                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "selectTable_", levelNumber)) {
                    // Call the method to show the coin count popup
                    showroomTablesPopup(v);
                }else{
                    Toast.makeText(getContext(), "Not allowed to perform this action.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        buttonShiftNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelNumber = Integer.parseInt(cashierLevel);
                if(hasInProgressOrPRF){
                    Toast.makeText(getContext(), getText(R.string.pleaseclosetransacts), Toast.LENGTH_SHORT).show();

                }else   if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "setShiftNumber_", levelNumber)) {

                    // Create a dialog with a custom layout
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    LayoutInflater inflater = requireActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.close_shift_report_popup, null);
                    builder.setView(dialogView);
                    Button extraButton = dialogView.findViewById(R.id.extraButton);
                    Button closeShift = dialogView.findViewById(R.id.closeShift);
                    spinnerReportType = dialogView.findViewById(R.id.spinnerReportType);
                    spinnershiftnum = dialogView.findViewById(R.id.spinnerShiftnumber);
                    recyclerViewReports = dialogView.findViewById(R.id.recyclerViewSalesReport);
                    secondRecyclerView = dialogView.findViewById(R.id.secondRecyclerView);
                    // Update TextViews
                    TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                    TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);
                    TextView textViewnodata= dialogView.findViewById(R.id.textViewnodata);
                    // Initialize the database helper
                    mDatabaseHelper = new DatabaseHelper(requireContext());

                    // Populate spinner with report types (daily, weekly, monthly, yearly)
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                            R.array.report_types, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerReportType.setAdapter(adapter);


                    // Fetch shift numbers for today
                    List<Integer> shiftNumbers = mDatabaseHelper.getShiftNumbersForToday();

                    // Assume shiftNumbers is a List<Integer> that you populated from the database
                    if ( shiftNumbers.isEmpty()) {
                        Log.d("shiftNumbers", "shiftNumbers: " + shiftNumbers);
                        // Handle the case where no data is populated
                         spinnerReportType.setVisibility(View.GONE);
                                spinnershiftnum.setVisibility(View.GONE);
                                recyclerViewReports.setVisibility(View.GONE);
                                secondRecyclerView.setVisibility(View.GONE);
                                textViewTotalTax.setVisibility(View.GONE);
                                textViewTotalAmount.setVisibility(View.GONE);
                                extraButton.setVisibility(View.GONE);
                                closeShift.setVisibility(View.GONE);
                                textViewnodata.setVisibility(View.VISIBLE);



                        } else if (shiftNumbers.size() == 1 && shiftNumbers.get(0) == 0) {
                        Log.d("shiftNumbersnull", "shiftNumbers: " + shiftNumbers);
                        spinnerReportType.setVisibility(View.GONE);
                        spinnershiftnum.setVisibility(View.GONE);
                        recyclerViewReports.setVisibility(View.GONE);
                        secondRecyclerView.setVisibility(View.GONE);
                        textViewTotalTax.setVisibility(View.GONE);
                        textViewTotalAmount.setVisibility(View.GONE);
                        extraButton.setVisibility(View.GONE);
                        closeShift.setVisibility(View.VISIBLE);
                        textViewnodata.setVisibility(View.GONE);
                    } else {

                        Log.d("shiftNumbers", "shiftNumbers: " + shiftNumbers);
                        // Populate the spinner with shift numbers
                        ArrayAdapter<Integer> adaptershiftnum = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, shiftNumbers);
                        adaptershiftnum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnershiftnum.setAdapter(adaptershiftnum);
                        spinnershiftnum.setEnabled(true);
                    }

                    // Set up RecyclerViews and their adapters
                    setUpRecyclerViews(dialogView);

                    // Variables to hold the selected report type, formatted total tax, and formatted total amount
                    final String[] selectedReportType = new String[1];
                    final String[] formattedTotalTax = new String[1];
                    final String[] formattedTotalamountwithouVat = new String[1];
                    final String[] formattedTotalAmount = new String[1];
                    final String[] selectedshift = new String[1];




                    // Set the spinner's item selected listener
                    spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            selectedReportType[0] = spinnerReportType.getSelectedItem().toString();

                            // Log the selected report type and shift number for debugging
                            Log.d("ReportPopupDialog", "Selected Report Type: " + selectedReportType[0]);
                            Log.d("ReportPopupDialog", "Selected Shift Number: " + selectedshift[0] );
                            List<DataModel> newDataList ;
                            List<PaymentMethodDataModel>newDataLists;
                            double totalTax=0;
                            double totalAmount=0;
                            double totalAmountwithoutvat= 0;
                            if( selectedshift[0]== null){
                                selectedshift[0] ="0";

                                 }
                            // Fetch data based on the selected report type and shift number
                            newDataList = fetchDataBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));
                            newDataLists = fetchPaymentMethodDataBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));

                            // Log the fetched data for debugging
                            Log.d("ReportPopupDialog", "Fetched Data: " + newDataList.toString());

                            // Update the adapters with the new data
                            reportAdapter.updateData(newDataList);
                            paymentMethodAdapter.updateData(newDataLists);

                            // Assuming you have methods to fetch total tax and total amount based on report type and shift number
                            totalTax = mDatabaseHelper.getTotalTaxBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));
                            totalAmountwithoutvat = mDatabaseHelper.getTotalAmountBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));
                             totalAmount = totalAmountwithoutvat + totalTax;


                            // Format totalTax and totalAmount to display only two decimal places
                            formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                            formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", totalAmount);
                            formattedTotalamountwithouVat[0] = String.format(Locale.getDefault(), "%.2f", totalAmountwithoutvat);

                            textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                            textViewTotalAmount.setText("Total Amount: Rs " + formattedTotalAmount[0]);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Do nothing here
                        }
                    });
                    spinnershiftnum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // Get the selected report type and shift number


                            selectedReportType[0] = spinnerReportType.getSelectedItem().toString();
                            selectedshift[0] = spinnershiftnum.getSelectedItem().toString();
                            // Log the selected report type and shift number for debugging
                            Log.d("ReportPopupDialog", "Selected Report Type: " + selectedReportType[0]);
                            Log.d("ReportPopupDialog", "Selected Shift Number: " + selectedshift[0] );

                            // Fetch data based on the selected report type and shift number
                            List<DataModel> newDataList = fetchDataBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));
                            List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));

                            // Log the fetched data for debugging
                            Log.d("ReportPopupDialog", "Fetched Data: " + newDataList.toString());

                            // Update the adapters with the new data
                            reportAdapter.updateData(newDataList);
                            paymentMethodAdapter.updateData(newDataLists);

                            // Assuming you have methods to fetch total tax and total amount based on report type and shift number
                            double totalTax = mDatabaseHelper.getTotalTaxBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));
                            double totalAmountwithoutvat = mDatabaseHelper.getTotalAmountBasedOnReportTypeAndShift(selectedReportType[0], Integer.parseInt(selectedshift[0]));
                            double totalAmount = totalAmountwithoutvat + totalTax;

                            // Update TextViews
                            TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                            TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);


                            // Format totalTax and totalAmount to display only two decimal places
                            formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                            formattedTotalamountwithouVat[0] = String.format(Locale.getDefault(), "%.2f", totalAmountwithoutvat);
                            formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", totalAmount);
                            textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                            textViewTotalAmount.setText("Total Amount: Rs " + formattedTotalAmount[0]);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Do nothing here
                        }
                    });
                    builder.setTitle("Close/Shift Report")
                            .setPositiveButton("OK", (dialog, which) -> {

                            })
                            .setNegativeButton("Cancel", null) // Optional: Add a cancel button
                            .show();


                    extraButton.setOnClickListener(s -> {
                        Configuration configuration = getResources().getConfiguration();
                        Locale currentLocale = configuration.locale;

                            // Start the CloseShiftReport activity
                            Intent intent = new Intent(getContext(), PrintShiftReport.class);
                            intent.putExtra("locale", currentLocale.toString());
                            intent.putExtra("reportType", selectedReportType[0]);
                            intent.putExtra("shiftNumber", selectedshift[0]);
                            intent.putExtra("reportType", selectedReportType[0]);
                            intent.putExtra("totalTax", formattedTotalTax[0]);
                            intent.putExtra("totalAmount", formattedTotalamountwithouVat[0]);
                            startActivity(intent);

                    });


                    closeShift.setOnClickListener(s -> {
                        Configuration configuration = getResources().getConfiguration();
                        Locale currentLocale = configuration.locale;
                        if(selectedshift[0] =="0"){
                            Toast.makeText(getContext(), R.string.notransactionstarted, Toast.LENGTH_SHORT).show();
                        }else {

                            mDatabaseHelper.setShiftNumberInHeader();
                            int actualshift=mDatabaseHelper.getactualShiftNumber();
                            int newshiftnumber= mDatabaseHelper.getLastSetShiftNumber();
                            Log.d("actualshift", String.valueOf(actualshift));
                            Log.d("newshiftnumber", String.valueOf(newshiftnumber));

                            mDatabaseHelper.setShiftNumberInFinancialTable(newshiftnumber);
                            mDatabaseHelper.setShiftNumberInCountingReportTable(newshiftnumber);
                            mDatabaseHelper.setShiftNumberInCashReportTable(newshiftnumber);

                            // Start the CloseShiftReport activity
                            Intent intent = new Intent(getContext(), CloseShiftReport.class);
                            intent.putExtra("reportType", "Daily");
                            intent.putExtra("shiftNumber", selectedshift[0]);
                            intent.putExtra("reportType", selectedReportType[0]);
                            intent.putExtra("totalTax", formattedTotalTax[0]);
                            intent.putExtra("totalAmount", formattedTotalamountwithouVat[0]);
                            startActivity(intent);
                        }
                    });
                    MssqlDataSync mssqlDataSync = new MssqlDataSync();
                    mssqlDataSync.syncTransactionsFromSQLiteToMSSQL(requireContext());
                    mssqlDataSync.syncTransactionHeaderFromMSSQLToSQLite(requireContext());
                    mssqlDataSync.syncInvoiceSettlementFromMSSQLToSQLite(requireContext());
                    mssqlDataSync.syncCountingReportDataFromSQLiteToMSSQL(requireContext());
                    mssqlDataSync.syncCashReportDataFromSQLiteToMSSQL(requireContext());
                    mssqlDataSync.syncFinancialReportDataFromSQLiteToMSSQL(requireContext());
                }else{
                    Toast.makeText(getContext(), "Not allowed to perform this action.", Toast.LENGTH_SHORT).show();

                }


            }
        });

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

                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
                                int minute = calendar.get(Calendar.MINUTE);
                                int second = calendar.get(Calendar.SECOND);

                                // Format the time
                                String formattedTime = String.format("%02d:%02d:%02d", hour, minute, second);

                                values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Insert only the date
                                values.put(FINANCIAL_COLUMN_CURRENT_TIME, formattedTime);
                                values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId); // Use the current cashier ID
                                values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Pick Up");
                                values.put(FINANCIAL_COLUMN_POSNUM, posNum); // Insert the posnum
                                values.put(FINANCIAL_COLUMN_TOTALIZER, "Pick Up"); // Update the totalizer
                                values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer
                                values.put(FINANCIAL_COLUMN_SHOP_NUMBER, shopId); // Update the totalizer

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
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, "Pick Up"); // Update the totalizer
                                    values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer

                                    db.update(FINANCIAL_TABLE_NAME, values,
                                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                            whereArgs);
                                } else {
                                    // If no row with the same transaction code, date, cashier ID, and posnum exists, insert a new row
                                    values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                                    values.put(FINANCIAL_COLUMN_TOTAL, negativePickupAmount); // Initialize total as the negative pickup amount
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, "Pick Up"); // Update the totalizer
                                    values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer

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
                                Intent intent = new Intent(getActivity(), printerSetupForPickUp.class);
                                intent.putExtra("type", "Pick Up");
                                intent.putExtra("amount", negativePickupAmount);
                                intent.putExtra("date", currentDate);
                                intent.putExtra("time", formattedTime);
                                intent.putExtra("shopnum", shopId);
                                Log.d("amount1", String.valueOf(negativePickupAmount));

                                startActivity(intent);
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
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
                                int minute = calendar.get(Calendar.MINUTE);
                                int second = calendar.get(Calendar.SECOND);

                                // Format the time
                                String formattedTime = String.format("%02d:%02d:%02d", hour, minute, second);

                                values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Insert only the date
                                values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId); // Use the current cashier ID
                                values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Cash In");
                                values.put(FINANCIAL_COLUMN_POSNUM, posNum); // Insert the posNum
                                values.put(FINANCIAL_COLUMN_TOTALIZER, "Cash In"); // Update the totalizer
                                values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer
                                values.put(FINANCIAL_COLUMN_SHOP_NUMBER, shopId); // Update the totalizer

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
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, "Cash In"); // Update the totalizer
                                    values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer

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
                                Intent intent = new Intent(getActivity(), printerSetupForPickUp.class);
                                intent.putExtra("type", "Cash In");
                                intent.putExtra("amount", Double.parseDouble(cashinAmount));
                                intent.putExtra("date", currentDate);
                                intent.putExtra("time", formattedTime);
                                intent.putExtra("shopnum", shopId);
                                Log.d("amount1",cashinAmount);
                                startActivity(intent);
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
                int levelNumber = Integer.parseInt(cashierLevel);

                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "openClose_", levelNumber)) {


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

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String currentDate = dateFormat.format(new Date());
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
                                int minute = calendar.get(Calendar.MINUTE);
                                int second = calendar.get(Calendar.SECOND);

                                // Format the time
                                String formattedTime = String.format("%02d:%02d:%02d", hour, minute, second);

                                values.put(FINANCIAL_COLUMN_DATETIME, currentDate); // Insert only the date
                                values.put(FINANCIAL_COLUMN_CURRENT_TIME, formattedTime); // Insert only the time
                                values.put(FINANCIAL_COLUMN_CASHOR_ID, cashierId); // Use the current cashier ID
                                values.put(FINANCIAL_COLUMN_TRANSACTION_CODE, "Loan");
                                values.put(FINANCIAL_COLUMN_POSNUM, posNum); // Insert the posnum
                                values.put(FINANCIAL_COLUMN_TOTALIZER, "Loan"); // Update the totalizer
                                values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer
                                values.put(FINANCIAL_COLUMN_SHOP_NUMBER, shopId); // Update the totalizer

// Check if a row with the same transaction code ("Loan"), date, cashier ID, and posnum already exists
                                String[] whereArgs = new String[] {"Loan", currentDate, String.valueOf(cashierId), String.valueOf(posNum)};
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
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, "Loan"); // Update the totalizer
                                    values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer

                                    db.update(FINANCIAL_TABLE_NAME, values,
                                            FINANCIAL_COLUMN_TRANSACTION_CODE + " = ? AND " + FINANCIAL_COLUMN_DATETIME + " = ? AND " +
                                                    FINANCIAL_COLUMN_CASHOR_ID + " = ? AND " + FINANCIAL_COLUMN_POSNUM + " = ?",
                                            whereArgs);
                                } else {
                                    // If no row with the same transaction code, date, cashier ID, and posnum exists, insert a new row
                                    values.put(FINANCIAL_COLUMN_QUANTITY, 1); // Initialize quantity to 1 for a new entry
                                    values.put(FINANCIAL_COLUMN_TOTAL, Double.parseDouble(loanAmount)); // Initialize total
                                    values.put(FINANCIAL_COLUMN_TOTALIZER, "Loan"); // Update the totalizer
                                    values.put(FINANCIAL_COLUMN_PAYMENT, "Cash"); // Update the totalizer

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

                                Intent intent = new Intent(getActivity(), printerSetupForPickUp.class);
                                intent.putExtra("type", "Loan");
                                intent.putExtra("amount", Double.parseDouble(loanAmount));
                                intent.putExtra("date", currentDate);
                                intent.putExtra("time", formattedTime);
                                intent.putExtra("shopnum", shopId);
                                Log.d("amount1",loanAmount);
                                startActivity(intent);
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
                int levelNumber= Integer.parseInt(cashierLevel);
                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "openDrawer_", levelNumber)) {
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
        // Inflate the popup layout
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.searchtable, null);

        // Initialize UI components
         editTextOption1 = popupView.findViewById(R.id.popup_tableEditText);
        Button okButton = popupView.findViewById(R.id.popup_okButton);
        Button cancelButton = popupView.findViewById(R.id.popup_cancelButton);
// Find the number buttons and set OnClickListener
        Button button1 = popupView.findViewById(R.id.button1);
        Button button2 = popupView.findViewById(R.id.button2);
        Button button3 = popupView.findViewById(R.id.button3);
        Button button4 = popupView.findViewById(R.id.button4);
        Button button5 = popupView.findViewById(R.id.button5);
        Button button6 = popupView.findViewById(R.id.button6);
        Button button7 = popupView.findViewById(R.id.button7);
        Button button8 = popupView.findViewById(R.id.button8);
        Button button9 = popupView.findViewById(R.id.button9);
        Button button0 = popupView.findViewById(R.id.button0);
        Button buttonbackspace = popupView.findViewById(R.id.buttonbackspace);
        Button buttonClear = popupView.findViewById(R.id.buttonClear);

        Button buttonSpace = popupView.findViewById(R.id.buttonInsertspace);

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



        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }        });





        // Initialize the PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

// Center the PopupWindow on the screen
        popupWindow.showAtLocation(anchorView.getRootView(), Gravity.CENTER, 0, 0);


        // Fetch the original table data to display in RecyclerView initially
        filterRecyclerView("");  // Empty query shows all tables initially

        // Attach a TextWatcher to the EditText to listen for search input
        editTextOption1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the filter based on the search query
                handleTableSearch(s.toString());

                String selectedTableName = editTextOption1.getText().toString();
                //popupWindow.dismiss();

                // Call a method in MainActivity to handle the insert and filter
                ((MainActivity) requireActivity()).filterAndInsertTable(selectedTableName);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTableName = editTextOption1.getText().toString();
                popupWindow.dismiss();

                // Call a method in MainActivity to handle the insert and filter
                ((MainActivity) requireActivity()).filterAndInsertTable(selectedTableName);
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
        // Ensure that the fragment's view is not null
        if (getView() == null) {
            return;  // Avoid NullPointerException if the fragment's view is not initialized
        }

        AppCompatImageView imageView = getView().findViewById(R.id.empty_image_view);
        FrameLayout emptyFrameLayout = getView().findViewById(R.id.empty_frame_layout);

        // Check if imageView and emptyFrameLayout are properly initialized
        if (imageView != null && emptyFrameLayout != null) {
            // Use Glide to load the GIF
            Glide.with(getContext())
                    .asGif()
                    .load(R.drawable.folderwalk)
                    .into(imageView);

            // Show or hide the empty state based on the argument
            if (showEmpty) {
                mRecyclerView.setVisibility(View.GONE);
                emptyFrameLayout.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyFrameLayout.setVisibility(View.GONE);
            }
        } else {
            // Handle the case where one or more views are not properly initialized
            Log.e("showEmptyState", "ImageView or Empty Frame Layout is null");
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
    public void showroomTablesPopup(View view) {
        // Inflate the pop-up layout
        final View popupView = getLayoutInflater().inflate(R.layout.popuproom_table, null);

        // Create the pop-up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(popupView);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // Get all room names and IDs in a list
        List<Room> roomList = dbHelper.getAllRoomnum();

        // Find the Spinner in the popup layout
        Spinner roomSpinner = popupView.findViewById(R.id.room_spinner);

        // Create an ArrayAdapter using the Room list
        ArrayAdapter<Room> roomAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item, // Use spinner item layout
                roomList
        );
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(roomAdapter); // Set adapter to Spinner
// Declare the dialog variable here for accessibility
        AlertDialog dialog = builder.create();
        // Set listener for room selection
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get selected room
                Room selectedRoom = (Room) parentView.getItemAtPosition(position);
                int selectedRoomId = selectedRoom.getId(); // Get the room ID
                Log.d("selectedRoomId", "Selected Room ID: " + selectedRoomId);

                // Query tables filtered by the selected room
                Cursor tablesCursor = dbHelper.getRoomTablesFilteredByMerged(String.valueOf(selectedRoomId));

                RecyclerView tableRecyclerView = popupView.findViewById(R.id.recycler_view); // RecyclerView in layout

                int numberOfColumns = 4; // Specify the number of columns you want
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
                tableRecyclerView.setLayoutManager(gridLayoutManager);
                tableRecyclerView.setAdapter(mRoomAdapter); // Set adapter for tables
                tableRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getContext(), tableRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView idTextView = view.findViewById(R.id.Buyerid);
                                TextView subjectEditText = view.findViewById(R.id.BuyerAdapter);
                                TextView priceTextView = view.findViewById(R.id.textViewTAN);
                                TextView roomnumTextView = view.findViewById(R.id.roomid);

                                String id = idTextView.getText().toString();
                                String title = subjectEditText.getText().toString();
                                String tableNum = priceTextView.getText().toString();
                                String roomnum = roomnumTextView.getText().toString();
                                String statusType = mDatabaseHelper.getLatestTransactionStatus(roomnum, tableNum);
                                String latesttransId = mDatabaseHelper.getLatestTransactionId(roomnum, tableNum, statusType);
                                updateTableId1(tableNum, roomnum);

                                if (latesttransId == null) {
                                    notifyTableClicked(tableNum, roomnum);
                                    refreshsales(roomnum, tableNum);
                                    showSalesTypePopup(tableNum, roomnum,dialog);
                                    mRoomAdapter.setSelectedTableId(id, tableNum);
                                    mDatabaseHelper.setItemsUnselected(roomnum, tableNum);

                                } else {
                                    notifyTableClicked(tableNum, roomnum);
                                    refreshsales(roomnum, tableNum);
                                    mRoomAdapter.setSelectedTableId(id, tableNum);
                                    mDatabaseHelper.setItemsUnselected(roomnum, tableNum);
                                    showSecondaryScreen(data);
                                    dialog.dismiss(); // Dismiss the dialog here
                                }

                                mRoomAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                TextView tableNumTextView = view.findViewById(R.id.textViewTAN);
                                TextView roomnumTextView = view.findViewById(R.id.roomid);
                                String tableNum = tableNumTextView.getText().toString();
                                String roomnumNum = roomnumTextView.getText().toString();
                                showMergeDialog(tableNum, roomnumNum);
                            }
                        })
                );

                mRoomAdapter.swapCursor(tablesCursor); // Update RecyclerView with filtered tables
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optionally handle case when nothing is selected
            }
        });


        // Show the pop-up dialog
        dialog.show();
    }


    private void showSalesTypePopup(final String tableNum, final String roomnum,AlertDialog dialogs) {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View popupView = inflater.inflate(R.layout.popupselectsalestype, null); // Inflate your layout file

        // Initialize the buttons in the layout
        Button dineInButton = popupView.findViewById(R.id.onspot);
        Button deliveryButton = popupView.findViewById(R.id.delivery);
        Button freeSalesButton = popupView.findViewById(R.id.FreeSales); // This is currently hidden, but could be shown if needed

        // Create the AlertDialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(popupView);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle "Dine In" button click
        dineInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save "Dine In" status to SharedPreferences
                saveDiningStatusToPreferences("Dine In");

                // Close the dialog and proceed to the next step
                dialog.dismiss();
                showNumberOfCoversDialog(tableNum, roomnum, dialogs);
            }
        });

        // Handle "Delivery" button click
        deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save "Delivery" status to SharedPreferences
                saveDiningStatusToPreferences("Delivery");

                // Close the dialog and proceed to the next step
                dialog.dismiss();
                showNumberOfCoversDialog(tableNum, roomnum, dialogs);
            }
        });

        // If you decide to show the "Free Sales" button, handle its click event here
        freeSalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle free sales logic if needed
                saveDiningStatusToPreferences("Free Sales");

                // Close the dialog and proceed to the next step
                dialog.dismiss();
                showNumberOfCoversDialog(tableNum, roomnum, dialogs);
            }
        });
    }

    private void saveDiningStatusToPreferences(String status) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("YourPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", status); // Save the status (either "Dine In" or "Delivery")
        editor.apply(); // Apply the changes
    }

    private void refreshsales(String roomId,String tableid) {
        // Call the interface method to notify the activity
        if (reloadListener != null) {
            reloadListener.onReloadFragment(roomId,tableid);
        }

        Bundle bundle = new Bundle();
        bundle.putString("room", roomId);
        bundle.putString("table", tableid);

        SalesFragment receivingFragment = new SalesFragment();
        receivingFragment.setArguments(bundle);

        // Perform fragment transaction to replace the current fragment with the receivingFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.sales_fragment, receivingFragment);
        transaction.addToBackStack(null);  // Optional: Add to back stack if needed
        transaction.commit();
    }
    private void showMergeDialog(final String selectedTableNum,String roomId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a Table to Merge");

        // Fetch the list of available tables from your database
        List<String> availableTables = getAvailableTables(selectedTableNum,roomId);
        Log.d("tab1",  selectedTableNum );

        // Convert the list to an array for dialog selection
        final CharSequence[] tableArray = availableTables.toArray(new CharSequence[availableTables.size()]);

        builder.setItems(tableArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedTableToMerge = tableArray[which].toString();  // Store the selected table to merge
                String statusType1= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomId),selectedTableToMerge);
                String oldstatusType1= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomId),selectedTableNum);
                String newTransactionId=mDatabaseHelper.getLatestTransactionId(String.valueOf(roomId),selectedTableToMerge,statusType1);
                String oldTransactionId=mDatabaseHelper.getLatestTransactionId(String.valueOf(roomId),selectedTableNum,oldstatusType1);
                int totalcovers=0;

                    int table1cover = mDatabaseHelper.getSumOfCoverCountByTableNumber(Integer.parseInt(selectedTableNum));
                    int table2cover = mDatabaseHelper.getSumOfCoverCountByTableNumber(Integer.parseInt(selectedTableToMerge));

                    totalcovers = table1cover + table2cover;


                mergeTables(selectedTableNum, selectedTableToMerge,roomId,totalcovers);
                // Call the interface method to notify the MainActivity about the table click

            }
        });


        // Add a button for transfer
        builder.setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle transfer action here
                transferTable(selectedTableNum, roomId);
            }
        });


        builder.show();
    }
    private void notifyTableClicked(String tableId, String roomnum) {
        if (tableClickListener != null) {
            tableClickListener.onTableClicked(tableId,roomnum);
        }
    }

    public interface OnTableClickListener {
        void onTableClicked(String tableId, String roomnum);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TablesFragment.OnTableClickListener) {
            tableClickListener = (FunctionFragment.OnTableClickListener) context;
            reloadListener = (TablesFragment.OnReloadFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnTableClickListener");
        }

    }
    private void transferTable(final String selectedTableNum,String roomId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a Table to Transfer");

        // Fetch the list of available tables from your database
        List<String> availableTables = getAvailableTables(selectedTableNum,roomId);
        Log.d("tab1",  selectedTableNum );

        // Convert the list to an array for dialog selection
        final CharSequence[] tableArray = availableTables.toArray(new CharSequence[availableTables.size()]);

        builder.setItems(tableArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedTableToTransfer = tableArray[which].toString();  // Store the selected table to merge

                transfer(selectedTableNum, selectedTableToTransfer,roomId);
                // Call the interface method to notify the MainActivity about the table click

            }
        });


        builder.show();
    }
    private void transfer(String selectedTableNum, String selectedTableToTransfer, String roomId) {
        // Perform the transfer operation by updating the database
        // For example, update the table number of the selected table with the new table number
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Log.d("selectedTableNum", selectedTableNum);
        Log.d("selectedTableToTransfer", selectedTableToTransfer);
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomId),selectedTableToTransfer);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomId),selectedTableToTransfer,statusType);
        //  mDatabaseHelper.updateTableIdInTransactionsTransfer(db,cashierId,roomId, selectedTableNum, selectedTableToTransfer,latesttransId);
        // Proceed with the update if latesttransId is not null
        if (latesttransId != null) {
            //  Log.d("transactionIdInProgress", transactionIdInProgress);
            mDatabaseHelper.updateTableNumber(latesttransId,selectedTableNum, selectedTableToTransfer, roomId);
            mDatabaseHelper.updateTransactionTableNumber(selectedTableNum, selectedTableToTransfer, roomId,latesttransId);
            if (selectedTableNum.startsWith("T T")) {
                selectedTableNum = selectedTableNum.replaceFirst("T", ""); // Remove first "T " from the beginning
                unmergeTable(selectedTableNum);
            } else if (selectedTableNum.startsWith("T")) {
                unmergeTable(selectedTableNum);
            }        } else {
            // Handle the case where latesttransId is null (optional)
            mDatabaseHelper.updateTableNumberfornew(selectedTableNum, selectedTableToTransfer, roomId);
            mDatabaseHelper.updateTransactionTableNumberFornew(selectedTableNum, selectedTableToTransfer, roomId);
            if (selectedTableNum.startsWith("T T")) {
                selectedTableNum = selectedTableNum.replaceFirst("T", ""); // Remove first "T " from the beginning
                unmergeTable(selectedTableNum);

            } else if (selectedTableNum.startsWith("T")) {
                unmergeTable(selectedTableNum);

            }

        }

        //mDatabaseHelper.deleteTransactionsByConditions(db,roomId,selectedTableNum,latesttransId);
        mRoomAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", selectedTableToTransfer).apply();
        sharedPreferences.edit().putInt("roomnum", Integer.parseInt(roomId)).apply();


        // Notify the listener that an item is added
        if (itemAddedListener != null) {
            itemAddedListener.onItemAdded(roomId,selectedTableToTransfer);
        }
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }
    private void mergeTables(String selectedTableNum, String selectedTableToMerge,String roomnum,int totalcovers) {
        // Update your database to mark both tables with the same MERGED_SET_ID
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues valuesTable1 = new ContentValues();
        ContentValues valuesTable2 = new ContentValues();

        // Concatenate the TABLE_NUMBER of the first table with the second table
        String newTableNumber = "T " + selectedTableNum + " + T " + selectedTableToMerge;


        Log.d("mergeTables", "Before calling updateTableIdInTransactions");
        Log.d("roomnum", roomnum );
        Log.d("test", selectedTableNum + " " + selectedTableToMerge );

        Log.d("totalcovers", String.valueOf(totalcovers));

        mDatabaseHelper.updateTableIdInTransactions(cashierId,roomnum, selectedTableNum, newTableNumber,totalcovers);
        mDatabaseHelper.updateCoverCount(totalcovers, selectedTableNum, Integer.parseInt(roomnum));
        mDatabaseHelper.updateCoverCount(0, selectedTableToMerge, Integer.parseInt(roomnum));

        Log.d("test", selectedTableNum + " " + selectedTableToMerge );
        if (newTableNumber.startsWith("T T")) {
            newTableNumber = newTableNumber.replaceFirst("T", ""); // Remove first "T " from the beginning
            Log.e("newTableId1", "newTableId1: " + newTableNumber);
        }
        Log.d("newTableNumber", newTableNumber);

        // Update the first table
        valuesTable1.put(DatabaseHelper.MERGED_SET_ID, newTableNumber);
        valuesTable1.put(DatabaseHelper.MERGED, 1);


        int rowsUpdated1 = db.update(DatabaseHelper.TABLES, valuesTable1,
                DatabaseHelper.TABLE_NUMBER + " = ? AND " + DatabaseHelper.ROOM_ID + " = ?",
                new String[]{selectedTableNum, roomnum});

// If updating based on TABLE_NUMBER fails, update based on MERGED_SET_ID
        if (rowsUpdated1 == 0) {
            db.update(DatabaseHelper.TABLES, valuesTable1, DatabaseHelper.MERGED_SET_ID + " = ? AND " + DatabaseHelper.ROOM_ID + " = ?",
                    new String[]{selectedTableNum,roomnum});
        }
        // Update the second table
        valuesTable2.put(DatabaseHelper.MERGED, 1);
// Update the second table based on TABLE_NUMBER
        int rowsUpdated2 = db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.TABLE_NUMBER + " = ? AND " + DatabaseHelper.ROOM_ID + " = ?",
                new String[]{selectedTableToMerge,roomnum});

// If updating based on TABLE_NUMBER fails, update based on MERGED_SET_ID
        if (rowsUpdated2 == 0) {
            db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.MERGED_SET_ID + " = ? AND " + DatabaseHelper.ROOM_ID + " = ?",
                    new String[]{newTableNumber,roomnum});
        }

// Log the number of rows updated
        Log.d("mergeTables", "Rows Updated for " + selectedTableNum + ": " + rowsUpdated1);
        Log.d("mergeTables", "Rows Updated for " + selectedTableToMerge + ": " + rowsUpdated2);
        db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.TABLE_NUMBER + " = ? AND " + DatabaseHelper.ROOM_ID + " = ?",
                new String[]{selectedTableToMerge,roomnum});

        db.close();

        mAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", newTableNumber).apply();
        sharedPreferences.edit().putInt("roomnum", Integer.parseInt(String.valueOf(roomid))).apply();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }
    public void clearTransact(){
        // Create an instance of the DatabaseHelper class
// Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        int  roomid = preferences.getInt("roomnum", 0);
        String    tableid = preferences.getString("table_id", "");
        mDatabaseHelper.deleteDataByInProgressStatus(String.valueOf(roomid),tableid);


        // Optionally, you can notify the user or perform any other actions after clearing the transaction
// Notify the listener that an item is added



        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);


        if ("sunmit2".equalsIgnoreCase(deviceType)) {
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
    private String extractNumericPart(String tableString) {
        // Split the string based on space and return the last part
        String[] parts = tableString.split(" ");
        return parts[parts.length - 1];
    }

    private void unmergeTable(String selectedTableNum) {
//delete  data from transac
        clearTransact();
        // Extract the numeric part from selectedTableToMerge
        String numericPart = extractNumericPart(selectedTableNum);

        // Update your database to unmerge the table
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues valuesTable2 = new ContentValues();
        // Update MERGED and MERGED_SET_ID columns
        values.put(DatabaseHelper.MERGED, 0);
        values.put(DatabaseHelper.MERGED_SET_ID,"0");

        int rowsUpdated = db.update(DatabaseHelper.TABLES, values, DatabaseHelper.MERGED_SET_ID + " = ?",
                new String[]{selectedTableNum});
        valuesTable2.put(DatabaseHelper.MERGED, 0);

        db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.TABLE_NUMBER + " = ?",
                new String[]{numericPart});
        db.close();

        if (rowsUpdated > 0) {
            // Table unmerged successfully
            Log.d("UnmergeTableq", "Table " + selectedTableNum + " has been unmerged.");
        } else if (rowsUpdated == 0) {
            // No rows were updated
            Log.d("UnmergeTableq", "Failed to unmerge table " + selectedTableNum + ". No rows were updated.");
        } else {
            // An error occurred
            Log.d("UnmergeTableq", "Failed to unmerge table " + selectedTableNum + ". Error occurred during update.");
        }

        // Refresh the UI to reflect the changes
        // You may need to update your RecyclerView adapter or rerun the database query to fetch updated data
        mRoomAdapter.notifyDataSetChanged();
        // Send broadcast to refresh MainActivity
        // Navigate to MainActivity when OK is clicked

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", "0").apply();
        sharedPreferences.edit().putInt("roomnum", 0).apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);

    }
    private List<String> getAvailableTables(String selectedTableNum, String roomId) {
        // Query your database to get a list of tables excluding the selected one, merged tables, and those with a different roomId
        // Adjust the query based on your database schema

        // For example:
        List<String> tables = new ArrayList<>();

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.TABLE_NUMBER + " FROM " + DatabaseHelper.TABLES +
                " WHERE " + DatabaseHelper.TABLE_NUMBER + " != ? AND " +
                DatabaseHelper.ROOM_ID + " = ? AND (" +
                DatabaseHelper.MERGED + " = 0 OR " + DatabaseHelper.MERGED + " IS NULL)";
        Cursor cursor = db.rawQuery(query, new String[]{selectedTableNum, roomId});

        if (cursor.moveToFirst()) {
            do {
                String tableNum = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NUMBER));
                tables.add(tableNum);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tables;
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
    private void showNumberOfCoversDialog(String tablenum,String roomnum,AlertDialog dialogs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Inflate the custom layout
        View dialogView = inflater.inflate(R.layout.number_of_covers_dialog, null);
        builder.setView(dialogView);

        // Get references to the EditText and buttons
        final EditText numberEditText = dialogView.findViewById(R.id.numberEditText);
        Button button0 = dialogView.findViewById(R.id.button0);
        Button button1 = dialogView.findViewById(R.id.button1);
        Button button2 = dialogView.findViewById(R.id.button2);
        Button button3 = dialogView.findViewById(R.id.button3);
        Button button4 = dialogView.findViewById(R.id.button4);
        Button button5 = dialogView.findViewById(R.id.button5);
        Button button6 = dialogView.findViewById(R.id.button6);
        Button button7 = dialogView.findViewById(R.id.button7);
        Button button8 = dialogView.findViewById(R.id.button8);
        Button button9 = dialogView.findViewById(R.id.button9);
        Button buttonClear = dialogView.findViewById(R.id.buttonClear);
        Button buttonDelete= dialogView.findViewById(R.id.buttonDelete);

        // Set button click listeners to append number to the EditText
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String currentText = numberEditText.getText().toString();
                numberEditText.setText(currentText + button.getText().toString());
            }
        };

        button0.setOnClickListener(numberClickListener);
        button1.setOnClickListener(numberClickListener);
        button2.setOnClickListener(numberClickListener);
        button3.setOnClickListener(numberClickListener);
        button4.setOnClickListener(numberClickListener);
        button5.setOnClickListener(numberClickListener);
        button6.setOnClickListener(numberClickListener);
        button7.setOnClickListener(numberClickListener);
        button8.setOnClickListener(numberClickListener);
        button9.setOnClickListener(numberClickListener);



        // Set the same listener for all other buttons...

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEditText.setText("");
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = numberEditText.getText().toString();
                if (!currentText.isEmpty()) {
                    numberEditText.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numberOfCoversStr = numberEditText.getText().toString().trim();
                if (!numberOfCoversStr.isEmpty()) {
                    try {
                        int numberOfCovers = Integer.parseInt(numberOfCoversStr);
                        processNumberOfCovers( numberOfCovers,roomnum,tablenum,dialogs);
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

        // Show the dialog
        builder.create().show();
    }
    private void processNumberOfCovers(int numberOfCovers,String roomnum, String tableNum,AlertDialog dialogs) {
        // Use the numberOfCovers variable as needed in your code
        // For example, store it as a class member or pass it to another method
        Log.d("NumberOfCovers", "Number of covers entered: " + numberOfCovers);

        // Continue with your application logic after setting numberOfCovers
        // Update the room ID in SharedPreferences


        mDatabaseHelper.updateCoverCount(numberOfCovers,tableNum,Integer.parseInt(roomnum));
        mRoomAdapter.notifyDataSetChanged();
        Cursor  filteredCursor = mDatabaseHelper.getTablesFilteredByMerged(String.valueOf(roomid));

        mRoomAdapter.swapCursor(filteredCursor);
        dialogs.dismiss();
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
    public void displayOnLCD() {
        if (woyouService == null) {

            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(TRANSACTION_TOTAL_TTC);
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

    private void setUpRecyclerViews(View view) {
        // Set up RecyclerView and its adapter for Transaction Details
        List<DataModel> transactionDataList = fetchDataBasedOnReportTypeAndShift("Daily",0);
        reportAdapter = new SalesReportAdapter(transactionDataList);
        recyclerViewReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewReports.setAdapter(reportAdapter);

        // Set up RecyclerView and its adapter for Payment Method Details
        List<PaymentMethodDataModel> paymentMethodDataList = fetchPaymentMethodDataBasedOnReportTypeAndShift("Daily",0);
        paymentMethodAdapter = new PaymentMethodAdapter(paymentMethodDataList);
        secondRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        secondRecyclerView.setAdapter(paymentMethodAdapter);
    }
    public void oncommentButtonClick(View view, String letter) {
        if (editTextOption1 != null) {
            // Insert the letter into the EditText
            editTextOption1.append(letter);
        }
    }

    private void updateTableId1(String newTableId,String roomId) {
        // Update the table ID in SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("table_id", newTableId);

        editor.putInt("roomnum", Integer.parseInt(roomId));
        editor.putInt("room_id", Integer.parseInt(roomId));

        Log.d("roomtables", roomId + " " + newTableId);
        editor.apply();

        // Now the table ID in SharedPreferences is updated and can be accessed elsewhere in your app
        // Notify the adapter or update UI to reflect the selection change
        mAdapter.notifyDataSetChanged();
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
    private List<DataModel> fetchDataBasedOnReportType(String reportType) {
        // Implement your logic to fetch data based on the selected report type
        // For now, return a dummy list
        List<DataModel> dummyDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch data based on report type
        // Replace the method and parameters with your actual database queries
        dummyDataList = mDatabaseHelper.getDataBasedOnReportType(reportType);

        return dummyDataList;
    }

    private List<PaymentMethodDataModel> fetchPaymentMethodDataBasedOnReportType(String reportType) {
        // Implement logic to fetch data based on the selected report type for payment method
        // Replace it with your actual logic
        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch payment method data based on report type
        // Replace the method and parameters with your actual database queries
        paymentMethodDataList = mDatabaseHelper.getPaymentMethodDataBasedOnReportType(reportType);
        Log.d("ReportPopupDialog", "Payment Method Data: " + paymentMethodDataList.toString());

        return paymentMethodDataList;
    }
    private List<DataModel> fetchDataBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        // Fetch data based on the selected report type and shift number
        return mDatabaseHelper.getDataBasedOnReportTypeAndShift(reportType, shiftNumber);
    }

    private List<PaymentMethodDataModel> fetchPaymentMethodDataBasedOnReportTypeAndShift(String reportType, int shiftNumber) {
        // Fetch payment method data based on the selected report type and shift number
        return mDatabaseHelper.getPaymentMethodDataBasedOnReportTypeAndShift(reportType, shiftNumber);
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
