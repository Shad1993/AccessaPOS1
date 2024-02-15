package com.accessa.ibora.sales.ticket;


import static com.accessa.ibora.product.items.DatabaseHelper.ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_INVOICE_REF;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_UNIT_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT_Type;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.accessa.ibora.Buyer.Buyer;
import com.accessa.ibora.ItemsReport.DataModel;
import com.accessa.ibora.ItemsReport.PaymentMethodAdapter;
import com.accessa.ibora.ItemsReport.PaymentMethodDataModel;
import com.accessa.ibora.ItemsReport.SalesReportAdapter;
import com.accessa.ibora.MRA.MRADBN;
import com.accessa.ibora.MRA.MRAdaptor;
import com.accessa.ibora.MRA.Mra;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Report.ReportActivity;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Settings.SettingsDashboard;

import com.accessa.ibora.SplashFlashActivity;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.accessa.ibora.scanner.InbuiltScannerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class TicketFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;
    private List<String> checkedItems;
    private Buyer selectedBuyer;
    private String cashierId,cashierLevel,shopname, cashiername;
    private DatabaseHelper mDatabaseHelper;
private double totalAmount,TaxtotalAmount;
    private   FrameLayout emptyFrameLayout;
    private Spinner spinnerReportType;
    private RecyclerView recyclerViewReports;
    private RecyclerView secondRecyclerView;
    private SalesReportAdapter reportAdapter;
    private PaymentMethodAdapter paymentMethodAdapter;

    FloatingActionButton mAddItem;
    private  String ItemId,PosNum;
    private String VatVall;
    private static final String POSNumber="posNumber";
private String transactionIdInProgress;
    private int transactionCounter = 1;
