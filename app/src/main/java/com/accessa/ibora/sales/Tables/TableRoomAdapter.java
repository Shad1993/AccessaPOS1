package com.accessa.ibora.sales.Tables;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.List;

public class TableRoomAdapter extends RecyclerView.Adapter<TableRoomAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private Cursor originalCursor; // Store the original cursor for filtering
String roomid;
String tableid;
    private DatabaseHelper mDatabaseHelper;
    private String selectedTableId = ""; // New field to store the selected table ID
    private String selectedTableName = ""; // New field to store the selected table name

    public TableRoomAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        originalCursor = cursor; // Initialize the original cursor

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView;
        public TextView seattxtview;
        public TextView captextview;
        public TextView Table;
        public TextView brn;
        public TextView room;
        public CardView button; // Add this line to include the CardView reference

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.Buyerid);
            seattxtview = itemView.findViewById(R.id.seat);
            captextview = itemView.findViewById(R.id.capacity);

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
        View view = inflater.inflate(R.layout.tablepopuplayout, parent, false);
        mDatabaseHelper = new DatabaseHelper(mContext);
        // Calculate the width of each item to achieve a grid layout with 5 items on the screen
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int itemWidth = screenWidth / 8;
        view.getLayoutParams().width = itemWidth;
        SharedPreferences preferences = mContext.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getString("table_id", "0");
        tableid = preferences.getString("table_id", "0");
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ID));
        String status = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.STATUS));
        String seat = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.SEAT_COUNT));
        String tablenum = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_NUMBER));
        String covernumber = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.CoverCount));
       String roomid = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ROOM_ID));
        String mergedtableId = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.MERGED_SET_ID));
        if(covernumber== null){

            covernumber="0";
        }
        holder.idTextView.setText(id);

       // holder.nameTextView.setText("Table " + name);
        holder.Table.setText(tablenum);
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
            tablenum=mergedSetId;
        } else {
            // The table is not merged or has a MERGED_SET_ID of "0", display the individual table number
            holder.nameTextView.setText("T " + tablenum);
            holder.Table.setText(tablenum);
        }

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(false);
        }
        List<String[]> prfRoomAndTableIds = mDatabaseHelper.getRoomAndTableIdsWithPRFStatus();


        String currentRoomId = holder.room.getText().toString();
        String currentTableId = holder.idTextView.getText().toString();
        String currentTableId1 = holder.Table.getText().toString();
        List<String[]> inProgressRoomAndTableIds = mDatabaseHelper.getRoomAndTableIdsWithInProgressStatus();
        boolean isCurrentTableInProgress = false;
        boolean isCurrentTablePRF = false;
        boolean isselectedaftermerged = false;
        for (String[] roomAndTableIds : inProgressRoomAndTableIds) {
            String inProgressRoomId = roomAndTableIds[0];
            String inProgressTableId = roomAndTableIds[1];
            SharedPreferences preferences = mContext.getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
            int roomid1 = preferences.getInt("roomnum", 0);
            String tableid1 = preferences.getString("table_id", "0");

            if (currentRoomId.equals(inProgressRoomId) && tableid1.equals(currentTableId1)) {
                // The current table is in progress
                isselectedaftermerged = true;

                break;
            }

            if (currentRoomId.equals(inProgressRoomId) && currentTableId1.equals(inProgressTableId)) {
                // The current table is in progress
                isCurrentTableInProgress = true;

                break;
            }
        }
        for (String[] roomAndTableIds : prfRoomAndTableIds) {
            String prfRoomId = roomAndTableIds[0];
            String prfTableId = roomAndTableIds[1];

            if (currentRoomId.equals(prfRoomId) && currentTableId1.equals(prfTableId)) {
                // The current table has a status of "PRF"
                isCurrentTablePRF = true;
                break;
            }
        }



// Check if this item's table ID matches the selected table ID
        // Check if this item's table ID matches the selected table ID

        if (status.equals("reserved")) {
            // This item is selected, set a different button color
            holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.lightSkyBlue));
            holder.nameTextView.setText("Reserved/T " + tablenum);
            holder.seattxtview.setText("S " + covernumber + "/" + seat);
        }
        else if (id.equals(selectedTableId) || isselectedaftermerged ) {
            // This item is selected, set a different button color
            if (mergedStatus == 1) {
                // The current table does not have a status of "PRF" or "InProgress"
                holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
                holder.nameTextView.setText(tablenum + "\nS " + covernumber + "\n" + seat);
                holder.seattxtview.setText("S " + covernumber + "/" + seat);
            }else{
                // The current table does not have a status of "PRF" or "InProgress"
                holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
                holder.nameTextView.setText("T " +tablenum + "\nS " + covernumber + "\n" + seat);
                holder.seattxtview.setText("S " + covernumber + "/" + seat);

            }
        } else {
            if (isCurrentTablePRF) {
                if (mergedStatus == 1) {
                    // The current table does not have a status of "PRF" or "InProgress"
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                    holder.nameTextView.setText(tablenum + "\nS " + covernumber + "\n" + seat);
                    holder.seattxtview.setText("S " + covernumber + "/" + seat);
                }else{
                    // The current table does not have a status of "PRF" or "InProgress"
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                    holder.nameTextView.setText("T " +tablenum + "\nS " + covernumber + "\n" + seat);
                    holder.seattxtview.setText("S " + covernumber + "/" + seat);

                }
            } else if (isCurrentTableInProgress) {
                if (mergedStatus == 1) {
                    // The current table does not have a status of "PRF" or "InProgress"
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
                    holder.nameTextView.setText(tablenum + "\nS " + covernumber + "\n" + seat);
                    holder.seattxtview.setText("S " + covernumber + "/" + seat);
                }else{
                    // The current table does not have a status of "PRF" or "InProgress"
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
                    holder.nameTextView.setText("T " +tablenum + "\nS " + covernumber + "\n" + seat);
                    holder.seattxtview.setText("S " + covernumber + "/" + seat);

                }
            } else {
                if (mergedStatus == 1) {
                    // The current table does not have a status of "PRF" or "InProgress"
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                    holder.nameTextView.setText(tablenum + "\nS " + covernumber + "\n" + seat);
                    holder.seattxtview.setText("S " + covernumber + "/" + seat);
                }else{

                    // The current table does not have a status of "PRF" or "InProgress"
                    holder.button.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                    holder.nameTextView.setText("T " +tablenum + "\nS " + covernumber + "\n" + seat);
                    holder.seattxtview.setText("S " + covernumber + "/" + seat);


                }

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
    public void setSelectedTableId(String newTableId,String tablename) {
        selectedTableId = newTableId;
        selectedTableName = tablename;
    }
}
