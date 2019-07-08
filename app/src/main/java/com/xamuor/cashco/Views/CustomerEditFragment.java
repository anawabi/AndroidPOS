package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Model.CustomerDataModal;
import com.xamuor.cashco.cashco.R;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.Users;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerEditFragment extends Fragment {
    private EditText editCustName, editCustLastname, editCustPhone, editCustEmail, editCustState, editCustAddress;
    private Button btnEdit, btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_customer_edit, container, false);
//        Fetch values from Bundle
        Bundle bundle = getArguments();
        final int custId = bundle.getInt("cId");
        String custName = bundle.getString("cName");
        final String custLastName = bundle.getString("cLname").replace("null", "");
        String custPhone = bundle.getString("cPhone");
        String custEmail = Objects.requireNonNull(bundle.getString("cEmail")).replace("null", "");
        String custState = bundle.getString("cState");
        String custAddress = bundle.getString("cAddress");
//        Initiating the bove defined widgets
        editCustName = view.findViewById(R.id.edit_change_cust_name);
        editCustLastname = view.findViewById(R.id.edit_change_cust_lastname);
        editCustPhone = view.findViewById(R.id.edit_change_cust_phone);
        editCustEmail = view.findViewById(R.id.edit_change_cust_email);
        editCustState = view.findViewById(R.id.edit_change_cust_state);
        editCustAddress = view.findViewById(R.id.edit_change_cust_address);
        
//        Set values inside editTextes to edit
        editCustName.setText(custName);
        editCustLastname.setText(custLastName);
        editCustPhone.setText(custPhone);
        editCustEmail.setText(custEmail);
        editCustState.setText(custState);
        editCustAddress.setText(custAddress);
        btnEdit = view.findViewById(R.id.btn_edit_customer);
        btnCancel = view.findViewById(R.id.btn_cancel_edit_customer);

//        Now edit customer
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (editCustName.getText().toString().isEmpty()) {
                        editCustName.setError("Name required.");
                    }
                    if (editCustPhone.getText().toString().isEmpty()) {
                        editCustPhone.setError("Phone required.");
                    }
                    if (editCustState.getText().toString().isEmpty()) {
                        editCustState.setError("State/Province required");
                    }
                    if (editCustAddress.getText().toString().isEmpty()) {
                        editCustAddress.setError("Address required.");
                    }
                    if (!editCustName.getText().toString().isEmpty() && !editCustPhone.getText().toString().isEmpty()
                    && !editCustState.getText().toString().isEmpty() && !editCustAddress.getText().toString().isEmpty()){
                        final String custFName = editCustName.getText().toString();
                        final String custLName = editCustLastname.getText().toString();
                        final String custPhone = editCustPhone.getText().toString();
                        final String custEmail = editCustEmail.getText().toString();
                        final String custState = editCustState.getText().toString();
                        final String custAddress = editCustAddress.getText().toString();
/*
//                        to avoid calling method inside worker thread because it is for background tasks
                        Handler handler = new Handler(Looper.myLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                    // Call method to edit customer
                                    onEditCustomer(custId, custFName, custLName, custPhone, custEmail, custState, custAddress);
                                }
                            }
                        });*/
                        onEditCustomer(custId, custFName, custLName, custPhone, custEmail, custState, custAddress);
//                        Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
                    }

            }
        });
//        Cancel customer-editing
        btnCancel.setOnClickListener(new View.OnClickListener() {
            final String custFName = editCustName.getText().toString();
            final String custLName = editCustLastname.getText().toString();
            final String custPhone = editCustPhone.getText().toString();
            final String custEmail = editCustEmail.getText().toString();
            final String custState = editCustState.getText().toString();
            final String custAddress = editCustAddress.getText().toString();
            CustomerDataModal modal = new CustomerDataModal(custId, 0, custFName, custLName, custPhone, custEmail, custState, custAddress);
            @Override
            public void onClick(View view) {
                onRefreshCustomerDetailFragment(modal, getContext());
            }
        });
        return  view;
    }
// Send data to server through to this method to edit a customer
    private void onEditCustomer(final int id, final String fname, final String lname, final String phone,
                                final String email, final String state, final String address) {
//        setting a progress-dialog
        final ProgressDialog dialog = new ProgressDialog(getContext());

        StringRequest custEditRequest = new StringRequest(Request.Method.POST, Routes.setUrl("editCustomer"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    CustomerDataModal modal = new CustomerDataModal(id, 0, fname, lname, phone, email, state, address);
                    Toast.makeText(getContext(), "Customer edited successfully!", Toast.LENGTH_SHORT).show();
                    onRefreshCustomerFragment();
                    onRefreshCustomerDetailFragment(modal, getContext());
                    dialog.dismiss();
                } else if (response.trim().equals("fail")) {
                    Toast.makeText(getContext(), "Sorry, customer not edited, try again.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
                map.put("custId", String.valueOf(id));
                map.put("compId", String.valueOf(Users.getCompanyId()));
                map.put("custName", fname);
                map.put("custLname", lname);
                map.put("custPhone", phone);
                map.put("custEmail", email);
                map.put("custState", state);
                map.put("custAddress", address);
              /*  dialog.setMessage("Sending request, just a moment...");
                dialog.show();*/
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(custEditRequest);
    }

    //    To refresh CustomerFragment
    private void onRefreshCustomerFragment() {
        android.support.v4.app.FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CustomerFragment myfragment = new CustomerFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.menu_item_frgs, myfragment);
        fragmentTransaction.commit();
    }
//    To refresh CustomerDetailFragment
    public static void onRefreshCustomerDetailFragment(@Nullable CustomerDataModal modal, @Nullable Context context) {
        Bundle custValues = new Bundle();
//                Send customer firstname & lastname through Bundle to CustomerDetailFragment
        custValues.putInt("c_id", modal.getCustId());
        custValues.putString("c_name", modal.getCustFname());
        custValues.putString("c_lname", modal.getCustLname());
        custValues.putString("c_phone", modal.getCustPhone());
        custValues.putString("c_email", modal.getCustEmail());
        custValues.putString("c_state", modal.getCust_state());
        custValues.putString("c_address", modal.getCust_addr());

//                CustomerDetailFragment to show more detail for any customer
        FragmentManager fragmentManager =  ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CustomerDetailFragment myfragment = new CustomerDetailFragment();  //your fragment
        myfragment.setArguments(custValues);
        fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
        fragmentTransaction.commit();
    }
}
