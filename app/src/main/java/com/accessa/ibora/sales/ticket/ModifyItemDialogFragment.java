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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.printer.externalprinterlibrary2.Kitchen.SendNoteToKitchenActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.sales.Sales.SalesFragment;
import com.accessa.ibora.sales.ticket.Checkout.DiscountAdapter;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModifyItemDialogFragment extends DialogFragment {
    private DBManager Xdatabasemanager;

    private DiscountAppliedListener discountAppliedListener;

    private ItemClearedListener itemclearedListener;
EditText pinEditText;
    private SalesFragment.ItemAddedListener itemAddedListener;
    private String tableid;
    private int roomid;
    private String  CatName;
    private EditText editTextOption1,quantityEditText,priceEditText,longDescEditText;

    private static final String ARG_QUANTITY = "quantity";
    private static final String ARG_PRICE = "price";
    private static final String ARG_LONG_DESC = "long_desc";
    private static final String ARG_uniqueitem_id = "Uniqueitemid";
    private static final String ARG_item_id = "Item_Id";


    private static final String ARG_Barcode = "Barcode";
    private   String Unique_ITEM_ID,Barcode,ITEM_ID;

    private static DatabaseHelper mDatabaseHelper;

    public static ModifyItemDialogFragment newInstance(String quantity, String price, String longDesc, String uniqueitemid,String itemid,String Barcode) {
        ModifyItemDialogFragment fragment = new ModifyItemDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUANTITY, quantity);
        args.putString(ARG_PRICE, price);
        args.putString(ARG_LONG_DESC, longDesc);
        args.putString(ARG_uniqueitem_id, uniqueitemid);
        args.putString(ARG_item_id, itemid);
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
        longDescEditText = view.findViewById(R.id.long_desc_edit_text);
        longDescEditText.setInputType(InputType.TYPE_NULL);
        longDescEditText.setTextIsSelectable(true);

        priceEditText = view.findViewById(R.id.price_edit_text);

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
            Unique_ITEM_ID = args.getString(ARG_uniqueitem_id);
            ITEM_ID=  args.getString(ARG_item_id);
            Barcode= args.getString(ARG_Barcode);
            String famille= mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(Unique_ITEM_ID));
            CatName=mDatabaseHelper.getCatNameById(famille);
            Log.d("Unique_ITEM_ID", String.valueOf(Unique_ITEM_ID));
            Log.d("famille", String.valueOf(famille));
            Log.d("CatName", String.valueOf(CatName));

            // Remove the "x " prefix from the quantity
            if (quantity != null && quantity.startsWith("x ")) {
                quantity = quantity.substring(2);
            }

            // Remove the "Rs " prefix from the quantity
            if (price != null && price.startsWith("Rs ")) {
                price = price.substring(3);
            }


