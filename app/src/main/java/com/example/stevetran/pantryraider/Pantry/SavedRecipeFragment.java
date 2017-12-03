package com.example.stevetran.pantryraider.Pantry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stevetran.pantryraider.R;

import java.util.ArrayList;

/**
 * Created by William Ma on 11/17/2017.
 */

public class SavedRecipeFragment extends Fragment {
    View view;
    private ListView SavedRecipeList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_recipe, container, false);
        inflateListView();
        return view;
    }

    private void inflateListView(){
        SavedRecipeList = (ListView) view.findViewById(R.id.SavedRecipeList);
        final ArrayList<SavedRecipe> recipeList = SavedRecipe.getRecipesFromFile("recipes.json", getActivity());

        SavedRecipeAdapter adapter = new SavedRecipeAdapter(getActivity(), recipeList);
        SavedRecipeList.setAdapter(adapter);
    }
}