package com.example.runway.model;


/**
 * The obstacle class. Stores parameters of an obstacle.
 */
public class Obstacle {

  // private properties of objects in an Obstacle class
  private String name;
  private double height;
  private double length;
  private double distanceCentre;
  private double distanceThreshold;

  /**
   * constructor of creating a new Obstacle
   * @param name                   for the Obstacle
   * @param height                 highest point of the obstacle in meters
   * @param distanceCentre         distance of the obstacle from the centreline in meters
   * @param distanceThreshold      distance of the obstacle from the left threshold in meters
   */
  public Obstacle(String name, double height, double length, double distanceCentre, double distanceThreshold) {
    this.name = name;
    this.height = height;
    this.length = length;
    this.distanceCentre = distanceCentre;
    this.distanceThreshold = distanceThreshold;
  }

  /**
   * Accessor
   * @return name of the obstacle
   */
  public String getName() {
    return this.name;
  }

  /**
   * Accessor
   * @return height of the obstacle
   */
  public double getHeight() {
    return this.height;
  }

  /**
   * Accessor
   * @return length of the obstacle
   */
  public double getLength() {
    return this.length;
  }

  /**
   * Accessor
   * @return distance from centreline of the obstacle
   */
  public double getDistanceCentre() {
    return this.distanceCentre;
  }

  /**
   * Accessor
   * @return distance from left threshold of the obstacle
   */
  public double getDistanceFromThreshold() {
    return this.distanceThreshold;
  }

  /**
   * Mutator
   * @param name of the obstacle
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Mutator
   * @param height of the obstacle
   */
  public void setHeight(double height) {
    this.height = height;
  }

  /**
   * Mutator
   * @param length of the obstacle
   */
  public void setLength(double length) {
    this.length = length;
  }

  /**
   * Mutator
   *
   * @param distanceCentre of the obstacle
   */
  public void setDistanceCentre(double distanceCentre) {
    this.distanceCentre = distanceCentre;
  }

  /**
   * Mutator
   * @param distanceThreshold of the obstacle
   */
  public void setDistanceThreshold(double distanceThreshold) {
    this.distanceThreshold = distanceThreshold;
  }

}
