package com.example.worldchef.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.worldchef.Activities.MealDetailActivity;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.R;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{

    public ArrayList<Favourite> favMeals = new ArrayList<>();


    public void setData(ArrayList<Favourite> favMealsToAdapt) {

        this.favMeals = favMealsToAdapt;

    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {

        public TextView favMealName;
        public View view;
        public ImageView mealPhoto;

        public FavouriteViewHolder(View v) {
            super(v);
            view = v;
            favMealName = v.findViewById(R.id.fav_mealName);
            mealPhoto = v.findViewById(R.id.fav_cardimage);

        }

    }
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite, parent, false);

        FavouriteViewHolder favouriteViewHolder= new FavouriteViewHolder(view);
        return favouriteViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final FavouriteViewHolder holder, final int position) {
        final Favourite currentFavourite = favMeals.get(position);

        if(holder!=null) {
            if(currentFavourite !=null) {
                System.out.println(currentFavourite.getMealName());
                holder.favMealName.setText(currentFavourite.getMealName());
                String imageUrl = currentFavourite.getMealImageUrl();
                Glide.with(holder.favMealName.getContext()).load(imageUrl).into(holder.mealPhoto);
            } else{
                Log.e(getClass().getSimpleName(),"Model object is null");
            }
        } else {
            Log.e(getClass().getSimpleName(),"holder object is null");

        }



        //Click on the favourited meal and it takes you back to activity detail
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent explicitIntent = new Intent(context, MealDetailActivity.class);
                explicitIntent.putExtra("strMeal", currentFavourite.getMealName());
                context.startActivity(explicitIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return favMeals.size();
    }

    public Favourite getFavouriteAt(int position) {
        return favMeals.get(position);
    }

}