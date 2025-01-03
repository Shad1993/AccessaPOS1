package com.accessa.ibora.sales.ticket.Checkout;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

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
        private Button button;


        public ItemViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            Icon= itemView.findViewById(R.id.icon);
            button=itemView.findViewById(R.id.button);


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
        String iconPath = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PAYMENT_METHOD_COLUMN_ICON));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.Icon.setText(iconPath);

        // Check if id is "1" and name is "POP"
        if (id.equals("1") && name.equals("POP")) {
            Drawable resizedPopLogo = resizeDrawable(mContext, R.drawable.poplogo, 50, 50);
            if (resizedPopLogo != null) {
                holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, resizedPopLogo, null);
                holder.button.setCompoundDrawablePadding(8);
                holder.button.setBackgroundResource(R.drawable.button_dynamic_color);
            }
        } else if (id.equals("6") && name.equals("Coupon Code")) {
            Drawable couponDrawable = ContextCompat.getDrawable(mContext, R.drawable.couponwhite);
            holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, couponDrawable, null);
            holder.button.setCompoundDrawablePadding(8);
            holder.button.setBackgroundResource(R.drawable.button_dynamic_color);
        } else {
            // Load custom icon from file path or URL using Glide
            if (iconPath != null && !iconPath.isEmpty()) {
                Glide.with(mContext)
                        .load(iconPath)  // Load the image from the icon path
                        .override(50, 50)  // Resize as needed
                        .error(R.drawable.meanspayment)  // Fallback to default icon if loading fails
                        .into(new CustomViewTarget<Button, Drawable>(holder.button) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                holder.button.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                holder.button.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.meanspayment), null);
                            }

                            @Override
                            protected void onResourceCleared(@Nullable Drawable placeholder) {
                                holder.button.setCompoundDrawablesWithIntrinsicBounds(null, null, placeholder, null);
                            }
                        });
            } else {
                // Use default icon if no valid icon path is provided
                holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.meanspayment, 0);
            }
            holder.button.setCompoundDrawablePadding(8); // Set padding between text and drawable
        }

        holder.itemView.setTag(id);
    }





    private Drawable resizeDrawable(Context context, int drawableResId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            return new BitmapDrawable(context.getResources(), resizedBitmap);
        }
        return null;
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
