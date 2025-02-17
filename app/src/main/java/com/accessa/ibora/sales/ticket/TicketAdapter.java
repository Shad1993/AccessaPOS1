package com.accessa.ibora.sales.ticket;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
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
    private DatabaseHelper mDatabaseHelper;
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
                String relatedoptionid = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RELATED_Option_ID));
                int itemQuantity = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.QUANTITY));
                int item_id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.ITEM_ID));
                int unique_id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper._ID));
                String unitPrice = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_UNIT_PRICE));
                double totaldisc = mCursor.getDouble(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_DISCOUNT));

                Transaction item = new Transaction(unique_id,itemName, itemPrice, itemQuantity, unitPrice,item_id,relatedoptionid,totaldisc);
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
        private TextView ItemIdTextView,CommentTextView;
        public ImageView relatedOptionIcon, commenticon,discountcon; // Add this line

        CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ItemIdTextView = itemView.findViewById(R.id.id_text_view);
            uniqueidTextView = itemView.findViewById(R.id.uniqueid_text_view);
            nameTextView = itemView.findViewById(R.id.Longdescription_text_view);
            PriceTextView = itemView.findViewById(R.id.price_text_view);
            QuantityTextView = itemView.findViewById(R.id.quantity_text_view);
            relatedOptionIcon = itemView.findViewById(R.id.related_option_icon); // Initialize the ImageView
            commenticon=itemView.findViewById(R.id.comment_icon);
            CommentTextView=itemView.findViewById(R.id.comment_text_view);
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
        mDatabaseHelper = new DatabaseHelper(mContext);
        // Check if the Cursor is null or if it has not been moved to the current position.
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.ITEM_ID));
        String uniques = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String relatedoptionid = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RELATED_Option_ID));
        String quantity = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.QUANTITY));
        String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
        String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE));
        String comment=mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_COMMENT));
        String senttoKitchen=mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_SentToKitchen));
        double totaldisc = mCursor.getDouble(mCursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_DISCOUNT));
        String famille= mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(uniques));
         String CatName=mDatabaseHelper.getCatNameById(famille);
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
        holder.checkBox.setChecked(checkedItems.contains(uniques));

        // Set an OnClickListener for the checkbox
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update IS_SELECTED field in the database when the checkbox is clicked
                updateItemSelectedInDatabase(uniques, holder.checkBox.isChecked());

                // Update the list of checked items
                if (holder.checkBox.isChecked()) {
                    checkedItems.add(uniques);
                } else {
                    checkedItems.remove(uniques);
                }
            }
        });

        // Change the background color if the comment is not null
        if (senttoKitchen != null && !senttoKitchen.isEmpty() && senttoKitchen.equals("1")) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow)); // Replace with your desired color
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.textviewextColor)); // Default color
        }
        if ("OPEN FOOD".equals(CatName) && comment != null) {
            holder.commenticon.setVisibility(View.VISIBLE);
            holder.CommentTextView.setVisibility(View.VISIBLE); // Show comment
            holder.CommentTextView.setText(comment); // Set the comment text
        }else{
            holder.commenticon.setVisibility(View.GONE);
            holder.CommentTextView.setVisibility(View.GONE); // Show comment
        }
        // Update item view based on relatedOptionId and description
        if (description.startsWith("Sup")) {
            holder.relatedOptionIcon.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.leftMargin = 50; // Adjust the left margin as needed
            holder.itemView.setLayoutParams(params);
        } else {
            holder.relatedOptionIcon.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.leftMargin = 0; // Reset the left margin if there's no relatedOptionId or description starts with "SUP"
            holder.itemView.setLayoutParams(params);
        }

    /*    if (totaldisc >0 ) {
            holder.relatedOptionIcon.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.leftMargin = 50; // Adjust the left margin as needed
            holder.itemView.setLayoutParams(params);
        } else {
            holder.relatedOptionIcon.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.leftMargin = 0; // Reset the left margin if there's no relatedOptionId or description starts with "SUP"
            holder.itemView.setLayoutParams(params);
        }

     */
    }
    public void uncheckAllCheckboxes() {
        // Clear the list of checked items
        checkedItems.clear();



        // Notify the adapter to refresh the view
        notifyDataSetChanged();
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