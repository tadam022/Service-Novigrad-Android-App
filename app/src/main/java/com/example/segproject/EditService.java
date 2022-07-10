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

public class EditService extends AppCompatActivity {

    private EditText serviceNameScreen;
    private TextView editServiceTitle;
    private Button backButtonedit;
    private Button updateServicebtn;
    private String id;
    private  Services serviceToEdit;

    private CheckBox firstEdit;
    private CheckBox lastEdit;
    private CheckBox addressEdit;
    private CheckBox dobEdit;
    private CheckBox licenseEdit;
    private CheckBox statusEdit;
    private CheckBox residenceEdit;
    private CheckBox photoEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        editServiceTitle = (TextView) findViewById(R.id.textViewEditService);
        serviceNameScreen = (EditText) findViewById(R.id.editTexteditServiceName);
        backButtonedit = (Button) findViewById(R.id.buttonBackto2);
        updateServicebtn = (Button) findViewById(R.id.buttonUpdate);

        firstEdit = (CheckBox) findViewById(R.id.checkbox_editfirst);
        lastEdit = (CheckBox) findViewById(R.id.checkbox_editlast);
        addressEdit = (CheckBox) findViewById(R.id.checkbox_editaddress);
        licenseEdit = (CheckBox) findViewById(R.id.checkbox_editlicense);
        dobEdit = (CheckBox) findViewById(R.id.checkbox_editdob);
        statusEdit = (CheckBox) findViewById(R.id.checkbox_editstatus);
        residenceEdit = (CheckBox) findViewById(R.id.checkbox_editresidence);
        photoEdit = (CheckBox) findViewById(R.id.checkbox_editphoto);

        

        if(getIntent().hasExtra("ID")){
            id = getIntent().getStringExtra("ID");
        }
        else{
            Toast.makeText(getApplicationContext(),"Unable to edit service", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(getApplicationContext(),ServicesList.class);
            startActivity(intent);
        }

        id = getIntent().getStringExtra("ID"); // make sure this exists first

        DatabaseReference serviceNode = FirebaseDatabase.getInstance().getReference("Services");
        serviceNode.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    serviceToEdit = snapshot.getValue(Services.class);
                    editServiceTitle.setText("Editing Service");
                    serviceNameScreen.setText(serviceToEdit.getServiceName());
                    firstEdit.setChecked(serviceToEdit.isFirstNameRequired());
                    lastEdit.setChecked(serviceToEdit.isLastNameRequired());
                    addressEdit.setChecked(serviceToEdit.isAddressRequired());
                    licenseEdit.setChecked(serviceToEdit.isLicenseTypeRequired());
                    dobEdit.setChecked(serviceToEdit.isDateofbirthRequired());
                    statusEdit.setChecked(serviceToEdit.isProofOfStatusRequired());
                    residenceEdit.setChecked(serviceToEdit.isProofofResidenceRequired());
                    photoEdit.setChecked(serviceToEdit.isPhotoRequired());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),"Unable to edit service", Toast.LENGTH_SHORT).show();
            }
        });


        updateServicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateService();
            }
        });

        backButtonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),ServicesList.class);
                startActivity(intent);
            }
        });


    }

    private void updateService(){
        try{
            id = getIntent().getStringExtra("ID"); // make sure this exists first
            DatabaseReference serviceNode = FirebaseDatabase.getInstance().getReference("Services");
            serviceToEdit.setServiceName(serviceNameScreen.getText().toString());
            setUpNewBooleanValues(serviceToEdit);
            serviceNode.child(id).setValue(serviceToEdit);
            String id = serviceToEdit.getId();
            updateServiceBranches(id,serviceToEdit);


            Toast.makeText(getApplicationContext(),"Updated service successfully", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Unable to edit service", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateServiceBranches(String id, Services editedService){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        Query query = root.child("Users").orderByChild("accountType").equalTo("employee");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap: snapshot.getChildren()){
                        Employee e = snap.getValue(Employee.class);
                        if(e.getBranch()!= null){
                            if(e.getBranch().getBranchServices()!= null){
                                for(int i = 0; i < e.getBranch().getBranchServices().size(); i++){
                                    try{
                                        if(e.getBranch().getBranchServices().get(i).getId().equals(id)){
                                            e.getBranch().getBranchServices().set(i,editedService);
                                            root.child("Users").child(e.getUID()).child("branch").child("branchServices").setValue(e.getBranch().getBranchServices());

                                        }
                                    }
                                    catch(IndexOutOfBoundsException exception){

                                    }

                                }

                            }
                        }


                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot update branch's service (no existing employee accounts)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onCheckboxClicked(View view) {

    }

    private void setUpNewBooleanValues(Services service){
        if(firstEdit.isChecked()){
            service.setFirstNameRequired(true);
        }

        if(lastEdit.isChecked()){
            service.setLastNameRequired(true);
        }
        if(licenseEdit.isChecked()){
            service.setLicenseTypeRequired(true);
        }
        if(addressEdit.isChecked()){
            service.setAddressRequired(true);
        }
        if(dobEdit.isChecked()){
            service.setDateofbirthRequired(true);
        }
        if(statusEdit.isChecked()){
            service.setProofOfStatusRequired(true);
        }
        if(residenceEdit.isChecked()){
            service.setProofofResidenceRequired(true);
        }
        if(photoEdit.isChecked()){
            service.setPhotoRequired(true);
        }

    }
}