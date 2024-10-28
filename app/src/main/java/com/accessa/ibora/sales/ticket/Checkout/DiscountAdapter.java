package com.accessa.ibora.sales.ticket.Checkout;

import static com.accessa.ibora.product.items.DatabaseHelper.DISCOUNT_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.DISCOUNT_VALUE;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {
    private Cursor cursor;
    private OnDiscountClickListener listener;

    public interface OnDiscountClickListener {
        void onDiscountClick(double discountValue);
    }

    public DiscountAdapter(Cursor cursor, OnDiscountClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String discountName = cursor.getString(cursor.getColumnIndexOrThrow(DISCOUNT_NAME));
            double discountValue = cursor.getDouble(cursor.getColumnIndexOrThrow(DISCOUNT_VALUE));

            holder.discountButton.setText(discountName);
            holder.discountButton.setOnClickListener(v -> listener.onDiscountClick(discountValue));
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        Button discountButton;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discountButton = itemView.findViewById(R.id.discount_button);
        }
    }
}

