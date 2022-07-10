package com.example.segproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class fillForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {


    private Spinner spinnerLicenseType;

    private Button back;
    private String bookingServiceName;
    private Services serviceToBook;
    private String employeeID;
    private String customerID;
    private Branch branch;
    private Button selectDate;
    private TextView bookingTitle;
    private EditText bookingFirstName;
    private EditText bookingLastName;
    private EditText bookingAddress;
    private EditText bookingDateOfBirth;
    private Button attachDocuments;
    private Button submit;
    private TextView dateSelectionDisplay;
    private TextView documentsDisplay;
    private TextView documentsAttached;
    private ArrayList<ArrayList<Integer>> workHours;
    private boolean documentsRequired;
    private boolean firstRequired;
    private boolean lastRequired;
    private boolean licenseRequired;
    private boolean dobRequired;
    private boolean addressRequired;
    private String branchName;

    @Override
    protected void onStart() {
        super.onStart();

        customerID = getIntent().getStringExtra("customerID");
        employeeID = getIntent().getStringExtra("uID");
        bookingServiceName = getIntent().getStringExtra("serviceName");
        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        DatabaseReference referenceUsers = firebaseData.getReference("Users");
        referenceUsers.child(employeeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   Employee e = snapshot.getValue(Employee.class);
                   branch = e.getBranch();
                   bookingTitle.setText("Booking for " + bookingServiceName + " at " + branch.getBranchName());
                    branchName = branch.getBranchName();
                   for(int i = 0; i < e.getBranch().getBranchServices().size(); i++){
                        if(e.getBranch().getBranchServices().get(i).getServiceName().equals(bookingServiceName)){
                            //bookingTitle.setText("Found");

                            Services s = e.getBranch().getBranchServices().get(i);
                            serviceToBook = s;
                            boolean documents = false;
                            if(serviceToBook.isPhotoRequired() || serviceToBook.isProofofResidenceRequired() || serviceToBook.isProofOfStatusRequired()){
                                documents = true;

                            }
                            setUpDisplays(serviceToBook.isFirstNameRequired(),serviceToBook.isLastNameRequired(),serviceToBook.isAddressRequired(), serviceToBook.isDateofbirthRequired(),
                                    serviceToBook.isLicenseTypeRequired(), documents);

                        }
                   }

                   try{
                       workHours = branch.getWorkingHours();
                   }
                   catch(Exception exception){
                       workHours = new ArrayList<ArrayList<Integer>>();
                       for(int i = 0; i < 7; i++){
                           ArrayList<Integer> a = new ArrayList<Integer>();
                           a.add(0);
                           a.add(0);
                           workHours.add(a);
                       }
                   }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot find branch", Toast.LENGTH_SHORT);
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


        setContentView(R.layout.activity_fill_form);
        back = (Button) findViewById(R.id.button4Back);
        documentsDisplay = (TextView) findViewById(R.id.textViewDocumentsDisplay);
        bookingTitle = (TextView) findViewById(R.id.textViewBookingTitle);
        attachDocuments = (Button) findViewById(R.id.buttonAttachDocuments);
        submit = (Button) findViewById(R.id.button4Submit);
        selectDate = (Button) findViewById(R.id.button2SelectDate);
        dateSelectionDisplay = (TextView) findViewById(R.id.textViewChosenDateDisplay);
        documentsAttached = (TextView) findViewById(R.id.textViewDocumentsAttached);
        dateSelectionDisplay.setText("");
        spinnerLicenseType = (Spinner) findViewById(R.id.spinnerLicenseType);
        ArrayList<String> listOfLicenseTypes = new ArrayList<String>(Arrays.asList("Select License Type","G1","G2","G"));
        ArrayAdapter<String> arrayAdapterServiceNames = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOfLicenseTypes);
        arrayAdapterServiceNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLicenseType.setAdapter(arrayAdapterServiceNames);



        bookingFirstName = (EditText) findViewById(R.id.editTextTextPersonName3);
        bookingLastName= (EditText) findViewById(R.id.editTextTextPersonName4);
        bookingAddress= (EditText) findViewById(R.id.editTextTextPersonName5);
        bookingDateOfBirth= (EditText) findViewById(R.id.editTextDateOfBirth);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), searchResultsList.class);
                intent.putExtra("isCustomer", "YES");
                intent.putExtra("serviceName", getIntent().getStringExtra("serviceName"));
                intent.putExtra("uID", getIntent().getStringExtra("uID"));
                intent.putExtra("customerID",getIntent().getStringExtra("customerID"));
                intent.putExtra("firstHour", getIntent().getIntExtra("firstHour" ,0));
                intent.putExtra("secondHour", getIntent().getIntExtra("secondHour",0));
                if(getIntent().hasExtra("address") && getIntent().getStringExtra("address")!=null){
                    if(!getIntent().getStringExtra("address").equals("")){
                        intent.putExtra("address",getIntent().getStringExtra("address"));
                    }

                }
                startActivity(intent);
            }
        });

        attachDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentsAttached.setText("Added images");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(validateInputs()){
                    String chosenDate = dateSelectionDisplay.getText().toString();
                    String serviceName = getIntent().getStringExtra("serviceName");
                    String first = bookingFirstName.getText().toString();
                    String last = bookingLastName.getText().toString();
                    String dob = bookingDateOfBirth.getText().toString();
                    String address= bookingAddress.getText().toString();
                    String license = spinnerLicenseType.getSelectedItem().toString();
                    String ID = getIntent().getStringExtra("customerID");
                    String empID = getIntent().getStringExtra("uID");
                    customerServiceRequests request = new customerServiceRequests(chosenDate, serviceName, first, last, dob, address, license, serviceRequestStatus.PENDING,ID, empID, branchName);

                    updateBranchServiceRequests(empID, request);
                        Toast.makeText(getApplicationContext(),"Service Request created successfully",Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), searchResultsList.class);
                        intent.putExtra("isCustomer", "YES");
                        intent.putExtra("serviceName", getIntent().getStringExtra("serviceName"));
                        intent.putExtra("uID", getIntent().getStringExtra("uID"));
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
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickDate = new datepickercustomer();
                pickDate.show(getSupportFragmentManager(),"Pick Date");
            }
        });
    }

    private void updateBranchServiceRequests(String id, customerServiceRequests serviceRequest){

        FirebaseDatabase firebaseData = FirebaseDatabase.getInstance();
        DatabaseReference referenceUsers = firebaseData.getReference("Users");
        referenceUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    // more concise to use branch variable instead but this already works
                    Employee e = snapshot.getValue(Employee.class);
                    Branch b = e.getBranch();
                    ArrayList<customerServiceRequests> temp = b.getServiceRequests();
                    try{

                        temp.add(serviceRequest);
                        b.setServiceRequests(temp);
                        referenceUsers.child(id).child("branch").setValue(b);
                    }
                    catch(Exception exception){
                        temp = new ArrayList<customerServiceRequests>();
                        temp.add(serviceRequest);
                        b.setServiceRequests(temp);
                        referenceUsers.child(id).child("branch").setValue(b);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error adding service request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                throw error.toException();
            }
        });



    }

    // Checks if date is in future, and not current date
    public boolean checkDate(int year, int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        if(year < currentYear ){
            return false;
        }
        else if(year == currentYear && month < currentMonth){
            return false;
        }
        else if(year == currentYear && month == currentMonth && dayOfMonth < currentDay){
            return false;
        }

        else if (year == currentYear && month == currentMonth && dayOfMonth == currentDay){
            return false;
        }


        return true;
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Toast.makeText(this,"year :" + year + " month: "+ month +" day: " + dayOfMonth, Toast.LENGTH_LONG).show();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        String dayName = new DateFormatSymbols().getWeekdays()[dayIndex];

        if(dayIndex >= 2){
            dayIndex -= 2;
        }
        else{ // if Sunday
            dayIndex = 6;
        }
        /*
        Log.i("parameters", "year :" + year + " month: "+ month +" day: " + dayOfMonth);
        Log.i("current","year :" + currentYear + " month: "+ currentMonth +" day: " + currentDay);
        Log.i("dayName", dayName);
        Log.i("dayIndex",String.valueOf(dayIndex));
        */

        if(year < currentYear ){
            Toast.makeText(getApplicationContext(),"You cannot select a date in the past.", Toast.LENGTH_SHORT).show();
        }
        else if(year == currentYear && month < currentMonth){
            Toast.makeText(getApplicationContext(),"You cannot select a date in the past.", Toast.LENGTH_SHORT).show();
        }
        else if(year == currentYear && month == currentMonth && dayOfMonth < currentDay){
            Toast.makeText(getApplicationContext(),"You cannot select a date in the past.", Toast.LENGTH_SHORT).show();
        }



        else if (year == currentYear && month == currentMonth && dayOfMonth == currentDay){
            Toast.makeText(getApplicationContext(),"You cannot make an appointment on the current date", Toast.LENGTH_SHORT).show();
        }
        else if(workHours.get(dayIndex).get(0)==0 && workHours.get(dayIndex).get(1) == 0){
            Toast.makeText(getApplicationContext(),"This branch is closed on " + dayName + "s", Toast.LENGTH_SHORT).show();
        }
        else{
            String monthName = new DateFormatSymbols().getMonths()[month]; // month-1?
            dateSelectionDisplay.setText(monthName + " " + dayOfMonth + " , " + year);
            Toast.makeText(getApplicationContext(),"Date selected", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean validateInputs(){
       // Log.i("truth values2", String.valueOf(firstRequired) + " " + String.valueOf(lastRequired)+ " " + String.valueOf(addressRequired) + " " + String.valueOf(dobRequired)
        //        + " " + String.valueOf(licenseRequired) + " " + String.valueOf(documentsRequired));
        if (!dateSelectionDisplay.getText().toString().isEmpty()){

            if(validName(bookingFirstName.getText().toString(), firstRequired)){

                if(validName(bookingLastName.getText().toString(), lastRequired)){

                    if(validAddress(bookingAddress.getText().toString(),addressRequired)){
                        if(validLicense(licenseRequired)){
                            if(validDateOfBirth(bookingDateOfBirth.getText().toString(),dobRequired)){
                                return true;
                            }
                        }
                    }

                }
            }


        }
        else{
            Toast.makeText(getApplicationContext(),"Please pick a date for booking",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean validName(String name, boolean required){

        if(!required){
            return true;
        }

        if(name.length() > 30){
            Toast.makeText(getApplicationContext(),"First and Last name cannot exceed 30 characters",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(name.isEmpty() && required){
            Toast.makeText(getApplicationContext(),"Please fill in name fields",Toast.LENGTH_SHORT).show();
            return false;
        }
        char[] charArray = name.toCharArray();
        for(char c: charArray){
            if(!Character.isLetter(c) && c!= '-'){ // maybe flag for only one hyphen
                Toast.makeText(getApplicationContext(),"Cannot use numbers in name",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean validAddress(String address, boolean required){

        if(!required){
            return true;
        }
        if((address == null || address.length() == 0) && required){
            Toast.makeText(getApplicationContext(), "Please fill out address", Toast.LENGTH_SHORT).show();

            return false;
        }

        if((address == null || address.length() == 0) && !required){
            return true;
        }


        if(address.length() > 30){
            Toast.makeText(getApplicationContext(), "Address cannot exceed 30 characters", Toast.LENGTH_SHORT).show();

            return false;
        }

        address = address.replaceAll(" ","");
        try{
            double num = Double.parseDouble(address);
        }
        catch(NumberFormatException e){
            return true;
        }
        //bookingAddress.setHintTextColor(Color.RED);
        //bookingAddress.setHint("The address cannot be just a number.");
        Toast.makeText(getApplicationContext(), "The address cannot be just a number.", Toast.LENGTH_SHORT).show();

        return false;



    }

    public boolean validLicense(boolean required){
        if(!required){
            return true;
        }
        if(spinnerLicenseType.getSelectedItem().toString().equals("Select License Type") && required){
            Toast.makeText(getApplicationContext(),"Please select your license type", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean validDateOfBirth(String dob, boolean required){

        if(!required){
            return true;
        }
        if(dob.isEmpty()  && required){
            Toast.makeText(getApplicationContext(), "Please fill in your date of birth", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dob.length()!= 10){
            Toast.makeText(getApplicationContext(), "Invalid Format. Must be dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try{
            Date date = format.parse(dob);
            int d = Integer.parseInt(dob.substring(0,2));
            int m = Integer.parseInt(dob.substring(3,5));
            int y = Integer.parseInt(dob.substring(6,10));
            if(!checkDate(y,m,d)){
                return true;
            }
            else{
                Toast.makeText(getApplicationContext(), "Invalid Date of Birth. Cannot be today or in the future.", Toast.LENGTH_SHORT).show();
                return false;
            }

        }catch (ParseException e){
            //bookingDateOfBirth.setHintTextColor(Color.RED);
            //bookingDateOfBirth.setHint("Invalid Date or Format. Must be dd/mm/yyyy");
            Toast.makeText(getApplicationContext(), "Invalid Date or Format. Must be dd/mm/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void setUpDisplays(boolean first, boolean last, boolean address, boolean dob, boolean license, boolean documents){

        firstRequired = first;
        lastRequired = last;
        addressRequired = address;
        dobRequired = dob;
        licenseRequired = license;
        documentsRequired = documents;
        Log.i("truth values set up", String.valueOf(firstRequired) + " " + String.valueOf(lastRequired)+ " " + String.valueOf(addressRequired) + " " + String.valueOf(dobRequired)
                + " " + String.valueOf(licenseRequired) + " " + String.valueOf(documentsRequired));
        if(!firstRequired){
            bookingFirstName.setEnabled(false);
            bookingFirstName.setHint(bookingFirstName.getHint() + "(Not required)");
        }
        else{
            bookingFirstName.setEnabled(true);

        }

        if(!lastRequired){
            bookingLastName.setEnabled(false);
            bookingLastName.setHint(bookingLastName.getHint() + "(Not required)");
        }
        else{
            bookingLastName.setEnabled(true);


        }

        if(!licenseRequired){
            spinnerLicenseType.setEnabled(false);

        }
        else{
            spinnerLicenseType.setEnabled(true);

        }

        if(!addressRequired){
            bookingAddress.setEnabled(false);
            bookingAddress.setHint(bookingAddress.getHint() + "(Not required)");
        }
        else{
            bookingAddress.setEnabled(true);

        }

        if(!dobRequired){
            bookingDateOfBirth.setEnabled(false);
            bookingDateOfBirth.setHint(bookingDateOfBirth.getHint() + "(Not required)");
        }
        else{
            bookingDateOfBirth.setEnabled(true);


        }

        if(documentsRequired){
            documentsDisplay.setText(serviceToBook.getStringDocuments());
            attachDocuments.setEnabled(true);

        }
        else{
            attachDocuments.setEnabled(false);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(this, "Nothing selected", Toast.LENGTH_LONG).show();
    }
}