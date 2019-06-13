package com.xamuor.cashco.pos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    Intent loginIntent;
    EditText editUserName, editPassword;
    Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editUserName = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });
    }
// To let System-admin log into the system
    private void onLogin() {
        final String username = editUserName.getText().toString();
        final String password = editPassword.getText().toString();
       final StringRequest loginRequest = new StringRequest(Request.Method.POST, MyUrl.setUrl("login"), new Listener<String>() {
           @Override
           public void onResponse(String response) {
               if (response.trim().contains("fail")) {
                   if (username.isEmpty()) {
                       editUserName.setError("User Name required!");
                   }
                   if (password.isEmpty()) {
                       editPassword.setError("Password required!");
                   } else  {
                       editUserName.getText().clear();
                       editPassword.getText().clear();
                       editUserName.setError("Username or password wrong!");
                   }
               }
               else {
                   try {
                       JSONObject jsonObject = new JSONObject(response);
                       MyCompany.setCompanyId(jsonObject.getInt("compId"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   loginIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                   startActivity(loginIntent);
                   finish();
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
               Map<String, String> map = new HashMap<String, String>();
               map.put("username", username);
               map.put("password", password);
               return map;

           }

       };
loginRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       Volley.newRequestQueue(getApplicationContext()).add(loginRequest);
    }
}
