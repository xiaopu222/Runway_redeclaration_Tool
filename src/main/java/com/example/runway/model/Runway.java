package com.example.runway.model;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * The runway class. Manages and calculates mathematical values. Stores runway data.
 */
public class Runway {

  // Obstacles of the runway:
  private List<Obstacle> obstacles;
  private Obstacle obstacle;

    //Number given to runways based on the degree of the runway's heading direction
    private String runwayNumber;
    // private int number;

    //Take-Off Run Available (the length of the runway available for take-off)
    private double tora;
    //Take-Off Distance Available (the length of the runway (TORA) plus any Clearway)
    private double toda;
    //Accelerate-Stop Distance Available (the length of the runway (TORA) plus any Stopway)
    private double asda;
    //Landing Distance Available (the length of the runway available for landing)
    private double lda;
    //Displaced Distance for runway
    private double displacedThreshold = 0;

    // Default runway parameters:
    private double defaultTora;
    private double defaultToda;
    private double defaultAsda;
    private double defaultLda;

    //Predefined terms
    //minimum RESA is typically 240m wide
    private double resa = 240;
    //slope of 1:50 is assumed (ALS & TOCS ?)
    private double slopeValue = 50;
    //new strip end is typically 60m
    private double newStripEnd = 60;
    //engine blast allowance for obstacles behind is typically 300 - 500m (depends on aircraft)
    private double engineBlastAllowance = 300;

    // Formatter for setting numbers to 1 d.p.
    private DecimalFormat df = new DecimalFormat("0.0");

  /**
   * Create a new runway
   * @param runwayNumber     the number of the runway
   * @param tora             the initial TORA value
   * @param toda             the initial TODA value
   * @param asda             the initial ASDA value
   * @param lda              the initial LDA value
   */
    public Runway(String runwayNumber, double tora, double toda, double asda, double lda, double displacedThreshold) {
        this.runwayNumber = runwayNumber;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.displacedThreshold = displacedThreshold;

        this.defaultTora = tora;
        this.defaultToda = toda;
        this.defaultAsda = asda;
        this.defaultLda = lda;

        obstacles = new ArrayList<>();
         // Predefined obstacles:
        obstacles.add(new Obstacle("ob1", 12, 10, 0, 60));
        obstacles.add(new Obstacle("ob2", 25, 5, 20, 500));
        obstacles.add(new Obstacle("ob3", 15, 2, 60, 150));
        obstacles.add(new Obstacle("ob4", 20, 15, 20, 65));
    }

    /**
     * Set runway parameters to their default values
     */
    public void setDefault() {
      this.tora = defaultTora;
      this.toda = defaultToda;
      this.asda = defaultAsda;
      this.lda = defaultLda;
    }

  /**
   * Getter for default TORA
   * @return default TORA
   */
    public double getDefaultTora() {
      return defaultTora;
    }

  /**
   * Getter for default TODA
   * @return default TODA
   */
  public double getDefaultToda() {
    return defaultToda;
  }

  /**
   * Getter for default ASDA
   * @return default ASDA
   */
  public double getDefaultAsda() {
    return defaultAsda;
  }

  /**
   * Getter for default LDA
   * @return default LDA
   */
  public double getDefaultLda() {
    return defaultLda;
  }

  /**
   * Setter for default TORA
   * @param defaultTora new default TORA
   */
  public void setDefaultTora(double defaultTora) {
      this.defaultTora = defaultTora;
  }

  /**
   * Setter for default TODA
   * @param defaultToda new default TODA
   */
  public void setDefaultToda(double defaultToda) {
    this.defaultToda = defaultToda;
  }

  /**
   * Setter for default ASDA
   * @param defaultAsda new default ASDA
   */
  public void setDefaultAsda(double defaultAsda) {
    this.defaultAsda = defaultAsda;
  }

  /**
   * Setter for default LDA
   * @param defaultLda new default LDA
   */
  public void setDefaultLda(double defaultLda) {
    this.defaultLda = defaultLda;
  }

  /**
   * Getter for the number part of runway number
   * @return runway number without letter
   */
  public double getWithoutLetter() {
      try {
        int num = Integer.parseInt(runwayNumber);
        return num;
      }
      catch (Exception e) {
        String noLastChar = runwayNumber.substring(0, runwayNumber.length() - 1);
        return Integer.parseInt(noLastChar);
      }
  }

  /**
   * Getter for runway's direction
   * @return direction
   */
  public int getDirection() {
      if (getWithoutLetter() <= 18) {
        return 0;
      }
      return 1;
  }

  /**
   * Getter for runway number
   * @return     runway number
   */
    public String getRunwayNumber() {
      return runwayNumber;
    }


  /**
   * Getter for TORA value
   * @return     TORA
   */
    public double getTora() {
      return tora;
    }

