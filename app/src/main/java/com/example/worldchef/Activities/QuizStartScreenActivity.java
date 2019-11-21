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
import com.example.worldchef.AsyncTasks.GetQuestionCountAsyncTask;
import com.example.worldchef.AsyncTasks.GetUserByUsernameAsyncTask;
import com.example.worldchef.AsyncTasks.InsertPointsAsyncTask;
import com.example.worldchef.AsyncTasks.InsertQuestionsAsyncTask;
import com.example.worldchef.Models.Quiz;
import com.example.worldchef.Models.User;
import com.example.worldchef.R;
import com.example.worldchef.TaskDelegates.AsyncTaskQuizDelegate;
import com.example.worldchef.TaskDelegates.AsyncTaskUserDelegate;

import java.util.ArrayList;
import java.util.List;

import static com.example.worldchef.Activities.MainScreenActivity.username;

public class QuizStartScreenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AsyncTaskQuizDelegate, AsyncTaskUserDelegate {

    //Quiz implementation adapted from : https://www.youtube.com/watch?v=pEDVdSUuWXE
    private Button mStartQuizButton;
    private Spinner mCategorySpinner;
    private String categorySelected = " ";
    private TextView totalPointsTxtView;
    private static final int CODE_QUIZ_RESULT = 1;
    private User currentUser;
    private int score;

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



        AppDatabase db = AppDatabase.getInstance(QuizStartScreenActivity.this);
        //Insert questions if it doesn't already exist in the Quiz table
        GetQuestionCountAsyncTask getQuestionCountAsyncTask = new GetQuestionCountAsyncTask();
        getQuestionCountAsyncTask.setDatabase(db);
        getQuestionCountAsyncTask.setDelegate(QuizStartScreenActivity.this);
        getQuestionCountAsyncTask.execute();



        //Get points from user
        GetUserByUsernameAsyncTask getUserByUsernameAsyncTask = new GetUserByUsernameAsyncTask();
        getUserByUsernameAsyncTask.setDatabase(db);
        getUserByUsernameAsyncTask.setDelegate(QuizStartScreenActivity.this);
        getUserByUsernameAsyncTask.execute(username);

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
        InsertQuestionsAsyncTask insertQuestionAsyncTask = new InsertQuestionsAsyncTask();
        insertQuestionAsyncTask.setDatabase(db);
        insertQuestionAsyncTask.setDelegate(QuizStartScreenActivity.this);

        //chicken
        ArrayList<Quiz> questions = new ArrayList<>();
//        questions.add(new Quiz("From inside to out, what is the correct composition of a beef wellington?", "Meat, mushroom duxelle, prosciutto, pastry","Meat, mushroom duxelle, pastry, prosciutto","Meat, pastry, eggwash, salt",
//                1,"Beef"));
//
//        questions.add(new Quiz("When preparing a beef roast, how long should the meat be left out of the fridge to come up to temperature?", "5 minutes","10 minutes","30 minutes",
//                3,"Beef"));
//
//        questions.add(new Quiz("When cooking a beef roast, are the vegetables placed above or below the meat?", "Mushroom","Paprika","Chives",
//                3,"Beef"));
//
//        questions.add(new Quiz("Which ingredient is not required in a beef stroganoff?", "Above","Below","They are cooked separately",
//                2,"Beef"));
//
//        questions.add(new Quiz("For how long should you cook your mushrooms when making a duxelle for beef wellington?", "2 minutes","Until golden brown","Until there is no excess moisture",
//                3,"Beef"));
//
//        questions.add(new Quiz("A beef bourguignon can most accurately be described as a? ", "Beef stew","Steak","Meatloaf",
//                1,"Beef"));
//
//        questions.add(new Quiz("What temperature should a medium steak be cooked to (in degrees Celsius)? ", "56","63","71",
//                3,"Beef"));
//
//        questions.add(new Quiz("What is the defining characteristic of a steak diane? ", "Served with sauce made from pan juices","Extremely rare","Cut of meat must have a marbling score of at least 4+",
//                1,"Beef"));
//
//        questions.add(new Quiz("Which steak temperature is the most rare? ", "Medium","Medium-rare","Blue",
//                3,"Beef"));
//
//        questions.add(new Quiz("What is the correct definition of a 'blue rare' steak?", "Cooked on the outside with a red centre","Seared on the outside and completely red throughout","Completely raw",
//                2,"Beef"));
//
//        questions.add(new Quiz("Kobe beef is a type of steak that originates from which country?", "America","Korea","Japan",
//                3,"Beef"));

