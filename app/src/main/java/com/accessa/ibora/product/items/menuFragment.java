package com.accessa.ibora.product.items;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.sales.Sales.ItemGridAdapter;
import com.accessa.ibora.sales.Sales.SalesFragment;

import java.util.ArrayList;
import java.util.List;

public class menuFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<String> categories;
    private DatabaseHelper dbHelper;


    private ItemGridAdapter itemGridAdapter; // Add this variable



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        recyclerView = view.findViewById(R.id.categoryRecyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        categories = fetchCategories();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(categoryAdapter);

        // Initialize the ItemGridAdapter
        itemGridAdapter = new ItemGridAdapter(requireContext(), null); // Pass null initially
        recyclerView.setAdapter(categoryAdapter);

        // Handle item click events
        categoryAdapter.setOnItemClickListener((position, category) -> {
            if (category.equals("All Categories")) {
                // Display all items
                ItemGridAdapter itemGridAdapter = new ItemGridAdapter(requireContext(), getAllItemsCursor());
                // Set the adapter to your RecyclerView
                 SalesFragment.mRecyclerView.setAdapter(itemGridAdapter);
            } else {
                // Update the ItemGridAdapter with the selected category
                updateItemGridAdapter(category);
                // Set the category filter in the ItemGridAdapter
                itemGridAdapter.setCategoryFilter(category);
            }

        });
    }

    private Cursor getAllItemsCursor() {
        // Implement logic to fetch all items from the database
        // For example:
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    private Cursor getItemsForCategoryCursor(String category) {
        // Implement logic to fetch items for the specified category from the database
        // For example:
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.Category + "=?";
        String[] selectionArgs = {category};
        return db.query(DatabaseHelper.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    private void updateItemGridAdapter(String selectedCategory) {
        // Assuming dbHelper is a member variable in your menuFragment
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Fetch items for the selected category
        Cursor newCursor = db.query(DatabaseHelper.TABLE_NAME,
                null,
                DatabaseHelper.Category + " = ?",
                new String[]{selectedCategory},
                null,
                null,
                null);

        // Update the ItemGridAdapter with the new cursor
        if (getActivity() != null) {
            ItemGridAdapter itemGridAdapter = new ItemGridAdapter(getActivity(), newCursor);
            SalesFragment.mRecyclerView.setAdapter(itemGridAdapter);

        }
    }

    private List<String> fetchCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper.Category}, null, null, null, null, null, null);

        List<String> categories = new ArrayList<>();
        // Add "All Categories" at the top
        categories.add("All Categories");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category));
                categories.add(category);
            }
            cursor.close();
        }

        db.close();
        return categories;
    }

}


