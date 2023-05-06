package com.accessa.ibora.product.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.accessa.ibora.R;


// main activity (FragmentActivity provides fragment compatibility pre-HC)
public class Product  extends FragmentActivity implements MenuFragment.OnMenufragListener {

    // called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

    }

    // MenuFragment listener
    @Override
    public void onMenufrag(String s) {

        // get body fragment (native method is getFragmentManager)
        BodyFragment fragment1 = (BodyFragment) getSupportFragmentManager().findFragmentById(R.id.bodyFragment);

        // if fragment is not null and in layout, set text, else launch BodyActivity
        if ((fragment1!=null)&&fragment1.isInLayout()) {
            fragment1.setText(s);
        } else {
            Intent intent = new Intent(this, BodyActivity.class);
            intent.putExtra("value",s);
            startActivity(intent);
        }

    }

    //extended from compatibility Fragment for pre-HC fragment support

}