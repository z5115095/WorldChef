package com.example.worldchef.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.worldchef.Models.Favourite;

import java.util.List;

@Dao
public interface FavouriteDao {

    //Add to favourites
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertFavourite(Favourite favourite);

    //Grab list of favourites based on username
    @Query("SELECT * FROM FAVOURITE WHERE USERNAME = :username")
    List<Favourite> getFavouriteListByUsername(String username);

    @Query("SELECT * FROM FAVOURITE WHERE USERNAME = :username AND MEALNAME = :mealName")
    Favourite getFavouriteByUsernameAndMeal(String username, String mealName);

    @Query("SELECT COUNT(*) FROM FAVOURITE WHERE USERNAME =:username AND MEALNAME = :mealName")
    long getCountOfFavourite(String username, String mealName);

    @Delete
    void deleteFavourite(Favourite favourite);

    @Query("DELETE FROM FAVOURITE WHERE USERNAME =:username")
    void DeleteFavouritesByUsername(String username);



}
