package com.accessa.ibora.Settings.PaymentFragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        public TextView idTextView,iconTextView,Opens;
        private ImageView imageView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            iconTextView= itemView.findViewById(R.id.icon_text_view);
            Opens = itemView.findViewById(R.id.Available_text_view);
            imageView=itemView.findViewById(R.id.image);

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
        String Draweropens = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.OpenDrawer));
        if (Draweropens.equals("true")) {
            holder.Opens.setText( mContext.getResources().getString(R.string.DrawerOpens));
            holder.Opens.setTextColor(mContext.getResources().getColor(R.color.BleuAccessaText)); // Set text color

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.check); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }


            holder.Opens.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        } else {
            holder.Opens.setText( mContext.getResources().getString(R.string.DrawerNotOpens));

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.cancel); // Replace with your drawable resource
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size by dividing the width and height by the desired scale factor
            }
            holder.Opens.setCompoundDrawables(null, null, drawable, null); // Set the scaled drawable
        }
        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.iconTextView.setText(icon);
        // Set the image resource based on the ID and name
        if (id.equals("1") && name.equals("POP")) {
            holder.imageView.setImageResource(R.drawable.poplogo);
        } else {
            holder.imageView.setImageResource(R.drawable.payment);
        }
        if (id.equals("2") && name.equals("Cheque")) {
            holder.imageView.setImageResource(R.drawable.cheque);
        } else {
            holder.imageView.setImageResource(R.drawable.payment);
        }
        if (id.equals("3") && name.equals("Cash")) {
            holder.imageView.setImageResource(R.drawable.baseline_monetization_on_24);
        } else {
            holder.imageView.setImageResource(R.drawable.payment);
        }
        if (id.equals("4") && name.equals("Credit Card")) {
            holder.imageView.setImageResource(R.drawable.credit);
        } else {
            holder.imageView.setImageResource(R.drawable.payment);
        }
        if (id.equals("5") && name.equals("Debit Card")) {
            holder.imageView.setImageResource(R.drawable.debit1);
        } else {
            holder.imageView.setImageResource(R.drawable.payment);
        }

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
