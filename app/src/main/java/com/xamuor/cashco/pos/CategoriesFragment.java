package com.xamuor.cashco.pos;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
public class CategoriesFragment extends Fragment {
    private RecyclerView categoryRV;
    private List<CategoryDataModal> ctgList;
    private CategoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categoryRV = view.findViewById(R.id.category_rv);
        ctgList = new ArrayList<>();
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        Call method to load categories
        loadCategories();
        return view;
    }
// Load categories from server
    private void loadCategories() {
        StringRequest request = new StringRequest(Request.Method.POST, MyUrl.setUrl("listCategory"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int c = 0; c < jsonArray.length(); c++) {
                        JSONObject jo = jsonArray.getJSONObject(c);
                        String ctgName = jo.getString("ctg_name");
                        String ctgDesc = jo.getString("ctg_desc");
                        String ctgDate = jo.getString("created_at");
                        CategoryDataModal modal = new CategoryDataModal(ctgName, ctgDesc, ctgDate);
                        ctgList.add(modal);
                    }
                    adapter = new CategoryAdapter(getContext(), ctgList);
                    categoryRV.setAdapter(adapter);
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
                return  map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }
}
