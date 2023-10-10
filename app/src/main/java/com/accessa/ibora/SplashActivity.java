package com.accessa.ibora;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.product.items.DatabaseHelper;

import woyou.aidlservice.jiuiv5.IWoyouService;

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
    private DatabaseHelper mDatabaseHelper;
    private static IWoyouService woyouService;
    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);

            // Call the method to display on the LCD here
            displayOnLCD();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashfile);
        showSecondaryScreen();
        logoImageView = findViewById(R.id.logoImageView);
        loadingBar = findViewById(R.id.loadingBar);



        Intent intent1 = new Intent();
        intent1.setPackage("woyou.aidlservice.jiuiv5");
        intent1.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent1, connService, Context.BIND_AUTO_CREATE);

        mDatabaseHelper = new DatabaseHelper(this);
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
    private void showSecondaryScreen() {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();

        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            SeconScreenDisplay secondaryDisplay = new SeconScreenDisplay(this, presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();

            // Update the RecyclerView data on the secondary screen
            secondaryDisplay.displaylogo();

        } else {
            // Secondary screen not found or not supported
            displayOnLCD();
        }
    }

    public  void displayOnLCD() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {


                woyouService.sendLCDDoubleString("Welcome" , "Back " , null);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private Display getPresentationDisplay() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                return display;
            }
        }
        return null;
    }
    private void navigateToNextScreen() {
        // Start the next activity (e.g., main activity)
        Intent intent = new Intent(this, SelectDevice.class);
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
