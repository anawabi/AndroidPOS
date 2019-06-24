package com.xamuor.cashco.pos;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
