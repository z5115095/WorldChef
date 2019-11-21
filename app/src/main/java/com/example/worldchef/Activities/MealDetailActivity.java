package com.example.worldchef.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Delete;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.DeleteFavouriteAsyncTask;
import com.example.worldchef.AsyncTasks.DeleteFavouritesByUserAsyncTask;
import com.example.worldchef.AsyncTasks.GetCountFavouriteAsyncTask;
import com.example.worldchef.AsyncTasks.GetFavByUserAndMealAsyncTask;
import com.example.worldchef.AsyncTasks.GetMealByIdAsyncTask;
import com.example.worldchef.AsyncTasks.InsertFavouriteAsyncTask;
import com.example.worldchef.AsyncTasks.InsertMealDetailAsyncTask;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.Models.MealDetail;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskMealDetailDelegate;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

//Collapsing tool bar with image adapted from: https://www.youtube.com/watch?v=-pTW3EOPxtQ&t=421s

public class MealDetailActivity extends AppCompatActivity implements AsyncTaskMealDetailDelegate, AsyncTaskFavouriteDelegate {

    ImageView appBarImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView mealCountry;
    TextView mealCategory;
    TextView mealIngredients;
    TextView mealInstructions;
    CardView youtubeCv;
    FloatingActionButton favouriteActionButton;
    public static String mealName;
    public long favouriteCount;
    private int mealId;
    private MealDetail.Meal currentMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        appBarImage = findViewById(R.id.app_bar_image);
        collapsingToolbarLayout = findViewById(R.id.mealdetail_collapse_tb);
        mealCountry = findViewById(R.id.mealdetail_area);
        mealCategory = findViewById(R.id.mealdetail_category);
        mealIngredients = findViewById(R.id.mealdetail_ingredients);
        mealInstructions = findViewById(R.id.mealdetail_instructions);
        youtubeCv = findViewById(R.id.mealdetail_youtube_cv);
        favouriteActionButton = findViewById(R.id.mealdetail_fav_button);


        //Grab the meal name that was clicked on.
        Intent explicitIntent = getIntent();
        mealId = explicitIntent.getIntExtra("idMeal", 0);
        mealName = explicitIntent.getStringExtra("strMeal");

        //Change title of Toolbar
        collapsingToolbarLayout.setTitle(mealName);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        //Count - if count = 0, then it hasn't been favourited yet - see method below
        final AppDatabase db = AppDatabase.getInstance(MealDetailActivity.this);
        final GetCountFavouriteAsyncTask getCountFavouriteAsyncTask = new GetCountFavouriteAsyncTask();
        getCountFavouriteAsyncTask.setDatabase(db);
        getCountFavouriteAsyncTask.setDelegate(MealDetailActivity.this);
        getCountFavouriteAsyncTask.execute(username, mealName);

        //Use API to call upon the meal by ID
        String mealUrl = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + mealId;
        System.out.println("This is the meal string URL: " + mealUrl);

        final Context context = MealDetailActivity.this;

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("This is what you are getting from API: " + response);
                Gson gson = new Gson();

                MealDetail thisMealDetail = gson.fromJson(response, MealDetail.class);

                //Get meal - effectively I've now got the specific meal
                List<MealDetail.Meal> mealList = thisMealDetail.getMeals();

                //Grab the specific meal and put it into the database
                MealDetail.Meal thisMealApi = mealList.get(0);

                //Add this meal into my database - this calls the handleInsertMealDetailTask method
                InsertMealDetailAsyncTask insertMealDetailAsyncTask = new InsertMealDetailAsyncTask();
                insertMealDetailAsyncTask.setDatabase(db);
                insertMealDetailAsyncTask.setDelegate(MealDetailActivity.this);
                insertMealDetailAsyncTask.execute(thisMealApi);


