package com.accessa.ibora.sales.ticket;


import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.IS_PAID;
import static com.accessa.ibora.product.items.DatabaseHelper.ITEM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.PriceAfterDiscount;
import static com.accessa.ibora.product.items.DatabaseHelper.QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.ROOM_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_Users;
import static com.accessa.ibora.product.items.DatabaseHelper.TOTAL_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_BARCODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_CURRENCY;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE_CREATED;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE_MODIFIED;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DATE_TRANSACTION;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DESCRIPTION;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_DISCOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_FAMILLE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_INVOICE_REF;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_IS_TAXABLE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ITEM_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_NATURE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_QUANTITY;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_SHOP_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_STATUS;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TAX_CODE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TERMINAL_NO;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TIME_CREATED;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TIME_MODIFIED;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TIME_TRANSACTION;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTALIZER;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_DISCOUNT;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_HT_A;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_HT_B;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_TTC;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TYPE_TAX;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_UNIT_PRICE;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_VAT_AFTER_DISC;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_WEIGHTS;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT;
import static com.accessa.ibora.product.items.DatabaseHelper.VAT_Type;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.accessa.ibora.Settings.Buyer.Buyer;
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
import com.accessa.ibora.printer.PrintDailyReport;
import com.accessa.ibora.printer.PrintDailyReportPerCashior;
import com.accessa.ibora.printer.externalprinterlibrary2.Kitchen.SendNoteToKitchenActivity;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Tables.TableAdapter;
import com.accessa.ibora.sales.ticket.Checkout.Cashier;
import com.accessa.ibora.sales.ticket.Checkout.SettlementItem;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class TicketFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private StringBuilder enterednumber;
    private RecyclerView splitBillRecyclerView;

    private String creditNoteReason; // Member variable to hold the reason

    EditText numberOfPeopleEditText;
        private   double CashReturnVal;
    private LinearLayout FooterLayout,validate,EmptyFooter; // Added
    private View separationline;

    private RecyclerView mRecyclerView,SplittedItemsRecycleView;
    private TicketAdapter mAdapter;
    private SplitBillTicketAdapter mSplitBillAdapter;
    private TableAdapter mTableAdapter;
    private SplittedTicketAdapter mSplittedAdapter;
    private List<String> checkedItems;
    private SparseBooleanArray checkedState = new SparseBooleanArray();
    private CoverAmountListener CoverClearedListener;
    private Buyer selectedBuyer;
    private String cashierId,cashierLevel,shopname, cashiername;
    private DatabaseHelper mDatabaseHelper;
private double totalAmount,TaxtotalAmount;
    private   FrameLayout emptyFrameLayout;
    private   LinearLayout totalntax;
    private Spinner spinnerReportType,cashiorspinnerReportType;
    private RecyclerView recyclerViewReports;
    private RecyclerView secondRecyclerView;
    private SalesReportAdapter reportAdapter;
    private PaymentMethodAdapter paymentMethodAdapter;

    FloatingActionButton mAddItem,mcheckbox;
    private  String ItemId,PosNum,uniqueid;
    private String VatVall;
    private static final String POSNumber="posNumber";
private String transactionIdInProgress;
    private int transactionCounter = 1;
private TextView textViewVATs,textViewTotals,textviewpaymentmethod,textviewSubTotal,textviewdiscount;
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

                    showSecondaryScreen(data);

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
        tableid = preferences.getString("table_id", "0");


        mDatabaseHelper = new DatabaseHelper(getContext());

        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        transactionIdInProgress= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        if ("sunmit2".equalsIgnoreCase(deviceType)) {
            //Log.d("deviceType1",deviceType  );

            showSecondaryScreen(data);
        }  else {
           // Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
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

       /* if (id == R.id.nav_settings) {
            Context context = getContext(); // Get the Context object
            if (context != null) {
                Intent intent = new Intent(context, SettingsDashboard.class);
                startActivity(intent);
            }
            return true;
        } else */if (id == R.id.nav_buyer) {
            if (isBuyerSelected) {
                // Handle "Remove Buyer" behavior
                selectedBuyer = null;
                isBuyerSelected = false;

                // Clear the title, subtitle, and icon
                toolbar.setTitle(getString(R.string.Tickets));
                toolbar.setSubtitle(null);
                toolbar.setNavigationIcon(null);

                // Remove the buyer info from shared preferences
                clearBuyerInfoFromPrefs();

                // Update the menu item's title back to "Select Buyer"
                item.setTitle("Select Buyer");
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

                        if (priceLevel != null && !priceLevel.isEmpty()) {
                            // Store buyer info in shared preferences
                            saveBuyerInfoToPrefs(buyerName, priceLevel);

                            SharedPreferences sharedPreferencesPriceLevel = requireContext().getSharedPreferences("pricelevel", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferencesPriceLevel.edit();
                            editor.putString("selectedPriceLevel", priceLevel);
                            editor.apply();

                            // Show a new dialog with a message
                            AlertDialog.Builder messageBuilder = new AlertDialog.Builder(requireContext());
                            messageBuilder.setTitle("Message")
                                    .setMessage("Price level selected: " + priceLevel)
                                    .setPositiveButton("OK", (dialogInterface, whichButton) -> {
                                        // Initialize SharedPreferences
                                        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                                        roomid = preferences.getInt("roomnum", 0);
                                        tableid = preferences.getString("table_id", "0");

                                        // Retrieve the TRANSACTION_TICKET_NO where TRANSACTION_STATUS is 'InProgress'
                                        String transactionTicketNo = mDatabaseHelper.getInProgressTransactionTicketNo();

                                        // Update based on selected price level
                                        mDatabaseHelper.updateTransactionBasedOnInProgressTicketNo(transactionTicketNo, priceLevel);
                                        double sumPriceAfterDiscountInProgress = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
                                        double sumTaxAfterDiscountInProgress = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);
                                        refreshData(sumPriceAfterDiscountInProgress, sumTaxAfterDiscountInProgress, "movetobottom");
                                    });
                            messageBuilder.create().show();

                            // Set the menu item title to "Remove Buyer"
                            item.setTitle("Remove Buyer");
                        }
                    }
                });

                builder.show();
            }
            return true;
        }
        else if
        (id == R.id.nav_logout) {
            MainActivity mainActivity = (MainActivity) requireActivity(); // Get the MainActivity object
            mainActivity.logout(); // Call the logout() function
            // Remove the buyer info from shared preferences
            clearBuyerInfoFromPrefs();
            return true;
        } else if
        (id == R.id.edit_cover_num) {
            int levelNumber= Integer.parseInt(cashierLevel);
            String Activity="editServingsAmount_";
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
            // Assuming you have a way to get the levelNumber

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
                    SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
                    int roomId = preferences.getInt("room_id", 0);
                    tableid = String.valueOf(preferences.getString("table_id", "0"));


// Check if roomid or tableid is 0
                    if (roomId == 0 || tableid == null || tableid.isEmpty() || tableid.equals("0")) {
                        Toast.makeText(getContext(), "Please select a table and room", Toast.LENGTH_SHORT).show();
                    } else {



                        showEditNumberOfCoversDialog(String.valueOf(roomId),tableid);


                    }
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
                int roomId = preferences.getInt("room_id", 0);
                tableid = String.valueOf(preferences.getString("table_id", "0"));


// Check if roomid or tableid is 0
                if (roomId == 0 || tableid == null || tableid.isEmpty() || tableid.equals("0")) {
                    Toast.makeText(getContext(), "Please select a table and room", Toast.LENGTH_SHORT).show();
                } else {



                    showEditNumberOfCoversDialog(String.valueOf(roomid),tableid);


                }            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }



            return true;
        } else if
        (id == R.id.clear_ticket) {
            int levelNumber= Integer.parseInt(cashierLevel);
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

            String Activity="clearTransaction_";

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
                    SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
                    int roomId = preferences.getInt("room_id", 0);
                    tableid = String.valueOf(preferences.getString("table_id", "0"));
                    String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                    String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

                    mDatabaseHelper.flagTransactionItemsAsCleared(latesttransId);
                    unmergeTable(tableid);
                    clearTransact(); // Call the clearTransact() function on the CustomerLcdFragment
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
                int roomId = preferences.getInt("room_id", 0);
                tableid = String.valueOf(preferences.getString("table_id", "0"));
                String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);
                clearTransact();
                mDatabaseHelper.flagTransactionItemsAsCleared(latesttransId);
                unmergeTable(tableid);
                // Call the clearTransact() function on the CustomerLcdFragment
                deleteInvalidTables();
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }

            return true;
        }  else if
        (id == R.id.clear_Payment) {
            int levelNumber= Integer.parseInt(cashierLevel);
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

            String Activity="clearPayment_";

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
                    clearPayment(); // Call the clearTransact() function on the CustomerLcdFragment
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                clearPayment(); // Call the clearTransact() function on the CustomerLcdFragment
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }

            return true;
        }/*else if
        (id == R.id.open_drawer) {
            int levelNumber = Integer.parseInt(cashierLevel);
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

            String Activity="openDrawer_";

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
                    try {
                                            Log.d("drawer26", "drawer26");

                        woyouService.openDrawer(null);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                try {
                                        Log.d("drawer27", "drawer27");

                    woyouService.openDrawer(null);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }

            return true;
        }*/
        /*else if
        (id == R.id.Report) {
            int levelNumber = Integer.parseInt(cashierLevel);
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

            String Activity="report_";

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
                    Context context = getContext(); // Get the Context object
                    Intent intent = new Intent(context, ReportActivity.class);
                    startActivity(intent);
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                Context context = getContext(); // Get the Context object
                Intent intent = new Intent(context, ReportActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }


            return true;
        }*/ else if (id == R.id.CashiorSalesReport) {
            int levelNumber = Integer.parseInt(cashierLevel);
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

            String Activity="cashierSalesReport_";

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
                            // Create a dialog with a custom layout
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            LayoutInflater inflater = requireActivity().getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.cashior_report, null);
                            builder.setView(dialogView);

                            spinnerReportType = dialogView.findViewById(R.id.spinnerReportType);
                            cashiorspinnerReportType = dialogView.findViewById(R.id.cashiorspinnerReportType);
                            recyclerViewReports = dialogView.findViewById(R.id.recyclerViewSalesReport);
                            secondRecyclerView = dialogView.findViewById(R.id.secondRecyclerView);

                            // Initialize the database helper
                            mDatabaseHelper = new DatabaseHelper(requireContext());

                            // Populate spinner with report types (daily, weekly, monthly, yearly)
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                                    R.array.report_types, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerReportType.setAdapter(adapter);


                            // Fetch cashiers from the database
                            List<Cashier> cashiers = fetchCashiers();
                            ArrayAdapter<Cashier> cashierAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cashiers);
                            cashierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cashiorspinnerReportType.setAdapter(cashierAdapter);
                            // Set up RecyclerViews and their adapters
                            setUpRecyclerViews(dialogView,1);

                            // Variables to hold the selected report type, formatted total tax, and formatted total amount
                            final String[] selectedReportType = new String[1];
                            final int[] selectedCashierId = new int[1];
                            final String[] formattedTotalTax = new String[1];
                            final String[] formattedTotalAmount = new String[1];
                            final String[] formattednewTotalAmount = new String[1];

// Set the spinner's item selected listener for report types
                            spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    // Fetch data based on the selected report type
                                    selectedReportType[0] = spinnerReportType.getSelectedItem().toString();



                                    // Fetch data based on the selected report type and cashier ID
                                    List<DataModel> newDataList = fetchDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);
                                    List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);

                                    // Log the fetched data for debugging

                                    // Update the adapter with the new data
                                    reportAdapter.updateData(newDataList);
                                    paymentMethodAdapter.updateData(newDataLists);

                                    // Fetch and update total tax and total amount

                                    double totalamountwithoutTax = mDatabaseHelper.getSumOfTransactionTotalHTA(String.valueOf(selectedCashierId[0]),selectedReportType[0] );
                                    double totalAmount = mDatabaseHelper.getSumOfTransactionTotalTTC(String.valueOf(selectedCashierId[0]), selectedReportType[0]);
                                    double totalTax=totalAmount-totalamountwithoutTax;
                                    double newtotalAmount= totalAmount ;

                                    // Update TextViews
                                    TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                                    TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                                    // Format totalTax and totalAmount to display only two decimal places
                                    formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                                    formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", totalAmount);
                                    formattednewTotalAmount[0]= String.format(Locale.getDefault(), "%.2f", newtotalAmount);
                                    textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                                    textViewTotalAmount.setText("Total Amount: Rs " + formattednewTotalAmount[0]);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // Do nothing here
                                }
                            });

