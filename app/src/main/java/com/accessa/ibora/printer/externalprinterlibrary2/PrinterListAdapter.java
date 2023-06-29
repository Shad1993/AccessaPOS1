package com.accessa.ibora.printer.externalprinterlibrary2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.sunmi.externalprinterlibrary2.printer.CloudPrinter;

import java.util.ArrayList;
import java.util.List;

public class PrinterListAdapter extends RecyclerView.Adapter<PrinterListAdapter.ViewHolder> {

    private List<CloudPrinter> data;
    private OnItemClickListener listener;

    public PrinterListAdapter() {
        this.data = new ArrayList<>();
    }

    public void addData(CloudPrinter cloudPrinter) {
        data.add(cloudPrinter);
        notifyItemInserted(getItemCount());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_printer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvBlueName.setText(data.get(i).getCloudPrinterInfo().name);

    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvBlueName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBlueName = itemView.findViewById(R.id.item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(data.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CloudPrinter cloudPrinter);
    }

}
