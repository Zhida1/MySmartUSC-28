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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardActivity extends Fragment implements View.OnClickListener {
    public Button addKeywordBn;
    public Button viewKeywordsBn;
    public Button favEmailBn;
    public TextView displayNotification;
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
        TextView helloUser = (TextView) view.findViewById(R.id.hello_user);
        String greeting = "Hello " + MainActivity.usr.getUsername();
        helloUser.setText(greeting);

        // settings for add keyword button
        addKeywordBn = view.findViewById(R.id.to_add_keyword);
        addKeywordBn.setOnClickListener(this);

        // settings for view keyword button
        viewKeywordsBn = view.findViewById(R.id.to_view_keywords);
        viewKeywordsBn.setOnClickListener(this);

        favEmailBn = view.findViewById(R.id.to_fav_emails);
        favEmailBn.setOnClickListener(this);

        // settings for display notification
        displayNotification = view.findViewById(R.id.display_notification);
        RefreshLayout();
        return view;

    }

    public void RefreshLayout() {
        String info = "";
        displayNotification.setText(info);
        ArrayList<Notification> newNotifs = MainActivity.usr.parseEmail();
        if(newNotifs.size() != 0) {
            MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
        }
        for(Notification toSend : newNotifs) {
            NotificationCompat.Builder builder = appNotification.getAppChannelNotification(toSend.getSender(), toSend.getSubject());
            appNotification.getManager().notify(new Random().nextInt(), builder.build());
        }

        ArrayList<Notification> allNotifs = MainActivity.usr.getNotifications();
        info = "";
        for(Notification toDisplay : allNotifs) {
            info += "\n\n" + "New Email from: " + toDisplay.getSender() + "\nSubject: " + toDisplay.getSubject() + "\nType: " + toDisplay.getType();
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
            case R.id.to_fav_emails:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new FavEmailActivity()).addToBackStack(null).commit();
                break;
        }

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}

