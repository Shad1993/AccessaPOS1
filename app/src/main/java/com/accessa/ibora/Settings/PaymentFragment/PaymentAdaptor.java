package com.accessa.ibora.Settings.PaymentFragment;

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


public class PaymentAdaptor extends RecyclerView.Adapter<PaymentAdaptor.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public PaymentAdaptor(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView,iconTextView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            iconTextView= itemView.findViewById(R.id.icon_text_view);

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.payment_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToFirst()) {
            return;
        }
        // Move the cursor to the specified position.
        mCursor.moveToPosition(position);

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_NAME));
        String icon = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.iconTextView.setText(icon);
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
