package com.example.worldchef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldchef.Activities.MainScreenActivity;
import com.example.worldchef.Activities.RegisterActivity;
import com.example.worldchef.Activities.SplashScreenActivity;
import com.example.worldchef.AsyncTasks.GetAllUsernamesAsyncTask;
import com.example.worldchef.AsyncTasks.GetUserByUsernameAsyncTask;
import com.example.worldchef.Models.User;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskUserDelegate {


    // Is Adam

    private Button loginButton;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private List<String> existingUsernames;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.activity_main_login_button);
        mUsername = findViewById(R.id.login_username);
        mPassword = findViewById(R.id.login_password);
        mRegister = findViewById(R.id.login_register_click);


        //Clicking login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 AppDatabase db = AppDatabase.getInstance(MainActivity.this);


                //Check if username exists by grabbing all existing usernames:
                //Grab all the usernames to check if the same username doesn't already exist
                GetAllUsernamesAsyncTask getAllUsernamesAsyncTask = new GetAllUsernamesAsyncTask();
                getAllUsernamesAsyncTask.setDatabase(db);
                getAllUsernamesAsyncTask.setDelegate(MainActivity.this);
                getAllUsernamesAsyncTask.execute();


            //this is Lucas making a comment
            }
        });

        //Clicking register button
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch pages to register page
                Intent intent = new Intent (MainActivity.this, RegisterActivity.class);

                startActivity(intent);

            }
        });


    }

    public void usernameNotExist() {
        Toast.makeText(MainActivity.this, "Username does not exist.", Toast.LENGTH_SHORT).show();
    }

    public void passwordIncorrect() {
        Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleInsertUserResult(String result) {

    }

    @Override
    public void handleGetUserResult(User user) {

    }

    @Override
    public void handleGetAllUsersResult(List<User> users) {

    }

    @Override
    public void handleGetUsernamesResult(List<String> usernames) {

        existingUsernames = usernames;
        if(existingUsernames.contains(mUsername.getText().toString())) {

            //If there does exist a specific user, then grab that user. Otherwise, it doesn't exist and display toast
            AppDatabase db = AppDatabase.getInstance(MainActivity.this);

            //Grab the specific user object
            GetUserByUsernameAsyncTask getUserByUsernameAsyncTask = new GetUserByUsernameAsyncTask();
            getUserByUsernameAsyncTask.setDatabase(db);
            getUserByUsernameAsyncTask.setDelegate(this);
            getUserByUsernameAsyncTask.execute(mUsername.getText().toString());


        } else {
            //Display toast
            usernameNotExist();
        }

    }

    @Override
    public void handleGetUserByUserName(User user) {

        currentUser = user;
        //Now that we have a ccorrect username, let's check if the password is correct
        //Check if password matches:
        if(currentUser.getPassword().equals(mPassword.getText().toString())) {
            //go to login page
            //Switch pages
            Intent intent = new Intent (MainActivity.this, SplashScreenActivity.class);
            intent.putExtra("username", currentUser.getUsername());

            //Add something here when we get User ID
            startActivity(intent);
        } else {
            //display toast
            passwordIncorrect();
        };


    }

    @Override
    public void handleInsertPoints(String result) {

    }
}