  /**
   * Getter for TODA value
   * @return     TODA
   */
    public double getToda() {
      return toda;
    }

  /**
   * Getter for ASDA value
   * @return     ASDA
   */
    public double getAsda() {
      return asda;
    }

  /**
   * Getter for LDA value
   * @return     LDA
   */
    public double getLda() {
      return lda;
    }

  /**
   * Getter for displaced threshold
   * @return     displaced threshold
   */
  public double getDisplacedThreshold() {
    return displacedThreshold;
  }

  /**
   * Getter for RESA value
   * @return     RESA
   */
  public double getResa() {
    return resa;
  }

  /**
   * Getter for new strip end value
   * @return     new strip end
   */
  public double getNewStripEnd() {
    return newStripEnd;
  }

  /**
   * Getter for slope
   * @return     slope
   */
  public double getSlopeValue() {
    return slopeValue;
  }

  /**
   * Getter for blast allowance
   * @return     blast allowance
   */
  public double getBlastAllowance() {
    return engineBlastAllowance;
  }

  /**
   * Getter for the runway's obstacles
   * @return obstacle list
   */
    public List<Obstacle> getObstacles() {
      return obstacles;
    }

  /**
   * Getter for the runway's obstacle names
   * @return obstacle names list
   */
    public List<String> getObstacleNames() {
      List<String> names = new ArrayList<>();
      for (Obstacle obstacle : obstacles) {
        names.add(obstacle.getName());
      }
      return names;
    }

  /**
   * Add a new obstacle to the runway
   */
    public void addObstacle(Obstacle obstacle) {
      obstacles.add(obstacle);
    }

  /**
   * Set current obstacle of the runway
   */
    public void setCurrentObstacle(Obstacle obstacle) {
      this.obstacle = obstacle;
    }

  /**
   * Getter for runway's current obstacle
   * @return current obstacle
   */
    public Obstacle getCurrentObstacle() {
      return obstacle;
    }

  /**
   * Re-calculate values when landing over obstacle:
   * @param obstacle    the obstacle
   */
    public void redeclarationLandingOver(Obstacle obstacle){
        //(R) LDA = 3884 – 500 – (25 * 50) – 60 = 2074m
        lda = lda - obstacle.getDistanceFromThreshold() - (obstacle.getHeight() * slopeValue) - newStripEnd - displacedThreshold;
        handleNegative();
    }

  /**
   * Re-calculate values when landing towards an obstacle:
   * @param obstacle    the obstacle
   */
    public void redeclarationLandingTowards(Obstacle obstacle){
        //(R) LDA = 2600 – 240 – 60 = 2300m
        lda = obstacle.getDistanceFromThreshold() - resa - newStripEnd;
        handleNegative();
    }

  /**
   * Re-calculate values when taking-off towards an obstacle:
   * @param obstacle    the obstacle
   */
    public void redeclarationTakeOffTowards(Obstacle obstacle){
        //(R) TORA = 2500 + 306 – 25*50 – 60 = 1496m
        //(R) ASDA = (R) TODA = (R) TORA
        tora = obstacle.getDistanceFromThreshold() + displacedThreshold - (obstacle.getHeight()*slopeValue) - newStripEnd;
        toda = tora;
        asda = tora;
        handleNegative();
    }

  /**
   * Re-calculate values when taking-off away from an obstacle:
   * @param obstacle    the obstacle
   */
    public void redeclarationTakeOffAway(Obstacle obstacle){
        //(R) TORA = 3884 – 500 – 300 = 3084m
        //(R) TODA = 3962 – 500 – 300 = 3162m
        //(R) ASDA = 3884 – 500 – 300 = 3084m
        double way = Math.max(getClearWay(), getStopWay());
        tora = tora - obstacle.getDistanceFromThreshold() - engineBlastAllowance - displacedThreshold + way;
        toda = tora + getDefaultClearWay();
        asda = tora + getDefaultStopWay();
        handleNegative();
    }

  /**
   * Calculate runway's clear way
   * @return clear way
   */
    public double getClearWay() {
      return toda - tora;
    }

  /**
   * Calculate runway's stop way
   * @return stop way
   */
    public double getStopWay() {
      return asda - tora;
    }

  /**
   * Calculate runway's default clear way
   * @return default clear way
   */
  public double getDefaultClearWay() {
    return defaultToda - defaultTora;
  }

  /**
   * Calculate runway's default stop way
   * @return default stop way
   */
  public double getDefaultStopWay() {
    return defaultAsda - defaultTora;
  }

  /**
   * Calculate runway's ALS / TOCS
   * @return ALS / TOCS
   */
  public double getAlsTocs() {
    return (getCurrentObstacle().getHeight() * slopeValue) / Math.cos(slopeValue);
  }

