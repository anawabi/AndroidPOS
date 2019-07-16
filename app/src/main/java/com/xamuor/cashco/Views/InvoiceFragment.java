package com.xamuor.cashco.Views;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xamuor.cashco.Adapters.InventoryAdapter;
import com.xamuor.cashco.Adapters.InvoiceAdapter;
import com.xamuor.cashco.Customer;
import com.xamuor.cashco.CustomerIDForInvoice;
import com.xamuor.cashco.Model.InvoiceDataModal;
import com.xamuor.cashco.NewCustomerActivity;
import com.xamuor.cashco.Product;
import com.xamuor.cashco.Users;
import com.xamuor.cashco.Utilities.PosDatabase;
import com.xamuor.cashco.Utilities.Routes;
import com.xamuor.cashco.cashco.R;

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
public class InvoiceFragment extends Fragment {
    private String custName;
    public static PosDatabase posDatabase;

    private Spinner spnCustomers;
    Intent intent;
    ListView invoiceListView;
    private CheckBox chkAllAmount;
    private LinearLayout layoutForPayment;
    private EditText editTransCode, editRecievable, editRecieved;
    private Spinner spnPaymentType;
    private ImageButton btnPrint;
    private Button btnCancel;
    private TextView txtTotal;
    private String paymentValue = "";

    public InvoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
//        Initiate textView of total-price
        txtTotal = view.findViewById(R.id.txt_total_price);
        //        Initiate widgets
        spnPaymentType = view.findViewById(R.id.spn_payment_type);
        layoutForPayment = view.findViewById(R.id.layout_for_payment);
        chkAllAmount = view.findViewById(R.id.chk_paid_all);
        editTransCode = view.findViewById(R.id.edit_trans_code);
        editRecievable = view.findViewById(R.id.edit_rcvable_amount);
        editRecieved = view.findViewById(R.id.edit_rcv_amount);
        ImageView imgNewCustomer = view.findViewById(R.id.img_new_customer);
        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
        imgNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), NewCustomerActivity.class);
                startActivity(intent);
            }
        });

//        Call onPayAmount()
        onPayAmount();
        //        Initiate button PRINT & CANCEL
        btnPrint = view.findViewById(R.id.btn_print);
        btnCancel = view.findViewById(R.id.btn_cancel);
//        Call to enable/disable btnCancel
        onCheckCart();

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateSale();

            }
        });
//        Cancel invoices
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InvoiceFragment.posDatabase.myDao().delete();
                onRefresh();

            }
        });
