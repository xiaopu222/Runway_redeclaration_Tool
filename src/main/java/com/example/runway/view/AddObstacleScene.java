package com.example.runway.view;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.model.Obstacle;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The add obstacle scene. Asks the user to input obstacle data and saves the new obstacle.
 */
public class AddObstacleScene extends MainScene {

  private TextField txtName;
  private TextField txtHeight;
  private TextField txtDistanceCentre;
  private TextField txtDistanceThreshold;
  private TextField txtLength;

  /**
   * Create a new add obstacle scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public AddObstacleScene(MainPage mainPage, InputManager inputManager, FileManager fileManager) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;
  }

  /**
   * Draw the UI components
   */
  @Override
  public void draw() {
    // Set up the main pane
    root = new MainPane();
    root.getStylesheets().add(getClass().getResource(inputManager.getStyle()).toExternalForm());
    root.getStyleClass().add("menu-background");

    // Draw UI for user input:
    VBox boxInputs = new VBox();
    boxInputs.setAlignment(Pos.CENTER);
    boxInputs.setSpacing(40);

    Label lblHeading = new Label("Add a New Obstacle");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    VBox boxValues = new VBox();
    boxValues.setAlignment(Pos.CENTER_RIGHT);
    VBox.setMargin(boxValues, new Insets(0, 140, 0, 0));
    boxValues.setMaxWidth(400);
    boxValues.setSpacing(20);

    HBox boxName = new HBox();
    boxName.setSpacing(5);
    boxName.setAlignment(Pos.CENTER_RIGHT);

    Label lblName = new Label("Obstacle name:");
    lblName.getStyleClass().add("input-output-labels");
    txtName = new TextField();
    txtName.setMaxWidth(100);

    boxName.getChildren().addAll(lblName, txtName);

    HBox boxHeight = new HBox();
    boxHeight.setSpacing(5);
    boxHeight.setAlignment(Pos.CENTER_RIGHT);

    Label lblHeight = new Label("Height:");
    lblHeight.getStyleClass().add("input-output-labels");
    txtHeight = new TextField();
    txtHeight.setMaxWidth(100);

    boxHeight.getChildren().addAll(lblHeight, txtHeight);

    HBox boxLength = new HBox();
    boxLength.setSpacing(5);
    boxLength.setAlignment(Pos.CENTER_RIGHT);

    Label lblLength = new Label("Length:");
    lblLength.getStyleClass().add("input-output-labels");
    txtLength = new TextField();
    txtLength.setMaxWidth(100);

    boxLength.getChildren().addAll(lblLength, txtLength);

    HBox boxDistanceCentre = new HBox();
    boxDistanceCentre.setSpacing(5);
    boxDistanceCentre.setAlignment(Pos.CENTER_RIGHT);

    Label lblDistanceCentre = new Label("Distance from centre line:");
    lblDistanceCentre.getStyleClass().add("input-output-labels");
    txtDistanceCentre = new TextField();
    txtDistanceCentre.setMaxWidth(100);

    boxDistanceCentre.getChildren().addAll(lblDistanceCentre, txtDistanceCentre);

    HBox boxDistanceThreshold = new HBox();
    boxDistanceThreshold.setSpacing(5);
    boxDistanceThreshold.setAlignment(Pos.CENTER_RIGHT);

    Label lblDistanceThreshold = new Label("Distance from threshold:");
    lblDistanceThreshold.getStyleClass().add("input-output-labels");
    txtDistanceThreshold = new TextField();
    txtDistanceThreshold.setMaxWidth(100);

    boxDistanceThreshold.getChildren().addAll(lblDistanceThreshold, txtDistanceThreshold);

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


    boxValues.getChildren().addAll(boxName, boxHeight, boxLength, boxDistanceCentre, boxDistanceThreshold);
    boxButtons.getChildren().addAll(btnBack, btnNext);

    boxInputs.getChildren().addAll(boxValues, boxButtons);
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
   * Handle when the Next button is clicked (saves the new obstacle)
   * @param event    action event
   */
  private void next(ActionEvent event) {
    String obstacleName = txtName.getText();
    if (validations.isValidName(obstacleName)) {
      if (validations.isValidObstacleHeight(txtHeight.getText())) {
        if (validations.isValidObstacleHeight(txtLength.getText())) {
          if (validations.isValidObstacleDistance(txtDistanceCentre.getText(), true)) {
            if (validations.isValidObstacleDistance(txtDistanceThreshold.getText(), false) &&
                Double.parseDouble(txtDistanceThreshold.getText()) < inputManager.getCurrentRunway().getTora()) {
              if (!inputManager.getCurrentRunway().getObstacleNames().contains(obstacleName)) {
                // Create a new obstacle object:
                Obstacle obstacle = new Obstacle(obstacleName,
                    Double.parseDouble(txtHeight.getText()),
                    Double.parseDouble(txtLength.getText()),
                    Double.parseDouble(txtDistanceCentre.getText()),
                    Double.parseDouble(txtDistanceThreshold.getText()));
                inputManager.getCurrentRunway().addObstacle(obstacle);
                inputManager.getCurrentRunway().setCurrentObstacle(obstacle);

                // Display notification:
                Text txtNotification = new Text(
                    "A new obstacle \"" + obstacleName + "\" has been added to the \""
                        + inputManager.getCurrentRunway().getRunwayNumber() + "\" runway of the \""
                        + inputManager.getCurrentAirport().getName() + "\" airport.\n");
                txtNotification.getStyleClass().add("black-notification");
                inputManager.addNotification(txtNotification);

                // Return to main page:
                mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
              }
              // Appropriate error messages displayed in case of invalid user input:
              else {
                alerts.alertError("An obstacle with this name already exists",
                    "Please choose a different name.");
              }
            } else {
              alerts.alertError("No re-declaration possible",
                  "Distance from threshold has to be a positive number,\n "
                      + "which is greater than or equal to 60.");
            }
          } else {
            alerts.alertError("No re-declaration possible",
                "Distance from centre line has to be either 0,\n "
                    + "or a positive number which is less than or equal to 75.");
          }
        } else {
          alerts.alertError("Invalid obstacle length",
              "It has to be a number, which is greater than 0.");
        }
      }
      else {
          alerts.alertError("Invalid obstacle height",
              "It has to be a number, which is greater than 0.");
      }
    }
    else {
      alerts.alertError("Invalid obstacle name", "It has to consist of letters and numbers,\n where the first character is a letter.");
    }
  }

}