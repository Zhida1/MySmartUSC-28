package com.example.zhidachen.mysmartusc_28;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase appDatabase;
    public static FragmentManager fragmentManager;
    public static User usr;
    public Button redirect_loginBn;
    public AppNotification appNotification;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appNotification = new AppNotification(this);
        fragmentManager = getSupportFragmentManager();
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "userDatabase").allowMainThreadQueries().build();
        List<User> users = appDatabase.appDao().getUsers();
        if(users.size() == 0) {
            User temp = new User("New User");
            usr = temp;
            appDatabase.appDao().addUser(temp);
        } else {
            usr = users.get(0);
        }
        startTransactionToDB();
        if(findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }
        }

        // temporary notification
        NotificationCompat.Builder builder = appNotification.getAppChannelNotification("Welcome! " + usr.getUsername() + ".", "Demoing Notification Feature");
        appNotification.getManager().notify(new Random().nextInt(), builder.build());
        // end of temp notification

        // Temp Notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.SECOND, 5);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);


        redirect_loginBn = (Button) findViewById(R.id.Login_Button);
        redirect_loginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(toLoginActivityIntent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startTransactionToDB();
    }

    public void startTransactionToDB() {
        Intent intent = getIntent();
        if(intent.getStringExtra("callMethod").equals("startTransaction")){
            List<User> users = appDatabase.appDao().getUsers();
            if(users.size() == 0) {
                User temp = new User("New User");
                usr = temp;
                appDatabase.appDao().addUser(temp);
            } else {
                usr = users.get(0);
            }
            fragmentManager.beginTransaction().add(R.id.fragment_container, new DashboardActivity(), "DashboardLayout").commit();

        }
    }
}
