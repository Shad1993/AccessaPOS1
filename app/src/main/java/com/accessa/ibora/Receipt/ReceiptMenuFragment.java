package com.accessa.ibora.Receipt;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.accessa.ibora.R;
import com.accessa.ibora.Settings.QRMethods.QRSettingsFragment;
import com.accessa.ibora.product.Department.RecyclerDepartmentClickListener;
import com.accessa.ibora.product.items.DatabaseHelper;


public class ReceiptMenuFragment extends Fragment {


    private String toolbarTitle;
    private RecyclerView simpleList;

    private DatabaseHelper mDatabaseHelper;
    private ReceiptAdapter mAdapter;

    private FragmentManager fragmentManager;




    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        toolbarTitle = getString(R.string.Receipts);
        super.onCreate(savedInstanceState);
        toolbarTitle = getString(R.string.Receipts);



        setHasOptionsMenu(true);
        Fragment newFragment = new EmptyReceiptBodyFragment();
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
        View view = inflater.inflate(R.layout.recycleview_for_menu,container,false);

        // Get the FragmentManager
        fragmentManager = requireActivity().getSupportFragmentManager();


//menu items

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor ReceiptCursor = mDatabaseHelper.getAllReceipt();
        mAdapter = new ReceiptAdapter(getContext(), ReceiptCursor);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerDepartmentClickListener(getContext(), recyclerView, new RecyclerDepartmentClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {



                    // Handle item click here


                        TextView idTextView = view.findViewById(R.id.id_text_view);
                        TextView DeptNameEditText = view.findViewById(R.id.name_text_view);
                        TextView DeptCodeEditText = view.findViewById(R.id.total_text_view);
                        TextView LastModifiedTextView = view.findViewById(R.id.transdate_edittex);

                        String id1 = idTextView.getText().toString();
                        String id = idTextView.getText().toString();
                        String name = DeptNameEditText.getText().toString();
                        String DeptCode = DeptCodeEditText.getText().toString();

                        // Example: navigate to ReceiptBodyFragment and pass ID
                        String itemId = name;
                        Fragment newFragment = ReceiptBodyFragment.newInstance(itemId);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                        fragmentTransaction.addToBackStack(newFragment.toString());
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Do whatever you want on long item click
                    }
                })
        );

        return view;
    }



}
