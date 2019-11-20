package com.example.worldchef.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.MealDetail;

import java.util.List;

@Dao
public interface MealDetailDao {

    @Query("SELECT * FROM MEAL")
    List<MealDetail.Meal> getMeals();

    //Get meal from meal name
    @Query("SELECT * FROM MEAL WHERE strMeal = :strMeal")
    MealDetail.Meal getMealByName(String strMeal);

    //Get meal from meal ID
    @Query("SELECT * FROM MEAL WHERE idMeal = :idMeal")
    MealDetail.Meal getMealById(int idMeal);

    //Insert a list of meals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealList (List<MealDetail.Meal> meals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal (MealDetail.Meal meal);
}
