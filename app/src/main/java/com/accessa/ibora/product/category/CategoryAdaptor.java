package com.accessa.ibora.product.category;

import static com.accessa.ibora.product.items.DatabaseHelper.LongDescription;
import static com.accessa.ibora.product.items.DatabaseHelper._ID;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public CategoryAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public CategoryAdapter(List<Category> items) {
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView CatNameTextView;
        public TextView ColorTextView;

        public TextView IdTextView;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            IdTextView = itemView.findViewById(R.id.id_text_view);
            CatNameTextView = itemView.findViewById(R.id.name_text_view);
            ColorTextView = itemView.findViewById(R.id.Longdescription_text_view);

        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.itemlayout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String CatName = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
        String id1 = mCursor.getString(mCursor.getColumnIndex(_ID));
        String Color = mCursor.getString(mCursor.getColumnIndex(Color));
        long id = mCursor.getLong(mCursor.getColumnIndex(_ID));



        holder.IdTextView.setText(id1);
        holder.CatNameTextView.setText(CatName);
        holder.Color.setText(Color);

        holder.itemView.setTag(id);
    }




    @Override
    public int getCategoryCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}

