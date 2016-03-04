package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class addVacation extends Activity implements LocationListener {

    private String provider;

    String costOfVacation = null;
    String username;

    UserInfoLocalStore userInfoLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vacation);

//        used for pull up user information
        userInfoLocalStore = new UserInfoLocalStore(this);
        UserInfo userInfo = userInfoLocalStore.getLoggedInUser();
        username = userInfo.username;

    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
//        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
//        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public void sendMessage(View view) {
        EditText nameText = (EditText) findViewById(R.id.vacation_name);
        String vacation_name = nameText.getText().toString();
        EditText locationText = (EditText) findViewById(R.id.vacation_spot);
        String vacation_spot = locationText.getText().toString();
        EditText daysText = (EditText) findViewById(R.id.number_days);
        String number_days = daysText.getText().toString();

//      Input validation
        if (vacation_name.length() < 10) {
            if (vacation_name.length() == 0)
                nameText.setError("Title is required!");
            else if (vacation_name.length() < 10)
                nameText.setError("Be more descriptive!");

        } else if (vacation_spot.length() == 0) {
            locationText.setError("Location is required!");
        } else if (number_days.length() == 0) {
            daysText.setError("Number of days is required!");
        } else {
            new JSONTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationlist", vacation_name, vacation_spot, number_days, costOfVacation, username);
        }

    }

    public void imageButton(View view) {
        Log.d("Diag", "inside imageButton");
        Intent intent = new Intent(this, addImage.class);
        startActivity(intent);
    }


    //    http://developer.android.com/guide/topics/ui/controls/radiobutton.html
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.costCheap:
                if (checked)
                    costOfVacation = "$";
                break;
            case R.id.costMed:
                if (checked)
                    costOfVacation = "$$";
                break;
            case R.id.costExpensive:
                if (checked)
                    costOfVacation = "$$$";
                break;
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
                postInfo.put("name", params[1]);
                postInfo.put("location", params[2]);
                postInfo.put("days", params[3]);
                postInfo.put("cost", params[4]);
                postInfo.put("username", params[5]);
                String JSONstring = postInfo.toString();

                byte[] outputInBytes = JSONstring.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();

                InputStream inStream = connection.getInputStream();

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
            Intent intent = new Intent(addVacation.this, MainActivity.class);
            startActivity(intent);
        }
    }

}