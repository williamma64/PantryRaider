package com.example.stevetran.pantryraider.Pantry.Ingredients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stevetran.pantryraider.R;

public class MyIngredientsFragment extends Fragment {
    View view;
    private ListView savedRecipeList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_recipe, container, false);

        //overwrites default backbutton
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                    viewPager.setCurrentItem(0);
                    //set up a new title
                    TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
                    mTitle.setText("Pantry");
                    return true;
                }
                return false;
            }
        });

        return view;
    }
}