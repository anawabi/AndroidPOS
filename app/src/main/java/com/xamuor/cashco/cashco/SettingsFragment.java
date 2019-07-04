package com.xamuor.cashco.cashco;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
public class SettingsFragment extends Fragment {
    private RecyclerView settingsRV;
    private List<SettingsDataModal> userList;
    private SettingsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
        //        Initiate widgets
        settingsRV = view.findViewById(R.id.settings_rv);
        userList = new ArrayList<>();
        settingsRV.setHasFixedSize(true);
        settingsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        onLoadUsers();
    return view;
    }
    //    Load users from server
    private void onLoadUsers() {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("listUser"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int u = 0; u < jsonArray.length(); u++) {
                        JSONObject jo = jsonArray.getJSONObject(u);
                        int userId = jo.getInt("id");
                        String photo = jo.getString("photo");
                        String fname = jo.getString("name");
                        String lname = jo.getString("lastname");
                        String phone = jo.getString("phone");
                        String role = jo.getString("role");
                        int status = jo.getInt("status");
                        SettingsDataModal modal = new SettingsDataModal(userId, photo, fname, lname, phone, role, status);
                        userList.add(modal);
                    }
                    adapter = new SettingsAdapter(userList, getContext());
                    settingsRV.setAdapter(adapter);
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
                map.put("compId", String.valueOf(Users.getCompanyId()));
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

}
