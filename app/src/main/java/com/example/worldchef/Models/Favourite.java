package com.example.worldchef.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favourite {

    @PrimaryKey(autoGenerate = true)
    private int favouriteId;
    //Username of current user
    private String username;
    private String mealName;
    private String mealImageUrl;

    public Favourite(int favouriteId, String username, String mealName, String mealImageUrl) {
        this.favouriteId = favouriteId;
        this.username = username;
        this.mealName = mealName;
        this.mealImageUrl = mealImageUrl;
    }

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealImageUrl() {
        return mealImageUrl;
    }

    public void setMealImageUrl(String mealImageUrl) {
        this.mealImageUrl = mealImageUrl;
    }
}
