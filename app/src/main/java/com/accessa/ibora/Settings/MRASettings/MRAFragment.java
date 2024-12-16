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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.MRA.ItemData;
import com.accessa.ibora.MRA.MRABULKActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MRAFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences usersharedPreferences;

    private List<String> selectedTransactionIds = new ArrayList<>();

    private String cashorId,cashorName,Shopname,cashorlevel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mra_screen_control, container, false);

        mDatabaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        usersharedPreferences = getActivity().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

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
        Button editsettings = view.findViewById(R.id.editsettings);
        CardView SendBulk = view.findViewById(R.id.sendBulkCard);
        CardView failedreceipt = view.findViewById(R.id.failedreceipt);
        CardView sucessreceipt = view.findViewById(R.id.sucessreceipt);
        Cashiername.setText(cashorName);
        ShopName.setText(Shopname);
// Assuming you have a reference to your NestedScrollView


        Cursor receiptCursor = mDatabaseHelper.getAllReceipt();
        editsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int levelNumber = Integer.parseInt(cashorlevel);
                SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                String Activity="editMraSettings_";
                SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                if (canHigherAccessSyncDatabase) {
                    showPinDialog(Activity, () -> {
                        showEditSettingsDialog();

                    });
                } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                    showEditSettingsDialog();

                } else{
                    Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                }

            }
        });

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
                        CheckBox checkBox = view.findViewById(R.id.myCheckBox);
                        String id = idTextView.getText().toString();
                        String name = deptNameEditText.getText().toString();
                        // Toggle the CheckBox's checked state
                        boolean isChecked = !checkBox.isChecked();
                        checkBox.setChecked(isChecked);

                        if (isChecked) {
                            // Add the transaction ID to the list
                            selectedTransactionIds.add(name);
                        } else {
                            // Remove the transaction ID from the list
                            selectedTransactionIds.remove(name);
                        }
                        saveSelectedTransactionIds();

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
    private void showPinDialog(String activity, Runnable onSuccessAction) {
        // Inflate the PIN dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View pinDialogView = inflater.inflate(R.layout.pin_dialog, null);
        EditText pinEditText = pinDialogView.findViewById(R.id.editTextPIN);

        // Find buttons
        Button buttonClear = pinDialogView.findViewById(R.id.buttonClear);
        Button buttonLogin = pinDialogView.findViewById(R.id.buttonLogin);

        // Set up button click listeners
        setPinButtonClickListeners(pinDialogView, pinEditText);

        // Create the PIN dialog
        AlertDialog.Builder pinBuilder = new AlertDialog.Builder(getContext());
        pinBuilder.setTitle("Enter PIN")
                .setView(pinDialogView);
        AlertDialog pinDialog = pinBuilder.create();
        pinDialog.show();

        // Clear button functionality
        buttonClear.setOnClickListener(v -> onpinClearButtonClick(pinEditText));

        // Login button functionality
        buttonLogin.setOnClickListener(v -> {
            String enteredPIN = pinEditText.getText().toString();
            int cashorLevel = validatePIN(enteredPIN);

            if (cashorLevel != -1) { // PIN is valid
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);

                // Check if the user has permission
                boolean accessAllowed = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, activity, cashorLevel);
                if (accessAllowed) {
                    String cashorName =mDatabaseHelper.getCashorNameByPin(enteredPIN);
                    int cashorId =mDatabaseHelper.getCashorIdByPin(enteredPIN);
                    mDatabaseHelper.logUserActivity(cashorId, cashorName, cashorLevel, activity);
                    onSuccessAction.run(); // Execute the provided action on success
                    pinDialog.dismiss(); // Dismiss the PIN dialog after successful login
                } else {
                    showPermissionDeniedDialog(); // Show a permission denied dialog
                }
            } else {
                Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Permission Denied")
                .setMessage("You do not have permission to access this feature.")
                .setPositiveButton("OK", null)
                .show();
    }


    public void onPinNumberButtonClick(Button button, EditText pinEditText) {
        if (pinEditText != null) {
            String buttonText = button.getText().toString();

            switch (buttonText) {
                case "Clear": // Handle clear
                    pinEditText.setText("");
                    break;
                case "BS": // Handle backspace
                    CharSequence currentText = pinEditText.getText();
                    if (currentText.length() > 0) {
                        pinEditText.setText(currentText.subSequence(0, currentText.length() - 1));
                    }
                    break;
                default: // Handle numbers
                    pinEditText.append(buttonText);
                    break;
            }
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "EditText is not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPinButtonClickListeners(View pinDialogView, final EditText pinEditText) {
        int[] buttonIds = new int[] {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonClear
        };

        for (int id : buttonIds) {
            Button button = pinDialogView.findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPinNumberButtonClick((Button) v, pinEditText);
                }
            });
        }
    }
    private int validatePIN(String enteredPIN) {
        // Fetch the cashor level based on the entered PIN
        int cashorLevel = mDatabaseHelper.getCashorLevelByPIN(enteredPIN);

        // Return the cashor level if valid, or -1 if invalid
        return cashorLevel;
    }
    public void onpinClearButtonClick(EditText ReceivedEditText) {

        onclearButtonClick(ReceivedEditText);
        onPinclearButtonClick(ReceivedEditText);


    }
    private void onPinclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");

        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    private void onclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");
            // ReceivedEditText.setText("");
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
    }
    private void showEditSettingsDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_settings, null);

        // Create and set up the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        // Find views in the dialog
        EditText editText1 = dialogView.findViewById(R.id.editText1);
        EditText editText2 = dialogView.findViewById(R.id.editText2);
        EditText editText3 = dialogView.findViewById(R.id.editText3);
        EditText editText4 = dialogView.findViewById(R.id.editText4);
        Button editButton = dialogView.findViewById(R.id.editButton);

        // Retrieve the stored values from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("mraparams", Context.MODE_PRIVATE);
        String userNamemra = prefs.getString("User_Name", "");
        String ebsMraIdmra = prefs.getString("ebsMraId", "");
        String areaCodemra = prefs.getString("Area_Code", "");
        String passwordmra = prefs.getString("Password", "");

        // Set the retrieved values into the EditText fields
        editText1.setText(userNamemra);
        editText2.setText(ebsMraIdmra);
        editText3.setText(areaCodemra);
        editText4.setText(passwordmra);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Set up the edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input data from the EditTexts
                String userName = editText1.getText().toString();
                String ebsMraId = editText2.getText().toString();
                String areaCode = editText3.getText().toString();
                String password = editText4.getText().toString();

                // Check if area code is a valid integer
                try {
                    int areaCodeInt = Integer.parseInt(areaCode);
                    // Save data to SharedPreferences if area code is valid
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("User_Name", userName);
                    editor.putString("ebsMraId", ebsMraId);
                    editor.putString("Area_Code", areaCode); // Storing as string to retain leading zeros if any
                    editor.putString("Password", password);
                    editor.apply();

                    // Show confirmation Toast
                    Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show();

                    // Dismiss the dialog
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    // Show error if area code is not a valid integer
                    editText3.setError("Please enter a valid integer for Area Code");
                }
            }
        });

        // Show the dialog
        dialog.show();
    }



    private void saveSelectedTransactionIds() {
        // Convert the list to a JSON string
        Gson gson = new Gson();
        String selectedIdsJson = gson.toJson(selectedTransactionIds);

        // Save the JSON string in SharedPreferences
        SharedPreferences.Editor editor = getContext().getSharedPreferences("idtoBulk", Context.MODE_PRIVATE).edit();
        editor.putString("selectedTransactionIds", selectedIdsJson);
        editor.apply();
    }
}
