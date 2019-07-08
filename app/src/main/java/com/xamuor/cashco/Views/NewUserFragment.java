package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.cashco.R;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.SettingsActivity;
import com.xamuor.cashco.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUserFragment extends Fragment {
    private EditText editFname, editLname, editPhone, editEmail, editPassword, editConfirm;
    private Spinner spnRole;
    private Button btnAddUser, btnCancelUser;
    private String fname, lname, phone, email, password, confirm, role;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);
    //        Initiate widgets
            editFname = view.findViewById(R.id.edit_user_fname);
            editLname = view.findViewById(R.id.edit_user_lname);
            editPhone = view.findViewById(R.id.edit_user_phone);
            editEmail = view.findViewById(R.id.edit_user_email);
            editPassword = view.findViewById(R.id.edit_user_password);
            editConfirm = view.findViewById(R.id.edit_user_confirm);
            spnRole = view.findViewById(R.id.spn_user_role);
            btnAddUser = view.findViewById(R.id.btn_add_user);
            btnCancelUser = view.findViewById(R.id.btn_cancel_user);
    //            Add user
            btnAddUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fname = editFname.getText().toString();
                    lname = editLname.getText().toString();
                    phone = editPhone.getText().toString();
                    email = editEmail.getText().toString();
                    password = editPassword.getText().toString();
                    confirm = editConfirm.getText().toString();

                    if (fname.isEmpty()) {
                        editFname.setError("First name required.");
                    }
                    if (lname.isEmpty()) {
                        editLname.setError("Last name required.");
                    }
                    if (phone.isEmpty()) {
                        editPhone.setError("Phone required.");
                    }
                    if (password.isEmpty()) {
                        editPassword.setError("Password required.");
                    }
                    if (confirm.isEmpty()) {
                        editConfirm.setError("Password confirmation required.");
                    }
                    if (!fname.isEmpty() && !lname.isEmpty() && !phone.isEmpty() && !password.isEmpty() && !confirm.isEmpty()) {
                        if (!password.equals(confirm)) {
                            Toast.makeText(getContext(), "Sorry, password does not match, try again!", Toast.LENGTH_LONG).show();
                            editPassword.getText().clear();
                            editConfirm.getText().clear();
                        } else {
                            onRegisterUser(fname, lname, phone, email, password);
                        }

                    }
                }
            });
            btnCancelUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
    //        Get role from spinner
            spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    role = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        return view;
    }
// Send User-data to server
    private void onRegisterUser(final String fname, final String lname, final String phone, final String email, final String password) {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("registerUser"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String r = jsonObject.getString("user_msg");
                    Toast.makeText(getContext(), r, Toast.LENGTH_LONG).show();
                    editFname.getText().clear();
                    editLname.getText().clear();
                    editPhone.getText().clear();
                    editEmail.getText().clear();
                    editPassword.getText().clear();
                    editConfirm.getText().clear();
                   onRefresh();
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Map<String , String> map = new HashMap<>();
                map.put("compId", String.valueOf(Users.getCompanyId()));
                map.put("userId", String.valueOf(Users.getUserId()));
                map.put("uName", fname);
                map.put("uLastname", lname);
                map.put("uPhone", phone);
                map.put("uEmail", email);
                map.put("uRole", role);
                map.put("uPassword", password);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void onRefresh() {
        android.support.v4.app.FragmentManager fragmentManager = (getActivity().getSupportFragmentManager());
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment myfragment = new SettingsFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.first_frag, myfragment);
        fragmentTransaction.commit();
    }
}
