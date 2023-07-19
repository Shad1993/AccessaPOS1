package com.accessa.ibora.QR;

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

import java.util.List;

public class QRGridAdapter extends RecyclerView.Adapter<QRGridAdapter.ItemViewHolder> {

    public static final int PERMISSION_REQUEST_CODE = 123;

    private Context mContext;
    private Cursor mCursor;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public QRGridAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

    }

    public QR getItem(int childAdapterPosition) {
        int realPosition = childAdapterPosition;
        if (mCursor.moveToPosition(realPosition)) {
            return getItemFromCursor(mCursor);
        }
        return null;
    }


    public interface ItemClickListener {
        void onItemClick(String item);
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        private ImageView imageView;
        public TextView idTextView,Code;



        public ItemViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            Code= itemView.findViewById(R.id.code);
            imageView=itemView.findViewById(R.id.image_view);


        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.qr_button_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_METHOD));
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_ID));
        String code = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_QR_CODE_NUM));


        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.Code.setText(code);
        // Set the image resource based on the ID and name
        if (id.equals("1") && name.equals("POP")) {
            holder.imageView.setImageResource(R.drawable.poplogo);
        } else {
            holder.imageView.setImageResource(R.drawable.purpleqr);
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

    private QR getItemFromCursor(Cursor cursor) {
        // Retrieve the item data from the cursor and create an Item object
        // Modify this code based on your cursor structure and item class
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_METHOD));
        String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_ID));
        String code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QR_CODE_NUM));


        QR item = new QR(name, id, code);
        return item;
    }


}
