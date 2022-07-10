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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewCustomerServiceRequests extends AppCompatActivity {

    private Button backToCustomerPortal;
    private ArrayList<customerServiceRequests> customerRequests;
    private RecyclerView recyclerCustomerView;
    private TextView ifServicesDisplay;

    @Override
    protected void onStart() {
        super.onStart();

        customerRequests = new ArrayList<customerServiceRequests>();
        String customerID = getIntent().getStringExtra("customerID");
        Log.i("CUSTOMER ID", customerID);
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        Query query = root.child("Users").orderByChild("accountType").equalTo("employee");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerRequests.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snap: snapshot.getChildren()){
                        Employee e = snap.getValue(Employee.class);

                        if(e.getBranch()!=null){ // branch must be set up

                            Branch b = e.getBranch();

                            try{
                                for(customerServiceRequests r: b.getServiceRequests()){

                                    if(r.getCustomerID().equals(customerID)){

                                        customerRequests.add(r);

                                    }
                                }
                            }catch(NullPointerException exception){
                                // no customer service requests
                            }
                        }
                    }
                    updateCustomerList();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unfortunately, there are no branches currently operating", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_view_customer_service_requests);

        backToCustomerPortal = (Button) findViewById(R.id.button2BackCustomerPortal);
        recyclerCustomerView = (RecyclerView) findViewById(R.id.recyclerCustomerRequests);
        ifServicesDisplay = (TextView) findViewById(R.id.textView22);
        ifServicesDisplay.setText("Your Service Requests: ");
        backToCustomerPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), customerPortal.class);
                intent.putExtra("customerID", getIntent().getStringExtra("customerID"));
                startActivity(intent);
            }
        });
    }

    public void updateCustomerList(){
        customerViewRequestsAdapter adapter = new customerViewRequestsAdapter(this, customerRequests);
        recyclerCustomerView.setAdapter(adapter);
        recyclerCustomerView.setLayoutManager(new LinearLayoutManager(this));
        if(customerRequests == null || customerRequests.size() == 0){
            //Toast.makeText(getApplicationContext(),"No service requests submitted as of now", Toast.LENGTH_SHORT).show();
            ifServicesDisplay.setText("No service requests submitted as of now.");

        }

        customerRequests = new ArrayList<customerServiceRequests>();
    }
}