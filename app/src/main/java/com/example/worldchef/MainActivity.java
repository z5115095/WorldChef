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
import com.example.worldchef.Models.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    // Is Adam

    private Button loginButton;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;

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

                //Get username and make sure password matches
                AppDatabase.getInstance(MainActivity.this).userDao().getUsernames();

                //Check if username exists:
                List<String> existingUsernames = AppDatabase.getInstance(MainActivity.this).userDao().getUsernames();
                if(existingUsernames.contains(mUsername.getText().toString())) {

                    //Grab the specific user object
                    User currentUser = AppDatabase.getInstance(MainActivity.this).userDao().getUserByUsername(mUsername.getText().toString());

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
                        passwordIncorrect(v);
                    };


                } else {
                    //Display toast
                    usernameNotExist(v);
                }





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

    public void usernameNotExist(View v) {
        Toast.makeText(MainActivity.this, "Username does not exist.", Toast.LENGTH_SHORT).show();
    }

    public void passwordIncorrect(View v) {
        Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
    }
}
