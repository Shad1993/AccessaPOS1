package com.accessa.ibora.product.items;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.product.category.Category;
import com.accessa.ibora.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnSubcategoryClickListener onSubcategoryClickListener;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);

        // Set the category name
        holder.categoryNameTextView.setText(category.getCatName());

        // Show subcategories if expanded
        if (category.isExpanded()) {
            holder.subcategoryListView.setVisibility(View.VISIBLE);
            holder.bindSubcategories(category.getSubcategories(), subcategory -> {

                // You can add more logic here, like starting a new activity or updating the UI
                if (onSubcategoryClickListener != null) {
                    onSubcategoryClickListener.onSubcategoryClick(subcategory);

                }
            });
        } else {
            holder.subcategoryListView.setVisibility(View.GONE);
        }

        // Handle regular item click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, category);
            }
        });

        // Handle long click to toggle subcategories
        holder.itemView.setOnLongClickListener(v -> {
            // Check if the category name is null before proceeding
            if (category.getCatName() == null) {
                Log.e("CategoryAdapter", "Category name is null. Cannot fetch subcategories.");
                return true; // Return true to indicate the long click was handled
            }
            if (category.isExpanded()) {
                // If the category is expanded, collapse it
                category.setExpanded(false);
                holder.subcategoryListView.setVisibility(View.GONE); // Hide the subcategories
                Log.d("CategoryAdapter", "Category collapsed: " + category.getCatName());
            } else {
                // If the category is collapsed, fetch and expand it
                DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());
                Log.d("CategoryAdapter", "Subcategories fetched for: " + category.getCatName());
                int catid = databaseHelper.getCategoryIdByName(category.getCatName());
                // Fetch subcategories from the database
                fetchSubcategoriesForCategory(holder.itemView.getContext(), catid, position);
                Log.d("CategoryAdapter", "Subcategories fetched for: " + category.getCatName());
            }
            return true; // Return true to indicate the long click was handled
        });

    }

    // Method to fetch subcategories from the database
    private void fetchSubcategoriesForCategory(Context context, int categoryId, int position) {
        // Use the passed context to create the DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        // Fetch the subcategories using the category ID
        List<String> subcategories = databaseHelper.getSubcategoriesByCategoryId(categoryId);

        // Update the category object and notify changes
        Category category = categoryList.get(position);
        category.setSubcategories(subcategories);
        category.setExpanded(true); // Set it to expanded state

        notifyItemChanged(position); // Refresh the item view
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // ViewHolder for Category items
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTextView;
        private ViewGroup subcategoryListView; // A container (like LinearLayout) for subcategories

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            subcategoryListView = itemView.findViewById(R.id.subcategoryListView); // Subcategories container
        }

        // Bind category data to the view
        public void bind(Category category) {
            categoryNameTextView.setText(category.getCatName());
        }

        // Bind subcategories to the subcategory view
        public void bindSubcategories(List<String> subcategories, OnSubcategoryClickListener listener) {
            subcategoryListView.removeAllViews(); // Clear existing subcategories

            // Assuming you have an icon resource defined, like R.drawable.ic_subcategory_icon
            int iconResourceId = R.drawable.comment_arrow; // Replace with your actual icon resource

            for (String subcategory : subcategories) {
                // Create a horizontal LinearLayout
                LinearLayout subcategoryLayout = new LinearLayout(itemView.getContext());
                subcategoryLayout.setOrientation(LinearLayout.HORIZONTAL);
                subcategoryLayout.setPadding(16, 8, 16, 8); // Add padding for styling

                // Create an ImageView for the icon
                ImageView iconImageView = new ImageView(itemView.getContext());
                iconImageView.setImageResource(iconResourceId); // Set the icon resource
                iconImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Create a TextView for the subcategory name
                TextView subcategoryTextView = new TextView(itemView.getContext());
                subcategoryTextView.setText(subcategory);
                subcategoryTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Add the icon and text view to the horizontal layout
                subcategoryLayout.addView(iconImageView);
                subcategoryLayout.addView(subcategoryTextView);

                // Add the horizontal layout to the subcategory container
                subcategoryListView.addView(subcategoryLayout);

                // Set an onClickListener for the subcategory layout
                subcategoryLayout.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onSubcategoryClick(subcategory); // Pass the subcategory to the listener
                    }
                });
            }
        }


    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(int position, Category category);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, String category);
    }
    public interface OnSubcategoryClickListener {
        void onSubcategoryClick(String subcategory);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public void setOnSubcategoryClickListener(OnSubcategoryClickListener listener) {
        this.onSubcategoryClickListener = listener;
    }



}
