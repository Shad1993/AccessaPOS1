package com.accessa.ibora.Settings.Rooms;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Rooms.Table;
import com.accessa.ibora.product.items.DatabaseHelper;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ItemViewHolder> {
    public interface OnItemLongClickListener {
        void onItemLongClick(Table table);
    }
    private Context mContext;
    private Cursor mCursor;

    public RoomsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView;
        public TextView Tan;
        public TextView brn;
        public TextView addresse;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.Buyerid);
            nameTextView = itemView.findViewById(R.id.BuyerAdapter);
            Tan = itemView.findViewById(R.id.textViewTAN);
            brn = itemView.findViewById(R.id.textViewBRN);
            addresse = itemView.findViewById(R.id.textViewAdress);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.room, parent, false);

        // Set a blue background for each item
        view.setBackgroundResource(R.drawable.restaurant);

        // Calculate the width and height of each item to achieve a grid layout
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int itemWidth = screenWidth / 5; // Adjust this value as needed
        int itemHeight = calculateItemHeight(); // Implement a method to calculate the height

        // Set the layout parameters for both width and height
        view.getLayoutParams().width = itemWidth;
        view.getLayoutParams().height = itemHeight;

        return new ItemViewHolder(view);
    }

    // Method to calculate the height of each item, adjust as needed
    private int calculateItemHeight() {
        // Return a fixed height value in pixels
        return (int) mContext.getResources().getDimension(R.dimen.item_fixed_height);
    }



    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ROOM_NAME));
        String longDescription = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_COUNT));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.Tan.setText(longDescription);

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
}
