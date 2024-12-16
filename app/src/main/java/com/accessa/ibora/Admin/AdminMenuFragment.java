package com.accessa.ibora.Admin;

import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_POS_Num;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_TerminalNo;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_POS_ACCESS;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_STD_ACCESS;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.accessa.ibora.Admin.CompanyInfo.CompanyInfoFragment;
import com.accessa.ibora.Admin.HigherLevelRequired.HigherLevelAccessFragment;
import com.accessa.ibora.Admin.People.PeopleFragment;
import com.accessa.ibora.Admin.RightAccess.RightAccessFragment;
import com.accessa.ibora.DeviceInfo;
import com.accessa.ibora.R;

import com.accessa.ibora.Sync.MasterSync.MssqlDataSync;
import com.accessa.ibora.Sync.SyncService;
import com.accessa.ibora.Sync.Syncforold;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.CustomAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// extended from compatibility Fragment for pre-HC fragment support
public class AdminMenuFragment extends Fragment {
    private static final String POSNumber="posNumber";
    private String Shopname,PosNum;
    private String toolbarTitle;
    private ListView simpleList;
    private String[] menuList;
    private int[] icons;

    private SharedPreferences sharedPreferences;
    private String cashorId;
    private String cashorName;
    private TextView CompanyName;
    private String ShopName;
    private DatabaseHelper mDatabaseHelper;
    EditText pinEditText;
    // activity listener
    private OnMenufragListener menufragListener;



    // interface for communication with activity
    public interface OnMenufragListener {
        public void onMenufrag(String s);
    }

    // onAttach
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            menufragListener = (OnMenufragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnMenufragListener");
        }
    }

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        toolbarTitle = getString(R.string.Admin);
        super.onCreate(savedInstanceState);
        toolbarTitle = getString(R.string.Admin);
        menuList = new String[]{
                getString(R.string.People),
                getString(R.string.Rights),
                getString(R.string.HigherLevelAccess),
                getString(R.string.CompanyInfo),
                getString(R.string.sync),
                getString(R.string.POSINFO)
        };
        icons = new int[]{
                R.drawable.usericon,
                R.drawable.key,
                R.drawable.higherlevelblue,
                R.drawable.companyicon,
                R.drawable.sync,
                R.drawable.pos
        };

        setHasOptionsMenu(true);
        Fragment newFragment = new PeopleFragment();
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
        fragmentTransaction.addToBackStack(newFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(getActivity());
// Retrieve the shared preferences
        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        ShopName = sharedPreferences.getString("ShopName", null); // Retrieve company name
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_menu,container,false);






