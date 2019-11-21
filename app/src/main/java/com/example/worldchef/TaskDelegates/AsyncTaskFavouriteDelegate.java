package com.example.worldchef.TaskDelegates;

import com.example.worldchef.Models.Favourite;

import java.util.List;

public interface AsyncTaskFavouriteDelegate {

    void handleInsertFavouriteTask(String result);
    void handleGetFavouritesByUsernameTask(List<Favourite> favourites);
    void handleGetFavouriteByUsernameAndMealNameTask(Favourite favourite);
    void handleGetCountOfFavouriteTask(long count);
    void handleDeleteFavouritesByUsername(String result);
    void handleDeleteFavourite(String result);
}
