package com.accessa.ibora.product.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.accessa.ibora.product.couponcode.CouponDialogFragment;
import com.accessa.ibora.product.items.FirstFragment;

// extended from compatibility Fragment for pre-HC fragment support
public class MenuFragment extends Fragment {


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
        toolbarTitle = getString(R.string.Items);
        super.onCreate(savedInstanceState);
        toolbarTitle = getString(R.string.Items);
        menuList = new String[]{
                getString(R.string.Items),
                getString(R.string.Department),
                getString(R.string.SubDept),
                getString(R.string.Category),
                getString(R.string.vendor),
                getString(R.string.cost),
                getString(R.string.discount),
                getString(R.string.Coupon)
        };
        icons = new int[]{
                R.drawable.cart,
                R.drawable.department,
                R.drawable.department,
                R.drawable.category,
                R.drawable.vendor,
                R.drawable.cost,
                R.drawable.baseline_discount_24,
                R.drawable.coupon
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

                    toolbarTitle = getString(R.string.Items);
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
                    toolbarTitle = getString(R.string.Department);
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
                    toolbarTitle = getString(R.string.SubDept);
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

                } else if (position == 3) {
                    toolbarTitle = getString(R.string.Category);
                    Fragment newFragment = new CategoryFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();


                } else if (position == 4) {
                    toolbarTitle = getString(R.string.vendor);
                    Fragment newFragment = new VendorFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

                } else if (position == 5) {
                    toolbarTitle = getString(R.string.cost);
                    Fragment newFragment = new CostFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                } else if (position == 6) {
                    toolbarTitle = getString(R.string.discount);
                    Fragment newFragment = new DiscountFragment();
                    // create a FragmentManager
                    FragmentManager fm = getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.bodyFragment, newFragment);
                    fragmentTransaction.addToBackStack(newFragment.toString());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                } else if (position == 7) {
                    toolbarTitle = getString(R.string.couponcode);

                    // Show the CouponDialogFragment
                    CouponDialogFragment couponDialogFragment = new CouponDialogFragment();
                    couponDialogFragment.show(getFragmentManager(), "coupon_dialog");
                }


                else {
                    toolbarTitle = getString(R.string.Items); // Set a default value if needed
                }

                // Set the toolbar title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
            }
        });







        return view;
    }



}
