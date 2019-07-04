package com.xamuor.cashco.cashco;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductFragment extends Fragment {
    private Bitmap bitmap;
    private EditText editProductName, editProductDesc, editProductPurchase, editProductSell, editProductQty, editProductBarcode;
    private RadioGroup rdgTax;
    private RadioButton rdbTaxable, rdbNotTaxable;
    private ImageView imgProductImage;
    private Button btnAddProduct, btnCancelProduct;
    private TextView txtCategoryName;
    private String pName, pDesc, pPurchase, pSell, pQty, pBarcode, isTaxable;

 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view = inflater.inflate(R.layout.fragment_new_product, container, false);
//     Initiate widgets
     editProductName = view.findViewById(R.id.edit_product_name);
     editProductDesc = view.findViewById(R.id.edit_product_desc);
     editProductPurchase = view.findViewById(R.id.edit_product_purchase);
     editProductSell = view.findViewById(R.id.edit_product_sell);
     editProductQty = view.findViewById(R.id.edit_product_qty);
     editProductBarcode = view.findViewById(R.id.edit_product_barcode);
     txtCategoryName = view.findViewById(R.id.txt_ctg_name);
     rdgTax = view.findViewById(R.id.rdg_for_tax);
     rdbTaxable = view.findViewById(R.id.rdb_taxable);
     rdbNotTaxable = view.findViewById(R.id.rdb_not_taxable);
     imgProductImage = view.findViewById(R.id.img_product_image);
     btnAddProduct = view.findViewById(R.id.btn_add_product);
     btnCancelProduct = view.findViewById(R.id.btn_cancel_product);

//     TextView on layout of new-product-fragment to help what product for category inserted
     txtCategoryName.setText(CategoryRelatedFragment.ctgName);
     txtCategoryName.setBackgroundColor(Color.LTGRAY);
     int padding = getResources().getDimensionPixelOffset(R.dimen.padding);
     txtCategoryName.setPadding(padding, padding, padding, padding);

//     listener to add new product
     btnAddProduct.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View view) {
//             Get text or values
             pName = editProductName.getText().toString();
             pDesc = editProductDesc.getText().toString();
             pPurchase = editProductPurchase.getText().toString();
             pSell = editProductSell.getText().toString();
             pQty = editProductQty.getText().toString();
             pBarcode = editProductBarcode.getText().toString();
             if (rdbTaxable.isChecked()) {
                 isTaxable = rdbTaxable.getText().toString();
             } else {
                 isTaxable = rdbNotTaxable.getText().toString();
             }

             if (pName.isEmpty()) {
                editProductName.setError("Product name required!");
            }
            if (pPurchase.isEmpty()) {
                editProductPurchase.setError("Purchase price required!");
            }
            if (pSell.isEmpty()) {
                editProductSell.setError("Sell price required!");
            }
            if (pQty.isEmpty()) {
                editProductQty.setError("Product quantity required!");
            }
            if (pBarcode.isEmpty()) {
                editProductBarcode.setError("Barcode required!");
            }
            if (!pName.isEmpty() && !pPurchase.isEmpty() && !pSell.isEmpty() && !pQty.isEmpty() && !pBarcode.isEmpty()) {
//                 Cast to Double or Int
                 double puchase = Double.parseDouble(pPurchase);
                 double sell = Double.parseDouble(pSell);
                 int qty = Integer.parseInt(pQty);
                 int barcode = Integer.parseInt(pBarcode);
                sendProduct(pName, pDesc, puchase, sell, qty, barcode, isTaxable);
            }
         }
     });

     imgProductImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent photoIntent = new Intent();
             photoIntent.setType("image/*");
             photoIntent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(photoIntent, "Choose a product image ..."), 0);

         }
     });

//     Cancel Adding products
     btnCancelProduct.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             pName = editProductName.getText().toString();
             pDesc = editProductDesc.getText().toString();
             //                data-modal is required to refresh CategoryRelatedFragment
             CategoryDataModal modal = new CategoryDataModal(CategoryRelatedFragment.ctgId, CategoryRelatedFragment.ctgName, pDesc);
             CategoryRelatedFragment.onRefreshCategoryRelated(modal, getContext());
         }
     });

     return view;
    }
//    send product to server
 private void sendProduct(final String pName, @Nullable final String pDesc, final double puchase, final double sell, final int qty, final int barcode, final String taxable) {
     StringRequest request = new StringRequest(Request.Method.POST, Routes.setUrl("newProduct"), new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
            if (response.trim().equals("success")) {
                Toast.makeText(getActivity(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                editProductName.getText().clear();
                editProductDesc.getText().clear();
                editProductPurchase.getText().clear();
                editProductSell.getText().clear();
                editProductQty.getText().clear();
                editProductBarcode.getText().clear();

            } else if (response.trim().equals("fail")) {
                Toast.makeText(getActivity(), "Sorry, product not added, try again", Toast.LENGTH_SHORT).show();
            }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             AlertDialog.Builder d = new AlertDialog.Builder(getContext());
             d.setMessage(error.toString());
             d.show();
         }
     }) {
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
//             String pImage = getProductImage(bitmap);
             Map<String, String> map = new HashMap<>();
             map.put("compId", String.valueOf(Users.getCompanyId()));
             map.put("ctgId", String.valueOf(CategoryRelatedFragment.ctgId));
             map.put("pName", pName);
             map.put("pDesc", pDesc);
//             map.put("pImage", pImage);
             map.put("pPurchase", String.valueOf(puchase));
             map.put("pSell", String.valueOf(sell));
             map.put("pQty", String.valueOf(qty));
             map.put("pBarcode", String.valueOf(barcode));
             map.put("pTaxable", String.valueOf(taxable));
             return map;
         }
     };
     Volley.newRequestQueue(getActivity()).add(request);
 }

    // Method for getting image from storage;
    private String getProductImage(Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        byte[] imageBytes = b.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgProductImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    Send product Image to server
    private void onSendProductImage() {
     StringRequest request = new StringRequest(Request.Method.POST, Routes.onLoadImage("product_images"), new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {

         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

         }
     }) {
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> map = new HashMap<>();
//             map.put("")
             return map;
         }
     };
     Volley.newRequestQueue(getActivity()).add(request);
    }
}
