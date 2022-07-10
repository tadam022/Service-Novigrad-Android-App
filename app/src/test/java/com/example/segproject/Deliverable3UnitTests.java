package com.example.segproject;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Deliverable3UnitTests {

    // Need at least two unit tests

    @Test
    public void checkValidBranchHours1(){

        int hour1 = 5;
        int hour2 = 12;
        hoursdialog activity = new hoursdialog();
        assertTrue(activity.validHours(hour1,hour2));

    }

    @Test
    public void checkValidBranchHours2(){
        int hour1 = 5;
        int hour2 = 3;
        hoursdialog activity = new hoursdialog();
        assertFalse(activity.validHours(hour1,hour2));

    }

    @Test
    public void checkValidBranchHours3(){
        int hour1 = 5;
        int hour2 = 5;
        hoursdialog activity = new hoursdialog();
        assertFalse(activity.validHours(hour1,hour2));

    }

    @Test
    public void checkValidBranchName1(){
        String name = "";
        setupBranch activity = new setupBranch();
        assertFalse(activity.validBranchName(name));

    }

    @Test
    public void checkValidBranchName2(){
        String name = "abcdefghijklmnopqrstuvwxyz  ";
        setupBranch activity = new setupBranch();
        assertFalse(activity.validBranchName(name));

    }

    @Test
    public void checkValidBranchName3(){
        String name = " Branch Name ";
        setupBranch activity = new setupBranch();
        assertTrue(activity.validBranchName(name));

    }
}
