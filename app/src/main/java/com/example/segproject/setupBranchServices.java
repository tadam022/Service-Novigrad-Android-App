package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class setupBranchServices extends AppCompatActivity {
    TextView branchServicesDisplay;
    private ArrayList<Services> listOfBranchServices = new ArrayList<Services>();
    private Button doneBranchServices;
    private RecyclerView recycler;
    private ArrayList<Services> newServices = new ArrayList<Services>();
    private branchServiceListAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        DatabaseReference referenceService = firebaseData.getReference("Services");
        referenceService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfBranchServices.clear();

                for(DataSnapshot servicesSnapShot : snapshot.getChildren()){
                    Services services = servicesSnapShot.getValue(Services.class);
                    listOfBranchServices.add(services);

                }
                String id = getIntent().getStringExtra("uID");
                updateList(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                throw error.toException();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listOfBranchServices = new ArrayList<Services>();

        setContentView(R.layout.activity_setup_branch_services);
        branchServicesDisplay = findViewById(R.id.textViewBranchServices);
        branchServicesDisplay.setText("");
        doneBranchServices = (Button) findViewById(R.id.buttonBranchServicesDone);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        String id = getIntent().getStringExtra("uID");

        doneBranchServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateEmployee(id);
                finish();
                Intent intent = new Intent(getApplicationContext(), employeePortal.class);
                intent.putExtra("employeeID",id);
                startActivity(intent);
            }
        });
    }


    public void updateList(String id){

        adapter = new branchServiceListAdapter(this,listOfBranchServices, id, newServices);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        if(listOfBranchServices == null || listOfBranchServices.size() == 0){
            branchServicesDisplay.setText("There are currently no services that you can provide.");
        }
        else{
            newServices = adapter.getDesiredServices();
            Log.d("new services:", newServices.toString());
        }



    }



    public void updateEmployee(String uid){
        //String uid = getIntent().getStringExtra("uID");
        try{
            DatabaseReference userNode = FirebaseDatabase.getInstance().getReference("Users");
            userNode.child(uid).child("branch").child("branchServices").setValue(newServices);
            Toast.makeText(getApplicationContext(), "Branch's Provided Services updated successfully", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Unable to update branch's provided services: You must set up the branch first.", Toast.LENGTH_LONG).show();
        }

        /*
        userNode.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    try {
                        Employee emp = snapshot.getValue(Employee.class);
                        emp.getBranch().setBranchServices(newServices);
                        branchServicesDisplay.setText(uid);
                        userNode.child(uid).setValue(emp);
                        Toast.makeText(getApplicationContext(), "Branch's Provided Services updated successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable to update branch's provided services", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unable to update branch's provided services", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */


    }



}