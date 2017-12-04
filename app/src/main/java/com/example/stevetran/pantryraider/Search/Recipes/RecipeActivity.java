package com.example.stevetran.pantryraider.Search.Recipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Util.BottomNavigationHelper;
import com.example.stevetran.pantryraider.Util.SharedConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rongfalu on 11/30/17.
 */

public class RecipeActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUM = 2;
    private Context mContext = RecipeActivity.this;

    //private View = R.layout.activity_recipe;
    private ImageView mImage;
    private TextView mName;
    private ListView mInstructions;

    private String image_url = "https://spoonacular.com/recipeImages/615348-556x370.jpg";
    private String name = "What";

    private String rid;
    private String uid;
    ArrayAdapter<String> adapter;
    ArrayList<String> ListInstructions = new ArrayList<>();

    private DatabaseReference mDatabase;
    private DatabaseReference relPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up the bottom navigation view
        //setupBottomNavigationView();

        Intent myIntent = getIntent();
        rid = myIntent.getStringExtra("rid");
        uid = myIntent.getStringExtra("uid");

        makeRequest(rid, uid);

        setContentView(R.layout.activity_recipe);

        mImage = findViewById(R.id.image_detail);
        mName = findViewById(R.id.recipename_detail);
        mInstructions = findViewById(R.id.InstructionList_detail);

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                ListInstructions
        );
        mInstructions.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void makeRequest(final String rid, final String uid) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://107.21.6.189:8080/recipe_detail";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);

                            image_url = json.getString("image_url");
                            name = json.getString("title");
                            /*JSONArray ing = json.getJSONArray("ingredients");
                            JSONArray ins = json.getJSONArray("instructions");

                            ListInstructions.add("INGREDIENTS:");
                            for(int i = 0; i < ing.length(); i++) {
                                ListInstructions.add(ing.getString(i));
                            }
                            ListInstructions.add("\n\nINSTRUCTIONS:");
                            for(int i = 0; i < ins.length(); i++) {
                                ListInstructions.add(ins.getJSONObject(i).getString("step"));
                            }*/

                            mName.setText(name);
                            Picasso.with(mContext)
                                    .load(image_url)
                                    .into(mImage);

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                params.put("rid",rid);
                //params.put("uid",uid);
                return params;
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void saveToDB(View view){
        String key = SharedConstants.FIREBASE_USER_ID;
        mDatabase.child("/Saved_Recipes/").child(key+"/").child(rid + "/").setValue(name);

    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationViewEx, this);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}