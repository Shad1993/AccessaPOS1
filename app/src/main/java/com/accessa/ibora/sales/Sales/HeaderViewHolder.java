package com.accessa.ibora.sales.Sales;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView headerTextView;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        headerTextView = itemView.findViewById(R.id.subcategoryTitle); // Make sure to use the correct ID for the subcategory header.
    }
}
