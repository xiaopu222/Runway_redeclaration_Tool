package com.example.runway.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RunwayTest {
    private Runway runway;

    @Before
    public void setUp(){
      runway = new Runway("10R", 3884, 3962, 3884, 3884, 0);
    }

    @After
    public void tearDown() throws Exception {
        runway = null;
    }

  @Test
  public void testValuesSaveInObject() {
    System.out.println("Testing the values get saved well inside runway.");
    assertEquals("Runway number is not stored properly", String.valueOf(runway.getRunwayNumber()), "10R");
    assertEquals("TORA is not stored properly", String.valueOf(runway.getDefaultTora()), "3884.0");
    assertEquals("TODA is not stored properly", String.valueOf(runway.getDefaultToda()), "3962.0");
    assertEquals("ASDA is not stored properly", String.valueOf(runway.getDefaultAsda()), "3884.0");
    assertEquals("LDA is not stored properly", String.valueOf(runway.getDefaultLda()), "3884.0");
  }

    @Test
    public void testLandingOver() {
      System.out.println("Testing Landing Over obstacle");
      runway.redeclarationLandingOver(new Obstacle("obstacle", 25, 10, 0, 500));
      String lda = String.valueOf(runway.getLda());
      assertEquals("Landing over calculation returned an incorrect LDA", lda, "2074.0");
    }

  @Test
  public void testLandingTowards() {
    System.out.println("Testing Landing Towards obstacle");
    runway.redeclarationLandingTowards(new Obstacle("obstacle", 25, 10, 0, 2600));
    String lda = String.valueOf(runway.getLda());
    assertEquals("Landing towards calculation returned an incorrect LDA", "2300.0", lda);
  }

  @Test
  public void testTakeOffTowards() {
    System.out.println("Testing Take-off Towards obstacle");
    runway.redeclarationTakeOffTowards(new Obstacle("obstacle", 25, 10, 0, 2500));
    String tora = String.valueOf(runway.getTora());
    String toda = String.valueOf(runway.getToda());
    String asda = String.valueOf(runway.getAsda());
    assertEquals("Take-off towards calculation returned an incorrect TORA", "1190.0", tora);
    assertEquals("Take-off towards calculation returned an incorrect TODA", "1190.0", toda);
    assertEquals("Take-off towards calculation returned an incorrect ASDA", "1190.0", asda);
  }

  @Test
  public void testTakeOffAway() {
    System.out.println("Testing Take-off Away from obstacle");
    runway.redeclarationTakeOffAway(new Obstacle("obstacle", 25, 10, 0, 500));
    String tora = String.valueOf(runway.getTora());
    String toda = String.valueOf(runway.getToda());
    String asda = String.valueOf(runway.getAsda());
    assertEquals("Take-off away calculation returned an incorrect TORA", "3162.0", tora);
    assertEquals("Take-off away calculation returned an incorrect TODA", "3240.0", toda);
    assertEquals("Take-off away calculation returned an incorrect ASDA", "3162.0", asda);
  }

  @Test
  public void testObstacleStorage() {
    System.out.println("Testing how well obstacle of the runway gets stored.");

    Obstacle obstacle = new Obstacle("obstacle", 1, 10, 1, 1);
    runway.addObstacle(obstacle);

    assertEquals("obstacle's name didn't get stored properly",
        "obstacle", obstacle.getName());
    assertEquals("obstacle's height didn't get stored properly",
        "1.0", String.valueOf(obstacle.getHeight()));
    assertEquals("obstacle's distance from centre line didn't get stored properly",
        "1.0", String.valueOf(obstacle.getDistanceCentre()));
    assertEquals("obstacle's distance from threshold didn't get stored properly",
        "1.0", String.valueOf(obstacle.getDistanceFromThreshold()));
    }

}