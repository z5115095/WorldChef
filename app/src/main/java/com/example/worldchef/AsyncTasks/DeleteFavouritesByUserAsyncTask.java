package com.example.worldchef.AsyncTasks;

import android.os.AsyncTask;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;

public class DeleteFavouritesByUserAsyncTask extends AsyncTask<String, Integer, String> {

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
        protected String doInBackground(String...strings) {

                db.favouriteDao().DeleteFavouritesByUsername(strings[0]);


                return "Successfully deleted all favourites!";
        }

        @Override
        protected void onPostExecute(String result) {

                delegate.handleDeleteFavouritesByUsername(result);
        }

}