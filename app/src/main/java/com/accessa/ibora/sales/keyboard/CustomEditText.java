package com.accessa.ibora.sales.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        // Call the super method to handle the click event
        boolean handled = super.performClick();

        // Perform any additional actions you want when the view is clicked

        return handled;
    }
}