// Set the spinner's item selected listener for cashiers
                            cashiorspinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    // Fetch the selected cashier
                                    Cashier selectedCashier = (Cashier) cashiorspinnerReportType.getSelectedItem();
                                    selectedCashierId[0] = selectedCashier.getId();

                                    // Log the selected cashier ID for debugging
                                    // Log the selected report type for debugging

                                    // Fetch data based on the selected report type and cashier ID
                                    List<DataModel> newDataList = fetchDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);
                                    List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);

                                    // Log the fetched data for debugging

                                    // Update the adapter with the new data
                                    reportAdapter.updateData(newDataList);
                                    paymentMethodAdapter.updateData(newDataLists);

                                    double totalamountwithoutTax = mDatabaseHelper.getSumOfTransactionTotalHTA(String.valueOf(selectedCashierId[0]),selectedReportType[0] );
                                    double totalAmount = mDatabaseHelper.getSumOfTransactionTotalTTC(String.valueOf(selectedCashierId[0]), selectedReportType[0]);
                                    double totalTax=totalAmount-totalamountwithoutTax;
                                    double newtotalAmount= totalAmount ;

                                    // Update TextViews
                                    TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                                    TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                                    // Format totalTax and totalAmount to display only two decimal places
                                    formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                                    formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", totalAmount);
                                    formattednewTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", newtotalAmount);

                                    textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                                    textViewTotalAmount.setText("Total Amount: Rs " + formattednewTotalAmount[0]);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // Do nothing hereF
                                }
                            });

                            builder.setTitle("Item Sales Report Per Cashior")
                                    .setPositiveButton("OK", (dialog, which) -> {

                                    })
                                    .setNegativeButton("Cancel", null) // Optional: Add a cancel button
                                    .show();

                            Button extraButton = dialogView.findViewById(R.id.extraButton);
                            extraButton.setOnClickListener(v -> {
                                Configuration configuration = getResources().getConfiguration();
                                Locale currentLocale = configuration.locale;

                                // Start the PrintDailyReport activity
                                Intent intent = new Intent(getContext(), PrintDailyReportPerCashior.class);
                                intent.putExtra("locale", currentLocale.toString());
                                intent.putExtra("reportType", selectedReportType[0]);
                                intent.putExtra("Cashior", selectedCashierId[0]);
                                intent.putExtra("totalTax", formattedTotalTax[0]);
                                intent.putExtra("totalAmount", formattedTotalAmount[0]);
                                startActivity(intent);
                            });
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                // Create a dialog with a custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.cashior_report, null);
                builder.setView(dialogView);

                spinnerReportType = dialogView.findViewById(R.id.spinnerReportType);
                cashiorspinnerReportType = dialogView.findViewById(R.id.cashiorspinnerReportType);
                recyclerViewReports = dialogView.findViewById(R.id.recyclerViewSalesReport);
                secondRecyclerView = dialogView.findViewById(R.id.secondRecyclerView);

                // Initialize the database helper
                mDatabaseHelper = new DatabaseHelper(requireContext());

                // Populate spinner with report types (daily, weekly, monthly, yearly)
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                        R.array.report_types, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerReportType.setAdapter(adapter);


                // Fetch cashiers from the database
                List<Cashier> cashiers = fetchCashiers();
                ArrayAdapter<Cashier> cashierAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cashiers);
                cashierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cashiorspinnerReportType.setAdapter(cashierAdapter);
                // Set up RecyclerViews and their adapters
                setUpRecyclerViews(dialogView,1);
                // Variable to hold the selected report type
                // Variable to hold the selected report type
                // Variables to hold the selected report type, formatted total tax, and formatted total amount
                final String[] selectedReportType = new String[1];
                final int[] selectedCashierId = new int[1];
                final String[] formattedTotalTax = new String[1];
                final String[] formattedTotalAmount = new String[1];
                final String[] formattednewTotalAmount = new String[1];

// Set the spinner's item selected listener for report types
                spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Fetch data based on the selected report type
                        selectedReportType[0] = spinnerReportType.getSelectedItem().toString();



                        // Fetch data based on the selected report type and cashier ID
                        List<DataModel> newDataList = fetchDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);
                        List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);

                        // Log the fetched data for debugging

                        // Update the adapter with the new data
                        reportAdapter.updateData(newDataList);
                        paymentMethodAdapter.updateData(newDataLists);

                        // Fetch and update total tax and total amount

                        double totalamountwithoutTax = mDatabaseHelper.getSumOfTransactionTotalHTA(String.valueOf(selectedCashierId[0]),selectedReportType[0] );
                        double totalAmount = mDatabaseHelper.getSumOfTransactionTotalTTC(String.valueOf(selectedCashierId[0]), selectedReportType[0]);
                        double totalTax=totalAmount-totalamountwithoutTax;
                        double newtotalAmount= totalAmount ;

                        // Update TextViews
                        TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                        TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                        // Format totalTax and totalAmount to display only two decimal places
                        formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                        formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", totalAmount);
                        formattednewTotalAmount[0]= String.format(Locale.getDefault(), "%.2f", newtotalAmount);
                        textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                        textViewTotalAmount.setText("Total Amount: Rs " + formattednewTotalAmount[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing here
                    }
                });

// Set the spinner's item selected listener for cashiers
                cashiorspinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Fetch the selected cashier
                        Cashier selectedCashier = (Cashier) cashiorspinnerReportType.getSelectedItem();
                        selectedCashierId[0] = selectedCashier.getId();

                        // Log the selected cashier ID for debugging
                        // Log the selected report type for debugging

                        // Fetch data based on the selected report type and cashier ID
                        List<DataModel> newDataList = fetchDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);
                        List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportTypeAndCashier(selectedReportType[0], selectedCashierId[0]);

                        // Log the fetched data for debugging

                        // Update the adapter with the new data
                        reportAdapter.updateData(newDataList);
                        paymentMethodAdapter.updateData(newDataLists);

                        // Fetch and update total tax and total amount
                        double totalamountwithoutTax = mDatabaseHelper.getSumOfTransactionTotalHTA(String.valueOf(selectedCashierId[0]),selectedReportType[0] );
                        double totalAmount = mDatabaseHelper.getSumOfTransactionTotalTTC(String.valueOf(selectedCashierId[0]), selectedReportType[0]);
                        double totalTax=totalAmount-totalamountwithoutTax;
                        double newtotalAmount= totalAmount ;

                        // Update TextViews
                        TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                        TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                        // Format totalTax and totalAmount to display only two decimal places
                        formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                        formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", totalAmount);
                        formattednewTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", newtotalAmount);

                        textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                        textViewTotalAmount.setText("Total Amount: Rs " + formattednewTotalAmount[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing here
                    }
                });

                builder.setTitle("Item Sales Report Per Cashior")
                        .setPositiveButton("OK", (dialog, which) -> {

                        })
                        .setNegativeButton("Cancel", null) // Optional: Add a cancel button
                        .show();

                Button extraButton = dialogView.findViewById(R.id.extraButton);
                extraButton.setOnClickListener(v -> {
                    Configuration configuration = getResources().getConfiguration();
                    Locale currentLocale = configuration.locale;

                    // Start the PrintDailyReport activity
                    Intent intent = new Intent(getContext(), PrintDailyReportPerCashior.class);
                    intent.putExtra("locale", currentLocale.toString());
                    intent.putExtra("reportType", selectedReportType[0]);
                    intent.putExtra("Cashior", selectedCashierId[0]);
                    intent.putExtra("totalTax", formattedTotalTax[0]);
                    intent.putExtra("totalAmount", formattedTotalAmount[0]);
                    startActivity(intent);
                });
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }


            return true;
        }
        else if (id == R.id.SalesReport) {
            int levelNumber = Integer.parseInt(cashierLevel);
            String Activity="salesReport_";
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
            if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {
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
                    setUpRecyclerViews(dialogView,1);

                    // Variables to hold the selected report type, formatted total tax, and formatted total amount
                    final String[] selectedReportType = new String[1];
                    final String[] formattedTotalTax = new String[1];
                    final String[] formattedTotalAmount = new String[1];

                    // Set the spinner's item selected listener
                    spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // Fetch data based on the selected report type and update the adapter
                            selectedReportType[0] = spinnerReportType.getSelectedItem().toString();

                            // Log the selected report type for debugging

                            // Fetch data based on the selected report type
                            List<DataModel> newDataList = fetchDataBasedOnReportType(selectedReportType[0]);
                            List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportType(selectedReportType[0]);
                            // Log the fetched data for debugging

                            // Update the adapter with the new data
                            reportAdapter.updateData(newDataList);
                            paymentMethodAdapter.updateData(newDataLists);

                            // Assuming you have a method to fetch total tax and total amount based on report type
                            double totalamountwithoutTax = mDatabaseHelper.getSumOfTransactionTotalHTAWithoutShift(selectedReportType[0]);
                            double totalAmount = mDatabaseHelper.getSumOfTransactionTotalTTCWithoutShift(selectedReportType[0]);
                            double totalTax=totalAmount-totalamountwithoutTax;
                            double newtotalAmount= totalAmount ;
                            // Update TextViews
                            TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                            TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                            // Format totalTax and totalAmount to display only two decimal places
                            formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                            formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", newtotalAmount);

                            textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                            textViewTotalAmount.setText("Total Amount: Rs " + formattedTotalAmount[0]);
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

                    Button extraButton = dialogView.findViewById(R.id.extraButton);
                    extraButton.setOnClickListener(v -> {
                        Configuration configuration = getResources().getConfiguration();
                        Locale currentLocale = configuration.locale;

                        // Start the PrintDailyReport activity
                        Intent intent = new Intent(getContext(), PrintDailyReport.class);
                        intent.putExtra("locale", currentLocale.toString());
                        intent.putExtra("reportType", selectedReportType[0]);
                        intent.putExtra("totalTax", formattedTotalTax[0]);
                        intent.putExtra("totalAmount", formattedTotalAmount[0]);
                        startActivity(intent);
                    });
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
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
                setUpRecyclerViews(dialogView,1);

                // Variables to hold the selected report type, formatted total tax, and formatted total amount
                final String[] selectedReportType = new String[1];
                final String[] formattedTotalTax = new String[1];
                final String[] formattedTotalAmount = new String[1];

                // Set the spinner's item selected listener
                spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Fetch data based on the selected report type and update the adapter
                        selectedReportType[0] = spinnerReportType.getSelectedItem().toString();

                        // Log the selected report type for debugging

                        // Fetch data based on the selected report type
                        List<DataModel> newDataList = fetchDataBasedOnReportType(selectedReportType[0]);
                        List<PaymentMethodDataModel> newDataLists = fetchPaymentMethodDataBasedOnReportType(selectedReportType[0]);
                        // Log the fetched data for debugging

                        // Update the adapter with the new data
                        reportAdapter.updateData(newDataList);
                        paymentMethodAdapter.updateData(newDataLists);

                        // Assuming you have a method to fetch total tax and total amount based on report type
                        double totalamountwithoutTax = mDatabaseHelper.getSumOfTransactionTotalHTAWithoutShift(selectedReportType[0]);
                        double totalAmount = mDatabaseHelper.getSumOfTransactionTotalTTCWithoutShift(selectedReportType[0]);
                        double totalTax=totalAmount-totalamountwithoutTax;
                        double newtotalAmount= totalAmount ;
                        // Update TextViews
                        TextView textViewTotalTax = dialogView.findViewById(R.id.textViewTotalTax);
                        TextView textViewTotalAmount = dialogView.findViewById(R.id.totalAmounttextview);

                        // Format totalTax and totalAmount to display only two decimal places
                        formattedTotalTax[0] = String.format(Locale.getDefault(), "%.2f", totalTax);
                        formattedTotalAmount[0] = String.format(Locale.getDefault(), "%.2f", newtotalAmount);

                        textViewTotalTax.setText("Total Tax: Rs " + formattedTotalTax[0]);
                        textViewTotalAmount.setText("Total Amount: Rs " + formattedTotalAmount[0]);
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

                Button extraButton = dialogView.findViewById(R.id.extraButton);
                extraButton.setOnClickListener(v -> {
                    Configuration configuration = getResources().getConfiguration();
                    Locale currentLocale = configuration.locale;

                    // Start the PrintDailyReport activity
                    Intent intent = new Intent(getContext(), PrintDailyReport.class);
                    intent.putExtra("locale", currentLocale.toString());
                    intent.putExtra("reportType", selectedReportType[0]);
                    intent.putExtra("totalTax", formattedTotalTax[0]);
                    intent.putExtra("totalAmount", formattedTotalAmount[0]);
                    startActivity(intent);
                });
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
            }


            return true;
        }
        else if (id == R.id.SplitBill) {

            AtomicReference<SharedPreferences> preferences = new AtomicReference<>(getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE));
            AtomicInteger roomid = new AtomicInteger(preferences.get().getInt("roomnum", 0));
            AtomicReference<String> tableid = new AtomicReference<>(preferences.get().getString("table_id", ""));
            boolean isalreadysplitted=false;

            if (roomid.get() != 0 && tableid.get() != null && !"0".equals(tableid) && !tableid.get().isEmpty()) {
                String transactionIdInProgress1 = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid.get()), tableid.get());

                if (transactionIdInProgress1 != null && !transactionIdInProgress1.isEmpty()) {
                    isalreadysplitted = mDatabaseHelper.isTransactionSplitted(transactionIdInProgress1);
                } else {
                    Toast.makeText(getContext(), "Transaction ID is invalid or not found.", Toast.LENGTH_LONG).show();
                    return false;
                }

            } else {
                // Show a Toast message if roomid or tableid is invalid
                Toast.makeText(getContext(), "Please select both room and table.", Toast.LENGTH_LONG).show();
                return false;
            }

            int levelNumber = Integer.parseInt(cashierLevel);
            String Activity="splitBill_";
            SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
            SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);


            boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
            boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
           if(isalreadysplitted){
               Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();

           }else  if (canHigherAccessReceipt ) {
                showPinDialog(Activity, () -> {

                    // Create a dialog with a custom layout
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    LayoutInflater inflater = requireActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.splitbill, null);
                    builder.setView(dialogView);

                    // Find the RecyclerView
                    splitBillRecyclerView = dialogView.findViewById(R.id.splitBillRecyclerView);

                    // Fetch the transaction status and ID
                    String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), String.valueOf(tableid));
                    String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), String.valueOf(tableid), statusType);

                    // Fetch the data using the transaction ID
                    Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId, String.valueOf(roomid), String.valueOf(tableid));

                    // Check if the RecyclerView exists in the provided view
                    RecyclerView splitBillRecyclerView = dialogView.findViewById(R.id.splitBillRecyclerView);

                    // Ensure the RecyclerView is not null
                    if (splitBillRecyclerView != null) {
                        // Set up the adapter with the cursor
                        mSplitBillAdapter = new SplitBillTicketAdapter(getContext(), cursor);

                        // Configure the RecyclerView layout and adapter
                        splitBillRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        splitBillRecyclerView.setAdapter(mSplitBillAdapter);
                    } else {
                        Log.e("SplitBillDebug", "RecyclerView not found in the provided dialog view.");
                    }
                    // Fetch the data and pass it to the adapter
                 //   fetchDataAndUpdateRecyclerView();
                  //  setUpSplitBillRecyclerView(dialogView);
                    numberOfPeopleEditText = dialogView.findViewById(R.id.editTextNumberOfPeople);
                    numberOfPeopleEditText.setInputType(InputType.TYPE_NULL);
                    numberOfPeopleEditText.setTextIsSelectable(true);
                    enterednumber = new StringBuilder();
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
                    Button buttonClear = dialogView.findViewById(R.id.buttonClear);


                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("1");
                        }
                    });

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("2");
                        }
                    });

                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("3");
                        }
                    });

                    button4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("4");
                        }
                    });

                    button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("5");
                        }
                    });

                    button6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("6");
                        }
                    });

                    button7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("7");
                        }
                    });

                    button8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("8");
                        }
                    });

                    button9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("9");
                        }
                    });

                    button0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberButtonClick("0");
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


                    // Set up the dialog's UI elements
                    EditText numberOfPeopleEditText = dialogView.findViewById(R.id.editTextNumberOfPeople);
                    TextView resultTextView = dialogView.findViewById(R.id.resultTextView);
                    EditText splitAmountEditText = dialogView.findViewById(R.id.editTextSplitAmount);
                    EditText splitTaxtotalAmount = dialogView.findViewById(R.id.editTextSplitVat);
                    Button splitButton = dialogView.findViewById(R.id.splitButton);

                    // Retrieve roomid and tableid from SharedPreferences
                    preferences.set(getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE));
                    roomid.set(preferences.get().getInt("roomnum", 0));
                    tableid.set(preferences.get().getString("table_id", ""));