        questions.add(new Quiz("What type of dish is szechuan beef?", "Rich and mild Thai curry","Midly spicy Indian stew","Spicy Chinese stir fry",
                3,"Beef"));

        questions.add(new Quiz("The most tender cut of beef that you can buy is a?", "NY Strip","Filet Mignon","Poterhouse",
                2,"Beef"));

        questions.add(new Quiz("What is not used to make the rub for Montreal smoked meat?", "Crushed herbs","Coriander","Garlic powder",
                1,"Beef"));

        questions.add(new Quiz("The most tender cut of beef is a filet mignon. From which cut of meat is a filet mignon obtained?", "Porterhouse","Ribeye","Flank",
                1,"Beef"));

        questions.add(new Quiz("What temperature should be used to cook Montreal smoked meat (in degrees Celsius)?", "150","225","275",
                2,"Beef"));

        questions.add(new Quiz("What is the best way to cook a T-bone steak?", "Pan fry","Reverse sear","Broil",
                3,"Beef"));

        questions.add(new Quiz("In a cut of beef, what does 'marbling' refer to?", "Intramuscular fat","Fat content","The diet fed to the cattle",
                1,"Beef"));

        questions.add(new Quiz("What type of beef is recommended for use in spaghetti bolognese?", "Mince","Chuck","Flank",
                1,"Beef"));

        questions.add(new Quiz("For how long can beef be safely frozen?", "2-4 weeks","1-3 months","6-12 months",
                3,"Beef"));

        questions.add(new Quiz("For how long should pate chinois be baked?", "30 minutes","60 minutes","90 minutes",
                1,"Beef"));

        //Chicken
//        questions.add(new Quiz("In the chicken and mushroom hotpot, roux is used. What is roux?", "A sauce thickener made from flour","An extract made from chicken stock","A seasoning agent",
//                1,"Chicken"));
//
//        questions.add(new Quiz("Before the chicken and mushroom hotpot is baked in the oven, what are the potatoes brushed with?", "Egg","Butter","Water",
//                2,"Chicken"));
//
//        questions.add(new Quiz("What is used to cook the chicken in chicken alfredo primavera?", "Pot","Deep fryer","Skillet",
//                3,"Chicken"));
//
//        questions.add(new Quiz("What is the correct marinade used in chicken congee?", "Lemon, pepper and salt","Chicken salt, pepper and ginger juice","Worcestershire sauce, garlic, salt",
//                2,"Chicken"));
//
//        questions.add(new Quiz("What is the recommended method to thin chicken congee if it is too thick?", "Put it on high heat","Let it cool","Add water",
//                3,"Chicken"));
//
//        questions.add(new Quiz("How long should you cook the chicken in chicken couscous?", "3-5 minutes","7-10 minutes","10-12 minutes",
//                2,"Chicken"));
//
//        questions.add(new Quiz("After cooking, how long should the chicken couscous be left to rest?", "2 minutes","5 minutes","10 minutes",
//                2,"Chicken"));
//
//        questions.add(new Quiz("Why is the chicken couscous left to cool after cooking?", "To soften and soak up the stock","To cool down to an edible temperature","To allow the semolina structures to reform",
//                1,"Chicken"));
//
//        questions.add(new Quiz("Where does the dish chicken fajita mac and cheese originate?", "Spain","America","Cuba",
//                2,"Chicken"));
//
//        questions.add(new Quiz("Should chicken be washed before used in cooking?", "Yes - it removes surface bacteria","Yes - only if there is visible blood","No - it can spread the germs on the chicken",
//                3,"Chicken"));
//
//        questions.add(new Quiz("For how long can chicken be safely frozen?", "Up to 3 months","Up to 6 months","Up to 12 months",
//                1,"Chicken"));

