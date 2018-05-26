package com.example.leolin.inforapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private EditText passwordVerify;
    private OkHttpClient client;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
        Button btn = (Button) findViewById(R.id.register_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCheck();
            }
        });
    }

    private void init(){
        name = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        passwordVerify = (EditText) findViewById(R.id.password_verify);
        client = new OkHttpClient();
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
    }

    private void registerCheck(){
        String username = name.getText().toString();
        String passwd = password.getText().toString();
        String passwdv = passwordVerify.getText().toString();

        if(username.equals("")){
            Toast.makeText(RegisterActivity.this,"Username can't be empty.",Toast.LENGTH_SHORT).show();
            name.requestFocus();
        } else if(passwd.equals("")){
            Toast.makeText(RegisterActivity.this,"Password can't be empty.",Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else if(passwdv.equals("")){
            Toast.makeText(RegisterActivity.this,"Password Verification can't be empty.",Toast.LENGTH_SHORT).show();
            passwordVerify.requestFocus();
        } else if(!passwd.equals(passwdv)){
            Toast.makeText(RegisterActivity.this,"Password and Password Verification are different",Toast.LENGTH_SHORT).show();
            passwordVerify.requestFocus();
        } else {
            //TODO:check here
            RequestBody formBody = new FormBody.Builder()
                    .add("user",username)
                    .add("passwd",passwd)
                    .build();
            Request request = new Request.Builder()
                    .url("http://group.infor.org/android/loginr")
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"Register Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) findViewById(R.id.show_result);
                            tv.setText(result);
                            //TODO:toast base on response
                        }
                    });

                }
            });
        }
    }

}
