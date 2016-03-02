package com.dusty.test;

/**
 * Created by Dustin on 2/29/2016.
 */
//Used to store the user data locally
public class UserInfo {
    String username,password, id;

    public UserInfo (String username, String password, String id){
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public UserInfo (String username, String id)
    {
        this.username = username;
        this.id = id;
        this.password = "";
    }

}
