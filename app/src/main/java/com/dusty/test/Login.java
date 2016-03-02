package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity implements View.OnClickListener {

    Button loginButton, registerLink;
    EditText enteredUser, enteredPassword;
    UserInfoLocalStore userInfoLocalStore;

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

        //        grants access to the local store
        userInfoLocalStore = new UserInfoLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                String username = enteredUser.getText().toString();
                String password = enteredUser.getText().toString();

                UserInfo enteredUserInfo = new UserInfo(null, null);
                userInfoLocalStore.storeUserData(enteredUserInfo);
                userInfoLocalStore.setUserLoggedIn(true);

                new JSONTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/login", username, password);
                Log.d("Diag", "Working so far");
                break;
            case R.id.registerLink:
                Log.d("Diag", "user pressed registerLink");
                startActivity(new Intent(this, Register.class));

        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            HttpURLConnection postConnection = null;

            try {
//              used https://www.youtube.com/watch?v=Gyaay7OTy-w as a reference
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
//            //sets the connection up for sending data
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();

                // give it 15 seconds to respond
//          connection.setReadTimeout(15*1000)
                JSONObject postInfo = new JSONObject();
                postInfo.put("username", params[1]);
                postInfo.put("password", params[2]);
                String JSONstring = postInfo.toString();

                byte[] outputInBytes = JSONstring.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();

//                Receive reply from server
                InputStream inStream = connection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(inStream));
////                Read in response one line at a time
//                String line ="";
//                while((line = reader.readLine())!=null)
//                {
//                    buffer.append(line);
//                }
////                Convert to a String
//                String finalJSON = buffer.toString();
//                Log.d("Diag", "Works so far");
//
//                Log.d("Diag", finalJSON);
////                Convert to JSONObject for manipulation
//                JSONObject returnValue = new JSONObject(finalJSON);
//                String id = returnValue.getString("_id");
//                String username =returnValue.getString("username");
//                Log.d("Diag", "The id is " + id + "The username is " + username);
//
////                Store user name and ID
//                UserInfo enteredUserInfo = new UserInfo(username, id);
//
//                userInfoLocalStore.storeUserData(enteredUserInfo);
//                userInfoLocalStore.setUserLoggedIn(true);

                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }


}
