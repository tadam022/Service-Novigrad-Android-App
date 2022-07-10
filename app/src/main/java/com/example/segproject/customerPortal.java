package com.example.segproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class customerPortal extends AppCompatActivity {
    private Button logout;
    private Button findBranchButton;
    private Button viewNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_portal);

        logout = (Button) findViewById(R.id.button6logout);
        viewNotifications = (Button) findViewById(R.id.buttonNotifications);
        findBranchButton = (Button) findViewById(R.id.button4findBranch);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        findBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), customerServiceSearch.class);
                intent.putExtra("customerID", getIntent().getStringExtra("customerID"));
                startActivity(intent);
            }
        });

        viewNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), viewCustomerServiceRequests.class);
                intent.putExtra("customerID", getIntent().getStringExtra("customerID"));
                startActivity(intent);
            }
        });

    }
}