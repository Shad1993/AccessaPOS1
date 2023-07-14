package com.accessa.ibora.Report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<PaymentItem> paymentItems;

    public PaymentAdapter(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sales, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentItem item = paymentItems.get(position);

        holder.paymentNameTextView.setText(item.getPaymentName());
        holder.amountPaidTextView.setText(String.valueOf(item.getAmountPaid()));
    }

    @Override
    public int getItemCount() {
        return paymentItems.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView paymentNameTextView;
        TextView amountPaidTextView;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            paymentNameTextView = itemView.findViewById(R.id.paymentNameTextView);
            amountPaidTextView = itemView.findViewById(R.id.amountPaidTextView);
        }
    }
}

