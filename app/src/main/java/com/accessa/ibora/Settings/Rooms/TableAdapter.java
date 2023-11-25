package com.accessa.ibora.Settings.Rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Rooms.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private static List<Table> mTableList;
    private static OnItemClickListener mListener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        Table table = mTableList.get(position);
        holder.tableNameTextView.setText(table.getTableNumbers());



    }


    @Override
    public int getItemCount() {
        return mTableList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tableNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            tableNameTextView = itemView.findViewById(R.id.tableNameTextView);

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

