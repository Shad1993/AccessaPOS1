package com.accessa.ibora.POP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.accessa.ibora.R;

public class MyCustomDialogFragment extends DialogFragment {

    private static final String ARG_API_REQUEST = "api_request";

    public static MyCustomDialogFragment newInstance(String apiRequest) {
        MyCustomDialogFragment fragment = new MyCustomDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_API_REQUEST, apiRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String apiRequest = getArguments().getString(ARG_API_REQUEST);

        // Create a custom dialog with your desired layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pop, null); // Use the custom_dialog_layout.xml here
        builder.setView(view);

        // You can access your views and set data here if needed
        // For example, if you have a TextView with id "textView" in custom_dialog_layout.xml:
        // TextView textView = view.findViewById(R.id.textView);
        // textView.setText(apiRequest);

        return builder.create();
    }
}
