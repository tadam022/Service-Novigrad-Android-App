package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserPortal extends AppCompatActivity {


    FirebaseDatabase firebaseData;
    String userType;
    String firstName;
    String UID;
    TextView welcomeScreen;
    Button logout;
    Button portal;
    Boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portal);
        welcomeScreen = (TextView) findViewById(R.id.textView);
        logout = (Button) findViewById(R.id.buttonLogOut);
        portal = (Button) findViewById(R.id.portalbtn);
        admin = false;
        if( getIntent().getExtras() != null){
            if(getIntent().getStringExtra("userType").equals("admin")){
                admin = true;


            }
            else if(getIntent().getStringExtra("userType").equals("employee")){

            }
            userType = getIntent().getStringExtra("userType");
            firstName = getIntent().getStringExtra("firstName");
            UID = getIntent().getStringExtra("uID");
        }

        firebaseData = FirebaseDatabase.getInstance();

        /*if(admin){ // no longer needed
            String displayText = ("Welcome admin! You are logged in as: admin.");
            welcomeScreen.setText(displayText);
        }
        else{*/
        String displayText = ("Welcome " + firstName + "! You are logged in as: " + userType + ".");
        welcomeScreen.setText(displayText);





        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        portal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals("admin")){
                    finish();/////////////////////////////////////////
                    Intent intent = new Intent(getApplicationContext(),adminPortal.class);
                    startActivity(intent);
                }

                else if(userType.equals("employee")){
                    finish();
                    Intent intent = new Intent(getApplicationContext(),employeePortal.class);
                    intent.putExtra("employeeID", UID);
                    startActivity(intent);

                }
                else{ // customer
                    finish();
                    Intent intent = new Intent(getApplicationContext(),customerPortal.class);
                    intent.putExtra("customerID", UID);
                    startActivity(intent);
                }


            }
        });
    }
}