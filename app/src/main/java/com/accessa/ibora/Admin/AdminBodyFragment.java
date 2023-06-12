package com.accessa.ibora.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.accessa.ibora.R;

public class AdminBodyFragment extends Fragment {

    private boolean doubleBackToExitPressedOnce = false;

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_body, container, false);
        return view;
    }

    // set text
    public void setText(String item) {
        TextView view = getView().findViewById(R.id.detailsText);
        view.setText(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
    }

    // Handle back button press

}
