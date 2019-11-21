package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.List;

public class GetAllUsernamesAsyncTask extends AsyncTask<Void, Integer, List<String>> {

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
        protected List<String> doInBackground(Void... voids) {
                return db.userDao().getUsernames();
        }

        @Override
        protected void onPostExecute(List<String> result) {
                delegate.handleGetUsernamesResult(result);
        }
}

