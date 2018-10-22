package com.example.zhidachen.mysmartusc_28;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardActivity extends Fragment implements View.OnClickListener {
    public Button addKeywordBn;

    public DashboardActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_activity, container, false);
        addKeywordBn = view.findViewById(R.id.to_add_keyword);
        addKeywordBn.setOnClickListener(this);
        return view;
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.to_add_keyword:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new PreferenceActivity()).addToBackStack(null).commit();
                break;

        }
    }
}
