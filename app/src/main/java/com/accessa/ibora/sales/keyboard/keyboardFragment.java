package com.accessa.ibora.sales.keyboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.Constants;
import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.Functions.FunctionFragment;
import com.accessa.ibora.QR.QRFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.printerSetup;
import com.accessa.ibora.scanner.InbuiltScannerFragment;


public class keyboardFragment extends Fragment {
    private AlertDialog alertDialog;
    private SearchView searchView;
    private StringBuilder enteredBarcode;
    private CustomEditText editTextBarcode;
    private SQLiteDatabase database;
    String dbName = Constants.DB_NAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("device", Context.MODE_PRIVATE);
        String deviceType = sharedPreferences.getString("device_type", null);


        // Check the value and set the content view accordingly
        if ("mobile".equalsIgnoreCase(deviceType)) {
            view = inflater.inflate(R.layout.mobile_keyboard_fragment, container, false);
        } else if ("sunmiT2".equalsIgnoreCase(deviceType)) {
             view = inflater.inflate(R.layout.keyboard_fragment, container, false);
        }
        // Set the screen orientation to landscape
      //  requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initialize views

        editTextBarcode = view.findViewById(R.id.editTextBarcode);
        // Initialize the StringBuilder for entered PIN
        enteredBarcode = new StringBuilder();
        // Set the focus to the EditText initially
        editTextBarcode.requestFocus();
        // Keep focus on editTextBarcode
        editTextBarcode.setNextFocusDownId(R.id.editTextBarcode);


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
        Button buttonbackspace = view.findViewById(R.id.buttonbackspace);
        Button buttonComma = view.findViewById(R.id.buttonComma);
        Button buttonClear = view.findViewById(R.id.buttonClear);
        CardView buttonEnter = view.findViewById(R.id.buttonEnter);



        Button buttonfunction = view.findViewById(R.id.buttonFunctions);
        Button buttonQr = view.findViewById(R.id.buttonQr);

        EditText editTextSearch = view.findViewById(R.id.editTextBarcode);
        editTextBarcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextBarcode.getRight() - editTextBarcode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Search icon clicked, perform the search functionality
                        String searchText = editTextBarcode.getText().toString().trim();

                        performItemSearch(searchText);
                        return true;
                    }
                }
                return false;
            }
        });

        editTextBarcode = view.findViewById(R.id.editTextBarcode);

        editTextBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // "Enter" key was pressed
                    // Handle the barcode data here
                    String barcode = editTextBarcode.getText().toString().trim();
                    onclearButtonClick();
                    editTextBarcode.requestFocus();
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString("barcode", barcode);
                    getParentFragmentManager().setFragmentResult("barcodeKey", resultBundle);
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    editTextBarcode.requestFocus();
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

        buttonbackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackspaceButtonClick();
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

        buttonfunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.scanner_container);
                if (currentFragment instanceof CustomerLcdFragment) {
                    buttonfunction.setText(getString(R.string.functions));
                    FunctionFragment customerFragment = new FunctionFragment();
                    replaceFragment(customerFragment);
                } else if(currentFragment instanceof InbuiltScannerFragment) {
                    buttonfunction.setText(getString(R.string.functions));
                    FunctionFragment customerFragment = new FunctionFragment();
                    replaceFragment(customerFragment);
            }else {
                // The current fragment is CustomerLcdFragment, replace it with InbuiltScannerFragment
                    buttonfunction.setText(getString(R.string.functions));
                    FunctionFragment customerFragment = new FunctionFragment();
                    replaceFragment(customerFragment);
            }
            }
        });

        buttonQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.scanner_container);
                if (currentFragment instanceof CustomerLcdFragment) {
                    // The current fragment is CustomerLcdFragment, replace it with InbuiltScannerFragment
                    buttonQr.setText(getString(R.string.QR));
                    InbuiltScannerFragment newScannerFragment = new InbuiltScannerFragment();
                    replaceFragment(newScannerFragment);
                } else if(currentFragment instanceof InbuiltScannerFragment) {
                    buttonQr.setText(getString(R.string.Scan));
                    QRFragment customerFragment = new QRFragment();
                    replaceFragment(customerFragment);
                }else {
                    // The current fragment is CustomerLcdFragment, replace it with InbuiltScannerFragment
                    buttonQr.setText(getString(R.string.QR));
                    InbuiltScannerFragment newScannerFragment = new InbuiltScannerFragment();
                    replaceFragment(newScannerFragment);
                }
            }

        });

        return view;
    }
    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.scanner_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
        fragmentTransaction.commit();
    }
    private void onEnterButtonClick() {
        String barcode = editTextBarcode.getText().toString().trim();

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

            onclearButtonClick();


    }
    private void onclearButtonClick() {
        // Clear the entered PIN and update the PIN EditText
        enteredBarcode.setLength(0);
        editTextBarcode.setText("");
        editTextBarcode.setText("");
        editTextBarcode.requestFocus();

        }

    private void onBackspaceButtonClick() {
        // Check if there are characters to remove
        if (enteredBarcode.length() > 0) {
            // Remove the last character from the entered barcode
            enteredBarcode.deleteCharAt(enteredBarcode.length() - 1);
            editTextBarcode.setText(enteredBarcode.toString());
        }
    }

    private void performItemSearch(String searchText) {

        Bundle resultBundle = new Bundle();
        resultBundle.putString("barcode", searchText);
        getParentFragmentManager().setFragmentResult("barcodesearched", resultBundle);


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
