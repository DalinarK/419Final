package com.dusty.test;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dustin on 2/29/2016.
 */
public class UserInfoLocalStore {

    public static final String SP_Name = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserInfoLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_Name, 0);
    }

//    Takes the data and stores it in UserInfo User
    public void storeUserData (UserInfo user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("id", user.id);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }

//    Pulls user info out of local DB and returns it.
    public UserInfo getLoggedInUser(){
        String id = userLocalDatabase.getString("id", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");

        UserInfo storedUser = new UserInfo(username, password, id);

        return storedUser;
    }

//    this sets the login status as either true or false in the local database
    public void setUserLoggedIn (boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

//  returns the status of login
    public boolean getUserLoggedInStatus(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == true){
            return true;
        }
        return false;
}

//    this clears all the user info
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
