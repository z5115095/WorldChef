package com.example.worldchef.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worldchef.Adapters.SocialAdapter;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.GetAllUsersAsyncTask;
import com.example.worldchef.AsyncTasks.GetUserByUsernameAsyncTask;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class SocialFragment extends Fragment implements AsyncTaskUserDelegate {

    private TextView mUsername;
    private RecyclerView socialRecyclerView;
    private TextView mUserLevel;
    private TextView mUserPoints;
    private ImageView mProfilePicture;
    private User currentUser;
    private List<User> userList;
    private SocialAdapter socialAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_social, container, false);

        mUsername = view.findViewById(R.id.social_username);
        socialRecyclerView = view.findViewById(R.id.social_leaderboardrv);
       // mUserLevel = view.findViewById(R.id.social_cooklevel);
        mUserPoints = view.findViewById(R.id.social_points);
        mProfilePicture = view.findViewById(R.id.social_profile_pic);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        socialRecyclerView.setLayoutManager(layoutManager);

        socialAdapter = new SocialAdapter();

        //get the username
        AppDatabase db = AppDatabase.getInstance(view.getContext());
        GetUserByUsernameAsyncTask getUserByUsernameAsyncTask = new GetUserByUsernameAsyncTask();
        getUserByUsernameAsyncTask.setDatabase(db);
        getUserByUsernameAsyncTask.setDelegate(SocialFragment.this);
        getUserByUsernameAsyncTask.execute(username);


        //Get list of all users in ascending order by points
        GetAllUsersAsyncTask getAllUsersAsyncTask = new GetAllUsersAsyncTask();
        getAllUsersAsyncTask.setDatabase(db);
        getAllUsersAsyncTask.setDelegate(SocialFragment.this);
        getAllUsersAsyncTask.execute();

        return view;
    }

    @Override
    public void handleInsertUserResult(String result) {

    }

    @Override
    public void handleGetUserResult(User user) {

    }

    @Override
    public void handleGetAllUsersResult(List<User> users) {
        userList = users;
        socialAdapter.setData(userList);
        socialRecyclerView.setAdapter(socialAdapter);

    }

    @Override
    public void handleGetUsernamesResult(List<String> usernames) {

    }
    @Override
    public void handleInsertPoints(String result) {

    }

    @Override
    public void handleGetUserByUserName(User user) {

        //Display all the UI stuff here
        currentUser = user;

        mUsername.setText(currentUser.getUsername());
        mUserPoints.setText(" " + currentUser.getPoints());


        //Change profile picture depending on gender
        if(currentUser.getGender().contentEquals("Male")) {

            //Placeholder
            mProfilePicture.setImageResource(R.drawable.man);

        } else if (currentUser.getGender().contentEquals("Female")) {
            mProfilePicture.setImageResource(R.drawable.girl);
        } else {
            //for others
            mProfilePicture.setImageResource(R.drawable.defaultprofile);
        }

    }
}
