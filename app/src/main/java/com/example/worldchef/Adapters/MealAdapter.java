package com.example.worldchef.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.worldchef.Activities.MealDetailActivity;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.Models.MealsPerCategory;
import com.example.worldchef.R;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> implements Filterable {

    //List of meals
    public List<MealsPerCategory.MealFromCategory> meals;

    private List<MealsPerCategory.MealFromCategory> mealsListFull;



    public void setData(List<MealsPerCategory.MealFromCategory> mealsToAdapt) {
        this.meals = mealsToAdapt;
        mealsListFull = new ArrayList<>(mealsToAdapt);
    }

    //Creating viewholder
    public static class MealViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView mMealName;
        public ImageView mMealImage;

        public MealViewHolder(View v) {
            super(v);
            view = v;

            mMealImage = v.findViewById(R.id.meal_cardimage);
            mMealName = v.findViewById(R.id.meal_name);


        }
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal, parent, false);

        MealViewHolder mealViewHolder = new MealViewHolder(view);
        return mealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MealViewHolder holder, final int position) {

        final MealsPerCategory.MealFromCategory currentMeal = meals.get(position);

        holder.mMealName.setText(currentMeal.getStrMeal());
        String imageUrl = currentMeal.getStrMealThumb();
        Glide.with(holder.mMealName.getContext()).load(imageUrl).into(holder.mMealImage);

        //Clicking will shift activity
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();

                //Give meal detail the meal name, then start the activity
                Intent explicitIntent = new Intent(context, MealDetailActivity.class);
                explicitIntent.putExtra("strMeal", currentMeal.getStrMeal());
                explicitIntent.putExtra("idMeal",currentMeal.getIdMeal());
                System.out.println("this is the meal name we are giving to meal detail activity: " + currentMeal.getStrMeal());
                System.out.println("This is the meal ID: " + currentMeal.getIdMeal());
                context.startActivity(explicitIntent);


            }
        });




    }

    @Override
    public int getItemCount() { return meals.size();}

    @Override
    public Filter getFilter() { return mealFilter;}

    //creating filter
    private Filter mealFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //return filtered results
            List<MealsPerCategory.MealFromCategory> filteredMealList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                //show all the results
                filteredMealList.addAll(mealsListFull);
            } else {

                //Show filtered result

                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MealsPerCategory.MealFromCategory mealFilters : mealsListFull) {

                    //Removing case sensitivity and seeing if there is a match

                    if (mealFilters.getStrMeal().toLowerCase().contains(filterPattern)) {
                        filteredMealList.add(mealFilters);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredMealList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            meals.clear();
            meals.addAll((ArrayList) results.values);

            //Refresh Adapter
            notifyDataSetChanged();;


        }
    };
}
