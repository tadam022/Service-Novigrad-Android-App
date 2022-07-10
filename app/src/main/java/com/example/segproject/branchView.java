package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class branchView extends AppCompatActivity {
    private TextView branchNameDisplay;
    private TextView branchAddressDisplay;
    private TextView branchPostalDisplay;
    private TextView branchPhoneDisplay;

    private TextView mondayHoursDisplay;
    private TextView tuesdayHoursDisplay;
    private TextView wednesdayHoursDisplay;
    private TextView thursdayHoursDisplay;
    private TextView fridayHoursDisplay;
    private TextView saturdayHoursDisplay;
    private TextView sundayHoursDisplay;
    private TextView branchRatingDisplay;

    private ArrayList<String> servicesDisplayList = new ArrayList<String>();
    private TextView servicesDisplay;
    private Button back;
    @Override
    protected void onStart() {
        super.onStart();
        String id = getIntent().getStringExtra("uID");
        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        DatabaseReference referenceUsers = firebaseData.getReference("Users");
        //addValueEventListener
        referenceUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                servicesDisplayList.clear();

                Employee employee = snapshot.getValue(Employee.class);
                branchNameDisplay.setText(employee.getBranch().getBranchName());
                branchAddressDisplay.setText("Address: " +employee.getBranch().getAddress());
                branchPostalDisplay.setText("Postal Code: " +employee.getBranch().getPostal());
                branchPhoneDisplay.setText("Phone number: " + employee.getBranch().getPhoneNumber());
                ArrayList<ArrayList<Integer>> Hours = employee.getBranch().getWorkingHours();
                setupHourDisplays(Hours);
                double rating;
                double rounded;
                int size;
                try{
                    rating = employee.getBranch().getBranchRating();
                    rounded = roundToNearestHalf(employee.getBranch().getBranchRating());
                    size = employee.getBranch().getRatedBy().size();
                    branchRatingDisplay.setText("Rating (rounded): " + rounded + " / 5.0" + "\n" + "Rated by : " + size + " customers." );
                }
                catch(Exception e){   // null pointer if no ratings
                    branchRatingDisplay.setText("No ratings yet" );
                }



                servicesDisplay.setText("Services: ");

                for(Services s: employee.getBranch().getBranchServices()){
                    try{
                        servicesDisplay.setText(servicesDisplay.getText().toString() + s.getServiceName() + " , ");
                    }
                    catch(Exception exception){
                        // Null pointer exception  shouldn't occur now because this onDataChange is a single value event listener so the listener should be destroyed so other onDataChange calls in files wont activate this one
                    }


                }
                if(employee.getBranch().getBranchServices().size()== 0 || employee.getBranch().getBranchServices() == null){
                    servicesDisplay.setText("Services: None offered as of now.");
                }
                else{
                    servicesDisplay.setText(servicesDisplay.getText().toString().substring(0,servicesDisplay.getText().toString().length()-2));
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
        setContentView(R.layout.activity_branch_view);
        back = (Button) findViewById(R.id.buttonDoneViewing);
        branchNameDisplay = (TextView) findViewById(R.id.txtView13Name);
        branchAddressDisplay = (TextView) findViewById(R.id.txtView14Address);
        branchPostalDisplay = (TextView) findViewById(R.id.txtView15Postal);
        branchPhoneDisplay = (TextView) findViewById(R.id.txtView16Phone);
        branchRatingDisplay = (TextView) findViewById(R.id.textViewBranchRating);
        mondayHoursDisplay = (TextView) findViewById(R.id.textView18Monday);
        tuesdayHoursDisplay = (TextView) findViewById(R.id.textView19Tuesday);
        wednesdayHoursDisplay = (TextView) findViewById(R.id.textView20Wednesday);
        thursdayHoursDisplay = (TextView) findViewById(R.id.textView21Thursday);
        fridayHoursDisplay = (TextView) findViewById(R.id.textView22Friday);
        saturdayHoursDisplay = (TextView) findViewById(R.id.textView23Saturday);
        sundayHoursDisplay = (TextView) findViewById(R.id.textView24Sunday);

        servicesDisplay = (TextView) findViewById(R.id.textViewServiceNamesDisplay);
        String id = getIntent().getStringExtra("uID");
        String serviceName = getIntent().getStringExtra("serviceName");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().hasExtra("isCustomer")){
                    if(getIntent().getStringExtra("isCustomer").equals("YES")){
                        finish();
                        Intent intent = new Intent(getApplicationContext(), searchResultsList.class);
                        intent.putExtra("isCustomer", "YES");
                        intent.putExtra("serviceName", serviceName);
                        intent.putExtra("uID", id); // employee id
                        intent.putExtra("customerID", getIntent().getStringExtra("customerID"));
                        intent.putExtra("firstHour", getIntent().getIntExtra("firstHour" ,0));
                        intent.putExtra("secondHour", getIntent().getIntExtra("secondHour",0));
                        if(getIntent().hasExtra("address") && getIntent().getStringExtra("address")!=null){
                                if(!getIntent().getStringExtra("address").equals("")){
                                    intent.putExtra("address",getIntent().getStringExtra("address"));
                                }

                        }

                        startActivity(intent);
                    }
                }

                else{
                    finish();
                    Intent intent = new Intent(getApplicationContext(), employeePortal.class);
                    intent.putExtra("employeeID", id);
                    startActivity(intent);

                }

            }
        });

    }

    public void setupHourDisplays(ArrayList<ArrayList<Integer>> Hours ){

        if(Hours.get(0).get(0) == 0 && Hours.get(0).get(1) == 0){
            mondayHoursDisplay.setText("Closed Mondays");
        }
        else{
            mondayHoursDisplay.setText("Open Monday from:  " + Hours.get(0).get(0) + " : 00" + " to " + Hours.get(0).get(1) + " : 00");
        }

        if(Hours.get(1).get(0) == 0 && Hours.get(1).get(1) == 0){
            tuesdayHoursDisplay.setText("Closed Tuesdays");
        }
        else{
            tuesdayHoursDisplay.setText("Open Tuesday from:  " + Hours.get(1).get(0) + " : 00" + " to " + Hours.get(1).get(1) + " : 00");
        }

        if(Hours.get(2).get(0) == 0 && Hours.get(2).get(1) == 0){
            wednesdayHoursDisplay.setText("Closed Wednesday");
        }
        else{
            wednesdayHoursDisplay.setText("Open Wednesday from:  " + Hours.get(2).get(0) + " : 00" + " to " + Hours.get(2).get(1) + " : 00");
        }

        if(Hours.get(3).get(0) == 0 && Hours.get(3).get(1) == 0){
            thursdayHoursDisplay.setText("Closed Thursdays");
        }
        else{
            thursdayHoursDisplay.setText("Open Thursday from:  " + Hours.get(3).get(0) + " : 00" + " to " + Hours.get(3).get(1) + " : 00");
        }

        if(Hours.get(4).get(0) == 0 && Hours.get(4).get(1) == 0){
            fridayHoursDisplay.setText("Closed Fridays");
        }
        else{
            fridayHoursDisplay.setText("Open Friday from:  " + Hours.get(4).get(0) + " : 00" + " to " + Hours.get(4).get(1) + " : 00");
        }

        if(Hours.get(5).get(0) == 0 && Hours.get(5).get(1) == 0){
            saturdayHoursDisplay.setText("Closed Saturdays");
        }
        else{
            saturdayHoursDisplay.setText("Open Saturday from:  " + Hours.get(5).get(0) + " : 00" + " to " + Hours.get(5).get(1) + " : 00");
        }

        if(Hours.get(6).get(0) == 0 && Hours.get(6).get(1) == 0){
            sundayHoursDisplay.setText("Closed Sundays");
        }
        else{
            sundayHoursDisplay.setText("Open Sunday from:  " + Hours.get(6).get(0) + " : 00" + " to " + Hours.get(6).get(1) + " : 00");
        }

    }

    public static double roundToNearestHalf(double rating){

        return Math.round(rating * 2) / 2.0;
    }
}