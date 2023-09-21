package com.example.runway.controller;

import com.example.runway.model.Airport;
import com.example.runway.model.Obstacle;
import com.example.runway.model.Runway;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.Text;


/**
 * The input manager class. Connects view to model by passing inputs and results of calculations between them.
 */
public class InputManager {

  private List<Airport> airports;
  private Airport airport;
  private Runway runway;
  private boolean modified = false;

  private String style = "/style/style.css";

  // private Obstacle obstacle;
  // Array of predefined landing/take-off methods:
  private final int[] methods = {1,2,3,4};
  private int method;

  private List<Text> notifications;

  /**
   * Create an input controller
   */
  public InputManager() {
    airports = new ArrayList<>();
    notifications = new ArrayList<>();
  }

  /**
   * Setter for the current airport
   * @param airport      new current airport
   */
  public void setCurrentAirport(Airport airport) {
    this.airport = airport;
  }

  /**
   * Setter for the current runway
   * @param runway      new current runway
   */
  public void setCurrentRunway(Runway runway) {
    this.runway = runway;
  }

  /**
   * Getter for current runway
   * @return     current runway
   */
  public Runway getCurrentRunway() {
    return runway;
  }

  /**
   * Getter for current airport
   * @return     current airport
   */
  public Airport getCurrentAirport() {
    return airport;
  }

  /**
   * Getter for list of all airports
   * @return     list of airport
   */
  public List<Airport> getAirports() {
    return airports;
  }

  /**
   * Getter for CSS style file
   * @return     CSS style file
   */
  public String getStyle() {
    return style;
  }

  /**
   * Setter for CSS style file
   * @param style new CSS style file
   */
  public void setStyle(String style) {
    this.style = style;
  }

  /**
   * Getter for list of all airport names
   * @return     list of airport names
   */
  public List<String> getAirportNames() {
    List<String> names = new ArrayList<>();
    for (Airport airport : getAirports()) {
      names.add(airport.getName());
    }
    return names;
  }

  /**
   * Getter for landing/take-off methods array
   * @return    methods
   */
  public int[] getMethods() {
    return methods;
  }

  /**
   * Getter for current landing/take-off method
   * @return    method
   */
  public int getMethod() {
    return method;
  }

  /**
   * Setter for the current landing/take-off method
   * @param method     current method
   */
  public void setMethod(int method) {
    this.method = method;
  }

  /**
   * Calls the right calculation method depending on which landing/take-off method was chosen.
   */
  public void calculateValues() {
    switch (method) {
      case 1 -> runway.redeclarationLandingOver(runway.getCurrentObstacle());
      case 2 -> runway.redeclarationLandingTowards(runway.getCurrentObstacle());
      case 3 -> runway.redeclarationTakeOffTowards(runway.getCurrentObstacle());
      case 4 -> runway.redeclarationTakeOffAway(runway.getCurrentObstacle());
    }
  }

  /**
   * Getter for the current runway's obstacles
   * @return     obstacle list
   */
  public List<Obstacle> getObstacles() {
    return runway.getObstacles();
  }

  /**
   * Add a new airport
   * @param airport new airport
   */
  public void addAirport(Airport airport) {
    airports.add(airport);
  }

  /**
   * Getter for notifications
   * @return     notifications list
   */
  public List<Text> getNotifications() {
    return notifications;
  }

  /**
   * Show a new airport
   * @param notification new notification
   */
  public void addNotification(Text notification) {
    notifications.add(notification);
  }

  /**
   * Getter for modified value
   * @return modified boolean
   */
  public boolean isModified() {
    return modified;
  }

  /**
   * Setter for modified value
   * @param modified boolean value
   */
  public void setModified(boolean modified) {
    this.modified = modified;
  }

}
