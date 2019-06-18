package com.xamuor.cashco.pos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductEditFragment extends Fragment {
    private EditText editPname, editPdesc, editPpurchase, editPsell, editPqty, editPbarcode;
    private RadioGroup rdbChangeTax;
    private RadioButton rdbTaxable, rdbNotTaxable;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_product_edit, container, false);
    return view;
    }

}
