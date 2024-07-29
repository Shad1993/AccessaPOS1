package com.accessa.ibora;

import static com.accessa.ibora.product.items.DatabaseHelper.PREFERENCE_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.STATUS_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;

public class salestype extends AppCompatActivity {
    private AlertDialog alertDialog;

    String cashorName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectsalestype);

        Button onSpotButton = findViewById(R.id.onspot);
        Button deliveryButton = findViewById(R.id.delivery);
        Button freeSalesButton = findViewById(R.id.FreeSales);

        // Check and set the status based on the clicked button
        onSpotButton.setOnClickListener(v -> {
            saveStatusAndNavigate("Dine In");
        });

        deliveryButton.setOnClickListener(v -> {
            saveStatusAndNavigate("Delivery");
        });

        freeSalesButton.setOnClickListener(v -> {
            saveStatusAndNavigate("Free Sales");
        });
    }
    private void saveStatusAndNavigate(String status) {
        saveStatus(status);
        // Get the data passed from login activity
        Intent loginIntent = getIntent();
        cashorName = loginIntent.getStringExtra("cashorName");
        String cashorId = loginIntent.getStringExtra("cashorId");
        String ShopName = loginIntent.getStringExtra("ShopName");
        // Inflate the custom view for the AlertDialog
        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null);
        // Create and show the AlertDialog with the welcome message and cashor's name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.welcome);
        builder.setMessage(getString(R.string.welcomes) + " " + cashorName + "!");
        // Get a reference to the AppCompatImageView
        AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.hello)
                .into(gifImageView);
        // Find the "Retry" button
        Button retryButton = view.findViewById(R.id.button_retry);

        // Set the visibility of the "Retry" button to GONE
        retryButton.setVisibility(View.GONE);

        // Remove the click listener for the "Retry" button
        retryButton.setOnClickListener(null);

        // Set the custom view to the AlertDialog
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("status", status);



        // Pass additional data to MainActivity
        intent.putExtra("cashorName", cashorName);
        intent.putExtra("cashorId", cashorId);
        intent.putExtra("ShopName", ShopName);

        startActivity(intent);
        finish(); // Optional: Close the current activity if needed
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) { // Check if the activity is still running
                    startActivity(intent);
                    finish(); // Optional: Finish the current activity if needed
                }
            }
        }, 1000);  // Delay in milliseconds (1 second
        builder.setCancelable(false);
    }
    private void saveStatus(String status) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(STATUS_KEY, status);
        editor.apply();
    }


}
