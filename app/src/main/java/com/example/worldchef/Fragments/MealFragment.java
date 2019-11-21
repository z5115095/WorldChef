package com.example.worldchef.Fragments;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.worldchef.Adapters.MealAdapter;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.GetCategoryByNameAsyncTask;
import com.example.worldchef.AsyncTasks.GetMealCategoryListAsyncTask;
import com.example.worldchef.AsyncTasks.InsertMealCategoryAsyncTask;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.MealsPerCategory;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskMealsCategoryDelegate;
import com.google.gson.Gson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

public class MealFragment extends Fragment implements AsyncTaskCategoryDelegate, AsyncTaskMealsCategoryDelegate {

    private RecyclerView recyclerView;
    private ImageView mCategoryImage;
    private TextView mCategoryName;
    private SearchView mealSearchView;
    private CardView mCardView;
    private Categories.Category thisCategory;
    private MealAdapter mealAdapter;
    private List<MealsPerCategory.MealFromCategory> mealList;

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_meal, container, false);

        //Linking to layout
        recyclerView = view.findViewById(R.id.meal_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mealAdapter = new MealAdapter();

        mealSearchView = view.findViewById(R.id.meal_sv);
        //mCardView = view.findViewById(R.id.meal_cardview);
        mCategoryImage = view.findViewById(R.id.categorydetail_cardimage);
        mCategoryName = view.findViewById(R.id.categorydetail_name);
        mealSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mealSearchView.setQueryHint("Search meals");



        //Creating the search view mechanics:
        mealSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mealAdapter.getFilter().filter(newText);
                return false;
            }
        });


        //Extract the Meal category name from the previous fragment
        String categoryName = getArguments().getString("strCategory");

        //Get the category via category name so then we can apply the image and name on the UI
        final AppDatabase db = AppDatabase.getInstance(view.getContext());
        GetCategoryByNameAsyncTask getCategoryByIdAsyncTask = new GetCategoryByNameAsyncTask();
        getCategoryByIdAsyncTask.setDatabase(db);
        getCategoryByIdAsyncTask.setDelegate(MealFragment.this);
        getCategoryByIdAsyncTask.execute(categoryName);


        //get the recycler view going
        //Use API to call upon the list of meals within that category
        String mealUrl = "https://www.themealdb.com/api/json/v1/1/filter.php?c=" + categoryName;

        Context context = getContext();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();

                MealsPerCategory thisMealCategory = gson.fromJson(response, MealsPerCategory.class);

                mealList = thisMealCategory.getMeals();

                //Convert to array so we can use async task
                MealsPerCategory.MealFromCategory[] mealsArray = mealList.toArray(new MealsPerCategory.MealFromCategory[mealList.size()]);

                //Add this into my database
                InsertMealCategoryAsyncTask insertMealCategoryAsyncTask = new InsertMealCategoryAsyncTask();
                insertMealCategoryAsyncTask.setDatabase(db);
                insertMealCategoryAsyncTask.setDelegate(MealFragment.this);
                insertMealCategoryAsyncTask.execute(mealsArray);



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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mealUrl, responseListener, errorListener);
        requestQueue.add(stringRequest);



        return view;
    }

    @Override
    public void handleGetAllCategoriesTask(List<Categories.Category> categories) {

    }

    @Override
    public void handleGetCategoryByNameTask(Categories.Category category) {
        thisCategory = category;
        String categoryName = thisCategory.getStrCategory();
        mCategoryName.setText(categoryName);
        String categoryImageUrl =  thisCategory.getStrCategoryThumb();
        Glide.with(mCategoryName.getContext()).load(categoryImageUrl).into(mCategoryImage);


    }


    @Override
    public void handleInsertCategoryListTask(String result) {

    }

    @Override
    public void handleGetMealCategoryListTask(List<MealsPerCategory.MealFromCategory> meals) {

        //We now have a list of meals that we want to display on recycler view
        meals = mealList;
        mealAdapter.setData(meals);
        recyclerView.setAdapter(mealAdapter);
    }

    @Override
    public void handleInsertMealCategoryTask(String result) {

        System.out.println(result);
        //Once we have inserted the meals of the category into the database, we need to extract it.
        //Extract list of categories from database instead - need get meals from WHERE category...
        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        GetMealCategoryListAsyncTask getMealCategoryListAsyncTask = new GetMealCategoryListAsyncTask();
        getMealCategoryListAsyncTask.setDatabase(db);
        getMealCategoryListAsyncTask.setDelegate(MealFragment.this);
        getMealCategoryListAsyncTask.execute();


    }
}
