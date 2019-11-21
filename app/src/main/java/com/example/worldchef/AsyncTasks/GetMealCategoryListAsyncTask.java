package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.MealsPerCategory;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskMealsCategoryDelegate;

import java.util.List;

public class GetMealCategoryListAsyncTask extends AsyncTask<Void, Integer, List<MealsPerCategory.MealFromCategory>> {

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
    protected List<MealsPerCategory.MealFromCategory> doInBackground(Void... voids) {
        return db.mealsFromCategoryDao().getMealsFromCategory();
    }

    @Override
    protected void onPostExecute(List<MealsPerCategory.MealFromCategory> result) {
        delegate.handleGetMealCategoryListTask(result);

    }
}
