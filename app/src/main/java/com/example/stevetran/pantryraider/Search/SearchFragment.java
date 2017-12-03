package com.example.stevetran.pantryraider.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stevetran.pantryraider.Pantry.Recipe;
import com.example.stevetran.pantryraider.Pantry.RecipeActivity;
import com.example.stevetran.pantryraider.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevetran on 11/19/17.
 */

public class SearchFragment extends Fragment implements View.OnClickListener{

    //this view
    View view;

    //buttons
    Button searchButton;

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayRecipes = new ArrayList<>();

    ArrayList<String> rid = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        //set up buttons
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        ListView lv = (ListView) view.findViewById(R.id.listview);
        arrayRecipes.addAll(Arrays.asList(getResources().getStringArray(R.array.arrayRecipes)));
        adapter = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                arrayRecipes
        );
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(SearchFragment.this.getActivity(), RecipeActivity.class);
                myIntent.putExtra("rid", rid.get((int)id));
                SearchFragment.this.getActivity().startActivity(myIntent);
            }
        });


        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                EditText query = (EditText) view.findViewById(R.id.CPoldpassword);
                Log.d("A", "query = " + String.valueOf(query));
                makeRequest(query.getText().toString());
//                Intent pwIntent = new Intent(getActivity(), ChangePasswordActivity.class);
//                startActivity(pwIntent);
//                break;
//            case R.id.setPreferencesButton:
//                Intent spIntent = new Intent(getActivity(), SetPreferencesActivity.class);
//                startActivity(spIntent);
//                break;
        }
    }

    private void makeRequest(final String query) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url ="http://107.21.6.189:8080/search_recipes";
        Log.d("A", "ooooooo");


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rid.clear();
                        Log.d("A", "response = " + response);
                        // Display the first 500 characters of the response string.
                        arrayRecipes.clear();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            JSONArray recipes = json.getJSONArray("recipes");

                            for(int i = 0; i < recipes.length(); i++) {

                                Recipe recipe = new Recipe();
                                recipe.title = recipes.getJSONObject(i).getString("title");
                                recipe.rid = recipes.getJSONObject(i).getString("rid");
                                recipe.instructionUrl = recipes.getJSONObject(i).getString("image_url");
                                arrayRecipes.add(recipe.title);
                                rid.add(recipe.rid);
                                Log.d("A", recipe.title);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }

        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("query",query);

                return params;
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
