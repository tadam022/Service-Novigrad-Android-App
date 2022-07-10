package com.example.segproject;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Deliverable4UnitTests {

    @Test
    public void checkBranchRatingRounding1(){

        double rating = 3.77;
        assertEquals(branchView.roundToNearestHalf(rating),4.0,0.01);

    }

    @Test
    public void checkBranchRatingRounding2(){

        double rating = 3.45;
        assertEquals(branchView.roundToNearestHalf(rating),3.5,0.01);

    }

    @Test
    public void checkBranchRatingRounding3(){

        double rating = 1.1;
        assertEquals(branchView.roundToNearestHalf(rating),1.0,0.01);

    }

    @Test
    public void checkBranchRatingRounding4(){

        double rating = 4.03;
        assertEquals(branchView.roundToNearestHalf(rating),4.0,0.01);

    }



    @Test
    public void checkValidBirthDate1(){


        fillForm activity = new fillForm();
        assertTrue(!activity.checkDate(1990,10,11));

    }

    @Test
    public void checkValidBirthDate2(){


        fillForm activity = new fillForm();
        assertFalse(!activity.checkDate(2120,8,19));

    }

    @Test
    public void checkValidBirthDate3(){


        fillForm activity = new fillForm();
        assertFalse(!activity.checkDate(2060,12,23));

    }

    @Test
    public void checkValidAppointmentDate1(){


        fillForm activity = new fillForm();
        assertTrue(activity.checkDate(2060,12,23));

    }

    @Test
    public void checkValidAppointmentDate2(){


        fillForm activity = new fillForm();
        assertTrue(activity.checkDate(2030,5,23));

    }

    @Test
    public void checkValidAppointmentDate3(){

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        fillForm activity = new fillForm();
        assertFalse(activity.checkDate(currentYear,currentMonth,currentDay));

    }

    @Test
    public void checkValidAppointmentDate4(){

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        fillForm activity = new fillForm();
        assertFalse(activity.checkDate(currentYear-1,currentMonth,currentDay));

    }

    @Test
    public void checkRemovePunctuation(){

       String tester = "ab,cd=,ade,-d";
        searchResultsList activity = new searchResultsList();
        assertEquals(activity.removePunctuation(tester),"abcdaded");

    }





}
