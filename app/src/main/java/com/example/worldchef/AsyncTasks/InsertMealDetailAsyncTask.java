package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.MealDetail;
import com.example.worldchef.TaskDelegates.AsyncTaskMealDetailDelegate;

public class InsertMealDetailAsyncTask extends AsyncTask<MealDetail.Meal, Integer, String> {

    private AsyncTaskMealDetailDelegate delegate;

    private AppDatabase db;

    public void setDelegate(AsyncTaskMealDetailDelegate delegate) {
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
    protected String doInBackground(MealDetail.Meal...meals) {

        db.mealDetailDao().insertMeal(meals[0]);

        return "Successfully added meal!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertMealTask(result);
    }

}