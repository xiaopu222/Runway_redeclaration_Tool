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
 * The modify scene. Allows the user to modify existing data.
 */
public class ModifyScene extends MainScene {

  private TextField txtName;
  private TextField txtTora;
  private TextField txtToda;
  private TextField txtAsda;
  private TextField txtLda;
  private TextField txtDisplaced;
  private TextField txtHeight;
  private TextField txtLength;
  private TextField txtDistanceCentre;
  private TextField txtDistanceThreshold;

  private VBox boxInputs;
  private final int num;

  /**
   * Create a new modify scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   * @param num which data to modify: airport=1, runway=2, obstacle=3
   */
  public ModifyScene(MainPage mainPage, InputManager inputManager, FileManager fileManager, int num) {
    super(mainPage);

    this.inputManager = inputManager;
    this.fileManager = fileManager;
    this.num = num;
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
    boxInputs = new VBox();
    boxInputs.setAlignment(Pos.CENTER);
    boxInputs.setSpacing(40);
    switch (num) {
      case 1 -> drawModifyAirport();
      case 2 -> drawModifyRunway();
      case 3 -> drawModifyObstacle();
    }

    // Draw buttons:
    HBox boxButtons = new HBox();
    boxButtons.setAlignment(Pos.CENTER);
    boxButtons.setSpacing(50);

    Button btnNext = new Button("Next");
    btnNext.getStyleClass().add("button-next");
    btnNext.setOnAction(this::next);

    Button btnBack = new Button("Back");
    btnBack.getStyleClass().add("button-next");
    btnBack.setOnAction(this::back);

    Button btnDelete = new Button("Delete");
    btnDelete.getStyleClass().add("button-delete");
    btnDelete.setOnAction(this::delete);

    boxButtons.getChildren().addAll(btnBack, btnNext, btnDelete);

    boxInputs.getChildren().add(boxButtons);
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
   * Draw UI for modifying an airport
   */
  public void drawModifyAirport() {
    // Page heading label:
    Label lblHeading = new Label("Modify Airport \"" + inputManager.getCurrentAirport().getName() + "\"");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    // Draw UI for user input:
    HBox boxName = new HBox();
    boxName.setSpacing(5);
    boxName.setAlignment(Pos.CENTER);

    Label lblName = new Label("Airport name:");
    lblName.getStyleClass().add("input-output-labels");
    txtName = new TextField(inputManager.getCurrentAirport().getName());
    txtName.setMaxWidth(100);

    boxName.getChildren().addAll(lblName, txtName);
    boxInputs.getChildren().add(boxName);
  }

  /**
   * Draw UI for modifying a runway
   */
  public void drawModifyRunway() {
    // Page heading label:
    Label lblHeading = new Label("Modify Runway \"" + inputManager.getCurrentRunway().getRunwayNumber() + "\"");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    // Draw UI for user input:
    VBox boxValues = new VBox();
    boxValues.setAlignment(Pos.CENTER_RIGHT);
    VBox.setMargin(boxValues, new Insets(0, 70, 0, 0));
    boxValues.setMaxWidth(320);
    boxValues.setSpacing(20);

    HBox boxName = new HBox();
    boxName.setSpacing(5);
    boxName.setAlignment(Pos.CENTER_RIGHT);
    Label lblName = new Label("Runway number:");
    lblName.getStyleClass().add("input-output-labels");
    txtName = new TextField(inputManager.getCurrentRunway().getRunwayNumber());
    txtName.setMaxWidth(100);
    boxName.getChildren().addAll(lblName, txtName);

    HBox boxTora = new HBox();
    boxTora.setSpacing(5);
    boxTora.setAlignment(Pos.CENTER_RIGHT);
    Label lblTora = new Label("TORA:");
    lblTora.getStyleClass().add("input-output-labels");
    txtTora = new TextField(String.valueOf(inputManager.getCurrentRunway().getDefaultTora()));
    txtTora.setMaxWidth(100);
    boxTora.getChildren().addAll(lblTora, txtTora);


    HBox boxToda = new HBox();
    boxToda.setSpacing(5);
    boxToda.setAlignment(Pos.CENTER_RIGHT);
    Label lblToda = new Label("TODA:");
    lblToda.getStyleClass().add("input-output-labels");
    txtToda = new TextField(String.valueOf(inputManager.getCurrentRunway().getDefaultToda()));
    txtToda.setMaxWidth(100);
    boxToda.getChildren().addAll(lblToda, txtToda);


    HBox boxAsda = new HBox();
    boxAsda.setSpacing(5);
    boxAsda.setAlignment(Pos.CENTER_RIGHT);
    Label lblAsda = new Label("ASDA:");
    lblAsda.getStyleClass().add("input-output-labels");
    txtAsda = new TextField(String.valueOf(inputManager.getCurrentRunway().getDefaultAsda()));
    txtAsda.setMaxWidth(100);
    boxAsda.getChildren().addAll(lblAsda, txtAsda);


    HBox boxLda = new HBox();
    boxLda.setSpacing(5);
    boxLda.setAlignment(Pos.CENTER_RIGHT);
    Label lblLda = new Label("LDA:");
    lblLda.getStyleClass().add("input-output-labels");
    txtLda = new TextField(String.valueOf(inputManager.getCurrentRunway().getDefaultLda()));
    txtLda.setMaxWidth(100);
    boxLda.getChildren().addAll(lblLda, txtLda);

    HBox boxDisplaced = new HBox();
    boxDisplaced.setSpacing(5);
    boxDisplaced.setAlignment(Pos.CENTER_RIGHT);
    Label lblDisplaced = new Label("Displaced threshold:");
    lblDisplaced.getStyleClass().add("input-output-labels");
    txtDisplaced = new TextField(String.valueOf(inputManager.getCurrentRunway().getDisplacedThreshold()));
    txtDisplaced.setMaxWidth(100);
    boxDisplaced.getChildren().addAll(lblDisplaced, txtDisplaced);


    boxValues.getChildren().addAll(boxName, boxTora, boxToda, boxAsda, boxLda, boxDisplaced);
    boxInputs.getChildren().add(boxValues);
  }

  /**
   * Draw UI for modifying an obstacle
   */
  public void drawModifyObstacle() {
    // Page heading label:
    Label lblHeading = new Label("Modify Obstacle \"" + inputManager.getCurrentRunway().getCurrentObstacle().getName() + "\"");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    // Draw UI for user input:
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
    txtName = new TextField(inputManager.getCurrentRunway().getCurrentObstacle().getName());
    txtName.setMaxWidth(100);
    boxName.getChildren().addAll(lblName, txtName);

    HBox boxHeight = new HBox();
    boxHeight.setSpacing(5);
    boxHeight.setAlignment(Pos.CENTER_RIGHT);

    Label lblHeight = new Label("Height:");
    lblHeight.getStyleClass().add("input-output-labels");
    txtHeight = new TextField(String.valueOf(inputManager.getCurrentRunway().getCurrentObstacle().getHeight()));
    txtHeight.setMaxWidth(100);

    boxHeight.getChildren().addAll(lblHeight, txtHeight);

    HBox boxLength = new HBox();
    boxLength.setSpacing(5);
    boxLength.setAlignment(Pos.CENTER_RIGHT);

    Label lblLength = new Label("Length:");
    lblLength.getStyleClass().add("input-output-labels");
    txtLength = new TextField(String.valueOf(inputManager.getCurrentRunway().getCurrentObstacle().getLength()));
    txtLength.setMaxWidth(100);

    boxLength.getChildren().addAll(lblLength, txtLength);

    HBox boxDistanceCentre = new HBox();
    boxDistanceCentre.setSpacing(5);
    boxDistanceCentre.setAlignment(Pos.CENTER_RIGHT);
    Label lblDistanceCentre = new Label("Distance from centre line:");
    lblDistanceCentre.getStyleClass().add("input-output-labels");
    txtDistanceCentre = new TextField(String.valueOf(inputManager.getCurrentRunway().getCurrentObstacle().getDistanceCentre()));
    txtDistanceCentre.setMaxWidth(100);
    boxDistanceCentre.getChildren().addAll(lblDistanceCentre, txtDistanceCentre);

    HBox boxDistanceThreshold = new HBox();
    boxDistanceThreshold.setSpacing(5);
    boxDistanceThreshold.setAlignment(Pos.CENTER_RIGHT);
    Label lblDistanceThreshold = new Label("Distance from threshold:");
    lblDistanceThreshold.getStyleClass().add("input-output-labels");
    txtDistanceThreshold = new TextField(String.valueOf(inputManager.getCurrentRunway().getCurrentObstacle().getDistanceFromThreshold()));
    txtDistanceThreshold.setMaxWidth(100);
    boxDistanceThreshold.getChildren().addAll(lblDistanceThreshold, txtDistanceThreshold);

    boxValues.getChildren().addAll(boxName, boxHeight, boxLength, boxDistanceCentre, boxDistanceThreshold);
    boxInputs.getChildren().add(boxValues);
  }

  /**
   * Handle when the Next button is clicked (saves changes)
   * @param event    action event
   */
  private void next(ActionEvent event) {
    if (num == 1) {
      modifyAirport();
    }
    else if (num == 2) {
      modifyRunway();
    }
    else {
      modifyObstacle();
    }
  }

  /**
   * Handle when the Delete button is clicked (deletes data)
   * @param event    action event
   */
  private void delete(ActionEvent event) {
    if (num == 1) {
      deleteAirport();
    }
    else if (num == 2) {
      deleteRunway();
    }
    else {
      deleteObstacle();
    }
  }

  /**
   * Save changes made to airport
   */
  public void modifyAirport() {
    String airportName = txtName.getText();
    if (!airportName.equals(inputManager.getCurrentAirport().getName())) {
      if (validations.isValidName(airportName)) {
        if (!inputManager.getAirportNames().contains(airportName)) {

          inputManager.setModified(true);

          // Modify the airport
          fileManager.deleteAirport(inputManager.getCurrentAirport().getName());
          inputManager.getCurrentAirport().setName(airportName);

          // Display notification
          Text txtNotification = new Text(
              "Airport name successfully changed to \"" + airportName + "\".\n");
          txtNotification.getStyleClass().add("black-notification");
          inputManager.addNotification(txtNotification);

          // Return to main page:
          mainPage.showRunway(inputManager, fileManager);
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
    else {
      mainPage.showRunway(inputManager, fileManager);
    }
  }

  /**
   * Save changes made to runway
   */
  public void modifyRunway() {
    String runwayNumber = txtName.getText();
    String tora = txtTora.getText();
    String toda = txtToda.getText();
    String asda = txtAsda.getText();
    String lda = txtLda.getText();
    String displaced = txtDisplaced.getText();
    boolean runwayModified = !runwayNumber.equals(inputManager.getCurrentRunway().getRunwayNumber());
    if (!(runwayNumber.equals(inputManager.getCurrentRunway().getRunwayNumber()) &&
        tora.equals(String.valueOf(inputManager.getCurrentRunway().getDefaultTora())) &&
        toda.equals(String.valueOf(inputManager.getCurrentRunway().getDefaultToda())) &&
        asda.equals(String.valueOf(inputManager.getCurrentRunway().getDefaultAsda())) &&
        lda.equals(String.valueOf(inputManager.getCurrentRunway().getDefaultLda())) &&
        displaced.equals(String.valueOf(inputManager.getCurrentRunway().getDisplacedThreshold())))) {
      if (validations.isValidRunwayNumber(runwayNumber)) {
          if (validations.isValidRunwayTora(txtTora.getText())) {
            double toraN = Double.parseDouble(txtTora.getText());
            if (validations.isValidRunwayAsda(txtAsda.getText(), toraN)) {
              double asdaN = Double.parseDouble(txtAsda.getText());
              if (validations.isValidRunwayToda(txtToda.getText(), asdaN)) {
                if (validations.isValidRunwayLda(txtLda.getText(), toraN)) {
                  if (validations.isValidRunwayDisplaced(displaced)) {
                    if (runwayModified && runwayExists(runwayNumber)) {
                      alerts.alertError("A runway with this number already exists",
                          "Please choose a different number.");
                    } else {
                      inputManager.setModified(true);

                      // Modify the runway:
                      inputManager.getCurrentRunway().setRunwayNumber(runwayNumber);
                      inputManager.getCurrentRunway().setDefaultTora(Double.parseDouble(tora));
                      inputManager.getCurrentRunway().setDefaultToda(Double.parseDouble(toda));
                      inputManager.getCurrentRunway().setDefaultAsda(Double.parseDouble(asda));
                      inputManager.getCurrentRunway().setDefaultLda(Double.parseDouble(lda));
                      inputManager.getCurrentRunway().setDisplacedThreshold(Double.parseDouble(displaced));

                      // Display notification:
                      Text txtNotification = new Text(
                          "Runway \"" + runwayNumber + "\" successfully modified.\n");
                      txtNotification.getStyleClass().add("black-notification");
                      inputManager.addNotification(txtNotification);
                      inputManager.getCurrentAirport().setDefaultRunways();

                      // Return to main page:
                      mainPage.showRunway(inputManager, fileManager,
                          inputManager.getCurrentRunway());
                    }
                  }
                  // Appropriate error messages displayed in case of invalid user input:
                  else {
                    alerts.alertError("Invalid Displaced threshold", "The value has to be a number, which is greater than or equal to 0.");
                  }
                }
                else {
                  alerts.alertError("Invalid LDA", "The value has to be a number, which is greater than 0\n and less than or equal to TORA.");
                }
              }
              else {
                alerts.alertError("Invalid TODA", "The value has to be a number, which is greater than 0\n and greater than or equal to ASDA.");
              }
            }
            else {
              alerts.alertError("Invalid ASDA", "The value has to be a number, which is greater than 0\n and greater than or equal to TORA.");
            }
          }
          else {
            alerts.alertError("Invalid TORA",
                "The value has to be a number, which is greater than 0.");
          }
      }
      else {
        alerts.alertError("Invalid runway number", "Please enter one in the correct format (example: \"10R\")");
      }
    }
    else {
      mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
    }
  }

  /**
   * Check whether a given runway number already exists in the current airport:
   * @param runwayNumber    runway number
   * @return true if exists
   */
  public boolean runwayExists(String runwayNumber) {
    try {
      int num = Integer.parseInt(runwayNumber);
      String number = String.valueOf(num);
      for (String runwayNum : inputManager.getCurrentAirport().getRunwayNumbers()) {
        if (runwayNum.substring(0, runwayNum.length() - 1).equals(number) || runwayNum.equals(number)) {
          return true;
        }
      }
    }
    catch (Exception e) {
      for (String runwayNum : inputManager.getCurrentAirport().getRunwayNumbers()) {
        if (runwayNum.substring(0, runwayNum.length() - 1).equals(runwayNumber.substring(0, runwayNumber.length() - 1)) &&
            runwayNum.charAt(runwayNum.length() - 1) == runwayNumber.charAt(runwayNumber.length() - 1)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Save changes made to obstacle
   */
  public void modifyObstacle() {
    Obstacle current = inputManager.getCurrentRunway().getCurrentObstacle();
    String name = txtName.getText();
    String height = txtHeight.getText();
    String length = txtLength.getText();
    String distanceCentre = txtDistanceCentre.getText();
    String distanceThreshold = txtDistanceThreshold.getText();
    boolean nameModified = !name.equals(current.getName());
    if (!(name.equals(current.getName()) &&
        height.equals(String.valueOf(current.getHeight())) &&
        distanceCentre.equals(String.valueOf(current.getDistanceCentre())) &&
        distanceThreshold.equals(String.valueOf(current.getDistanceFromThreshold())))) {
      if (validations.isValidName(name)) {
        if (validations.isValidObstacleHeight(height)) {
          if (validations.isValidObstacleHeight(length)) {
            if (validations.isValidObstacleDistance(distanceCentre, true)) {
              if (validations.isValidObstacleDistance(distanceThreshold, false) &&
              Double.parseDouble(distanceThreshold) < inputManager.getCurrentRunway().getTora()) {
                if (nameModified && obstacleExists(name)) {
                  alerts.alertError("An obstacle with this name already exists",
                      "Please choose a different name.");
                } else {
                  inputManager.setModified(true);

                  // Modify the obstacle:
                  current.setName(name);
                  current.setHeight(Double.parseDouble(height));
                  current.setDistanceCentre(Double.parseDouble(distanceCentre));
                  current.setDistanceThreshold(Double.parseDouble(distanceThreshold));

                  // Display notification:
                  Text txtNotification = new Text(
                      "Obstacle \"" + name + "\" successfully modified.\n");
                  txtNotification.getStyleClass().add("black-notification");
                  inputManager.addNotification(txtNotification);

                  // Reurn to main page:
                  inputManager.getCurrentAirport().setDefaultRunways();
                  mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
                }
              }
              // Appropriate error messages displayed in case of invalid user input:
              else {
                alerts.alertError("No re-declaration possible",
                    "Distance from threshold has to be a positive number,\n "
                        + "which is greater than or equal to 60.");
              }
            } else {
              alerts.alertError("No re-declaration possible",
                  "Distance from centre line has to be either 0,\n "
                      + "or a positive number which is less than or equal to 75.");
            }
          }
          else {
            alerts.alertError("Invalid length",
                "The value has to be a number, which is greater than 0.");
          }
        }
        else {
          alerts.alertError("Invalid height",
              "The value has to be a number, which is greater than 0.");
        }
      }
      else {
        alerts.alertError("Invalid name", "Obstacle name can consist of letters and numbers,\n and start with a letter.");
      }
    }
    else {
      mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
    }
  }

  /**
   * Check whether a given obstacle name already exists in the current airport:
   * @param name    obstacle name
   * @return true if exists
   */
  public boolean obstacleExists(String name) {
    for (String n : inputManager.getCurrentRunway().getObstacleNames()) {
      if (n.equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Delete the chosen airport
   */
  public void deleteAirport() {
    // Display the "Are you sure?" alert:
    if (alerts.alertConfirmation("Are you sure that you want to delete the airport?", "You will not be able to restore the deleted details.")) {
      String name = inputManager.getCurrentAirport().getName();
      inputManager.setModified(true);

      // Delete the airport:
      fileManager.deleteAirport(name);
      inputManager.getAirports().remove(inputManager.getCurrentAirport());

      // Display notification:
      Text txtNotification = new Text(
          "Airport \"" + name + "\" successfully deleted.\n");
      txtNotification.getStyleClass().add("black-notification");
      inputManager.addNotification(txtNotification);

      // Return to main page:
      mainPage.showRunway(inputManager, fileManager);
    }
  }

  /**
   * Delete the chosen runway
   */
  public void deleteRunway() {
    // Display the "Are you sure?" alert:
    if (alerts.alertConfirmation("Are you sure that you want to delete the runway?", "You will not be able to restore the deleted details.")) {
      String number = inputManager.getCurrentRunway().getRunwayNumber();
      inputManager.setModified(true);

      // Delete the runway:
      fileManager.deleteAirport(inputManager.getCurrentAirport().getName());
      inputManager.getCurrentAirport().getRunways().remove(inputManager.getCurrentRunway());

      // Display notification:
      Text txtNotification = new Text(
          "Runway \"" + number + "\" successfully deleted.\n");
      txtNotification.getStyleClass().add("black-notification");
      inputManager.addNotification(txtNotification);

      // Return to main page:
      mainPage.showRunway(inputManager, fileManager);
    }
  }

  /**
   * Delete the chosen obstacle
   */
  public void deleteObstacle() {
    // Display the "Are you sure?" alert:
    if (alerts.alertConfirmation("Are you sure that you want to delete the obstacle?", "You will not be able to restore the deleted details.")) {
      String name = inputManager.getCurrentRunway().getCurrentObstacle().getName();
      inputManager.setModified(true);

      // Delete the obstacle:
      fileManager.deleteAirport(inputManager.getCurrentAirport().getName());
      inputManager.getCurrentRunway().getObstacles().remove(inputManager.getCurrentRunway().getCurrentObstacle());

      // Display notification:
      Text txtNotification = new Text(
          "Obstacle \"" + name + "\" successfully deleted.\n");
      txtNotification.getStyleClass().add("black-notification");
      inputManager.addNotification(txtNotification);

      // Return to main page:
      mainPage.showRunway(inputManager, fileManager, inputManager.getCurrentRunway());
    }
  }

}