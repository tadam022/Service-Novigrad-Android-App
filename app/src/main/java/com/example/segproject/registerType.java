package com.example.segproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class registerType extends AppCompatActivity {
    Button customerButton;
    Button employeeButton;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);

        customerButton = (Button) findViewById(R.id.buttonCustomer);
        employeeButton = (Button) findViewById(R.id.buttonEmployee);
        back = (Button) findViewById(R.id.backToLogin);

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), customerRegistration.class);
                intent.putExtra("userType", "customer");
                startActivity(intent);
            }
        });

        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), customerRegistration.class);
                intent.putExtra("userType", "employee");
                startActivity(intent);
            }
        });

        back.setOnClickListener((v)->{
            finish();

        });
    }


}