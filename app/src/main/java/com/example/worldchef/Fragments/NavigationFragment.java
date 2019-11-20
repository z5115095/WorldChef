package com.example.worldchef.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.worldchef.Adapters.CategoryAdapter;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationFragment extends Fragment {

//Currently navigation fragment isn't even working

    public NavigationFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        BottomNavigationView bottomNav = view.findViewById(R.id.navigation_bar);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.bottom_navigation_learn:
                        selectedFragment = new LearnFragment();
                        break;
                    case R.id.bottom_navigation_social:
                        selectedFragment = new SocialFragment();
                        break;
                    case R.id.bottom_navigation_recipe:
                        selectedFragment = new FavouritesFragment();
                        break;
                }
                System.out.println("Successfully switched!");

                //Change Fragment A with whatever is clicked
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainscreen_fragmentA,
                        selectedFragment).commit();

                return true;
            }
        });


        return view;
    }




}



