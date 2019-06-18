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

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {
    private Context context;
    private List<ListProductDataModal> productList;

    public ListProductAdapter(Context context, List<ListProductDataModal> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_data_modal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListProductDataModal modal = productList.get(position);
        Glide.with(context).load(MyUrl.onLoadImage("product_images/").concat(modal.getProductImage())).into(holder.pImage);
        holder.txtPname.setText(modal.getProductName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pImage;
        private TextView txtPname;
        public ViewHolder(View itemView) {
            super(itemView);
            pImage = itemView.findViewById(R.id.img_product_list_image);
            txtPname = itemView.findViewById(R.id.txt_product_list_name);
        }
    }
}
