package com.xamuor.cashco;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

import java.util.HashMap;
import java.util.Map;

public class NewCustomerActivity extends AppCompatActivity {
    //    Define widegets
    private EditText editCustName, editCustLastname, editCustPhone, editCustEmail, editCustState, editCustAddress;
    private Button btnRegisterCustomer;
    private String custName, custLastName, custPhone, custEmail, custState, custAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
//        Initiate register-button
        btnRegisterCustomer = findViewById(R.id.btn_register_customer);
        btnRegisterCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onValidate();
            }
        });
    }

//    Validate inputs
    private void onValidate() {
        //        Initiate the widgets
        editCustName = findViewById(R.id.edit_cust_name);
        editCustLastname = findViewById(R.id.edit_cust_lastname);
        editCustPhone = findViewById(R.id.edit_cust_phone);
        editCustEmail = findViewById(R.id.edit_cust_email);
        editCustState = findViewById(R.id.edit_cust_state);
        editCustAddress = findViewById(R.id.edit_cust_address);

        custName = editCustName.getText().toString();
        custLastName = editCustLastname.getText().toString();
        custPhone = editCustPhone.getText().toString();
        custEmail = editCustEmail.getText().toString();
        custState = editCustState.getText().toString();
        custAddress = editCustAddress.getText().toString();

//        validate any of input
        if (custName.isEmpty()) {
            editCustName.setError("Name Required.");
        }
        if (custPhone.isEmpty()) {
            editCustPhone.setError("Phone Required.");
        }
        if (custState.isEmpty()) {
            editCustState.setError("State/Province required.");
        }
        if (custAddress.isEmpty()) {
            editCustAddress.setError("Address required.");
        }
        if (!custEmail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(custEmail).matches()) {
            editCustEmail.setError("Sorry, wrong email address.");
        }
        if (!custName.isEmpty() && !custPhone.isEmpty() && !custState.isEmpty() && !custAddress.isEmpty()){
            onRegisterCustomer(custName, custLastName, custPhone, custEmail, custState, custAddress);
        }
    }
//    Register a customer using Volley
    private void onRegisterCustomer(final String name, final String lastname, final String phone, final String email, final String state, final String address) {
        //        Initiate the widgets
        editCustName = findViewById(R.id.edit_cust_name);
        editCustLastname = findViewById(R.id.edit_cust_lastname);
        editCustPhone = findViewById(R.id.edit_cust_phone);
        editCustEmail = findViewById(R.id.edit_cust_email);
        editCustState = findViewById(R.id.edit_cust_state);
        editCustAddress = findViewById(R.id.edit_cust_address);

//        send customer-data through request-of-volley
        StringRequest customerRequest = new StringRequest(Request.Method.POST, Routes.setUrl("newCustomer"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().contains("success")) {
                    Toast.makeText(NewCustomerActivity.this, "Customer registered successfully", Toast.LENGTH_SHORT).show();
                    editCustName.getText().clear();
                    editCustLastname.getText().clear();
                    editCustPhone.getText().clear();
                    editCustEmail.getText().clear();
                    editCustState.getText().clear();
                    editCustAddress.getText().clear();
//                    go back to inventory
                    Intent intent = new Intent(NewCustomerActivity.this, InventoryActivity.class);
                    startActivity(intent);
                } else if (response.trim().contains("fail")) {
                    Toast.makeText(NewCustomerActivity.this, "Customer registration failed, please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder d = new AlertDialog.Builder(NewCustomerActivity.this);
                d.setTitle(error.toString());
                d.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("compId", Users.getCompanyId() + "");
                map.put("custName", name);
                map.put("custLastName", lastname);
                map.put("custPhone", phone);
                map.put("custEmail", email);
                map.put("custState", state);
                map.put("custAddress", address);

                return map;
            }
        };
        Volley.newRequestQueue(NewCustomerActivity.this).add(customerRequest);
    }
}
