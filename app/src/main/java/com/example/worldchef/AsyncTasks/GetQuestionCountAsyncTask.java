package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskQuizDelegate;

public class GetQuestionCountAsyncTask extends AsyncTask<Void, Integer, Long> {

    private AsyncTaskQuizDelegate delegate;

    private AppDatabase db;

    public void setDelegate(AsyncTaskQuizDelegate delegate) {
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
    protected Long doInBackground(Void...voids) {

        return db.quizDao().getCountOfQuestions();

    }

    @Override
    protected void onPostExecute(Long result) {

        delegate.handleGetQuestionCountTask(result);
    }
}