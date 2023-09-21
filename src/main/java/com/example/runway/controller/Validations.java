package com.example.runway.controller;

import com.example.runway.model.Airport;
import com.example.runway.model.Obstacle;
import com.example.runway.model.Runway;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;


/**
 * The validations class. Used for validating user input.
 */
public class Validations {

  // List of characters allowed for runway number:
  private final List<Character> runwayChars = Arrays.asList('L', 'C', 'R');

  /**
   * Validate user input for runway number
   * @param runwayNumber    user input
   * @return    boolean value
   */
  public boolean isValidRunwayNumber(String runwayNumber) {
    int len = runwayNumber.length();
    if (len > 0 && len <= 3) {
      try {
        int number = Integer.parseInt(runwayNumber);
        return number >= 0 && number <= 36;
      }
      catch (Exception e) {
        char last = runwayNumber.charAt(len - 1);
        if (runwayChars.contains(Character.toUpperCase(last))) {
          try {
            int number = Integer.parseInt(runwayNumber.substring(0, len - 1));
            return number >= 0 && number <= 36;
          }
          catch (Exception ex) {
            return false;
          }
        }
      }
    }
    return false;
  }

  /**
   * Getter for list of characters allowed for runway number
   * @return     list of characters
   */
  public List<Character> getRunwayChars() {
    return runwayChars;
  }

  /**
   * Validate user input for TORA
   * @param tora    user input
   * @return    boolean value
   */
  public boolean isValidRunwayTora(String tora) {
    try {
      double number = Double.parseDouble(tora);
      return number > 0;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for TODA
   * @param toda    user input
   * @return    boolean value
   */
  public boolean isValidRunwayToda(String toda, double asda) {
    try {
      double number = Double.parseDouble(toda);
      return number >= asda;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for ASDA
   * @param asda    user input
   * @return    boolean value
   */
  public boolean isValidRunwayAsda(String asda, double tora) {
    try {
      double number = Double.parseDouble(asda);
      return number >= tora;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for LDA
   * @param lda    user input
   * @return    boolean value
   */
  public boolean isValidRunwayLda(String lda, double tora) {
    try {
      double number = Double.parseDouble(lda);
      return number > 0 && number <= tora;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for displaced threshold
   * @param displaced    user input
   * @return    boolean value
   */
  public boolean isValidRunwayDisplaced(String displaced) {
    try {
      double number = Double.parseDouble(displaced);
      return number >= 0;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for airport/obstacle name
   * @param name    user input
   * @return    boolean value
   */
  public boolean isValidName(String name) {
    try {
      if (name.length()== 0) {
        return false;
      }
      for (int i = 0; i < name.length(); i++) {
        if (!Character.isLetterOrDigit(name.charAt(i))) {
          return false;
        }
      }
      return Character.isLetter(name.charAt(0));
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for obstacle height
   * @param height    user input
   * @return    boolean value
   */
  public boolean isValidObstacleHeight(String height) {
    try {
      double number = Double.parseDouble(height);
      return number > 0;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for obstacle distance from centreline/threshold
   * @param distance    user input
   * @return    boolean value
   */
  public boolean isValidObstacleDistance(String distance, boolean centre) {
    try {
      double number = Double.parseDouble(distance);
      if (centre) {
        return number >= 0 && number <= 75;
      }
      else {
        return number >= 60;
      }
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate user input for file name
   * @param filename    user input
   * @return    boolean value
   */
  public boolean isValidXMLFilename(String filename) {
    try {
      String[] parts = filename.split("\\.");
      if (parts.length < 2) {
        return false;
      }
      return isValidName(parts[0]) && parts[1].equals("xml");
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate XML file for import
   * @param xml    XML file
   * @return    boolean value
   */
  public boolean isValidXMLFile(File xml) {
    try {
      SchemaFactory factory =
          SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      // Check the file against schema:
      try (InputStream in = getClass().getResourceAsStream("/xml/schema.xsd");
          BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String cwd = Path.of("").toAbsolutePath().toString();
        File f = new File(String.valueOf(Path.of(cwd + "/storage_files/temp.txt")));
        String line = reader.readLine();

        FileWriter fw = new FileWriter(f);

        while (line != null) {
          fw.write(line + "\n");

          line = reader.readLine();
        }

        fw.close();

        f.renameTo(new File(String.valueOf(Path.of(cwd + "/storage_files/temp.xsd"))));

        File xsd = new File(String.valueOf(Path.of(cwd + "/storage_files/temp.xsd")));

        Schema schema = factory.newSchema(xsd);
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xml));

        xsd.delete();
      }
    }
    catch (IOException | SAXException e) {
      return false;
    }
    return true;
  }

  /**
   * Validate airport data
   * @param airport    an airport
   * @param inputManager controller for user inputs
   * @return    boolean value
   */
  public boolean isValidAirport(Airport airport, InputManager inputManager) {
    String name = airport.getName();
    if (isValidName(name) && !inputManager.getAirportNames().contains(name)) {
      // Validate airport's runways:
      List<String> runwayNumbers = new ArrayList<>();
      for (Runway runway : airport.getRunways()) {
        String runwayNumber = runway.getRunwayNumber();
        if (!isValidRunway(runway) || runwayNumbers.contains(runwayNumber)) {
          return false;
        }
        runwayNumbers.add(runwayNumber);
      }
      return true;
    }
    return false;
  }

  /**
   * Validate runway data
   * @param runway    a runway
   * @return    boolean value
   */
  public boolean isValidRunway(Runway runway) {
    if (isValidRunwayNumber(runway.getRunwayNumber())) {
      String strTora = String.valueOf(runway.getTora());
      if (isValidRunwayTora(strTora)) {
        double tora = Double.parseDouble(strTora);
        if (isValidRunwayAsda(String.valueOf(runway.getAsda()), tora)) {
          double asda = Double.parseDouble(String.valueOf(runway.getAsda()));
          if (isValidRunwayToda(String.valueOf(runway.getToda()), asda) &&
              isValidRunwayLda(String.valueOf(runway.getLda()), tora) &&
              isValidRunwayDisplaced(String.valueOf(runway.getDisplacedThreshold()))) {
            // Validate runway's obstacles:
            List<String> obstacleNames = new ArrayList<>();
            for (Obstacle obstacle : runway.getObstacles()) {
              String obstacleName = obstacle.getName();
              if (!isValidObstacle(obstacle, tora) || obstacleNames.contains(obstacleName)) {
                return false;
              }
              obstacleNames.add(obstacleName);
            }
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Validate obstacle data
   * @param obstacle    an obstacle
   * @param tora runway TORA
   * @return    boolean value
   */
  public boolean isValidObstacle(Obstacle obstacle, Double tora) {
    if (isValidName(obstacle.getName())) {
      if (isValidObstacleDistance(String.valueOf(obstacle.getDistanceCentre()), true) &&
          isValidObstacleDistance(String.valueOf(obstacle.getDistanceFromThreshold()), false) &&
          isValidObstacleHeight(String.valueOf(obstacle.getHeight())) &&
          isValidObstacleHeight(String.valueOf(obstacle.getLength()))) {
        return obstacle.getDistanceFromThreshold() < tora;
      }
    }
    return false;
  }

}
