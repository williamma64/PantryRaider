package com.example.stevetran.pantryraider.Pantry;

/**
 * Created by rongfalu on 11/30/17.
 */


import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stevetran.pantryraider.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter{
    ArrayList<Recipe> modelItems = null;
    Context context;

    public RecipeAdapter(Context context, ArrayList<Recipe> resource) {
        super(context, R.layout.view_row,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.view_row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        ImageView cb = (ImageView) convertView.findViewById(R.id.image1);
        name.setText(fixLength(modelItems.get(position).getName()));
        Picasso.with(context)
                .load(modelItems.get(position).getImage_url())
                .resize(200, 200)
                .into(cb);
        return convertView;
    }

    private String fixLength(String name) {
        if(name.length() > 25) {
            name = name.substring(0, 25) + "...";
        }
        Log.d("A", "name = " + name);
        return name;
    }
}