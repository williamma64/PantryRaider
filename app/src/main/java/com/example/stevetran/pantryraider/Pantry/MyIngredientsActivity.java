package com.example.stevetran.pantryraider.Pantry;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Search.SearchFragment;
import com.example.stevetran.pantryraider.Util.BottomNavigationHelper;
import com.example.stevetran.pantryraider.Util.SectionsPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


public class MyIngredientsActivity extends AppCompatActivity {
    Context mContext = MyIngredientsActivity.this;
    private static final int ACTIVITY_NUM = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("A", "setContentView");
        setContentView(R.layout.activity_my_ingredients);
        Log.d("A", "set up fragment");
        setupViewPager();
        setupBottomNavigationView();
    }
    /**
     * start fragments
     */
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyIngredientsFragment()); //index 2
        ViewPager viewPager = (ViewPager) findViewById(R.id.container); //from layout_center_viewpager
        viewPager.setAdapter(adapter);

    }
    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationViewEx, this);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
