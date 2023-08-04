package com.accessa.ibora.QR;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.POP.PopMobileDialogFragment;
import com.accessa.ibora.POP.PopQRDialogFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.ItemAdapter;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.sales.ticket.Checkout.validateticketDialogFragment;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Locale;


public class QRFragment extends Fragment {
    public interface DataPassListener {
        void onDataPass(String name, String id, String QR);
    }
    private DataPassListener dataPassListener;
    private RecyclerView mRecyclerView;
    private QRGridAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String cashierId;
    private DBManager dbManager;
    public TextView nameTextView;
    public TextView idTextView;

    private static final String TRANSACTION_ID_KEY = "transaction_id";

    private  String transactionIdInProgress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_fragment, container, false);


        int numberOfColumns = 3;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);



        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashierId = sharedPreference.getString("cashorId", null);
        dbManager = new DBManager(getContext());
        dbManager.open();


        mRecyclerView = view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mDatabaseHelper = new DatabaseHelper(getContext());
        loadQRData();
        Cursor cursor = mDatabaseHelper.getAllQR();

        mAdapter = new QRGridAdapter(getContext(), cursor);
        mRecyclerView.setAdapter(mAdapter);

        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(this)
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);
        if (mAdapter.getItemCount() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idTextView = view.findViewById(R.id.id_text_view);
                TextView NameEditText = view.findViewById(R.id.name_text_view);
                TextView QrEditText = view.findViewById(R.id.code);

                String id1 = idTextView.getText().toString();
                String id = idTextView.getText().toString();
                String QR = QrEditText.getText().toString();
                String name = NameEditText.getText().toString();
                if(id !=null && (id.equals("1") && name.equals("POP")))
                {
                  showPopOptionsDialog(); // Call the showPopOptionsDialog() method here for "POP" button click

                }else {

                    showSecondaryScreen(name,id, QR);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Handle long item click
                // ...
            }
        }));

        return view;

    }
    private void showPopOptionsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Select Payment Method");
        String[] options = {"Pay by mobile number", "Pay by QR code"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's selection here
                if (which == 0) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    builder.setView(R.layout.validate_num);

                    // Set up the dialog
                    androidx.appcompat.app.AlertDialog dialogmob = builder.create();
                    dialogmob.show();

                    // Get references to the views in the popup layout
                    EditText editTerminalNo = dialogmob.findViewById(R.id.editTerminalNo);
                    EditText editCompanyName = dialogmob.findViewById(R.id.editCompanyName);
                    Button btnInsert = dialogmob.findViewById(R.id.btnInsert);

                    // Set up button click listener
                    btnInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Retrieve the entered mobile number from the EditText
                            String mobileNumber = editTerminalNo.getText().toString();

                            // Create the PopMobileDialogFragment instance and pass the mobile number as an argument
                            PopMobileDialogFragment popMobileDialogFragment = PopMobileDialogFragment.newInstance(mobileNumber);
                            // Hide the keyboard before showing the dialog
                            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            // Show the PopMobileDialogFragment
                            popMobileDialogFragment.show(getChildFragmentManager(), "pop_mobile_dialog");

                            // Dismiss the dialog
                            dialog.dismiss();
                            // Dismiss the dialogmob
                            dialogmob.dismiss();

                        }
                    });
                } else if (which == 1) {
                    // User selected "Pay by QR code"
                    // Perform the action for this option
                    // For example, open a QR code scanner activity
                    Cursor cursor = mDatabaseHelper.getTransactionHeader();
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);

                        double totalAmount = cursor.getDouble(columnIndexTotalAmount);

                        // Format the double to a string with two decimal places
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String formattedTotalAmount = decimalFormat.format(totalAmount);
                        Log.e("total amount", formattedTotalAmount);
                        String fileNameMerchName = "merch_name.txt";
                        String fileNamePhone = "mob_num.txt";
                        String fileNametill = "till_num.txt";

                        String MerchantName = readTextFromFile(fileNameMerchName);
                        String TillNo = readTextFromFile(fileNametill);
                        String PhoneNum = readTextFromFile(fileNamePhone);
                        String amountsVariation = countAndConcatenate(formattedTotalAmount);
                        String QR=    "00020101021126630009mu.maucas0112BKONMUM0XXXX021103011065958031500000000000005252047278530348054" +amountsVariation+"5802MU5912"+MerchantName+"6015Agalega North I62220211"+PhoneNum+"0703"+TillNo+"6304BE4F";
                        Log.e("qrstring", QR);
                        showSecondaryScreen("POP","1",QR);


                        // Create the PopMobileDialogFragment instance and pass the mobile number as an argument
                        PopQRDialogFragment popMobileDialogFragment = PopQRDialogFragment.newInstance(QR);
                        // Hide the keyboard before showing the dialog
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                        // Show the PopMobileDialogFragment
                        popMobileDialogFragment.show(getChildFragmentManager(), "pop_qr_dialog");

                        // Dismiss the dialog
                        dialog.dismiss();
                    }


                }
            }
        });
        builder.show();
    }


    public static String countAndConcatenate(String input) {
        int charCount = input.length();
        String formattedCharCount = String.format("%02d", charCount);
        return formattedCharCount + input;
    }

    private void showSecondaryScreen(String name,String id, String QR) {
        // Obtain a real secondary screen
        Display presentationDisplay = getPresentationDisplay();
        String formattedTaxAmount = null,formattedTotalAmount = null;
        if (presentationDisplay != null) {
            // Create an instance of SeconScreenDisplay using the obtained display
            SeconScreenDisplay secondaryDisplay = new SeconScreenDisplay(getActivity(), presentationDisplay);

            // Show the secondary display
            secondaryDisplay.show();
            Cursor cursor = mDatabaseHelper.getTransactionHeader();
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexTotalAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TTC);
                int columnIndexTotalTaxAmount = cursor.getColumnIndex(DatabaseHelper.TRANSACTION_TOTAL_TX_1);

                double totalAmount = cursor.getDouble(columnIndexTotalAmount);
                double taxTotalAmount = cursor.getDouble(columnIndexTotalTaxAmount);

                formattedTaxAmount = String.format("%.2f", taxTotalAmount);
                formattedTotalAmount = String.format("%.2f", totalAmount);
            }
            // Get the selected item from the RecyclerView
            String selectedName = name;
            String selectedQR = QR;
            // Convert the QR code string to a Bitmap
            Bitmap qrBitmap = generateQRCodeBitmap(selectedQR);
            // Update the text and QR code on the secondary screen
            secondaryDisplay.updateTextAndQRCode(selectedName, qrBitmap, formattedTaxAmount, formattedTotalAmount);
        } else {
            // Secondary screen not found or not supported
          //  Toast.makeText(getActivity(), "Secondary screen not found or not supported", Toast.LENGTH_SHORT).show();
            dataPassListener.onDataPass(name, id, QR);

        }
    }

    private Bitmap generateQRCodeBitmap(String qrCode) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 400;
        int height = 400;
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(qrCode, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        Bitmap qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return qrBitmap;
    }

    private Display getPresentationDisplay() {
        DisplayManager displayManager = (DisplayManager) requireContext().getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                    && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                    && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                return display;
            }
        }
        return null;
    }

    private void loadQRData() {
        // Retrieve data from the database, including the default item
        Cursor cursor = mDatabaseHelper.getAllQR();

        // Check if the adapter is null
        if (mAdapter != null) {
            // Add the cursor position for the default item
            cursor.moveToPosition(-1); // Move to before the first position

            // Set the adapter with the cursor
            mAdapter.swapCursor(cursor);
        }
    }

    private void showItemDialog(String searchText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the custom dialog layout
        View dialogView = inflater.inflate(R.layout.search_by_barcode, null);
        builder.setView(dialogView);

        // Find the RecyclerView in the dialog layout
        RecyclerView dialogRecyclerView = dialogView.findViewById(R.id.dialog_recycler_view);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get the item data for the RecyclerView
        Cursor itemCursor = mDatabaseHelper.searchqr(searchText);
        ItemAdapter dialogAdapter = new ItemAdapter(getActivity(), itemCursor);
        dialogRecyclerView.setAdapter(dialogAdapter);

        // Set the OnItemTouchListener on the RecyclerView
        dialogRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), dialogRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Handle item click
                        // ...
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Handle long item click
                        // ...
                    }
                }));

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private  String readTextFromFile(String fileName) {
        try {
            FileInputStream fileInputStream = getContext().openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataPassListener = (DataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataPassListener");
        }
    }
}
