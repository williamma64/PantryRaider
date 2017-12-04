package com.example.stevetran.pantryraider.Pantry;

/**
 * Created by rongfalu on 11/28/17.
 */

public class Recipe {
    public String title;
    public String description;
    public String imageUrl;
    public String instructionUrl;
    public String label;
    public String rid;

    public Recipe(){}

    public String getName() {
        return this.title;
    }

    public String getImage_url() {
        return this.imageUrl;
    }

    public String getId() {
        return this.rid;
    }
}