package com.accessa.ibora.Settings.MRASettings;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class MRAAdaptor extends RecyclerView.Adapter<MRAAdaptor.ViewHolder> {

    private Cursor mCursor;

    public MRAAdaptor(Cursor cursor) {
        this.mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mra_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (mCursor == null || !mCursor.moveToFirst()) {
            return;
        }
        // Move the cursor to the specified position.
        mCursor.moveToPosition(position);

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_TICKET_NO));
        String qr = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_MRA_QR));
        String value = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_DATE_CREATED));

        if (qr == null || qr.contains("Request Failed")) {
            // Hide the elements if qr is null or contains "Request Failed"
            holder.qrtextView.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.qrtextView.setText("Failed");
        } else {
            // Show the elements for other cases
            holder.qrtextView.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
            holder.qrtextView.setText("Passed");
        }

        // Set the item data
        holder.idTextView.setText(id);
        holder.textView.setText(name);
    }
    public void updateData(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close(); // Close the existing cursor if it's not null
        }

        mCursor = newCursor; // Assign the new cursor to your dataset

        // Notify the adapter that the data has changed.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,idTextView,qrtextView;
        public CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            textView = itemView.findViewById(R.id.name_text_view); // Replace with your TextView ID
            qrtextView = itemView.findViewById(R.id.Available_text_view); // Replace with your TextView ID
            checkbox = itemView.findViewById(R.id.myCheckBox);
        }
    }
}
