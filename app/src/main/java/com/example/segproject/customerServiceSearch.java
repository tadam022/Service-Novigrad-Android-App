package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class customerServiceSearch extends AppCompatActivity implements NumberPicker.OnValueChangeListener , AdapterView.OnItemSelectedListener {
    private Spinner spinnerServiceName;

    private List<String> listOfServiceNames;

    private Button backToPortal;
    private Button search;
    private EditText addressEdit;
    private String selectedServiceName;
    private NumberPicker start;
    private NumberPicker end;
    private NumberPicker AMorPM;
    private NumberPicker AMorPM2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_search);

        listOfServiceNames = new ArrayList<String>();

        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        DatabaseReference referenceService = firebaseData.getReference("Services");


        referenceService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfServiceNames.clear();

                for(DataSnapshot servicesSnapShot : snapshot.getChildren()){
                    Services service = servicesSnapShot.getValue(Services.class);
                    listOfServiceNames.add(service.getServiceName());

                }
                ArrayAdapter<String> arrayAdapterServiceNames = new ArrayAdapter<String>(customerServiceSearch.this, android.R.layout.simple_spinner_item, listOfServiceNames);
                arrayAdapterServiceNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //arrayAdapterServiceNames.notifyDataSetChanged();
                spinnerServiceName.setAdapter(arrayAdapterServiceNames);
                //spinnerServiceName.setOnItemSelectedListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                throw error.toException();
            }
        });



        

        backToPortal = (Button) findViewById(R.id.buttonCustomerPortal2);
        search = (Button) findViewById(R.id.buttonToSearchResults);
        addressEdit = (EditText) findViewById(R.id.editTextAddressSearch);
        spinnerServiceName = (Spinner) findViewById(R.id.spinnerService);




        start = (NumberPicker) findViewById(R.id.startHour);
        end = (NumberPicker) findViewById(R.id.endHour);
        AMorPM = (NumberPicker) findViewById(R.id.time3);
        AMorPM2 = (NumberPicker) findViewById(R.id.time2);

        start.setMinValue(0);
        start.setMaxValue(11);

        end.setMinValue(0);
        end.setMaxValue(11);

        AMorPM.setMinValue(0);
        AMorPM.setMaxValue(1);
        AMorPM2.setMinValue(0);
        AMorPM2.setMaxValue(1);
        AMorPM.setDisplayedValues(new String[] {"AM", "PM"});
        AMorPM2.setDisplayedValues(new String[] {"AM", "PM"});

        AMorPM.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        AMorPM2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        backToPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), customerPortal.class);
                intent.putExtra("customerID", getIntent().getStringExtra("customerID"));
                startActivity(intent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int first = start.getValue();
                int second = end.getValue();
                if(AMorPM.getValue() == 1){
                    first += 12;
                }
                if(AMorPM2.getValue() == 1){
                    second+= 12;
                }

                if(validSearchHours(first,second)){

                    finish();
                    Intent intent = new Intent(getApplicationContext(), searchResultsList.class);
                    intent.putExtra("customerID", getIntent().getStringExtra("customerID")); ///////////// CUSTOMER ID
                    intent.putExtra("firstHour", first);
                    intent.putExtra("secondHour", second);
                    //intent.putExtra("serviceName", selectedServiceName);
                    intent.putExtra("serviceName", (String) spinnerServiceName.getSelectedItem());
                    intent.putExtra("address", addressEdit.getText().toString());
                    startActivity(intent);

                }
                else{
                    if(first > second){
                        Toast.makeText(getApplicationContext(),"Opening hours cannot be later than closing hours.", Toast.LENGTH_SHORT).show();

                    }
                    /*
                    else if(first == second){
                        Toast.makeText(getApplicationContext(),"The opening and closing hours cannot be at the same time.", Toast.LENGTH_SHORT).show();

                    }
                     */
                }
            }
        });

    }



    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    private boolean validSearchHours(int hour1, int hour2){
        if(hour1 == 0 && hour2 == 0){
            return true;
        }
        if(hour1 >= hour2){
            return false;
        }
        return true;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(this, "Nothing selected", Toast.LENGTH_LONG).show();
    }
}