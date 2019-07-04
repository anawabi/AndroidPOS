package com.xamuor.cashco.cashco;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerBalanceFragment extends Fragment {
    private RecyclerView custBalanceRV;
    private List<BalanceDataModal> balanceList;
    private BalanceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_balance, container, false);
        custBalanceRV = view.findViewById(R.id.rv_balance);
        custBalanceRV.setHasFixedSize(true);
        custBalanceRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        balanceList = new ArrayList<>();

//        fetch data through Bundle
        Bundle bundle = getArguments();
        int custId = bundle.getInt("custId");
        loadCustBalance(custId);

//        swipe-delete any balance
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                BalanceDataModal modal = balanceList.get(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), "Item deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(custBalanceRV);
        return view;
    }

    private void loadCustBalance(final int customer) {
        final StringRequest balanceRequest = new StringRequest(Request.Method.POST, Routes.setUrl("customerBalance"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int b = 0; b < jsonArray.length(); b++) {
                        JSONObject jo = jsonArray.getJSONObject(b);
                        int invoiceID = jo.getInt("inv_id");
                        String customer = jo.getString("cust_name");
                        String payType = jo.getString("payment_type");
                        double recieved = jo.getDouble("recieved_amount");
                        String recievable = jo.getString("recievable_amount");
                        Log.d("CustName", customer);
                        BalanceDataModal modal = new BalanceDataModal(invoiceID, payType, customer, recieved, recievable, "2019");
                        balanceList.add(modal);
                    }
                    adapter = new BalanceAdapter(balanceList, getContext());
                    custBalanceRV.setAdapter(adapter);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("custId", String.valueOf(customer));
                map.put("compId", String.valueOf(Users.getCompanyId()));
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(balanceRequest);
    }

}
