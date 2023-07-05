package com.accessa.ibora.Receipt;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity {
    private EditText searchEditText;
    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private ReceiptAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;

    final String[] froms = new String[]{DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.LongDescription, DatabaseHelper.Price};
    final int[] tos = new int[]{R.id.id, R.id.name, R.id.LongDescription, R.id.price};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity);

        spinner = findViewById(R.id.spinner);
        arrow = findViewById(R.id.spinner_icon);
        mDatabaseHelper = new DatabaseHelper(ReceiptActivity.this);
        Cursor cursor = mDatabaseHelper.getAllReceipt();

        List<String> Receipt = new ArrayList<>();
        Receipt.add(getString(R.string.AllReceipt));

        if (cursor.moveToFirst()) {
            do {
                String receipt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TICKET_NO));
                Receipt.add(receipt);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(ReceiptActivity.this, 0, Receipt) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(ContextCompat.getColor(ReceiptActivity.this, R.color.white));

                textView.setText(getItem(position));

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                textView.setTextColor(ContextCompat.getColor(ReceiptActivity.this, R.color.white));

                textView.setText(getItem(position));

                return convertView;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                filterRecyclerView(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterRecyclerView(null);
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ReceiptActivity.this));

        mDatabaseHelper = new DatabaseHelper(ReceiptActivity.this);
        Cursor ReceiptCursor = mDatabaseHelper.getAllReceipt();
        mAdapter = new ReceiptAdapter(ReceiptActivity.this, ReceiptCursor);
        mRecyclerView.setAdapter(mAdapter);

        AppCompatImageView imageView = findViewById(R.id.empty_image_view);
        Glide.with(ReceiptActivity.this).asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);
        FrameLayout emptyFrameLayout = findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }

        mSearchView = findViewById(R.id.search_view);
        searchEditText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(ReceiptActivity.this, android.R.color.white));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = mDatabaseHelper.searchReceipt(newText);
                mAdapter.swapCursor(newCursor);
                if (newText.isEmpty()) {
                    searchEditText.setTextColor(ContextCompat.getColor(ReceiptActivity.this, android.R.color.white));
                } else {
                    searchEditText.setTextColor(ContextCompat.getColor(ReceiptActivity.this, android.R.color.white));
                }
                return true;
            }
        });

        dbManager = new DBManager(ReceiptActivity.this);
        dbManager.open();
        Cursor cursor1 = dbManager.fetch();


        adapter = new SimpleCursorAdapter(ReceiptActivity.this, R.layout.activity_view_record, cursor1, froms, tos, 0);
        adapter.notifyDataSetChanged();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerDepartmentClickListener(ReceiptActivity.this, mRecyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView DeptNameEditText = view.findViewById(R.id.name_text_view);
                        TextView DeptCodeEditText = view.findViewById(R.id.deptcode_text_view);
                        TextView LastModifiedTextView = view.findViewById(R.id.LastModified_edittex);

                        String id1 = idTextView.getText().toString();
                        String id = idTextView.getText().toString();
                        String name = DeptNameEditText.getText().toString();
                        String DeptCode = DeptCodeEditText.getText().toString();



                        Intent modifyIntent = new Intent(ReceiptActivity.this, TransactionDetailsActivity.class);

                        modifyIntent.putExtra("id", name);

                        startActivity(modifyIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );


    }

    private void filterRecyclerView(String selectedItem) {
        Cursor filteredCursor;
        if (selectedItem == null || selectedItem.equals(getString(R.string.AllReceipt))) {
            filteredCursor = mDatabaseHelper.getAllReceipt();
        } else {
            filteredCursor = mDatabaseHelper.searchReceipt(selectedItem);
        }
        mAdapter.swapCursor(filteredCursor);

        showEmptyState(mAdapter.getItemCount() <= 0);
    }

    private void showEmptyState(boolean showEmpty) {
        AppCompatImageView imageView = findViewById(R.id.empty_image_view);
        Glide.with(ReceiptActivity.this).asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);
        FrameLayout emptyFrameLayout = findViewById(R.id.empty_frame_layout);
        if (showEmpty) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
    }


}

