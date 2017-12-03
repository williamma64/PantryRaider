package com.example.stevetran.pantryraider.Pantry;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stevetran.pantryraider.R;
import com.example.stevetran.pantryraider.Search.SearchFragment;
import com.example.stevetran.pantryraider.Util.SectionsPagerAdapter;


public class MyIngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("A", "setContentView");
        setContentView(R.layout.activity_my_ingredients);
        Log.d("A", "set up fragment");
        setupViewPager();
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
}