        questions.add(new Quiz("Is it safe to eat chicken that is pink?", "Yes - there is no harm in eating pink chicken","No - there are harmful germs and bacteria still present","Maybe - depends on the temperature of the meat",
                3,"Chicken"));

        questions.add(new Quiz("What temperature must chicken be at to safely eat (in degrees Celsius)?", "74","79","85",
                1,"Chicken"));

        questions.add(new Quiz("From which country does chicken, ham and leek pie originate?", "England","America","Australia",
                1,"Chicken"));

        questions.add(new Quiz("What type of dish most accurately describes chicken parmentier?", "A chicken stew","A chicken casserole with a potato lid","A lightly crumbed chicken schnitzel",
                2,"Chicken"));

        questions.add(new Quiz("From which country does chicken parmentier originate?", "Australia","America","France",
                3,"Chicken"));

        questions.add(new Quiz("The dish coq au vin requires wine. Which type of wine is recommended?", "Cooking wine","Red wine","White wine",
                2,"Chicken"));

        questions.add(new Quiz("From which country does the dish General Tso's chicken originate?", "Japan","China","Singapore",
                2,"Chicken"));

        questions.add(new Quiz("When preparing the marinade for jerk chicken with rice and peas, what should you do if the jerk mixture is too salty and sour?", "Add chillis","Add water","Add brown sugar",
                3,"Chicken"));

        questions.add(new Quiz("For how long should you have the coals burning before you barbeque your chicken?", "30 minutes","60 minutes","90 minutes",
                2,"Chicken"));

        questions.add(new Quiz("Rappie pie is a Canadian chicken dish which can best be described as?", "A sweet dessert","A chicken pot pie","A casserole-like potato pie",
                3,"Chicken"));

        //lamb
        questions.add(new Quiz("When making the lamb and potato pie, the recipe states to score the top of the pastry with 3 slits. Why is this necessary?", "To release steam while cooking","To make it aesthetically pleasing","To ensure the pastry doesn't crack",
                1,"Lamb"));

        questions.add(new Quiz("For how long should you cook the lamb in the dish lamb rogan josh?", "10-20 minutes","20-40 minutes","40-60 minutes",
                3,"Lamb"));

        questions.add(new Quiz("From which country does lamb rogan josh originate?", "India","America","England",
                1,"Lamb"));

        questions.add(new Quiz("From which country does rigatoni with fennel sausage sauce originate?", "Italy","France","Morocco",
                3,"Lamb"));

        questions.add(new Quiz("For how long can lamb be safely frozen?", "1-3 months","3-6 months","6-9 months",
                3,"Lamb"));

        questions.add(new Quiz("The lamb dish kapsalon can most accurately be described as?", "A lamb roast","A fast-food French fries dish","A slow-baked stew",
                2,"Lamb"));

        questions.add(new Quiz("For how long should the lamb be cooked in the Tunisian dish keleya zaara?", "5 minutes","10 minutes","20 minutes",
                1,"Lamb"));

        questions.add(new Quiz("When should you remove the onions from the fryer when making lamb biryani?", "After 5 minutes","When the onion begins to fall apart","When the onion turns a light brown",
                3,"Lamb"));

        questions.add(new Quiz("Why should you take the skillet off the heat when adding yoghurt in lamb biryani?", "To prevent the yoghurt from curdling","To prevent the yoghurt from going sour","To prevent the yoghurt from melting",
                1,"Lamb"));

        questions.add(new Quiz("What other meat is required in the dish Tunisian lamb soup?", "Beef","Chicken","Pork",
                2,"Lamb"));

        //Pasta
        questions.add(new Quiz("How long should you cook the prawns in chilli prawn linguine?", "About 3 minutes","Until they turn pink","Both answers are correct",
                3,"Pasta"));

