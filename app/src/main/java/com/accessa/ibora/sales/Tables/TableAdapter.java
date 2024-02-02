package com.accessa.ibora.sales.Tables;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private Cursor originalCursor; // Store the original cursor for filtering

    private DatabaseHelper mDatabaseHelper;
    private String selectedTableId = ""; // New field to store the selected table ID

    public TableAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        originalCursor = cursor; // Initialize the original cursor

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView;
        public TextView Table;
        public TextView brn;
        public TextView room;
        public CardView button; // Add this line to include the CardView reference

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.Buyerid);
            nameTextView = itemView.findViewById(R.id.BuyerAdapter);
            Table = itemView.findViewById(R.id.textViewTAN);
            brn = itemView.findViewById(R.id.textViewBRN);
            room = itemView.findViewById(R.id.roomid);
            button = itemView.findViewById(R.id.button); // Add this line to initialize the CardView reference

        }
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tablelayout, parent, false);
        mDatabaseHelper = new DatabaseHelper(mContext);
        // Calculate the width of each item to achieve a grid layout with 5 items on the screen
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int itemWidth = screenWidth / 8;
        view.getLayoutParams().width = itemWidth;

        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_NUMBER));
        String tablenum = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_NUMBER));
       String roomid = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ROOM_ID));

        holder.idTextView.setText(id);
       // holder.nameTextView.setText("Table " + name);
       // holder.Table.setText(tablenum);
        holder.room.setText(roomid);

        // Check if the table is merged
        int mergedStatus = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.MERGED));
        String mergedSetId = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.MERGED_SET_ID));
        // Exclude items where merged is 1 and mergedSetId is "0"
        if (mergedStatus == 1 && "0".equals(mergedSetId)) {
            // Exclude this item
            holder.itemView.getLayoutParams().height = 0;
            return;
        } else {
            // Reset the height in case it was previously set to 0
            holder.itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        // Continue with the display logic
        if (mergedStatus == 1) {
            // The table is merged, concatenate table numbers
            holder.nameTextView.setText(mergedSetId);
            holder.Table.setText(mergedSetId);
        } else {
            // The table is not merged or has a MERGED_SET_ID of "0", display the individual table number
            holder.nameTextView.setText("T " + name);
            holder.Table.setText(tablenum);
        }

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(false);
        }
        List<String[]> prfRoomAndTableIds = mDatabaseHelper.getRoomAndTableIdsWithPRFStatus();
        String currentRoomId = holder.room.getText().toString();
        String currentTableId = holder.idTextView.getText().toString();
        List<String[]> inProgressRoomAndTableIds = mDatabaseHelper.getRoomAndTableIdsWithInProgressStatus();
        boolean isCurrentTableInProgress = false;
        boolean isCurrentTablePRF = false;
        for (String[] roomAndTableIds : inProgressRoomAndTableIds) {
            String inProgressRoomId = roomAndTableIds[0];
            String inProgressTableId = roomAndTableIds[1];

            if (currentRoomId.equals(inProgressRoomId) && currentTableId.equals(inProgressTableId)) {
                // The current table is in progress
                isCurrentTableInProgress = true;
                break;
            }
        }
        for (String[] roomAndTableIds : prfRoomAndTableIds) {
            String prfRoomId = roomAndTableIds[0];
            String prfTableId = roomAndTableIds[1];

            if (currentRoomId.equals(prfRoomId) && currentTableId.equals(prfTableId)) {
                // The current table has a status of "PRF"
                isCurrentTablePRF = true;
                break;
            }
        }

// Check if this item's table ID matches the selected table ID
        // Check if this item's table ID matches the selected table ID
        if (id.equals(selectedTableId)) {
            // This item is selected, set a different button color
            holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        } else {
            if (isCurrentTablePRF) {
                // The current table has a status of "PRF"
                holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            } else if (isCurrentTableInProgress) {
                // The current table is in progress
                holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
            } else {
                // The current table does not have a status of "PRF" or "InProgress"
                holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
            }
        }

        holder.itemView.setTag(id);

    }


    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    // New method to update the selected table ID
    public void setSelectedTableId(String newTableId) {
        selectedTableId = newTableId;
    }
}
