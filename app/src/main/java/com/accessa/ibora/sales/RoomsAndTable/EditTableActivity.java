package com.accessa.ibora.sales.RoomsAndTable;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Rooms.Table;
import com.accessa.ibora.product.items.DatabaseHelper;

public class EditTableActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private String roomId;
    private int tableNumber;

    private EditText editTextSeatCount;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table);

        mDatabaseHelper = new DatabaseHelper(this);

        editTextSeatCount = findViewById(R.id.editTextSeatCount);
        buttonSave = findViewById(R.id.buttonSave);

        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("roomId") && intent.hasExtra("tableNumber")) {
            roomId = intent.getStringExtra("roomId");
            tableNumber = intent.getIntExtra("tableNumber", -1);

            // Retrieve existing table details and populate the UI
            Table existingTable = mDatabaseHelper.getTableByRoomAndNumber(roomId, tableNumber);
            if (existingTable != null) {
                editTextSeatCount.setText(String.valueOf(existingTable.getSeatCount()));
            } else {
                // Handle the case where the table data is not available
                // For example, you might show a message or set a default value
                editTextSeatCount.setText("0");
            }
        }

        // Set a click listener for the save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEditedTableDetails();
            }
        });
    }

    // Method to save the edited table details
    private void saveEditedTableDetails() {
        // Retrieve the new seat count from the EditText
        int newSeatCount;
        try {
            newSeatCount = Integer.parseInt(editTextSeatCount.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Seat Count", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the seat count in the database
        boolean updateSuccess = mDatabaseHelper.updateSeatCount(roomId, tableNumber, newSeatCount);

        if (updateSuccess) {
            Toast.makeText(this, "Seat count updated successfully", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity after updating
        } else {
            Toast.makeText(this, tableNumber, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Failed to update seat count", Toast.LENGTH_SHORT).show();
        }
    }
}


