package com.xamuor.cashco.pos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter <ReportAdapter.ViewHolder> {
    private List<ReportDataModal> reportList;
    private Context context;

    public ReportAdapter(List<ReportDataModal> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_data_modal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReportDataModal repordModal = reportList.get(position);
        Glide.with(context).load(MyUrl.onLoadImage("product_images/").concat(repordModal.getpImage())).into(holder.imgProduct);
        holder.txtProductName.setText(repordModal.getpName());
        holder.txtCustomer.setText(repordModal.getCustomer());
        holder.txtInvoice.setText(repordModal.getInvoice()+"");
        holder.txtQty.setText(repordModal.getQtySold()+"");
        holder.txtPayment.setText(repordModal.getPayment());
        holder.txtDate.setText(repordModal.getDate());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName, txtCustomer, txtInvoice, txtQty, txtPayment, txtDate;
        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_report);
            txtProductName = itemView.findViewById(R.id.txt_report_product);
            txtCustomer = itemView.findViewById(R.id.txt_report_customer);
            txtInvoice = itemView.findViewById(R.id.txt_report_invoice);
            txtQty = itemView.findViewById(R.id.txt_report_qty);
            txtPayment = itemView.findViewById(R.id.txt_report_payment);
            txtDate = itemView.findViewById(R.id.txt_report_date);
        }
    }
}
