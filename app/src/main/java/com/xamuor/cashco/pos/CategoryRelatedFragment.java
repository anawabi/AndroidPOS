package com.xamuor.cashco.pos;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryRelatedFragment extends Fragment {
    private ImageView imgNewCategory, imgNewProduct, imgEditCategory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_related, container, false);
//       Initiate widgets
        imgNewCategory = view.findViewById(R.id.img_add_category);
        imgNewProduct = view.findViewById(R.id.img_add_product);
        imgEditCategory = view.findViewById(R.id.img_edit_category);
        imgNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewCategory();
            }
        });
        return view;
    }
    public static void onRefreshCategoryRelated(@Nullable CategoryDataModal modal, @Nullable Context context) {

//                CustomerDetailFragment to show more detail for any customer
        FragmentManager fragmentManager =  ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryRelatedFragment myfragment = new CategoryRelatedFragment();  //your fragment
        fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
        fragmentTransaction.commit();
    }
//    Add new Category
    private void onNewCategory() {
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewCategoryFragment myfragment = new NewCategoryFragment();  //your fragment
        fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
        fragmentTransaction.commit();
    }
}
