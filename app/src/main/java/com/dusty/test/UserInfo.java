package com.dusty.test;

/**
 * Created by Dustin on 2/29/2016.
 */
//Used to store the user data locally
public class UserInfo {
    String username,password, name;

    public UserInfo (String username, String password, String name){
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public UserInfo (String username, String password)
    {
        this.username = username;
        this.password = password;
        this.name = "";
    }

}
