package com.accessa.ibora.sales.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
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

import com.accessa.ibora.R;

import com.accessa.ibora.Settings.Rooms.SpacesItemDecoration;

import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TablesFragment extends Fragment {
    private  EditText searchEditText;
    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private OnTableClickListener tableClickListener;

    private OnReloadFragmentListener reloadListener;


    public  String roomId;
    private TableAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;

    final String[] froms = new String[]{DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.LongDescription, DatabaseHelper.Price};
    final int[] tos = new int[]{R.id.id, R.id.name, R.id.LongDescription, R.id.price};

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

// Assume "your_preferences_name" is the name of your SharedPreferences file
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);

// Retrieve the room id, with a default value of 1 if not found
         roomId = preferences.getString("room_id_key", "1");


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

                        // Call the interface method to notify the MainActivity about the table click
                          notifyTableClicked(tableNum,roomnum);


                        refreshsales(roomnum,tableNum);
                        // Update the room ID in SharedPreferences
                        updateTableId(tableNum);

                        // Update the selected table ID in the adapter
                        mAdapter.setSelectedTableId(tableNum);

                        mDatabaseHelper.setItemsUnselected(roomnum, tableNum);

                        // Notify the adapter that the data has changed, triggering a rebind of items
                        mAdapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Get the table ID from the clicked item
                        TextView tableNumTextView = view.findViewById(R.id.textViewTAN);
                        String tableNum = tableNumTextView.getText().toString();

                        // Show a dialog or a popup to select tables for merging
                        showMergeDialog(tableNum);
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


    private void updateTableId(String newTableId) {
        // Update the table ID in SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("table_id", newTableId);
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
    private void showMergeDialog(final String selectedTableNum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a Table to Merge");

        // Fetch the list of available tables from your database
        List<String> availableTables = getAvailableTables(selectedTableNum);

        // Convert the list to an array for dialog selection
        final CharSequence[] tableArray = availableTables.toArray(new CharSequence[availableTables.size()]);

        builder.setItems(tableArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedTableToMerge = tableArray[which].toString();


                mergeTables(selectedTableNum, selectedTableToMerge);
            }
        });

        builder.show();
    }

    private List<String> getAvailableTables(String selectedTableNum) {
        // Query your database to get a list of tables excluding the selected one and merged tables
        // Adjust the query based on your database schema

        // For example:
        List<String> tables = new ArrayList<>();

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.TABLE_NUMBER + " FROM " + DatabaseHelper.TABLES +
                " WHERE " + DatabaseHelper.TABLE_NUMBER + " != ? AND (" +
                DatabaseHelper.MERGED + " = 0 OR " + DatabaseHelper.MERGED + " IS NULL)";
        Cursor cursor = db.rawQuery(query, new String[]{selectedTableNum});

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


    private void mergeTables(String selectedTableNum, String selectedTableToMerge) {
        // Update your database to mark both tables with the same MERGED_SET_ID
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues valuesTable1 = new ContentValues();
        ContentValues valuesTable2 = new ContentValues();

        // Concatenate the TABLE_NUMBER of the first table with the second table
        String newTableNumber = "T " + selectedTableNum + " + T " + selectedTableToMerge;

        // Update the first table
        valuesTable1.put(DatabaseHelper.MERGED_SET_ID, newTableNumber);
        valuesTable1.put(DatabaseHelper.MERGED, 1);

        db.update(DatabaseHelper.TABLES, valuesTable1, DatabaseHelper.TABLE_NUMBER + " = ?",
                new String[]{selectedTableNum});

        // Update the second table

        valuesTable2.put(DatabaseHelper.MERGED, 1);

        db.update(DatabaseHelper.TABLES, valuesTable2, DatabaseHelper.TABLE_NUMBER + " = ?",
                new String[]{selectedTableToMerge});

        db.close();

        // Refresh the UI to reflect the changes
        // You may need to update your RecyclerView adapter or rerun the database query to fetch updated data
        mAdapter.notifyDataSetChanged();
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