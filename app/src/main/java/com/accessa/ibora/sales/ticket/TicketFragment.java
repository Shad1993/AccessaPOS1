package com.accessa.ibora.sales.ticket;

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
import com.accessa.ibora.product.Cost.CostAdapter;
import com.accessa.ibora.product.Cost.ModifyCostActivity;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.SubDepartment.AddSubDepartmentActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class TicketFragment extends Fragment {
    private EditText searchEditText;
    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private TicketAdapter mAdapter;
    private TextView textViewSubtotals;
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
        View view = inflater.inflate(R.layout.ticket_fragment, container, false);





            // RecyclerView
            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mDatabaseHelper = new DatabaseHelper(getContext());

            Cursor Cursor = mDatabaseHelper.getAllTransactions();
            mAdapter = new TicketAdapter(getActivity(), Cursor);
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

                            String id1 = idTextView.getText().toString();
                            String id = idTextView.getText().toString();
                            String name = DeptNameEditText.getText().toString();
                            String DeptCode = DeptCodeEditText.getText().toString();


                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // Do whatever you want on long item click
                        }
                    })
            );






        return view;
    }


}