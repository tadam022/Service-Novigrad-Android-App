package com.example.segproject;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Deliverable2UnitTests {

    // need at least 5 unit tests

    @Test
    public void validServiceName1(){
        String serviceName = "service";
        AddService activity = new AddService();
        assertTrue(activity.isValidServiceName(serviceName));

    }
    @Test
    public void validServiceName2(){
        String serviceName = " 6serviceA2 ";
        AddService activity = new AddService();
        assertTrue(activity.isValidServiceName(serviceName));

    }

    @Test
    public void validServiceName3(){
        String serviceName = "12345";
        AddService activity = new AddService();
        assertFalse(activity.isValidServiceName(serviceName));

    }

    @Test
    public void validServiceName4(){
        String serviceName = "13 45 113 ";
        AddService activity = new AddService();
        assertFalse(activity.isValidServiceName(serviceName));

    }

    @Test
    public void validServiceName5(){
        String serviceName = "";
        AddService activity = new AddService();
        assertFalse(activity.isValidServiceName(serviceName));

    }

    @Test
    public void validServiceName6(){
        String serviceName = " ";
        AddService activity = new AddService();
        assertFalse(activity.isValidServiceName(serviceName));

    }

}
