package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class deleteAccountScreen extends AppCompatActivity {
    EditText emailInput;
    Button deletebtn;
    Button back;
    FirebaseAuth firebase;

    FirebaseDatabase firebasedata;
    DatabaseReference referenceService;
    ArrayList<User> listOfUsers = new ArrayList<User>();
    ArrayList<String> userKeys = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_screen);
        emailInput = (EditText) findViewById(R.id.editTextEnterEmail);
        deletebtn = (Button) findViewById(R.id.button2delete);
        back = (Button) findViewById(R.id.btnBack);


        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        referenceService = firebaseData.getReference("Users");

        referenceService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (listOfUsers!= null){
                    for(DataSnapshot servicesSnapShot : snapshot.getChildren()){
                        User user = servicesSnapShot.getValue(User.class);
                        listOfUsers.add(user);
                        userKeys.add(servicesSnapShot.getKey());

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                if(email.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please input an email", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(email.equals("admin")){
                    Toast toast = Toast.makeText(getApplicationContext(), "You cannot delete the admin account", Toast.LENGTH_SHORT);
                    toast.show();
                    emailInput.setText("");
                }
                else{

                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    Query query = root.child("Users").orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int index = 0;
                            for(User u: listOfUsers){
                                if(u.getEmail().equals(email)){
                                    break;

                                }
                                index++;
                            }

                            //String key = snapshot.getKey(); // this just gives "Users"

                            //emailInput.setText(key);
                            if(!snapshot.exists()){
                                Toast toast = Toast.makeText(getApplicationContext(), "No account with this email", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                String key = userKeys.get(index);
                                firebasedata = FirebaseDatabase.getInstance();
                                referenceService = firebasedata.getReference("Users");

                                referenceService.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"account deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Unable to delete account", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    emailInput.setText("");
                }





            }



        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),adminPortal.class);
                startActivity(intent);

            }
        });
    }


}