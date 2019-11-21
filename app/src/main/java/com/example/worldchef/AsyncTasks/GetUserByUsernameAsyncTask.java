package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

public class GetUserByUsernameAsyncTask extends AsyncTask<String, Integer, User> {

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
        protected User doInBackground(String... strings) {
                return db.userDao().getUserByUsername(strings[0]);

        }

        @Override
        protected void onPostExecute(User result) {
                delegate.handleGetUserByUserName(result);
        }
}