// Set the selected item in the spinner
            if (quantity != null) {
                quantity = quantity.trim();
                int quantityInt = Integer.parseInt(quantity);
                int spinnerPosition = adapter.getPosition(quantityInt);
                quantitySpinner.setSelection(spinnerPosition);
            }
         String  comment=  mDatabaseHelper.getTransactionCommentById(Integer.parseInt(Unique_ITEM_ID));
            // Set the values in the edit texts
            editTextOption1.setText(comment);
            priceEditText.setText(price);
            longDescEditText.setText(longDesc);
        }
        if ("OPEN FOOD".equals(CatName)) {
            // Make the EditText editable
            priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            priceEditText.setTextIsSelectable(false); // Disable text selection if needed
            priceEditText.setFocusable(true);
            priceEditText.setFocusableInTouchMode(true);
        } else {
            // Make the EditText non-editable
            priceEditText.setInputType(InputType.TYPE_NULL);
            priceEditText.setTextIsSelectable(true); // Enable text selection
            priceEditText.setFocusable(false);
            priceEditText.setFocusableInTouchMode(false);
        }

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

        Button itemdiscountButton = view.findViewById(R.id.ietmdiscount_button);


        itemdiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inflate the PIN dialog layout
                LayoutInflater inflater = getLayoutInflater();
                View pinDialogView = inflater.inflate(R.layout.pin_dialog, null);
                pinEditText = pinDialogView.findViewById(R.id.editTextPIN);

                // Find the number buttons and set OnClickListener
                Button button1 = pinDialogView.findViewById(R.id.button1);
                Button button2 = pinDialogView.findViewById(R.id.button2);
                Button button3 = pinDialogView.findViewById(R.id.button3);
                Button button4 = pinDialogView.findViewById(R.id.button4);
                Button button5 = pinDialogView.findViewById(R.id.button5);
                Button button6 = pinDialogView.findViewById(R.id.button6);
                Button button7 = pinDialogView.findViewById(R.id.button7);
                Button button8 = pinDialogView.findViewById(R.id.button8);
                Button button9 = pinDialogView.findViewById(R.id.button9);
                Button button0 = pinDialogView.findViewById(R.id.button0);
                Button buttonClear = pinDialogView.findViewById(R.id.buttonClear);
                Button buttonLogin = pinDialogView.findViewById(R.id.buttonLogin);

                // Set up button click listeners in the dialog
                setPinButtonClickListeners(pinDialogView, pinEditText);

                // Create the PIN dialog without positive and negative buttons
                AlertDialog.Builder pinBuilder = new AlertDialog.Builder(getContext());
                pinBuilder.setTitle("Enter PIN")
                        .setView(pinDialogView);

                AlertDialog pinDialog = pinBuilder.create();
                pinDialog.show();

                // Set click listener for the Clear button
                buttonClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClearButtonClick(pinEditText);
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pinEditText != null) {
                            onPinNumberButtonClick(v,"1");
                        } else {
                            // Handle the case where clickedEditText is null

                            // Alternatively, perform another action to handle this case
                        }
                    }
                });


                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "No EditText selected", Toast.LENGTH_SHORT).show();
                        onPinNumberButtonClick(v,"2");
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"3");
                    }
                });

                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"4");
                    }
                });

                button5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"5");
                    }
                });

                button6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"6");
                    }
                });

                button7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"7");
                    }
                });

                button8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"8");
                    }
                });

                button9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"9");
                    }
                });

                button0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPinNumberButtonClick(v,"0");
                    }
                });




                // Set click listener for the Login button
                buttonLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String enteredPIN = pinEditText.getText().toString();

                        // Validate the entered PIN
                        int cashorLevel = validatePIN(enteredPIN);
                        if (cashorLevel != -1) {
                            // PIN is valid, now show the discount dialog
                            showDiscountDialog(cashorLevel,Unique_ITEM_ID);

                            // Dismiss the dialog
                            dismiss();
                            pinDialog.dismiss(); // Dismiss the PIN dialog after successful login
                        } else {
                            // PIN is invalid, show an error message
                            Toast.makeText(getActivity(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
// Retrieve the delete button and set its click listener
        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

                String cashierLevel = sharedPreference.getString("cashorlevel", null);

                int levelNumber = Integer.parseInt(cashierLevel); // Get the user level
                // Permission checks

                boolean canHigherAccessvoiditem = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "voiditem_", levelNumber);


                boolean candovoiditem = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "voiditem_", levelNumber);

                String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                String latesttransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);
                int distinctitemcount = mDatabaseHelper.getDistinctItemCountByTransactionId(latesttransId);
                if (canHigherAccessvoiditem) {
                    showPinDialog("voiditem_", () -> {
                if (distinctitemcount == 1) {
// Show a pop-up dialog
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm Action")
                            .setMessage("There is only one item left in this transaction. Do you want to void the item or clear the entire transaction?")
                            .setPositiveButton("Void Item", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItem(Unique_ITEM_ID, latesttransId);
                                    // Perform the delete operation here
                                    if (Xdatabasemanager != null) {
                                        Xdatabasemanager.flagTransactionItemAsVoid(Unique_ITEM_ID, latesttransId);
                                        if (itemclearedListener != null) {
                                            itemclearedListener.onItemDeleted();
                                        }

                                        refreshTicketFragment();
                                        dismiss(); // Close the dialog after deleting the item

                                    }
                                }
                            })
                            .setNegativeButton("Clear Transaction", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDatabaseHelper.flagTransactionItemsAsCleared(latesttransId);
                                    clearTransact();
                                    returnHome();
                                }
                            })
                            .setCancelable(false)
                            .show();

                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm Action")
                            .setMessage("Do you want to void this Item?")
                            .setPositiveButton("Void Item", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItem(Unique_ITEM_ID, latesttransId);
                                    // Perform the delete operation here
                                    if (Xdatabasemanager != null) {
                                        Xdatabasemanager.flagTransactionItemAsVoid(Unique_ITEM_ID, latesttransId);
                                        if (itemclearedListener != null) {
                                            itemclearedListener.onItemDeleted();
                                        }

                                        refreshTicketFragment();

                                        if (itemclearedListener != null) {
                                            itemclearedListener.onAmountModified();
                                            dismiss(); // Close the dialog after deleting the item
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); // Close the dialog without doing anything
                                }
                            })
                            .show();


                }
                    });
                }else if(!canHigherAccessvoiditem && candovoiditem) {
                    if (distinctitemcount == 1) {
// Show a pop-up dialog
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirm Action")
                                .setMessage("There is only one item left in this transaction. Do you want to void the item or clear the entire transaction?")
                                .setPositiveButton("Void Item", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteItem(Unique_ITEM_ID, latesttransId);
                                        // Perform the delete operation here
                                        if (Xdatabasemanager != null) {
                                            Xdatabasemanager.flagTransactionItemAsVoid(Unique_ITEM_ID, latesttransId);
                                            if (itemclearedListener != null) {
                                                itemclearedListener.onItemDeleted();
                                            }

                                            refreshTicketFragment();
                                            dismiss(); // Close the dialog after deleting the item

                                        }
                                    }
                                })
                                .setNegativeButton("Clear Transaction", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabaseHelper.flagTransactionItemsAsCleared(latesttransId);
                                        clearTransact();
                                        returnHome();
                                    }
                                })
                                .setCancelable(false)
                                .show();

                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirm Action")
                                .setMessage("Do you want to void this Item?")
                                .setPositiveButton("Void Item", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteItem(Unique_ITEM_ID, latesttransId);
                                        // Perform the delete operation here
                                        if (Xdatabasemanager != null) {
                                            Xdatabasemanager.flagTransactionItemAsVoid(Unique_ITEM_ID, latesttransId);
                                            if (itemclearedListener != null) {
                                                itemclearedListener.onItemDeleted();
                                            }

                                            refreshTicketFragment();

                                            if (itemclearedListener != null) {
                                                itemclearedListener.onAmountModified();
                                                dismiss(); // Close the dialog after deleting the item
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); // Close the dialog without doing anything
                                    }
                                })
                                .show();

                    }
                }else {
                    showPermissionDeniedDialog(); // Method to show a message to the user
                }

            }
        });
        // Retrieve the delete button and set its click listener
        Button ppiButton = view.findViewById(R.id.payperitems);
        ppiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the dialog for editing quantity
                showEditQuantityDialog();


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
                        String Uniqueid= Unique_ITEM_ID;
                        String itemid= ITEM_ID;
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

                        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                        String taxcode=mDatabaseHelper.getTransactionTaxCode(latesttransId,Uniqueid);

                       double currentprice= mDatabaseHelper.getItemPrice(itemid);

                       if (currentprice==0.0){
                    currentprice=mDatabaseHelper.getVariantPriceByItemId(itemid);
                           if (currentprice==0.0){
                            currentprice=mDatabaseHelper.getSupplementPrice(itemid);

                           }
                       }

                        //String VatType=mDatabaseHelper.getVatTypeById(itemid);
                        double Quantity= Double.parseDouble(quantity);
                       double  totalPrice= Quantity * currentprice;
                        if ("OPEN FOOD".equals(CatName)) {
                            totalPrice = Quantity * Double.parseDouble(price);

                        }

                        double vatAmount=0;
                        if (taxcode.equals("TC01")) {
                            double vatRate = 0.15; // 15% VAT rate

                            // Calculate the price without VAT
                            double priceExcludingVAT = totalPrice / (1 + vatRate);

                            // Calculate the VAT amount
                             vatAmount = totalPrice - priceExcludingVAT;


                        }

                        Xdatabasemanager.updateTransItem(latesttransId,Long.parseLong(Uniqueid), quantity, String.valueOf(totalPrice), String.valueOf(vatAmount), longDesc,lastmodified);
                        if (itemclearedListener != null) {
                            itemclearedListener.onAmountModified();
                        }
                        String option1Text = editTextOption1.getText().toString();
                        updateCommentForTransaction(latesttransId,option1Text,Uniqueid );
                        if ("OPEN FOOD".equals(CatName) && !TextUtils.isEmpty(option1Text)) {
                            if (itemclearedListener != null) {
                                itemclearedListener.onAmountModified();
                            }


                        }else if (!TextUtils.isEmpty(option1Text)) {

                            // Create an Intent to launch SendNoteToKitchenActivity
                            Intent intent = new Intent(getActivity(), SendNoteToKitchenActivity.class);
                            // Put the text as an extra in the Intent
                            intent.putExtra("note_text", option1Text);
                            intent.putExtra("barcode", Barcodes);

                            // Start the activity
                            startActivity(intent);
                        } else {
                            // Show a toast or alert indicating that the EditText is empty

                           // returnHome();
                            refreshTicketFragment();
                            if (itemclearedListener != null) {
                                itemclearedListener.onAmountModified();
                            }

                        }


                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
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
    // Helper method to provide quantity options for the spinner
    private List<Integer> getQuantityOptions() {
        List<Integer> quantities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            quantities.add(i); // Add numbers from 1 to 10 as options
        }
        return quantities;
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
        }
    }
    public double calculateTax(String price,String VatVall) {
        double taxAmount = 0.0;

        double unitPriceInclusive = Double.parseDouble(price);
        double unitPriceExclusive = unitPriceInclusive / 1.15; // Removing 15% VAT to get exclusive price

        if (VatVall.equals("VAT 15%")) {
            taxAmount = unitPriceExclusive * 0.15; // Calculate VAT based on exclusive price
        }

        // Ensure that the tax amount is rounded to two decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return taxAmount;
    }
    public void updateCommentForTransaction(String transactionId, String comment,String itemid) {
        // Call your database helper method to update the comment for the given transaction ID
        mDatabaseHelper.updateTransactionComment(transactionId, comment,itemid);
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void deleteItem(String uniqueitemId,String transactionid) {


        // Handle the user's selection here




                    if (Xdatabasemanager != null) {
                        boolean deleted = Xdatabasemanager.flagTransactionItemAsVoid(uniqueitemId,transactionid);
                        if (itemclearedListener != null) {
                            itemclearedListener.onItemDeleted();
                        }
                        if (deleted) {
                            //returnHome();
                            refreshTicketFragment();
                            if (itemAddedListener != null) {
                                itemAddedListener.onItemAdded(String.valueOf(roomid),tableid);
                            }
                        } else {
                        }
                    }



        // Perform the delete operation here

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
        androidx.appcompat.app.AlertDialog.Builder pinBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        pinBuilder.setTitle("Enter PIN")
                .setView(pinDialogView);
        androidx.appcompat.app.AlertDialog pinDialog = pinBuilder.create();
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

    public void onpinClearButtonClick(EditText ReceivedEditText) {

        onclearButtonClicks(ReceivedEditText);
        onPinclearButtonClick(ReceivedEditText);


    }

    private void showPermissionDeniedDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Permission Denied")
                .setMessage("You do not have permission to access this feature.")
                .setPositiveButton("OK", null)
                .show();
    }
    private void onclearButtonClicks(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");
            // ReceivedEditText.setText("");
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
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
    private void showDiscountDialog(int cashorLevel,String unique_ITEM_ID) {
        LayoutInflater inflater = getLayoutInflater();
        View discountDialogView = inflater.inflate(R.layout.discount_dialog, null);

        RecyclerView recyclerView = discountDialogView.findViewById(R.id.discount_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Cursor discountsCursor = mDatabaseHelper.getAllDiscounts(); // Fetch all discounts

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Discount")
                .setView(discountDialogView)
                .setNegativeButton("Cancel", null);

        // Show the dialog and keep a reference to it
        AlertDialog discountDialog = builder.create();
        discountDialog.show();

        // Set up the adapter for the RecyclerView
        DiscountAdapter adapter = new DiscountAdapter(discountsCursor, discountValue -> {
            // Check if cashor level is less than 3 and the discount exceeds 10%
            if (cashorLevel < 3 && discountValue > 10) {
                Toast.makeText(getContext(), "You cannot apply more than 10% discount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Apply the discount
            applyDiscount(discountValue,unique_ITEM_ID);


            // Dismiss the dialog after the discount is applied
            discountDialog.dismiss();
        });

        recyclerView.setAdapter(adapter);
    }

    private void showEditQuantityDialog() {
        // Create a dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_quantity); // Set the custom layout for the dialog

        // Get references to the views in the dialog
        Spinner quantitySpinner = dialog.findViewById(R.id.quantity_spinner);
        Button payButton = dialog.findViewById(R.id.pay_button);

        // Populate the spinner with quantity options (example: 1 to 10)
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                getQuantityOptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);

        // Retrieve passed arguments and set initial values
        Bundle args = getArguments();
        if (args != null) {
            String quantity = args.getString(ARG_QUANTITY);
            String price = args.getString(ARG_PRICE);
            String longDesc = args.getString(ARG_LONG_DESC);
            Unique_ITEM_ID = args.getString(ARG_uniqueitem_id);
            ITEM_ID = args.getString(ARG_item_id);
            Barcode = args.getString(ARG_Barcode);
            String famille = mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(Unique_ITEM_ID));
            CatName = mDatabaseHelper.getCatNameById(famille);

            Log.d("Unique_ITEM_ID", String.valueOf(Unique_ITEM_ID));
            Log.d("famille", String.valueOf(famille));
            Log.d("CatName", String.valueOf(CatName));

            // Remove the "x " prefix from the quantity if present
            if (quantity != null && quantity.startsWith("x ")) {
                quantity = quantity.substring(2);
            }

            // Set the selected item in the spinner
            if (quantity != null) {
                int quantityInt = Integer.parseInt(quantity.trim());
                int spinnerPosition = adapter.getPosition(quantityInt);
                quantitySpinner.setSelection(spinnerPosition);
            }
        }

        // Set the click listener for the Pay button
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected quantity from the spinner
                int selectedQuantity = (int) quantitySpinner.getSelectedItem();

                // Do something with the selected quantity, like updating the database, etc.
                Log.d("Selected Quantity", String.valueOf(selectedQuantity));
                // Retrieve passed arguments and set initial values
                Bundle args = getArguments();
                if (args != null) {
                    String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
                    String latestTransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

                    String quantity = args.getString(ARG_QUANTITY);
                    String price = args.getString(ARG_PRICE);
                    String longDesc = args.getString(ARG_LONG_DESC);
                    Unique_ITEM_ID = args.getString(ARG_uniqueitem_id);
                    ITEM_ID = args.getString(ARG_item_id);
                    Barcode = args.getString(ARG_Barcode);
                    String famille = mDatabaseHelper.getTransactionFamilieById(Integer.parseInt(Unique_ITEM_ID));
                    CatName = mDatabaseHelper.getCatNameById(famille);
                    String lastmodified = getCurrentDateTime();
                    String Uniqueid= Unique_ITEM_ID;
                    String itemid= ITEM_ID;
                    String Barcodes= Barcode;
                    Log.d("Unique_ITEM_ID", String.valueOf(Unique_ITEM_ID));
                    Log.d("famille", String.valueOf(famille));
                    Log.d("CatName", String.valueOf(CatName));

                    // Remove the "x " prefix from the quantity if present
                    if (quantity != null && quantity.startsWith("x ")) {
                        quantity = quantity.substring(2);
                    }
                    quantity = quantity.trim();
                    int quant= Integer.parseInt(quantity);
                int newquantity= quant- selectedQuantity;
                    // Set the selected item in the spinner
                    if (quantity != null) {
                        int quantityInt = Integer.parseInt(quantity.trim());
                        int spinnerPosition = adapter.getPosition(quantityInt);
                        quantitySpinner.setSelection(spinnerPosition);
                    }
                    // Pass the modified values back to the listener
                    if (getTargetFragment() instanceof ModifyItemDialogListener) {
                        ModifyItemDialogListener listener = (ModifyItemDialogListener) getTargetFragment();
                        listener.onItemModified(quantity, price, longDesc);
                    }

                     statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
                    String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);
                    String taxcode=mDatabaseHelper.getTransactionTaxCode(latesttransId,Uniqueid);

                    double currentprice= mDatabaseHelper.getItemPrice(itemid);

                    if (currentprice==0.0){
                        currentprice=mDatabaseHelper.getVariantPriceByItemId(itemid);
                        if (currentprice==0.0){
                            currentprice=mDatabaseHelper.getSupplementPrice(itemid);

                        }
                    }

                    //String VatType=mDatabaseHelper.getVatTypeById(itemid);
                    double Quantity= Double.parseDouble(quantity);
                    double  totalPrice= newquantity * currentprice;
                    double  newtotalPrice= selectedQuantity * currentprice;
                    if ("OPEN FOOD".equals(CatName)) {
                        totalPrice = newquantity * Double.parseDouble(price);

                    }

                    double vatAmount=0;
                    if (taxcode.equals("TC01")) {
                        double vatRate = 0.15; // 15% VAT rate

                        // Calculate the price without VAT
                        double priceExcludingVAT = totalPrice / (1 + vatRate);

                        // Calculate the VAT amount
                        vatAmount = totalPrice - priceExcludingVAT;


                    }

                    double newvatAmount=0;
                    if (taxcode.equals("TC01")) {
                        double vatRate = 0.15; // 15% VAT rate

                        // Calculate the price without VAT
                        double priceExcludingVAT = newtotalPrice / (1 + vatRate);

                        // Calculate the VAT amount
                        newvatAmount = newtotalPrice - priceExcludingVAT;


                    }
                    long newId = mDatabaseHelper.duplicateTransactionById(Integer.parseInt(Uniqueid));
                    Log.d("newId" , String.valueOf(newId));

                    Xdatabasemanager.updateTransItem(latestTransId,newId, String.valueOf(selectedQuantity), String.valueOf(newtotalPrice), String.valueOf(newvatAmount), longDesc,lastmodified);

                    Xdatabasemanager.updateTransItem(latestTransId,Long.parseLong(Uniqueid), String.valueOf(newquantity), String.valueOf(totalPrice), String.valueOf(vatAmount), longDesc,lastmodified);
                    if (itemclearedListener != null) {
                        itemclearedListener.onAmountModified();
                    }
                }

                // Dismiss the dialog after paying
                dialog.dismiss();
                dismiss(); // Close the dialog after deleting the item
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void applyDiscount(double discountPercentage,String unique_ITEM_ID) {
        // Retrieve the latest transaction details
        String statusType = mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid), tableid);
        String latestTransId = mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid), tableid, statusType);

        // Calculate the total amount and tax total amount
        double totalAmount = mDatabaseHelper.calculateTotalAmountsBySelectedId(latestTransId, String.valueOf(roomid), tableid,unique_ITEM_ID);
        double taxTotalAmount = mDatabaseHelper.calculateTotalTaxAmountsBySelectedId(latestTransId, String.valueOf(roomid), tableid,unique_ITEM_ID);

        // Calculate the discount amount
        double discountAmount = totalAmount * (discountPercentage / 100);
        double discounttaxAmount = taxTotalAmount * (discountPercentage / 100);
        // Calculate the new total amount after applying the discount
        double newTotalAmount = totalAmount - discountAmount;

        // Ensure the new total amount is not negative
        if (newTotalAmount < 0) {
            newTotalAmount = 0;
        }


        mDatabaseHelper.applyDiscountToTransactionItemsById(latestTransId,discountPercentage, Long.parseLong(unique_ITEM_ID));


        // Calculate the total amount and tax total amount
         totalAmount = mDatabaseHelper.calculateTotalAmountsNotSelectedNotPaid(latestTransId, String.valueOf(roomid), tableid);
         taxTotalAmount = mDatabaseHelper.calculateTotalTaxAmountsNotSelectedNotPaid(latestTransId, String.valueOf(roomid), tableid);

        // Calculate the discount amount
         discountAmount = totalAmount * (discountPercentage / 100);
         discounttaxAmount = taxTotalAmount * (discountPercentage / 100);
        // Calculate the new total amount after applying the discount
         newTotalAmount = totalAmount - discountAmount;

        // Ensure the new total amount is not negative
        if (newTotalAmount < 0) {
            newTotalAmount = 0;
        }

        // Update the database with the new total amount and discount amount
        mDatabaseHelper.updateDiscountByTransactionId(newTotalAmount, discountAmount,discounttaxAmount, latestTransId);

        // After applying the discount, trigger the DiscountAppliedListener
        if (discountAppliedListener != null) {
            discountAppliedListener.onDiscountApplied();
        }

    }
    public  void clearTransact(){


        String statusType= mDatabaseHelper.getLatestTransactionStatus(String.valueOf(roomid),tableid);
        String latesttransId= mDatabaseHelper.getLatestTransactionId(String.valueOf(roomid),tableid,statusType);

        mDatabaseHelper.updateStatusToVoid(latesttransId);


        // Optionally, you can notify the user or perform any other actions after clearing the transaction
// Notify the listener that an item is added
        double totalAmount = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
        double TaxtotalAmount = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);

        refreshTicketFragment();
        dismiss(); // Close the dialog after deleting the item
        if (itemclearedListener != null) {
            itemclearedListener.onItemDeleted();
        }

        if (itemAddedListener != null) {
            itemAddedListener.onItemAdded(String.valueOf(roomid),tableid);
        }


    }


    public void refreshTicketFragment() {


        TicketFragment ticketFragment = (TicketFragment) getChildFragmentManager().findFragmentById(R.id.right_container);
        double totalPriceSum = mDatabaseHelper.calculateTotalAmount(String.valueOf(roomid), tableid);
        double totalVATSum = mDatabaseHelper.calculateTotalTaxAmount(String.valueOf(roomid), tableid);

        Log.d("totalPriceSum", totalPriceSum + " " + totalVATSum);

        if (ticketFragment != null) {
            ticketFragment.refreshData(totalPriceSum,totalVATSum,"movetobottom");
        }
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
    public void onPinNumberButtonClick(View view, String number) {

        if (pinEditText != null) {
            // Insert the letter into the EditText
            pinEditText.append(number);
            //ReceivedEditText.append(letter);
        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }

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
    private int validatePIN(String enteredPIN) {
        // Fetch the cashor level based on the entered PIN
        int cashorLevel = mDatabaseHelper.getCashorLevelByPIN(enteredPIN);

        // Return the cashor level if valid, or -1 if invalid
        return cashorLevel;
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
        if (context instanceof DiscountAppliedListener) {
            discountAppliedListener = (DiscountAppliedListener) context;
        }

    }
    public interface ItemClearedListener {


        void onItemDeleted();
        void onAmountModified();
    }

    public interface DiscountAppliedListener {
        void onDiscountApplied();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemclearedListener = null;
    }
}
