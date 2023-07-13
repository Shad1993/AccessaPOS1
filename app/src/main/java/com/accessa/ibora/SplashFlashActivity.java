package com.accessa.ibora;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;

public class SplashFlashActivity extends Activity {

    private static final long SPLASH_DURATION = 1000; // Splash screen duration in milliseconds
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        // Inflate the custom layout for the dialog
        builder.setView(R.layout.splashflash);

        // Set a positive button to do nothing (disable back button press)
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {





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
        });

        // Show the AlertDialog
        dialog.show();
    }


    private void navigateToNextScreen() {
        // Start the next activity (e.g., main activity)
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(navigateToNextScreenRunnable);
    }
}
