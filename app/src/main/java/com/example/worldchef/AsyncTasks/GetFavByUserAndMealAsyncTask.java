package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;

public class GetFavByUserAndMealAsyncTask extends AsyncTask<String, Integer, Favourite> {

    private AsyncTaskFavouriteDelegate delegate;

    private AppDatabase db;

    public void setDelegate(AsyncTaskFavouriteDelegate delegate) {
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
    protected Favourite doInBackground(String... strings) {

        //string 0 represents username, whilst string 1 represents meal
        return db.favouriteDao().getFavouriteByUsernameAndMeal(strings[0], strings[1]);

    }

    @Override
    protected void onPostExecute(Favourite result) {

        delegate.handleGetFavouriteByUsernameAndMealNameTask(result);
    }
}