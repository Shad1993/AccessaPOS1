package com.accessa.ibora.sales.ticket;

import static androidx.core.app.ActivityCompat.recreate;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_HEADER_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_ID;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TABLE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TICKET_NO;
import static com.accessa.ibora.product.items.DatabaseHelper._ID;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Report.ReportActivity;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.SplashActivity;

import com.accessa.ibora.SplashFlashActivity;
import com.accessa.ibora.printer.cloudPrinter.bluetoothPrinter;
import com.accessa.ibora.printer.externalprinterlibrary2.CloudPrinterActivity;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class TicketFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;

    private String cashierId,cashierLevel,shopname;
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
    private Toolbar toolbar;
    private DBManager dbManager;
    private String actualdate;
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

            // Call the method to display on the LCD here
            displayOnLCD();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSecondaryScreen(data);
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
        } else if
        (id == R.id.nav_logout) {
            MainActivity mainActivity = (MainActivity) requireActivity(); // Get the MainActivity object
            mainActivity.logout(); // Call the logout() function
            return true;
        }else if
        (id == R.id.clear_ticket) {

               clearTransact(); // Call the clearTransact() function on the CustomerLcdFragment
            return true;
        }else if
        (id == R.id.open_drawer ) {
            int level= Integer.parseInt(cashierLevel);
            if(level == 7){
                try {
                    woyouService.openDrawer(null);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
        }else {
                Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if
        (id == R.id.Report) {
            int level= Integer.parseInt(cashierLevel);
            if(level >= 6){
                Context context = getContext(); // Get the Context object
                Intent intent = new Intent(context, ReportActivity.class);
                startActivity(intent);

            }else {
                Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        // Handle other menu items as needed

        return super.onOptionsItemSelected(item);
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
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactions();
        actualdate = mDatabaseHelper.getCurrentDate();

        mAdapter = new TicketAdapter(getActivity(), cursor1);
        mRecyclerView.setAdapter(mAdapter);

        dbManager = new DBManager(getContext());
        dbManager.open();

        textViewVATs=view.findViewById(R.id.textViewVATs);
        textViewTotals=view.findViewById(R.id.textViewTotals);
        textViewVATs.setText(getString(R.string.tax) + " : Rs 0.00");
        textViewTotals.setText(getString(R.string.Total) + " : Rs 0.00");


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);


        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        cashierLevel = sharedPreference.getString("cashorlevel", null);
        shopname = sharedPreference.getString("ShopName", null);

        SharedPreferences shardPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        PosNum = shardPreference.getString(POSNumber, null);



        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper

        // Retrieve the total amount and total tax amount from the transactionheader table
        Cursor cursor = mDatabaseHelper.getTransactionHeader();
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


        // Retrieve the checkout button and set its click listener
        Button checkoutButton = view.findViewById(R.id.buttonCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the activity associated with the fragment
                AppCompatActivity activity = (AppCompatActivity) requireActivity();

                // Create and show the dialog fragment with the data
                validateticketDialogFragment dialogFragment = validateticketDialogFragment.newInstance(transactionIdInProgress);
                dialogFragment.setTargetFragment(TicketFragment.this, 0);
                dialogFragment.show(activity.getSupportFragmentManager(), "validate_transaction_dialog");
            }
        });


        // Retrieve the checkout button and set its click listener
        Button saveButton = view.findViewById(R.id.saveTicket);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = calculateTotalAmount();
                double TaxtotalAmount = calculateTotalTax();
                SaveTransaction(totalAmount,TaxtotalAmount);
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
                        String Quatity = QuantityEditText.getText().toString();
                        String Price = PriceEditText.getText().toString();
                         ItemId = itemIdEditText.getText().toString();


                        // Create and show the dialog fragment with the data
                        ModifyItemDialogFragment dialogFragment = ModifyItemDialogFragment.newInstance(Quatity, Price, LongDesc,ItemId);
                        dialogFragment.setTargetFragment(TicketFragment.this, 0);
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

    private void SaveTransaction(double totalAmount, double TaxtotalAmount) {


        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount);

        // Get the latest transaction counter value from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE);
        int latestTransactionCounter = sharedPreferences.getInt("transaction_counter_memo", 0);

        SharedPreferences sharedPreference = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreference.getString(TRANSACTION_ID_KEY, null);

        // Increment the transaction counter
        latestTransactionCounter++;

        // Generate the transaction ID with the format "MEMO-integer"
        String newTransactionId = "MEMO-" + latestTransactionCounter;
        // Update the transaction ID in the transaction table for transactions with status "InProgress"
        mDatabaseHelper.updateTransactionTransactionIdInProgress(transactionIdInProgress,newTransactionId);
        // Update the transaction ID in the header table for transactions with status "InProgress"
        mDatabaseHelper.updateHeaderTransactionIdInProgress(newTransactionId);



        // Update the transaction counter in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("transaction_counter_memo", latestTransactionCounter);
        editor.apply();
        // Update the transaction status for all in-progress transactions to "saved"
        mDatabaseHelper.updateAllTransactionsStatus(DatabaseHelper.TRANSACTION_STATUS_Saved);
        updateTransactionStatus();
        Intent intent = new Intent(getActivity(), SplashFlashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable window animation
        startActivity(intent);
        getActivity().finish();
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

        mDatabaseHelper.deleteDataByInProgressStatus();

        // Optionally, you can notify the user or perform any other actions after clearing the transaction
// Notify the listener that an item is added

        // Refresh the data in the RecyclerView
        refreshData(totalAmount, TaxtotalAmount);
              displayOnLCD();
              showSecondaryScreen(data);
     //   recreate(getActivity());
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
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
            // Secondary screen not found or not supported
            Toast.makeText(getActivity(), "Secondary screen not found or not supported", Toast.LENGTH_SHORT).show();
        }
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

    public void displayOnLCD() {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve the total amount and total tax amount from the transactionheader table
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
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
                cashierId
        );

        if (success) {
            showSecondaryScreen(data);
        } else {

            Intent intent = new Intent(getActivity(), SplashFlashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable window animation
            startActivity(intent);
            getActivity().finish();


        }


}


    public  void refreshData(double totalAmount, double TaxtotalAmount) {


        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions();
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

    private void playSoundEffect() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
