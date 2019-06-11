package com.example.amannawabi.pos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Inventory2Adapter extends RecyclerView.Adapter<Inventory2Adapter.ViewHolder> {
    private Context context;
    private List<Inventory2DataModal> pList;

    public Inventory2Adapter(Context context, List<Inventory2DataModal> pList) {
        this.context = context;
        this.pList = pList;
    }

    @Override
    public Inventory2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_data_modal, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Inventory2Adapter.ViewHolder holder, int position) {
       final Inventory2DataModal modal = pList.get(position);
        Glide.with(context).load(MyUrl.onLoadImage("product_images/".concat(modal.getpImage()))).into(holder.pImage);
        holder.pName.setText(modal.getpName());
        holder.pQty.setText(modal.getpQty()+"");
        holder.pPurchasePrice.setText(modal.getpPurchasePrice()+"");
        holder.pSellPrice.setText(modal.getpSellPrice()+"");
        holder.pRegDate.setText(modal.getpRegDate());


    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pImage;
        private TextView pName, pQty, pPurchasePrice, pSellPrice, pRegDate;

        public ViewHolder(View itemView) {
            super(itemView);

//            Initiating widgets
            pImage = itemView.findViewById(R.id.img_item_inventory);
            pName = itemView.findViewById(R.id.txt_product2);
            pQty = itemView.findViewById(R.id.txt_in_stock);
            pPurchasePrice = itemView.findViewById(R.id.txt_purchase_price);
            pSellPrice = itemView.findViewById(R.id.txt_sell_price);
            pRegDate = itemView.findViewById(R.id.txt_reg_date);
        }
    }
}
