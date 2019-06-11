package com.example.amannawabi.pos;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class InventoryFragment extends Fragment {
    private RecyclerView inventory2rv;
    private Inventory2Adapter adapter;
    List<Inventory2DataModal> inventoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        inventory2rv = v.findViewById(R.id.rv_inventory);
        inventory2rv.setHasFixedSize(true);
        inventory2rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        inventoryList = new ArrayList<>();
        loadInventory();
        return v;
    }
//    load all products from server
private void loadInventory() {
    StringRequest request = new StringRequest(Request.Method.POST, MyUrl.setUrl("loadProduct"), new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONArray productArray = new JSONArray(response);
                for (int a = 0; a < productArray.length(); a++) {
                    JSONObject productObject = productArray.getJSONObject(a);
                    Inventory2DataModal dataModal = new Inventory2DataModal(productObject.getInt("item_id"),
                            productObject.getString("item_image"), productObject.getString("item_name"), productObject.getInt("quantity"), productObject.getDouble("purchase_price"),
                            productObject.getDouble("sell_price"), productObject.getString("created_at"));
                    inventoryList.add(dataModal);
                }
                adapter = new Inventory2Adapter(getContext(), inventoryList);
                inventory2rv.setAdapter(adapter);
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
            map.put("compId", MyCompany.getCompanyId()+"");
            return map;
        }
    };
    Volley.newRequestQueue(getContext()).add(request);
}
}
