package com.accessa.ibora.Settings.Rooms;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DatabaseHelper;

public class EditRoomDialogFragment extends DialogFragment {

    private static final String ARG_ROOM_ID = "roomId";
    private static final String ARG_ROOM_NAME = "roomName";

    // Add the new arguments for TABLE_NUMBER, SEAT_COUNT, and STATUS
    private static final String ARG_TABLE_NUMBER = "tableNumber";
    private static final String ARG_SEAT_COUNT = "seatCount";
    private static final String ARG_STATUS = "status";

    private String roomId;
    private String roomName;

    // Add the new fields for TABLE_NUMBER, SEAT_COUNT, and STATUS
    private int tableNumber;
    private int seatCount;
    private String status;

    private EditText roomNameEditText;
    private EditText tableNumberEditText;
    private EditText seatCountEditText;
    private Spinner statusSpinner;

    public static EditRoomDialogFragment newInstance(String roomId) {
        EditRoomDialogFragment fragment = new EditRoomDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_ID, roomId);
       
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            roomId = getArguments().getString(ARG_ROOM_ID);

            // Retrieve additional details from the database based on room id
            DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
            Rooms roomDetails = databaseHelper.getRoomDetails(roomId);

            if (roomDetails != null) {
                seatCount = roomDetails.getTableCount();
                status = roomDetails.getStatus();
            } else {
                // Handle the case when room details are not found
                // Log an error, show a message, or handle it appropriately
                return super.onCreateDialog(savedInstanceState);
            }
        } else {
            // Handle the case when arguments are null
            // Log an error, show a message, or handle it appropriately
            return super.onCreateDialog(savedInstanceState);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_room, null);

        roomNameEditText = view.findViewById(R.id.editRoomName);
        tableNumberEditText = view.findViewById(R.id.editTableNumber);
        seatCountEditText = view.findViewById(R.id.editSeatCount);
        statusSpinner = view.findViewById(R.id.editStatusSpinner);

        roomNameEditText.setText(roomName);
        tableNumberEditText.setText(String.valueOf(tableNumber));
        seatCountEditText.setText(String.valueOf(seatCount));
        // Set the selected item in the status spinner
        int statusPosition = status.equals("reserved") ? 0 : 1;
        statusSpinner.setSelection(statusPosition);

        builder.setView(view)
                .setTitle("Edit Room")
                .setPositiveButton("Save", (dialog, which) -> {
                    // Handle save button click
                    String newRoomName = roomNameEditText.getText().toString();
                    int newTableNumber = Integer.parseInt(tableNumberEditText.getText().toString());
                    int newSeatCount = Integer.parseInt(seatCountEditText.getText().toString());
                    String newStatus = statusSpinner.getSelectedItem().toString();

                    // Update the room details in the database
                    DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
                    databaseHelper.updateRoomDetails(roomId, newRoomName, newTableNumber, newSeatCount, newStatus);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel button click
                    dialog.cancel();
                });

        return builder.create();
    }
}

