package com.xamuor.cashco.cashco;

import android.annotation.SuppressLint;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    private TextView txtTotal, txtRecieved, txtRecievable, txtCash, txtMaster, txtDebit, txtReportLabel;
    private ImageView imgPrintReport;
    private double totalSales;
    public static String REPORT_LABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
//        Initiate widgets
        txtTotal = findViewById(R.id.txt_total_sales_value);
        txtRecieved = findViewById(R.id.txt_recieved_value);
        txtRecievable = findViewById(R.id.txt_recievable_value);
        imgPrintReport = findViewById(R.id.img_print_report);
        txtCash = findViewById(R.id.txt_cash_value);
        txtMaster = findViewById(R.id.txt_master_value);
        txtDebit = findViewById(R.id.txt_debit_value);
        txtReportLabel = findViewById(R.id.txt_report_lbl);
//        By default shows today's report
        onRecieveReport("today", 0, "TODAY'S SALES");
//        Call to print report

            imgPrintReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPrint();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_today:
                REPORT_LABLE = "TODAY'S SALES";
                onRecieveReport("today", 0, REPORT_LABLE);
                break;
            case R.id.menu_yesterday:
                REPORT_LABLE = "YESTERDAY'S SALES";
                onRecieveReport("yesterday", 1, REPORT_LABLE);
                break;
            case R.id.menu_last7d:
                REPORT_LABLE = "LAST 7 DAYS' SALES";
                onRecieveReport("lastSevenDay", 2, REPORT_LABLE);

                break;
            case R.id.menu_30d:
                REPORT_LABLE = "LAST 30 DAYS' SALES";
                onRecieveReport("lastThirtyDay", 3, REPORT_LABLE);
                break;
            case R.id.menu_this_week:
                REPORT_LABLE = "THIS WEEK'S SALES";
                onRecieveReport("thisWeek", 4, REPORT_LABLE);
                break;
            case R.id.menu_last_week:
                REPORT_LABLE = "LAST WEEK'S SALES";
                onRecieveReport("lastWeek", 5, REPORT_LABLE);
                break;
            case R.id.menu_this_month:
                REPORT_LABLE = "THIS MONTH'S SALES";
                onRecieveReport("thisMonth", 6, REPORT_LABLE);
                break;
            case R.id.menu_last_month:
                REPORT_LABLE = "LAST MONTH'S SALES";
                onRecieveReport("lastMonth", 7, REPORT_LABLE);
                break;
            case R.id.menu_this_year:
                REPORT_LABLE = "THIS YEAR'S SALES";
                onRecieveReport("thisYear", 8, REPORT_LABLE);
                break;
            case R.id.menu_last_year:
                REPORT_LABLE = "LAST YEAR'S SALES";
                onRecieveReport("lastYear", 9, REPORT_LABLE);
                break;
            case R.id.menu_all_time:
                REPORT_LABLE = "ALL TIME'S SALES";
                onRecieveReport("allTime", 10, REPORT_LABLE);

        }
        return super.onOptionsItemSelected(item);
    }

//    Fetch data from server based on time
    private void onRecieveReport(final String url, final int value, String label) {
        final String currency = getResources().getString(R.string.txt_usd_currency);
//        Set a label at the top of report area
        txtReportLabel.setText(label);
        StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl(url), new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    double total = jsonObject.getDouble("total");
                    double cash = jsonObject.getDouble("cash");
                    double master = jsonObject.getDouble("master");
                    double debit = jsonObject.getDouble("debit");
                    double recieved = jsonObject.getDouble("recieved");
                    double recievable = jsonObject.getDouble("recievable");
                    if (total <= 0.0) {
                        imgPrintReport.setEnabled(false);
                        imgPrintReport.setColorFilter(R.color.disable_color);
                    } else {
                        imgPrintReport.setEnabled(true);
                        imgPrintReport.setColorFilter(R.color.colorPrimary);
                    }
                    txtTotal.setText(currency + "" + total);
                    txtCash.setText(currency + "" + cash);
                    txtMaster.setText(currency + "" + master);
                    txtDebit.setText(currency + "" + debit);
                    txtRecieved.setText(currency + "" + recieved);
                    txtRecievable.setText(currency + "" + recievable);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder d = new AlertDialog.Builder(getBaseContext());
                d.setMessage(error.toString());
                d.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("compId", String.valueOf(Users.getCompanyId()));
                map.put("url", url);
                map.put("timeValue", String.valueOf(value));
                return map;
            }
        };
        Volley.newRequestQueue(getBaseContext()).add(request);
    }

//    Print report
private void onPrint() {
    PrintManager printManager = (PrintManager) getBaseContext().getSystemService(getBaseContext().PRINT_SERVICE);
    WebView webView = new WebView(getBaseContext());
    PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter();
    assert printManager != null;
    printManager.print(String.valueOf(R.id.report_inner_layout), adapter, null);
}
}
