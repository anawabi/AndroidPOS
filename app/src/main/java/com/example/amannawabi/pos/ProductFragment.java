package com.example.amannawabi.pos;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.TextView;

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
public class ProductFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static PosDatabase posDatabase;
    private Intent intent;
    private RecyclerView inventoryRv;
    private InventoryAdapter adapter;
    private List<InventoryDataModal> productList;


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        inventoryRv = view.findViewById(R.id.rcv_inventory);
        inventoryRv.setHasFixedSize(true);
        inventoryRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        Room Database
        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
        productList = new ArrayList<>();
//        Searchview
        SearchView searchCategory = view.findViewById(R.id.search_category);
        searchCategory.setQueryHint("Search...");
        searchCategory.setOnQueryTextListener(this);

        // call the method to load data
        loadInventoryData();
        return view;



    }

    // Load Inventory data for a specific authenticated System-Admin
    private void loadInventoryData() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Data loading...");
        pd.show();

        StringRequest inventoryReqeust = new StringRequest(Request.Method.POST, MyUrl.setUrl("loadProduct"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            InventoryDataModal dataModal = new InventoryDataModal(jsonObject.getInt("item_id"), jsonObject.getString("item_image"), jsonObject.getString("item_name"), jsonObject.getDouble("sell_price"));
                            productList.add(dataModal);
                        }
                        adapter = new InventoryAdapter(productList, getContext());
                        inventoryRv.setAdapter(adapter);
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
        Volley.newRequestQueue(getContext()).add(inventoryReqeust);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String name) {
        String userInput = name.toLowerCase();

        List<InventoryDataModal> newList = new ArrayList<>();
        for (InventoryDataModal category : productList) {
            if ((category.getProductName().toLowerCase().contains(userInput))) {
                newList.add(category);
            }
        }
        adapter.onUpdateList(newList);
        return true;
    }
}
