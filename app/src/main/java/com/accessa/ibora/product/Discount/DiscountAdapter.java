package com.accessa.ibora.product.Discount;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public DiscountAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView nameTextView;
        public TextView valueTextView;
        public TextView timestampTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            valueTextView = itemView.findViewById(R.id.deptcode_text_view);
            timestampTextView = itemView.findViewById(R.id.LastModified_edittex);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.discount_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Check if the Cursor is null or if it has not been moved to the first row.
        if (mCursor == null || !mCursor.moveToFirst()) {
            return;
        }
        // Move the cursor to the specified position.
        mCursor.moveToPosition(position);

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DISCOUNT_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DISCOUNT_NAME));
        String value = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DISCOUNT_VALUE));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DISCOUNT_TIMESTAMP));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.valueTextView.setText(value + "%"); // Append '%' symbol
        holder.timestampTextView.setText(timestamp);
        holder.itemView.setTag(id);
    }

    @Override
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
