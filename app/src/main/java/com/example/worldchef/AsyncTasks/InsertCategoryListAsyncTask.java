package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.Categories.Category;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.Arrays;
import java.util.List;

public class InsertCategoryListAsyncTask extends AsyncTask<Category, Integer, String> {

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
    protected String doInBackground(Category... categories) {

        db.categoryDao().insertCategoryList(Arrays.asList(categories));

        return "Successfully inserted categories!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertCategoryListTask(result);
    }

}

