package com.accessa.ibora.sales.ticket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;

import com.accessa.ibora.product.menu.Product;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class TicketsFragment  extends Fragment {


    private boolean doubleBackToExitPressedOnce = false;
    private RecyclerView recyclerView;
    private ticketAdapter TicketAdapter;

    private ticketAdapter mAdapter;
    private TextView emptyView;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;

    private DatabaseHelper mDatabaseHelper;
    private TextView cashorNameTextView;
    private TextView cashorIdTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_fragment,container,false);
        // Set the screen orientation to landscape




        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabaseHelper = new DatabaseHelper(getContext());

        Cursor cursor = mDatabaseHelper.getAllItems();
        mAdapter = new ticketAdapter(getContext(), cursor);
        mRecyclerView.setAdapter(mAdapter);


// Get a reference to the AppCompatImageView
        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);

// Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

// Find the empty FrameLayout
        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);

        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
        String cashorName = getActivity().getIntent().getStringExtra("cashorName");
        String cashorId = getActivity().getIntent().getStringExtra("cashorId");




        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up the views and listeners here, after the view has been created
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mDatabaseHelper = new DatabaseHelper(requireContext());

        // ... Other view setup code

        // Set up the onBackPressedCallback
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Replace the code below with the intent to navigate to the login screen
                Intent intent = new Intent(requireContext(), login.class);
                startActivity(intent);
            }
        });
    }

    private void logout() {
        // Perform any necessary cleanup or logout actions here
        // For example, you can clear session data, close database connections, etc.

        // Redirect to the login activity
        Intent intent = new Intent(requireContext(), login.class);
        startActivity(intent);
        requireActivity().finish(); // Optional: Finish the current activity to prevent navigating back to it using the back button
    }



}
