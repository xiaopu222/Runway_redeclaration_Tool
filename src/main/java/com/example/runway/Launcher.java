package com.example.runway;

/**
 * The Launcher class for running the program.
 */
public class Launcher {

  /**
   * Start the JavaFX program
   * @param args    command-line arguments
   */
  public static void main(String[] args) {
    try {
      App.main(args);
    }
    catch (Exception e) {
      System.out.println("Error");
      e.printStackTrace();
    }
  }

}
