package com.accessa.ibora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ForceRestart {

    private static final String PREFS_NAME = "AppSessionPrefs";
    private static final String KEY_SAME_SESSION = "IsSameSession";
    public static void restartApp(Context context) {
        // Store a flag in SharedPreferences to indicate the same session


        // Restart the app
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        System.exit(0); // Kill the current process
    }

    // Method to check if it's the same session


    // Method to clear the session flag
    public static void clearSessionFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SAME_SESSION, false);
        editor.apply(); // Save changes asynchronously
    }
}
