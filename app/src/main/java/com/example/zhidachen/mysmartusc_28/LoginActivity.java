package com.example.zhidachen.mysmartusc_28;

import android.app.Notification;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    // Google Account related variables
    protected SignInButton LoginWithGoogle;
    //protected Button testingBn;
    protected GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    // what the hell is 9001?
    private static final int RC_SIGN_IN = 9001;
    // ClientSecretObject for obtaining Authorization
    protected GoogleClientSecrets clientSecrets;
    // Authorization JSON file (include access token)
    protected AccessTokenJSON authorizationObject;
    
    // Our own variables (including database, buttons, etc.)
    //User
    //User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Configure Google sign-in
        // Upon success, we have access to: user ID, email, basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestScopes(new Scope(GmailScopes.GMAIL_READONLY))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Google Sign-in OnClickListener
        LoginWithGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        LoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
        // ---------------- Test BUTTON to skip login ---------------------------
        Button DebugButton = (Button) findViewById(R.id.without_signin_forTesting);
        DebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDashBoardActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                toDashBoardActivityIntent.putExtra("callMethod", "startTransaction");
                startActivity(toDashBoardActivityIntent);
            }
        });
        // ---------------- End of test BUTTON to skip login ---------------------------

    } // end of method onCreate(Bundle savedInstanceState)


    // signIn(): allow user to sign-in via Google's sign-in interface
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    } // end of signIn method

    // onActivityResult(): continuation of signInIntent from signIn() method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    } // end of onActivityResult method

    // handleSignInResult(): collect AuthCode, AccessToken, RefreshToken; add user to database, redirect/update UI
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            // create Google sign-in account upon Google sign-in call
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // create user, save to database
            AddUserToDatabase(account);
            //final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);

            //----------------------- OkHttp POST Request --------------------------------
            // Exchange auth code for access token
            String idToken = account.getIdToken();
            String authCode = account.getServerAuthCode();
            // ExchangeAuthCodeForAccessToken(authCode, idToken);
            // convert client_secret json into InputStream, InputStreamReader, GoogleClientSecrets object
            InputStream isClientSecret = getResources().openRawResource(R.raw.client_secret);
            InputStreamReader isrClientSecret = new InputStreamReader(isClientSecret, Charset.forName("UTF-8"));

            try {
                clientSecrets =
                        GoogleClientSecrets.load(
                                JacksonFactory.getDefaultInstance(), isrClientSecret);
            } catch (IOException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //.addFormDataPart("access_type","offline")
                    .addFormDataPart("grant_type", "authorization_code")
                    .addFormDataPart("client_id", clientSecrets.getDetails().getClientId())
                    .addFormDataPart("client_secret", clientSecrets.getDetails().getClientSecret())
                    .addFormDataPart("code", authCode)
                    .addFormDataPart("redirect_url", "")
                    .addFormDataPart("id_token", idToken)
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v4/token")
                    .post(body)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.w("Fail: ", "Unable to get response");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject authorization = new JSONObject(response.body().string());
                        final String message = authorization.toString(5);
                        Log.i("Response: ", message);
                        // convert JSON into Java class
                        Reader r = new StringReader(message);
                        BufferedReader br = new BufferedReader(r);
                        authorizationObject = new Gson().fromJson(br, AccessTokenJSON.class);
                        // add information to user
//                        AddTokenInfoToDatabase(authorizationObject);

                        String accessToken = (String) authorization.get("access_token");
                        //String refreshToken = (String) authorization.get("refresh_token");
                        final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
                        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                        //.setRefreshToken(refreshToken)


                        final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();

                        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                                .setApplicationName("MySmartUSC28")
                                .build();

                        ListMessagesResponse gmail_response = service.users()
                                .messages()
                                .list("me")
                                .setMaxResults(Long.valueOf(20))
                                .execute();

                        List<com.google.api.services.gmail.model.Message> messages = new ArrayList<>();

                        while (gmail_response.getMessages() != null) {
                            messages.addAll(gmail_response.getMessages());
                            if (gmail_response.getNextPageToken() != null) {
                                String pageToken = gmail_response.getNextPageToken();
                                gmail_response = service.users().messages().list("me")
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
                            Message single_message = service.users().messages().get("me", userId).execute();
                            MessagePart part = single_message.getPayload();
                            Log.w("Inside:", single_message.toString());

                            // read, decode email content --> store it into content
                            if (single_message.toString().contains("payload\":{\"body\":{\"data")) {
                                byte[] bodyBytes = Base64.decodeBase64(single_message.getPayload().getBody().getData());
                                String bd = new String(bodyBytes, "UTF-8");
                                content = bd;
                                Log.w("Inside Gmail UPDATES: ", content);
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

                            } // end of else

                            // check if match keywords



                            // addNotificationToDatabase(single_message);


                            //MainActivity.usr.addNotification(String sender, String subject, String type);
                            //MainActivity.appDatabase.appDao().updateUser(MainActivity.usr);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Signed in successfully, show authenticated UI.
            //appDatabase.appDao().updateUser(usr);
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }

    } // end of handleSignInResult method

    private void addNotificationToDatabase(Message single_message) {

    }


    private void AddUserToDatabase(GoogleSignInAccount acct) {
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "userDatabase").allowMainThreadQueries().build();
        List<User> users = appDatabase.appDao().getUsers();
        if (users.size() == 0) {
            User usr = new User(acct.getGivenName());
            appDatabase.appDao().addUser(usr);
        } else {
            User usr = users.get(0);
            usr.setUsername(acct.getGivenName());
            appDatabase.appDao().updateUser(usr);

        }
    } // end of AddUserToDatabase method

//    private void AddTokenInfoToDatabase(AccessTokenJSON authorizationObject) {
//        if(usr == null) {
//            Log.w("Authorization failed.", "Failed to Add to Datrabase");
//        }
//        usr.access_token = authorizationObject.getAccess_token();
//        usr.expires_in = authorizationObject.getExpires_in();
//        usr.scope = authorizationObject.getScope();
//        usr.token_type = authorizationObject.getToken_type();
//        usr.id_token = authorizationObject.getId_token();
//    }

    protected void updateUI(@Nullable GoogleSignInAccount account) {
        if (account == null) {
            // display sign-in button
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        } else {
            // launch our main activity
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Intent toDashBoardActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            toDashBoardActivityIntent.putExtra("callMethod", "startTransaction");
            startActivity(toDashBoardActivityIntent);
        }
    } // end of updateUI method


    //    protected void onStart() {
//        super.onStart();
//        // check if user sign in to google and update UI
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//
//        // request user profile; Add code to store into database for further use
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//        }
//    }

} // end of LoginActivity.java