        questions.add(new Quiz("Which side dishes are recommended to accompany the chilli prawn linguine?", "Coleslaw and bread","Salad and bread","Garlic bread and fries",
                2,"Pasta"));

        questions.add(new Quiz("What ingredient is not required in fettucine alfredo?", "Pepper","Nutmeg","Basil",
                3,"Pasta"));

        questions.add(new Quiz("When making fettucine alfredo, it is important to collect some of the pasta water. What is its purpose?", "To serve alongside the pasta","To reuse when cooking the next batch of pastsa","To thin the sauce and make it glossy",
                3,"Pasta"));

        questions.add(new Quiz("How much of the pasta water should be added to fettucini alfredo?", "3 tablespoons","1 cup","2 cups",
                1,"Pasta"));

        questions.add(new Quiz("How much salt should be added to pasta water?", "2 teaspoons","1 tablespoon","Until it 'tastes like the ocean'",
                3,"Pasta"));

        questions.add(new Quiz("What is al dente pasta?", "Raw pasta","Pasta that is cooked but firm","A long, thin pasta noodle",
                2,"Pasta"));

        questions.add(new Quiz("For how long should the duck be cooked when making Venetian duck ragu?", "5 minutes","10 minutes","20 minutes",
                2,"Pasta"));

        questions.add(new Quiz("From which country does lasagna originate?", "Italy","America","Spain",
                1,"Pasta"));

        questions.add(new Quiz("What should be added to the tomato puree in lasagna if it is not sweet enough?", "Sugar","Honey","Tomatoes",
                2,"Pasta"));

        //Pork
        questions.add(new Quiz("What gives the pork dish 'bubble and squeak' its name?", "The sound it makes when you bite into it","The potato that sticks in the pan when cooking","The nickname of the creator of the dish",
                2,"Pork"));

        questions.add(new Quiz("What type of pork is used in the dish bubble and squeak?", "Ham","Pork loin","Bacon",
                3,"Pork"));

        questions.add(new Quiz("From which country does bubble and squeak originate?", "America","England","Germany",
                2,"Pork"));

        questions.add(new Quiz("What type of stock should be added to hot and sour soup?", "Pork","Beef","Chicken",
                3,"Pork"));

        questions.add(new Quiz("For how long should you boil the potatoes when making stamppot?", "5 minutes","10 minutes","20 minutes",
                3,"Pork"));

        questions.add(new Quiz("Stamppot is a pork dish, but what type of pork product does it contain?", "Bacon","Sausage","Short rib",
                2,"Pork"));

        questions.add(new Quiz("How many eggs do you need to make the dish toad in a hole?", "2","3","5",
                1,"Pork"));

        questions.add(new Quiz("How many wontons can be cooked at the same time in a pot?", "4-5 wontons","5-10 wontons","10+ wontons",
                1,"Pork"));

        questions.add(new Quiz("From which country does tourtiere originate?", "France","Morocco","Canada",
                3,"Pork"));

        questions.add(new Quiz("The Vietnamese pork dish 'bun-thit-nuong' is also known as?", "Vietnamese grilled pork","Vietnamese spring rolls","Pork pho",
                1,"Pork"));

        //seafood
        questions.add(new Quiz("How hot should the oven be when cooking baked salmon with fennel and tomoatoes (in degrees Celsius)?", "160-180","180-200","200-220",
                1,"Seafood"));

        questions.add(new Quiz("The dish cajun spiced fish tacos is best suited to which season?", "Summer","Autumn","Spring",
                1,"Seafood"));

        questions.add(new Quiz("How long should the tortilla be heated for when making cajun spiced fish tacos?", "5-10 seconds","10-20 seconds","20-30 seconds",
                1,"Seafood"));

        questions.add(new Quiz("From which country does escovitch fish originate?", "Cuba","Spain","Jamaica",
                3,"Seafood"));

