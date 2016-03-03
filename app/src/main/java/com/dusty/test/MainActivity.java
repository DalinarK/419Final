package com.dusty.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dusty.test.Authentication.Login;
import com.dusty.test.tripmodel.tripmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{


//
    Button logoutButton;
    Button addVacation;
    EditText currentUser, currentID;
    UserInfoLocalStore userInfoLocalStore;
//    Related to displaying list
    ListView lvVacations;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = (EditText) findViewById(R.id.userName);
        currentID = (EditText) findViewById(R.id.userID);
        addVacation = (Button) findViewById(R.id.addVacation);
        addVacation.setOnClickListener(this);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

//        grants access to the local store
        userInfoLocalStore = new UserInfoLocalStore(this);
        UserInfo userInfo = userInfoLocalStore.getLoggedInUser();
        String username = userInfo.username;

       //related to displaying lists
        lvVacations = (ListView)findViewById(R.id.lvVacation);


        new JSONTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationbyuser/" + username);

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
        currentID.setText(userInfo.id);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addVacation:
                Log.d("Diag", "addVacation button clicked");
                startActivity(new Intent(this, addVacation.class));
                break;
            case R.id.logoutButton:
                userInfoLocalStore.clearUserData();
                userInfoLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
        }
    }

    public class JSONTask extends AsyncTask<String, String,List<tripmodel> > {

        @Override
        protected List <tripmodel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            try{
//
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
//            //sets the connection up for sending data
//            connection.setDoOutput(true);
//            connection.setChunkedStreamingMode(0);
//            connection.setRequestMethod("GET");
                // give it 15 seconds to respond
//          connection.setReadTimeout(15*1000);
                connection.connect();
                InputStream inStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inStream));


                String line ="";
                while((line = reader.readLine())!=null)
                {
                    buffer.append(line);
                }
//
                String finalJSON = buffer.toString();
//                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = new JSONArray(finalJSON);

                List <tripmodel> vacationModelList = new ArrayList<>();

                String locationRec = "";
                String idRec = "";
                String nameRec = "";
                String daysRec = "";
                String costRec = "";
                String demographic ="";

                for (int i = 0; i < parentArray.length(); i++)
                {
                    tripmodel tripModel = new tripmodel();
                    JSONObject finalObject = parentArray.getJSONObject(i);
//                    http://stackoverflow.com/questions/10588763/android-json-and-null-values
                    if(finalObject.isNull("_id")) {
                        idRec = null;
                    } else {
                        idRec = finalObject.getString("_id");
                    }

                    if(finalObject.isNull("name")) {
                        nameRec = null;
                    } else {
                        nameRec = finalObject.getString("name");
                    }

                    if(finalObject.isNull("location")) {
                        locationRec = null;
                    } else {
                        locationRec = "Location: " + finalObject.getString("location");
                    }

                    if(finalObject.isNull("days")) {
                        daysRec = null;
                    } else {
                        daysRec = "Duration: " + finalObject.getString("days");
                    }

                    if(finalObject.isNull("cost")) {
                        costRec = null;
                    } else {
                        costRec = "Cost: " + finalObject.getString("cost");
                    }

                    tripModel.set_id(idRec);
                    tripModel.setLocation(locationRec);
                    tripModel.setName(nameRec);
                    tripModel.setDays(daysRec);
                    tripModel.setCost(costRec);
////                    Need to implement the actual activities list after finishing this.
                    vacationModelList.add(tripModel);
                }

//                tripmodel testModel = vacationModelList.get(0);
//                String output = testModel.getLocation();

                return vacationModelList;

            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List <tripmodel> result) {
            super.onPostExecute(result);
            vacationAdapter adapter = new vacationAdapter(getApplicationContext(), R.layout.vacationrow, result);
            Log.d("Diag", "Finished query");
            lvVacations.setAdapter(adapter);
            lvVacations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursorID = (Cursor) lvVacations.getItemAtPosition(position);
                    Log.d("Diag", "clicked on vacation");
                }
            });
//            outputView.setText("filler");
        }
    }

    public class vacationAdapter extends ArrayAdapter
    {

        private List<tripmodel> vacationModelList;
        private int resource;
        private LayoutInflater inflater;

        public vacationAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            vacationModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.vacationrow, null);
            }

            TextView xlocation;
            TextView xname;
            TextView xdays;
            TextView xcost;
            TextView xid;

            xlocation = (TextView)convertView.findViewById(R.id.xlocation);
            xname = (TextView)convertView.findViewById(R.id.xname);
            xdays = (TextView)convertView.findViewById(R.id.xdays);
            xcost = (TextView)convertView.findViewById(R.id.xcost);
            xid = (TextView)convertView.findViewById(R.id.xvacationID);

            xlocation.setText(vacationModelList.get(position).getLocation());
            xname.setText(vacationModelList.get(position).getName());
            xdays.setText(vacationModelList.get(position).getDays());
            xcost.setText(vacationModelList.get(position).getCost());
            xid.setText(vacationModelList.get(position).get_id());


            return convertView;
        }
    }
}