  /**
   * Set all negative values to 0
   */
    public void handleNegative() {
      if (tora < 0) {
        tora = 0;
      }
      if (toda < 0) {
        toda = 0;
      }
      if (asda < 0) {
        asda = 0;
      }
      if (lda < 0) {
        lda = 0;
      }
    }

  /**
   * Setter for runway number
   * @param runwayNumber new runway number
   */
    public void setRunwayNumber(String runwayNumber) {
        this.runwayNumber = runwayNumber;
    }

  /**
   * Setter for TORA
   * @param tora new TORA
   */
    public void setTora(double tora) {
        this.tora = tora;
    }

  /**
   * Setter for TODA
   * @param toda new TODA
   */
    public void setToda(double toda) {
        this.toda = toda;
    }

  /**
   * Setter for ASDA
   * @param asda new ASDA
   */
    public void setAsda(double asda) {
        this.asda = asda;
    }

  /**
   * Setter for LDA
   * @param lda new LDA
   */
    public void setLda(double lda) {
        this.lda = lda;
    }

  /**
   * Setter for displaced threshold
   * @param displacedThreshold new displaced threshold
   */
    public void setDisplacedThreshold(double displacedThreshold) {
        this.displacedThreshold = displacedThreshold;
    }

  /**
   * Get the list of parameters that calculated into negative numbers (used for notifications)
   * @return  parameters that resulted in negative
   */
    public List<String> getNegativeValues() {
        List<String> negative = new ArrayList<>();
        if (tora == 0) {
          negative.add("TORA");
        }
        if (toda == 0) {
          negative.add("TODA");
        }
        if (asda == 0) {
          negative.add("ASDA");
        }
        if (lda == 0) {
          negative.add("LDA");
        }
        return negative;
    }

  /**
   * Get the text for Calculation Breakdown
   * @return calculation breakdown text
   */
    public Pair<String, String> getBreakdown(int method) {
      String redeclared;
      String breakdown;
      // Landing over:
      if (method == 1) {
        redeclared = "Re-declared value: LDA";
        breakdown = "LDA = LDA - obstacle distance from threshold - h * slope - new strip end - displaced threshold\n"
            + "LDA = " + df.format(defaultLda) + " - " + df.format(obstacle.getDistanceFromThreshold()) + " - " + df.format(obstacle.getHeight()) + " * " + (int) slopeValue + " - " + (int) newStripEnd + " - " + df.format(displacedThreshold) + "\n"
            + "LDA = " + df.format(lda) + "\n\n";
      }
      // Landing towards:
      else if (method == 2) {
        redeclared = "Re-declared value: LDA";
        breakdown = "LDA = obstacle distance from threshold - RESA - new strip end\n"
            + "LDA = " + df.format(obstacle.getDistanceFromThreshold()) + " - " + (int) resa + " - " + (int) newStripEnd + "\n"
            + "LDA = " + df.format(lda);
      }
      // Take-off towards:
      else if (method == 3) {
        redeclared = "Re-declared values: TORA, TODA, ASDA";
        breakdown = "TORA = obstacle distance from threshold + displaced threshold - h * slope - new strip end\n"
            + "TORA = " + df.format(obstacle.getDistanceFromThreshold()) + " + " + df.format(displacedThreshold) + " - " + df.format(obstacle.getHeight()) + " * " + (int) slopeValue + " - " + (int) newStripEnd + "\n"
            + "TORA = " + df.format(tora) + "\n\n";
        breakdown += "ASDA = TORA = TODA = " + df.format(tora);
      }
      // Take-off away:
      else {
        redeclared = "Re-declared values: TORA, TODA, ASDA";
        breakdown = "TORA = TORA - obstacle distance from threshold - displaced threshold - engine blast allowance + largest of (clear way, stop way)\n"
            + "TORA = " + df.format(defaultTora) + " - " + df.format(obstacle.getDistanceFromThreshold()) + " - " + df.format(displacedThreshold) + " - " + (int) engineBlastAllowance + " + " + df.format(Math.max(getDefaultClearWay(), getDefaultStopWay())) + "\n"
            + "TORA = " + df.format(tora) + "\n\n";
        breakdown += "TODA = TORA + clear way\n"
            + "TODA = " + df.format(tora) + " + " + df.format(getDefaultClearWay()) + "\n"
            + "TODA = " + df.format(toda) + "\n\n";;
        breakdown += "ASDA = TORA + stop way\n"
            + "ASDA = " + df.format(tora) + " + " + df.format(getDefaultStopWay()) + "\n"
            + "ASDA = " + df.format(asda) + "\n\n";;
      }
      return new Pair<>(redeclared, breakdown);
    }

  /**
   * Returns whether negative values were calculated
   * @return true if negative results
   */
    public boolean hasNegative(){
      return tora == 0 || toda== 0 || asda == 0 || lda == 0;
    }

}
