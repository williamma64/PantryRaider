
package com.example.stevetran.pantryraider.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stevetran.pantryraider.Login.LoginActivity;
import com.example.stevetran.pantryraider.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Abel on 11/17/2017.
 */

public class SettingFragment extends Fragment implements View.OnClickListener{
    //this view
    View view;

    //buttons
    Button changePasswordButton;
    Button setPreferencesButton;
    Button signOutButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        //set up buttons
        changePasswordButton = (Button) view.findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);
        setPreferencesButton = (Button) view.findViewById(R.id.setPreferencesButton);
        setPreferencesButton.setOnClickListener(this);
        signOutButton = (Button) view.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePasswordButton:
                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                viewPager.setCurrentItem(1);
                //set up a new title
                TextView mTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
                mTitle.setText("Change Password");
                break;
            case R.id.setPreferencesButton:
                ViewPager viewPager2 = (ViewPager) getActivity().findViewById(R.id.container);
                viewPager2.setCurrentItem(2);
                //set up a new title
                TextView mTitle2 = (TextView) getActivity().findViewById(R.id.toolbar_title);
                mTitle2.setText("Set Preferences");
                break;
            case R.id.signOutButton:
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent signOutIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(signOutIntent);
                break;
        }
    }
}