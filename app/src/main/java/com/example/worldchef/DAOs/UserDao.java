package com.example.worldchef.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.worldchef.Models.User;

import java.util.List;

@Dao
public interface UserDao {

    //Grab the user by username
    @Query("SELECT * FROM USER WHERE username = :username")
    User getUserByUsername(String username);

    @Insert (onConflict = OnConflictStrategy.ABORT)
    public void insertUser (User user);

    //Grab list of all the users, in descending order of points
    @Query("SELECT * FROM USER ORDER BY POINTS DESC")
    List<User> getAllUsers();

    //Search if username exists already
    @Query("SELECT username FROM USER")
    List<String> getUsernames();

    //Add stars for completing quizzes
    @Query("UPDATE USER SET points = (:additionalPoints + :currentPoints)  WHERE username =:username")
    public void addPoints(int additionalPoints,int currentPoints, String username);



}
