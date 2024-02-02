package com.accessa.ibora.sales.RoomsAndTable;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private boolean isSearching = false;
    public RoomAdapter(Context context, Cursor cursor) {
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
        View view = inflater.inflate(R.layout.roomlayout, parent, false);

        // Set a blue background for each item
       // view.setBackgroundResource(R.drawable.restaurant);
        ImageView searchIcon = view.findViewById(R.id.searchIcon);
        EditText searchEditText = view.findViewById(R.id.searchEditText);

        searchIcon.setOnClickListener(v -> {
            isSearching = !isSearching;
            if (isSearching) {
                searchIcon.setVisibility(View.GONE);
                searchEditText.setVisibility(View.VISIBLE);
            } else {
                searchIcon.setVisibility(View.VISIBLE);
                searchEditText.setVisibility(View.GONE);
                // Clear search EditText if needed: searchEditText.setText("");
            }
        });
        // Calculate the width of each item to achieve a grid layout with 5 items on the screen
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int itemWidth = screenWidth / 1;
        view.getLayoutParams().width = itemWidth;

        return new ItemViewHolder(view);
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
        //holder.Tan.setText(longDescription);


        if (isSearching) {
            holder.nameTextView.setVisibility(View.GONE);
            holder.idTextView.setVisibility(View.GONE);

        } else {
            holder.nameTextView.setVisibility(View.VISIBLE);
            holder.idTextView.setVisibility(View.VISIBLE);


            // Set data to views for non-search mode
            holder.idTextView.setText(id);
            holder.nameTextView.setText(name);
            //holder.Tan.setText(longDescription);
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
}
