package com.accessa.ibora.sales;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.accessa.ibora.Constants;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.scanner.USBScanner;
import com.accessa.ibora.scanner.inbuildScannerSunmiT2Mini;

public class keyboardFragment extends Fragment {
    private AlertDialog alertDialog;
    private EditText editTextBarcode;
    private StringBuilder enteredBarcode;

    private SQLiteDatabase database;
    String dbName = Constants.DB_NAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyboard_fragment, container, false);


        // Set the screen orientation to landscape
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initialize views
        editTextBarcode = view.findViewById(R.id.editTextBarcode);

        // Initialize the StringBuilder for entered PIN
        enteredBarcode = new StringBuilder();

        // Find the number buttons and set OnClickListener
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        Button button3 = view.findViewById(R.id.button3);
        Button button4 = view.findViewById(R.id.button4);
        Button button5 = view.findViewById(R.id.button5);
        Button button6 = view.findViewById(R.id.button6);
        Button button7 = view.findViewById(R.id.button7);
        Button button8 = view.findViewById(R.id.button8);
        Button button9 = view.findViewById(R.id.button9);
        Button button0 = view.findViewById(R.id.button0);
        Button button00 = view.findViewById(R.id.button00);
        Button buttonComma = view.findViewById(R.id.buttonComma);
        Button buttonClear = view.findViewById(R.id.buttonClear);
        Button buttonEnter = view.findViewById(R.id.buttonEnter);
        Button buttonPrint = view.findViewById(R.id.buttonPrint);
        Button buttonQr = view.findViewById(R.id.buttonQr);


        // Set the focus to the EditText initially
        editTextBarcode.requestFocus();

        editTextBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // "Enter" key was pressed
                    // Handle the barcode data here
                    String barcode = editTextBarcode.getText().toString().trim();
                    Toast.makeText(getContext(), "Barcode : " + barcode, Toast.LENGTH_SHORT).show();

                    v.requestFocus();
                    if (!barcode.isEmpty()) {
                        // Store the barcode or perform desired action with it
                        // barcodeString = barcode;

                        // Clear the entered barcode and update the EditText
                        editTextBarcode.setText("");

                        // Set the focus back to the EditText for further scanning
                        editTextBarcode.requestFocus();

                        // Return true to indicate that the key press event has been handled
                        return true;
                    }
                }

                // Return false to allow the key press event to be handled by the system as well
                return false;
            }

        });


        // Set OnClickListener for number buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("0");
            }
        });

        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick("00");
            }
        });

        buttonComma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberButtonClick(",");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }        });

        // Set OnClickListener for other buttons
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnterButtonClick();
            }
        });

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the print button click
                // Implement the desired functionality here
                // Handle the print button click
                Intent intent = new Intent(getActivity(), printerSetup.class);
                startActivity(intent);
            }
        });

        buttonQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the QR button click
                // Implement the desired functionality here
                Intent intent = new Intent(getActivity(), inbuildScannerSunmiT2Mini.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void onEnterButtonClick() {
        String barcode = editTextBarcode.getText().toString().trim();
        Toast.makeText(getContext(), "Barcode : " + barcode, Toast.LENGTH_SHORT).show();
        // Clear the entered barcode and update the EditText
        editTextBarcode.setText("");
        editTextBarcode.requestFocus();
        // Set the focus back to the EditText for further scanning
        editTextBarcode.requestFocus();
        if (!barcode.isEmpty()) {
            // Store the barcode or perform desired action with it
            // barcodeString = barcode;

            // Clear the entered barcode and update the EditText
            editTextBarcode.setText("");

            // Set the focus back to the EditText for further scanning
            editTextBarcode.requestFocus();
    }
    }

    public void onNumberButtonClick(String number) {
        // Append the pressed number to the entered PIN
        enteredBarcode.append(number);

        // Update the PIN EditText with the entered numbers
        editTextBarcode.setText(enteredBarcode.toString());
    }
    public void onClearButtonClick(View view) {
        // Clear the entered PIN and update the PIN EditText
        enteredBarcode.setLength(0);
        editTextBarcode.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Dismiss the dialog if it is showing to prevent window leaks
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        // Close the database connection when the fragment is destroyed
        if (database != null) {
            database.close();
        }
    }
}
