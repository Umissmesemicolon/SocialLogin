package com.example.sociallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private LoginButton loginButton;
    Button button;
    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView=findViewById(R.id.txTv);
        loginButton=findViewById(R.id.login_button);
        button=findViewById(R.id.googleBtn);
        callbackManager=CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("email","user_birthday"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GoogleAcitivity.class);
                startActivity(intent);
            }
        });
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken==null){
                textView.setText("");
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();

            }
            else{
                loaduserProfile(currentAccessToken);
            }
        }
    };
    private void loaduserProfile(AccessToken currentAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(currentAccessToken,((object, response) -> {
            if (object!=null){
                try {
                    String email=object.getString("email");
                    String id=object.getString("id");
                    textView.setText(email);
                }
                catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        }));
        Bundle parameters = new Bundle();
        parameters.putString("fields","email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}