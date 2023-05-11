package com.accessa.ibora.product.category;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import android.graphics.Color;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryFragment  extends Fragment   {
    private int CategoryColor;
    FloatingActionButton CatAddFab;
    private SearchView CatSearchView;
    private CategoryDBManager CatManager;
    private CategoryAdaptor CatAdapter;
    private TextView emptyView;
    private RecyclerView CatRecyclerView;
    private SimpleCursorAdapter Catadapter;

    private CategoryDatabaseHelper CatDatabaseHelper;

    final String[] from = new String[] { CategoryDatabaseHelper._ID,
            CategoryDatabaseHelper.CatName, CategoryDatabaseHelper.Color };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc , R.id.price};
    Toolbar mActionBarToolbar;

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CategoryColor=0;
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cat_fragment,container,false);



        //RecyclerView
        CatRecyclerView = view.findViewById(R.id.recycler_view);

        CatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CatDatabaseHelper = new CategoryDatabaseHelper(getContext());

        Cursor cursor = CatDatabaseHelper.getAllCategory();

        CatRecyclerView.setAdapter(CatAdapter);


        CatAdapter = new CategoryAdaptor(getActivity(), cursor, CategoryColor);
        CatRecyclerView.setAdapter(CatAdapter);


// Get a reference to the AppCompatImageView
        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);

// Load the GIF using Glide
        Glide.with(getContext())
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

// Find the empty FrameLayout
        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);

        if (CatAdapter.getItemCount() <= 0) {
            CatRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            CatRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
        CatSearchView = view.findViewById(R.id.search_view);
        CatSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = CatDatabaseHelper.searchCategory(newText);
                CatAdapter.swapCursor(newCursor);
                if (newText.isEmpty()) {
                    // Set default text color
                    EditText searchEditText = CatSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    searchEditText.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                } else {
                    // Set custom text color
                    EditText searchEditText = CatSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    searchEditText.setTextColor(getResources().getColor(R.color.white));
                }
                return true;
            }
        });


        CatManager = new CategoryDBManager(getContext());
        CatManager.open();
        Cursor cursor1 = CatManager.fetch();
        CatAddFab = view.findViewById(R.id.add_fab);


        Catadapter = new SimpleCursorAdapter(getContext(), R.layout.viewcategory_activity, cursor1, from, to, 0);
        Catadapter.notifyDataSetChanged();



        CatRecyclerView.addOnItemTouchListener(
                new RecyclerCategoryClickListener(getContext(), CatRecyclerView ,new RecyclerCategoryClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView idTextView = (TextView) view.findViewById(R.id.id_text_view);
                        TextView CatName_edittext = (TextView) view.findViewById(R.id.Catname_text_view);
                        TextView Color_edittext = (TextView) view.findViewById(R.id.Color_text_view);



                        String id1 = idTextView.getText().toString();

                        String id = idTextView.getText().toString();
                        String CatName = CatName_edittext.getText().toString();
                        String Color =Color_edittext .getText().toString();

                        Intent modify_intent = new Intent(getActivity().getApplicationContext(), ModifyCategoryActivity.class);
                        modify_intent.putExtra("CatName", CatName);
                        modify_intent.putExtra("Color", Color);
                        modify_intent.putExtra("id", id);

                        startActivity(modify_intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );





        ;

        CatAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivity();
            }
        });
        return view;
    }



    public void openNewActivity(){
        Intent intent = new Intent(getContext(), AddCategoryActivity.class);
        startActivity(intent);
    }





}