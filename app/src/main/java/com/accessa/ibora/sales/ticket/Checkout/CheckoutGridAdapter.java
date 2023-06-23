package com.accessa.ibora.sales.ticket.Checkout;

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

public class CheckoutGridAdapter extends RecyclerView.Adapter<CheckoutGridAdapter.ItemViewHolder> {



    private Context mContext;
    private Cursor mCursor;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public CheckoutGridAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

    }

    public Payment getItem(int childAdapterPosition) {
        int realPosition = childAdapterPosition;
        if (mCursor.moveToPosition(realPosition)) {
            return getItemFromCursor(mCursor);
        }
        return null;
    }


    public interface ItemClickListener {
        void onItemClick(String item);
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;

        public TextView idTextView,Icon;



        public ItemViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            Icon= itemView.findViewById(R.id.icon);


        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.payment_options_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME));
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ID));
        String icon = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON));


        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.Icon.setText(icon);
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

    private Payment getItemFromCursor(Cursor cursor) {
        // Retrieve the item data from the cursor and create an Item object
        // Modify this code based on your cursor structure and item class
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME));
        String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ID));
        String icon = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON));


        Payment payment = new Payment(name, id, icon);
        return payment;
    }


}
