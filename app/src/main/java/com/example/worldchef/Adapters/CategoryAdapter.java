package com.example.worldchef.Adapters;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.worldchef.Activities.MainScreenActivity;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.Fragments.LearnFragment;
import com.example.worldchef.Fragments.MealFragment;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable {

    //List of categories
    public List<Categories.Category> categories;

    private List<Categories.Category> categoryListFull;



    public void setData(List<Categories.Category> categoriesToAdapt) {
        this.categories = categoriesToAdapt;
        categoryListFull = new ArrayList<>(categoriesToAdapt);
    }

    //Creating viewholder
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView mCategoryName;
        public ImageView mCategoryImage;

        public CategoryViewHolder(View v) {
            super(v);
            view = v;
            mCategoryImage = v.findViewById(R.id.category_cardimage);
            mCategoryName = v.findViewById(R.id.category_name);


        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);

        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

        final Categories.Category currentCategory = categories.get(position);

        holder.mCategoryName.setText(currentCategory.getStrCategory());
        String imageUrl = currentCategory.getStrCategoryThumb();

        //get points of user
        User currentUser = AppDatabase.getInstance(holder.mCategoryName.getContext()).userDao().getUserByUsername(username);
        final int currentPoints = currentUser.getPoints();

        //if user has less than 5 points, then set goat as locked
        if (currentPoints <5 && currentCategory.getStrCategory().contentEquals("Goat")) {
            holder.mCategoryImage.setImageResource(R.drawable.lockedcategory);
        } else {
            Glide.with(holder.mCategoryName.getContext()).load(imageUrl).into(holder.mCategoryImage);

        }



        //Clicking will transition to another fragment.
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If user that has less than 5 points clicks on it, then they won't be able to access.

                if (currentPoints < 5 && currentCategory.getStrCategory().contentEquals("Goat")) {
                    Toast.makeText(holder.mCategoryName.getContext(),"Must have at least 5 Michelin stars to unlock!",Toast.LENGTH_SHORT).show();
                } else {

                    Fragment mealFragment = new MealFragment();

                    //Storing the category name in bundle to give it to the mealFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("strCategory", currentCategory.getStrCategory());
                    mealFragment.setArguments(bundle);

                    //Transition fragment
                    MainScreenActivity activity = (MainScreenActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainscreen_fragmentA, mealFragment).commit();
                }


            }
        });
    }

    @Override
    public int getItemCount() { return categories.size();}

    @Override
    public Filter getFilter() { return categoryFilter;}

    //creating filter
    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //return filtered results
            List<Categories.Category> filteredCategoryList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                //show all the results
                filteredCategoryList.addAll(categoryListFull);
            } else {

                //Show filtered result

                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Categories.Category categoryFilters : categoryListFull) {

                    //Removing case sensitivity and seeing if there is a match

                    if (categoryFilters.getStrCategory().toLowerCase().contains(filterPattern)) {
                        filteredCategoryList.add(categoryFilters);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredCategoryList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categories.clear();
            categories.addAll((ArrayList) results.values);

            //Refresh Adapter
            notifyDataSetChanged();;


        }
    };
}
