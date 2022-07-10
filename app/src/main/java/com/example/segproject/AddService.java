package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddService extends AppCompatActivity {
    FirebaseDatabase firebasedata;
    DatabaseReference referenceService;
    EditText serviceName;
    TextView newServiceTitle;
    Button backButton;
    Button create;
    CheckBox first;
    CheckBox last;
    CheckBox address;
    CheckBox dob;
    CheckBox license;
    CheckBox status;
    CheckBox residence;
    CheckBox photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        newServiceTitle = (TextView) findViewById(R.id.textView3);
        serviceName = (EditText) findViewById(R.id.editTextServiceName);
        backButton = (Button) findViewById(R.id.buttonBackto);
        create = (Button) findViewById(R.id.buttonCreate);



        first = (CheckBox) findViewById(R.id.checkbox_first);
        last = (CheckBox) findViewById(R.id.checkbox_last);
        address = (CheckBox) findViewById(R.id.checkbox_address);
        license = (CheckBox) findViewById(R.id.checkbox_license);
        dob = (CheckBox) findViewById(R.id.checkbox_dob);
        status= (CheckBox) findViewById(R.id.checkbox_status);
        residence = (CheckBox) findViewById(R.id.checkbox_residence);
        photo = (CheckBox) findViewById(R.id.checkbox_photo);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),ServicesList.class);
                startActivity(intent);
            }
        });


    }

    private void addService(){
        String name = serviceName.getText().toString();
        if(isValidServiceName(name)){

            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            Query query = root.child("Services").orderByChild("serviceName").equalTo(name);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(!snapshot.exists()){

                        firebasedata = FirebaseDatabase.getInstance();
                        referenceService = firebasedata.getReference("Services/");
                        String id = referenceService.push().getKey();
                        Services newService = new Services(id, name);
                        setUpBooleanValues(newService);
                        referenceService.child(id).setValue(newService);
                        serviceName.setText("");
                        uncheckBoxes();
                        Toast toast = Toast.makeText(getApplicationContext(), "Created new service successfully.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "There already exists a service with this name.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
        else {
            if (name == null || name.length() == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Please input the name of the service.", Toast.LENGTH_SHORT);
                toast.show();

            }
            else if(name.length() > 24){
                Toast toast = Toast.makeText(getApplicationContext(), "Service name cannot exceed 24 characters.", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "The service name cannot be just a number.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }



    public void openDialog(){

    }
    /*
    public boolean isValidServiceName(String name){

        if(name == null || name.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Please input the name of the service.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        name = name.replaceAll(" ","");
        // check if branch name is just a number
        try{
            double num = Double.parseDouble(name);
            //serviceName.setText("");
            Toast toast = Toast.makeText(getApplicationContext(), "The service name cannot be just a number.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        catch(NumberFormatException e){
            return true;
        }

    }*/

    public boolean isValidServiceName(String name){
        if(name.length() > 24){
            return false;
        }
        name = name.replaceAll(" ","");
        if(name == null || name.length() == 0){
            return false;
        }

        try{
            double num = Double.parseDouble(name);
            return false;
        }
        catch(NumberFormatException e){
            return true;
        }


    }

    public void onCheckboxClicked(View view) {

    }

    private void setUpBooleanValues(Services service){
        if(first.isChecked()){
            service.setFirstNameRequired(true);
        }

        if(last.isChecked()){
            service.setLastNameRequired(true);
        }
        if(license.isChecked()){
            service.setLicenseTypeRequired(true);
        }
        if(address.isChecked()){
            service.setAddressRequired(true);
        }
        if(dob.isChecked()){
            service.setDateofbirthRequired(true);
        }
        if(status.isChecked()){
            service.setProofOfStatusRequired(true);
        }
        if(residence.isChecked()){
            service.setProofofResidenceRequired(true);
        }
        if(photo.isChecked()){
            service.setPhotoRequired(true);
        }

    }

    private void uncheckBoxes(){
        if(first.isChecked()){
            first.toggle();
        }
        if(last.isChecked()){
            last.toggle();
        }
        if(license.isChecked()){
            license.toggle();
        }
        if(address.isChecked()){
            address.toggle();
        }
        if(dob.isChecked()){
            dob.toggle();
        }
        if(status.isChecked()){
            status.toggle();
        }
        if(residence.isChecked()){
            residence.toggle();
        }
        if(photo.isChecked()){
            photo.toggle();
        }

    }


}