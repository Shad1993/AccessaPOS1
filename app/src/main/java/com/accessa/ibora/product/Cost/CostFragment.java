package com.accessa.ibora.product.Cost;

import android.content.Intent;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.SubDepartment.AddSubDepartmentActivity;
import com.accessa.ibora.product.SubDepartment.ModifySubDepartmentActivity;
import com.accessa.ibora.product.SubDepartment.SubDepartmentAdapter;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class CostFragment extends Fragment {


    private SearchView mSearchView;
    private DBManager dbManager;
    private CostAdapter mAdapter;
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
        View view = inflater.inflate(R.layout.cost_fragment, container, false);

        // Spinner
        spinner = view.findViewById(R.id.spinner);

        //arrow
        arrow=view.findViewById(R.id.spinner_icon);
        // Retrieve the items from the database
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllCosts();

        List<String> Costs = new ArrayList<>();
        Costs.add(getString(R.string.AllCosts));

        if (cursor.moveToFirst()) {
            do {
                String cost = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Barcode));
                Costs.add(cost);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Create an ArrayAdapter for the spinner with the custom layout
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(), 0, Costs) {
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

        Cursor CostCursor = mDatabaseHelper.getAllCosts();
        mAdapter = new CostAdapter(getActivity(), CostCursor);
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

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = mDatabaseHelper.searchCost(newText);
                mAdapter.swapCursor(newCursor);
                if (newText.isEmpty()) {
                    EditText searchEditText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    searchEditText.setTextColor(getResources().getColor(android.R.color.white));
                    searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
                } else {
                    EditText searchEditText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    searchEditText.setTextColor(getResources().getColor(R.color.white));
                    searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
                }
                return true;
            }
        });

        dbManager = new DBManager(getContext());
        dbManager.open();
        Cursor cursor1 = dbManager.fetch();



        adapter = new SimpleCursorAdapter(getContext(), R.layout.activity_view_record, cursor1, froms, tos, 0);
        adapter.notifyDataSetChanged();
        // Set item click listener for RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerDepartmentClickListener(getContext(), mRecyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView DeptNameEditText = view.findViewById(R.id.name_text_view);
                        TextView DeptCodeEditText = view.findViewById(R.id.deptcode_text_view);
                        TextView LastModifiedTextView = view.findViewById(R.id.LastModified_edittex);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );



        return view;
    }
    // Filter the RecyclerView based on the selected item
    private void filterRecyclerView(String selectedItem) {
        Cursor filteredCursor;
        if (selectedItem == null || selectedItem.equals(getString(R.string.AllCosts))) {
            filteredCursor = mDatabaseHelper.getAllCosts();
        } else {
            filteredCursor = mDatabaseHelper.searchCost(selectedItem);
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

}