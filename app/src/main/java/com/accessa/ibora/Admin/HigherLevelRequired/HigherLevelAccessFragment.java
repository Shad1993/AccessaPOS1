package com.accessa.ibora.Admin.HigherLevelRequired;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.items.AddItemActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HigherLevelAccessFragment extends Fragment {
private  EditText searchEditText;


    private DBManager dbManager;
    private LevelAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private ImageView arrow;
    private DatabaseHelper mDatabaseHelper;

    final String[] froms = new String[]{DatabaseHelper._ID, DatabaseHelper.Name, DatabaseHelper.LongDescription, DatabaseHelper.Price};
    final int[] tos = new int[]{R.id.id, R.id.name, R.id.LongDescription, R.id.price};

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_access, container, false);
        // Get the current locale

        // Spinner
        spinner = view.findViewById(R.id.spinner);

        //arrow
        arrow=view.findViewById(R.id.spinner_icon);
        // Retrieve the items from the database
        mDatabaseHelper = new DatabaseHelper(getContext());



        // Create a custom list of levels
        List<String> levels = new ArrayList<>();
        levels.add(getString(R.string.AllLevels)); // Optional: Add "All Items" first
        for (int i = 1; i <= 6; i++) {
            levels.add("Level " + i);  // Adding "Level 1" to "Level 5"
        }

        // Set the spinner adapter with the custom list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, levels) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current view for the spinner
                View view = super.getView(position, convertView, parent);

                // Get the TextView from the default layout (android.R.id.text1)
                TextView textView = view.findViewById(android.R.id.text1);

                // Set the text color to white
                textView.setTextColor(getResources().getColor(R.color.white));

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Get the dropdown view for the spinner
                View view = super.getDropDownView(position, convertView, parent);

                // Get the TextView from the default layout (android.R.id.text1)
                TextView textView = view.findViewById(android.R.id.text1);

                // Set the text color to white
                textView.setTextColor(getResources().getColor(R.color.white));

                return view;
            }
        };

        spinner.setAdapter(adapter);


        // Set a listener for item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                // Handle the selected item here
                filterRecyclerView(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
                filterRecyclerView(null); // Remove any applied filter
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick(); // Programmatically open the dropdown list

            }
        });
        // RecyclerView
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseHelper = new DatabaseHelper(getContext());

        // Create a list to store the six levels
         levels = new ArrayList<>();

        // Create a list to store the six levels
         levels = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            levels.add("Level " + i);  // Add "Level 1" to "Level 5"
        }

        // Pass the levels list to the new LevelAdapter
        mAdapter = new LevelAdapter(getActivity(), levels);
        mRecyclerView.setAdapter(mAdapter);  // Set the new adapter to the RecyclerView


        // Empty state
        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif()
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

        // SearchView


        dbManager = new DBManager(getContext());
        dbManager.open();
        Cursor cursor1 = dbManager.fetch();



        // Set item click listener for RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView idTextView = view.findViewById(R.id.textView);

                        String levelid = idTextView.getText().toString();

                        // Remove "Level" and trim any extra spaces
                        String levelNumberStr = levelid.replaceAll("Level", "").trim();
                        int levelNumber = Integer.parseInt(levelNumberStr);

                        // Inflate the custom layout for the dialog
                        LayoutInflater inflater = LayoutInflater.from(requireContext());
                        View dialogView = inflater.inflate(R.layout.option_level_control, null);

                        // Get the Switch elements from the custom layout
                        Switch switchCompanyInfo = dialogView.findViewById(R.id.switchCompanyInfo);
                        Switch switchRightAccess = dialogView.findViewById(R.id.switchRightAccess);
                        Switch switchPeopleManagement = dialogView.findViewById(R.id.switchPeopleManagement);
                        Switch switchPOSNumber = dialogView.findViewById(R.id.switchPOSNumber);

                        Switch switchSales = dialogView.findViewById(R.id.switchSales);
                        Switch switchReceipts = dialogView.findViewById(R.id.switchReceipts);
                        Switch switchShift = dialogView.findViewById(R.id.switchShift);
                        Switch switchItems = dialogView.findViewById(R.id.switchItems);



                        Switch switchproforma = dialogView.findViewById(R.id.switchProformat);
                        Switch switchdebitnote = dialogView.findViewById(R.id.switchdebitnote);
                        Switch switchcreditnote = dialogView.findViewById(R.id.switchcreditnote);
                        Switch switchSendtokitchen = dialogView.findViewById(R.id.switchsendtokitchen);
                        Switch switchHigherRightAccess = dialogView.findViewById(R.id.switchHigherRightAccess);
                        Switch switchSettings = dialogView.findViewById(R.id.switchSettings);
                        Switch switchAdmin = dialogView.findViewById(R.id.switchAdmin);
                        Switch switchOpenClose = dialogView.findViewById(R.id.switchOpenClose);
                        Switch switchOpenDrawer = dialogView.findViewById(R.id.switchOpenDrawer);
                        Switch switchSetShiftNumber = dialogView.findViewById(R.id.switchSetShiftNumber);
                        Switch switchSelectTable = dialogView.findViewById(R.id.switchSelectTable);
                        Switch switchEditServingsAmount = dialogView.findViewById(R.id.switchEditServingsAmount);
                        Switch switchClearTransaction = dialogView.findViewById(R.id.switchClearTransaction);
                        Switch switchClearPayment = dialogView.findViewById(R.id.switchClearPayment);
                        Switch switchReport = dialogView.findViewById(R.id.switchReport);
                        Switch switchSalesReport = dialogView.findViewById(R.id.switchSalesReport);
                        Switch switchCashierSalesReport = dialogView.findViewById(R.id.switchCashierSalesReport);
                        Switch switchSplitBill = dialogView.findViewById(R.id.switchSplitBill);
                        Switch switchApplyDiscount = dialogView.findViewById(R.id.switchApplyDiscount);
                        Switch switchPrintReport = dialogView.findViewById(R.id.switchPrintReport);
                        Switch switchPrintReceiptDuplicata = dialogView.findViewById(R.id.switchPrintReceiptDuplicata);
                        Switch switchAddItems = dialogView.findViewById(R.id.switchAddItems);
                        Switch switchModifyItems = dialogView.findViewById(R.id.switchModifyItems);
                        Switch switchAddDepartment = dialogView.findViewById(R.id.switchAddDepartment);
                        Switch switchModifyDepartment = dialogView.findViewById(R.id.switchModifyDepartment);
                        Switch switchAddSubDepartment = dialogView.findViewById(R.id.switchAddSubDepartment);
                        Switch switchModifySubDepartment = dialogView.findViewById(R.id.switchModifySubDepartment);
                        Switch switchAddCategory = dialogView.findViewById(R.id.switchAddCategory);
                        Switch switchModifyCategory = dialogView.findViewById(R.id.switchModifyCategory);
                        Switch switchAddSubCategory = dialogView.findViewById(R.id.switchAddSubCategory);
                        Switch switchModifySubCategory = dialogView.findViewById(R.id.switchModifySubCategory);// New fields for vendors, options, supplements, and discounts
                        Switch switchAddVendor = dialogView.findViewById(R.id.switchAddVendor);
                        Switch switchModifyVendor = dialogView.findViewById(R.id.switchModifyVendor);

                        Switch switchAddOption = dialogView.findViewById(R.id.switchAddOption);
                        Switch switchModifyOption = dialogView.findViewById(R.id.switchModifyOption);

                        Switch switchAddSupplement = dialogView.findViewById(R.id.switchAddSupplement);
                        Switch switchModifySupplement = dialogView.findViewById(R.id.switchModifySupplement);

                        Switch switchAddDiscount = dialogView.findViewById(R.id.switchAddDiscount);
                        Switch switchModifyDiscount = dialogView.findViewById(R.id.switchModifyDiscount);
                        Switch switchAddCouponCode = dialogView.findViewById(R.id.switchAddCouponCode);
                        Switch switchModifyCouponCode = dialogView.findViewById(R.id.switchModifyCouponCode);
                        Switch switchQrSettings = dialogView.findViewById(R.id.switchQrSettings);
                        Switch switchLedDisplaySettings = dialogView.findViewById(R.id.switchLedDisplaySettings);
                        Switch switchPaymentMethods = dialogView.findViewById(R.id.switchPaymentMethods);
                        Switch switchPopSettings = dialogView.findViewById(R.id.switchPopSettings);
                        Switch switchBuyer = dialogView.findViewById(R.id.switchBuyer);
                        Switch switchMra = dialogView.findViewById(R.id.switchMra);
                        Switch switchRoomsAndTables = dialogView.findViewById(R.id.switchRoomsAndTables);
                        Switch switchPrinterSettings = dialogView.findViewById(R.id.switchPrinterSettings);
                        Switch switchLanguages = dialogView.findViewById(R.id.switchLanguages);
                        Switch switchServerSettings = dialogView.findViewById(R.id.switchserversettings);
                        Switch switchAddQr = dialogView.findViewById(R.id.switchAddQr);
                        Switch switchModifyQr = dialogView.findViewById(R.id.switchModifyQr);
                        Switch switchAddPaymentMethod = dialogView.findViewById(R.id.switchAddPaymentMethod);
                        Switch switchModifyPaymentMethod = dialogView.findViewById(R.id.switchModifyPaymentMethod);
                        Switch switchAddBuyer = dialogView.findViewById(R.id.switchAddBuyer);
                        Switch switchModifyBuyer = dialogView.findViewById(R.id.switchModifyBuyer);
                        Switch switchEditMraSettings = dialogView.findViewById(R.id.switchEditMraSettings);
                        Switch switchSyncRooms  = dialogView.findViewById(R.id.switchSyncRooms);
                        Switch switchAddRoomsAndTables = dialogView.findViewById(R.id.switchAddRoomsAndTables);
                        Switch switchModifyRoomsAndTables = dialogView.findViewById(R.id.switchModifyRoomsAndTables);
                        Switch switchAddUser  = dialogView.findViewById(R.id.switchAddUser);
                        Switch switchModifyUser  = dialogView.findViewById(R.id.switchModifyUser);
                        Switch switchSync  = dialogView.findViewById(R.id.switchSync);

                        // Load saved preferences for switches
                        // Load saved preferences for switches
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                        switchCompanyInfo.setChecked(sharedPreferences.getBoolean("companyInfo_" + levelNumber, false));
                        switchRightAccess.setChecked(sharedPreferences.getBoolean("rightAccess_" + levelNumber, false));
                        switchPeopleManagement.setChecked(sharedPreferences.getBoolean("peopleManagement_" + levelNumber, false));
                        switchHigherRightAccess.setChecked(sharedPreferences.getBoolean("HigherLevel_" + levelNumber, true));
                        switchPOSNumber.setChecked(sharedPreferences.getBoolean("posNumber_" + levelNumber, false));
                        switchSales.setChecked(sharedPreferences.getBoolean("sales_" + levelNumber, false));
                        switchReceipts.setChecked(sharedPreferences.getBoolean("Receipts_" + levelNumber, false));
                        switchShift.setChecked(sharedPreferences.getBoolean("shift_" + levelNumber, false));
                        switchItems.setChecked(sharedPreferences.getBoolean("Items_" + levelNumber, false));


                        switchproforma.setChecked(sharedPreferences.getBoolean("proforma_" + levelNumber, false));
                        switchdebitnote.setChecked(sharedPreferences.getBoolean("debitnote_" + levelNumber, false));
                        switchcreditnote.setChecked(sharedPreferences.getBoolean("creditnote_" + levelNumber, false));
                        switchSendtokitchen.setChecked(sharedPreferences.getBoolean("sendtokitchen_" + levelNumber, false));


                        switchSettings.setChecked(sharedPreferences.getBoolean("settings_" + levelNumber, false));
                        switchAdmin.setChecked(sharedPreferences.getBoolean("admin_" + levelNumber, false));
                        switchOpenClose.setChecked(sharedPreferences.getBoolean("openClose_" + levelNumber, false));
                        switchOpenDrawer.setChecked(sharedPreferences.getBoolean("openDrawer_" + levelNumber, false));
                        switchSetShiftNumber.setChecked(sharedPreferences.getBoolean("setShiftNumber_" + levelNumber, false));
                        switchSelectTable.setChecked(sharedPreferences.getBoolean("selectTable_" + levelNumber, false));
                        switchEditServingsAmount.setChecked(sharedPreferences.getBoolean("editServingsAmount_" + levelNumber, false));
                        switchClearTransaction.setChecked(sharedPreferences.getBoolean("clearTransaction_" + levelNumber, false));
                        switchClearPayment.setChecked(sharedPreferences.getBoolean("clearPayment_" + levelNumber, false));
                        switchReport.setChecked(sharedPreferences.getBoolean("report_" + levelNumber, false));
                        switchSalesReport.setChecked(sharedPreferences.getBoolean("salesReport_" + levelNumber, false));
                        switchCashierSalesReport.setChecked(sharedPreferences.getBoolean("cashierSalesReport_" + levelNumber, false));
                        switchSplitBill.setChecked(sharedPreferences.getBoolean("splitBill_" + levelNumber, false));
                        switchApplyDiscount.setChecked(sharedPreferences.getBoolean("applyDiscount_" + levelNumber, false));
                        switchPrintReport.setChecked(sharedPreferences.getBoolean("printReport_" + levelNumber, false));
                        switchPrintReceiptDuplicata.setChecked(sharedPreferences.getBoolean("printReceiptDuplicata_" + levelNumber, false));
                        switchAddItems.setChecked(sharedPreferences.getBoolean("addItems_" + levelNumber, false));
                        switchModifyItems.setChecked(sharedPreferences.getBoolean("modifyItems_" + levelNumber, true));
                        switchAddDepartment.setChecked(sharedPreferences.getBoolean("addDepartment_" + levelNumber, false));
                        switchModifyDepartment.setChecked(sharedPreferences.getBoolean("modifyDepartment_" + levelNumber, false));
                        switchAddSubDepartment.setChecked(sharedPreferences.getBoolean("addSubDepartment_" + levelNumber, false));
                        switchModifySubDepartment.setChecked(sharedPreferences.getBoolean("modifySubDepartment_" + levelNumber, false));
                        switchAddCategory.setChecked(sharedPreferences.getBoolean("addCategory_" + levelNumber, false));
                        switchModifyCategory.setChecked(sharedPreferences.getBoolean("modifyCategory_" + levelNumber, false));
                        switchAddSubCategory.setChecked(sharedPreferences.getBoolean("addSubCategory_" + levelNumber, false));
                        switchModifySubCategory.setChecked(sharedPreferences.getBoolean("modifySubCategory_" + levelNumber, false));
