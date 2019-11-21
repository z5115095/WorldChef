package com.example.worldchef.TaskDelegates;

import com.example.worldchef.Models.MealsPerCategory;

import java.util.List;

public interface AsyncTaskMealsCategoryDelegate {

    void handleGetMealCategoryListTask(List<MealsPerCategory.MealFromCategory> meals);
    void handleInsertMealCategoryTask(String result);
}
