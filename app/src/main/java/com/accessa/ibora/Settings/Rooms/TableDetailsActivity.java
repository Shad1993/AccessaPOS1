package com.accessa.ibora.Settings.Rooms;

import com.accessa.ibora.Settings.Rooms.TableAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Rooms.Table;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.sales.RoomsAndTable.EditTableActivity;

import java.util.ArrayList;
import java.util.List;

public class TableDetailsActivity extends AppCompatActivity implements TableAdapter.OnItemClickListener{

    private DatabaseHelper mDatabaseHelper;
    private TextView roomNameTextView;

    private RecyclerView mTableRecyclerView;
    private TableAdapter mTableAdapter;
    private List<Table> mTableList;
    String   roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_details);
        mDatabaseHelper = new DatabaseHelper(this);  // Initialize your DatabaseHelper
        roomNameTextView = findViewById(R.id.roomNameTextView);

        mTableRecyclerView = findViewById(R.id.tableRecyclerView);
        mTableList = new ArrayList<>();
        mTableAdapter = new TableAdapter(mTableList,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTableRecyclerView.setLayoutManager(layoutManager);
        mTableRecyclerView.setAdapter(mTableAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("roomId") && intent.hasExtra("roomName")) {
             roomId = intent.getStringExtra("roomId");
            String roomName = intent.getStringExtra("roomName");

            // Set the room name to the TextView
            roomNameTextView.setText(roomName);
// Retrieve the table count for the given room ID (You need to implement this)
            int tableCount = mDatabaseHelper.getTableCountForRoom(roomId);

            // Add dummy data based on the actual table_count
            for (int i = 1; i <= tableCount; i++) {
                mTableList.add(new Table("Table " + i));
            }

            // Update the adapter to reflect the changes
            mTableAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onItemClick(Table table) {
        // Handle the item click event, e.g., start an activity for editing table data
        Intent editTableIntent = new Intent(this, EditTableActivity.class);
        editTableIntent.putExtra("roomId", roomId);

        // Pass the position or index of the clicked item
        editTableIntent.putExtra("tableNumber", mTableList.indexOf(table) + 1);

        startActivity(editTableIntent);
    }



}