// New Switches for Vendors, Options, Supplements, and Discounts
                        switchAddVendor.setChecked(sharedPreferences.getBoolean("addVendor_" + levelNumber, false));
                        switchModifyVendor.setChecked(sharedPreferences.getBoolean("modifyVendor_" + levelNumber, false));

                        switchAddOption.setChecked(sharedPreferences.getBoolean("addOption_" + levelNumber, false));
                        switchModifyOption.setChecked(sharedPreferences.getBoolean("modifyOption_" + levelNumber, false));

                        switchAddSupplement.setChecked(sharedPreferences.getBoolean("addSupplement_" + levelNumber, false));
                        switchModifySupplement.setChecked(sharedPreferences.getBoolean("modifySupplement_" + levelNumber, false));

                        switchAddDiscount.setChecked(sharedPreferences.getBoolean("addDiscount_" + levelNumber, false));
                        switchModifyDiscount.setChecked(sharedPreferences.getBoolean("modifyDiscount_" + levelNumber, false));

                        switchAddCouponCode.setChecked(sharedPreferences.getBoolean("addCouponCode_" + levelNumber, false));
                        switchModifyCouponCode.setChecked(sharedPreferences.getBoolean("modifyCouponCode_" + levelNumber, false));
                        switchQrSettings.setChecked(sharedPreferences.getBoolean("qrSettings_" + levelNumber, false));
                        switchLedDisplaySettings.setChecked(sharedPreferences.getBoolean("ledDisplaySettings_" + levelNumber, false));
                        switchPaymentMethods.setChecked(sharedPreferences.getBoolean("paymentMethods_" + levelNumber, false));
                        switchPopSettings.setChecked(sharedPreferences.getBoolean("popSettings_" + levelNumber, false));
                        switchBuyer.setChecked(sharedPreferences.getBoolean("buyer_" + levelNumber, false));
                        switchMra.setChecked(sharedPreferences.getBoolean("mra_" + levelNumber, false));
                        switchRoomsAndTables.setChecked(sharedPreferences.getBoolean("roomsAndTables_" + levelNumber, false));
                        switchPrinterSettings.setChecked(sharedPreferences.getBoolean("printerSettings_" + levelNumber, false));
                        switchLanguages.setChecked(sharedPreferences.getBoolean("languages_" + levelNumber, false));
                        switchServerSettings.setChecked(sharedPreferences.getBoolean("serversettings_" + levelNumber, false));
                        switchAddQr.setChecked(sharedPreferences.getBoolean("addQr_" + levelNumber, false));
                        switchModifyQr.setChecked(sharedPreferences.getBoolean("modifyQr_" + levelNumber, false));
                        switchAddPaymentMethod.setChecked(sharedPreferences.getBoolean("addPaymentMethod_" + levelNumber, false));
                        switchModifyPaymentMethod.setChecked(sharedPreferences.getBoolean("modifyPaymentMethod_" + levelNumber, false));
                        switchAddBuyer.setChecked(sharedPreferences.getBoolean("addBuyer_" + levelNumber, false));
                        switchModifyBuyer.setChecked(sharedPreferences.getBoolean("modifyBuyer_" + levelNumber, false));
                        switchEditMraSettings.setChecked(sharedPreferences.getBoolean("editMraSettings_" + levelNumber, false));
                        switchAddRoomsAndTables.setChecked(sharedPreferences.getBoolean("addRoomsAndTables_" + levelNumber, false));
                        switchModifyRoomsAndTables.setChecked(sharedPreferences.getBoolean("modifyRoomsAndTables_" + levelNumber, false));
                        switchSyncRooms.setChecked(sharedPreferences.getBoolean("SyncRoomsAndTables_" + levelNumber, false));
                        switchAddUser.setChecked(sharedPreferences.getBoolean("addUser_" + levelNumber, false));
                        switchModifyUser.setChecked(sharedPreferences.getBoolean("modifyUser_" + levelNumber, false));
                        switchSync.setChecked(sharedPreferences.getBoolean("syncDatabase_" + levelNumber, false));

                        // Show the dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Set Higher Access for Level " + levelNumber)
                                .setView(dialogView)
                                .setPositiveButton("Save", (dialog, which) -> {
                                    // Save the switch states in SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("companyInfo_" + levelNumber, switchCompanyInfo.isChecked());
                                    editor.putBoolean("rightAccess_" + levelNumber, switchRightAccess.isChecked());
                                    editor.putBoolean("peopleManagement_" + levelNumber, switchPeopleManagement.isChecked());
                                    editor.putBoolean("HigherLevel_" + levelNumber, switchHigherRightAccess.isChecked());
                                    editor.putBoolean("posNumber_" + levelNumber, switchPOSNumber.isChecked());
                                    editor.putBoolean("sales_" + levelNumber, switchSales.isChecked());
                                    editor.putBoolean("Receipts_" + levelNumber, switchReceipts.isChecked());
                                    editor.putBoolean("shift_" + levelNumber, switchShift.isChecked());
                                    editor.putBoolean("Items_" + levelNumber, switchItems.isChecked());
                                    editor.putBoolean("proforma_" + levelNumber, switchproforma.isChecked());
                                    editor.putBoolean("debitnote_" + levelNumber, switchdebitnote.isChecked());
                                    editor.putBoolean("creditnote_" + levelNumber, switchcreditnote.isChecked());
                                    editor.putBoolean("sendtokitchen_" + levelNumber, switchSendtokitchen.isChecked());

                                    editor.putBoolean("settings_" + levelNumber, switchSettings.isChecked());
                                    editor.putBoolean("admin_" + levelNumber, switchAdmin.isChecked());
                                    editor.putBoolean("openClose_" + levelNumber, switchOpenClose.isChecked());
                                    editor.putBoolean("openDrawer_" + levelNumber, switchOpenDrawer.isChecked());
                                    editor.putBoolean("setShiftNumber_" + levelNumber, switchSetShiftNumber.isChecked());
                                    editor.putBoolean("selectTable_" + levelNumber, switchSelectTable.isChecked());
                                    editor.putBoolean("editServingsAmount_" + levelNumber, switchEditServingsAmount.isChecked());
                                    editor.putBoolean("clearTransaction_" + levelNumber, switchClearTransaction.isChecked());
                                    editor.putBoolean("clearPayment_" + levelNumber, switchClearPayment.isChecked());
                                    editor.putBoolean("report_" + levelNumber, switchReport.isChecked());
                                    editor.putBoolean("salesReport_" + levelNumber, switchSalesReport.isChecked());
                                    editor.putBoolean("cashierSalesReport_" + levelNumber, switchCashierSalesReport.isChecked());
                                    editor.putBoolean("splitBill_" + levelNumber, switchSplitBill.isChecked());
                                    editor.putBoolean("applyDiscount_" + levelNumber, switchApplyDiscount.isChecked());
                                    editor.putBoolean("printReport_" + levelNumber, switchPrintReport.isChecked());
                                    editor.putBoolean("printReceiptDuplicata_" + levelNumber, switchPrintReceiptDuplicata.isChecked());
                                    editor.putBoolean("addItems_" + levelNumber, switchAddItems.isChecked());
                                    editor.putBoolean("modifyItems_" + levelNumber, switchModifyItems.isChecked());
                                    editor.putBoolean("addDepartment_" + levelNumber, switchAddDepartment.isChecked());
                                    editor.putBoolean("modifyDepartment_" + levelNumber, switchModifyDepartment.isChecked());
                                    editor.putBoolean("addSubDepartment_" + levelNumber, switchAddSubDepartment.isChecked());
                                    editor.putBoolean("modifySubDepartment_" + levelNumber, switchModifySubDepartment.isChecked());
                                    editor.putBoolean("addCategory_" + levelNumber, switchAddCategory.isChecked());
                                    editor.putBoolean("modifyCategory_" + levelNumber, switchModifyCategory.isChecked());
                                    editor.putBoolean("addSubCategory_" + levelNumber, switchAddSubCategory.isChecked());
                                    editor.putBoolean("modifySubCategory_" + levelNumber, switchModifySubCategory.isChecked());

                                    editor.putBoolean("addVendor_" + levelNumber, switchAddVendor.isChecked());
                                    editor.putBoolean("modifyVendor_" + levelNumber, switchModifyVendor.isChecked());

                                    editor.putBoolean("addOption_" + levelNumber, switchAddOption.isChecked());
                                    editor.putBoolean("modifyOption_" + levelNumber, switchModifyOption.isChecked());

                                    editor.putBoolean("addSupplement_" + levelNumber, switchAddSupplement.isChecked());
                                    editor.putBoolean("modifySupplement_" + levelNumber, switchModifySupplement.isChecked());

                                    editor.putBoolean("addDiscount_" + levelNumber, switchAddDiscount.isChecked());
                                    editor.putBoolean("modifyDiscount_" + levelNumber, switchModifyDiscount.isChecked());

                                    editor.putBoolean("addCouponCode_" + levelNumber, switchAddCouponCode.isChecked());
                                    editor.putBoolean("modifyCouponCode_" + levelNumber, switchModifyCouponCode.isChecked());
                                    editor.putBoolean("qrSettings_" + levelNumber, switchQrSettings.isChecked());
                                    editor.putBoolean("ledDisplaySettings_" + levelNumber, switchLedDisplaySettings.isChecked());
                                    editor.putBoolean("paymentMethods_" + levelNumber, switchPaymentMethods.isChecked());
                                    editor.putBoolean("popSettings_" + levelNumber, switchPopSettings.isChecked());
                                    editor.putBoolean("buyer_" + levelNumber, switchBuyer.isChecked());
                                    editor.putBoolean("mra_" + levelNumber, switchMra.isChecked());
                                    editor.putBoolean("roomsAndTables_" + levelNumber, switchRoomsAndTables.isChecked());
                                    editor.putBoolean("printerSettings_" + levelNumber, switchPrinterSettings.isChecked());
                                    editor.putBoolean("languages_" + levelNumber, switchLanguages.isChecked());
                                    editor.putBoolean("serversettings_" + levelNumber, switchServerSettings.isChecked());
                                    editor.putBoolean("addQr_" + levelNumber, switchAddQr.isChecked());
                                    editor.putBoolean("modifyQr_" + levelNumber, switchModifyQr.isChecked());
                                    editor.putBoolean("addPaymentMethod_" + levelNumber, switchAddPaymentMethod.isChecked());
                                    editor.putBoolean("modifyPaymentMethod_" + levelNumber, switchModifyPaymentMethod.isChecked());
                                    editor.putBoolean("addBuyer_" + levelNumber, switchAddBuyer.isChecked());
                                    editor.putBoolean("modifyBuyer_" + levelNumber, switchModifyBuyer.isChecked());
                                    editor.putBoolean("editMraSettings_" + levelNumber, switchEditMraSettings.isChecked());
                                    editor.putBoolean("addRoomsAndTables_" + levelNumber, switchAddRoomsAndTables.isChecked());
                                    editor.putBoolean("modifyRoomsAndTables_" + levelNumber, switchModifyRoomsAndTables.isChecked());
                                    editor.putBoolean("SyncRoomsAndTables_" + levelNumber, switchSyncRooms.isChecked());
                                    editor.putBoolean("addUser_" + levelNumber, switchAddUser.isChecked());
                                    editor.putBoolean("modifyUser_" + levelNumber, switchModifyUser.isChecked());
                                    editor.putBoolean("syncDatabase_" + levelNumber, switchSync.isChecked());



                                    editor.apply();


                                })
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );



        return view;
    }
    // Filter the RecyclerView based on the selected item
    private void filterRecyclerView(String selectedItem) {
        Cursor filteredCursor;
        if (selectedItem == null || selectedItem.equals(getString(R.string.AllItems))) {
            filteredCursor = mDatabaseHelper.getAllItems();
        } else {
            filteredCursor = mDatabaseHelper.searchItems(selectedItem);
        }


        // Show or hide the empty state
        showEmptyState(mAdapter.getItemCount() <= 0);
    }

    // Show or hide the empty state based on the item count
    private void showEmptyState(boolean showEmpty) {
        AppCompatImageView imageView = getView().findViewById(R.id.empty_image_view);
        Glide.with(getContext()).asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);
        FrameLayout emptyFrameLayout = getView().findViewById(R.id.empty_frame_layout);
        if (showEmpty) {
            mRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
    }
    public void openNewActivity() {
        Configuration configuration = getResources().getConfiguration();
        Locale currentLocale = configuration.locale;


        // Start the AddItemActivity
        Intent intent = new Intent(requireContext(), AddItemActivity.class);
        intent.putExtra("locale", currentLocale.toString());
        startActivity(intent);
    }



}