package com.accessa.ibora.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.Report.PaymentItem;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<PaymentItem> paymentItems;
    private Context context;
    private double totalAmount; // Add a field to store the total amount

    public PaymentAdapter(List<PaymentItem> paymentItems, Context context) {
        if (paymentItems == null) {
            this.paymentItems = new ArrayList<>();
        } else {
            this.paymentItems = paymentItems;
        }
        this.context = context;
    }


    public void updateItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
        notifyDataSetChanged();
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
        holder.amountPaidTextView.setText(context.getString(R.string.currency_symbol) + " " + String.valueOf(item.getAmountPaid()));

    }

    @Override
    public int getItemCount() {
        return paymentItems.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView paymentNameTextView;
        TextView amountPaidTextView;
        TextView totalAmountTextView; // Add a TextView for the total amount

        public PaymentViewHolder(View itemView) {
            super(itemView);
            paymentNameTextView = itemView.findViewById(R.id.paymentNameTextView);
            amountPaidTextView = itemView.findViewById(R.id.amountPaidTextView);

        }
    }
}
