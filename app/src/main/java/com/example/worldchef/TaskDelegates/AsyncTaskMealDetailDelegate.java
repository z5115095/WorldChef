package com.example.worldchef.TaskDelegates;

import com.example.worldchef.Models.MealDetail;

public interface AsyncTaskMealDetailDelegate {

    void handleGetMealByIdTask(MealDetail.Meal meal);
    void handleInsertMealTask(String result);
}
