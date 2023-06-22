package com.accessa.ibora.Settings;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.accessa.ibora.Admin.AdminBodyFragment;
import com.accessa.ibora.R;


//created only when in portrait mode (FragmentActivity provides fragment compatibility pre-HC)
public class SettingsBodyActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check orientation to avoid crash (this activity is not necessary in landscape)
        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        } else setContentView(R.layout.ad_ac_body);

        // show body content as requested in Intent extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get data from Intent extra
            String s = extras.getString("value");
            // get body fragment
            AdminBodyFragment fragment = (AdminBodyFragment) getSupportFragmentManager().findFragmentById(R.id.bodyFragment);
            // if fragment is not null and in layout set text
            if ((fragment!=null)&&fragment.isInLayout()) {

                fragment.setText(s);
            }
        }

    }

}
