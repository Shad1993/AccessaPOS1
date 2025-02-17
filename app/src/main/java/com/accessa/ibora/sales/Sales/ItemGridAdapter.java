package com.accessa.ibora.sales.Sales;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int PERMISSION_REQUEST_CODE = 123;
    public static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context mContext;
    private Cursor mCursor;
    private List<Object> itemsWithHeaders; // List of items and headers
    private List<Integer> availableItemsIndices;

     public int totalItemCount = 0; // Track total items
    private ItemClickListener itemClickListener;
    private int blankSpacesCount = 0;
    private String selectedCategory;
    private String selectedSubCategory;
    private int subcategoryTitleCount = 0;


    public ItemGridAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        itemsWithHeaders = new ArrayList<>();
        updateAvailableItemsWithHeaders();
        availableItemsIndices = new ArrayList<>(); // Initialize the list here
       // updateAvailableItemsIndices();
    }

    public void setCategoryFilter(String category) {
        selectedCategory = category;
        updateAvailableItemsWithHeaders();
       // updateAvailableItemsIndices();
        notifyDataSetChanged();
    }

    public void setSubCategoryFilter(String subCategory) {
        selectedSubCategory = subCategory;
        updateAvailableItemsWithHeaders();
        notifyDataSetChanged();
    }

    private void updateAvailableItemsWithHeaders() {
        if (mCursor == null) {
            return;
        }

        itemsWithHeaders.clear();
        mCursor.moveToPosition(-1);

        String currentSubCategory = null;
        int itemsPerRow = 12; // Number of items per row in the grid

        int currentRowCount = 0; // Track the current position within the row
        subcategoryTitleCount = 0; // Reset subcategory title count
        blankSpacesCount = 0; // Reset blank spaces count

        while (mCursor.moveToNext()) {
            String availableForSale = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.AvailableForSale));
            String category = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Category));
            String subCategory = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.SubCategory));
            String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));

            if (subCategory == null) {
                subCategory = "Uncategorized"; // Default for items with no subcategory
            }

            boolean categoryMatches = (selectedCategory == null || category.equals(selectedCategory));
            boolean subCategoryMatches = (selectedSubCategory == null || subCategory.equals(selectedSubCategory));
            Log.d("subCategoryMatches", String.valueOf(subCategoryMatches));

            if (availableForSale.equals("true") && categoryMatches && subCategoryMatches) {
                // If we encounter a new subcategory, add it as a header
                if (!subCategory.equals(currentSubCategory)) {
                    // Add blank cells to complete the previous row if necessary
                    if (currentRowCount > 0 && currentRowCount % itemsPerRow != 0) {
                        int blankCells = itemsPerRow - (currentRowCount % itemsPerRow);
                        Log.d("AddingBlankCells", String.valueOf(blankCells));
                        for (int i = 0; i < blankCells; i++) {

                        }
                        blankSpacesCount += blankCells;
                    }

                    // Add subcategory title as a full-width row
                    currentSubCategory = subCategory;


                    itemsWithHeaders.add(currentSubCategory); // Add subcategory title as a header
                    subcategoryTitleCount++; // Increment subcategory title count
                    currentRowCount = 0; // Reset for the new subcategory
                }

                // Add the current item
                itemsWithHeaders.add(mCursor.getPosition());
                totalItemCount++;
                currentRowCount++;
            }
        }

        // Handle blank cells for the last row after all items
        if (currentRowCount > 0 && currentRowCount % itemsPerRow != 0) {
            int blankCells = itemsPerRow - (currentRowCount % itemsPerRow);
            Log.d("FinalBlankCells", String.valueOf(blankCells));
            for (int i = 0; i < blankCells; i++) {

            }
            blankSpacesCount += blankCells;
        }

        // Log final counts
        Log.d("FinalCounts", "TotalItemCount: " + totalItemCount + ", SubcategoryTitleCount: " + subcategoryTitleCount + ", BlankSpacesCount: " + blankSpacesCount);
    }

    public interface ItemClickListener {
        void onItemClick(Item item);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < itemsWithHeaders.size()) {
            if (itemsWithHeaders.get(position) instanceof String) {
                return VIEW_TYPE_HEADER;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }
        // Handle cases where position is out of bounds, if needed
        return super.getItemViewType(position);  // Default return
    }



    @Override
    public int getItemCount() {
        // Add the size of availableItemsIndices and any additional blank spaces you are inserting

        return totalItemCount + subcategoryTitleCount;
    }


    private int getRealPosition(int position) {
        return availableItemsIndices.get(position);
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
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sub_category_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_gridlayout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < 0 || position >= itemsWithHeaders.size()) {
            // Handle invalid position (e.g., return null)
            return;
        }
        // Check if this position corresponds to a header (subcategory title)
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            String subCategory = (String) itemsWithHeaders.get(position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            if (subCategory == null || subCategory.trim().isEmpty()) {
                // If the subcategory is blank, hide the header view
                headerHolder.headerTextView.setText("");


            } else {

                // Display the subcategory title
                headerHolder.headerTextView.setText(subCategory);

            }
        } else {
            // Handle regular items (non-header)
            int realPosition = (int) itemsWithHeaders.get(position);

            if (!mCursor.moveToPosition(realPosition)) {
                return;
            }

            // Fetch item data from the cursor
            String subCategory = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.SubCategory)); // Sub-category column


            SharedPreferences sharedPreferencepos = mContext.getSharedPreferences("pricelevel", Context.MODE_PRIVATE);
            String pLevel = sharedPreferencepos.getString("selectedPriceLevel", "Price Level 1");
            String PriceInRs = getPriceinRSBasedOnLevel(pLevel);
            String price = getPriceBasedOnLevel(pLevel);

            final String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
            final String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));
            final String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper._ID));
            final String productImageName = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Image));

            // Bind item data to the views
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.idTextView.setText(id);
            itemHolder.nameTextView.setText(name);
            itemHolder.descriptionTextView.setText(description);
            itemHolder.priceTextView.setText(PriceInRs);
            itemHolder.priceNorsTextView.setText(price);


            // Load image based on its source (web or local)
            if (isWebLink(productImageName)) {
                // Load web image using Glide
                Glide.with(mContext)
                        .load(productImageName) // Load the web link directly
                        .override(100, 100) // Set desired dimensions
                        .skipMemoryCache(true) // Avoid using memory cache
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Avoid disk caching
                        .into(itemHolder.productImage); // Directly load the image
            } else {
                // Check permissions for reading external storage
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            (Activity) mContext,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE
                    );
                } else {
                    if (productImageName != null && !productImageName.isEmpty()) {
                        File imageFile = new File(productImageName);
                        if (imageFile.exists()) {
                            // Load the local image
                            loadLocalImage(itemHolder.productImage, productImageName);
                        } else {
                            // Set the default vector drawable if the file doesn't exist
                            itemHolder.productImage.setImageResource(R.drawable.testimage); // Use your vector drawable resource
                        }
                    } else {
                        // Set the default vector drawable for null or empty productImageName
                        itemHolder.productImage.setImageResource(R.drawable.testimage); // Use your vector drawable resource
                    }
                }
            }



            // Set item click listener
            itemHolder.itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    Item item = new Item(name, id, price, description, productImageName);
                    itemClickListener.onItemClick(item);
                }
            });
        }
    }






    private String getPriceinRSBasedOnLevel(String pLevel) {
        switch (pLevel) {
            case "Price Level 2":
                return "Rs " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price2AfterDiscount));
            case "Price Level 3":
                return "Rs " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price3AfterDiscount));
            default:
                return "Rs " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PriceAfterDiscount));
        }
    }

    private String getPriceBasedOnLevel(String pLevel) {
        switch (pLevel) {
            case "Price Level 2":
                return mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price2AfterDiscount));
            case "Price Level 3":
                return mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price3AfterDiscount));
            default:
                return mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PriceAfterDiscount));
        }
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
                options.inSampleSize = calculateInSampleSize(options, 100, 100); // Replace 100, 100 with your desired dimensions

                // Set options to load the scaled-down version of the image
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565; // Adjust this based on your requirements

                // Load the image with the updated options
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

                // Set the bitmap to the ImageView
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    // Clear the ImageView to display nothing
                    imageView.setImageResource(R.drawable.testimage); // Replace 'resto' with the actual name of your vector drawable resource

                }
            } else {
                // Clear the ImageView to display nothing
                imageView.setImageResource(R.drawable.testimage); // Replace 'resto' with the actual name of your vector drawable resource

            }
        } else {
            // Clear the ImageView to display nothing
            imageView.setImageResource(R.drawable.testimage); // Replace 'resto' with the actual name of your vector drawable resource

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
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        updateAvailableItemsWithHeaders();


        if (newCursor != null) {
            updateAvailableItemsWithHeaders();


            notifyDataSetChanged();


        }
    }
    public Item getItem(int position) {
        if (position < 0 || position >= itemsWithHeaders.size()) {
            return null; // Return null if the position is out of bounds
        }

        Object itemOrHeader = itemsWithHeaders.get(position);

        // Ensure it's not a header
        if (itemOrHeader instanceof Integer) {
            int realPosition = (int) itemOrHeader;
            if (mCursor.moveToPosition(realPosition)) {
                String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Name));
                String price = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.Price));
                String description = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LongDescription));

                // Create and return the Item object
                return new Item(name, price, description);
            }
        }
        return null; // Return null if it's a header or invalid position
    }
    public void updateData(List<Object> newData) {
        this.itemsWithHeaders = newData;
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.header_text_view); // Ensure this ID exists in sub_category_header layout
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView idTextView;
        public TextView priceNorsTextView,initialprice;

        public TextView descriptionTextView;
        public ImageView productImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_text_view);

            nameTextView = itemView.findViewById(R.id.name_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            priceNorsTextView=itemView.findViewById(R.id.priceNoRs_text_view);
            productImage = itemView.findViewById(R.id.ProductImage);
            descriptionTextView = itemView.findViewById(R.id.Longdescription_text_view);
            initialprice= itemView.findViewById(R.id.initialprice_text_view);

        }
    }

    public static boolean isWebLink(String url) {
        return URLUtil.isValidUrl(url);
    }
}
