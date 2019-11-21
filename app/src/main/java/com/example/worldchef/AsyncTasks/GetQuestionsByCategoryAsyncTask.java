package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.TaskDelegates.AsyncTaskQuizDelegate;

import java.util.List;

public class GetQuestionsByCategoryAsyncTask extends AsyncTask<String, Integer, List<Quiz>> {

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
    protected List<Quiz> doInBackground(String... strings) {
        return db.quizDao().getAllQuestionsByCategory(strings[0]);

    }

    @Override
    protected void onPostExecute(List<Quiz> result) {

        delegate.handleGetQuestionTask(result);
    }
}


