package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.Arrays;
import java.util.List;

public class InsertUserAsyncTask extends AsyncTask<User, Integer, String> {

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
    protected String doInBackground(User...users) {

        db.userDao().insertUser(users[0]);

        return "Successfully registered!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertUserResult(result);
    }

}
