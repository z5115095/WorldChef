package com.example.worldchef.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.MainActivity;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class QuizStartScreenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Quiz implementation adapted from : https://www.youtube.com/watch?v=pEDVdSUuWXE
    private Button mStartQuizButton;
    private Spinner mCategorySpinner;
    private String categorySelected = " ";
    private TextView totalPointsTxtView;
    private static final int CODE_QUIZ_RESULT = 1;
    private int mostRecentScore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start_screen);
        mStartQuizButton = findViewById(R.id.quiz_start_button);
        mCategorySpinner = findViewById(R.id.quiz_category_spinner);
        totalPointsTxtView = findViewById(R.id.quiz_start_user_score);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.Categories, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(spinnerAdapter);
        mCategorySpinner.setOnItemSelectedListener(this);

        //handing button
        mStartQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                if(!categorySelected.equals(" ")) {

                    //switch to quiz and give it the category selected
                    Intent intent = new Intent (QuizStartScreenActivity.this, QuizActivity.class);
                    intent.putExtra("category", categorySelected);

                    //
                    startActivityForResult(intent, CODE_QUIZ_RESULT);


                } else {
                    Toast.makeText(QuizStartScreenActivity.this, "Please select category first", Toast.LENGTH_SHORT).show();
                }




            }
        });

        //Insert questions if it doesn't already exist in the Quiz table
        long countOfQuestions = AppDatabase.getInstance(this).quizDao().getCountOfQuestions();

        if(countOfQuestions == 0) {

            //Insert questions
            createQuestionsDatabase();;
        }


        //Get points from user
        User currentUser = AppDatabase.getInstance(this).userDao().getUserByUsername(username);
        totalPointsTxtView.setText("Total Michelin stars: " + currentUser.getPoints());




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        categorySelected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createQuestionsDatabase(){

        AppDatabase db = AppDatabase.getInstance(this);

        //Insert questions
        //Chicken
        db.quizDao().insertQuestion(new Quiz("What is 1 + 1", "1","2","3",
                2,"Chicken"));

        db.quizDao().insertQuestion(new Quiz("What is 2 + 2", "4","2","3",
                1,"Chicken"));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_QUIZ_RESULT) {
            if(resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);

                //Add their score to their total accumulated points
                AppDatabase db = AppDatabase.getInstance(this);
                User currentUser = db.userDao().getUserByUsername(username);
                //Insert new points
                AppDatabase.getInstance(this).userDao().addPoints(score,currentUser.getPoints(),
                        currentUser.getUsername());

                System.out.println(score);

                //update aggregate
                totalPointsTxtView.setText("Total Michelin stars: " + (currentUser.getPoints() + score));

                //Display toast if they've just reached 5 points and have unlocked goat category
                if(currentUser.getPoints() < 5 && (currentUser.getPoints() + score) >=5) {
                    Toast.makeText(QuizStartScreenActivity.this,"Congratulations you have unlocked the Goat Category!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
