package com.example.worldchef.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.worldchef.Models.Quiz;
import com.example.worldchef.Models.User;

import java.util.List;

@Dao
public interface QuizDao {

    //Insert question
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertQuestionList (List<Quiz> quizzes);

    //Are there questions in this quiz already?
    @Query("SELECT COUNT(*) FROM QUIZ")
    long getCountOfQuestions();

    //Get questions by category
    @Query("SELECT * FROM QUIZ WHERE category = :category")
    List<Quiz> getAllQuestionsByCategory(String category);
}
