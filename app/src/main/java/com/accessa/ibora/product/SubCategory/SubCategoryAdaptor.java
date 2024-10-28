package com.accessa.ibora.product.SubCategory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class SubCategoryAdaptor extends RecyclerView.Adapter<SubCategoryAdaptor.CategoryViewHolder> {

    private Context context;
    private Cursor cursor;
    private int defaultColor;

    public SubCategoryAdaptor(Context context, Cursor cursor, int defaultColor) {
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
        View view = inflater.inflate(R.layout.subcatlayout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID));
        String subcatName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SUBCatName));
        String relatedcat = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Related_CAT));

        holder.idTextView.setText(String.valueOf(id));
        holder.catNameTextView.setText(subcatName);
        holder.colorTextView.setText(relatedcat);




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
