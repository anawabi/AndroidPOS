package com.xamuor.cashco.pos;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class ListProductFragment extends Fragment {
    private RecyclerView listProductRV;
    private List<ListProductDataModal> productList;
    private ListProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_list_product, container, false);
    listProductRV = view.findViewById(R.id.product_rv);
    productList = new ArrayList<>();
    listProductRV.setHasFixedSize(true);
    listProductRV.setLayoutManager(new GridLayoutManager(getActivity(), 5));
    loadProducts(CategoryRelatedFragment.ctgId);
    return view;
    }

//    Load all products for a specific category from server
    private void loadProducts(final int ctgId) {
        StringRequest request = new StringRequest(Request.Method.POST, MyUrl.setUrl("listProduct"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int p = 0; p < jsonArray.length(); p++) {
                        JSONObject jo = jsonArray.getJSONObject(p);
                        int pId = jo.getInt("item_id");
                        String pImage = jo.getString("item_image");
                        String pName = jo.getString("item_name");
                        ListProductDataModal modal = new ListProductDataModal(pId, pImage, pName);
                        productList.add(modal);

                    }
                    adapter = new ListProductAdapter(getContext(), productList);
                    listProductRV.setAdapter(adapter);
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
                map.put("compId", String.valueOf(MyCompany.getCompanyId()));
                map.put("ctgId", String.valueOf(ctgId));
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_for_products, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.mnu_edit:
                Toast.makeText(getActivity(), "Editing product...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnu_delete:
                Toast.makeText(getActivity(), "Deleting product...", Toast.LENGTH_SHORT).show();
        }
        this.adapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }
}
