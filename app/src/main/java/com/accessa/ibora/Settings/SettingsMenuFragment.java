package com.accessa.ibora.Settings;

import android.app.Activity;
import android.content.Context;
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

import com.accessa.ibora.Admin.CompanyInfo.CompanyInfoFragment;
import com.accessa.ibora.Admin.People.PeopleFragment;
import com.accessa.ibora.Admin.RightAccess.RightAccessFragment;
import com.accessa.ibora.Buyer.buyerFragment;
import com.accessa.ibora.CustomerLcd.CustomerLcdFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.Settings.Language.SelectLanguageFragment;
import com.accessa.ibora.Settings.MRASettings.MRAFragment;
import com.accessa.ibora.Settings.POPSettings.POPSettingsFragment;
import com.accessa.ibora.Settings.PaymentFragment.PaymentFragment;
import com.accessa.ibora.Settings.PrinterSetup.PrinterSetUpFragment;
import com.accessa.ibora.Settings.QRMethods.QRSettingsFragment;
import com.accessa.ibora.Settings.Rooms.RoomsFragment;
import com.accessa.ibora.Sync.SyncService;
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
                getString(R.string.ServerSettings)

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
                R.drawable.dataserverblue

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

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "qrSettings_", levelNumber)) {

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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }
                } else if (position==1) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "ledDisplaySettings_", levelNumber)) {

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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }

            }else if (position==2) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "paymentMethods_", levelNumber)) {

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

                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }

                }else if (position==3) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "popSettings_", levelNumber)) {
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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }
                }

                else if (position==4) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "buyer_", levelNumber)) {
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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }

                } else if (position==5) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "mra_", levelNumber)) {

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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }
                } else if (position==6) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "roomsAndTables_", levelNumber)) {

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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }
                     }else if (position==7) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "printerSettings_", levelNumber)) {

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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }
                }else if (position==8) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "serversettings_", levelNumber)) {

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
                    }else{
                        Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 9) {
                    int levelNumber = Integer.parseInt(cashorlevel);

                    if (mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, "languages_", levelNumber)) {

                        // Create an AlertDialog
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
                }
                else {
                    toolbarTitle = getString(R.string.Settings); // Set a default value if needed
                }

                // Set the toolbar title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
                }else{
                    Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
    }



}
