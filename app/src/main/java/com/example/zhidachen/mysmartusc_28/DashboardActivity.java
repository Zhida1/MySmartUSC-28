package com.example.zhidachen.mysmartusc_28;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardActivity extends Fragment implements View.OnClickListener {
    public Button addKeywordBn;
    public TextView displayNotification;

    public DashboardActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.dashboard_activity, container, false);
        addKeywordBn = view.findViewById(R.id.to_add_keyword);
        displayNotification = view.findViewById(R.id.display_notification);
        addKeywordBn.setOnClickListener(this);
        ArrayList<Notification> keywords = MainActivity.usr.getNotifications();
        String info = "";
        for(Notification temp : keywords) {
            info += "\n\n" + "New Email from: " + temp.getSender() + "\nSubject: " + temp.getSubject() + "\nType: " + temp.getType();
        }
        displayNotification.setText(info);
        return view;

    }

    public void RefreshLayout() {
        ArrayList<Notification> keywords = MainActivity.usr.getNotifications();
        String info = "";
        for(Notification temp : keywords) {
            info += "\n\n" + "New Email from: " + temp.getSender() + "\nSubject: " + temp.getSubject() + "\nType: " + temp.getType();
        }
        displayNotification.setText(info);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.to_add_keyword:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new PreferenceActivity()).addToBackStack(null).commit();
                break;

        }
    }
}
