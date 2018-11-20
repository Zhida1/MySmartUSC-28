package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AppDao {

    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM User")
    List<User> getUsers();

    @Update
    public void updateUser(User user);
}
