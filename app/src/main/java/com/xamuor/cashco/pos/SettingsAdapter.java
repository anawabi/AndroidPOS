package com.xamuor.cashco.pos;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
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

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHoler> {
    private List<SettingsDataModal> userList;
    private Context context;

    public SettingsAdapter(List<SettingsDataModal> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_data_modal, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(final ViewHoler holder, int position) {
        final SettingsDataModal modal = userList.get(position);
        Glide.with(context).load(MyUrl.onLoadImage("user_photos/").concat(modal.getPhoto())).into(holder.userPhoto);
        holder.txtFname.setText(modal.getFname());
        holder.txtLname.setText(modal.getLname());
//        holder.txtPhone.setText(modal.getPhone());
        holder.txtRole.setText(modal.getRole());
//         Check if user-status is active
        int userStatus = modal.getStatus();
        if (userStatus == 1) {
            holder.switchStatus.setChecked(true);
        }
//        Change switch status
        holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.switchStatus.isChecked()) {
                    onChangeUserStatus(modal.getUserId(), 1, modal.getFname());
                } else {
                    onChangeUserStatus(modal.getUserId(), 0, modal.getFname());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        private ImageView userPhoto;
        private TextView txtFname, txtLname, txtPhone, txtRole;
        private SwitchCompat switchStatus;
        public ViewHoler(View itemView) {
            super(itemView);
//            Initiate above widgets
            userPhoto = itemView.findViewById(R.id.img_user);
            txtFname = itemView.findViewById(R.id.txt_user_fname);
            txtLname = itemView.findViewById(R.id.txt_user_lname);
//            txtPhone = itemView.findViewById(R.id.txt_user_phone);
            txtRole = itemView.findViewById(R.id.txt_user_role);
            switchStatus = itemView.findViewById(R.id.switch_user_status);
        }
    }

    //    Change user-status (Active or Inactive)
    private void onChangeUserStatus(final int userId, final int statusValue, final String user) {
        StringRequest statusRequest = new StringRequest(Request.Method.POST, MyUrl.setUrl("userStatus"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("active")) {
                    Toast.makeText(context,  user + " is active.", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equals("inactive")){
                    Toast.makeText(context, user + " is inactive.", Toast.LENGTH_SHORT).show();
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
                map.put("userId", String.valueOf(userId));
                map.put("statusValue", String.valueOf(statusValue));
                return map;
            }
        };
        Volley.newRequestQueue(context).add(statusRequest);
    }
}
