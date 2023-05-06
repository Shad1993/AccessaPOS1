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

    private ImageView logoImageView;
    private ProgressBar loadingBar;
    private Handler handler;
    private Runnable navigateToNextScreenRunnable;
    private MediaPlayer mediaPlayer;

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

        // Delay transition to the next screen
        handler.postDelayed(navigateToNextScreenRunnable, SPLASH_DURATION);
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
