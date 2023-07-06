package com.accessa.ibora.Receipt;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private DatabaseHelper mDatabaseHelper;

    public ReceiptAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView TransTextView;
        public TextView TotalTextView;
        public TextView TransdateTextView;
        public TextView statusTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            TransTextView = itemView.findViewById(R.id.name_text_view);
            TotalTextView = itemView.findViewById(R.id.total_text_view);
            TransdateTextView = itemView.findViewById(R.id.transdate_edittex);
            statusTextView=itemView.findViewById(R.id.status_text_view);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.receipt_layout, parent, false);
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

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_TICKET_NO));
        String value = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED));
        mDatabaseHelper = new DatabaseHelper(mContext);
        String status = mDatabaseHelper.getTransactionStatus(name);
        if (status.equals("Saved")) {
            holder.statusTextView.setText("Saved ");
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.BleuAccessaText)); // Set text color

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.saved); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }


            holder.statusTextView.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        } else if (status.equals("InProgress")){
            holder.statusTextView.setText("In Progress ");
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.OrangeAccessa)); // Set text color
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.inprog); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }
            holder.statusTextView.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        }
        else if (status.equals("Completed")){
            holder.statusTextView.setText("Completed");
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.green)); // Set text color
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.check); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }
            holder.statusTextView.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        }
        holder.idTextView.setText( id);

        holder.TransTextView.setText(name);
        holder.TotalTextView.setText(value);
        holder.TransdateTextView.setText(timestamp);
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