// Retrieve the total amount and total tax amount from the transactionheader table
                    double totalAmount = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid.get()), tableid.get());
                    double TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid.get()), tableid.get());


// Alternatively, format the values as strings if needed for display
                    String formattedTotalAmount = String.format("%.2f", totalAmount);
                    String formattedTaxTotalAmount = String.format("%.2f", TaxtotalAmount);

                    splitAmountEditText.setText(String.valueOf(formattedTotalAmount));
                    splitTaxtotalAmount.setText(String.valueOf(formattedTaxTotalAmount));

                    splitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
// Get the input from EditText
                            String numberOfPeopleStr = numberOfPeopleEditText.getText().toString();

                            // Check if the input is empty
                            if (numberOfPeopleStr.isEmpty()) {
                                // Show a Toast message to the user if the input is empty
                                Toast.makeText(getContext(), "Please enter the number of people", Toast.LENGTH_SHORT).show();
                                return; // Exit the method to avoid further processing
                            }

                            try {
                                // Format the current date and time as per your requirement
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String currentDate = dateFormat.format(new Date());
                                double totalAmount = Double.parseDouble(splitAmountEditText.getText().toString());
                                double totalTax = Double.parseDouble(splitTaxtotalAmount.getText().toString());
                                int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                                double splitAmounts = totalAmount / numberOfPeople;
                                BigDecimal SpliTAmountRounded = new BigDecimal(splitAmounts).setScale(2, RoundingMode.HALF_UP);
                                double splitAmount = SpliTAmountRounded.doubleValue();
                                double SpliVats = totalTax / numberOfPeople;
                                BigDecimal SpliVatRounded = new BigDecimal(SpliVats).setScale(2, RoundingMode.HALF_UP);
                                double SpliVat = SpliVatRounded.doubleValue();
                                SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                                int roomid = preferences.getInt("roomnum", 0);
                                String tableid = preferences.getString("table_id", "");
                                String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                                String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                                String transactionIdInProgress1 = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);

                                String transactionIdInProgress = latesttransId;
                                StringBuilder resultTextBuilder = new StringBuilder();
                                ContentValues values1 = new ContentValues();
                                values1.put(TRANSACTION_STATUS, "Splitted");

                                int rowsAffected = mDatabaseHelper.getWritableDatabase().update(
                                        TRANSACTION_TABLE_NAME,
                                        values1,
                                        ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_ID + " = ?",
                                        new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                                );
                                for (int i = 0; i < numberOfPeople; i++) {
                                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                    cashierId = sharedPreference.getString("cashorId", null);

                                    String ShopName = sharedPreference.getString("ShopName", null);

                                    Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                    if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                        int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                        String MRAMETHOD = "Single";
                                        String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                        // Insert new records
                                        ContentValues values = new ContentValues();
                                        values.put(ROOM_ID, roomid);
                                        values.put(TABLE_ID, tableid);
                                        values.put(TRANSACTION_ID, transactionIdInProgress);
                                        long uniqueItemId = System.currentTimeMillis();
                                        values.put(ITEM_ID, 2131);
                                        values.put(TRANSACTION_DATE, currentDate);
                                        values.put(QUANTITY, 1);
                                        values.put(TRANSACTION_QUANTITY, 1);
                                        values.put(TOTAL_PRICE, splitAmount);
                                        values.put(TRANSACTION_UNIT_PRICE, splitAmount);
                                        values.put(TRANSACTION_STATUS, "VALID");
                                        values.put(TRANSACTION_BARCODE, "0001112233344");

                                        String vat = String.valueOf(calculateTaxforsplit(String.valueOf(splitAmount), "VAT 15%"));
                                        values.put(VAT, vat);
                                        double priceWithoutVat= splitAmount - calculateTaxforsplit(String.valueOf(splitAmount) , "VAT 15%");
                                        values.put(TRANSACTION_TOTAL_HT_A, priceWithoutVat);
                                        values.put(TRANSACTION_TOTAL_HT_B, priceWithoutVat);
                                        values.put(VAT_Type, "VAT 15%");
                                        values.put(LongDescription, "Menu Repas");
                                        values.put(TRANSACTION_DESCRIPTION, "Menu Repas");
                                        values.put(TRANSACTION_TOTALIZER, "SALES");
                                        // values.put(TRANSACTION_VAT_BEFORE_DISC, taxwithoutdiscount);
                                        values.put(TRANSACTION_SHOP_NO, columnCompanyShopNumber);
                                        values.put(TRANSACTION_NATURE, "GOODS");
                                        values.put(TRANSACTION_TOTAL_TTC, splitAmount);
                                        double roundedUnitPrice = Math.round(splitAmount * 100.0) / 100.0;
                                        values.put(TRANSACTION_UNIT_PRICE, roundedUnitPrice);
                                        values.put(TRANSACTION_TERMINAL_NO, PosNum);
                                        values.put(TRANSACTION_STATUS, "VALID");
                                        values.put(IS_PAID, 0); // Add IS_PAID to the ContentValues
                                        String taxCode="TC01";

                                        values.put(TRANSACTION_ITEM_CODE, 999);
                                        values.put(TRANSACTION_CURRENCY, "MUR");
                                        values.put(TRANSACTION_TAX_CODE, taxCode);
                                        values.put(PriceAfterDiscount, splitAmount);
                                        values.put(TRANSACTION_TYPE_TAX, taxCode);
                                        values.put(TRANSACTION_IS_TAXABLE, taxCode.equals("TC01") ? 1 : 0); // If taxCode is "TC01", set to 1, otherwise set to 0

                                        values.put(TRANSACTION_FAMILLE, 0);
                                        values.put(TRANSACTION_VAT_AFTER_DISC, vat);

                                        // Get current date and time
                                        Date currentDate1 = new Date();
                                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat timeFormat1 = new SimpleDateFormat("HH:mm:ss");

                                        // Format date and time
                                        String formattedDate = dateFormat1.format(currentDate1);
                                        String formattedTime = timeFormat1.format(currentDate1);
                                        values.put(TRANSACTION_DATE_TRANSACTION, formattedDate);
                                        values.put(TRANSACTION_TIME_TRANSACTION, formattedTime);
                                        values.put(TRANSACTION_DATE_CREATED, formattedDate);
                                        values.put(TRANSACTION_DATE_MODIFIED, formattedDate);
                                        values.put(TRANSACTION_TIME_CREATED, formattedTime);
                                        values.put(TRANSACTION_TIME_MODIFIED, formattedTime);
                                        values.put(TRANSACTION_CODE, transactionIdInProgress);
                                        values.put(TRANSACTION_WEIGHTS, 0.0);
                                        double roundedTotalDiscount = Math.round(splitAmount * 100.0) / 100.0;
                                        values.put(TRANSACTION_TOTAL_DISCOUNT, 0);
                                        values.put(TRANSACTION_DISCOUNT, 0);

                                        // Parse VAT and VAT_Type from existing records
                                        Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery(
                                                "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                                                        " WHERE " + ROOM_ID + " = ? AND " +
                                                        TABLE_ID + " = ? AND " +
                                                        TRANSACTION_ID + " = ?",
                                                new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                                        );

                                        values.put(VAT, SpliVat); // Replace with default value or retrieve from somewhere else
                                        if (SpliVat < 0) {
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
                                    refreshData(totalAmount, TaxtotalAmount,"movetobottom");

                                }
                            } catch (NumberFormatException e) {
                                // Show a Toast message in case of a NumberFormatException
                                Toast.makeText(getContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setTitle("Split Bill")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()) // Optional: Add a cancel button
                            .show();
                });
            }
            // Check permission for Receipts
            else if (!canHigherAccessReceipt && canAccessReceipt) {
                // Create a dialog with a custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.splitbill, null);
                builder.setView(dialogView);
               // Find the RecyclerView
               splitBillRecyclerView = dialogView.findViewById(R.id.splitBillRecyclerView);

               // Fetch the transaction status and ID
               String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), String.valueOf(tableid));
               String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), String.valueOf(tableid), statusType);

               // Fetch the data using the transaction ID
               Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId, String.valueOf(roomid), String.valueOf(tableid));

               // Check if the RecyclerView exists in the provided view
               RecyclerView splitBillRecyclerView = dialogView.findViewById(R.id.splitBillRecyclerView);

               // Ensure the RecyclerView is not null
               if (splitBillRecyclerView != null) {
                   // Set up the adapter with the cursor
                   mSplitBillAdapter = new SplitBillTicketAdapter(getContext(), cursor);

                   // Configure the RecyclerView layout and adapter
                   splitBillRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                   splitBillRecyclerView.setAdapter(mSplitBillAdapter);
               } else {
                   Log.e("SplitBillDebug", "RecyclerView not found in the provided dialog view.");
               }
                numberOfPeopleEditText = dialogView.findViewById(R.id.editTextNumberOfPeople);
                numberOfPeopleEditText.setInputType(InputType.TYPE_NULL);
                numberOfPeopleEditText.setTextIsSelectable(true);
                enterednumber = new StringBuilder();
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
                Button buttonClear = dialogView.findViewById(R.id.buttonClear);


                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("1");
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("2");
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("3");
                    }
                });

                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("4");
                    }
                });

                button5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("5");
                    }
                });

                button6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("6");
                    }
                });

                button7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("7");
                    }
                });

                button8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("8");
                    }
                });

                button9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("9");
                    }
                });

                button0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNumberButtonClick("0");
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


                // Set up the dialog's UI elements
                EditText numberOfPeopleEditText = dialogView.findViewById(R.id.editTextNumberOfPeople);
                TextView resultTextView = dialogView.findViewById(R.id.resultTextView);
                EditText splitAmountEditText = dialogView.findViewById(R.id.editTextSplitAmount);
                EditText splitTaxtotalAmount = dialogView.findViewById(R.id.editTextSplitVat);
                Button splitButton = dialogView.findViewById(R.id.splitButton);
                // Retrieve roomid and tableid from SharedPreferences
                preferences.set(getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE));
                roomid.set(preferences.get().getInt("roomnum", 0));
                tableid.set(preferences.get().getString("table_id", ""));

// Retrieve the total amount and total tax amount from the transactionheader table
                double totalAmount = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid.get()), tableid.get());
                double TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid.get()), tableid.get());
               String formattedTotalAmount = String.format("%.2f", totalAmount);
               String formattedTaxTotalAmount = String.format("%.2f", TaxtotalAmount);

               splitAmountEditText.setText(String.valueOf(formattedTotalAmount));
               splitTaxtotalAmount.setText(String.valueOf(formattedTaxTotalAmount));



                splitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
