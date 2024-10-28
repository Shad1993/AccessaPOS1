package com.accessa.ibora.product.items;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.DeviceInfo;
import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.Sync.SyncService;
import com.accessa.ibora.Sync.Syncforold;
import com.bumptech.glide.Glide;
import com.accessa.ibora.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {
private  EditText searchEditText;
    FloatingActionButton mAddFab;
    FloatingActionButton mSyncFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private SharedPreferences sharedPreferences,usersharedPreferences;
    String cashorlevel;
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
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        // Get the current locale

        // Spinner
        spinner = view.findViewById(R.id.spinner);
        usersharedPreferences = getActivity().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
        // Retrieve the shared preferences
        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);


         cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level


        //arrow
        arrow=view.findViewById(R.id.spinner_icon);
        // Retrieve the items from the database
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllItems();

        List<String> items = new ArrayList<>();
        items.add(String.valueOf(getString(R.string.AllItems)));
        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LongDescription));
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseHelper = new DatabaseHelper(getContext());

        Cursor itemCursor = mDatabaseHelper.getAllItems();
        mAdapter = new ItemAdapter(getActivity(), itemCursor);
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
                Cursor newCursor = mDatabaseHelper.searchItems(newText);
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
        mSyncFab = view.findViewById(R.id.sync_fab);


        adapter = new SimpleCursorAdapter(getContext(), R.layout.activity_view_record, cursor1, froms, tos, 0);
        adapter.notifyDataSetChanged();
        // Set item click listener for RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int levelNumber = Integer.parseInt(cashorlevel);

                        if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "modifyItems_", levelNumber)) {

                            TextView idTextView = view.findViewById(R.id.id_text_view);
                            TextView subjectEditText = view.findViewById(R.id.name_text_view);
                            TextView longDescriptionEditText = view.findViewById(R.id.Longdescription_text_view);
                            TextView priceTextView = view.findViewById(R.id.price_text_view);

                            String id1 = idTextView.getText().toString();
                            String id = idTextView.getText().toString();
                            String title = subjectEditText.getText().toString();
                            String longDescription = longDescriptionEditText.getText().toString();

                            Intent modifyIntent = new Intent(requireActivity().getApplicationContext(), ModifyItemActivity.class);
                            modifyIntent.putExtra("title", title);
                            modifyIntent.putExtra("desc", longDescription);
                            modifyIntent.putExtra("id", id);

                            startActivity(modifyIntent);
                        }else{

                                Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );

        mAddFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int levelNumber = Integer.parseInt(cashorlevel);

                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "addItems_", levelNumber)) {
                    openNewActivity();
                }else {
                    Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();

                }
            }
        });

        mSyncFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelNumber = Integer.parseInt(cashorlevel);

                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "syncDatabase_", levelNumber)) {

                    //clear table
                    // Create an instance of MssqlDataSync
                    MssqlDataSync mssqlDataSync = new MssqlDataSync();

                    // Call the synchronization method

                    //mssqlDataSync.syncAllDataFromSQLiteToMSSQL(requireContext());
                    //mssqlDataSync.syncDataOptionsFromSQLiteToMSSQL(requireContext());
                    // mssqlDataSync.syncCostDataFromSQLiteToMSSQL(requireContext());
                    //  mssqlDataSync.syncSupplementsOptionsFromSQLiteToMSSQL(requireContext());
                    //  mssqlDataSync.syncTablesFromSQLiteToMSSQL(requireContext());
                    //  mssqlDataSync.syncRoomsFromSQLiteToMSSQL(requireContext());

        /*        mssqlDataSync.syncTransactionsFromSQLiteToMSSQL(requireContext());
                mssqlDataSync.syncTransactionHeaderFromMSSQLToSQLite(requireContext());
                mssqlDataSync.syncInvoiceSettlementFromMSSQLToSQLite(requireContext());
                mssqlDataSync.syncCountingReportDataFromSQLiteToMSSQL(requireContext());
                mssqlDataSync.syncCashReportDataFromSQLiteToMSSQL(requireContext());
                mssqlDataSync.syncFinancialReportDataFromSQLiteToMSSQL(requireContext());

               */
                    mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.TABLE_NAME);
                    mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.COST_TABLE_NAME);
                    mDatabaseHelper.deleteAllDataFromTableTable(DatabaseHelper.TABLES);
                    mDatabaseHelper.deleteAllDataFromRoomsTable(DatabaseHelper.ROOMS);
                    mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.CAT_TABLE_NAME);
                    // dbManager.deleteItemsWithSyncStatusNotOffline();
                    String androidVersion = DeviceInfo.getAndroidVersion();
                    Log.d("DeviceInfo", "Android Version: " + androidVersion);
                    // Trim the strings to avoid any leading or trailing whitespace issues
                    if (androidVersion.trim().equals("Android 7.1.1 (API Level 25) - Nougat MR1".trim())) {
                        Log.d("SyncService", "Starting Syncforold");
                        Syncforold.startSync(requireContext());
                    } else {
                        Log.d("SyncService", "Starting SyncService");
                        SyncService.startSync(requireContext());
                    }

                }else{
                    Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();

                }
            }
        });


        return view;
    }


    // Filter the RecyclerView based on the selected item
    private void filterRecyclerView(String selectedItem) {
        Cursor filteredCursor;
        if (selectedItem == null || selectedItem.equals(getString(R.string.AllItems))) {
            filteredCursor = mDatabaseHelper.getAllItems();
        } else {
            filteredCursor = mDatabaseHelper.searchItems(selectedItem);
        }
        mAdapter.swapCursor(filteredCursor);

        // Show or hide the empty state
        showEmptyState(mAdapter.getItemCount() <= 0);
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


        // Start the AddItemActivity
        Intent intent = new Intent(requireContext(), AddItemActivity.class);
        intent.putExtra("locale", currentLocale.toString());
        startActivity(intent);
    }



}