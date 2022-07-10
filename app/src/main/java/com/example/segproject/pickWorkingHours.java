package com.example.segproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class pickWorkingHours extends AppCompatActivity implements  hoursdialog.listenerDialog {


    private int[][] workHours;

    TextView mondayHoursDisplay;
    TextView tuesdayHoursDisplay;
    TextView wednesdayHoursDisplay;
    TextView thursdayHoursDisplay;
    TextView fridayHoursDisplay;
    TextView saturdayHoursDisplay;
    TextView sundayHoursDisplay;
    CheckBox monday;
    CheckBox tuesday;
    CheckBox wednesday;
    CheckBox thursday;
    CheckBox friday;
    CheckBox saturday;
    CheckBox sunday;
    Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_working_hours);

        workHours = new int[7][2];


        // Set TextViews
        mondayHoursDisplay = findViewById(R.id.textViewMondayHours);
        tuesdayHoursDisplay = findViewById(R.id.textViewTuesdayHours);
        wednesdayHoursDisplay = findViewById(R.id.textViewWednesdayHours);
        thursdayHoursDisplay = findViewById(R.id.textViewThursdayHours);
        fridayHoursDisplay = findViewById(R.id.textViewFridayHours);
        saturdayHoursDisplay = findViewById(R.id.textViewSaturdayHours);
        sundayHoursDisplay = findViewById(R.id.textViewSundayHours);

        //Set checkboxes
        monday = (CheckBox) findViewById(R.id.checkBoxMonday);
        tuesday = (CheckBox) findViewById(R.id.checkBoxTuesday);
        wednesday = (CheckBox) findViewById(R.id.checkBoxWednesday);
        thursday = (CheckBox) findViewById(R.id.checkBoxThursday);
        friday = (CheckBox) findViewById(R.id.checkBoxFriday);
        saturday = (CheckBox) findViewById(R.id.checkBoxSaturday);
        sunday = (CheckBox) findViewById(R.id.checkBoxSunday);

        done = (Button) findViewById(R.id.buttonFinishHours);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                Intent intent = new Intent(getApplicationContext(), setupBranch.class);
                for(int i = 0; i < 7; i++){
                    intent.putExtra(Integer.toString(i), workHours[i]);
                }
                intent.putExtra("uID", getIntent().getStringExtra("uID"));

                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("postal", getIntent().getStringExtra("postal"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                startActivity(intent);
            }
        });

    }

    public void onCheckboxClicked(View view) {
        boolean check = ((CheckBox) view).isChecked();

        int id = view.getId();
        if(id == R.id.checkBoxMonday){
            if(check){
                openHoursDialog(0);
            }
            else{
                mondayHoursDisplay.setText("Closed");
                clearDaysHours(0);
            }

        }
        else if(id == R.id.checkBoxTuesday){
            if(check){
                openHoursDialog(1);
            }
            else{
                tuesdayHoursDisplay.setText("Closed");
                clearDaysHours(1);
            }
        }
        else if(id == R.id.checkBoxWednesday){
            if(check){
                openHoursDialog(2);
            }
            else{
                wednesdayHoursDisplay.setText("Closed");
                clearDaysHours(2);
            }
        }
        else if(id == R.id.checkBoxThursday){
            if(check){
                openHoursDialog(3);
            }
            else{
                thursdayHoursDisplay.setText("Closed");
                clearDaysHours(3);
            }
        }
        else if(id == R.id.checkBoxFriday){
            if(check){
                openHoursDialog(4);
            }
            else{
                fridayHoursDisplay.setText("Closed");
                clearDaysHours(4);
            }
        }
        else if(id == R.id.checkBoxSaturday){
            if(check){
                openHoursDialog(5);
            }
            else{
                saturdayHoursDisplay.setText("Closed");
                clearDaysHours(5);
            }
        }
        else if(id == R.id.checkBoxSunday){
            if(check){
                openHoursDialog(6);
            }
            else{
                sundayHoursDisplay.setText("Closed");
                clearDaysHours(6);
            }
        }


    }

    public void openHoursDialog(int day){

        hoursdialog dialog = new hoursdialog();
        Bundle bundle = new Bundle();
        bundle.putInt("day", day);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "pick hours");


    }

    @Override
    public void passHours(int[] times, int day) {
        if(times[0] == -1 && times[1] == -1){
            uncheckBox(day);
            return;
        }
        workHours[day][0] = times[0];
        workHours[day][1] = times[1];
        if(day == 0){
            mondayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }

        else if(day == 1){
            tuesdayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }
        else if(day == 2){
            wednesdayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }
        else if(day == 3){
            thursdayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }
        else if(day == 4){
            fridayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }
        else if(day == 5){
            saturdayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }
        else{
            sundayHoursDisplay.setText("Opens: " + workHours[day][0] + " : 00" + " Closes: " + workHours[day][1] + " : 00");
        }

    }

    private void clearDaysHours(int day){
        workHours[day][0] = 0;
        workHours[day][1] = 1;
    }

    private void uncheckBox(int day){
        if(day == 0){
            monday.toggle();
        }

        else if(day == 1){
            tuesday.toggle();
        }
        else if(day == 2){
            wednesday.toggle();
        }
        else if(day == 3){
            thursday.toggle();
        }
        else if(day == 4){
            friday.toggle();
        }
        else if(day == 5){
            saturday.toggle();
        }
        else{
            sunday.toggle();
        }


    }
}