private TextView textViewVATs,textViewTotals;
    private SoundPool soundPool;
    private int soundId;
    private IWoyouService woyouService;
    private static final String TRANSACTION_ID_KEY = "transaction_id";
    private Toolbar toolbar;
    private DBManager dbManager;
    private String actualdate,tableid;
    private int roomid;
    private boolean isBuyerSelected = false;

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
        tableid = String.valueOf(preferences.getInt("table_id", 0));


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
        activity.setSupportActionBar(toolbar);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.navigation_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Context context = getContext(); // Get the Context object
            if (context != null) {
                Intent intent = new Intent(context, SettingsDashboard.class);
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.nav_buyer) {
            if (isBuyerSelected) {
                // Handle "Remove Buyer" behavior
                // For example, clear the selection and update the title
                selectedBuyer = null;
                isBuyerSelected = false;

                // Clear the title, subtitle, and icon
                toolbar.setTitle(getString(R.string.Tickets));
                toolbar.setSubtitle(null);
                toolbar.setNavigationIcon(null);

                // Clear the selectedBuyer variable
                selectedBuyer = null;

                // Remove the buyer info from shared preferences
                clearBuyerInfoFromPrefs();
            } else {
                // Handle "Select a Buyer" behavior
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select a Buyer");
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

                List<Buyer> buyerList = mDatabaseHelper.getAllBuyers();

                ArrayAdapter<Buyer> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, buyerList);
                builder.setAdapter(adapter, (dialog, which) -> {
                    selectedBuyer = buyerList.get(which);
                    isBuyerSelected = true; // Update the flag

                    // Update the app bar title with the buyer's name and price level
                    if (selectedBuyer != null) {
                        String buyerName = selectedBuyer.getNames();
                        String priceLevel = selectedBuyer.getPriceLevel();
                        toolbar.setTitle(buyerName);
                        toolbar.setSubtitle(priceLevel);
                        toolbar.setNavigationIcon(R.drawable.people);
                        String selectedPriceLevel = priceLevel;

                        if (selectedPriceLevel != null && !selectedPriceLevel.isEmpty()) {
                            // Store buyer info in shared preferences
                            saveBuyerInfoToPrefs(buyerName, selectedPriceLevel);
                            // Store buyer info in shared preferences
                            saveBuyerInfoToPrefs(buyerName, selectedPriceLevel);
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


                                            // Retrieve the TRANSACTION_TICKET_NO where TRANSACTION_STATUS is 'InProgress'
                                            String transactionTicketNo = mDatabaseHelper.getInProgressTransactionTicketNo();
                                            if (selectedPriceLevel.equals("Price Level 1")) {
                                                // Update the TRANSACTION table based on the retrieved TRANSACTION_TICKET_NO
                                                mDatabaseHelper.updateTransactionBasedOnInProgressTicketNo(transactionTicketNo, "Price Level 1");
                                                double sumPriceAfterDiscountInProgress = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);


                                                refreshData(sumPriceAfterDiscountInProgress, TaxtotalAmount);
                                            } else if (selectedPriceLevel.equals("Price Level 2")) {
                                                // Update the TRANSACTION table based on the retrieved TRANSACTION_TICKET_NO
                                                mDatabaseHelper.updateTransactionBasedOnInProgressTicketNo(transactionTicketNo, "Price Level 2");
                                                double sumPriceAfterDiscountInProgress = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);

                                                refreshData(sumPriceAfterDiscountInProgress, TaxtotalAmount);
                                            } else if (selectedPriceLevel.equals("Price Level 3")) {
                                                // Update the TRANSACTION table based on the retrieved TRANSACTION_TICKET_NO
                                                mDatabaseHelper.updateTransactionBasedOnInProgressTicketNo(transactionTicketNo, "Price Level 3");
                                                double sumPriceAfterDiscountInProgress = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);

                                                refreshData(sumPriceAfterDiscountInProgress, TaxtotalAmount);
                                            }


                                            // Navigate to MainActivity when OK is clicked
                                            Intent intent = new Intent(requireContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            messageBuilder.create().show();
                            isBuyerSelected = false; // Update the flag
                            // Clear the AlertDialog if a buyer is selected
                            dialog.dismiss();
                        }
                    }
                });

                builder.show();
                item.setTitle("Remove Buyer"); // Update the menu item's title
            }
            return true;
        } else if
        (id == R.id.nav_logout) {
            MainActivity mainActivity = (MainActivity) requireActivity(); // Get the MainActivity object
            mainActivity.logout(); // Call the logout() function
            // Remove the buyer info from shared preferences
            clearBuyerInfoFromPrefs();
            return true;
        } else if
        (id == R.id.nav_scan) {
            InbuiltScannerFragment newScannerFragment = new InbuiltScannerFragment();
            replaceFragment(newScannerFragment);

            return true;
        } else if
        (id == R.id.clear_ticket) {

            clearTransact(); // Call the clearTransact() function on the CustomerLcdFragment
            return true;
        } else if
        (id == R.id.open_drawer) {
            int level = Integer.parseInt(cashierLevel);
            if (level == 7) {
                try {
                    woyouService.openDrawer(null);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if
        (id == R.id.Report) {
            int level = Integer.parseInt(cashierLevel);
            if (level >= 6) {
                Context context = getContext(); // Get the Context object
                Intent intent = new Intent(context, ReportActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
            }

            return true;
        } // In your activity or fragment
        else if (id == R.id.SalesReport) {
            int level = Integer.parseInt(cashierLevel);
            if (level >= 6) {
                // Create a dialog with a custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.sales_report_popup, null);
                builder.setView(dialogView);

                 spinnerReportType = dialogView.findViewById(R.id.spinnerReportType);
                 recyclerViewReports = dialogView.findViewById(R.id.recyclerViewSalesReport);
                 secondRecyclerView = dialogView.findViewById(R.id.secondRecyclerView);

                // Initialize the database helper
                mDatabaseHelper = new DatabaseHelper(requireContext());

                // Populate spinner with report types (daily, weekly, monthly, yearly)
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                        R.array.report_types, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerReportType.setAdapter(adapter);

                // Set up RecyclerViews and their adapters
                setUpRecyclerViews(dialogView);

                // Set the spinner's item selected listener
                spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Fetch data based on the selected report type and update the adapter
                        String selectedReportType = spinnerReportType.getSelectedItem().toString();

                        // Log the selected report type for debugging
                        Log.d("ReportPopupDialog", "Selected Report Type: " + selectedReportType);

                        // Fetch data based on the selected report type
                        List<DataModel> newDataList = fetchDataBasedOnReportType(selectedReportType);
                        List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportType(selectedReportType);
                        // Log the fetched data for debugging
                        Log.d("ReportPopupDialog", "Fetched Data: " + newDataList.toString());

                        // Update the adapter with the new data
                        reportAdapter.updateData(newDataList);
                        paymentMethodAdapter.updateData(newDataLists);
                        // Update total tax and total amount TextViews

                            // Assuming you have a method to fetch total tax and total amount based on report type
                            double totalTax = mDatabaseHelper.getTotalTaxBasedOnReportType(selectedReportType);
                            double totalAmount = mDatabaseHelper.getTotalAmountBasedOnReportType(selectedReportType);

                        // Update TextViews
                        TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                        TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                        // Format totalTax and totalAmount to display only two decimal places
                        String formattedTotalTax = String.format(Locale.getDefault(), "%.2f", totalTax);
                        String formattedTotalAmount = String.format(Locale.getDefault(), "%.2f", totalAmount);

                        textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax);
                        textViewTotalAmount.setText("Total Amount: Rs " + formattedTotalAmount);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing here
                    }
                });

                builder.setTitle("Item Sales Report")
                        .setPositiveButton("OK", (dialog, which) -> {

                        })
                        .setNegativeButton("Cancel", null) // Optional: Add a cancel button
                        .show();
                //ReportPopupDialog reportPopupDialog = new ReportPopupDialog();
                //reportPopupDialog.show(getChildFragmentManager(), "ReportPopupDialog");
            } else {
                Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        else if (id == R.id.SplitBill) {
            int level = Integer.parseInt(cashierLevel);
            if (level >= 6) {
                // Create a dialog with a custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.splitbill, null);
                builder.setView(dialogView);

                // Set up the dialog's UI elements
                EditText numberOfPeopleEditText = dialogView.findViewById(R.id.editTextNumberOfPeople);
                TextView resultTextView = dialogView.findViewById(R.id.resultTextView);
                EditText splitAmountEditText = dialogView.findViewById(R.id.editTextSplitAmount);
                EditText splitTaxtotalAmount = dialogView.findViewById(R.id.editTextSplitVat);
                Button splitButton = dialogView.findViewById(R.id.splitButton);
                // Retrieve roomid and tableid from SharedPreferences
                SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                int roomid = preferences.getInt("roomnum", 0);
                String tableid = preferences.getString("table_id", "");
                String transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);

// Retrieve the total amount and total tax amount from the transactionheader table
                double totalAmount = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
                double TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);


                splitAmountEditText.setText(String.valueOf(totalAmount));
                splitTaxtotalAmount.setText(String.valueOf(TaxtotalAmount));

                splitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Format the current date and time as per your requirement
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentDate = dateFormat.format(new Date());
                        double totalAmount = Double.parseDouble(splitAmountEditText.getText().toString());
                        double totalTax = Double.parseDouble(splitTaxtotalAmount.getText().toString());
                        int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                        double splitAmount = totalAmount / numberOfPeople;
                        double SpliVat = totalTax / numberOfPeople;
                        SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        int roomid = preferences.getInt("roomnum", 0);
                        String tableid = preferences.getString("table_id", "");

                        String transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);
                        StringBuilder resultTextBuilder = new StringBuilder();
                        mDatabaseHelper.getWritableDatabase().delete(
                                TRANSACTION_TABLE_NAME,
                                ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_ID + " = ?",
                                new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                        );
                        for (int i = 0; i < numberOfPeople; i++) {

                            // Insert new records
                            ContentValues values = new ContentValues();
                            values.put(ROOM_ID, roomid);
                            values.put(TABLE_ID, tableid);
                            values.put(TRANSACTION_ID, transactionIdInProgress);
                            long uniqueItemId = System.currentTimeMillis();
                            values.put(ITEM_ID, uniqueItemId);
                            values.put(TRANSACTION_DATE, currentDate);
                            values.put(QUANTITY, 1);
                            values.put(TOTAL_PRICE, splitAmount);
                            values.put(TRANSACTION_UNIT_PRICE, splitAmount);

                            // Parse VAT and VAT_Type from existing records
                            Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery(
                                    "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                                            " WHERE " + ROOM_ID + " = ? AND " +
                                            TABLE_ID + " = ? AND " +
                                            TRANSACTION_ID + " = ?",
                                    new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                            );


                            values.put(VAT, SpliVat); // Replace with default value or retrieve from somewhere else
                            if(SpliVat>0) {
                                values.put(VAT_Type, "VAT 0%"); // Replace with default value or retrieve from somewhere else
                            } else{
                                values.put(VAT_Type, "VAT 15%");
                            }

                            values.put(LongDescription, "Menu Repas");

                            mDatabaseHelper.getWritableDatabase().insert(TRANSACTION_TABLE_NAME, null, values);

                            mDatabaseHelper.updateTransactionSplitType(transactionIdInProgress,"Splitted");
                            // Build result text
                            resultTextBuilder.append("Menu Repas - Rs ").append(formatDecimal(splitAmount));
                            if (i < numberOfPeople - 1) {
                                resultTextBuilder.append("\n");
                            }
                        }

                        // Display result text
                        resultTextView.setText(resultTextBuilder.toString());
                        refreshData(totalAmount,TaxtotalAmount);
                    }
                });
                builder.setTitle("Split Bill")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Get the number of people from the EditText
                            int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());



                            // Format the current date and time as per your requirement
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String currentDate = dateFormat.format(new Date());

                            // Clear existing in-progress transaction
                            mDatabaseHelper.getWritableDatabase().delete(
                                    TRANSACTION_TABLE_NAME,
                                    ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_ID + " = ?",
                                    new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                            );

                            // Split the bill and insert new records
                            double splitAmount = totalAmount / numberOfPeople;
                            double splitVat = TaxtotalAmount / numberOfPeople;

                            StringBuilder resultTextBuilder = new StringBuilder();
                            for (int i = 0; i < numberOfPeople; i++) {
                                ContentValues values = new ContentValues();
                                values.put(ROOM_ID, roomid);
                                values.put(TABLE_ID, tableid);
                                values.put(TRANSACTION_ID, transactionIdInProgress);
                                long uniqueItemId = System.currentTimeMillis();
                                values.put(ITEM_ID, uniqueItemId);
                                values.put(TRANSACTION_DATE, currentDate);
                                values.put(QUANTITY, 1);
                                values.put(TOTAL_PRICE, splitAmount);
                                values.put(TRANSACTION_UNIT_PRICE, splitAmount);

                                // Parse VAT and VAT_Type from existing records
                                Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery(
                                        "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                                                " WHERE " + ROOM_ID + " = ? AND " +
                                                TABLE_ID + " = ? AND " +
                                                TRANSACTION_ID + " = ?",
                                        new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                                );

                                values.put(VAT, splitVat); // Replace with default value or retrieve from somewhere else
                                if (splitVat > 0) {
                                    values.put(VAT_Type, "VAT 0%"); // Replace with default value or retrieve from somewhere else
                                } else {
                                    values.put(VAT_Type, "VAT 15%");
                                }

                                values.put(LongDescription, "Menu Repas");

                                mDatabaseHelper.getWritableDatabase().insert(TRANSACTION_TABLE_NAME, null, values);

                                mDatabaseHelper.updateTransactionSplitType(transactionIdInProgress, "Splitted");

                                // Build result text
                                resultTextBuilder.append("Menu Repas - Rs ").append(formatDecimal(splitAmount));
                                if (i < numberOfPeople - 1) {
                                    resultTextBuilder.append("\n");
                                }
                            }

                            // Display result text
                            resultTextView.setText(resultTextBuilder.toString());
                            refreshData(totalAmount,TaxtotalAmount);
                        })
                        .setNegativeButton("Cancel", null) // Optional: Add a cancel button
                        .show();
            } else {
                Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
            }


    } else {
            // Handle other menu item clicks
            return super.onOptionsItemSelected(item);
        }

        return true;

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_fragment, container, false);



        // Find the toolbar view
        toolbar = view.findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(this);
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);
        SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
       String  buyername = sharedPreferences2.getString("BuyerName", null);
        String  buyerpricelevel = sharedPreferences2.getString("PriceLevel", null);
        if (buyername != null && buyerpricelevel != null ) {

            toolbar.setTitle(buyername);
            toolbar.setSubtitle(buyerpricelevel);
            toolbar.setNavigationIcon(R.drawable.people);

        }



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

        mAddItem = view.findViewById(R.id.fab);


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



         emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use getActivity() to get the activity context from the fragment
                Context context = getActivity();

                // Create a custom dialog with custom dimensions
                Dialog dialog = new Dialog(context);

                // Set the layout for the dialog
                dialog.setContentView(R.layout.popup_layout_add_dept);

                // Set the dialog width and height
                Window window = dialog.getWindow();
                WindowManager.LayoutParams params = window.getAttributes();

                // Set the width and height as per your requirements
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;

                // Apply the layout parameters to the window
                window.setAttributes(params);

                // Find the Spinner and EditText in the dialog layout
                Spinner spinnerDepartment = dialog.findViewById(R.id.spinner_department);
                EditText editTextAmount = dialog.findViewById(R.id.edit_text_amount);

                // Find the Type Spinner in the dialog layout
                Spinner spinnerType = dialog.findViewById(R.id.spinner_type);

