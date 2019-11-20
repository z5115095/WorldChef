package com.example.worldchef.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.Models.MealDetail;
import com.example.worldchef.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class MealDetailActivity extends AppCompatActivity {

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
        int mealId = explicitIntent.getIntExtra("idMeal", 0);
        mealName = explicitIntent.getStringExtra("strMeal");

        //Change title of Toolbar
        collapsingToolbarLayout.setTitle(mealName);

        //Count - if count = 0, then it hasn't been favourited yet
        favouriteCount = AppDatabase.getInstance(MealDetailActivity.this).favouriteDao()
                .getCountOfFavourite(username, mealName);

        //set the favourite button image to be dark-hearted if it has already been favourited

        if(favouriteCount != 0) {
            favouriteActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

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

                //Add this meal into my database
                AppDatabase.getInstance(context).mealDetailDao().insertMeal(thisMealApi);

                //Extract the specific meal from my database based on name of the meal
                final MealDetail.Meal currentMeal = AppDatabase.getInstance(context).mealDetailDao().getMealByName(mealName);

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


//                get youtube URL
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

                //Button to add to favourites
                favouriteActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        favouriteCount = AppDatabase.getInstance(MealDetailActivity.this).favouriteDao()
                                .getCountOfFavourite(username, mealName);

                        //Check if not favourited already. If the count = 0, then it has not been favourited yet
                        if (favouriteCount == 0){
                            //Add to favourites database
                            AppDatabase.getInstance(MealDetailActivity.this).favouriteDao()
                                    .insertFavourite(new Favourite(0,username,currentMeal.getStrMeal(),currentMeal.getStrMealThumb()));

                            //Display toast
                            favourited(v);

                            //change button
                            favouriteActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);


                        } else {
                            //Unfavourite
                            //Get favourite
                            Favourite favourite = AppDatabase.getInstance(MealDetailActivity.this)
                                    .favouriteDao().getFavouriteByUsernameAndMeal(username,mealName);

                            //Delete favourite
                            AppDatabase.getInstance(MealDetailActivity.this).favouriteDao().deleteFavourite(favourite);

                            //Display toast
                            unFavourited(v);

                            //Change button colour
                            favouriteActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);



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

    public void favourited(View v) {
        Toast.makeText(MealDetailActivity.this, "You have successfully favourited " + mealName, Toast.LENGTH_SHORT).show();
    }

    public void unFavourited(View v) {
        Toast.makeText(MealDetailActivity.this, "You have successfully unfavourited " + mealName, Toast.LENGTH_SHORT).show();
    }
}
