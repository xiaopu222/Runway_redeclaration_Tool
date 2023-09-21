package com.example.runway.view;

import com.example.runway.controller.FileManager;
import com.example.runway.controller.InputManager;
import com.example.runway.model.Runway;
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
 * The add runway scene. Asks the user to input runway data and saves the new runway.
 */
public class AddRunwayScene extends MainScene {

  private TextField txtRunwayNumber;
  private TextField txtTora;
  private TextField txtToda;
  private TextField txtAsda;
  private TextField txtLda;
  private TextField txtDisplaced;

  /**
   * Create a new add runway scene
   * @param mainPage     the main page parameters of the UI
   * @param inputManager controller for all user inputs
   * @param fileManager controller for all files
   */
  public AddRunwayScene(MainPage mainPage, InputManager inputManager, FileManager fileManager) {
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

    Label lblHeading = new Label("Add a New Runway");
    lblHeading.getStyleClass().add("heading");
    boxInputs.getChildren().add(lblHeading);

    VBox boxValues = new VBox();
    boxValues.setAlignment(Pos.CENTER_RIGHT);
    VBox.setMargin(boxValues, new Insets(0, 70, 0, 0));
    boxValues.setMaxWidth(320);
    boxValues.setSpacing(20);

    HBox boxRunwayNumber = new HBox();
    boxRunwayNumber.setSpacing(5);
    boxRunwayNumber.setAlignment(Pos.CENTER_RIGHT);
    Label lblRunwayNumber = new Label("Runway number:");
    lblRunwayNumber.getStyleClass().add("input-output-labels");
    txtRunwayNumber = new TextField();
    txtRunwayNumber.setMaxWidth(100);
    boxRunwayNumber.getChildren().addAll(lblRunwayNumber, txtRunwayNumber);


    HBox boxTora = new HBox();
    boxTora.setSpacing(5);
    boxTora.setAlignment(Pos.CENTER_RIGHT);
    Label lblTora = new Label("TORA:");
    lblTora.getStyleClass().add("input-output-labels");
    txtTora = new TextField();
    txtTora.setMaxWidth(100);
    boxTora.getChildren().addAll(lblTora, txtTora);


    HBox boxToda = new HBox();
    boxToda.setSpacing(5);
    boxToda.setAlignment(Pos.CENTER_RIGHT);
    Label lblToda = new Label("TODA:");
    lblToda.getStyleClass().add("input-output-labels");
    txtToda = new TextField();
    txtToda.setMaxWidth(100);
    boxToda.getChildren().addAll(lblToda, txtToda);


    HBox boxAsda = new HBox();
    boxAsda.setSpacing(5);
    boxAsda.setAlignment(Pos.CENTER_RIGHT);
    Label lblAsda = new Label("ASDA:");
    lblAsda.getStyleClass().add("input-output-labels");
    txtAsda = new TextField();
    txtAsda.setMaxWidth(100);
    boxAsda.getChildren().addAll(lblAsda, txtAsda);


    HBox boxLda = new HBox();
    boxLda.setSpacing(5);
    boxLda.setAlignment(Pos.CENTER_RIGHT);
    Label lblLda = new Label("LDA:");
    lblLda.getStyleClass().add("input-output-labels");
    txtLda = new TextField();
    txtLda.setMaxWidth(100);
    boxLda.getChildren().addAll(lblLda, txtLda);


    HBox boxDisplaced = new HBox();
    boxDisplaced.setSpacing(5);
    boxDisplaced.setAlignment(Pos.CENTER_RIGHT);
    Label lblDisplaced = new Label("Displaced threshold:");
    lblDisplaced.getStyleClass().add("input-output-labels");
    txtDisplaced = new TextField();
    txtDisplaced.setMaxWidth(100);
    boxDisplaced.getChildren().addAll(lblDisplaced, txtDisplaced);

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


    boxValues.getChildren().addAll(boxRunwayNumber, boxTora, boxToda, boxAsda, boxLda, boxDisplaced);
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
   * Handle when the Next button is clicked (saves the new runway)
   * @param event    action event
   */
  private void next(ActionEvent event) {
    String runwayNumber = txtRunwayNumber.getText().toUpperCase();
    if (validations.isValidRunwayNumber(runwayNumber)) {
      if (validations.isValidRunwayTora(txtTora.getText())) {
        double tora = Double.parseDouble(txtTora.getText());
        if (validations.isValidRunwayAsda(txtAsda.getText(), tora)) {
          double asda = Double.parseDouble(txtAsda.getText());
          if (validations.isValidRunwayToda(txtToda.getText(), asda)) {
              if (validations.isValidRunwayLda(txtLda.getText(), tora)) {
                if (validations.isValidRunwayDisplaced(txtDisplaced.getText())) {
                  if (!runwayExists(runwayNumber)) {
                    // Create a new runway object:
                    Runway runway = new Runway(runwayNumber, tora,
                        Double.parseDouble(txtToda.getText()),
                        Double.parseDouble(txtAsda.getText()),
                        Double.parseDouble(txtLda.getText()),
                        Double.parseDouble(txtDisplaced.getText()));
                    inputManager.setCurrentRunway(runway);
                    inputManager.getCurrentAirport().addRunway(runway);

                    // Display notification:
                    Text txtNotification = new Text(
                        "A new runway \"" + runwayNumber + "\" has been added to the \""
                            + inputManager.getCurrentAirport().getName() + "\" airport.\n");
                    txtNotification.getStyleClass().add("black-notification");
                    inputManager.addNotification(txtNotification);

                    // Return to main page:
                    mainPage.showRunway(inputManager, fileManager, runway);
                  }
                  // Appropriate error messages displayed in case of invalid user input:
                  else {
                    alerts.alertError("A runway with this number already exists",
                        "Please choose a different number.");
                  }
                } else {
                  alerts.alertError("Invalid Displaced threshold",
                      "The value has to be a number, which is greater than or equal to 0.");
                }
              } else {
                alerts.alertError("Invalid LDA",
                    "The value has to be a number, which is greater than 0\n and less than or equal to TORA.");
              }
            }
          else {
            alerts.alertError("Invalid TODA",
                "The value has to be a number, which is greater than 0\n and greater than or equal to ASDA.");
          }
        }
        else {
          alerts.alertError("Invalid ASDA",
              "The value has to be a number, which is greater than 0\n and greater than or equal to TORA.");
        }
      }
      else {
        alerts.alertError("Invalid TORA", "The value has to be a number, which is greater than 0.");
      }
    }
    else {
      alerts.alertError("Invalid runway number", "Please enter one in the correct format (example: \"10R\")");
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

}
