package com.accessa.ibora.Admin.CompanyInfo.FormPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.accessa.ibora.R;
import com.bumptech.glide.Glide;

public class ForthSlideFragment extends Fragment {

    private EditText editADR1;
    private EditText editADR2;
    private EditText editADR3;
    private EditText editTelNo;
    private EditText editFaxNo;

    public ForthSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forth_slide, container, false);

        editADR1 = view.findViewById(R.id.editADR1);
        editADR2 = view.findViewById(R.id.editADR2);
        editADR3 = view.findViewById(R.id.editADR3);
        editTelNo = view.findViewById(R.id.editTelNo);
        editFaxNo = view.findViewById(R.id.editFaxNo);
        // Get a reference to the AppCompatImageView
        AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.location)
                .into(gifImageView);

        return view;
    }

    public String getCompADR1() {
        return editADR1.getText().toString();
    }

    public String getCompADR2() {
        return editADR2.getText().toString();
    }

    public String getCompADR3() {
        return editADR3.getText().toString();
    }

    public String getCompTelNo() {
        return editTelNo.getText().toString();
    }

    public String getCompFaxNo() {
        return editFaxNo.getText().toString();
    }
}

