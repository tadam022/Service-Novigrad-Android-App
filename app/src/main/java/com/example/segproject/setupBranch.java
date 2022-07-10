package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class setupBranch extends AppCompatActivity {

    private  Button finish;
    private Button setWorkingHours;
    private  EditText editBranchName;
    private EditText editBranchPhone;
    private EditText editBranchPostal;
    private  EditText editBranchAddress;
    private TextView hoursSetDisplay;

    private Employee employeeToEdit;
    private ArrayList<Services> listOfBranchServices = new ArrayList<Services>();
    private ArrayList<ArrayList<Integer>> hours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_branch);
        editBranchAddress = (EditText) findViewById(R.id.editTextBranchAddress);
        editBranchPhone = (EditText) findViewById(R.id.editTextBranchPhone);
        editBranchName = (EditText) findViewById(R.id.editTextBranchName);
        editBranchPostal = (EditText) findViewById(R.id.editTextBranchPostalCode);
        hoursSetDisplay = (TextView) findViewById(R.id.textViewHoursAreSet);
        finish = findViewById(R.id.button5);
        setWorkingHours = findViewById(R.id.buttonSetWorkingHours);
        boolean set = false;

        hours = new ArrayList<ArrayList<Integer>>();
        int[] h;
        for(int i = 0; i < 7 ; i++){
            if(getIntent().hasExtra(Integer.toString(i))){
                h = getIntent().getIntArrayExtra(Integer.toString(i));
                ArrayList<Integer> a = new ArrayList<Integer>();
                a.add(h[0]);
                a.add(h[1]);
                hours.add(a);
                set = true;
            }
        }
        if(set){
            hoursSetDisplay.setText("Hours set");
            editBranchAddress.setText(getIntent().getStringExtra("address"));
            editBranchPhone.setText(getIntent().getStringExtra("phone"));
            editBranchName.setText(getIntent().getStringExtra("name"));
            editBranchPostal.setText(getIntent().getStringExtra("postal"));
        }






        setWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(getApplicationContext(), pickWorkingHours.class);
                intent.putExtra("uID", getIntent().getStringExtra("uID"));
                intent.putExtra("name", editBranchName.getText().toString());
                intent.putExtra("postal", editBranchPostal.getText().toString());
                intent.putExtra("address", editBranchAddress.getText().toString());
                intent.putExtra("phone", editBranchPhone.getText().toString());
                startActivity(intent);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editBranchPhone.getText().toString();
                String address = editBranchAddress.getText().toString();
                String postal = editBranchPostal.getText().toString();
                String branchName = editBranchName.getText().toString();
                if(validBranchName(branchName) ){
                    if(validAddress(address) && validPhoneNumber(phone) && validPostal(postal)){
                        String uid = getIntent().getStringExtra("uID");
                        DatabaseReference userNode = FirebaseDatabase.getInstance().getReference("Users");
                        userNode.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    employeeToEdit = snapshot.getValue(Employee.class);
                                    try{

                                        listOfBranchServices = null;
                                        ArrayList<customerServiceRequests> ServiceRequests = new ArrayList<customerServiceRequests>();
                                        ArrayList<customerRatings> currentRatings = new ArrayList<customerRatings>();
                                        Branch branch = new Branch(phone, postal, branchName, address, hours, listOfBranchServices, currentRatings, 0, ServiceRequests);
                                    /*
                                    if(employeeToEdit == null){
                                        hoursSetDisplay.setText("emptoedit is null...");
                                    }*/
                                        employeeToEdit.setBranch(branch);
                                        userNode.child(uid).setValue(employeeToEdit);

                                        Toast toast2 = Toast.makeText(getApplicationContext(), "The Branch has been set up successfully", Toast.LENGTH_SHORT);
                                        toast2.show();
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), employeePortal.class);
                                        intent.putExtra("employeeID",uid);
                                        startActivity(intent);
                                    }
                                    catch(Exception e){
                                        Toast.makeText(getApplicationContext(),"Error updating branch", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
                                    }

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Error finding branch", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(getApplicationContext(),"Unable to retrieve info", Toast.LENGTH_SHORT).show();
                                throw error.toException();
                            }
                        });
                    }

                }
                else{
                    if(branchName.length() > 24){
                        Toast toast = Toast.makeText(getApplicationContext(), "Branch name cannot exceed 24 characters", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if(branchName == null || branchName.length() == 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "Please fill out Branch Name", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "The Branch name cannot be a number.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }

            }
        });

    }

    public boolean validBranchName(String branchName){
        if(branchName == null || branchName.length() == 0 || branchName.length() > 24){

            return false;
        }
        branchName = branchName.replaceAll(" ","");
        // check if branch name is just a number
        try{
            double num = Double.parseDouble(branchName);
        }
        catch(NumberFormatException e){
            return true;
        }
        Toast toast = Toast.makeText(getApplicationContext(), "The name cannot be just a number.", Toast.LENGTH_SHORT);
        toast.show();
        return false;
    }

    public boolean validAddress(String address){
        if(address == null || address.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill out address", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(address.length() > 30){
            Toast toast = Toast.makeText(getApplicationContext(), "Address cannot exceed 30 characters", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        address = address.replaceAll(" ","");
        try{
            double num = Double.parseDouble(address);
        }
        catch(NumberFormatException e){
            return true;
        }
        Toast toast = Toast.makeText(getApplicationContext(), "The address cannot be just a number.", Toast.LENGTH_SHORT);
        toast.show();
        return false;

    }

    public boolean validPostal(String postal){
        if(postal == null || postal.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter the postal code", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(postal.length() > 10){
            Toast toast = Toast.makeText(getApplicationContext(), "Postal code cannot exceed 10 characters", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;

    }

    public boolean validPhoneNumber(String phoneNumber){
        if(phoneNumber == null || phoneNumber.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter the phone number for the branch", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(phoneNumber.length() > 20){
            Toast toast = Toast.makeText(getApplicationContext(), "Phone number cannot exceed 20 characters", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;

    }
}