        questions.add(new Quiz("How many days in advance can the sauce be made for escovitch fish?", "2 days","4 days","1 week",
                1,"Seafood"));

        questions.add(new Quiz("The dish fish stew with rouille requires wine. Which type of wine is this?", "Red","White","Champagne",
                2,"Seafood"));

        questions.add(new Quiz("From which country does tuna nicoise originate?", "Spain","France","England",
                2,"Seafood"));

        questions.add(new Quiz("How long should you bake a three fish pie for?", "30-40 minutes","Until golden brown and bubbling around the edges","Both answers are correct",
                3,"Seafood"));

        questions.add(new Quiz("Which type of seafood is not used in seafood fideua?", "Swordfish","Monkfish","Mussels",
                1,"Seafood"));

        questions.add(new Quiz("The dish saltfish and ackee contains ackee. What most accurately describes this ingredient?", "A spongy deepwater fish","A spicy herb","A West-African fruit",
                3,"Seafood"));

        //sides
        questions.add(new Quiz("After your corba comes to a boil, for how much longer should you cook it over medium-low heat?", "5-10 minutes","10-15 minutes","15-20 minutes",
                3,"Sides"));

        questions.add(new Quiz("How long can corba be left in the fridge after cooking?", "1 day","3 days","A week",
                3,"Sides"));

        questions.add(new Quiz("After adding sugar to your French onion soup, for how much longer should you cook it?", "20 minutes","Until it is caramelised","Both answers are correct",
                3,"Sides"));

        questions.add(new Quiz("Kumpir is a Turkish side dish. Which description most accurately describes this side?", "Sweet corn","Baked potato","Fried onions",
                2,"Sides"));

        questions.add(new Quiz("Snert is a Dutch side which contains which meat?", "Pork","Chicken","Lamb",
                1,"Sides"));

        questions.add(new Quiz("From which country does split pea soup originate?", "Canada","England","Australia",
                1,"Sides"));

        questions.add(new Quiz("You must simmer your split pea soup, but for how long?", "30-90 minutes","90-150 minutes","150-210 minutes",
                2,"Sides"));

        questions.add(new Quiz("Prawn and fennel bisque requires wine in the cooking process. Which wine is correct?", "White","Red","Cooking",
                1,"Sides"));

        questions.add(new Quiz("From which country does corba originate?", "Canada","Turkey","Belgium",
                2,"Sides"));

        questions.add(new Quiz("Fennel dauphinoise is a French side served with which type of cheese?", "Gouda","Mozzarella","Parmesan",
                3,"Sides"));

        //starter
        questions.add(new Quiz("What type of oil is used to make broccoli and stilton soup", "Sunflower","Rapeseed","Peanut",
                2,"Starter"));

        questions.add(new Quiz("When making clam chowder, it is recommended to save some empty clam shells. Why?", "To help soften the potatoes","To be used as a scoop to eat the chowder","Presentation purposes",
                3,"Starter"));

        questions.add(new Quiz("How much clam stock should be used in making clam chowder?", "200ml","400ml","800ml",
                3,"Starter"));

        questions.add(new Quiz("After making creamy tomato soup, how long can it be kept for if frozen?", "Up to a week","Up to a month","Up to 3 months",
                3,"Starter"));

        questions.add(new Quiz("From which country does creamy tomato soup originate?", "France","England","Neither",
                2,"Starter"));

        questions.add(new Quiz("From which country does clam chowder originate?", "England","France","Neither",
                3,"Starter"));

        questions.add(new Quiz("A major ingredient of broccoli and stilton soup is stilton. What is it?", "A meat","A cheese","A herb",
                2,"Starter"));

        questions.add(new Quiz("From which country does broccoli and stilton soup originate?", "England","France","Neither",
                1,"Starter"));

        questions.add(new Quiz("What is recommended to be served alongside creamy tomato soup?", "French fries","Scones","Cheesy sausage rolls",
                3,"Starter"));

        questions.add(new Quiz("How many potatoes are required to make clam chowder?", "3 large potatoes","2 medium potatoes","None",
                2,"Starter"));

