package com.xamuor.cashco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xamuor.cashco.Views.CategoriesFragment;
import com.xamuor.cashco.Views.CustomerFragment;
import com.xamuor.cashco.cashco.R;

public class DrawerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);

        if (MenuID.getMenuId() == 1) {
            android.support.v4.app.FragmentManager fragmentManager = (this.getSupportFragmentManager());
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CustomerFragment myfragment = new CustomerFragment();  //your fragment
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.menu_item_frgs, myfragment);
            fragmentTransaction.commit();
        } else if (MenuID.getMenuId() == 2){
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
           /* android.support.v4.app.FragmentManager fragmentManager = (this.getSupportFragmentManager());
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            InventoryFragment myfragment = new InventoryFragment();  //your fragment
            // work here to add, remove, etc
            fragmentTransaction.replace(R.id.menu_item_frgs, myfragment);
            fragmentTransaction.commit();*/
        }
        else if (MenuID.getMenuId() == 3) {
            android.support.v4.app.FragmentManager fragmentManager = (this.getSupportFragmentManager());
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CategoriesFragment myfragment = new CategoriesFragment();  //your fragment
            // work here to add, remove, etc
            fragmentTransaction.replace(R.id.menu_item_frgs, myfragment);
            fragmentTransaction.commit();
        }
    }



}


    //    To refresh the fragment
    /*private void onLoadFragment(FragmentManager fragment) {
        android.support.v4.app.FragmentManager fragmentManager = (this.getSupportFragmentManager());
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment myfragment = new fragment();  //your fragment

        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.menu_item_frg, myfragment);
        fragmentTransaction.commit();
    }*/

