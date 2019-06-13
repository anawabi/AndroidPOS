package com.xamuor.cashco.pos;


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
        ImageButton btnNewCustomer = view.findViewById(R.id.btn_new_customer);
        posDatabase = Room.databaseBuilder(getContext(), PosDatabase.class, "newpos_db").allowMainThreadQueries().build();
        btnNewCustomer.setOnClickListener(new View.OnClickListener() {
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
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateSale();
               /* senCompanyId();
                onInvoiceGenerated();*/

               /* android.support.v4.app.FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                InvoiceFragment myfragment = new InvoiceFragment();  //your fragment

                // work here to add, remove, etc
                fragmentTransaction.replace(R.id.frg_invoice, myfragment);
                fragmentTransaction.commit();*/
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
                    btnCancel.setEnabled(false);
                } else {
                    CustomerIDForInvoice.setCustomerID(adapterView.getItemAtPosition(i));
                    chkAllAmount.setEnabled(true);
                    spnPaymentType.setEnabled(true);
                    layoutForPayment.setVisibility(View.VISIBLE);
                    btnPrint.setEnabled(true);
                    btnCancel.setEnabled(true);
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
        return view;
    }

    // Populate spinner with customers
    private void listCustomers() {
//     InvoiceFragment.posDatabase.myDao().deleteCustomer();
        StringRequest selectCustomerRequest = new StringRequest(Request.Method.POST, MyUrl.setUrl("listCustomer"), new Response.Listener<String>() {
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
//                     Customer customer = new Customer();
//                     To populate customers into it for spinner
                        List<String> customerList = new ArrayList<String>();
//                     Existing customers into ROOM database
//                     List<Customer> existingCustomer = InvoiceFragment.posDatabase.myDao().getCustomers(MyCompany.getCompanyId());
//                      First Delete previous customers
                        customerList.add("Choose / Add a customer");
                        for (int c = 0; c < customers.length(); c++) {
                            JSONObject custObject = customers.getJSONObject(c);
                            int custID = custObject.getInt("cust_id");
                            String custName = custObject.getString("cust_name");

                            customerList.add(String.valueOf(custID));
//                         set json-values into ROOM
                      /*   customer.setCompanyId(MyCompany.getCompanyId());
                         customer.setCustomerId(custID);
                         customer.setCustomerName(custName);
                         InvoiceFragment.posDatabase.myDao().insertCustomer(customer);*/
                        }
                     /*for (Customer cust : existingCustomer) {
                         int rCustId = cust.getCustomerId();
                         String rCustName = cust.getCustomerName();
                         customerList.add(rCustId + " " + rCustName);
                     }*/
                        ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, customerList);
                        spnCustomers.setAdapter(customerAdapter);
                        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                map.put("compId", MyCompany.getCompanyId() + "");
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
        List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(MyCompany.getCompanyId());
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
                    if (paymentValue.equals("Cash")) {
                        layoutForPayment.setVisibility(View.GONE);
                    } else {
                        layoutForPayment.setVisibility(View.VISIBLE);
                        editRecievable.setVisibility(View.INVISIBLE);
                        editRecieved.setVisibility(View.INVISIBLE);
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
            StringRequest saleRequest = new StringRequest(Request.Method.POST, MyUrl.setUrl("createSale"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.trim().contains("success!")) {
                        Toast.makeText(getContext(), "Sale was successful!", Toast.LENGTH_LONG).show();
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
                    List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(MyCompany.getCompanyId());
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject;
                    double total = 0;
                    for (int i = 0; i < dataList.size(); i++) {

                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", dataList.get(i).getProductId());
                            jsonObject.put("compId", MyCompany.getCompanyId());
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
                    StringRequest saleRequest = new StringRequest(Request.Method.POST, MyUrl.setUrl("createSale"), new Response.Listener<String>() {
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
                            List<Product> dataList = InvoiceFragment.posDatabase.myDao().getProducts(MyCompany.getCompanyId());
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject;
                            double total = 0;
                            for (int i = 0; i < dataList.size(); i++) {

                                jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("id", dataList.get(i).getProductId());
                                    jsonObject.put("compId", MyCompany.getCompanyId());
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
}
