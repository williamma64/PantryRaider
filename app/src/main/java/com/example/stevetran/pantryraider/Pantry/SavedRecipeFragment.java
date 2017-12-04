package com.example.stevetran.pantryraider.Pantry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Search.SearchFragment;
import com.example.stevetran.pantryraider.Util.SharedConstants;

import java.util.ArrayList;

/**
 * Created by William Ma on 11/17/2017.
 */

public class SavedRecipeFragment extends Fragment {
    View view;
    private ListView savedRecipeList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_recipe, container, false);
        inflateListView();
        return view;
    }

    private void inflateListView(){
        savedRecipeList = (ListView) getActivity().findViewById(R.id.SavedRecipeList);

        final ArrayList<SavedRecipe> recipeList = new ArrayList<>();
        //final ArrayList<SavedRecipe> recipeList = SavedRecipe.getRecipesFromFile("recipes.json", getActivity());
        SavedRecipe.getRecipesFromFirebase(getActivity(), recipeList);
        ListView savedRecipeList = (ListView) view.findViewById(R.id.SavedRecipeList);

        savedRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), RecipeActivity.class);
                myIntent.putExtra("rid", recipeList.get((int)id).rid);
                String key = SharedConstants.FIREBASE_USER_ID;
                myIntent.putExtra("uid", key);
                getActivity().startActivity(myIntent);
            }
        });
    }
}