package com.accessa.ibora.product.category;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.accessa.ibora.R;
import com.accessa.ibora.product.menu.Product;

import top.defaults.colorpicker.ColorPickerPopup;

public class ModifyCategoryActivity extends Activity {

    private EditText CatNameText;
    private Button buttonupdate, buttondelete;
    private EditText ColorText;

    private long _id;

    private Cursor CursorUpdate;
    private CategoryDBManager CatdbManager;

    private CategoryDatabaseHelper mDatabaseHelper;

    private ImageView imageView;


    private Button  mPickColorButton;

    private View mColorPreview;

    private int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Category");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.modify_category);

        CatdbManager = new CategoryDBManager(this);
        CatdbManager.open();

        CatNameText = (EditText) findViewById(R.id.CatName_edittext);
        ColorText = (EditText) findViewById(R.id.Color_edittext);



        mPickColorButton = findViewById(R.id.pick_color_button);


        mColorPreview = findViewById(R.id.preview_selected_color);

        mDefaultColor = 0;

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String CatName = intent.getStringExtra("CatName");
        String colorValue = intent.getStringExtra("Color");
        mDefaultColor = Color.parseColor(colorValue);
        _id = Long.parseLong(id);
        CatNameText.setText(CatName);
        ColorText.setText(colorValue);
        mColorPreview.setBackgroundColor(mDefaultColor);
        mPickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ColorPickerPopup.Builder(ModifyCategoryActivity.this)
                        .initialColor(android.graphics.Color.RED)
                        .enableBrightness(true)
                        .enableAlpha(true)
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                mDefaultColor = color;
                                mColorPreview.setBackgroundColor(mDefaultColor);

                                String selectedColor = String.format("#%06X", (0xFFFFFF & mDefaultColor));
                                ColorText.setText(selectedColor);
                            }
                        });
            }
        });



        buttonupdate = (Button) findViewById(R.id.btn_update);
        buttonupdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivity();
            }
        });

        buttondelete = (Button) findViewById(R.id.btn_delete);
        buttondelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivityDel();
            }
        });
    }

    public void openNewActivity() {
        String CatName = CatNameText.getText().toString();
        String colorValue = ColorText.getText().toString();

        Toast.makeText(getApplicationContext(), "Category MODIFIED", Toast.LENGTH_LONG).show();
        CatdbManager.update(_id, CatName, colorValue);
        returnHome();
    }

    public void openNewActivityDel() {
        CatdbManager.delete(_id);
        returnHome();
        Toast.makeText(getApplicationContext(), "Category Deleted", Toast.LENGTH_LONG).show();
    }

    public void returnHome() {
        Intent home_intent1 = new Intent(getApplicationContext(), Product.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Category_fragment");
        startActivity(home_intent1);
    }
}