                //Button to add to favourites
                favouriteActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {

                        //Check if not favourited already. If the count = 0, then it has not been favourited yet
                        if (favouriteCount == 0){
                            //Add to favourites database
                            InsertFavouriteAsyncTask insertFavouriteAsyncTask = new InsertFavouriteAsyncTask();
                            insertFavouriteAsyncTask.setDatabase(db);
                            insertFavouriteAsyncTask.setDelegate(MealDetailActivity.this);
                            insertFavouriteAsyncTask.execute(new Favourite(0,username,
                                    currentMeal.getStrMeal(),currentMeal.getStrMealThumb(), currentMeal.getIdMeal()));



                        } else {
                            //Unfavourite
                            //Get favourite --> handleGetFav method
                            GetFavByUserAndMealAsyncTask getFavByUserAndMealAsyncTask = new GetFavByUserAndMealAsyncTask();
                            getFavByUserAndMealAsyncTask.setDatabase(db);
                            getFavByUserAndMealAsyncTask.setDelegate(MealDetailActivity.this);
                            getFavByUserAndMealAsyncTask.execute(username,mealName);


                        }


                    }
                });


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




    }

    public void ingredientsCheck(String ingredient) {
        //Check if strIngredient is null or "". If not, then add it to ingredients

        if (ingredient != null && !ingredient.isEmpty()) {
            mealIngredients.append("\n -" + ingredient);

        }


    }

    public void favourited() {
        Toast.makeText(MealDetailActivity.this, "You have successfully favourited " + mealName, Toast.LENGTH_SHORT).show();
    }

    public void unFavourited() {
        Toast.makeText(MealDetailActivity.this, "You have successfully unfavourited " + mealName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleInsertFavouriteTask(String result) {
        //if successfully favourited, display toast and change the heart colour
        //Display toast
        favourited();
        //change button
        favouriteActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);

        //reset favouriteCount
        favouriteCount = 1;
    }

    @Override
    public void handleGetFavouritesByUsernameTask(List<Favourite> favourites) {

    }

    @Override
    public void handleGetFavouriteByUsernameAndMealNameTask(Favourite favourite) {

       //Delete this favourite we are getting
        AppDatabase db = AppDatabase.getInstance(MealDetailActivity.this);
        DeleteFavouriteAsyncTask deleteFavouriteAsyncTask = new DeleteFavouriteAsyncTask();
        deleteFavouriteAsyncTask.setDatabase(db);
        deleteFavouriteAsyncTask.setDelegate(MealDetailActivity.this);
        deleteFavouriteAsyncTask.execute(favourite);

    }

    @Override
    public void handleGetCountOfFavouriteTask(long count) {
        favouriteCount = count;
        //set the favourite button image to be dark-hearted if it has already been favourited
        if(favouriteCount != 0) {
            favouriteActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

    }

    @Override
    public void handleDeleteFavouritesByUsername(String result) {

    }

    @Override
    public void handleDeleteFavourite(String result) {
        //Once we have deleted the favourite, make toast and change the favourite action button

        //Display toast
        unFavourited();

        //Change button colour
        favouriteActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        //reset favouriteCount
        favouriteCount = 0;

    }

    @Override
    public void handleGetMealByIdTask(MealDetail.Meal meal) {
        //Extract the specific meal from my database based on name of the meal
        currentMeal = meal;

        //Display all the relevant content
        mealCountry.setText(currentMeal.getStrArea());
        mealCategory.setText(currentMeal.getStrCategory());
        mealInstructions.setText(currentMeal.getStrInstructions());

        String imageUrl = currentMeal.getStrMealThumb();
        System.out.println(imageUrl);
        Glide.with(MealDetailActivity.this).load(imageUrl).into(appBarImage);

        //Check if strIngredient is null or "". If not, then add it to ingredients
        ingredientsCheck(currentMeal.getStrIngredient1());
        ingredientsCheck(currentMeal.getStrIngredient2());
        ingredientsCheck(currentMeal.getStrIngredient3());
        ingredientsCheck(currentMeal.getStrIngredient4());
        ingredientsCheck(currentMeal.getStrIngredient5());
        ingredientsCheck(currentMeal.getStrIngredient6());
        ingredientsCheck(currentMeal.getStrIngredient7());
        ingredientsCheck(currentMeal.getStrIngredient8());
        ingredientsCheck(currentMeal.getStrIngredient9());
        ingredientsCheck(currentMeal.getStrIngredient10());
        ingredientsCheck(currentMeal.getStrIngredient11());
        ingredientsCheck(currentMeal.getStrIngredient12());
        ingredientsCheck(currentMeal.getStrIngredient13());
        ingredientsCheck(currentMeal.getStrIngredient14());
        ingredientsCheck(currentMeal.getStrIngredient15());
        ingredientsCheck(currentMeal.getStrIngredient16());
        ingredientsCheck(currentMeal.getStrIngredient17());
        ingredientsCheck(currentMeal.getStrIngredient18());
        ingredientsCheck(currentMeal.getStrIngredient19());
        ingredientsCheck(currentMeal.getStrIngredient20());

        //get youtube URL
        final String youtubeUrl = currentMeal.getStrYoutube();

        youtubeCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent implicitIntent = new Intent(Intent.ACTION_VIEW);
                implicitIntent.setData(Uri.parse(youtubeUrl));
                context.startActivity(implicitIntent);
            }

        }

        );
    }

    @Override
    public void handleInsertMealTask(String result) {
        System.out.println(result);

        //now we retrieve this meal from the database based on its ID
        AppDatabase db = AppDatabase.getInstance(MealDetailActivity.this);
        GetMealByIdAsyncTask getMealByIdAsyncTask = new GetMealByIdAsyncTask();
        getMealByIdAsyncTask.setDatabase(db);
        getMealByIdAsyncTask.setDelegate(MealDetailActivity.this);
        getMealByIdAsyncTask.execute(mealId);

    }
}
