package com.example.segproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceInfoDisplay extends AppCompatActivity {
    TextView serviceTitleDisplay;
    TextView serviceFormDisplay;
    TextView serviceDocumentsDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info_display);

        serviceTitleDisplay = (TextView) findViewById(R.id.textView7);
        serviceFormDisplay = (TextView) findViewById(R.id.textView8);
        serviceDocumentsDisplay = (TextView) findViewById(R.id.textView9);

        if(getIntent().hasExtra("name") && getIntent().hasExtra("form") && getIntent().hasExtra("documents")){
            serviceTitleDisplay.setText(getIntent().getStringExtra("name"));
            serviceFormDisplay.setText(getIntent().getStringExtra("form"));
            serviceDocumentsDisplay.setText(getIntent().getStringExtra("documents"));
        }
        else{
            Toast.makeText(this,"Unable to retrieve service details.", Toast.LENGTH_SHORT).show();

        }
    }
}