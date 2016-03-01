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

    UserInfoLocalStore userInfoLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = (EditText) findViewById(R.id.userEntry);
        currentPassword = (EditText) findViewById(R.id.passwordEntry);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

//        grants access to the local store
        userInfoLocalStore = new UserInfoLocalStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authenticateStatus() == true){
            displayUserDetails();
//            This is where we'll integrate the display of the activities
        }
    }

//    checks to see if user is logged in or logged out
    private boolean authenticateStatus(){
        return userInfoLocalStore.getUserLoggedInStatus();
    }

//    Gets the user info from the local store and sets it on the main screen.
    private void displayUserDetails(){
        UserInfo userInfo = userInfoLocalStore.getLoggedInUser();

        currentUser.setText(userInfo.username);
        currentPassword.setText(userInfo.password);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logoutButton:
                userInfoLocalStore.clearUserData();
                userInfoLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));

        }
    }
}

