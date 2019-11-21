package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.TaskDelegates.AsyncTaskQuizDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

public class InsertPointsAsyncTask extends AsyncTask<Object, Integer, String> {

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
    protected String doInBackground(Object...params) {

        //Getting additional points (1st parameter), current points (2nd parameter) and username (3rd parameter)
        //see userDAO
        int additionalPoints = (int)params[0];
        int currentPoints = (int)params[1];
        String username = (String) params[2];

        db.userDao().addPoints(additionalPoints,currentPoints,username);


        return "Successfully added points!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.handleInsertPoints(result);
    }

}