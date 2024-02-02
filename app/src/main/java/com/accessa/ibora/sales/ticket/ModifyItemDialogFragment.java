package com.accessa.ibora.sales.ticket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;
import com.accessa.ibora.sales.Sales.SalesFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModifyItemDialogFragment extends DialogFragment {
    private DBManager Xdatabasemanager;
    private ItemClearedListener itemclearedListener;
    private String tableid;
    private int roomid;
    private static final String ARG_QUANTITY = "quantity";
    private static final String ARG_PRICE = "price";
    private static final String ARG_LONG_DESC = "long_desc";
    private static final String ARG_Item_id = "Item_Id";
    private   String ITEM_ID;

    private static DatabaseHelper mDatabaseHelper;

    public static ModifyItemDialogFragment newInstance(String quantity, String price, String longDesc, String itemId) {
        ModifyItemDialogFragment fragment = new ModifyItemDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUANTITY, quantity);
        args.putString(ARG_PRICE, price);
        args.putString(ARG_LONG_DESC, longDesc);
        args.putString(ARG_Item_id, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_modify_item, null);
        EditText quantityEditText = view.findViewById(R.id.quantity_edit_text);
        EditText priceEditText = view.findViewById(R.id.price_edit_text);
        EditText longDescEditText = view.findViewById(R.id.long_desc_edit_text);

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



        // Remove the "x " prefix from the quantity
            if (quantity != null && quantity.startsWith("x ")) {
                quantity = quantity.substring(2);
            }

            // Remove the "Rs " prefix from the quantity
            if (price != null && price.startsWith("Rs ")) {
                price = price.substring(3);
            }



            // Set the values in the edit texts
            quantityEditText.setText(quantity);
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
                        String quantity = quantityEditText.getText().toString();
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
                        returnHome();

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
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
