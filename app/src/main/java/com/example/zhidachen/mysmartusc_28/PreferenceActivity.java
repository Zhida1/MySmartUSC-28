package com.example.zhidachen.mysmartusc_28;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceActivity extends Fragment {
    private Spinner spinner;
    private TextInputEditText userInput;
    private Button addKeywordBn;
    private Button removeKeywordBn;
    private Button checkKeywordBn;

    public PreferenceActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.preference_activity,container, false);
        spinner = view.findViewById(R.id.check_area);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.areasforcheck,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        userInput = view.findViewById(R.id.user_input);
        addKeywordBn = view.findViewById(R.id.add_keyword);
        removeKeywordBn = view.findViewById(R.id.remove_keyword);
        checkKeywordBn = view.findViewById(R.id.check_keyword);
        addKeywordBn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String keyword = userInput.getText().toString();
               String checkArea = spinner.getSelectedItem().toString();
               if(!MainActivity.usr.checkKeyword(keyword, checkArea)) {
                   MainActivity.usr.addKeyword(keyword, checkArea);
                   MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
                   Toast.makeText(getActivity(), "Keyword has been added", Toast.LENGTH_SHORT).show();
               }
               else {
                   Toast.makeText(getActivity(), "Keyword already exists", Toast.LENGTH_SHORT).show();
               }
               userInput.setText("");
           }
        });
        removeKeywordBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = userInput.getText().toString();
                String checkArea = spinner.getSelectedItem().toString();
                if(MainActivity.usr.checkKeyword(keyword, checkArea)) {
                    MainActivity.usr.removeKeyword(keyword, checkArea);
                    MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
                    Toast.makeText(getActivity(), "Keyword has been removed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Keyword does not exist", Toast.LENGTH_SHORT).show();
                }
                userInput.setText("");
            }
        });
        checkKeywordBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new ViewKeywordActivity()).addToBackStack(null).commit();
            }
        });

        return view;
    }

}
