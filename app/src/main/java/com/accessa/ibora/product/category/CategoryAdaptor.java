package com.accessa.ibora.product.category;

import static com.accessa.ibora.product.category.CategoryDatabaseHelper.CatName;
import static com.accessa.ibora.product.category.CategoryDatabaseHelper.TABLE_NAME;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.CategoryViewHolder> {

    private Context context;
    private Cursor cursor;
    private int defaultColor;

    public CategoryAdaptor(Context context, Cursor cursor, int defaultColor) {
        this.context = context;
        this.cursor = cursor;
        this.defaultColor = defaultColor;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView catNameTextView;
        public TextView colorTextView;
        public ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            catNameTextView = itemView.findViewById(R.id.Catname_text_view);
            colorTextView = itemView.findViewById(R.id.Color_text_view);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.catlayout, parent, false);
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

        // Change the color of the vector asset programmatically

        defaultColor=Color.parseColor(color);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.category);
        drawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        DrawableCompat.setTint(drawable, defaultColor);
        holder.imageView.setImageDrawable(drawable);

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
