package com.example.zhidachen.mysmartusc_28;

import android.app.IntentService;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckEmailService extends IntentService {

    public CheckEmailService() {
        super("CheckEmailService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppNotification appNotification = new AppNotification(this);
        MainActivity.currCheck = 0;
        ArrayList<com.example.zhidachen.mysmartusc_28.Notification> newNotifs = new ArrayList<Notification>();
        boolean flag = true;
        while(flag) {
            if(MainActivity.service != null) {
                if(MainActivity.loginCheck == 1) {
                    flag = false;
                    try{
                        ListMessagesResponse gmail_response = MainActivity.service.users()
                                .messages()
                                .list("me")
                                .setMaxResults(Long.valueOf(20))
                                .execute();

                        List<Message> messages = new ArrayList<>();

                        while (gmail_response.getMessages() != null) {
                            messages.addAll(gmail_response.getMessages());
                            if (gmail_response.getNextPageToken() != null) {
                                String pageToken = gmail_response.getNextPageToken();
                                gmail_response = MainActivity.service.users().messages().list("me")
                                        .setPageToken(pageToken).execute();
                            } else {
                                break;
                            }
                        }
                        Log.w("No. of gmail msgs: ", String.valueOf(messages.size()));
                        for (int i = 0; i < 5; i++) {
                            String userId = messages.get(i).getId();
                            String emailId = messages.get(i).getId();
                            String sender = "";
                            String subject = "";
                            String type = "";
                            String content = "";

                            // get single message
                            Message single_message = MainActivity.service.users().messages().get("me", userId).execute();
                            MessagePart part = single_message.getPayload();
                            Log.w("Inside:", single_message.toString());
                            if(single_message.toString().indexOf("UNREAD") == -1) {
                                continue;
                            }


                            // read, decode email content --> store it into content
                            if (single_message.toString().contains("payload\":{\"body\":{\"data")) {
                                byte[] bodyBytes = Base64.decodeBase64(single_message.getPayload().getBody().getData());
                                String bd = new String(bodyBytes, "UTF-8");
                                content = bd;
                                Log.w("Inside Gmail UPDATES: ", content);
                                UserEmail mail = new UserEmail(content);
                                MainActivity.usr.addUserEmail(mail);
                                MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
                                break;
                            } else {
                                String msg = StringUtils.newStringUtf8(Base64.decodeBase64(single_message.getPayload().getParts().get(0).getBody().getData()));
                                content = msg;
                                Log.w("Inside Gmail message:", content);
                                System.out.println(content);
                                //Log.w("Inside Gmail message:", StringUtils.newStringUtf8(Base64.decodeBase64(single_message.getRaw()))); need to call setFormat("raw")

                                // get sender from header
                                List<MessagePartHeader> headers = single_message.getPayload().getHeaders();
                                boolean gotSubject = false;
                                boolean gotSender = false;
                                for (int j = 0; j < headers.size(); j++) {
                                    if (headers.get(j).getName().equals("From")) {
                                        sender = headers.get(j).getValue();
                                        Log.w("Header: ", sender);
                                        System.out.println(sender);
                                        gotSender = true;
                                    }
                                    if (headers.get(j).getName().equals("Subject")) {
                                        subject = headers.get(j).getValue();
                                        Log.w("Subject: ", subject);
                                        System.out.println(subject);
                                        gotSubject = true;
                                    }
                                    if (gotSender && gotSubject) {
                                        UserEmail mail = new UserEmail(emailId, sender.substring(sender.indexOf("<")+1,sender.indexOf(">")), subject, content);
                                        MainActivity.usr.addUserEmail(mail);
                                        MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
                                        break;
                                    }

//                                    Log.w("Header: ", headers.get(j).toString());
                                }

                            }
                        }
                        newNotifs = MainActivity.usr.parseEmail();
                        MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);
                        for(com.example.zhidachen.mysmartusc_28.Notification toSend : newNotifs) {
                            NotificationCompat.Builder builder = appNotification.getAppChannelNotification(toSend.getSender(), toSend.getSubject());
                            appNotification.getManager().notify(new Random().nextInt(), builder.build());
                        }
                        MainActivity.currCheck = 1;
                    } catch (IOException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        // Please refer to the GoogleSignInStatusCodes class reference for more information.
                        //Log.w(Tag, "signInResult:failed code=" + e.getStatusCode());
                    }

                }
            }
        }
        while(MainActivity.currCheck == 0){

        }
        ResultReceiver rec = intent.getParcelableExtra("receiverTag");
        Bundle b= new Bundle();
        if(newNotifs.size() == 0) {
            b.putString("serviceResult","noUpdate");
        }
        else {
            b.putString("serviceResult","doUpdate");
        }
        rec.send(0, b);

    }
}
