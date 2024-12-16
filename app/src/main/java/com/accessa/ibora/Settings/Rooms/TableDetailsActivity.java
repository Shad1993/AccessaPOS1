package com.accessa.ibora.Settings.Rooms;

import com.accessa.ibora.Settings.Rooms.TableAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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




        int spanCount = 5; // Number of items to display in one line
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_between_rooms); // Set your desired spacing dimension
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Set the span size for items to be 1, so that each item takes one span
                return 1;
            }
        });
        mTableRecyclerView.setLayoutManager(layoutManager);

        // Add spacing item decoration
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(spanCount, spacing);
        mTableRecyclerView.addItemDecoration(itemDecoration);

        mTableRecyclerView.setLayoutManager(layoutManager);
        mTableRecyclerView.setAdapter(mTableAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("roomId") && intent.hasExtra("roomName")) {
             roomId = intent.getStringExtra("roomId");
            String roomName = intent.getStringExtra("roomName");

            // Set the room name to the TextView
            roomNameTextView.setText(roomName);
            // Fetch table details from the database
            List<Table> tables = mDatabaseHelper.getTablesForRoom(roomId);

            // Clear the existing list and add the fetched tables
            mTableList.clear();
            mTableList.addAll(tables);

            // Update the adapter to reflect the changes
            mTableAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onItemClick(Table table) {
// Get the table ID or any unique identifier

        showEditTableDialog(this, Integer.parseInt(roomId), table);
        // Handle the item click event, e.g., start an activity for editing table data
       // Intent editTableIntent = new Intent(this, EditTableActivity.class);
       // editTableIntent.putExtra("roomId", roomId);

        // Pass the position or index of the clicked item
      //  editTableIntent.putExtra("tableNumber", mTableList.indexOf(table) + 1);

       // startActivity(editTableIntent);
    }

    public void showEditTableDialog(Context context, final int roomId, final Table table) {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_table, null);

        // Initialize the UI components
        final EditText editSeatCount = dialogView.findViewById(R.id.edit_seat_count);
        final EditText editTableNumber = dialogView.findViewById(R.id.edit_table_name); // New field for table name

        final Spinner statusSpinner = dialogView.findViewById(R.id.status_spinner);
        Button btnUpdate = dialogView.findViewById(R.id.btn_update);

        int realtableidnum= table.getTableNumber();
        int seatcount= mDatabaseHelper.getSeatCount(roomId,realtableidnum);
        // Populate the existing data in the fields
        editSeatCount.setText(String.valueOf(seatcount));
        editTableNumber.setText(String.valueOf(table.getTableNumber())); // Fix: Convert int to String

        // Set up the status spinner with "reserved" and "not_reserved" values
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"reserved", "not_reserved"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Set the existing status in the spinner
        String currentStatus = table.getStatus();

        if (currentStatus.equals("reserved")) {
            statusSpinner.setSelection(0);
        } else {
            statusSpinner.setSelection(1);
        }

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setTitle("Edit Table")
                .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());

        // Create the dialog
        final AlertDialog dialog = builder.create(); // Declare it final so it can be accessed inside the button listener

        // Set the update button action
        btnUpdate.setOnClickListener(v -> {
            int seatCount = Integer.parseInt(editSeatCount.getText().toString());
            String status = statusSpinner.getSelectedItem().toString();
            String tableNumber = editTableNumber.getText().toString(); // Get the table name from the EditText

            // Update the table in the database
            mDatabaseHelper.updateTableData(roomId,tableNumber, realtableidnum, seatCount, status);
            Log.d("table update", "R " + roomId + " T: " + realtableidnum);

            // Dismiss the dialog
            dialog.dismiss(); // Use the final dialog variable to dismiss it
            
        });

        // Show the dialog
        dialog.show();
    }




}

