package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        new JSONTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationbyuser/test");

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
            Intent intent = new Intent(Main2Activity.this,MainActivity.class );
            ActivitiesBridge.setObject(result);
            startActivity(intent);

        }
    }
}