// Define an array of type options
                String[] typeOptions = {"Goods", "Services"};

// Create an ArrayAdapter for the Type Spinner
                ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        typeOptions
                );
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the Type Spinner adapter
                spinnerType.setAdapter(typeAdapter);


                // Find the VAT Spinner in the dialog layout
                Spinner spinnerVAT = dialog.findViewById(R.id.spinner_vat);
                // Find the VAT Spinner in the dialog layout
                Spinner spinnerDiscount = dialog.findViewById(R.id.spinner_discount);

// Define an array of VAT options
                String[] vatOptions = {"VAT 0%", "VAT Exempted", "VAT 15%"};

// Create an ArrayAdapter for the VAT Spinner
                ArrayAdapter<String> vatAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        vatOptions
                );
                vatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the VAT Spinner adapter
                spinnerVAT.setAdapter(vatAdapter);
                String department=null;
                String departmentid=null;
                Cursor cursor = mDatabaseHelper.getAllDepartment();
                List<String> departments = new ArrayList<>();
                departments.add(getString(R.string.AllDepartments));
                if (cursor.moveToFirst()) {
                    do {
                         department = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
                        departmentid = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DEPARTMENT_ID));
                        departments.add(department);
                    } while (cursor.moveToNext());
                }
                cursor.close();

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(), 0, departments) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                        }

                        TextView textView = convertView.findViewById(android.R.id.text1);
                        textView.setTextColor(getResources().getColor(R.color.white));


                        // Set the text for the selected item
                        textView.setText(getItem(position));

                        return convertView;
                    }
                };
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cursor discursor = mDatabaseHelper.getAllDiscounts();

                List<String> Discounts = new ArrayList<>();
                Discounts.add(getString(R.string.AllDiscount));

                if (discursor.moveToFirst()) {
                    do {
                        String discount = discursor.getString(discursor.getColumnIndex(DatabaseHelper.DISCOUNT_NAME));
                        Discounts.add(discount);
                    } while (discursor.moveToNext());
                }
                discursor.close();

                // Create an ArrayAdapter for the spinner with the custom layout
                ArrayAdapter<String> discountAdapter = new ArrayAdapter<String>(requireContext(), 0, Discounts) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                        }

                        TextView textView = convertView.findViewById(android.R.id.text1);
                        textView.setTextColor(getResources().getColor(R.color.white));


                        // Set the text for the selected item
                        textView.setText(getItem(position));

                        return convertView;
                    }

                };
                discountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Set the Spinner adapter
                spinnerDiscount.setAdapter(discountAdapter);
                    // Set the Spinner adapter
                spinnerDepartment.setAdapter(spinnerAdapter);

                // Add a button to save the data
                Button saveButton = dialog.findViewById(R.id.save_button);
                String finalDepartmentid = departmentid;
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Get the current date and time for the transaction
                        String transactionDate = getCurrentDateTime();
                        // Get the selected department and entered amount
                        // Get the selected VAT and entered amount
                        String selectedVAT = spinnerVAT.getSelectedItem().toString();
                        String selectedDepartment = spinnerDepartment.getSelectedItem().toString();
                        String enteredAmounts = editTextAmount.getText().toString();
                        double enteredAmount= Double.parseDouble(enteredAmounts);
                        String selectedType = spinnerType.getSelectedItem().toString();

                        SendToHeader(enteredAmount,0.00);
                        // Perform your desired action with the data (e.g., save it)
                        mDatabaseHelper.insertTransaction(Integer.parseInt(finalDepartmentid), transactionIdInProgress, transactionDate, 1, enteredAmount, 0.00, selectedDepartment, enteredAmount, 0.00, selectedVAT, PosNum, selectedType, finalDepartmentid, "MUR","TC01",0.0,0.00, String.valueOf(roomid),tableid,0);


                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Show the dialog
                dialog.show();
            }
        });


        // Retrieve the checkout button and set its click listener
        Button checkoutButton = view.findViewById(R.id.buttonCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBuyer != null) {
                    // Retrieve the total amount and total tax amount from the transactionheader table
                    Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
                    int currentCounter = 1; // Default value if no data is present in the table

                    if (cursor != null && cursor.moveToFirst()) {

                        int columnIndexInvoiceRef = cursor.getColumnIndex(TRANSACTION_INVOICE_REF);



                        String InvoiceRefIdentifyer = cursor.getString(columnIndexInvoiceRef);
                        // Access selectedBuyer properties here
                        Intent intent = new Intent(getActivity(), Mra.class);

                        intent.putExtra("roomid", roomid);
                        intent.putExtra("tableid", tableid);
                        intent.putExtra("transactionIdInProgress", transactionIdInProgress);
                        System.out.println("tr1: " + transactionIdInProgress);
                        SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        transactionIdInProgress = sharedPreference.getString(TRANSACTION_ID_KEY, null);



                        mDatabaseHelper.updateTransactionBuyerInfo(
                                transactionIdInProgress,
                                InvoiceRefIdentifyer,
                                selectedBuyer.getNames(),
                                selectedBuyer.getTan(),
                                selectedBuyer.getCompanyName(),
                                selectedBuyer.getBusinessAddr(),
                                selectedBuyer.getBrn(),
                                cashierId
                        );


                        String amount = "0";
                        String popFraction = "novalue";
                        // Create and show the dialog fragment with the data
                        validateticketDialogFragment dialogFragment = validateticketDialogFragment.newInstance(

                                transactionIdInProgress,
                                amount,
                                popFraction,
                                selectedBuyer.getBuyerType(),
                                selectedBuyer.getNames(),
                                selectedBuyer.getTan(),
                                selectedBuyer.getCompanyName(),
                                selectedBuyer.getBusinessAddr(),
                                selectedBuyer.getBrn(),
                                selectedBuyer.getNic(),
                                selectedBuyer.getProfile()
                        );
                        System.out.println("tr2: " + transactionIdInProgress);


                        // Use getChildFragmentManager() to show the dialog within the fragment.
                        dialogFragment.show(getChildFragmentManager(), "validate_transaction_dialog");

                    }

                }else {
                    String amount = "0";
                    String popFraction = "novalue";
                    // Handle the case when selectedBuyer is null
                    validateticketDialogFragment dialogFragment = validateticketDialogFragment.newInstance(
                            transactionIdInProgress,
                            amount,
                            popFraction,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );


                    dialogFragment.show(getChildFragmentManager(), "validate_transaction_dialog");


                }
                clearBuyerInfoFromPrefs();
            }

        });


        // Retrieve the checkout button and set its click listener
        Button saveButton = view.findViewById(R.id.saveTicket);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveOptionsDialog();
            }
        });


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
                        String Quantity = QuantityEditText.getText().toString();
                        String Price = PriceEditText.getText().toString();
                        ItemId = itemIdEditText.getText().toString();

                        // Update CheckBox visibility on item click
                        CheckBox checkBox = view.findViewById(R.id.checkbox);
                        boolean isCheckBoxVisible = checkBox.getVisibility() == View.VISIBLE;

                        // Create and show the dialog fragment with the data only if the CheckBox is not visible
                        if (!isCheckBoxVisible) {
                            ModifyItemDialogFragment dialogFragment = ModifyItemDialogFragment.newInstance(Quantity, Price, LongDesc, ItemId);
                            dialogFragment.setTargetFragment(TicketFragment.this, 0);
                            dialogFragment.show(activity.getSupportFragmentManager(), "modify_item_dialog");
                        } else {
                            // CheckBox is visible, toggle the checked state
                            checkBox.setChecked(!checkBox.isChecked());
                            checkedItems = mAdapter.checkedItems;
                            // Update the list of checked items
                            if (checkBox.isChecked()) {
                                checkedItems.add(ItemId);
                                // Update the isSelected field in the database
                                mDatabaseHelper.updateItemSelected(Long.parseLong(ItemId), checkBox.isChecked());
                                double totalAmount = mDatabaseHelper.calculateTotalAmounts(String.valueOf(roomid),tableid);
                                double totalTax = mDatabaseHelper.calculateTotalTaxAmounts(String.valueOf(roomid),tableid);
                                refreshData(totalAmount, totalTax);

                            } else {
                                checkedItems.remove(ItemId);
                                // Update the isSelected field in the database
                                mDatabaseHelper.updateItemSelected(Long.parseLong(ItemId), checkBox.isChecked());
                                double totalAmount = mDatabaseHelper.calculateTotalAmounts(String.valueOf(roomid),tableid);
                                double totalTax = mDatabaseHelper.calculateTotalTaxAmounts(String.valueOf(roomid),tableid);
                                refreshData(totalAmount, totalTax);
                            }

// Calculate the total amount
                            double totalAmount = mDatabaseHelper.calculateTotalAmounts(String.valueOf(roomid),tableid);
                            double totalTax = mDatabaseHelper.calculateTotalTaxAmounts(String.valueOf(roomid),tableid);
                            refreshData(totalAmount, totalTax);



                        }
                    }



                    @Override
                    public void onLongItemClick(View view, int position) {
                        TicketAdapter adapter = (TicketAdapter) mRecyclerView.getAdapter();
                        adapter.setCheckBoxVisibility(true);
                    }
                })
        );

        return view;
    }

    private double calculateTotalAmounts() {
        double totalAmount = 0.0;

        for (String itemId : checkedItems) {
            // Get the total price for the checked item and add it to the total amount
            // You need to implement a method to get the total price based on the itemId
            double itemTotalPrice = getTotalPriceForItemId(itemId);
            totalAmount += itemTotalPrice;
        }

        return totalAmount;
    }

    private double getTotalPriceForItemId(String itemId) {
        // Implement this method to get the total price for the specified itemId from your data
        // Return 0.0 or handle the case when the item is not found
        return 0.0;
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
    private void showSaveOptionsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Save Transaction as");
        String[] options = {"Addition", "Debit Note", "Credit Note"};
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
    Cursor receiptCursor = mDatabaseHelper.getAllReceipts(transactionIdInProgress);
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
                    tableid = String.valueOf(preferences.getInt("table_id", 0));
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
    tableid = String.valueOf(preferences.getInt("table_id", 0));

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
    Cursor newCursor1 = mDatabaseHelper.getAllReceipts(transactionIdInProgress);

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
    // Function to update total tax and total amount TextViews

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

        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = String.valueOf(preferences.getInt("table_id", 0));
Log.d("room and table", roomid+ " " +tableid);
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
        TicketAdapter adapter = (TicketAdapter) mRecyclerView.getAdapter();

        adapter.setCheckBoxVisibility(false);
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
    private void setUpRecyclerViews(View view) {
        // Set up RecyclerView and its adapter for Transaction Details
        List<DataModel> transactionDataList = fetchDataBasedOnReportType("Daily");
        reportAdapter = new SalesReportAdapter(transactionDataList);
        recyclerViewReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewReports.setAdapter(reportAdapter);

        // Set up RecyclerView and its adapter for Payment Method Details
        List<PaymentMethodDataModel> paymentMethodDataList = fetchPaymentMethodDataBasedOnReportType("Daily");
        paymentMethodAdapter = new PaymentMethodAdapter(paymentMethodDataList);
        secondRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        secondRecyclerView.setAdapter(paymentMethodAdapter);
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


    private List<DataModel> fetchDataBasedOnReportType(String reportType) {
        // Implement your logic to fetch data based on the selected report type
        // For now, return a dummy list
        List<DataModel> dummyDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch data based on report type
        // Replace the method and parameters with your actual database queries
        dummyDataList = mDatabaseHelper.getDataBasedOnReportType(reportType);

        return dummyDataList;
    }
    // Helper method to save buyer info to shared preferences
    private void saveBuyerInfoToPrefs(String buyerName, String priceLevel) {
        SharedPreferences sharedPrefs = requireContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("BuyerName", buyerName);
        editor.putString("PriceLevel", priceLevel);
        editor.apply();
    }
    private String formatDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(value);
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
