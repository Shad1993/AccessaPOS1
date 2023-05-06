package com.accessa.ibora.product.items;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FirstFragment  extends Fragment   {

    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private ItemAdapter mAdapter;
    private TextView emptyView;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;

    private DatabaseHelper mDatabaseHelper;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.Name, DatabaseHelper.DESC,DatabaseHelper.Price };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc , R.id.price};
    Toolbar mActionBarToolbar;

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first,container,false);

        emptyView = (TextView) view.findViewById(R.id.empty);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseHelper = new DatabaseHelper(getContext());

        Cursor cursor = mDatabaseHelper.getAllItems();
        mAdapter = new ItemAdapter(getActivity(), cursor);
        mRecyclerView.setAdapter(mAdapter);


// display message if empty

        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }


        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = mDatabaseHelper.searchItems(newText);
                mAdapter.swapCursor(newCursor);
                return true;
            }
        });


        dbManager = new DBManager(getContext());
        dbManager.open();
        Cursor cursor1 = dbManager.fetch();
        mAddFab = view.findViewById(R.id.add_fab);


        adapter = new SimpleCursorAdapter(getContext(), R.layout.activity_view_record, cursor1, from, to, 0);
        adapter.notifyDataSetChanged();



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView idTextView = (TextView) view.findViewById(R.id.id_text_view);
                        TextView subject_edittext = (TextView) view.findViewById(R.id.name_text_view);
                        TextView Longdescription_edittext = (TextView) view.findViewById(R.id.Longdescription_text_view);
                        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);


                        String id1 = idTextView.getText().toString();

                        // retrieve
                        DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                        Cursor cursor = dbHelper.getDataById(Long.parseLong(id1));
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                String id = String.valueOf(cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID)));
                                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Name));
                               String description =cursor.getString(cursor.getColumnIndex(DatabaseHelper.LongDescription));
                                Item data = new Item(id, name, description);
                                Intent intent = new Intent(view.getContext(), ModifyItemActivity.class);
                                intent.putExtra("data", (CharSequence) data);
                                view.getContext().startActivity(intent);
                            }
                            cursor.close();
                        }

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );





        ;

        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivity();
            }
        });
        return view;
    }



    public void openNewActivity(){
        Intent intent = new Intent(getContext(), AddItemActivity.class);
        startActivity(intent);
    }





}