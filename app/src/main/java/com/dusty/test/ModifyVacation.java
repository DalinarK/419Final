package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

public class ModifyVacation extends Activity {

    String xid;
    String xname;
    String xlocation;
    String xcost;
    String xdays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_vacation);

        Intent intent = getIntent();
        xid = intent.getStringExtra("id");
        xname = intent.getStringExtra("name");
        xlocation = intent.getStringExtra("location");
        xcost = intent.getStringExtra("cost");
        xdays = intent.getStringExtra("duration");

        EditText nameText = (EditText) findViewById(R.id.vacation_name);
        nameText.setText(xname);
        EditText locationText = (EditText) findViewById(R.id.vacation_spot);
        locationText.setText(xlocation);
        EditText daysText = (EditText) findViewById(R.id.number_days);
        daysText.setText(xdays);
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
        else if(xcost.equals("$$")){
            Log.d("Diag", "cost is $$$");
            RadioButton radio = (RadioButton) findViewById(R.id.costExpensive);
            radio.setChecked(true);
        }




    }
}
