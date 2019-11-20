package com.example.worldchef.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.Fragments.LearnFragment;
import com.example.worldchef.Fragments.NavigationFragment;
import com.example.worldchef.Fragments.SocialFragment;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;

public class MainScreenActivity extends AppCompatActivity {

    //This is the main screen
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        //Have learning fragment from the start
        Fragment fragmentLearn= new LearnFragment();
        swapFragmentA(fragmentLearn);

        //Have the nav bar
        Fragment fragmentNav = new NavigationFragment();
        swapFragmentB(fragmentNav);

        //Grab intent
        Intent intent = getIntent();

        username = intent.getStringExtra("username");

//        //give it to social Fragment
//        Bundle userBundle = new Bundle();
//        userBundle.putString("username", username);
//
//        Fragment socialFragment = new SocialFragment();
//        socialFragment.setArguments(userBundle);

    }

    //Swap fragment A out
    private void swapFragmentA (Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace
                (R.id.mainscreen_fragmentA, newFragment).commit();
    }
    //Swap fragment B out
    private void swapFragmentB (Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace
                (R.id.mainscreen_fragmentB, newFragment).commit();
    }

}
