package com.xamuor.cashco.cashco;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryEditFragment extends Fragment {
    private EditText editCtgName, editCtgDes;
    private Button btnEdit, btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_category_edit, container, false);
//    Initiate widgets
        editCtgName = view.findViewById(R.id.edit_change_ctg_name);
        editCtgDes = view.findViewById(R.id.edit_change_ctg_desc);
        btnEdit = view.findViewById(R.id.btn_edit_category);
        btnCancel = view.findViewById(R.id.btn_cancel_category);
//        set values into editTexts from static variables;
        editCtgName.setText(CategoryRelatedFragment.ctgName);
        editCtgDes.setText(CategoryRelatedFragment.ctgDesc);

//        Edit Category
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ctgID = CategoryRelatedFragment.ctgId;
                String ctgName = editCtgName.getText().toString();
                String ctgDesc = editCtgDes.getText().toString();
                if (ctgName.isEmpty()) {
                    editCtgName.setError("Category name required.");
                } else {
                    onEditCategory(ctgID, ctgName, ctgDesc);
                }
            }
        });
//        Cancel Editing category
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryDataModal modal = new CategoryDataModal(CategoryRelatedFragment.ctgId, CategoryRelatedFragment.ctgName, CategoryRelatedFragment.ctgDesc);
                CategoryRelatedFragment.onRefreshCategoryRelated(modal, getContext());
            }
        });
    return view;
    }
// Send data to SERVER to edit category
    private void onEditCategory(final int id, final String name, @Nullable final String desc) {
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("editCategory"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    editCtgName.getText().clear();
                    editCtgDes.getText().clear();
                    Toast.makeText(getContext(), "Category changed successfully!", Toast.LENGTH_SHORT).show();
                    onRefreshCategoryFragment();
                } else if (response.trim().equals("fail")) {
                    Toast.makeText(getContext(), "Sorry, category not change, please, try again.", Toast.LENGTH_SHORT).show();
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
                map.put("ctgId", String.valueOf(id));
                map.put("ctgName", name);
                map.put("ctgDesc", desc);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

//    Refresh CategoryFragment
    private void onRefreshCategoryFragment() {
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoriesFragment myfragment = new CategoriesFragment();  //your fragment
        fragmentTransaction.replace(R.id.menu_item_frgs, myfragment);
        fragmentTransaction.commit();
    }
}
