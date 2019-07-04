package com.xamuor.cashco.cashco;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.ViewHoler> {
    private List<BalanceDataModal> balanceList;
    private Context context;

    public BalanceAdapter(List<BalanceDataModal> balanceList, Context context) {
        this.balanceList = balanceList;
        this.context = context;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_data_modal, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        BalanceDataModal modal = balanceList.get(position);
        holder.txtInvoice.setText(String.valueOf(modal.getInvoiceNO()));
        holder.txtPayment.setText(String.valueOf(modal.getPaymentType()));
        holder.txtCustomer.setText(String.valueOf(modal.getCustomer()));
        holder.txtRecieved.setText(String.valueOf(modal.getRecieved()));
        holder.txtRecievable.setText(String.valueOf(modal.getRecievable()).replace("null", "0"));
        holder.txtBalanceDate.setText(String.valueOf(modal.getPurDate()));
    }

    @Override
    public int getItemCount() {
        return balanceList.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        private TextView txtInvoice, txtPayment, txtCustomer, txtRecieved, txtRecievable, txtBalanceDate;
        public ViewHoler(View itemView) {
            super(itemView);
//            Initiating widgets
            txtInvoice = itemView.findViewById(R.id.txt_invoice_num);
            txtPayment = itemView.findViewById(R.id.txt_payment);
            txtCustomer = itemView.findViewById(R.id.txt_customer);
            txtRecieved = itemView.findViewById(R.id.txt_recieved);
            txtRecievable = itemView.findViewById(R.id.txt_recievable);
            txtBalanceDate = itemView.findViewById(R.id.txt_balance_date);
        }
    }

//    Remove balance
    public void removeBalance(int position) {
         balanceList.get(position);
        balanceList.remove(position);
        notifyItemRemoved(position);
    }
}
