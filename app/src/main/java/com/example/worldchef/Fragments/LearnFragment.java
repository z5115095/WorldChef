package com.example.worldchef.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worldchef.Activities.QuizStartScreenActivity;
import com.example.worldchef.Adapters.CategoryAdapter;
import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.GetAllCategoriesAsyncTask;
import com.example.worldchef.Models.Categories;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskCategoryDelegate;

import java.util.List;

//Search bar adapted from: https://www.youtube.com/watch?v=sJ-Z9G0SDhc

public class LearnFragment extends Fragment implements AsyncTaskCategoryDelegate {

    private RecyclerView categoryRecyclerView;
    private SearchView categorySearchView;
    private ImageView mQuizImage;
    private  CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_learn, container, false);

        categoryRecyclerView = view.findViewById(R.id.learn_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        categoryRecyclerView.setLayoutManager(layoutManager);

        mQuizImage = view.findViewById(R.id.learn_quiz_image);


        categoryAdapter = new CategoryAdapter();

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
        AppDatabase db = AppDatabase.getInstance(view.getContext());
        GetAllCategoriesAsyncTask getAllCategoriesAsyncTask = new GetAllCategoriesAsyncTask();
        getAllCategoriesAsyncTask.setDatabase(db);
        getAllCategoriesAsyncTask.setDelegate(LearnFragment.this);
        getAllCategoriesAsyncTask.execute();

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

        //Extract list of categories that was derived from the splash screen
        AppDatabase db = AppDatabase.getInstance(context);
        GetAllCategoriesAsyncTask getAllCategoriesAsyncTask = new GetAllCategoriesAsyncTask();
        getAllCategoriesAsyncTask.setDatabase(db);
        getAllCategoriesAsyncTask.setDelegate(LearnFragment.this);
        getAllCategoriesAsyncTask.execute();

    }

    @Override
    public void handleGetAllCategoriesTask(List<Categories.Category> categories) {

        //Load the list of categories onto recycler view
        List<Categories.Category> categoryListDatabase = categories;
        categoryAdapter.setData(categoryListDatabase);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void handleGetCategoryByNameTask(Categories.Category category) {

    }

    @Override
    public void handleInsertCategoryListTask(String result) {

    }
}
