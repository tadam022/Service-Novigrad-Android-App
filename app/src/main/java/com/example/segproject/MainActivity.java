package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText emailScreen;
    EditText passwordScreen;
    TextView loginAttempts;
    Button registerButton;
    Button loginButton;
    FirebaseAuth firebase;
    ProgressBar progressbar;
    DatabaseReference referenceUsers;
    int progress;
    ArrayList<User> usersList = new ArrayList<User>();
    ArrayList<String> userIDs = new ArrayList<String>();
    @Override
    protected void onStart() {

        super.onStart();
        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        referenceUsers = firebaseData.getReference("Users");


        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (usersList!= null){
                    for(DataSnapshot servicesSnapShot : snapshot.getChildren()){
                        User user = servicesSnapShot.getValue(User.class);
                        usersList.add(user);
                        userIDs.add(servicesSnapShot.getKey());
                    }

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
        setContentView(R.layout.activity_main);

        progressbar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        progressbar.setVisibility(View.INVISIBLE);
        emailScreen = (EditText) findViewById(R.id.editTextUsername);
        passwordScreen = (EditText) findViewById(R.id.editTextPassword);
        loginAttempts = (TextView) findViewById(R.id.textViewLoginAttempts);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        firebase = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebase.getCurrentUser();



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registerType.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener((v) -> {

            String email = emailScreen.getText().toString();
            String password = passwordScreen.getText().toString();

            if(email.isEmpty() || password.isEmpty()){
                Toast toast = Toast.makeText(getApplicationContext(), "Please input your password and username to login", Toast.LENGTH_SHORT);
                toast.show();

            }

            else{ // might be a better way to do this
                User user = null;
                if(usersList == null || usersList.size() == 0){
                    user = null;
                }
                else{
                    for(User u: usersList){
                        if(email.equals(u.getEmail()) && password.equals(u.getPassword())){
                            user = u;
                        }
                    }
                }

                if(user == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else{
                    Intent intent = new Intent(getApplicationContext(), UserPortal.class);
                    intent.putExtra("userType", user.getAccountType());


                    intent.putExtra("firstName", user.getFirstName());
                    intent.putExtra("uID", user.getUID());
                    startActivity(intent);
                }








                /*
                firebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressbar.setVisibility(View.VISIBLE);
                            Toast toast = Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(getApplicationContext(), UserPortal.class);
                            startActivity(intent);
                        }
                        else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                }); */

            }
        });
    }

    public void retrieve(String email, String password){


    }


}