package com.example.worldchef.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.worldchef.Models.Categories;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM CATEGORY")
    List<Categories.Category> getCategories();

    //Get category from category ID
    @Query("SELECT * FROM CATEGORY WHERE idCategory = :idCategory")
    Categories.Category getCategoryById(int idCategory);

    //Get category from category name
    @Query("SELECT * FROM CATEGORY WHERE strCategory = :strCategory")
    Categories.Category getCategoryByName(String strCategory);

    //Insert a list of categories
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertCategoryList (List<Categories.Category> categories);


}
