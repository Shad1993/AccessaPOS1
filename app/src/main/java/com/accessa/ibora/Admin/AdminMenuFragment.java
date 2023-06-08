package com.accessa.ibora.Admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.product.Cost.CostFragment;
import com.accessa.ibora.product.Department.DepartmentFragment;
import com.accessa.ibora.R;
import com.accessa.ibora.product.Discount.DiscountFragment;
import com.accessa.ibora.product.SubDepartment.SubDepartmentFragment;
import com.accessa.ibora.product.Vendor.VendorFragment;
import com.accessa.ibora.product.category.CategoryFragment;
import com.accessa.ibora.product.items.FirstFragment;
import com.accessa.ibora.product.menu.CustomAdapter;

// extended from compatibility Fragment for pre-HC fragment support
public class AdminMenuFragment extends Fragment {


    private String toolbarTitle;
    private ListView simpleList;
    private String[] menuList;
    private int[] icons;



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
                getString(R.string.CompanyInfo)
        };
        icons = new int[]{
                R.drawable.cashier,
                R.drawable.key,
                R.drawable.comp
        };

        setHasOptionsMenu(true);
        Fragment newFragment = new FirstFragment();
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

                    toolbarTitle = getString(R.string.People);
                    // Create new fragment and transaction
                    Fragment newFragment = new FirstFragment();
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
                    Fragment newFragment = new DepartmentFragment();
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
                    Fragment newFragment = new SubDepartmentFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }else {
                    toolbarTitle = getString(R.string.Admin); // Set a default value if needed
                }

                // Set the toolbar title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
            }
        });







        return view;
    }



}
