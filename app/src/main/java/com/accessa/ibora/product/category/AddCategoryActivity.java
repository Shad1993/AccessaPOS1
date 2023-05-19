package com.accessa.ibora.product.category;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import top.defaults.colorpicker.ColorPickerPopup;

import com.accessa.ibora.R;
import com.accessa.ibora.product.menu.Product;


public class AddCategoryActivity extends Activity {

    private Button addTodoBtn;
    private EditText CatNameEditText;
    private EditText ColorEditText;
    Button AddButton;
    private CategoryDBManager CatdbManager;


    // Inside your activity or fragment

    private Button colorPickerButton;



    // two buttons to open color picker dialog and one to
    // set the color for GFG text
    private Button mPickColorButton;

    // view box to preview the selected color
    private View mColorPreview;

    // this is the default color of the preview box
    private int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Add Category");

        setContentView(R.layout.addcategory);

        CatNameEditText = (EditText) findViewById(R.id.CatName_edittext);
        ColorEditText = (EditText) findViewById(R.id.Color_edittext);



        // register two of the buttons with their
        // appropriate IDs
        mPickColorButton = findViewById(R.id.pick_color_button);

        // and also register the view which shows the
        // preview of the color chosen by the user
        mColorPreview = findViewById(R.id.preview_selected_color);

        // set the default color to 0 as it is black
        mDefaultColor = 0;

        // handling the Pick Color Button to open color
        // picker dialog
        mPickColorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new ColorPickerPopup.Builder(AddCategoryActivity.this).initialColor(
                                        Color.RED) // set initial color
                                // of the color
                                // picker dialog
                                .enableBrightness(
                                        true) // enable color brightness
                                // slider or not
                                .enableAlpha(
                                        true) // enable color alpha
                                // changer on slider or
                                // not
                                .okTitle(
                                        "Choose") // this is top right
                                // Choose button
                                .cancelTitle(
                                        "Cancel") // this is top left
                                // Cancel button which
                                // closes the
                                .showIndicator(
                                        true) // this is the small box
                                // which shows the chosen
                                // color by user at the
                                // bottom of the cancel
                                // button
                                .showValue(
                                        true) // this is the value which
                                // shows the selected
                                // color hex code
                                // the above all values can be made
                                // false to disable them on the
                                // color picker dialog.
                                .build()
                                .show(
                                        v,
                                        new ColorPickerPopup.ColorPickerObserver() {
                                            @Override
                                            public void
                                            onColorPicked(int color) {
                                                // set the color
                                                // which is returned
                                                // by the color
                                                // picker
                                                mDefaultColor = color;

                                                // now as soon as
                                                // the dialog closes
                                                // set the preview
                                                // box to returned
                                                // color
                                                mColorPreview.setBackgroundColor(mDefaultColor);

                                                String selectedColor = String.format("#%06X", (0xFFFFFF & mDefaultColor));

                                                // Set the selected color code to the color EditText
                                                ColorEditText.setText(selectedColor);
                                            }
                                        });
                    }
                });

        // handling the Set Color button to set the selected
        // color for the GFG text.


        //Add Record
        addTodoBtn = (Button) findViewById(R.id.add_record);

        CatdbManager = new CategoryDBManager(this);
        CatdbManager.open();


        // Add Record
        AddButton = (Button) findViewById(R.id.add_record);
        AddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
    }



    public void openNewActivity() {
        final String CatName = CatNameEditText.getText().toString();
        final String Color = ColorEditText.getText().toString();
        if (CatName.isEmpty() ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        CatdbManager.insert(CatName, Color);

        // Create an Intent to navigate to the desired Fragment
        Intent intent = new Intent(AddCategoryActivity.this, Product.class);
        intent.putExtra("fragment", "Category_fragment");

        startActivity(intent);
    }



}
