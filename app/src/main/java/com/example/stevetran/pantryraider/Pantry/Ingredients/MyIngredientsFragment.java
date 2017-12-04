package com.example.stevetran.pantryraider.Pantry.Ingredients;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;

import com.example.stevetran.pantryraider.Pantry.IngredientAddActivity;
import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Util.SharedConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * Created by William Ma on 11/17/2017.
 */
public class MyIngredientsFragment extends Fragment implements View.OnClickListener{
    Button addIngredientsButton;
    View view;
    ArrayList<String> currIngredients = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;
    private String key = SharedConstants.FIREBASE_USER_ID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("A", "oncreateview fragment");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        view = inflater.inflate(R.layout.fragment_my_ingredients, container, false);
        //set up buttons
        addIngredientsButton = (Button) view.findViewById(R.id.addIngredientsButton);
        addIngredientsButton.setOnClickListener(this);
        readUserIngredients();
        ListView lvSearch = (ListView) view.findViewById(R.id.MyIngredients);
        adapter = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                currIngredients
        );
        lvSearch.setAdapter(adapter);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long oId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(currIngredients.get((int)oId))
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Done
                                String removeIng = currIngredients.get((int)(oId));
                                currIngredients.remove((int)oId);
                                adapter.notifyDataSetChanged();
                                mDatabase.child("/Account/"+key+"/Ingredients").child(removeIng).removeValue();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();
            }
        });
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addIngredientsButton:
                Intent intent = new Intent(getActivity(), IngredientAddActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void readUserIngredients() {
        String key = SharedConstants.FIREBASE_USER_ID;
        mDatabase.child("/Account/").child(key + "/").child("Ingredients/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // addIngredients is a JSON object storing all ingredients in
                // the user's pantry, need to populate ListView in 'My Ingredients'
                if(snapshot.getValue() == null) {
                    Log.e("Server Error", "onDataChange: snapshot.getValue() is null");
                    return;
                }
                String ingList = snapshot.getValue().toString();
                ArrayList<String> ingArray = new ArrayList<>();
                ingList = ingList.substring(1, ingList.length());
                String[] tmp = ingList.split(", ");
                String name = "";
                for(int i = 0; i < tmp.length; i++) {
                    name = tmp[i].substring(0, tmp[i].indexOf('='));
                    if(!(name.equals("dummy"))){
                        currIngredients.add(name);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}