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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<String> datesList; // List to store unique dates
    private String previousDate = "";

    public ReceiptAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mDatabaseHelper = new DatabaseHelper(mContext);
        datesList = new ArrayList<>();
        getUniqueDates(); // Populate the list with unique dates
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView TransTextView;
        public TextView TotalTextView;
        public TextView TransdateTextView;
        public TextView statusTextView;
        public TextView dateTitleTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            TransTextView = itemView.findViewById(R.id.name_text_view);
            TotalTextView = itemView.findViewById(R.id.total_text_view);
            TransdateTextView = itemView.findViewById(R.id.transdate_edittex);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            dateTitleTextView = itemView.findViewById(R.id.date_separator_text_view);
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
        String status = mDatabaseHelper.getTransactionStatus(name);

        // Set the item data
        holder.idTextView.setText(id);
        holder.TransTextView.setText(name);
        holder.TotalTextView.setText("Rs " + value);
        holder.TransdateTextView.setText(timestamp);
        holder.itemView.setTag(id);

        // Get the date from the timestamp
        String date = getDateFromTimestamp(timestamp);

        // Check if the current date is different from the previous date
        if (!date.equals(previousDate)) {
            // Show the date separator
            holder.dateTitleTextView.setVisibility(View.VISIBLE);
            holder.dateTitleTextView.setText(date);
        } else {
            // Hide the date separator
            holder.dateTitleTextView.setVisibility(View.GONE);

        }



        // Check if the current date is different from the previous date
        if (position > 0) {
            mCursor.moveToPosition(position - 1);
            String previousTimestamp = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED));
            mCursor.moveToPosition(position);

            String previousDate = getDateFromTimestamp(previousTimestamp);

            if (!date.equals(previousDate)) {
                // Show the date separator
                holder.dateTitleTextView.setVisibility(View.VISIBLE);
                holder.dateTitleTextView.setText(date);
            } else {
                // Hide the date separator
                holder.dateTitleTextView.setVisibility(View.GONE);
            }
        } else {
            // First item, show the date separator
            holder.dateTitleTextView.setVisibility(View.VISIBLE);
            holder.dateTitleTextView.setText(date);
        }


        // Update the previous date
        previousDate = date;
        // Set the status text and color
        if (status.equals("Saved")) {
            holder.statusTextView.setText("Saved");
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.BleuAccessaText));
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.saved);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
            }
            holder.statusTextView.setCompoundDrawables(null, null, drawable, null);
        } else if (status.equals("InProgress")) {
            holder.statusTextView.setText("In Progress");
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.OrangeAccessa));
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.inprog);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
            }
            holder.statusTextView.setCompoundDrawables(null, null, drawable, null);
        } else if (status.equals("Completed")) {
            holder.statusTextView.setText("Completed");
            holder.statusTextView.setTextColor(mContext.getResources().getColor(R.color.green));
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.check);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
            }
            holder.statusTextView.setCompoundDrawables(null, null, drawable, null);
        }

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
            getUniqueDates(); // Update the unique dates list
            notifyDataSetChanged();
        }
    }

    private void getUniqueDates() {
        datesList.clear();

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                String timestamp = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED));
                String date = getDateFromTimestamp(timestamp);
                if (!datesList.contains(date)) {
                    datesList.add(date);
                }
            } while (mCursor.moveToNext());
        }
    }

    private String getDateFromTimestamp(String timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(timestamp);
            if (date != null) {
                return dateFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDateFromTimestamp(String timestamp, int position) {
        if (position >= 0 && position < datesList.size()) {
            return datesList.get(position);
        }
        return getDateFromTimestamp(timestamp);
    }
}
