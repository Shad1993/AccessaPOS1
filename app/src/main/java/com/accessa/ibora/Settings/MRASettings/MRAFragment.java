package com.accessa.ibora.Settings.MRASettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.accessa.ibora.R;
import com.accessa.ibora.Receipt.ReceiptAdapter;
import com.accessa.ibora.Receipt.ReceiptBodyFragment;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;

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
        ImageButton backB = view.findViewById(R.id.backB);
        ImageButton logOutB = view.findViewById(R.id.logOutB);
        ImageButton profileB = view.findViewById(R.id.profileB);
        Button todoB = view.findViewById(R.id.todoB);
        Button editProfileB = view.findViewById(R.id.editProfileB);
        CardView contributeCard = view.findViewById(R.id.contributeCard);
        CardView practiceCard = view.findViewById(R.id.practiceCard);
        CardView learnCard = view.findViewById(R.id.learnCard);
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
                        TextView deptCodeEditText = view.findViewById(R.id.total_text_view);
                        TextView lastModifiedTextView = view.findViewById(R.id.transdate_edittex);

                        String id1 = idTextView.getText().toString();
                        String id = idTextView.getText().toString();
                        String name = deptNameEditText.getText().toString();
                        String deptCode = deptCodeEditText.getText().toString();


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );

        // Handle UI component clicks
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Back Button", Toast.LENGTH_SHORT).show();
            }
        });

        logOutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logout Button", Toast.LENGTH_SHORT).show();
            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Profile Image", Toast.LENGTH_SHORT).show();
            }
        });

        todoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and execute a new cursor query to get new data

                Cursor newCursor  = mDatabaseHelper.getAllReceiptwithoutQR();
                // Update the adapter with the new cursor data
                adapter.updateData(newCursor);
            }
        });

        editProfileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Editing Profile", Toast.LENGTH_SHORT).show();
            }
        });

        contributeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Contribute Articles", Toast.LENGTH_SHORT).show();
            }
        });

        practiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Practice Programming", Toast.LENGTH_SHORT).show();
            }
        });

        learnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor newCursor  = mDatabaseHelper.getAllItems();
                // Update the adapter with the new cursor data
                adapter.updateData(newCursor);
            }
        });



        return view;
    }
}
