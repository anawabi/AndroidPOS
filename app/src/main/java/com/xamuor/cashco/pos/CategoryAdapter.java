package com.xamuor.cashco.pos;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryDataModal> ctgList;

    public CategoryAdapter(Context context, List<CategoryDataModal> ctgList) {
        this.context = context;
        this.ctgList = ctgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_data_modal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategoryDataModal modal = ctgList.get(position);
        holder.txtCtgName.setText(modal.getCtgName().toString());
        holder.txtCtgDesc.setText(modal.getCtgDesc().toString());
//        card is clickable
        holder.cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryRelatedFragment.onRefreshCategoryRelated(modal, context);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ctgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCtgName;
        private TextView txtCtgDesc;
        private CardView cardCategory;
        public ViewHolder(View itemView) {
            super(itemView);
            txtCtgName = itemView.findViewById(R.id.txt_ctg_name);
            txtCtgDesc = itemView.findViewById(R.id.txt_ctg_desc);
            cardCategory = itemView.findViewById(R.id.card_category);
        }
    }
}
