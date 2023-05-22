package com.accessa.ibora.product.Department;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.login.RegistorCashor;
import com.accessa.ibora.product.items.DatabaseHelper;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public DepartmentAdapter(Context context, Cursor cursor) {
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
        View view = inflater.inflate(R.layout.department_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_NAME));
        String lastModified = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_LAST_MODIFIED));
        String DepartmentCode = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DEPARTMENT_CODE));
        String UserID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_CASHOR_id));


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
