package com.accessa.ibora.product.SubCategory;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accessa.ibora.R;
import com.accessa.ibora.product.Department.ModifyDepartmentActivity;
import com.accessa.ibora.product.category.AddCategoryActivity;
import com.accessa.ibora.product.category.CategoryAdaptor;
import com.accessa.ibora.product.category.CategoryDBManager;
import com.accessa.ibora.product.category.CategoryDatabaseHelper;
import com.accessa.ibora.product.category.ModifyCategoryActivity;
import com.accessa.ibora.product.category.RecyclerCategoryClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SubCategoryFragment extends Fragment   {
    private  EditText searchEditText;
    private int CategoryColor;
    FloatingActionButton CatAddFab;
    private SearchView CatSearchView;
    private CategoryDBManager CatManager;
    private SubCategoryAdaptor CatAdapter;
    private TextView emptyView;
    private RecyclerView CatRecyclerView;
    private SimpleCursorAdapter Catadapter;

    private DatabaseHelper CatDatabaseHelper;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences sharedPreferences,usersharedPreferences;
    String cashorlevel;

    final String[] from = new String[] { CategoryDatabaseHelper._ID,CategoryDatabaseHelper.CAT_PRINTER_OPTION,
            CategoryDatabaseHelper.CatName, CategoryDatabaseHelper.Color };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc , R.id.price};
    Toolbar mActionBarToolbar;

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CategoryColor=0;
    }

    // onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_cat_fragment,container,false);



        //RecyclerView
        CatRecyclerView = view.findViewById(R.id.recycler_view);

        CatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CatDatabaseHelper = new DatabaseHelper(getContext());
        mDatabaseHelper= new DatabaseHelper(getContext());
        Cursor cursor = CatDatabaseHelper.getAllSubCategory();

        CatRecyclerView.setAdapter(CatAdapter);


        CatAdapter = new SubCategoryAdaptor(getActivity(), cursor, CategoryColor);
        CatRecyclerView.setAdapter(CatAdapter);
        usersharedPreferences = getActivity().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);


        cashorlevel = sharedPreferences.getString("cashorlevel", null); // Retrieve cashor's level

// Get a reference to the AppCompatImageView
        AppCompatImageView imageView = view.findViewById(R.id.empty_image_view);

// Load the GIF using Glide
        Glide.with(getContext())
                .asGif()
                .load(R.drawable.folderwalk)
                .into(imageView);

