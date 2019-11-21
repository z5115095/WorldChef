package com.example.worldchef.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldchef.AppDatabase;
import com.example.worldchef.AsyncTasks.GetQuestionsByCategoryAsyncTask;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskQuizDelegate;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class QuizActivity extends AppCompatActivity implements AsyncTaskQuizDelegate {

    //Quiz implementation adapted from https://www.youtube.com/watch?v=tlgrX3HF6AI
    //Reference back to quiz start screen adapted from: https://www.youtube.com/watch?v=y1FxIOFuIAs
    //countdown timer adapted from: https://www.youtube.com/watch?v=bLUXfWkZMD8

    private TextView questionTxtView;
    private TextView scoreTxtView;
    private TextView questionCountdown;
    private TextView questionCountTxtView;
    private RadioGroup questionRadioGroup;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private Button confirmButton;
    private ColorStateList defaultColourRb;
    private List<Quiz> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Quiz currentQuestion;
    private int score;
    private boolean answered;
    public static final String EXTRA_SCORE = "extraScore";

    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private long countdownTimeLeftMillis;
    private ColorStateList defaultColourCountdown;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Connecting with layout
        questionTxtView = findViewById(R.id.quiz_questions);
        scoreTxtView = findViewById(R.id.quiz_score);
        questionCountdown = findViewById(R.id.quiz_countdown);
        questionCountTxtView = findViewById(R.id.quiz_questionCount);
        questionRadioGroup = findViewById(R.id.quiz_rg);
        option1 = findViewById(R.id.quiz_rb1);
        option2 = findViewById(R.id.quiz_rb2);
        option3 = findViewById(R.id.quiz_rb3);
        confirmButton = findViewById(R.id.quiz_confirm_btn);

        //Set the default colour
        defaultColourRb = option1.getTextColors();
        defaultColourCountdown = questionCountdown.getTextColors();

        //Get the category chosen by user
        Intent explicitIntent = getIntent();
        String category = explicitIntent.getStringExtra("category");
        System.out.println(category);

        //Grab list of questions by category
        AppDatabase db = AppDatabase.getInstance(this);
        GetQuestionsByCategoryAsyncTask getQuestionsByCategoryAsyncTask = new GetQuestionsByCategoryAsyncTask();
        getQuestionsByCategoryAsyncTask.setDatabase(db);
        getQuestionsByCategoryAsyncTask.setDelegate(QuizActivity.this);
        getQuestionsByCategoryAsyncTask.execute(category);



        //Clicking submit answer
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    //Check if radio button is selected
                    if (option1.isChecked() || option2.isChecked() || option3.isChecked()) {

                        //See if answer is correct
                        markAnswer();

                    } else {
                        Toast.makeText(QuizActivity.this, "Please choose an answer first!",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    displayNextQuestion();
                }
            }
        });



    }


    private void displayNextQuestion() {

        //set options as default
        option1.setTextColor(defaultColourRb);
        option2.setTextColor(defaultColourRb);
        option3.setTextColor(defaultColourRb);

        //Unselect the option for the next question
        questionRadioGroup.clearCheck();

        //only switch questions if there are any left
        if (questionCounter < questionCountTotal) {


            //set the question based on the question#
            currentQuestion = questionList.get(questionCounter);

            //Set everything for this question
            questionTxtView.setText(currentQuestion.getQuestion());
            option1.setText(currentQuestion.getOption1());
            option2.setText(currentQuestion.getOption2());
            option3.setText(currentQuestion.getOption3());

            //Increment question
            questionCounter++;

            questionCountTxtView.setText("Question: " + questionCounter + "/" + questionCountTotal);

            answered = false;

            //restart countdown
            countdownTimeLeftMillis = COUNTDOWN_IN_MILLIS;

            startCountdown();


        } else {


            //Display toast
            Toast.makeText(this,"Congratulations, you've just earned " + score + " new Michelin stars!",
                    Toast.LENGTH_SHORT).show();

            //Finish quiz after last question

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCORE, score);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(countdownTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                countdownTimeLeftMillis = millisUntilFinished;

                //update the countdown text
                changeCountdownText();

            }

            @Override
            public void onFinish() {
                //reset
                countdownTimeLeftMillis = 0;
                changeCountdownText();
                markAnswer();



            }
        }.start();
    }

    private void changeCountdownText() {
        int minutes = (int) (countdownTimeLeftMillis/1000) / 60;
        int seconds = (int) (countdownTimeLeftMillis/1000) % 60;
        String time = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        questionCountdown.setText(time);

        if(countdownTimeLeftMillis < 5000) {
            questionCountdown.setTextColor(Color.RED);
        } else{
            questionCountdown.setTextColor(defaultColourCountdown);
        }
    }

    private void markAnswer() {
        answered = true;

        countDownTimer.cancel();
        //Return the id with whatever Radio button is checked
        RadioButton optionSelected = findViewById(questionRadioGroup.getCheckedRadioButtonId());

        //Convert button into answer number which we then compare to our database
        int answerNumberChosen = questionRadioGroup.indexOfChild(optionSelected) + 1;

        if (answerNumberChosen == currentQuestion.getAnswerNumber()) {
            score++;
            scoreTxtView.setText("Score : " + score);
        }


        //Display solution
        displaySolution();
    }

    private void displaySolution() {

        //Set them all to wrong
        option1.setTextColor(Color.RED);
        option2.setTextColor(Color.RED);
        option3.setTextColor(Color.RED);

        if (currentQuestion.getAnswerNumber() == 1) {
            option1.setTextColor(Color.GREEN);
            questionTxtView.setText("Option 1 is correct!");
        } else if (currentQuestion.getAnswerNumber() == 2) {
            option2.setTextColor(Color.GREEN);
            questionTxtView.setText("Option 2 is correct!");
        } else {
            option3.setTextColor(Color.GREEN);
            questionTxtView.setText("Option 3 is correct!");
        }

        //Check if this is the last question or not
        if(questionCounter < questionCountTotal) {
            confirmButton.setText("Next question");
        } else {
            confirmButton.setText("Finish quiz");
        }


    }

    @Override
    protected void onDestroy() {

        //Cancel countdown timer when activity is finished
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }


    @Override
    public void handleInsertQuestionTask(String result) {

    }

    @Override
    public void handleGetQuestionCountTask(long count) {

    }

    @Override
    public void handleGetQuestionTask(List<Quiz> questions) {

        questionList = questions;
        questionCountTotal = questionList.size();

        //Randomise order of the questions
        Collections.shuffle(questionList);

        //Call next question
        displayNextQuestion();

    }
}