//        Select customers -- dropdown list
        spnCustomers = view.findViewById(R.id.spn_select_customer);
        spnCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    chkAllAmount.setEnabled(false);
                    spnPaymentType.setEnabled(false);
                    layoutForPayment.setVisibility(View.GONE);
                    btnPrint.setEnabled(false);
                    btnPrint.setBackgroundColor(getResources().getColor(R.color.disable_color));
                } else {
                    String custName = (String) adapterView.getItemAtPosition(i);
// we give the name of customer, ROOM returns back its custID
                    int custId = InvoiceFragment.posDatabase.myDao().getCustId(custName);
                     CustomerIDForInvoice.setCustomerID(custId);
                    chkAllAmount.setEnabled(true);
                    spnPaymentType.setEnabled(true);
                    layoutForPayment.setVisibility(View.VISIBLE);
                    btnPrint.setEnabled(true);
                    btnPrint.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        invoiceListView = view.findViewById(R.id.list_invoice_content);
        onInvoice();
        // call method to load customers
        listCustomers();

        //        set permission for cashier
        if (Users.getRole().equalsIgnoreCase("cashier")) {
            imgNewCustomer.setEnabled(false);
            imgNewCustomer.setColorFilter(getActivity().getResources().getColor(R.color.disable_color));
            spnCustomers.setEnabled(false);
        }
        return view;
    }

    // Populate spinner with customers
    private void listCustomers() {
//     InvoiceFragment.posDatabase.myDao().deleteCustomer();
        StringRequest selectCustomerRequest = new StringRequest(Request.Method.POST, Routes.setUrl("listCustomer"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//             InvoiceFragment.posDatabase.myDao().deleteCustomer();
                if (response.trim().contains("not found")) {
                    Toast.makeText(getContext(), "Sorry, no customer existing, please register first.", Toast.LENGTH_LONG).show();
                } else {
                    try {
//                     Json-array for json data from server
                        JSONArray customers = new JSONArray(response);

//                     Customer object to insert data into ROOM
                     Customer customer = new Customer();
//                     To populate customers into it for spinner
                        List<String> customerList = new ArrayList<String>();
//                     Existing customers into ROOM database
                     List<Customer> existingCustomer = InvoiceFragment.posDatabase.myDao().getCustomers(Users.getCompanyId());
//                      First Delete previous customers
                        if (getActivity() != null) {
                            customerList.add(getResources().getString(R.string.spn_choose_customer));
                        }
                        for (int c = 0; c < customers.length(); c++) {
                            JSONObject custObject = customers.getJSONObject(c);
                            int custID = custObject.getInt("cust_id");
                            String custName = custObject.getString("cust_name");
//                            customerList.add(String.valueOf(custID));
//                         set json-values into ROOM
                                customer.setCompanyId(Users.getCompanyId());
                                customer.setCustomerId(custID);
                                customer.setCustomerName(custName);
                                InvoiceFragment.posDatabase.myDao().insertCustomer(customer);
                        }
                     for (Customer cust : existingCustomer) {
                         int rCustId = cust.getCustomerId();
                         String rCustName = cust.getCustomerName();
                             customerList.add(rCustName);
                     }
                     if (getActivity() != null) {
                         ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, customerList);
                         spnCustomers.setAdapter(customerAdapter);
                         customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                map.put("compId", Users.getCompanyId() + "");
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(selectCustomerRequest);
    }

    @SuppressLint("SetTextI18n")
    private void onInvoice() {

        InvoiceDataModal modal = null;
        ArrayList<InvoiceDataModal> list = new ArrayList<>();
//     Read from room-architecture
        List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
        double total = 0;
        for (Product p : dataList) {
            int qty = p.getProductQty();
            String item = p.getProductName();
            double price = p.getProductPrice();
            double subTotal = qty * price;
            modal = new InvoiceDataModal(qty, item, price, subTotal);
            list.add(modal);
            total = total + subTotal;
        }
        if (total > 0.0) {
            txtTotal.setVisibility(View.VISIBLE);
            txtTotal.setText("$" + total);
            editRecievable.setText(total + "");
        }
//     To print sold-items into listView
        InvoiceAdapter adapter = new InvoiceAdapter(getContext(), R.layout.invoice_datamodal_layout, list);
        invoiceListView.setAdapter(adapter);
        invoiceListView.deferNotifyDataSetChanged();
    }

    // see if all amount paid
    @SuppressLint("SetTextI18n")
    private void onPayAmount() {

        spnPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    editTransCode.setVisibility(View.GONE);
                } else {
                    editTransCode.setVisibility(View.VISIBLE);
                }

                paymentValue = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        chkAllAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkAllAmount.isChecked()) {
                    if (paymentValue.equals(getString(R.string.txt_cash_lble))) {
                        layoutForPayment.setVisibility(View.GONE);
                    } else {
                        layoutForPayment.setVisibility(View.VISIBLE);
                        editRecievable.setVisibility(View.INVISIBLE);
                        editRecieved.setVisibility(View.INVISIBLE);
                        editTransCode.setVisibility(View.VISIBLE);
                    }

                } else if (!chkAllAmount.isChecked()) {
                    layoutForPayment.setVisibility(View.VISIBLE);
                    editRecievable.setVisibility(View.VISIBLE);
                    editRecieved.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //    To send bill-contents to server
    @SuppressLint("SetTextI18n")
    private void onCreateSale() {
        final String transCode = editTransCode.getText().toString();
        final double recievable = Double.parseDouble(editRecievable.getText().toString());
        double rvd = 0;
        double rvable = 0;
        if (chkAllAmount.isChecked()) {
            final double paidAllValue = recievable;
            StringRequest saleRequest = new StringRequest(Request.Method.POST, Routes.setUrl("createSale"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.trim().contains("success!")) {
                        Toast.makeText(getContext(), "Sale was successful!", Toast.LENGTH_SHORT).show();
                        //                    The contents of invoice should be deleted
                        InvoiceFragment.posDatabase.myDao().delete();
//                      print the invoice
                        onPrint();
                        //To refresh the fragment
                        onRefresh();
                    } else if (response.trim().contains("fail")) {
                        Toast.makeText(getContext(), "Sorry, sale not done, please try again!", Toast.LENGTH_SHORT).show();
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
                    List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject;
                    double total = 0;
                    for (int i = 0; i < dataList.size(); i++) {
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", dataList.get(i).getProductId());
                            jsonObject.put("compId", Users.getCompanyId());
                            jsonObject.put("custId", CustomerIDForInvoice.getCustomerID());
                            int qty = dataList.get(i).getProductQty();
                            double price = dataList.get(i).getProductPrice();
                            double subtotal = qty * price;
                            jsonObject.put("qty", qty);
                            jsonObject.put("price", price);
                            jsonObject.put("subtotal", subtotal);
                            //                        total
                            total = total + subtotal;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("params", jsonArray.toString());
                    map.put("transCode", transCode);
//                   map.put("recievable", String.valueOf(finalRvable));
                    map.put("recieved", String.valueOf(paidAllValue));
                    map.put("payType", String.valueOf(paymentValue));
                    return map;
                }
            };
            Volley.newRequestQueue(getContext()).add(saleRequest);
        } else if (!chkAllAmount.isChecked()) {
            if (editRecieved.getText().toString().isEmpty()) {
                editRecieved.setError("Sorry, recieved amount cannot be blank.");
            } else {
                final double recieved = Double.parseDouble(editRecieved.getText().toString());
                if (recieved < recievable) {
                    double remainingAmount = recievable - recieved;
                    rvd = recieved;
                    rvable = remainingAmount;
                    editRecievable.setText(remainingAmount + "");
//                   Toast.makeText(getContext(), "Recieved: " + rvd + " Payment: " + paymentValue, Toast.LENGTH_SHORT).show();
                    final double finalRvd = rvd;
                    final double finalRvable = rvable;
                    StringRequest saleRequest = new StringRequest(Request.Method.POST, Routes.setUrl("createSale"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.trim().contains("success!")) {
                                Toast.makeText(getContext(), "Sale was successful!", Toast.LENGTH_LONG).show();
                                //                    The contents of invoice should be deleted
                                InvoiceFragment.posDatabase.myDao().delete();
//                                print the invoice
                             onPrint();
                                //To refresh the fragment
                                onRefresh();
                            } else if (response.trim().contains("fail")) {
                                Toast.makeText(getContext(), "Sorry, sale not done, please try again!", Toast.LENGTH_SHORT).show();
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
                            List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject;
                            double total = 0;
                            for (int i = 0; i < dataList.size(); i++) {

                                jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("id", dataList.get(i).getProductId());
                                    jsonObject.put("compId", Users.getCompanyId());
                                    jsonObject.put("custId", CustomerIDForInvoice.getCustomerID());
                                    int qty = dataList.get(i).getProductQty();
                                    double price = dataList.get(i).getProductPrice();
                                    double subtotal = qty * price;
                                    jsonObject.put("qty", qty);
                                    jsonObject.put("price", price);
                                    jsonObject.put("subtotal", subtotal);
                                    //                        total
                                    total = total + subtotal;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(jsonObject);
                            }

                            Map<String, String> map = new HashMap<>();
                            map.put("params", jsonArray.toString());
                            map.put("transCode", transCode);
                            map.put("recievable", String.valueOf(finalRvable));
                            map.put("recieved", String.valueOf(finalRvd));
                            map.put("payType", String.valueOf(paymentValue));
                            return map;
                        }
                    };
                    Volley.newRequestQueue(getContext()).add(saleRequest);
                    editRecieved.getText().clear();
                } else {
                    editRecieved.setError("Sorry, recieved amount can only be between 0 and " + recievable);
                }
            }
        }
    }

    //    To refresh the fragment
    private void onRefresh() {
        InventoryAdapter.qty = 1;
        android.support.v4.app.FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        InvoiceFragment myfragment = new InvoiceFragment();  //your fragment

        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.frg_invoice, myfragment);
        fragmentTransaction.commit();


    }

//    Print invoice which is here the listview
    private void onPrint() {
        PrintManager printManager = (PrintManager) getContext().getSystemService(getContext().PRINT_SERVICE);
        WebView webView = new WebView(getContext());
        PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter();
        assert printManager != null;
        printManager.print(String.valueOf(R.id.list_invoice_content), adapter, null);
    }

//    To check if products are added in cart/invoice
    private void onCheckCart() {
        List<Product> products = InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
        if (products.size() > 0) {
            btnCancel.setEnabled(true);
            btnCancel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btnCancel.setEnabled(false);
            btnCancel.setBackgroundColor(getResources().getColor(R.color.disable_color));
        }
    }
}
