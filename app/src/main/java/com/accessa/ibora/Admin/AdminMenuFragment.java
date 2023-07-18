package com.accessa.ibora.Admin;

import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_CASHOR_id;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_COMPANY_NAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_SHOPNAME;
import static com.accessa.ibora.product.items.DatabaseHelper.COLUMN_TerminalNo;
import static com.accessa.ibora.product.items.DatabaseHelper.TABLE_NAME_POS_ACCESS;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.accessa.ibora.Admin.People.PeopleFragment;
import com.accessa.ibora.Admin.RightAccess.RightAccessFragment;
import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;

import com.accessa.ibora.Report.SalesReportActivity;
import com.accessa.ibora.Sync.connectToMssql;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.CustomAdapter;
import com.accessa.ibora.welcome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// extended from compatibility Fragment for pre-HC fragment support
public class AdminMenuFragment extends Fragment {


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
                getString(R.string.CompanyInfo),
                getString(R.string.sync),
                getString(R.string.POSINFO)
        };
        icons = new int[]{
                R.drawable.cashier,
                R.drawable.key,
                R.drawable.comp,
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


        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View  view, int position, long id) {


                if (position == 0) {

                    toolbarTitle = getString(R.string.People);
                    // Create new fragment and transaction
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

                } else if (position == 1) {
                    toolbarTitle = getString(R.string.Rights);
                    Fragment newFragment = new RightAccessFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

                } else if (position == 2) {
                    toolbarTitle = getString(R.string.CompanyInfo);
                    Fragment newFragment = new CompanyInfoFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                } else if (position== 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(R.layout.dialog_layout_offline_online);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Button buttonWorkOffline = dialog.findViewById(R.id.buttonWorkOffline);
                    Button buttonSynchronize = dialog.findViewById(R.id.buttonSynchronize);

                    buttonWorkOffline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss(); // Close the dialog
                        }
                    });

                    buttonSynchronize.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getContext(), connectToMssql.class);
                            startActivity(intent);
                        }
                    });


            }else if (position== 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(R.layout.popup_insert_pos_access);

                    // Set up the dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Get references to the views in the popup layout
                    EditText editTerminalNo = dialog.findViewById(R.id.editTerminalNo);
                    EditText editCompanyName = dialog.findViewById(R.id.editCompanyName);
                    Button btnInsert = dialog.findViewById(R.id.btnInsert);

                    // Set up button click listener
                    btnInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the entered data from the views
                            String terminalNo = editTerminalNo.getText().toString();
                            String shopName = ShopName;
                            String cashOrId = cashorId;

                            // Perform validation and data insertion into the database
                            if (validateData(terminalNo)) {
                                insertDataIntoPosAccessTable(terminalNo, shopName, cashOrId);
                                dialog.dismiss(); // Close the popup
                            }
                        }
                    });


                }else {
                    toolbarTitle = getString(R.string.Admin); // Set a default value if needed
                }

                // Set the toolbar title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
            }
        });







        return view;
    }
    private boolean validateData(String terminalNo) {
        // Perform validation checks
        if (TextUtils.isEmpty(terminalNo)) {
            // Display an error message or show a Toast indicating that Terminal No is required
            Toast.makeText(getContext(), "Terminal No is required", Toast.LENGTH_SHORT).show();
            return false;
        }


        // All data is valid
        return true;
    }

    private void insertDataIntoPosAccessTable(String terminalNo, String ShopName, String cashOrId) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // Get the current date and time
        String dateTime = getCurrentDateTime();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TerminalNo, terminalNo);
        values.put(COLUMN_SHOPNAME, ShopName);
        values.put(COLUMN_CASHOR_id, cashOrId);
        values.put(DatabaseHelper.LastModified, dateTime);
        values.put(DatabaseHelper.DateCreated, dateTime);

        long result = db.insert(TABLE_NAME_POS_ACCESS, null, values);
        if (result != -1) {
            // Data inserted successfully
            savePosNumber(terminalNo); // Save POS number to SharedPreferences
            Toast.makeText(getContext(), "POS Number saved", Toast.LENGTH_SHORT).show();
        } else {
            // Failed to insert data
            Toast.makeText(getContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
        }

        db.close();
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
