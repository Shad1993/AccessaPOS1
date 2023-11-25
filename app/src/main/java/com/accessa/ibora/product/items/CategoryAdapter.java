package com.accessa.ibora.product.items;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<String> categories;
    private OnItemClickListener listener;
    private int selectedCategoryIndex = 0; // Default: "All Categories"


    public CategoryAdapter(List<String> categories) {
        this.categories = categories;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.bind(category);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, category);

                // Update the selected category index
                selectedCategoryIndex = position;
                notifyDataSetChanged(); // Notify the adapter that the data set has changed
            }
        });

        // Change background color based on the selected category
        if (position == selectedCategoryIndex) {

            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.textviewextColor));

        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT); // Set the default background color
        }
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }

        public void bind(String category) {
            categoryTextView.setText(category);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}



