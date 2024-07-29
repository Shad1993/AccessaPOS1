package com.accessa.ibora.sales.ticket;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private boolean isCheckBoxVisible = false;
    public List<String> checkedItems = new ArrayList<>(); // Add this line

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
                int item_id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.ITEM_ID));
                int unique_id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper._ID));
                String unitPrice = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_UNIT_PRICE));

                Transaction item = new Transaction(unique_id,itemName, itemPrice, itemQuantity, unitPrice,item_id);
                transactionList.add(0, item); // Add the item at the beginning of the list to maintain the desired order
            } while (mCursor.moveToPrevious());
        }

        return transactionList;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView uniqueidTextView;
        public TextView nameTextView;
        public TextView PriceTextView;
        public TextView QuantityTextView;
        private TextView ItemIdTextView;

        CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ItemIdTextView = itemView.findViewById(R.id.id_text_view);
            uniqueidTextView = itemView.findViewById(R.id.uniqueid_text_view);
            nameTextView = itemView.findViewById(R.id.Longdescription_text_view);
            PriceTextView = itemView.findViewById(R.id.price_text_view);
            QuantityTextView = itemView.findViewById(R.id.quantity_text_view);
            // Initialize other views...
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setVisibility(isCheckBoxVisible ? View.VISIBLE : View.GONE);
        }

        // Helper method to bind data to the view holder
        public void bindData(String itemId, boolean isItemSelected) {
            checkBox.setChecked(isItemSelected); // Set the checked status based on IS_SELECTED field

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update IS_SELECTED field in the database when the checkbox is clicked

                }
            });
        }
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ticket_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.ItemViewHolder holder, int position) {
        // Check if the Cursor is null or if it has not been moved to the current position.
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ITEM_ID));
        String uniques = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String quantity = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.QUANTITY));
        String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
        String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));
        String comment=mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_COMMENT));
        String senttoKitchen=mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_SentToKitchen));

        // Limit the description length to a certain number of characters
        int maxDescriptionLength = 20; // Change this value to your desired length
        if (description.length() > maxDescriptionLength) {
            description = description.substring(0, maxDescriptionLength) + "...";
        }
        // Format the price to two decimal places
        double formattedPrice = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double totalPrice = formattedPrice;
        String totalPriceString = decimalFormat.format(totalPrice);

        holder.ItemIdTextView.setText(id);
        holder.uniqueidTextView.setText(uniques);
        holder.nameTextView.setText(description);
        holder.QuantityTextView.setText("x  " + quantity); // Add a multiplication sign after the quantity value
        holder.PriceTextView.setText("Rs " + totalPriceString);

        holder.checkBox.setVisibility(isCheckBoxVisible ? View.VISIBLE : View.GONE);

        // Bind data to the view holder
        holder.checkBox.setChecked(checkedItems.contains(id));

        // Set an OnClickListener for the checkbox
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update IS_SELECTED field in the database when the checkbox is clicked
                updateItemSelectedInDatabase(id, holder.checkBox.isChecked());

                // Update the list of checked items
                if (holder.checkBox.isChecked()) {
                    checkedItems.add(id);
                } else {
                    checkedItems.remove(id);
                }
            }
        });

        // Change the background color if the comment is not null
        if (senttoKitchen != null && !senttoKitchen.isEmpty() && senttoKitchen.equals("1")) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow)); // Replace with your desired color
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.textviewextColor)); // Default color
        }
    }


    private void updateItemSelectedInDatabase(String itemId, boolean isSelected) {
        // Get the instance of your DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);

        // Update IS_SELECTED field
        dbHelper.updateItemSelected(Long.parseLong(itemId), isSelected);

        // Close the database connection
        dbHelper.close();
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

    public void setCheckBoxVisibility(boolean isVisible) {
        isCheckBoxVisible = isVisible;
        notifyDataSetChanged();
    }
    public Cursor getCursor() {
        return mCursor;
    }
    public class TicketViewHolder {

    }
}