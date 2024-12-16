package com.accessa.ibora.Settings.Rooms;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;


public class EditRoomActivity extends AppCompatActivity {

    private static final String EXTRA_ROOM_ID = "roomId";

    private EditText roomNameEditText;
    private EditText tableNumberEditText;
    private EditText seatCountEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        roomNameEditText = findViewById(R.id.editRoomName);
        seatCountEditText = findViewById(R.id.editSeatCount);

        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra("ROOM_ID");
        Log.d("roomId", roomId);

        if (roomId != null) {
            // Fetch room details from the database
            Rooms roomDetails = databaseHelper.getRoomDetails(roomId);

            if (roomDetails != null) {
                // Populate the fields with existing room data
                roomNameEditText.setText(roomDetails.getRoomName());
                seatCountEditText.setText(String.valueOf(roomDetails.getTableCount()));

            }
        }

        saveButton.setOnClickListener(v -> {
            // Handle save button click
            String newRoomName = roomNameEditText.getText().toString();
            String seatCountText = seatCountEditText.getText().toString();

            // Validate that seat count is a valid integer
            int seatcountNumber;
            try {
                seatcountNumber = Integer.parseInt(seatCountText);
            } catch (NumberFormatException e) {
                // Show an error if seat count is not a valid integer
                seatCountEditText.setError("Please enter a valid integer for seat count");
                return;
            }


            if (roomId != null) {
                // Update the room details in the database
                boolean updated = databaseHelper.updateRoomDetails(Integer.parseInt(roomId), newRoomName, seatcountNumber);

                if (updated) {
                    Toast.makeText(this, "Room updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update room", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancelButton.setOnClickListener(v -> {
            // Handle cancel button click
            finish();
        });
    }

    public static Intent newIntent(Context context, String roomId) {
        Intent intent = new Intent(context, EditRoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }
}

