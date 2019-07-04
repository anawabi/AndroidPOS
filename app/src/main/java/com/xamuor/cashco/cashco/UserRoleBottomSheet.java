package com.xamuor.cashco.cashco;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UserRoleBottomSheet extends BottomSheetDialogFragment {
    private RadioGroup rdgUserRole;
    private RadioButton rdbAdmin, rdbStockManager, rdbCashier, rdbSelected;
    private Button btnSetRole;
    private String roleValue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_role_bottom_sheet, container, false);
//        Initiate widgets
        rdgUserRole = view.findViewById(R.id.rdg_user_role);
        rdbAdmin = view.findViewById(R.id.rdb_admin);
        rdbStockManager = view.findViewById(R.id.rdb_stock_manager);
        rdbCashier = view.findViewById(R.id.rdb_cashier);
        btnSetRole = view.findViewById(R.id.btn_set_user_role);
        if (SettingsAdapter.userRole.equalsIgnoreCase("System Admin")) {
            rdbAdmin.setChecked(true);
        } else if (SettingsAdapter.userRole.equalsIgnoreCase("Stock Manager")) {
            rdbStockManager.setChecked(true);
        } else if (SettingsAdapter.userRole.equalsIgnoreCase("Cashier")) {
            rdbCashier.setChecked(true);
        }

        rdgUserRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rdb_admin:
                        roleValue = rdbAdmin.getText().toString();
                        break;
                    case R.id.rdb_stock_manager:
                        roleValue = rdbStockManager.getText().toString();
                        break;
                    case R.id.rdb_cashier:
                        roleValue = rdbCashier.getText().toString();
                }
            }
        });
        btnSetRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetRole(SettingsAdapter.userId, roleValue);
//                Close bottomSheet after role set
                UserRoleBottomSheet d = new UserRoleBottomSheet();
                d.show(( getActivity()).getSupportFragmentManager(), "Set Role");
                d.dismiss();
            }
        });
        return view;
    }

//    Set role for a user
    private void onSetRole(final int id, final String role) {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("setUserRole"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getContext(), "Role set successfully!", Toast.LENGTH_SHORT).show();
                    onRefresh();
                } else if (response.trim().equals("fail")) {
                    Toast.makeText(getContext(), "Sorry, role not set, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                d.setMessage(error.toString());
                d.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(id));
                map.put("role", role);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }
// To refresh users when user-role changed
    private void onRefresh() {
        android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment myfragment = new SettingsFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.first_frag, myfragment);
        fragmentTransaction.commit();
    }
}
