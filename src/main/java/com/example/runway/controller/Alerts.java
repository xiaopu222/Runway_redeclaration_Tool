package com.example.runway.controller;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * The alerts class. Used for displaying errors and other types of pop-up messages.
 */
public class Alerts {

  // Alert for error messages:
  private final Alert error = new Alert(AlertType.ERROR);

  // Alert for calculation breakdown messages:
  private final Alert breakdown = new Alert(AlertType.INFORMATION);

  // Alert for confirmation messages:
  private final Alert confirmation = new Alert(AlertType.CONFIRMATION);

  /**
   * Create a new alerts class
   */
  public Alerts() {
    error.setTitle("ERROR");

    breakdown.setTitle("Calculation Breakdown");
    breakdown.setWidth(500);
    breakdown.setHeight(300);
    breakdown.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
  }

  /**
   * Display an error message
   * @param header     text for header of pop-up message
   * @param content    text for contents of pop-up message
   */
  public void alertError(String header, String content) {
    error.setHeaderText(header);
    error.setContentText(content);
    error.showAndWait();
  }

  /**
   * Display calculation breakdown
   * @param header     text for header of pop-up message
   * @param content    text for contents of pop-up message
   */
  public void alertBreakdown(String header, String content) {
    breakdown.setHeaderText(header);
    breakdown.setContentText(content);
    breakdown.showAndWait();
  }

  /**
   * Display user Confirmation pop up
   * @param header     text for header of pop-up message
   * @param content    text for contents of pop-up message
   */
  public boolean alertConfirmation(String header, String content) {
    confirmation.setHeaderText(header);
    confirmation.setContentText(content);
    Optional<ButtonType> result = confirmation.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
  }

}
