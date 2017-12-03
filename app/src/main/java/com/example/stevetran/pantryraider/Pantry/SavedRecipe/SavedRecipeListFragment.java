package com.example.stevetran.pantryraider.Pantry.SavedRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Search.Recipes.RecipeActivity;

import java.util.ArrayList;

/**
 * Created by William Ma on 11/17/2017.
 */

public class SavedRecipeListFragment extends Fragment {
    View view;
    private ListView SavedRecipeList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_recipe, container, false);
        inflateListView();

        //overwrites default backbutton
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK) {
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

    private void inflateListView(){
        SavedRecipeList = (ListView) view.findViewById(R.id.SavedRecipeList);
        final ArrayList<SavedRecipe> recipeList = SavedRecipe.getRecipesFromFile("recipes.json", getActivity());

        SavedRecipeAdapter adapter = new SavedRecipeAdapter(getActivity(), recipeList);
        SavedRecipeList.setAdapter(adapter);
        SavedRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavedRecipe recipe = (SavedRecipe)SavedRecipeList.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("rid", "30000");
                startActivity(intent);
            }
        });
    }
}