// Get the input from EditText
                        String numberOfPeopleStr = numberOfPeopleEditText.getText().toString();

                        // Check if the input is empty
                        if (numberOfPeopleStr.isEmpty()) {
                            // Show a Toast message to the user if the input is empty
                            Toast.makeText(getContext(), "Please enter the number of people", Toast.LENGTH_SHORT).show();
                            return; // Exit the method to avoid further processing
                        }

                        try {
                            // Format the current date and time as per your requirement
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String currentDate = dateFormat.format(new Date());
                            double totalAmount = Double.parseDouble(splitAmountEditText.getText().toString());
                            double totalTax = Double.parseDouble(splitTaxtotalAmount.getText().toString());
                            int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                            double splitAmounts = totalAmount / numberOfPeople;
                            BigDecimal SpliTAmountRounded = new BigDecimal(splitAmounts).setScale(2, RoundingMode.HALF_UP);
                            double splitAmount = SpliTAmountRounded.doubleValue();
                            double SpliVats = totalTax / numberOfPeople;
                            BigDecimal SpliVatRounded = new BigDecimal(SpliVats).setScale(2, RoundingMode.HALF_UP);
                            double SpliVat = SpliVatRounded.doubleValue();
                            SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                            int roomid = preferences.getInt("roomnum", 0);
                            String tableid = preferences.getString("table_id", "");
                            String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                            String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                            String transactionIdInProgress1 = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);

                            String transactionIdInProgress = latesttransId;
                            StringBuilder resultTextBuilder = new StringBuilder();
                            ContentValues values1 = new ContentValues();
                            values1.put(TRANSACTION_STATUS, "Splitted");

                            int rowsAffected = mDatabaseHelper.getWritableDatabase().update(
                                    TRANSACTION_TABLE_NAME,
                                    values1,
                                    ROOM_ID + " = ? AND " + TABLE_ID + " = ? AND " + TRANSACTION_ID + " = ?",
                                    new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                            );
                            for (int i = 0; i < numberOfPeople; i++) {
                                SharedPreferences sharedPreference = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                cashierId = sharedPreference.getString("cashorId", null);

                                String ShopName = sharedPreference.getString("ShopName", null);

                                Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);
                                if (cursorCompany != null && cursorCompany.moveToFirst()) {
                                    int columnCompanyShopNumber = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_SHOPNUMBER);

                                    String MRAMETHOD = "Single";
                                    String CompanyShopNumber = cursorCompany.getString(columnCompanyShopNumber);
                                    // Insert new records
                                    ContentValues values = new ContentValues();
                                    values.put(ROOM_ID, roomid);
                                    values.put(TABLE_ID, tableid);
                                    values.put(TRANSACTION_ID, transactionIdInProgress);
                                    long uniqueItemId = System.currentTimeMillis();
                                    values.put(ITEM_ID, 2131);
                                    values.put(TRANSACTION_DATE, currentDate);
                                    values.put(QUANTITY, 1);
                                    values.put(TRANSACTION_QUANTITY, 1);
                                    values.put(TOTAL_PRICE, splitAmount);
                                    values.put(TRANSACTION_UNIT_PRICE, splitAmount);
                                    values.put(TRANSACTION_STATUS, "VALID");
                                    values.put(TRANSACTION_BARCODE, "0001112233344");

                                    String vat = String.valueOf(calculateTaxforsplit(String.valueOf(splitAmount), "VAT 15%"));
                                    values.put(VAT, vat);
                                    double priceWithoutVat= splitAmount - calculateTaxforsplit(String.valueOf(splitAmount) , "VAT 15%");
                                    values.put(TRANSACTION_TOTAL_HT_A, priceWithoutVat);
                                    values.put(TRANSACTION_TOTAL_HT_B, priceWithoutVat);
                                    values.put(VAT_Type, "VAT 15%");
                                    values.put(LongDescription, "Menu Repas");
                                    values.put(TRANSACTION_DESCRIPTION, "Menu Repas");
                                    values.put(TRANSACTION_TOTALIZER, "SALES");
                                    // values.put(TRANSACTION_VAT_BEFORE_DISC, taxwithoutdiscount);
                                    values.put(TRANSACTION_SHOP_NO, columnCompanyShopNumber);
                                    values.put(TRANSACTION_NATURE, "GOODS");
                                    values.put(TRANSACTION_TOTAL_TTC, splitAmount);
                                    double roundedUnitPrice = Math.round(splitAmount * 100.0) / 100.0;
                                    values.put(TRANSACTION_UNIT_PRICE, roundedUnitPrice);
                                    values.put(TRANSACTION_TERMINAL_NO, PosNum);
                                    values.put(TRANSACTION_STATUS, "VALID");
                                    values.put(IS_PAID, 0); // Add IS_PAID to the ContentValues
                                    String taxCode="TC01";

                                    values.put(TRANSACTION_ITEM_CODE, 999);
                                    values.put(TRANSACTION_CURRENCY, "MUR");
                                    values.put(TRANSACTION_TAX_CODE, taxCode);
                                    values.put(PriceAfterDiscount, splitAmount);
                                    values.put(TRANSACTION_TYPE_TAX, taxCode);
                                    values.put(TRANSACTION_IS_TAXABLE, taxCode.equals("TC01") ? 1 : 0); // If taxCode is "TC01", set to 1, otherwise set to 0

                                    values.put(TRANSACTION_FAMILLE, 0);
                                    values.put(TRANSACTION_VAT_AFTER_DISC, vat);

                                    // Get current date and time
                                    Date currentDate1 = new Date();
                                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat timeFormat1 = new SimpleDateFormat("HH:mm:ss");

                                    // Format date and time
                                    String formattedDate = dateFormat1.format(currentDate1);
                                    String formattedTime = timeFormat1.format(currentDate1);
                                    values.put(TRANSACTION_DATE_TRANSACTION, formattedDate);
                                    values.put(TRANSACTION_TIME_TRANSACTION, formattedTime);
                                    values.put(TRANSACTION_DATE_CREATED, formattedDate);
                                    values.put(TRANSACTION_DATE_MODIFIED, formattedDate);
                                    values.put(TRANSACTION_TIME_CREATED, formattedTime);
                                    values.put(TRANSACTION_TIME_MODIFIED, formattedTime);
                                    values.put(TRANSACTION_CODE, transactionIdInProgress);
                                    values.put(TRANSACTION_WEIGHTS, 0.0);
                                    double roundedTotalDiscount = Math.round(splitAmount * 100.0) / 100.0;
                                    values.put(TRANSACTION_TOTAL_DISCOUNT, 0);
                                    values.put(TRANSACTION_DISCOUNT, 0);

                                    // Parse VAT and VAT_Type from existing records
                                    Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery(
                                            "SELECT * FROM " + TRANSACTION_TABLE_NAME +
                                                    " WHERE " + ROOM_ID + " = ? AND " +
                                                    TABLE_ID + " = ? AND " +
                                                    TRANSACTION_ID + " = ?",
                                            new String[]{String.valueOf(roomid), tableid, transactionIdInProgress}
                                    );

                                    values.put(VAT, SpliVat); // Replace with default value or retrieve from somewhere else
                                    if (SpliVat < 0) {
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
                                refreshData(totalAmount, TaxtotalAmount,"movetobottom");

                            }
                        } catch (NumberFormatException e) {
                            // Show a Toast message in case of a NumberFormatException
                            Toast.makeText(getContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setTitle("Split Bill")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()) // Optional: Add a cancel button
                        .show();
            } else {
                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
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
        Drawable yourDrawable = ContextCompat.getDrawable(getContext(), R.drawable.baseline_menu_book_24);

// Set your custom drawable as the overflow icon
        toolbar.setOverflowIcon(yourDrawable);
        toolbar.setOnMenuItemClickListener(this);

        validate=view.findViewById(R.id.val_layout);

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

        separationline=view.findViewById(R.id.sep);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        SplittedItemsRecycleView=view.findViewById(R.id.splittedrecycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SplittedItemsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        totalntax = view.findViewById(R.id.totalntax);
        FooterLayout = view.findViewById(R.id.footer_layout); // Initialize the container layout
        EmptyFooter=view.findViewById(R.id.emptyfooter_layout);
        SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreference.getString(TRANSACTION_ID_KEY, null);
        Cursor cursor1,cursorsplitted;

            String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
            String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
            cursor1 = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
        cursorsplitted= mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
        Cursor  filteredCursor = mDatabaseHelper.getTablesFilteredByMerged(String.valueOf(roomid));



        actualdate = mDatabaseHelper.getCurrentDate();

        mAdapter = new TicketAdapter(getActivity(), cursor1);
        mSplittedAdapter=new SplittedTicketAdapter(getActivity(),cursorsplitted);
        mRecyclerView.setAdapter(mAdapter);
        SplittedItemsRecycleView.setAdapter(mSplittedAdapter);
        mTableAdapter=new TableAdapter(getContext(),filteredCursor);
        dbManager = new DBManager(getContext());
        dbManager.open();

        textViewVATs=view.findViewById(R.id.textViewVATs);
        textViewTotals=view.findViewById(R.id.textViewTotals);
        textviewdiscount=view.findViewById(R.id.textViewdiscount);
        textViewVATs.setText(getString(R.string.tax) + " : Rs 0.00");
        textViewTotals.setText(getString(R.string.Total) + " : Rs 0.00");
        textviewdiscount.setText(getString(R.string.discount) + " : Rs 0.00");
        mAddItem = view.findViewById(R.id.fab);
        mcheckbox= view.findViewById(R.id.checkboxfab);

         sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierLevel = sharedPreference.getString("cashorlevel", null);
        shopname = sharedPreference.getString("ShopName", null);
        cashiername = sharedPreference.getString("cashorName", null);

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);



        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper




        // Retrieve the total amount and total tax amount from the transactionheader table

        double totalPriceSum = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
        double totalVATSum = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);
        if(latesttransId != null){
            double discount=mDatabaseHelper.getSumOfTransactionTotalDiscount(latesttransId);
            String formattedTotadisc = String.format("%.2f", discount);
            textviewdiscount.setText(getString(R.string.discount) + ": Rs " + formattedTotadisc);
        }else{
            textviewdiscount.setText(getString(R.string.discount) + ": Rs0.00 ");
        }

        TextView totalAmountTextView = view.findViewById(R.id.textViewTotal);
        String formattedTotalAmount = String.format("%.2f", totalPriceSum);
        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);
        // Update the tax and total amount TextViews
        TextView taxTextView = view.findViewById(R.id.textViewVAT);
        String formattedTaxAmount = String.format("%.2f", totalVATSum);
        taxTextView.setText(getString(R.string.tax) + ": Rs " + formattedTaxAmount);

        TextView roomTextView = view.findViewById(R.id.textViewRoom);
        roomTextView.setText("Room: " + roomid);

        TextView tableTextView = view.findViewById(R.id.textViewTable);
        tableTextView.setText(" - Table: " + tableid);

        TextView cashierTextView = view.findViewById(R.id.textViewCashier);
        cashierTextView.setText(" - Cashier: " + cashierId);
        // Load the data based on the transaction ID

            Cursor cursor2 = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
        cursorsplitted= mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);

            mAdapter.swapCursor(cursor2);
        mSplittedAdapter.swapCursor(cursorsplitted);




         emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
            FooterLayout.setVisibility(View.GONE);

            EmptyFooter.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            FooterLayout.setVisibility(View.VISIBLE);

            EmptyFooter.setVisibility(View.GONE);
        }
        if (mSplittedAdapter.getItemCount() <= 0) {
            SplittedItemsRecycleView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
            FooterLayout.setVisibility(View.GONE);

            EmptyFooter.setVisibility(View.VISIBLE);
        } else {
            SplittedItemsRecycleView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            FooterLayout.setVisibility(View.VISIBLE);
            EmptyFooter.setVisibility(View.GONE);

        }

        mcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                int roomid = preferences.getInt("roomnum", 0);
                String tableid = preferences.getString("table_id", "");
                String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                mDatabaseHelper.resetIsSelectedByTransactionId(latesttransId);
                TicketAdapter adapter = (TicketAdapter) mRecyclerView.getAdapter();
                adapter.setCheckBoxVisibility(false);
                adapter.uncheckAllCheckboxes();
                mcheckbox.setVisibility(View.GONE);
                refreshData(calculateTotalAmount(), calculateTotalTax(),"Notmovetobottom");
            }
        });
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
                        mDatabaseHelper.insertTransaction(null,Integer.parseInt(finalDepartmentid),"00",0,0,0,"0","Supplements", transactionIdInProgress, transactionDate, 1, enteredAmount, 0.00, selectedDepartment, enteredAmount, 0.00, selectedVAT, PosNum, selectedType, finalDepartmentid, "MUR","TC01",0.0,0.00, String.valueOf(roomid),tableid,0);


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
        Button validateButton = view.findViewById(R.id.ValidateTicket);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                printData();
            }

            private void printData() {

                SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                roomid = preferences.getInt("room_id", 0);
                tableid = preferences.getString("table_id", "0");
            String Roomid= String.valueOf(roomid);
                double totalPriceSum = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
                double totalVATSum = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);




                // Declare a variable to hold the total amount
                double totalAmountinserted = totalAmount;
                String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);


                String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                // Fetch data from the database
                List<DatabaseHelper.SettlementSummary> summaries = mDatabaseHelper.getSettlementSummaries(latesttransId, roomid, tableid);

// Create an ArrayList to hold SettlementItem objects
                ArrayList<SettlementItem> settlementItems = new ArrayList<>();

// Calculate the subtotal and create SettlementItem objects
                if (summaries != null && !summaries.isEmpty()) {
                    double sum = 0.0;

                    for (DatabaseHelper.SettlementSummary summary : summaries) {
                        sum += summary.sum;
                        settlementItems.add(new SettlementItem(summary.paymentName, summary.sum));
                    }

                    double subtotal = totalPriceSum- sum;
                    if(subtotal< 0){

                        subtotal=0;
                    }
                    CashReturnVal = sum - totalPriceSum;
                    if (subtotal < 0) {
                        CashReturnVal = -1 * subtotal;
                    }

                    // Add the subtotal as a SettlementItem
                    settlementItems.add(new SettlementItem("Subtotal", subtotal));
                }

