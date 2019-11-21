package com.example.worldchef.TaskDelegates;

import com.example.worldchef.Models.User;

import java.util.List;

public interface AsyncTaskUserDelegate {

    void handleInsertUserResult(String result);
    void handleGetUserResult(User user);
    void handleGetAllUsersResult(List<User> users);
    void handleGetUsernamesResult(List<String> usernames);
    void handleGetUserByUserName(User user);
    void handleInsertPoints(String result);
}
