package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.List;

public class GetAllCategoriesAsyncTask extends AsyncTask<Void, Integer, List<Categories.Category>> {

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
    protected List<Categories.Category> doInBackground(Void... voids) {
        return db.categoryDao().getCategories();
    }

    @Override
    protected void onPostExecute(List<Categories.Category> result) {
        delegate.handleGetAllCategoriesTask(result);
    }

}
