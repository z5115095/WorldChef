package com.example.worldchef.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.GetAllUsernamesAsyncTask;
import com.example.worldchef.AsyncTasks.InsertUserAsyncTask;
import com.example.worldchef.MainActivity;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AsyncTaskUserDelegate {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mUsername;
    private EditText  mPassword;
    private Button mRegister;
    private EditText mConfirmPassword;
    private Spinner mGenderSpinner;
    private String genderSelected = " ";
    private List<String> unavaiableUsernames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.register_username);
        mFirstName = findViewById(R.id.register_firstname);
        mLastName = findViewById(R.id.register_lastname);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mConfirmPassword = findViewById(R.id.register_confirmpassword);
        mRegister = findViewById(R.id.register_button);
        mGenderSpinner = findViewById(R.id.register_gender_spinner);


        //Setting up spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(spinnerAdapter);
        mGenderSpinner.setOnItemSelectedListener(this);

        //Need to make sure they enter all the necessary fields
        //Need to make sure password and confirmed password equal

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Making sure all forms have been filled:
                if(checkTextEmpty(mUsername) || checkTextEmpty(mFirstName) ||
                        checkTextEmpty(mLastName) ||checkTextEmpty(mEmail) ||
                        checkTextEmpty(mPassword) || checkTextEmpty(mConfirmPassword) || genderSelected.equals(" ")) {
                   //Display toast
                    incompleteRegister(v);

                } else {
                    AppDatabase db = AppDatabase.getInstance(RegisterActivity.this);

                    //Grab all the usernames to check if the same username doesn't already exist
                    GetAllUsernamesAsyncTask getAllUsernamesAsyncTask = new GetAllUsernamesAsyncTask();
                    getAllUsernamesAsyncTask.setDatabase(db);
                    getAllUsernamesAsyncTask.setDelegate(RegisterActivity.this);
                    getAllUsernamesAsyncTask.execute();

                }
            }
        });


        //Need to make sure no violations of primary key constraint (i.e username not the same)
    }

    public void incompleteRegister(View v) {
        Toast.makeText(RegisterActivity.this, "You must fill in all parts of the form!", Toast.LENGTH_SHORT).show();
    }

    public void userNameTakenToast() {
        Toast.makeText(RegisterActivity.this, "Sorry! Username already taken", Toast.LENGTH_SHORT).show();
    }

    public void passwordNotMatch() {
        Toast.makeText(RegisterActivity.this, "Your passwords are not matching", Toast.LENGTH_SHORT).show();
    }
    public void cleanRegister() {
        Toast.makeText(RegisterActivity.this, "Congratulations! You have registered", Toast.LENGTH_SHORT).show();
    }

    public boolean checkTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        genderSelected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void handleInsertUserResult(String result) {

        //Once user has successfully been added to user database, then this is executed

        //Display toast
        cleanRegister();
        //Switch pages

        //Move back to login page
        Intent intent = new Intent (RegisterActivity.this, MainActivity.class);

        startActivity(intent);
    }

    @Override
    public void handleGetUserResult(User user) {

    }

    @Override
    public void handleGetAllUsersResult(List<User> users) {

    }

    @Override
    public void handleGetUsernamesResult(List<String> usernames) {

        this.unavaiableUsernames = usernames;
        //If everything is filled in, check if username is not taken
        if(unavaiableUsernames.contains(mUsername.getText().toString())) {
            userNameTakenToast();
        } else {
            //If username is fine, check if both passwords are correct
            if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                AppDatabase db = AppDatabase.getInstance(RegisterActivity.this);

                //add this user to database
                InsertUserAsyncTask insertUserAsyncTask = new InsertUserAsyncTask();
                insertUserAsyncTask.setDatabase(db);
                insertUserAsyncTask.setDelegate(this);
                insertUserAsyncTask.execute(new User(mUsername.getText().toString(), mPassword.getText().toString(),
                        mFirstName.getText().toString(),
                        mLastName.getText().toString(), mEmail.getText().toString(), genderSelected, 0));

                //Once this is done, handle insertUserResult will now be triggered


            } else {
                //Display toast
                passwordNotMatch();
                System.out.println("hello");
                System.out.println("this is the username: " + mUsername.getText().toString());

                System.out.println("this is the passwords" + mConfirmPassword.getText().toString() + " " + mPassword.getText().toString());
            }

        }

    }

    @Override
    public void handleGetUserByUserName(User user) {


    }

    @Override
    public void handleInsertPoints(String result) {

    }
}
