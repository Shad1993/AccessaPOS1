package com.accessa.ibora.product.items;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.sales.Sales.ItemGridAdapter;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.product.category.Category;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class menuFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Category> categories;
    private DatabaseHelper dbHelper;

    private ItemGridAdapter itemGridAdapter; // Add this variable
    private OnCategoryClickListener categoryClickListener;

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


        // Pass List<Category> to CategoryAdapter
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),categories);
        recyclerView.setAdapter(categoryAdapter);
        Cursor cursor = dbHelper.getAllItems();
        // Initialize the ItemGridAdapter
        itemGridAdapter = new ItemGridAdapter(requireContext(), cursor); // Pass null initially

        // Handle item click events

        categoryAdapter.setOnItemClickListener((position, category) -> {
            if (category.getCatName().equals("All Categories")) {
                // Display all items
                handleCategoryClick(null);
            } else {
                // Update the ItemGridAdapter with the selected category

                //ItemGridAdapter itemGridAdapter = new ItemGridAdapter(requireContext(), getAllItemsCategoryCursor(category.getCatName()));

                //SalesFragment.mRecyclerView.setAdapter(itemGridAdapter);
                // Set the category filter in the ItemGridAdapter
                //itemGridAdapter.setCategoryFilter(category.getCatName());

                handleCategoryClick(category.getCatName());
            }

        });

        categoryAdapter.setOnSubcategoryClickListener(subcategory -> {
            // Call updateItemGridAdapter with the selected subcategory
            Log.d("menu Clicked", "Clicked on: " + subcategory);
            updateItemGridAdapterWithSubcategory(subcategory);
        });
        // Handle long click events
        categoryAdapter.setOnItemLongClickListener((position, category) -> {
            // Handle long click here, for example, show a pop-up to delete the category or some other action
            Toast.makeText(requireContext(), "Long clicked on: " , Toast.LENGTH_SHORT).show();
            return true; // Return true to indicate the event has been handled
        });
    }
    private void updateRecyclerViewWithItems(Cursor cursor, GridLayoutManager gridLayoutManager) {
        if (cursor != null) {
            itemGridAdapter = new ItemGridAdapter(requireContext(), cursor);
            SalesFragment.mRecyclerView.setAdapter(itemGridAdapter);

        }
    }

    private Cursor getAllItemsCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    private Cursor getAllItemsCategoryCursor(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "(" + DatabaseHelper.Category + " = ? OR " + DatabaseHelper.Category + " IS NULL OR " + DatabaseHelper.Category + " = '')";
        String[] selectionArgs = {category};

        return db.query(DatabaseHelper.TABLE_NAME,
                null, // Select all columns
                selection,
                selectionArgs,
                null,
                null,
                DatabaseHelper.Category); // Order by Category
    }



    private void updateItemGridAdapterWithSubcategory(String selectedSubcategory) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor newCursor = db.query(DatabaseHelper.TABLE_NAME,
                null,
                DatabaseHelper.SubCategory + " = ?",
                new String[]{selectedSubcategory},
                null,
                null,
                null);

        if (getActivity() != null) {
            ItemGridAdapter itemGridAdapter = new ItemGridAdapter(getActivity(), newCursor);
            SalesFragment.mRecyclerView.setAdapter(itemGridAdapter);
        }
    }
    private void updateItemGridAdapter(String selectedCategory) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor newCursor = db.query(DatabaseHelper.TABLE_NAME,
                null,
                DatabaseHelper.Category + " = ?",  // Use correct column name
               null,
                null,
                null,
                null); // Sort by subcategory

        if (newCursor != null) {
            Log.d("updateItemGridAdapter", "Filtered items count: " + newCursor.getCount());
            if (getActivity() != null) {
                ItemGridAdapter itemGridAdapter = new ItemGridAdapter(getActivity(), newCursor);
                SalesFragment.mRecyclerView.setAdapter(itemGridAdapter);
            }
        } else {
            Log.e("updateItemGridAdapter", "No items found for category: " + selectedCategory);
        }
    }


    // Update this method to return a List<Category>
    private List<Category> fetchCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Ensure column name is correct
        String query = "SELECT DISTINCT " + DatabaseHelper.Category + " FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        Set<String> uniqueCategories = new HashSet<>();

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.Category);
            if (columnIndex == -1) {
                Log.e("fetchCategories", "Column " + DatabaseHelper.Category + " not found in table " + DatabaseHelper.TABLE_NAME);
            } else {
                while (cursor.moveToNext()) {
                    String category = cursor.getString(columnIndex);

                    if (category != null) {
                        uniqueCategories.add(category); // Add only non-null categories
                    } else {
                        Log.w("fetchCategories", "Null category found. Skipping.");
                    }
                }
            }
            cursor.close();
        } else {
            Log.e("fetchCategories", "Cursor is null. Failed to retrieve categories.");
        }

        db.close();

        List<Category> categoryList = new ArrayList<>();

        // Comment out or remove the "All Categories" entry
         categoryList.add(new Category(0, "All Categories", "#000000"));

        // Convert set to list of Category objects
        for (String categoryName : uniqueCategories) {
            categoryList.add(new Category(1, categoryName, "#FF0000")); // Example: Assign a default color
        }

        return categoryList;
    }

    public interface OnCategoryClickListener {
        void onCategorySelected(String categoryId);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoryClickListener) {
            categoryClickListener = (OnCategoryClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCategoryClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        categoryClickListener = null;
    }

    private void handleCategoryClick(String categoryId) {
        if (categoryClickListener != null) {
            categoryClickListener.onCategorySelected(categoryId);
        }
    }

}

