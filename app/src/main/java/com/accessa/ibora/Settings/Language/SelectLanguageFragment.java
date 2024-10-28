package com.accessa.ibora.Settings.Language;
import static android.content.Context.MODE_PRIVATE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.accessa.ibora.Admin.RegistorCashor;
import com.accessa.ibora.R;
import com.accessa.ibora.SecondScreen.SeconScreenDisplay;
import com.accessa.ibora.SecondScreen.TransactionDisplay;
import com.accessa.ibora.Settings.SettingsDashboard;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.List;
import java.util.Locale;

import woyou.aidlservice.jiuiv5.IWoyouService;
public class SelectLanguageFragment extends Fragment {

    private Button buttonEng, buttonFr;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase database;
    private static IWoyouService woyouService;

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
            displayOnLCd();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_selectlanguage, container, false);

        // Bind the service
        Intent intent1 = new Intent();
        intent1.setPackage("woyou.aidlservice.jiuiv5");
        intent1.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        requireActivity().bindService(intent1, connService, Context.BIND_AUTO_CREATE);

        showSecondaryScreen();

        // Initialize the DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(requireContext());
        database = mDatabaseHelper.getReadableDatabase();
        loadLanguagePreference();
        // Eng language
        buttonEng = rootView.findViewById(R.id.buttonEng);
        buttonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Locale.ENGLISH);
                saveLanguagePreference("en");
                setButtonStates("en");
            }
        });

        // Fr language
        buttonFr = rootView.findViewById(R.id.buttonFr);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(Locale.FRENCH);
                saveLanguagePreference("fr");
                setButtonStates("fr");  // Set button states
            }
        });
        // Set the initial button states based on the current language preference
        String currentLanguage = getCurrentLanguagePreference();
        setButtonStates(currentLanguage);
        return rootView;
    }
    public String getCurrentLanguagePreference() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getString("Selected_Language", "en"); // Default to English if not set
    }


    public void setButtonStates(String languageCode) {
        // Set colors and states for buttons based on the selected language
        if ("en".equals(languageCode)) {
            buttonEng.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.BleuAccessaText));
            buttonFr.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.grey));
            buttonEng.setEnabled(false); // Disable the English button as it is the current selection
            buttonFr.setEnabled(true); // Enable the French button as it is not the current selection
        } else if ("fr".equals(languageCode)) {
            buttonFr.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.BleuAccessaText));
            buttonEng.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.grey));
            buttonFr.setEnabled(false); // Disable the French button as it is the current selection
            buttonEng.setEnabled(true); // Enable the English button as it is not the current selection
        }
    }
    public void saveLanguagePreference(String languageCode) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Selected_Language", languageCode);
        editor.apply();
    }
    public void loadLanguagePreference() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String languageCode = prefs.getString("Selected_Language", "en"); // Default to English if not set
        setLocale(languageCode); // Method to apply the language (define it based on your existing setup)
    }

    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireActivity().getResources().updateConfiguration(config, requireActivity().getResources().getDisplayMetrics());

        // Optionally restart the activity to apply the language
       // requireActivity().recreate();
    }
    public void returnHome() {
        Intent home_intent1 = new Intent(getContext(), SettingsDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent1.putExtra("fragment", "language_fragment");
        startActivity(home_intent1);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update button states in case the language preference was changed and fragment was resumed
        String currentLanguage = getCurrentLanguagePreference();
        setButtonStates(currentLanguage);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Bind the service again if needed
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind the service when the fragment is stopped
        requireActivity().unbindService(connService);
    }

    public void openNewActivity(Locale locale) {
        // Set the app's locale to the selected language
        Locale.setDefault(locale);

        // Update the configuration to reflect the new locale
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Optionally restart the activity to apply the language
        //requireActivity().recreate(); // Restart the activity
        returnHome();

    }



    private void showSecondaryScreen() {
        Display presentationDisplay = getPresentationDisplay();

        if (presentationDisplay != null) {
            SeconScreenDisplay secondaryDisplay = new SeconScreenDisplay(requireContext(), presentationDisplay);
            secondaryDisplay.show();
            secondaryDisplay.displaydefault();
        } else {
            displayOnLCd();
        }
    }

    public void displayOnLCd() {
        if (woyouService == null) {
            Toast.makeText(requireContext(), "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDDoubleString("Welcome", "Back", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Display getPresentationDisplay() {
        DisplayManager displayManager = (DisplayManager) requireActivity().getSystemService(Context.DISPLAY_SERVICE);
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


}
