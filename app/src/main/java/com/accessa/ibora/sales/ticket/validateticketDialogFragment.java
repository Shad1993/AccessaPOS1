package com.accessa.ibora.sales.ticket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class validateticketDialogFragment extends DialogFragment {
    private DBManager Xdatabasemanager;
    private static final String ARG_QUANTITY = "quantity";
    private static final String ARG_PRICE = "price";
    private static final String ARG_LONG_DESC = "long_desc";
    private static final String ARG_Transaction_id = "transactionId";
    private   String Transaction_Id;

    private DatabaseHelper mDatabaseHelper;

    public static validateticketDialogFragment newInstance( String transactionId) {
        validateticketDialogFragment fragment = new validateticketDialogFragment();
        Bundle args = new Bundle();

        args.putString(ARG_Transaction_id, transactionId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_validate_transaction, null);




        Xdatabasemanager = new DBManager(getContext());
        Xdatabasemanager.open();

        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(getContext());

        // Get the arguments passed to the dialog fragment
        Bundle args = getArguments();
        if (args != null) {

            Transaction_Id = args.getString(ARG_Transaction_id);






        }
        // Retrieve the delete button and set its click listener
        Button cashButton = view.findViewById(R.id.buttonCash);
        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        // Retrieve the delete button and set its click listener
        Button cardButton = view.findViewById(R.id.buttonCard);
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.ValTicket))
                .setView(view)
                .setPositiveButton(getString(R.string.Save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Get the current date and time for the transaction
                        String lastmodified = getCurrentDateTime();
                        String id= Transaction_Id;



                        double UnitPrice= mDatabaseHelper.getItemPrice(id);



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
            if (deleted) {
                Toast.makeText(getActivity(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                returnHome();
            } else {
                Toast.makeText(getActivity(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            }
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
}
