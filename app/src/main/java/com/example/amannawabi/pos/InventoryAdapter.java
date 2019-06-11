package com.example.amannawabi.pos;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<InventoryDataModal> productList;
    private Context context;

    public InventoryAdapter(List<InventoryDataModal> dataModals, Context context) {
        this.productList = dataModals;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InventoryAdapter.ViewHolder holder, final int position) {
        final InventoryDataModal modal = productList.get(position);
        holder.txtProduct.setText(modal.getProductName());
        holder.txtPrice.setText("$" + modal.getProductPrice());
        Glide.with(context).load(MyUrl.onLoadImage("product_images/".concat(modal.getProductImage()))).into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private int qty = 1;

            @Override
            public void onClick(View view) {

                Product product = new Product();
                TextView txtProduct = (TextView) view.findViewById(R.id.txt_product);
                String myProduct = txtProduct.getText().toString();
                InventoryDataModal productList = new InventoryDataModal(modal.getProductId(), modal.getProductImage(), modal.getProductName(), modal.getProductPrice());
                InvoiceFragment.posDatabase.myDao().getProducts(MyCompany.getCompanyId());

//               Initiate Product entity
                List<Product> existingList = InvoiceFragment.posDatabase.myDao().getItem(myProduct);
                if (existingList.size() > 0) {
                    InvoiceFragment.posDatabase.myDao().updateItem(++qty, MyCompany.getCompanyId(), myProduct);

                    onRefresh();
                    /*InvoiceFragment myfragment = new InvoiceFragment();  //your fragment
                    android.support.v4.app.FragmentManager fragmentManager = ((InventoryActivity) context).getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.frg_invoice, myfragment).commit();*/

                } else {
                    product.setProductId(productList.getProductId());
                    product.setCompanyId(MyCompany.getCompanyId());
                    product.setProductQty(qty);
                    product.setProductName(productList.getProductName());
                    product.setProductPrice(productList.getProductPrice());
                    ProductFragment.posDatabase.myDao().insert(product);
                    Toast.makeText(context, "Item added.", Toast.LENGTH_SHORT).show();

                    onRefresh();

                    /*android.support.v4.app.FragmentManager fragmentManager = ((InventoryActivity) context).getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    InvoiceFragment myfragment = new InvoiceFragment();  //your fragment
                    // work here to add, remove, etc
                    fragmentTransaction.replace(R.id.frg_invoice, myfragment).commit();*/
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView txtProduct, txtPrice;


        public ViewHolder(View itemView) {
            super(itemView);
//                by default is hidden, when an item clicked it gets visible
            InvoiceFragment invoiceFragment = new InvoiceFragment();
            productImage = itemView.findViewById(R.id.img_product);
            txtProduct = itemView.findViewById(R.id.txt_product);
            txtPrice = itemView.findViewById(R.id.txt_price);


        }
    }

    //    To change array-list based on query input
    public void onUpdateList(List<InventoryDataModal> newList) {
        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    //    To refresh the fragment
    private void onRefresh() {
        android.support.v4.app.FragmentManager fragmentManager = ((InventoryActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        InvoiceFragment myfragment = new InvoiceFragment();  //your fragment

        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.frg_invoice, myfragment);
        fragmentTransaction.commit();
    }
}
