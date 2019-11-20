package com.example.worldchef.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.worldchef.Models.MealsPerCategory;

import java.util.List;

@Dao
public interface MealsFromCategoryDao {

    //Get list of meals based on this category
    @Query("SELECT * FROM MEALFROMCATEGORY")
    List<MealsPerCategory.MealFromCategory> getMealsFromCategory();

    //Insert a list of categories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealList (List<MealsPerCategory.MealFromCategory> mealsFromCategory);


}