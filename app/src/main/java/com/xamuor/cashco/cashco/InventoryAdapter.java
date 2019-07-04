package com.xamuor.cashco.cashco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    public static int qty = 1;
    public int remainingQty = 0;
    private int initialQty = 0;
    private int q;
    private List<InventoryDataModal> productList;
    private Context context;
    private List<Inventories> inventoriesList;

    public InventoryAdapter(List<InventoryDataModal> dataModals, Context context) {
        this.productList = dataModals;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final InventoryAdapter.ViewHolder holder, final int position) {
        final InventoryDataModal modal = productList.get(position);
        holder.txtProduct.setText(modal.getProductName());
        holder.txtPrice.setText("$" + modal.getProductPrice());
        holder.txtQty.setText(String.valueOf(modal.getProductQty()));
        Glide.with(context).load(Routes.onLoadImage("product_images/".concat(modal.getProductImage()))).into(holder.productImage);

//        See if any product qty is zero of not existing anymore
        remainingQty = Integer.parseInt(holder.txtQty.getText().toString());
        if (remainingQty <= 0) {
            Toast.makeText(context, modal.getProductName() + " not existing anymore.", Toast.LENGTH_SHORT).show();
            holder.txtQty.setTextColor(Color.rgb(139, 0, 0));
        } else if (remainingQty <= 5) {
            holder.txtQty.setTextColor(Color.rgb(139, 0, 0));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//     Get the initial value of qty shown in the card
                initialQty = Integer.parseInt(holder.txtQty.getText().toString());
                Product product = new Product();
                TextView txtProduct = (TextView) view.findViewById(R.id.txt_product);
                String myProduct = txtProduct.getText().toString();
                InventoryDataModal productList = new InventoryDataModal(modal.getProductId(), modal.getProductImage(), modal.getProductName(), modal.getProductPrice(), modal.getProductQty());
                InvoiceFragment.posDatabase.myDao().getProducts(Users.getCompanyId());
//                remainingQty--;
//               Initiate Product entity
                List<Product> existingList = InvoiceFragment.posDatabase.myDao().getItem(myProduct);
                if (existingList.size() > 0) {
//                  decrease one unit the qty on any click on the same item
                    initialQty--;
//                    update qty in inventories table in ROOM db with decremented value
                    InvoiceFragment.posDatabase.myDao().updateInventory(initialQty, Users.getCompanyId(), modal.getProductId());
//                  Fetch back the update qty from inventories in ROOM db
                    remainingQty = InvoiceFragment.posDatabase.myDao().getQty(Users.getCompanyId(), modal.getProductId());
                    if (remainingQty == 5) {
                        holder.txtQty.setTextColor(Color.rgb(139, 0, 0));
                    } else if (remainingQty == 0) {
                        AlertDialog.Builder d = new AlertDialog.Builder(context);
                        d.setMessage("Sorry, " + modal.getProductName() + " has been finished!");
                        holder.itemView.setEnabled(false);
                    }
//                   Set the new qty in textview in the cards
                    holder.txtQty.setText(remainingQty+"");

                    q = ++qty;
                    InvoiceFragment.posDatabase.myDao().updateItem(q, Users.getCompanyId(), myProduct);
//                    holder.txtQty.setText(remainingQty+"");

                    onRefreshInvoiceFragment();
                } else {
                    //                  decrease one unit the qty on any click on the same item
                    initialQty--;
//                    update qty in inventories table in ROOM db with decremented value
                    InvoiceFragment.posDatabase.myDao().updateInventory(initialQty, Users.getCompanyId(), modal.getProductId());
//                  Fetch back the update qty from inventories in ROOM db
                    remainingQty = InvoiceFragment.posDatabase.myDao().getQty(Users.getCompanyId(), modal.getProductId());
//                   Set the new qty in textview in the cards
                    holder.txtQty.setText(remainingQty+"");

/* Print values in the invoice when an item is clicked */
                    qty = 1;
                    product.setProductId(productList.getProductId());
                    product.setCompanyId(Users.getCompanyId());
                    product.setProductQty(qty);
                    product.setProductName(productList.getProductName());
                    product.setProductPrice(productList.getProductPrice());
                    ProductFragment.posDatabase.myDao().insert(product);
                    onRefreshInvoiceFragment();
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
        TextView txtProduct, txtPrice, txtQty;


        public ViewHolder(View itemView) {
            super(itemView);
//                by default is hidden, when an item clicked it gets visible
            InvoiceFragment invoiceFragment = new InvoiceFragment();
            productImage = itemView.findViewById(R.id.img_product);
            txtProduct = itemView.findViewById(R.id.txt_product);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtQty = itemView.findViewById(R.id.txt_qty);
//            Set permission for cashier
            if (Users.getRole().equalsIgnoreCase("cashier")) {
                itemView.setEnabled(false);

            }

        }
    }

    //    To change array-list based on query input
    public void onUpdateList(List<InventoryDataModal> newList) {
        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    //    To refresh the invoiceFragment
    private void onRefreshInvoiceFragment() {
        android.support.v4.app.FragmentManager fragmentManager = ((InventoryActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InvoiceFragment myfragment = new InvoiceFragment();  //your fragment
        // work here to add, remove, etc
        fragmentTransaction.replace(R.id.frg_invoice, myfragment);
        fragmentTransaction.commit();
    }
}
