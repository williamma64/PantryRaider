package com.example.stevetran.pantryraider.Pantry;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Search.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by William Ma on 11/30/2017.
 */

public class IngredientSearchFragment extends Fragment implements View.OnClickListener {

    View view;
    Button ing_search_button;

    ArrayList<String> ing_results = new ArrayList();
    ArrayList<String> ing_add_results = new ArrayList();
    ArrayList<String> ing_results_rid;
    ArrayAdapter<String> adapterSearch;
    //CustomAdapter adapterSearch;
    ArrayAdapter<String> adapterAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ing_search, container, false);

        //set up buttons
        ing_search_button = (Button) view.findViewById(R.id.ing_search_button);
        ing_search_button.setOnClickListener(this);

        ListView lvSearch = (ListView) view.findViewById(R.id.ing_search_res);
        ing_results.addAll(Arrays.asList(getResources().getStringArray(R.array.ing_search_results)));
        //adapterSearch = new CustomAdapter(this.getContext(), ing_results);
        adapterSearch = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                ing_results
        );
        lvSearch.setAdapter(adapterSearch);

        ing_add_results.addAll(Arrays.asList(getResources().getStringArray(R.array.ing_add_results)));
        ListView lvAdd = (ListView) view.findViewById(R.id.myCart);
        adapterAdd = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                ing_add_results
        );
        lvAdd.setAdapter(adapterAdd);

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!ing_add_results.contains(ing_results.get((int)id))) {
                    ing_add_results.add(ing_results.get((int) id));
                    adapterAdd.notifyDataSetChanged();
                }
            }
        });

        lvAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ing_add_results.remove((int)id);
                adapterAdd.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ing_search_button:
                EditText query = (EditText) view.findViewById(R.id.ing_query);
                makeRequest(query.getText().toString());

        }
    }

    private void makeRequest(final String query) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url ="http://107.21.6.189:8080/auto_ingredients";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ing_results.clear();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            JSONArray ingredients = json.getJSONArray("ingredients");

                            for(int i = 0; i < ingredients.length(); i++) {
                                String name = ingredients.getJSONObject(i).getString("name");
                                String gid = ingredients.getJSONObject(i).getString("gid");
                                String image_url = ingredients.getJSONObject(i).getString("image_url");
                                Ingredient ing = new Ingredient(name, image_url, gid);

                                ing_results.add(ing.getName());
                                Log.d("A", ing.name);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapterSearch.notifyDataSetChanged();

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
