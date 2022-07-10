package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class employeePortal extends AppCompatActivity {

    private Button setup;
    private Button addBranchServices;
    private Button serviceRequests;
    private Button viewBranch;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_portal);
        setup = (Button) findViewById(R.id.buttonSetUp);
        addBranchServices = (Button) findViewById(R.id.buttonAddBranchServices);
        serviceRequests = (Button) findViewById(R.id.buttonServiceRequests);
        viewBranch = (Button) findViewById(R.id.buttonViewBranch);
        logout = (Button) findViewById(R.id.button2LogOut);


        String id = getIntent().getStringExtra("employeeID");
        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        DatabaseReference referenceUsers = firebaseData.getReference("Users");
        referenceUsers.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("branch")){
                    addBranchServices.setEnabled(true);
                    viewBranch.setEnabled(true);
                    setup.setEnabled(false);
                    serviceRequests.setEnabled(true);


                }
                else{
                    //Toast.makeText(getApplicationContext(), "Branch is not created.", Toast.LENGTH_LONG).show();
                    addBranchServices.setEnabled(false);
                    viewBranch.setEnabled(false);
                    setup.setEnabled(true);
                    serviceRequests.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                throw error.toException();
            }
        });
        //Toast.makeText(getApplicationContext(), "branch value " + branchCreated, Toast.LENGTH_LONG).show();


        viewBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), branchView.class);
                intent.putExtra("uID", id);

                startActivity(intent);
            }
        });

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), setupBranch.class);
                intent.putExtra("uID", id);
                startActivity(intent);
            }
        });

        addBranchServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), setupBranchServices.class);
                intent.putExtra("uID", id);
                startActivity(intent);
            }
        });

        serviceRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), viewServiceRequests.class);
                intent.putExtra("uID", id);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}