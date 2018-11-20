package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<Keyword> fromStringToKeyword(String value){
        Type listType= new TypeToken<ArrayList<Keyword>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayListKeyword(ArrayList<Keyword> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
    @TypeConverter
    public static ArrayList<Notification> fromStringToNotification(String value){
        Type listType= new TypeToken<ArrayList<Keyword>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayListNotification(ArrayList<Notification> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<UserEmail> fromStringToEmail(String value){
        Type listType= new TypeToken<ArrayList<UserEmail>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayListUserEmail(ArrayList<UserEmail> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
