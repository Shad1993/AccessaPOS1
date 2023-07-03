package com.accessa.ibora.SecondScreen;


import android.app.Presentation;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.ticket.TicketAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeconScreenDisplay extends Presentation {
    private TextView textView;
    private ImageView imageView;
    private VideoView videoView;
    private DatabaseHelper mDatabaseHelper;
    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    private FrameLayout QRFrameLayout,Splashlayout;
    public SeconScreenDisplay(Context context, Display display) {
        super(context, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.presentation_layout);

        // Initialize the views to display the content
        textView = findViewById(R.id.presentation_text);
        imageView = findViewById(R.id.presentation_image);
        videoView = findViewById(R.id.presentation_video);


    }

    public void updateContent(String content) {
        // Check the content type and display accordingly
        if (isImageContent(content)) {
            displayImage(content);
        } else if (isVideoContent(content)) {
            displayVideo(content);
        } else {
            displayText(content);
        }
    }
    // Update the RecyclerView data

    private boolean isImageContent(String content) {
        // Implement your logic to determine if the content is an image
        // Return true if it is an image, false otherwise
        return true;
    }

    private boolean isVideoContent(String content) {
        // Implement your logic to determine if the content is a video
        // Return true if it is a video, false otherwise
        return true;
    }
    public void updateTextAndQRCode(String text, Bitmap qrCode,String formattedTaxAmount, String formattedTotalAmount) {
        QRFrameLayout = findViewById(R.id.qr_frame_layout);

            QRFrameLayout.setVisibility(View.VISIBLE);



        // Update the text displayed on the secondary screen
        TextView textView = findViewById(R.id.text_view);
        TextView TotaltextView = findViewById(R.id.Total_text_view);
        TextView TaxtextView = findViewById(R.id.Tax_text_view);
        textView.setText(text);
        TotaltextView.setText(getContext().getString(R.string.Total) + ": Rs " + formattedTotalAmount);
        TaxtextView.setText (getContext().getString(R.string.tax) + ": Rs " + formattedTaxAmount);

        // Update the QR code displayed on the secondary screen
        ImageView qrCodeImageView = findViewById(R.id.qr_code_image_view);
        qrCodeImageView.setImageBitmap(qrCode);
    }

    private void displayText(String text) {
        // Display the text content
        if (textView != null) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
    }
    public void displaylogo() {
        Splashlayout = findViewById(R.id.splash_frame_layout);

        Splashlayout.setVisibility(View.VISIBLE);
// Get references to the logo ImageView and text TextView
        CircleImageView logoImageView = findViewById(R.id.logo_image_view);
        TextView welcomeTextView = findViewById(R.id.welcome_text_view);

// Create animations
        Animation logoAnimation = new TranslateAnimation(-200, 0, 0, 0); // Move logo from left (-200px) to center (0px)
        logoAnimation.setDuration(1000); // Animation duration in milliseconds

        Animation textAnimation = new TranslateAnimation(200, 0, 0, 0); // Move text from right (200px) to center (0px)
        textAnimation.setDuration(1000); // Animation duration in milliseconds

// Apply animations to views
        logoImageView.startAnimation(logoAnimation);
        welcomeTextView.startAnimation(textAnimation);

    }


    public void displaydefault() {
        Splashlayout = findViewById(R.id.default_frame_layout);

        Splashlayout.setVisibility(View.VISIBLE);
// Get references to the logo ImageView and text TextView
        CircleImageView logoImageView = findViewById(R.id.logo_image_view);
        TextView welcomeTextView = findViewById(R.id.welcome_text_view);

// Create animations
        Animation logoAnimation = new TranslateAnimation(-200, 0, 0, 0); // Move logo from left (-200px) to center (0px)
        logoAnimation.setDuration(1000); // Animation duration in milliseconds

        Animation textAnimation = new TranslateAnimation(200, 0, 0, 0); // Move text from right (200px) to center (0px)
        textAnimation.setDuration(1000); // Animation duration in milliseconds

// Apply animations to views
        logoImageView.startAnimation(logoAnimation);
        welcomeTextView.startAnimation(textAnimation);

    }
    private void displayImage(String imageUrl) {
        // Display the image content
        if (imageView != null) {
            // Load the image from the imageUrl using a library like Picasso or Glide
            // Example using Picasso:
            Picasso.get().load(imageUrl).into(imageView);

            imageView.setVisibility(View.VISIBLE);
        }
        textView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
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


}