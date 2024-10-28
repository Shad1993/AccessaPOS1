package com.accessa.ibora.Settings.PrinterSetup;

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

import java.util.List;

public class PrinterSetupAdaptor extends RecyclerView.Adapter<PrinterSetupAdaptor.ItemViewHolder> {
    private List<PrinterSetUpMethod> paymentMethods;
    private Context mContext;

    // Constructor with list of PaymentMethod
    public PrinterSetupAdaptor(Context context, List<PrinterSetUpMethod> printersetupMethods) {
        mContext = context;
        this.paymentMethods = printersetupMethods;
    }

    public void swapCursor(Cursor newCursor) {


        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView idTextView, iconTextView, Opens;
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            iconTextView = itemView.findViewById(R.id.icon_text_view);
            Opens = itemView.findViewById(R.id.Available_text_view);
            imageView = itemView.findViewById(R.id.image);
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
        if (paymentMethods == null || position >= paymentMethods.size()) {
            return;
        }

        PrinterSetUpMethod paymentMethod = paymentMethods.get(position);

        String id = paymentMethod.getId();
        String name = paymentMethod.getName();
        String icon = paymentMethod.getIcon();
        boolean printallowed = paymentMethod.isDrawerOpens();

        // Set drawer opens text and icon
        if (printallowed) {
            holder.Opens.setText(mContext.getResources().getString(R.string.printallowed));
            holder.Opens.setTextColor(mContext.getResources().getColor(R.color.BleuAccessaText)); // Set text color

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.check);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size
            }
            holder.Opens.setCompoundDrawables(null, null, drawable, null);
        } else {
            holder.Opens.setText(mContext.getResources().getString(R.string.notallowprinting));
            holder.Opens.setTextColor(mContext.getResources().getColor(R.color.OrangeAccessa)); // Set text color

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.cancel);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2); // Adjust the size
            }
            holder.Opens.setCompoundDrawables(null, null, drawable, null);
        }

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.iconTextView.setText(icon);
        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.iconTextView.setText(icon);

        // Set the image resource based on the ID and name
        holder.imageView.setImageResource(R.drawable.printersetup);

        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return (paymentMethods != null) ? paymentMethods.size() : 0;
    }
}