        //Vegan
        questions.add(new Quiz("What most accurately describes paella?", "Spanish rice","Sticky pasta","French bread",
                1,"Vegan"));

        questions.add(new Quiz("What is the telltale sign that you have achieved the classic layer of toasted rice in paella?", "Smoke rising from the pan","Hearing a slight crackle","A toasted smell",
                2,"Vegan"));

        questions.add(new Quiz("When making roast fennel and aubergine paella, is the wine added before or after toasting the rice?", "Before","After","Wine is not used",
                2,"Vegan"));

        questions.add(new Quiz("What temperature should your oven be set at to make vegan chocolate cake (in degrees Celsius)?", "180","210","240",
                1,"Vegan"));

        questions.add(new Quiz("For how long should you bake your vegan chocolate cake at 180 degrees Celsius?", "30 minutes","45 minutes","60 minutes",
                2,"Vegan"));

        questions.add(new Quiz("What type of milk is used to make vegan chocolate cake?", "Coconut","Pistachio","Almond",
                3,"Vegan"));

        questions.add(new Quiz("From which country does vegan lasagna originate?", "Italy","America","England",
                1,"Vegan"));

        questions.add(new Quiz("What type of meat is used in vegan lasagna?", "Beef","Chicken","Neither",
                3,"Vegan"));

        questions.add(new Quiz("How many eggs are required to make vegan chocolate cake?", "3 medium eggs","1 large free-range egg","None",
                3,"Vegan"));

        questions.add(new Quiz("From which country does vegan chocolate cake originate?", "America","England","Germany",
                1,"Vegan"));

        //Vegetarian
        questions.add(new Quiz("You are required to wilt the spinach when making spinach and ricotta cannelloni. Which method is recommended?", "Put it in the microwave","Place it in a pot and bring it to boil","Place it in a colander and pour boiling water over it",
                3,"Vegetarian"));

        questions.add(new Quiz("For how long can Shakshuka be frozen for after cooking?", "1 month","2 months","Cannot be frozen",
                1,"Vegetarian"));

        questions.add(new Quiz("From which country does shakshuka originate?", "Spain","Egypt","New Zealand",
                2,"Vegetarian"));

        questions.add(new Quiz("How many cloves of garlic are required to make Spanish tortilla?", "2 cloves","4 cloves","8 cloves",
                3,"Vegetarian"));

        questions.add(new Quiz("What is recommended to accompany the Spanish tortilla?", "Melted butter","Warmed baguette","Roasted potatoes",
                2,"Vegetarian"));

        questions.add(new Quiz("From which country does spinach and ricotta cannelloni originate?", "France","Spain","Neither",
                3,"Vegetarian"));

        questions.add(new Quiz("Yaki udon is a dish which requires which oil to make?", "Canola","Vegetable","Sesame seed",
                3,"Vegetarian"));

        questions.add(new Quiz("How many carrots are required to make vegetarian casserole?", "1","2","3",
                3,"Vegetarian"));

        questions.add(new Quiz("Summer pistou is recommended to be consumed within how many days of cooking?", "1 day","2 days","A week",
                2,"Vegetarian"));

        questions.add(new Quiz("From which country does Tahini lentils originate?", "Morocco","Egypt","Madagascar",
                1,"Vegetarian"));

        //Breakfast
        questions.add(new Quiz("What should you do to your bacon before making breakfast potatoes?", "Wash it","Slice it","Freeze it",
                3,"Breakfast"));

        questions.add(new Quiz("When making breakfast potatoes, you do not want your potatoes to brown after being cut. How do you accomplish this?", "Put them in a bowl of water","Put them in the fridge","Don't peel the potatoes before cutting",
                1,"Breakfast"));

        questions.add(new Quiz("What is the first thing that should be cooked in an English breakfast?", "Eggs","Sausages","Mushrooms",
                2,"Breakfast"));

        questions.add(new Quiz("From which country does fruit and cream cheese breakfast pastries originate?", "England","France","America",
                3,"Breakfast"));

