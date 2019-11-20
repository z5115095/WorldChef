package com.example.worldchef.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldchef.Adapters.FavouriteAdapter;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class FavouritesFragment extends Fragment {

    private RecyclerView favouriteRecyclerView;
    private TextView mNoFavourites;
    private ImageView mClearAllBin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        favouriteRecyclerView = view.findViewById(R.id.fav_rv);
        mNoFavourites = view.findViewById(R.id.fav_norecipes);
        mClearAllBin = view.findViewById(R.id.fav_delete_all);

        //Set no favourites notification as invisible
        mNoFavourites.setVisibility(TextView.INVISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        favouriteRecyclerView.setLayoutManager(layoutManager);

        final FavouriteAdapter favouriteAdapter = new FavouriteAdapter();

        //Grab list of favourited meals based on username
        List<Favourite> favourites = AppDatabase.getInstance(view.getContext()).favouriteDao().getFavouriteListByUsername(username);
        //Convert into arraylist
        final ArrayList<Favourite> favouriteArrayList = new ArrayList<Favourite>(favourites);

        //if there are no favourites
        if (favouriteArrayList.size() < 1 ) {
            mNoFavourites.setVisibility(TextView.VISIBLE);
        }

        favouriteAdapter.setData(favouriteArrayList);

        favouriteRecyclerView.setAdapter(favouriteAdapter);


        //Create a delete by swipe function
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Context context = getContext();
                //Delete the favourite
                AppDatabase.getInstance(context).favouriteDao().deleteFavourite(favouriteAdapter.getFavouriteAt(viewHolder.getAdapterPosition()));
                //favouriteArrayList.remove(viewHolder.getAdapterPosition());

                //viewHolder.setIsRecyclable(false);
                favouriteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(context, "Favourite deleted", Toast.LENGTH_SHORT).show();

                //Check if favourites is empty after swipe
                long count = favouriteArrayList.size();

                if(count < 1) {
                    mNoFavourites.setVisibility((TextView.VISIBLE));
                }

            }
        }).attachToRecyclerView(favouriteRecyclerView);


        //Clicking on bin
        mClearAllBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Delete
                AppDatabase.getInstance(mClearAllBin.getContext()).favouriteDao().DeleteFavouritesByUsername(username);
                //Grab list of favourited meals based on username
                List<Favourite> favourites = AppDatabase.getInstance(mClearAllBin.getContext()).favouriteDao().getFavouriteListByUsername(username);
                //Convert into arraylist
                ArrayList<Favourite> favouriteArrayList = new ArrayList<Favourite>(favourites);

                //if there are no favourites
                if (favouriteArrayList.size() < 1 ) {
                    mNoFavourites.setVisibility(TextView.VISIBLE);
                }

                favouriteAdapter.setData(favouriteArrayList);

                favouriteRecyclerView.setAdapter(favouriteAdapter);

                Toast.makeText(mClearAllBin.getContext(), "Deleted all favourites", Toast.LENGTH_SHORT).show();




            }
        });


        return view;

    }

    //Refreshing the fragment when back button is pressed
    @Override
    public void  onResume() {
        super.onResume();
        final FavouriteAdapter favouriteAdapter = new FavouriteAdapter();

        mNoFavourites.setVisibility(TextView.INVISIBLE);

        Context context = getContext();
        //Grab list of favourited meals based on username
        List<Favourite> favourites = AppDatabase.getInstance(context).favouriteDao().getFavouriteListByUsername(username);
        //Convert into arraylist
        ArrayList<Favourite> favouriteArrayList = new ArrayList<Favourite>(favourites);

        //if there are no favourites
        if (favouriteArrayList.size() < 1 ) {
            mNoFavourites.setVisibility(TextView.VISIBLE);
        }

        favouriteAdapter.setData(favouriteArrayList);

        favouriteRecyclerView.setAdapter(favouriteAdapter);

    }
}
