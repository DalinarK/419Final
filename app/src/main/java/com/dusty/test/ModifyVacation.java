package com.dusty.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ModifyVacation extends Activity implements LocationListener {

    private TextView locationField;
    private LocationManager locationManager;
    private String provider;
    private Location location;

    String xid;
    String xname;
    String xlocation;
    String xcost;
    String xdays;
    String xgpsCoords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_vacation);

//      Used to find out current GPS location
        locationField = (TextView) findViewById(R.id.gpsCoordinates);
//
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

//        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            locationField.setText("Location not yet available");
        }


//      Used to find out the vacation to be modified
        Intent intent = getIntent();
        xid = intent.getStringExtra("id");
        xname = intent.getStringExtra("name");
        xlocation = intent.getStringExtra("location");
        xcost = intent.getStringExtra("cost");
        xdays = intent.getStringExtra("duration");
        xgpsCoords = intent.getStringExtra("gpsCoords");

        EditText nameText = (EditText) findViewById(R.id.vacation_name);
        nameText.setText(xname);
        EditText locationText = (EditText) findViewById(R.id.vacation_spot);
        locationText.setText(xlocation);
        EditText daysText = (EditText) findViewById(R.id.number_days);
        daysText.setText(xdays);
        EditText gpsText = (EditText) findViewById(R.id.gpsCoordinates);
        gpsText.setText(xgpsCoords);
        EditText cost = (EditText) findViewById(R.id.number_days);

        Log.d("Diag", "cost is " + xcost);

        if (xcost.equals("$")){
            Log.d("Diag", "cost is $");
            RadioButton radio = (RadioButton) findViewById(R.id.costCheap);
            radio.setChecked(true);
        }
        else if (xcost.equals("$$")){
            Log.d("Diag", "cost is $$");
            RadioButton radio = (RadioButton) findViewById(R.id.costMed);
            radio.setChecked(true);
        }
        else if(xcost.equals("$$$")){
            Log.d("Diag", "cost is $$$");
            RadioButton radio = (RadioButton) findViewById(R.id.costExpensive);
            radio.setChecked(true);
        }

    }

    public void Delete (View view) {
        Log.d("Diag", "Deleting " + xid);
            new deleteTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationlist/" + xid);
    }

    public void Modify (View view) {
        EditText nameText = (EditText) findViewById(R.id.vacation_name);
        String vacation_name = nameText.getText().toString();
        EditText locationText = (EditText) findViewById(R.id.vacation_spot);
        String vacation_spot = locationText.getText().toString();
        EditText daysText = (EditText) findViewById(R.id.number_days);
        String number_days = daysText.getText().toString();
        EditText gpsText = (EditText) findViewById(R.id.gpsCoordinates);
        xgpsCoords = gpsText.getText().toString();

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
            new modifyTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationlist/"+xid, vacation_name, vacation_spot, number_days, xcost, xname, xgpsCoords);
        }

    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
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

    public void useCurrentLocation(View view)
    {
        Log.d("Diag", "GPS button clicked");
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        locationField.setText(lat + ", " + lng);
    }

    public class deleteTask extends AsyncTask<String, String, String> {

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
                connection.setRequestMethod("DELETE");
                connection.setUseCaches(false);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                // give it 15 seconds to respond
//          connection.setReadTimeout(15*1000)
                JSONObject postInfo = new JSONObject();
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
            Intent intent = new Intent(ModifyVacation.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public class modifyTask extends AsyncTask<String, String, String> {

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
                connection.setRequestMethod("PUT");
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
                postInfo.put("gpsCoords", params[6]);
                String JSONstring = postInfo.toString();

                Log.d("Diag", "gps is" + params[6]);

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
            Intent intent = new Intent(ModifyVacation.this, MainActivity.class);
            startActivity(intent);
        }
    }

//    http://developer.android.com/guide/topics/ui/controls/radiobutton.html
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.costCheap:
                if (checked)
                    xcost = "$";
                break;
            case R.id.costMed:
                if (checked)
                    xcost = "$$";
                break;
            case R.id.costExpensive:
                if (checked)
                    xcost = "$$$";
                break;
        }
    }
}
