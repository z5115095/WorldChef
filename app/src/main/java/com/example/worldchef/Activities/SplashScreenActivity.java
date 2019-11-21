package com.example.worldchef.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.InsertCategoryListAsyncTask;
import com.example.worldchef.MainActivity;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;
import com.google.gson.Gson;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity implements AsyncTaskCategoryDelegate {

    //Splash screen adapted from https://www.youtube.com/watch?v=jXtof6OUtcE


    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //grab data
        Intent getIntent = getIntent();

        final String username = getIntent.getStringExtra("username");

        //Extract API to display on next screen

        String categoryUrl = "https://www.themealdb.com/api/json/v1/1/categories.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(SplashScreenActivity.this);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();

                Categories thisCategories = gson.fromJson(response, Categories.class);

                List<Categories.Category> categoryList = thisCategories.getCategories();
                Categories.Category[] categoryArray = categoryList.toArray(new Categories.Category[categoryList.size()]);

                //Add list of categories into my database - Do this so it can be loaded up beforehand in mainscreen
                AppDatabase db = AppDatabase.getInstance(SplashScreenActivity.this);
                InsertCategoryListAsyncTask insertCategoryListAsyncTask = new InsertCategoryListAsyncTask();
                insertCategoryListAsyncTask.setDatabase(db);
                insertCategoryListAsyncTask.setDelegate(SplashScreenActivity.this);
                insertCategoryListAsyncTask.execute(categoryArray);
                requestQueue.stop();


            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {
                System.out.println(error.toString());
                requestQueue.stop();
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.GET, categoryUrl, responseListener, errorListener);
        requestQueue.add(stringRequest);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,MainScreenActivity.class);
                intent.putExtra("username", username);
                SplashScreenActivity.this.startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);




}

    @Override
    public void handleGetAllCategoriesTask(List<Categories.Category> categories) {

    }

    @Override
    public void handleGetCategoryByNameTask(Categories.Category category) {

    }


    @Override
    public void handleInsertCategoryListTask(String result) {

        System.out.println(result);
    }
};
