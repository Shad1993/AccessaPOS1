package com.accessa.ibora.product.category;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.CategoryViewHolder> {

    private Context context;
    private Cursor cursor;

    public CategoryAdaptor(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView catNameTextView;
        public TextView colorTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            catNameTextView = itemView.findViewById(R.id.name_text_view);

        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cat_fragment, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        long id = cursor.getLong(cursor.getColumnIndex(CategoryDatabaseHelper._ID));
        String catName = cursor.getString(cursor.getColumnIndex(CategoryDatabaseHelper.CatName));
        String color = cursor.getString(cursor.getColumnIndex(CategoryDatabaseHelper.Color));

        holder.idTextView.setText(String.valueOf(id));
        holder.catNameTextView.setText(catName);
        holder.colorTextView.setText(color);

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
