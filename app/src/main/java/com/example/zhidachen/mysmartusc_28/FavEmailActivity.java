package com.example.zhidachen.mysmartusc_28;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavEmailActivity extends Fragment {
    public TextView displayEmail;

    public FavEmailActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favemail_activity, container, false);
        displayEmail = view.findViewById(R.id.display_email_textview);
        displayEmail.setMovementMethod(new ScrollingMovementMethod());
        RefreshLayout();
        return view;
    }

    public void RefreshLayout() {
        ArrayList<UserEmail> mails = MainActivity.usr.getUserEmails();
        String info = "";
        displayEmail.setText(info);
        System.out.println("size: " + mails.size());
        for(int i = mails.size() - 1; i >= 0; i--) {
            if(mails.get(i).getFavNot() == 1) {
                info += "\n\n" + mails.get(i).getSender() + "\n" + mails.get(i).getSubject() + "\n" + mails.get(i).getContent();
            }
        }
        displayEmail.setText(info);
    }
}
