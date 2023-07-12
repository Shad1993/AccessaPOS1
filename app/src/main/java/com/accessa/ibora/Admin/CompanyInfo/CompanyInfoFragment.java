package com.accessa.ibora.Admin.CompanyInfo;

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CompanyInfoFragment extends Fragment {

    private EditText Abv, StockNo, PriceNo, DefSupplierCode, VATNo, BRNNo, ADR1, ADR2, ADR3, TelNo, FaxNo, CompanyName,CompAd1,CompAd2,CompAd3, cashior, date, lastmodified, openinghours;
    private DatabaseHelper mDatabaseHelper;
    private DBManager dbManager;
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase database;
    private String cashorId,cashiername,cashierlevel,cashorName;
    private Button buttonUpdate;
    private Button buttonDelete;
    private String imagePath;
    private EditText editCompFaxNo;
    private EditText editCompTelNo;
    private String ImageLink;
    private  ImageView imageView;
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
        view = inflater.inflate(R.layout.edit_company_info, container, false);
        Abv = view.findViewById(R.id.editAbbrev);
        VATNo = view.findViewById(R.id.editVATNo);
        BRNNo = view.findViewById(R.id.editBRNNo);
        ADR1 = view.findViewById(R.id.editADR1);
        ADR2 = view.findViewById(R.id.editADR2);
        ADR3 = view.findViewById(R.id.editADR3);
        CompAd1 =view.findViewById(R.id.editCompADR1);
        CompAd2 =view.findViewById(R.id.editCompADR2);
        CompAd3 =view.findViewById(R.id.editCompADR3);
        TelNo = view.findViewById(R.id.editTelNo);
        FaxNo = view.findViewById(R.id.editFaxNo);
        CompanyName = view.findViewById(R.id.editCompanyName);
        cashior = view.findViewById(R.id.editCahiorNameid);
        lastmodified = view.findViewById(R.id.lastmod);
        lastmodified = view.findViewById(R.id.lastmod);
        openinghours =view.findViewById(R.id.editOpeninghours);
        editCompTelNo=view.findViewById(R.id.editcompTelNo);
        editCompFaxNo=view.findViewById(R.id.editcompFaxNo);
         imageView = view.findViewById(R.id.image_view);

        sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

         cashorName = sharedPreferences.getString("cashorName", null); // Retrieve cashor's name
        cashorId = sharedPreferences.getString("cashorId", null); // Retrieve cashor's ID
        cashierlevel= sharedPreferences.getString("cashorlevel", null);




        mDatabaseHelper = new DatabaseHelper(getContext());


        database = mDatabaseHelper.getWritableDatabase();
        dbManager = new DBManager(getContext());
        dbManager.open();

        Company company = dbManager.getCompanyInfo();
        if (company != null) {
            Abv.setText(company.getShopName());

            VATNo.setText(company.getVATNo());
            BRNNo.setText(company.getBRNNo());
            ADR1.setText(company.getADR1());
            ADR2.setText(company.getADR2());
            ADR3.setText(company.getADR3());
            CompAd1.setText(company.getCompADR());
            CompAd2.setText(company.getCompADR2());
            CompAd3.setText(company.getCompADR3());

            TelNo.setText(company.getTelNo());
            FaxNo.setText(company.getFaxNo());
            editCompTelNo.setText(company.getComptel());
            editCompFaxNo.setText(company.getCompFax());

            CompanyName.setText(company.getCompanyName());
            openinghours.setText(company.getOpeninghours());


            ImageLink= company.getImage();

            Glide.with(this).load(ImageLink).into(imageView);

        }
        cashior.setText(String.valueOf(cashorId));

        buttonUpdate = view.findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCompanyInfo();
            }

        });


        // Image selection button
        Button imageButton = view.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOptionsDialog();
            }
        });

        return view;
    }


        private void updateCompanyInfo() {
            String abv = Abv.getText().toString().trim();

            // Get the current timestamp
            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String lastmodified = dateFormat.format(new Date(currentTimeMillis));
            String UserId = cashorId;
            String vatNo = VATNo.getText().toString().trim();
            String brnNo = BRNNo.getText().toString().trim();
            String adr1 = ADR1.getText().toString().trim();
            String adr2 = ADR2.getText().toString().trim();
            String adr3 = ADR3.getText().toString().trim();
            String CompAdr = CompAd1.getText().toString().trim();
            String CompAdr2 = CompAd2.getText().toString().trim();
            String CompAdr3 = CompAd3.getText().toString().trim();
            String telNo = TelNo.getText().toString().trim();
            String faxNo = FaxNo.getText().toString().trim();
            String companyName = CompanyName.getText().toString().trim();
            String openhour = openinghours.getText().toString().trim();
            String comptel = editCompTelNo.getText().toString().trim();
            String compfax = editCompFaxNo.getText().toString().trim();


            String image ;
            if( imagePath== null) {
                image=  ImageLink;

            } else{
                image=  imagePath;

            }

            if (abv.isEmpty() || lastmodified.isEmpty() || UserId.isEmpty()  || vatNo.isEmpty() || brnNo.isEmpty()
                    || adr1.isEmpty() || adr2.isEmpty() || adr3.isEmpty() || CompAdr.isEmpty()|| CompAdr2.isEmpty()|| CompAdr3.isEmpty()
                    || telNo.isEmpty() || faxNo.isEmpty() || companyName.isEmpty() || openhour.isEmpty() || comptel.isEmpty() || compfax.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues value = new ContentValues();
            value.put("ShopName", abv);
            // Insert the record into the database
            DBManager dbManager = new DBManager(getContext());
            dbManager.open();




        boolean isUpdated = dbManager.updateCompanyInfo( abv, lastmodified, UserId,vatNo,brnNo,adr1,adr2,adr3, CompAdr,CompAdr2,CompAdr3,telNo,faxNo,companyName,image,openhour,comptel,compfax);
            int rowsAffected = database.update("Users", value, null, null);
            dbManager.close();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cashorName", cashorName); // Store cashor's name
            editor.putString("cashorId", cashorId); // Store cashor's ID
            editor.putString("cashorlevel", cashierlevel); // Store cashor's level
            editor.putString("CompanyName", companyName); // Store company name
            editor.apply();

        if (isUpdated) {
            Toast.makeText(getContext(), getString(R.string.update), Toast.LENGTH_SHORT).show();
            getActivity().finish();
            Intent intent = new Intent(getContext(), login.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(R.string.failedupdate), Toast.LENGTH_SHORT).show();
        }
    }
    private void openImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Web Link", "Device Memory"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Web Link
                        openWebImageDialog();
                        break;
                    case 1: // Device Memory
                        openDeviceImagePicker();
                        break;
                }
            }
        });
        builder.show();
    }
    private void openWebImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Image URL");

        final EditText urlEditText = new EditText(getContext());
        urlEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(48, 0, 48, 0);
        urlEditText.setLayoutParams(layoutParams);

        builder.setView(urlEditText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imageUrl = urlEditText.getText().toString().trim();
                loadImageFromUrl(imageUrl);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openDeviceImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                Uri selectedImageUri = data.getData();
                imagePath = getPathFromUri(selectedImageUri);
                displaySelectedImage(selectedImageUri);
            }
        }
    }
    private void displaySelectedImage(Uri imageUri) {
        ImageView imageView = view.findViewById(R.id.image_view); // Initialize imageView


        if (isWebLink(String.valueOf(imageUri))) {
            // Load image from web link
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.emptybasket) // Placeholder image while loading
                    .error(R.drawable.iboralogos1)
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Load image from local storage
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadLocalImage(imageView, String.valueOf(imageUri));
            }
        }
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private boolean isWebLink(String url) {
        return URLUtil.isValidUrl(url);
    }
    private void loadLocalImage(ImageView imageView, String imageLocation) {
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.emptybasket);
        }
    }
    private void loadImageFromUrl(String imageUrl) {
        // Implement code to load image from URL
        imagePath = imageUrl;
        // You can also display the selected image here if needed
        ImageView imageView = view.findViewById(R.id.image_view);
        Glide.with(this).load(imageUrl).into(imageView);
    }






}