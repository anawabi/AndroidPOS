package com.xamuor.cashco.pos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private List<CustomerDataModal> custList;
    private Context context;
    public CustomerAdapter(Context context, List<CustomerDataModal> custList) {
        this.context = context;
        this.custList = custList;
    }

    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_data_modal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerAdapter.ViewHolder holder, int position) {
        final CustomerDataModal modal = custList.get(position);
        holder.txtCustFname.setText(modal.getCustFname().concat(" ").concat(modal.getCustLname().replace("null", "")));
//        check customer-status
        int custStatus = modal.getCustStatus();
        if (custStatus == 1) {
            holder.switchCustStatus.setChecked(true);

        }
        holder.cardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerEditFragment.onRefreshCustomerDetailFragment(modal, context);
              /*  Bundle custValues = new Bundle();
//                Send customer firstname & lastname through Bundle to CustomerDetailFragment
                custValues.putInt("c_id", modal.getCustId());
                custValues.putString("c_name", modal.getCustFname());
                custValues.putString("c_lname", modal.getCustLname());
                custValues.putString("c_phone", modal.getCustPhone());
                custValues.putString("c_email", modal.getCustEmail());
                custValues.putString("c_state", modal.getCust_state());
                custValues.putString("c_address", modal.getCust_addr());

//                CustomerDetailFragment to show more detail for any customer
                FragmentManager fragmentManager =  ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CustomerDetailFragment myfragment = new CustomerDetailFragment();  //your fragment
                myfragment.setArguments(custValues);
                fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
                fragmentTransaction.commit();*/
            }
        });
        holder.switchCustStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int statusValue = 0;
                boolean status = holder.switchCustStatus.isChecked();
                if (status) {
                    statusValue = 1;
                    onChangeCustomerStatus(modal.getCustId(), statusValue, modal.getCustFname());
                } else {
                    statusValue = 0;
                    onChangeCustomerStatus(modal.getCustId(), statusValue, modal.getCustFname());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return custList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView imageCustomer;
        private CardView cardCustomer;
        private TextView txtCustFname;
        private SwitchCompat switchCustStatus;
        public ViewHolder(View itemView) {
            super(itemView);
//            imageCustomer = itemView.findViewById(R.id.image_customer);
            txtCustFname = itemView.findViewById(R.id.txt_cust_fullname);
            cardCustomer = itemView.findViewById(R.id.card_customer);
            switchCustStatus = itemView.findViewById(R.id.switch_cust_status);

        }
    }

//    Change customer-status (Active or Inactive)
  private void onChangeCustomerStatus(final int custId, final int statusValue, final String customer) {
      StringRequest statusRequest = new StringRequest(Request.Method.POST, MyUrl.setUrl("customerStatus"), new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (response.trim().equals("active")) {
                Toast.makeText(context,  customer + " is active.", Toast.LENGTH_SHORT).show();
            } else if (response.trim().equals("inactive")){
                Toast.makeText(context, customer + " is inactive.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something is wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              AlertDialog.Builder d = new AlertDialog.Builder(context);
              d.setMessage(error.toString());
              d.show();
          }
      }) {
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> map = new HashMap<>();
              map.put("custId", String.valueOf(custId));
              map.put("statusValue", String.valueOf(statusValue));
              return map;
          }
      };
      Volley.newRequestQueue(context).add(statusRequest);
  }
}
