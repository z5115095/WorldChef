package com.example.worldchef.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealsPerCategory {

    //This model is used to extract all the meals from each category
    public List<MealFromCategory> meals;

    public MealsPerCategory(List<MealFromCategory> meals) {
        this.meals = meals;
    }

    public List<MealFromCategory> getMeals() {
        return meals;
    }

    public void setMeals(List<MealFromCategory> meals) {
        this.meals = meals;
    }

    @Entity
    public static class MealFromCategory {

        //Meal ID
        @PrimaryKey
        public int idMeal;

        //Meal name
        public String strMeal;

        //Meal image
        public String strMealThumb;

        public int getIdMeal() {
            return idMeal;
        }

        public void setIdMeal(int idMeal) {
            this.idMeal = idMeal;
        }

        public String getStrMeal() {
            return strMeal;
        }

        public void setStrMeal(String strMeal) {
            this.strMeal = strMeal;
        }

        public String getStrMealThumb() {
            return strMealThumb;
        }

        public void setStrMealThumb(String strMealThumb) {
            this.strMealThumb = strMealThumb;
        }
    }
}
