package com.accessa.ibora.product.items;

import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper.Name;
import static com.accessa.ibora.product.items.DatabaseHelper.Price;
import static com.accessa.ibora.product.items.DatabaseHelper._ID;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public ItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public ItemAdapter(List<Item> items) {
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView IdTextView;
        public TextView priceTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            IdTextView = itemView.findViewById(R.id.id_text_view);
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
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
        String id1 = mCursor.getString(mCursor.getColumnIndex(_ID));
        String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price));
        String Longdescription = mCursor.getString(mCursor.getColumnIndex(LongDescription));
        long id = mCursor.getLong(mCursor.getColumnIndex(_ID));



        holder.IdTextView.setText(id1);
        holder.nameTextView.setText(name);
        holder.descriptionTextView.setText(Longdescription);
        holder.priceTextView.setText(price);
        holder.itemView.setTag(id);
    }




    @Override
    public int getItemCount() {
        return mCursor.getCount();
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

