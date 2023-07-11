package com.accessa.ibora.Admin.CompanyInfo.FormPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.accessa.ibora.R;
import com.bumptech.glide.Glide;

public class SecondSlideFragment extends Fragment {

    private EditText editVATNo;
    private EditText editBRNNo;

    public SecondSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second_slide, container, false);

        editVATNo = view.findViewById(R.id.editVATNo);
        editBRNNo = view.findViewById(R.id.editBRNNo);
        AppCompatImageView gifImageView = view.findViewById(R.id.gif_image_view);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.letter)
                .into(gifImageView);
        return view;
    }

    public String getVATNo() {
        return editVATNo.getText().toString();
    }

    public String getBRNNo() {
        return editBRNNo.getText().toString();
    }
}

