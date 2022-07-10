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

public class searchResultsList extends AppCompatActivity {
    private TextView addressView;
    private TextView firstHour;
    private TextView secondHour;
    private TextView nameView;
    private Button backToSearch;
    private ArrayList<Branch> listOfBranches;
    private ArrayList<String> listOfIDS;
    private int firstHourSearch;
    private int secondHourSearch;
    private String serviceNameSearch;
    private String addressSearch;
    private RecyclerView recyclerViewBranches;

    @Override
    protected void onStart() {
        super.onStart();
        listOfBranches = new ArrayList<Branch>();
        listOfIDS = new ArrayList<String>();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        Query query = root.child("Users").orderByChild("accountType").equalTo("employee");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfBranches.clear();
                listOfIDS.clear();
                if(snapshot.exists()){
                    //Toast.makeText(getApplicationContext(),"Snapshot exists", Toast.LENGTH_SHORT).show();
                    for(DataSnapshot snap: snapshot.getChildren()){
                        Employee e = snap.getValue(Employee.class);
                        if(e.getBranch() != null){ // branch must be set up
                            String address;


                            boolean anyHours = false;
                            boolean matchHours = false;
                            boolean anyName = false;
                            boolean matchName = false;
                            if(addressView.getText().toString().equals("")){
                                address = e.getBranch().getAddress();
                            }
                            else{
                                address = addressSearch;
                            }

                            if(nameView.getText().toString().equals("")){
                                anyName = true;
                            }
                            else{
                                for(int i = 0; i < e.getBranch().getBranchServices().size(); i++){
                                    try{

                                        if(e.getBranch().getBranchServices().get(i).getServiceName().toString().equals(serviceNameSearch)){
                                            matchName = true;
                                            //Toast.makeText(getApplicationContext(),"MATCHED NAME", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    catch(IndexOutOfBoundsException exception){

                                    }

                                }
                            }
                            Log.i("hour values ", "first: " + firstHourSearch + "   second: " + secondHourSearch);
                            if(!(firstHourSearch ==0 && secondHourSearch==0)){
                                for(int i = 0; i < 7; i++){
                                    try{
                                        if(firstHourSearch >= e.getBranch().getWorkingHours().get(i).get(0) ){
                                            if(secondHourSearch <= e.getBranch().getWorkingHours().get(i).get(1)){
                                                if(!(e.getBranch().getWorkingHours().get(i).get(0) == 0 && e.getBranch().getWorkingHours().get(i).get(1) ==0)){ // Closed days

                                                    matchHours = true;
                                                    //Toast.makeText(getApplicationContext(),"MATCHED Hours: " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                                                    break;
                                                }

                                            }
                                        }
                                    }
                                    catch(IndexOutOfBoundsException exception){
                                        Log.i("exception index bounds", "out of bounds exception");
                                    }

                                }
                            }
                            else{
                                anyHours = true;
                            }


                            // could use .contains() for substrings...
                            String address1 =  removePunctuation(e.getBranch().getAddress().replaceAll(" ","").toLowerCase());
                            String address3 = removePunctuation(e.getBranch().getPostal().replaceAll(" ","").toLowerCase());
                            String address2 = removePunctuation(address.replaceAll(" ","").toLowerCase());
                            //address1 = removePunctuation(address1);
                            //address2 = removePunctuation(address2);
                            //address3 = removePunctuation(address3);

                            Log.i("addresses: ", " 1: " + address1 + " 2: " + address2 + " 3: " + address3);
                            Log.i("hoursNames", String.valueOf(anyHours) + " " + String.valueOf(matchHours)+ " " + String.valueOf(anyName) + " " + String.valueOf(matchName));
                            if((address1.equals(address2) || address3.equals(address2))
                                    && (anyHours || matchHours) && (anyName || matchName)){
                                listOfBranches.add(e.getBranch());
                                listOfIDS.add(e.getUID());

                            }
                        }

                    }
                    updateBranchList();

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

    public String removePunctuation(String p){

        String output = "";
        for(Character c : p.toCharArray()){
            if(Character.isLetterOrDigit(c)){
                output += c;
            }
        }
        return output;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_list);
        nameView = (TextView) findViewById(R.id.textView14);
        addressView =  (TextView) findViewById(R.id.textView15);
        firstHour =  (TextView) findViewById(R.id.textView16);
        secondHour =  (TextView) findViewById(R.id.textView17);

        backToSearch = (Button) findViewById(R.id.buttonBackToSearch);

        recyclerViewBranches = (RecyclerView) findViewById(R.id.recyclerBranchView);




        if(getIntent().hasExtra("firstHour")){
            firstHourSearch = getIntent().getIntExtra("firstHour" ,0);

        }

        if(getIntent().hasExtra("secondHour")){
            secondHourSearch = getIntent().getIntExtra("secondHour",0);

        }

        if(!(firstHourSearch == 0 && secondHourSearch == 0)){
            firstHour.setText("Opens at: "+  String.valueOf(firstHourSearch) +":00");
            secondHour.setText("Closes at " + String.valueOf(secondHourSearch)  +":00");

        }
        else{
            firstHour.setText("");
            secondHour.setText("");
        }

        if(getIntent().hasExtra("address") && !getIntent().getStringExtra("address").equals("")){
            addressSearch = getIntent().getStringExtra("address");
            addressView.setText("Address: "+ addressSearch);

        }
        else{
            addressView.setText("");
        }

        if(getIntent().hasExtra("serviceName")){
            serviceNameSearch = getIntent().getStringExtra("serviceName");
            nameView.setText("Service name: "+ serviceNameSearch);

        }
        else{
            nameView.setText("");
        }

        backToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), customerServiceSearch.class);
                intent.putExtra("customerID", getIntent().getStringExtra("customerID"));
                startActivity(intent);
            }
        });
    }

    public void updateBranchList(){
        branchListAdapter adapter = new branchListAdapter(this, listOfBranches, listOfIDS, getIntent().getStringExtra("customerID"), firstHourSearch, secondHourSearch,
                addressSearch, serviceNameSearch);
        recyclerViewBranches.setAdapter(adapter);
        recyclerViewBranches.setLayoutManager(new LinearLayoutManager(this));
        if(listOfBranches == null || listOfBranches.size() == 0){
            Toast.makeText(getApplicationContext(),"No branches found with this search.", Toast.LENGTH_SHORT).show();

        }
        listOfBranches = new ArrayList<Branch>();
    }

    private void setUpDisplays(){


    }
}