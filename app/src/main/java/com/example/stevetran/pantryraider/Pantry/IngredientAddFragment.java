package com.example.stevetran.pantryraider.Pantry;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.SharedPreferences;
import android.content.Context;

import com.example.stevetran.pantryraider.R;

import java.util.ArrayList;

/**
 * Created by William Ma on 11/30/2017.
 */

public class IngredientAddFragment extends Fragment implements View.OnClickListener {

    View view;
    Button addToMyIngredients;
    ArrayList<String> myCart;
    ArrayAdapter<String> adapter;
    static Context mContext;
/*
    public static IngredientAddFragment newInstance(Context con) {
        IngredientAddFragment fragment = new IngredientAddFragment();

        mContext = con;
    }
*/
    public IngredientAddFragment() {}

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_ingredient, container, false);


        return view;
    }

    @Override
    public void onClick(View v) {
        //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        switch (v.getId()) {
            case R.id.add_ing_button:

                break;
            case R.id.ing_search_res:

        }

    }
}
