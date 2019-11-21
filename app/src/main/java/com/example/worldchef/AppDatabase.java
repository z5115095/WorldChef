package com.example.worldchef;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.worldchef.DAOs.CategoryDao;
import com.example.worldchef.DAOs.FavouriteDao;
import com.example.worldchef.DAOs.MealDetailDao;
import com.example.worldchef.DAOs.MealsFromCategoryDao;
import com.example.worldchef.DAOs.QuizDao;
import com.example.worldchef.DAOs.UserDao;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.Models.MealDetail;
import com.example.worldchef.Models.MealsPerCategory;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.Models.User;

@Database(entities = {User.class, Categories.Category.class, MealsPerCategory.MealFromCategory.class,
        MealDetail.Meal.class, Favourite.class, Quiz.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract MealsFromCategoryDao mealsFromCategoryDao();
    public abstract MealDetailDao mealDetailDao();
    public abstract FavouriteDao favouriteDao();
    public abstract QuizDao quizDao();

    private static AppDatabase instance;
    public static AppDatabase getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "worldChefDbv6000")
                   // .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
