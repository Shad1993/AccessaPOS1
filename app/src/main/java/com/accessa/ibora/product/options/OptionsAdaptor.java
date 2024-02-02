package com.accessa.ibora.product.options;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class OptionsAdaptor extends RecyclerView.Adapter<OptionsAdaptor.CategoryViewHolder> {

    private Context context;
    private Cursor cursor;
    private int defaultColor;

    public OptionsAdaptor(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView Optionname_text_view;

        public ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            Optionname_text_view = itemView.findViewById(R.id.optionname_text_view);

            imageView = itemView.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.option_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OPTION_ID));
        String catName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OPTION_NAME));


        holder.Optionname_text_view.setText(catName);
        holder.idTextView.setText( id);

        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
