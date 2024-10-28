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
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ReceiptMenuFragment extends Fragment {

    private String toolbarTitle;
    private RecyclerView mRecyclerView;
    private DatabaseHelper mDatabaseHelper;
    private ReceiptAdapter mAdapter;
    private FragmentManager fragmentManager;
    private SearchView mSearchView;
    private EditText searchEditText;
    private Spinner spinner;
    private Spinner statusFilterSpinner;

    private String selectedDate;
    private String selectedStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle = getString(R.string.Receipts);
        setHasOptionsMenu(true);

        Fragment newFragment = new EmptyReceiptBodyFragment();
        fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
        fragmentTransaction.addToBackStack(newFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview_for_menu, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

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
                searchEditText.setTextColor(getResources().getColor(R.color.BleuAccessaText));
                return true;
            }
        });

        spinner = view.findViewById(R.id.date_filter_spinner);
        statusFilterSpinner = view.findViewById(R.id.status_filter_spinner);

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllDistinctReceipt();

        List<String> items = new ArrayList<>();
        items.add(getString(R.string.AllReceipt));
        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> spinnerAdapter = createSpinnerAdapter(items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDate = (String) parent.getItemAtPosition(position);
                filterRecyclerViews(selectedStatus, selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDate = null;
                filterRecyclerViews(selectedStatus, selectedDate); // Remove any applied filter
            }
        });

        ArrayAdapter<String> statusAdapter = createStatusAdapter();
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusFilterSpinner.setAdapter(statusAdapter);

        statusFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = (String) parent.getItemAtPosition(position);
                filterRecyclerViews(selectedStatus, selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStatus = null;
                filterRecyclerViews(selectedStatus, selectedDate); // Remove any applied filter
            }
        });

        Cursor receiptCursor = mDatabaseHelper.getAllReceipt();
        mAdapter = new ReceiptAdapter(getContext(), receiptCursor);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerDepartmentClickListener(getContext(), mRecyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView deptNameEditText = view.findViewById(R.id.name_text_view);
                        TextView deptCodeEditText = view.findViewById(R.id.total_text_view);
                        TextView lastModifiedTextView = view.findViewById(R.id.transdate_edittex);

                        String id1 = idTextView.getText().toString();
                        String id = idTextView.getText().toString();
                        String name = deptNameEditText.getText().toString();
                        String deptCode = deptCodeEditText.getText().toString();

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

    private void filterRecyclerViews(String selectedStatus, String selectedDate) {
        Cursor filteredCursor;
        if ((selectedStatus == null || selectedStatus.equals(getString(R.string.AllStatus))) && (selectedDate == null || selectedDate.equals(getString(R.string.AllReceipt)))) {
            filteredCursor = mDatabaseHelper.getAllReceipt();
        } else if (selectedStatus == null || selectedStatus.equals(getString(R.string.AllStatus))) {
            filteredCursor = mDatabaseHelper.searchTransactions(selectedDate);
        } else if (selectedDate == null || selectedDate.equals(getString(R.string.AllReceipt))) {
            filteredCursor = mDatabaseHelper.getReceiptsByStatus(selectedStatus);
        } else {
            filteredCursor = mDatabaseHelper.getReceiptsByStatusAndDate(selectedStatus, selectedDate);
        }
        mAdapter.swapCursor(filteredCursor);
        showEmptyState(mAdapter.getItemCount() <= 0);
    }

    private void showEmptyState(boolean showEmpty) {
        AppCompatImageView imageView = getView().findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif().load(R.drawable.receiptgif).into(imageView);
        FrameLayout emptyFrameLayout = getView().findViewById(R.id.empty_frame_layout);
        if (showEmpty) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<String> items) {
        return new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.BleuAccessaText));

                String selectedItem = getItem(position);
                if (selectedItem.equals(getString(R.string.AllReceipt))) {
                    // Modify the displayed text here based on your condition
                    selectedItem = getString(R.string.AllReceipt);
                } else {
                    // Modify the displayed text here based on your condition
                    // For example, you can format the date string
                    // selectedItem = formatDate(selectedItem);
                }

                // Set the text for the selected item
                textView.setText(selectedItem);

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.BleuAccessaText));
                textView.setText(getItem(position));

                return convertView;
            }
        };
    }

    private ArrayAdapter<String> createStatusAdapter() {
        Cursor statusCursor = mDatabaseHelper.getDistinctStatusCursor();
        List<String> statusList = new ArrayList<>();
        statusList.add(getString(R.string.AllStatus));

        if (statusCursor.moveToFirst()) {
            do {
                String status = statusCursor.getString(statusCursor.getColumnIndex(DatabaseHelper.TRANSACTION_STATUS));
                statusList.add(status);
            } while (statusCursor.moveToNext());
        }
        statusCursor.close();

        return new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, statusList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.BleuAccessaText));

                String originalStatus = getItem(position);
                String displayedStatus;
                // Modify the displayed text here based on your condition
                if (originalStatus.equals("InProgress")) {
                    displayedStatus = getString(R.string.inprogress);
                } else if (originalStatus.equals("Proforma")) {
                    displayedStatus = getString(R.string.Proforma);
                } else if (originalStatus.equals("DBN")) {
                    displayedStatus = getString(R.string.DBN);
                } else if (originalStatus.equals("CRN")) {
                    displayedStatus = getString(R.string.CRN);
                } else if (originalStatus.equals("Completed")) {
                    displayedStatus = getString(R.string.completed);
                 } else if (originalStatus.equals("OLDPRF")) {
                displayedStatus = getString(R.string.Proforma);
               } else if (originalStatus.equals("CancelledOrder")) {
                displayedStatus = getString(R.string.CancelledOrder);
            } else {
                    displayedStatus = originalStatus;
                }

                // Set the text for the selected item
                textView.setText(displayedStatus);

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.BleuAccessaText));

                String originalStatus = getItem(position);
                String displayedStatus;
                // Modify the displayed text here based on your condition
                if (originalStatus.equals("InProgress")) {
                    displayedStatus = getString(R.string.inprogress);
                } else if (originalStatus.equals("Proforma")) {
                    displayedStatus = getString(R.string.Proforma);
                } else if (originalStatus.equals("DBN")) {
                    displayedStatus = getString(R.string.DBN);
                } else if (originalStatus.equals("CRN")) {
                    displayedStatus = getString(R.string.CRN);
                } else if (originalStatus.equals("Completed")) {
                    displayedStatus = getString(R.string.completed);
             } else if (originalStatus.equals("OLDPRF")) {
                displayedStatus = getString(R.string.Proforma);
            } else if (originalStatus.equals("CancelledOrder")) {
                    displayedStatus = getString(R.string.CancelledOrder);
                } else {
                    displayedStatus = originalStatus;
                }

                // Set the text for the dropdown item
                textView.setText(displayedStatus);

                return convertView;
            }
        };
    }

}
