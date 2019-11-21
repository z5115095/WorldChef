package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;

public class GetCountFavouriteAsyncTask extends AsyncTask<String, Integer, Long> {

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
protected Long doInBackground(String... strings) {

        //string 0 represents username, whilst string 1 represents meal
        return db.favouriteDao().getCountOfFavourite(strings[0], strings[1]);

        }

@Override
protected void onPostExecute(Long result) {

        delegate.handleGetCountOfFavouriteTask(result);
        }
        }