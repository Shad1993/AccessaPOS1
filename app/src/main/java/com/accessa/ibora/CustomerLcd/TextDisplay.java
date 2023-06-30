package com.accessa.ibora.CustomerLcd;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.accessa.ibora.R;

public class TextDisplay extends Presentation {
    private static final String TAG = "TextDisplay";
    private Display[] displays;

    public TextDisplay(Context context, Display display) {
        super(context, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for the second screen
        setContentView(R.layout.print_layout); // Replace with your desired layout file
    }

    public static TextDisplay createInstance(Context context) {
        Display presentationDisplay = getPresentationDisplay(context);
        if (presentationDisplay != null) {
            return new TextDisplay(context, presentationDisplay);
        }
        return null;
    }

    private static Display getPresentationDisplay(Context context) {
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();
        for (Display display : displays) {
            Log.e(TAG, "Screen " + display);
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                Log.e(TAG, "First real second screen " + display);
                return display;
            }
        }
        return null;
    }


    public void updateText(String content) {
    }
}
