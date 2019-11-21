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

        // When the task is finished, it will return.
        // You would normally want to return the result of your task.
        // For example, if my task was to get books from DB, I would make this method return the list
        // of books. The return value goes straight to onPostExecute.
        return "Successfully registered!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertUserResult(result);
    }

}
