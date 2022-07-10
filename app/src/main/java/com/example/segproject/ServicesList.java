package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class ServicesList extends AppCompatActivity {

    private DatabaseReference referenceService;

    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private ArrayList<Services> listOfServices = new ArrayList<Services>();
    private TextView displayifServices;
    private Button back;

    @Override
    protected void onStart() { // needs to be onStart for list to refresh

        super.onStart();
        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        referenceService = firebaseData.getReference("Services");


        referenceService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfServices.clear();
                    if(snapshot.exists()){
                        for(DataSnapshot servicesSnapShot : snapshot.getChildren()){
                            Services services = servicesSnapShot.getValue(Services.class);
                            listOfServices.add(services);

                        }
                        updateList();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"No Services", Toast.LENGTH_SHORT);
                    }


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
            setContentView(R.layout.activity_services_list);
            addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            back = (Button) findViewById(R.id.button2Back);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            displayifServices = (TextView) findViewById(R.id.textView10);
            displayifServices.setText("");







        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();///////////////////////////////////////////////////////////////////////////////////////////////////////////
                Intent intent = new Intent(getApplicationContext(), AddService.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), adminPortal.class);
                startActivity(intent);
            }
        });



    }


    public void updateList(){

        servicelistadapter adapter = new servicelistadapter(this,listOfServices);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(listOfServices == null || listOfServices.size() == 0){
            displayifServices.setText("There are currently no services added.");
        }
        listOfServices = new ArrayList<Services>();
    }
}