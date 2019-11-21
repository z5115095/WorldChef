package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;

public class GetCategoryByNameAsyncTask extends AsyncTask<String, Integer, Categories.Category> {

    private AsyncTaskCategoryDelegate delegate;

    private AppDatabase db;

    public void setDelegate(AsyncTaskCategoryDelegate delegate) {
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
    protected Categories.Category doInBackground(String...strings) {
        return db.categoryDao().getCategoryByName(strings[0]);

    }

    @Override
    protected void onPostExecute(Categories.Category result) {
        delegate.handleGetCategoryByNameTask(result);
    }
}
