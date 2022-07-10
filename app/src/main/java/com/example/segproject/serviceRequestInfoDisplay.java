package com.example.segproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class serviceRequestInfoDisplay extends AppCompatActivity {
    private TextView firstDisplay;
    private TextView lastDisplay;
    private TextView dobDisplay;
    private TextView addressDisplay;
    private TextView licenseDisplay;
    private TextView appointmentDateDisplay;
    private TextView statusDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request_info_display);
        firstDisplay = (TextView) findViewById(R.id.textView21a);
        lastDisplay = (TextView) findViewById(R.id.textView23a);
        dobDisplay = (TextView) findViewById(R.id.textView24a);
        addressDisplay = (TextView) findViewById(R.id.textView25a);
        licenseDisplay = (TextView) findViewById(R.id.textView26a);
        appointmentDateDisplay = (TextView) findViewById(R.id.textView27a);
        statusDisplay = (TextView) findViewById(R.id.textView28a);

        String first = getIntent().getStringExtra("firstName");
        String last = getIntent().getStringExtra("lastName");
        String dob = getIntent().getStringExtra("dob");
        String address = getIntent().getStringExtra("address");
        String license = getIntent().getStringExtra("licenseType");
        String date = getIntent().getStringExtra("chosenDate");
        String status = getIntent().getStringExtra("status");

        if(first== null || first == ""){
            firstDisplay.setText("Not required");

        }
        else{
            firstDisplay.setText("First Name: "+ first);
        }
        if(last== null || last == ""){
            lastDisplay.setText("Not required");

        }
        else{
            lastDisplay.setText("Last Name: "+ last);
        }
        if(dob== null || dob == ""){
            dobDisplay.setText("D.O.B Not required");

        }
        else{
            dobDisplay.setText("D.O.B: "+ dob);
        }
        if(address == null || address == ""){
            addressDisplay.setText("Address Not required");

        }
        else{
            addressDisplay.setText("Address: "+ first);
        }
        if(license == null || license == ""){
            licenseDisplay.setText("License Type Not required");

        }
        else{
            licenseDisplay.setText("License type: "+ first);
        }

        appointmentDateDisplay.setText("Appointment date: " + date);
        statusDisplay.setText("Status: " + status);



    }


}