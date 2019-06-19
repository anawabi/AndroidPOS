package com.xamuor.cashco.pos;


import android.app.AlertDialog;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
public class AnnuallyReportFragment extends Fragment {
    private ImageView imgPrintReport;
    private RelativeLayout reportLayout;
    private TextView txtTotal;
    private RecyclerView reportRV;
    private List<ReportDataModal> reportList;
    private ReportAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.my_fragment_report, container, false);
        //        Initiate widgets
        txtTotal = view.findViewById(R.id.txt_report_total);
        imgPrintReport = view.findViewById(R.id.img_print_report);
        reportLayout = view.findViewById(R.id.report_layout);
        imgPrintReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrint();
            }
        });
        reportRV = view.findViewById(R.id.report_rv);
        reportList = new ArrayList<>();
        reportRV.setHasFixedSize(true);
        reportRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        Call to load annual detailed report
        onLoadAnnualDetailedReport("annual");
//        Call to load annual total report
        onLoadAnnualTotal("annualTotal");
    return view;
    }
    // Load monthly-report data
    private void onLoadAnnualDetailedReport(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, MyUrl.setUrl(url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int r = 0; r < jsonArray.length(); r++) {
                        JSONObject jo = jsonArray.getJSONObject(r);
                        String pImage = jo.getString("item_image");
                        String pName = jo.getString("item_name");
                        String customer = jo.getString("cust_name");
                        String payment = jo.getString("payment_type");
                        int invoice = jo.getInt("inv_id");
                        int qty = jo.getInt("qty_sold");
                        String date = jo.getString("DATE(sales.created_at)");
                        ReportDataModal repordModal = new ReportDataModal(pImage, pName, customer, payment, date, invoice, qty);
                        reportList.add(repordModal);
                    }
                    adapter = new ReportAdapter(reportList, getContext());
                    reportRV.setAdapter(adapter);
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
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    //    load monthly-total-sales
    private void onLoadAnnualTotal(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, MyUrl.setUrl(url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String defualtValue = getResources().getString(R.string.txt_report_annual_total);
                txtTotal.setText(defualtValue.concat(" $" + response));
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
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }
    //    Print report which is the parent layout
    private void onPrint() {
        PrintManager printManager = (PrintManager) getContext().getSystemService(getContext().PRINT_SERVICE);
        WebView webView = new WebView(getContext());
        PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter();
        assert printManager != null;
        printManager.print(String.valueOf(R.id.report_layout), adapter, null);
    }
}
