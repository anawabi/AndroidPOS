package com.xamuor.cashco.Views;


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

import com.xamuor.cashco.Model.CategoryDataModal;
import com.xamuor.cashco.cashco.R;
import com.xamuor.cashco.Users;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryRelatedFragment extends Fragment {
    private ImageView imgNewCategory, imgNewProduct, imgEditCategory;
//    category-id to be inserted into table items into SERVER
    public static int ctgId = 0;
    public static String ctgName;
    public static String ctgDesc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_related, container, false);
//       Initiate widgets
        imgNewCategory = view.findViewById(R.id.img_add_category);
        imgNewProduct = view.findViewById(R.id.img_add_product);
        imgEditCategory = view.findViewById(R.id.img_edit_category);
// Set permissions
        if (!Users.getRole().equalsIgnoreCase("system admin")) {
            imgEditCategory.setVisibility(View.GONE);
            imgNewCategory.setVisibility(View.GONE);
            imgNewProduct.setVisibility(View.GONE);
        }

//        Add new category
        imgNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewCategory();
            }
        });

//        Add new product
        imgNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewProduct();
            }
        });
//      When edit-icon touched
        imgEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeCategory();
            }
        });


//        Loading fragment ListProductFragment
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListProductFragment myfragment = new ListProductFragment();  //your fragment
        fragmentTransaction.replace(R.id.frg_product_list, myfragment);
        fragmentTransaction.commit();

        return view;
    }
    public static void onRefreshCategoryRelated(@Nullable CategoryDataModal modal, @Nullable Context context) {
        ctgId = modal.getCtgId();
        ctgName = modal.getCtgName();
        ctgDesc = modal.getCtgDesc();
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
//    Add new product
    private void onNewProduct() {
        FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewProductFragment myfragment = new NewProductFragment();  //your fragment
        fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
        fragmentTransaction.commit();
    }

// Change product
private void onChangeCategory() {
    FragmentManager fragmentManager =  (getActivity()).getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    CategoryEditFragment myfragment = new CategoryEditFragment();  //your fragment
    fragmentTransaction.replace(R.id.menu_item_frg_cust_detail, myfragment);
    fragmentTransaction.commit();
}
}
