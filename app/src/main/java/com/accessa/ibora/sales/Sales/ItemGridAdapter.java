package com.accessa.ibora.sales.Sales;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

import com.accessa.ibora.product.items.Item;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemGridAdapter extends RecyclerView.Adapter<ItemGridAdapter.ItemViewHolder> {

    public static final int PERMISSION_REQUEST_CODE = 123;

    private Context mContext;
    private Cursor mCursor;
    private List<Integer> availableItemsIndices;
    private ItemClickListener itemClickListener;
    private String selectedCategory; // Variable to store the selected category filter
    public void setCategoryFilter(String category) {
        selectedCategory = category;
        updateAvailableItemsIndices();
        notifyDataSetChanged();
    }
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public ItemGridAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        availableItemsIndices = new ArrayList<>(); // Initialize the list here
        updateAvailableItemsIndices();
    }

    public Item getItem(int childAdapterPosition) {
        int realPosition = getRealPosition(childAdapterPosition);
        if (mCursor.moveToPosition(realPosition)) {
            return getItemFromCursor(mCursor);
        }
        return null;
    }


    public interface ItemClickListener {
        void onItemClick(Item item);
    }

    private void updateAvailableItemsIndices() {
        if (mCursor == null) {
            return; // Add this check to avoid NPE
        }
        availableItemsIndices.clear();
        mCursor.moveToPosition(-1); // Move cursor to the beginning
        int position = 0;
        while (mCursor.moveToNext()) {
            String availableForSale = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.AvailableForSale));
            String category = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Category));


            if (availableForSale.equals("true") && (selectedCategory == null || category.equals(selectedCategory))) {
                availableItemsIndices.add(position);
            }
            position++;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView idTextView;
        public TextView priceTextView,priceNorsTextView;
        public ImageView productImage;


        public ItemViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.Longdescription_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            priceNorsTextView=itemView.findViewById(R.id.priceNoRs_text_view);
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
        if (!mCursor.moveToPosition(getRealPosition(position))) {
            return;
        }

        SharedPreferences sharedPreferencepos = mContext.getSharedPreferences("pricelevel", Context.MODE_PRIVATE);

        // Check if "selectedPriceLevel" is present in the preferences
        if (!sharedPreferencepos.contains("selectedPriceLevel")) {
            // If not present, set the default value as "Price Level 1"
            SharedPreferences.Editor editor = sharedPreferencepos.edit();
            editor.putString("selectedPriceLevel", "Price Level 1");
            editor.apply();
        }

        String pLevel = sharedPreferencepos.getString("selectedPriceLevel", null);
        String price = null;
        String PriceInRs = null;

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
        if(pLevel.equals("Price Level 1")) {
            price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PriceAfterDiscount));
            PriceInRs = "Rs " + price;
        } else if (pLevel.equals("Price Level 2")) {
            price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price2AfterDiscount));
            PriceInRs = "Rs " + price;

        } else if (pLevel.equals("Price Level 3")) {
            price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price3AfterDiscount));
            PriceInRs = "Rs " + price;

        } else   {
            price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PriceAfterDiscount));
            PriceInRs = "Rs " + price;

        }


        String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
        String productImageName = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Image));

        holder.idTextView.setText(id);
        holder.nameTextView.setText(name);
        holder.descriptionTextView.setText(description);
        holder.priceTextView.setText(PriceInRs);
        holder.priceNorsTextView.setText(price);

        if (isWebLink(productImageName)) {
            // Load image from web link
            Glide.with(mContext)
                    .load(productImageName)
                    .placeholder(R.drawable.emptybasket) // Placeholder image while loading
                    .error(R.drawable.iboralogos1)
                    .into(holder.productImage);
        } else {
            // Load image from local storage
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadLocalImage(holder.productImage, productImageName);
            }
        }

        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return availableItemsIndices.size();
    }

    private int getRealPosition(int position) {
        return availableItemsIndices.get(position);
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        updateAvailableItemsIndices();

        if (newCursor != null) {
            updateAvailableItemsIndices();
            notifyDataSetChanged();
        }
    }

    public static boolean isWebLink(String url) {
        return URLUtil.isValidUrl(url);
    }

    public static void loadLocalImage(ImageView imageView, String imageLocation) {
        if (imageLocation != null && !imageLocation.isEmpty()) {
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {
                // Calculate the image's dimensions without loading it into memory
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

                // Calculate the desired sample size to scale down the image
                options.inSampleSize = calculateInSampleSize(options, 200, 200); // Replace desiredWidth and desiredHeight with the desired dimensions

                // Set options to load the scaled-down version of the image
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565; // Adjust this based on your requirements

                // Load the image with the updated options
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.emptybasket);
            }
        } else {
            imageView.setImageResource(R.drawable.emptybasket);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int desiredWidth, int desiredHeight) {
        final int imageWidth = options.outWidth;
        final int imageHeight = options.outHeight;
        int inSampleSize = 1;

        if (imageHeight > desiredHeight || imageWidth > desiredWidth) {
            final int halfHeight = imageHeight / 2;
            final int halfWidth = imageWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // the width and height larger than the desired dimensions.
            while ((halfHeight / inSampleSize) >= desiredHeight && (halfWidth / inSampleSize) >= desiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Item getItemFromCursor(Cursor cursor) {
        // Retrieve the item data from the cursor and create an Item object
        // Modify this code based on your cursor structure and item class
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Name));
        String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID));
        String price = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Price));
        String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LongDescription));
        String productImageName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Image));

        Item item = new Item(name, id, price, description, productImageName);
        return item;
    }

    // Handle the permission request response
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reload the image
                notifyDataSetChanged();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or use a placeholder image)
                Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
