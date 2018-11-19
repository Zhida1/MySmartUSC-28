package com.example.zhidachen.mysmartusc_28;


import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardActivity extends Fragment implements View.OnClickListener {
    public Button addKeywordBn;
    public Button viewKeywordsBn;
    public TextView displayNotification;
    public static AppDatabase appDatabase;
    public AppNotification appNotification;

    public DashboardActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_activity, container, false);

        // settings for greeting message
        appNotification = new AppNotification((MainActivity)getActivity());
        AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "userDatabase").allowMainThreadQueries().build();
        List<User> users = appDatabase.appDao().getUsers();
        TextView helloUser = (TextView) view.findViewById(R.id.hello_user);
        String greeting = "Hello " + users.get(0).getUsername();
        helloUser.setText(greeting);

        // settings for add keyword button
        addKeywordBn = view.findViewById(R.id.to_add_keyword);
        addKeywordBn.setOnClickListener(this);

        // settings for view keyword button
        viewKeywordsBn = view.findViewById(R.id.to_view_keywords);
        viewKeywordsBn.setOnClickListener(this);

        // settings for display notification
        displayNotification = view.findViewById(R.id.display_notification);
        RefreshLayout();
        return view;

    }

    public void RefreshLayout() {
        ArrayList<Notification> notifToSend = MainActivity.usr.parseEmail();
        MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
        for(Notification temp : notifToSend) {
            NotificationCompat.Builder builder = appNotification.getAppChannelNotification(temp.getSender(), temp.getSubject());
            appNotification.getManager().notify(new Random().nextInt(), builder.build());
        }
        ArrayList<Notification> allNotif = MainActivity.usr.getNotifications();
        String info = "";
        for(Notification temp : allNotif) {
            info += "\n\n" + "New Email from: " + temp.getSender() + "\nSubject: " + temp.getSubject() + "\nType: " + temp.getType();
        }
        displayNotification.setText(info);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.to_add_keyword:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new PreferenceActivity()).addToBackStack(null).commit();
                break;
            case R.id.to_view_keywords:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new ViewKeywordActivity()).addToBackStack(null).commit();
                break;
        }

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}

