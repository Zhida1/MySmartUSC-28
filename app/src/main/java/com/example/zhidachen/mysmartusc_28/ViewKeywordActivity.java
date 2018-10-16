package com.example.zhidachen.mysmartusc_28;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewKeywordActivity extends Fragment {
    public TextView displayKeyword;

    public ViewKeywordActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.viewkeyword_activity, container, false);
        displayKeyword = view.findViewById(R.id.display_keyword);
        ArrayList<Keyword> keywords = MainActivity.usr.getKeywords();
        String info = "";
        for(Keyword temp : keywords) {
            info += "\n\n" + "Keyword: " + temp.getKeyword() + "\nCheck On: " + temp.getCheckArea();
        }
        displayKeyword.setText(info);
        return view;
    }

}
