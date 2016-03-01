package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements View.OnClickListener {

    Button loginButton, registerLink;
    EditText enteredUser, enteredPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enteredUser = (EditText) findViewById(R.id.userEntry);
        enteredPassword = (EditText) findViewById(R.id.passwordEntry);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
        registerLink = (Button) findViewById(R.id.registerLink);
        registerLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                break;
            case R.id.registerLink:
                Log.d("Diag", "user pressed registerLink");
                startActivity(new Intent(this, Register.class));

        }
    }

    public class LoginConn extends AsyncTask
}
