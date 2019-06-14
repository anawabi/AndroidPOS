package com.xamuor.cashco.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    private TextView txtCustFname, txtCustAddress, txtCustPhone, txtCustEmail;
    private ImageView imgNewCustomer, imgCustBalance, imgEditCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_customer_detail, container, false);
//        Initaiting Widgets
        txtCustFname = view.findViewById(R.id.txt_cust_detail_fname);
        imgNewCustomer = view.findViewById(R.id.img_detail_add_customer);
        imgCustBalance = view.findViewById(R.id.img_detail_balance);
        imgEditCustomer = view.findViewById(R.id.img_cust_detail_edit);
        txtCustAddress = view.findViewById(R.id.txt_cust_detail_address);
        txtCustPhone = view.findViewById(R.id.txt_cust_detail_phone);
        txtCustEmail = view.findViewById(R.id.txt_cust_detail_email);

//        To add new customer
        imgNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewCustomerActivity.class);
                startActivity(intent);
            }
        });

//        To recieve customer values from CustomerFragment through Bundle
        Bundle bundle = getArguments();
        final int custId = bundle.getInt("c_id");
        final String custName = bundle.getString("c_name");
        final String custLastName = bundle.getString("c_lname");
        final String custPhone = bundle.getString("c_phone");
        final String custEmail = bundle.getString("c_email").replace("null", "");
        final String custState = bundle.getString("c_state");
        final String custAddress = bundle.getString("c_address");
//        Set the fetched customer-values
        txtCustFname.setText(custName.toString() + " " + custLastName.toString().replace("null", ""));
        txtCustAddress.setText(custState.toString() + ", " + custAddress);
        txtCustPhone.setText(custPhone.toString());
        txtCustEmail.setText(custEmail.toString());

        //        To edit customers
        imgEditCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("cId", custId);
                b.putString("cName", custName);
                b.putString("cLname", custLastName);
                b.putString("cPhone", custPhone);
                b.putString("cEmail", custEmail);
                b.putString("cState", custState);
                b.putString("cAddress", custAddress);
                android.support.v4.app.FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CustomerEditFragment myfragment = new CustomerEditFragment();  //your fragment
                myfragment.setArguments(b);
                // work here to add, remove, etc
                fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
                fragmentTransaction.commit();



            }
        });
        return view;
    }

}
