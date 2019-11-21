package com.example.worldchef.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Delete;

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
import com.example.worldchef.AsyncTasks.DeleteFavouriteAsyncTask;
import com.example.worldchef.AsyncTasks.DeleteFavouritesByUserAsyncTask;
import com.example.worldchef.AsyncTasks.GetFavouritesByUsernameAsyncTask;
import com.example.worldchef.Models.Favourite;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskFavouriteDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class FavouritesFragment extends Fragment implements AsyncTaskFavouriteDelegate {

    //Swipe to delete functionality was adapted from https://www.youtube.com/watch?v=dYbbTGiZ2sA

    private RecyclerView favouriteRecyclerView;
    private TextView mNoFavourites;
    private ImageView mClearAllBin;
    private List<Favourite> favourites;
    private ArrayList<Favourite> favouriteArrayList;

    private FavouriteAdapter favouriteAdapter;
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

         favouriteAdapter = new FavouriteAdapter();

        //Grab list of favourited meals based on username
        final AppDatabase db = AppDatabase.getInstance(view.getContext());
        GetFavouritesByUsernameAsyncTask getFavouritesByUsernameAsyncTask = new GetFavouritesByUsernameAsyncTask();
        getFavouritesByUsernameAsyncTask.setDatabase(db);
        getFavouritesByUsernameAsyncTask.setDelegate(FavouritesFragment.this);
        getFavouritesByUsernameAsyncTask.execute(username);


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
                DeleteFavouriteAsyncTask deleteFavouriteAsyncTask = new DeleteFavouriteAsyncTask();
                deleteFavouriteAsyncTask.setDatabase(db);
                deleteFavouriteAsyncTask.setDelegate(FavouritesFragment.this);
                deleteFavouriteAsyncTask.execute(favouriteAdapter.getFavouriteAt(viewHolder.getAdapterPosition()));

                //Might be an issue since it is executing it later
               // favouriteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());


            }
        }).attachToRecyclerView(favouriteRecyclerView);


        //Clicking on bin
        mClearAllBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete all favourites
                DeleteFavouritesByUserAsyncTask deleteFavouritesByUserAsyncTask = new DeleteFavouritesByUserAsyncTask();
                deleteFavouritesByUserAsyncTask.setDatabase(db);
                deleteFavouritesByUserAsyncTask.setDelegate(FavouritesFragment.this);
                deleteFavouritesByUserAsyncTask.execute(username);

            }
        });


        return view;

    }

    //Refreshing the fragment when back button is pressed
    @Override
    public void  onResume() {
        super.onResume();
        favouriteAdapter = new FavouriteAdapter();

        mNoFavourites.setVisibility(TextView.INVISIBLE);

        Context context = getContext();
        //Grab list of favourited meals based on username
        final AppDatabase db = AppDatabase.getInstance(context);
        GetFavouritesByUsernameAsyncTask getFavouritesByUsernameAsyncTask = new GetFavouritesByUsernameAsyncTask();
        getFavouritesByUsernameAsyncTask.setDatabase(db);
        getFavouritesByUsernameAsyncTask.setDelegate(FavouritesFragment.this);
        getFavouritesByUsernameAsyncTask.execute(username);

    }

    @Override
    public void handleInsertFavouriteTask(String result) {

    }

    @Override
    public void handleGetFavouritesByUsernameTask(List<Favourite> favouritesList) {

        this.favourites = favouritesList;
        //Convert into arraylist
        favouriteArrayList = new ArrayList<Favourite>(favourites);

        //if there are no favourites
        if (favouriteArrayList.size() < 1 ) {
            mNoFavourites.setVisibility(TextView.VISIBLE);
        }

        favouriteAdapter.setData(favouriteArrayList);

        favouriteRecyclerView.setAdapter(favouriteAdapter);

    }

    @Override
    public void handleGetFavouriteByUsernameAndMealNameTask(Favourite favourite) {

    }

    @Override
    public void handleGetCountOfFavouriteTask(long count) {

    }

    @Override
    public void handleDeleteFavouritesByUsername(String result) {

        //Once deleted, reset the recycler view
        System.out.println(result);
        Context context = getContext();
        Toast.makeText(context, "Deleted all favourites", Toast.LENGTH_SHORT).show();

        final AppDatabase db = AppDatabase.getInstance(context);
        GetFavouritesByUsernameAsyncTask getFavouritesByUsernameAsyncTask = new GetFavouritesByUsernameAsyncTask();
        getFavouritesByUsernameAsyncTask.setDatabase(db);
        getFavouritesByUsernameAsyncTask.setDelegate(FavouritesFragment.this);
        getFavouritesByUsernameAsyncTask.execute(username);

    }

    @Override
    public void handleDeleteFavourite(String result) {

        System.out.println(result);
        Context context = getContext();
        //Check if favourites is empty after swipe
        //Grab all the favourites list again (after specific favourite has been delete)
        final AppDatabase db = AppDatabase.getInstance(context);
        GetFavouritesByUsernameAsyncTask getFavouritesByUsernameAsyncTask = new GetFavouritesByUsernameAsyncTask();
        getFavouritesByUsernameAsyncTask.setDatabase(db);
        getFavouritesByUsernameAsyncTask.setDelegate(FavouritesFragment.this);
        getFavouritesByUsernameAsyncTask.execute(username);


        long count = favouriteArrayList.size();

        if(count < 1) {
            mNoFavourites.setVisibility((TextView.VISIBLE));
        }

        favouriteAdapter.notifyDataSetChanged();
        Toast.makeText(context, "Favourite deleted", Toast.LENGTH_SHORT).show();



    }
}
