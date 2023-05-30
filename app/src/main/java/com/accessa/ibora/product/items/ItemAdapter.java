package com.accessa.ibora.product.items;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.text.DecimalFormat;

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
        public TextView Available;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.Longdescription_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            Available = itemView.findViewById(R.id.Available_text_view);
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
        String availableForSale = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.AvailableForSale));

        if (availableForSale.equals("true")) {
            holder.Available.setText("Available ");
            holder.Available.setTextColor(mContext.getResources().getColor(R.color.BleuAccessaText)); // Set text color

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.check); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }


            holder.Available.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        } else {
            holder.Available.setText("Not Available ");

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.cancel); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }
            holder.Available.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        }

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.descriptionTextView.setText(longDescription);


        // Format the price to two decimal places
        double formattedPrice = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedPriceString = decimalFormat.format(formattedPrice);


        holder.priceTextView.setText(formattedPriceString);

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
