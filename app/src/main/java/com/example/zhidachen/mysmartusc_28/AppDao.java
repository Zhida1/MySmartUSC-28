package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

@Dao
public interface AppDao {

    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM User")
    ArrayList<User> getUsers();
}
