package com.example.stevetran.pantryraider.Pantry;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import com.example.stevetran.pantryraider.Util.SectionsPagerAdapter;
import com.example.stevetran.pantryraider.Util.SharedConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by William Ma on 11/30/2017.
 */
public class IngredientSearchFragment extends Fragment implements View.OnClickListener {
    View view;
    Button ing_search_button;
    Button ing_add_button;
    ArrayList<String> ing_results = new ArrayList<String>();
    ArrayList<String> ing_add_results = new ArrayList<String>();
    Map<String,String> ings = new HashMap<String, String>();
    ArrayAdapter<String> adapterSearch;
    ArrayAdapter<String> adapterAdd;
    private DatabaseReference mDatabase;
    private DatabaseReference relPath;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ing_search, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //readUserIngredients();
        //set up buttons
        ing_search_button = (Button) view.findViewById(R.id.ing_search_button);
        ing_search_button.setOnClickListener(this);
        ing_add_button = (Button) view.findViewById(R.id.add_ing_button);
        ing_add_button.setOnClickListener(this);
        ListView lvSearch = (ListView) view.findViewById(R.id.ing_search_res);
        adapterSearch = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                ing_results
        );
        lvSearch.setAdapter(adapterSearch);
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
                if (!ing_add_results.contains(ing_results.get((int) id))) {
                    ing_add_results.add(ing_results.get((int) id));
                    adapterAdd.notifyDataSetChanged();
                }
            }
        });
        lvAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ing_add_results.remove((int) id);
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
                break;
            case R.id.add_ing_button:
                //readUserIngredients();
                storeUserIngredients();
                Intent myIng = new Intent(this.getActivity(), MyIngredientsActivity.class);
                startActivity(myIng);
                break;
        }
    }
    private void makeRequest(final String query) {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = "http://54.175.239.59:8080/auto_ingredients";
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
                            JSONArray ingredients = json.getJSONArray("results");
                            for (int i = 0; i < ingredients.length(); i++) {
                                String name = ingredients.getJSONObject(i).getString("name");
                                String gid = ingredients.getJSONObject(i).getString("gid");
                                String image_url = ingredients.getJSONObject(i).getString("image_url");
                                Ingredient ing = new Ingredient(name, image_url, gid);
                                ing_results.add(ing.getName());
                                if(!ings.containsKey(ing.getName())) {
                                    ings.put(ing.getName(), gid);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterSearch.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query", query);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void readUserIngredients() {
        String key = SharedConstants.FIREBASE_USER_ID;
        mDatabase.child("/Account/").child(key + "/").child("Ingredients/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("PRINTINGGGGGGGG: " + (snapshot.getValue()));
                JSONObject addIngredients;
                try {
                    // addIngredients is a JSON object storing all ingredients in
                    // the user's pantry, need to populate ListView in 'My Ingredients'
                    addIngredients = new JSONObject(snapshot.getValue().toString().replace(" ", "_"));
                    Log.d("AAAAA", "AAAAA");
                    Iterator<String> it = addIngredients.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        ing_add_results.add(key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void storeUserIngredients() {
        String key = SharedConstants.FIREBASE_USER_ID;//mDatabase.child("Account").push().getKey();
        System.out.println(key);

        int placeHolderCount = 0;
        for (String ingredient : ing_add_results) {
            mDatabase.child("/Account/"+key+"/Ingredients/").child(ingredient).setValue(ings.get(ingredient));
        }
    }
}
