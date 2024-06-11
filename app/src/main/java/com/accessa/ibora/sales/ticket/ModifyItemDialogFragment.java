package com.accessa.ibora.sales.ticket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.externalprinterlibrary2.Kitchen.SendNoteToKitchenActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModifyItemDialogFragment extends DialogFragment {
    private DBManager Xdatabasemanager;
    private ItemClearedListener itemclearedListener;
    private String tableid;
    private int roomid;
    private EditText editTextOption1,quantityEditText,priceEditText,longDescEditText;

    private static final String ARG_QUANTITY = "quantity";
    private static final String ARG_PRICE = "price";
    private static final String ARG_LONG_DESC = "long_desc";
    private static final String ARG_Item_id = "Item_Id";
    private static final String ARG_Barcode = "Barcode";
    private   String ITEM_ID,Barcode;

    private static DatabaseHelper mDatabaseHelper;

    public static ModifyItemDialogFragment newInstance(String quantity, String price, String longDesc, String itemId,String Barcode) {
        ModifyItemDialogFragment fragment = new ModifyItemDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUANTITY, quantity);
        args.putString(ARG_PRICE, price);
        args.putString(ARG_LONG_DESC, longDesc);
        args.putString(ARG_Item_id, itemId);
        args.putString(ARG_Barcode, Barcode);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_modify_item, null);




        // Find the EditText field in the custom layout
        editTextOption1 = view.findViewById(R.id.editTextOption1);
        editTextOption1.setInputType(InputType.TYPE_NULL);
        editTextOption1.setTextIsSelectable(true);

        // Find the EditText field in the custom layout
        final Spinner quantitySpinner = view.findViewById(R.id.quantity_spinner);
        // Populate the Spinner with numbers
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) { // You can customize the range as needed
            numbers.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);

        editTextOption1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        // Find the EditText field in the custom layout
        priceEditText = view.findViewById(R.id.price_edit_text);
        priceEditText.setInputType(InputType.TYPE_NULL);
        priceEditText.setTextIsSelectable(true);

        // Find the EditText field in the custom layout
        longDescEditText = view.findViewById(R.id.long_desc_edit_text);
        longDescEditText.setInputType(InputType.TYPE_NULL);
        longDescEditText.setTextIsSelectable(true);
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
        Button buttonA = view.findViewById(R.id.buttonInsertA);
        Button buttonB = view.findViewById(R.id.buttonInsertB);
        Button buttonC = view.findViewById(R.id.buttonInsertC);
        Button buttonD = view.findViewById(R.id.buttonInsertD);
        Button buttonE = view.findViewById(R.id.buttonInsertE);
        Button buttonF = view.findViewById(R.id.buttonInsertF);
        Button buttonG = view.findViewById(R.id.buttonInsertG);
        Button buttonH = view.findViewById(R.id.buttonInsertH);
        Button buttonI = view.findViewById(R.id.buttonInsertI);
        Button buttonJ = view.findViewById(R.id.buttonInsertJ);
        Button buttonK = view.findViewById(R.id.buttonInsertK);
        Button buttonL = view.findViewById(R.id.buttonInsertL);
        Button buttonM = view.findViewById(R.id.buttonInsertM);
        Button buttonN = view.findViewById(R.id.buttonInsertN);
        Button buttonO = view.findViewById(R.id.buttonInsertO);
        Button buttonP = view.findViewById(R.id.buttonInsertP);
        Button buttonQ = view.findViewById(R.id.buttonInsertQ);
        Button buttonR = view.findViewById(R.id.buttonInsertR);
        Button buttonS = view.findViewById(R.id.buttonInsertS);
        Button buttonT = view.findViewById(R.id.buttonInsertT);
        Button buttonU = view.findViewById(R.id.buttonInsertU);
        Button buttonV = view.findViewById(R.id.buttonInsertV);
        Button buttonW = view.findViewById(R.id.buttonInsertW);
        Button buttonX = view.findViewById(R.id.buttonInsertX);
        Button buttonY = view.findViewById(R.id.buttonInsertY);
        Button buttonZ = view.findViewById(R.id.buttonInsertZ);
        Button buttonSpace = view.findViewById(R.id.buttonInsertspace);
        // Set OnClickListener for number buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "0");
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
                oncommentButtonClick(editTextOption1, ",");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick(v);
            }        });


        buttonSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, " ");
            }
        });
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "A");
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "B");
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "C");
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "D");
            }
        });
        buttonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "E");
            }
        });
        buttonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "F");
            }
        });
        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "G");
            }
        });
        buttonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "H");
            }
        });
        buttonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "I");
            }
        });
        buttonJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "J");
            }
        });
        buttonK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "K");
            }
        });
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "L");
            }
        });
        buttonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "M");
            }
        });
        buttonN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "N");
            }
        });
        buttonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "O");
            }
        });
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "P");
            }
        });
        buttonQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Q");
            }
        });
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "R");
            }
        });
        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "S");
            }
        });
        buttonT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "T");
            }
        });
        buttonU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "U");
            }
        });
        buttonV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "V");
            }
        });
        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "W");
            }
        });
        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "X");
            }
        });
        buttonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Y");
            }
        });
        buttonZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncommentButtonClick(editTextOption1, "Z");
            }
        });

        // Initialize SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("roomandtable", Context.MODE_PRIVATE);
        roomid = preferences.getInt("roomnum", 0);
        tableid = preferences.getString("table_id", "");

        Xdatabasemanager = new DBManager(getContext());
        Xdatabasemanager.open();

        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(getContext());

        // Get the arguments passed to the dialog fragment
        Bundle args = getArguments();
        if (args != null) {
            String quantity = args.getString(ARG_QUANTITY);
            String price = args.getString(ARG_PRICE);
            String longDesc = args.getString(ARG_LONG_DESC);
             ITEM_ID = args.getString(ARG_Item_id);
            Barcode= args.getString(ARG_Barcode);


        // Remove the "x " prefix from the quantity
            if (quantity != null && quantity.startsWith("x ")) {
                quantity = quantity.substring(2);
            }

            // Remove the "Rs " prefix from the quantity
            if (price != null && price.startsWith("Rs ")) {
                price = price.substring(3);
            }



            // Set the values in the edit texts

            priceEditText.setText(price);
            longDescEditText.setText(longDesc);
        }
        // Retrieve the delete button and set its click listener
        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.delete_confirmation))
                        .setMessage(getString(R.string.delete_confirmation_message))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(ITEM_ID); // Call the deleteItem() method with the item ID
                                refreshTicketFragment();
                                dismiss(); // Close the dialog after deleting the item
                                if (itemclearedListener != null) {
                                    itemclearedListener.onItemDeleted();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.modItem))
                .setView(view)
                .setPositiveButton(getString(R.string.Save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Get the current date and time for the transaction
                        String lastmodified = getCurrentDateTime();
                        String id= ITEM_ID;
                        String Barcodes= Barcode;
                        String quantity = quantitySpinner.getSelectedItem().toString();
                        String price = priceEditText.getText().toString();
                        String longDesc = longDescEditText.getText().toString();
                        if (quantity.isEmpty() ) {
                            Toast.makeText(getContext(), getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Pass the modified values back to the listener
                        if (getTargetFragment() instanceof ModifyItemDialogListener) {
                            ModifyItemDialogListener listener = (ModifyItemDialogListener) getTargetFragment();
                            listener.onItemModified(quantity, price, longDesc);
                        }
                       double UnitPrice= mDatabaseHelper.getItemPrice(id);

                        double Quantity= Double.parseDouble(quantity);
                       double  totalPrice= Quantity * UnitPrice;
                        boolean isUpdated = Xdatabasemanager.updateTransItem(Long.parseLong(id), quantity, String.valueOf(totalPrice), longDesc,lastmodified);
                        if (itemclearedListener != null) {
                            itemclearedListener.onAmountModified();
                        }
                        String option1Text = editTextOption1.getText().toString();
                        Log.d("DialogFragment", "Option1 Text: " + option1Text);
                        updateCommentForTransaction(mDatabaseHelper.getInProgressTransactionId(String.valueOf(roomid),tableid),option1Text,id );
                        if (!TextUtils.isEmpty(option1Text)) {
                            Log.d("DialogFragment", "Starting SendNoteToKitchenActivity");

                            // Create an Intent to launch SendNoteToKitchenActivity
                            Intent intent = new Intent(getActivity(), SendNoteToKitchenActivity.class);
                            // Put the text as an extra in the Intent
                            intent.putExtra("note_text", option1Text);
                            intent.putExtra("barcode", Barcodes);
                            Log.d("barcode", Barcodes);
                            // Start the activity
                            startActivity(intent);
                        } else {
                            // Show a toast or alert indicating that the EditText is empty
                            Toast.makeText(getContext(), "Please enter a note", Toast.LENGTH_SHORT).show();
                            returnHome();
                        }

                        // Check if the EditText is not null and not empty

                       //

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
    }

    public void updateCommentForTransaction(String transactionId, String comment,String itemid) {
        // Call your database helper method to update the comment for the given transaction ID
        mDatabaseHelper.updateTransactionComment(transactionId, comment,itemid);
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void deleteItem(String itemId) {
        // Perform the delete operation here
        if (Xdatabasemanager != null) {
            boolean deleted = Xdatabasemanager.deleteTransacItem(Long.parseLong(itemId));
            if (itemclearedListener != null) {
                itemclearedListener.onItemDeleted();
            }
            if (deleted) {
                Toast.makeText(getActivity(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                returnHome();
            } else {
                Toast.makeText(getActivity(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void refreshTicketFragment() {
        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        if (ticketFragment != null) {
            ticketFragment.refreshData(calculateTotalAmount(String.valueOf(roomid),tableid),calculateTotalTax(roomid,tableid));
        }
    }
    public static double calculateTotalAmount(String roomid, String tableid) {
        if (roomid == null && tableid ==null) {
            // Continue with the rest of your logic
            Cursor cursor = mDatabaseHelper.getAllInProgressTransactions("0", "0");
            double totalAmount = 0.0;
            if (cursor != null && cursor.moveToFirst()) {
                int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
                do {
                    double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                    totalAmount += totalPrice;
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }

            return totalAmount;
        } else {
            // Continue with the rest of your logic
            Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(roomid, tableid);
            double totalAmount = 0.0;
            if (cursor != null && cursor.moveToFirst()) {
                int totalPriceColumnIndex = cursor.getColumnIndex(DatabaseHelper.TOTAL_PRICE);
                do {
                    double totalPrice = cursor.getDouble(totalPriceColumnIndex);
                    totalAmount += totalPrice;
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }

            return totalAmount;
        }
    }

    public static double calculateTotalTax(int roomid,String tableid) {
        Cursor cursor = mDatabaseHelper.getAllInProgressTransactions(String.valueOf(roomid),tableid);
        double TaxtotalAmount = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalTaxColumnIndex = cursor.getColumnIndex(DatabaseHelper.VAT);
            do {
                double totalPrice = cursor.getDouble(totalTaxColumnIndex);
                TaxtotalAmount += totalPrice;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return TaxtotalAmount;
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "Ticket_fragment");
        startActivity(home_intent1);
    }
    public interface ModifyItemDialogListener {
        void onItemModified(String quantity, String price, String longDesc);
    }
    private void onBackspaceButtonClick() {
        // Get the current text from editTextOption1
        Editable editable = editTextOption1.getText();

        // Get the length of the text
        int length = editable.length();

        // If there are characters in the text, remove the last character
        if (length > 0) {
            editable.delete(length - 1, length);
        }
    }
    public void onClearButtonClick(View view) {

        onclearButtonClick();


    }

    private void onclearButtonClick() {
        editTextOption1.setText(""); // Set the text of editTextOption1 to an empty string
    }

    public void oncommentButtonClick(View view, String letter) {
        if (editTextOption1 != null) {
            // Insert the letter into the EditText
            editTextOption1.append(letter);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ItemClearedListener) {
            itemclearedListener = (ItemClearedListener) context;
        }

    }
    public interface ItemClearedListener {


        void onItemDeleted();
        void onAmountModified();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        itemclearedListener = null;
    }
}
