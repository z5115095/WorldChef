package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

public class InsertFavouriteAsyncTask extends AsyncTask<Favourite, Integer, String> {

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
    protected String doInBackground(Favourite...favourites) {

        db.favouriteDao().insertFavourite(favourites[0]);


        return "Successfully added as favourite!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertFavouriteTask(result);
    }

}