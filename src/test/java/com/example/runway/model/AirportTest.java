package com.example.runway.model;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AirportTest {
    private Airport airport;
    private Runway runway1;
    private Runway runway2;

    @Before
    public void setUp(){
        airport = new Airport("Airport");
    }

    @After
    public void tearDown(){
        airport=null;
        runway1=null;
        runway2=null;
    }

    @org.junit.Test
    public void getRunwaysNone() {
        ArrayList<Runway> runways = new ArrayList<Runway>();
        assertEquals(".getRunway() function returns incorrect values for no runways",runways,airport.getRunways());
    }

    @org.junit.Test
    public void getRunwaysGeneral() {
        runway1 = new Runway("09L", 3000, 3000, 3000, 3000, 0);
        runway2 = new Runway("27R", 2500, 2500, 2500, 2500, 0);
        airport.addRunway(runway1);
        airport.addRunway(runway2);
        ArrayList<Runway> runways = new ArrayList<Runway>(Arrays.asList(runway1,runway2));
        assertEquals(".getRunway() function returns incorrect values",runways,airport.getRunways());
    }

    @org.junit.Test
    public void getRunwayNumbersNone() {
        ArrayList<String> runwayNumbers = new ArrayList<String>();
        assertEquals(".getRunwayNumbers() function returns incorrect values for no runways",runwayNumbers,airport.getRunwayNumbers());
    }

    @org.junit.Test
    public void getRunwayNumbers() {
        runway1 = new Runway("09L", 3000, 3000, 3000, 3000, 0);
        runway2 = new Runway("27R", 2500, 2500, 2500, 2500, 0);
        airport.addRunway(runway1);
        airport.addRunway(runway2);
        ArrayList<String> runwayNumbers = new ArrayList<String>(Arrays.asList("09L","27R"));
        assertEquals(".getRunwayNumbers() function returns incorrect values",runwayNumbers,airport.getRunwayNumbers());
    }

    @org.junit.Test
    public void getRunwaysDeleting() {
        runway1 = new Runway("09L", 3000, 3000, 3000, 3000, 0);
        runway2 = new Runway("27R", 2500, 2500, 2500, 2500, 0);
        airport.addRunway(runway1);
        airport.addRunway(runway2);
        airport.deleteRunway(runway1);
        ArrayList<Runway> runways = new ArrayList<Runway>(Arrays.asList(runway2));
        assertEquals(".getRunway() fails when deleting a runway",runways,airport.getRunways());
    }
}