package com.accessa.ibora.Receipt;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.Settings.QRMethods.QrAdaptor;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmptyReceiptBodyFragment extends Fragment {
    private  EditText searchEditText;
    FloatingActionButton mAddFab;
    private SearchView mSearchView;
    private DBManager dbManager;
    private QrAdaptor mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;

    final String[] froms = new String[]{DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.LongDescription, DatabaseHelper.Price};
    final int[] tos = new int[]{R.id.id, R.id.name, R.id.LongDescription, R.id.price};

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_blank, container, false);




        // Empty state
        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif()
                .load(R.drawable.receiptgif)
                .into(imageView);
        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);



            emptyFrameLayout.setVisibility(View.VISIBLE);







        return view;
    }






}