// Example of how to print or use the ArrayList
                for (SettlementItem item : settlementItems) {
                    System.out.println("test"+ item);
                }

                // Print or use the total amount as needed

              double  cashReturn= CashReturnVal;

                System.out.println("selectedBuyer: " + selectedBuyer);
                if (selectedBuyer != null) {
                    // Retrieve the total amount and total tax amount from the transactionheader table
                    Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);
                    int currentCounter = 1; // Default value if no data is present in the table

                    if (cursor != null && cursor.moveToFirst()) {

                        int columnIndexInvoiceRef = cursor.getColumnIndex(TRANSACTION_INVOICE_REF);



                        String InvoiceRefIdentifyer = cursor.getString(columnIndexInvoiceRef);
                        // Access selectedBuyer properties here

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
                        Intent intent = new Intent(getActivity(), Mra.class);
                        intent.putExtra("roomid", Roomid);
                        intent.putExtra("tableid", tableid);
                        intent.putExtra("transactionIdInProgress", transactionIdInProgress);

                        System.out.println("tr2: " + transactionIdInProgress);
                        // Pass the amount received and settlement items as extras to the print activity
                        intent.putExtra("amount_received", String.valueOf(totalAmountinserted));
                        intent.putExtra("cash_return", String.valueOf(cashReturn));
                        intent.putExtra("settlement_items", settlementItems);
                        intent.putExtra("id", latesttransId);
                        intent.putExtra("selectedBuyerName", selectedBuyer.getNames());
                        intent.putExtra("selectedBuyerTAN", selectedBuyer.getTan());
                        intent.putExtra("selectedBuyerCompanyName", selectedBuyer.getCompanyName());
                        intent.putExtra("selectedBuyerType",selectedBuyer.getBuyerType());
                        intent.putExtra("selectedBuyerBRN", selectedBuyer.getBrn());
                        intent.putExtra("selectedBuyerNIC", selectedBuyer.getNic());
                        intent.putExtra("selectedBuyerAddresse", selectedBuyer.getBusinessAddr());
                        intent.putExtra("selectedBuyerprofile", selectedBuyer.getProfile());


                        startActivity(intent);

                    }

                }else {
                    Intent intent = new Intent(getActivity(), Mra.class);

                    intent.putExtra("roomid", Roomid);
                    intent.putExtra("tableid", tableid);
                    intent.putExtra("transactionIdInProgress", transactionIdInProgress);

                    System.out.println("tr2: " + transactionIdInProgress);
                    // Pass the amount received and settlement items as extras to the print activity
                    intent.putExtra("amount_received", String.valueOf(totalAmountinserted));
                    intent.putExtra("cash_return", String.valueOf(cashReturn));
                    intent.putExtra("settlement_items", settlementItems);
                    intent.putExtra("id", transactionIdInProgress);
                    intent.putExtra("selectedBuyerName", "");
                    intent.putExtra("selectedBuyerTAN", "");
                    intent.putExtra("selectedBuyerCompanyName", "");
                    intent.putExtra("selectedBuyerType","");
                    intent.putExtra("selectedBuyerBRN", "");
                    intent.putExtra("selectedBuyerNIC", "");
                    intent.putExtra("selectedBuyerAddresse", "");
                    intent.putExtra("selectedBuyerprofile", "");

                    startActivity(intent);
                }


            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("selectedBuyer: " + selectedBuyer);
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

                        SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        transactionIdInProgress = sharedPreference.getString(TRANSACTION_ID_KEY, null);

                        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);


                        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                        Log.d("selectedBuyer.getNames()", selectedBuyer.getNames());
                        Log.d(" selectedBuyer.getTan(),",  selectedBuyer.getTan());
                        Log.d("  selectedBuyer.getCompanyName(),",  selectedBuyer.getCompanyName());
                        Log.d("   selectedBuyer.getBusinessAddr(),",   selectedBuyer.getBusinessAddr());
                        Log.d("    selectedBuyer.getBrn(),",    selectedBuyer.getBrn());
                        Log.d("    cashierId,",    cashierId);
                        Log.d("    latesttransId,",    latesttransId);

                        mDatabaseHelper.updateTransactionBuyerInfo(
                                latesttransId,
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

                                latesttransId,
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
                    String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);


                    String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

                    String amount = "0";
                    String popFraction = "novalue";
                    // Handle the case when selectedBuyer is null
                    validateticketDialogFragment dialogFragment = validateticketDialogFragment.newInstance(
                            latesttransId,
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
                //clearBuyerInfoFromPrefs();
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
                        TextView uniqueidEditText = view.findViewById(R.id.uniqueid_text_view);
                        TextView PriceEditText = view.findViewById(R.id.price_text_view);
                        TextView itemIdEditText = view.findViewById(R.id.id_text_view);

                        String LongDesc = LongDescTextView.getText().toString();
                        String Quantity = QuantityEditText.getText().toString();
                        String Price = PriceEditText.getText().toString();
                        ItemId = itemIdEditText.getText().toString();
                        uniqueid=uniqueidEditText.getText().toString();
                       String newbc = mDatabaseHelper.getBarcodeByItemId(ItemId);



                        if (newbc != null) {
                            // Barcode was successfully retrieved

                        } else {
                            // Handle the case where no barcode was found

                            newbc = mDatabaseHelper.getOptionsBarcodeByItemId(ItemId);

                        }
                        // Update CheckBox visibility on item click
                        CheckBox checkBox = view.findViewById(R.id.checkbox);
                        boolean isCheckBoxVisible = checkBox.getVisibility() == View.VISIBLE;

                        // Create and show the dialog fragment with the data only if the CheckBox is not visible
                        if (!isCheckBoxVisible) {
                            ModifyItemDialogFragment dialogFragment = ModifyItemDialogFragment.newInstance(Quantity, Price, LongDesc,uniqueid, ItemId,newbc);
                            dialogFragment.setTargetFragment(TicketFragment.this, 0);
                            dialogFragment.show(activity.getSupportFragmentManager(), "modify_item_dialog");
                        } else {
                            // CheckBox is visible, toggle the checked state
                            checkBox.setChecked(!checkBox.isChecked());
                            checkedItems = mAdapter.checkedItems;
                            // Update the list of checked items
                            if (checkBox.isChecked()) {
                                checkedItems.add(uniqueid);
                                // Update the isSelected field in the database
                                mDatabaseHelper.updateItemSelectedperuniqueid(uniqueid, checkBox.isChecked());
                                double totalAmount = mDatabaseHelper.calculateTotalAmounts(String.valueOf(roomid),tableid);
                                double totalTax = mDatabaseHelper.calculateTotalTaxAmounts(String.valueOf(roomid),tableid);
                                refreshData(totalAmount, totalTax,"Notmovetobottom");

                            } else {
                                checkedItems.remove(uniqueid);
                                // Check if all items are unchecked
                                if (checkedItems.isEmpty()) {
                                    SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                                    int roomid = preferences.getInt("roomnum", 0);
                                    String tableid = preferences.getString("table_id", "");
                                    String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                                    String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                                    mDatabaseHelper.resetIsSelectedByTransactionId(latesttransId);
                                    TicketAdapter adapter = (TicketAdapter) mRecyclerView.getAdapter();
                                    adapter.setCheckBoxVisibility(false);
                                    adapter.uncheckAllCheckboxes();
                                    mcheckbox.setVisibility(View.GONE);
                                    refreshData(calculateTotalAmount(), calculateTotalTax(),"Notmovetobottom");

                                }else{

                                    // Update the isSelected field in the database
                                    mDatabaseHelper.updateItemSelectedperuniqueid(uniqueid, checkBox.isChecked());
                                    double totalAmount = mDatabaseHelper.calculateTotalAmounts(String.valueOf(roomid),tableid);
                                    double totalTax = mDatabaseHelper.calculateTotalTaxAmounts(String.valueOf(roomid),tableid);
                                    refreshData(totalAmount, totalTax,"Notmovetobottom");
                                }



                            }
                            if (checkedItems.isEmpty()) {
                                refreshData(calculateTotalAmount(), calculateTotalTax(),"Notmovetobottom");
                            }else{
                                // Calculate the total amount
                                double totalAmount = mDatabaseHelper.calculateTotalAmounts(String.valueOf(roomid),tableid);
                                double totalTax = mDatabaseHelper.calculateTotalTaxAmounts(String.valueOf(roomid),tableid);
                                refreshData(totalAmount, totalTax,"Notmovetobottom");
                            }


                        }
                    }



                    @Override
                    public void onLongItemClick(View view, int position) {
                        TicketAdapter adapter = (TicketAdapter) mRecyclerView.getAdapter();
                        adapter.setCheckBoxVisibility(true);
                        mcheckbox.setVisibility(View.VISIBLE);
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
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Save Transaction as");
        String[] options = {"Pro Format", "Debit Note", "Credit Note","Send To Kitchen"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Assuming you have a way to get the levelNumber
                int levelNumber = Integer.parseInt(cashierLevel); // Get the user level
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

                // Permission checks

                boolean canHigherAccessProforma = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "proforma_", levelNumber);
                boolean canHigherAccessdebit = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "debitnote_", levelNumber);
                boolean canHigherAccesscredit = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "creditnote_", levelNumber);
                boolean canHigherAccessstk = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "sendtokitchen_", levelNumber);

                boolean candoproforma = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "proforma_", levelNumber);
                boolean candodebitnote = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "debitnote_", levelNumber);
                boolean candocreditnote = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "creditnote_", levelNumber);
                boolean candosenttokitchen = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "sendtokitchen_", levelNumber);

                // Handle the user's selection here
                if (which == 0) {

                    if (canHigherAccessProforma) {


                        showPinDialog("proforma_", () -> {
                            String Type="PRF";
                            double totalAmount = calculateTotalAmount();
                            double TaxtotalAmount = calculateTotalTax();
                            SaveTransaction(Type, totalAmount, TaxtotalAmount);
                            clearBuyerInfoFromPrefs();
                        });

                    }else if(!canHigherAccessProforma && candoproforma) {
                        String Type="PRF";
                        double totalAmount = calculateTotalAmount();
                        double TaxtotalAmount = calculateTotalTax();
                        SaveTransaction(Type, totalAmount, TaxtotalAmount);
                        clearBuyerInfoFromPrefs();
                    }else {
                        showPermissionDeniedDialog(); // Method to show a message to the user
                    }

                } else if (which == 1) {

                    if (canHigherAccessdebit) {


                        showPinDialog("debitnote_", () -> {
                            String Type="DRN";
                            double totalAmount = calculateTotalAmount();
                            double TaxtotalAmount = calculateTotalTax();
                            SaveTransaction(Type,totalAmount,TaxtotalAmount);
                            clearBuyerInfoFromPrefs();
                        });

                    }else   if (! canHigherAccessdebit && candodebitnote) {
                        String Type="DRN";
                    double totalAmount = calculateTotalAmount();
                    double TaxtotalAmount = calculateTotalTax();
                    SaveTransaction(Type,totalAmount,TaxtotalAmount);
                    clearBuyerInfoFromPrefs();
                    }else {
                        showPermissionDeniedDialog(); // Method to show a message to the user
                    }
                }
                else if (which == 2) {


                    if (canHigherAccesscredit) {


                        showPinDialog("creditnote_", () -> {
                            String Type="CRN";
                            double totalAmount = calculateTotalAmount();
                            double TaxtotalAmount = calculateTotalTax();

                            SaveTransaction(Type,totalAmount,TaxtotalAmount);
                            clearBuyerInfoFromPrefs();
                        });

                    }else   if (! canHigherAccesscredit && candocreditnote) {
                        String Type="CRN";
                        double totalAmount = calculateTotalAmount();
                        double TaxtotalAmount = calculateTotalTax();

                        SaveTransaction(Type,totalAmount,TaxtotalAmount);
                        clearBuyerInfoFromPrefs();
                    }else {
                        showPermissionDeniedDialog(); // Method to show a message to the user
                    }

                }
                else if (which == 3) {
                    if (canHigherAccessstk) {


                        showPinDialog("sendtokitchen_", () -> {
                            // Create an Intent to launch SendNoteToKitchenActivity
                            Intent intent = new Intent(getContext(), SendNoteToKitchenActivity.class);
                            // Put the text as an extra in the Intent

                            // Start the activity
                            startActivity(intent);
                        });
                        } else if (!canHigherAccessstk && candosenttokitchen) {
                        // Create an Intent to launch SendNoteToKitchenActivity
                        Intent intent = new Intent(getContext(), SendNoteToKitchenActivity.class);
                        // Put the text as an extra in the Intent

                        // Start the activity
                        startActivity(intent);
                    }else {
                        showPermissionDeniedDialog(); // Method to show a message to the user
                    }

                }
            }
        });
        builder.show();
    }


    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void startNewActivity(String type, String newTransactionId,String invoiceRefIdentifier,String roomid, String tableid,String creditNoteReason) {
        // Create an Intent to start the desired activity based on the selected type
        Intent intent = new Intent(getContext(), MRADBN.class);

        // Pass any data you need to the new activity using intent extras
        intent.putExtra("TransactionType", type);
        intent.putExtra("newtransactionid", newTransactionId);
        intent.putExtra("invoiceRefIdentifier", invoiceRefIdentifier);
        intent.putExtra("roomid", roomid);
        intent.putExtra("tableid", tableid);
        intent.putExtra("creditNoteReason", creditNoteReason);

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

        Log.d("Type", Type);

        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount,"movetobottom");

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

                    // Show the dialog to capture the reason
                    AlertDialog.Builder reasonDialogBuilder = new AlertDialog.Builder(getContext());
                    View reasonDialogView = getLayoutInflater().inflate(R.layout.popup_reason_input, null);
                    reasonDialogBuilder.setView(reasonDialogView);
                    reasonDialogBuilder.setTitle("Enter the Reason");

                    final EditText reasonEditText = reasonDialogView.findViewById(R.id.reasonEditText);

                    reasonDialogBuilder.setPositiveButton("Submit", (dialog, which) -> {
                        // Capture the reason entered by the user
                        String creditNoteReason = reasonEditText.getText().toString();

                        // Get the latest transaction counter value from SharedPreferences
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
                        int latestTransactionDBNCounter = sharedPreferences.getInt("transaction_counter_DBN", 0);
                        SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                        // Initialize SharedPreferences for room and table
                        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        roomid = preferences.getInt("roomnum", 0);
                        tableid = preferences.getString("table_id", "0");

                        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                        String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

                        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);

                        // Increment the transaction counter
                        latestTransactionDBNCounter++;
                        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                        PosNum = shardPreference.getString(POSNumber, null);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String currentDateTime = dateFormat.format(new Date());
                        // Generate the transaction ID
                        String newTransactionId = currentDateTime + "-" + Type + "-" + PosNum +"-" +  latestTransactionDBNCounter;

                        // Update the transaction ID in the transaction table
                        mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId, newTransactionId, String.valueOf(roomid), tableid, 2);

                        // Update the transaction ID in the header table
                        mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId, newTransactionId, String.valueOf(roomid), tableid);

                        // Save the updated transaction counter in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("transaction_counter_DBN", latestTransactionDBNCounter);
                        editor.apply();

                        String MRAMETHOD = "Single";

                        // Update the transaction status
                        mDatabaseHelper.updateAllTransactionsStatus(Type, MRAMETHOD, name, String.valueOf(roomid), tableid);
                        mDatabaseHelper.updateReasonStatus(newTransactionId, creditNoteReason);

                        updateTransactionStatus();
                        decrementTransactionCounter();
                        // Start the activity with the selected receipt data
                        startNewActivity(Type, newTransactionId, name, String.valueOf(roomid), tableid, creditNoteReason);
                        getActivity().finish();
                    });

                    reasonDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                    AlertDialog reasonDialog = reasonDialogBuilder.create();
                    reasonDialog.show();
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
    tableid = preferences.getString("table_id", "0");


    transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);
    String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
    String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);


    SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
    PosNum = shardPreference.getString(POSNumber, null);

    // Increment the transaction counter
    latestTransactionProformaCounter++;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String currentDateTime = dateFormat.format(new Date());
    // Generate the transaction ID with the format "MEMO-integer"
    String newTransactionId = currentDateTime + "-" +Type +"-" +PosNum +  "-" + latestTransactionProformaCounter;
    // Update the transaction ID in the transaction table for transactions with status "InProgress"
    //mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);

   // mDatabaseHelper.duplicateTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
    //Log.d("latesttransId", latesttransId);
    //Log.d("newTransactionId", newTransactionId);

    Log.d("rlatesttransIdz" , latesttransId );
    Log.d("newTransactionIdz" , newTransactionId );

        if (newTransactionId.contains("-PRF-")) {
          //  mDatabaseHelper.duplicateTransactionAndHeaderData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid,0);
        mDatabaseHelper.duplicateHeaderTransactionData(latesttransId,newTransactionId, String.valueOf(roomid),tableid);
        mDatabaseHelper.duplicateTransactionLines(latesttransId,newTransactionId);
        mDatabaseHelper.updateTransactionStatusoldprf(newTransactionId,"OLDPRF");

            latesttransId=newTransactionId;
    }else{
            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
            // Update the transaction ID in the header table for transactions with status "InProgress"
            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

       }
   // mDatabaseHelper.duplicateHeaderTransactionData(Type,latesttransId,newTransactionId, String.valueOf(roomid),tableid);
    // Update the transaction ID in the header table for transactions with status "InProgress"
   // mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

    // Update the transaction counter in SharedPreferences
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt("transaction_counter_Proforma", latestTransactionProformaCounter);
    editor.apply();
                    String MRAMETHOD="Single";
    // Update the transaction status for all in-progress transactions to "saved"
    mDatabaseHelper.updateTransactionStatusByTransactionId("OLDPRF",MRAMETHOD,null, newTransactionId);
  //  updateTransactionStatus();

                    // Start the activity with the selected receipt data

    startNewActivity("OLDPRF", newTransactionId, null, String.valueOf(roomid),tableid,null);
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
            Log.d("latestTransactionCDNCounter" , String.valueOf(latestTransactionCDNCounter));
            // Initialize SharedPreferences
            SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
            roomid = preferences.getInt("roomnum", 0);
            tableid = preferences.getString("table_id", "");
            transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid);
            String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
            String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
            // Use a specific Locale for date formatting
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDate = dateFormat.format(new Date());


            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String currentTime = timeFormat.format(new Date());

            SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
            PosNum = shardPreference.getString(POSNumber, null);


            // Increment the transaction counter
            latestTransactionCDNCounter++;

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentDateTime = dateFormat1.format(new Date());

            // Generate the transaction ID with the format "MEMO-integer"
            String newTransactionId = currentDateTime + "-" + Type + "-" +PosNum +"-" +latestTransactionCDNCounter;
            // Update the transaction ID in the transaction table for transactions with status "InProgress"
            mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid,3);
            // Update the transaction ID in the header table for transactions with status "InProgress"
            mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId,newTransactionId, String.valueOf(roomid),tableid);

            // Update the transaction counter in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("transaction_counter_CDN", latestTransactionCDNCounter);
            editor.apply();
            String MRAMETHOD="Single";
            // Update the transaction status for all in-progress transactions to "saved"
            mDatabaseHelper.updateAllTransactionsStatus(Type,MRAMETHOD,null, String.valueOf(roomid),tableid);
            updateTransactionStatus();
            double totalPriceSum = mDatabaseHelper.getTotalAmount(newTransactionId, String.valueOf(roomid), tableid);


            // Start the activity with the selected receipt data
            mDatabaseHelper.insertSettlementAmount("Refund", -totalPriceSum, newTransactionId, PosNum, currentDate,currentTime, String.valueOf(roomid),tableid);


            startNewActivity(Type, newTransactionId, "null", String.valueOf(roomid),tableid,null);

            getActivity().finish();


            // Dismiss the dialog when the "Cancel" button is clFicked
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

                    String id = idTextView.getText().toString();
                    String name = deptNameEditText.getText().toString();

                    // Show the second dialog asking for the reason and capture the reason input
                    AlertDialog.Builder reasonDialogBuilder = new AlertDialog.Builder(getContext());
                    View reasonDialogView = getLayoutInflater().inflate(R.layout.popup_reason_input, null);
                    reasonDialogBuilder.setView(reasonDialogView);
                    reasonDialogBuilder.setTitle("Enter the Reason");

                    final EditText reasonEditText = reasonDialogView.findViewById(R.id.reasonEditText);

                    reasonDialogBuilder.setPositiveButton("Submit", (dialog, which) -> {
                        // Capture the reason entered by the user
                        String creditNoteReason = reasonEditText.getText().toString();

                        // Proceed with transaction updates and status changes
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
                        int latestTransactionCDNCounter = sharedPreferences.getInt("transaction_counter_CDN", 0);
                        Log.d("latestTransactionCDNCounter" , String.valueOf(latestTransactionCDNCounter));

                        // Retrieve other data like roomid, tableid, etc.
                        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
                        roomid = preferences.getInt("roomnum", 0);
                        tableid = preferences.getString("table_id", "");
                        transactionIdInProgress = mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid), tableid);
                        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                        String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

                        // Generate new transaction ID
                        latestTransactionCDNCounter++;
                        // Use a specific Locale for date formatting
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String currentDate = dateFormat.format(new Date());

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                        String currentTime = timeFormat.format(new Date());
                        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                        PosNum = shardPreference.getString(POSNumber, null);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                        String currentDateTime = dateFormat1.format(new Date());

                        String newTransactionId = currentDateTime + "-" + Type + "-" + PosNum +"-"  + latestTransactionCDNCounter;

                        // Update transaction details in the database
                        mDatabaseHelper.updateOnresetTransactionTransactionIdInProgress(latesttransId, newTransactionId, String.valueOf(roomid), tableid, 4);
                        mDatabaseHelper.updateHeaderTransactionIdInProgress(latesttransId, newTransactionId, String.valueOf(roomid), tableid);
                        mDatabaseHelper.updateReasonStatus(newTransactionId, creditNoteReason);

                        // Update transaction status
                        String MRAMETHOD = "Single";
                        mDatabaseHelper.updateAllTransactionsStatus(Type, MRAMETHOD, name, String.valueOf(roomid), tableid);

                        double totalPriceSum = mDatabaseHelper.getTotalAmount(newTransactionId, String.valueOf(roomid), tableid);
                        mDatabaseHelper.insertSettlementAmount("Refund", -totalPriceSum, newTransactionId, PosNum, currentDate, currentTime, String.valueOf(roomid), tableid);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("transaction_counter_CDN", latestTransactionCDNCounter);
                        editor.apply();
                        // Launch the next activity with the creditNoteReason
                        startNewActivity(Type, newTransactionId, name, String.valueOf(roomid), tableid, creditNoteReason);
                        decrementTransactionCounter();
                        getActivity().finish();
                    });

                    reasonDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                    AlertDialog reasonDialog = reasonDialogBuilder.create();
                    reasonDialog.show();
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

    public void clearPayment(){
        // Create an instance of the DatabaseHelper class
// Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");

        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        mDatabaseHelper.clearData(roomid,tableid,latesttransId);
        mDatabaseHelper.clearLinesByTransId(latesttransId);
        mDatabaseHelper.adjustFinancialRecordsByTransactionId(latesttransId);
        double totalAmount = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
        double TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);
        // Optionally, you can notify the user or perform any other actions after clearing the transaction
