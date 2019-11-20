package com.example.worldchef.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worldchef.Adapters.SocialAdapter;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;

import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class SocialFragment extends Fragment {

    private TextView mUsername;
    private RecyclerView socialRecyclerView;
    private TextView mUserLevel;
    private TextView mUserPoints;
    private Button mAddPointsButton;
    private ImageView mProfilePicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.social_fragment, container, false);

        mUsername = view.findViewById(R.id.social_username);
        socialRecyclerView = view.findViewById(R.id.social_leaderboardrv);
        mUserLevel = view.findViewById(R.id.social_cooklevel);
        mUserPoints = view.findViewById(R.id.social_points);
        mProfilePicture = view.findViewById(R.id.social_profile_pic);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        socialRecyclerView.setLayoutManager(layoutManager);

        final SocialAdapter socialAdapter = new SocialAdapter();

        //Extract bundle
        //String username = getArguments().getString("username");
        AppDatabase db = AppDatabase.getInstance(view.getContext());
        final User currentUser = db.userDao().getUserByUsername(username);

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


        //Grab data from database:

        List<User> userList = AppDatabase.getInstance(view.getContext()).userDao().getAllUsers();
        socialAdapter.setData(userList);
        socialRecyclerView.setAdapter(socialAdapter);

//        mAddPointsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Add points as a test then refresh
//                AppDatabase.getInstance(view.getContext()).userDao().addPoints(50,
//                        currentUser.getPoints(),currentUser.getUsername());
//
//
//            }
//
//            });



        return view;
    }
}
