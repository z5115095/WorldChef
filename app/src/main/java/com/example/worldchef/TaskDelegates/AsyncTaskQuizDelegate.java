package com.example.worldchef.TaskDelegates;

import com.example.worldchef.Models.Quiz;

import java.util.List;

public interface AsyncTaskQuizDelegate {
    void handleInsertQuestionTask(String result);
    void handleGetQuestionCountTask(long count);
    void handleGetQuestionTask(List<Quiz> questions);
}
