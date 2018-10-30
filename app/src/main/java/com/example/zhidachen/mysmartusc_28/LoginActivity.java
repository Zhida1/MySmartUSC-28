package com.example.zhidachen.mysmartusc_28;

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

import java.io.BufferedReader;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // request access scopes
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
//        LoginWithGoogle.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                switch (v.getId()) {
//                    case R.id.sign_in_button:
//                        signIn();
//                        break;
//                }
//            }
//        });
        Button DebugButton = (Button) findViewById(R.id.without_signin_forTesting);
        DebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDashBoardActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(toDashBoardActivityIntent);
            }
        });
    }

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
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String authCode = account.getServerAuthCode();

//            HttpPost httpPost = new HttpPost("https://yourbackend.example.com/authcode");

            // use tokeninfo endpoint method to authenticate
            //send http get request
//            String tokenURL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + idToken;
//            URL url = new URL(tokenURL);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.connect();
//            try {
//                InputStream stream = urlConnection.getInputStream();
//                br = new BufferedReader(new InputStreamReader(stream));
//
//
//
//
//
//                readStream(br);
//            } finally {
//                urlConnection.disconnect();
//            }


            // Signed in successfully, show authenticated UI.
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
// catch (MalformedURLException e) {
//            Log.w("Malformed Exception**: ", "cao...");
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.w("IOException: !!!!", "caoni....");
//            e.printStackTrace();
//        }
    }
//
//    private String readStream(BufferedReader is) {
//        try {
//
//            ByteArrayOutputStream bo = new ByteArrayOutputStream();
//            int i = is.read();
//            while (i != -1) {
//                bo.write(i);
//                i = is.read();
//            }
//            Log.w("HTTP return: ", bo.toString());
//            return bo.toString();
//        } catch (IOException e) {
//            return "";
//        }
//    }

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

