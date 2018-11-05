package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;

public class LoginActivity extends AppCompatActivity {

    protected SignInButton LoginWithGoogle;
    //protected Button testingBn;
    protected GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    // what the hell is 9001?
    private static final int RC_SIGN_IN = 9001;
    //protected String server_client_id = "279365372965-1d0jcpld8vrhf5d5vtigfhb2dio6ag6c.apps.googleusercontent.com";

    //buffer reader from HTTP get request
    BufferedReader br;

    //User
    User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // request access scopes

        // --------------- uncomment ----------------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestScopes(new Scope(Scopes.EMAIL))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        //GoogleSignInClient user = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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
        // --------------end of uncomment ------------------------


        Button DebugButton = (Button) findViewById(R.id.without_signin_forTesting);
        DebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDashBoardActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(toDashBoardActivityIntent);
            }
        });
    } // end of method onCreate(Bundle savedInstanceState)

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


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            // create Google sign-in account upon Google sign-in call
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String authCode = account.getServerAuthCode();
            // create user, save to database
            AddUserToDatabase(account);

            // post request to Google
            //---------------------------
            // Exchange auth code for access token
            // convert client_secret json into InputStream, InputStreamReader, GoogleClientSecrets object
            InputStream isClientSecret = getResources().openRawResource(R.raw.client_secret);
            InputStreamReader isrClientSecret = new InputStreamReader(isClientSecret, Charset.forName("UTF-8"));
            Log.w("Before Response: ", "Lets GO!!");

            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(
                            JacksonFactory.getDefaultInstance(), isrClientSecret);

            //----------------okHttp sending request --------------------

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
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
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        final String message = jsonObject.toString(5);
                        Log.i("Response: ", message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Signed in successfully, show authenticated UI.
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        } catch (FileNotFoundException e) {
            Log.w("File not found:::", "FNF");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void AddUserToDatabase(GoogleSignInAccount acct) {
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "userDatabase").allowMainThreadQueries().build();
        List<User> users = appDatabase.appDao().getUsers();
        if (users.size() == 0) {
            usr = new User(acct.getGivenName());
            appDatabase.appDao().addUser(usr);
        } else {
            usr = users.get(0);
            usr.setUsername(acct.getGivenName());
        }
    }


    protected void updateUI(@Nullable GoogleSignInAccount account) {
        if (account == null) {
            // display sign-in button
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        } else {
            // launch our main activity
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Intent toDashBoardActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(toDashBoardActivityIntent);

        }
    }
    
}
