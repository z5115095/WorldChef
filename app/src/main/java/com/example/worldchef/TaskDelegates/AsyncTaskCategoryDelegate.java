package com.example.worldchef.TaskDelegates;

import com.example.worldchef.Models.Categories;

import java.util.List;

public interface AsyncTaskCategoryDelegate {

    void handleGetAllCategoriesTask(List<Categories.Category> categories);
    void handleGetCategoryByNameTask(Categories.Category category);
    void handleInsertCategoryListTask(String result);
}
