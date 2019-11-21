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
import com.example.worldchef.AsyncTasks.GetUserByUsernameAsyncTask;
import com.example.worldchef.Fragments.LearnFragment;
import com.example.worldchef.Fragments.MealFragment;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.ArrayList;
import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable, AsyncTaskUserDelegate {

    //List of categories
    public List<Categories.Category> categories;

    private List<Categories.Category> categoryListFull;
    private User currentUser;
    private int currentPoints;



    public void setData(List<Categories.Category> categoriesToAdapt) {
        this.categories = categoriesToAdapt;
        categoryListFull = new ArrayList<>(categoriesToAdapt);
    }

    @Override
    public void handleInsertUserResult(String result) {

    }

    @Override
    public void handleGetUserResult(User user) {

    }

    @Override
    public void handleGetAllUsersResult(List<User> users) {

    }

    @Override
    public void handleGetUsernamesResult(List<String> usernames) {

    }

    @Override
    public void handleGetUserByUserName(User user) {

        //set current points of the user

        currentUser = user;
        currentPoints = currentUser.getPoints();


    }

    private void checkForLocks(final CategoryViewHolder holder, final int position) {


    }

    @Override
    public void handleInsertPoints(String result) {

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

        //get points of user for unlock system
        AppDatabase db = AppDatabase.getInstance(holder.mCategoryName.getContext());
        GetUserByUsernameAsyncTask getUserByUsernameAsyncTask = new GetUserByUsernameAsyncTask();
        getUserByUsernameAsyncTask.setDatabase(db);
        getUserByUsernameAsyncTask.setDelegate(CategoryAdapter.this);
        getUserByUsernameAsyncTask.execute(username);

        //Set the image

        //If user that has less than the applicable points, display the locked photo
        //Unlockable categories: Miscellaneous, goat, and Dessert
        if (currentPoints <10 && currentCategory.getStrCategory().contentEquals("Goat")) {
            holder.mCategoryImage.setImageResource(R.drawable.lockedcategory);
        } else if(currentPoints <20 && currentCategory.getStrCategory().contentEquals("Dessert")) {
            holder.mCategoryImage.setImageResource(R.drawable.lockedcategory);
        } else if (currentPoints <30 && currentCategory.getStrCategory().contentEquals("Miscellaneous")) {
            holder.mCategoryImage.setImageResource(R.drawable.lockedcategory);
        } else {
            Glide.with(holder.mCategoryName.getContext()).load(imageUrl).into(holder.mCategoryImage);
        }

        //Clicking will transition to another fragment.
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If user that has less than the applicable points, they are unable to access these categories
                //Unlockable categories: Miscellaneous, goat, and Dessert

                if (currentPoints < 10 && currentCategory.getStrCategory().contentEquals("Goat")) {
                    Toast.makeText(holder.mCategoryName.getContext(),"Must have at least 10 Michelin stars to unlock!",Toast.LENGTH_SHORT).show();
                } else if(currentPoints <20  && currentCategory.getStrCategory().contentEquals("Dessert")) {
                    Toast.makeText(holder.mCategoryName.getContext(),"Must have at least 20 Michelin stars to unlock!",Toast.LENGTH_SHORT).show();
                } else if (currentPoints < 30 && currentCategory.getStrCategory().contentEquals("Miscellaneous")) {
                    Toast.makeText(holder.mCategoryName.getContext(),"Must have at least 30 Michelin stars to unlock!",Toast.LENGTH_SHORT).show();
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
