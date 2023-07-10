package com.accessa.ibora.Admin.CompanyInfo.FormPager;

import static com.accessa.ibora.sales.Sales.ItemGridAdapter.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.accessa.ibora.R;

public class FirstSlideFragment extends Fragment {

    private EditText editCompanyName;
    private EditText editShopName;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageLogo;
    private Button btnUploadImage;
    private Uri imageUri;
    private String imagePath;
    private String logoPath;

    public FirstSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_slide, container, false);

        editCompanyName = view.findViewById(R.id.editCompanyName);
        editShopName = view.findViewById(R.id.editShopName);
        imageLogo = view.findViewById(R.id.imageLogo);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOptionsDialog();
            }
        });
        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri selectedImageUri = data.getData();
                imagePath = getImagePathFromUri(selectedImageUri);
                displaySelectedImage(selectedImageUri);
            }
        }
    }

    private String getImagePathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private void displaySelectedImage(Uri imageUri) {
        ImageView imageView = getView().findViewById(R.id.imageLogo);

        if (isWebLink(imageUri.toString())) {
            // Image is from web link
            logoPath = imageUri.toString();
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().placeholder(R.drawable.accessalogo))
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Image is from local storage
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadLocalImage(imageView, imageUri);
            }
        }
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

    private void loadImageFromUrl(String imageUrl) {
        // Implement code to load image from URL
        imagePath = imageUrl;
        // You can also display the selected image here if needed
        ImageView imageView = getView().findViewById(R.id.imageLogo);
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
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
                        openImagePicker();
                        break;
                }
            }
        });
        builder.show();
    }

    private void loadLocalImage(ImageView imageView, Uri uri) {
        Glide.with(this)
                .load(uri)
                .apply(new RequestOptions().placeholder(R.drawable.accessalogo))
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getCompanyName() {
        return editCompanyName.getText().toString();
    }

    public String getShopName() {
        return editShopName.getText().toString();
    }

    private boolean isWebLink(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