        questions.add(new Quiz("Which fruit is not used to make fruit and cream cheese breakfast pastries?", "Raspberries","Strawberries","Blueberries",
                3,"Breakfast"));

        questions.add(new Quiz("What type of flour is required to make mandazi?", "Strong flour","All-purpose flour","Self-raising flour",
                3,"Breakfast"));

        questions.add(new Quiz("From which country does mandazi originate?", "Cuba","Kenya","Mongolia",
                2,"Breakfast"));

        questions.add(new Quiz("The dish salmon eggs benedict requires a hollandaise sauce. What is the primary ingredient in this sauce?", "Egg","Cream","Pepper",
                1,"Breakfast"));

        questions.add(new Quiz("How many eggs are required to make smoked haddock kedgeree?", "1","3","None",
                2,"Breakfast"));

        questions.add(new Quiz("What type of bread is best suited to make fried bread in a full English breakfast?", "Rye","White","Bread that is a couple days old",
                3,"Breakfast"));



        Quiz[] quizzArray = questions.toArray(new Quiz[questions.size()]);


        insertQuestionAsyncTask.execute(quizzArray);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == CODE_QUIZ_RESULT) {
            if(resultCode == RESULT_OK) {
                score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                System.out.println("this is the score from your latest game: " + score);

                //Add their score to their total accumulated points
                //Insert new points
                AppDatabase db = AppDatabase.getInstance(QuizStartScreenActivity.this);
                InsertPointsAsyncTask insertPointsAsyncTask = new InsertPointsAsyncTask();
                insertPointsAsyncTask.setDatabase(db);
                insertPointsAsyncTask.setDelegate(QuizStartScreenActivity.this);
                insertPointsAsyncTask.execute(score, currentUser.getPoints(),currentUser.getUsername());

            }
        }
    }

    @Override
    public void handleInsertQuestionTask(String result) {
        System.out.println(result);
    }

    @Override
    public void handleGetQuestionCountTask(long count) {

        long countOfQuestions = count;
        System.out.println("number of questions that exist: " + countOfQuestions);

        if(countOfQuestions == 0) {

            //Insert questions if it doesn't exist
            createQuestionsDatabase();

        }

    }

    @Override
    public void handleGetQuestionTask(List<Quiz> questions) {

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

    }

    @Override
    public void handleGetUserByUserName(User user) {

        currentUser = user;
        totalPointsTxtView.setText("Total Michelin stars: " + currentUser.getPoints());

    }

    @Override
    public void handleInsertPoints(String result) {

        //We need to update score by updating user
        AppDatabase db = AppDatabase.getInstance(QuizStartScreenActivity.this);
        GetUserByUsernameAsyncTask getUserByUsernameAsyncTask = new GetUserByUsernameAsyncTask();
        getUserByUsernameAsyncTask.setDatabase(db);
        getUserByUsernameAsyncTask.setDelegate(QuizStartScreenActivity.this);
        getUserByUsernameAsyncTask.execute(username);

        System.out.println("This is how much points you have after getting more " + currentUser.getPoints());
        System.out.println("this is the value of the int score " + score);
        //update aggregate
        //Display toast if they've just reached 5 points and have unlocked goat category
        if(currentUser.getPoints() < 10 && (currentUser.getPoints() + score) >=10) {
            Toast.makeText(QuizStartScreenActivity.this,"Congratulations you have unlocked the Goat Category!",Toast.LENGTH_SHORT).show();
        } else if (currentUser.getPoints() < 20 && (currentUser.getPoints() + score) >=20) {
            Toast.makeText(QuizStartScreenActivity.this,"Congratulations you have unlocked the Dessert Category!",Toast.LENGTH_SHORT).show();
        } else if (currentUser.getPoints() < 30 && (currentUser.getPoints() + score) >=30) {
            Toast.makeText(QuizStartScreenActivity.this,"Congratulations you have unlocked the Miscellaneous Category!",Toast.LENGTH_SHORT).show();
        }
    }
}
