package com.accessa.ibora.Admin.CompanyInfo.FormPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.accessa.ibora.R;

public class ThirdSlideFragment extends Fragment {

    private EditText editADR1;
    private EditText editADR2;
    private EditText editADR3;
    private EditText editTelNo;
    private EditText editFaxNo;

    public ThirdSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_slide, container, false);

        editADR1 = view.findViewById(R.id.editADR1);
        editADR2 = view.findViewById(R.id.editADR2);
        editADR3 = view.findViewById(R.id.editADR3);
        editTelNo = view.findViewById(R.id.editTelNo);
        editFaxNo = view.findViewById(R.id.editFaxNo);

        return view;
    }

    public String getADR1() {
        return editADR1.getText().toString();
    }

    public String getADR2() {
        return editADR2.getText().toString();
    }

    public String getADR3() {
        return editADR3.getText().toString();
    }

    public String getTelNo() {
        return editTelNo.getText().toString();
    }

    public String getFaxNo() {
        return editFaxNo.getText().toString();
    }
}

