package com.example.runway.view;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.model.Airport;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The add airport scene. Asks the user to input airport data and saves the new airport.
 */
public class AddAirportScene extends MainScene {

  private TextField txtName;

  /**
   * Create a new add airport scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public AddAirportScene(MainPage mainPage, InputManager inputManager, FileManager fileManager) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;
  }

  /**
   * Draw the UI components
   */
  @Override
  public void draw() {
    // Set up the main pane:
    root = new MainPane();
    root.getStylesheets().add(getClass().getResource(inputManager.getStyle()).toExternalForm());
    root.getStyleClass().add("menu-background");

    // Draw UI for user input:
    VBox boxInputs = new VBox();
    boxInputs.setAlignment(Pos.CENTER);
    boxInputs.setSpacing(40);

    Label lblHeading = new Label("Add a New Airport");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    HBox boxName = new HBox();
    boxName.setSpacing(5);
    boxName.setAlignment(Pos.CENTER);

    Label lblName = new Label("Airport name:");
    lblName.getStyleClass().add("input-output-labels");
    txtName = new TextField();
    txtName.setMaxWidth(100);

    boxName.getChildren().addAll(lblName, txtName);

    // Draw buttons:
    HBox boxButtons = new HBox();
    boxButtons.setAlignment(Pos.CENTER);
    boxButtons.setSpacing(70);

    Button btnNext = new Button("Next");
    btnNext.getStyleClass().add("button-next");
    btnNext.setOnAction(this::next);

    Button btnBack = new Button("Back");
    btnBack.getStyleClass().add("button-next");
    btnBack.setOnAction(this::back);

    boxButtons.getChildren().addAll(btnBack, btnNext);

    boxInputs.getChildren().addAll(boxName, boxButtons);
    root.getChildren().add(boxInputs);
  }

  /**
   * Handle when the Back button is clicked (loads previous scene)
   * @param event    action event
   */
  private void back(ActionEvent event) {
    mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
  }

  /**
   * Handle when the Next button is clicked (saves the new airport)
   * @param event    action event
   */
  private void next(ActionEvent event) {
    String airportName = txtName.getText();
    if (validations.isValidName(airportName)) {
      if (!inputManager.getAirportNames().contains(airportName)) {
        // Create new airport object:
        Airport airport = new Airport(airportName);
        inputManager.addAirport(airport);
        inputManager.setCurrentAirport(airport);

        // Display notification:
        Text txtNotification = new Text(
            "A new airport \"" + airportName + "\" has been added to the system.\n");
        txtNotification.getStyleClass().add("black-notification");
        inputManager.addNotification(txtNotification);

        // Return to main page:
        mainPage.showAddRunway(inputManager, fileManager);
      }
      // Appropriate error messages displayed in case of invalid user input:
      else {
        alerts.alertError("An airport with this name already exists", "Please choose a different name.");
      }
    }
    else {
      alerts.alertError("Invalid airport name", "It has to consist of letters and numbers,\n where the first character is a letter");
    }
  }

}
