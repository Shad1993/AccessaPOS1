package com.accessa.ibora.Settings.Rooms;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.DeviceInfo;
import com.accessa.ibora.R;
import com.accessa.ibora.Sync.SyncService;
import com.accessa.ibora.Sync.SyncServiceroomstable;
import com.accessa.ibora.Sync.Syncforold;
import com.accessa.ibora.Sync.Syncforoldroomstable;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomsFragment extends Fragment {
    private  EditText searchEditText;
    FloatingActionButton mAddFab;
    FloatingActionButton mSyncFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private RoomsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences sharedPreferences,usersharedPreferences;
    String cashorlevel;
    private int tableNumber;
    private int seatCount;
    private String status;

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

        //arrow
        arrow=view.findViewById(R.id.spinner_icon);
        // Retrieve the rooms from the database
        mDatabaseHelper = new DatabaseHelper(getContext());
        usersharedPreferences = getActivity().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);


        cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level

        Cursor cursor = mDatabaseHelper.getAllRooms();

        List<String> items = new ArrayList<>();
        items.add(getString(R.string.ALLRooms));
        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROOM_NAME));
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
        int spanCount = 5; // Number of items to display in one line
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_between_rooms); // Set your desired spacing dimension
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Set the span size for items to be 1, so that each item takes one span
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);

        // Add spacing item decoration
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(spanCount, spacing);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Use the modified getAllRooms() method to fetch rooms
        Cursor roomCursor = mDatabaseHelper.getAllRooms();
        mAdapter = new RoomsAdapter(getActivity(), roomCursor);
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
        mSyncFab = view.findViewById(R.id.sync_fab);

        adapter = new SimpleCursorAdapter(getContext(), R.layout.activity_view_record, cursor1, froms, tos, 0);
        adapter.notifyDataSetChanged();
        // Set item click listener for RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int levelNumber = Integer.parseInt(cashorlevel);

                        if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "modifyRoomsAndTables_", levelNumber)) {

                            TextView idTextView = view.findViewById(R.id.id);
                            TextView subjectEditText = view.findViewById(R.id.name);
                            TextView longDescriptionEditText = view.findViewById(R.id.textViewseatcount);
                            TextView priceTextView = view.findViewById(R.id.textViewBRN);


                            String id = idTextView.getText().toString();
                            String title = subjectEditText.getText().toString();
                            String longDescription = longDescriptionEditText.getText().toString();


                            // Pass the room ID to the TableDetailsActivity
                            Intent tableDetailsIntent = new Intent(requireActivity().getApplicationContext(), TableDetailsActivity.class);
                            tableDetailsIntent.putExtra("roomId", id);
                            tableDetailsIntent.putExtra("roomName", title);
                            startActivity(tableDetailsIntent);
                        }else{
                            Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        int levelNumber = Integer.parseInt(cashorlevel);

                        if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "modifyRoomsAndTables_", levelNumber)) {

                            TextView idTextView = view.findViewById(R.id.id);
                        TextView subjectEditText = view.findViewById(R.id.name);

                        String roomId = idTextView.getText().toString();
                        String roomName = subjectEditText.getText().toString();

                        Log.d("LongItemClick", "RoomId: " + roomId + ", RoomName: " + roomName);

                        // Show the edit room dialog
                       // EditRoomDialogFragment dialogFragment = EditRoomDialogFragment.newInstance(roomId);
                       // dialogFragment.show(getChildFragmentManager(), "EditRoomDialog");
                        // Create an AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Room Options")
                                .setMessage("What would you like to do with the selected room?")
                                .setPositiveButton("Edit", (dialog, which) -> {
                                    // Handle the Edit option
                                    Intent intent = new Intent(requireContext(), EditRoomActivity.class);
                                    intent.putExtra("ROOM_ID", roomId); // Pass the room ID to the new activity
                                    startActivity(intent);
                                })
                                .setNegativeButton("Delete", (dialog, which) -> {
                                    // Handle the Delete option
                                    mDatabaseHelper.deleteRoom(roomId);
                                    mDatabaseHelper.deleteRoomById(Integer.parseInt(roomId));
                                    mAdapter.swapCursor(mDatabaseHelper.getAllRooms()); // Refresh the list after deletion
                                    dialog.dismiss();
                                })
                                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss()); // Dismiss the dialog
                        builder.create().show();
                        }else{
                            Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                        }
                    }


                })
        );

        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelNumber = Integer.parseInt(cashorlevel);

                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "addRoomsAndTables_", levelNumber)) {

                    openNewActivity();
                }else{
                    Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                }

            }
        });
        mSyncFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int levelNumber = Integer.parseInt(cashorlevel);

                if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "SyncRoomsAndTables_", levelNumber)) {

                    mDatabaseHelper.deleteAllDataFromTableTable(DatabaseHelper.TABLES);
                mDatabaseHelper.deleteAllDataFromRoomsTable(DatabaseHelper.ROOMS);
                String androidVersion = DeviceInfo.getAndroidVersion();
                Log.d("DeviceInfo", "Android Version: " + androidVersion);
                // Trim the strings to avoid any leading or trailing whitespace issues
                if (androidVersion.trim().equals("Android 7.1.1 (API Level 25) - Nougat MR1".trim())) {
                    Log.d("SyncService", "Starting Syncforoldroomstable");
                    Syncforoldroomstable.startSync(requireContext());
                } else {
                    Log.d("SyncService", "Starting SyncServiceroomstable");
                    SyncServiceroomstable.startSync(requireContext());
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
        if (selectedItem == null || selectedItem.equals(getString(R.string.ALLRooms))) {
            filteredCursor = mDatabaseHelper.getAllRooms();
        } else {
            filteredCursor = mDatabaseHelper.searchRoom(selectedItem);
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

        // Start the AddRoomsActivity
        Intent intent = new Intent(requireContext(), AddRoomsActivity.class);
        intent.putExtra("locale", currentLocale.toString());
        startActivity(intent);
    }



}