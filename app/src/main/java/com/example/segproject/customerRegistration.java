package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class customerRegistration extends AppCompatActivity {
    String firstName;
    String lastName;
    String accountType;
    String email;
    String password;
    String selection;
    EditText userFirstName;
    EditText userLastName;
    EditText userEmail;
    EditText userPassword;
    Button register;
    Button login;
    ProgressBar progressbar;
    FirebaseAuth authenticator;
    DatabaseReference firebasedataRoot;

    TextView title;
    TextView registering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cusomter_registration);
        selection = getIntent().getStringExtra("userType");
        title = (TextView) findViewById(R.id.textViewTitle);
        if(selection.equals("customer")){
            accountType = "customer";
            title.setText("New Customer registration");
        }
        else{
            accountType = "employee";
            title.setText("New Novigrad Employee registration");
        }
        progressbar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        progressbar.setVisibility(View.INVISIBLE);

        registering = (TextView) findViewById(R.id.textViewRegistering);
        authenticator = FirebaseAuth.getInstance();
        register = (Button) findViewById(R.id.button);
        login = (Button) findViewById(R.id.buttonLogin);
        userFirstName = (EditText) findViewById(R.id.editTextFirstName);
        userLastName = (EditText) findViewById(R.id.editTextLastName);
        userEmail = (EditText) findViewById(R.id.editTextemail);
        userPassword = (EditText) findViewById(R.id.editTextTextPassword);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString();
                password = userPassword.getText().toString();
                firstName = userFirstName.getText().toString();
                lastName = userLastName.getText().toString();

                CharSequence successText = "Registered successfully";
                CharSequence failText = "Registration failed";
                if(checkIfValid(email, password, firstName,lastName)){
                    progressbar.setVisibility(View.VISIBLE);
                    registering.setText("Registering...");

                    firebasedataRoot = FirebaseDatabase.getInstance().getReference();

                    Query query = firebasedataRoot.child("Users").orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(!snapshot.exists()){
                                DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");

                                if(selection.equals("customer")){
                                    String id = referenceUser.push().getKey();
                                    Customer user = new Customer(id,firstName,lastName,password,email);
                                    referenceUser.child(id).setValue(user);
                                }
                                else{
                                    String id = referenceUser.push().getKey();
                                    Employee user = new Employee(id,firstName,lastName,password,email,null);
                                    referenceUser.child(id).setValue(user);
                                }
                                Toast toast = Toast.makeText(getApplicationContext(), successText, Toast.LENGTH_SHORT);
                                toast.show();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);


                            }
                            else{
                                Toast toast = Toast.makeText(getApplicationContext(), "This email already belongs to a registered account. Please login with the button below.", Toast.LENGTH_SHORT);
                                toast.show();
                                progressbar.setVisibility(View.INVISIBLE);
                                registering.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast toast = Toast.makeText(getApplicationContext(), failText, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
                else{
                    registering.setText("");
                }



            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean checkIfValid(String email, String password, String firstName, String lastName){

        if(email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast toast = Toast.makeText(getApplicationContext(), "Please input a valid email address", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!isAlphabetical(firstName) || !isAlphabetical(lastName)){
            Toast toast = Toast.makeText(getApplicationContext(), "First and last names can only contain letters and hyphens", Toast.LENGTH_SHORT);
            toast.show();
            return false;

        }
        return true;
    }

    private boolean isAlphabetical(String s){
        char[] charArray = s.toCharArray();
        for(char c: charArray){
            if(!Character.isLetter(c) && c!= '-'){ // maybe flag for only one hyphen
                return false;
            }
        }
        return true;
    }



}