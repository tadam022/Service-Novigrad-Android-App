package com.example.segproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class hoursdialog extends AppCompatDialogFragment implements NumberPicker.OnValueChangeListener  {

    private NumberPicker start;
    private NumberPicker end;
    private NumberPicker AMorPM;
    private NumberPicker AMorPM2;

    listenerDialog dialogListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder dialogBuilder  = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_layout_hours, null);

        start = (NumberPicker) v.findViewById(R.id.startHour2);
        end = (NumberPicker) v.findViewById(R.id.endHour2);
        AMorPM = (NumberPicker) v.findViewById(R.id.time12);
        AMorPM2 = (NumberPicker) v.findViewById(R.id.time22);


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

        start.setOnValueChangedListener(this);

        dialogBuilder.setView(v).setTitle("Set hours").setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int first = start.getValue();
                int second = end.getValue();
                if(AMorPM.getValue() == 1){
                    first += 12;
                }
                if(AMorPM2.getValue() == 1){
                    second+= 12;
                }

                if(validHours(first,second)){
                    int[] hours = {first, second};
                    Bundle bundle = getArguments();
                    int d = bundle.getInt("day");
                    dialogListener.passHours(hours,d);
                }
                else{
                    if(first > second){
                        Toast.makeText(getContext(),"Opening hours cannot be later than closing hours.", Toast.LENGTH_SHORT).show();
                        int[] hours = {-1, -1};
                        Bundle bundle = getArguments();
                        int d = bundle.getInt("day");
                        dialogListener.passHours(hours,d);
                    }
                    else if(first == second){
                        Toast.makeText(getContext(),"The opening and closing hours cannot be at the same time.", Toast.LENGTH_SHORT).show();
                        int[] hours = {-1, -1};
                        Bundle bundle = getArguments();
                        int d = bundle.getInt("day");
                        dialogListener.passHours(hours,d);
                    }
                }

            }
        });



        return dialogBuilder.create();

    }
    public boolean validHours(int hour1, int hour2){
        if(hour1 >= hour2){
            return false;
        }
        return true;

    }

    public interface listenerDialog{ //
        void passHours(int[] times, int day);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (listenerDialog) context;
        } catch (Exception e) {

            Toast.makeText(getContext(),"Unable to open dialog.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }



}
