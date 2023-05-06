package com.accessa.ibora.product.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.accessa.ibora.MainActivity;
import com.accessa.ibora.R;
import com.accessa.ibora.product.items.FirstFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

// extended from compatibility Fragment for pre-HC fragment support
public class MenuFragment extends Fragment {
    ListView simpleList;
    String Menulist[] = {"Items", "Department", "Sub department","Category", "Discount"};
    int icons[] = {R.drawable.cart, R.drawable.department, R.drawable.department, R.drawable.category, R.drawable.baseline_discount_24};
    // views
    Button btn1,btn2;

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
        super.onCreate(savedInstanceState);
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
        CustomAdapter customAdapter = new CustomAdapter(getContext(), Menulist, icons);
        simpleList.setAdapter(customAdapter);


        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View  view, int position, long id)
            {
                if(position==0){

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
                } else if (position==3) {

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

                }else if (position==4) {
                    Toast.makeText(getContext(), "soon coming for discounts" + position, Toast.LENGTH_SHORT).show();
                }else if (position==1) {
                    Toast.makeText(getContext(), "soon coming for department" + position, Toast.LENGTH_SHORT).show();
                }else if (position==2) {
                    Toast.makeText(getContext(), "soon coming for sub department" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });




        //toolbar
        MaterialToolbar toolbar = view.findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                if(id==R.id.nav_home){
                    Toast.makeText(getContext(), "Home is Clicked", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.nav_message) {
                    Toast.makeText(getContext(), "Message is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.synch) {
                    Toast.makeText(getContext(), "Synch is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.trash) {
                    Toast.makeText(getContext(), "Trash is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.settings) {
                    Toast.makeText(getContext(), "settings is Clicked",Toast.LENGTH_SHORT).show();
                }
                else if (id==R.id.nav_login) {
                    Toast.makeText(getContext(), "login is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.nav_share) {
                    Toast.makeText(getContext(), "share is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.settings) {
                    Toast.makeText(getContext(), "settings is Clicked",Toast.LENGTH_SHORT).show();
                }else if (id==R.id.nav_rate) {
                    Toast.makeText(getContext(), "rate is Clicked",Toast.LENGTH_SHORT).show();
                }


                return true;
            }
        });


        return view;
    }



}
