package com.accessa.ibora.Settings.POPSettings;

import static android.app.Activity.RESULT_OK;
import static com.accessa.ibora.sales.Sales.ItemGridAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.Admin.AdminActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.company.Company;
import com.accessa.ibora.login.login;
import com.accessa.ibora.product.Department.Department;
import com.accessa.ibora.product.items.AddItemActivity;
import com.accessa.ibora.product.items.DBManager;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.accessa.ibora.product.items.ItemAdapter;
import com.accessa.ibora.product.items.ModifyItemActivity;
import com.accessa.ibora.product.items.RecyclerItemClickListener;
import com.accessa.ibora.product.menu.Product;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class POPSettingsFragment extends Fragment {

    private EditText LinkValidate, LinkStatus, LinkCancel, user, password, TllNo, OutletNo,ClientID,MerchantName,PhoneNumber,TillNum;
    private DatabaseHelper mDatabaseHelper;
    private DBManager dbManager;
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase database;
    private String cashorId,cashiername,cashierlevel,cashorName;
    private Button buttonUpdate;
private  Context context;
    private EditText editCompFaxNo;
    private EditText editCompTelNo;

    private View view; // Variable to store the inflated fragment view
    private static final int REQUEST_IMAGE_GALLERY = 1;
    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_pop_settings, container, false);
        context = getContext();
        LinkValidate = view.findViewById(R.id.apilinkvalidate);
        LinkStatus = view.findViewById(R.id.apilinkstatus);
        LinkCancel = view.findViewById(R.id.apilinkcancel);
        user = view.findViewById(R.id.edituser);
        password = view.findViewById(R.id.editpassword);
        TllNo = view.findViewById(R.id.editTillNo);
        OutletNo = view.findViewById(R.id.editOutletNo);
        ClientID = view.findViewById(R.id.editclientId);
        MerchantName= view.findViewById(R.id.editmerchantname);
        PhoneNumber= view.findViewById(R.id.editmobilenum);
        TillNum= view.findViewById(R.id.editTillNumber);

        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashierlevel = sharedPreferences.getString("cashorlevel", null);

        mDatabaseHelper = new DatabaseHelper(getContext());
        database = mDatabaseHelper.getWritableDatabase();
        dbManager = new DBManager(getContext());
        dbManager.open();

        // Read the content from internal storage files
        String validate = readTextFromFile("api_addresss.txt");
        String status = readTextFromFile("payment_status.txt");
        String cancel = readTextFromFile("cancelpoptransact.txt");
        String username = readTextFromFile("api_user.txt");
        String passwordValue = readTextFromFile("password.txt");
        String tillnum = readTextFromFile("till_id.txt");
        String outletnum = readTextFromFile("outlet.txt");
        String clientId = readTextFromFile("client_id.txt");
        String MerchName = readTextFromFile("merch_name.txt");
        String mobile_num = readTextFromFile("mob_num.txt");
        String Till_num = readTextFromFile("till_num.txt");

        // Set the text to the corresponding EditText fields
        LinkValidate.setText(validate);
        LinkStatus.setText(status);
        LinkCancel.setText(cancel);
        user.setText(username);
        password.setText(passwordValue);
        TllNo.setText(tillnum);
        OutletNo.setText(outletnum);
        ClientID.setText(clientId);
        MerchantName.setText(MerchName);
        PhoneNumber.setText(mobile_num);
        TillNum.setText(Till_num);

        buttonUpdate = view.findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePOPInfo();
            }
        });

        return view;
    }


    private void updatePOPInfo() {

        LinkValidate = view.findViewById(R.id.apilinkvalidate);
        LinkStatus = view.findViewById(R.id.apilinkstatus);
        LinkCancel = view.findViewById(R.id.apilinkcancel);
        user = view.findViewById(R.id.edituser);
        password = view.findViewById(R.id.editpassword);
        TllNo = view.findViewById(R.id.editTillNo);
        OutletNo =view.findViewById(R.id.editOutletNo);
        ClientID=view.findViewById(R.id.editclientId);
        MerchantName=view.findViewById(R.id.editmerchantname);
        PhoneNumber=view.findViewById(R.id.editmobilenum);
        TillNum=view.findViewById(R.id.editTillNumber);


        String linkval = LinkValidate.getText().toString().trim();
        String linkstatus = LinkStatus.getText().toString().trim();
        String linkcancel = LinkCancel.getText().toString().trim();
        String users = user.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String tillnum = TllNo.getText().toString().trim();
        String outletnum = OutletNo.getText().toString().trim();
        String Client_Id = ClientID.getText().toString().trim();
        String Merch_name = MerchantName.getText().toString().trim();
        String Phone_num = PhoneNumber.getText().toString().trim();
        String Til_number = TillNum.getText().toString().trim();



// Define custom file names for each setting
        String fileNameLinkValidate = "api_addresss.txt";
        String fileNameLinkStatus = "payment_status.txt";
        String fileNameLinkCancel = "cancelpoptransact.txt";
        String fileNameUser = "api_user.txt";
        String fileNamePassword = "password.txt";
        String fileNameTllNo = "till_id.txt";
        String fileNameOutletNo = "outlet.txt";
        String fileNameclientId = "client_id.txt";
        String fileNameMerchName = "merch_name.txt";
        String fileNamePhone = "mob_num.txt";
        String fileNametill = "till_num.txt";

        // Write the updated content to internal storage with custom file names
        writeToInternalStorage(fileNameLinkValidate, linkval);
        writeToInternalStorage(fileNameLinkStatus, linkstatus);
        writeToInternalStorage(fileNameLinkCancel, linkcancel);
        writeToInternalStorage(fileNameUser, users);
        writeToInternalStorage(fileNamePassword, Password);
        writeToInternalStorage(fileNameTllNo, tillnum);
        writeToInternalStorage(fileNameOutletNo, outletnum);
        writeToInternalStorage(fileNameclientId, Client_Id);
        writeToInternalStorage(fileNameMerchName, Merch_name);
        writeToInternalStorage(fileNamePhone, Phone_num);
        writeToInternalStorage(fileNametill, Til_number);

        // Show a toast indicating that the update was successful
        Toast.makeText(getContext(), "Settings updated successfully!", Toast.LENGTH_SHORT).show();


    }


    // Helper method to read text from a file in internal storage
    private String readTextFromFile(String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
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

    private void writeToInternalStorage(String fileName, String content) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}