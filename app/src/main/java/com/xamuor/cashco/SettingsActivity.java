package com.xamuor.cashco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xamuor.cashco.Views.NewUserFragment;
import com.xamuor.cashco.Views.SettingsFragment;
import com.xamuor.cashco.cashco.R;

public class SettingsActivity extends AppCompatActivity {
    private ImageView imgNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        Initiate widgets
        imgNewUser = findViewById(R.id.img_new_user);
        imgNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgNewUser.setVisibility(View.INVISIBLE);
                android.support.v4.app.FragmentManager fragmentManager = (SettingsActivity.this.getSupportFragmentManager());
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NewUserFragment myfragment = new NewUserFragment();  //your fragment
                // work here to add, remove, etc
                fragmentTransaction.replace(R.id.second_frag, myfragment);
                fragmentTransaction.commit();
            }
        });
        onRefresh();
    }
    private void onRefresh() {
        android.support.v4.app.FragmentManager fragmentManager = (SettingsActivity.this.getSupportFragmentManager());
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment myfragment = new SettingsFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.first_frag, myfragment);
        fragmentTransaction.commit();
    }
}
