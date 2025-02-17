package com.accessa.ibora.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.Settings.Buyer.buyerFragment;
import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.Settings.Language.SelectLanguageFragment;
import com.accessa.ibora.Settings.MRASettings.MRAFragment;
import com.accessa.ibora.Settings.POPSettings.POPSettingsFragment;
import com.accessa.ibora.Settings.PaymentFragment.PaymentFragment;
import com.accessa.ibora.Settings.PrinterSetup.PrinterSetUpFragment;
import com.accessa.ibora.Settings.QRMethods.QRSettingsFragment;
import com.accessa.ibora.Settings.Rooms.RoomsFragment;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.menu.CustomAdapter;

// extended from compatibility Fragment for pre-HC fragment support
public class SettingsMenuFragment extends Fragment {


    private String toolbarTitle;
    private ListView simpleList;
    private String[] menuList;
    private int[] icons;

    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences sharedPreferences,usersharedPreferences;
    String cashorlevel;
    private static final String TRANSACTION_ID_KEY = "transaction_id";

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
        toolbarTitle = getString(R.string.Settings);
        super.onCreate(savedInstanceState);
        toolbarTitle = getString(R.string.Settings);
        mDatabaseHelper = new DatabaseHelper(getContext());
        usersharedPreferences = getActivity().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);


        cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level
        menuList = new String[]{
                getString(R.string.QR),
                getString(R.string.QRDisplaySettings),
                getString(R.string.PaymentMethods),
                getString(R.string.PopSettings),
                getString(R.string.buyer),
                getString(R.string.MRA),
                getString(R.string.Roomstable),
                getString(R.string.PrinterSettings),
                getString(R.string.Language),
                getString(R.string.ServerSettings),
                getString(R.string.pricelevel)

        };
        icons = new int[]{
                R.drawable.qr,
                R.drawable.led_display,
                R.drawable.payment,
                R.drawable.poplogo,
                R.drawable.buyericon,
                R.drawable.mra_logo,
                R.drawable.room,
                R.drawable.printersetup,
                R.drawable.flag,
                R.drawable.dataserverblue,
                R.drawable.pricelevel

        };

        setHasOptionsMenu(true);
        Fragment newFragment = new QRSettingsFragment();
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
        fragmentTransaction.addToBackStack(newFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
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
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="qrSettings_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.QR);
                            // Create new fragment and transaction
                            Fragment newFragment = new QRSettingsFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();

                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.QR);
                        // Create new fragment and transaction
                        Fragment newFragment = new QRSettingsFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }

                } else if (position==1) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="ledDisplaySettings_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {

                            toolbarTitle = getString(R.string.PaymentMethods);
                            // Create new fragment and transaction
                            Fragment newFragment = new CustomerLcdFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();

                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {

                        toolbarTitle = getString(R.string.PaymentMethods);
                        // Create new fragment and transaction
                        Fragment newFragment = new CustomerLcdFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }


            }else if (position==2) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="paymentMethods_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {

                            toolbarTitle = getString(R.string.LEDScreenSettings);
                            // Create new fragment and transaction
                            Fragment newFragment = new PaymentFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();


                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {

                        toolbarTitle = getString(R.string.LEDScreenSettings);
                        // Create new fragment and transaction
                        Fragment newFragment = new PaymentFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();


                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }


                }else if (position==3) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="popSettings_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.PopSettings);
                            Fragment newFragment = new POPSettingsFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();

                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.PopSettings);
                        Fragment newFragment = new POPSettingsFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }

                }

                else if (position==4) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="buyer_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.buyer);
                            Fragment newFragment = new buyerFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.buyer);
                        Fragment newFragment = new buyerFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }


                } else if (position==5) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="mra_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.MRA);
                            Fragment newFragment = new MRAFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();

                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.MRA);
                        Fragment newFragment = new MRAFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }

                } else if (position==6) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="roomsAndTables_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.Roomstable);
                            Fragment newFragment = new RoomsFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.Roomstable);
                        Fragment newFragment = new RoomsFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }

                     }else if (position==7) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="printerSettings_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.PrinterSettings);
                            Fragment newFragment = new PrinterSetUpFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();

                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.PrinterSettings);
                        Fragment newFragment = new PrinterSetUpFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }

                }else if (position==8) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="languages_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            toolbarTitle = getString(R.string.Language);
                            Fragment newFragment = new SelectLanguageFragment();
                            // create a FragmentManager
                            FragmentManager fm = getFragmentManager();
                            // create a FragmentTransaction to begin the transaction and replace the Fragment
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            // replace the FrameLayout with new Fragment
                            fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                            fragmentTransaction.addToBackStack(newFragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        toolbarTitle = getString(R.string.Language);
                        Fragment newFragment = new SelectLanguageFragment();
                        // create a FragmentManager
                        FragmentManager fm = getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }

                } else if (position == 9) {
                    int levelNumber = Integer.parseInt(cashorlevel);
                    SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                    String Activity="serversettings_";
                    SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                    boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                    if (canHigherAccessSyncDatabase) {
                        showPinDialog(Activity, () -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.serverparams, null);
                            builder.setView(dialogView);

                            final EditText editTextUser = dialogView.findViewById(R.id.editTextUser);
                            final EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
                            final EditText editTextDB = dialogView.findViewById(R.id.editTextDB);
                            final EditText editTextServer = dialogView.findViewById(R.id.editTextServer);
                            Button buttonSave = dialogView.findViewById(R.id.buttonSave);

                            SharedPreferences preferences = getContext().getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);
                            String user = preferences.getString("_user", "");
                            String pass = preferences.getString("_pass", "");
                            String db = preferences.getString("_DB", "");
                            String server = preferences.getString("_server", "");

                            // Populate the fields with existing data
                            editTextUser.setText(user);
                            editTextPassword.setText(pass);
                            editTextDB.setText(db);
                            editTextServer.setText(server);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            buttonSave.setOnClickListener(v -> {
                                // Save the new values to SharedPreferences
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("_user", editTextUser.getText().toString());
                                editor.putString("_pass", editTextPassword.getText().toString());
                                editor.putString("_DB", editTextDB.getText().toString());
                                editor.putString("_server", editTextServer.getText().toString());
                                editor.apply();

                                Toast.makeText(getContext(), "Database parameters saved!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                        });
                    } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.serverparams, null);
                        builder.setView(dialogView);

                        final EditText editTextUser = dialogView.findViewById(R.id.editTextUser);
                        final EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
                        final EditText editTextDB = dialogView.findViewById(R.id.editTextDB);
                        final EditText editTextServer = dialogView.findViewById(R.id.editTextServer);
                        final EditText editTextZoneId = dialogView.findViewById(R.id.editTextZone);
                        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

                        SharedPreferences preferences = getContext().getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);
                        String user = preferences.getString("_user", "");
                        String pass = preferences.getString("_pass", "");
                        String db = preferences.getString("_DB", "");
                        String server = preferences.getString("_server", "");
                        String zone = preferences.getString("_zone", "");

                        // Populate the fields with existing data
                        editTextUser.setText(user);
                        editTextPassword.setText(pass);
                        editTextDB.setText(db);
                        editTextServer.setText(server);
                        editTextZoneId.setText(zone);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        buttonSave.setOnClickListener(v -> {
                            // Save the new values to SharedPreferences
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("_user", editTextUser.getText().toString());
                            editor.putString("_pass", editTextPassword.getText().toString());
                            editor.putString("_DB", editTextDB.getText().toString());
                            editor.putString("_server", editTextServer.getText().toString());
                            editor.putString("_zone", editTextZoneId.getText().toString());
                            editor.apply();

                            Toast.makeText(getContext(), "Database parameters saved!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    } else{
                        Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                    }



                // Set the toolbar title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
                  } else if (position == 10)

            {
                // Define the options
                final String[] priceLevels = {"Price Level 1", "Price Level 2", "Price Level 3"};

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
               String  transactionIdInProgress = sharedPreferences.getString(TRANSACTION_ID_KEY, null);

                SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("BuyerInfo", Context.MODE_PRIVATE);
                String buyerName = sharedPreferences2.getString("BuyerName", null);

                if (transactionIdInProgress != null ) {
                    if (buyerName == null || buyerName.isEmpty()) {
                        // Both conditions are met (no transaction in progress and no selected buyer), so allow changing price level.

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Select a Price Level")
                                .setItems(priceLevels, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Handle the selected price level
                                        String selectedPriceLevel = priceLevels[which];

                                        if (selectedPriceLevel != null && !selectedPriceLevel.isEmpty()) {
                                            SharedPreferences sharedPreferencesPriceLevel = requireContext().getSharedPreferences("pricelevel", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferencesPriceLevel.edit();
                                            editor.putString("selectedPriceLevel", selectedPriceLevel);
                                            editor.apply();

                                            // Show a new dialog with a message
                                            AlertDialog.Builder messageBuilder = new AlertDialog.Builder(requireContext());
                                            messageBuilder.setTitle("Message")
                                                    .setMessage("Price level selected: " + selectedPriceLevel)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Navigate to MainActivity when OK is clicked
                                                            Intent intent = new Intent(requireContext(), MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                            messageBuilder.create().show();
                                        }
                                    }
                                });
                        builder.create().show();
                    } else {
                        // If a buyer is selected, show a message to handle this case.
                        // Transaction is in progress, show a message to complete the transaction or update individual items.
                        AlertDialog.Builder transactionBuilder = new AlertDialog.Builder(requireContext());
                        transactionBuilder.setTitle("Buyer  Selected")
                                .setMessage("You must remove active Buyer  before changing the price level.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Handle what to do when the user clicks "OK" in the incomplete transaction dialog.
                                        // This might involve taking the user to a transaction screen or item price update screen.
                                    }
                                });
                        transactionBuilder.create().show();
                    }
                } else {
                    // Transaction is in progress, show a message to complete the transaction or update individual items.
                    AlertDialog.Builder transactionBuilder = new AlertDialog.Builder(requireContext());
                    transactionBuilder.setTitle("Incomplete Transaction")
                            .setMessage("You must complete the transaction or update individual item prices before changing the price level.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle what to do when the user clicks "OK" in the incomplete transaction dialog.
                                    // This might involve taking the user to a transaction screen or item price update screen.
                                }
                            });
                    transactionBuilder.create().show();
                }
            }
            else{
                    Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
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
    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Permission Denied")
                .setMessage("You do not have permission to access this feature.")
                .setPositiveButton("OK", null)
                .show();
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
    private void onPinclearButtonClick(EditText ReceivedEditText) {

        if (ReceivedEditText != null) {
            // Insert the letter into the EditText
            ReceivedEditText.setText("");

        } else {
            // Show a toast message if EditText is null
            Toast.makeText(getContext(), "Please select an input field first", Toast.LENGTH_SHORT).show();
        }
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

}
