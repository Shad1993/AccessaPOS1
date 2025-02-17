package com.accessa.ibora.SecondScreen;


import static com.accessa.ibora.sales.Sales.ItemGridAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class TransactionDisplay extends Presentation {
    private TextView textViewTime;
    private Handler handler;
    private Context context;
    private TextView textView;
    private ImageView imageView;
    private String transactionIdInProgress;
    private VideoView videoView;
    private DatabaseHelper mDatabaseHelper;
    private static final String TRANSACTION_ID_KEY = "transaction_id";

    private String tableid;
    private int roomid;
    private FrameLayout emptyFrameLayout;
    private LinearLayout total;
    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    public TransactionDisplay(Context context, Display display) {
        super(context, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the views to display the content

        mDatabaseHelper = new DatabaseHelper(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = String.valueOf(preferences.getString("table_id", "0"));
        // Check if tableid is "0" and set to "1" if true
        if (tableid.equals("0")) {
            tableid = "1";
            String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
            transactionIdInProgress= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
            Log.d("tableid", tableid);
            Log.d("roomid", String.valueOf(roomid));
        }else{
            Log.d("tableid", tableid);
            Log.d("roomid", String.valueOf(roomid));
            String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
            transactionIdInProgress= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
        }

// Log the table ID for debugging

        setContentView(R.layout.second_screen_presentation);

        textViewTime = findViewById(R.id.textViewTime);
        handler = new Handler(Looper.getMainLooper());

        // Start updating the time display
        startUpdatingTime();
        AppCompatImageView imageView = findViewById(R.id.empty_image_view);
        SharedPreferences sharedPreference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        String ShopName = sharedPreference.getString("ShopName", null);
        Log.d("ShopName", ShopName);
        // Retrieve the total amount and total tax amount from the transactionheader table
        Cursor cursorCompany = mDatabaseHelper.getCompanyInfo(ShopName);

        if (cursorCompany != null && cursorCompany.moveToFirst()) {
            int columnLogoPathIndex = cursorCompany.getColumnIndex(DatabaseHelper.COLUMN_Logo);

            if (columnLogoPathIndex != -1) {
                String logoPath = cursorCompany.getString(columnLogoPathIndex);

                // Ensure the logo path is not null or empty


                    if (logoPath != null && !logoPath.isEmpty()) {
                        // Load image from web link
                        Glide.with(getContext())
                                .load(logoPath)
                                .placeholder(R.drawable.logobonibora) // Placeholder image while loading
                                .error(R.drawable.logobonibora)
                                .into(imageView);
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        // Load image from local storage

                            loadLocalImage(imageView, String.valueOf(logoPath));

                    }

            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactionsbytable(transactionIdInProgress,String.valueOf(roomid),tableid);

        adapter= new TicketAdapter(getContext(), cursor1);
        recyclerView.setAdapter(adapter);


        total=findViewById(R.id.total);
        emptyFrameLayout = findViewById(R.id.empty_frame_layout);
        if (adapter.getItemCount() <= 0) {
            recyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
            total.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
            total.setVisibility(View.VISIBLE);
        }


        // Scroll to the last item in the RecyclerView
        int itemCount = adapter.getItemCount();
        if (itemCount > 0) {
            recyclerView.smoothScrollToPosition(itemCount - 1);
        }

        // Show or hide the RecyclerView and empty view based on the cursor count
        if (cursor1 != null && cursor1.getCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);

        } else {
            recyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        }
        // Retrieve the total amount and total tax amount from the transactionheader table
        mDatabaseHelper = new DatabaseHelper(getContext()); // Initialize DatabaseHelper
        Cursor cursor3 = mDatabaseHelper.getTransactionHeader(String.valueOf(roomid),tableid);

        if (cursor3 != null && cursor3.moveToFirst()) {
            int columnIndexTotalAmount = cursor3.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
            int columnIndexTotalTaxAmount = cursor3.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

            double totalAmount = cursor3.getDouble(columnIndexTotalAmount);
            double taxTotalAmount = cursor3.getDouble(columnIndexTotalTaxAmount);

            String formattedTaxAmount = String.format("%.2f", taxTotalAmount);
            String formattedTotalAmount = String.format("%.2f", totalAmount);

            // Update the tax and total amount TextViews
            TextView taxTextView = findViewById(R.id.textViewVAT);
            taxTextView.setText(getContext().getString(R.string.tax) + ": Rs " + formattedTaxAmount);

            TextView totalAmountTextView = findViewById(R.id.textViewTotal);
            totalAmountTextView.setText(getContext().getString(R.string.Total) + ": Rs " + formattedTotalAmount);
        }

    }


    // Update the RecyclerView data
    public  void updateRecyclerViewData(List<String> newData) {
        adapter.setData(newData);
        adapter.notifyDataSetChanged();
    }

    private void startUpdatingTime() {
        handler.post(timeRunnable);
    }

    private void updateCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        textViewTime.setText(currentTime);
    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            updateCurrentTime();
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        // Stop updating the time when the presentation is stopped
        handler.removeCallbacks(timeRunnable);
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
                options.inSampleSize = calculateInSampleSize(options, 100, 100); // Replace desiredWidth and desiredHeight with the desired dimensions

                // Set options to load the scaled-down version of the image
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565; // Adjust this based on your requirements

                // Load the image with the updated options
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.logobonibora);
            }
        } else {
            imageView.setImageResource(R.drawable.logobonibora);
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
}