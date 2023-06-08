package com.accessa.ibora.Admin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.accessa.ibora.company.InsertCompanyDataActivity;

import java.util.Locale;

public class SelectAdminOptions extends AppCompatActivity {
    Button buttonEng, buttonFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.select_admin_options);

        // Eng Registor  cashior
        buttonEng = findViewById(R.id.buttonRegistor);
        buttonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        // Fr company
        buttonFr = findViewById(R.id.buttonCompany);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                openNewActivityAdmin();
            }
        });

        // Set button labels from French string resources
        updateButtonLabels();

    }

    public void openNewActivity() {
        Intent intent = new Intent(this, RegistorCashor.class);
        startActivity(intent);
    }

    public void openNewActivityAdmin() {
        Intent intent = new Intent(this, InsertCompanyDataActivity.class);
        startActivity(intent);
    }

    private void updateButtonLabels() {
        Configuration configuration = getResources().getConfiguration();
        Locale currentLocale = configuration.locale;

        Resources resources = getResources();
        if (currentLocale.getLanguage().equals("fr")) {
            buttonEng.setText(resources.getString(R.string.RegistorUser));
            buttonFr.setText(resources.getString(R.string.EditComapanyInfo));
        } else {
            buttonEng.setText(resources.getString(R.string.RegistorUser));
            buttonFr.setText(resources.getString(R.string.EditComapanyInfo));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Update button labels when the configuration (e.g., language) changes
        updateButtonLabels();
    }
}