//menu items
        simpleList = (ListView) view.findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), menuList, icons);
        simpleList.setAdapter(customAdapter);

        String toolbarTitle = getString(R.string.Admin); // Provide a default title

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the shared preferences
                sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                String cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level

                // Assuming you have a way to get the levelNumber
                int levelNumber = Integer.parseInt(cashorlevel); // Get the user level
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

                // Permission checks
                boolean canAccessCompanyInfo = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "companyInfo_", levelNumber);
                boolean canAccessRightAccess = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "rightAccess_", levelNumber);
                boolean canAccessHigherLevelAccess = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "HigherLevel_", levelNumber);
                boolean canAccessPeopleManagement = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "peopleManagement_", levelNumber);
                boolean canAccessPOSNumber = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "posNumber_", levelNumber);
                boolean canAccessSyncDatabase = mDatabaseHelper.getPermissionWithDefault(sharedPreferences, "syncDatabase_", levelNumber);

                // Permission checks
                boolean canHigherAccessCompanyInfo = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "companyInfo_", levelNumber);
                boolean canHigherAccessRightAccess = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "rightAccess_", levelNumber);
                boolean canHigherAccessHigherLevelAccess = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "HigherLevel_", levelNumber);
                boolean canHigherAccessPeopleManagement = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "peopleManagement_", levelNumber);
                boolean canHigherAccessPOSNumber = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "posNumber_", levelNumber);
                boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, "syncDatabase_", levelNumber);

                switch (position) {
                    case 0:
                        if (canHigherAccessPeopleManagement) {


                            showPinDialog("peopleManagement_", () -> {
                                String toolbarTitle = getString(R.string.People);
                                Fragment newFragment = new PeopleFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                                fragmentTransaction.addToBackStack(newFragment.toString());
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();
                            });

                        }else if (!canHigherAccessPeopleManagement && canAccessPeopleManagement) {

                            String toolbarTitle = getString(R.string.People);
                            Fragment newFragment = new PeopleFragment();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        } else {


                            showPermissionDeniedDialog(); // Method to show a message to the user
                        }
                        break;

                    case 1:
                        if (canHigherAccessRightAccess) {


                            showPinDialog("rightAccess_", () -> {
                                String toolbarTitle = getString(R.string.Rights);
                                Fragment newFragment = new RightAccessFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                                fragmentTransaction.addToBackStack(newFragment.toString());
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();
                            });

                        }else   if (! canHigherAccessRightAccess && canAccessRightAccess) {
                            String toolbarTitle = getString(R.string.Rights);
                            Fragment newFragment = new RightAccessFragment();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        } else {
                            showPermissionDeniedDialog();
                        }
                        break;
                    case 2:
                        if (canHigherAccessHigherLevelAccess) {
                            showPinDialog("HigherLevel_", () -> {
                                String toolbarTitle = getString(R.string.HigherLevelAccess);
                                Fragment newFragment = new HigherLevelAccessFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                                fragmentTransaction.addToBackStack(newFragment.toString());
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();
                            });
                        }
                        else if (!canHigherAccessHigherLevelAccess && canAccessHigherLevelAccess) {
                            String toolbarTitle = getString(R.string.HigherLevelAccess);
                            Fragment newFragment = new HigherLevelAccessFragment();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();

                        } else {
                            showPermissionDeniedDialog();
                        }
                        break;
                    case 3:
                        if (canHigherAccessCompanyInfo) {
                            showPinDialog("companyInfo_", () -> {
                                String  toolbarTitle = getString(R.string.CompanyInfo);
                                Fragment newFragment = new CompanyInfoFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                                fragmentTransaction.addToBackStack(newFragment.toString());
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();
                            });
                            }else
                        if (!canHigherAccessCompanyInfo && canAccessCompanyInfo) {
                            String  toolbarTitle = getString(R.string.CompanyInfo);
                            Fragment newFragment = new CompanyInfoFragment();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        } else {
                            showPermissionDeniedDialog();
                        }
                        break;

                    case 4:
                        if (canHigherAccessSyncDatabase) {
                            showPinDialog("syncDatabase_", () -> {
                                // Create and show sync dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setView(R.layout.dialog_layout_sync);

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                Button buttonSynchronize = dialog.findViewById(R.id.buttonSynchronize);
                                if (buttonSynchronize != null) {
                                    buttonSynchronize.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Sync data with MSSQL
                                            MssqlDataSync mssqlDataSync = new MssqlDataSync();
                                            mssqlDataSync.syncTransactionsFromSQLiteToMSSQL(requireContext());
                                            mssqlDataSync.syncTransactionHeaderFromMSSQLToSQLite(requireContext());
                                            mssqlDataSync.syncInvoiceSettlementFromMSSQLToSQLite(requireContext());
                                            mssqlDataSync.syncCountingReportDataFromSQLiteToMSSQL(requireContext());
                                            mssqlDataSync.syncCashReportDataFromSQLiteToMSSQL(requireContext());
                                            mssqlDataSync.syncFinancialReportDataFromSQLiteToMSSQL(requireContext());


                                            mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.TABLE_NAME);
                                            mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.COST_TABLE_NAME);
                                            mDatabaseHelper.deleteAllDataFromTableTable(DatabaseHelper.TABLES);
                                            mDatabaseHelper.deleteAllDataFromRoomsTable(DatabaseHelper.ROOMS);
                                            mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.CAT_TABLE_NAME);
                                            mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.TABLE_NAME_STD_ACCESS);
                                            mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.SUB_CAT_TABLE_NAME);
                                            // Check Android version and start appropriate sync service
                                            String androidVersion = DeviceInfo.getAndroidVersion();
                                            Log.d("DeviceInfo", "Android Version: " + androidVersion);
                                            if (androidVersion.trim().equals("Android 7.1.1 (API Level 25) - Nougat MR1")) {
                                                Log.d("SyncService", "Starting Syncforold");
                                                Syncforold.startSync(requireContext());
                                            } else {
                                                Log.d("SyncService", "Starting SyncService");
                                                SyncService.startSync(requireContext());
                                            }

                                            dialog.dismiss(); // Close the dialog after synchronization
                                        }
                                    });
                                }
                            });
                            }else
                        if (!canHigherAccessSyncDatabase && canAccessSyncDatabase) {
                            // Create and show sync dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setView(R.layout.dialog_layout_sync);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            Button buttonSynchronize = dialog.findViewById(R.id.buttonSynchronize);
                            if (buttonSynchronize != null) {
                                buttonSynchronize.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Sync data with MSSQL
                                        MssqlDataSync mssqlDataSync = new MssqlDataSync();
                                        mssqlDataSync.syncTransactionsFromSQLiteToMSSQL(requireContext());
                                        mssqlDataSync.syncTransactionHeaderFromMSSQLToSQLite(requireContext());
                                        mssqlDataSync.syncInvoiceSettlementFromMSSQLToSQLite(requireContext());
                                        mssqlDataSync.syncCountingReportDataFromSQLiteToMSSQL(requireContext());
                                        mssqlDataSync.syncCashReportDataFromSQLiteToMSSQL(requireContext());
                                        mssqlDataSync.syncFinancialReportDataFromSQLiteToMSSQL(requireContext());
                                        mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.TABLE_NAME);
                                        mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.COST_TABLE_NAME);
                                        mDatabaseHelper.deleteAllDataFromTableTable(DatabaseHelper.TABLES);
                                        mDatabaseHelper.deleteAllDataFromRoomsTable(DatabaseHelper.ROOMS);
                                        mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.CAT_TABLE_NAME);
                                        mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.TABLE_NAME_STD_ACCESS);
                                        mDatabaseHelper.deleteAllDataFromTable(DatabaseHelper.SUB_CAT_TABLE_NAME);
                                        // Check Android version and start appropriate sync service
                                        String androidVersion = DeviceInfo.getAndroidVersion();
                                        Log.d("DeviceInfo", "Android Version: " + androidVersion);
                                        if (androidVersion.trim().equals("Android 7.1.1 (API Level 25) - Nougat MR1")) {
                                            Log.d("SyncService", "Starting Syncforold");
                                            Syncforold.startSync(requireContext());
                                        } else {
                                            Log.d("SyncService", "Starting SyncService");
                                            SyncService.startSync(requireContext());
                                        }

                                        dialog.dismiss(); // Close the dialog after synchronization
                                    }
                                });
                            }
                        } else {
                            showPermissionDeniedDialog();
                        }
                        break;

                    case 5:
                        if (canHigherAccessPOSNumber) {
                            showPinDialog("posNumber_", () -> {
                                // Create and show POS number dialog
                                // Create and show POS number dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setView(R.layout.popup_insert_pos_access);

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                SharedPreferences sharedPreference = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                                PosNum = sharedPreference.getString(POSNumber, null);

                                EditText editTerminalNo = dialog.findViewById(R.id.editTerminalNo);
                                if (editTerminalNo != null) {
                                    editTerminalNo.setText(PosNum);
                                }

                                Button btnInsert = dialog.findViewById(R.id.btnInsert);
                                if (btnInsert != null) {
                                    btnInsert.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String terminalNo = editTerminalNo.getText().toString();
                                            String shopName = ShopName;
                                            String cashOrId = cashorId;

                                            // Use regex to validate terminalNo input
                                            if (validateData(terminalNo)) {
                                                insertOrUpdatePosAccess(terminalNo, shopName, cashOrId);
                                                dialog.dismiss(); // Close the dialog after inserting/updating
                                            } else {
                                                // Show error message if validation fails
                                                editTerminalNo.setError("Invalid input. Only numbers are allowed.");
                                            }
                                        }
                                    });
                                }
                            });
                            }
                        else if (!canHigherAccessPOSNumber && canAccessPOSNumber) {
                            // Create and show POS number dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setView(R.layout.popup_insert_pos_access);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            sharedPreferences = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
                            PosNum = sharedPreferences.getString(POSNumber, null);

                            EditText editTerminalNo = dialog.findViewById(R.id.editTerminalNo);
                            if (editTerminalNo != null) {
                                editTerminalNo.setText(PosNum);
                            }

                            Button btnInsert = dialog.findViewById(R.id.btnInsert);
                            if (btnInsert != null) {
                                btnInsert.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String terminalNo = editTerminalNo.getText().toString();
                                        String shopName = ShopName;
                                        String cashOrId = cashorId;

                                        // Use regex to validate terminalNo input
                                        if (validateData(terminalNo)) {
                                            insertOrUpdatePosAccess(terminalNo, shopName, cashOrId);
                                            dialog.dismiss(); // Close the dialog after inserting/updating
                                        } else {
                                            // Show error message if validation fails
                                            editTerminalNo.setError("Invalid input. Only numbers are allowed.");
                                        }
                                    }
                                });
                            }
                        } else {
                            showPermissionDeniedDialog();
                        }
                        break;


                    default:
                        String toolbarTitle = getString(R.string.Admin); // Default title if needed
                        break;
                }

                // Set the toolbar title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
            }
        });

        return view;
    }

    // Method to get permission with default logic
    private boolean validateData(String terminalNo) {
        // Check if terminalNo is empty
        if (TextUtils.isEmpty(terminalNo)) {
            Toast.makeText(getContext(), "Terminal No is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if terminalNo contains only digits (integer only)
        String regex = "\\d+"; // Modify this if you need a different pattern
        if (!terminalNo.matches(regex)) {
            Toast.makeText(getContext(), "Invalid Terminal No. Only numbers are allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All data is valid
        return true;
    }

    // Method to show a dialog indicating permission denial
    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Permission Denied")
                .setMessage("You do not have permission to access this feature.")
                .setPositiveButton("OK", null)
                .show();
    }
    private void insertOrUpdatePosAccess(String terminalNo, String shopName, String cashOrId) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // Get the current date and time
        String dateTime = getCurrentDateTime();

        // Prepare the new values to insert or update
        ContentValues values = new ContentValues();
        values.put(COLUMN_TerminalNo, terminalNo);
        values.put(COLUMN_SHOPNAME, shopName);
        values.put(COLUMN_CASHOR_id, cashOrId);
        values.put(DatabaseHelper.LastModified, dateTime);

        // Step 1: Check if the table is empty
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_NAME_POS_ACCESS;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);

        if (rowCount == 0) {
            // Step 2: Table is empty, insert a new row
            values.put(DatabaseHelper.DateCreated, dateTime); // Include DateCreated for new inserts
            long result = db.insert(TABLE_NAME_POS_ACCESS, null, values);

            if (result != -1) {
                // Data inserted successfully
                savePosNumber(terminalNo); // Save POS number to SharedPreferences
                Toast.makeText(getContext(), "POS Number inserted", Toast.LENGTH_SHORT).show();
                Log.d("insert", "POS Number inserted");
            } else {
                // Failed to insert data
                Toast.makeText(getContext(), "Failed to insert POS data", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Step 3: Table is not empty, update the existing row
            int rowsAffected = db.update(TABLE_NAME_POS_ACCESS, values, null, null);
            if (rowsAffected > 0) {
                // Row was updated successfully
                savePosNumber(terminalNo); // Save POS number to SharedPreferences
                Toast.makeText(getContext(), "POS Number updated", Toast.LENGTH_SHORT).show();
                Log.d("update", "POS Number updated");
            } else {
                // Failed to update
                Toast.makeText(getContext(), "Failed to update POS data", Toast.LENGTH_SHORT).show();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        // Now, update all rows in the std table with the same pos_num
        ContentValues updateValues = new ContentValues();
        updateValues.put(COLUMN_POS_Num, terminalNo); // Update the pos_num column

        // Perform the update operation on the std table without a WHERE clause (update all rows)
        int rowsUpdated = db.update(TABLE_NAME_STD_ACCESS, updateValues, null, null);

        if (rowsUpdated > 0) {
            // Rows in std table updated successfully
            Log.d("update", "std table updated");
            Toast.makeText(getContext(), "std table updated", Toast.LENGTH_SHORT).show();
        } else {
            // No rows were updated
            Toast.makeText(getContext(), "std table not updated", Toast.LENGTH_SHORT).show();
            Log.d("not update", "std table not updated");
        }

        db.close();
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
    private void onPinclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");

        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
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

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void savePosNumber(String posNumber) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("POSNum", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("posNumber", posNumber);
        editor.apply();
    }
}
