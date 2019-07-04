package com.xamuor.cashco.cashco;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InvoiceAdapter extends ArrayAdapter<InvoiceDataModal> {
    ArrayList<InvoiceDataModal> products;
    Context context;
    int myResource;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<InvoiceDataModal> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.invoice_datamodal_layout, null, true);
        }

        InvoiceDataModal product = getItem(position);
//        Initiate widgets of invoice-data-modal
        TextView txtItemName = convertView.findViewById(R.id.txt_item_name);
        TextView txtItemQty = convertView.findViewById(R.id.txt_item_qty);
        TextView txtItemPrice = convertView.findViewById(R.id.txt_item_price);
        TextView txtItemSubtotal = convertView.findViewById(R.id.txt_item_subtotal);
//        define values inside widgets from invoiceDataModal
        if (product != null) {
            txtItemQty.setText(product.getProductQty()+"");
        }
        if (product != null) {
            txtItemName.setText(product.getProductName());
        }
        if (product != null) {
            txtItemPrice.setText(product.getProductPrice() + "");
        }
        if (product != null) {
            txtItemSubtotal.setText(product.getProductSubtotal() + "");
        }
        return convertView;
    }
}
