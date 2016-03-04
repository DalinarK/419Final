package com.dusty.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class ModifyVacation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_vacation);

        Intent intent = getIntent();
        String xid = intent.getStringExtra("xid");
        String xname = intent.getStringExtra("xname");
        String xlocation = intent.getStringExtra("xlocation");
        String xcost = intent.getStringExtra("xcost");
        String xdays = intent.getStringExtra("xdays");

        EditText nameText = (EditText) findViewById(R.id.vacation_name);
        nameText.setText(xname);
        EditText locationText = (EditText) findViewById(R.id.vacation_spot);
        locationText.setText(xlocation);
        EditText daysText = (EditText) findViewById(R.id.number_days);
        daysText.setText(xdays);
    }
}