// Notify the listener that an item is added
        mDatabaseHelper.deleteTenderAmountByTransactionId(latesttransId);
        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount,"movetobottom");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);


        if ("sunmit2".equalsIgnoreCase(deviceType)) {
            //Log.d("deviceType2",deviceType  );
            showSecondaryScreen(data);
        }  else {
          //  Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
        }
        //   recreate(getActivity());
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
        Cursor cursorsplitted= mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);


        mSplittedAdapter.swapCursor(cursorsplitted);
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
        mSplittedAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), getText(R.string.transactioncleared), Toast.LENGTH_SHORT).show();


    }

    public  void clearTransact(){
        // Create an instance of the DatabaseHelper class
// Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = String.valueOf(preferences.getString("table_id", "0"));

        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        Log.d("statusType", roomid +" " + tableid);
        Log.d("statusType", statusType);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        mDatabaseHelper.updateStatusToVoid(latesttransId);

        mDatabaseHelper.updateCoverCount(0,tableid,roomid);

        // Optionally, you can notify the user or perform any other actions after clearing the transaction
// Notify the listener that an item is added
        double totalAmount = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
        double TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);
        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount,"movetobottom");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);


        if ("sunmit2".equalsIgnoreCase(deviceType)) {
          //  Log.d("deviceType2",deviceType  );
            showSecondaryScreen(data);


        }  else {
           // Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
        }
     //   recreate(getActivity());

        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);
        Cursor cursorsplitted= mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);


        mSplittedAdapter.swapCursor(cursorsplitted);
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
        mSplittedAdapter.notifyDataSetChanged();

        Toast.makeText(getContext(), getText(R.string.transactioncleared), Toast.LENGTH_SHORT).show();


    }


    public void showSecondaryScreen(List<String> data) {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();

        if (presentationDisplay != null) {
            // Create an instance of SecondaryScreenDisplay using the obtained display
            TransactionDisplay secondaryDisplay = new TransactionDisplay(getActivity(), presentationDisplay);

            // Run on the UI thread to prevent freezing
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Show the secondary display
                    try {
                        // Show the secondary display
                        secondaryDisplay.show();
                        // Update RecyclerView data on the secondary screen
                        secondaryDisplay.updateRecyclerViewData(data);
                    } catch (Exception e) {
                        Log.e("SecondaryScreenError", "Error showing secondary display", e);
                    }
                }
            });

        } else {
            // Secondary screen not found or not supported
            displayOnLCD();
        }
    }


    private Display getPresentationDisplay() {
        if (isAdded()) {  // Ensure the Fragment is attached to an Activity
            DisplayManager displayManager = (DisplayManager) requireContext().getSystemService(Context.DISPLAY_SERVICE);
            if (displayManager != null) {
                // Retrieve only displays suitable for presentations
                Display[] displays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                if (displays.length > 0) {
                    return displays[0]; // Return the first available presentation display
                }
            }
        }
        return null; // No suitable display found
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
    private void refreshTotal() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            ticketFragment.updateheader(10,calculateTotalTax());
        }
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


    public void updateheadertable(double totalAmount, double TaxtotalAmount, String roomid, String tableid) {

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


            if ("sunmit2".equalsIgnoreCase(deviceType)) {
              //  Log.d("deviceType4",deviceType  );
                showSecondaryScreen(data);
            } else {
               // Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
            }

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


            if ("sunmit2".equalsIgnoreCase(deviceType)) {
              //  Log.d("deviceType5",deviceType  );
                showSecondaryScreen(data);
            }  else {
              //  Toast.makeText(getContext(), "No Secondary Screen", Toast.LENGTH_SHORT).show();
            }

        } else {

            Intent intent = new Intent(getActivity(), SplashFlashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable window animation
            startActivity(intent);
            getActivity().finish();


        }


    }






    public  void refreshData(double totalAmount, double TaxtotalAmount,String movetobottom) {

        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = String.valueOf(preferences.getString("table_id", "0"));

        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
        // Check if the status is PRF and the item count is 0, then update status to InProgress
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId, String.valueOf(roomid), tableid);
        if ("PRF".equals(statusType) && (cursor == null || cursor.getCount() == 0)) {
            mDatabaseHelper.flagTransactionItemsAsCleared(latesttransId);
            clearTransact();


        }


        Cursor cursorsplitted= mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(latesttransId,String.valueOf(roomid),tableid);


        mSplittedAdapter.swapCursor(cursorsplitted);
        mSplittedAdapter.notifyDataSetChanged();
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
if(!movetobottom.equals("Notmovetobottom")){
    // Scroll to the last item in the RecyclerView
    int itemCount = mAdapter.getItemCount();
    if (itemCount > 0) {
        mRecyclerView.smoothScrollToPosition(itemCount - 1);
        FooterLayout.setVisibility(View.VISIBLE);

        EmptyFooter.setVisibility(View.GONE);
    }else{
        FooterLayout.setVisibility(View.GONE);

        EmptyFooter.setVisibility(View.VISIBLE);
    }
}


        // Show or hide the RecyclerView and empty view based on the cursor count
        if (cursor != null && cursor.getCount() > 0 && (cursorsplitted == null || cursorsplitted.getCount() <= 0)) {
            // Case 2: Only cursor has data, cursorsplitted is null or empty
           // Log.d("condition", "2");
            mRecyclerView.setVisibility(View.VISIBLE);
            SplittedItemsRecycleView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.GONE);
            separationline.setVisibility(View.GONE);

        } else if (cursor == null || cursor.getCount() <= 0 && cursorsplitted != null && cursorsplitted.getCount() > 0) {
            // Case 3: Only cursorsplitted has data, cursor is null or empty
            Log.d("condition", "3");
            mRecyclerView.setVisibility(View.GONE);
            SplittedItemsRecycleView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            separationline.setVisibility(View.GONE);

        } else if (cursor != null && cursor.getCount() > 0 && cursorsplitted != null && cursorsplitted.getCount() > 0) {
            // Case 1: Both cursors have data
           // Log.d("condition", "1");
            mRecyclerView.setVisibility(View.VISIBLE);
            SplittedItemsRecycleView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            separationline.setVisibility(View.VISIBLE);

        } else {
            // Default case: neither cursor has data
            //Log.d("condition", "4");
            mRecyclerView.setVisibility(View.GONE);
            SplittedItemsRecycleView.setVisibility(View.GONE);
            separationline.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        }

        // Update the tax and total amount TextViews
        TextView taxTextView = getView().findViewById(R.id.textViewVAT);
        String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
        taxTextView.setText(getString(R.string.tax) + ": Rs " + formattedTaxAmount);

        TextView totalAmountTextView = getView().findViewById(R.id.textViewTotal);
        String formattedTotalAmount = String.format("%.2f", totalAmount);
        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);

        if(latesttransId != null){
            double discount=mDatabaseHelper.getSumOfTransactionTotalDiscount(latesttransId);
            String formattedTotadisc = String.format("%.2f", discount);
            textviewdiscount.setText(getString(R.string.discount) + ": Rs " + formattedTotadisc);
        }else{
            textviewdiscount.setText(getString(R.string.discount) + ": Rs0.00 ");
        }
        TextView textviewpaymentmethod=getView().findViewById(R.id.textViewpaymentname);
        TextView textviewSubTotal=getView().findViewById(R.id.textViewSubtotal);
        TextView cashreturn=getView().findViewById(R.id.textViewCashReturn);
       FooterLayout = getView().findViewById(R.id.footer_layout); // Initialize the container layout
        EmptyFooter=getView().findViewById(R.id.emptyfooter_layout);

        validate=getView().findViewById(R.id.val_layout);
        // Call the getSettlementSummaries method
        List<DatabaseHelper.SettlementSummary> summaries = mDatabaseHelper.getSettlementSummaries(latesttransId, roomid, tableid);

        // Calculate the subtotal and display payment methods
        if (summaries != null && !summaries.isEmpty()) {
            double sum = 0.0;
            StringBuilder paymentMethods = new StringBuilder();

            for (DatabaseHelper.SettlementSummary summary : summaries) {
                sum += summary.sum;
                paymentMethods.append(summary.paymentName).append(": Rs ").append(String.format("%.2f", summary.sum)).append("\n");
            }

            double subtotal = totalAmount - sum;
             CashReturnVal= sum-totalAmount;
            boolean isAtLeastOneItemPaid =  mDatabaseHelper.isAtLeastOneItemPaid(latesttransId);
            boolean isTransactionIdExists=  mDatabaseHelper.isTransactionIdExists(latesttransId);

            String formattedSubTotalAmount = String.format("%.2f", subtotal);
            String formattedCashReturn = String.format("%.2f", CashReturnVal);

            textviewpaymentmethod.setText(paymentMethods.toString().trim());
            if(isTransactionIdExists  ){
                if(subtotal<=0){
                    textviewSubTotal.setText(getString(R.string.SUBTOTAL) + ": Rs 0.00");
                    cashreturn.setVisibility(View.VISIBLE);
                    FooterLayout.setVisibility(View.GONE);

                    validate.setVisibility(View.VISIBLE);
                    cashreturn.setText(getString(R.string.cashReturn) + ": Rs " + formattedCashReturn);

                }else {
                    FooterLayout.setVisibility(View.VISIBLE);

                    validate.setVisibility(View.GONE);
                    textviewSubTotal.setText(getString(R.string.SUBTOTAL) + ": Rs " + formattedSubTotalAmount);
                    textviewpaymentmethod.setVisibility(View.VISIBLE);
                    textviewSubTotal.setVisibility(View.VISIBLE);
                    System.out.println("SUBTOTAL: " + sum + ", formattedSubTotalAmount: " + formattedSubTotalAmount);
                }
                System.out.println("isTransactionIdExists: " + sum + ", Payment Methods: " + paymentMethods.toString());
            } else  if(isAtLeastOneItemPaid){

                textviewpaymentmethod.setVisibility(View.GONE);
                textviewSubTotal.setVisibility(View.GONE);
                FooterLayout.setVisibility(View.VISIBLE);

                validate.setVisibility(View.GONE);
                textviewSubTotal.setText(getString(R.string.SUBTOTAL) + ": Rs " + formattedSubTotalAmount);
                System.out.println("areAllItemsPaid: " + sum + ", Payment Methods: " + paymentMethods.toString());
            }


            // Log the results
            System.out.println("Sum: " + sum + ", Payment Methods: " + paymentMethods.toString());
        } else {
            textviewpaymentmethod.setVisibility(View.GONE);
            textviewSubTotal.setVisibility(View.GONE);
            cashreturn.setVisibility(View.GONE);
            FooterLayout.setVisibility(View.VISIBLE);

            validate.setVisibility(View.GONE);

            // Handle the case where no data was found
            System.out.println("No data found for transaction ID: " + latesttransId + ", room ID: " + roomid + ", table ID: " + tableid);
        }



        // Play the sound effect
        playSoundEffect();
    }
    public  void refreshDatatable(double totalAmount, double TaxtotalAmount,String roonum,String tableid) {
       TicketAdapter adapter = (TicketAdapter) mRecyclerView.getAdapter();
        SplittedTicketAdapter sadapter= (SplittedTicketAdapter) SplittedItemsRecycleView.getAdapter();
        adapter.setCheckBoxVisibility(false);
        sadapter.setCheckBoxVisibility(false);
        // Set the data to the fragment views
        TextView roomTextView = getView().findViewById(R.id.textViewRoom);
        roomTextView.setText("Room: " + roonum);

        TextView tableTextView = getView().findViewById(R.id.textViewTable);
        tableTextView.setText(" - Table: " + tableid);

        TextView cashierTextView = getView().findViewById(R.id.textViewCashier);
        cashierTextView.setText(" - Cashier: " + cashierId);
        Cursor cursor;
        String statusType= mDatabaseHelper.getLatestTransactionStatus(roonum,tableid);


            String latesttransId= mDatabaseHelper.getLatestTransactionId(roonum,tableid,statusType);
             cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId,roonum,tableid);

        Cursor cursorsplitted= mDatabaseHelper.RestoreViewAllInSplittedProgressTransactionsbytable(latesttransId,roonum,tableid);


        mSplittedAdapter.swapCursor(cursorsplitted);
        mSplittedAdapter.notifyDataSetChanged();

        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();

        // Scroll to the last item in the RecyclerView
        int itemCount = mAdapter.getItemCount();
        Log.d("itemCount", String.valueOf(itemCount));

        if (itemCount > 0) {
            mRecyclerView.smoothScrollToPosition(itemCount - 1);
            FooterLayout.setVisibility(View.VISIBLE);
            EmptyFooter.setVisibility(View.GONE);

        }else{
            FooterLayout.setVisibility(View.GONE);
            EmptyFooter.setVisibility(View.VISIBLE);


        }
        int itemCounts = mSplittedAdapter.getItemCount();
        Log.d("itemCounts", String.valueOf(itemCounts));
        if (itemCounts > 0) {
            SplittedItemsRecycleView.smoothScrollToPosition(itemCounts - 1);
            FooterLayout.setVisibility(View.VISIBLE);

            EmptyFooter.setVisibility(View.GONE);
        }else{
            FooterLayout.setVisibility(View.GONE);

            EmptyFooter.setVisibility(View.VISIBLE);
        }
        // Show or hide the RecyclerView and empty view based on the cursor count

        if (cursor != null && cursor.getCount() > 0 && (cursorsplitted == null || cursorsplitted.getCount() <= 0)) {
            // Case 2: Only cursor has data, cursorsplitted is null or empty
            //Log.d("condition", "2");
            mRecyclerView.setVisibility(View.VISIBLE);
            SplittedItemsRecycleView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.GONE);
            separationline.setVisibility(View.GONE);
            FooterLayout.setVisibility(View.VISIBLE);

            EmptyFooter.setVisibility(View.GONE);
        } else if ((cursor == null || cursor.getCount() <= 0) && (cursorsplitted != null && cursorsplitted.getCount() > 0)) {
            // Case 3: Only cursorsplitted has data, cursor is null or empty
           // Log.d("condition", "3");
            mRecyclerView.setVisibility(View.GONE);
            SplittedItemsRecycleView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            separationline.setVisibility(View.GONE);
            FooterLayout.setVisibility(View.VISIBLE);

            EmptyFooter.setVisibility(View.GONE);

        } else if (cursor != null && cursor.getCount() > 0 && cursorsplitted != null && cursorsplitted.getCount() > 0) {
            // Case 1: Both cursors have data
           // Log.d("condition", "1");
            mRecyclerView.setVisibility(View.VISIBLE);
            SplittedItemsRecycleView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            separationline.setVisibility(View.VISIBLE);
            FooterLayout.setVisibility(View.VISIBLE);

            EmptyFooter.setVisibility(View.GONE);
        } else {
            // Default case: neither cursor has data
          //  Log.d("condition", "4");
            mRecyclerView.setVisibility(View.GONE);
            SplittedItemsRecycleView.setVisibility(View.GONE);
            separationline.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
            FooterLayout.setVisibility(View.GONE);

            EmptyFooter.setVisibility(View.VISIBLE);
        }


        textviewdiscount.setText(getString(R.string.discount) + " : Rs 0.00");

        textViewVATs.setText(getString(R.string.tax) + " : Rs 0.00");
        textViewTotals.setText(getString(R.string.Total) + " : Rs 0.00");
        if(latesttransId != null){
            double discount=mDatabaseHelper.getSumOfTransactionTotalDiscount(latesttransId);
            String formattedTotadisc = String.format("%.2f", discount);
            textviewdiscount.setText(getString(R.string.discount) + ": Rs " + formattedTotadisc);

        }else{
            textviewdiscount.setText(getString(R.string.discount) + ": Rs0.00 ");
        }


        // Update the tax and total amount TextViews
        TextView taxTextView = getView().findViewById(R.id.textViewVAT);
        String formattedTaxAmount = String.format("%.2f", TaxtotalAmount);
        taxTextView.setText(getString(R.string.tax) + ": Rs " + formattedTaxAmount);
        //Log.d("update payment method", "onItemAdded called with roomid: " + roonum + ", tableid: " + tableid);
        TextView totalAmountTextView = getView().findViewById(R.id.textViewTotal);
        String formattedTotalAmount = String.format("%.2f", totalAmount);
        totalAmountTextView.setText(getString(R.string.Total) + ": Rs " + formattedTotalAmount);



        TextView textviewpaymentmethod=getView().findViewById(R.id.textViewpaymentname);
        TextView textviewSubTotal=getView().findViewById(R.id.textViewSubtotal);
        TextView cashreturn=getView().findViewById(R.id.textViewCashReturn);
        FooterLayout = getView().findViewById(R.id.footer_layout); // Initialize the container layout
        validate=getView().findViewById(R.id.val_layout);

        List<DatabaseHelper.SettlementSummary> summaries = mDatabaseHelper.getSettlementSummaries(latesttransId, Integer.parseInt(roonum), tableid);

        // Calculate the subtotal and display payment methods
        if (summaries != null && !summaries.isEmpty()) {
            double sum = 0.0;
            StringBuilder paymentMethods = new StringBuilder();

            for (DatabaseHelper.SettlementSummary summary : summaries) {
                sum += summary.sum;
                paymentMethods.append(summary.paymentName).append(": Rs ").append(String.format("%.2f", summary.sum)).append("\n");
            }

            double subtotal = totalAmount - sum;
            double CashReturnVal= sum-totalAmount;
            if(subtotal<0){
                CashReturnVal= -1 * subtotal;

            }

            boolean isAtLeastOneItemPaid =  mDatabaseHelper.isAtLeastOneItemPaid(latesttransId);
            boolean isTransactionIdExists=  mDatabaseHelper.isTransactionIdExists(latesttransId);

            String formattedSubTotalAmount = String.format("%.2f", subtotal);
            String formattedCashReturn = String.format("%.2f", CashReturnVal);
            textviewpaymentmethod.setVisibility(View.VISIBLE);
            textviewSubTotal.setVisibility(View.VISIBLE);
            textviewpaymentmethod.setText(paymentMethods.toString().trim());

            if(isTransactionIdExists && !isAtLeastOneItemPaid ){
                if(subtotal<=0){
                    textviewSubTotal.setText(getString(R.string.SUBTOTAL) + ": Rs 0.00");
                    cashreturn.setVisibility(View.VISIBLE);

                    validate.setVisibility(View.VISIBLE);
                    cashreturn.setText(getString(R.string.cashReturn) + ": Rs " + formattedCashReturn);

                }else {

                    validate.setVisibility(View.GONE);
                    textviewSubTotal.setText(getString(R.string.SUBTOTAL) + ": Rs " + formattedSubTotalAmount);
                    textviewpaymentmethod.setVisibility(View.VISIBLE);
                    textviewSubTotal.setVisibility(View.VISIBLE);
                    System.out.println("SUBTOTAL: " + sum + ", formattedSubTotalAmount: " + formattedSubTotalAmount);
                }
                System.out.println("isTransactionIdExists: " + sum + ", Payment Methods: " + paymentMethods.toString());
            } else  if(isAtLeastOneItemPaid){

                textviewpaymentmethod.setVisibility(View.GONE);
                textviewSubTotal.setVisibility(View.GONE);


                validate.setVisibility(View.GONE);
                textviewSubTotal.setText(getString(R.string.SUBTOTAL) + ": Rs " + formattedSubTotalAmount);
                System.out.println("areAllItemsPaid: " + sum + ", Payment Methods: " + paymentMethods.toString());
            }


            // Log the results
            System.out.println("Sum: " + sum + ", Payment Methods: " + paymentMethods.toString());
        } else {
            textviewpaymentmethod.setVisibility(View.GONE);
            textviewSubTotal.setVisibility(View.GONE);
            cashreturn.setVisibility(View.GONE);



            validate.setVisibility(View.GONE);

            // Handle the case where no data was found
            System.out.println("No data found for transaction ID: " + latesttransId + ", room ID: " + roonum + ", table ID: " + tableid);
        }





        // Play the sound effect
        playSoundEffect();
    }

    // Inside your Fragment

    // Inside your Fragment
    private void updateUIBasedOnItems() {
        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);


        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        // Assuming `cursor` is the data source, check if the cursor has data.
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId, String.valueOf(roomid), tableid);

        // Check if there are items
         if (cursor != null && cursor.getCount() > 0) {
            // Items exist, show the buttons and hide empty state
            emptyFrameLayout.setVisibility(View.GONE);
            totalntax.setVisibility(View.GONE);  // Assuming footerLayout is your footer with buttons
            FooterLayout.setVisibility(View.VISIBLE);

             // You can also set the total values here if you have those values

        } else {
            emptyFrameLayout.setVisibility(View.VISIBLE);
            totalntax.setVisibility(View.VISIBLE);  // Assuming footerLayout is your footer with buttons
            FooterLayout.setVisibility(View.GONE);


             // Set default values for totals and discounts
            textViewTotals.setText(getString(R.string.Total) + " : Rs 0.00");
            textviewdiscount.setText(getString(R.string.discount) + " : Rs 0.00");
            textViewVATs.setText(getString(R.string.tax) + " : Rs 0.00");
        }
    }
    private void setUpSplitBillRecyclerView(View dialogView) {
        // Fetch the transaction status and ID
        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
        String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

        // Fetch the data using the transaction ID
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId, String.valueOf(roomid), tableid);

        // Check if the RecyclerView exists in the provided view
        RecyclerView splitBillRecyclerView = dialogView.findViewById(R.id.splitBillRecyclerView);

        // Ensure the RecyclerView is not null
        if (splitBillRecyclerView != null) {
            // Set up the adapter with the cursor
            mSplitBillAdapter = new SplitBillTicketAdapter(getContext(), cursor);

            // Configure the RecyclerView layout and adapter
            splitBillRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            splitBillRecyclerView.setAdapter(mSplitBillAdapter);
        } else {
            Log.e("SplitBillDebug", "RecyclerView not found in the provided dialog view.");
        }
    }



    private void setUpRecyclerViews(View view,int cashiorid) {
        // Set up RecyclerView and its adapter for Transaction Details
        List<DataModel> transactionDataList = fetchDataBasedOnReportTypeAndCashier("Daily",cashiorid);
        reportAdapter = new SalesReportAdapter(transactionDataList);
        recyclerViewReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewReports.setAdapter(reportAdapter);

        // Set up RecyclerView and its adapter for Payment Method Details
        List<PaymentMethodDataModel> paymentMethodDataList = fetchPaymentMethodDataBasedOnReportType("Daily");
        paymentMethodAdapter = new PaymentMethodAdapter(paymentMethodDataList);
        secondRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        secondRecyclerView.setAdapter(paymentMethodAdapter);
    }
    public void showEditNumberOfCoversDialog(final String roomId, String tableid) {

        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomId), tableid);
        String transactionIdInProgress = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomId), tableid, statusType);
