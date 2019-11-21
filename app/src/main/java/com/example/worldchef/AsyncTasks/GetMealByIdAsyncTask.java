package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.MealDetail;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.TaskDelegates.AsyncTaskMealDetailDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskQuizDelegate;

import java.util.List;

public class GetMealByIdAsyncTask extends AsyncTask<Integer, Integer, MealDetail.Meal> {

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
    protected MealDetail.Meal doInBackground(Integer...ints) {
        return db.mealDetailDao().getMealById(ints[0]);

    }

    @Override
    protected void onPostExecute(MealDetail.Meal result) {

        delegate.handleGetMealByIdTask(result );
    }
}

