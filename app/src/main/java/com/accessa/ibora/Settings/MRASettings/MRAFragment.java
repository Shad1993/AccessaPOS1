package com.accessa.ibora.Settings.MRASettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MRA.ItemData;
import com.accessa.ibora.MRA.MRABULKActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptAdapter;
import com.accessa.ibora.Receipt.ReceiptBodyFragment;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MRAFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private DatabaseHelper mDatabaseHelper;
    private String cashorId,cashorName,Shopname,cashorlevel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mra_screen_control, container, false);

        mDatabaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        Shopname = sharedPreferences.getString("ShopName", null); // Retrieve company name



        // Register all the UI components with their appropriate IDs
        TextView Cashiername=view.findViewById(R.id.textView2);
        TextView ShopName=view.findViewById(R.id.textView3);
        ImageButton refresher = view.findViewById(R.id.refresh);
        ImageButton profileB = view.findViewById(R.id.profileB);
        Button allReceipt = view.findViewById(R.id.allreceipt);
        CardView SendBulk = view.findViewById(R.id.sendBulkCard);
        CardView failedreceipt = view.findViewById(R.id.failedreceipt);
        CardView sucessreceipt = view.findViewById(R.id.sucessreceipt);
        Cashiername.setText(cashorName);
        ShopName.setText(Shopname);
// Assuming you have a reference to your NestedScrollView


        Cursor receiptCursor = mDatabaseHelper.getAllReceipt();


// Initialize RecyclerView and set its adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        MRAAdaptor adapter = new MRAAdaptor(receiptCursor); // Replace with your Cursor
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerDepartmentClickListener(getContext(), recyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView deptNameEditText = view.findViewById(R.id.name_text_view);
                        TextView QrEditText = view.findViewById(R.id.Available_text_view);
                        CheckBox checkBox = view.findViewById(R.id.myCheckBox); // Find the CheckBox

                        String id1 = idTextView.getText().toString();
                        String id = idTextView.getText().toString();
                        String name = deptNameEditText.getText().toString();
                        String deptCode = QrEditText.getText().toString();

                        // Toggle the CheckBox's checked state
                        checkBox.setChecked(!checkBox.isChecked());
                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );

        // Handle UI component clicks
        refresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh data here, for example, by querying the database again
                Cursor refreshedCursor = mDatabaseHelper.getAllReceipt();

                // Update the RecyclerView adapter with the new data
                adapter.updateData(refreshedCursor);

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();

                // Show a toast to indicate that the refresh has occurred
                Toast.makeText(getActivity(), "Fragment Refreshed", Toast.LENGTH_SHORT).show();
            }
        });




        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Profile Image", Toast.LENGTH_SHORT).show();
            }
        });

        allReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and execute a new cursor query to get new data

                Cursor allreceipt  = mDatabaseHelper.getAllReceipt();
                // Update the adapter with the new cursor data
                adapter.updateData(allreceipt);
            }
        });



        SendBulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a list to store selected items
                List<ItemData> selectedItems = new ArrayList<>();

                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View view = recyclerView.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.myCheckBox);

                    if (checkBox.isChecked()) {
                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView deptNameEditText = view.findViewById(R.id.name_text_view);
                        String id = idTextView.getText().toString();
                        String name = deptNameEditText.getText().toString();

                        // Add the selected item to the list
                        selectedItems.add(new ItemData(id, name));
                    }
                }

                if (!selectedItems.isEmpty()) {
                    // Create an Intent to start the other activity
                    Intent intent = new Intent(getActivity(), MRABULKActivity.class);

                    // Pass the list of selected items as a Serializable extra
                    intent.putExtra("selectedItems", (Serializable) selectedItems);

                    // Start the other activity
                    startActivity(intent);
                } else {
                    // Handle the case where no item is selected
                    Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_SHORT).show();
                }
            }
        });



        failedreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor newCursor  = mDatabaseHelper.getAllReceiptwithoutQR();
                // Update the adapter with the new cursor data
                adapter.updateData(newCursor);
            }
        });

        sucessreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor newCursor1  = mDatabaseHelper.getAllReceiptWithQR();
                // Update the adapter with the new cursor data
                adapter.updateData(newCursor1);
            }
        });



        return view;
    }
}
