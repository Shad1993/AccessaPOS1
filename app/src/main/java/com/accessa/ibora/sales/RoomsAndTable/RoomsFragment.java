package com.accessa.ibora.sales.RoomsAndTable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.Rooms.AddRoomsActivity;
import com.accessa.ibora.Settings.Rooms.SpacesItemDecoration;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomsFragment extends Fragment {

    private OnRoomUpdateListener onRoomUpdateListener;

    private static final String PREF_FILE_NAME = "room_and_table_prefs";
    private static final String PREF_ROOM_ID = "room_id";
    private static final String PREF_TABLE_ID = "table_id";

    private PopupWindow popupWindow;
    private  EditText searchEditText;
    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private RoomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private static final String PREF_CURRENT_ID = "current_id";

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
        View view = inflater.inflate(R.layout.fragment_roomstable, container, false);
        // Get the current locale
// Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        // Use the modified getAllRooms() method to fetch rooms


        // Set default value for current ID if not set
        if (!sharedPreferences.contains(PREF_CURRENT_ID)) {
            sharedPreferences.edit().putInt(PREF_CURRENT_ID, 1).apply();
        }
        // Spinner
        spinner = view.findViewById(R.id.spinner);

        //arrow
        arrow=view.findViewById(R.id.spinner_icon);
        // Retrieve the rooms from the database
        mDatabaseHelper = new DatabaseHelper(getContext());
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
        int spanCount = 1; // Number of items to display in one line
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
        Cursor roomCursor = mDatabaseHelper.getAllRooms(); // Assuming mDatabaseHelper is your DatabaseHelper instance

        mAdapter = new RoomAdapter(getActivity(), roomCursor);
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

                        TextView priceTextView = view.findViewById(R.id.textViewBRN);

                        String id = idTextView.getText().toString();
                        String title = subjectEditText.getText().toString();


                        // Here, you can dynamically update your UI based on the clicked item
                        updateUIForClickedItem(id, title);
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
                openNewActivity();
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

    public void setOnRoomUpdateListener(OnRoomUpdateListener listener) {
        this.onRoomUpdateListener = listener;
    }
    public void openNewActivity() {
        Configuration configuration = getResources().getConfiguration();
        Locale currentLocale = configuration.locale;

        // Start the AddRoomsActivity
        Intent intent = new Intent(requireContext(), AddRoomsActivity.class);
        intent.putExtra("locale", currentLocale.toString());
        startActivity(intent);
    }
    private void updateUIForClickedItem(String id, String title) {
        // Get the maximum ID from the database
        int maxRoomId = mDatabaseHelper.getMaxRoomId();
        int newRoomId = Integer.parseInt(id);

        // Get the next room ID from SharedPreferences
        int currentRoomId = sharedPreferences.getInt(PREF_ROOM_ID, newRoomId);

        // Get the room name associated with the current room ID
        String roomName = mDatabaseHelper.getRoomNameForId(String.valueOf(currentRoomId));

        // Update your UI elements based on the room name
        TextView selectedItemTextView = getView().findViewById(R.id.BuyerAdapter);
        selectedItemTextView.setText(roomName);

        // Notify the listener to update the TablesFragment with room ID
        if (onRoomUpdateListener != null) {
            onRoomUpdateListener.onRoomUpdate(currentRoomId);
        }
        int actualroom=currentRoomId;
        // Increment the current room ID and save it to SharedPreferences
        currentRoomId = (currentRoomId % maxRoomId) + 1;
        sharedPreferences.edit().putInt(PREF_ROOM_ID, currentRoomId).apply();

        // Now, handle the table ID (assuming you want to set it to 1)
        int defaultTableId = 0;
        sharedPreferences.edit().putInt(PREF_TABLE_ID, defaultTableId).apply();
        sharedPreferences.edit().putString("roomnum", String.valueOf(actualroom)).apply();
    }



    public interface OnRoomUpdateListener {
        void onRoomUpdate(int currentId);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onRoomUpdateListener = (OnRoomUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRoomUpdateListener");
        }
    }

}