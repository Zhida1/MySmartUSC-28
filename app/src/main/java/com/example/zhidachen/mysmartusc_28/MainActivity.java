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
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.api.services.gmail.Gmail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MessageResultReceiver.Receiver {
    public static AppDatabase appDatabase;
    public static FragmentManager fragmentManager;
    public static User usr;
    public Button redirect_loginBn;
    public AppNotification appNotification;
    public static Gmail service = null;
    public MessageResultReceiver mReceiver;
    public static int loginCheck = 0;
    public static int currCheck = 0;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appNotification = new AppNotification(this);
        fragmentManager = getSupportFragmentManager();
        mReceiver = new MessageResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "userDatabase").allowMainThreadQueries().build();
        List<User> users = appDatabase.appDao().getUsers();
        if(users.size() == 0) {
            User temp = new User("New User");
            usr = temp;
            appDatabase.appDao().addUser(temp);
        } else {
            usr = users.get(0);
        }
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

        startTransactionToDB();


        redirect_loginBn = (Button) findViewById(R.id.Login_Button);
        redirect_loginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(toLoginActivityIntent);
            }
        });
    }

    public void sendNotification(String flag) {
        if(flag.equals("parse")) {
            ArrayList<com.example.zhidachen.mysmartusc_28.Notification> newNotifs = usr.parseEmail();
            appDatabase.appDao().updateUser(usr);
            for(com.example.zhidachen.mysmartusc_28.Notification toSend : newNotifs) {
                NotificationCompat.Builder builder = appNotification.getAppChannelNotification(toSend.getSender(), toSend.getSubject());
                appNotification.getManager().notify(new Random().nextInt(), builder.build());
            }
        }
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
            if(service != null) {
                System.out.println("not null");
            }
            else{
                System.out.println("fail");
            }
            List<User> users = appDatabase.appDao().getUsers();
            if(users.size() == 0) {
                User temp = new User("New User");
                usr = temp;
                appDatabase.appDao().addUser(temp);
            } else {
                usr = users.get(0);
            }
            usr.parseEmail();
            System.out.println("first round");
            Intent messageServiceIntent = new Intent(this, CheckEmailService.class);
            //messageServiceIntent.putExtra("nameTag","sohail" );
            messageServiceIntent.putExtra("receiverTag", mReceiver);
            startService(messageServiceIntent);
            fragmentManager.beginTransaction().add(R.id.fragment_container, new DashboardActivity(), "DashboardLayout").commit();

        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        // TODO Auto-generated method stub
        if(resultData.getString("serviceResult").equals("noUpdate")) {
            System.out.println("nothing");
        }
        else if(resultData.getString("serviceResult").equals("doUpdate")) {
            System.out.println("something");

        }
        Intent messageServiceIntent = new Intent(this, CheckEmailService.class);
        messageServiceIntent.putExtra("receiverTag", mReceiver);
        startService(messageServiceIntent);
        //Log.d("sohail","received result from Service="+resultData.getString("ServiceTag"));

    }
}
