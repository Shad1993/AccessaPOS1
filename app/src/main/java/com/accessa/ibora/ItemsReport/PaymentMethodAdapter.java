package com.accessa.ibora.ItemsReport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private List<PaymentMethodDataModel> dataList;

    public PaymentMethodAdapter(List<PaymentMethodDataModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentMethodDataModel data = dataList.get(position);

        holder.textViewPaymentName.setText(data.getPaymentName());
        holder.textViewPaymentCount.setText(String.valueOf(data.getPaymentCount()));
        String formattedTotalPrice = "Rs " + String.format("%.2f", data.getTotalAmount());
        holder.textViewTotalAmount.setText(formattedTotalPrice);
    }
    public void updateData(List<PaymentMethodDataModel> newDataList) {
        dataList.clear();
        dataList.addAll(newDataList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPaymentName;
        TextView textViewPaymentCount;
        TextView textViewTotalAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPaymentName = itemView.findViewById(R.id.textViewPaymentName);
            textViewPaymentCount = itemView.findViewById(R.id.textViewPaymentCount);
            textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
        }
    }
}
