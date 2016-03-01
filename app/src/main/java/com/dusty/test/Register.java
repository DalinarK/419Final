package com.dusty.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity implements View.OnClickListener {

    Button registerButton;
    EditText userEntered, passwordEntered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userEntered = (EditText) findViewById(R.id.userRegister);
        passwordEntered = (EditText) findViewById(R.id.passwordRegister);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                String username = userEntered.getText().toString();
                String password = passwordEntered.getText().toString();

                UserInfo registrationInfo = new UserInfo(username, password);
                break;
        }
    }
}