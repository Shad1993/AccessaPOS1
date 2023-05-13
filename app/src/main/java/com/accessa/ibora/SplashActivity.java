package com.accessa.ibora;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {

    private static final long SPLASH_DURATION = 3000; // Splash screen duration in milliseconds
    private static final int PROGRESS_MAX = 100; // Maximum progress value for the loading bar
    private static final long PROGRESS_DURATION = 1500; // Duration of the progress animation in milliseconds

    private ImageView logoImageView;
    private ProgressBar loadingBar;
    private Handler handler;
    private Runnable navigateToNextScreenRunnable;
    private MediaPlayer mediaPlayer;
    private int progress = 0; // Current progress value for the loading bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.splashfile);

        logoImageView = findViewById(R.id.logoImageView);
        loadingBar = findViewById(R.id.loadingBar);

        // Apply bouncing animation to the logo image
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
        bounceAnimation.setInterpolator(new BounceInterpolator());
        logoImageView.startAnimation(bounceAnimation);

        // Load and play the splash sound
        mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound);
        mediaPlayer.start();

        handler = new Handler();
        navigateToNextScreenRunnable = new Runnable() {
            @Override
            public void run() {
                navigateToNextScreen();
            }
        };

        // Update loading bar progress with animation
        animateLoadingBarProgress();

        // Delay transition to the next screen
        handler.postDelayed(navigateToNextScreenRunnable, SPLASH_DURATION);
    }

    private void animateLoadingBarProgress() {
        loadingBar.setProgress(0); // Set the initial progress to 0

        // Calculate the progress increment for each animation frame
        final float progressIncrement = (float) PROGRESS_MAX / (PROGRESS_DURATION / 16);

        // Animate the progress until it reaches the maximum
        final Runnable progressUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (progress < PROGRESS_MAX) {
                    progress += progressIncrement;
                    loadingBar.setProgress(progress);
                    handler.postDelayed(this, 16); // 16ms delay for 60fps animation
                }
            }
        };

        // Start the progress animation
        handler.post(progressUpdateRunnable);
    }

    private void navigateToNextScreen() {
        // Start the next activity (e.g., main activity)
        Intent intent = new Intent(this, welcome.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacks(navigateToNextScreenRunnable);
    }
}
