package com.accessa.ibora;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.accessa.ibora.SelectProfile;
import com.accessa.ibora.login.RegistorCashor;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.menu.Product;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {
    Button buttonEng, buttonFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.selectlanguage);

        // Eng language
        buttonEng = findViewById(R.id.buttonEng);
        buttonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Locale.ENGLISH);
            }
        });

        // Fr language
        buttonFr = findViewById(R.id.buttonFr);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Locale.FRENCH);
            }
        });
    }

    public void openNewActivity(Locale locale) {
        // Set the app's locale to the selected language
        Locale.setDefault(locale);

        // Update the configuration to reflect the new locale
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Start the SelectProfile activity
        Intent intent = new Intent(this, SelectProfile.class);
        startActivity(intent);
    }
}
