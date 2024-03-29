package com.accessa.ibora.product.couponcode;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public CouponAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView DeptcodeTextView;
        public TextView idTextView;
        public TextView LastModifiedTextView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            DeptcodeTextView = itemView.findViewById(R.id.deptcode_text_view);
            LastModifiedTextView = itemView.findViewById(R.id.LastModified_edittex);

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.coupon_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COUPON_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COUPON_CODE));
        String lastModified = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COUPON_END_DATE));
        String DepartmentCode = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COUPON_STATUS));
        String UserID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COUPON_CASHIER_ID));


        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.DeptcodeTextView.setText(DepartmentCode);
        holder.LastModifiedTextView.setText(lastModified);
        holder.itemView.setTag(id);
    }



    @Override
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
