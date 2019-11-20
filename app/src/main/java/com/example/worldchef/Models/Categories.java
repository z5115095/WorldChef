package com.example.worldchef.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

public class Categories {



    //API is in the form of a single object whom's only attribute is a list called "categories"
    public List<Category> categories;

    public Categories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Entity
    public static class Category {
        @PrimaryKey
        @NonNull
       public int idCategory;

       public String strCategory;

       //This is the URL for the image
       public String strCategoryThumb;

        public Category(int idCategory, String strCategory, String strCategoryThumb) {
            this.idCategory = idCategory;
            this.strCategory = strCategory;
            this.strCategoryThumb = strCategoryThumb;
        }

        public int getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(int idCategory) {
            this.idCategory = idCategory;
        }

        public String getStrCategory() {
            return strCategory;
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrCategoryThumb() {
            return strCategoryThumb;
        }

        public void setStrCategoryThumb(String strCategoryThumb) {
            this.strCategoryThumb = strCategoryThumb;
        }
    }


}
