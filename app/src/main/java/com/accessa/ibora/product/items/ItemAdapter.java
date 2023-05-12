package com.accessa.ibora.product.items;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public ItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView idTextView;
        public TextView priceTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.Longdescription_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.itemlayout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
        String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price));
        String longDescription = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.descriptionTextView.setText(longDescription);
        holder.priceTextView.setText(price);
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
