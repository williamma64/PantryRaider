package com.example.stevetran.pantryraider.Search.Recipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Util.BottomNavigationHelper;
import com.example.stevetran.pantryraider.Util.SharedConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
    private ListView mListIngredients;
    private ListView mSteps;
    private Button saveButton;

    private String image_url = "https://spoonacular.com/recipeImages/615348-556x370.jpg";
    private String name = "What";

    private String rid;

    ArrayAdapter<String> adapter_ingredinets;
    ArrayAdapter<String> adapter_steps;
    ArrayList<String> ListIngredients = new ArrayList<>();
    ArrayList<String> Steps = new ArrayList<>();

    private DatabaseReference mDatabase;
    private String key = SharedConstants.FIREBASE_USER_ID;

    private boolean isCurrentlySaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up the bottom navigation view
        //setupBottomNavigationView();

        Intent myIntent = getIntent();
        rid = myIntent.getStringExtra("rid");
        makeRequest(rid);

        setContentView(R.layout.activity_recipe);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = SharedConstants.FIREBASE_USER_ID;

        saveButton = findViewById(R.id.Save_Button);
        mDatabase.child("/Saved_Recipes/" + key + "/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("r"+rid).exists()){
                    // change button to unsave
                    saveButton.setText("Unsave Recipe");
                    isCurrentlySaved = true;
                } else {
                    // change button to save
                    saveButton.setText("Save Recipe");
                    isCurrentlySaved = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        mImage = findViewById(R.id.image_detail);
        mName = findViewById(R.id.recipename_detail);
        mListIngredients = findViewById(R.id.IngredientList_detail);
        mSteps = findViewById(R.id.Steps);



        adapter_ingredinets = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                ListIngredients
        );
        mListIngredients.setAdapter(adapter_ingredinets);

        adapter_steps = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Steps
        );
        mSteps.setAdapter(adapter_steps);

    }

    private void makeRequest(final String rid) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://54.175.239.59:8080/recipe_detail";


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
                            JSONArray ing = json.getJSONArray("ingredients");
                            for(int i = 0; i < ing.length(); i++) {
                                ListIngredients.add(ing.getJSONObject(i).getString("string"));
                            }
                            JSONArray instructions = json.getJSONArray("instructions");
                            for(int i = 0; i < instructions.length(); i++) {
                                Steps.add(instructions.getString(i));
                            }
                            mName.setText(name);
                            Picasso.with(mContext)
                                    .load(image_url)
                                    .into(mImage);

                            adapter_ingredinets.notifyDataSetChanged();
                            adapter_steps.notifyDataSetChanged();
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

                return params;
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    public void savePressed(View view){
        if(isCurrentlySaved) deleteFromSavedDB();
        else saveToDB();

    }
    public void saveToDB(){
        Toast.makeText(RecipeActivity.this, "Recipe Saved!",
                Toast.LENGTH_SHORT).show();
        mDatabase.child("/Saved_Recipes/" + key + "/").child("r"+rid).setValue(name);
        saveButton.setText("Unsave Recipe");
        isCurrentlySaved = true;


    }

    public  void deleteFromSavedDB() {
        Toast.makeText(RecipeActivity.this, "Recipe Unsaved!",
                Toast.LENGTH_SHORT).show();
        mDatabase.child("/Saved_Recipes/" + key + "/").child("r"+rid).removeValue();
        saveButton.setText("Save Recipe");
        isCurrentlySaved = false;

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