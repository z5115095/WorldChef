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
import com.example.worldchef.MainActivity;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mUsername;
    private EditText  mPassword;
    private Button mRegister;
    private EditText mConfirmPassword;
    private Spinner mGenderSpinner;
    private String genderSelected = " ";

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

                    //If everything is filled in, check if username is not taken
                    List<String> unAvailableUsernames = AppDatabase.getInstance(RegisterActivity.this).userDao().getUsernames();
                    if(unAvailableUsernames.contains(mUsername.getText().toString())) {
                        userNameTakenToast(v);
                    } else {
                        //If username is fine, check if both passwords are correct
                        if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                            //add to database

                            AppDatabase.getInstance(RegisterActivity.this).userDao().
                                    insertUser(new User(mUsername.getText().toString(), mPassword.getText().toString(),
                                            mFirstName.getText().toString(),
                                            mLastName.getText().toString(), mEmail.getText().toString(), genderSelected, 0));


                            //Display toast
                            cleanRegister(v);
                            //Switch pages

                            //Move back to login page
                            Intent intent = new Intent (RegisterActivity.this, MainActivity.class);

                            startActivity(intent);

                        } else {
                            //Display toast
                            passwordNotMatch(v);
                            System.out.println("hello");
                            System.out.println("this is the username: " + mUsername.getText().toString());

                            System.out.println("this is the passwords" + mConfirmPassword.getText().toString() + " " + mPassword.getText().toString());
                        }

                    }

                }
            }
        });


        //Need to make sure no violations of primary key constraint (i.e username not the same)
    }

    public void incompleteRegister(View v) {
        Toast.makeText(RegisterActivity.this, "You must fill in all parts of the form!", Toast.LENGTH_SHORT).show();
    }

    public void userNameTakenToast(View v) {
        Toast.makeText(RegisterActivity.this, "Sorry! Username already taken", Toast.LENGTH_SHORT).show();
    }

    public void passwordNotMatch(View v) {
        Toast.makeText(RegisterActivity.this, "Your passwords are not matching", Toast.LENGTH_SHORT).show();
    }
    public void cleanRegister(View v) {
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
}
