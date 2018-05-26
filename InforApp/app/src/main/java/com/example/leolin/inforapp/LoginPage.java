package com.example.leolin.inforapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginPage extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    private static final String TAG = "Preference";
    public static final String prefAccount = "User";
    public static final String prefPasswd = "Passwd";
    public static final String LOGINED = "Login";
    private String result = "";
    private SharedPreferences settings;
    private boolean logined = false;
    private boolean cancel = false;
    private OkHttpClient client;
    private Username mUSERNAME;
    private ExecutorService service;
    //private UserLoginTask mAuthTask = null;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mUser;
    private EditText mPassword;
    private View mProgressView;
    private View mLoginFormView;
    //private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        service = Executors.newSingleThreadExecutor();

        // Set up the login form.
        mUser = (EditText) findViewById(R.id.user);

        //result = (TextView) findViewById(R.id.result);
        mPassword = (EditText) findViewById(R.id.password);

        //setoneditoractionlistener works when you press the return button after typing
        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JIZZPRESSS","BUTTON");
                attemptLogin(false);
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginPage.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mUSERNAME = (Username)getApplication();

        resPref();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(boolean logined) {

        Log.d("AttemptLogin",String.valueOf(logined));

        // Store values at the time of the login attempt.
        final String user = mUser.getText().toString();
        final String password = mPassword.getText().toString();

        if(logined){
            showProgress(true);
            Intent intent = new Intent();
            intent.setClass(LoginPage.this,MenuActivity.class);
            startActivity(intent);
            Toast.makeText(LoginPage.this,mUSERNAME.getUSERNAME() + " " + mUSERNAME.getPASSWORD(),Toast.LENGTH_SHORT).show();
            finish();
        }

        if(!user.equals("") && !password.equals("")){
                showProgress(true);
                RequestBody formBody = new FormBody.Builder()
                        .add("user",user)
                        .add("passwd",password)
                        .build();

                Request request = new Request.Builder()
                        .url("http://group.infor.org/android/login")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginPage.this,"Login Failed.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String str = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserLogin(str);
                            }
                        });
                        //login{true(correct),false(fail)},errorType{false(no error),0(passwd error),1(name not exist)}
                    }
                });
        } else {
            Log.d("WHYYYYYYYYYYYY","AHHHHHHHHHHH");
            if(!logined){
                if(user.equals("")){
                    Toast.makeText(LoginPage.this,"Username Can't be blank",Toast.LENGTH_SHORT).show();
                    mUser.requestFocus();
                } else {
                    Toast.makeText(LoginPage.this,"Password can't be blank",Toast.LENGTH_SHORT).show();
                    mPassword.requestFocus();
                }
            }
        }
    }

    private void UserLogin(String response){
        if(!response.equals("")){
            StringBuilder all = new StringBuilder();

            for(int i = 1;i < response.length();++i){
                if(response.charAt(i - 1) == ':'){
                    for(int j = i;response.charAt(j) != ',' && response.charAt(j) != '}';++j){
                        all.append(response.charAt(j));
                    }
                    all.append(" ");
                }
            }
            final String[] params = all.toString().split(" ");
            if(params[0].equals("false")){
                cancel = true;
                Log.d("params[0]==false","JIZZZZWHY");
                if(params[1].equals("0")){
                    Toast.makeText(LoginPage.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                } else if(params[1].equals("1")){
                    Toast.makeText(LoginPage.this,"User doesn't exist",Toast.LENGTH_SHORT).show();
                }
            } else{
                cancel = false;
                Log.d("params[0]==true","JIZZZZWHY");
            }
        }
        if (!cancel) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Log.d("AttemptCancel",String.valueOf(cancel));
            Log.d("AttemptLogined",String.valueOf(logined));

            settings = getSharedPreferences(TAG,0);
            settings.edit()
                    .putString(prefAccount,mUser.getText().toString())
                    .putString(prefPasswd,mPassword.getText().toString())
                    .putBoolean(LOGINED,true)
                    .apply();

            mUSERNAME.setUSERNAME(mUser.getText().toString());
            mUSERNAME.setPASSWORD(mPassword.getText().toString());

            Intent intent = new Intent();
            intent.setClass(LoginPage.this,MenuActivity.class);
            startActivity(intent);
            Toast.makeText(LoginPage.this,mUSERNAME.getUSERNAME(),Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            //login page disappear
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            //progressbar appear
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void resPref(){
        SharedPreferences setting = getSharedPreferences(TAG,0);
        String name = setting.getString(prefAccount,"");
        String passwd = setting.getString(prefPasswd,"");
        boolean login = setting.getBoolean(LOGINED,false);
        Log.d("JIZZZZZZZZZZZZZZZZZZZZ",name + "yee");
        Log.d("JIZZZZZZZZZZZZZZZZZZZZ",String.valueOf(login));
        if(!name.equals("") && login){
            Log.d("JIZZZZZZZZZZZZ","LOGIN===TRUE");
            mUSERNAME.setUSERNAME(name);
            mUSERNAME.setPASSWORD(passwd);
            attemptLogin(login);
        }
    }

}