if(transactionIdInProgress != null) {
    // Fetch current number of covers
    int currentNumberOfCovers = mDatabaseHelper.getNumberOfCoversByTransactionTicketNo(transactionIdInProgress);
    // Create an AlertDialog builder
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Edit Number of Covers");

    // Set up the input
    final EditText input = new EditText(getContext());
    input.setInputType(InputType.TYPE_CLASS_NUMBER);
    input.setText(String.valueOf(currentNumberOfCovers));
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomId), tableid);
            String transactionIdInProgress = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomId), tableid, statusType);
            int newNumberOfCovers = Integer.parseInt(input.getText().toString());
            mDatabaseHelper.updateNumberOfCovers(transactionIdInProgress, newNumberOfCovers);
            mDatabaseHelper.updateCoverCount(newNumberOfCovers, tableid, roomid);
            Cursor filteredCursor = mDatabaseHelper.getTablesFilteredByMerged(String.valueOf(roomid));

            mTableAdapter.swapCursor(filteredCursor);
            CoverClearedListener.onAmountCoverModified();

        }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

    builder.show();
}else{
     Toast.makeText(getContext(), "No Transaction In progress", Toast.LENGTH_SHORT).show();

    int currentNumberOfCovers = mDatabaseHelper.getCoverCount(tableid,roomid);
    // Create an AlertDialog builder
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Edit Number of Covers");

    // Set up the input
    final EditText input = new EditText(getContext());
    input.setInputType(InputType.TYPE_CLASS_NUMBER);
    input.setText(String.valueOf(currentNumberOfCovers));
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            int newNumberOfCovers = Integer.parseInt(input.getText().toString());

            mDatabaseHelper.updateCoverCount(newNumberOfCovers, tableid, roomid);
            Cursor filteredCursor = mDatabaseHelper.getTablesFilteredByMerged(String.valueOf(roomid));
            CoverClearedListener.onAmountCoverModified();

            mTableAdapter.swapCursor(filteredCursor);

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

    }
    public void decrementTransactionCounter() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("TransactionCounter", Context.MODE_PRIVATE);
        int lastCounter = sharedPreferences.getInt("counter", 1);

        // Ensure the counter does not go below 1
        if (lastCounter > 1) {
            lastCounter--; // Decrement the counter
        }

        // Save the updated counter value in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("counter", lastCounter);
        editor.apply();

        Log.d("TransactionCounter", "Counter decremented: " + lastCounter);
    }

    private List<PaymentMethodDataModel> fetchPaymentMethodDataBasedOnReportType(String reportType) {
        // Implement logic to fetch data based on the selected report type for payment method
        // Replace it with your actual logic
        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch payment method data based on report type
        // Replace the method and parameters with your actual database queries
        paymentMethodDataList = mDatabaseHelper.getPaymentMethodDataBasedOnReportType(reportType);
      //  Log.d("ReportPopupDialog", "Payment Method Data: " + paymentMethodDataList.toString());

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

    private List<DataModel> fetchDataBasedOnReportTypeAndCashier(String reportType, int cashierId) {
        // Implement the logic to fetch data based on the report type and cashier ID
        List<DataModel> dummyDataList = new ArrayList<>();
        dummyDataList = mDatabaseHelper.getDataBasedOnReportTypeAndCashierId(reportType, String.valueOf(cashierId));
        return dummyDataList;
    }

    private List<PaymentMethodDataModel> fetchPaymentMethodDataBasedOnReportTypeAndCashier(String reportType, int cashierId) {
        // Implement the logic to fetch payment method data based on the report type and cashier ID


        List<PaymentMethodDataModel> paymentMethodDataList = new ArrayList<>();

        // Assuming you have a method in YourDatabaseHelper to fetch payment method data based on report type
        // Replace the method and parameters with your actual database queries
        paymentMethodDataList = mDatabaseHelper.getPaymentMethodDataBasedOnReportTypeAndCashierId(reportType, cashierId);
       // Log.d("ReportPopupDialog", "Payment Method Data: " + paymentMethodDataList.toString());

        return paymentMethodDataList;
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
    public void onClearButtonClick(View view) {

        onclearButtonClick();


    }


    private void onclearButtonClick() {
        // Clear the entered PIN and update the PIN EditText

        numberOfPeopleEditText.setText("");
        numberOfPeopleEditText.setText("");
        numberOfPeopleEditText.requestFocus();

    }

    private List<Cashier> fetchCashiers() {
        List<Cashier> cashiers = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        String query = "SELECT " + COLUMN_CASHOR_id + ", " + COLUMN_CASHOR_NAME + " FROM " + TABLE_NAME_Users;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CASHOR_id));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CASHOR_NAME));
                cashiers.add(new Cashier(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cashiers;
    }

    public double calculateTaxforsplit(String price,String vatval) {
        double taxAmount = 0.0;

        double unitPriceInclusive = Double.parseDouble(price);
        double unitPriceExclusive = unitPriceInclusive / 1.15; // Removing 15% VAT to get exclusive price

        if (vatval.equals("VAT 15%")) {
            taxAmount = unitPriceExclusive * 0.15; // Calculate VAT based on exclusive price
        }

        // Ensure that the tax amount is rounded to two decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return taxAmount;
    }
    private void onBackspaceButtonClick() {
        // Check if there are characters to remove
        if (enterednumber.length() > 0) {
            // Remove the last character from the entered barcode
            enterednumber.deleteCharAt(enterednumber.length() - 1);
            numberOfPeopleEditText.setText(enterednumber.toString());
        }
    }
    public void onNumberButtonClick(String number) {
        if (numberOfPeopleEditText != null) {
            // Insert the letter into the EditText
            numberOfPeopleEditText.append(number);
        }
    }

    public void deleteInvalidTables() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // Get writable database

        // Define the WHERE clause to match tables starting with 'T' or having ROOM_ID = -2
        String whereClause = DatabaseHelper.TABLE_NUMBER + " LIKE ? OR " + DatabaseHelper.TABLE_NUMBER + " = ?";
        String[] whereArgs = new String[]{"T%", "-2"};

        // Execute the delete operation
        int rowsDeleted = db.delete(DatabaseHelper.TABLES, whereClause, whereArgs);

        // Log the result
        if (rowsDeleted > 0) {
            Log.d("Database Delete", rowsDeleted + " invalid table(s) deleted successfully.");
        } else {
            Log.d("Database Delete", "No invalid tables found to delete.");
        }

        db.close(); // Close the database connection
    }
    private void unmergeTable(String selectedTableNum) {


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
           Log.d("UnmergeTable", "Table " + selectedTableNum + " has been unmerged.");
        } else if (rowsUpdated == 0) {
            // No rows were updated
            Log.d("UnmergeTable", "Failed to unmerge table " + selectedTableNum + ". No rows were updated.");
        } else {
            // An error occurred
            Log.d("UnmergeTable", "Failed to unmerge table " + selectedTableNum + ". Error occurred during update.");
        }

        // Refresh the UI to reflect the changes
        // You may need to update your RecyclerView adapter or rerun the database query to fetch updated data


        SharedPreferences    sharedPreferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", "0").apply();
        sharedPreferences.edit().putInt("roomnum", Integer.parseInt(String.valueOf(roomid))).apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }

    // Method to show the reason input dialog
    private void showPinDialog(String activity, Runnable onSuccessAction) {
        // Inflate the PIN dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View pinDialogView = inflater.inflate(R.layout.pin_dialog, null);
        EditText pinEditText = pinDialogView.findViewById(R.id.editTextPIN);

        // Find buttons
        Button buttonClear = pinDialogView.findViewById(R.id.buttonClear);
        Button buttonLogin = pinDialogView.findViewById(R.id.buttonLogin);

        // Set up button click listeners
        setPinButtonClickListeners(pinDialogView, pinEditText);

        // Create the PIN dialog
        AlertDialog.Builder pinBuilder = new AlertDialog.Builder(getContext());
        pinBuilder.setTitle("Enter PIN")
                .setView(pinDialogView);
        AlertDialog pinDialog = pinBuilder.create();
        pinDialog.show();

        // Clear button functionality
        buttonClear.setOnClickListener(v -> onpinClearButtonClick(pinEditText));

        // Login button functionality
        buttonLogin.setOnClickListener(v -> {
            String enteredPIN = pinEditText.getText().toString();
            int cashorLevel = validatePIN(enteredPIN);

            if (cashorLevel != -1) { // PIN is valid
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

                // Check if the user has permission
                boolean accessAllowed = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, activity, cashorLevel);
                if (accessAllowed) {
                    String cashorName =mDatabaseHelper.getCashorNameByPin(enteredPIN);
                    int cashorId =mDatabaseHelper.getCashorIdByPin(enteredPIN);
                    mDatabaseHelper.logUserActivity(cashorId, cashorName, cashorLevel, activity);
                    onSuccessAction.run(); // Execute the provided action on success
                    pinDialog.dismiss(); // Dismiss the PIN dialog after successful login
                } else {
                    showPermissionDeniedDialog(); // Show a permission denied dialog
                }
            } else {
                Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchDataAndUpdateRecyclerView() {
        // Example: Fetch data from the database
        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
        String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactionsbytable(latesttransId, String.valueOf(roomid), tableid);


        if (cursor != null && cursor.getCount() > 0) {
            // If there is data, pass it to the adapter
            mSplitBillAdapter.swapCursor(cursor);
        } else {
            Log.d("RecyclerView", "No data found for the given transaction.");
        }
    }

    public void onPinNumberButtonClick(Button button, EditText pinEditText) {
        if (pinEditText != null) {
            String buttonText = button.getText().toString();

            switch (buttonText) {
                case "Clear": // Handle clear
                    pinEditText.setText("");
                    break;
                case "BS": // Handle backspace
                    CharSequence currentText = pinEditText.getText();
                    if (currentText.length() > 0) {
                        pinEditText.setText(currentText.subSequence(0, currentText.length() - 1));
                    }
                    break;
                default: // Handle numbers
                    pinEditText.append(buttonText);
                    break;
            }
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "EditText is not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPinButtonClickListeners(View pinDialogView, final EditText pinEditText) {
        int[] buttonIds = new int[] {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonClear
        };

        for (int id : buttonIds) {
            Button button = pinDialogView.findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPinNumberButtonClick((Button) v, pinEditText);
                }
            });
        }
    }
    private int validatePIN(String enteredPIN) {
        // Fetch the cashor level based on the entered PIN
        int cashorLevel = mDatabaseHelper.getCashorLevelByPIN(enteredPIN);

        // Return the cashor level if valid, or -1 if invalid
        return cashorLevel;
    }
    public void onpinClearButtonClick(EditText ReceivedEditText) {

        onclearButtonClick(ReceivedEditText);
        onPinclearButtonClick(ReceivedEditText);


    }
    private void onPinclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");

        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    private void onclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");
            // ReceivedEditText.setText("");
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    private void showReasonDialog(String receiptName) {
        AlertDialog.Builder reasonDialogBuilder = new AlertDialog.Builder(getActivity());
        View reasonDialogView = getLayoutInflater().inflate(R.layout.dialog_reason_input, null);
        reasonDialogBuilder.setView(reasonDialogView);
        reasonDialogBuilder.setTitle("Reason for Credit Note");

        AlertDialog reasonDialog = reasonDialogBuilder.create();
        reasonDialog.show();

        EditText reasonEditText = reasonDialogView.findViewById(R.id.reason_edit_text);
        Button submitButton = reasonDialogView.findViewById(R.id.submit_button);
        Button cancelButton = reasonDialogView.findViewById(R.id.cancel_button);

        // Handle the submit button click
        submitButton.setOnClickListener(v -> {
            String reason = reasonEditText.getText().toString();
            if (!reason.isEmpty()) {
                // Store the reason in the member variable
                creditNoteReason = reason;

            }
            reasonDialog.dismiss();
        });

        // Handle the cancel button click
        cancelButton.setOnClickListener(v -> reasonDialog.dismiss());
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Permission Denied")
                .setMessage("You do not have permission to access this feature.")
                .setPositiveButton("OK", null)
                .show();
    }


    // Method to handle credit note processing with the reason

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CoverAmountListener) {
            CoverClearedListener = (CoverAmountListener) context;
        }
    }
    public interface CoverAmountListener {


        void onAmountCoverModified();

    }
    private String extractNumericPart(String tableString) {
        // Split the string based on space and return the last part
        String[] parts = tableString.split(" ");
        return parts[parts.length - 1];
    }
    private void playSoundEffect() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }


}
