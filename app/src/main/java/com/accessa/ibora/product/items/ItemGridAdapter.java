package com.accessa.ibora.product.items;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;

public class ItemGridAdapter extends RecyclerView.Adapter<ItemGridAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public ItemGridAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView idTextView;
        public TextView priceTextView;
        public ImageView productImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.Longdescription_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            productImage = itemView.findViewById(R.id.ProductImage);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_gridlayout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price));
        String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
        String productImageName = id; // Use the ID as the image name

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.descriptionTextView.setText(description);
        holder.priceTextView.setText(price);

        // Load image from mipmaps
        int resId = mContext.getResources().getIdentifier(productImageName, "mipmap", mContext.getPackageName());
        if (resId != 0) {
            Glide.with(mContext)
                    .load(resId)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.emptybasket)
                            .error(R.drawable.emptybasket))
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.emptybasket);
        }

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

    private boolean isWebLink(String url) {
        return URLUtil.isValidUrl(url);
    }
}
