package com.accessa.ibora.QR;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.ItemAdapter;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.bumptech.glide.Glide;


public class QRFragment extends Fragment {
    public interface DataPassListener {
        void onDataPass(String name, String id, String QR);
    }
    private DataPassListener dataPassListener;
    private RecyclerView mRecyclerView;
    private QRGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierId;
    private DBManager dbManager;
    public TextView nameTextView;
    public TextView idTextView;

    private static final String TRANSACTION_ID_KEY = "transaction_id";

    private  String transactionIdInProgress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_fragment, container, false);


        int numberOfColumns = 3;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);



        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        dbManager = new DBManager(getContext());
        dbManager.open();


        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor cursor = mDatabaseHelper.getAllQR();

        mAdapter = new QRGridAdapter(getContext(), cursor);
        mRecyclerView.setAdapter(mAdapter);

        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(this)
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idTextView = view.findViewById(R.id.id_text_view);
                TextView NameEditText = view.findViewById(R.id.name_text_view);
                TextView QrEditText = view.findViewById(R.id.code);



                String id1 = idTextView.getText().toString();
                String id = idTextView.getText().toString();
                String QR = QrEditText.getText().toString();
                String name = NameEditText.getText().toString();

                dataPassListener.onDataPass(name, id, QR);


            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click
                // ...
            }
        }));

        return view;

    }


    private void showItemDialog(String searchText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the custom dialog layout
        View dialogView = inflater.inflate(R.layout.search_by_barcode, null);
        builder.setView(dialogView);

        // Find the RecyclerView in the dialog layout
        RecyclerView dialogRecyclerView = dialogView.findViewById(R.id.dialog_recycler_view);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get the item data for the RecyclerView
        Cursor itemCursor = mDatabaseHelper.searchqr(searchText);
        ItemAdapter dialogAdapter = new ItemAdapter(getActivity(), itemCursor);
        dialogRecyclerView.setAdapter(dialogAdapter);

        // Set the OnItemTouchListener on the RecyclerView{
        dialogRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), dialogRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {



                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Handle long item click
                        // ...
                    }
                }));

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataPassListener = (DataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataPassListener");
        }
    }




}