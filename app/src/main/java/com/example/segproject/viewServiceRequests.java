package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewServiceRequests extends AppCompatActivity {
    private Button goBackToPortal;
    private TextView serviceRequestsAddedDisplay;
    private ArrayList<customerServiceRequests> listOfRequests;
    private RecyclerView recyclerViewRequests;

    @Override
    protected void onStart() {
        super.onStart();
        listOfRequests = new ArrayList<customerServiceRequests>();
        String employeeID = getIntent().getStringExtra("uID");
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        //addListener for single value event - child("branch")
        root.child("Users").child(employeeID).child("branch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfRequests.clear();

                if(snapshot.exists()){
                    //Toast.makeText(getApplicationContext(),"Snapshot exists", Toast.LENGTH_SHORT).show();
                    //for(DataSnapshot snap: snapshot.getChildren()){
                        //Employee e = snapshot.getValue(Employee.class);
                        Branch b = snapshot.getValue(Branch.class);
                        try{
                            for(customerServiceRequests request: b.getServiceRequests()){

                                if(request.getStatus()== serviceRequestStatus.PENDING){ // dont show already denied / accepted requests to branch
                                    listOfRequests.add(request);
                                }

                                //listOfRequests.add(request);

                            }
                        }
                        catch(Exception exception){
                            // no requests

                        }

                   // }
                    updateRequestsList();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error: Cannot retrieve the branch data", Toast.LENGTH_SHORT).show();
                    // impossible to access this screen without being an employee and setting up a branch, so there must be a branch.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_requests);
        String id = getIntent().getStringExtra("uID");
        goBackToPortal = (Button) findViewById(R.id.button2goBack);
        serviceRequestsAddedDisplay = (TextView) findViewById(R.id.textViewServiceRequestsDisplay);
        recyclerViewRequests = (RecyclerView) findViewById(R.id.recyclerViewRequests);

        goBackToPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), employeePortal.class);
                intent.putExtra("employeeID", id);
                startActivity(intent);
            }
        });

    }

    public void updateRequestsList(){
        requestListAdapter adapter = new requestListAdapter(this, listOfRequests,getIntent().getStringExtra("uID"));
        recyclerViewRequests.setAdapter(adapter);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this));
        if(listOfRequests == null || listOfRequests.size() == 0){
            serviceRequestsAddedDisplay.setText("No New Service Requests submitted by Customers yet");
            //Toast.makeText(getApplicationContext(),"No service requests submitted as of now", Toast.LENGTH_SHORT).show();
        }

        listOfRequests = new ArrayList<customerServiceRequests>();
    }
}