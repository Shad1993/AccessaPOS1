package com.accessa.ibora.ItemsReport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

// ReportAdapter.java
public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {

    private List<DataModel> dataList;

    public SalesReportAdapter(List<DataModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel data = dataList.get(position);

        // Set the long description
        holder.textViewLongDescription.setText(data.getLongDescription());

        // Set the total price with "Rs" and 2 decimal places
        String formattedTotalPrice = "Rs " + String.format("%.2f", data.getTotalPrice());
        holder.textViewTotalPrice.setText(formattedTotalPrice);

        // Set the quantity
        holder.textViewQuantity.setText(String.valueOf(data.getQuantity()));
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewLongDescription;
        TextView textViewTotalPrice;
        TextView textViewQuantity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewLongDescription = itemView.findViewById(R.id.textViewLongDescription);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }


    public void updateData(List<DataModel> newDataList) {
        dataList.clear();
        dataList.addAll(newDataList);
        notifyDataSetChanged();
    }
}

