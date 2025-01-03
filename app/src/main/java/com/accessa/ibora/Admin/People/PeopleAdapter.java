package com.accessa.ibora.Admin.People;

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


public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public PeopleAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView Dept,Level;
        public TextView idTextView;
        public TextView priceTextView;
        public TextView Available;
        public ImageView profileImageView;  // Add this line for the ImageView

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
           Dept = itemView.findViewById(R.id.Dept_text_view);
            Level = itemView.findViewById(R.id.Level_text_view);
            profileImageView = itemView.findViewById(R.id.image);  // Initialize the ImageView

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.people_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToFirst()) {
            return;
        }
        mCursor.moveToPosition(position);

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_NAME));
        String dept = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_DEPARTMENT));
        String level = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_LEVEL));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.Dept.setText(dept);
        holder.Level.setText(level);

        // Set a different image if level is "0"
        if ("0".equals(level)) {
            holder.profileImageView.setImageResource(R.drawable.trainee);  // Replace with your trainee image
        } else {
            holder.profileImageView.setImageResource(R.drawable.usericon);  // Default image
        }

        holder.itemView.setTag(id);
    }


    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
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
