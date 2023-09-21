package com.example.runway.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The airport class. Stores parameters of an airport.
 */
public class Airport {


  private String name;

  // Runways of the airport:
  private List<Runway> runways = new ArrayList<>();

  /**
   * Create a new airport
   * @param name airport name
   */
  public Airport(String name) {
    this.name = name;
  }

  /**
   * Getter for airport's runways
   * @return runway list
   */
  public List<Runway> getRunways() {
    return runways;
  }

  /**
   * Getter for airport's runway numbers
   * @return runway numbers list
   */
  public List<String> getRunwayNumbers() {
    List<String> numbers = new ArrayList<>();
    for (Runway runway : runways) {
      numbers.add(runway.getRunwayNumber());
    }
    return numbers;
  }

  /**
   * Add runway to the airport
   */
  public void addRunway(Runway runway) {
    runways.add(runway);
  }

  /**
   * Delete runway from the airport
   */
  public void deleteRunway(Runway runway) {
    runways.remove(runway);
  }

  /**
   * Getter for airport name
   * @return name
   */
  public String getName(){
    return this.name;
  }

  /**
   * Set all runway parameters to default values for each runway of the airport
   */
  public void setDefaultRunways() {
    for (Runway runway : runways) {
      runway.setDefault();
    }
  }

  /**
   * Setter for airport name
   * @param name new airport name
   */
  public void setName(String name) {
    this.name = name;
  }

}
