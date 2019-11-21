package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.List;

public class GetAllUsersAsyncTask extends AsyncTask<Void, Integer, List<User>> {

    private AsyncTaskUserDelegate delegate;

    private AppDatabase db;

    public void setDelegate(AsyncTaskUserDelegate delegate) {
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
    protected List<User> doInBackground(Void... voids) {
        return db.userDao().getAllUsers();
    }

    @Override
    protected void onPostExecute(List<User> result) {
        delegate.handleGetAllUsersResult(result);
    }

}
