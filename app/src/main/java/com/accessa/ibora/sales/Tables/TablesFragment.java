package com.accessa.ibora.sales.Tables;


import static com.accessa.ibora.product.items.DatabaseHelper.TRANSACTION_TOTAL_TTC;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Settings.Rooms.SpacesItemDecoration;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.TicketFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class TablesFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private SalesFragment.ItemAddedListener itemAddedListener;
    private  List<String> data ;
    private  EditText searchEditText;
    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private OnTableClickListener tableClickListener;
    String cashierId;
String transactionIdInProgress;
    private OnReloadFragmentListener reloadListener;
    private String selectedTableToMerge;
    private String selectedTableToTransfer;

    public  String roomId,tableid;
    private TableAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private IWoyouService woyouService;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;

    final String[] froms = new String[]{DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.LongDescription, DatabaseHelper.Price};
    final int[] tos = new int[]{R.id.id, R.id.name, R.id.LongDescription, R.id.price};
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
    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        // Get the current locale
        // Retrieve the room ID from the arguments bundle
        SharedPreferences sharedPreference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
// Assume "your_preferences_name" is the name of your SharedPreferences file
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
         roomId = String.valueOf(preferences.getInt("room_id", 0));
        tableid = String.valueOf(preferences.getString("table_id", "0"));

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString("transaction_id", null);
        // Spinner
        spinner = view.findViewById(R.id.spinner);

        //arrow
        arrow=view.findViewById(R.id.spinner_icon);
        // Retrieve the rooms from the database
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getTablesFilteredByMerged(roomId);

        List<String> items = new ArrayList<>();
        items.add(getString(R.string.ALLTables));
        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NUMBER));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Create an ArrayAdapter for the spinner with the custom layout
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(), 0, items) {
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



            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.white));

                // Set the icon for the item
                ImageView iconImageView = convertView.findViewById(android.R.id.icon);


                // Set the text for the item
                textView.setText(getItem(position));

                return convertView;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Initialize the spinner and set the adapter
        spinner.setAdapter(spinnerAdapter);


        // Set a listener for item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                // Handle the selected item here
                filterRecyclerView(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
                filterRecyclerView(null); // Remove any applied filter
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick(); // Programmatically open the dropdown list

            }
        });

        // RecyclerView
        mRecyclerView = view.findViewById(R.id.recycler_view);
        int spanCount = 4; // Number of items to display in one line
        // Use LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // Adjust spacing - reduce the spacing
        int reducedSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_between_tables); // Set a reduced spacing value
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(spanCount, reducedSpacing);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Use the modified getAllRooms() method to fetch rooms
        Cursor roomCursor = mDatabaseHelper.getTablesFilteredByMerged(roomId);
        mAdapter = new TableAdapter(getActivity(), roomCursor);
        mRecyclerView.setAdapter(mAdapter);
        // Empty state
        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif()
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

        // SearchView
        mSearchView = view.findViewById(R.id.search_view);
        searchEditText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = mDatabaseHelper.searchRoom(newText);
                mAdapter.swapCursor(newCursor);
                if (newText.isEmpty()) {

                    searchEditText.setTextColor(getResources().getColor(android.R.color.white));

                } else {

                    searchEditText.setTextColor(getResources().getColor(R.color.white));

                }
                return true;
            }
        });

        dbManager = new DBManager(getContext());
        dbManager.open();
        Cursor cursor1 = dbManager.fetch();
        mAddFab = view.findViewById(R.id.add_fab);


        adapter = new SimpleCursorAdapter(getContext(), R.layout.activity_view_record, cursor1, froms, tos, 0);
        adapter.notifyDataSetChanged();
        // Set item click listener for RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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
                          String statusType= mDatabaseHelper.getLatestTransactionStatus(roomnum,tableNum);
                          String latesttransId= mDatabaseHelper.getLatestTransactionId(roomnum,tableNum,statusType);

                       //   if(latesttransId== null) {
                         //     showNumberOfCoversDialog(tableNum, roomnum);

                        //  }else{
                              updateTableId1(tableNum, roomnum);
                         // }
                            // Call the interface method to notify the MainActivity about the table click
                            notifyTableClicked(tableNum,roomnum);


                            refreshsales(roomnum,tableNum);


                            // Update the selected table ID in the adapter
                            mAdapter.setSelectedTableId(id,tableNum);

                            mDatabaseHelper.setItemsUnselected(roomnum, tableNum);

                            showSecondaryScreen(data);

                            // Notify the adapter that the data has changed, triggering a rebind of items
                            mAdapter.notifyDataSetChanged();




                    }


                    @Override
                    public void onLongItemClick(View view, int position) {


                        // Get the table ID from the clicked item
                        TextView tableNumTextView = view.findViewById(R.id.textViewTAN);
                        TextView roomnumTextView = view.findViewById(R.id.roomid);
                        String tableNum = tableNumTextView.getText().toString();
                        String roomnumNum = roomnumTextView.getText().toString();

                        // Show a dialog or a popup to select tables for merging
                        showMergeDialog(tableNum,roomnumNum);
                    }


                })
        );

        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        return view;
    }
    // Method to show popup dialog for number of covers
    private void showNumberOfCoversDialog(String  tablenum, String roomnum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Number of Covers");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numberOfCoversStr = input.getText().toString().trim();
                if (!numberOfCoversStr.isEmpty()) {
                    try {
                        int numberOfCovers = Integer.parseInt(numberOfCoversStr);
                        // Use numberOfCovers variable as needed
                        processNumberOfCovers(numberOfCovers,roomnum, tablenum);
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

    // Method to process the number of covers entered
    private void processNumberOfCovers(int numberOfCovers,String roomnum, String tableNum) {
        // Use the numberOfCovers variable as needed in your code
        // For example, store it as a class member or pass it to another method
        Log.d("NumberOfCovers", "Number of covers entered: " + numberOfCovers);

        // Continue with your application logic after setting numberOfCovers
        // Update the room ID in SharedPreferences
        updateTableId(tableNum,roomnum,numberOfCovers);
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
            Cursor cursor = mDatabaseHelper.getTransactionHeader(String.valueOf(roomId),tableid);
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

    private void updateTableId(String newTableId,String roomId,int numbercover) {


        // Update the table ID in SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("table_id", newTableId);
        editor.putString("table_num", newTableId);
        editor.putInt("servings", numbercover);
        editor.putInt("roomnum", Integer.parseInt(roomId));
        editor.putInt("room_id", Integer.parseInt(roomId));

        Log.d("roomtables", roomId + " " + newTableId);
        editor.apply();

        // Now the table ID in SharedPreferences is updated and can be accessed elsewhere in your app
        // Notify the adapter or update UI to reflect the selection change
        mAdapter.notifyDataSetChanged();
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
    private void filterRecyclerView(String selectedItem) {
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomId = String.valueOf(preferences.getInt("roomnum", 0));
        Cursor filteredCursor;

        // Update the query to filter out tables with merged = 0
        if (selectedItem == null || selectedItem.equals(getString(R.string.ALLTables))) {
            filteredCursor = mDatabaseHelper.getTablesFilteredByMerged(roomId);
        } else {
            // Log the selected item before executing the query
            Log.d("FilterRecyclerView", "Selected Item: " + selectedItem);

            // Execute the query
            filteredCursor = mDatabaseHelper.searchTables(roomId, selectedItem);

            // Log the cursor count and some data
            Log.d("FilterRecyclerView", "Cursor Count: " + filteredCursor.getCount());
            Log.d("RoomID", "RoomID: " + roomId);

        }

        mAdapter.swapCursor(filteredCursor);

        // Show or hide the empty state
        boolean isEmptyStateVisible = filteredCursor.getCount() <= 0;
        Log.d("FilterRecyclerView", "Empty State Visible: " + isEmptyStateVisible);
        showEmptyState(isEmptyStateVisible);
    }



    // Method to show the merge dialog


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
                if(oldTransactionId != null && newTransactionId != null) {
                    int table1cover = mDatabaseHelper.getNumberOfCoversByTransactionTicketNo(oldTransactionId);
                    int table2cover = mDatabaseHelper.getNumberOfCoversByTransactionTicketNo(newTransactionId);
                     totalcovers = table1cover + table2cover;
                }

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
        mAdapter.notifyDataSetChanged();
        // Send broadcast to refresh MainActivity
        // Navigate to MainActivity when OK is clicked

        sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", "0").apply();
        sharedPreferences.edit().putInt("roomnum", 0).apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);

    }
    private String extractNumericPart(String tableString) {
        // Split the string based on space and return the last part
        String[] parts = tableString.split(" ");
        return parts[parts.length - 1];
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
                Log.d("startwithTT", "startwithT");
            } else if (selectedTableNum.startsWith("T")) {
                unmergeTable(selectedTableNum);
                Log.d("startwithT", "startwithT");
            }
            Log.d("updateTableId", "Skipping update since latesttransId is null");
        }

        //mDatabaseHelper.deleteTransactionsByConditions(db,roomId,selectedTableNum,latesttransId);
        mAdapter.notifyDataSetChanged();
        sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
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

        mDatabaseHelper.updateTableIdInTransactions(db,cashierId,roomnum, selectedTableNum, newTableNumber,totalcovers);


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
        sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("table_id", newTableNumber).apply();
        sharedPreferences.edit().putInt("roomnum", Integer.parseInt(roomId)).apply();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }



    public void updateUIForRoomUpdate(int currentId) {


        // Use the modified getAllTables1() method to fetch updated tables data
        Cursor updatedTableCursor = mDatabaseHelper.getTablesFilteredByMerged(String.valueOf(currentId));
        mAdapter.swapCursor(updatedTableCursor);

        // Check if the dataset is empty and show/hide the empty state accordingly
        showEmptyState(updatedTableCursor.getCount() <= 0);
    }
    // Show or hide the empty state based on the item count
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



    public void openNewActivity() {
        Configuration configuration = getResources().getConfiguration();
        Locale currentLocale = configuration.locale;

        // Start the AddRoomsActivity
      //  Intent intent = new Intent(requireContext(), AddRoomsActivity.class);
       // intent.putExtra("locale", currentLocale.toString());
        //startActivity(intent);
    }

    public interface OnTableClickListener {
        void onTableClicked(String tableId, String roomnum);
    }
    public interface OnReloadFragmentListener {
        void onReloadFragment(String tableId, String roomnum);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTableClickListener) {
            tableClickListener = (OnTableClickListener) context;
            reloadListener = (OnReloadFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnTableClickListener");
        }

    }




    public void handleFilterAndInsert(String newTableName) {
        // Perform the insert operation using your DatabaseHelper or DBManager
        // For example:
        // long newRowId = mDatabaseHelper.insertTable(newTableName, roomId);

        // After inserting, refresh the RecyclerView to reflect the changes
        // Call the method to show tables based on the new table name
        showFilteredTables(newTableName);
    }

    private void showFilteredTables(String tableName) {
        // Implement the logic to filter tables based on the provided table name
        // You can use the tableName to filter the tables in your RecyclerView or perform any other action.
        // Update the RecyclerView adapter accordingly.
        // For example, you can call filterRecyclerView(tableName) method or update the query directly in your RecyclerView adapter.
        filterRecyclerView(tableName);
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
    // Call this method when a table is clicked
    private void notifyTableClicked(String tableId, String roomnum) {
        if (tableClickListener != null) {
            tableClickListener.onTableClicked(tableId,roomnum);
        }
    }

}