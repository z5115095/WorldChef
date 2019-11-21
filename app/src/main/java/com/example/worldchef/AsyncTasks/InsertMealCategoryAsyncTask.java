package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.MealsPerCategory;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskMealsCategoryDelegate;

import java.util.Arrays;

public class InsertMealCategoryAsyncTask extends AsyncTask<MealsPerCategory.MealFromCategory, Integer, String> {

    private AsyncTaskMealsCategoryDelegate delegate;

    private AppDatabase db;

    public void setDelegate(AsyncTaskMealsCategoryDelegate delegate) {
        this.delegate = delegate;
    }

    public void setDatabase(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(MealsPerCategory.MealFromCategory... meals) {

        db.mealsFromCategoryDao().insertMealList(Arrays.asList(meals));

        return "Successfully inserted meals from category!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertMealCategoryTask(result);
    }

}