// Find the empty FrameLayout
        FrameLayout emptyFrameLayout = view.findViewById(R.id.empty_frame_layout);

        if (CatAdapter.getItemCount() <= 0) {
            CatRecyclerView.setVisibility(View.GONE);
            emptyFrameLayout.setVisibility(View.VISIBLE);
        } else {
            CatRecyclerView.setVisibility(View.VISIBLE);
            emptyFrameLayout.setVisibility(View.GONE);
        }
        CatSearchView = view.findViewById(R.id.search_view);
         searchEditText = CatSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        CatSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor newCursor = CatDatabaseHelper.searchSubCategory(newText);
                CatAdapter.swapCursor(newCursor);
                if (newText.isEmpty()) {
                    // Set default text color

                    searchEditText.setTextColor(getResources().getColor(android.R.color.primary_text_light));


                } else {
                    // Set custom text color

                    searchEditText.setTextColor(getResources().getColor(R.color.white));
                }
                return true;
            }
        });


        CatManager = new CategoryDBManager(getContext());
        CatManager.open();
        Cursor cursor1 = CatManager.fetch();
        CatAddFab = view.findViewById(R.id.add_fab);


        Catadapter = new SimpleCursorAdapter(getContext(), R.layout.viewcategory_activity, cursor1, from, to, 0);
        Catadapter.notifyDataSetChanged();



        CatRecyclerView.addOnItemTouchListener(
                new RecyclerCategoryClickListener(getContext(), CatRecyclerView ,new RecyclerCategoryClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int levelNumber = Integer.parseInt(cashorlevel);
                        String Activity="modifySubCategory_";
                        SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);

                        boolean canHigherAccessReceipt = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);
                        boolean canAccessReceipt = mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber);
                        if (canHigherAccessReceipt ) {
                            showPinDialog(Activity, () -> {
                                TextView idTextView = (TextView) view.findViewById(R.id.id_text_view);
                                TextView CatName_edittext = (TextView) view.findViewById(R.id.Catname_text_view);
                                TextView Color_edittext = (TextView) view.findViewById(R.id.Color_text_view);
                                TextView printing_status_edittext = (TextView) view.findViewById(R.id.printingstatus_view);


                                String id1 = idTextView.getText().toString();

                                String id = idTextView.getText().toString();
                                String CatName = CatName_edittext.getText().toString();
                                String relatedcatid = Color_edittext.getText().toString();
                                String printingstatus = printing_status_edittext.getText().toString();

                                Intent modify_intent = new Intent(getActivity().getApplicationContext(), ModifySubCategoryActivity.class);
                                modify_intent.putExtra("CatName", CatName);
                                modify_intent.putExtra("relatedcatid", relatedcatid);
                                modify_intent.putExtra("id", id);
                                modify_intent.putExtra("printingstatus", printingstatus);

                                startActivity(modify_intent);
                            });
                        }
                        // Check permission for Receipts
                        else if (!canHigherAccessReceipt && canAccessReceipt) {
                            TextView idTextView = (TextView) view.findViewById(R.id.id_text_view);
                            TextView CatName_edittext = (TextView) view.findViewById(R.id.Catname_text_view);
                            TextView Color_edittext = (TextView) view.findViewById(R.id.Color_text_view);
                            TextView printing_status_edittext = (TextView) view.findViewById(R.id.printingstatus_view);


                            String id1 = idTextView.getText().toString();

                            String id = idTextView.getText().toString();
                            String CatName = CatName_edittext.getText().toString();
                            String relatedcatid = Color_edittext.getText().toString();
                            String printingstatus = printing_status_edittext.getText().toString();

                            Intent modify_intent = new Intent(getActivity().getApplicationContext(), ModifySubCategoryActivity.class);
                            modify_intent.putExtra("CatName", CatName);
                            modify_intent.putExtra("relatedcatid", relatedcatid);
                            modify_intent.putExtra("id", id);
                            modify_intent.putExtra("printingstatus", printingstatus);

                            startActivity(modify_intent);
                        } else {
                            Toast.makeText(getContext(), R.string.Notallowed, Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );





        ;

        CatAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                int levelNumber = Integer.parseInt(cashorlevel);
                SharedPreferences AccessLevelsharedPreferences = getContext().getSharedPreferences("HigherLevelConfig", Context.MODE_PRIVATE);
                String Activity="addSubCategory_";
                SharedPreferences usersharedPreferences = getContext().getSharedPreferences("UserLevelConfig", Context.MODE_PRIVATE);
                boolean canHigherAccessSyncDatabase = mDatabaseHelper.getAccessPermissionWithDefault(AccessLevelsharedPreferences, Activity, levelNumber);

                if (canHigherAccessSyncDatabase) {
                    showPinDialog(Activity, () -> {
                        openNewActivity();

                    });
                } else if (!canHigherAccessSyncDatabase && mDatabaseHelper.getPermissionWithDefault(usersharedPreferences, Activity, levelNumber)) {
                    openNewActivity();

                } else{
                    Toast.makeText(getContext(), getText(R.string.Notallowed), Toast.LENGTH_SHORT).show();

                }

            }
        });
        return view;
    }



    public void openNewActivity(){
        Intent intent = new Intent(getContext(), AddSubCategoryActivity.class);
        startActivity(intent);
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
                Toast.makeText(getContext(), "Invalid PIN", Toast.LENGTH_SHORT).show();
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


}