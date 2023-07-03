package com.accessa.ibora.sales.ticket;

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
import com.accessa.ibora.product.items.ItemAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private List<String> data;

    public TicketAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public void setData(List<String> newData) {
        data = newData;
    }
    public void updateData(List<String> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }
    public List<Transaction> getData() {
        List<Transaction> transactionList = new ArrayList<>();

        // Iterate through the cursor and add data to the list
        if (mCursor != null && mCursor.moveToLast()) {
            do {
                String itemName = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
                double itemPrice = mCursor.getDouble(mCursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));
                int itemQuantity = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.QUANTITY));
                double unitPrice = mCursor.getDouble(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_UNIT_PRICE));

                Transaction item = new Transaction(itemName, itemPrice, itemQuantity, unitPrice);
                transactionList.add(0, item); // Add the item at the beginning of the list to maintain the desired order
            } while (mCursor.moveToPrevious());
        }

        return transactionList;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView nameTextView;
        public TextView PriceTextView;
        public TextView QuantityTextView;
        private TextView ItemIdTextView;



        public ItemViewHolder(View itemView) {
            super(itemView);
            ItemIdTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.Longdescription_text_view);
            PriceTextView = itemView.findViewById(R.id.price_text_view);
            QuantityTextView = itemView.findViewById(R.id.quantity_text_view);


        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ticket_item, parent, false);
        return new ItemViewHolder(view);
    }

    public void onBindViewHolder(@NonNull TicketAdapter.ItemViewHolder holder, int position) {
        // Check if the Cursor is null or if it has not been moved to the current position.
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ITEM_ID));
        String quantity = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.QUANTITY));
        String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
        String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));

// Limit the description length to a certain number of characters
        int maxDescriptionLength = 20; // Change this value to your desired length
        if (description.length() > maxDescriptionLength) {
            description = description.substring(0, maxDescriptionLength) + "...";
        }
        // Format the price to two decimal places
        double formattedPrice = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double totalPrice = formattedPrice ;
        String totalPriceString = decimalFormat.format(totalPrice);


        holder.ItemIdTextView.setText(id);
        holder.nameTextView.setText(description);
        holder.QuantityTextView.setText("x  " + quantity ); // Add a multiplication sign after the quantity value
        holder.PriceTextView.setText("Rs " + totalPriceString);

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
    public Cursor getCursor() {
        return mCursor;
    }
    public class TicketViewHolder {

    }
}