package com.example.worldchef.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
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
import com.example.worldchef.Activities.QuizStartScreenActivity;
import com.example.worldchef.Activities.RegisterActivity;
import com.example.worldchef.Adapters.CategoryAdapter;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.MainActivity;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.worldchef.R;

public class LearnFragment extends Fragment {

    private RecyclerView categoryRecyclerView;
    private SearchView categorySearchView;
    private ImageView mQuizImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_learn_fragment, container, false);

        categoryRecyclerView = view.findViewById(R.id.learn_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        categoryRecyclerView.setLayoutManager(layoutManager);

        mQuizImage = view.findViewById(R.id.learn_quiz_image);


        final CategoryAdapter categoryAdapter = new CategoryAdapter();

        //Setting search bar
        categorySearchView = view.findViewById(R.id.search_bar);
        categorySearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        categorySearchView.setQueryHint("Search food category");

        //Prevent the keyboard from pushing up the entire layout
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        categorySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                categoryAdapter.getFilter().filter(newText);
                return false;
            }
        });

        //Extract list of categories that was derived from the splash screen
                List<Categories.Category> categoryListDatabase = AppDatabase.getInstance(view.getContext()).categoryDao().getCategories();
                categoryAdapter.setData(categoryListDatabase);
                categoryRecyclerView.setAdapter(categoryAdapter);



        //Click on image to go to quiz page
        mQuizImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Move to quiz page
                Context context = getContext();
                Intent intent = new Intent (context, QuizStartScreenActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    //When back button is pressed back, update this fragment
    @Override
    public void  onResume() {
        super.onResume();

        Context context = getContext();

        final CategoryAdapter categoryAdapter = new CategoryAdapter();
        //Extract list of categories that was derived from the splash screen
        List<Categories.Category> categoryListDatabase = AppDatabase.getInstance(context).categoryDao().getCategories();
        categoryAdapter.setData(categoryListDatabase);
        categoryRecyclerView.setAdapter(categoryAdapter);

    }

}
