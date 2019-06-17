package com.xamuor.cashco.pos;


import android.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCategoryFragment extends Fragment {
    private Button btnAddCategory, btnCancelCategory;
    private EditText editCategoryName, editCategoryDesc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);
//        Initiate widgets
        btnAddCategory = view.findViewById(R.id.btn_add_category);
        btnCancelCategory = view.findViewById(R.id.btn_cancel_category);
        editCategoryName = view.findViewById(R.id.edit_category_name);
        editCategoryDesc = view.findViewById(R.id.edit_category_desc);

//        Add new category
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ctgName = editCategoryName.getText().toString();
                String ctgDesc = editCategoryDesc.getText().toString();

                if (ctgName.isEmpty()) {
                    editCategoryName.setError("Category required!");
                } else {
                    onSendCategory(ctgName, ctgDesc);
                }
            }
        });

        btnCancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                data-modal is required to refresh CategoryRelatedFragment
                CategoryDataModal modal = new CategoryDataModal(editCategoryName.getText().toString(), editCategoryDesc.getText().toString());
                CategoryRelatedFragment.onRefreshCategoryRelated(modal, getContext());
            }
        });
        return view;
    }
    // Send categores to server
private void onSendCategory(final String ctgName, @Nullable final String ctgDesc) {
    StringRequest request = new StringRequest(Request.Method.POST, MyUrl.setUrl("newCategory"), new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (response.trim().equals("success")) {
                Toast.makeText(getContext(), "Category added successfully!", Toast.LENGTH_SHORT).show();
                editCategoryName.getText().clear();
                editCategoryDesc.getText().clear();
//                Call to refresh fragment
                onRefershCategory();
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
            map.put("ctgName", ctgName);
            map.put("ctgDesc", ctgDesc);
            return map;
        }
    };
    Volley.newRequestQueue(getActivity()).add(request);
}
// Refresh Category-fragment
    private void onRefershCategory() {
        //                CustomerDetailFragment to show more detail for any customer
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryRelatedFragment myfragment = new CategoryRelatedFragment();  //your fragment
        fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
        fragmentTransaction.commit();
    }

}
