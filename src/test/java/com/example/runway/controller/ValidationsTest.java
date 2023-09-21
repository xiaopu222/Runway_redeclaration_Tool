package com.example.runway.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.runway.controller.Validations;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ValidationsTest {
  private Validations validations;

  @Before
  public void setUp(){
    validations = new Validations();
  }

  @After
  public void tearDown() throws Exception {
    validations = null;
  }

  @Test
  public void testRunwayNumberValidation() {
    System.out.println("Testing validation of runway number");
    assertTrue("Runway number \"10R\" should be valid.", validations.isValidRunwayNumber("10R"));
    assertTrue("Runway number \"10L\" should be valid.", validations.isValidRunwayNumber("10L"));
    assertTrue("Runway number \"10C\" should be valid.", validations.isValidRunwayNumber("10C"));
    assertTrue("Runway number \"2\" should be valid.", validations.isValidRunwayNumber("2"));
    assertTrue("Runway number \"0\" should be valid.", validations.isValidRunwayNumber("0"));
    assertTrue("Runway number \"36\" should be valid.", validations.isValidRunwayNumber("36"));
    assertFalse("Runway number \"37\" should be invalid.", validations.isValidRunwayNumber("37"));
    assertFalse("Runway number \"40\" should be invalid.", validations.isValidRunwayNumber("100"));
    assertFalse("Runway number \"nine left\" should be invalid.", validations.isValidRunwayNumber("nine left"));
  }

  @Test
  public void testRunwayParametersValidation() {
    System.out.println("Testing validation of TORA, TODA, ASDA and LDA");

    assertTrue("TORA with value 3902 should be valid.", validations.isValidRunwayTora("3902"));
    assertFalse("TORA with value 0 should be invalid.", validations.isValidRunwayTora("0"));
    assertFalse("TORA with value \"five hundred\" should be invalid.", validations.isValidRunwayTora("five hundred"));

    assertTrue("TODA with value 3902 should be valid.", validations.isValidRunwayToda("3902", 3902));
    assertFalse("TODA with value 0 should be invalid.", validations.isValidRunwayToda("0", 3902));
    assertFalse("TODA with value \"five hundred\" should be invalid.", validations.isValidRunwayToda("five hundred", 3902));

    assertTrue("ASDA with value 3902 should be valid.", validations.isValidRunwayAsda("3902", 3902));
    assertFalse("ASDA with value 0 should be invalid.", validations.isValidRunwayAsda("0", 3902));
    assertFalse("ASDA with value \"five hundred\" should be invalid.", validations.isValidRunwayAsda("five hundred", 3902));

    assertTrue("LDA with value 3902 should be valid.", validations.isValidRunwayLda("3902", 3902));
    assertFalse("LDA with value 0 should be invalid.", validations.isValidRunwayLda("0", 3902));
    assertFalse("LDA with value \"five hundred\" should be invalid.", validations.isValidRunwayLda("five hundred", 3902));
  }

  @Test
  public void testObstacleParametersValidation() {
    System.out.println("Testing obstacle parameters");

    assertTrue("Obstacle name \"BOEING\" should be valid.", validations.isValidName("BOEING"));
    assertTrue("Obstacle name \"ob1\" should be valid.", validations.isValidName("ob1"));
    assertFalse("Obstacle name \"1\" should be invalid.", validations.isValidName("1"));
    assertFalse("Obstacle name \"-\" should be invalid.", validations.isValidName("-"));

    assertTrue("Obstacle height with value 50 should be valid.", validations.isValidObstacleHeight("50"));
    assertFalse("Obstacle height with value 0 should be invalid.", validations.isValidObstacleHeight("0"));
    assertTrue("Obstacle height with value 1 should be valid.", validations.isValidObstacleHeight("1"));
    assertFalse("Obstacle height with value \"fourty\" should be invalid.", validations.isValidObstacleHeight("fourty"));

    assertTrue("Obstacle distance from centre line with value 50 should be valid.", validations.isValidObstacleDistance("50", true));
    assertTrue("Obstacle distance from centre line with value 1 should be valid.", validations.isValidObstacleDistance("1", true));
    assertFalse("Obstacle distance from centre line with value -1 should be invalid.", validations.isValidObstacleDistance("-1", true));
    assertFalse("Obstacle distance from centre line with value \"fourty\" should be invalid.", validations.isValidObstacleDistance("fourty", true));

    assertTrue("Obstacle distance from threshold with value 60 should be valid.", validations.isValidObstacleDistance("60", false));
    assertFalse("Obstacle distance from threshold with value 1 should be invalid.", validations.isValidObstacleDistance("1", false));
    assertFalse("Obstacle distance from threshold with value -1 should be invalid.", validations.isValidObstacleDistance("-1", false));
    assertFalse("Obstacle distance from threshold with value \"fourty\" should be invalid.", validations.isValidObstacleDistance("fourty", false));
  }

  @Test
  public void testXMLFilenameValidation() {
    System.out.println("Testing XML filename validation");

    assertTrue("File name \"file.xml\" should be valid.", validations.isValidXMLFilename("file.xml"));
    assertTrue("File name \"f1.xml\" should be valid.", validations.isValidXMLFilename("f1.xml"));
    assertFalse("File name \"0.xml\" should be invalid.", validations.isValidXMLFilename("0.xml"));
    assertFalse("File name \"file.txt\" should be invalid.", validations.isValidXMLFilename("file.txt"));
    assertFalse("File name \"file\" should be invalid.", validations.isValidXMLFilename("file"));
    assertFalse("File name \"file-xml\" should be invalid.", validations.isValidXMLFilename("file-xml"));
  }

  @Test
  public void testXMLFileValidation() {
    System.out.println("Testing XML file validation");

    assertFalse("File name \"invalid.xml\" should be invalid.",
        validations.isValidXMLFile(new File("invalid.xml")));
  }

}
