package com.xamuor.cashco.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerDetailFragment extends Fragment {
    private TextView custFname;
    private ImageView imgNewCustomer, imgCustBalance, imgEditCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_customer_detail, container, false);
//        Initaiting Widgets
        custFname = view.findViewById(R.id.txt_cust_detail_fname);
        imgNewCustomer = view.findViewById(R.id.img_detail_add_customer);
        imgCustBalance = view.findViewById(R.id.img_detail_balance);
        imgEditCustomer = view.findViewById(R.id.img_cust_detail_edit);
        imgNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewCustomerActivity.class);
                startActivity(intent);
            }
        });
        
//        To recieve customer values from CustomerFragment through Bundle
        Bundle bundle = getArguments();
        String custName = bundle.getString("c_name");
        String custLastName = bundle.getString("c_lname");
//        Set the fetched customer-values
        custFname.setText(custName.toString() + " " + custLastName.toString().replace("null", ""));
        return view;
    }

}
