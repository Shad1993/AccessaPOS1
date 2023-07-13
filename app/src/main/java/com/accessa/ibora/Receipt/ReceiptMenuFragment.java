package com.accessa.ibora.Receipt;


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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.accessa.ibora.R;
import com.accessa.ibora.Settings.QRMethods.QRSettingsFragment;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ReceiptMenuFragment extends Fragment {


    private String toolbarTitle;
    private RecyclerView simpleList;

    private DatabaseHelper mDatabaseHelper;
    private ReceiptAdapter mAdapter;

    private FragmentManager fragmentManager;

    private SearchView mSearchView;
    private EditText searchEditText;
    private Spinner spinner;
    private Spinner statusFilterSpinner;
    private RecyclerView mRecyclerView;
    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        toolbarTitle = getString(R.string.Receipts);
        super.onCreate(savedInstanceState);
        toolbarTitle = getString(R.string.Receipts);


        setHasOptionsMenu(true);
        Fragment newFragment = new EmptyReceiptBodyFragment();
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
        fragmentTransaction.addToBackStack(newFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview_for_menu,container,false);

        // Get the FragmentManager
        fragmentManager = requireActivity().getSupportFragmentManager();


//menu items

        mRecyclerView= view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // SearchView
        mSearchView = view.findViewById(R.id.search_view);
        searchEditText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.BleuAccessaText));
        searchEditText.setHintTextColor(getResources().getColor(R.color.BleuAccessaText));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = mDatabaseHelper.searchTransactions(newText);
                mAdapter.swapCursor(newCursor);
                if (newText.isEmpty()) {

                    searchEditText.setTextColor(getResources().getColor(R.color.BleuAccessaText));

                } else {

                    searchEditText.setTextColor(getResources().getColor(R.color.BleuAccessaText));

                }
                return true;
            }
        });

        // Spinner
        spinner = view.findViewById(R.id.date_filter_spinner);
        statusFilterSpinner = view.findViewById(R.id.status_filter_spinner);


        // Retrieve the items from the database
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllDistinctReceipt();

        List<String> items = new ArrayList<>();
        items.add(String.valueOf(getString(R.string.AllReceipt)));
        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED));
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
                textView.setTextColor(getResources().getColor(R.color.BleuAccessaText));



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
                textView.setTextColor(getResources().getColor(R.color.BleuAccessaText));

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

        statusFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                filterRecyclerViews(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
                filterRecyclerViews(null); // Remove any applied filter
            }
        });


        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor ReceiptCursor = mDatabaseHelper.getAllReceipt();
        mAdapter = new ReceiptAdapter(getContext(), ReceiptCursor);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerDepartmentClickListener(getContext(), mRecyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {



                    // Handle item click here


                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView DeptNameEditText = view.findViewById(R.id.name_text_view);
                        TextView DeptCodeEditText = view.findViewById(R.id.total_text_view);
                        TextView LastModifiedTextView = view.findViewById(R.id.transdate_edittex);

                        String id1 = idTextView.getText().toString();
                        String id = idTextView.getText().toString();
                        String name = DeptNameEditText.getText().toString();
                        String DeptCode = DeptCodeEditText.getText().toString();

                        // Example: navigate to ReceiptBodyFragment and pass ID
                        String itemId = name;
                        Fragment newFragment = ReceiptBodyFragment.newInstance(itemId);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );

        return view;
    }
    private void filterRecyclerViews(String selectedStatus) {
        Cursor filteredCursor;
        if (selectedStatus == null || selectedStatus.equals("All")) {
            filteredCursor = mDatabaseHelper.getAllReceipt();
        } else {
            filteredCursor = mDatabaseHelper.getReceiptsByStatus(selectedStatus);
        }
        mAdapter.swapCursor(filteredCursor);

        // Show or hide the empty state
        showEmptyState(mAdapter.getItemCount() <= 0);
    }
    private void filterRecyclerView(String selectedItem) {
        Cursor filteredCursor;
        if (selectedItem == null || selectedItem.equals(getString(R.string.AllReceipt))) {
            filteredCursor = mDatabaseHelper.getAllReceipt();
        } else {
            filteredCursor = mDatabaseHelper.searchTransactions(selectedItem);
        }
        mAdapter.swapCursor(filteredCursor);

        // Show or hide the empty state
        showEmptyState(mAdapter.getItemCount() <= 0);
    }
    private void showEmptyState(boolean showEmpty) {
        AppCompatImageView imageView = getView().findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif()
                .load(R.drawable.receiptgif)
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


}
