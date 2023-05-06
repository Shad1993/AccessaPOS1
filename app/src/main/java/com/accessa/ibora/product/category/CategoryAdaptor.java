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

import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.CategoryViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public CategoryAdaptor(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public CategoryAdaptor(List<category> category) {
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView IdTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            IdTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.Longdescription_text_view);
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

        String name = mCursor.getString(mCursor.getColumnIndex(CatDatabaseHelper.SUBJECT));
        String id1 = mCursor.getString(mCursor.getColumnIndex(CatDatabaseHelper._ID));
        String description = mCursor.getString(mCursor.getColumnIndex(CatDatabaseHelper.DESC));
        long id = mCursor.getLong(mCursor.getColumnIndex(CatDatabaseHelper._ID));



        holder.IdTextView.setText(id1);
        holder.nameTextView.setText(name);
        holder.descriptionTextView.setText(description);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
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

