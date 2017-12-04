package com.example.stevetran.pantryraider.Setting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Util.SharedConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SetPreferencesFragment extends Fragment {
    View view;
    GridLayout dietPref;
    GridLayout allergyPref;

    private DatabaseReference mDatabase;
    private DatabaseReference relPath;
    Button savePrefButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set_preferences, container, false);
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
                    mTitle.setText("Setting");

                    return true;
                }
                return false;
            }
        });
        dietPref = (GridLayout)view.findViewById(R.id.DietGrid);
        allergyPref = (GridLayout)view.findViewById(R.id.AllergyGrid);
        //Get existing list
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readUserPreferences();
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savePrefButton = (Button) view.findViewById(R.id.savePrefButton);
        savePrefButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                updatePreferencesClicked(view);
            }
        });
    }

    private void readUserPreferences(){
        String key = SharedConstants.FIREBASE_USER_ID;

        mDatabase.child("/Account/").child(key + "/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                JSONObject allPrefs, dietPrefs, allergyPrefs;
                try {
                    allPrefs = new JSONObject(snapshot.getValue().toString());
                    dietPrefs = allPrefs.getJSONObject("Preferences");
                    allergyPrefs = allPrefs.getJSONObject("Allergies");

                    //Parse JSON Objects
                    for(int i = 0; i < dietPref.getChildCount(); i++){
                        CheckBox v = (CheckBox) dietPref.getChildAt(i);
                        v.setChecked(dietPrefs.getBoolean((String)v.getTag()));
                    }

                    for(int i = 0; i < allergyPref.getChildCount(); i++){
                        CheckBox v = (CheckBox) allergyPref.getChildAt(i);
                        v.setChecked(allergyPrefs.getBoolean((String)v.getTag()));
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


    public void updatePreferencesClicked(View view) {
        updatePrefsToFirebase();
    }

    private void updatePrefsToFirebase(){
        String key = SharedConstants.FIREBASE_USER_ID;//mDatabase.child("Account").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();

        Map<String, Object> postDietValues = new HashMap<>();
        Map<String, Object> postAllergyValues = new HashMap<>();


        for(int i = 0; i < dietPref.getChildCount(); i++){
            CheckBox v = (CheckBox) dietPref.getChildAt(i);
            if(v.isChecked()){
                postDietValues.put((String)v.getTag(), true);
            } else {
                postDietValues.put((String)v.getTag(), false);
            }
        }

        for(int i = 0; i < allergyPref.getChildCount(); i++){
            CheckBox v = (CheckBox) allergyPref.getChildAt(i);
            if(v.isChecked()){
                postAllergyValues.put((String)v.getTag(), true);
            } else {
                postAllergyValues.put((String)v.getTag(), false);
            }
        }

        childUpdates.put("/Account/" + key + "/Preferences/", postDietValues);
        childUpdates.put("/Account/" + key + "/Allergies/", postAllergyValues);
        mDatabase.updateChildren(childUpdates);
    }
}
