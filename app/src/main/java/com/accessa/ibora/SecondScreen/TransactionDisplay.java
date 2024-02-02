package com.accessa.ibora.SecondScreen;


import android.app.Presentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
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

        setContentView(R.layout.second_screen_presentation);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);
        SharedPreferences preferences = getContext().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = String.valueOf(preferences.getInt("table_id", 0));

        textViewTime = findViewById(R.id.textViewTime);
        handler = new Handler(Looper.getMainLooper());

        // Start updating the time display
        startUpdatingTime();
        AppCompatImageView imageView = findViewById(R.id.empty_image_view);

        Glide.with(getContext())
                .asGif()
                .load(R.drawable.ads)
                .into(imageView);



        // Initialize the views to display the content

        mDatabaseHelper = new DatabaseHelper(getContext());
        mDatabaseHelper = new DatabaseHelper(getContext());
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor cursor1 = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
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


    public void updateRecyclerView(List<String> newData) {
        adapter.updateData(newData);
    }




    private void displayVideo(String videoUrl) {
        // Display the video content
        if (videoView != null) {
            // Set the video URI to the videoUrl
            videoView.setVideoURI(Uri.parse(videoUrl));

            // Start playing the video
            videoView.start();

            videoView.setVisibility(View.VISIBLE);
        }
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
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
}