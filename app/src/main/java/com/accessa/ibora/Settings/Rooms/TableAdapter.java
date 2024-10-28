package com.accessa.ibora.Settings.Rooms;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Rooms.Table;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private static List<Table> mTableList;
    private static OnItemClickListener mListener;
    private DatabaseHelper mDatabaseHelper;

    public TableAdapter(List<Table> tableList, OnItemClickListener listener ) {
        this.mTableList = tableList;
        this.mListener = listener;

    }
    public interface OnItemClickListener {
        void onItemClick(Table table);
    }
    // Implement the necessary methods



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create and return a new ViewHolder instance
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_display, parent, false);

        mDatabaseHelper = new DatabaseHelper(parent.getContext());  // Initialize your DatabaseHelper

        // Set a blue background for each item
        view.setBackgroundResource(R.drawable.restab);

        // Calculate the width and height of each item to achieve a grid layout
        int screenWidth = parent.getContext().getResources().getDisplayMetrics().widthPixels;
        int itemWidth = screenWidth / 5; // Adjust this value as needed
        int itemHeight = calculateItemHeight(parent.getContext()); // Implement a method to calculate the height

        // Set the layout parameters for both width and height
        view.getLayoutParams().width = itemWidth;
        view.getLayoutParams().height = itemHeight;
        return new ViewHolder(view);

    }
    private int calculateItemHeight(Context context) {
        // Return a fixed height value in pixels

        return (int) context.getResources().getDimension(R.dimen.item_fixed_height);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        Table table = mTableList.get(position);
        holder.tableNameTextView.setText(table.getTableNumbers());


        // Set the image based on the status
        if ("reserved".equals(table.getStatus())) {
            holder.tableImageView.setImageResource(R.drawable.resertab); // Replace with your reserved image resource
        } else {
            holder.tableImageView.setImageResource(R.drawable.restab); // Replace with your not reserved image resource
        }


    }


    @Override
    public int getItemCount() {
        return mTableList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tableNameTextView;
        ImageView tableImageView; // Added ImageView
        ViewHolder(View itemView) {
            super(itemView);
            tableNameTextView = itemView.findViewById(R.id.tableNameTextView);
            tableImageView = itemView.findViewById(R.id.tableImageView); // Initialize ImageView

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(mTableList.get(position));
                        }
                    }
                }
            });
        }
    }
}

