package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener{

    Button logoutButton;
    EditText currentUser, currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = (EditText) findViewById(R.id.userEntry);
        currentPassword = (EditText) findViewById(R.id.passwordEntry);

        logoutButton = (Button) findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logoutButton:
                startActivity(new Intent(this, Login.class));
        }
    }
}

