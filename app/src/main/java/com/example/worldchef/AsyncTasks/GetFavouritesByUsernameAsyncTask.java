package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;

import java.util.List;

public class GetFavouritesByUsernameAsyncTask extends AsyncTask<String, Integer, List<Favourite>> {

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
    protected List<Favourite> doInBackground(String... strings) {

        //string 0 represents username
        return db.favouriteDao().getFavouriteListByUsername(strings[0]);

    }

    @Override
    protected void onPostExecute(List<Favourite> result) {

        delegate.handleGetFavouritesByUsernameTask(result);
    }
}