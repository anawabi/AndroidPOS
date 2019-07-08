package com.xamuor.cashco.Views;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.CustomerAdapter;
import com.xamuor.cashco.Model.CustomerDataModal;
import com.xamuor.cashco.cashco.R;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerFragment extends Fragment {
    private RecyclerView customerRv;
    private CustomerAdapter adapter;
    private List<CustomerDataModal> custList;
    private SwitchCompat switchCustStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        customerRv = view.findViewById(R.id.customer_rv);
        customerRv.setHasFixedSize(true);
        customerRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        Initiating widgets...
        switchCustStatus = view.findViewById(R.id.switch_cust_status);
        // calling the method for fetching customer-data
        custList = new ArrayList<>();
        loadCustomerData();

//        Who should set customer's status
        return view;
    }
// load customer-data from server
    private void loadCustomerData() {
        int cStatus = 0;
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("listCustomer"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int c = 0; c < jsonArray.length(); c++) {
                        JSONObject jo = jsonArray.getJSONObject(c);
                        int custId = jo.getInt("cust_id");
                        String custFname = jo.getString("cust_name");
                        String custLname = jo.getString("cust_lastname");
                        int custStatus = jo.getInt("cust_status");
                        String custPhone = jo.getString("cust_phone");
                        String custEmail = jo.getString("cust_email");
                        String custState = jo.getString("cust_state");
                        String custAddress = jo.getString("cust_addr");
                        CustomerDataModal dataModal = new CustomerDataModal(custId, custStatus, custFname, custLname, custPhone, custEmail, custState, custAddress);
                        custList.add(dataModal);
                    }
                    adapter = new CustomerAdapter(getContext(), custList);
                    customerRv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("compId", Users.getCompanyId()+"");
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }

}
